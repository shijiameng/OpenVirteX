package net.onrc.openvirtex.elements.datapath.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openflow.protocol.OFMarker;
import org.openflow.protocol.OFMessage;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.OFMarkerPenaltySetVendorData;
import org.openflow.vendor.enslab.OFMarkerReplyVendorData;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.OFSrtcmPenalty;
import org.openflow.vendor.enslab.OFSrtcmStats;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.marker.Marker;
import net.onrc.openvirtex.elements.marker.SrtcMarker;
import net.onrc.openvirtex.messages.OVXVendor;

public class Scheduler implements OVXSendMsg {
	
	private static final double ALPHA = 0.4;
	
	private PhysicalSwitch sw;
	
	private enum BucketType { C_BUCKET, E_BUCKET; }
	
	Logger log = LogManager.getLogger(Scheduler.class.getName());
	
	public Scheduler(PhysicalSwitch sw) {
		this.sw = sw;
	}
	
	private long getGlobalBucketTotalLend(final BucketType type) {
		SrtcMarker globalMarker = (SrtcMarker) sw.getMarker(OFMarker.OFPM_GLOBAL.getValue());
		return getMarkerTotalBorrowed(globalMarker, type);
	}
	
	private long getMarkerTotalBorrowed(final SrtcMarker marker, final BucketType type) {
		OFMarkerReplyVendorData stats = sw.getMarkerStatistics(marker.getMarkerId());
		long totalBorrowed = 0;
		
		if (stats.getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
			OFSrtcmStats srtcmStats = (OFSrtcmStats) stats.getReply();
			if (type == BucketType.C_BUCKET) 
				totalBorrowed = srtcmStats.getNumberOfCBorrowed();
			else
				totalBorrowed = srtcmStats.getNumberOfEBorrowed();
		}
		
		return totalBorrowed;
	}
	
	private double getGlobalTokenUsage(final SrtcMarker marker, final BucketType type) {
		long totalLend = getGlobalBucketTotalLend(type);
		long totalBorrowed = getMarkerTotalBorrowed(marker, type);
		
		return totalLend > 0 ? (double) totalBorrowed / (double) totalLend : 0; 
	}
	
	private double getGlobalIdleIndicator(final BucketType type) {
		SrtcMarker globalMarker = (SrtcMarker) sw.getMarker(OFMarker.OFPM_GLOBAL.getValue());
		OFMarkerReplyVendorData stats = sw.getMarkerStatistics(OFMarker.OFPM_GLOBAL.getValue());
		OFSrtcmStats srtcmStats = (OFSrtcmStats) stats.getReply();
		double idleIndicator = 0;
		
		
		if (type == BucketType.C_BUCKET) {
			if (globalMarker.getCommittedBurstSize() > 0)
				idleIndicator = (double) srtcmStats.getNumberOfCTokens() / (double) globalMarker.getCommittedBurstSize();
		} else {
			if (globalMarker.getExceedBurstSize() > 0)
				idleIndicator = (double) srtcmStats.getNumberOfETokens() / (double) globalMarker.getExceedBurstSize();
		}
		
		return idleIndicator;
	}
	
	public double calcPenalty(final SrtcMarker marker, final BucketType type) {
		final int currentBW = marker.getCurrentBandwidth();
		final double currentPenalty;
		double newPenalty;
		
		if (type == BucketType.C_BUCKET) 
			currentPenalty = marker.getCTokenPenalty();
		else
			currentPenalty = marker.getETokenPenalty();
		
		if (currentBW > marker.getCommittedInfoRate()) {
			double globalUsage = this.getGlobalTokenUsage(marker, type);
			if (this.getGlobalIdleIndicator(type) < ALPHA) {
				if (currentPenalty > 0) {
					newPenalty = Math.min(currentPenalty + (1 + globalUsage) * (1 - marker.getWeight()) * currentPenalty, 1);
				} else {
					newPenalty = marker.getTypeOfService().initialPenalty();
				}
			} else {
				newPenalty = Math.max(currentPenalty - (2 - globalUsage) * marker.getWeight() * currentPenalty, 0);
			}
		} else {
			newPenalty = Math.max(currentPenalty - marker.getWeight() * currentPenalty, 0);
		}
		
		return newPenalty;
	}
	
	public void sendMarkerScheduler(final SrtcMarker marker) {
		OVXVendor msg = new OVXVendor();
		OFMarkerPenaltySetVendorData vendorData = new OFMarkerPenaltySetVendorData();
		OFSrtcmPenalty markerData = new OFSrtcmPenalty();
		
		markerData.setCBucketPenalty((int) Math.ceil(marker.getCTokenPenalty() * 100));
		markerData.setEBucketPenalty((int) Math.ceil(marker.getETokenPenalty() * 100));
		
		vendorData.setMarkerId(marker.getMarkerId());
		vendorData.setMarkerType(OFMarkerType.ENSLAB_MARKER_SRTC);
		vendorData.setMarkerData(markerData);
		
		msg.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
		msg.setVendorData(vendorData);
		msg.setLengthU(OVXVendor.MINIMUM_LENGTH + vendorData.getLength());
		
		this.sendMsg(msg, this);
	}
	
	public void scheduleNext() {
		Marker[] marker = sw.getAllMarkers();
		
		for (int i = 0; i < marker.length; i++) {
			if (marker[i].getMarkerId() == OFMarker.OFPM_GLOBAL.getValue())
				continue;
			if (marker[i].getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
				SrtcMarker srtcm = (SrtcMarker) marker[i];
				double cPenalty = this.calcPenalty(srtcm, BucketType.C_BUCKET);
				double ePenalty = this.calcPenalty(srtcm, BucketType.E_BUCKET);
				srtcm.setCTokenPenalty(cPenalty);
				srtcm.setETokenPenalty(ePenalty);
				this.sendMarkerScheduler(srtcm);
			}
		}
	}

	@Override
	public void sendMsg(OFMessage msg, OVXSendMsg from) {
		// TODO Auto-generated method stub
		this.sw.sendMsg(msg, from);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
