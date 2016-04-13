package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.openflow.vendor.enslab.OFMarkerData;
import org.openflow.vendor.enslab.OFMarkerType;

public class OFMarkerStatisticsReply extends OFEnslabStatistics {

    public final static int ENSLAB_MARKER_STATS_REPLY = 103;
    
    protected int markerId;
	protected int markerType;
	protected OFMarkerData markerData;

	public OFMarkerStatisticsReply() {
		super(ENSLAB_MARKER_STATS_REPLY);
		// TODO Auto-generated constructor stub
	}
	
	public void setMarkerId(final int markerId) {
		this.markerId = markerId;
	}
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	
	public void setMarkerType(final OFMarkerType markerType) {
		this.markerType = markerType.getValue();
	}
	
	public OFMarkerType getMarkerType() {
		return OFMarkerType.valueOf(this.markerType);
	}
	
	public OFMarkerData getReply() {
		return this.markerData;
	}

	@Override
	public int getLength() {
		return super.getLength() + 8;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
		super.readFrom(data);
		this.markerId = data.readInt();
		this.markerType = data.readInt();
		markerData.readFrom(data);	
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		super.writeTo(data);
		data.writeInt(this.markerId);
		data.writeInt(this.markerType);
		markerData.writeTo(data);
	}
	
	@Override
	public byte[] toByteArray() {
		ChannelBuffer buffer = ChannelBuffers.buffer(this.getLength());
		this.writeTo(buffer);
		return buffer.array();
	}

}
