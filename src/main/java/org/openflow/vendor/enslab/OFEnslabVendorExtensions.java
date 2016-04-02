package org.openflow.vendor.enslab;

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


        initialized = true;
    }
}
