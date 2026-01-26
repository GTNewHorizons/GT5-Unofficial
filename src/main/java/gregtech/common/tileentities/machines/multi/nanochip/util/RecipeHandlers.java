package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.common.powergoggles.PowerGogglesConstants.SECONDS;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.recipe.metadata.NanochipAssemblyRecipeInfo;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent.CircuitComponentStack;
import gtPlusPlus.core.material.MaterialMisc;

public class RecipeHandlers {

    // Adds a simple processing recipe for circuit components in a module. The recipe map used for processing is
    // inferred from the map stored by the input component.
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output,
        ModuleRecipeInfo info, int duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .itemInputs(input.getFakeStack(1))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(info.recipeEUt)
            .addTo(recipeMap);
    }

    // Adds a simple processing recipe with a fluid for circuit components in a module. The recipe map
    // used for processing is inferred from the map stored by the input component.
    private static void addSimpleProcessingRecipe(CircuitComponent input, FluidStack inputStack,
        CircuitComponent output, ModuleRecipeInfo info, long eut, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .itemInputs(input.getFakeStack(1))
            .fluidInputs(inputStack)
            .itemOutputs(output.getFakeStack(1))
            .duration(20)
            .eut(eut)
            .addTo(recipeMap);
    }

    // Adds a board processing recipe with a fluid as metadata.
    private static void addBoardProcessingRecipe(CircuitComponent input, Integer fluidType, CircuitComponent output,
        ModuleRecipeInfo info, long eut) {
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, fluidType)
            .itemInputs(input.getFakeStack(1))
            .itemOutputs(output.getFakeStack(1))
            .duration(20)
            .eut(eut)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);
    }

    private static void addAssemblyMatrixRecipe(List<CircuitComponentStack> input, List<FluidStack> fluidInputs,
        CircuitComponent output, int duration, long eut, int recipeTier) {
        if (output.realComponent == null) {
            throw new IllegalArgumentException("No real circuit was defined for given output!");
        }
        ItemStack realOutput = output.realComponent.get();
        realOutput.stackSize = 1;
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
        ItemStack[] inputsWithFakeCircuits = input.stream()
            .map(
                c -> c.getCircuitComponent()
                    .getFakeStack(c.getSize()))
            .toArray(ItemStack[]::new);
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            // .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .fluidInputs(fluidInputs.toArray(new FluidStack[] {}))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut);
        // Add real recipe that will actually be utilized in recipe checks
        builder.copy()
            .hidden()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .itemInputs(inputsWithFakeCircuits)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
        // Add fake recipe that the user can see in NEI but will never actually be used for recipe checks
        builder.copy()
            .fake()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, recipeTier)
            .itemInputs(inputsWithRealCircuits)
            .itemOutputs(realOutput)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }

    public static void populateCircuitComponentRecipeMaps() {
        registerConversionRecipes();
        registerWireRecipes();
        registerBoardRecipes();
        registerCuttingRecipes();
        registerEtchingRecipes();
        registerOpticalRecipes();
        registerSuperconductorRecipes();
        registerFrameBoxRecipes();
        registerCasingRecipes();
    }

    public static void registerConversionRecipes() {
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

    public static void registerWireRecipes() {
        // Processing recipes
        // Wire processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.WireNiobiumTitanium,
            CircuitComponent.ProcessedWireNiobiumTitanium,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipWireTracer);
        addSimpleProcessingRecipe(
            CircuitComponent.WireYttriumBariumCuprate,
            CircuitComponent.ProcessedWireYttriumBariumCuprate,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipWireTracer);
        addSimpleProcessingRecipe(
            CircuitComponent.WireLumiium,
            CircuitComponent.ProcessedWireLumiium,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipWireTracer);
        addSimpleProcessingRecipe(
            CircuitComponent.WireLanthanum,
            CircuitComponent.ProcessedWireLanthanum,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipWireTracer);
        addSimpleProcessingRecipe(
            CircuitComponent.WireSpacetime,
            CircuitComponent.ProcessedWireSpacetime,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipWireTracer);

        // SMD processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.SMDResistor,
            CircuitComponent.ProcessedSMDResistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDTransistor,
            CircuitComponent.ProcessedSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDInductor,
            CircuitComponent.ProcessedSMDInductor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDCapacitor,
            CircuitComponent.ProcessedSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDDiode,
            CircuitComponent.ProcessedSMDDiode,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDResistor,
            CircuitComponent.ProcessedAdvSMDResistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDTransistor,
            CircuitComponent.ProcessedAdvSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDInductor,
            CircuitComponent.ProcessedAdvSMDInductor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDCapacitor,
            CircuitComponent.ProcessedAdvSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDDiode,
            CircuitComponent.ProcessedAdvSMDDiode,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDResistor,
            CircuitComponent.ProcessedOpticalSMDResistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDTransistor,
            CircuitComponent.ProcessedOpticalSMDTransistor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDInductor,
            CircuitComponent.ProcessedOpticalSMDInductor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDCapacitor,
            CircuitComponent.ProcessedOpticalSMDCapacitor,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDDiode,
            CircuitComponent.ProcessedOpticalSMDDiode,
            ModuleRecipeInfo.LowTier,
            20,
            RecipeMaps.nanochipSMDProcessorRecipes);
    }

    public static void registerBoardRecipes() {
        // Board processing recipes

        addBoardProcessingRecipe(
            CircuitComponent.BoardMultifiberglassElite,
            1,
            CircuitComponent.ProcessedBoardMultifiberglassElite,
            ModuleRecipeInfo.HighTier,
            20);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardWetwareLifesupport,
            Materials.GrowthMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardWetwareLifesupport,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipBoardProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardBioMutated,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardBioMutated,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipBoardProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardOptical,
            Materials.MysteriousCrystal.getMolten(1000),
            CircuitComponent.ProcessedBoardOptical,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipBoardProcessorRecipes);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardOptical,
            Materials.MysteriousCrystal.getMolten(1000),
            CircuitComponent.ProcessedBoardOptical,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipBoardProcessorRecipes);

        addSimpleProcessingRecipe(
            CircuitComponent.NeuroProcessingUnit,
            Materials.GrowthMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedNeuroProcessingUnit,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipBiologicalCoordinator);
        addSimpleProcessingRecipe(
            CircuitComponent.BioProcessingUnit,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBioProcessingUnit,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipBiologicalCoordinator);
    }

    public static void registerEtchingRecipes() {
        // CPU processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalCPU,
            CircuitComponent.ProcessedChipCrystalCPU,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipEtchingArray);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipAdvCrystalCPU,
            CircuitComponent.ProcessedChipAdvCrystalCPU,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipEtchingArray);

    }

    public static void registerOpticalRecipes() {
        addSimpleProcessingRecipe(
            CircuitComponent.ChipOpticalCPU,
            CircuitComponent.ProcessedChipOpticalCPU,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipOpticalOrganizer);
        // RAM processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalRAM,
            CircuitComponent.ProcessedOpticalRAM,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipOpticalOrganizer);
    }

    public static void registerCuttingRecipes() {
        // Wafer cutting processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNanoCPU,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipNanoCPU,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipRAM,
            Materials.Lubricant.getFluid(67),
            CircuitComponent.ProcessedChipRAM,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNOR,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNOR,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNAND,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNAND,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipASOC,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipASOC,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipPikoPIC,
            Materials.Lubricant.getFluid(375),
            CircuitComponent.ProcessedChipPikoPIC,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipQuantumPIC,
            Materials.Lubricant.getFluid(500),
            CircuitComponent.ProcessedChipQuantumPIC,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
        addSimpleProcessingRecipe(
            CircuitComponent.WaferPico,
            Materials.DimensionallyShiftedSuperfluid.getFluid(10),
            CircuitComponent.ProcessedChipPico,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipCuttingChamber);
    }

    public static void registerSuperconductorRecipes() {
        // Superconductor processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorZPM,
            CircuitComponent.ProcessedSuperconductorZPM,
            ModuleRecipeInfo.MediumTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUV,
            CircuitComponent.ProcessedSuperconductorUV,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUHV,
            CircuitComponent.ProcessedSuperconductorUHV,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUEV,
            CircuitComponent.ProcessedSuperconductorUEV,
            ModuleRecipeInfo.HighTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUIV,
            CircuitComponent.ProcessedSuperconductorUIV,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUMV,
            CircuitComponent.ProcessedSuperconductorUMV,
            ModuleRecipeInfo.ExtremeTier,
            20,
            RecipeMaps.nanochipSuperconductorSplitter);
    }

    public static void registerFrameBoxRecipes() {
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

    public static void registerCasingRecipes() {
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
    }

    public static void populateFinishedCircuitRecipeMaps() {
        // TODO: dummy recipes
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.CrystalProcessor,
            5,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.OpticalProcessor,
            5,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.BiowareProcessor,
            5,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);
        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 1)),
            Arrays.asList(Materials.Iron.getFluid(1)),
            CircuitComponent.WetwareProcessor,
            5,
            TierEU.RECIPE_LuV,
            VoltageIndex.UHV);

        // Circuit assembly line replacements
        // TODO: Tweak EU/t
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.CrystalProcessor,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.CrystalAssembly,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 75)),
            CircuitComponent.CrystalComputer,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 288)),
            CircuitComponent.CrystalMainframe,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.WetwareProcessor,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.WetwareAssembly,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.WetwareComputer,
            5,
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
                FluidRegistry.getFluidStack("molten.indalloy140", 2880),
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                Materials.Radon.getGas(2500)),
            CircuitComponent.WetwareMainframe,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.BiowareProcessor,
            5,
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
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.BiowareAssembly,
            5,
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
            5,
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
            5,
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
            5,
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
                FluidRegistry.getFluidStack("oganesson", 500)),
            CircuitComponent.OpticalAssembly,
            5,
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
                FluidRegistry.getFluidStack("oganesson", 1000)),
            CircuitComponent.OpticalComputer,
            5,
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
                FluidRegistry.getFluidStack("oganesson", 2000)),
            CircuitComponent.OpticalMainframe,
            5,
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
                5,
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
                5,
                TierEU.RECIPE_LuV,
                VoltageIndex.UHV);
        }
    }
}
