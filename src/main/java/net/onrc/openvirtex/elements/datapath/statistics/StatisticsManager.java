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
package net.onrc.openvirtex.elements.datapath.statistics;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import net.onrc.openvirtex.core.OpenVirteXController;
import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.network.PhysicalNetwork;
import net.onrc.openvirtex.messages.OVXStatisticsRequest;
import net.onrc.openvirtex.messages.statistics.OVXFlowStatisticsRequest;
import net.onrc.openvirtex.messages.statistics.OVXPortStatisticsRequest;
import net.onrc.openvirtex.messages.statistics.OVXVendorStatistics;
import net.onrc.openvirtex.protocol.OVXMatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.openflow.protocol.OFMarker;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.Wildcards;
import org.openflow.protocol.statistics.OFStatisticsType;
import org.openflow.vendor.enslab.OFEnslabVendorData;
import org.openflow.vendor.enslab.statistics.OFMarkerStatisticsRequest;

public class StatisticsManager implements TimerTask, OVXSendMsg {

    private HashedWheelTimer timer = null;
    private PhysicalSwitch sw;

    Logger log = LogManager.getLogger(StatisticsManager.class.getName());

    private Integer refreshInterval = 5;
    private boolean stopTimer = false;
    
    // SJM NIaaS
//    private MarkerScheduler sch;
    // SJM NIaaS END

    public StatisticsManager(PhysicalSwitch sw) {
        /*
         * Get the timer from the PhysicalNetwork class.
         */
        this.timer = PhysicalNetwork.getTimer();
        this.sw = sw;
        this.refreshInterval = OpenVirteXController.getInstance()
                .getStatsRefresh();
//        this.sch = new MarkerScheduler(sw);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
//        log.debug("Collecting stats for {}", this.sw.getSwitchName());
    	log.info("Collecting stats for {}", this.sw.getSwitchName());
        sendPortStatistics();
        sendFlowStatistics(0, (short) 0);
        // SJM NIaaS: Collect marker statistics
        sendMarkerStatistics();
//        sch.scheduleNext();
        // SJM NIaaS END
        
        if (!this.stopTimer) {
            log.debug("Scheduling stats collection in {} seconds for {}",
                    this.refreshInterval, this.sw.getSwitchName());
            timeout.getTimer().newTimeout(this, refreshInterval,
                    TimeUnit.SECONDS);
        }
    }

    private void sendFlowStatistics(int tid, short port) {
        OVXStatisticsRequest req = new OVXStatisticsRequest();
        // TODO: stuff like below should be wrapped into an XIDUtil class
        int xid = (tid << 16) | port;
        req.setXid(xid);
        req.setStatisticType(OFStatisticsType.FLOW);
        OVXFlowStatisticsRequest freq = new OVXFlowStatisticsRequest();
        OVXMatch match = new OVXMatch();
        match.setWildcards(Wildcards.FULL);
        freq.setMatch(match);
        freq.setOutPort(OFPort.OFPP_NONE.getValue());
        freq.setTableId((byte) 0xFF);
        req.setStatistics(Collections.singletonList(freq));
        req.setLengthU(req.getLengthU() + freq.getLength());
        sendMsg(req, this);
    }

    private void sendPortStatistics() {
        OVXStatisticsRequest req = new OVXStatisticsRequest();
        req.setStatisticType(OFStatisticsType.PORT);
        OVXPortStatisticsRequest preq = new OVXPortStatisticsRequest();
        preq.setPortNumber(OFPort.OFPP_NONE.getValue());
        req.setStatistics(Collections.singletonList(preq));
        req.setLengthU(req.getLengthU() + preq.getLength());
        sendMsg(req, this);
    }
    
    // SJM NIaaS: Send marker statistics request
    private void sendMarkerStatistics() {
    	OVXStatisticsRequest req = new OVXStatisticsRequest();
    	req.setStatisticType(OFStatisticsType.VENDOR);
    	
    	OVXVendorStatistics vreq = new OVXVendorStatistics();
    	vreq.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
    	
    	OFMarkerStatisticsRequest mreq = new OFMarkerStatisticsRequest();
    	mreq.setMarkerId(OFMarker.OFPM_ALL.getValue());
    	
    	vreq.setVendorBody(OFMarkerStatisticsRequest.ENSLAB_MARKER_STATS_REQUEST, mreq);
    	vreq.setLength(4 + 8 + mreq.getLength());
    	
    	req.setStatistics(Collections.singletonList(vreq));
    	req.setLengthU(req.getLengthU() + vreq.getLength());
    	
    	sendMsg(req, this);
    }
    
    public int getInterval() {
    	return this.refreshInterval;
    }
    // SJM NIaaS END

    public void start() {

        /*
         * Initially start polling quickly. Then drop down to configured value
         */
        log.info("Starting Stats collection thread for {}",
                this.sw.getSwitchName());
        timer.newTimeout(this, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        log.info("Stopping Stats collection thread for {}",
                this.sw.getSwitchName());
        this.stopTimer = true;
    }

    @Override
    public void sendMsg(OFMessage msg, OVXSendMsg from) {
        sw.sendMsg(msg, from);
    }

    @Override
    public String getName() {
        return "Statistics Manager (" + sw.getName() + ")";
    }

    public void cleanUpTenant(Integer tenantId, short port) {
        sendFlowStatistics(tenantId, port);
    }

}
