package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerPenaltySetVendorData extends OFEnslabVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerPenaltySetVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerPenaltySetVendorData.instantiable;
    }
    
    public static final int MINIMUM_LENGTH = 8;
    
    protected int markerId;
    
    protected OFMarkerType type;
    
    protected OFMarkerData markerData;
    
    public OFMarkerPenaltySetVendorData() {
    	super(OFEnslabVendorData.ENSLAB_MARKER_PENALTY_SET);
    }
    
    public void setMarkerId(final int markerId) {
    	this.markerId = markerId;
    }
    
    public int getMarkerId() {
    	return this.markerId;
    }
    
    public void setMarkerType(final OFMarkerType type) {
    	this.type = type;
    }
    
    public OFMarkerType getMarkerType() {
    	return this.type;
    }
    
    public void setMarkerData(final OFMarkerData data) {
    	this.markerData = data;
    }
    
    public OFMarkerData getMarkerData() {
    	return this.markerData;
    }
    
    @Override
    public int getLength() {
    	return super.getLength() + OFMarkerPenaltySetVendorData.MINIMUM_LENGTH + markerData.getLength();
    }
    
    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
    	super.readFrom(data, length);
    	this.markerId = data.readInt();
    	this.type = OFMarkerType.valueOf(data.readInt());
    	this.markerData = this.type.newInstance(OFEnslabVendorData.ENSLAB_MARKER_PENALTY_SET);
    	markerData.readFrom(data);
    }
    
    @Override
    public void writeTo(final ChannelBuffer data) {
    	data.writeInt(this.markerId);
    	data.writeInt(this.type.value());
    	this.markerData.writeTo(data);
    }
    
}
