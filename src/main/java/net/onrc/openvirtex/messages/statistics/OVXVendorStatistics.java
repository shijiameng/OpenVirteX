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
package net.onrc.openvirtex.messages.statistics;

import net.onrc.openvirtex.elements.datapath.OVXSwitch;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.messages.OVXStatisticsReply;
import net.onrc.openvirtex.messages.OVXStatisticsRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.openflow.protocol.statistics.OFStatistics;
import org.openflow.protocol.statistics.OFVendorStatistics;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.statistics.OFMarkerStatisticsReply;

public class OVXVendorStatistics extends OFVendorStatistics implements
        VirtualizableStatistic, DevirtualizableStatistic {

	static Logger log = LogManager.getLogger(OVXVendorStatistics.class.getName());
	
    @Override
    public void devirtualizeStatistic(final OVXSwitch sw,
            final OVXStatisticsRequest msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void virtualizeStatistic(final PhysicalSwitch sw,
            final OVXStatisticsReply msg) {
        // TODO Auto-generated method stub
    	
    	List<? extends OFStatistics> vStatList = msg.getStatistics();
        Map<Integer, OFMarkerStatisticsReply> statMap = new HashMap<Integer, OFMarkerStatisticsReply>();
        
    	for (OFStatistics stat : vStatList) {
    		OVXVendorStatistics vStat = (OVXVendorStatistics) stat;
    		ChannelBuffer buffer = ChannelBuffers.buffer(vStat.getLength());
    		
    		buffer.writeBytes(vStat.getVendorBody());
    		int dataType = buffer.readInt();
    		buffer.readInt();
    		
    		if (dataType == OFMarkerStatisticsReply.ENSLAB_MARKER_STATS_REPLY) {
    			while (buffer.readable()) {
		    		OFMarkerStatisticsReply reply = new OFMarkerStatisticsReply();	
		    		reply.readFrom(buffer);
		    		if (reply.getMarkerType() != OFMarkerType.ENSLAB_MARKER_SRTC) 
		    			break;
		    		synchronized (log) {
			    		log.info("DPID - {}", sw.getSwitchId());
			    		log.info(reply.toString());
			    		log.info(reply.getMarkerData().toString());
		    		}
		    		statMap.put(reply.getMarkerId(), reply);
    			}
    		}
    		
    		sw.setMarkerStatistics(statMap);
    	}

    }

}
