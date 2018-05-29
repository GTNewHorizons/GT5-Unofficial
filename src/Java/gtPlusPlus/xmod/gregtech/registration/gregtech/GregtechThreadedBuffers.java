package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bitsd;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ThreadedSuperBuffer;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative.GT_MetaTileEntity_InfiniteItemHolder;

public class GregtechThreadedBuffers {

	public static void run() {
		run2();
	}
	
	private static void run2() {
		
		GregtechItemList.Infinite_Item_Chest.set((new GT_MetaTileEntity_InfiniteItemHolder(31010, "infinite.chest.tier.01", "Infinite Item Chest", 1)).getStackForm(1L));
				
		GregtechItemList.Automation_Threaded_SuperBuffer_ULV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31000,"automation.superbuffer.threaded.tier.00", 
				"ULV Super Buffer [Threaded]", 0).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_LV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31001, "automation.superbuffer.threaded.tier.01", 
				"LV Super Buffer [Threaded]", 1).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_MV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31002, "automation.superbuffer.threaded.tier.02",
				"MV Super Buffer [Threaded]", 2).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_HV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31003, "automation.superbuffer.threaded.tier.03",
				"HV Super Buffer [Threaded]", 3).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_EV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31004, "automation.superbuffer.threaded.tier.04",
				"EV Super Buffer [Threaded]", 4).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_IV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31005, "automation.superbuffer.threaded.tier.05",
				"IV Super Buffer [Threaded]", 5).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_LuV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31006, "automation.superbuffer.threaded.tier.06", 
				"LuV Super Buffer [Threaded]", 6).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_ZPM.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31007, "automation.superbuffer.threaded.tier.07",
				"ZPM Super Buffer [Threaded]", 7).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_UV.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31008, "automation.superbuffer.threaded.tier.08",
				"UV Super Buffer [Threaded]", 8).getStackForm(1L));
		GregtechItemList.Automation_Threaded_SuperBuffer_MAX.set(new GT_MetaTileEntity_ThreadedSuperBuffer(31009, "automation.superbuffer.threaded.tier.09",
				"MAX Super Buffer [Threaded]", 9).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_ULV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_ULV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_LV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_LV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_MV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_MV, 'V', ItemList.Conveyor_Module_MV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_HV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_HV, 'V', ItemList.Conveyor_Module_HV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_EV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_EV, 'V', ItemList.Conveyor_Module_EV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_IV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_IV, 'V', ItemList.Conveyor_Module_IV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_LuV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_ZPM.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_UV.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_UV, 'V', ItemList.Conveyor_Module_UV, 'D', ItemList.Tool_DataOrb});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Automation_Threaded_SuperBuffer_MAX.get(1L, new Object[0]), bitsd, new Object[]{
				"DMV", 'M', ItemList.Automation_SuperBuffer_MAX, 'V', GregtechItemList.Conveyor_Module_MAX, 'D', ItemList.Tool_DataOrb});
	}
	
}
