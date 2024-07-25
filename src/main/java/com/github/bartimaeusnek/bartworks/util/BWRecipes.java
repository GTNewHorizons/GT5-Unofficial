/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util;

import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bacterialVatRecipes;
import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bioLabRecipes;
import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.radioHatchRecipes;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.calculateSv;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.specialToByte;
import static gregtech.api.util.GT_RecipeConstants.DECAY_TICKS;
import static gregtech.api.util.GT_RecipeConstants.SIEVERTS;

import javax.annotation.Nonnegative;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

@SuppressWarnings("UnusedReturnValue")
public class BWRecipes {

    public static final BWRecipes instance = new BWRecipes();

    public static long calcDecayTicks(int x) {
        long ret;
        if (x == 43) ret = 5000;
        else if (x == 61) ret = 4500;
        else if (x <= 100) ret = MathUtils.ceilLong((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else ret = MathUtils.ceilLong(8000D * Math.tanh(-x / 65D) + 8000D);
        return ret;
    }

    public static int computeSieverts(int givenSievert, int specialValue, int glassTier,
        boolean requiresExactSieverts) {
        int sievertValue = 0;
        if (givenSievert >= 83 || givenSievert == 61 || givenSievert == 43) sievertValue += givenSievert;
        sievertValue = sievertValue << 1;
        sievertValue = sievertValue | (requiresExactSieverts ? 1 : 0);
        sievertValue = sievertValue << 2;
        sievertValue = sievertValue | specialToByte(specialValue);
        sievertValue = sievertValue << 4;
        sievertValue = sievertValue | glassTier;
        return sievertValue;
    }

    public static int computeSieverts(Materials material, int aSpecialValue, int glassTier,
        boolean requiresExactSieverts) {
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += calculateSv(material);
        aSievert = aSievert << 1;
        aSievert = aSievert | (requiresExactSieverts ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialToByte(aSpecialValue);
        aSievert = aSievert << 4;
        aSievert = aSievert | glassTier;
        return aSievert;
    }

    @Deprecated
    public boolean addRadHatch(ItemStack item, int radioLevel, int amount, short[] rgba) {
        GT_Values.RA.stdBuilder()
            .itemInputs(item)
            .duration(amount)
            .eut(radioLevel)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(radioLevel))
            .noOptimize()
            .addTo(radioHatchRecipes);
        return true;
    }

    @Deprecated
    public boolean addBioLabRecipe(ItemStack[] aInputs, ItemStack aOutput, ItemStack aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        GT_Values.RA.stdBuilder()
            .itemInputs(aInputs)
            .itemOutputs(aOutput)
            .outputChances(aChances)
            .special(aSpecialItems)
            .fluidInputs(aFluidInputs)
            .fluidOutputs(aFluidOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .specialValue(aSpecialValue)
            .addTo(bioLabRecipes);

        return true;
    }

    @Deprecated
    public boolean addBioLabRecipeIncubation(ItemStack aInput, BioCulture aOutput, int[] aChances,
        FluidStack[] aFluidInputs, int aDuration, int aEUt, int aSpecialValue) {
        GT_Values.RA.stdBuilder()
            .itemInputs(BioItemList.getPetriDish(null), aInput)
            .itemOutputs(BioItemList.getPetriDish(aOutput))
            .outputChances(aChances)
            .fluidInputs(aFluidInputs)
            .duration(aDuration)
            .eut(aEUt)
            .specialValue(aSpecialValue)
            .addTo(bioLabRecipes);
        return true;
    }

    @Deprecated
    public boolean addBioLabRecipeIncubation(ItemStack aInput, BioCulture aOutput, int[] aChances,
        FluidStack aFluidInputs, int aDuration, int aEUt, int aSpecialValue) {
        GT_Values.RA.stdBuilder()
            .itemInputs(BioItemList.getPetriDish(null), aInput)
            .itemOutputs(BioItemList.getPetriDish(aOutput))
            .outputChances(aChances)
            .fluidInputs(aFluidInputs)
            .duration(aDuration)
            .eut(aEUt)
            .specialValue(aSpecialValue)
            .addTo(bioLabRecipes);

        return true;
    }

    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture aCulture, FluidStack[] aFluidInputs,
        FluidStack[] aFluidOutputs, @Nonnegative int aDuration, @Nonnegative int aEUt, @Nonnegative int aSv,
        @Nonnegative int glasTier, int aSpecialValue, boolean exactSv) {
        GT_Values.RA.stdBuilder()
            .itemInputs(aInputs)
            .special(BioItemList.getPetriDish(aCulture))
            .fluidInputs(aFluidInputs)
            .fluidOutputs(aFluidOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .metadata(SIEVERTS, computeSieverts(aSv, aSpecialValue, glasTier, exactSv))
            .noOptimize()
            .addTo(bacterialVatRecipes);

        return true;
    }

    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture aCulture, FluidStack[] aFluidInputs,
        FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, @Nonnegative int glasTier,
        int aSpecialValue, boolean exactSv) {
        GT_Values.RA.stdBuilder()
            .itemInputs(aInputs)
            .special(BioItemList.getPetriDish(aCulture))
            .fluidInputs(aFluidInputs)
            .fluidOutputs(aFluidOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .metadata(SIEVERTS, computeSieverts(material, aSpecialValue, glasTier, exactSv))
            .noOptimize()
            .addTo(bacterialVatRecipes);

        return true;
    }

    /**
     * Adds a Vat recipe without Rad requirements but with Glas requirements
     */
    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture culture, FluidStack[] aFluidInputs,
        FluidStack[] aFluidOutputs, int aDuration, int aEUt, byte glasTier) {
        int aSievert = 0;
        aSievert = aSievert | glasTier;
        GT_Values.RA.stdBuilder()
            .itemInputs(aInputs)
            .special(BioItemList.getPetriDish(culture))
            .fluidInputs(aFluidInputs)
            .fluidOutputs(aFluidOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .metadata(SIEVERTS, aSievert)
            .noOptimize()
            .addTo(bacterialVatRecipes);

        return true;
    }

    /**
     * Adds a Vat recipe without Rad or Glas requirements
     */
    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, BioCulture culture,
        FluidStack[] aFluidOutputs, int aDuration, int aEUt) {
        GT_Values.RA.stdBuilder()
            .itemInputs(aInputs)
            .special(BioItemList.getPetriDish(culture))
            .fluidInputs(aFluidInputs)
            .fluidOutputs(aFluidOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .noOptimize()
            .addTo(bacterialVatRecipes);

        return true;
    }
}
