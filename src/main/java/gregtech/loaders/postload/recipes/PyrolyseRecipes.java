package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class PyrolyseRecipes implements Runnable {

    @Override
    public void run() {
        var logWood16 = new OreDictItemStack("logWood", 16);

        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(1)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.Creosote.getFluid(4_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(2)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.Creosote.getFluid(4_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(3)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(4)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(5)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodGas.getGas(1_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(6)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodGas.getGas(1_500))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(7)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(8)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(9)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodTar.getFluid(1_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(10)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodTar.getFluid(1_500))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(11)
            .itemOutputs(Materials.Ash.getDust(4))
            .fluidOutputs(Materials.OilHeavy.getFluid(200))
            .duration(16 * SECONDS)
            .eut(192)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(1)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidOutputs(Materials.Creosote.getFluid(8_000))
                .duration(32 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(2)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidInputs(Materials.Nitrogen.getGas(1_000))
                .fluidOutputs(Materials.Creosote.getFluid(8_000))
                .duration(16 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(1)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidOutputs(Materials.Creosote.getFluid(32_000))
                .duration(2 * MINUTES + 8 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(2)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidInputs(Materials.Nitrogen.getGas(1_000))
                .fluidOutputs(Materials.Creosote.getFluid(32_000))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);
        }

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4))
                .circuit(1)
                .fluidInputs(Materials.Water.getFluid(4_000))
                .fluidOutputs(Materials.Biomass.getFluid(5_000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 32))
                .circuit(1)
                .fluidInputs(Materials.Water.getFluid(4_000))
                .fluidOutputs(Materials.Biomass.getFluid(5_000))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Chaff.get(4))
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(4_000))
            .fluidOutputs(Materials.Biomass.getFluid(5_000))
            .duration(45 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Chaff.get(1))
            .circuit(2)
            .fluidInputs(Materials.Water.getFluid(1_500))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1_500))
            .duration(10 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.Biomass.getFluid(1_000))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23))
            .circuit(1)
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidOutputs(Materials.Water.getFluid(1_500))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(23))
            .circuit(2)
            .itemOutputs(Materials.Charcoal.getDust(12))
            .fluidInputs(Materials.Nitrogen.getGas(500))
            .fluidOutputs(Materials.Water.getFluid(1_500))
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Wood.getDust(10))
            .circuit(10)
            .fluidInputs(new FluidStack(FluidLoader.Kerogen, 1_000))
            .fluidOutputs(Materials.Oil.getFluid(1_000))
            .duration(5 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
    }
}
