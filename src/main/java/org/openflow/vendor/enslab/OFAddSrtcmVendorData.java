package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFAddSrtcmVendorData extends OFMarkerAddVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFAddSrtcmVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFAddSrtcmVendorData.instantiable;
    }
    
    private int CIR;
    private long CBS, EBS;
    
    public OFAddSrtcmVendorData() {
    	super();
    	super.setMarkerType(OFMarkerAddVendorData.ENSLAB_MARKER_TYPE_SRTCM);
    	//super.setMarkerLength(24);
    }
    
    public OFAddSrtcmVendorData(final int markerId) {
    	super(markerId, OFMarkerAddVendorData.ENSLAB_MARKER_TYPE_SRTCM);
    	//super.setMarkerLength(24);
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
