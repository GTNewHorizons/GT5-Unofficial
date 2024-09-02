package detrav.items.processing;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import detrav.DetravScannerMod;
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;

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
        if (DetravScannerMod.DEBUG_ENABLED) return;
        try {
            // ULV disabled
            // GTModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, aMaterial,
            // Materials.Lead, null), GTModHandler.RecipeBits.DISMANTLEABLE |
            // GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED, new
            // Object[]{"SHE","CPC","PXP",'E',OreDictionary.getOres("cellSulfuricAcid").get(0),'S',OreDictionary.getOres("cellHydroxide").get(0),'H',OrePrefixes.toolHeadDrill.get(aMaterial),'P',OrePrefixes.plate.get(aMaterial),'C',OrePrefixes.circuit.get(Materials.Primitive),'X',gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Sensor_ULV});
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(2, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Basic), 'X', ItemList.Sensor_LV });
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(4, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Good), 'X', ItemList.Sensor_MV });

            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(6, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Advanced), 'X', ItemList.Sensor_HV });
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(8, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Data), 'X', ItemList.Sensor_EV });
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(10, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellSodiumPersulfate")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Elite), 'X', ItemList.Sensor_IV });

            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(12, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Master), 'X', ItemList.Sensor_LuV });
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(14, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Ultimate), 'X', ItemList.Sensor_ZPM });
            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(16, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellLithiumPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'X', ItemList.Sensor_UV });

            GTModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(18, 1, material, Materials.Steel, null),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                    .get(0), 'S',
                    OreDictionary.getOres("cellHydrogenPeroxide")
                        .get(0),
                    'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                    OrePrefixes.circuit.get(Materials.Infinite), 'X', ItemList.Sensor_UHV });

        } catch (Exception ignored) {}

    }
}
