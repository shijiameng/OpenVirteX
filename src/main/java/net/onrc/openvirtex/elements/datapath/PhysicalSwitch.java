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
package net.onrc.openvirtex.elements.datapath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.scheduler.MarkerScheduler;
import net.onrc.openvirtex.elements.datapath.statistics.StatisticsManager;
import net.onrc.openvirtex.elements.marker.Marker;
import net.onrc.openvirtex.elements.marker.SrtcMarker;
import net.onrc.openvirtex.elements.network.PhysicalNetwork;
import net.onrc.openvirtex.elements.port.PhysicalPort;
import net.onrc.openvirtex.exceptions.SwitchMappingException;
import net.onrc.openvirtex.messages.OVXFlowMod;
import net.onrc.openvirtex.messages.OVXStatisticsReply;
import net.onrc.openvirtex.messages.Virtualizable;
import net.onrc.openvirtex.messages.statistics.OVXFlowStatisticsReply;
import net.onrc.openvirtex.messages.statistics.OVXPortStatisticsReply;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.openflow.protocol.OFMarker;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPhysicalPort;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.OFVendor;
import org.openflow.protocol.statistics.OFStatistics;
import org.openflow.vendor.enslab.OFMarkerType;
import org.openflow.vendor.enslab.statistics.OFMarkerStatisticsReply;
import org.openflow.vendor.enslab.statistics.OFSrtcmStatistics;

/**
 * The Class PhysicalSwitch.
 */
public class PhysicalSwitch extends Switch<PhysicalPort> {

    private static Logger log = LogManager.getLogger(PhysicalSwitch.class.getName());
    // The Xid mapper
    private final XidTranslator<OVXSwitch> translator;
    private StatisticsManager statsMan = null;
    // SJM NIaaS
    private MarkerScheduler sch = null;
    // SJM NIaaS END
    private AtomicReference<Map<Short, OVXPortStatisticsReply>> portStats;
    private AtomicReference<Map<Integer, List<OVXFlowStatisticsReply>>> flowStats;
    // SJM NIaaS
    private Map<Integer, Marker> markerMap;
    private AtomicReference<Map<Integer, OFMarkerStatisticsReply>> markerStats;
    // SJM NIaaS END
    /**
     * Unregisters OVXSwitches and associated virtual elements mapped to this
     * PhysicalSwitch. Called by unregister() when the PhysicalSwitch is torn
     * down.
     */
    class DeregAction implements Runnable {

        private PhysicalSwitch psw;
        private int tid;

        DeregAction(PhysicalSwitch s, int t) {
            this.psw = s;
            this.tid = t;
        }

        @Override
        public void run() {
            OVXSwitch vsw;
            try {
                if (psw.map.hasVirtualSwitch(psw, tid)) {
                    vsw = psw.map.getVirtualSwitch(psw, tid);
                    /* save = don't destroy the switch, it can be saved */
                    boolean save = false;
                    if (vsw instanceof OVXBigSwitch) {
                        save = ((OVXBigSwitch) vsw).tryRecovery(psw);
                    }
                    if (!save) {
                        vsw.unregister();
                    }
                }
            } catch (SwitchMappingException e) {
                log.warn("Inconsistency in OVXMap: {}", e.getMessage());
            }
        }
    }

    /**
     * Instantiates a new physical switch.
     *
     * @param switchId
     *            the switch id
     */
    public PhysicalSwitch(final long switchId) {
        super(switchId);
        this.translator = new XidTranslator<OVXSwitch>();
        this.portStats = new AtomicReference<Map<Short, OVXPortStatisticsReply>>();
        this.flowStats = new AtomicReference<Map<Integer, List<OVXFlowStatisticsReply>>>();
        this.statsMan = new StatisticsManager(this);
        // SJM NIaaS
        this.sch = new MarkerScheduler(this);
        this.markerMap = new HashMap<Integer, Marker>();
        this.markerStats = new AtomicReference<Map<Integer, OFMarkerStatisticsReply>>();
       
        SrtcMarker marker = new SrtcMarker(OFMarker.OFPM_GLOBAL.getValue(), this, null);
        marker.setCommittedBurstSize(0);
        marker.setCommittedInfoRate(0);
        marker.setExceedBurstSize(0);
        this.addMarker(marker);
        // SJM NIaaS END
    }

    // SJM NIaaS    
    private void updateAllMarkersWeight() {
    	List<Marker> markers = this.getAllMarkers();
    	int totalWeight = 0;

    	for (Marker marker : markers) {
    		if (marker.getMarkerId() == OFMarker.OFPM_GLOBAL.getValue())
    			continue;
    		if (marker.getTypeOfService() != null) {
    			totalWeight += marker.getTypeOfService().getWeight();
    		} else {
    			log.error("Service type of marker {} does not determined", marker.getMarkerId());
    		}
    	}
    	
    	for (Marker marker : markers) {
    		if (marker.getMarkerId() == OFMarker.OFPM_GLOBAL.getValue())
    			continue;
	    	if (totalWeight != 0 && marker.getTypeOfService() != null) {
	    		double w = (double) marker.getTypeOfService().getWeight() / (double) totalWeight;
				marker.setWeight(w);
				log.info("Updated weight of marker {} as {}", marker.getMarkerId(), w);
			}
    	}
    }
    
    private void updateGlobalMarker() {
    	SrtcMarker globalMarker = (SrtcMarker) this.getMarker(OFMarker.OFPM_GLOBAL.getValue());
    	
    	if (globalMarker != null) {
	    	List<Marker> markers = this.getAllMarkers();
	    	long totalCBS = 0, totalEBS = 0;
	    	for (Marker marker : markers) {
	    		if (marker.getMarkerId() == OFMarker.OFPM_GLOBAL.getValue())
	    			continue;
	    		totalCBS += ((SrtcMarker) marker).getCommittedBurstSize();
	    		totalEBS += ((SrtcMarker) marker).getExceedBurstSize();
	    	}
	    	
	    	if (markers.size() - 1 > 0) {
	    		globalMarker.setCommittedBurstSize(totalCBS / (markers.size() - 1));
	    		globalMarker.setExceedBurstSize(totalEBS / (markers.size() - 1));
	    	} else {
	    		globalMarker.setCommittedBurstSize(0);
	    		globalMarker.setExceedBurstSize(0);
	    	}
	    	
	    	log.info("Parameters of global marker have been updated: CBS={}, EBS={}", 
	    			globalMarker.getCommittedBurstSize(), globalMarker.getExceedBurstSize());
    	} else {
    		log.error("Global Marker does not exist!");
    	}
    }
    
    public void addMarker(Marker marker) {
    	this.markerMap.put(marker.getMarkerId(), marker);
    	if (marker.getMarkerId() != OFMarker.OFPM_GLOBAL.getValue()) {
	    	this.updateAllMarkersWeight();
	    	this.updateGlobalMarker();
    	}
    }
    
    public void removeMarker(Marker marker) {
    	this.markerMap.remove(marker.getMarkerId());
    	if (marker.getMarkerId() != OFMarker.OFPM_GLOBAL.getValue()) {
	    	this.updateAllMarkersWeight();
	    	this.updateGlobalMarker();
    	}
    }
    
    public Marker getMarker(Integer markerId) {
    	return this.markerMap.get(markerId);
    }
    
    public List<Marker> getAllMarkers() {
    	return new ArrayList<Marker>(this.markerMap.values());
    }
    // SJM NIaaS END
    
    /**
     * Gets the OVX port number.
     *
     * @param physicalPortNumber the physical port number
     * @param tenantId the tenant id
     * @param vLinkId the virtual link ID
     * @return the virtual port number
     */
    public Short getOVXPortNumber(final Short physicalPortNumber,
            final Integer tenantId, final Integer vLinkId) {
        return this.portMap.get(physicalPortNumber)
                .getOVXPort(tenantId, vLinkId).getPortNumber();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.onrc.openvirtex.elements.datapath.Switch#handleIO(org.openflow.protocol
     * .OFMessage)
     */
    @Override
    public void handleIO(final OFMessage msg, Channel channel) {
        try {
            ((Virtualizable) msg).virtualize(this);
        } catch (final ClassCastException e) {
            PhysicalSwitch.log.error("Received illegal message : " + msg);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see net.onrc.openvirtex.elements.datapath.Switch#tearDown()
     */
    @Override
    public void tearDown() {
        PhysicalSwitch.log.info("Switch disconnected {} ",
                this.featuresReply.getDatapathId());
        this.statsMan.stop();
        this.channel.disconnect();
        this.map.removePhysicalSwitch(this);
    }

    /**
     * Fill port map. Assume all ports are edges until discovery says otherwise.
     */
    protected void fillPortMap() {
        for (final OFPhysicalPort port : this.featuresReply.getPorts()) {
            final PhysicalPort physicalPort = new PhysicalPort(port, this, true);
            this.addPort(physicalPort);
        }
    }

    @Override
    public boolean addPort(final PhysicalPort port) {
        final boolean result = super.addPort(port);
        if (result) {
            PhysicalNetwork.getInstance().addPort(port);
        }
        return result;
    }

    /**
     * Removes the specified port from this PhysicalSwitch. This includes
     * removal from the switch's port map, topology discovery, and the
     * PhysicalNetwork topology.
     *
     * @param port the physical port instance
     * @return true if successful, false otherwise
     */
    public boolean removePort(final PhysicalPort port) {
        final boolean result = super.removePort(port.getPortNumber());
        if (result) {
            PhysicalNetwork pnet = PhysicalNetwork.getInstance();
            pnet.removePort(pnet.getDiscoveryManager(this.getSwitchId()), port);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.onrc.openvirtex.elements.datapath.Switch#init()
     */
    @Override
    public boolean boot() {
        PhysicalSwitch.log.info("Switch connected with dpid {}, name {} and type {}",
                this.featuresReply.getDatapathId(), this.getSwitchName(),
                this.desc.getHardwareDescription());
        
        // SJM NIaaS: Add a global marker when booting this switch
//        OVXVendor vendor = new OVXVendor();
//        OFMarkerAddVendorData vendorData = new OFMarkerAddVendorData();
//        OFSrtcmFeatures srtcmFeatures = new OFSrtcmFeatures();
        
//        srtcmFeatures.setCIR(0);
//        srtcmFeatures.setCBS(0);
//        srtcmFeatures.setEBS(0);
//        srtcmFeatures.setCBorrowSuccessProb(OFEnslabVendorData.OFPM_BRW_SUCC_NA);
//        srtcmFeatures.setEBorrowSuccessProb(OFEnslabVendorData.OFPM_BRW_SUCC_NA);
//        
//        vendorData.setMarkerType(OFMarkerType.ENSLAB_MARKER_SRTC);
//        vendorData.setMarkerId(OFMarker.OFPM_GLOBAL.getValue());
//        vendorData.setMarkerData(srtcmFeatures);
//        
//        vendor.setVendor(OFEnslabVendorData.ENSLAB_VENDOR_ID);
//        vendor.setVendorData(vendorData);
//        vendor.setLengthU(OVXVendor.MINIMUM_LENGTH + vendorData.getLength());
//        this.sendMsg(vendor, this);
        Marker globalMarker = this.markerMap.get(OFMarker.OFPM_GLOBAL.getValue());
        if (globalMarker != null) {
        	globalMarker.boot();
        }
        // SJM NIaaS END
        
        PhysicalNetwork.getInstance().addSwitch(this);
        this.fillPortMap();
        this.statsMan.start();
        
        return true;
    }
    
    /**
     * Removes this PhysicalSwitch from the network. Also removes associated
     * ports, links, and virtual elements mapped to it (OVX*Switch, etc.).
     */
    @Override
    public void unregister() {
        /* tear down OVXSingleSwitches mapped to this PhysialSwitch */
        for (Integer tid : this.map.listVirtualNetworks().keySet()) {
            DeregAction dereg = new DeregAction(this, tid);
            new Thread(dereg).start();
        }
        /* try to remove from network and disconnect */
        PhysicalNetwork.getInstance().removeSwitch(this);
        this.portMap.clear();
        this.tearDown();
    }

    @Override
    public void sendMsg(final OFMessage msg, final OVXSendMsg from) {
        if ((this.channel.isOpen()) && (this.isConnected)) {
            this.channel.write(Collections.singletonList(msg));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see net.onrc.openvirtex.elements.datapath.Switch#toString()
     */
    @Override
    public String toString() {
        return "DPID : "
                + this.switchId
                + ", remoteAddr : "
                + ((this.channel == null) ? "None" : this.channel
                        .getRemoteAddress().toString());
    }

    /**
     * Gets the port.
     *
     * @param portNumber
     *            the port number
     * @return the port instance
     */
    @Override
    public PhysicalPort getPort(final Short portNumber) {
        return this.portMap.get(portNumber);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof PhysicalSwitch) {
            return this.switchId == ((PhysicalSwitch) other).switchId;
        }

        return false;
    }

    public int translate(final OFMessage ofm, final OVXSwitch sw) {
        return this.translator.translate(ofm.getXid(), sw);
    }

    public XidPair<OVXSwitch> untranslate(final OFMessage ofm) {
        final XidPair<OVXSwitch> pair = this.translator.untranslate(ofm
                .getXid());
        if (pair == null) {
            return null;
        }
        return pair;
    }

    public void setPortStatistics(Map<Short, OVXPortStatisticsReply> stats) {
        this.portStats.set(stats);
    }

    public void setFlowStatistics(
            Map<Integer, List<OVXFlowStatisticsReply>> stats) {
        this.flowStats.set(stats);

    }
    
    // SJM NIaaS
    public void setMarkerStatistics(Map<Integer, OFMarkerStatisticsReply> stats) {
    	Map<Integer, OFMarkerStatisticsReply> oldStats = this.markerStats.get();
    	for (Integer markerId : stats.keySet()) {
    		if (markerId == OFMarker.OFPM_GLOBAL.getValue()) 
    			continue;
    		
    		OFMarkerStatisticsReply newMarkerStatsReply = stats.get(markerId);
    		OFMarkerStatisticsReply oldMarkerStatsReply = oldStats.get(markerId);
    		Marker marker = this.getMarker(markerId);
    		
    		if (newMarkerStatsReply.getMarkerType() == OFMarkerType.ENSLAB_MARKER_SRTC) {
    			OFSrtcmStatistics newMarkerStats = (OFSrtcmStatistics) newMarkerStatsReply.getMarkerData();
    			long dataRate = 0;
    			if (oldMarkerStatsReply != null) {
	    			OFSrtcmStatistics oldMarkerStats = (OFSrtcmStatistics) oldMarkerStatsReply.getMarkerData();
	    			dataRate = (newMarkerStats.getNumberOfBytes() - oldMarkerStats.getNumberOfBytes()) * 8 / this.statsMan.getInterval();
	    			marker.setMeteredDataRate((int) dataRate);
    			} else {
    				dataRate = newMarkerStats.getNumberOfBytes() * 8 / this.statsMan.getInterval();
    				marker.setMeteredDataRate((int) dataRate);
    			}
    		}
    	}
    	this.markerStats.set(stats);
    	sch.scheduleNext();
    }
    
    public OFMarkerStatisticsReply getMarkerStatistics(Integer markerId) {
    	return this.markerStats.get().get(markerId);
    }
    // SJM NIaaS END

    public List<OVXFlowStatisticsReply> getFlowStats(int tid) {
        Map<Integer, List<OVXFlowStatisticsReply>> stats = this.flowStats.get();
        if (stats != null && stats.containsKey(tid)) {
            return Collections.unmodifiableList(stats.get(tid));
        }
        return null;
    }

    public OVXPortStatisticsReply getPortStat(short portNumber) {
        Map<Short, OVXPortStatisticsReply> stats = this.portStats.get();
        if (stats != null) {
            return stats.get(portNumber);
        }
        return null;
    }

    public void cleanUpTenant(Integer tenantId, Short port) {
        this.statsMan.cleanUpTenant(tenantId, port);
    }

    public void removeFlowMods(OVXStatisticsReply msg) {
        int tid = msg.getXid() >> 16;
        short port = (short) (msg.getXid() & 0xFFFF);
        for (OFStatistics stat : msg.getStatistics()) {
            OVXFlowStatisticsReply reply = (OVXFlowStatisticsReply) stat;
            if (tid != this.getTidFromCookie(reply.getCookie())) {
                continue;
            }
            if (port != 0) {
                sendDeleteFlowMod(reply, port);
                if (reply.getMatch().getInputPort() == port) {
                    sendDeleteFlowMod(reply, OFPort.OFPP_NONE.getValue());
                }
            } else {
                sendDeleteFlowMod(reply, OFPort.OFPP_NONE.getValue());
            }
        }
    }

    private void sendDeleteFlowMod(OVXFlowStatisticsReply reply, short port) {
        OVXFlowMod dFm = new OVXFlowMod();
        dFm.setCommand(OVXFlowMod.OFPFC_DELETE_STRICT);
        dFm.setMatch(reply.getMatch());
        dFm.setOutPort(port);
        dFm.setLengthU(OVXFlowMod.MINIMUM_LENGTH);
        this.sendMsg(dFm, this);
    }

    private int getTidFromCookie(long cookie) {
        return (int) (cookie >> 32);
    }

    @Override
    public void handleRoleIO(OFVendor msg, Channel channel) {
        log.warn(
                "Received Role message {} from switch {}, but no role was requested",
                msg, this.switchName);
    }

    @Override
    public void removeChannel(Channel channel) {

    }

}
