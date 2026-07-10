package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;

public class Autoclave implements Runnable {

    @Override
    public void run() {
        Material[] gasSterilizers = { Materials2Materials.Ammonia, Materials2Materials.Chlorine };
        for (Material used : gasSterilizers) {
            addSterilizerRecipes(MaterialLibAPI.getFluidStack(used, Materials2FluidShapes.shapeFluidGas, 8));
        }

        Material[] liquidSterilizers = { Materials2Materials.Ethanol, Materials2Materials.Methanol };
        for (Material used : liquidSterilizers) {
            addSterilizerRecipes(MaterialLibAPI.getFluidStack(used, Materials2FluidShapes.shapeFluidLiquid, 16));
        }
    }

    private static void addSterilizerRecipes(FluidStack fluid) {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Parts_PetriDish.get(1L))
            .itemOutputs(ItemList.EmptyPetriDish.get(1))
            .fluidInputs(fluid)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.glass_bottle))
            .itemOutputs(ItemList.EmptyDNAFlask.get(1))
            .fluidInputs(fluid)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
    }
}
