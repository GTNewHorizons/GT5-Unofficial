package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Plasma;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_MiniFusionPlant;

public class GregtechMiniRaFusion {

	public static void run() {
		// Register the Simple Fusion Entity.
		GregtechItemList.Miniature_Fusion.set(new GregtechMTE_MiniFusionPlant(31015, "gtplusplus.fusion.single", "Helium Prime").getStackForm(1L));
		GregtechItemList.Plasma_Tank.set(new GT_MetaTileEntity_Hatch_Plasma(31016, "gtplusplus.tank.plasma", "Plasma Tank").getStackForm(1L));
		
	}
	
	public static boolean generateSlowFusionrecipes() {		
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList){
			if (x.mEnabled) {
				GT_Recipe y = x.copy();
				y.mDuration *= 16;				
				long z = y.mEUt * 4;
				if (z > Integer.MAX_VALUE) {
					y.mEnabled = false;
					continue;
				}
				y.mEUt = (int) Math.min(Math.max(0, z), Integer.MAX_VALUE);
				y.mCanBeBuffered = true;
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
