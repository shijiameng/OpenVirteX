package org.openflow.protocol.action;

import org.jboss.netty.buffer.ChannelBuffer;

public class OFActionMark extends OFAction {
	public static int MINIMUM_LENGTH = 8;
	
	protected int markerId;
	
	public OFActionMark() {
        super.setType(OFActionType.OPAQUE_MARK);
        super.setLength((short) OFActionMark.MINIMUM_LENGTH);
    }

    public OFActionMark(final int markerId) {
        this();
        this.markerId = markerId;
    }
    
    public void setMarkerId(final int markerId) {
    	this.markerId = markerId;
    }
    
    public int getMarkerId() {
    	return this.markerId;
    }

    @Override
    public void readFrom(final ChannelBuffer data) {
        super.readFrom(data);
        this.markerId = data.readInt();
    }

    @Override
    public void writeTo(final ChannelBuffer data) {
        super.writeTo(data);
        data.writeInt(this.markerId);
    }

    @Override
    public int hashCode() {
        final int prime = 691;
        int result = super.hashCode();
        result = prime * result + this.markerId;
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof OFActionMark)) {
            return false;
        }
        final OFActionMark other = (OFActionMark) obj;
        if (this.markerId != other.markerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.type);
        builder.append("[");
        builder.append("Marker Id: ");
        builder.append(this.markerId);
        builder.append("]");
        return builder.toString();
    }
}
