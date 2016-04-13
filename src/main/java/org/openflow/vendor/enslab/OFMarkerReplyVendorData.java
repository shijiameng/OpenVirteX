package org.openflow.vendor.enslab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerReplyVendorData extends OFEnslabVendorData {
	Logger log = LogManager.getLogger(OFMarkerReplyVendorData.class.getName());
	
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerReplyVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerReplyVendorData.instantiable;
    }
    
	public static final int ENSLAB_MARKER_FEATURES_REPLY = 103;
    public static final int ENSLAB_MARKER_STATS_REPLY = 105;
    
	protected int markerId;
	protected OFMarkerType markerType;
	protected OFMarkerData reply;
	
	public OFMarkerReplyVendorData() {	
	}
	
	public OFMarkerReplyVendorData(final int dataType) {
		super.setDataType(dataType);
	}
	
	public void setDataType(final int dataType) {
		super.setDataType(dataType);
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
		this.markerType = OFMarkerType.valueOf(markerType.getValue());
	}
	
	public OFMarkerType getMarkerType() {
		return this.markerType;
	}
	
	public OFMarkerData getReply() {
		return reply;
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
        if (this.markerType == OFMarkerType.ENSLAB_MARKER_SRTC) {
        	this.reply = this.markerType.newInstance(this.getDataType());
        }
        this.reply.readFrom(data);
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerId);
        data.writeInt(this.markerType.getValue());
        reply.writeTo(data);
    }
}
