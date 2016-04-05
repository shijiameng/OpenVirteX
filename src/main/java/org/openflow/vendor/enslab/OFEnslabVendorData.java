package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.vendor.OFVendorData;

public class OFEnslabVendorData implements OFVendorData {
	
	public static final int ENSLAB_VENDOR_ID = 0xff001021;
	
	public static final int ENSLAB_MARKER_ADD = 100;
	public static final int ENSLAB_MARKER_REMOVE = 101;
	public static final int ENSLAB_MARKER_FEATURES_REQUEST = 102;
	public static final int ENSLAB_MARKER_FEATURES_REPLY = 103;
	public static final int ENSLAB_MARKER_STATS_REQUEST = 104;
	public static final int ENSLAB_MARKER_STATS_REPLY = 105;
	
	protected int dataType;
	
	public OFEnslabVendorData() {
	}
	
	public OFEnslabVendorData(final int dataType) {
		this.dataType = dataType;
	}
	
	public int getDataType() {
		return this.dataType;
	}

	public void setDataType(final int dataType) {
        this.dataType = dataType;
    }
	
	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public void readFrom(ChannelBuffer data, int length) {
		dataType = data.readInt();
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		data.writeInt(dataType);
	}

}
