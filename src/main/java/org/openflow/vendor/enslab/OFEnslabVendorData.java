package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.vendor.OFVendorData;

public class OFEnslabVendorData implements OFVendorData {
	
	public static final int ENSLAB_VENDOR_ID = 0xff001021;
	
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
