package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.*;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_Old_Circuits  implements IOreRecipeRegistrator {
	public RECIPES_Old_Circuits() {
		OrePrefixes.crafting.add(this);
	}

	@Override
	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName, final String aModName, final ItemStack aStack) {
		if (aOreDictName.equals(OreDictNames.craftingLensRed.toString())) {
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.AnnealedCopper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(1L, new Object[0]), 64, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensGreen.toString())) {
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Olivine, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Emerald, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensBlue.toString())) {
			GT_Values.RA.addLaserEngraverRecipe(ItemList.IC2_LapotronCrystal.getWildcard(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), 256, 480);

		}		
	}


	public static boolean addCircuitRecipes(){
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), new Object[]{GT_ModHandler.getIC2Item("casingadviron", 1L), OrePrefixes.wireGt01.get(Materials.RedAlloy), OrePrefixes.wireGt01.get(Materials.RedAlloy), OrePrefixes.wireGt01.get(Materials.Tin)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WWW", "CPC", "WWW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OreDictNames.craftingWireCopper, 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WCW", "WPW", "WCW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OreDictNames.craftingWireCopper, 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WWW", "CPC", "WWW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WCW", "WPW", "WCW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'P', OrePrefixes.plate.get(Materials.Steel)});

		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Basic.get(1L, new Object[0]), 32, 16);
		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), 32, 64);
		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), 32, 256);

		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), 32, 64);
	    GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), 32, 64);
		
		int tMultiplier;
		for (Materials tMat : Materials.values()) {
			if ((tMat.mStandardMoltenFluid != null) && (tMat.contains(SubTag.SOLDERING_MATERIAL)))
			{
				tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : 2;

				GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Item_Casing_Steel.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 2L), tMat.getMolten(144L * tMultiplier / 8L), GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), 16, 8);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), tMat.getMolten(144L * tMultiplier / 8L), GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), 16, 8);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Primitive.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), 32, 16);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Primitive.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Old_Circuit_Good.get(1L, new Object[0]), 32, 16);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Circuit_Advanced.get(1L, new Object[0]), 32, 64);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Circuit_Data.get(1L, new Object[0]), 32, 64);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Data.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Elite.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Master.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Data.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 2L), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Tool_DataStick.get(1L, new Object[0]), 128, 64);
			}
		}

		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Elite.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(18L, new Object[0]), GT_Values.NF, GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0]), 512, 256);
		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Master.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(18L, new Object[0]), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L, new Object[0]), 512, 1024);
		GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), 32, 16);
	    GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L), GregtechItemList.Old_Empty_Board_Elite.get(1L, new Object[0]), 32, 256);
	    
	    
	    
		return true;
	}

}
