package detrav.items.processing;

import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerEV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerHV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerIV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerLV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerLuV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerMV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerUHV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerUV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerZPM;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;

import detrav.DetravScannerMod;
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProspector implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDetravToolProspector() {
        OrePrefixes.toolHeadDrill.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Material material, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aPrefix.doGenerateItem(material)) return;
        if (DetravScannerMod.DEBUG_ENABLED) return;

        Material steel = Materials.Steel;
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerLV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellSaltWater")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.LV.getIngredient(), 'X',
                ItemList.Sensor_LV });
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerMV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellSulfuricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellSaltWater")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.MV.getIngredient(), 'X',
                ItemList.Sensor_MV });

        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerHV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellSodiumPersulfate")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.HV.getIngredient(), 'X',
                ItemList.Sensor_HV });
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerEV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellSodiumPersulfate")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.EV.getIngredient(), 'X',
                ItemList.Sensor_EV });
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerIV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellNitricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellSodiumPersulfate")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.IV.getIngredient(), 'X',
                ItemList.Sensor_IV });

        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerLuV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellLithiumPeroxide")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.LuV.getIngredient(), 'X',
                ItemList.Sensor_LuV });
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerZPM.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellLithiumPeroxide")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.ZPM.getIngredient(), 'X',
                ItemList.Sensor_ZPM });
        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerUV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellLithiumPeroxide")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.UV.getIngredient(), 'X',
                ItemList.Sensor_UV });

        GTModHandler.addCraftingRecipe(
            DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(ProspectorScannerUHV.ID, 1, material, steel, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres("cellHydrofluoricAcid")
                .get(0), 'S',
                OreDictionary.getOres("cellHydrogenPeroxide")
                    .get(0),
                'H', MaterialParts.craftIngredient(OrePrefixes.toolHeadDrill, material), 'P',
                MaterialParts.craftIngredient(OrePrefixes.plate, material), 'C', Circuits.UHV.getIngredient(), 'X',
                ItemList.Sensor_UHV });

    }
}
