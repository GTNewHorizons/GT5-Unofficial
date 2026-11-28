package gregtech.loaders.postload.postea;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.loaders.postload.postea.Transformers.remapBlock;
import static gregtech.loaders.postload.postea.Transformers.remapItem;
import static gregtech.loaders.postload.postea.Transformers.replaceBlock;
import static gregtech.loaders.postload.postea.Transformers.replaceItem;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.postload.GtppFrameCutoverTable;
import gregtech.loaders.postload.GtppItemCutoverTable;
import gregtech.loaders.postload.GtppOreCutoverTable;

final class GTPlusPlusTransformers {

    private GTPlusPlusTransformers() {}

    static void register() {
        registerItems();
        registerGtppItemCutoverTransformers();
        registerGtppOreCutoverTransformers();
        registerGtppFrameCutoverTransformers();
    }

    // spotless:off
    private static void registerItems() {
        // Extruder shape unification (#8771)
        replaceItem("miscutils:MU-metaitem.01", 32040, ItemList.Shape_Extruder_Shaft.get(1));
        replaceItem("miscutils:item.BasicAgrichemItem", 22, ItemList.Shape_Extruder_Pellet.get(1));

        // gtPlusPlus cells that GtppItemCutoverTable has no row for
        replaceItem("miscutils:itemCellIodine", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "Iodine"), 1));
        replaceItem("miscutils:itemCellThoriumTetrafluoride", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "ThoriumTetrafluoride"), 1));
        replaceItem("miscutils:itemCellZirconiumTetrafluoride", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "ZirconiumTetrafluoride"), 1));
        replaceItem("miscutils:itemCellXenon", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "Xenon"), 1));
        replaceItem("miscutils:itemCellNeon", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "Neon"), 1));
        replaceItem("miscutils:itemCellKrypton", MaterialParts.cell(MaterialLibAPI.getMaterial("gregtech", "Krypton"), 1));

        // Thermal Foundation dusts gtPlusPlus put on its material item. Meta 1, the Blizz rod, keeps its item
        replaceItem("miscutils:material", 2, MaterialLibAPI.getStack(Materials.Blizz, Shapes.dust, 1));
        replaceItem("miscutils:material", 3, MaterialLibAPI.getStack(Materials.Pyrotheum, Shapes.dust, 1));
        replaceItem("miscutils:material", 4, MaterialLibAPI.getStack(Materials.Cryotheum, Shapes.dust, 1));
    }
    // spotless:on

    /// Migrates saved gtPlusPlus per-material part stacks (`miscutils:item*`/`miscutils:block*`, one distinct
    /// registered item/block per (material, prefix), always damage 0) into the equivalent MaterialLib stack,
    /// resolved through [GtppItemCutoverTable]'s pinned (prefix, material, registry name) rows. `block` rows also
    /// migrate placed blocks. A row whose material carries no shape for its prefix is left on its legacy slot.
    private static void registerGtppItemCutoverTransformers() {
        for (GtppItemCutoverTable.Entry entry : GtppItemCutoverTable.ENTRIES) {
            remapItem(entry.registryName(), damage -> resolveGtppCutoverStack(entry));
            if (entry.prefix() == OrePrefixes.block) {
                remapBlock(entry.registryName(), meta -> resolveGtppCutoverStack(entry));
            }
        }
        GT_FML_LOGGER.debug(
            "PosteaTransformers: registered gtpp item transformers for {} legacy items",
            GtppItemCutoverTable.ENTRIES.length);
    }

    static ItemStack resolveGtppCutoverStack(GtppItemCutoverTable.Entry entry) {
        Material material = MaterialLibAPI.getMaterial("gregtech", entry.materialName());
        if (entry.prefix() == OrePrefixes.cell) {
            return MaterialParts.cell(material, 1);
        }
        return MaterialParts.stack(entry.prefix(), material, 1);
    }

    /// Migrates saved placed and inventory gtPlusPlus ore blocks (`BlockBaseOre`, one registered block per material)
    /// into the material's MaterialLib [OreShapes#ore] stack. A material without the `ore` shape is left on its
    /// legacy slot.
    private static void registerGtppOreCutoverTransformers() {
        for (GtppOreCutoverTable.Entry entry : GtppOreCutoverTable.ENTRIES) {
            remapItem(entry.registryName(), damage -> resolveGtppOreCutoverStack(entry));
            remapBlock(entry.registryName(), meta -> resolveGtppOreCutoverStack(entry));
        }
        GT_FML_LOGGER.debug(
            "PosteaTransformers: registered gtpp ore transformers for {} legacy blocks",
            GtppOreCutoverTable.ENTRIES.length);
    }

    static ItemStack resolveGtppOreCutoverStack(GtppOreCutoverTable.Entry entry) {
        Material ml = MaterialLibAPI.getMaterial("gregtech", entry.unlocalizedName());
        if (ml == null || !ml.hasShape(OreShapes.ore)) return null;
        return MaterialLibAPI.getStack(ml, OreShapes.ore, 1);
    }

    /// gtPlusPlus registered one frame block per material (`miscutils:blockFrameGt<Name>`, meta 0), so each material
    /// gets its own registration. A material whose frame shape did not generate is left on its legacy slot.
    private static void registerGtppFrameCutoverTransformers() {
        int count = 0;
        for (Material material : GtppFrameCutoverTable.materials()) {
            if (!material.hasShape(TEBlockShapes.frameGt)) continue;
            ItemStack cutover = MaterialLibAPI.getStack(material, TEBlockShapes.frameGt, 1);
            replaceBlock("miscutils:blockFrameGt" + material.getName(), 0, cutover);
            count++;
        }
        GT_FML_LOGGER.debug("PosteaTransformers: registered gtpp frame transformers for {} legacy blocks", count);
    }
}
