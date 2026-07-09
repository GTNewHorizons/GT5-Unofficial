package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.common.blocks.GTStorageShapeBlock;

/// Hand-maintained block [Shape] declaration for GT's compressed storage blocks (unlike [Materials2Shapes], not
/// `gen_shapes.py`-generated: `block` is block-kind, which that generator excludes -- see
/// `scripts/mu/gen_shapes.py`'s `is_block_kind`). Membership is driven by `dumps/legacy-blocks.json`, captured
/// directly from the legacy `gregtech.common.blocks.BlockMetal` instances (see
/// `MaterialDataDump#dumpLegacyBlocks`), since the `block` `OrePrefixes` entry generates through a hand-curated
/// per-instance `Materials[]` array rather than the generic capability-bit pipeline every other prefix uses
/// (its dumped `generationBits` is `0`).
public class Materials2BlockShapes {

    public static Shape shapeBlock;

    public static void init() {
        shapeBlock = MaterialLibAPI
            .registerBlockShape(new GTStorageShapeBlock("gregtech", "block", "Block of %s", "block"));
    }

    private Materials2BlockShapes() {}
}
