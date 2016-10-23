package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class RecipeGen_ShapedCrafting {

	public static void generateRecipes(Material material){
		Utils.LOG_INFO("Generating Shaped Crafting recipes for "+material.getLocalizedName()); //TODO
		//Ring Recipe
		if (!material.isRadioactive){
			if (UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolWrench", null, null,
					null, material.getRod(1), null,
					null, null, null,
					material.getRing(1))){
				Utils.LOG_INFO("Ring Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_INFO("Ring Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}

		//Framebox Recipe
		if (!material.isRadioactive){
			ItemStack stackStick = material.getRod(1);
			if (UtilsRecipe.addShapedGregtechRecipe(
					stackStick, stackStick, stackStick,
					stackStick, "craftingToolWrench", stackStick,
					stackStick, stackStick, stackStick,
					material.getFrameBox(2))){
				Utils.LOG_INFO("Framebox Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_INFO("Framebox Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}

		//Shaped Recipe - Bolts
		if (!material.isRadioactive){
			if (UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolSaw", null, null,
					null, material.getRod(1), null,
					null, null, null,
					material.getBolt(2))){
				Utils.LOG_INFO("Bolt Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_INFO("Bolt Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}


		//Shaped Recipe - Ingot to Rod
		if (UtilsRecipe.addShapedGregtechRecipe(
				"craftingToolFile", null, null,
				null, material.getIngot(1), null,
				null, null, null,
				material.getRod(1))){
			Utils.LOG_INFO("Rod Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Rod Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		//Shaped Recipe - Long Rod to two smalls
		if (UtilsRecipe.addShapedGregtechRecipe(
				"craftingToolSaw", null, null,
				material.getLongRod(1), null, null,
				null, null, null,
				material.getRod(2))){
			Utils.LOG_INFO("Rod Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Rod Recipe: "+material.getLocalizedName()+" - Failed");			
		}

		//Two small to long rod
		if (UtilsRecipe.addShapedGregtechRecipe(
				material.getRod(1), "craftingToolHardHammer", material.getRod(1),
				null, null, null,
				null, null, null,
				material.getLongRod(1))){
			Utils.LOG_INFO("Long Rod Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Long Rod Recipe: "+material.getLocalizedName()+" - Failed");			
		}

		//Rotor Recipe
		if (!material.isRadioactive){
			if (UtilsRecipe.addShapedGregtechRecipe(
					material.getPlate(1), "craftingToolHardHammer", material.getPlate(1),
					material.getScrew(1), material.getRing(1), "craftingToolFile",
					material.getPlate(1), "craftingToolScrewdriver", material.getPlate(1),
					material.getRotor(1))){
				Utils.LOG_INFO("Rotor Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_INFO("Rotor Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}

		//Screws
		if (!material.isRadioactive){
			if (UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolFile", material.getBolt(1), null,
					material.getBolt(1), null, null,
					null, null, null,
					material.getScrew(1))){
				Utils.LOG_INFO("Screw Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_INFO("Screw Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}
	}
}
