package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.nanochipPartProcessorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gtPlusPlus.core.material.MaterialsAlloy;

/**
 * Class to allow for NAC testing in dev-env without hassle
 */
public class DevEnvNanochipRecipes implements Runnable {

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
                inputs[i] = ccStack.getCircuitComponent()
                    .getFakeStack(ccStack.getSize());
            } else if (inputThing instanceof Object[]alts) {
                inputs[i] = Arrays.stream(alts)
                    .map(c -> {
                        if (c instanceof CircuitComponent.CircuitComponentStack ccStack) {
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
            .metadata(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, output.getCircuitComponent().circuitType)
            .itemInputs(inputs)
            .fluidInputs(fluidInputs.toArray(new FluidStack[0]))
            .itemOutputs(
                output.getCircuitComponent()
                    .getFakeStack(output.getSize()))
            .duration(duration)
            .eut(eut)
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
                inputs[i] = ccStack.getCircuitComponent()
                    .getFakeStack(ccStack.getSize());
            } else if (inputThing instanceof Object[]alts) {
                inputs[i] = Arrays.stream(alts)
                    .map(c -> {
                        if (c instanceof CircuitComponent.CircuitComponentStack ccStack) {
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
            .metadata(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, output.circuitType)
            .itemInputs(inputs)
            .fluidInputs(fluidInputs.toArray(new FluidStack[0]))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }

    // Adds a simple processing recipe for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output, long eut,
        int duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .itemInputs(input.getFakeStack(1))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut)
            .addTo(recipeMap);
    }

    // Adds a simple processing recipe with a fluid for circuit components in a module
    private static void addSimpleProcessingRecipe(CircuitComponent input, FluidStack inputStack,
        CircuitComponent output, long eut, long duration, RecipeMap<?> recipeMap) {
        GTValues.RA.stdBuilder()
            .itemInputs(input.getFakeStack(1))
            .fluidInputs(inputStack)
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(eut)
            .addTo(recipeMap);
    }

    @Override
    public void run() {

        for (CircuitComponent cc : CircuitComponent.VALUES) {
            if (cc.isProcessed) continue;
            if (cc.realComponent == null) throw new IllegalStateException("CC must define its representative item!");

            GTValues.RA.stdBuilder()
                .itemInputs(cc.realComponent.get())
                .itemOutputs(cc.getFakeStack(1))
                .duration(5 * SECONDS)
                .eut(0)
                .addTo(RecipeMaps.nanochipConversionRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(cc.getFakeStack(1))
                .itemOutputs(cc.realComponent.get())
                .duration(5 * SECONDS)
                .eut(0)
                .addTo(RecipeMaps.nanochipConversionRecipes);
        }

        addAssemblyMatrixRecipe(
            Arrays.asList(new CircuitComponent.CircuitComponentStack(CircuitComponent.WireNiobiumTitanium, 1)),
            Arrays.asList(MaterialsAlloy.INDALLOY_140.getFluidStack(4)),
            CircuitComponent.BiowareMainframe,
            20 * SECONDS,
            TierEU.RECIPE_MAX,
            VoltageIndex.UIV);

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

    }

    private static void registerBoardProcessorRecipes() {
        // Elite Board
        GTValues.RA.stdBuilder()
            .hidden()
            .metadata(BoardProcessingModuleFluidKey.INSTANCE, 1)
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

        // Elite Board fake recipe
        GTValues.RA.stdBuilder()
            .fake()
            .itemInputs(CircuitComponent.BoardMultifiberglassElite.getFakeStack(1))
            .fluidInputs(Materials.IronIIIChloride.getFluid(0))
            .fluidOutputs(GGMaterial.ferrousChloride.getFluidOrGas(0))
            .itemOutputs(CircuitComponent.ProcessedBoardMultifiberglassElite.getFakeStack(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(RecipeMaps.nanochipBoardProcessorRecipes);

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

        }

        // Crystal
        GTValues.RA.stdBuilder()
            .itemInputs(CircuitComponent.ProcessedFrameboxAluminium.getFakeStack(2))
            .itemOutputs(CircuitComponent.ProcessedBasicMainframeCasing.getFakeStack(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(RecipeMaps.nanochipEncasementWrapper);

    }

    private static void registerEtchingArrayRecipes() {
        // Raw Advanced Crystal Chip
        addSimpleProcessingRecipe(
            CircuitComponent.ChipRawAdvancedCrystal,
            CircuitComponent.ProcessedChipRawAdvancedCrystal,
            TierEU.RECIPE_UEV,
            16 * SECONDS,
            RecipeMaps.nanochipEtchingArray);

    }

    private static void registerOpticalOrganizerRecipes() {
        // Optical CPU
        addSimpleProcessingRecipe(
            CircuitComponent.ChipOpticalCPU,
            CircuitComponent.ProcessedChipOpticalCPU,
            TierEU.RECIPE_UEV,
            30 * SECONDS,
            RecipeMaps.nanochipOpticalOrganizer);

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

        }

        // Optical SMDs
        {
            addSimpleProcessingRecipe(
                CircuitComponent.OpticalSMDResistor,
                CircuitComponent.ProcessedOpticalSMDResistor,
                TierEU.RECIPE_ZPM,
                1 * SECONDS,
                RecipeMaps.nanochipPartProcessorRecipes);

        }

        // Plate recipes
        {
            // Rhugnor Plate
            GTValues.RA.stdBuilder()
                .itemInputs(CircuitComponent.PlateRhugnor.getFakeStack(1), CircuitComponent.BoltRhugnor.getFakeStack(4))
                .itemOutputs(CircuitComponent.ProcessedPlateRhugnor.getFakeStack(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(1 * SECONDS)
                .addTo(nanochipPartProcessorRecipes);

        }

    }

    private static void registerSuperconductorSplitterRecipes() {
        // LuV
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            TierEU.RECIPE_UV,
            5 * SECONDS,
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

    }
}
