package com.detrav.items.processing;

import com.detrav.enums.DetravItemList;
import com.detrav.enums.DetravSimpleItems;
import com.detrav.items.DetravMetaGeneratedItem01;
import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProPick implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingDetravToolProPick() {
        OrePrefixes.toolHeadPickaxe.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if(!aPrefix.doGenerateItem(aMaterial)) return;;
        GT_ModHandler.
                addShapelessCraftingRecipe(
                        DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, aMaterial, aMaterial.mHandleMaterial, null),
                        new Object[]{DetravSimpleItems.toolHeadProPick.get(aMaterial), OrePrefixes.stick.get(aMaterial.mHandleMaterial)});


        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(100, 1, aMaterial, Materials.StainlessSteel, new long[]{100000L, 32L, 1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_LV,Character.valueOf('E'), ItemList.Emitter_LV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('B'), ItemList.Battery_RE_LV_Lithium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(100, 1, aMaterial, Materials.StainlessSteel, new long[]{75000L, 32L, 1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_LV,Character.valueOf('E'), ItemList.Emitter_LV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('B'), ItemList.Battery_RE_LV_Cadmium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(100, 1, aMaterial, Materials.StainlessSteel, new long[]{50000L, 32L, 1L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_LV,Character.valueOf('E'), ItemList.Emitter_LV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('B'), ItemList.Battery_RE_LV_Sodium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(102, 1, aMaterial, Materials.Titanium, new long[]{400000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_MV,Character.valueOf('E'), ItemList.Emitter_MV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('B'), ItemList.Battery_RE_MV_Lithium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(102, 1, aMaterial, Materials.Titanium, new long[]{300000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_MV,Character.valueOf('E'), ItemList.Emitter_MV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('B'), ItemList.Battery_RE_MV_Cadmium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(102, 1, aMaterial, Materials.Titanium, new long[]{200000L, 128L, 2L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_MV,Character.valueOf('E'), ItemList.Emitter_MV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('B'), ItemList.Battery_RE_MV_Sodium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(104, 1, aMaterial, Materials.TungstenSteel, new long[]{1600000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_HV,Character.valueOf('E'), ItemList.Emitter_HV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.TungstenSteel),  Character.valueOf('B'), ItemList.Battery_RE_HV_Lithium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(104, 1, aMaterial, Materials.TungstenSteel, new long[]{1200000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_HV,Character.valueOf('E'), ItemList.Emitter_HV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.TungstenSteel),  Character.valueOf('B'), ItemList.Battery_RE_HV_Cadmium.get(1L, new Object[0])});
        GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(104, 1, aMaterial, Materials.TungstenSteel, new long[]{800000L, 512L, 3L, -1L}), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"EHR", "CSC", "PBP", Character.valueOf('S'), ItemList.Cover_Screen, Character.valueOf('R'), ItemList.Sensor_HV,Character.valueOf('E'), ItemList.Emitter_HV,Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('H'), DetravSimpleItems.toolHeadProPick.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(Materials.TungstenSteel), Character.valueOf('B'), ItemList.Battery_RE_HV_Sodium.get(1L, new Object[0])});

        GT_ModHandler.addCraftingRecipe(DetravItemList.Shape_Extruder_ProPick.get(1L,new Object[0]),
                new Object[]{"h","X","M", Character.valueOf('X'),DetravSimpleItems.toolHeadProPick.get(aMaterial),Character.valueOf('M'),ItemList.Shape_Empty});
    }
}