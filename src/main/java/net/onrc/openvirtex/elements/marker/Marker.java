package net.onrc.openvirtex.elements.marker;

import java.util.Map;

import net.onrc.openvirtex.core.io.OVXSendMsg;
import net.onrc.openvirtex.elements.Persistable;
import net.onrc.openvirtex.elements.datapath.PhysicalSwitch;

public abstract class Marker implements Persistable, OVXSendMsg {
	
	public static final String DB_KEY = "markers";
	
	protected int markerId;
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
	
	public int getMarkerId() {
		return this.markerId;
	}
	
	public PhysicalSwitch getSwitch() {
		return this.parentSwitch;
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
