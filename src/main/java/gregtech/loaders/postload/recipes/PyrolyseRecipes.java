package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.industrialCokeOvenRecipes;
import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
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
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (4_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (4_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (1_500)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(6)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (1_500)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(7)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(9)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_500)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(10)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_500)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (200)))
            .duration(16 * SECONDS)
            .eut(192)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(1)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (8_000)))
                .duration(32 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(2)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (8_000)))
                .duration(16 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(1)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (32_000)))
                .duration(2 * MINUTES + 8 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(2)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (1_000)))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (32_000)))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        }

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4))
                .circuit(1)
                .fluidInputs(GTUtility.getWater(4_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, (int) (5_000)))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 32))
                .circuit(1)
                .fluidInputs(GTUtility.getWater(4_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, (int) (5_000)))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 4))
            .circuit(1)
            .fluidInputs(GTUtility.getWater(4_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5_000))
            .duration(45 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 1))
            .circuit(2)
            .fluidInputs(GTUtility.getWater(1_500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_500)))
            .duration(10 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 23))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 12))
            .fluidOutputs(GTUtility.getWater(1_500))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 23))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 12))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (500)))
            .fluidOutputs(GTUtility.getWater(1_500))
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 10))
            .circuit(10)
            .fluidInputs(new FluidStack(FluidLoader.Kerogen, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(5 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(pyrolyseRecipes, industrialCokeOvenRecipes);
    }
}
