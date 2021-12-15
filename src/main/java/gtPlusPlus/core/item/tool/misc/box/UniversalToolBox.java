package gtPlusPlus.core.item.tool.misc.box;

import gtPlusPlus.core.handler.GuiHandler;

public class UniversalToolBox extends BaseBoxItem {

	public final static int SLOTS = 16;
	
	public UniversalToolBox(String displayName) {
		super(displayName, new String[] {"Can store tools from Gregtech, IC2, BC, Forestry", "Please ask for additional mod support on Github"}, GuiHandler.GUI10);
	}
	
}
