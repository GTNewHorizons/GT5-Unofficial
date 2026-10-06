package gregtech.loaders.materials;

import gregtech.GTMod;
import gregtech.common.config.Gregtech;

/// Applies GregTech's own per-material settings that MaterialLib has no equivalent for. These cover prefix
/// generation overrides, which materials skip a hot ingot, and the configured harvest levels in [GTMod#proxy].
public final class LoaderGTMaterialPasses {

    private LoaderGTMaterialPasses() {}

    public static void run() {
        addHarvestLevels();
    }

    private static void addHarvestLevels() {
        GTMod.proxy.mChangeHarvestLevels = Gregtech.harvestLevel.activateHarvestLevelChange;
        GTMod.proxy.mMaxHarvestLevel = Math.min(15, Gregtech.harvestLevel.maxHarvestLevel);
        GTMod.proxy.mGraniteHavestLevel = Gregtech.harvestLevel.graniteHarvestLevel;
    }
}
