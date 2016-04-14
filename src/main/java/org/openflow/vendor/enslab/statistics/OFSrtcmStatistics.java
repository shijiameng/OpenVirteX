package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.vendor.enslab.OFMarkerData;

public class OFSrtcmStatistics implements OFMarkerData {
	
    protected long nPackets, nBytes;
    protected long nGreenPackets, nGreenBytes;
    protected long nYellowPackets, nYellowBytes;
    protected long nRedPackets, nRedBytes;
    protected long nCTokens, nETokens;
    protected long nCBorrowed, nEBorrowed;

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
    
    public long getNumberOfCBorrowed() {
    	return this.nCBorrowed;
    }
    
    public void setNumberOfCBorrowed(final long nTokens) {
    	this.nCBorrowed = nTokens;
    }
    
    public long getNumberOfEBorrowed() {
    	return this.nEBorrowed;
    }
    
    public void setNumberOfEBorrowed(final long nTokens) {
    	this.nEBorrowed = nTokens;
    }
    
    
    @Override
    public String toString() {
    	return "n_packets=" + this.nPackets + ";n_bytes=" + this.nBytes +
    			";n_g_packets=" + this.nGreenPackets + ";n_g_bytes=" + this.nGreenBytes  +
    			";n_y_packets=" + this.nYellowPackets + ";n_y_bytes=" + this.nYellowBytes +
    			";n_r_packets=" + this.nRedPackets + ";n_r_bytes=" + this.nRedBytes +
    			";n_c_tokens=" + this.nCTokens + ";n_e_tokens=" + this.nETokens +
    			";n_c_borrowed=" + this.nCBorrowed + ";n_e_borrowed=" + this.nEBorrowed;
    }

	@Override
	public int getLength() {
		return 96;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
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
		this.nCBorrowed = data.readLong();
		this.nEBorrowed = data.readLong();
	}

	@Override
	public void writeTo(ChannelBuffer data) {
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
		data.writeLong(this.nCBorrowed);
		data.writeLong(this.nEBorrowed);
	}
}
