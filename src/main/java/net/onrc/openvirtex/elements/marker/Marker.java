package net.onrc.openvirtex.elements.marker;

import org.openflow.vendor.enslab.OFMarkerType;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;
import net.onrc.openvirtex.elements.network.TypeOfService;

public abstract class Marker implements OVXSendMsg {
	
	public static final String DB_KEY = "markers";
	
	protected int markerId;
	protected OFMarkerType type;
	
	protected TypeOfService toS;
	protected int meteredDataRate;
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
	
	public void setMeteredDataRate(final int dataRate) {
		this.meteredDataRate = dataRate;
	}
	
	public int getMeteredDataRate() {
		return this.meteredDataRate;
	}
	
	public void setTypeOfService(final TypeOfService toS) {
		this.toS = toS;
	}
	
	public void setTypeOfService(final byte value) {
		this.toS = TypeOfService.valueOf(value);
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
	
}
