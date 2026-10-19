package gregtech.loaders.postload.recipes;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

import static gregtech.api.recipe.RecipeMaps.nanochipBoardProcessorRecipes;
import static gregtech.api.recipe.RecipeMaps.nanochipEncasementWrapper;
import static gregtech.api.recipe.RecipeMaps.nanochipEtchingArray;
import static gregtech.api.recipe.RecipeMaps.nanochipOpticalOrganizer;
import static gregtech.api.recipe.RecipeMaps.nanochipPartProcessorRecipes;
import static gregtech.api.recipe.RecipeMaps.nanochipWireTracer;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

/**
 * Class to allow for NAC testing in dev-env without hassle
 */
public class DevEnvNanochipRecipes implements Runnable{


    // Overload that allows for the output to be more than one item
    private static void addAssemblyMatrixRecipe(List<Object> input, List<FluidStack> fluidInputs,
        CircuitComponent.CircuitComponentStack output, int duration, long eut, int recipeTier) {
        if (output.getCircuitComponent().realComponent == null) {
            throw new IllegalArgumentException("No real circuit was defined for given output!");
        }

        Object[] inputs = new Object[input.size()];
        for (int i = 0; i < input.size(); i++) {
            Object inputThing = input.get(i);
            if (inputThing instanceof CircuitComponent.CircuitComponentStack ccStack) {
                inputs[i] = ccStack.getCircuitComponent().getFakeStack(ccStack.getSize());
            } else if (inputThing instanceof Object[]alts) {
                inputs[i] = Arrays.stream(alts).map(c -> {
                    if (c instanceof CircuitComponent.CircuitComponentStack ccStack) {
                        return ccStack.getCircuitComponent().getFakeStack(ccStack.getSize());
                    } else if (c instanceof ItemStack stack) {
                        return stack;
                    }
                    throw new IllegalArgumentException("Must pass a CircuitComponentStack or ItemStack array!");
                }).toArray(ItemStack[]::new);
            }
        }

        GTValues.RA.stdBuilder().metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .metadata(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, output.getCircuitComponent().circuitType)
            .itemInputs(inputs).fluidInputs(fluidInputs.toArray(new FluidStack[0]))
            .itemOutputs(output.getCircuitComponent().getFakeStack(output.getSize())).duration(duration).eut(eut)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }

    private static void addAssemblyMatrixRecipe(List<Object> input, List<FluidStack> fluidInputs,
        CircuitComponent output, int duration, long eut, int recipeTier) {
        if (output.realComponent == null) {
            throw new IllegalArgumentException("No real circuit was defined for given output!");
        }

        ItemStack realOutput = output.realComponent.get();
        realOutput.stackSize = 1;

        Object[] inputs = new Object[input.size()];
        for (int i = 0; i < input.size(); i++) {
            Object inputThing = input.get(i);
            if (inputThing instanceof CircuitComponent.CircuitComponentStack ccStack) {
                inputs[i] = ccStack.getCircuitComponent().getFakeStack(ccStack.getSize());
            } else if (inputThing instanceof Object[]alts) {
                inputs[i] = Arrays.stream(alts).map(c -> {
                    if (c instanceof CircuitComponent.CircuitComponentStack ccStack) {
                        return ccStack.getCircuitComponent().getFakeStack(ccStack.getSize());
                    } else if (c instanceof ItemStack stack) {
                        return stack;
                    }
                    throw new IllegalArgumentException("Must pass a CircuitComponentStack or ItemStack array!");
                }).toArray(ItemStack[]::new);
            }
        }

        GTValues.RA.stdBuilder().metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .metadata(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, output.circuitType).itemInputs(inputs)
            .fluidInputs(fluidInputs.toArray(new FluidStack[0])).itemOutputs(output.getFakeStack(1))
            .duration(duration).eut(eut).addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }

    // Adds a simple processing recipe for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output, long eut,
        int duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder().itemInputs(input.getFakeStack(1)).itemOutputs(output.getFakeStack(1))
            .duration(duration).eut(eut).addTo(recipeMap);
    }

    // Adds a simple processing recipe with a fluid for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, FluidStack inputStack,
        CircuitComponent output, long eut, long duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder().itemInputs(input.getFakeStack(1)).fluidInputs(inputStack)
            .itemOutputs(output.getFakeStack(1)).duration(duration).eut(eut).addTo(recipeMap);
    }
    @Override
    public void run() {

        for (CircuitComponent cc : CircuitComponent.VALUES) {
            if (cc.isProcessed) continue;
            if (cc.realComponent == null) throw new IllegalStateException("CC must define its representative item!");

            GTValues.RA.stdBuilder().itemInputs(cc.realComponent.get()).itemOutputs(cc.getFakeStack(1))
                .duration(5 * SECONDS).eut(0).addTo(RecipeMaps.nanochipConversionRecipes);

            GTValues.RA.stdBuilder().itemInputs(cc.getFakeStack(1)).itemOutputs(cc.realComponent.get())
                .duration(5 * SECONDS).eut(0).addTo(RecipeMaps.nanochipConversionRecipes);
        }


        registerBiologicalCoordinatorRecipes();
        registerBoardProcessorRecipes();
        registerCuttingChamberRecipes();
        registerEncasementWrapperRecipes();
        registerEtchingArrayRecipes();
        registerOpticalOrganizerRecipes();
        registerPartProcessorRecipes();
        registerSuperconductorSplitterRecipes();
        registerWireTracerRecipes();

    }

    private static void registerBiologicalCoordinatorRecipes() {
        // Living Crystal Chip
        addSimpleProcessingRecipe(
            CircuitComponent.ChipLivingCrystal,
            Materials.BioMediumSterilized.getFluid(5),
            CircuitComponent.ProcessedChipLivingCrystal,
            TierEU.RECIPE_UHV,
            16 * SECONDS,
            RecipeMaps.nanochipBiologicalCoordinator);

        // Bio Processing Unit
        addSimpleProcessingRecipe(
            CircuitComponent.BioProcessingUnit,
            Materials.BioMediumSterilized.getFluid(50),
            CircuitComponent.ProcessedBioProcessingUnit,
            TierEU.RECIPE_UEV,
            32 * SECONDS,
            RecipeMaps.nanochipBiologicalCoordinator);

        // Living Bio Chip
        addSimpleProcessingRecipe(
            CircuitComponent.LivingBioChip,
            Materials.BioMediumSterilized.getFluid(50),
            CircuitComponent.ProcessedLivingBioChip,
            TierEU.RECIPE_UEV,
            32 * SECONDS,
            RecipeMaps.nanochipBiologicalCoordinator);

        addSimpleProcessingRecipe(
            CircuitComponent.ProcessedBoardMultifiberglassElite,
            Materials.BioMediumSterilized.getFluid(20),
            CircuitComponent.UnattunedCircuitry,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipBiologicalCoordinator);
    }

    private static void registerBoardProcessorRecipes() {
        // Elite Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 1)
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Elite Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .fluidInputs(Materials.IronIIIChloride.getFluid(0))
            .fluidOutputs(GGMaterial.ferrousChloride.getFluidOrGas(0))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Wetware Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 2)
            .itemInputs(CircuitComponent.BoardWetwareLifesupport.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardWetwareLifesupport.getFakeStack(1)).duration(16 * SECONDS)
            .eut(TierEU.RECIPE_UHV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Wetware Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardWetwareLifesupport.getFakeStack(1))
            .fluidInputs(Materials.GrowthMediumSterilized.getFluid(0))
            .fluidOutputs(Materials.GrowthMediumRaw.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardWetwareLifesupport.getFakeStack(1)).duration(16 * SECONDS)
            .eut(TierEU.RECIPE_UHV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Bio Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 3)
            .itemInputs(CircuitComponent.BoardBioMutated.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardBioMutated.getFakeStack(1)).duration(16 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Bio Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardBioMutated.getFakeStack(1))
            .fluidInputs(Materials.BioMediumSterilized.getFluid(0)).fluidOutputs(Materials.BioMediumRaw.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardBioMutated.getFakeStack(1)).duration(16 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Optical Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 4)
            .itemInputs(CircuitComponent.BoardOptical.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardOptical.getFakeStack(1)).duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Optical Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardOptical.getFakeStack(1))
            .fluidInputs(Materials.PrismaticAcid.getFluid(0)).fluidOutputs(Materials.PrismaticGas.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardOptical.getFakeStack(1)).duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Pico Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 5)
            .itemInputs(CircuitComponent.BoardPico.getFakeStack(1))
            .itemOutputs(CircuitComponent.CleansedBoardPico.getFakeStack(1)).duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipBoardProcessorRecipes);

        // Pico Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardPico.getFakeStack(1))
            .fluidInputs(Materials.UUMatter.getFluid(0)).fluidOutputs(Materials.UUAmplifier.getFluid(0))
            .itemOutputs(CircuitComponent.CleansedBoardPico.getFakeStack(1)).duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipBoardProcessorRecipes);

        // Quantum Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 5)
            .itemInputs(CircuitComponent.BoardQuantum.getFakeStack(1))
            .itemOutputs(CircuitComponent.CleansedBoardQuantum.getFakeStack(1)).duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UXV).addTo(nanochipBoardProcessorRecipes);

        // Quantum Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardQuantum.getFakeStack(1))
            .fluidInputs(Materials.UUMatter.getFluid(0)).fluidOutputs(Materials.UUAmplifier.getFluid(0))
            .itemOutputs(CircuitComponent.CleansedBoardQuantum.getFakeStack(1)).duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UXV).addTo(nanochipBoardProcessorRecipes);

        // Plank Board
        GTValues.RA.stdBuilder().hidden().metadata(BoardProcessingModuleFluidKey.INSTANCE, 5)
            .itemInputs(CircuitComponent.BoardPlanck.getFakeStack(1))
            .itemOutputs(CircuitComponent.CleansedBoardPlanck.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MAX).addTo(nanochipBoardProcessorRecipes);

        // Planck Board fake recipe
        GTValues.RA.stdBuilder().fake().itemInputs(CircuitComponent.BoardPlanck.getFakeStack(1))
            .fluidInputs(Materials.UUMatter.getFluid(0)).fluidOutputs(Materials.UUAmplifier.getFluid(0))
            .itemOutputs(CircuitComponent.CleansedBoardPlanck.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MAX).addTo(nanochipBoardProcessorRecipes);
    }

    private static void registerCuttingChamberRecipes() {
        // Chips
        {
            // CPU
            addSimpleProcessingRecipe(
                CircuitComponent.ChipNanoCPU,
                Materials.Lubricant.getFluid(50),
                CircuitComponent.ProcessedChipNanoCPU,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // RAM
            addSimpleProcessingRecipe(
                CircuitComponent.ChipRAM,
                Materials.Lubricant.getFluid(50),
                CircuitComponent.ProcessedChipRAM,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // NOR
            addSimpleProcessingRecipe(
                CircuitComponent.ChipNOR,
                Materials.Lubricant.getFluid(50),
                CircuitComponent.ProcessedChipNOR,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // NAND
            addSimpleProcessingRecipe(
                CircuitComponent.ChipNAND,
                Materials.Lubricant.getFluid(50),
                CircuitComponent.ProcessedChipNAND,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // ASoC
            addSimpleProcessingRecipe(
                CircuitComponent.ChipASOC,
                Materials.Lubricant.getFluid(50),
                CircuitComponent.ProcessedChipASOC,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // APIC
            addSimpleProcessingRecipe(
                CircuitComponent.ChipAttoPIC,
                Materials.DimensionallyShiftedSuperfluid.getFluid(10),
                CircuitComponent.ProcessedChipAttoPIC,
                TierEU.RECIPE_UMV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // ZPIC
            addSimpleProcessingRecipe(
                CircuitComponent.ChipZeptoPIC,
                Materials.DimensionallyShiftedSuperfluid.getFluid(10),
                CircuitComponent.ProcessedChipZeptoPIC,
                TierEU.RECIPE_UMV,
                20 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // YPIC
            addSimpleProcessingRecipe(
                CircuitComponent.ChipYoctoPIC,
                Materials.DimensionallyShiftedSuperfluid.getFluid(10),
                CircuitComponent.ProcessedChipYoctoPIC,
                TierEU.RECIPE_UXV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);
        }

        // Bolts
        {
            // Enriched Holmium
            addSimpleProcessingRecipe(
                CircuitComponent.BoltEnrichedHolmium,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltEnrichedHolmium,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Transcendent Metal
            addSimpleProcessingRecipe(
                CircuitComponent.BoltTranscendentMetal,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltTranscendentMetal,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Yttrium Barium Cuprate
            addSimpleProcessingRecipe(
                CircuitComponent.BoltYttriumBariumCuprate,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltYttriumBariumCuprate,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Cosmic Neutronium
            addSimpleProcessingRecipe(
                CircuitComponent.BoltCosmicNeutronium,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltCosmicNeutronium,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Chromatic Glass
            addSimpleProcessingRecipe(
                CircuitComponent.BoltChromaticGlass,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltChromaticGlass,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // SC Base UMV
            addSimpleProcessingRecipe(
                CircuitComponent.BoltUMVSuperconductor,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltUMVSuperconductor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // White Dwarf Matter
            addSimpleProcessingRecipe(
                CircuitComponent.BoltWhiteDwarfMatter,
                Materials.Lubricant.getFluid(20),
                CircuitComponent.ProcessedBoltWhiteDwarfMatter,
                TierEU.RECIPE_UV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Copper Bolt
            addSimpleProcessingRecipe(
                CircuitComponent.BoltCopper,
                Materials.Lubricant.getFluid(5),
                CircuitComponent.ProcessedBoltCopper,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Red Alloy Bolt
            addSimpleProcessingRecipe(
                CircuitComponent.BoltRedAlloy,
                Materials.Lubricant.getFluid(5),
                CircuitComponent.ProcessedBoltRedAlloy,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Platinum Bolt
            addSimpleProcessingRecipe(
                CircuitComponent.BoltPlatinum,
                Materials.Lubricant.getFluid(5),
                CircuitComponent.ProcessedBoltPlatinum,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Niobium Titanium Bolt
            addSimpleProcessingRecipe(
                CircuitComponent.BoltNiobiumTitanium,
                Materials.Lubricant.getFluid(5),
                CircuitComponent.ProcessedBoltNiobiumTitanium,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);
        }
        // Frame box recipes
        {
            // Aluminium
            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxAluminium,
                Materials.Grade1PurifiedWater.getFluid(500),
                CircuitComponent.ProcessedFrameboxAluminium,
                TierEU.RECIPE_UV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxAluminium,
                Materials.Grade2PurifiedWater.getFluid(250),
                CircuitComponent.ProcessedFrameboxAluminium,
                TierEU.RECIPE_UV,
                5 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Tritanium
            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxTritanium,
                Materials.Grade3PurifiedWater.getFluid(500),
                CircuitComponent.ProcessedFrameboxTritanium,
                TierEU.RECIPE_UHV,
                20 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxTritanium,
                Materials.Grade4PurifiedWater.getFluid(250),
                CircuitComponent.ProcessedFrameboxTritanium,
                TierEU.RECIPE_UHV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Transcendent Metal
            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxCelestialTungsten,
                Materials.Grade5PurifiedWater.getFluid(250),
                CircuitComponent.ProcessedFrameboxCelestialTungsten,
                TierEU.RECIPE_UEV,
                20 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxCelestialTungsten,
                Materials.Grade6PurifiedWater.getFluid(250),
                CircuitComponent.ProcessedFrameboxCelestialTungsten,
                TierEU.RECIPE_UEV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Hypogen
            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxHypogen,
                Materials.Grade7PurifiedWater.getFluid(500),
                CircuitComponent.ProcessedFrameboxHypogen,
                TierEU.RECIPE_UIV,
                20 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxHypogen,
                Materials.Grade8PurifiedWater.getFluid(250),
                CircuitComponent.ProcessedFrameboxHypogen,
                TierEU.RECIPE_UIV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            // Magmatter
            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxMagMatter,
                Materials.Grade8PurifiedWater.getFluid(500),
                CircuitComponent.ProcessedFrameboxMagMatter,
                TierEU.RECIPE_UMV,
                20 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);

            addSimpleProcessingRecipe(
                CircuitComponent.FrameboxMagMatter,
                Materials.DimensionallyShiftedSuperfluid.getFluid(100),
                CircuitComponent.ProcessedFrameboxMagMatter,
                TierEU.RECIPE_UMV,
                10 * SECONDS,
                RecipeMaps.nanochipCuttingChamber);
        }
    }

    private static void registerEncasementWrapperRecipes() {
        // Foils
        {
            addSimpleProcessingRecipe(
                CircuitComponent.FoilPolybenzimidazole,
                CircuitComponent.ProcessedFoilPolybenzimidazole,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);

            addSimpleProcessingRecipe(
                CircuitComponent.FoilSiliconeRubber,
                CircuitComponent.ProcessedFoilSiliconeRubber,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);

            addSimpleProcessingRecipe(
                CircuitComponent.FoilStyreneRubber,
                CircuitComponent.ProcessedFoilStyreneRubber,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);

            addSimpleProcessingRecipe(
                CircuitComponent.FoilRadoxPolymer,
                CircuitComponent.ProcessedFoilRadoxPolymer,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);

            addSimpleProcessingRecipe(
                CircuitComponent.FoilShirabon,
                CircuitComponent.ProcessedFoilShirabon,
                TierEU.RECIPE_UV,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);

            addSimpleProcessingRecipe(
                CircuitComponent.FoilEternity,
                CircuitComponent.ProcessedFoilEternity,
                TierEU.RECIPE_UV,
                1 * SECONDS,
                RecipeMaps.nanochipEncasementWrapper);
        }

        // Crystal
        GTValues.RA.stdBuilder().itemInputs(CircuitComponent.ProcessedFrameboxAluminium.getFakeStack(2))
            .itemOutputs(CircuitComponent.ProcessedBasicMainframeCasing.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Wetware, Bioware, Optical
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilSiliconeRubber.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxTritanium.getFakeStack(1),
                CircuitComponent.ProcessedFoilPolybenzimidazole.getFakeStack(16))
            .itemOutputs(CircuitComponent.ProcessedAdvancedMainframeCasing.getFakeStack(1)).duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipEncasementWrapper);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilStyreneRubber.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxTritanium.getFakeStack(1),
                CircuitComponent.ProcessedFoilPolybenzimidazole.getFakeStack(16))
            .itemOutputs(CircuitComponent.ProcessedAdvancedMainframeCasing.getFakeStack(1)).duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UEV).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Pico
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilRadoxPolymer.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxCelestialTungsten.getFakeStack(2),
                CircuitComponent.ScrewAstralTitanium.getFakeStack(4),
                CircuitComponent.CasingUEVSuperconductor.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedPicoCircuitCasing.getFakeStack(1)).duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Quantum
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilShirabon.getFakeStack(32),
                CircuitComponent.ProcessedFrameboxHypogen.getFakeStack(2),
                CircuitComponent.ScrewSixPhasedCopper.getFakeStack(4),
                CircuitComponent.CasingCreon.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedQuantumCircuitCasing.getFakeStack(1)).duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UXV).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Thermal SC
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ThermalSuperconductor.getFakeStack(1),
                CircuitComponent.ProcessedSuperconductorUMV.getFakeStack(8))
            .itemOutputs(CircuitComponent.ProcessedCoiledThermalSuperconductor.getFakeStack(1))
            .duration(4 * SECONDS).eut(TierEU.RECIPE_UXV).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Planck
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilEternity.getFakeStack(64),
                CircuitComponent.ProcessedFrameboxMagMatter.getFakeStack(2),
                CircuitComponent.ScrewUniversium.getFakeStack(2),
                CircuitComponent.CasingBlackDwarfMatter.getFakeStack(2))
            .itemOutputs(CircuitComponent.ProcessedPlanckCircuitCasing.getFakeStack(1)).duration(7 * SECONDS)
            .eut(TierEU.RECIPE_MAX).addTo(RecipeMaps.nanochipEncasementWrapper);

        // Pico CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.OpticalMainframeRack.getFakeStack(1),
                CircuitComponent.ProcessedPlateRhugnor.getFakeStack(2),
                CircuitComponent.ProcessedChipAttoPIC.getFakeStack(8))
            .fluidInputs(Materials.Space.getMolten(250)).itemOutputs(CircuitComponent.PicoCPU.getFakeStack(1))
            .duration(20 * SECONDS).eut(TierEU.RECIPE_UMV).addTo(nanochipEncasementWrapper);

        // Quantum CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.PicoCircuitRack.getFakeStack(1),
                CircuitComponent.ProcessedPlateMetastableOganesson.getFakeStack(2),
                CircuitComponent.ProcessedChipZeptoPIC.getFakeStack(8))
            .fluidInputs(Materials.Space.getMolten(500)).itemOutputs(CircuitComponent.QuantumCPU.getFakeStack(1))
            .duration(20 * SECONDS).eut(TierEU.RECIPE_UXV).addTo(nanochipEncasementWrapper);

        // Planck CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.RealizedQuantumCircuitRack.getFakeStack(1),
                CircuitComponent.ProcessedPlateHexanite.getFakeStack(2),
                CircuitComponent.ProcessedChipYoctoPIC.getFakeStack(8))
            .fluidInputs(Materials.Space.getMolten(1_000)).itemOutputs(CircuitComponent.PlanckCPU.getFakeStack(1))
            .duration(7 * SECONDS).eut(TierEU.RECIPE_MAX).addTo(nanochipEncasementWrapper);
    }

    private static void registerEtchingArrayRecipes() {
        // Raw Advanced Crystal Chip
        addSimpleProcessingRecipe(
            CircuitComponent.ChipRawAdvancedCrystal,
            CircuitComponent.ProcessedChipRawAdvancedCrystal,
            TierEU.RECIPE_UEV,
            16 * SECONDS,
            RecipeMaps.nanochipEtchingArray);

        // Crystal SoC
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalSoC,
            CircuitComponent.ProcessedChipCrystalSoC,
            TierEU.RECIPE_UV,
            40 * SECONDS,
            RecipeMaps.nanochipEtchingArray);

        // Silenced Strands
        addSimpleProcessingRecipe(
            CircuitComponent.EntangledSuppressionStrands,
            CircuitComponent.SilencedStrands,
            TierEU.RECIPE_UMV,
            15 * SECONDS,
            nanochipEtchingArray);

        // Settled Strands
        addSimpleProcessingRecipe(
            CircuitComponent.EntangledAnnihilationStrands,
            CircuitComponent.SettledStrands,
            TierEU.RECIPE_UMV,
            15 * SECONDS,
            nanochipEtchingArray);

        // Pulsating White Dwarf Matter Strands
        addSimpleProcessingRecipe(
            CircuitComponent.MicrocosmicStrands,
            CircuitComponent.PulsatingWhiteDwarfMatterStrands,
            TierEU.RECIPE_UXV,
            4 * SECONDS,
            nanochipEtchingArray);

        // Rebound Black Dwarf Matter Strands
        addSimpleProcessingRecipe(
            CircuitComponent.IsolatedBlackDwarfMatterStrands,
            CircuitComponent.ReboundBlackDwarfMatterStrands,
            TierEU.RECIPE_UXV,
            4 * SECONDS,
            nanochipEtchingArray);

        // Optical Mainframe Rack
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.OpticalMainframe.getFakeStack(2),
                CircuitComponent.ScrewEnrichedNaquadahAlloy.getFakeStack(2))
            .itemOutputs(CircuitComponent.OpticalMainframeRack.getFakeStack(1)).duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UXV).addTo(nanochipEtchingArray);

        // Pico Circuit Rack
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.OrganizedPicoCircuit.getFakeStack(2),
                CircuitComponent.ScrewQuantum.getFakeStack(2))
            .itemOutputs(
                CircuitComponent.PicoCircuitRack.getFakeStack(1),
                CircuitComponent.CompressionResidue.getFakeStack(4))
            .duration(20 * SECONDS).eut(TierEU.RECIPE_UMV).addTo(nanochipEtchingArray);

        // Lost Quantum Circuit Rack
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.OrganizedQuantumCircuit.getFakeStack(2),
                CircuitComponent.ScrewEternity.getFakeStack(2))
            .itemOutputs(
                CircuitComponent.LostQuantumCircuitRack.getFakeStack(1),
                CircuitComponent.CollapsingResidue.getFakeStack(4))
            .duration(13 * SECONDS).eut(TierEU.RECIPE_MAX).addTo(nanochipEtchingArray);
    }

    private static void registerOpticalOrganizerRecipes() {
        // Optical CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipOpticalCPU,
            CircuitComponent.ProcessedChipOpticalCPU,
            TierEU.RECIPE_UEV,
            30 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Memory
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalRAM,
            CircuitComponent.ProcessedOpticalRAM,
            TierEU.RECIPE_UEV,
            15 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Processor
        addSimpleProcessingRecipe(
            CircuitComponent.ProcessedCircuitOpticalProcessor,
            CircuitComponent.OpticalProcessor,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Assembly
        addSimpleProcessingRecipe(
            CircuitComponent.ProcessedCircuitOpticalAssembly,
            CircuitComponent.OpticalAssembly,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Computer
        addSimpleProcessingRecipe(
            CircuitComponent.ProcessedCircuitOpticalComputer,
            CircuitComponent.OpticalComputer,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Mainframe
        addSimpleProcessingRecipe(
            CircuitComponent.ProcessedCircuitOpticalMainframe,
            CircuitComponent.OpticalMainframe,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Organized Pico Circuit
        addSimpleProcessingRecipe(
            CircuitComponent.PicoCircuit,
            CircuitComponent.OrganizedPicoCircuit,
            TierEU.RECIPE_UXV,
            20 * SECONDS,
            nanochipOpticalOrganizer);

        // Organized Quantum Circuit
        addSimpleProcessingRecipe(
            CircuitComponent.QuantumCircuit,
            CircuitComponent.OrganizedQuantumCircuit,
            TierEU.RECIPE_MAX,
            10 * SECONDS,
            nanochipOpticalOrganizer);
    }

    private static void registerPartProcessorRecipes() {
        // ASMDs
        {
            addSimpleProcessingRecipe(
                CircuitComponent.AdvSMDResistor,
                CircuitComponent.ProcessedAdvSMDResistor,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.AdvSMDTransistor,
                CircuitComponent.ProcessedAdvSMDTransistor,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.AdvSMDInductor,
                CircuitComponent.ProcessedAdvSMDInductor,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.AdvSMDCapacitor,
                CircuitComponent.ProcessedAdvSMDCapacitor,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.AdvSMDDiode,
                CircuitComponent.ProcessedAdvSMDDiode,
                TierEU.RECIPE_LuV,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);
        }

        // Optical SMDs
        {
            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDResistor,
                CircuitComponent.ProcessedOpticalSMDResistor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDTransistor,
                CircuitComponent.ProcessedOpticalSMDTransistor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDInductor,
                CircuitComponent.ProcessedOpticalSMDInductor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDCapacitor,
                CircuitComponent.ProcessedOpticalSMDCapacitor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDDiode,
                CircuitComponent.ProcessedOpticalSMDDiode,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);
        }

        // Plate recipes
        {
            // Rhugnor Plate
            GTValues.RA.stdBuilder()
                .itemInputs(
                    CircuitComponent.PlateRhugnor.getFakeStack(1),
                    CircuitComponent.BoltRhugnor.getFakeStack(4))
                .itemOutputs(CircuitComponent.ProcessedPlateRhugnor.getFakeStack(1)).eut(TierEU.RECIPE_ZPM)
                .duration(1 * SECONDS).addTo(nanochipPartProcessorRecipes);

            // Metastable Oganesson Plate
            GTValues.RA.stdBuilder()
                .itemInputs(
                    CircuitComponent.PlateMetastableOganesson.getFakeStack(1),
                    CircuitComponent.BoltMetastableOganesson.getFakeStack(4))
                .itemOutputs(CircuitComponent.ProcessedPlateMetastableOganesson.getFakeStack(1))
                .eut(TierEU.RECIPE_ZPM).duration(1 * SECONDS).addTo(nanochipPartProcessorRecipes);

            // Hexanite Plate
            GTValues.RA.stdBuilder()
                .itemInputs(
                    CircuitComponent.PlateHexanite.getFakeStack(1),
                    CircuitComponent.BoltHexanite.getFakeStack(4))
                .itemOutputs(CircuitComponent.ProcessedPlateHexanite.getFakeStack(1)).eut(TierEU.RECIPE_UV)
                .duration(1 * SECONDS).addTo(nanochipPartProcessorRecipes);
        }

        // Pico Board
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedOpticalSMDInductor.getFakeStack(16),
                CircuitComponent.ProcessedOpticalSMDCapacitor.getFakeStack(16),
                CircuitComponent.ProcessedOpticalSMDResistor.getFakeStack(16),
                CircuitComponent.ProcessedOpticalSMDTransistor.getFakeStack(16),
                CircuitComponent.ProcessedOpticalSMDDiode.getFakeStack(16),
                CircuitComponent.ProcessedBoardOptical.getFakeStack(2))
            .fluidInputs(Materials.Kevlar.getMolten(1 * INGOTS))
            .itemOutputs(CircuitComponent.BoardPico.getFakeStack(1)).duration(40 * SECONDS).eut(TierEU.RECIPE_UMV)
            .addTo(nanochipPartProcessorRecipes);

        // Quantum Board
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedOpticalSMDInductor.getFakeStack(32),
                CircuitComponent.ProcessedOpticalSMDCapacitor.getFakeStack(32),
                CircuitComponent.ProcessedOpticalSMDResistor.getFakeStack(32),
                CircuitComponent.ProcessedOpticalSMDTransistor.getFakeStack(32),
                CircuitComponent.ProcessedOpticalSMDDiode.getFakeStack(32),
                CircuitComponent.ProcessedBoardOptical.getFakeStack(4))
            .fluidInputs(Materials.RadoxPolymer.getMolten(1 * INGOTS), Materials.DTR.getFluid(1000))
            .itemOutputs(CircuitComponent.BoardQuantum.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipPartProcessorRecipes);

        // Disorderly Spool
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedWireTairitsu.getFakeStack(1),
                CircuitComponent.ProcessedWireChuritsu.getFakeStack(2),
                CircuitComponent.ProcessedWireShijima.getFakeStack(1))
            .itemOutputs(CircuitComponent.DisorderlySpool.getFakeStack(1)).duration(8 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipPartProcessorRecipes);

        // Bundled Unity Wire
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.SettledStrands.getFakeStack(1),
                CircuitComponent.AlienatedStrands.getFakeStack(2),
                CircuitComponent.SilencedStrands.getFakeStack(1))
            .itemOutputs(CircuitComponent.BundledUnityWire.getFakeStack(1)).duration(8 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipPartProcessorRecipes);

        // Planck Board
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedOpticalSMDInductor.getFakeStack(64),
                CircuitComponent.ProcessedOpticalSMDCapacitor.getFakeStack(64),
                CircuitComponent.ProcessedOpticalSMDResistor.getFakeStack(64),
                CircuitComponent.ProcessedOpticalSMDTransistor.getFakeStack(64),
                CircuitComponent.ProcessedOpticalSMDDiode.getFakeStack(64),
                CircuitComponent.ProcessedBoardOptical.getFakeStack(8))
            .fluidInputs(
                GGMaterial.shirabon.getMolten(1 * INGOTS),
                Materials.RawStarMatter.getFluid(1000),
                Materials.QuarkGluonPlasma.getFluid(1000))
            .itemOutputs(CircuitComponent.BoardPlanck.getFakeStack(1)).duration(3 * SECONDS).eut(TierEU.RECIPE_MAX)
            .addTo(nanochipPartProcessorRecipes);

        // Supermassive Spool
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedWireWhiteDwarfMatter.getFakeStack(1),
                CircuitComponent.ProcessedWireBlackDwarfMatter.getFakeStack(1),
                CircuitComponent.ProcessedWireUniversium.getFakeStack(1))
            .itemOutputs(CircuitComponent.SupermassiveSpool.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipPartProcessorRecipes);

        // Bundled Stellar Harmony Wire
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.UnboundWhiteDwarfMatterStrands.getFakeStack(1),
                CircuitComponent.ReboundBlackDwarfMatterStrands.getFakeStack(1),
                CircuitComponent.CosmologicalStrands.getFakeStack(1))
            .itemOutputs(CircuitComponent.BundledStellarHarmonyWire.getFakeStack(1)).duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UMV).addTo(nanochipPartProcessorRecipes);
    }

    private static void registerSuperconductorSplitterRecipes() {
        // LuV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            TierEU.RECIPE_UV,
            5 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // ZPM
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorZPM,
            CircuitComponent.ProcessedSuperconductorZPM,
            TierEU.RECIPE_UHV,
            2 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUV,
            CircuitComponent.ProcessedSuperconductorUV,
            TierEU.RECIPE_UHV,
            4 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UHV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUHV,
            CircuitComponent.ProcessedSuperconductorUHV,
            TierEU.RECIPE_UEV,
            2 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UEV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUEV,
            CircuitComponent.ProcessedSuperconductorUEV,
            TierEU.RECIPE_UEV,
            4 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UIV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUIV,
            CircuitComponent.ProcessedSuperconductorUIV,
            TierEU.RECIPE_UIV,
            2 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UMV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUMV,
            CircuitComponent.ProcessedSuperconductorUMV,
            TierEU.RECIPE_UIV,
            4 * SECONDS,
            RecipeMaps.nanochipSuperconductorSplitter);
    }

    private static void registerWireTracerRecipes() {
        // NbTi
        addSimpleProcessingRecipe(
            CircuitComponent.WireNiobiumTitanium,
            CircuitComponent.ProcessedWireNiobiumTitanium,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // YBCO
        addSimpleProcessingRecipe(
            CircuitComponent.WireYttriumBariumCuprate,
            CircuitComponent.ProcessedWireYttriumBariumCuprate,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Lumiium
        addSimpleProcessingRecipe(
            CircuitComponent.WireLumiium,
            CircuitComponent.ProcessedWireLumiium,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Proto Halkonite
        addSimpleProcessingRecipe(
            CircuitComponent.WireProtoHalkonite,
            CircuitComponent.ProcessedWireProtoHalkonite,
            TierEU.RECIPE_ZPM,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Infinity
        addSimpleProcessingRecipe(
            CircuitComponent.WireInfinity,
            CircuitComponent.ProcessedWireInfinity,
            TierEU.RECIPE_ZPM,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Spacetime
        addSimpleProcessingRecipe(
            CircuitComponent.WireSpacetime,
            CircuitComponent.ProcessedWireSpacetime,
            TierEU.RECIPE_UV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Optical Cable
        addSimpleProcessingRecipe(
            CircuitComponent.CableOpticalFiber,
            CircuitComponent.ProcessedCableOpticalFiber,
            TierEU.RECIPE_UEV,
            3 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Hypogen
        addSimpleProcessingRecipe(
            CircuitComponent.WireHypogen,
            CircuitComponent.ProcessedWireHypogen,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Magmatter
        addSimpleProcessingRecipe(
            CircuitComponent.WireMagMatter,
            CircuitComponent.ProcessedWireMagMatter,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Tairitsu
        addSimpleProcessingRecipe(
            CircuitComponent.WireTairitsu,
            CircuitComponent.ProcessedWireTairitsu,
            TierEU.RECIPE_UV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Shijima
        addSimpleProcessingRecipe(
            CircuitComponent.WireShijima,
            CircuitComponent.ProcessedWireShijima,
            TierEU.RECIPE_UV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Churitsu
        addSimpleProcessingRecipe(
            CircuitComponent.WireChuritsu,
            CircuitComponent.ProcessedWireChuritsu,
            TierEU.RECIPE_UV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // White Dwarf
        addSimpleProcessingRecipe(
            CircuitComponent.WireWhiteDwarfMatter,
            CircuitComponent.ProcessedWireWhiteDwarfMatter,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Black Dwarf
        addSimpleProcessingRecipe(
            CircuitComponent.WireBlackDwarfMatter,
            CircuitComponent.ProcessedWireBlackDwarfMatter,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Universium
        addSimpleProcessingRecipe(
            CircuitComponent.WireUniversium,
            CircuitComponent.ProcessedWireUniversium,
            TierEU.RECIPE_UHV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Processing Disorderly Spool
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.DisorderlySpool.getFakeStack(1),
                CircuitComponent.CompressionResidue.getFakeStack(1))
            .itemOutputs(
                CircuitComponent.EntangledSuppressionStrands.getFakeStack(1),
                CircuitComponent.AlienatedStrands.getFakeStack(2),
                CircuitComponent.EntangledAnnihilationStrands.getFakeStack(1))
            .duration(10 * SECONDS).eut(TierEU.RECIPE_UXV).addTo(nanochipWireTracer);

        // Processing Supermassive Spool
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.SupermassiveSpool.getFakeStack(1),
                CircuitComponent.CollapsingResidue.getFakeStack(1))
            .itemOutputs(
                CircuitComponent.MicrocosmicStrands.getFakeStack(1),
                CircuitComponent.MacrocosmicStrands.getFakeStack(1),
                CircuitComponent.CosmologicalStrands.getFakeStack(1))
            .duration(5 * SECONDS).eut(TierEU.RECIPE_MAX).addTo(nanochipWireTracer);

        // Copper
        addSimpleProcessingRecipe(
            CircuitComponent.WireCopper,
            CircuitComponent.ProcessedWireCopper,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Red Alloy
        addSimpleProcessingRecipe(
            CircuitComponent.WireRedAlloy,
            CircuitComponent.ProcessedWireRedAlloy,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Electrum
        addSimpleProcessingRecipe(
            CircuitComponent.WireElectrum,
            CircuitComponent.ProcessedWireElectrum,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);

        // Platinum
        addSimpleProcessingRecipe(
            CircuitComponent.WirePlatinum,
            CircuitComponent.ProcessedWirePlatinum,
            TierEU.RECIPE_LuV,
            1 * SECONDS,
            RecipeMaps.nanochipWireTracer);
    }
}
