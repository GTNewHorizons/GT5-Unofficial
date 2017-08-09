package gtPlusPlus.core.recipe;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_Old_Circuits  implements IOreRecipeRegistrator {
	public RECIPES_Old_Circuits() {
		OrePrefixes.crafting.add(this);
	}

	@Override
	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName, final String aModName, final ItemStack aStack) {
		if (aOreDictName.equals(OreDictNames.craftingLensRed.toString())) {
			Utils.LOG_INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 2)");
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.AnnealedCopper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(1L, new Object[0]), 64, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensGreen.toString())) {
			Utils.LOG_INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 3)");
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Olivine, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Emerald, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensBlue.toString()) || aOreDictName.equals(OreDictNames.craftingLensCyan.toString()) || aOreDictName.equals(OreDictNames.craftingLensLightBlue.toString())) {
			Utils.LOG_INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 4)");
			GT_Values.RA.addLaserEngraverRecipe(ItemList.IC2_LapotronCrystal.getWildcard(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), 256, 480);
		}		
	}


	private static boolean addCircuitRecipes(){
		Utils.LOG_INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 1)");
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
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Master.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Master.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Data.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 2L), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Tool_DataStick.get(1L, new Object[0]), 128, 64);
			}
		}

		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Elite.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(18L, new Object[0]), GT_Values.NF, GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0]), 512, 256);
		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Master.get(2L, new Object[0]), ItemList.Circuit_Parts_Crystal_Chip_Master.get(18L, new Object[0]), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L, new Object[0]), 512, 1024);
		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Master.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(18L, new Object[0]), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L, new Object[0]), 512, 1024);
		GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), 32, 16);
		
		Materials plasticType = Materials.get("Polytetrafluoroethylene") != null ? Materials.get("Polytetrafluoroethylene") : Materials.Plastic;
		GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, plasticType, 1L), GregtechItemList.Old_Empty_Board_Elite.get(1L, new Object[0]), 32, 256);



		return true;
	}

	public static boolean handleCircuits(){

		hideCircuitsNEI();
		addCircuitRecipes();
		removeNewCircuits();
		return true;
	}

	private static boolean removeNewCircuits(){
		Utils.LOG_INFO("[Old Feature - Circuits] Overriding .28+ circuit values in the GT5u Itemlist with values from GT++.");

		ItemList.Circuit_Primitive.set(GregtechItemList.Old_Circuit_Primitive.get(1));
		ItemList.Circuit_Basic.set(GregtechItemList.Old_Circuit_Basic.get(1));
		if (ItemList.valueOf("Circuit_Integrated_Good") != null){
			ItemList.valueOf("Circuit_Integrated_Good").set(GregtechItemList.Old_Circuit_Good.get(1));
		}
		ItemList.Circuit_Good.set(GregtechItemList.Old_Circuit_Good.get(1));
		ItemList.Circuit_Advanced.set(GregtechItemList.Old_Circuit_Advanced.get(1));
		//ItemList.Circuit_Data.set(GregtechItemList.Old_Circuit_Data.get(1));
		ItemList.Circuit_Elite.set(GregtechItemList.Old_Circuit_Elite.get(1));
		ItemList.Circuit_Master.set(GregtechItemList.Old_Circuit_Master.get(1));
		ItemList.Circuit_Ultimate.set(GregtechItemList.Old_Circuit_Ultimate.get(1));


		ItemList.Circuit_Board_Basic.set(GregtechItemList.Old_Circuit_Board_Basic.get(1));
		ItemList.Circuit_Board_Advanced.set(GregtechItemList.Old_Circuit_Board_Advanced.get(1));
		ItemList.Circuit_Board_Elite.set(GregtechItemList.Old_Circuit_Board_Elite.get(1));
		ItemList.Circuit_Parts_Advanced.set(GregtechItemList.Old_Circuit_Parts_Advanced.get(1));
		ItemList.Circuit_Parts_Wiring_Basic.set(GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1));
		ItemList.Circuit_Parts_Wiring_Advanced.set(GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1));
		ItemList.Circuit_Parts_Wiring_Elite.set(GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(1));
		ItemList.Circuit_Parts_Crystal_Chip_Elite.set(GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1));
		ItemList.Circuit_Parts_Crystal_Chip_Master.set(GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(1));

		return true;
	}

	private static boolean hideCircuitsNEI(){
		Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
		if (isNEILoaded){
			Utils.LOG_INFO("[Old Feature - Circuits] Hiding .28+ circuits in NEI.");
			String[] CircuitToHide = {
					"Circuit_Board_Basic",
					"Circuit_Board_Advanced",
					"Circuit_Board_Elite",
					"Circuit_Parts_Advanced",
					"Circuit_Parts_Wiring_Basic",
					"Circuit_Parts_Wiring_Advanced",
					"Circuit_Parts_Wiring_Elite",
					"Circuit_Parts_Crystal_Chip_Elite",
					"Circuit_Parts_Crystal_Chip_Master",
					"Circuit_Primitive",
					"Circuit_Basic",
					"Circuit_Integrated_Good",
					"Circuit_Good",
					"Circuit_Advanced",
					"Circuit_Data",
					"Circuit_Elite",
					"Circuit_Master",
					"Circuit_Ultimate",
					"Circuit_Board_Coated", 
					"Circuit_Board_Phenolic", 
					"Circuit_Board_Epoxy", 
					"Circuit_Board_Fiberglass", 
					"Circuit_Board_Multifiberglass", 
					"Circuit_Board_Wetware", 
					"Circuit_Parts_Resistor", 
					"Circuit_Parts_ResistorSMD", 
					"Circuit_Parts_Glass_Tube", 
					"Circuit_Parts_Vacuum_Tube", 
					"Circuit_Parts_Coil", 
					"Circuit_Parts_Diode", 
					"Circuit_Parts_DiodeSMD", 
					"Circuit_Parts_Transistor", 
					"Circuit_Parts_TransistorSMD", 
					"Circuit_Parts_Capacitor", 
					"Circuit_Parts_CapacitorSMD", 
					"Circuit_Silicon_Ingot", 
					"Circuit_Silicon_Ingot2", 
					"Circuit_Silicon_Ingot3", 
					"Circuit_Silicon_Wafer", 
					"Circuit_Silicon_Wafer2", 
					"Circuit_Silicon_Wafer3",
					"Circuit_Wafer_ILC", 
					"Circuit_Chip_ILC", 
					"Circuit_Wafer_Ram", 
					"Circuit_Chip_Ram", 
					"Circuit_Wafer_NAND", 
					"Circuit_Chip_NAND", 
					"Circuit_Wafer_NOR", 
					"Circuit_Chip_NOR", 
					"Circuit_Wafer_CPU", 
					"Circuit_Chip_CPU", 
					"Circuit_Wafer_SoC", 
					"Circuit_Chip_SoC", 
					"Circuit_Wafer_SoC2", 
					"Circuit_Chip_SoC2", 
					"Circuit_Wafer_PIC", 
					"Circuit_Chip_PIC", 
					"Circuit_Wafer_HPIC", 
					"Circuit_Chip_HPIC", 
					"Circuit_Wafer_NanoCPU", 
					"Circuit_Chip_NanoCPU", 
					"Circuit_Wafer_QuantumCPU", 
					"Circuit_Chip_QuantumCPU", 
					"Circuit_Chip_CrystalCPU", 
					"Circuit_Chip_CrystalSoC", 
					"Circuit_Chip_NeuroCPU", 
					"Circuit_Chip_Stemcell",
					"Circuit_Processor", 
					"Circuit_Computer", 
					"Circuit_Nanoprocessor", 
					"Circuit_Nanocomputer", 
					"Circuit_Elitenanocomputer", 
					"Circuit_Quantumprocessor", 
					"Circuit_Quantumcomputer", 
					"Circuit_Masterquantumcomputer", 
					"Circuit_Quantummainframe", 
					"Circuit_Crystalprocessor", 
					"Circuit_Crystalcomputer", 
					"Circuit_Ultimatecrystalcomputer", 
					"Circuit_Crystalmainframe", 
					"Circuit_Neuroprocessor", 
					"Circuit_Wetwarecomputer", 
					"Circuit_Wetwaresupercomputer", 
					"Circuit_Wetwaremainframe", 
					"Circuit_Parts_RawCrystalChip"					
			};

			for (String component : CircuitToHide){
				API.hideItem(ItemList.valueOf(component).get(1L, new Object[0]));
			}			
		}
		return true;
	}

}
