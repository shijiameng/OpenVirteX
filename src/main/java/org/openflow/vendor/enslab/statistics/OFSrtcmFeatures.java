package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.vendor.enslab.OFMarkerData;

public class OFSrtcmFeatures implements OFMarkerData {
   
	protected byte weight;
	
	protected int CIR;
	
    protected long CBS, EBS;
    
    public void setWeight(final byte weight) {
    	this.weight = weight;
    }
    
    public byte getWeight() {
    	return this.weight;
    }
    
    public void setCIR(final int CIR) {
    	this.CIR = CIR;
    }
    
    public int getCIR() {
    	return this.CIR;
    }
    
    public void setCBS(final long CBS) {
    	this.CBS = CBS;
    }
    
    public long getCBS() {
    	return this.CBS;
    }
    
    public void setEBS(final long EBS) {
    	this.EBS = EBS;
    }
    
    public long getEBS() {
    	return this.EBS;
    }

	@Override
	public int getLength() {
		return 24;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
		this.weight = data.readByte();
		data.readByte();
		data.readShort();
		this.CIR = data.readInt();
		this.CBS = data.readLong();
		this.EBS = data.readLong();
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		data.writeByte(this.weight);
		data.writeByte((byte) 0);
		data.writeShort(0);
		data.writeInt(this.CIR);
		data.writeLong(this.CBS);
		data.writeLong(this.EBS);
	}
}
