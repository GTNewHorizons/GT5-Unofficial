package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.GregTech_API;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechMiniRaFusion {

	public static void run() {
		// Register the Simple Fusion Entity.
		GregtechItemList.Miniature_Fusion.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(994, "machine.simplefusion.tier.01", "Mimir", 8, "Universal Machine for Knowledge and Wisdom", Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes, 2, 9, 64000, 0, 1, "Dehydrator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.SENSOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.FIELD_GENERATOR}).getStackForm(1L));

	}
	
	public static boolean generateSlowFusionrecipes() {		
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList){
			if (x.mEnabled) {
				GT_Recipe y = x.copy();
				y.mDuration *= 4;
				Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes.add(y);
			}
		}
		int mRecipeCount = Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes.mRecipeList.size();
		if (mRecipeCount > 0) {
			Logger.INFO("[Pocket Fusion] Generated "+mRecipeCount+" recipes for the Pocket Fusion Reactor.");
			return true;
		}
		return false;
	}
	
	
}
