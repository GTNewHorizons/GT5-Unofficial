package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Plates extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Plates(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){

		final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;
		final ItemStack ingotStackOne = material.getIngot(1);
		final ItemStack ingotStackTwo = material.getIngot(2);
		final ItemStack shape_Mold = ItemList.Shape_Mold_Plate.get(0);
		final ItemStack plate_Single = material.getPlate(1);
		final ItemStack plate_SingleTwo = material.getPlate(2);
		final ItemStack plate_Double = material.getPlateDouble(1);

		Logger.WARNING("Generating Plate recipes for "+material.getLocalizedName());

		//Forge Hammer
		if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single))
		if (addForgeHammerRecipe(
				ingotStackTwo,
				plate_Single,
				(int) Math.max(material.getMass(), 1L),
				material.vVoltageMultiplier)){
			Logger.WARNING("Forge Hammer Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Forge Hammer Recipe: "+material.getLocalizedName()+" - Failed");
		}
		//Bender
		if (ItemUtils.checkForInvalidItems(ingotStackOne) && ItemUtils.checkForInvalidItems(plate_Single))
		if (addBenderRecipe(
				ingotStackOne,
				plate_Single,
				(int) Math.max(material.getMass() * 1L, 1L),
				material.vVoltageMultiplier)){
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");
		}
		//Alloy Smelter
		if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single))
		if (GT_Values.RA.addAlloySmelterRecipe(
				ingotStackTwo,
				shape_Mold,
				plate_Single,
				(int) Math.max(material.getMass() * 2L, 1L),
				material.vVoltageMultiplier)){
			Logger.WARNING("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Failed");
		}


		//Making Double Plates
		if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Double))
		if (addBenderRecipe(
				ingotStackTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				material.vVoltageMultiplier)){
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");
		}
		
		if (ItemUtils.checkForInvalidItems(plate_SingleTwo) && ItemUtils.checkForInvalidItems(plate_Double))
		if (addBenderRecipe(
				plate_SingleTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				material.vVoltageMultiplier)){
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");
		}
	}

	public static boolean addBenderRecipe(final ItemStack aInput1, final ItemStack aOutput1, int aDuration, final int aEUt) {
		if ((aInput1 == null) || (aOutput1 == null)) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
			return false;
		}
		new GT_Recipe(aEUt, aDuration, aInput1, aOutput1);
		return true;
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

	public static boolean addForgeHammerRecipe(final ItemStack aInput1, final ItemStack aOutput1, final int aDuration, final int aEUt) {
		if ((aInput1 == null) || (aOutput1 == null)) {
			return false;
		}
		if (!GregTech_API.sRecipeFile.get("forgehammer", aOutput1, true)) {
			return false;
		}
		GT_Recipe.GT_Recipe_Map.sHammerRecipes.addRecipe(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1}, null, null, null, aDuration, aEUt, 0);
		return true;
	}



}
