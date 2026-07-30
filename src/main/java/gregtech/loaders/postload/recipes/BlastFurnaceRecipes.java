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

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class BlastFurnaceRecipes implements Runnable {

    @Override
    public void run() {
        this.registerBlastFurnaceRecipes();
        this.registerPrimitiveBlastFurnaceRecipes();
    }

    public void registerBlastFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Gypsum, Shapes.dust, (int) (8)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSulfuricAcid,
                    FluidShapes.fluidLiquid,
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
                MaterialLibAPI.getStack(Materials.RoastedCopper, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedAntimony, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.Antimony, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedIron, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedNickel, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedZinc, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedCobalt, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedArsenic, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Arsenic, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoastedLead, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Lead, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Malachite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (3_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BandedIron, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Garnierite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Magnetite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.YellowLimonite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BrownLimonite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BasalticMineralSand, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.GraniticMineralSand, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CassiteriteSand, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, (int) (outputIngotAmount)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 2222)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(12 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, (int) (2_000)))
            .duration(4 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.CupricOxide, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Malachite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI
                        .getStack(Materials.Copper, Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (3_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.AntimonyTrioxide, Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Antimony, Shapes.ingot, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (3_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.BandedIron, Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Magnetite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.YellowLimonite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.BrownLimonite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.BasalticMineralSand, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.GraniticMineralSand, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, outputIngotAmount),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.CassiteriteSand, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, (int) (outputIngotAmount)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Garnierite, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Nickel, Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.CobaltOxide, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Cobalt, Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.ArsenicTrioxide, Shapes.dust, (int) (5)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Arsenic, Shapes.ingot, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Massicot, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, (int) (4)))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Lead, Shapes.ingot, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
                .outputChances(10000, 2222)
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CarbonDioxide,
                        FluidShapes.fluidGas,
                        (int) (1_000)))
                .duration(12 * SECONDS)
                .eut((int) TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 32),
                ItemList.GalliumArsenideCrystalSmallPart.get(1))
            .circuit(2)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1784)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 64),
                ItemList.GalliumArsenideCrystalSmallPart.get(2),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, (int) (8)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(1))
            .duration(10 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSolarGrade, 16),
                ItemList.GalliumArsenideCrystal.get(1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.ingot, (int) (1)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut((int) TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSolarGrade, 32),
                ItemList.GalliumArsenideCrystal.get(2),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.ingot, (int) (2)))
            .circuit(3)
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(1))
            .duration(15 * MINUTES)
            .eut((int) TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 6484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSolarGrade, 64),
                ItemList.GalliumArsenideCrystal.get(4),
                MaterialLibAPI.getStack(Materials.Americium, Shapes.ingot, (int) (4)))
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
                MaterialLibAPI.getStack(Materials.CalciumHydride, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CalciumDisilicide, Shapes.dust, (int) (3)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1273)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.SuperconductorUEVBase, Shapes.ingotHot, (int) (1)))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .metadata(COIL_HEAT, 11800)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SuperconductorUIVBase, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.SuperconductorUIVBase, Shapes.ingotHot, (int) (1)))
            .duration(13 * MINUTES + 6 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 12700)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.SuperconductorUMVBase, Shapes.ingotHot, (int) (1)))
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
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (3)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CacliumCarbide, Shapes.dust, 3))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2573)
            .addTo(blastFurnaceRecipes);

        // Ni + 3Al = NiAl3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (3)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NickelAluminide, Shapes.ingot, (int) (4)))
            .duration(45 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1688)
            .addTo(blastFurnaceRecipes);

        ItemStack[] tSiliconDioxide = new ItemStack[] {
            MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, (int) (3)),
            MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, (int) (6)) };

        // Roasting

        for (ItemStack silicon : tSiliconDioxide) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Chalcopyrite, Shapes.dust, (int) (1)),
                    silicon)
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.RoastedCopper, Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dust, (int) (5)))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfurDioxide,
                        FluidShapes.fluidGas,
                        (int) (2_000)))
                .duration(6 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 1200)
                .addTo(blastFurnaceRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tetrahedrite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedCopper, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.RoastedAntimony, Shapes.dustTiny, (int) (3)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pyrite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedIron, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pentlandite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedNickel, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (2_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sphalerite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedZinc, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cobaltite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedCobalt, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.RoastedArsenic, Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Stibnite, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedAntimony, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_500)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Galena, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RoastedLead, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, (int) (1)))
            .outputChances(10000, 1111)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.dust, (int) (1)))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ingotHot, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.CelestialTungsten, 1 * HALF_INGOTS))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 11701)
            .addTo(blastFurnaceRecipes);

        // Rh + 3Cl = RhCl3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RhodiumChloride, Shapes.dust, (int) (4)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (3_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 573)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 1),
                MaterialLibAPI.getStack(Materials.Dilithium, Shapes.dust, (int) (16)))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Mellion, FluidShapes.fluidMolten, (int) (48 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PhononCrystalSolution,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(300 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 17000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Phononic_Seed_Crystal.get(2),
                MaterialLibAPI.getStack(Materials.Eternity, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Shirabon, Shapes.dust, 8))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PhononCrystalSolution,
                    FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .duration(200 * SECONDS)
            .eut((int) TierEU.RECIPE_UXV)
            .metadata(COIL_HEAT, 50000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Mellion, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(ItemList.Harmonic_Compound.get(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Creon, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .metadata(COIL_HEAT, 14000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Shijima, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Shijima, Shapes.ingotHot, (int) (1)))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .metadata(COIL_HEAT, 7400)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .metadata(NO_GAS, true)
            .metadata(NO_GAS_CIRCUIT_CONFIG, 1)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Churitsu, Shapes.dust, (int) (1)))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Churitsu, Shapes.ingotHot, (int) (1)))
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
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.ingot, (int) (9)))
            .duration(54 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 36)
            .addTo(primitiveBlastRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.ingot, (int) (1)))
            .duration(6 * MINUTES)
            .metadata(ADDITIVE_AMOUNT, 2)
            .addTo(primitiveBlastRecipes);
    }
}
