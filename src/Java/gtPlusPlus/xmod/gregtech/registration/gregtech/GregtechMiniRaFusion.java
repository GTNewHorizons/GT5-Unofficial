package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.util.Collection;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_BasicWasher;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_CompactFusionReactor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMiniRaFusion {

	public static void run() {
		//if (CORE.ConfigSwitches.enableMachine_SimpleWasher){
			generateSlowFusionRecipes();
			// Register the Simple Fusion Entity.
			GregtechItemList.Miniature_Fusion
			.set(new GregtechMetaTileEntity_CompactFusionReactor(993, "simplefusion.tier.00", "Ra, Sun God - Mk I", 6)
					.getStackForm(1L));
		//}
	}

	private static boolean generateSlowFusionRecipes(){
		int mRecipeCount = 0;		
		GT_Recipe_Map r = GT_Recipe.GT_Recipe_Map.sFusionRecipes;		
			final Collection<GT_Recipe> x = r.mRecipeList;
			Logger.INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug.");
			for (final GT_Recipe newBo : x) {				
				if (Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes.addRecipe(
						true, 
						newBo.mInputs,
						newBo.mOutputs, 
						newBo.mSpecialItems,
						newBo.mFluidInputs.clone(), //Fluid In
						newBo.mFluidOutputs.clone(), //Fluid Out
						newBo.mDuration*4, //Duration
						newBo.mEUt, //Eu
						newBo.mSpecialValue //Special 
						) != null){
					mRecipeCount++;					
				}
			}
		
		
		
		if (Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes.mRecipeList.size() > mRecipeCount){
			return true;
		}
		return false;
	}
}
