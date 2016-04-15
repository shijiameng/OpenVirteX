package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;

public class OFMarkerStatisticsRequest implements OFEnslabStatistics {
	
	public final static int ENSLAB_MARKER_STATS_REQUEST = 105;
	
	protected int markerId;
	
	public void setMarkerId(final int markerId) {
		this.markerId = markerId;
	}
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	@Override
	public int getLength() {
		return 8;
	}
	
	@Override
	public void readFrom(ChannelBuffer data) {
		this.markerId = data.readInt();
		data.readInt();
	}
	
	@Override
	public void writeTo(ChannelBuffer data) {
		data.writeInt(this.markerId);
		data.writeInt(0);
	}
}
