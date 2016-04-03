package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;

public class OFSrtcmAddVendorData extends OFMarkerAddVendorData {
    
    protected int CIR;
    protected long CBS, EBS;
    
    public OFSrtcmAddVendorData() {
    	super();
    	super.setMarkerType(OFMarkerAddVendorData.ENSLAB_MARKER_TYPE_SRTCM);
    }
    
    public OFSrtcmAddVendorData(final int markerId) {
    	super(markerId, OFMarkerAddVendorData.ENSLAB_MARKER_TYPE_SRTCM);
    }
    
    public void setCIR(final int CIR) {
    	this.CIR = CIR;
    }
    
    public void setCBS(final long CBS) {
    	this.CBS = CBS;
    }
    
    public void setEBS(final long EBS) {
    	this.EBS = EBS;
    }
    
    public int getCIR() {
    	return this.CIR;
    }
    
    public long getCBS() {
    	return this.CBS;
    }
    
    public long getEBS() {
    	return this.EBS;
    }
    
    @Override
    public int getLength() {
    	return super.getLength() + 24;
    }
    
    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.CIR = data.readInt();
        data.readInt(); // pad[4]
        this.CBS = data.readLong();
        this.EBS = data.readLong();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.CIR);
        data.writeInt(0); // pad[4]
        data.writeLong(this.CBS);
        data.writeLong(this.EBS);
    }
}
