package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class OFEnslabStatistics {
	
	protected int dataType;
	
	public OFEnslabStatistics(final int dataType) {
		this.dataType = dataType;
	}
	
	public void setDataType(final int dataType) {
		this.dataType = dataType;
	}
	
	public int getDataType() {
		return this.dataType;
	}
	
	public int getLength() {
		return 8; // dataType + pad[4]
	}
	
	public void readFrom(ChannelBuffer data) {
		this.dataType = data.readInt();
		data.readInt();
	}
	
	public void writeTo(ChannelBuffer data) {
		data.writeInt(this.dataType);
		data.writeInt(0);
	}
	
	public byte[] toByteArray() {
		ChannelBuffer buffer = ChannelBuffers.buffer(this.getLength());
		this.writeTo(buffer);
		return buffer.array();
	}
}
