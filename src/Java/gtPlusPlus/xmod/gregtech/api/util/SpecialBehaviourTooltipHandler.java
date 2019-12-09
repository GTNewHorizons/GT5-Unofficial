package gtPlusPlus.xmod.gregtech.api.util;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class SpecialBehaviourTooltipHandler {

	private static final HashMap<ItemStack, String> mTooltipCache = new HashMap<ItemStack, String>();
	
	public static void addTooltipForItem(ItemStack aStack, String aTooltip) {
		mTooltipCache.put(aStack, aTooltip);
	}
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){		
		if (event != null) {
			if (event.itemStack != null) {				
				String s = mTooltipCache.get(event.itemStack);
				if (s != null && s.length() > 0) {
					event.toolTip.add(s);					
				}				
			}
		}		
	}
	
}
