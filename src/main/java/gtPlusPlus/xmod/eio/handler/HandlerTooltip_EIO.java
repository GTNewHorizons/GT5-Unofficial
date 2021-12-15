package gtPlusPlus.xmod.eio.handler;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.disableEnderIOIngotTooltips;

import java.lang.reflect.Field;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import gregtech.api.enums.Materials;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.eio.material.MaterialEIO;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HandlerTooltip_EIO {

	private static Item mIngot;
	Class oMainClass;
	Class oIngotClass;

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){
		//Is EIO loaded?
		if (!disableEnderIOIngotTooltips && LoadedMods.EnderIO){

			//Is the EIO Ingot Item null?
			//If it is, reflect in.
			if (mIngot == null){
				try {
					oMainClass = ReflectionUtils.getClass("crazypants.enderio.EnderIO");
					oIngotClass = ReflectionUtils.getClass("crazypants.enderio.material.ItemAlloy");
					if (oMainClass != null && oIngotClass != null){
						Field oAlloyField = ReflectionUtils.getField(oMainClass, "itemAlloy");
						Object oAlloy = oAlloyField.get(oMainClass);
						if (oAlloy != null){							
							if (oIngotClass.isInstance(oAlloy) || Item.class.isInstance(oAlloy)){
								mIngot = (Item) oAlloy;									
							}
						}
					}					
				}
				catch (Throwable e) {
				}
			}

			if (mIngot != null){
				//If the Item is an instance of ItemAlloy.class then proceed
				if (event.itemStack.getItem() == mIngot || oIngotClass.isInstance(event.itemStack.getItem()) || event.itemStack.getUnlocalizedName().toLowerCase().contains("item.itemAlloy")){

					//If stacks match, add a tooltip.						
					if (mIngot != null){
						if (event.itemStack.getItem() == mIngot){
							if (event.itemStack.getItemDamage() == 0){
								event.toolTip.add(MaterialEIO.ELECTRICAL_STEEL.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 1){
								event.toolTip.add(MaterialEIO.ENERGETIC_ALLOY.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 2){
								event.toolTip.add(MaterialEIO.VIBRANT_ALLOY.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 3){
								event.toolTip.add(MaterialEIO.REDSTONE_ALLOY.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 4){
								event.toolTip.add(MaterialEIO.CONDUCTIVE_IRON.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 5){
								event.toolTip.add(MaterialEIO.PULSATING_IRON.vChemicalFormula);										
							}
							else if (event.itemStack.getItemDamage() == 6){
								event.toolTip.add(Materials.DarkSteel.mChemicalFormula);									
							}
							else if (event.itemStack.getItemDamage() == 7){
								event.toolTip.add(MaterialEIO.SOULARIUM.vChemicalFormula);											
							}
						}
					}						
				}
			}
		}
	}
}
