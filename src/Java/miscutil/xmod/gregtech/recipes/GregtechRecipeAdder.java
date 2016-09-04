package miscutil.xmod.gregtech.recipes;

import gregtech.api.GregTech_API;
import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import miscutil.xmod.gregtech.api.util.GregtechRecipe;
import miscutil.xmod.gregtech.recipes.machines.RECIPEHANDLER_CokeOven;
import miscutil.xmod.gregtech.recipes.machines.RECIPEHANDLER_MatterFabricator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

	

	@Override
	public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
		try {
			try {
				RECIPEHANDLER_CokeOven.debug1();
				if (((aInput1 == null) /*&& (aFluidInput == null)*/) || ((aOutput == null) || (aFluidOutput == null))) {
					Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				RECIPEHANDLER_CokeOven.debug2(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
					Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				RECIPEHANDLER_CokeOven.debug3(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aFluidOutput == null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
					Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				RECIPEHANDLER_CokeOven.debug4(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if (aFluidInput == null){
					GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_CokeOven.debug5(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);

				return true;

			} catch (NullPointerException e){
				return false;
			}
		} catch (Throwable e){
			Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addMatterFabricatorRecipe(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt) {
		try {
			try {
				RECIPEHANDLER_MatterFabricator.debug1();
				if (aFluidOutput == null) {
					Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				RECIPEHANDLER_MatterFabricator.debug2(aFluidInput, aFluidOutput, aDuration, aEUt);
				if ((aFluidOutput == null)/* && ((aDuration = GregTech_API.sRecipeFile.get("matterfab", null, aDuration)) <= 0)*/) {
					Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			/*try {

				RECIPEHANDLER_MatterFabricator.debug3(aFluidInput, aFluidOutput, aDuration, aEUt);
				if ((aFluidOutput == null) && ((aDuration = GregTech_API.sRecipeFile.get("matterfab", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
					Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_WARNING("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}*/
			try {
				RECIPEHANDLER_MatterFabricator.debug4(aFluidInput, aFluidOutput, aDuration, aEUt);
				if (aFluidInput == null){
					//GregtechRecipe.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
					GregtechRecipe.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					//GregtechRecipe.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
					GregtechRecipe.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);

				return true;

			} catch (NullPointerException e){
				return false;
			}
		} catch (Throwable e){
			Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}

	@Override
	public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
        if (aInput1 == null) {
        	Utils.LOG_INFO("Fuel Input is Invalid.");
            return false;
        }
        new GregtechRecipe(aInput1, aOutput1, GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
        return true;
    }

	@Override
	public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, FluidStack aFluid, ItemStack[] aOutputItems, FluidStack aOutputFluid, int aDuration, int aEUt) {
		if ((aItemA == null) || (aItemB == null) || (aOutputItems == null)) {
            return false;
        }
        for (ItemStack tStack : aOutputItems) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
                    return false;
                }
                GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aItemA, aItemB}, aOutputItems.clone(), null, null, null, aDuration, aEUt, 0);
                return true;
            }
        }
        return false;
	}
	
	@Override
	public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, ItemStack[] aOutputItems, int aDuration, int aEUt) {
        if ((aItemA == null) || (aItemB == null) || (aOutputItems == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
            return false;
        }
        GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aItemA, aItemB}, aOutputItems.clone(), null, null, null, aDuration, aEUt, 0);
        return true;
    }
	
	@Override
	public boolean addDehydratorRecipe(ItemStack aInput, FluidStack aFluid, ItemStack[] aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aInput, aDuration)) <= 0) {
            return false;
        }
        GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutput.clone(), null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
        return true;
    }


}
