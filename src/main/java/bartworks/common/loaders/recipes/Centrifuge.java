package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTRecipeBuilder;
import kubatech.tileentity.gregtech.multiblock.MTEHighTempGasCooledReactor;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class Centrifuge implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thorium.getDust(1))
            .itemOutputs(
                Materials.Thorium.getDust(1),
                Materials.Thorium.getDust(1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1))
            .outputChances(800, 375, 22, 22, 5)
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        ItemStack[] pellets = new ItemStack[6];
        Arrays.fill(pellets, new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 64, 4));

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 3))
            .circuit(17)
            .itemOutputs(pellets)
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 5))
            .circuit(17)
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 64, 6))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 6))
            .itemOutputs(Materials.Lead.getDust(1))
            .outputChances(300)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        int i = 0;
        for (MTEHighTempGasCooledReactor.HTGRMaterials.Fuel_ fuel : MTEHighTempGasCooledReactor.HTGRMaterials.sHTGR_Fuel) {

            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 6))
                .itemOutputs(
                    fuel.recycledItems[0],
                    fuel.recycledItems[1],
                    fuel.recycledItems[2],
                    fuel.recycledItems[3],
                    fuel.recycledItems[4])
                .outputChances(fuel.recycleChances);
            if (fuel.recycledFluid != null) {
                recipeBuilder.fluidOutputs(fuel.recycledFluid);
            }
            recipeBuilder.duration(1 * MINUTES)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);

            i += MTEHighTempGasCooledReactor.HTGRMaterials.MATERIALS_PER_FUEL;
        }

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(BioItemList.getOther(4))
            .fluidInputs(new FluidStack(BioCultureLoader.eColi.getFluid(), 1000))
            .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[1], 10))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .fluidInputs(new FluidStack(FluidLoader.BioLabFluidMaterials[1], 1000))
            .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[3], 250))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .fluidInputs(new FluidStack(BioCultureLoader.CommonYeast.getFluid(), 1000))
            .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[2], 10))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

    }
}
