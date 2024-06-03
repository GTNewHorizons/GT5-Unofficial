package com.detrav.xmod;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

public class Dreamcraft {

    public static void registerOre(Materials material) {
        GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(
                        100,
                        1,
                        material,
                        Materials.Iridium,
                        new long[] { 102400000L, GT_Values.V[6], 6L, -1L }),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "EHR", "CSC", "PBP", 'S', ItemList.Cover_Screen, 'R', ItemList.Sensor_LuV, 'H',
                        OrePrefixes.toolHeadDrill.get(material), 'E', ItemList.Emitter_LuV, 'C',
                        OrePrefixes.circuit.get(Materials.Master), 'P', OrePrefixes.plate.get(Materials.Iridium), 'B',
                        CustomItemList.BatteryHull_LuV_Full.get(1L) });
        GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(
                        102,
                        1,
                        material,
                        Materials.Osmium,
                        new long[] { 409600000L, GT_Values.V[7], 7L, -1L }),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "EHR", "CSC", "PBP", 'S', ItemList.Cover_Screen, 'R', ItemList.Sensor_ZPM, 'H',
                        OrePrefixes.toolHeadDrill.get(material), 'E', ItemList.Emitter_ZPM, 'C',
                        OrePrefixes.circuit.get(Materials.Ultimate), 'P', OrePrefixes.plate.get(Materials.Osmium), 'B',
                        CustomItemList.BatteryHull_ZPM_Full.get(1L) });
        GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(
                        104,
                        1,
                        material,
                        Materials.Neutronium,
                        new long[] { 1638400000L, GT_Values.V[8], 8L, -1L }),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "EHR", "CSC", "PBP", 'S', ItemList.Cover_Screen, 'R', ItemList.Sensor_UV, 'H',
                        OrePrefixes.toolHeadDrill.get(material), 'E', ItemList.Emitter_UV, 'C',
                        OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'P',
                        OrePrefixes.plate.get(Materials.Neutronium), 'B', CustomItemList.BatteryHull_UV_Full.get(1L) });
        GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(
                        106,
                        1,
                        material,
                        Materials.Infinity,
                        new long[] { 6553600000L, GT_Values.V[9], 9L, -1L }),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "EHR", "CSC", "PBP", 'S', ItemList.Cover_Screen, 'R', ItemList.Sensor_UHV, 'H',
                        OrePrefixes.toolHeadDrill.get(material), 'E', ItemList.Emitter_UHV, 'C',
                        OrePrefixes.circuit.get(Materials.Infinite), 'P', OrePrefixes.plate.get(Materials.Infinity),
                        'B', CustomItemList.BatteryHull_UHV_Full.get(1L) });
    }
}
