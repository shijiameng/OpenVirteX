package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerConfigVendorData extends OFEnslabVendorData {
	public static final int ENSLAB_MARKER_CONFIG = 102;
	
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerConfigVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerConfigVendorData.instantiable;
    }
    
    public static final int MINIMUM_LENGTH = 8;
    
    protected OFMarkerType markerType;
    protected int markerId;
    protected OFMarkerData markerData;
    
    public OFMarkerConfigVendorData() {
    	super(OFMarkerConfigVendorData.ENSLAB_MARKER_CONFIG);
    }
    
    public void setMarkerId(final int markerId) {
    	this.markerId = markerId;
    }
    
    public void setMarkerType(final OFMarkerType markerType) {
    	this.markerType = markerType;
    }
    
    public int getMarkerId() {
    	return this.markerId;
    }
    
    public OFMarkerType getMarkerType() {
    	return this.markerType;
    }
    
    public void setMarkerData(final OFMarkerData markerData) {
    	this.markerData = markerData;
    }
    
    public OFMarkerData getMarkerData() {
    	return this.markerData;
    }
    
    @Override
    public int getLength() {
        return super.getLength() + OFMarkerConfigVendorData.MINIMUM_LENGTH + markerData.getLength();
    }

    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.markerType = OFMarkerType.valueOf(data.readInt());
        this.markerId = data.readInt();
        this.markerData = this.markerType.newInstance(OFEnslabVendorData.ENSLAB_MARKER_CONFIG);
        this.markerData.readFrom(data);
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerType.getValue());
        data.writeInt(this.markerId);
        this.markerData.writeTo(data);
    }
    
}
