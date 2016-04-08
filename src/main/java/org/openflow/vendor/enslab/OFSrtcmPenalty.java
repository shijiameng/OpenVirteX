package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;

public class OFSrtcmPenalty implements OFMarkerData {
	
	protected int cBucketPenalty;
	
	protected int eBucketPenalty;
	
	public OFSrtcmPenalty() {
		
	}
	
	public OFSrtcmPenalty(final int c, final int e) {
		this.cBucketPenalty = c;
		this.eBucketPenalty = e;
	}
	
	public void setCBucketPenalty(final int penalty) {
		this.cBucketPenalty = penalty;
	}
	
	public int getCBucketPenalty() {
		return this.cBucketPenalty;
	}
	
	public void setEBucketPenalty(final int penalty) {
		this.eBucketPenalty = penalty;
	}
	
	public int getEBucketPenalty() {
		return this.eBucketPenalty;
	}
	
	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
		// TODO Auto-generated method stub
		this.cBucketPenalty = data.readInt();
		this.eBucketPenalty = data.readInt();
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		// TODO Auto-generated method stub
		data.writeInt(this.cBucketPenalty);
		data.writeInt(this.eBucketPenalty);
	}

}
