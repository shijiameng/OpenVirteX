package org.openflow.vendor.enslab;

import java.lang.reflect.Constructor;

import org.openflow.protocol.Instantiable;
import org.openflow.vendor.enslab.statistics.OFEnslabStatistics;
import org.openflow.vendor.enslab.statistics.OFSrtcmFeatures;
import org.openflow.vendor.enslab.statistics.OFSrtcmStatistics;

public enum OFMarkerType {
	ENSLAB_MARKER_SRTC(1, "srTCM", 
			OFSrtcmFeatures.class, 
			OFSrtcmStatistics.class,
			new Instantiable<OFMarkerData>() {
				@Override
				public OFMarkerData instantiate() {
					return new OFSrtcmFeatures();
				}
			}, new Instantiable<OFMarkerData>() {
				@Override
				public OFMarkerData instantiate() {
					return new OFSrtcmStatistics();
				}
			}),
	
	ENSLAB_MARKER_TRTC(2, "trTCM");
	
	private int value;
	private String name;
	private Class<? extends OFMarkerData> features, stats;
	protected Constructor<? extends OFMarkerData> featuresConstructor, statsConstructor, penaltyConstructor;
	private Instantiable<OFMarkerData> featuresInstantiable, statsInstantiable;
    
    private OFMarkerType(final int markerType, final String name,
    		final Class<? extends OFMarkerData> featuresClass,
    		final Class<? extends OFMarkerData> statsClass,
            final Instantiable<OFMarkerData> featuresInstantiable,
            final Instantiable<OFMarkerData> statsInstantiable) {

    	this.value = markerType;
		this.name = name;
		this.features = featuresClass;
		try {
			this.featuresConstructor = featuresClass.getConstructor(new Class[] {});
		} catch (Exception e) {
			throw new RuntimeException(
                    "Failure getting constructor for class: " + featuresClass, e);
		} 
		
		this.stats = statsClass;
		try {
			this.statsConstructor = statsClass.getConstructor(new Class[] {});
		} catch (Exception e) {
			throw new RuntimeException(
                    "Failure getting constructor for class: " + statsClass, e);
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

	public int getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Class<? extends OFMarkerData> toClass(final int vendorDataType) {
		switch (vendorDataType) {
		case OFEnslabVendorData.ENSLAB_MARKER_ADD:
		case OFEnslabStatistics.ENSLAB_MARKER_FEATURES_REPLY:
			return this.features;
			
		case OFEnslabStatistics.ENSLAB_MARKER_STATS_REPLY:
			return this.stats;
			
		default:
			throw new RuntimeException(vendorDataType + " - Invalid data type for MARKER_REPLY");
		}
	}
	
	public Instantiable<OFMarkerData> getFeaturesInstantiable() {
		return this.featuresInstantiable;
	}
	
	public void setFeaturesInstantiable(Instantiable<OFMarkerData> featuresInstantiable) {
		this.featuresInstantiable = featuresInstantiable;
	}
	
	public Instantiable<OFMarkerData> getStatsInstantiable() {
		return this.statsInstantiable;
	}
	
	public void setStatsInstantiable(Instantiable<OFMarkerData> statsInstantiable) {
		this.statsInstantiable = statsInstantiable;
	}
	
	public OFMarkerData newInstance(final int dataType) {
		switch (dataType) {
		case OFEnslabVendorData.ENSLAB_MARKER_ADD:
		case OFEnslabStatistics.ENSLAB_MARKER_FEATURES_REPLY:
			return this.getFeaturesInstantiable().instantiate();
			
		case OFEnslabStatistics.ENSLAB_MARKER_STATS_REPLY:
			return this.getStatsInstantiable().instantiate();
			
		default:
			return null;
		}
	}
	
}
