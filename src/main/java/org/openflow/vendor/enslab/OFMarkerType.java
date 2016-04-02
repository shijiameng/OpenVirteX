package org.openflow.vendor.enslab;

public enum OFMarkerType {
	ENSLAB_MARKER_SRTC(1),
	ENSLAB_MARKER_TRTC(2);
	
	private int value;
	
	private OFMarkerType(final int markerType) {
		this.value = markerType;
	}
	
	public static OFMarkerType valueOf(int value) {
		switch (value) {
		case 1:
			return OFMarkerType.ENSLAB_MARKER_SRTC;
			
		case 2:
			return OFMarkerType.ENSLAB_MARKER_TRTC;
			
		default:
			return null;
		}
	}
	
	public int value() {
		return this.value;
	}	
}
