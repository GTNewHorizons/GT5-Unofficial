package gregtech.loaders.postload.postea;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.loaders.postload.postea.Transformers.remapBlock;
import static gregtech.loaders.postload.postea.Transformers.remapItem;
import static gregtech.loaders.postload.postea.Transformers.replaceBlock;
import static gregtech.loaders.postload.postea.Transformers.replaceItem;

import java.util.Map;
import java.util.function.IntFunction;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.MaterialParts;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;
import gregtech.loaders.postload.LegacyMetaItemTable;
import gregtech.loaders.postload.LegacyMetalItemsTable;
import gregtech.loaders.postload.LegacyPipeCutoverTable;
import gregtech.loaders.postload.LegacyStorageBlockTable;

final class GregTechTransformers {

    private GregTechTransformers() {}

    static void register() {
        registerItems();
        registerFrameboxTransformers();
        registerMetaItemCutoverTransformers();
        registerStorageBlockCutoverTransformers();
        registerFrameAndSheetmetalCutoverTransformers();
        registerPipeCutoverTransformers();
    }

    // spotless:off
    private static void registerItems() {
        // Extruder shape unification (#8771)
        replaceItem("gregtech:gt.metaitem.01", 32350, ItemList.Shape_Extruder_Plate.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32351, ItemList.Shape_Extruder_Rod.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32352, ItemList.Shape_Extruder_Bolt.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32353, ItemList.Shape_Extruder_Ring.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32354, ItemList.Shape_Extruder_Cell.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32355, ItemList.Shape_Extruder_Ingot.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32357, ItemList.Shape_Extruder_Casing.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32358, ItemList.Shape_Extruder_Pipe_Tiny.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32359, ItemList.Shape_Extruder_Pipe_Small.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32360, ItemList.Shape_Extruder_Pipe_Medium.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32361, ItemList.Shape_Extruder_Pipe_Large.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32362, ItemList.Shape_Extruder_Pipe_Huge.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32363, ItemList.Shape_Extruder_Block.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32364, ItemList.Shape_Extruder_Sword.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32365, ItemList.Shape_Extruder_Pickaxe.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32366, ItemList.Shape_Extruder_Shovel.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32367, ItemList.Shape_Extruder_Axe.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32368, ItemList.Shape_Extruder_Hoe.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32369, ItemList.Shape_Extruder_Hammer.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32370, ItemList.Shape_Extruder_File.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32371, ItemList.Shape_Extruder_Saw.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32372, ItemList.Shape_Extruder_Gear.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32373, ItemList.Shape_Extruder_Bottle.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32374, ItemList.Shape_Extruder_Rotor.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32375, ItemList.Shape_Extruder_Small_Gear.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32376, ItemList.Shape_Extruder_Turbine_Blade.get(1));
        replaceItem("gregtech:gt.metaitem.01", 32377, ItemList.Shape_Extruder_Drill.get(1));
    }
    // spotless:on

    private static void registerFrameboxTransformers() {
        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world, chunk) -> {
            // Legacy per-material framebox IDs are 4096 + the legacy material id
            int indexInMaterialList = tag.getInteger("mID") - 4096;
            Material material = LegacyMaterialIDIndex.get(indexInMaterialList);
            if (material == null || !material.hasShape(TEBlockShapes.frameGt)) {
                return null;
            }
            FrameShapeBlock frameBlock = (FrameShapeBlock) MaterialLibAPI.getBlock(TEBlockShapes.frameGt);

            // If this frame has a cover on it, we need to keep its TileEntity, rewritten to the single
            // material-agnostic frame MTE the shape block binds
            if (tag.hasKey(GTValues.NBT.COVERS)) {
                return new BlockInfo(frameBlock, material.getIndex(), coveredFrameTag -> {
                    coveredFrameTag.setInteger("mID", frameBlock.getMteId());
                    return coveredFrameTag;
                });
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(frameBlock, material.getIndex());
        });

        remapItem("gregtech:gt.blockmachines", id -> {
            int indexInMaterialList = id - 4096;
            // Not a frame box if the material for this id does not have a frame box associated with it.
            // Apparently the DEFC ID overlaps with the material ID for a Bastnasite frame box for example
            if (!LegacyMetalItemsTable.contains(indexInMaterialList)) {
                return null;
            }
            Material material = LegacyMaterialIDIndex.get(indexInMaterialList);
            if (material == null || !material.hasShape(TEBlockShapes.frameGt)) {
                return null;
            }
            Item frameItem = Item.getItemFromBlock(MaterialLibAPI.getBlock(TEBlockShapes.frameGt));
            return new ItemStack(frameItem, 1, material.getIndex());
        });
    }

    /// The material parts of `gt.metaitem.01/02/03` (below damage 32000) and the `gt.metaitem.99` molten and cracked
    /// cells decode through [LegacyMetaItemTable]. Hand-listed custom parts (damage 32000 and up on
    /// `metaitem.01/02/03`) pass through unchanged.
    private static void registerMetaItemCutoverTransformers() {
        registerMetaItemCutoverTransformer("gt.metaitem.01");
        registerMetaItemCutoverTransformer("gt.metaitem.02");
        registerMetaItemCutoverTransformer("gt.metaitem.03");
        remapItem("gregtech:gt.metaitem.99", damage -> {
            OrePrefixes prefix = LegacyMetaItemTable.cellPrefix(damage);
            if (prefix == null) return null;
            return MaterialParts.stack(prefix, LegacyMaterialIDIndex.get(damage % 1000), 1);
        });
    }

    private static void registerMetaItemCutoverTransformer(String itemName) {
        remapItem("gregtech:" + itemName, damage -> {
            OrePrefixes prefix = LegacyMetaItemTable.metaItemPrefix(itemName, damage);
            if (prefix == null) return null;
            return MaterialParts.stack(prefix, LegacyMaterialIDIndex.get(damage % 1000), 1);
        });
    }

    private static void registerStorageBlockCutoverTransformers() {
        LegacyStorageBlockTable.BATCHES.forEach((legacyId, batch) -> {
            for (int meta = 0; meta < batch.length; meta++) {
                Material material = batch[meta];
                if (material == null || !material.hasShape(BlockShapes.block)) continue;
                replaceBlock(legacyId, meta, MaterialLibAPI.getStack(material, BlockShapes.block, 1));
            }
        });
    }

    /// The legacy frameGt/sheetmetal blocks have the material id as meta and no TileEntity. The bw-named ids carry
    /// the gregtech domain because gregtech registered them, and their meta is a bartworks werkstoff id. An id the
    /// resolver doesn't know, or a material without the part, is left on its legacy slot.
    private static void registerFrameAndSheetmetalCutoverTransformers() {
        registerPartCutoverTransformer("gregtech:bw.frames", OrePrefixes.frameGt, LegacyWerkstoffIndex::get);
        registerPartCutoverTransformer("gregtech:bw.sheetmetal", OrePrefixes.sheetmetal, LegacyWerkstoffIndex::get);
        registerPartCutoverTransformer("gregtech:gt.sheetmetal", OrePrefixes.sheetmetal, LegacyMaterialIDIndex::get);
        registerPartCutoverTransformer("gregtech:gt.blockframes", OrePrefixes.frameGt, LegacyMaterialIDIndex::get);
    }

    private static void registerPartCutoverTransformer(String legacyId, OrePrefixes prefix,
        IntFunction<Material> materialResolver) {
        IntFunction<ItemStack> cutover = meta -> {
            Material material = materialResolver.apply(meta);
            return material == null ? null : MaterialParts.stack(prefix, material, 1);
        };
        remapBlock(legacyId, cutover);
        remapItem(legacyId, cutover);
    }

    /// Migrates saved per-material wire, cable, fluid pipe and item pipe MTEs (see [LegacyPipeCutoverTable]) onto
    /// the shape's [PipeShapeBlock], with the MaterialLib material index as meta. Placed pipes keep their tile entity
    /// with `mID` rewritten to the shape's MTE id, and the rest of the tag (connections, covers, paint, stored
    /// fluids/items) carries over. Ids outside the table pass through to the other `BaseMetaPipeEntity` and
    /// `gt.blockmachines` handlers unchanged.
    private static void registerPipeCutoverTransformers() {
        Map<Integer, LegacyPipeCutoverTable.Entry> table = LegacyPipeCutoverTable.entries();

        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world, chunk) -> {
            LegacyPipeCutoverTable.Entry entry = table.get(tag.getInteger("mID"));
            if (entry == null) return null;
            PipeShapeBlock shapeBlock = (PipeShapeBlock) MaterialLibAPI.getBlock(entry.shape());
            return new BlockInfo(
                shapeBlock,
                entry.material()
                    .getIndex(),
                pipeTag -> {
                    pipeTag.setInteger("mID", shapeBlock.getMteId());
                    return pipeTag;
                });
        });

        remapItem("gregtech:gt.blockmachines", damage -> {
            LegacyPipeCutoverTable.Entry entry = table.get(damage);
            if (entry == null) return null;
            Item shapeItem = Item.getItemFromBlock(MaterialLibAPI.getBlock(entry.shape()));
            return new ItemStack(
                shapeItem,
                1,
                entry.material()
                    .getIndex());
        });

        GT_FML_LOGGER
            .debug("PosteaTransformers: registered pipe-family transformers for {} legacy MTE ids", table.size());
    }
}
