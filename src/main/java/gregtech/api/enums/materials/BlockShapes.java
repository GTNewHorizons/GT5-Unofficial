package gregtech.api.enums.materials;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.common.blocks.GTCasingShapeBlock;
import gregtech.common.blocks.GTStorageShapeBlock;
import gregtech.common.blocks.SheetmetalShapeBlock;

/// Hand-maintained block [Shape] declarations for GT's compressed storage blocks and the decorative sheetmetal
/// block. `block`'s membership is a curated list rather than a capability-bit query, because the `block`
/// `OrePrefixes` entry generates through a per-instance `Materials[]` array on each
/// `gregtech.common.blocks.BlockMetal` rather than the generic pipeline every other prefix uses (its
/// `generationBits` is `0`). `sheetmetal` generates through that generic pipeline, same
/// as the pipe/frame shapes in [PipeShapes].
public class BlockShapes {

    public static Shape block;
    public static Shape blockCasing;
    public static Shape blockCasingAdvanced;
    public static Shape sheetmetal;

    public static void init() {
        block = MaterialLibAPI.registerBlockShape(new GTStorageShapeBlock("gregtech", "block", "Block of %s", "block"));
        // Bartworks' werkstoff casings (see GTCasingShapeBlock); display formats match the legacy
        // blockCasing/blockCasingAdvanced OrePrefixes entries. This shape/variant identity is fixed permanently
        // once shipped, same as block's.
        blockCasing = MaterialLibAPI
            .registerBlockShape(new GTCasingShapeBlock("gregtech", "blockCasing", "Bolted %s Casing", "blockCasing"));
        blockCasingAdvanced = MaterialLibAPI.registerBlockShape(
            new GTCasingShapeBlock("gregtech", "blockCasingAdvanced", "Rebolted %s Casing", "blockCasingAdvanced"));
        sheetmetal = MaterialLibAPI
            .registerBlockShape(new SheetmetalShapeBlock("sheetmetal", "%s Sheetmetal", "sheetmetal"));
    }

    private BlockShapes() {}
}
