package org.openflow.vendor.enslab;

import java.lang.reflect.Constructor;

import org.openflow.protocol.Instantiable;

public enum OFMarkerType {
	ENSLAB_MARKER_SRTC(1, "srTCM", 
			OFSrtcmFeaturesReply.class, OFSrtcmStatsReply.class,
			new Instantiable<OFMarkerReply>() {
				@Override
				public OFMarkerReply instantiate() {
					return new OFSrtcmFeaturesReply();
				}
			}, new Instantiable<OFMarkerReply>() {
				@Override
				public OFMarkerReply instantiate() {
					return new OFSrtcmStatsReply();
				}
			}),
	
	ENSLAB_MARKER_TRTC(2, "trTCM");
	
	private int value;
	private String name;
	private Class<? extends OFMarkerReply> featuresReply, statsReply;
	protected Constructor<? extends OFMarkerReply> featuresReplyConstructor, statsReplyConstructor;
	private Instantiable<OFMarkerReply> featuresInstantiable, statsInstantiable;
    
    private OFMarkerType(final int markerType, final String name,
    		final Class<? extends OFMarkerReply> featuresReplyClass,
    		final Class<? extends OFMarkerReply> statsReplyClass,
            final Instantiable<OFMarkerReply> featuresInstantiable,
            final Instantiable<OFMarkerReply> statsInstantiable) {

    	this.value = markerType;
		this.name = name;
		this.featuresReply = featuresReplyClass;
		try {
			this.featuresReplyConstructor = featuresReplyClass.getConstructor(new Class[] {});
		} catch (Exception e) {
			throw new RuntimeException(
                    "Failure getting constructor for class: " + featuresReplyClass, e);
		} 
		
		this.statsReply = statsReplyClass;
		try {
			this.statsReplyConstructor = statsReplyClass.getConstructor(new Class[] {});
		} catch (Exception e) {
			throw new RuntimeException(
                    "Failure getting constructor for class: " + statsReplyClass, e);
		}
		
		this.featuresInstantiable = featuresInstantiable;
		this.statsInstantiable = statsInstantiable;
		
    }
    
    private OFMarkerType(final int markerType, final String name) {
    	this.value = markerType;
    	this.name= name;
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
	
	public String getName() {
		return this.name;
	}
	
	public Class<? extends OFMarkerReply> toClass(final int vendorDataType) {
		switch (vendorDataType) {
		case OFMarkerReplyVendorData.ENSLAB_MARKER_FEATURES_REPLY:
			return this.featuresReply;
			
		case OFMarkerReplyVendorData.ENSLAB_MARKER_STATS_REPLY:
			return this.statsReply;
			
		default:
			throw new RuntimeException(vendorDataType + " - Invalid data type for MARKER_REPLY");
		}
	}
	
	public Instantiable<OFMarkerReply> getFeaturesInstantiable() {
		return this.featuresInstantiable;
	}
	
	public void setFeaturesInstantiable(Instantiable<OFMarkerReply> featuresInstantiable) {
		this.featuresInstantiable = featuresInstantiable;
	}
	
	public Instantiable<OFMarkerReply> getStatsInstantiable() {
		return this.statsInstantiable;
	}
	
	public void setStatsInstantiable(Instantiable<OFMarkerReply> statsInstantiable) {
		this.statsInstantiable = statsInstantiable;
	}
	
	public OFMarkerReply newInstance(final int dataType) {
		if (dataType == OFMarkerReplyVendorData.ENSLAB_MARKER_STATS_REPLY) {
			return this.getStatsInstantiable().instantiate();
		} else if (dataType == OFMarkerReplyVendorData.ENSLAB_MARKER_FEATURES_REPLY) {
			return this.getFeaturesInstantiable().instantiate();
		} else {
			return null;
		}
	}
}
