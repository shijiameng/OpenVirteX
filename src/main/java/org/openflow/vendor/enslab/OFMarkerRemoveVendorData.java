package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFMarkerRemoveVendorData extends OFEnslabVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFMarkerRemoveVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFMarkerRemoveVendorData.instantiable;
    }
    
    public static final int ENSLAB_MARKER_REMOVE = 101;
    
    protected int markerId;
    
    public OFMarkerRemoveVendorData() {
    	super(OFMarkerRemoveVendorData.ENSLAB_MARKER_REMOVE);
    }
    
    public OFMarkerRemoveVendorData(final int markerId) {
    	super(OFMarkerRemoveVendorData.ENSLAB_MARKER_REMOVE);
    	this.markerId = markerId;
    }
    
    public void setMarkerId(int markerId) {
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
        data.readInt(); // pad[4]
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerId);
        data.writeInt(0); // pad[4]
    }
}
