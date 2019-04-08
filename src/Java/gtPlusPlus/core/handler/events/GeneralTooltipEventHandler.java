package gtPlusPlus.core.handler.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.ItemList;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class GeneralTooltipEventHandler {

	ItemStack[] mGregtechTurbines = new ItemStack[6];
	String mTurbine;
	String mExtra;

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){

		
		if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED || GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.SERVER_START) {
			return;
		}
		if (event.itemStack == null) {
			return;
		}
		if (CORE.ConfigSwitches.chanceToDropFluoriteOre > 0) {
			if (BlockEventHandler.blockLimestone != null && !BlockEventHandler.blockLimestone.isEmpty()) {
				for (ItemStack h : BlockEventHandler.blockLimestone) {
					if (h != null && Block.getBlockFromItem(h.getItem()) == Block.getBlockFromItem(event.itemStack.getItem())) {
						if (ItemUtils.getModId(h) != null && !ItemUtils.getModId(h).toLowerCase().contains("biomesoplenty")) {
							event.toolTip.add("May contain Fluorite Ore");
						}
					}
				}
			}
			if (BlockEventHandler.oreLimestone != null && !BlockEventHandler.oreLimestone.isEmpty()) {
				for (ItemStack h : BlockEventHandler.oreLimestone) {
					if (h != null && Block.getBlockFromItem(h.getItem()) == Block.getBlockFromItem(event.itemStack.getItem())) {
						if (ItemUtils.getModId(h) != null && !ItemUtils.getModId(h).toLowerCase().contains("biomesoplenty")) {
							event.toolTip.add("May contain Fluorite Ore");
						}
					}
				}
			}
		}

		//Material Collector Tooltips		
		if (ModBlocks.blockPooCollector != null && Block.getBlockFromItem(event.itemStack.getItem()) == ModBlocks.blockPooCollector) {			
			//Normal
			if (event.itemStack.getItemDamage() == 0) {
				event.toolTip.add("Used to collect animal waste");
				event.toolTip.add("Collects in a 5x4x5 area starting at Y+1");
				event.toolTip.add("Use Hoppers/Pipes to empty");
				event.toolTip.add(EnumChatFormatting.GOLD+"Capacity: "+EnumChatFormatting.AQUA+"8000L");
			}
			//Advanced
			else {
				event.toolTip.add("Used to collect waste (Works on more than animals)");
				event.toolTip.add("Significantly faster than the simple version");
				event.toolTip.add("Collects in a 5x4x5 area starting at Y+1");
				event.toolTip.add("Use Hoppers/Pipes to empty");
				event.toolTip.add(EnumChatFormatting.GOLD+"Capacity: "+EnumChatFormatting.AQUA+"128000L");
			}
		}
		
		

		if (CORE.ConfigSwitches.enableAnimatedTurbines) {
			boolean shift = false;					
			try {

				if (KeyboardUtils.isShiftKeyDown()) {
					shift = true;
					mTurbine = "Animated Turbines can be disabled in the GT++ config";
				}
				else {
					mTurbine = EnumChatFormatting.ITALIC+"<Hold Shift>"+EnumChatFormatting.RESET;
				}
				for (int t=0;t<6;t++) {
					if (mGregtechTurbines[t] != null) {
						if (ItemStack.areItemStacksEqual(event.itemStack, mGregtechTurbines[t])){
							event.toolTip.add(mTurbine);	
							if (shift) {
								if (mExtra == null) {
									mExtra = CORE.GT_Tooltip;
								}	
								event.toolTip.add(mExtra);
							}
						}
					}
					else {
						mGregtechTurbines[t] = (t == 0 ? ItemList.Generator_Steam_Turbine_LV.get(1) : (t == 1 ? ItemList.Generator_Steam_Turbine_MV.get(1) : (t == 2 ? ItemList.Generator_Steam_Turbine_HV.get(1) : (t == 3 ? ItemList.Generator_Gas_Turbine_LV.get(1) : (t == 4 ? ItemList.Generator_Gas_Turbine_MV.get(1) : (ItemList.Generator_Gas_Turbine_HV.get(1)))))));
					}
				}
			}
			catch (Throwable t) {}
		}

	}

}
