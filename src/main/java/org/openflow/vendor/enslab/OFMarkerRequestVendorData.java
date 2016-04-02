package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerRequestVendorData extends OFEnslabVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerRequestVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerRequestVendorData.instantiable;
    }
    
    public static final int ENSLAB_MARKER_FEATURES_REQUEST = 102;
    public static final int ENSLAB_MARKER_STATS_REQUEST = 104;
    
	protected int markerId;
	
	public OFMarkerRequestVendorData() {	
	}
	
	public OFMarkerRequestVendorData(final int dataType) {
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
	
	@Override
    public int getLength() {
        return super.getLength() + 8;
    }

    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.markerId = data.readInt();
        data.readInt();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerId);
        data.writeInt(0);
    }
}
