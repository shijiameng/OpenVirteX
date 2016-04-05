package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;

public interface OFMarkerReply {
	public int getLength();
	public void readFrom(ChannelBuffer data);
	public void writeTo(ChannelBuffer data);
}
