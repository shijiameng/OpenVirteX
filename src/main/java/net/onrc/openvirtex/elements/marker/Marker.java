package net.onrc.openvirtex.elements.marker;

import java.util.Map;

import org.openflow.vendor.enslab.OFMarkerType;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.Persistable;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.network.TypeOfService;

public abstract class Marker implements Persistable, OVXSendMsg {
	
	public static final String DB_KEY = "markers";
	
	protected int markerId;
	protected OFMarkerType type;
	
	protected TypeOfService toS;
	protected int currentDataRate;
	protected double weight;
		
	protected PhysicalSwitch parentSwitch;
	protected boolean isBooted;
	
	
	protected Marker() {
		isBooted = false;
	}
	
	protected Marker(final int markerId, PhysicalSwitch parentSwitch) {
		setMarkerId(markerId);
		setParentSwitch(parentSwitch);
		isBooted = false;
	}
	
	public void setMarkerId(final int markerId) {
		this.markerId = markerId;
	}
	
	public void setParentSwitch(PhysicalSwitch parentSwitch) {
		this.parentSwitch = parentSwitch;
		parentSwitch.addMarker(this);
	}
	
	public void setMarkerType(final OFMarkerType type) {
		this.type = type;
	}
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	public OFMarkerType getMarkerType() {
		return this.type;
	}
	
	public PhysicalSwitch getSwitch() {
		return this.parentSwitch;
	}
	
	public void setCurrentDataRate(final int currentDataRate) {
		this.currentDataRate = currentDataRate;
	}
	
	public int getCurrentDataRate() {
		return this.currentDataRate;
	}
	
	public void setTypeOfService(final TypeOfService toS) {
		this.toS = toS;
	}
	
	public TypeOfService getTypeOfService() {
		return this.toS;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public void setWeight(final double weight) {
		this.weight = weight;
	}
	
	public boolean isBooted() {
		return isBooted;
	}
	
	public abstract void boot();
	
	public abstract void tearDown();

	@Override
	public Map<String, Object> getDBIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDBKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDBName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getDBObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
