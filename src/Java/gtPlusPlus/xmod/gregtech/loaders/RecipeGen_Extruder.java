package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;

public class RecipeGen_Extruder {

	public static void generateRecipes(Material material){

		int tVoltageMultiplier = material.getMeltingPoint_K() >= 2800 ? 64 : 16;
		ItemStack itemIngot = material.getIngot(1);
		ItemStack plate_Single = material.getPlate(1);
		ItemStack itemGear = material.getGear(1);

		ItemStack shape_Plate = ItemList.Shape_Extruder_Plate.get(0);
		ItemStack shape_Ring = ItemList.Shape_Extruder_Ring.get(0);
		ItemStack shape_Gear = ItemList.Shape_Extruder_Gear.get(0);
		ItemStack shape_Rod = ItemList.Shape_Extruder_Rod.get(0);
		ItemStack shape_Bolt = ItemList.Shape_Extruder_Bolt.get(0);

		Utils.LOG_WARNING("Generating Extruder recipes for "+material.getLocalizedName());


		//Plate Recipe
		if (addExtruderRecipe(
				itemIngot,
				shape_Plate,
				plate_Single,
				10, 4 * tVoltageMultiplier)){
			Utils.LOG_WARNING("Extruder Plate Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Extruder Plate Recipe: "+material.getLocalizedName()+" - Failed");			
		}

		//Ring Recipe
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					itemIngot,
					shape_Ring,
					material.getRing(4),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					6 * material.vVoltageMultiplier)){
				Utils.LOG_WARNING("Extruder Ring Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_WARNING("Extruder Ring Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}


		//Gear Recipe
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					material.getIngot(8),
					shape_Gear,
					itemGear,
					(int) Math.max(material.getMass() * 5L, 1),
					8 * material.vVoltageMultiplier)){
				Utils.LOG_WARNING("Extruder Gear Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_WARNING("Extruder Gear Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}


		//Rod Recipe
		if (addExtruderRecipe(
				itemIngot,
				shape_Rod,
				material.getRod(2),
				(int) Math.max(material.getMass() * 2L * 1, 1),
				6 * material.vVoltageMultiplier)){
			Utils.LOG_WARNING("Extruder Rod Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Extruder Rod Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		//Bolt Recipe
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					itemIngot, 
					shape_Bolt, 
					material.getBolt(8),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					6 * material.vVoltageMultiplier)){
				Utils.LOG_WARNING("Extruder Bolt Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Utils.LOG_WARNING("Extruder Bolt Recipe: "+material.getLocalizedName()+" - Failed");			
			}
		}

	}	


	public static boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
		if ((aInput == null) || (aShape == null) || (aOutput == null)) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("extruder", aOutput, aDuration)) <= 0) {
			return false;
		}
		GT_Recipe.GT_Recipe_Map.sExtruderRecipes.addRecipe(true, new ItemStack[]{aInput, aShape}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
		return true;
	}



}
