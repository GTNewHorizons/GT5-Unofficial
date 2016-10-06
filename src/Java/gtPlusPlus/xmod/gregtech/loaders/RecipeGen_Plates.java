package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;

public class RecipeGen_Plates {

	public static void generateRecipes(Material material){

		int tVoltageMultiplier = material.getMeltingPoint_K() >= 2800 ? 64 : 16;
		ItemStack ingotStackOne = material.getIngot(1);
		ItemStack ingotStackTwo = material.getIngot(2);
		ItemStack shape_Mold = ItemList.Shape_Mold_Plate.get(1);
		ItemStack shape_Extruder = ItemList.Shape_Extruder_Plate.get(1);
		ItemStack plate_Single = material.getPlate(1);
		ItemStack plate_SingleTwo = material.getPlate(2);
		ItemStack plate_Double = material.getPlateDouble(1);

		Utils.LOG_INFO("Generating Plate recipes for "+material.getLocalizedName());
		
		//Forge Hammer
		if (addForgeHammerRecipe(
				ingotStackTwo,
				plate_Single,
				(int) Math.max(material.getMass(), 1L),
				16)){
			Utils.LOG_INFO("Forge Hammer Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Forge Hammer Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		//Bender
		if (addBenderRecipe(
				ingotStackOne,
				plate_Single,
				(int) Math.max(material.getMass() * 1L, 1L),
				24)){
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		//Extruder
		if (addExtruderRecipe(
				ingotStackOne,
				shape_Extruder,
				plate_Single,
				10, 4 * tVoltageMultiplier)){
			Utils.LOG_INFO("Extruder Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Extruder Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		//Alloy Smelter
		if (GT_Values.RA.addAlloySmelterRecipe(
				ingotStackTwo,
				shape_Mold,
				plate_Single,
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier)){
			Utils.LOG_INFO("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		//Making Double Plates
		if (addBenderRecipe(
				ingotStackTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96)){
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		if (addBenderRecipe(
				plate_SingleTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96)){
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		Utils.LOG_INFO("Adding crafting recipes for "+material.getLocalizedName()+" Plates - Single & Double");

		//Single Plate Shaped/Shapeless
		GT_ModHandler.addCraftingRecipe(
				plate_Single,
				gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"h", "B", "I",
						Character.valueOf('I'),
						ingotStackOne,
						Character.valueOf('B'),
						ingotStackOne});

		GT_ModHandler.addShapelessCraftingRecipe(
				plate_Single,
				new Object[]{gregtech.api.enums.ToolDictNames.craftingToolForgeHammer,
						ingotStackOne,
						ingotStackOne});	     

		//Double Plate Shaped/Shapeless
		GT_ModHandler.addCraftingRecipe(
				plate_Double,
				gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"I", "B", "h",
						Character.valueOf('I'),
						plate_Single,
						Character.valueOf('B'),
						plate_Single});
		GT_ModHandler.addShapelessCraftingRecipe(
				plate_Double,
				new Object[]{gregtech.api.enums.ToolDictNames.craftingToolForgeHammer,
						plate_Single,
						plate_Single});	     
	}

	public static boolean addBenderRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
		if ((aInput1 == null) || (aOutput1 == null)) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
			return false;
		}
		new GT_Recipe(aEUt, aDuration, aInput1, aOutput1);
		return true;
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

	public static boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
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
