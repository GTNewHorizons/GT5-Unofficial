package gregtech.loaders.materials;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2IDIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.common.config.Gregtech;

/// gregtech-owned material passes that have no MaterialLib equivalent: disabling the hot-ingot variant for
/// materials whose blast furnace temperature does not warrant one, and writing the configured harvest-level
/// overrides into [GTMod#proxy]'s harvest-level table.
public final class LoaderLegacyMaterialPasses {

    private LoaderLegacyMaterialPasses() {}

    public static void run() {
        applyPrefixGenerationOverrides();
        disableUnusedHotIngots();
        addHarvestLevels();
        addHarvestLevelNerfs();
    }

    /// Applies each material's per-prefix generation overrides: [GTMaterialProperties#ADDED_PREFIXES] names the
    /// prefixes it generates beyond its generation-flag categories, and [GTMaterialProperties#REMOVED_PREFIXES]
    /// the ones it is excluded from. Both feed [OrePrefixes#doGenerateItem], so this runs before any pass that
    /// consults it.
    private static void applyPrefixGenerationOverrides() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            List<String> added = material.getProperty(GTMaterialProperties.ADDED_PREFIXES);
            if (added != null) for (String prefixName : added) {
                requirePrefix(prefixName, material).mGeneratedItems.add(material);
            }
            List<String> removed = material.getProperty(GTMaterialProperties.REMOVED_PREFIXES);
            if (removed != null) for (String prefixName : removed) {
                requirePrefix(prefixName, material).mNotGeneratedItems.add(material);
            }
        }
    }

    private static OrePrefixes requirePrefix(String name, Material material) {
        OrePrefixes prefix = OrePrefixes.getPrefix(name, null);
        if (prefix == null) {
            throw new IllegalStateException("No OrePrefixes named " + name + " for material " + material.getName());
        }
        return prefix;
    }

    private static void disableUnusedHotIngots() {
        Set<Material> mlHotIngots = MaterialLibAPI.getMaterials()
            .stream()
            .filter(
                m -> OrePrefixes.ingotHot.doGenerateItem(m) && MaterialUtils.blastFurnaceTemp(m) < 1750
                    && MaterialUtils.autoGenerateBlastFurnaceRecipes(m))
            .collect(Collectors.toSet());

        OrePrefixes.ingotHot.mDisabledItems.addAll(mlHotIngots);
        OrePrefixes.ingotHot.disableComponent(Materials.Reinforced);
        OrePrefixes.ingotHot.disableComponent(Materials.ConductiveIron);
        OrePrefixes.ingotHot.disableComponent(Materials.FierySteel);
        OrePrefixes.ingotHot.disableComponent(Materials.ElectricalSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.EndSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.Soularium);
        OrePrefixes.ingotHot.disableComponent(Materials.EnergeticSilver);
        OrePrefixes.ingotHot.disableComponent(Materials.Cheese);
        OrePrefixes.ingotHot.disableComponent(Materials.Calcium);
        OrePrefixes.ingotHot.disableComponent(Materials.FleroviumGT5U);
        OrePrefixes.ingotHot.disableComponent(Materials.Cobalt);
        OrePrefixes.ingotHot.disableComponent(Materials.RedstoneAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials.Ardite);
        OrePrefixes.ingotHot.disableComponent(Materials.DarkSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.BlackSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.EnergeticAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials.PulsatingIron);
        OrePrefixes.ingotHot.disableComponent(Materials.CrudeSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.Netherite);
        OrePrefixes.ingotHot.disableComponent(Materials.hotprotohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.protohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.hotexohalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.exohalkonite);
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
            int toolQuality = MaterialUtils.toolQuality(material);
            if (toolQuality > 0) GTMod.proxy.mHarvestLevel[id] = toolQuality;
        }
    }
}
