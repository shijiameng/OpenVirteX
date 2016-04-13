package org.openflow.vendor.enslab.statistics;

import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.vendor.enslab.OFMarkerData;

public class OFSrtcmFeatures implements OFMarkerData {
   
	protected int CIR;
	
    protected long CBS, EBS;
    
    protected byte cBorrowSuccessProb, eBorrowSuccessProb;
    
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
    
    public void setCBorrowSuccessProb(final byte cBorrowSuccessProb) {
    	this.cBorrowSuccessProb = cBorrowSuccessProb;
    }
    
    public void setEBorrowSuccessProb(final byte eBorrowSuccessProb) {
    	this.eBorrowSuccessProb = eBorrowSuccessProb;
    }
    
    public byte getCBorrowSuccessProb() {
    	return this.cBorrowSuccessProb;
    }
    
    public byte getEBorrowSuccessProb() {
    	return this.eBorrowSuccessProb;
    }

	@Override
	public int getLength() {
		return 32;
	}

	@Override
	public void readFrom(ChannelBuffer data) {
		this.CIR = data.readInt();
		data.readInt();
		this.CBS = data.readLong();
		this.EBS = data.readLong();
		this.cBorrowSuccessProb = data.readByte();
		this.eBorrowSuccessProb = data.readByte();
		data.readInt();
		data.readShort();
	}

	@Override
	public void writeTo(ChannelBuffer data) {
		data.writeInt(this.CIR);
		data.writeInt(0);
		data.writeLong(this.CBS);
		data.writeLong(this.EBS);
		data.writeByte(this.cBorrowSuccessProb);
		data.writeByte(this.eBorrowSuccessProb);
		data.writeInt(0);
		data.writeShort(0);
	}
}
