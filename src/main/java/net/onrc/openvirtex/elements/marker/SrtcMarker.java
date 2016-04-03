package net.onrc.openvirtex.elements.marker;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFVendor;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.OFMarkerRemoveVendorData;
import org.openflow.vendor.enslab.OFSrtcmAddVendorData;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.messages.OVXVendor;

public class SrtcMarker extends Marker {
	
	protected int committedInfoRate;
	
	protected long committedBurstSize;
	
	protected long exceedBurstSize;
	
	public SrtcMarker(final int markerId, PhysicalSwitch sw) {
		super(markerId, sw);
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
		OFSrtcmAddVendorData request = new OFSrtcmAddVendorData(this.markerId);
		vendor.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
		vendor.setVendorData(request);
        vendor.setLengthU(OFVendor.MINIMUM_LENGTH + request.getLength());
        request.setCBS(this.committedBurstSize);
        request.setCIR(this.committedInfoRate);
        request.setEBS(this.exceedBurstSize);
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
