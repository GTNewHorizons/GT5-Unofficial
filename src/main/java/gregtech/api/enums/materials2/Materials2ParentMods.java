package gregtech.api.enums.materials2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.material.MU;
import gregtech.common.blocks.BlockMetal;

/// The parent-mod availability gate for the [BlockMetal] storage-block materials as declared rows:
/// material -> the [Mods] entry whose absence disables the material. The legacy data declares exactly one
/// row -- `HSLA` is disabled when RotaryCraft is absent
/// (`gregtech.loaders.materialprocessing.ProcessingModSupport#onMaterialsInit`, the sole writer of
/// `Materials#mHasParentMod` beside its `true` default), so every other material passes unconditionally.
/// That clear only happens when `ProcessingModSupport` registers at all, which `Materials`'s init skips
/// under the `GTMod.proxy.mEnableAllMaterials` config -- [#hasParentMod] reproduces both legs.
public class Materials2ParentMods {

    private static Map<Material, Mods> table;

    private Materials2ParentMods() {}

    /// Whether `material`'s parent mod is present (or it has none) -- the [Material]-side read of
    /// `Materials#mHasParentMod`.
    public static boolean hasParentMod(Material material) {
        Mods parent = table().get(material);
        return parent == null || parent.isModLoaded() || GTMod.proxy.mEnableAllMaterials;
    }

    private static Map<Material, Mods> table() {
        if (table == null) {
            if (Materials2Materials.HSLA == null) {
                throw new IllegalStateException("Parent-mod table consulted before Materials2.init");
            }
            table = Map.of(Materials2Materials.HSLA, Mods.RotaryCraft);
        }
        return table;
    }

    /// Verifies the table-derived gate against the live legacy `mHasParentMod` flag for every material in
    /// the 13 [BlockMetal] storage-block arrays. Throws [IllegalStateException] on any mismatch. Requires
    /// the blocks to be constructed and the legacy facade to be fully initialized, so it can only run at
    /// boot.
    public static void verifyAgainstLegacy() {
        List<String> mismatches = new ArrayList<>();
        Block[] blocks = { GregTechAPI.sBlockMetal1, GregTechAPI.sBlockMetal2, GregTechAPI.sBlockMetal3,
            GregTechAPI.sBlockMetal4, GregTechAPI.sBlockMetal5, GregTechAPI.sBlockMetal6, GregTechAPI.sBlockMetal7,
            GregTechAPI.sBlockMetal8, GregTechAPI.sBlockMetal9, GregTechAPI.sBlockMetal10, GregTechAPI.sBlockGem1,
            GregTechAPI.sBlockGem2, GregTechAPI.sBlockGem3 };
        int checked = 0;
        for (Block block : blocks) {
            for (Material material : ((BlockMetal) block).mMats) {
                Materials legacy = MU.materialOf(material);
                boolean live = legacy != null && legacy.mHasParentMod;
                if (hasParentMod(material) != live) mismatches.add(MU.internalName(material));
                checked++;
            }
        }
        if (!mismatches.isEmpty()) {
            throw new IllegalStateException(
                "Declared parent-mod gate disagrees with live mHasParentMod for: " + mismatches);
        }
        GTMod.GT_FML_LOGGER.info(
            "Materials2ParentMods.verifyAgainstLegacy: parent-mod gate matches live mHasParentMod ({} materials)",
            checked);
    }
}
