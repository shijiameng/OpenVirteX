package org.openflow.protocol;

public enum OFMarker {
	OFPM_MAX	((int) 0x0000ff00),
	OFPM_NONE 	((int) 0x0000ffff),
	OFPM_GLOBAL ((int) 0x0000fffe);
	
	protected int value;
	
	private OFMarker(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
