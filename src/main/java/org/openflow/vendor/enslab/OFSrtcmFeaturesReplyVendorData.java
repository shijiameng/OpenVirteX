package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFSrtcmFeaturesReplyVendorData extends OFMarkerReplyVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFSrtcmFeaturesReplyVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFSrtcmFeaturesReplyVendorData.instantiable;
    }
    
    protected int CIR;
    protected long CBS, EBS;
    
    public OFSrtcmFeaturesReplyVendorData() {
    	super.setDataType(OFMarkerReplyVendorData.ENSLAB_MARKER_FEATURES_REPLY);
    }
    
    public void setCIR(final int CIR) {
    	this.CIR = CIR;
    }
    
    public int getCIR() {
    	return this.CIR;
    }
    
    public void setCBS(final long CBS) {
    	this.CBS = CBS;
    }
    
    public long getCBS() {
    	return this.CBS;
    }
    
    public void setEBS(final long EBS) {
    	this.EBS = EBS;
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
        data.readInt();
        this.CBS = data.readLong();
        this.EBS = data.readLong();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.CIR);
        data.writeInt(0);
        data.writeLong(this.CBS);
        data.writeLong(this.EBS);
    }
}
