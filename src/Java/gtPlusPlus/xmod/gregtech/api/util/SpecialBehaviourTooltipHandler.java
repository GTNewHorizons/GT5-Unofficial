package gtPlusPlus.xmod.gregtech.api.util;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
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
				for (ItemStack aKey : mTooltipCache.keySet()) {					
					if (GT_Utility.areStacksEqual(aKey, event.itemStack, false)) {
						String s = mTooltipCache.get(aKey);
						if (s != null && s.length() > 0) {
							event.toolTip.add(EnumChatFormatting.RED+s);					
						}
					}					
				}				
			}
		}		
	}
	
}
