package gregtech.loaders.materials;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2IDIndex;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.common.config.Gregtech;

/// gregtech-owned material passes that have no MaterialLib equivalent: disabling the hot-ingot variant for
/// materials whose blast furnace temperature does not warrant one, and writing the configured harvest-level
/// overrides into [GTMod#proxy]'s harvest-level table.
public final class LoaderLegacyMaterialPasses {

    private LoaderLegacyMaterialPasses() {}

    public static void run() {
        disableUnusedHotIngots();
        addHarvestLevels();
        addHarvestLevelNerfs();
    }

    private static void disableUnusedHotIngots() {
        Set<Materials> legacyHotIngots = Arrays.stream(Materials.values())
            .parallel()
            .filter(OrePrefixes.ingotHot::doGenerateItem)
            .filter(m -> m.mBlastFurnaceTemp < 1750 && m.mAutoGenerateBlastFurnaceRecipes)
            .collect(Collectors.toSet());
        Set<Material> mlHotIngots = MaterialLibAPI.getMaterials()
            .stream()
            .filter(
                m -> OrePrefixes.ingotHot.doGenerateItem(m) && MU.blastFurnaceTemp(m) < 1750
                    && MU.autoGenerateBlastFurnaceRecipes(m))
            .collect(Collectors.toSet());
        LegacyHelperParity.recordHotIngotSets(legacyHotIngots, mlHotIngots);

        OrePrefixes.ingotHot.mDisabledItems.addAll(mlHotIngots);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Reinforced);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.ConductiveIron);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.FierySteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.ElectricalSteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.EndSteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Soularium);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.EnergeticSilver);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Cheese);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Calcium);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.FleroviumGT5U);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Cobalt);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.RedstoneAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Ardite);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.DarkSteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.BlackSteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.EnergeticAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.PulsatingIron);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.CrudeSteel);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.Netherite);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.hotprotohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.protohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.hotexohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials2Materials.exohalkonite);
    }

    private static void addHarvestLevels() {
        GTMod.proxy.mChangeHarvestLevels = Gregtech.harvestLevel.activateHarvestLevelChange;
        GTMod.proxy.mMaxHarvestLevel = Math.min(15, Gregtech.harvestLevel.maxHarvestLevel);
        GTMod.proxy.mGraniteHavestLevel = Gregtech.harvestLevel.graniteHarvestLevel;
    }

    private static void addHarvestLevelNerfs() {
        if (!GTMod.proxy.mChangeHarvestLevels) return;
        for (int id = 0; id < GTMod.proxy.mHarvestLevel.length; id++) {
            Material material = Materials2IDIndex.get(id);
            if (material == null) continue;
            int toolQuality = MU.toolQuality(material);
            if (toolQuality > 0) GTMod.proxy.mHarvestLevel[id] = toolQuality;
        }
    }
}
