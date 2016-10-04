package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
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

		//Forge Hammer
		GT_Values.RA.addForgeHammerRecipe(
				ingotStackTwo,
				plate_Single,
				(int) Math.max(material.getMass(), 1L),
				16);
		//Bender
		GT_Values.RA.addBenderRecipe(
				ingotStackOne,
				plate_Single,
				(int) Math.max(material.getMass() * 1L, 1L),
				24);
		//Extruder
		GT_Values.RA.addExtruderRecipe(
				ingotStackOne,
				shape_Extruder,
				plate_Single,
				10, 4 * tVoltageMultiplier);
		//Alloy Smelter
		GT_Values.RA.addAlloySmelterRecipe(
				ingotStackTwo,
				shape_Mold,
				plate_Single,
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);


		//Making Double Plates
		GT_Values.RA.addBenderRecipe(
				ingotStackTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96);
		GT_Values.RA.addBenderRecipe(
				plate_SingleTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96);


		Utils.LOG_WARNING("Adding crafting recipes for "+material.getLocalizedName()+" Plates - Single & Double");

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



}
