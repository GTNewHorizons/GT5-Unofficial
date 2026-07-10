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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
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
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (4_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (4_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (4_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (4_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.shapeFluidGas, (int) (1_500)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(6)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.shapeFluidGas, (int) (1_500)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(7)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.WoodVinegar,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3_000)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.WoodVinegar,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3_000)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(9)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (1_500)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(10)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (1_500)))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(logWood16)
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 4))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.shapeFluidLiquid, (int) (200)))
            .duration(16 * SECONDS)
            .eut(192)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(1)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Creosote,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (8_000)))
                .duration(32 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
                .circuit(2)
                .itemOutputs(RailcraftToolItems.getCoalCoke(16))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Nitrogen,
                        Materials2FluidShapes.shapeFluidGas,
                        (int) (1_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Creosote,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (8_000)))
                .duration(16 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(1)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Creosote,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (32_000)))
                .duration(2 * MINUTES + 8 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8))
                .circuit(2)
                .itemOutputs(EnumCube.COKE_BLOCK.getItem(8))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Nitrogen,
                        Materials2FluidShapes.shapeFluidGas,
                        (int) (1_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Creosote,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (32_000)))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(96)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);
        }

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4))
                .circuit(1)
                .fluidInputs(Materials.Water.getFluid(4_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Biomass,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (5_000)))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 32))
                .circuit(1)
                .fluidInputs(Materials.Water.getFluid(4_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Biomass,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (5_000)))
                .duration(45 * SECONDS)
                .eut(10)
                .addTo(pyrolyseRecipes, cokeOvenRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 4))
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(4_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5_000))
            .duration(45 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biochaff", 1))
            .circuit(2)
            .fluidInputs(Materials.Water.getFluid(1_500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_500)))
            .duration(10 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(10)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 23))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 12))
            .fluidOutputs(Materials.Water.getFluid(1_500))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 23))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 12))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (500)))
            .fluidOutputs(Materials.Water.getFluid(1_500))
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 10))
            .circuit(10)
            .fluidInputs(new FluidStack(FluidLoader.Kerogen, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oil, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .duration(5 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
    }
}
