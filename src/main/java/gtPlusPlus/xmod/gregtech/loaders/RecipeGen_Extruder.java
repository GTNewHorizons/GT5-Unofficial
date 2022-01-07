package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Extruder extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Extruder(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){

		final ItemStack itemIngot = material.getIngot(1);
		final ItemStack itemPlate = material.getPlate(1);
		final ItemStack itemGear = material.getGear(1);

		final ItemStack shape_Plate = ItemList.Shape_Extruder_Plate.get(0);
		final ItemStack shape_Ring = ItemList.Shape_Extruder_Ring.get(0);
		final ItemStack shape_Gear = ItemList.Shape_Extruder_Gear.get(0);
		final ItemStack shape_Rod = ItemList.Shape_Extruder_Rod.get(0);
		final ItemStack shape_Bolt = ItemList.Shape_Extruder_Bolt.get(0);
		final ItemStack shape_Block = ItemList.Shape_Extruder_Block.get(0);
		final ItemStack shape_Ingot = ItemList.Shape_Extruder_Ingot.get(0);

		Logger.WARNING("Generating Extruder recipes for "+material.getLocalizedName());

		
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getBlock(1))) {
			//Ingot Recipe
			if (addExtruderRecipe(
					material.getBlock(1),
					shape_Ingot,
					material.getIngot(9),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					material.vVoltageMultiplier)){
				Logger.WARNING("Extruder Ingot Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("Extruder Ingot Recipe: "+material.getLocalizedName()+" - Failed");
			}

			//Block Recipe
			if (addExtruderRecipe(
					material.getIngot(9),
					shape_Block,
					material.getBlock(1),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					material.vVoltageMultiplier)){
				Logger.WARNING("Extruder Block Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("Extruder Block Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}


		//Plate Recipe
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getPlate(1)))
		if (addExtruderRecipe(
				itemIngot,
				shape_Plate,
				itemPlate,
				10, 
				material.vVoltageMultiplier)){
			Logger.WARNING("Extruder Plate Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Extruder Plate Recipe: "+material.getLocalizedName()+" - Failed");
		}

		//Ring Recipe
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getRing(1)))
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					itemIngot,
					shape_Ring,
					material.getRing(4),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					material.vVoltageMultiplier)){
				Logger.WARNING("Extruder Ring Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("Extruder Ring Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}


		//Gear Recipe
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getGear(1)))
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					material.getIngot(4),
					shape_Gear,
					itemGear,
					(int) Math.max(material.getMass() * 5L, 1),
					material.vVoltageMultiplier)){
				Logger.WARNING("Extruder Gear Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("Extruder Gear Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}


		//Rod Recipe
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getRod(1)))
		if (addExtruderRecipe(
				itemIngot,
				shape_Rod,
				material.getRod(2),
				(int) Math.max(material.getMass() * 2L * 1, 1),
				material.vVoltageMultiplier)){
			Logger.WARNING("Extruder Rod Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Extruder Rod Recipe: "+material.getLocalizedName()+" - Failed");
		}


		//Bolt Recipe
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getBolt(1)))
		if (!material.isRadioactive){
			if (addExtruderRecipe(
					itemIngot,
					shape_Bolt,
					material.getBolt(8),
					(int) Math.max(material.getMass() * 2L * 1, 1),
					material.vVoltageMultiplier)){
				Logger.WARNING("Extruder Bolt Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("Extruder Bolt Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}

	}


	public static boolean addExtruderRecipe(final ItemStack aInput, final ItemStack aShape, final ItemStack aOutput, int aDuration, final int aEUt) {
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
