package com.detrav.items.processing;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProPick implements gregtech.api.interfaces.IOreRecipeRegistrator {			
    public ProcessingDetravToolProPick() {
        OrePrefixes.toolHeadPickaxe.add(this);
        
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if(!aPrefix.doGenerateItem(aMaterial)) 
        	return;
        
        //ULV disabled
        //GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, aMaterial, Materials.Lead, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellSulfuricAcid").get(0),'S',OreDictionary.getOres("cellHydroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Primitive),'X',gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Sensor_ULV});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(2, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellSulfuricAcid").get(0),'S',OreDictionary.getOres("cellHydroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Basic),'X',ItemList.Sensor_LV});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(4, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellSulfuricAcid").get(0),'S',OreDictionary.getOres("cellHydroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Good),'X',ItemList.Sensor_MV});
        
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(6, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellNitricAcid").get(0),'S',OreDictionary.getOres("cellSodiumPersulfate").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Advanced),'X',ItemList.Sensor_HV});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(8, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellNitricAcid").get(0),'S',OreDictionary.getOres("cellSodiumPersulfate").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Data),'X',ItemList.Sensor_EV});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(10, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellNitricAcid").get(0),'S',OreDictionary.getOres("cellSodiumPersulfate").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Elite),'X',ItemList.Sensor_IV});
        
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(12, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellHydrofluoricAcid").get(0),'S',OreDictionary.getOres("cellLithiumPeroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Master),'X',ItemList.Sensor_LuV});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(14, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellHydrofluoricAcid").get(0),'S',OreDictionary.getOres("cellLithiumPeroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Ultimate),'X',ItemList.Sensor_ZPM});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(16, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellHydrofluoricAcid").get(0),'S',OreDictionary.getOres("cellLithiumPeroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Superconductor),'X',ItemList.Sensor_UV});
        
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(18, 1, aMaterial, Materials.Steel, null), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellHydrofluoricAcid").get(0),'S',OreDictionary.getOres("cellHydrogenPeroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Infinite),'X',ItemList.Sensor_UHV});
        
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(100, 1, aMaterial, Materials.Iridium, new long[]{102400000L, GT_Values.V[6], 6L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_LuV, Character.valueOf('H'), OrePrefixes.toolHeadDrill.get(aMaterial),Character.valueOf('E'), ItemList.Emitter_LuV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Master), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Iridium), Character.valueOf('B'), CustomItemList.BatteryHull_LuV_Full.get(1L)});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(102, 1, aMaterial, Materials.Osmium, new long[]{409600000L, GT_Values.V[7], 7L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_ZPM, Character.valueOf('H'), OrePrefixes.toolHeadDrill.get(aMaterial), Character.valueOf('E'), ItemList.Emitter_ZPM,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Ultimate), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Osmium), Character.valueOf('B'), CustomItemList.BatteryHull_ZPM_Full.get(1L)});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(104, 1, aMaterial, Materials.Neutronium, new long[]{1638400000L, GT_Values.V[8], 8L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_UV, Character.valueOf('H'), OrePrefixes.toolHeadDrill.get(aMaterial), Character.valueOf('E'), ItemList.Emitter_UV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Superconductor), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Neutronium), Character.valueOf('B'), CustomItemList.BatteryHull_UV_Full.get(1L)});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(106, 1, aMaterial, Materials.Infinity, new long[]{6553600000L, GT_Values.V[9], 9L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_UHV, Character.valueOf('H'), OrePrefixes.toolHeadDrill.get(aMaterial), Character.valueOf('E'), ItemList.Emitter_UHV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Infinite), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Infinity), Character.valueOf('B'), CustomItemList.BatteryHull_UHV_Full.get(1L)});

		
    }
}
