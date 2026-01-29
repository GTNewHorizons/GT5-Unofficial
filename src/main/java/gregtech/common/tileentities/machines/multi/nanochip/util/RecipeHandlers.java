package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.common.powergoggles.PowerGogglesConstants.SECONDS;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent.CircuitComponentStack;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;

public class RecipeHandlers {

    // Adds a simple processing recipe for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output,
        ModuleRecipeInfo info, int duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .itemInputs(input.getFakeStack(1))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(info.recipeEUt)
            .addTo(recipeMap);
    }

    // Adds a simple processing recipe with a fluid for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, FluidStack inputStack,
        CircuitComponent output, ModuleRecipeInfo info, long duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .itemInputs(input.getFakeStack(1))
            .fluidInputs(inputStack)
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(info.recipeEUt)
            .addTo(recipeMap);
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
            if (inputThing instanceof CircuitComponentStack ccStack) {
                inputs[i] = ccStack.getCircuitComponent()
                    .getFakeStack(ccStack.getSize());
            } else if (inputThing instanceof Object[]alts) {
                inputs[i] = Arrays.stream(alts)
                    .map(c -> {
                        if (c instanceof CircuitComponentStack ccStack) {
                            return ccStack.getCircuitComponent()
                                .getFakeStack(ccStack.getSize());
                        } else if (c instanceof ItemStack stack) {
                            return stack;
                        }
                        throw new IllegalArgumentException("Must pass a CircuitComponentStack or ItemStack array!");
                    })
                    .toArray(ItemStack[]::new);
            }
        }

        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .itemInputs(inputs)
            .fluidInputs(fluidInputs.toArray(new FluidStack[0]))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }

    public static void registerNanochipModuleRecipes() {
        registerConversionRecipes();

        registerBiologicalCoordinatorRecipes();
        registerBoardProcessorRecipes();
        registerCuttingChamberRecipes();
        registerEncasementWrapperRecipes();
        registerEtchingArrayRecipes();
        registerOpticalOrganizerRecipes();
        registerSMDProcessorRecipes();
        registerSuperconductorSplitterRecipes();
        registerWireTracerRecipes();

        registerAssemblyMatrixRecipes();
    }

    private static void registerConversionRecipes() {
        for (CircuitComponent cc : CircuitComponent.VALUES) {
            if (cc.isProcessed) continue;
            if (cc.realComponent == null) throw new IllegalStateException("CC must define its representative item!");

            GTValues.RA.stdBuilder()
                .itemInputs(cc.realComponent.get())
                .itemOutputs(cc.getFakeStack(1))
                .duration(5 * SECONDS)
                .eut(0)
                .addTo(RecipeMaps.nanochipConversionRecipes);
        }
    }

    private static void registerBiologicalCoordinatorRecipes() {
        // Neuro Processing Unit
        addSimpleProcessingRecipe(
            CircuitComponent.NeuroProcessingUnit,
            Materials.GrowthMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedNeuroProcessingUnit,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipBiologicalCoordinator);

        // Living Crystal Chip
        addSimpleProcessingRecipe(
            CircuitComponent.ChipLivingCrystal,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedChipLivingCrystal,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipBiologicalCoordinator);

        // Bio Processing Unit
        addSimpleProcessingRecipe(
            CircuitComponent.BioProcessingUnit,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBioProcessingUnit,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipBiologicalCoordinator);

        // Living Bio Chip
        addSimpleProcessingRecipe(
            CircuitComponent.LivingBioChip,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedLivingBioChip,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipBiologicalCoordinator);
    }

    // todo fluid metadata
    private static void registerBoardProcessorRecipes() {
        // Elite Board
        GTValues.RA.stdBuilder()
            .hidden()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 1)
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Elite Board fake recipe
        GTValues.RA.stdBuilder()
            .fake()
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .fluidInputs(Materials.IronIIIChloride.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Wetware Board
        GTValues.RA.stdBuilder()
            .hidden()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 2)
            .itemInputs(CircuitComponent.BoardWetwareLifesupport.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardWetwareLifesupport.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Wetware Board fake recipe
        GTValues.RA.stdBuilder()
            .fake()
            .itemInputs(CircuitComponent.BoardWetwareLifesupport.getFakeStack(1))
            .fluidInputs(Materials.GrowthMediumSterilized.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardWetwareLifesupport.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Bio Board
        GTValues.RA.stdBuilder()
            .hidden()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 3)
            .itemInputs(CircuitComponent.BoardBioMutated.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardBioMutated.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Bio Board fake recipe
        GTValues.RA.stdBuilder()
            .fake()
            .itemInputs(CircuitComponent.BoardBioMutated.getFakeStack(1))
            .fluidInputs(Materials.BioMediumSterilized.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardBioMutated.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Optical Board
        GTValues.RA.stdBuilder()
            .hidden()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 4)
            .itemInputs(CircuitComponent.BoardOptical.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardOptical.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Optical Board fake recipe
        GTValues.RA.stdBuilder()
            .fake()
            .itemInputs(CircuitComponent.BoardOptical.getFakeStack(1))
            .fluidInputs(Materials.PrismaticAcid.getFluid(0))
            .itemOutputs(CircuitComponent.ProcessedBoardOptical.getFakeStack(1))
            .duration(5 * SECONDS)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);
    }

    private static void registerCuttingChamberRecipes() {
        // Wafers
        // CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNanoCPU,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipNanoCPU,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // RAM
        addSimpleProcessingRecipe(
            CircuitComponent.ChipRAM,
            Materials.Lubricant.getFluid(67),
            CircuitComponent.ProcessedChipRAM,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // NOR
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNOR,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNOR,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // NAND
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNAND,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNAND,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // ASoC
        addSimpleProcessingRecipe(
            CircuitComponent.ChipASOC,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipASOC,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // PPIC
        addSimpleProcessingRecipe(
            CircuitComponent.ChipPikoPIC,
            Materials.Lubricant.getFluid(375),
            CircuitComponent.ProcessedChipPikoPIC,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // QPIC
        addSimpleProcessingRecipe(
            CircuitComponent.ChipQuantumPIC,
            Materials.Lubricant.getFluid(500),
            CircuitComponent.ProcessedChipQuantumPIC,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Pico
        addSimpleProcessingRecipe(
            CircuitComponent.WaferPico,
            Materials.DimensionallyShiftedSuperfluid.getFluid(10),
            CircuitComponent.ProcessedChipPico,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Bolts
        // Enriched Holmium
        addSimpleProcessingRecipe(
            CircuitComponent.BoltEnrichedHolmium,
            Materials.Lubricant.getFluid(20),
            CircuitComponent.ProcessedBoltEnrichedHolmium,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Transcendent Metal
        addSimpleProcessingRecipe(
            CircuitComponent.BoltTranscendentMetal,
            Materials.Lubricant.getFluid(20),
            CircuitComponent.ProcessedBoltTranscendentMetal,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Yttrium Barium Cuprate
        addSimpleProcessingRecipe(
            CircuitComponent.BoltYttriumBariumCuprate,
            Materials.Lubricant.getFluid(20),
            CircuitComponent.ProcessedBoltYttriumBariumCuprate,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Cosmic Neutronium
        addSimpleProcessingRecipe(
            CircuitComponent.BoltCosmicNeutronium,
            Materials.Lubricant.getFluid(20),
            CircuitComponent.ProcessedBoltCosmicNeutronium,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Chromatic Glass
        addSimpleProcessingRecipe(
            CircuitComponent.BoltChromaticGlass,
            Materials.Lubricant.getFluid(20),
            CircuitComponent.ProcessedBoltChromaticGlass,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Frame box recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxAluminium,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxAluminium,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxTritanium,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxTritanium,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxCelestialTungsten,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxCelestialTungsten,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxHypogen,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxHypogen,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);

        // Plate recipes
        addSimpleProcessingRecipe(
            CircuitComponent.PlateMetastableOganesson,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedPlateMetastableOganesson,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipCuttingChamber);
    }

    private static void registerEncasementWrapperRecipes() {
        // Crystal
        GTValues.RA.stdBuilder()
            .itemInputs(CircuitComponent.ProcessedFrameboxAluminium.getFakeStack(2))
            .itemOutputs(CircuitComponent.ProcessedBasicMainframeCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.MediumTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        // Wetware, Bioware, Optical
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilSiliconeRubber.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxTritanium.getFakeStack(1),
                CircuitComponent.ProcessedFoilPolybenzimidazole.getFakeStack(16))
            .itemOutputs(CircuitComponent.ProcessedAdvancedMainframeCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.MediumTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilStyreneRubber.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxTritanium.getFakeStack(1),
                CircuitComponent.ProcessedFoilPolybenzimidazole.getFakeStack(16))
            .itemOutputs(CircuitComponent.ProcessedAdvancedMainframeCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.MediumTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        // Pico
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFrameboxCelestialTungsten.getFakeStack(2),
                CircuitComponent.ScrewAstralTitanium.getFakeStack(4),
                CircuitComponent.CasingUEVSuperconductor.getFakeStack(1),
                CircuitComponent.ProcessedFoilRadoxPolymer.getFakeStack(16))
            .itemOutputs(CircuitComponent.ProcessedPicoCircuitCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.ExtremeTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        // Quantum
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFrameboxHypogen.getFakeStack(2),
                CircuitComponent.ScrewSixPhasedCopper.getFakeStack(4),
                CircuitComponent.CasingCreon.getFakeStack(1),
                CircuitComponent.ProcessedFoilShirabon.getFakeStack(48))
            .itemOutputs(CircuitComponent.ProcessedQuantumCircuitCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.ExtremeTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        // Foil processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FoilPolybenzimidazole,
            CircuitComponent.ProcessedFoilPolybenzimidazole,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipEncasementWrapper);

        addSimpleProcessingRecipe(
            CircuitComponent.FoilSiliconeRubber,
            CircuitComponent.ProcessedFoilSiliconeRubber,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipEncasementWrapper);

        addSimpleProcessingRecipe(
            CircuitComponent.FoilRadoxPolymer,
            CircuitComponent.ProcessedFoilRadoxPolymer,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipEncasementWrapper);

        addSimpleProcessingRecipe(
            CircuitComponent.FoilShirabon,
            CircuitComponent.ProcessedFoilShirabon,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipEncasementWrapper);

        addSimpleProcessingRecipe(
            CircuitComponent.FoilStyreneRubber,
            CircuitComponent.ProcessedFoilStyreneRubber,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipEncasementWrapper);
    }

    private static void registerEtchingArrayRecipes() {
        // Crystal CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalCPU,
            CircuitComponent.ProcessedChipCrystalCPU,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipEtchingArray);

        // Crystal SoC
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalSoC,
            CircuitComponent.ProcessedChipCrystalSoC,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipEtchingArray);
    }

    private static void registerOpticalOrganizerRecipes() {
        // Optical CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipOpticalCPU,
            CircuitComponent.ProcessedChipOpticalCPU,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipOpticalOrganizer);

        // Optical Memory
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalRAM,
            CircuitComponent.ProcessedOpticalRAM,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipOpticalOrganizer);
    }

    private static void registerSMDProcessorRecipes() {
        // ASMDs
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDResistor,
            CircuitComponent.ProcessedAdvSMDResistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDTransistor,
            CircuitComponent.ProcessedAdvSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDInductor,
            CircuitComponent.ProcessedAdvSMDInductor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDCapacitor,
            CircuitComponent.ProcessedAdvSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDDiode,
            CircuitComponent.ProcessedAdvSMDDiode,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        // Optical SMDs
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDResistor,
            CircuitComponent.ProcessedOpticalSMDResistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDTransistor,
            CircuitComponent.ProcessedOpticalSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDInductor,
            CircuitComponent.ProcessedOpticalSMDInductor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDCapacitor,
            CircuitComponent.ProcessedOpticalSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDDiode,
            CircuitComponent.ProcessedOpticalSMDDiode,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);
    }

    private static void registerSuperconductorSplitterRecipes() {
        // LuV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // ZPM
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorZPM,
            CircuitComponent.ProcessedSuperconductorZPM,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUV,
            CircuitComponent.ProcessedSuperconductorUV,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UHV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUHV,
            CircuitComponent.ProcessedSuperconductorUHV,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UEV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUEV,
            CircuitComponent.ProcessedSuperconductorUEV,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UIV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUIV,
            CircuitComponent.ProcessedSuperconductorUIV,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);

        // UMV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUMV,
            CircuitComponent.ProcessedSuperconductorUMV,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipSuperconductorSplitter);
    }

    private static void registerWireTracerRecipes() {
        // NbTi
        addSimpleProcessingRecipe(
            CircuitComponent.WireNiobiumTitanium,
            CircuitComponent.ProcessedWireNiobiumTitanium,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // YBCO
        addSimpleProcessingRecipe(
            CircuitComponent.WireYttriumBariumCuprate,
            CircuitComponent.ProcessedWireYttriumBariumCuprate,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // Lumiium
        addSimpleProcessingRecipe(
            CircuitComponent.WireLumiium,
            CircuitComponent.ProcessedWireLumiium,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // Proto Halkonite
        addSimpleProcessingRecipe(
            CircuitComponent.WireProtoHalkonite,
            CircuitComponent.ProcessedWireProtoHalkonite,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // Infinity
        addSimpleProcessingRecipe(
            CircuitComponent.WireInfinity,
            CircuitComponent.ProcessedWireInfinity,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // Spacetime
        addSimpleProcessingRecipe(
            CircuitComponent.WireSpacetime,
            CircuitComponent.ProcessedWireSpacetime,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);

        // Optical Cable
        addSimpleProcessingRecipe(
            CircuitComponent.CableOpticalFiber,
            CircuitComponent.ProcessedCableOpticalFiber,
            ModuleRecipeInfo.MediumTier,
            20 * TICKS,
            RecipeMaps.nanochipWireTracer);
    }

    // spotless:off
    // todo finalize recipes and fill in missing ones
    private static void registerAssemblyMatrixRecipes() {
        // TODO: dummy recipes
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.CrystalProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.OpticalProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.BiowareProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.WetwareProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedWireLumiium, 1),
                new Object[] {
                    new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 2),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireProtoHalkonite, 1)
                }),
            Arrays.asList(Materials.Iron.getMolten(1)),
            CircuitComponent.WetwareProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);

        // ======= //
        // Crystal //
        // ======= //
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.CrystalProcessor,
            4 * SECONDS,
            9_600, // LuV
            VoltageIndex.UHV);

        // SoC
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalSoC, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedBoltYttriumBariumCuprate, 4)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.CrystalProcessor,
            2 * SECONDS,
            153_600, // UV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.CrystalAssembly,
            8 * SECONDS,
            9_600, // LuV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNAND, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.CrystalComputer,
            16 * SECONDS,
            9_600, // LuV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBasicMainframeCasing, 1),
                new CircuitComponentStack(CircuitComponent.CrystalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorLuV, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(18)),
            CircuitComponent.CrystalMainframe,
            32 * SECONDS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);

        // ======= //
        // Wetware //
        // ======= //

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedNeuroProcessingUnit, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 8)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.WetwareProcessor,
            1 * SECONDS,
            153_600, // UV
            VoltageIndex.UHV);

        // SoC
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipLivingCrystal, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedBoltCosmicNeutronium, 4)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.WetwareProcessor,
            2 * SECONDS,
            614_400, // UHV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 1),
                new CircuitComponentStack(CircuitComponent.WetwareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 3),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.WetwareAssembly,
            1 * SECONDS + 10 * TICKS,
            153_600, // UV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 2),
                new CircuitComponentStack(CircuitComponent.WetwareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.WetwareComputer,
            3 * SECONDS,
            153_600, // UV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedAdvancedMainframeCasing, 2),
                new CircuitComponentStack(CircuitComponent.WetwareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 48),
                new Object[] {
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorZPM, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUV, 32),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUHV, 16),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUEV, 8),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUIV, 4),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUMV, 2),
                }),
            Arrays.asList(
                MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS),
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                Materials.Radon.getGas(2500)),
            CircuitComponent.WetwareMainframe,
            100 * SECONDS,
            300_000, // UV
            VoltageIndex.UHV);

        // ======= //
        // Bioware //
        // ======= //

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBioProcessingUnit, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRawAdvancedCrystal, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 3),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 3),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.BiowareProcessor,
            2 * SECONDS + 10 * TICKS,
            614_400, // UHV
            VoltageIndex.UHV);

        // SoC
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedLivingBioChip, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedBoltChromaticGlass, 4)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.BiowareProcessor,
            3 * SECONDS,
            2_457_600, // UEV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 1),
                new CircuitComponentStack(CircuitComponent.BiowareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 3),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(9)),
            CircuitComponent.BiowareAssembly,
            2 * SECONDS,
            614_400, // UHV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 2),
                new CircuitComponentStack(CircuitComponent.BiowareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32),
                new Object[] {new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64), new CircuitComponentStack(CircuitComponent.ProcessedFoilStyreneRubber,64)}),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS),
                Materials.BioMediumSterilized.getFluid(10 * INGOTS),
                Materials.SuperCoolant.getFluid(10000)),
            CircuitComponent.BiowareComputer,
            200 * SECONDS,
            TierEU.RECIPE_UV,
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedAdvancedMainframeCasing, 4),
                new CircuitComponentStack(CircuitComponent.BiowareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new Object[] {
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUHV, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUEV, 32),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUIV, 16),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUMV, 8),
                }),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.BioMediumSterilized.getFluid(20 * INGOTS),
                Materials.SuperCoolant.getFluid(20000)),
            CircuitComponent.BiowareMainframe,
            300 * SECONDS,
            TierEU.RECIPE_UHV,
            VoltageIndex.UHV);

        // ======= //
        // Optical //
        // ======= //

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedChipOpticalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalRAM, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedCableOpticalFiber, 4),
                new CircuitComponentStack(CircuitComponent.ProcessedBoltEnrichedHolmium, 4)),
            Arrays.asList(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18)),
            CircuitComponent.OpticalProcessor,
            15 * SECONDS,
            614_400, // UHV
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardOptical, 1),
                new CircuitComponentStack(CircuitComponent.OpticalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 20),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 20),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireLumiium, 24),
                new Object[] {new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64), new CircuitComponentStack(CircuitComponent.ProcessedFoilStyreneRubber,64)}),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS),
                Materials.Radon.getGas(10 * INGOTS),
                Materials.SuperCoolant.getFluid(10000),
                WerkstoffLoader.Oganesson.getFluidOrGas(500)),
            CircuitComponent.OpticalAssembly,
            20 * SECONDS,
            TierEU.RECIPE_UHV,
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardOptical, 2),
                new CircuitComponentStack(CircuitComponent.OpticalAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedChipASOC, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedWireLumiium, 32),
                new Object[] {new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64), new CircuitComponentStack(CircuitComponent.ProcessedFoilStyreneRubber,64)},
                new CircuitComponentStack(CircuitComponent.ProcessedFoilPolybenzimidazole, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.Radon.getGas(20 * INGOTS),
                Materials.SuperCoolant.getFluid(20000),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000)),
            CircuitComponent.OpticalComputer,
            200 * SECONDS,
            TierEU.RECIPE_UHV,
            VoltageIndex.UHV);

        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedAdvancedMainframeCasing, 8),
                new CircuitComponentStack(CircuitComponent.OpticalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipASOC, 64),
                new Object[] {
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUEV, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUIV, 32),
                    new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUMV, 16),
                }),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS),
                Materials.Radon.getGas(40 * INGOTS),
                Materials.SuperCoolant.getFluid(40000),
                WerkstoffLoader.Oganesson.getFluidOrGas(2000)),
            CircuitComponent.OpticalMainframe,
            300 * SECONDS,
            TierEU.RECIPE_UEV,
            VoltageIndex.UXV);

        if (NewHorizonsCoreMod.isModLoaded()) {
            addAssemblyMatrixRecipe(
                Arrays.asList(
                    new CircuitComponentStack(CircuitComponent.ProcessedPicoCircuitCasing, 1),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipPico, 4),
                    new CircuitComponentStack(CircuitComponent.OpticalMainframe, 2),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipPikoPIC, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireProtoHalkonite, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedBoltTranscendentMetal, 32),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireInfinity, 8)),
                Arrays.asList(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                    Materials.UUMatter.getFluid(8000),
                    GGMaterial.preciousMetalAlloy.getMolten(8 * INGOTS)),
                CircuitComponent.PicoCircuit,
                500 * SECONDS,
                TierEU.RECIPE_UMV,
                VoltageIndex.UHV);

            addAssemblyMatrixRecipe(
                Arrays.asList(
                    new CircuitComponentStack(CircuitComponent.ProcessedQuantumCircuitCasing, 1),
                    new CircuitComponentStack(CircuitComponent.ProcessedPlateMetastableOganesson, 2),
                    new CircuitComponentStack(CircuitComponent.PicoCircuit, 2),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipQuantumPIC, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireProtoHalkonite, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedBoltUMVSuperconductor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireSpacetime, 8)),
                Arrays.asList(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                    Materials.UUMatter.getFluid(24000),
                    GGMaterial.preciousMetalAlloy.getMolten(16 * INGOTS)),
                CircuitComponent.QuantumCircuit,
                1000 * SECONDS,
                TierEU.RECIPE_UMV,
                VoltageIndex.UHV);
        }
    }
    // spotless:on
}
