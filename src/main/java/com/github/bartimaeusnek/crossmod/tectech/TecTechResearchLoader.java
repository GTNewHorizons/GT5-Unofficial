package com.github.bartimaeusnek.crossmod.tectech;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraftforge.fluids.FluidStack;

public class TecTechResearchLoader  {

    public static void runResearches(){

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                64000,
                48,
                BW_Util.getMachineVoltageFromTier(8),
                8,
                new Object[]{
                        ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                        Materials.Neutronium.getBlocks(5),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong,Materials.Osmium,64),
                        GT_OreDictUnificator.get(OrePrefixes.ring,Materials.Osmium,64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01,Materials.Superconductor,64),
                        ItemList.Electric_Piston_UV.get(64),
                },
                new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(1440),
                        Materials.Osmium.getMolten(1440),
                        Materials.Neutronium.getMolten(1440)
                },
                ItemRegistry.eic.copy(),
                240000,
                BW_Util.getMachineVoltageFromTier(8)
        );

//        BartWorksCrossmod.LOGGER.info("Nerfing Assembly Lines >= LuV Recipes to run with TecTech!");
//        HashSet<GT_Recipe.GT_Recipe_AssemblyLine> toRem = new HashSet<>();
//        for (GT_Recipe.GT_Recipe_AssemblyLine recipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes){
//            if (recipe.mEUt >= BW_Util.getTierVoltage(6) && !GT_Utility.areStacksEqual(recipe.mResearchItem, CustomItemList.UnusedStuff.get(1L))){
//                String modId = GameRegistry.findUniqueIdentifierFor(recipe.mOutput.getItem()).modId;
//                if (!modId.equalsIgnoreCase("tectech"))
//                    if (!modId.equalsIgnoreCase("gregtech") || modId.equalsIgnoreCase("gregtech") && (recipe.mOutput.getItemDamage() < 15000 || recipe.mOutput.getItemDamage() > 16999))
//                        toRem.add(recipe);
//            }
//        }
//        HashSet<GT_Recipe> toRemVisual = new HashSet<>();
//        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.removeAll(toRem);
//
//        for (GT_Recipe.GT_Recipe_AssemblyLine recipe : toRem){
//            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.mRecipeList.stream().filter(re -> GT_Utility.areStacksEqual(re.mOutputs[0],recipe.mOutput)).forEach(toRemVisual::add);
//            TT_recipeAdder.addResearchableAssemblylineRecipe(recipe.mResearchItem, recipe.mResearchTime, recipe.mResearchTime/1000, recipe.mEUt, GT_Utility.getTier(recipe.mEUt)-2, recipe.mInputs, recipe.mFluidInputs, recipe.mOutput, recipe.mDuration, recipe.mEUt);
//        }
//
//        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.mRecipeList.removeAll(toRemVisual);
    }
}
