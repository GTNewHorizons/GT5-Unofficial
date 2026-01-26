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
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.util.GTRecipeBuilder;
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

    private static void addAssemblyMatrixRecipe(List<CircuitComponentStack> input, List<FluidStack> fluidInputs,
        CircuitComponent output, int duration, long eut, int recipeTier) {
        if (output.realComponent == null) {
            throw new IllegalArgumentException("No real circuit was defined for given output!");
        }

        ItemStack realOutput = output.realComponent.get();
        realOutput.stackSize = 1;

        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .fluidInputs(fluidInputs.toArray(new FluidStack[] {}))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut);

        // Add real recipe that will actually be utilized in recipe checks
        ItemStack[] inputsWithFakeCircuits = input.stream()
            .map(
                c -> c.getCircuitComponent()
                    .getFakeStack(c.getSize()))
            .toArray(ItemStack[]::new);

        builder.copy()
            .hidden()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .itemInputs(inputsWithFakeCircuits)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);

        // Add fake recipe that the user can see in NEI but will never actually be used for recipe checks
        ItemStack[] inputsWithRealCircuits = input.stream()
            .map(c -> {
                if (c.getCircuitComponent().realComponent != null) {
                    ItemStack realCircuit = c.getCircuitComponent().realComponent.get();
                    realCircuit.stackSize = c.getSize();
                    return realCircuit;
                }
                return c.getCircuitComponent()
                    .getFakeStack(c.getSize());
            })
            .toArray(ItemStack[]::new);

        builder.copy()
            .fake()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .itemInputs(inputsWithRealCircuits)
            .itemOutputs(realOutput)
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
                .eut(TierEU.RECIPE_UHV)
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

        // Bio Processing Unit
        addSimpleProcessingRecipe(
            CircuitComponent.BioProcessingUnit,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBioProcessingUnit,
            ModuleRecipeInfo.ExtremeTier,
            20 * TICKS,
            RecipeMaps.nanochipBiologicalCoordinator);
    }

    // todo fluid metadata
    private static void registerBoardProcessorRecipes() {
        // Elite Board
        GTValues.RA.stdBuilder()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 1)
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.HighTier.recipeEUt)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Wetware Board
        addSimpleProcessingRecipe(
            CircuitComponent.BoardWetwareLifesupport,
            Materials.GrowthMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardWetwareLifesupport,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipBoardProcessorRecipes);

        // Bio Board
        addSimpleProcessingRecipe(
            CircuitComponent.BoardBioMutated,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardBioMutated,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipBoardProcessorRecipes);

        // Optical Board
        addSimpleProcessingRecipe(
            CircuitComponent.BoardOptical,
            Materials.MysteriousCrystal.getMolten(1000),
            CircuitComponent.ProcessedBoardOptical,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipBoardProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.BoardOptical,
            Materials.MysteriousCrystal.getMolten(1000),
            CircuitComponent.ProcessedBoardOptical,
            ModuleRecipeInfo.HighTier,
            20 * TICKS,
            RecipeMaps.nanochipBoardProcessorRecipes);
    }

    private static void registerCuttingChamberRecipes() {
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
    }

    // todo finish migrating recipes to encasement wrapper
    private static void registerEncasementWrapperRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                CircuitComponent.ProcessedFoilSiliconeRubber.getFakeStack(16),
                CircuitComponent.ProcessedFrameboxTritanium.getFakeStack(1),
                CircuitComponent.ProcessedFoilPolybenzimidazole.getFakeStack(16))
            .itemOutputs(CircuitComponent.AdvancedMainframeCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.MediumTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        GTValues.RA.stdBuilder()
            .itemInputs(CircuitComponent.ProcessedFrameboxAluminium.getFakeStack(2))
            .itemOutputs(CircuitComponent.BasicMainframeCasing.getFakeStack(1))
            .duration(20)
            .eut(ModuleRecipeInfo.MediumTier.recipeEUt)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

        // Frame box processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxAluminium,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxAluminium,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxTritanium,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxTritanium,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxNeutronium,
            Materials.Grade1PurifiedWater.getFluid(500),
            CircuitComponent.ProcessedFrameboxNeutronium,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);

        // Foil processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FoilPolybenzimidazole,
            CircuitComponent.ProcessedFoilPolybenzimidazole,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipEncasementWrapper);
        addSimpleProcessingRecipe(
            CircuitComponent.FoilSiliconeRubber,
            CircuitComponent.ProcessedFoilSiliconeRubber,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipEncasementWrapper);
        addSimpleProcessingRecipe(
            CircuitComponent.FoilRadoxPolymer,
            CircuitComponent.ProcessedFoilRadoxPolymer,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipEncasementWrapper);
        addSimpleProcessingRecipe(
            CircuitComponent.FoilShirabon,
            CircuitComponent.ProcessedFoilShirabon,
            ModuleRecipeInfo.ExtremeTier,
            20,
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

        // Advanced Crystal CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipAdvCrystalCPU,
            CircuitComponent.ProcessedChipAdvCrystalCPU,
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
        // SMDs
        addSimpleProcessingRecipe(
            CircuitComponent.SMDResistor,
            CircuitComponent.ProcessedSMDResistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.SMDTransistor,
            CircuitComponent.ProcessedSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.SMDInductor,
            CircuitComponent.ProcessedSMDInductor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.SMDCapacitor,
            CircuitComponent.ProcessedSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.SMDDiode,
            CircuitComponent.ProcessedSMDDiode,
            ModuleRecipeInfo.LowTier,
            20 * TICKS,
            RecipeMaps.nanochipSMDProcessorRecipes);

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

        // Lanthanum
        addSimpleProcessingRecipe(
            CircuitComponent.WireLanthanum,
            CircuitComponent.ProcessedWireLanthanum,
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
    }

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
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(INGOTS / 2)),
            CircuitComponent.CrystalProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS)),
            CircuitComponent.CrystalAssembly,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNAND, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(INGOTS / 2)),
            CircuitComponent.CrystalComputer,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.BasicMainframeCasing, 1),
                new CircuitComponentStack(CircuitComponent.CrystalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorLuV, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS)),
            CircuitComponent.CrystalMainframe,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedNeuroProcessingUnit, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 8)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(INGOTS / 2)),
            CircuitComponent.WetwareProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 1),
                new CircuitComponentStack(CircuitComponent.WetwareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS)),
            CircuitComponent.WetwareAssembly,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 2),
                new CircuitComponentStack(CircuitComponent.WetwareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS)),
            CircuitComponent.WetwareComputer,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.AdvancedMainframeCasing, 2),
                new CircuitComponentStack(CircuitComponent.WetwareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 48),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorZPM, 64)),
            Arrays.asList(
                MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                Materials.Radon.getGas(2500)),
            CircuitComponent.WetwareMainframe,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBioProcessingUnit, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipAdvCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(INGOTS / 2)),
            CircuitComponent.BiowareProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 1),
                new CircuitComponentStack(CircuitComponent.BiowareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS)),
            CircuitComponent.BiowareAssembly,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 2),
                new CircuitComponentStack(CircuitComponent.BiowareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440),
                Materials.SuperCoolant.getFluid(10000)),
            CircuitComponent.BiowareComputer,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.AdvancedMainframeCasing, 4),
                new CircuitComponentStack(CircuitComponent.BiowareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUHV, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880),
                Materials.SuperCoolant.getFluid(20000)),
            CircuitComponent.BiowareMainframe,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedChipOpticalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalRAM, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.CableOpticalFiber, 4),
                new CircuitComponentStack(CircuitComponent.BoltEnrichedHolmium, 16)),
            Arrays.asList(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(288)),
            CircuitComponent.OpticalProcessor,
            5 * TICKS,
            TierEU.RECIPE_LuV,
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
                new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.Radon.getGas(1440),
                Materials.SuperCoolant.getFluid(10000),
                WerkstoffLoader.Oganesson.getFluidOrGas(500)),
            CircuitComponent.OpticalAssembly,
            5 * TICKS,
            TierEU.RECIPE_LuV,
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
                new CircuitComponentStack(CircuitComponent.ProcessedFoilSiliconeRubber, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedFoilPolybenzimidazole, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.Radon.getGas(2880),
                Materials.SuperCoolant.getFluid(20000),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000)),
            CircuitComponent.OpticalComputer,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.AdvancedMainframeCasing, 8),
                new CircuitComponentStack(CircuitComponent.OpticalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipASOC, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUEV, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(5760),
                Materials.Radon.getGas(5760),
                Materials.SuperCoolant.getFluid(40000),
                WerkstoffLoader.Oganesson.getFluidOrGas(2000)),
            CircuitComponent.OpticalMainframe,
            5 * TICKS,
            TierEU.RECIPE_LuV,
            VoltageIndex.UXV);

        if (NewHorizonsCoreMod.isModLoaded()) {
            addAssemblyMatrixRecipe(
                Arrays.asList(
                    new CircuitComponentStack(CircuitComponent.BoardOptical, 1),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipPico, 4),
                    new CircuitComponentStack(CircuitComponent.OpticalMainframe, 2),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 48),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipPikoPIC, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedFoilRadoxPolymer, 16),
                    new CircuitComponentStack(CircuitComponent.BoltTranscendentMetal, 32),
                    new CircuitComponentStack(CircuitComponent.BoltNeutronium, 16),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireLanthanum, 64)),
                Arrays.asList(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(26 * INGOTS),
                    Materials.UUMatter.getFluid(8000),
                    Materials.Osmium.getMolten(8 * INGOTS)),
                CircuitComponent.PicoCircuit,
                5 * TICKS,
                TierEU.RECIPE_LuV,
                VoltageIndex.UHV);

            addAssemblyMatrixRecipe(
                Arrays.asList(
                    new CircuitComponentStack(CircuitComponent.ProcessedFrameboxNeutronium, 16),
                    new CircuitComponentStack(CircuitComponent.PicoCircuit, 2),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedChipQuantumPIC, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedFoilShirabon, 64),
                    new CircuitComponentStack(CircuitComponent.BoltIndium, 64),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireSpacetime, 8),
                    new CircuitComponentStack(CircuitComponent.ProcessedWireLanthanum, 16)),
                Arrays.asList(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(26 * INGOTS),
                    Materials.UUMatter.getFluid(24000),
                    Materials.Osmium.getMolten(16 * INGOTS)),
                CircuitComponent.QuantumCircuit,
                5 * TICKS,
                TierEU.RECIPE_LuV,
                VoltageIndex.UHV);
        }
    }
}
