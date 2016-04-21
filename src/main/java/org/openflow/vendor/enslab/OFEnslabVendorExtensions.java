package org.openflow.vendor.enslab;

import org.openflow.protocol.vendor.OFBasicVendorDataType;
import org.openflow.protocol.vendor.OFBasicVendorId;
import org.openflow.protocol.vendor.OFVendorId;

public class OFEnslabVendorExtensions {
	private static boolean initialized = false;

    public static synchronized void initialize() {
        if (initialized)
            return;

        // Configure openflowj to be able to parse the role request/reply
        // vendor messages.
        OFBasicVendorId enslabVendorId = new OFBasicVendorId(
                OFEnslabVendorData.ENSLAB_VENDOR_ID, 4);
        OFVendorId.registerVendorId(enslabVendorId);
        
        OFBasicVendorDataType markerAddVendorData = new OFBasicVendorDataType(
                OFMarkerAddVendorData.ENSLAB_MARKER_ADD,
                OFMarkerAddVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerAddVendorData);
        
        OFBasicVendorDataType markerRemoveVendorData = new OFBasicVendorDataType(
                OFMarkerRemoveVendorData.ENSLAB_MARKER_REMOVE,
                OFMarkerRemoveVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerRemoveVendorData);
        
        OFBasicVendorDataType markerConfigVendorData = new OFBasicVendorDataType(
        		OFMarkerConfigVendorData.ENSLAB_MARKER_CONFIG,
        		OFMarkerConfigVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerConfigVendorData);
        
//        OFBasicVendorDataType markerStatisticsRequest = new OFBasicVendorDataType(
//        		OFMarkerStatisticsRequest.ENSLAB_MARKER_STATS_REQUEST,
//        		OFMarkerStatisticsRequest.getInstantiable());
//        enslabVendorId.registerVendorDataType(markerStatisticsRequest);
//        
//        OFBasicVendorDataType markerStatisticsReply = new OFBasicVendorDataType(
//        		OFMarkerStatisticsReply.ENSLAB_MARKER_STATS_REPLY,
//        		OFMarkerStatisticsReply.getInstantiable());
//        enslabVendorId.registerVendorDataType(markerStatisticsReply);
        		
        /*
        OFBasicVendorDataType markerFeaturesReplyVendorData = new OFBasicVendorDataType(
        		OFMarkerReplyVendorData.ENSLAB_MARKER_FEATURES_REPLY,
        		OFMarkerReplyVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerFeaturesReplyVendorData);
        
        OFBasicVendorDataType markerStatsReplyVendorData = new OFBasicVendorDataType(
        		OFMarkerReplyVendorData.ENSLAB_MARKER_STATS_REPLY,
        		OFMarkerReplyVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerStatsReplyVendorData);
        
        OFBasicVendorDataType markerFeaturesRequestVendorData = new OFBasicVendorDataType(
        		OFMarkerRequestVendorData.ENSLAB_MARKER_FEATURES_REQUEST,
        		OFMarkerRequestVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerFeaturesRequestVendorData);
        
        OFBasicVendorDataType markerStatsRequestVendorData = new OFBasicVendorDataType(
        		OFMarkerRequestVendorData.ENSLAB_MARKER_STATS_REQUEST,
        		OFMarkerRequestVendorData.getInstantiable());
        enslabVendorId.registerVendorDataType(markerStatsRequestVendorData);
        */

        initialized = true;
    }
}
