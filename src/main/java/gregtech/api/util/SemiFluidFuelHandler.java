package gregtech.api.util;

import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.objects.Logger;

public class SemiFluidFuelHandler {

    public static boolean generateFuels() {
        final FluidStack aCreosote = Materials.Creosote.getFluid(1_000);
        final FluidStack aHeavyFuel = Materials.HeavyFuel.getFluid(1_000);
        final FluidStack aHeavyOil = Materials.OilHeavy.getFluid(1_000);
        final HashMap<Integer, Pair<FluidStack, Integer>> aFoundFluidsFromItems = new HashMap<>();
        // Find Fluids From items
        for (final GTRecipe r : RecipeMaps.denseLiquidFuels.getAllRecipes()) {

            GTRecipe g = r.copy();

            if (g != null && g.mEnabled && g.mInputs.length > 0 && g.mInputs[0] != null) {
                for (ItemStack i : g.mInputs) {
                    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(i);
                    if (f != null) {
                        Pair<FluidStack, Integer> aData = Pair.of(f, g.mSpecialValue);
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
                g.mSpecialValue *= aContainsCreosote ? 6 : 3;
                Logger.INFO(
                    "Added " + g.mFluidInputs[0].getLocalizedName()
                        + " to the Semifluid Generator fuel map. Fuel Produces "
                        + g.mSpecialValue
                        + "EU per 1000L.");
                semiFluidFuels.add(g);
            }
        }
        for (Pair<FluidStack, Integer> p : aFoundFluidsFromItems.values()) {
            if (p == null) {
                continue;
            }

            int aFuelValue = p.getValue();
            if (p.getKey()
                .isFluidEqual(aCreosote)) {
                aFuelValue *= 6;
            } else if (p.getKey()
                .isFluidEqual(aHeavyFuel)
                || p.getKey()
                    .isFluidEqual(aHeavyOil)) {
                        aFuelValue *= 1.5;
                    } else {
                        aFuelValue *= 2;
                    }

            if (aFuelValue <= (128 * 4)) {

                GTValues.RA.stdBuilder()
                    .fluidInputs(p.getKey())
                    .duration(0)
                    .eut(0)
                    .metadata(FUEL_VALUE, aFuelValue)
                    .addTo(semiFluidFuels);

                Logger.INFO(
                    "Added " + p.getKey()
                        .getLocalizedName()
                        + " to the Semifluid Generator fuel map. Fuel Produces "
                        + (aFuelValue * 1000)
                        + "EU per 1000L.");

            } else {
                Logger.INFO(
                    "Boosted Fuel value for " + p.getKey()
                        .getLocalizedName() + " exceeds 512k, ignoring.");
            }

        }
        return !semiFluidFuels.getAllRecipes()
            .isEmpty();
    }
}
