package gtPlusPlus.xmod.gregtech.recipes;

import static gregtech.api.enums.GT_Values.RA;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

    @Override
    public boolean addCokeOvenRecipe(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput,
            final FluidStack aFluidOutput, final ItemStack aOutput, int aDuration, final int aEUt) {
        if (aInput1 == null || (aOutput == null || aFluidOutput == null)) {
            Logger.WARNING("Something was null, returning false");
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0) {
            Logger.WARNING("Something was null, returning false");
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aFluidOutput.getFluid().getName(), aDuration)) <= 0) {
            Logger.WARNING("Something was null, returning false");
            return false;
        }

        GT_Recipe aSpecialRecipe = new GT_Recipe(
                true,
                new ItemStack[] { aInput1, aInput2 },
                new ItemStack[] { aOutput },
                null,
                new int[] {},
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                aDuration,
                Math.max(1, aEUt),
                0);
        GTPPRecipeMaps.cokeOvenRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addCokeOvenRecipe(int aCircuit, ItemStack aInput2, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUt) {
        return addCokeOvenRecipe(
                aInput2,
                CI.getNumberedCircuit(aCircuit),
                aFluidInputs,
                aFluidOutputs,
                aOutputs,
                aDuration,
                aEUt);
    }

    @Override
    public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUt) {
        GT_Recipe aSpecialRecipe = new GT_Recipe(
                true,
                new ItemStack[] { aInput1, aInput2 },
                aOutputs,
                null,
                new int[] {},
                aFluidInputs,
                aFluidOutputs,
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);
        GTPPRecipeMaps.cokeOvenRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addMatterFabricatorRecipe(final FluidStack aFluidInput, final FluidStack aFluidOutput,
            final int aDuration, final int aEUt) {
        if (aFluidOutput == null) {
            Logger.WARNING("Something was null, returning false");
            return false;
        }
        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                aFluidInput == null ? new FluidStack[] {} : new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                aDuration,
                aEUt,
                0);
        GTPPRecipeMaps.multiblockMassFabricatorRecipes.addRecipe(aRecipe);
        return true;
    }

    @Override
    public boolean addDehydratorRecipe(final ItemStack[] aInput, final FluidStack aFluidInput,
            final FluidStack aFluidOutput, final ItemStack[] aOutputItems, final int[] aChances, int aDuration,
            final int aEUt) throws IndexOutOfBoundsException {
        if (((aInput == null || aInput.length == 0) && (aFluidInput == null))
                || ((aOutputItems == null || aOutputItems.length == 0) && (aFluidOutput == null))) {
            return false;
        }

        GT_Recipe aSpecialRecipe = new GT_Recipe(
                true,
                aInput,
                aOutputItems,
                null,
                aChances,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);
        GTPPRecipeMaps.chemicalDehydratorRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aOutput, final int aChance, int aDuration,
            final int aEUt) {
        return addBlastSmelterRecipe(
                aInput,
                (FluidStack) null,
                aOutput,
                new ItemStack[] {},
                new int[] { aChance },
                aDuration,
                aEUt,
                3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput,
            final int aChance, int aDuration, final int aEUt) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                new ItemStack[] {},
                new int[] { aChance },
                aDuration,
                aEUt,
                3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack[] aInputFluid, FluidStack aOutput,
            final int aChance, int aDuration, final int aEUt) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                new ItemStack[] {},
                new int[] { aChance },
                aDuration,
                aEUt,
                3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, final int[] aChance, int aDuration, final int aEUt) {
        return addBlastSmelterRecipe(aInput, aInputFluid, aOutput, aOutputStack, aChance, aDuration, aEUt, 3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack[] aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, final int[] aChance, int aDuration, final int aEUt) {
        return addBlastSmelterRecipe(aInput, aInputFluid, aOutput, aOutputStack, aChance, aDuration, aEUt, 3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput, int aChance,
            int aDuration, int aEUt, int aSpecialValue) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                new ItemStack[] {},
                new int[] { aChance },
                aDuration,
                aEUt,
                aSpecialValue);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack[] aInputFluid, FluidStack aOutput, int aChance,
            int aDuration, int aEUt, int aSpecialValue) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                new ItemStack[] {},
                new int[] { aChance },
                aDuration,
                aEUt,
                aSpecialValue);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, int[] aChance, int aDuration, int aEUt, int aSpecialValue) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                aOutputStack,
                aChance,
                aDuration,
                aEUt,
                aSpecialValue,
                true);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack[] aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, int[] aChance, int aDuration, int aEUt, int aSpecialValue) {
        return addBlastSmelterRecipe(
                aInput,
                aInputFluid,
                aOutput,
                aOutputStack,
                aChance,
                aDuration,
                aEUt,
                aSpecialValue,
                true);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, int[] aChance, int aDuration, int aEUt, int aSpecialValue,
            boolean aOptimizeRecipe) {
        return addBlastSmelterRecipe(
                aInput,
                new FluidStack[] { aInputFluid },
                aOutput,
                aOutputStack,
                aChance,
                aDuration,
                aEUt,
                aSpecialValue,
                aOptimizeRecipe);
    }

    @Override
    public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack[] aInputFluid, FluidStack aOutput,
            ItemStack[] aOutputStack, int[] aChance, int aDuration, int aEUt, int aSpecialValue,
            boolean aOptimizeRecipe) {
        if ((aInput == null) || (aOutput == null)) {
            Logger.WARNING("Fail - Input or Output was null.");
            return false;
        }

        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("blastsmelter", aOutput.getFluid().getName(), aDuration)) <= 0) {
            Logger.WARNING("Recipe did not register.");
            return false;
        }

        aInput = ArrayUtils.removeNulls(aInput);
        if (aInput.length <= 1) {
            return false;
        }

        GTPPRecipeMaps.alloyBlastSmelterRecipes.addRecipe(
                aOptimizeRecipe,
                aInput,
                aOutputStack,
                null,
                aChance,
                aInputFluid,
                new FluidStack[] { aOutput },
                aDuration,
                Math.max(1, aEUt),
                aSpecialValue);
        return true;
    }

    @Override
    public boolean addQuantumTransformerRecipe(ItemStack[] aInput, FluidStack[] aFluidInput, FluidStack[] aFluidOutput,
            ItemStack[] aOutputStack, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
        if (aInput == null) {
            aInput = new ItemStack[0];
        }
        if (aFluidInput == null) {
            aFluidInput = new FluidStack[0];
        }
        if (aOutputStack == null) {
            aOutputStack = new ItemStack[0];
        }
        if (aFluidOutput == null) {
            aFluidOutput = new FluidStack[0];
        }

        GTPPRecipeMaps.quantumForceTransformerRecipes.add(
                new GT_Recipe(
                        false,
                        aInput,
                        aOutputStack,
                        null,
                        aChances,
                        aFluidInput,
                        aFluidOutput,
                        aDuration,
                        aEUt,
                        aSpecialValue));
        return true;
    }

    @Override
    public boolean addFissionFuel(final FluidStack aInput1, final FluidStack aInput2, final FluidStack aInput3,
            final FluidStack aInput4, final FluidStack aInput5, final FluidStack aInput6, final FluidStack aInput7,
            final FluidStack aInput8, final FluidStack aInput9, final FluidStack aOutput1, final FluidStack aOutput2,
            final int aDuration, final int aEUt) {
        return addFissionFuel(
                false,
                aInput1,
                aInput2,
                aInput3,
                aInput4,
                aInput5,
                aInput6,
                aInput7,
                aInput8,
                aInput9,
                aOutput1,
                aOutput2,
                aDuration,
                aEUt);
    }

    @Override
    public boolean addFissionFuel(final boolean aOptimise, final FluidStack aInput1, final FluidStack aInput2,
            final FluidStack aInput3, final FluidStack aInput4, final FluidStack aInput5, final FluidStack aInput6,
            final FluidStack aInput7, final FluidStack aInput8, final FluidStack aInput9, final FluidStack aOutput1,
            final FluidStack aOutput2, final int aDuration, final int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
            return false;
        }
        final FluidStack[] inputs = { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 };
        final FluidStack[] outputs = { aOutput1, aOutput2 };

        GTPPRecipeMaps.fissionFuelProcessingRecipes.addRecipe(
                aOptimise,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                inputs,
                outputs,
                aDuration,
                aEUt,
                0);
        return true;
    }

    @Override
    public boolean addCyclotronRecipe(ItemStack aInputs, FluidStack aFluidInput, ItemStack[] aOutputs,
            FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
        return addCyclotronRecipe(
                new ItemStack[] { aInputs },
                aFluidInput,
                aOutputs,
                aFluidOutput,
                aChances,
                aDuration,
                aEUt,
                aSpecialValue);
    }

    @Override
    public boolean addCyclotronRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack[] aOutput,
            FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
        if (aOutput == null || aOutput.length < 1 || !ItemUtils.checkForInvalidItems(aOutput)) {
            Logger.INFO("Bad output for Cyclotron Recipe.");
            return false;
        }

        GT_Recipe aSpecialRecipe = new GT_Recipe(
                true,
                aInputs,
                aOutput,
                null,
                aChances,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                aSpecialValue);
        GTPPRecipeMaps.cyclotronRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
            FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
            ItemStack aOutput4, int aDuration, int aEUt) {
        if ((aInput1 == null && aFluidInput == null) || (aOutput1 == null && aFluidOutput == null)) {
            return false;
        }

        GT_Recipe aSpecialRecipe = new GT_Recipe(
                true,
                new ItemStack[] { aInput1, aInput2, aInput3, aInput4 },
                new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
                null,
                new int[] {},
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);
        RecipeMaps.mixerRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addMultiblockCentrifugeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }

        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Centrifuge recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.centrifugeNonCellRecipes.addRecipe(aRecipe);
        return true;
    }

    @Override
    public boolean addMultiblockElectrolyzerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Electrolyzer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.electrolyzerNonCellRecipes.addRecipe(aRecipe);
        return true;
    }

    @Override
    public boolean addAdvancedFreezerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }

        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Adv. Vac Freezer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.advancedFreezerRecipes.addRecipe(aRecipe);
        return false;
    }

    public boolean addMultiblockMixerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Mixer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.mixerNonCellRecipes.addRecipe(aRecipe);
        return true;
    }

    public boolean addMultiblockChemicalDehydratorRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Chemical Dehydrator recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.chemicalDehydratorNonCellRecipes.addRecipe(aRecipe);
        return true;
    }

    @Override
    public boolean addSixSlotAssemblingRecipe(ItemStack[] aInputs, FluidStack aInputFluid, ItemStack aOutput1,
            int aDuration, int aEUt) {
        return RA.addAssemblerRecipe(aInputs, aInputFluid, aOutput1, aDuration, aEUt);
    }

    @Override
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        return RA.addAssemblylineRecipe(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
    }

    @Override
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        return RA.addAssemblylineRecipe(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid,
            ItemStack output, int time, int eu) {
        return addChemicalRecipe(input1, input2, inputFluid, outputFluid, output, null, time, eu);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid,
            ItemStack output, Object object, int time, int eu) {
        return RA.addChemicalRecipe(input1, input2, inputFluid, outputFluid, output, time, eu);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid,
            ItemStack output, ItemStack output2, int time) {
        return RA.addChemicalRecipe(input1, input2, inputFluid, outputFluid, output, output2, time);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, int aCircuit, FluidStack inputFluid,
            FluidStack outputFluid, ItemStack output, ItemStack output2, int time, int eu) {
        if (aCircuit < 0 || aCircuit > 24) {
            aCircuit = 22;
        }
        GT_Recipe aSpecialRecipe = new GT_Recipe(
                false,
                new ItemStack[] { input1, input2 },
                new ItemStack[] { output, output2 },
                CI.getNumberedCircuit(aCircuit),
                new int[] {},
                new FluidStack[] { inputFluid },
                new FluidStack[] { outputFluid },
                time,
                eu,
                0);
        RecipeMaps.chemicalReactorRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addMultiblockChemicalRecipe(ItemStack[] itemStacks, FluidStack[] fluidStacks,
            FluidStack[] fluidStacks2, ItemStack[] outputs, int time, int eu) {
        return RA.addMultiblockChemicalRecipe(itemStacks, fluidStacks, fluidStacks2, outputs, time, eu);
    }

    private boolean areItemsAndFluidsBothNull(final ItemStack[] items, final FluidStack[] fluids) {
        boolean itemsNull = true;
        if (items != null) {
            for (final ItemStack itemStack : items) {
                if (itemStack != null) {
                    itemsNull = false;
                    break;
                }
            }
        }
        boolean fluidsNull = true;
        if (fluids != null) {
            for (final FluidStack fluidStack : fluids) {
                if (fluidStack != null) {
                    fluidsNull = false;
                    break;
                }
            }
        }
        return itemsNull && fluidsNull;
    }

    @Override
    public boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("compressor", aInput1, aDuration)) <= 0) {
            return false;
        }
        RecipeMaps.compressorRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1 },
                new ItemStack[] { aOutput1 },
                null,
                null,
                null,
                aDuration,
                aEUt,
                0);
        return true;
    }

    @Override
    public boolean addBrewingRecipe(int aCircuit, FluidStack aInput, FluidStack aOutput, int aTime, int aEu,
            boolean aHidden) {
        return addBrewingRecipe(CI.getNumberedCircuit(aCircuit), aInput, aOutput, aTime, aEu, aHidden);
    }

    @Override
    public boolean addBrewingRecipe(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aTime, int aEu,
            boolean aHidden) {
        if (aIngredient == null || aInput == null || aOutput == null) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return false;
        }
        GT_Recipe tRecipe = RecipeMaps.brewingRecipes.addRecipe(
                false,
                new ItemStack[] { aIngredient },
                null,
                null,
                new FluidStack[] { aInput },
                new FluidStack[] { aOutput },
                aTime,
                aEu,
                0);
        if (aHidden && tRecipe != null) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    /**
     * Lets me add recipes for GT 5.08 & 5.09, since someone broke the method headers.
     */
    @Override
    public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aDust, ItemStack aOutput) {
        return GT_ModHandler.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput, true);
    }

    @Override
    public boolean addFluidExtractionRecipe(ItemStack input, FluidStack output, int aTime, int aEu) {
        return RA.addFluidSmelterRecipe(GT_Utility.copyAmount(1, input), null, output, 10000, aTime, aEu, false);
    }

    @Override
    public boolean addFluidExtractionRecipe(ItemStack aEmpty, ItemStack aRemains, FluidStack aFluid, int aDuration,
            int aEU) {
        return RA.addFluidSmelterRecipe(
                GT_Utility.copyAmount(1, aEmpty),
                aRemains,
                aFluid,
                10000,
                aDuration,
                aEU,
                false);
    }

    @Override
    public boolean addFluidCannerRecipe(ItemStack aContainer, ItemStack aFullContainer, FluidStack rFluidIn) {
        return MaterialGenerator.addFluidCannerRecipe(aContainer, aFullContainer, rFluidIn, null);
    }

    @Override
    public boolean addFluidCannerRecipe(ItemStack aContainer, ItemStack aFullContainer, FluidStack rFluidIn,
            FluidStack rFluidOut) {
        return MaterialGenerator.addFluidCannerRecipe(aContainer, aFullContainer, rFluidIn, rFluidOut);
    }

    @Override
    public boolean addFluidCannerRecipe(ItemStack aFullContainer, ItemStack container, FluidStack rFluidIn,
            FluidStack rFluidOut, int aTime, int aEu) {
        return MaterialGenerator.addFluidCannerRecipe(container, aFullContainer, rFluidIn, rFluidOut, aTime, aEu);
    }

    @Override
    public boolean addSemifluidFuel(ItemStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addSemifluidFuel(FluidStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addVacuumFurnaceRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
            FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        if (aInput1 != null && aOutput1 != null) {
            return addVacuumFurnaceRecipe(
                    new ItemStack[] { aInput1, aInput2 },
                    new FluidStack[] { aFluidInput },
                    new ItemStack[] { aOutput1, aOutput2 },
                    new FluidStack[] { aFluidOutput },
                    aDuration,
                    aEUt,
                    aLevel);
        } else {
            return false;
        }
    }

    @Override
    public boolean addVacuumFurnaceRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aLevel) {
        if (aInputs != null && aOutputs != null) {
            GTPPRecipeMaps.vacuumFurnaceRecipes.addRecipe(
                    false,
                    aInputs,
                    aOutputs,
                    null,
                    new int[] {},
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aLevel);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addUvLaserRecipe(ItemStack aInput1, ItemStack aOutput, int time, long eu) {
        // Generate Special Laser Recipe
        GT_Recipe u = new GT_Recipe(
                false,
                new ItemStack[] { aInput1, GregtechItemList.Laser_Lens_WoodsGlass.get(0), },
                new ItemStack[] { aOutput },
                null,
                new int[] { 10000 },
                new FluidStack[] {},
                new FluidStack[] {},
                time,
                (int) eu,
                0);
        RecipeMaps.laserEngraverRecipes.add(u);
        return true;
    }

    @Override
    public boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs, int time, long eu, int aTier) {
        return addChemicalPlantRecipe(aInputs, aInputFluids, aOutputs, aFluidOutputs, new int[] {}, time, eu, aTier);
    }

    @Override
    public boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs, int[] aChances, int time, long eu, int aTier) {
        if (aInputs.length > 4 || aInputFluids.length > 4 || aOutputs.length > 4 || aFluidOutputs.length > 2) {
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aInputFluids));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            CORE.crash();
        }

        GTPPRecipeMaps.chemicalPlantRecipes.addRecipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aInputFluids,
                aFluidOutputs,
                time,
                (int) eu,
                aTier);
        return true;
    }

    @Override
    public boolean addBlastRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs, int time, long eu, int aHeat) {
        GT_Recipe aSpecialRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                new int[] {},
                aInputFluids,
                aFluidOutputs,
                time,
                (int) eu,
                aHeat);

        RecipeMaps.blastFurnaceRecipes.add(aSpecialRecipe);
        return true;
    }

    @Override
    public boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput,
            FluidStack aFluidOutput, int aDuration, int aEUt) {
        return RA.addPyrolyseRecipe(aInput, aFluidInput, intCircuit, aOutput, aFluidOutput, aDuration, aEUt);
    }

    @Override
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput,
            ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        if (aInput != null && aOutput != null) {
            if ((aDuration = GregTech_API.sRecipeFile
                    .get("distillery", aOutput.getFluid().getUnlocalizedName(), aDuration)) <= 0) {
                return false;
            } else {
                GT_Recipe tRecipe = RecipeMaps.distilleryRecipes.addRecipe(
                        true,
                        new ItemStack[] { aCircuit },
                        new ItemStack[] { aSolidOutput },
                        null,
                        new FluidStack[] { aInput },
                        new FluidStack[] { aOutput },
                        aDuration,
                        aEUt,
                        0);
                if (aHidden && tRecipe != null) {
                    tRecipe.mHidden = true;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addExtractorRecipe(aInput, aOutput, 10000, aDuration, aEUt);
    }

    @Override
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        if (aInput != null && aOutput != null) {
            GT_Recipe aRecipe = new GT_Recipe(
                    false,
                    new ItemStack[] { aInput.copy() },
                    new ItemStack[] { aOutput.copy() },
                    null,
                    new int[] { aChance },
                    null,
                    null,
                    aDuration,
                    aEUt,
                    0);
            RecipeMaps.extractorRecipes.add(aRecipe);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
            ItemStack aOutput3) {
        aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
        aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
        aOutput3 = GT_OreDictUnificator.get(true, aOutput3);
        if (GT_Utility.isStackInvalid(aInput) || GT_Utility.isStackInvalid(aOutput1)) {
            return false;
        }
        if (GT_Utility.getContainerItem(aInput, false) == null) {

            if (GregTech_API.sRecipeFile.get(ConfigCategories.Machines.maceration, aInput, true)) {
                GT_Utility.addSimpleIC2MachineRecipe(aInput, GT_ModHandler.getMaceratorRecipeList(), null, aOutput1);
            }
            GT_Values.RA.addPulveriserRecipe(
                    aInput,
                    new ItemStack[] { aOutput1, aOutput2, aOutput3 },
                    new int[] { 10000, 10000, 10000 },
                    400,
                    2);
        }
        return true;
    }

    @Override
    public boolean addMillingRecipe(Materials aMat, int aEU) {
        return addMillingRecipe(MaterialUtils.generateMaterialFromGtENUM(aMat), aEU);
    }

    @Override
    public boolean addMillingRecipe(Material aMat, int aEU) {

        ItemStack aOreStack = aMat.getOre(16);
        ItemStack aCrushedStack = aMat.getCrushed(16);

        ItemStack aMilledStackOres1 = aMat.getMilled(64);
        ItemStack aMilledStackCrushed1 = aMat.getMilled(32);
        ItemStack aMilledStackOres2 = aMat.getMilled(48);
        ItemStack aMilledStackCrushed2 = aMat.getMilled(16);

        ItemStack aMillingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack aMillingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        // Inputs
        ItemStack[] aInputsOre1 = new ItemStack[] { CI.getNumberedCircuit(10), aOreStack, aMillingBall_Alumina };

        ItemStack[] aInputsOre2 = new ItemStack[] { CI.getNumberedCircuit(11), aOreStack, aMillingBall_Soapstone };

        ItemStack[] aInputsCrushed1 = new ItemStack[] { CI.getNumberedCircuit(10), aCrushedStack,
                aMillingBall_Alumina };

        ItemStack[] aInputsCrushed2 = new ItemStack[] { CI.getNumberedCircuit(11), aCrushedStack,
                aMillingBall_Soapstone };

        // Outputs
        ItemStack[] aOutputsOre1 = new ItemStack[] { aMilledStackOres1 };

        ItemStack[] aOutputsOre2 = new ItemStack[] { aMilledStackOres2 };

        ItemStack[] aOutputsCrushed1 = new ItemStack[] { aMilledStackCrushed1 };

        ItemStack[] aOutputsCrushed2 = new ItemStack[] { aMilledStackCrushed2 };

        ItemStack[][] aInputArray = new ItemStack[][] { aInputsOre1, aInputsOre2, aInputsCrushed1, aInputsCrushed2 };
        ItemStack[][] aOutputArray = new ItemStack[][] { aOutputsOre1, aOutputsOre2, aOutputsCrushed1,
                aOutputsCrushed2 };
        int[] aTime = new int[] { 2400, 3000, 1200, 1500 };

        for (int i = 0; i < 4; i++) {
            GT_Recipe aOreRecipe = new GT_Recipe(
                    false,
                    aInputArray[i],
                    aOutputArray[i],
                    null,
                    new int[] {},
                    null,
                    null,
                    aTime[i],
                    aEU,
                    0);
            GTPPRecipeMaps.millingRecipes.add(aOreRecipe);
        }
        return true;
    }

    @Override
    public boolean addFlotationRecipe(Materials aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
            FluidStack[] aOutputFluids, int aTime, int aEU) {
        return addFlotationRecipe(
                MaterialUtils.generateMaterialFromGtENUM(aMat),
                aXanthate,
                aInputFluids,
                aOutputFluids,
                aTime,
                aEU);
    }

    @Override
    public boolean addFlotationRecipe(Material aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
            FluidStack[] aOutputFluids, int aTime, int aEU) {

        FlotationRecipeHandler.registerOreType(aMat);

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] { ItemUtils.getSimpleStack(aXanthate, 32), aMat.getMilled(64), aMat.getMilled(64),
                        aMat.getMilled(64), aMat.getMilled(64), },
                new ItemStack[] {},
                null,
                new int[] {},
                aInputFluids,
                aOutputFluids,
                aTime,
                aEU,
                0);
        GTPPRecipeMaps.flotationCellRecipes.add(aRecipe);
        return true;
    }

    @Override
    public boolean addpackagerRecipe(ItemStack aRecipeType, ItemStack aSmallDust, ItemStack aTinyDust,
            ItemStack aOutputStack1) {
        AutoMap<Boolean> aResults = new AutoMap<>();
        // Dust 1
        aResults.put(
                GT_Values.RA
                        .addBoxingRecipe(GT_Utility.copyAmount(4L, aSmallDust), aRecipeType, aOutputStack1, 100, 4));
        // Dust 2
        aResults.put(
                GT_Values.RA.addBoxingRecipe(GT_Utility.copyAmount(9L, aTinyDust), aRecipeType, aOutputStack1, 100, 4));
        for (boolean b : aResults) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addFuelForRTG(ItemStack aFuelPellet, int aFuelDays, int aVoltage) {
        GTPPRecipeMaps.rtgFuels.addRecipe(
                true,
                new ItemStack[] { aFuelPellet },
                new ItemStack[] {},
                null,
                null,
                null,
                0,
                aVoltage,
                aFuelDays);
        return true;
    }

    @Override
    public boolean addColdTrapRecipe(int aCircuit, ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
            int[] aChances, FluidStack aFluidOutput, int aTime, int aEU) {
        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedAdvancedCircuit(aCircuit), aInput },
                aOutputs,
                null,
                aChances,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                aTime,
                aEU,
                0);

        GTPPRecipeMaps.coldTrapRecipes.add(aRecipe);
        return true;
    }

    @Override
    public boolean addReactorProcessingUnitRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
            ItemStack[] aOutputs, int[] aChances, FluidStack aFluidOutput, int aTime, int aEU) {
        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] { aInput1, aInput2 },
                aOutputs,
                null,
                aChances,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                aTime,
                aEU,
                0);

        GTPPRecipeMaps.reactorProcessingUnitRecipes.add(aRecipe);
        return true;
    }

    public boolean addNuclearSaltProcessingPlantRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Salt Plant recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GT_Recipe aRecipe = new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUtick,
                aSpecial);
        GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes.addRecipe(aRecipe);
        return true;
    }

    @Override
    public boolean addFluidHeaterRecipe(ItemStack aInput, FluidStack aFluidInput, FluidStack aOutput, int aDuration,
            int aEUt) {
        if ((aInput == null && aFluidInput == null) || aOutput == null) {
            return false;
        }
        RecipeMaps.fluidHeaterRecipes.addRecipe(
                true,
                new ItemStack[] { aInput },
                null,
                null,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aOutput },
                aDuration,
                aEUt,
                0);
        return true;
    }

    @Override
    public boolean addVacuumFreezerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if (aInput == null || aOutput == null) {
            return false;
        }
        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] { aInput },
                new ItemStack[] { aOutput },
                null,
                new int[] { 10000 },
                new FluidStack[] {},
                new FluidStack[] {},
                aDuration,
                aEUt,
                0);

        RecipeMaps.vacuumFreezerRecipes.add(aRecipe);
        return true;
    }

    @Override
    public boolean addMolecularTransformerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if (aInput == null || aOutput == null) {
            return false;
        }
        GT_Recipe aRecipe = new GT_Recipe(
                false,
                new ItemStack[] { aInput },
                new ItemStack[] { aOutput },
                null,
                new int[] { 10000 },
                new FluidStack[] {},
                new FluidStack[] {},
                aDuration,
                aEUt,
                0);

        GTPPRecipeMaps.molecularTransformerRecipes.add(aRecipe);
        return true;
    }

    @Override
    public boolean addMolecularTransformerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt,
            int aAmps) {
        return addMolecularTransformerRecipe(aInput, aOutput, aDuration, aEUt);
    }
}
