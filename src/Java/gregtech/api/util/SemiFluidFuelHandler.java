package gregtech.api.util;

import gregtech.api.util.Recipe_GT.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraftforge.fluids.FluidStack;

public class SemiFluidFuelHandler {

	public static boolean generateFuels() {
		final FluidStack aCreosote = FluidUtils.getFluidStack("creosote", 1000);
		for (GT_Recipe g : gregtech.api.util.GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.mRecipeList) {
			if (g != null && g.mEnabled && g.mFluidInputs[0] != null) {		
				boolean aContainsCreosote = false;
				for (FluidStack f : g.mFluidInputs) {
					if (f.isFluidEqual(aCreosote)) {
						aContainsCreosote = true;
					}
				}				
				g.mSpecialValue *= aContainsCreosote ?  8 : 4;		
				Logger.INFO("Added "+g.mFluidInputs[0]+" to the Semi-Fluid Generator fuel map.");
				Gregtech_Recipe_Map.sSemiFluidLiquidFuels.add(g);
			}
		}		
		return Gregtech_Recipe_Map.sSemiFluidLiquidFuels.mRecipeList.size() > 0;
	}
	
}
