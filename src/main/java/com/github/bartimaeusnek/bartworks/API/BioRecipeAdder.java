/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import gregtech.api.enums.Materials;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("ALL")
public final class BioRecipeAdder {

    public static final int STANDART = 0;
    public static final int LOWGRAVITY = -100;
    public static final int CLEANROOM = -200;

    public static boolean addBioLabRecipe(
            ItemStack[] aInputs,
            ItemStack aOutput,
            ItemStack aSpecialItems,
            int[] aChances,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        return BWRecipes.instance.addBioLabRecipe(
                aInputs, aOutput, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
    }

    /**
     * Adds a Incubation Recipe for the BioLab.
     *
     * @param aInput        any item that will be added with a petri dish
     * @param aOutput       must be a BioCulture
     * @param aChances
     * @param aFluidInputs
     * @param aDuration
     * @param aEUt          use BW_Util.getMachineVoltageFromTier(tier) to get optimal EU/t
     * @param aSpecialValue 0 = STANDART, -100 = LowGravity, -200 = Cleanroom
     * @return if the recipe was added.
     */
    public static boolean addBioLabRecipeIncubation(
            ItemStack aInput,
            BioCulture aOutput,
            int[] aChances,
            FluidStack aFluidInputs,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        return BWRecipes.instance.addBioLabRecipeIncubation(
                aInput, aOutput, aChances, aFluidInputs, aDuration, aEUt, aSpecialValue);
    }

    /**
     * @param aInputs       Item Inputs, DO NOT PUT INTEGRATED CIRCUITS IN HERE! THEY WILL GET ADDED AUTOMATICALLY!, can be null
     * @param aCulture      the bio culture
     * @param aFluidInputs  may not be null
     * @param aFluidOutputs may not be null
     * @param aDuration
     * @param aEUt
     * @param Sv            Manual Sv entry i.e. for custom items
     * @param glasTier      the glass tier
     * @param aSpecialValue Space or Cleanroom, Not yet implemented
     * @param exactSv       if the recipe needs EXACTLY the Sv or can use less...
     * @return
     */
    public static boolean addBacterialVatRecipe(
            ItemStack[] aInputs,
            @Nonnull BioCulture aCulture,
            @Nonnull FluidStack[] aFluidInputs,
            @Nonnull FluidStack[] aFluidOutputs,
            @Nonnegative int aDuration,
            @Nonnegative int aEUt,
            int Sv,
            @Nonnegative int glasTier,
            int aSpecialValue,
            boolean exactSv) {
        return BWRecipes.instance.addBacterialVatRecipe(
                aInputs, aCulture, aFluidInputs, aFluidOutputs, aDuration, aEUt, Sv, glasTier, aSpecialValue, exactSv);
    }

    /**
     * @param aInputs       Item Inputs, DO NOT PUT INTEGRATED CIRCUITS IN HERE! THEY WILL GET ADDED AUTOMATICALLY!, can be null
     * @param aCulture      the bio culture
     * @param aFluidInputs  may not be null
     * @param aFluidOutputs may not be null
     * @param aDuration
     * @param aEUt
     * @param material      may be null. used for auto Sv calculation
     * @param glasTier      the glass tier
     * @param aSpecialValue Space or Cleanroom, Not yet implemented
     * @param exactSv       if the recipe needs EXACTLY the Sv or can use less...
     * @return
     */
    public static boolean addBacterialVatRecipe(
            ItemStack[] aInputs,
            @Nonnull BioCulture aCulture,
            @Nonnull FluidStack[] aFluidInputs,
            @Nonnull FluidStack[] aFluidOutputs,
            @Nonnegative int aDuration,
            @Nonnegative int aEUt,
            Materials material,
            @Nonnegative int glasTier,
            int aSpecialValue,
            boolean exactSv) {
        return BWRecipes.instance.addBacterialVatRecipe(
                aInputs,
                aCulture,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                material,
                glasTier,
                aSpecialValue,
                exactSv);
    }

    //    public static boolean addBacterialVatRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, FluidStack[]
    // aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSievert) {
    //        return BWRecipes.instance.addBacterialVatRecipe(aInputs, aOutputs, aFluidInputs, aFluidOutputs, aDuration,
    // aEUt, aSievert);
    //    }

    //    @Deprecated
    //    public static boolean addBioLabRecipeDNAExtraction(ItemStack[] aInputs, ItemStack aOutput, int[] aChances,
    // FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue){
    //        return BWRecipes.instance.addBioLabRecipeDNAExtraction(aInputs, aOutput, aChances, aFluidInputs,
    // aFluidOutputs, aDuration, aEUt, aSpecialValue);
    //    }
    //
    //    @Deprecated
    //    public static boolean addBioLabRecipePCRThermoclycling(ItemStack[] aInputs, ItemStack aOutput, int[] aChances,
    // FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue){
    //        return BWRecipes.instance.addBioLabRecipePCRThermoclycling(aInputs, aOutput, aChances, aFluidInputs,
    // aFluidOutputs, aDuration, aEUt, aSpecialValue);
    //    }
    //
    //    @Deprecated
    //    public static boolean addBioLabRecipePlasmidSynthesis(ItemStack[] aInputs, ItemStack aOutput, int[] aChances,
    // FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue){
    //        return BWRecipes.instance.addBioLabRecipePlasmidSynthesis(aInputs, aOutput, aChances, aFluidInputs,
    // aFluidOutputs, aDuration, aEUt, aSpecialValue);
    //    }
    //
    //    @Deprecated
    //    public static boolean addBioLabRecipeTransformation(ItemStack[] aInputs, ItemStack aOutput, int[] aChances,
    // FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue){
    //        return BWRecipes.instance.addBioLabRecipeTransformation(aInputs, aOutput, aChances, aFluidInputs,
    // aFluidOutputs, aDuration, aEUt, aSpecialValue);
    //    }
    //
    //    @Deprecated
    //    public static boolean addBioLabRecipeClonalCellularSynthesis(ItemStack[] aInputs, ItemStack aOutput, int[]
    // aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue){
    //        return BWRecipes.instance.addBioLabRecipeClonalCellularSynthesis(aInputs, aOutput, aChances, aFluidInputs,
    // aFluidOutputs, aDuration, aEUt, aSpecialValue);
    //    }

}
