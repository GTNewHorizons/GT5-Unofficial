package gregtech.loaders.materials;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2IDIndex;
import gregtech.api.enums.materials2.Materials2Markers;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialRef;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;

/// Temporary boot-time harness that proves the ML-native tail of `MU`'s ten transitional union helpers
/// ([MU#localizedNameKeyOf], [MU#localizedNameOf], [MU#textureSetOf], [MU#rgbaOf], [MU#validStonesOf],
/// [MU#generatesPrefix(Object,OrePrefixes)], [MU#hasSubTag(Object,SubTag)], [MU#partOf], [MU#idOf],
/// [MU#addTooltipsOf]) returns exactly what the live legacy-facade branch returns today, for every material
/// that currently resolves through the `Materials` facade. Each helper's live shape is `ML arm ->
/// legacyMaterialOf -> Materials arm -> legacy method`; once `Materials` is deleted every one of those calls
/// falls through to the ML-native tail unconditionally. This class reimplements each tail independently
/// (never by calling the `MU` helper itself, which would compare the live path against itself) and diffs it
/// against the current legacy result, so a divergence is caught before the facade is deleted rather than
/// after. It also checks five related pieces of state that the same deletion will strand: `ingotHot`'s
/// disabled-material set, tool-handle material resolution, the harvest-level array, and the legacy name
/// domain's coverage of [GTMaterialProperties#OLD_SUB_ID]-carrying materials. `handleOverrideOverlap` reports
/// the merged werkstoff/gregtech declarations where both the legacy `mHandleMaterial` field and the werkstoff
/// handle pass ([gregtech.loaders.postload.LoaderWerkstoffRegistrations#registerHandleMaterial]) claim a
/// handle, for triage once that pass gates on the legacy name domain instead of the facade.
///
/// Every mismatch is collected rather than thrown on first miss, so one run enumerates the full divergence
/// surface. DIES with the `Materials` facade -- delete this class in the same change that deletes it.
public final class LegacyHelperParity {

    private static final String[] KEYS = { "unresolved", "localizedNameKey", "localizedName", "textureSet", "rgba",
        "validStones", "generatesPrefix", "subTag", "part", "id", "tooltips", "disabledHotIngots", "handleMaterial",
        "handleOverrideOverlap", "harvestLevels", "oldSubIdInDomain", "nameDomainDiff" };

    private static Collection<Materials> recordedLegacyHotIngots;
    private static Collection<Material> recordedMlHotIngots;

    // spotless:off
    private static final String[] DISABLED_HOT_INGOT_NAMES = { "Reinforced", "ConductiveIron", "FierySteel",
        "ElectricalSteel", "EndSteel", "Soularium", "EnergeticSilver", "Cheese", "Calcium", "Flerovium", "Cobalt",
        "RedstoneAlloy", "Ardite", "DarkSteel", "BlackSteel", "EnergeticAlloy", "PulsatingIron", "ClayCompound",
        "Netherite", "HotProtoHalkonite", "ProtoHalkonite", "HotExoHalkonite", "ExoHalkonite" };

    private static final Materials[] DISABLED_HOT_INGOT_MATERIALS = { Materials.Reinforced, Materials.ConductiveIron,
        Materials.FierySteel, Materials.ElectricalSteel, Materials.EndSteel, Materials.Soularium,
        Materials.EnergeticSilver, Materials.Cheese, Materials.Calcium, Materials.Flerovium, Materials.Cobalt,
        Materials.RedstoneAlloy, Materials.Ardite, Materials.DarkSteel, Materials.BlackSteel,
        Materials.EnergeticAlloy, Materials.PulsatingIron, Materials.ClayCompound, Materials.Netherite,
        Materials.HotProtoHalkonite, Materials.ProtoHalkonite, Materials.HotExoHalkonite, Materials.ExoHalkonite };
    // spotless:on

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .disableHtmlEscaping()
        .serializeNulls()
        .serializeSpecialFloatingPointValues()
        .create();

    private LegacyHelperParity() {}

    /// Stashes the legacy and proposed disabled-hot-ingot sets `Materials#disableUnusedHotIngots` computes at
    /// population time, before its own mutation of `OrePrefixes#ingotHot`'s `mDisabledItems` would make a
    /// post-hoc recomputation self-referential ([OrePrefixes#doGenerateItem] consults `mDisabledItems`).
    public static void recordHotIngotSets(Collection<Materials> legacy, Collection<Material> ml) {
        recordedLegacyHotIngots = legacy;
        recordedMlHotIngots = ml;
    }

    public static void verifyAgainstLegacy() {
        Map<String, List<Map<String, Object>>> mismatches = new LinkedHashMap<>();
        for (String key : KEYS) mismatches.put(key, new ArrayList<>());

        Map<Materials, Material> pairs = buildPairs(mismatches);
        for (Map.Entry<Materials, Material> entry : pairs.entrySet()) {
            Materials legacy = entry.getKey();
            Material ml = entry.getValue();
            checkLocalizedNameKey(mismatches, legacy, ml);
            checkLocalizedName(mismatches, legacy, ml);
            checkTextureSet(mismatches, legacy, ml);
            checkRgba(mismatches, legacy, ml);
            checkValidStones(mismatches, legacy, ml);
            checkGeneratesPrefix(mismatches, legacy, ml);
            checkSubTag(mismatches, legacy, ml);
            checkPart(mismatches, legacy, ml);
            checkId(mismatches, legacy, ml);
            checkTooltips(mismatches, legacy, ml);
            checkHandleMaterial(mismatches, legacy, ml);
        }

        Map<String, String> disabledHotIngotNames = checkDisabledHotIngots(mismatches);
        checkHandleOverrideOverlap(mismatches);
        checkHarvestLevels(mismatches);
        checkOldSubIdInDomain(mismatches);
        checkNameDomainDiff(mismatches);

        write(mismatches, disabledHotIngotNames);
        log(mismatches);
    }

    // region pairing

    private static Map<Materials, Material> buildPairs(Map<String, List<Map<String, Object>>> mismatches) {
        Map<Materials, Material> pairs = new LinkedHashMap<>();

        for (Materials facade : Materials.values()) {
            Material ml = MU.material(facade);
            if (ml == null) {
                addUnresolved(mismatches, facade.mName);
                continue;
            }
            pairs.put(facade, ml);
        }

        for (Material ml : MaterialLibAPI.getMaterials()) {
            Materials facade = MU.materialOf(ml);
            if (facade != null) pairs.putIfAbsent(facade, ml);
        }

        addMarkerPair(pairs, mismatches, "AnyBronze", Materials2Markers.AnyBronze, Materials.AnyBronze);
        addMarkerPair(pairs, mismatches, "AnyCarbon", Materials2Markers.AnyCarbon, Materials.AnyCarbon);
        addMarkerPair(pairs, mismatches, "AnyCopper", Materials2Markers.AnyCopper, Materials.AnyCopper);
        addMarkerPair(pairs, mismatches, "AnyIron", Materials2Markers.AnyIron, Materials.AnyIron);
        addMarkerPair(pairs, mismatches, "AnyRubber", Materials2Markers.AnyRubber, Materials.AnyRubber);
        addMarkerPair(
            pairs,
            mismatches,
            "AnySyntheticRubber",
            Materials2Markers.AnySyntheticRubber,
            Materials.AnySyntheticRubber);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorMV",
            Materials2Markers.SuperconductorMV,
            Materials.SuperconductorMV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorHV",
            Materials2Markers.SuperconductorHV,
            Materials.SuperconductorHV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorEV",
            Materials2Markers.SuperconductorEV,
            Materials.SuperconductorEV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorIV",
            Materials2Markers.SuperconductorIV,
            Materials.SuperconductorIV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorLuV",
            Materials2Markers.SuperconductorLuV,
            Materials.SuperconductorLuV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorZPM",
            Materials2Markers.SuperconductorZPM,
            Materials.SuperconductorZPM);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorUV",
            Materials2Markers.SuperconductorUV,
            Materials.SuperconductorUV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorUHV",
            Materials2Markers.SuperconductorUHV,
            Materials.SuperconductorUHV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorUEV",
            Materials2Markers.SuperconductorUEV,
            Materials.SuperconductorUEV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorUIV",
            Materials2Markers.SuperconductorUIV,
            Materials.SuperconductorUIV);
        addMarkerPair(
            pairs,
            mismatches,
            "SuperconductorUMV",
            Materials2Markers.SuperconductorUMV,
            Materials.SuperconductorUMV);

        return pairs;
    }

    private static void addMarkerPair(Map<Materials, Material> pairs, Map<String, List<Map<String, Object>>> mismatches,
        String name, Material ml, Materials facade) {
        if (ml == null || facade == null) {
            addUnresolved(mismatches, name);
            return;
        }
        pairs.putIfAbsent(facade, ml);
    }

    // endregion

    // region the ten helper comparisons

    private static void checkLocalizedNameKey(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        String legacyValue = legacy.getLocalizedNameKey();
        String mlValue = "Material." + MU.internalName(ml)
            .toLowerCase();
        if (!Objects.equals(legacyValue, mlValue)) {
            mismatches.get("localizedNameKey")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    private static void checkLocalizedName(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        String legacyValue = legacy.getLocalizedName();
        String mlValue = StatCollector.translateToLocal(
            "Material." + MU.internalName(ml)
                .toLowerCase());
        if (!Objects.equals(legacyValue, mlValue)) {
            mismatches.get("localizedName")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    private static void checkTextureSet(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        TextureSet legacyValue = legacy.getTextureSet();
        TextureSet mlValue = MU.iconSet(ml);
        // Compared by name and custom flag, not identity: `withCustomTextures` allocates a fresh set per call, so
        // the five custom-overlay materials never share an instance between the two sides.
        if (!Objects.equals(legacyValue.mSetName, mlValue.mSetName) || legacyValue.is_custom != mlValue.is_custom) {
            mismatches.get("textureSet")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    private static void checkRgba(Map<String, List<Map<String, Object>>> mismatches, Materials legacy, Material ml) {
        short[] legacyValue = legacy.getRGBA();
        short[] mlValue = MU.rgba(ml);
        if (!Arrays.equals(legacyValue, mlValue)) {
            mismatches.get("rgba")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    private static void checkValidStones(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        List<IStoneType> legacyValue = legacy.getValidStones();
        List<IStoneType> mlValue = MU.hasFlag(ml, GTMaterialFlag.ICE_ORE) ? StoneType.ICES : StoneType.STONES;
        if (legacyValue != mlValue) {
            mismatches.get("validStones")
                .add(row(MU.internalName(ml), describeStones(legacyValue), describeStones(mlValue)));
        }
    }

    private static String describeStones(List<IStoneType> stones) {
        if (stones == StoneType.ICES) return "ICES";
        if (stones == StoneType.STONES) return "STONES";
        return String.valueOf(stones);
    }

    private static void checkGeneratesPrefix(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            boolean legacyValue = legacy.generatesPrefix(prefix);
            boolean mlValue = prefix.doGenerateItem(ml) || Materials2WerkstoffIndex.generatesPrefix(ml, prefix);
            if (legacyValue != mlValue) {
                mismatches.get("generatesPrefix")
                    .add(row(MU.internalName(ml), "prefix", prefix.name(), legacyValue, mlValue));
            }
        }
    }

    private static void checkSubTag(Map<String, List<Map<String, Object>>> mismatches, Materials legacy, Material ml) {
        for (SubTag tag : SubTag.sSubTags.values()) {
            boolean legacyValue = legacy.contains(tag);
            GTMaterialFlag flag = flagForSubTag(tag);
            boolean mlValue = flag != null && MU.hasFlag(ml, flag);
            if (legacyValue != mlValue) {
                mismatches.get("subTag")
                    .add(row(MU.internalName(ml), "subTag", tag.mName, legacyValue, mlValue));
            }
        }
    }

    private static void checkPart(Map<String, List<Map<String, Object>>> mismatches, Materials legacy, Material ml) {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            ItemStack legacyValue = legacy.getPart(prefix, 1);
            ItemStack mlValue = GTOreDictUnificator.get(prefix, ml, 1);
            boolean equal = legacyValue == null ? mlValue == null
                : mlValue != null && GTUtility.areStacksEqual(legacyValue, mlValue);
            if (!equal) {
                mismatches.get("part")
                    .add(row(MU.internalName(ml), "prefix", prefix.name(), legacyValue, mlValue));
            }
        }
    }

    private static void checkId(Map<String, List<Map<String, Object>>> mismatches, Materials legacy, Material ml) {
        int legacyValue = legacy.getId();
        int mlValue = MU.oldSubId(ml);
        if (legacyValue != mlValue) {
            mismatches.get("id")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    private static void checkTooltips(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        List<String> legacyValue = new ArrayList<>();
        legacy.addTooltips(legacyValue);
        List<String> mlValue = new ArrayList<>();
        if (Client.tooltip.showFormula) {
            String tooltip = MU.chemicalTooltip(ml, false);
            if (tooltip != null && !tooltip.isEmpty()) mlValue.add(tooltip);
        }
        if (!legacyValue.equals(mlValue)) {
            mismatches.get("tooltips")
                .add(row(MU.internalName(ml), legacyValue, mlValue));
        }
    }

    // endregion

    // region the five extra assertions

    private static Map<String, String> checkDisabledHotIngots(Map<String, List<Map<String, Object>>> mismatches) {
        Map<String, String> disabledHotIngotNames = new LinkedHashMap<>();
        for (int i = 0; i < DISABLED_HOT_INGOT_NAMES.length; i++) {
            Material ml = MU.material(DISABLED_HOT_INGOT_MATERIALS[i]);
            disabledHotIngotNames.put(DISABLED_HOT_INGOT_NAMES[i], ml != null ? ml.getName() : null);
        }

        if (recordedLegacyHotIngots == null || recordedMlHotIngots == null) {
            mismatches.get("disabledHotIngots")
                .add(materialRow("not recorded"));
            return disabledHotIngotNames;
        }

        Set<Material> legacySet = new LinkedHashSet<>();
        for (Materials legacy : recordedLegacyHotIngots) {
            Material ml = MU.material(legacy);
            if (ml != null) legacySet.add(ml);
        }
        Set<Material> mlSet = new LinkedHashSet<>(recordedMlHotIngots);

        for (Material ml : legacySet) {
            if (!mlSet.contains(ml)) {
                mismatches.get("disabledHotIngots")
                    .add(sideRow(MU.internalName(ml), "legacy-only"));
            }
        }
        for (Material ml : mlSet) {
            if (!legacySet.contains(ml)) {
                mismatches.get("disabledHotIngots")
                    .add(sideRow(MU.internalName(ml), "ml-only"));
            }
        }
        return disabledHotIngotNames;
    }

    private static void checkHandleMaterial(Map<String, List<Map<String, Object>>> mismatches, Materials legacy,
        Material ml) {
        Material legacyValue = MU.material(legacy.mHandleMaterial);
        Material mlValue = proposedHandleMaterial(ml);
        if (legacyValue != mlValue) {
            mismatches.get("handleMaterial")
                .add(
                    row(
                        MU.internalName(ml),
                        legacyValue == null ? null : MU.internalName(legacyValue),
                        mlValue == null ? null : MU.internalName(mlValue)));
        }
    }

    private static Material proposedHandleMaterial(Material ml) {
        MaterialRef ref = ml.getProperty(GTMaterialProperties.HANDLE_MATERIAL);
        if (ref == null) return ml;
        Material resolved = ref.resolve();
        return resolved != null ? resolved : ml;
    }

    /// Every merged werkstoff/gregtech declaration -- a [Material] carrying
    /// [GTMaterialProperties#WERKSTOFF_IDS] whose name also resolves through [LegacyNameDomain] -- where both
    /// `Materials#mHandleMaterial` and [gregtech.loaders.postload.LoaderWerkstoffRegistrations
    /// #registerHandleMaterial]'s werkstoff handle pass claim a handle for the same material. Reports both
    /// computed handles for every such material rather than only the ones that disagree: the point is
    /// surfacing the overlap itself, which the legacy name domain gate resolves by always preferring the
    /// legacy field.
    private static void checkHandleOverrideOverlap(Map<String, List<Map<String, Object>>> mismatches) {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            if (LegacyNameDomain.lookup(MU.internalName(ml)) != ml) continue;

            Materials legacy = MU.materialOf(ml);
            Material legacyHandle = legacy == null ? null : MU.material(legacy.mHandleMaterial);
            Material werkstoffHandle = proposedWerkstoffHandle(ml);
            mismatches.get("handleOverrideOverlap")
                .add(
                    row(
                        MU.internalName(ml),
                        legacyHandle == null ? null : MU.internalName(legacyHandle),
                        MU.internalName(werkstoffHandle)));
        }
    }

    /// Mirrors `LoaderWerkstoffRegistrations#registerHandleMaterial`'s tiering exactly: a burning or magical
    /// material takes its themed handle, otherwise durability picks the metal.
    private static Material proposedWerkstoffHandle(Material ml) {
        Materials handle;
        if (MU.hasSubTag(ml, SubTag.BURNING.mName)) handle = Materials.Blaze;
        else if (MU.hasSubTag(ml, SubTag.MAGICAL.mName)) handle = Materials.Thaumium;
        else {
            int durability = MU.durability(ml);
            handle = durability > 5120 ? Materials.TungstenSteel : durability > 1280 ? Materials.Steel : Materials.Wood;
        }
        return MU.material(handle);
    }

    private static void checkHarvestLevels(Map<String, List<Map<String, Object>>> mismatches) {
        int[] live = GTMod.proxy.mHarvestLevel;
        int[] fresh = new int[live.length];
        for (int id = 0; id < fresh.length; id++) {
            Material material = Materials2IDIndex.get(id);
            if (material != null && GTMod.proxy.mChangeHarvestLevels && MU.toolQuality(material) > 0) {
                fresh[id] = MU.toolQuality(material);
            }
        }
        for (int id = 0; id < live.length; id++) {
            if (live[id] != fresh[id]) {
                mismatches.get("harvestLevels")
                    .add(row(String.valueOf(id), live[id], fresh[id]));
            }
        }
    }

    private static void checkOldSubIdInDomain(Map<String, List<Map<String, Object>>> mismatches) {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.OLD_SUB_ID) == null) continue;
            String name = MU.internalName(ml);
            if (!LegacyNameDomainTable.DOMAIN.containsKey(name)) {
                mismatches.get("oldSubIdInDomain")
                    .add(materialRow(name));
            }
        }
    }

    private static void checkNameDomainDiff(Map<String, List<Map<String, Object>>> mismatches) {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            boolean viaMaterialOf = MU.materialOf(ml) != null;
            boolean viaNameDomain = LegacyNameDomain.lookup(MU.internalName(ml)) == ml;
            if (viaMaterialOf != viaNameDomain) {
                mismatches.get("nameDomainDiff")
                    .add(sideRow(MU.internalName(ml), viaMaterialOf ? "materialOf-only" : "nameDomain-only"));
            }
        }
    }

    // endregion

    /// The [GTMaterialFlag] whose enum-constant name equals `subTag`'s name, or null when none does. Mirrors
    /// `MU#flagForSubTag`, reimplemented here rather than called so [#checkSubTag]'s ML tail does not run
    /// through the helper this class exists to check.
    private static @Nullable GTMaterialFlag flagForSubTag(SubTag subTag) {
        switch (subTag.mName) {
            case "AnaerobeGas":
                return GTMaterialFlag.ANAEROBE_GAS;
            case "NobleGas":
                return GTMaterialFlag.NOBLE_GAS;
            default:
                break;
        }
        try {
            return GTMaterialFlag.valueOf(subTag.mName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // region output

    private static void addUnresolved(Map<String, List<Map<String, Object>>> mismatches, String materialName) {
        mismatches.get("unresolved")
            .add(materialRow(materialName));
    }

    private static Map<String, Object> materialRow(String material) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("material", material);
        return row;
    }

    private static Map<String, Object> sideRow(String material, String side) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("material", material);
        row.put("side", side);
        return row;
    }

    private static Map<String, Object> row(String material, Object legacy, Object ml) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("material", material);
        row.put("legacy", describe(legacy));
        row.put("ml", describe(ml));
        return row;
    }

    private static Map<String, Object> row(String material, String extraKey, String extraValue, Object legacy,
        Object ml) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("material", material);
        row.put(extraKey, extraValue);
        row.put("legacy", describe(legacy));
        row.put("ml", describe(ml));
        return row;
    }

    private static @Nullable String describe(@Nullable Object value) {
        if (value == null) return null;
        if (value instanceof short[]shorts) return Arrays.toString(shorts);
        if (value instanceof TextureSet set) return set.mSetName;
        if (value instanceof ItemStack stack) return describeStack(stack);
        return String.valueOf(value);
    }

    private static String describeStack(ItemStack stack) {
        UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return (id != null ? id.toString() : "UNKNOWN") + ":" + stack.getItemDamage() + "x" + stack.stackSize;
    }

    private static void write(Map<String, List<Map<String, Object>>> mismatches,
        Map<String, String> disabledHotIngotNames) {
        Map<String, Integer> summary = new LinkedHashMap<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : mismatches.entrySet()) {
            summary.put(
                entry.getKey(),
                entry.getValue()
                    .size());
        }

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("summary", summary);
        root.put("mismatches", mismatches);
        root.put("disabledHotIngotNames", disabledHotIngotNames);

        File directory = new File(Launch.minecraftHome, "material-dump");
        directory.mkdirs();
        File file = new File(directory, "helper-parity.json");
        try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write material dump " + file, e);
        }
        GTLog.out.println("LegacyHelperParity: wrote " + file);
    }

    private static void log(Map<String, List<Map<String, Object>>> mismatches) {
        int total = 0;
        for (Map.Entry<String, List<Map<String, Object>>> entry : mismatches.entrySet()) {
            int count = entry.getValue()
                .size();
            total += count;
            GTMod.GT_FML_LOGGER
                .info("LegacyHelperParity.verifyAgainstLegacy: {} mismatches for {}", count, entry.getKey());
        }
        GTMod.GT_FML_LOGGER.info("LegacyHelperParity.verifyAgainstLegacy: {} total mismatches", total);
    }

    // endregion
}
