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

package com.github.bartimaeusnek.crossmod.tectech;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TecTechResearchLoader {

    @SuppressWarnings("deprecation")
    public static void runResearches() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        if (LoaderReference.galacticgreg) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemRegistry.voidminer[0].copy(),
                    1024000,
                    256,
                    BW_Util.getMachineVoltageFromTier(7),
                    24,
                    new Object[] {
                        ItemRegistry.voidminer[0].copy(),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 9L),
                        Materials.BlackPlutonium.getPlates(3),
                        ItemList.Electric_Motor_ZPM.get(9L),
                        ItemList.Sensor_ZPM.get(9L),
                        ItemList.Field_Generator_ZPM.get(9L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlackPlutonium, 36L)
                    },
                    new FluidStack[] {new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Krypton.getFluidOrGas(20000)
                    },
                    ItemRegistry.voidminer[1].copy(),
                    216000,
                    BW_Util.getMachineVoltageFromTier(7));

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemRegistry.voidminer[1].copy(),
                    8192000,
                    512,
                    BW_Util.getMachineVoltageFromTier(8),
                    64,
                    new Object[] {
                        ItemRegistry.voidminer[1].copy(),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 9L),
                        Materials.Neutronium.getPlates(3),
                        ItemList.Electric_Motor_UV.get(9L),
                        ItemList.Sensor_UV.get(9L),
                        ItemList.Field_Generator_UV.get(9L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 36L)
                    },
                    new FluidStack[] {
                        new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Oganesson.getFluidOrGas(20000)
                    },
                    ItemRegistry.voidminer[2].copy(),
                    432000,
                    BW_Util.getMachineVoltageFromTier(8));
        }

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                64000,
                48,
                BW_Util.getMachineVoltageFromTier(8),
                8,
                new Object[] {
                    ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                    Materials.Neutronium.getBlocks(5),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Osmium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 64),
                    ItemList.Electric_Piston_UV.get(64),
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 1440),
                    Materials.Osmium.getMolten(1440),
                    Materials.Neutronium.getMolten(1440)
                },
                ItemRegistry.eic.copy(),
                240000,
                BW_Util.getMachineVoltageFromTier(8));

        //        BartWorksCrossmod.LOGGER.info("Nerfing Assembly Lines >= LuV Recipes to run with TecTech!");
        //        HashSet<GT_Recipe.GT_Recipe_AssemblyLine> toRem = new HashSet<>();
        //        for (GT_Recipe.GT_Recipe_AssemblyLine recipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes){
        //            if (recipe.mEUt >= BW_Util.getTierVoltage(6) && !GT_Utility.areStacksEqual(recipe.mResearchItem,
        // CustomItemList.UnusedStuff.get(1L))){
        //                String modId = GameRegistry.findUniqueIdentifierFor(recipe.mOutput.getItem()).modId;
        //                if (!modId.equalsIgnoreCase("tectech"))
        //                    if (!modId.equalsIgnoreCase("gregtech") || modId.equalsIgnoreCase("gregtech") &&
        // (recipe.mOutput.getItemDamage() < 15000 || recipe.mOutput.getItemDamage() > 16999))
        //                        toRem.add(recipe);
        //            }
        //        }
        //        HashSet<GT_Recipe> toRemVisualScanner = new HashSet<>();
        //        HashSet<GT_Recipe> toRemVisualAssLine = new HashSet<>();
        //        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.removeAll(toRem);
        //
        //        for (GT_Recipe.GT_Recipe_AssemblyLine recipe : toRem){
        //            GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.mRecipeList.stream().filter(re ->
        // GT_Utility.areStacksEqual(re.mOutputs[0],recipe.mOutput)).forEach(toRemVisualAssLine::add);
        //            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.mRecipeList.stream().filter(re ->
        // GT_Utility.areStacksEqual(re.mOutputs[0],recipe.mOutput)).forEach(toRemVisualScanner::add);
        //            TT_recipeAdder.addResearchableAssemblylineRecipe(recipe.mResearchItem, recipe.mResearchTime,
        // recipe.mResearchTime/1000, recipe.mEUt, GT_Utility.getTier(recipe.mEUt)-2, recipe.mInputs,
        // recipe.mFluidInputs, recipe.mOutput, recipe.mDuration, recipe.mEUt);
        //        }
        //
        //        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.mRecipeList.removeAll(toRemVisualScanner);
        //        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.mRecipeList.removeAll(toRemVisualAssLine);
    }
}
