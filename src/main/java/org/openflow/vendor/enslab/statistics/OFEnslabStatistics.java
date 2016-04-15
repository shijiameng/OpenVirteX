package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;

public interface OFEnslabStatistics {
	int getLength();
	void readFrom(ChannelBuffer data);
	void writeTo(ChannelBuffer data);
}
