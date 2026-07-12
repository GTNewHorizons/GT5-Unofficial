package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.NO_GAS;
import static gregtech.api.util.GTRecipeConstants.NO_GAS_CIRCUIT_CONFIG;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class BlastFurnaceRecipes implements Runnable {

    @Override
    public void run() {
        this.registerBlastFurnaceRecipes();
        this.registerPrimitiveBlastFurnaceRecipes();
    }

    public void registerBlastFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Gypsum, Materials2Shapes.dust, (int) (8)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_500)))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3200)
            .addTo(blastFurnaceRecipes);

        // Carbothermic Reduction
        // Depend on real amount except real ores
        int outputIngotAmount = GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedCopper, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedAntimony, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.Antimony, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedIron, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedNickel, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedZinc, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedCobalt, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedArsenic, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Arsenic, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedLead, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Malachite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BandedIron, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Garnierite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Magnetite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.YellowLimonite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BrownLimonite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BasalticMineralSand, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.GraniticMineralSand, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                Materials.Iron.getIngots(outputIngotAmount),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cassiterite, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CassiteriteSand, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(4 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.CupricOxide, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Malachite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI
                        .getStack(Materials2Materials.Copper, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (3_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.AntimonyTrioxide, Materials2Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Antimony, Materials2Shapes.ingot, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (3_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.BandedIron, Materials2Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(2),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Magnetite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(outputIngotAmount),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.YellowLimonite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(outputIngotAmount),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.BrownLimonite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(outputIngotAmount),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.BasalticMineralSand, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(outputIngotAmount),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.GraniticMineralSand, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    Materials.Iron.getIngots(outputIngotAmount),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Cassiterite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.CassiteriteSand, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Garnierite, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.CobaltOxide, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.ArsenicTrioxide, Materials2Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Arsenic, Materials2Shapes.ingot, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Massicot, Materials2Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CarbonDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystalSmallPart.get(1))
            .circuit(2)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1784)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystalSmallPart.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, (int) (8)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(1))
            .duration(10 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                ItemList.GalliumArsenideCrystal.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingot, (int) (1)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 32),
                ItemList.GalliumArsenideCrystal.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.ingot, (int) (2)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(1))
            .duration(15 * MINUTES)
            .eut((int) TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 6484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 64),
                ItemList.GalliumArsenideCrystal.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.ingot, (int) (4)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(1))
            .duration(17 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 9000)
            .metadata(ADDITIVE_AMOUNT, 16000)
            .addTo(BlastFurnaceWithGas);

        // CaH2 + 2Si = CaSi2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumDisilicide, Materials2Shapes.dust, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1273)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUEVBase, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUEVBase, Materials2Shapes.ingotHot, (int) (1)))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .metadata(COIL_HEAT, 11800)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.ingotHot, (int) (1)))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 12700)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUMVBase, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUMVBase, Materials2Shapes.ingotHot, (int) (1)))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 13600)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        // CaO + 3C = CaC2 + CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (3)))
            .itemOutputs(Materials.CalciumCarbide.getDust(3))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2573)
            .addTo(blastFurnaceRecipes);

        // Ni + 3Al = NiAl3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (3)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NickelAluminide, Materials2Shapes.ingot, (int) (4)))
            .duration(45 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1688)
            .addTo(blastFurnaceRecipes);

        ItemStack[] tSiliconDioxide = new ItemStack[] {
            MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.dust, (int) (6)) };

        // Roasting

        for (ItemStack silicon : tSiliconDioxide) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Chalcopyrite, Materials2Shapes.dust, (int) (1)),
                    silicon)
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.RoastedCopper, Materials2Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.Ferrosilite, Materials2Shapes.dust, (int) (5)))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfurDioxide,
                        Materials2FluidShapes.fluidGas,
                        (int) (2_000)))
                .duration(6 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tetrahedrite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedCopper, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.RoastedAntimony, Materials2Shapes.dustTiny, (int) (3)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pyrite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedIron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pentlandite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedNickel, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedZinc, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Cobaltite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedCobalt, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.RoastedArsenic, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Stibnite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedAntimony, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_500)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Galena, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RoastedLead, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.dust, (int) (1)))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.ingotHot, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tungsten, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(1 * HALF_INGOTS))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 11701)
            .addTo(blastFurnaceRecipes);

        // Rh + 3Cl = RhCl3

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 1, 78))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.RhodiumChloride, Materials2Shapes.dust, (int) (4)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 573)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(1),
                Materials.SixPhasedCopper.getNanite(1),
                MaterialLibAPI.getStack(Materials2Materials.Dilithium, Materials2Shapes.dust, (int) (16)))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mellion, Materials2FluidShapes.fluidMolten, (int) (48 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhononCrystalSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(300 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 17000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Eternity, Materials2Shapes.dust, (int) (8)),
                GGMaterial.shirabon.get(OrePrefixes.dust, 8))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PhononMedium, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhononCrystalSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .duration(200 * SECONDS)
            .eut((int) TierEU.RECIPE_UXV)
            .metadata(COIL_HEAT, 50000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(ItemList.Harmonic_Compound.get(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .metadata(COIL_HEAT, 14000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.ingotHot, (int) (1)))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 7400)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.ingotHot, (int) (1)))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 7400)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);
    }

    public void registerPrimitiveBlastFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getIngots(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getBlocks(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (9)))
            .duration(54 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 36)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 2)
            .addTo(primitiveBlastRecipes);
    }
}
