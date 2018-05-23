package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bitsd;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_ConnectableCrate;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;

public class GregtechSuperTanks {

	public static void run() {
		int mId = 946;
		GregtechItemList.Super_Chest_LV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.01", "Super Chest I", 1)).getStackForm(1L));
		GregtechItemList.Super_Chest_MV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.02", "Super Chest II", 2)).getStackForm(1L));
		GregtechItemList.Super_Chest_HV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.03", "Super Chest III", 3)).getStackForm(1L));
		GregtechItemList.Super_Chest_EV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.04", "Super Chest IV", 4)).getStackForm(1L));
		GregtechItemList.Super_Chest_IV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.05", "Super Chest V", 5)).getStackForm(1L));
		
				
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Super_Chest_LV.get(1L, new Object[0]), bitsd,
				new Object[]{"DPD", "PMP", "DGD", Character.valueOf('M'), ItemList.Hull_LV, Character.valueOf('G'),
						ItemList.Automation_ChestBuffer_LV, Character.valueOf('D'),
						OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('P'),
						OrePrefixes.plate.get(Materials.Invar)});
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Super_Chest_MV.get(1L, new Object[0]), bitsd,
				new Object[]{"DPD", "PMP", "DGD", Character.valueOf('M'), ItemList.Hull_MV, Character.valueOf('G'),
						ItemList.Automation_ChestBuffer_MV, Character.valueOf('D'), OrePrefixes.circuit.get(Materials.Good),
						Character.valueOf('P'), OrePrefixes.plate.get(Materials.Aluminium)});
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Super_Chest_HV.get(1L, new Object[0]), bitsd,
				new Object[]{"DPD", "PMP", "DGD", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('G'),
						ItemList.Automation_ChestBuffer_HV, Character.valueOf('D'), OrePrefixes.circuit.get(Materials.Advanced),
						Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel)});
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Super_Chest_EV.get(1L, new Object[0]), bitsd,
				new Object[]{"DPD", "PMP", "DGD", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('G'),
						ItemList.Automation_ChestBuffer_EV, Character.valueOf('D'), OrePrefixes.circuit.get(Materials.Data),
						Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium)});
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Super_Chest_IV.get(1L, new Object[0]), bitsd,
				new Object[]{"DPD", "PMP", "DGD", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('G'),
						ItemList.Automation_ChestBuffer_IV, Character.valueOf('D'),
						OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('P'),
						OrePrefixes.plate.get(Materials.Tungsten)});
		
		//Test Thing
		GregtechItemList.CrateStorage.set((new GT_MetaTileEntity_ConnectableCrate(GT_MetaTileEntity_ConnectableCrate.mCrateID, "crate.tier.01", "Interconnecting Storage Crate", 0)).getStackForm(1L));
		
	}

}
