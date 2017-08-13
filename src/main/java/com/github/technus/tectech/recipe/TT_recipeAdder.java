package com.github.technus.tectech.recipe;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gregtech.common.GT_RecipeAdder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TT_recipeAdder extends GT_RecipeAdder {
    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int computationRequired, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if(aInputs==null)aInputs=new ItemStack[0];
        if(aFluidInputs==null)aFluidInputs=new FluidStack[0];
        if ((aResearchItem==null)||(computationRequired<=0)||(aOutput == null) || aInputs.length>15) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
            return false;
        }
        for(ItemStack tItem : aInputs){
            if(tItem==null){
                System.out.println("addAssemblingLineRecipe "+aResearchItem.getDisplayName()+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
            }
        }
        TT_recipe.TT_Recipe_Map.sResearchableFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Writes Research result")}, null, null, computationRequired, 30, 0);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(false, aInputs, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Reads Research result")}, aFluidInputs, null, aDuration, aEUt, 0,true);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe.GT_Recipe_AssemblyLine( aResearchItem, 0/*ignored*/, aInputs, aFluidInputs, aOutput, aDuration, aEUt));
        return true;
    }

    //public boolean addResearchableEMAssemblyRecipe(ItemStack aResearchItem, int computationRequired, rElementalRecipe emREcipe, int aDuration, int aEUt) {
    //    if ((aResearchItem==null)||(computationRequired<=0)||(emREcipe == null)) {
    //        return false;
    //    }
    // todo write recipe maps
    //    GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Writes Research result", new Object[0])}, null, null, computationRequired, 30, 0);
    //    GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(false, aInputs, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Reads Research result", new Object[0])}, aFluidInputs, null, aDuration, aEUt, 0,true);
    //    GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe.GT_Recipe_AssemblyLine( aResearchItem, computationRequired, aInputs, aFluidInputs, aOutput, aDuration, aEUt));
    //    return true;
    //}
}
