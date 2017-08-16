package com.github.technus.tectech.recipe;

import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class TT_recipe extends GT_Recipe {
    public final cElementalDefinitionStackMap input[],output[], eCatalyst[];
    public final AdditionalCheck additionalCheck;

    public TT_recipe(boolean aOptimize,
                     ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
                     FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue,
                     cElementalDefinitionStackMap[] in, cElementalDefinitionStackMap[] out, cElementalDefinitionStackMap[] catalyst, AdditionalCheck check){
        super(aOptimize,aInputs,aOutputs,aSpecialItems,aChances,aFluidInputs,aFluidOutputs,aDuration,aEUt,aSpecialValue);
        input=in;
        output=out;
        eCatalyst=catalyst;
        additionalCheck=check;
    }

    public boolean EMisRecipeInputEqual(boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks){
        return EMisRecipeInputEqual(consume,doNotCheckStackSizes,itemStacks,fluidStacks,null,null);
    }

    public boolean EMisRecipeInputEqual(boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap[] in){
        return EMisRecipeInputEqual(consume,doNotCheckStackSizes,itemStacks,fluidStacks,in,null);
    }
    
    public boolean EMisRecipeInputEqual(boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap[] in, cElementalInstanceStackMap[] catalyst) {
        if(additionalCheck !=null && !additionalCheck.check(this,consume,doNotCheckStackSizes,itemStacks,fluidStacks,in,catalyst)) return false;
        if (this.eCatalyst != null) {
            if (catalyst != null && catalyst.length >= this.eCatalyst.length) {
                for (int i = 0; i < this.eCatalyst.length; i++) {
                    if (this.eCatalyst[i] != null && this.eCatalyst[i].hasStacks()) {
                        if (catalyst[i] != null && catalyst[i].hasStacks()) {
                            if (!catalyst[i].removeAllAmounts(true, this.eCatalyst[i])) return false;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        if (input != null) {
            if (in != null && in.length >= input.length) {
                for (int i = 0; i < input.length; i++) {
                    if (input[i] != null && input[i].hasStacks()) {
                        if (in[i] != null && in[i].hasStacks()) {
                            if (!in[i].removeAllAmounts(consume, input[i])) return false;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return super.isRecipeInputEqual(consume, doNotCheckStackSizes, fluidStacks, itemStacks);
    }

    public boolean EMisRecipeInputEqualConsumeFromOne(boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap in){
        return EMisRecipeInputEqualConsumeFromOne(consume,doNotCheckStackSizes,itemStacks,fluidStacks,in,null);
    }

    public boolean EMisRecipeInputEqualConsumeFromOne(boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap in, cElementalInstanceStackMap[] catalyst) {
        if(additionalCheck !=null && !additionalCheck.check(this,consume,doNotCheckStackSizes,itemStacks,fluidStacks,in,catalyst)) return false;
        if (this.eCatalyst != null) {
            if (catalyst != null && catalyst.length >= this.eCatalyst.length) {
                for (int i = 0; i < this.eCatalyst.length; i++) {
                    if (this.eCatalyst[i] != null && this.eCatalyst[i].hasStacks()) {
                        if (catalyst[i] != null && catalyst[i].hasStacks()) {
                            if (!catalyst[i].removeAllAmounts(true, this.eCatalyst[i])) return false;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        if (input != null) {
            if (in != null) {
                for (int i = 0; i < input.length; i++) {
                    if (input[i] != null && input[i].hasStacks()) {
                        if (in.hasStacks()) {
                            if (!in.removeAllAmounts(consume, input[i])) return false;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return super.isRecipeInputEqual(consume, doNotCheckStackSizes, fluidStacks, itemStacks);
    }
    
    public interface AdditionalCheck {
        boolean check(TT_recipe thisRecipe, boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap[] in, cElementalInstanceStackMap[] e);
        boolean check(TT_recipe thisRecipe, boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks, cElementalInstanceStackMap in, cElementalInstanceStackMap[] e);
    }

    public static class TT_Recipe_Map<T extends TT_recipe> {
        public static TT_Recipe_Map<TT_assLineRecipe> sEMcrafterRecipes = new TT_Recipe_Map();
        public static TT_Recipe_Map<TT_assLineRecipe> sEMmachineRecipes = new TT_Recipe_Map();

        public final HashMap<String,T> mRecipeMap;
        
        public TT_Recipe_Map(){
            mRecipeMap =new HashMap<>(16);
        }

        public T findRecipe(String identifier){
            return mRecipeMap.get(identifier);
        }
        
        public T findRecipe(ItemStack dataHandler){
            if(dataHandler==null || dataHandler.stackTagCompound==null) return null;
            return mRecipeMap.get(dataHandler.stackTagCompound.getString("mapID"));
        }

        public void add(String identifier, T recipe){
            if(identifier.length()==0) return;
            mRecipeMap.put(identifier,recipe);
        }

        public Collection<T> recipeList(){
            return mRecipeMap.values();
        }
    }

    public static class GT_Recipe_MapTT extends GT_Recipe.GT_Recipe_Map {
        public static GT_Recipe_MapTT sResearchableFakeRecipes =new GT_Recipe_MapTT(new HashSet(30), "gt.recipe.researchStation", "Research station", (String)null, "gregtech:textures/gui/multimachines/ResearchFake", 1, 1, 1, 0, 1, "", 1, "", true, false);//nei to false - using custom handler

        public GT_Recipe_MapTT(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }
    }

    public static class TT_assLineRecipe extends TT_recipe{
        public final ItemStack mResearchItem;
        public final String id;

        public TT_assLineRecipe(boolean aOptimize, ItemStack researchItem,
                                ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
                                FluidStack[] aFluidInputs, int aDuration, int aEUt, int aSpecialValue,
                                cElementalDefinitionStackMap[] in, cElementalDefinitionStackMap[] out, cElementalDefinitionStackMap[] catalyst, AdditionalCheck check) {
            super(aOptimize, aInputs, aOutputs, aSpecialItems, null, aFluidInputs, null, aDuration, aEUt, aSpecialValue, in, out, catalyst, check);
            mResearchItem=researchItem;
            GameRegistry.UniqueIdentifier id=GameRegistry.findUniqueIdentifierFor(aOutputs[0].getItem());
            this.id=id.modId+":"+id.name+":"+aOutputs[0].getItemDamage();
        }

        public TT_assLineRecipe(boolean aOptimize, ItemStack researchItem,
                                ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
                                FluidStack[] aFluidInputs, int aDuration, int aEUt, int aSpecialValue,
                                cElementalDefinitionStackMap[] in) {
            this(aOptimize, researchItem, aInputs, aOutputs, aSpecialItems, aFluidInputs, aDuration, aEUt, aSpecialValue, in, null, null,null);
        }
    }
}
