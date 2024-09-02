package com.detrav.items.processing;

import static com.detrav.DetravScannerMod.DEBUG_ENABLED;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.detrav.items.DetravMetaGeneratedTool01;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProspector implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDetravToolProspector() {
        OrePrefixes.toolHeadPickaxe.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials material, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aPrefix.doGenerateItem(material)) return;
        if (DEBUG_ENABLED) return;
        try {
            // ULV disabled
            // GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, aMaterial,
            // Materials.Lead, null), GT_ModHandler.RecipeBits.DISMANTLEABLE |
            // GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new
            // Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellSulfuricAcid").get(0),'S',OreDictionary.getOres("cellHydroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Primitive),'X',gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Sensor_ULV});
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(2, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Basic), 'X', ItemList.Sensor_LV });
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(4, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Good), 'X', ItemList.Sensor_MV });

            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(6, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Advanced), 'X', ItemList.Sensor_HV });
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(8, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Data), 'X', ItemList.Sensor_EV });
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(10, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Elite), 'X', ItemList.Sensor_IV });

            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(12, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Master), 'X', ItemList.Sensor_LuV });
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(14, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Ultimate), 'X', ItemList.Sensor_ZPM });
            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(16, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'X', ItemList.Sensor_UV });

            GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(18, 1, material, Materials.Steel, null),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydrogenPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Infinite), 'X', ItemList.Sensor_UHV });

        } catch (Exception ignored) {}

    }
}
