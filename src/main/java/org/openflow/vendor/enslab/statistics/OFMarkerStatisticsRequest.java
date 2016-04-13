package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class OFMarkerStatisticsRequest extends OFEnslabStatistics {
	
	public final static int ENSLAB_MARKER_STATS_REQUEST = 105;
	
	protected int markerId;
	
	public OFMarkerStatisticsRequest() {
		super(ENSLAB_MARKER_STATS_REQUEST);
		// TODO Auto-generated constructor stub
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
	public void readFrom(ChannelBuffer data) {
		super.readFrom(data);
		this.markerId = data.readInt();
		data.readInt();
	}
	
	@Override
	public void writeTo(ChannelBuffer data) {
		super.writeTo(data);
		data.writeInt(this.markerId);
		data.writeInt(0);
	}
	
	@Override
	public byte[] toByteArray() {
		ChannelBuffer buffer = ChannelBuffers.buffer(this.getLength());
		this.writeTo(buffer);
		return buffer.array();
	}
}
