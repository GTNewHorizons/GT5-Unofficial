package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.common.blocks.GTCasingShapeBlock;
import gregtech.common.blocks.GTStorageShapeBlock;
import gregtech.common.blocks.SheetmetalShapeBlock;

/// Hand-maintained block [Shape] declarations for GT's compressed storage blocks and the decorative sheetmetal
/// block (unlike [Materials2Shapes], not `gen_shapes.py`-generated: `block` is block-kind, which that generator
/// excludes -- see `scripts/mu/gen_shapes.py`'s `is_block_kind`). `block`'s membership is driven by
/// `dumps/legacy-blocks.json`, captured directly from the legacy `gregtech.common.blocks.BlockMetal` instances
/// (see `MaterialDataDump#dumpLegacyBlocks`), since the `block` `OrePrefixes` entry generates through a
/// hand-curated per-instance `Materials[]` array rather than the generic capability-bit pipeline every other
/// prefix uses (its dumped `generationBits` is `0`). `sheetmetal` generates through that generic pipeline, same
/// as the pipe/frame shapes in [Materials2PipeShapes].
public class Materials2BlockShapes {

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

    private Materials2BlockShapes() {}
}
