package net.onrc.openvirtex.messages.actions;

import java.util.List;

import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionMark;

import net.onrc.openvirtex.elements.datapath.OVXSwitch;
import net.onrc.openvirtex.exceptions.ActionVirtualizationDenied;
import net.onrc.openvirtex.exceptions.DroppedMessageException;
import net.onrc.openvirtex.protocol.OVXMatch;

public class OVXActionMark extends OFActionMark implements VirtualizableAction {

	@Override
	public void virtualize(OVXSwitch sw, List<OFAction> approvedActions, OVXMatch match)
			throws ActionVirtualizationDenied, DroppedMessageException {
		approvedActions.add(this);
	}
}
