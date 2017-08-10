package com.github.technus.tectech.recipe;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gregtech.common.GT_RecipeAdder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.HashSet;

public class TT_recipe /*extends GT_Recipe*/ {
    public static class TT_Recipe_Map extends GT_Recipe.GT_Recipe_Map {
        public static TT_Recipe_Map sResearchableFakeRecipes =new TT_Recipe_Map(new HashSet(300), "gt.recipe.researchStation", "Research station", (String)null, "gregtech:textures/gui/basicmachines/Scanner", 1, 1, 1, 0, 1, "", 1, "", true, true);

        public TT_Recipe_Map (Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }
    }
}
