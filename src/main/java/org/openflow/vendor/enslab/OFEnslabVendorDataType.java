package org.openflow.vendor.enslab;

import org.openflow.protocol.Instantiable;
import org.openflow.protocol.vendor.OFVendorData;

public enum OFEnslabVendorDataType {
	
	ENSLAB_MARKER_ADD(100, OFMarkerAddVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerAddVendorData.getInstantiable().instantiate();
		}
	}),
	ENSLAB_MARKER_REMOVE(101, OFMarkerRemoveVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerRemoveVendorData.getInstantiable().instantiate();
		}
	}),
	ENSLAB_MARKER_FEATURES_REQUEST(102, OFMarkerRequestVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerRequestVendorData.getInstantiable().instantiate();
		}
	}),
	ENSLAB_MARKER_FEATURES_REPLY(103, OFMarkerReplyVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerReplyVendorData.getInstantiable().instantiate();
		}
	}),
	ENSLAB_MARKER_STATS_REQUEST(104, OFMarkerRequestVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerRequestVendorData.getInstantiable().instantiate();
		}
	}),
	ENSLAB_MARKER_STATS_REPLY(105, OFMarkerReplyVendorData.class, new Instantiable<OFVendorData>() {
		@Override
		public OFVendorData instantiate() {
			return OFMarkerReplyVendorData.getInstantiable().instantiate();
		}
	});
	
	static OFEnslabVendorDataType[] typeMap;
	
	static void addMapping(final int thisType, final OFEnslabVendorDataType type) {
		if (typeMap == null) {
			typeMap = new OFEnslabVendorDataType[6];
		}
		
		if (thisType - 100 > 0) {
			typeMap[thisType - 100] = type;
		}
	}
	
	public static OFEnslabVendorDataType valueOf(int value) {
		return OFEnslabVendorDataType.typeMap[value - 100];
	}
	
	protected int dataType;
	protected Class<? extends OFEnslabVendorData> vendorDataClass;
	protected Instantiable<OFVendorData> instantiable;

	OFEnslabVendorDataType(final int dataType, 
			final Class<? extends OFEnslabVendorData> vendorDataClass,
			final Instantiable<OFVendorData> instantiable) {
		this.dataType = dataType;
		this.vendorDataClass = vendorDataClass;
		this.instantiable = instantiable;		
		OFEnslabVendorDataType.addMapping(dataType, this);
	}
	
	public Class<? extends OFEnslabVendorData> getVendorDataClass() {
		return this.vendorDataClass;
	}
	
	public Instantiable<OFVendorData> getInstantabile() {
		return this.instantiable;
	}
	
	public int getValue() {
		return dataType;
	}
}
