package gregtech.api.util;

import static gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map.sSemiFluidLiquidFuels;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SemiFluidFuelHandler {

	
	public static boolean addSemiFluidFuel(ItemStack aFuelItem, int aFuelValue) {
		FluidStack p = FluidContainerRegistry.getFluidForFilledItem(aFuelItem);
		if (p != null && aFuelValue > 0) {
			return addSemiFluidFuel(p, aFuelValue);
		} else {
			Logger.INFO("Fuel value for " + aFuelItem.getDisplayName() + " is <= 0, ignoring.");
		}
		return false;
	}
	
	
	public static boolean addSemiFluidFuel(FluidStack aFuel, int aFuelValue) {
		FluidStack p = aFuel;
		if (p != null && aFuelValue > 0) {
			GT_Recipe aRecipe =
					new Recipe_GT(true,
							new ItemStack[] {},
							new ItemStack[] {},
							null,
							new int[] {},
							new FluidStack[] { p },
							null,
							0,
							0,
							aFuelValue);
			if (aRecipe.mSpecialValue > 0) {
				Logger.INFO("Added " + aRecipe.mFluidInputs[0].getLocalizedName() + " to the Semi-Fluid Generator fuel map. Fuel Produces "+(aRecipe.mSpecialValue*1000)+"EU per 1000L.");
				sSemiFluidLiquidFuels.add(aRecipe);
				return true;
			}
		} else {
			Logger.INFO("Fuel value for " + p != null ? p.getLocalizedName() : "NULL Fluid" + " is <= 0, ignoring.");
		}
		return false;
	}
	
	
	
	
	
	
	public static boolean generateFuels() {
		final FluidStack aCreosote = FluidUtils.getFluidStack("creosote", 1000);
		final FluidStack aHeavyFuel = FluidUtils.getFluidStack("liquid_heavy_fuel", 1000);
		final HashMap<Integer, Pair<FluidStack, Integer>> aFoundFluidsFromItems = new HashMap<Integer, Pair<FluidStack, Integer>>();
		// Find Fluids From items
		for (final GT_Recipe r : gregtech.api.util.GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.mRecipeList) {
			
			GT_Recipe g = r.copy();
			
			
			if (g != null && g.mEnabled && g.mInputs.length > 0 && g.mInputs[0] != null) {
				for (ItemStack i : g.mInputs) {
					FluidStack f = FluidContainerRegistry.getFluidForFilledItem(i);
					if (f != null) {
						Pair<FluidStack, Integer> aData = new Pair<FluidStack, Integer>(f, g.mSpecialValue);
						aFoundFluidsFromItems.put(aData.hashCode(), aData);
					}
				}
			} else if (g != null && g.mEnabled && g.mFluidInputs.length > 0 && g.mFluidInputs[0] != null) {
				boolean aContainsCreosote = false;
				for (FluidStack f : g.mFluidInputs) {
					if (f.isFluidEqual(aCreosote)) {
						aContainsCreosote = true;
					}
				}
				g.mSpecialValue *= aContainsCreosote ? 8 : 4;
				Logger.INFO("Added " + g.mFluidInputs[0].getLocalizedName() + " to the Semi-Fluid Generator fuel map. Fuel Produces "+g.mSpecialValue+"EU per 1000L.");
				sSemiFluidLiquidFuels.add(g);
			}
		}
		for (Pair<FluidStack, Integer> p : aFoundFluidsFromItems.values()) {
			if (p != null) {
				int aFuelValue = p.getValue();
				if (p.getKey().isFluidEqual(aCreosote)) {
					aFuelValue *= 8;
				}
				else if (p.getKey().isFluidEqual(aHeavyFuel)){
					aFuelValue *= 1.5;
				}
				else {
					aFuelValue *= 2;
				}

				if (aFuelValue <= (128*3)) {
					GT_Recipe aRecipe = new Recipe_GT(true, new ItemStack[] {}, new ItemStack[] {}, null, new int[] {},
							new FluidStack[] { p.getKey() }, null, 0, 0, aFuelValue);
					if (aRecipe.mSpecialValue > 0) {
						Logger.INFO("Added " + aRecipe.mFluidInputs[0].getLocalizedName() + " to the Semi-Fluid Generator fuel map. Fuel Produces "+(aRecipe.mSpecialValue*1000)+"EU per 1000L.");
						sSemiFluidLiquidFuels.add(aRecipe);
					}
				} else {
					Logger.INFO("Boosted Fuel value for " + p.getKey().getLocalizedName() + " exceeds 512k, ignoring.");
				}
			}
		}
		return sSemiFluidLiquidFuels.mRecipeList.size() > 0;
	}

}
