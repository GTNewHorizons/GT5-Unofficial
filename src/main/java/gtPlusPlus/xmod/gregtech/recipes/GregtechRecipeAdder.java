package gtPlusPlus.xmod.gregtech.recipes;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {
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
    public boolean addSixSlotAssemblingRecipe(ItemStack[] aInputs, FluidStack aInputFluid, ItemStack aOutput1,
        int aDuration, int aEUt) {
        return RA.addAssemblerRecipe(aInputs, aInputFluid, aOutput1, aDuration, aEUt);
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
    public boolean addBrewingRecipe(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aTime, int aEu,
        boolean aHidden) {
        if (aIngredient == null || aInput == null || aOutput == null) {
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
    public boolean addSemifluidFuel(ItemStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addSemifluidFuel(FluidStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
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
    public boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int time, long eu, int aTier) {
        return addChemicalPlantRecipe(aInputs, aInputFluids, aOutputs, aFluidOutputs, new int[] {}, time, eu, aTier);
    }

    @Override
    public boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int[] aChances, int time, long eu, int aTier) {
        if (aInputs.length > 4 || aInputFluids.length > 4 || aOutputs.length > 6 || aFluidOutputs.length > 3) {
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aInputFluids));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            CORE.crash();
        }

        GTPPRecipeMaps.chemicalPlantRecipes
            .addRecipe(false, aInputs, aOutputs, null, aChances, aInputFluids, aFluidOutputs, time, (int) eu, aTier);
        return true;
    }

    @Override
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput,
        ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        if (aInput != null && aOutput != null) {
            if (aDuration <= 0) {
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
        ItemStack[] aInputsOre1 = new ItemStack[] {GT_Utility.getIntegratedCircuit(10), aOreStack, aMillingBall_Alumina };

        ItemStack[] aInputsOre2 = new ItemStack[] {GT_Utility.getIntegratedCircuit(11), aOreStack, aMillingBall_Soapstone };

        ItemStack[] aInputsCrushed1 = new ItemStack[] {GT_Utility.getIntegratedCircuit(10), aCrushedStack,
            aMillingBall_Alumina };

        ItemStack[] aInputsCrushed2 = new ItemStack[] {GT_Utility.getIntegratedCircuit(11), aCrushedStack,
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

        // Dust 1
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(4, aSmallDust),
                aRecipeType
            )
            .itemOutputs(aOutputStack1)
            .duration(5*SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Dust 2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(9, aTinyDust),
                aRecipeType
            )
            .itemOutputs(aOutputStack1)
            .duration(5*SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

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
        FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial) {
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
