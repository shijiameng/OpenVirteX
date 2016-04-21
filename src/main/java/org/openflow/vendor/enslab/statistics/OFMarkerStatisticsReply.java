package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.vendor.enslab.OFMarkerData;
import org.openflow.vendor.enslab.OFMarkerType;

public class OFMarkerStatisticsReply implements OFEnslabStatistics {

    public final static int ENSLAB_MARKER_STATS_REPLY = 105;
    
    protected int markerId;
	protected OFMarkerType markerType;
	protected OFMarkerData markerData;

	public void setMarkerId(final int markerId) {
		this.markerId = markerId;
	}
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	
	public void setMarkerType(final OFMarkerType markerType) {
		this.markerType = markerType;
	}
	
	public OFMarkerType getMarkerType() {
		return this.markerType;
	}
	
	public OFMarkerData getMarkerData() {
		return this.markerData;
	}

	@Override
	public int getLength() {
		return 8;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
		this.markerId = data.readInt();
		this.markerType = OFMarkerType.valueOf(data.readInt());
		this.markerData = this.markerType.newInstance(ENSLAB_MARKER_STATS_REPLY);
		markerData.readFrom(data);	
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		data.writeInt(this.markerId);
		data.writeInt(this.markerType.getValue());
		markerData.writeTo(data);
	}
	
	@Override
	public String toString() {
		return "OFMarker reply: marker_id=" + this.markerId +
				";type=" + this.markerType.getName();
	}
}
