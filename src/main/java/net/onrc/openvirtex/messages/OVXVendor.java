/*******************************************************************************
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.onrc.openvirtex.messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openflow.protocol.OFVendor;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.OFMarkerReplyVendorData;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.OFSrtcmStatsReply;

import net.onrc.openvirtex.elements.datapath.OVXSwitch;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;

public class OVXVendor extends OFVendor implements Virtualizable,
        Devirtualizable {
	
	static Logger log = LogManager.getLogger(OVXVendor.class.getName());
	
    @Override
    public void devirtualize(final OVXSwitch sw) {
        OVXMessageUtil.translateXidAndSend(this, sw);
    }

    @Override
    public void virtualize(final PhysicalSwitch sw) {
        //Commented by SJM: OVXMessageUtil.untranslateXidAndSend(this, sw);
        // SJM NIaaS
        if (this.getVendor() == OFEnslabVendorData.ENSLAB_VENDOR_ID) {
	        OFEnslabVendorData vendorData = (OFEnslabVendorData) this.getVendorData();
        	switch (vendorData.getDataType()) {
        	case OFEnslabVendorData.ENSLAB_MARKER_ADD:
        	case OFEnslabVendorData.ENSLAB_MARKER_REMOVE:
        	case OFEnslabVendorData.ENSLAB_MARKER_FEATURES_REQUEST:
        	case OFEnslabVendorData.ENSLAB_MARKER_STATS_REQUEST:
        		// here should not be reached forever
        		OVXVendor.log.fatal("Received invalid message - {}", vendorData.getDataType());
        		break;
        		
        	case OFEnslabVendorData.ENSLAB_MARKER_FEATURES_REPLY:
        		break;
        		
        	case OFEnslabVendorData.ENSLAB_MARKER_STATS_REPLY:
        		OFMarkerReplyVendorData statsReply = (OFMarkerReplyVendorData) vendorData;
	        	if (statsReply.getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
	        		OFSrtcmStatsReply srtcmStatsReply = (OFSrtcmStatsReply) statsReply.getReply();
	        		OVXVendor.log.info(srtcmStatsReply.toString());
	        	}
        		break;
        		
	        default:
	        	break;
	        }
        } else {
        	OVXMessageUtil.untranslateXidAndSend(this, sw);
        }
        // SJM NIaaS END
    }

}
