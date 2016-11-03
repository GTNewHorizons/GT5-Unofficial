package pers.gwyog.gtneioreplugin.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GTSmallOreHelper;

public class ClientTickHandler {
	public boolean hasInitialized = false;
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if (!hasInitialized) {
			new GTOreLayerHelper();
			new GTSmallOreHelper();
			hasInitialized = true;
		}
	}
}
