package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;

public class OFMarkerReplyVendorData extends OFEnslabVendorData {
	public static final int ENSLAB_MARKER_FEATURES_REQUEST = 102;
    public static final int ENSLAB_MARKER_STATS_REQUEST = 104;
    
	protected int markerId;
	protected OFMarkerType markerType;
	
	public OFMarkerReplyVendorData() {	
	}
	
	public OFMarkerReplyVendorData(final int dataType) {
		super.setDataType(dataType);
	}
	
	public void setDataType(final int dataType) {
		this.setDataType(dataType);
	} 
	
	public void setMarkerId(final int markerId) {
		this.markerId = markerId;
	}
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	public void setMarkerType(final int markerType) {
		this.markerType = OFMarkerType.valueOf(markerType);
	}
	
	public void setMarkerType(OFMarkerType markerType) {
		this.markerType = OFMarkerType.valueOf(markerType.value());
	}
	
	public OFMarkerType getMarkerType() {
		return this.markerType;
	}
	
	@Override
    public int getLength() {
        return super.getLength() + 8;
    }

    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.markerId = data.readInt();
        this.markerType = OFMarkerType.valueOf(data.readInt());
        data.readInt();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerId);
        data.writeInt(this.markerType.value());
    }
}
