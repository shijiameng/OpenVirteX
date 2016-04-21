package net.onrc.openvirtex.elements.datapath.scheduler;

import java.math.BigInteger;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openflow.protocol.OFMarker;
import org.openflow.protocol.OFMessage;
import org.openflow.util.U32;
import org.openflow.util.U64;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.OFMarkerConfigVendorData;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.statistics.OFMarkerStatisticsReply;
import org.openflow.vendor.enslab.statistics.OFSrtcmFeatures;
import org.openflow.vendor.enslab.statistics.OFSrtcmStatistics;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.marker.Marker;
import net.onrc.openvirtex.elements.marker.SrtcMarker;
import net.onrc.openvirtex.messages.OVXVendor;

public class MarkerScheduler implements OVXSendMsg {
	
	private static final double ALPHA = 0.4;
	
	private PhysicalSwitch sw;
	
	private enum BucketType { C_BUCKET, E_BUCKET; }
	
	Logger log = LogManager.getLogger(MarkerScheduler.class.getName());
	
	public MarkerScheduler(PhysicalSwitch sw) {
		this.sw = sw;
	}
	
	private long getGlobalBucketTotalLend(final BucketType type) {
		SrtcMarker globalMarker = (SrtcMarker) sw.getMarker(OFMarker.OFPM_GLOBAL.getValue());
		if (globalMarker == null) {
			log.fatal("globalMarker is NULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return 0;
		}
		return getMarkerTotalBorrowed(globalMarker, type);
	}
	
	private long getMarkerTotalBorrowed(final SrtcMarker marker, final BucketType type) {
		OFMarkerStatisticsReply stats = sw.getMarkerStatistics(marker.getMarkerId());
		long totalBorrowed = 0;
		
		if (stats.getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
			OFSrtcmStatistics srtcmStats = (OFSrtcmStatistics) stats.getMarkerData();
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
		
		// SJM TEST
		log.info("Total Lend={}, Total Borrowed={}", totalLend, totalBorrowed);
		// SJM TEST END
		
		return totalLend > 0 ? (double) totalBorrowed / (double) totalLend : 0; 
	}
	
	private double getGlobalIdleIndicator(final BucketType type) {
		SrtcMarker globalMarker = (SrtcMarker) sw.getMarker(OFMarker.OFPM_GLOBAL.getValue());
		OFMarkerStatisticsReply stats = sw.getMarkerStatistics(OFMarker.OFPM_GLOBAL.getValue());
		OFSrtcmStatistics srtcmStats = (OFSrtcmStatistics) stats.getMarkerData();
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
		final int currentDataRate = marker.getMeteredDataRate();
		final double currentPenalty;
		double newPenalty;
		
		if (type == BucketType.C_BUCKET) 
			currentPenalty = marker.getCTokenPenalty();
		else
			currentPenalty = marker.getETokenPenalty();
		
		if (currentDataRate > marker.getCommittedInfoRate()) {
			double globalUsage = this.getGlobalTokenUsage(marker, type);
			// SJM TEST
			log.info("globalUsage={}, globalIdleIndicator={}", globalUsage, this.getGlobalIdleIndicator(type));
			// SJM END
			if (this.getGlobalIdleIndicator(type) < ALPHA) {
				if (currentPenalty > 0) {
					newPenalty = Math.min(currentPenalty + (1 + globalUsage) * (1 - marker.getWeight()) * currentPenalty, 1);
					log.info("+++++++++++++increased newPenalty={}++++++++++++++++", newPenalty);
				} else {
					newPenalty = marker.getTypeOfService().initialPenalty();
					log.info("+++++++++++++initial newPenalty={}++++++++++++++", newPenalty);
				}
			} else {
				newPenalty = Math.max(currentPenalty - (2 - globalUsage) * marker.getWeight() * currentPenalty, 0);
				log.info("++++++++++decreased 1 newPenalty={}+++++++++", newPenalty);
			}
		} else {
			newPenalty = Math.max(currentPenalty - marker.getWeight() * currentPenalty, 0);
			log.info("++++++++++decreased 2 newPenalty={}+++++++++", newPenalty);
		}
		
		return newPenalty;
	}
	
	public void sendMarkerScheduler(final SrtcMarker marker) {
		OFSrtcmFeatures markerData = new OFSrtcmFeatures();
		markerData.setCIR(U32.t(-1));
		markerData.setCBS(U64.t(new BigInteger("-1")));
		markerData.setEBS(U64.t(new BigInteger("-1")));
		markerData.setCBorrowSuccessProb((byte) Math.ceil(100 - marker.getCTokenPenalty() * 100));
		markerData.setEBorrowSuccessProb((byte) Math.ceil(100 - marker.getETokenPenalty() * 100));
		
		OFMarkerConfigVendorData vendorData = new OFMarkerConfigVendorData();
		vendorData.setDataType(OFMarkerConfigVendorData.ENSLAB_MARKER_CONFIG);
		vendorData.setMarkerData(markerData);
		vendorData.setMarkerId(marker.getMarkerId());
		vendorData.setMarkerType(marker.getMarkerType());
		
		OVXVendor msg = new OVXVendor();
		msg.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
		msg.setVendorData(vendorData);
		msg.setLengthU(OVXVendor.MINIMUM_LENGTH + vendorData.getLength());
		
		this.sendMsg(msg, this);
	}
	
	public void scheduleNext() {
		List<Marker> markers = sw.getAllMarkers();
		
		for (Marker marker : markers) {
			if (marker.getMarkerId() == OFMarker.OFPM_GLOBAL.getValue())
				continue;
			if (marker.getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
				SrtcMarker srtcm = (SrtcMarker) marker;
				double cPenalty = this.calcPenalty(srtcm, BucketType.C_BUCKET);
				double ePenalty = this.calcPenalty(srtcm, BucketType.E_BUCKET);
				// SJM TEST
				log.info("cPenalty={}, ePenalty={}", cPenalty, ePenalty);
				// SJM TEST END
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
