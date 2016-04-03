package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerAddVendorData extends OFEnslabVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerAddVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerAddVendorData.instantiable;
    }
    
    public static final int ENSLAB_MARKER_TYPE_SRTCM = 1;
    
    public static final int ENSLAB_MARKER_TYPE_TRTCM = 2;
    
    public static final int ENSLAB_MARKER_ADD = 100;
    
    protected int markerType;
    protected int markerId;
    
    public OFMarkerAddVendorData() {
    	super(OFMarkerAddVendorData.ENSLAB_MARKER_ADD);
    }
    
    public OFMarkerAddVendorData(final int markerId) {
    	super(OFMarkerAddVendorData.ENSLAB_MARKER_ADD);
    	this.markerId = markerId;
    }
    
    public OFMarkerAddVendorData(final int markerId, final int markerType) {
    	super(OFMarkerAddVendorData.ENSLAB_MARKER_ADD);
    	this.markerId = markerId;
    	this.markerType = markerType;
    }
    
    public void setMarkerId(final int markerId) {
    	this.markerId = markerId;
    }
    
    public void setMarkerType(final int markerType) {
    	this.markerType = markerType;
    }
    
    public int getMarkerId() {
    	return this.markerId;
    }
    
    public int getMarkerType() {
    	return this.markerType;
    }
    
    @Override
    public int getLength() {
        return super.getLength() + 8;
    }

    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.markerType = data.readInt();
        this.markerId = data.readInt();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerType);
        data.writeInt(this.markerId);
    }
}
