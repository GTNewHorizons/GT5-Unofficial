package com.github.technus.tectech.recipe;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.thing.item.ElementalDefinitionContainer_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_machine;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gregtech.common.GT_RecipeAdder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TT_recipeAdder extends GT_RecipeAdder {
    public static final ItemStack[] nullItem=new ItemStack[0];
    public static final FluidStack[] nullFluid=new FluidStack[0];

    public static boolean addResearchableAssemblylineRecipe(ItemStack aResearchItem, int totalComputationRequired, int computationRequiredPerSec, int researchEUt, int researchAmperage, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int assDuration, int assEUt) {
        if(aInputs==null)aInputs=nullItem;
        if(aFluidInputs==null)aFluidInputs=nullFluid;
        if ((aResearchItem==null)||(totalComputationRequired<=0)||(aOutput == null) || aInputs.length>15) {
            return false;
        }
        for(ItemStack tItem : aInputs){
            if(tItem==null){
                TecTech.Logger.error("addResearchableAssemblingLineRecipe "+aResearchItem.getDisplayName()+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
            }
        }
        if(researchAmperage<=0) researchAmperage=1;
        else if(researchAmperage > Short.MAX_VALUE) researchAmperage=Short.MAX_VALUE;
        if(computationRequiredPerSec<=0) computationRequiredPerSec=1;
        else if(computationRequiredPerSec > Short.MAX_VALUE) computationRequiredPerSec=Short.MAX_VALUE;
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Writes Research result")}, null, null, totalComputationRequired, researchEUt, researchAmperage|(computationRequiredPerSec<<16));
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(false, aInputs, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Reads Research result")}, aFluidInputs, null, assDuration, assEUt, 0,true);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe.GT_Recipe_AssemblyLine( aResearchItem, 0/*ignored*/, aInputs, aFluidInputs, aOutput, assDuration, assEUt));
        return true;
    }

    /*
    (boolean aOptimize,
     ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
     FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue,
     cElementalDefinitionStackMap[] in, cElementalDefinitionStackMap[] out, cElementalDefinitionStackMap[] catalyst, AdditionalCheck check)
     */

    public static boolean addResearchableEMmachineRecipe(
            ItemStack aResearchItem, int totalComputationRequired, int computationRequiredPerSec, int researchEUt, int researchAmperage,
            ItemStack[] aInputs, FluidStack[] aFluidInputs, cElementalDefinitionStackMap[] eInputs,
            ItemStack aOutput, int machineDuration, int machineEUt, int machineAmperage) {
        if(aInputs==null)aInputs=nullItem;
        if(aFluidInputs==null)aFluidInputs=nullFluid;
        if ((aResearchItem==null)||(totalComputationRequired<=0)||(aOutput == null)) {
            return false;
        }
        for(ItemStack tItem : aInputs){
            if(tItem==null){
                TecTech.Logger.error("addResearchableEMmachineRecipe "+aResearchItem.getDisplayName()+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
            }
        }
        if(researchAmperage<=0) researchAmperage=1;
        else if(researchAmperage > Short.MAX_VALUE) researchAmperage=Short.MAX_VALUE;
        if(computationRequiredPerSec<=0) computationRequiredPerSec=1;
        else if(computationRequiredPerSec > Short.MAX_VALUE) computationRequiredPerSec=Short.MAX_VALUE;
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Writes Research result for "+ GT_MetaTileEntity_EM_machine.machine)}, null, null, totalComputationRequired, researchEUt, researchAmperage|(computationRequiredPerSec<<16));
        TT_recipe.TT_Recipe_Map.sMachineRecipes.add(new TT_recipe.TT_assLineRecipe(false,aResearchItem,aInputs,new ItemStack[]{aOutput},new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                        aFluidInputs,machineDuration,machineEUt,machineAmperage,eInputs));
        return true;
    }

    public static boolean addResearchableEMcrafterRecipe(
            ItemStack aResearchItem, int totalComputationRequired, int computationRequiredPerSec, int researchEUt, int researchAmperage,
            cElementalDefinitionStackMap[] eInputs, cElementalDefinitionStackMap[] catalyst, TT_recipe.AdditionalCheck check,
            ItemStack aOutput, int crafterDuration, int crafterEUt, int crafterAmperage) {
        if ((aResearchItem==null)||(totalComputationRequired<=0)||(aOutput == null)) {
            return false;
        }
        if(researchAmperage<=0) researchAmperage=1;
        else if(researchAmperage > Short.MAX_VALUE) researchAmperage=Short.MAX_VALUE;
        if(computationRequiredPerSec<=0) computationRequiredPerSec=1;
        else if(computationRequiredPerSec > Short.MAX_VALUE) computationRequiredPerSec=Short.MAX_VALUE;
        TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Writes Research result for "+ GT_MetaTileEntity_EM_crafting.crafter)}, null, null, totalComputationRequired, researchEUt, researchAmperage|(computationRequiredPerSec<<16));
        TT_recipe.TT_Recipe_Map.sCrafterRecipes.add(new TT_recipe.TT_assLineRecipe(false,aResearchItem,null,new ItemStack[]{aOutput},new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                        null,crafterDuration,crafterEUt,crafterAmperage,eInputs,null,catalyst,check));
        return true;
    }

    public static boolean addScannableEMmachineRecipe(
            iElementalDefinition aResearchEM, int totalComputationRequired, int computationRequiredPerSec, int researchEUt, int researchAmperage,
            ItemStack[] aInputs, FluidStack[] aFluidInputs, cElementalDefinitionStackMap[] eInputs,
            ItemStack aOutput, int machineDuration, int machineEUt, int machineAmperage) {
        if(aInputs==null)aInputs=nullItem;
        if(aFluidInputs==null)aFluidInputs=nullFluid;
        if ((aResearchEM==null)||(totalComputationRequired<=0)||(aOutput == null)) {
            return false;
        }
        for(ItemStack tItem : aInputs){
            if(tItem==null){
                TecTech.Logger.error("addScannableEMmachineRecipe "+aResearchEM+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
            }
        }
        if(researchAmperage<=0) researchAmperage=1;
        else if(researchAmperage > Short.MAX_VALUE) researchAmperage=Short.MAX_VALUE;
        if(computationRequiredPerSec<=0) computationRequiredPerSec=1;
        else if(computationRequiredPerSec > Short.MAX_VALUE) computationRequiredPerSec=Short.MAX_VALUE;
        ItemStack placeholder=new ItemStack(ElementalDefinitionContainer_EM.INSTANCE);
        ElementalDefinitionContainer_EM.setContent(placeholder,new cElementalDefinitionStackMap(new cElementalDefinitionStack(aResearchEM,1)));
        GT_Recipe thisRecipe=TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes.addFakeRecipe(false, new ItemStack[]{placeholder}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Writes Research result for "+ GT_MetaTileEntity_EM_machine.machine)}, null, null, totalComputationRequired, researchEUt, researchAmperage|(computationRequiredPerSec<<16));
        TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.add(new TT_recipe.TT_EMRecipe(false,thisRecipe,aResearchEM,aInputs,new ItemStack[]{aOutput},new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                aFluidInputs,machineDuration,machineEUt,machineAmperage,eInputs));
        return true;
    }

    public static boolean addScannableEMcrafterRecipe(
            iElementalDefinition aResearchEM, int totalComputationRequired, int computationRequiredPerSec, int researchEUt, int researchAmperage,
            cElementalDefinitionStackMap[] eInputs, cElementalDefinitionStackMap[] catalyst, TT_recipe.AdditionalCheck check,
            ItemStack aOutput, int crafterDuration, int crafterEUt, int crafterAmperage) {
        if ((aResearchEM==null)||(totalComputationRequired<=0)||(aOutput == null)) {
            return false;
        }
        if(researchAmperage<=0) researchAmperage=1;
        else if(researchAmperage > Short.MAX_VALUE) researchAmperage=Short.MAX_VALUE;
        if(computationRequiredPerSec<=0) computationRequiredPerSec=1;
        else if(computationRequiredPerSec > Short.MAX_VALUE) computationRequiredPerSec=Short.MAX_VALUE;
        ItemStack placeholder=new ItemStack(ElementalDefinitionContainer_EM.INSTANCE);
        ElementalDefinitionContainer_EM.setContent(placeholder,new cElementalDefinitionStackMap(new cElementalDefinitionStack(aResearchEM,1)));
        GT_Recipe thisRecipe=TT_recipe.GT_Recipe_MapTT.sScannableFakeRecipes.addFakeRecipe(false, new ItemStack[]{placeholder}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Writes Research result for "+ GT_MetaTileEntity_EM_crafting.crafter)}, null, null, totalComputationRequired, researchEUt, researchAmperage|(computationRequiredPerSec<<16));
        TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.add(new TT_recipe.TT_EMRecipe(false,thisRecipe,aResearchEM,null,new ItemStack[]{aOutput},new ItemStack[]{ItemList.Tool_DataOrb.getWithName(1L, "Reads Research result")},
                null,crafterDuration,crafterEUt,crafterAmperage,eInputs,null,catalyst,check));
        return true;
    }
}
