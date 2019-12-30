package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bitsd;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_ConnectableCrate;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;

public class GregtechSuperChests {

	public static void run() {
		int mId = 946;
		
		String aSuffix = "";
		if (CORE.GTNH) {
			aSuffix = " [Disabled]";
		}	
		
		GregtechItemList.Super_Chest_LV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.01", "Super Chest I"+aSuffix, 1)).getStackForm(1L));
		GregtechItemList.Super_Chest_MV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.02", "Super Chest II"+aSuffix, 2)).getStackForm(1L));
		GregtechItemList.Super_Chest_HV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.03", "Super Chest III"+aSuffix, 3)).getStackForm(1L));
		GregtechItemList.Super_Chest_EV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.04", "Super Chest IV"+aSuffix, 4)).getStackForm(1L));
		GregtechItemList.Super_Chest_IV.set((new GT_MetaTileEntity_TieredChest(mId++, "super.chest.tier.05", "Super Chest V"+aSuffix, 5)).getStackForm(1L));
		
		// Do not add Recipes for GTNH, hide them from NEI instead.
		if (CORE.GTNH) {
			ItemUtils.hideItemFromNEI(GregtechItemList.Super_Chest_LV.get(1L));
			ItemUtils.hideItemFromNEI(GregtechItemList.Super_Chest_MV.get(1L));
			ItemUtils.hideItemFromNEI(GregtechItemList.Super_Chest_HV.get(1L));
			ItemUtils.hideItemFromNEI(GregtechItemList.Super_Chest_EV.get(1L));
			ItemUtils.hideItemFromNEI(GregtechItemList.Super_Chest_IV.get(1L));
		}
		else {
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
		}		
		
		//Test Thing
		GregtechItemList.CrateStorage.set((new GT_MetaTileEntity_ConnectableCrate(GT_MetaTileEntity_ConnectableCrate.mCrateID, "crate.tier.01", "Interconnecting Storage Crate", 0)).getStackForm(1L));
		
	}

}
