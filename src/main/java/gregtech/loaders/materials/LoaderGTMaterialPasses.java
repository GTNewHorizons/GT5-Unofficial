package gregtech.loaders.materials;

import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.material.MaterialUtils;
import gregtech.common.config.Gregtech;

/// Applies GregTech's own per-material settings that MaterialLib has no equivalent for. These cover prefix
/// generation overrides, which materials skip a hot ingot, and the configured harvest levels in [GTMod#proxy].
public final class LoaderGTMaterialPasses {

    private LoaderGTMaterialPasses() {}

    public static void run() {
        addHarvestLevels();
        addHarvestLevelNerfs();
    }

    private static void addHarvestLevels() {
        GTMod.proxy.mChangeHarvestLevels = Gregtech.harvestLevel.activateHarvestLevelChange;
        GTMod.proxy.mMaxHarvestLevel = Math.min(15, Gregtech.harvestLevel.maxHarvestLevel);
        GTMod.proxy.mGraniteHavestLevel = Gregtech.harvestLevel.graniteHarvestLevel;
    }

    private static void addHarvestLevelNerfs() {
        if (!GTMod.proxy.mChangeHarvestLevels) return;
        for (int id = 0; id < GTMod.proxy.mHarvestLevel.length; id++) {
            Material material = LegacyMaterialIDIndex.get(id);
            if (material == null) continue;
            int toolQuality = MaterialUtils.toolQuality(material);
            if (toolQuality > 0) GTMod.proxy.mHarvestLevel[id] = toolQuality;
        }
    }
}
