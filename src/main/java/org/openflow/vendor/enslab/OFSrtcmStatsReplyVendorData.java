package org.openflow.vendor.enslab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public class OFSrtcmStatsReplyVendorData extends OFMarkerReplyVendorData {
	protected static Instantiable<OFVendorData> instantiable = new Instantiable<OFVendorData>() {
        @Override
        public OFVendorData instantiate() {
            return new OFSrtcmStatsReplyVendorData();
        }
    };
    
    public static Instantiable<OFVendorData> getInstantiable() {
        return OFSrtcmStatsReplyVendorData.instantiable;
    }
    
    protected long nPackets, nBytes;
    protected long nGreenPackets, nGreenBytes;
    protected long nYellowPackets, nYellowBytes;
    protected long nRedPackets, nRedBytes;
    protected long nCTokens, nETokens;

    public OFSrtcmStatsReplyVendorData() {
    	super.setDataType(OFMarkerReplyVendorData.ENSLAB_MARKER_STATS_REPLY);
    	super.setMarkerType(OFMarkerType.ENSLAB_MARKER_SRTC);
    }
    
    public void setNumberOfPackets(final long nPackets) {
    	this.nPackets = nPackets;
    }
    
    public void setNumberOfBytes(final long nBytes) {
    	this.nBytes = nBytes;
    }
    
    public long getNumberOfPackets() {
    	return this.nPackets;
    }
    
    public long getNumberOfBytes() {
    	return this.nBytes;
    }
    
    public void setNumberOfGreenPackets(final long nGreenPackets) {
    	this.nGreenPackets = nGreenPackets;
    }
    
    public void setNumberOfGreenBytes(final long nGreenBytes) {
    	this.nGreenBytes = nGreenBytes;
    }
    
    public long getNumberOfGreenPackets() {
    	return this.nGreenPackets;
    }
    
    public long getNumberOfGreenBytes() {
    	return this.nGreenBytes;
    }
    
    public void setNumberOfYellowPackets(final long nYellowPackets) {
    	this.nYellowPackets = nYellowPackets;
    }
    
    public void setNumberOfYellowBytes(final long nYellowBytes) {
    	this.nYellowBytes = nYellowBytes;
    }
    
    public long getNumberOfYellowPackets() {
    	return this.nYellowPackets;
    }
    
    public long getNumberOfYellowBytes() {
    	return this.nYellowBytes;
    }
    
    public void setNumberOfRedPackets(final long nRedPackets) {
    	this.nRedPackets = nRedPackets;
    }
    
    public void setNumberOfRedBytes(final long nRedBytes) {
    	this.nRedBytes = nRedBytes;
    }
    
    public long getNumberOfRedPackets() {
    	return this.nRedPackets;
    }
    
    public long getNumberOfRedBytes() {
    	return this.nRedBytes;
    }

    public void setNumberOfCTokens(final long nCTokens) {
    	this.nCTokens = nCTokens;
    }
    
    public long getNumberOfCTokens() {
    	return this.nCTokens;
    }
    
    public void setNumberOfETokens(final long nETokens) {
    	this.nETokens = nETokens;
    }
    
    public long getNumberOfETokens() {
    	return this.nETokens;
    }
    
    @Override
    public int getLength() {
        return super.getLength() + 80;
    }

    @Override
    public void readFrom(final ChannelBuffer data, final int length) {
        super.readFrom(data, length);
        this.nPackets = data.readLong();
        this.nBytes = data.readLong();
        this.nGreenPackets = data.readLong();
        this.nGreenBytes = data.readLong();
        this.nYellowPackets = data.readLong();
        this.nYellowBytes = data.readLong();
        this.nRedPackets = data.readLong();
        this.nRedBytes = data.readLong();
        this.nCTokens = data.readLong();
        this.nETokens = data.readLong();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeLong(this.nPackets);
        data.writeLong(this.nBytes);
        data.writeLong(this.nGreenPackets);
        data.writeLong(this.nGreenBytes);
        data.writeLong(this.nYellowPackets);
        data.writeLong(this.nYellowBytes);
        data.writeLong(this.nRedPackets);
        data.writeLong(this.nRedBytes);
        data.writeLong(this.nCTokens);
        data.writeLong(this.nETokens);
    }
}
