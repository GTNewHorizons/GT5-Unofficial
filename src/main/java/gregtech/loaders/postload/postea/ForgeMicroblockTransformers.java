package gregtech.loaders.postload.postea;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.crossmod.forgemultipart.MicroblockMaterials;
import gregtech.loaders.postload.LegacyStorageBlockTable;

final class ForgeMicroblockTransformers {

    private ForgeMicroblockTransformers() {}

    /// Remaps the saved microblock materials of the legacy storage blocks onto the MaterialLib blocks' names (see
    /// [MicroblockMaterials]), and those of `bartworks:BW_GlasBlocks` onto the tiered and extra bartworks glass.
    static void register() {
        LegacyStorageBlockTable.BATCHES.forEach((block, batch) -> {
            for (int meta = 0; meta < batch.length; meta++) {
                if (batch[meta] != null) MicroblockMaterials.remapLegacy(block, meta, batch[meta]);
            }
        });
        for (Material material : MaterialLibAPI.getMaterials()) {
            int id = LegacyWerkstoffIndex.idOf(material);
            if (id >= 0) MicroblockMaterials.remapLegacy("bartworks:bw.werkstoffblocks.01", id, material);
        }
        for (int meta = 0; meta <= 12; meta++) {
            String glass = meta == 12 ? "bartworks:BW_ExtraGlass" : "bartworks:BW_TieredGlass";
            MicroblockMaterials.remapLegacy("bartworks:BW_GlasBlocks", meta, glass, meta <= 5 ? meta : 0);
        }
    }
}
