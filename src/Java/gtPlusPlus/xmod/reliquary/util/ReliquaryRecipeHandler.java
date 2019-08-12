package gtPlusPlus.xmod.reliquary.util;

import static gtPlusPlus.core.lib.CORE.GTNH;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.reliquary.item.ReliquaryItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ReliquaryRecipeHandler {

	public static boolean removeAlkahestRecipe(ItemStack aOutput) {
		return false;
	}
	
	public static boolean addAlkahestRecipe(ItemStack[] aInputs, ItemStack aOutput) {
		if (aInputs == null || aInputs.length <= 0 || aInputs.length > 9) {
			return false;
		}
		return false;
	}
	
	public static void gregifyDefaultRecipes() {
		Logger.INFO("Gregifying Reliquary recipes.");
		if (GT_ModHandler.removeRecipeByOutput(ReliquaryItems.glowingWater())) {
			Logger.INFO("Removing recipe for Glowing Water.");			
		}
		if (GT_ModHandler.removeRecipeByOutput(ReliquaryItems.emptyVoidTear())) {
			Logger.INFO("Removing recipe for Empty Void Tears.");			
		}
		if (GT_ModHandler.removeRecipeByOutput(ReliquaryItems.emperorChalice())) {
			Logger.INFO("Removing recipe for the Emperor Chalice.");			
		}
		if (GT_ModHandler.removeRecipeByOutput(ReliquaryItems.infernalChalice())) {
			Logger.INFO("Removing recipe for the Infernal Chalice.");			
		}
		Logger.INFO("Finished removing original recipes.");
		
		// Add new recipes
		if (GT_Values.RA.addChemicalRecipe(ALLOY.ENERGYCRYSTAL.getDust(4), ReliquaryItems.emptyVial(), FluidUtils.getHotWater(2000), null, ReliquaryItems.glowingWater(), 20 * 30, 500)) {
			Logger.INFO("Added new recipe for Glowing Water.");
		}
		if (CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {ItemUtils.simpleMetaStack(Items.ender_eye, 0, 32), CI.getFieldGenerator(5, GTNH ? 8 : 4), CI.getTieredComponent(OrePrefixes.plate, 5, GTNH ? 16 : 8), CI.getEmitter(3, 20)}, FluidUtils.getUUM(50), ReliquaryItems.emptyVoidTear(), 20 * 2000, 2000)){
			Logger.INFO("Added new recipe for Empty Void Tears.");
		}	
		Logger.INFO("Finished adding new recipes.");
		
	}
}
