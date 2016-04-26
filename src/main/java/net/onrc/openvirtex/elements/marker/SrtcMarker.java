package net.onrc.openvirtex.elements.marker;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFVendor;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.OFMarkerAddVendorData;
import org.openflow.vendor.enslab.OFMarkerRemoveVendorData;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.statistics.OFSrtcmFeatures;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.network.TypeOfService;
import net.onrc.openvirtex.messages.OVXVendor;

public class SrtcMarker extends Marker {
	
	protected int committedInfoRate;
	
	protected long committedBurstSize;
	
	protected long exceedBurstSize;
		
	public SrtcMarker(final int markerId, PhysicalSwitch sw, final TypeOfService toS) {
		super(markerId, sw);
		super.setMarkerType(OFMarkerType.ENSLAB_MARKER_SRTC);
		super.setTypeOfService(toS);
		
		this.committedInfoRate = 0;
		this.committedBurstSize = 0L;
		this.exceedBurstSize = 0L;
	}
	
	public void setCommittedInfoRate(final int CIR) {
		this.committedInfoRate = CIR;
	}
	
	public void setCommittedBurstSize(final long CBS) {
		this.committedBurstSize = CBS;
	}
	
	public void setExceedBurstSize(final long EBS) {
		this.exceedBurstSize = EBS;
	}
	
	public int getCommittedInfoRate() {
		return this.committedInfoRate;
	}
	
	public long getCommittedBurstSize() {
		return this.committedBurstSize;
	}
	
	public long getExceedBurstSize() {
		return this.exceedBurstSize;
	}
	
	@Override
	public void boot() {
		OVXVendor vendor = new OVXVendor();
		OFMarkerAddVendorData vendorData = new OFMarkerAddVendorData();
		OFSrtcmFeatures srtcmFeatures = new OFSrtcmFeatures();
		
		srtcmFeatures.setCIR(this.committedInfoRate);
		srtcmFeatures.setCBS(this.committedBurstSize);
		srtcmFeatures.setEBS(this.exceedBurstSize);
		
		vendorData.setMarkerType(OFMarkerType.ENSLAB_MARKER_SRTC);
		vendorData.setMarkerId(this.markerId);
		vendorData.setMarkerData(srtcmFeatures);
		
		vendor.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
		vendor.setVendorData(vendorData);
		vendor.setLengthU(OVXVendor.MINIMUM_LENGTH + vendorData.getLength());
		
        sendMsg(vendor, this);
        isBooted = true;
	}

	@Override
	public void tearDown() {
		OVXVendor vendor = new OVXVendor();
		OFMarkerRemoveVendorData request = new OFMarkerRemoveVendorData(this.markerId);
		vendor.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
		vendor.setVendorData(request);
        vendor.setLengthU(OFVendor.MINIMUM_LENGTH + request.getLength());
        sendMsg(vendor, this);
        isBooted = false;
        this.parentSwitch.removeMarker(this);
	}

	@Override
	public void sendMsg(OFMessage msg, OVXSendMsg from) {
		// TODO Auto-generated method stub
		this.parentSwitch.sendMsg(msg, from);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
