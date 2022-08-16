package com.github.technus.tectech.api;

import codechicken.nei.PositionedStack;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.nei.GT_NEI_DefaultHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static gregtech.api.enums.GT_Values.*;

public class TT_Recipe extends GT_Recipe {

    public TT_Recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
    }

    public static final GT_Recipe_Map sCelestialContainmentRecipes = new GT_Recipe_Map_CelestialContainment(new HashSet<>(20), "gt.recipe.plasmaforge", "DTPF", null, RES_PATH_GUI + "basicmachines/PlasmaForge", 1, 1, 0, 0, 1, "Heat Capacity: ", 1, " K", false, true);

    public static class GT_Recipe_Map_CelestialContainment extends GT_Recipe_Map {

        long hydrogen_quantity = 0;

        public GT_Recipe_Map_CelestialContainment(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed, true);
        }


        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
            return addRecipe(new GT_Recipe_Map_CelestialContainment.GT_Recipe_CelestialContainment(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue, 10));
        }

        private static class GT_Recipe_CelestialContainment extends GT_Recipe {

            long hydrogen;

            public GT_Recipe_CelestialContainment(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue, long aHydrogenTest) {
                super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
                hydrogen = aHydrogenTest;
            }

            @Override
            public ArrayList<PositionedStack> getInputPositionedStacks() {
                ArrayList<PositionedStack> inputStacks = new ArrayList<>();
                int i = 0;
                if (mInputs != null) {
                    for (int j = 0; j < mInputs.length; j ++, i ++) {
                        if (mInputs[j] == NI) continue;
                        inputStacks.add(new GT_NEI_DefaultHandler.FixedPositionedStack(mInputs[j].copy(), 12 + 18 * (i % 3), 5 + 18 * (i / 3)));
                    }
                }
                if (mFluidInputs != null) {
                    for (int j = 0; j < mFluidInputs.length; j ++, i ++) {
                        if (mFluidInputs[j] == NF) continue;
                        inputStacks.add(new GT_NEI_DefaultHandler.FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[j], true), 12 + 18 * (i % 3), 5 + 18 * (i / 3)));
                    }
                }
                return inputStacks;
            }

            @Override
            public ArrayList<PositionedStack> getOutputPositionedStacks() {
                ArrayList<PositionedStack> outputStacks = new ArrayList<>();
                int i = 0;
                if (mOutputs != null) {
                    for (int j = 0; j < mOutputs.length; j ++, i ++) {
                        if (mOutputs[j] == NI) continue;
                        outputStacks.add(new GT_NEI_DefaultHandler.FixedPositionedStack(mOutputs[j].copy(), 102 + 18 * (i % 3), 5 + 18 * (i / 3)));
                    }
                }
                if (mFluidOutputs != null) {
                    for (int j = 0; j < mFluidOutputs.length; j ++, i ++) {
                        if (mFluidOutputs[j] == NF) continue;
                        outputStacks.add(new GT_NEI_DefaultHandler.FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidOutputs[j], true), 102 + 18 * (i % 3), 5 + 18 * (i / 3)));
                    }
                }
                return outputStacks;
            }

        }

    }
}
