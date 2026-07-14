package bartworks.system.material;

import static net.minecraft.util.EnumChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialAtomics;
import gregtech.api.material.MaterialRefStack;
import gregtech.loaders.materials.LegacyMaterials;

/// Rebuilds every legacy `Werkstoff` from MaterialLib data (the werkstoff counterpart of
/// `MaterialsLegacyBridge`): the pool declaration lists (`WerkstoffLoader`, `GGMaterial`,
/// `WerkstoffMaterialPool`, `BotWerkstoffMaterialPool`) initialize each field via [#byId], and the first
/// lookup builds ALL werkstoff-backed materials in ascending-first-id order (matching declaration-order
/// dependencies -- every werkstoff-to-werkstoff contents reference points at a lower id, validated at build).
///
/// Reconstruction inputs are the decomposed `GTMaterialProperties#WERKSTOFF_*` properties (bartworks-side
/// data, pool scalars preserved even where the gregtech dump won the shared keys -- see each property's
/// javadoc) plus the shared `ARGB`/`LOCAL_NAME`/`TEXTURE_SET`/`PROCESSING_MATERIAL_TIER_EU` values.
/// Contents/byproduct references come from the canonical [GTMaterialProperties#COMPOSITION]/
/// [GTMaterialProperties#ORE_BYPRODUCTS] and resolve against the registry the legacy declaration used
/// (see [#resolveContentRef]/[#resolveByproductRef]); a byproduct naming this material itself resolves to
/// the instance under construction (byproduct self-padding). `bridgeMaterial` stays null here --
/// `BridgeMaterialsLoader` assigns it at bartworks' init exactly as before.
///
/// [#applyGenerationBits] must run after `Werkstoff.GenerationFeatures.initPrefixLogic()` (see
/// `WerkstoffLoader#setUp`): construction encodes the prefix ground truth purely as explicit per-prefix
/// enables (the prefix-logic bitmasks are not initialized yet at class-load time), and the second pass then
/// fills the `toGenerate` bitmask (consulted by `WerkstoffLoader#addItemsForGeneration`'s aggregation) while
/// explicitly disabling every bitmask-covered prefix the ground truth omits, so `hasItemType` keeps returning
/// exactly the dumped set.
public final class WerkstoffReconstruction {

    /// Werkstoffe whose legacy `Stats#boilingPoint` the U1 melting/boiling collapse repointed at
    /// [GTMaterialProperties#BOILING_POINT] (previously a `WERKSTOFF_BOILING_POINT` override, elsewhere
    /// unset and defaulting to `0`) -- every other werkstoff-backed material keeps that `0` default even when
    /// it separately carries a [GTMaterialProperties#BOILING_POINT] of its own (real gregtech/gtpp fluid data
    /// unrelated to the legacy `Werkstoff` declaration, which never read it).
    private static final Set<String> COLLAPSED_BOILING_POINT_WERKSTOFFE = Set.of("Calcium", "Iodine", "LiquidHelium");

    /// Werkstoffe whose legacy `Stats#durOverride`/`#speedOverride`/`#qualityOverride` the U1 tool-stats
    /// collapse repointed at [GTMaterialProperties#DURABILITY]/[#TOOL_SPEED]/[#TOOL_QUALITY] -- each of these
    /// (former `WERKSTOFF_DURABILITY_OVERRIDE`/`WERKSTOFF_SPEED_OVERRIDE`/`WERKSTOFF_QUALITY_OVERRIDE`) equaled
    /// its canonical counterpart wherever it was present, so the override behavior survives the collapse by
    /// sourcing it from canonical for exactly this closed set instead -- every other werkstoff-backed material
    /// keeps computing the stat from protons/mass/melting point/contents (`Werkstoff#getDurability`/
    /// `#getToolSpeed`/`#getToolQuality`) even when it separately carries a nonzero canonical value of its own
    /// (128/109/369 of 392 do, entirely unrelated to any override -- see each canonical property's ordinary
    /// role). `HighDurabilityCompoundSteel` durability-overrides here though its legacy declaration used
    /// `Stats#durMod` (a 10x multiplier on the computed formula, also deleted) rather than a raw override --
    /// both landed on the same 2918 canonical value, so sourcing it as an override reproduces the same number.
    private static final Set<String> DURABILITY_OVERRIDE_WERKSTOFFE = Set.of(
        "CubicZirconia",
        "AdemicSteel",
        "TantalumHafniumCarbide",
        "ExtremelyUnstableNaquadah",
        "AdamantiumAlloy",
        "MAR-Ce-M200Steel",
        "Shirabon",
        "HighDurabilityCompoundSteel");
    private static final Set<String> SPEED_OVERRIDE_WERKSTOFFE = Set.of(
        "HighDurabilityCompoundSteel",
        "AdemicSteel",
        "TantalumHafniumCarbide",
        "ExtremelyUnstableNaquadah",
        "AdamantiumAlloy",
        "MAR-Ce-M200Steel",
        "PreciousMetalsAlloy",
        "EnrichedNaquadahAlloy",
        "Shirabon",
        "Mu-metal");
    private static final Set<String> QUALITY_OVERRIDE_WERKSTOFFE = Set
        .of("AdemicSteel", "TantalumHafniumCarbide", "ExtremelyUnstableNaquadah", "Shirabon");

    /// The two element werkstoffe that shadow a same-name gregtech `Materials` element: their legacy contents
    /// were the extension stub `[Materials.<name> x1]` (never a real chemical make-up, so the U1 composition
    /// collapse did not migrate it into [GTMaterialProperties#COMPOSITION]), and every other werkstoff's
    /// legacy contents reference to one of these names pointed at the `Materials` constant, never the
    /// werkstoff. Reconstruction synthesizes the stub for these two and resolves contents references to them
    /// through `Materials`.
    private static final Set<String> GT_ELEMENT_CONTENT_NAMES = Set.of("Calcium", "Tellurium");

    /// Werkstoffe carrying a canonical [GTMaterialProperties#COMPOSITION] (gregtech/gtpp-side chemical
    /// make-up) whose legacy `Werkstoff` declaration had no contents at all -- reconstruction keeps their
    /// contents empty rather than adopting the composition, which would alter the contents-derived legacy
    /// behaviors (tool-stat contents count, contents sub tags).
    private static final Set<String> CONTENTS_EXCLUDED_WERKSTOFFE = Set.of("Alumina", "ThoriumTetrafluoride");

    /// Byproduct references (`"owner:reference"`) whose legacy declaration named the `Materials` constant
    /// even though a same-name werkstoff exists; every other werkstoff-named byproduct reference resolved
    /// to the werkstoff registry.
    private static final Set<String> MATERIALS_BYPRODUCT_REFS = Set.of("Salt:RockSalt", "GreenFuchsite:Alumina");

    private static Map<Integer, Werkstoff> byId;

    private WerkstoffReconstruction() {}

    public static Werkstoff byId(int id) {
        ensureBuilt();
        Werkstoff werkstoff = byId.get(id);
        if (werkstoff == null) throw new IllegalStateException("No werkstoff-backed MaterialLib material for id " + id);
        return werkstoff;
    }

    /// Whether `werkstoff` was rebuilt from MaterialLib data (false for the `BWGTMaterialReference` proxies
    /// and any third-party WerkstoffAdder's werkstoff).
    public static boolean isReconstructed(Werkstoff werkstoff) {
        ensureBuilt();
        return byId.get((int) werkstoff.getmID()) == werkstoff;
    }

    /// See the class javadoc; called from `WerkstoffLoader#setUp` after `initPrefixLogic`.
    public static void applyGenerationBits() {
        ensureBuilt();
        for (Werkstoff werkstoff : new LinkedHashSet<>(byId.values())) {
            Werkstoff.GenerationFeatures features = werkstoff.getGenerationFeatures();
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                int bits = Werkstoff.GenerationFeatures.getPrefixDataRaw(prefix);
                if (bits == 0) continue;
                if (features.isExplicitlyEnabled(prefix)) {
                    features.toGenerate |= bits;
                } else {
                    features.removePrefix(prefix);
                }
            }
        }
    }

    private static synchronized void ensureBuilt() {
        if (byId != null) return;
        List<Material> werkstoffMaterials = new ArrayList<>();
        Map<String, Material> pending = new HashMap<>();
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!"gregtech".equals(material.getModId())) continue;
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) != null) {
                werkstoffMaterials.add(material);
                pending.put(material.getName(), material);
            }
        }
        // Ascending first id approximates declaration order; a forward contents reference (the pools declare
        // one: AtomicSeparationCatalyst 10022 -> Orundum 10023) recurses through resolveRef instead.
        werkstoffMaterials.sort(
            Comparator.comparingInt(
                material -> material.getProperty(GTMaterialProperties.WERKSTOFF_IDS)
                    .get(0)));
        Map<Integer, Werkstoff> built = new HashMap<>();
        Map<String, Werkstoff> byName = new HashMap<>();
        LinkedHashSet<String> inProgress = new LinkedHashSet<>();
        for (Material material : werkstoffMaterials) {
            build(material, built, byName, pending, inProgress);
        }
        byId = built;
    }

    private static void build(Material ml, Map<Integer, Werkstoff> built, Map<String, Werkstoff> byName,
        Map<String, Material> pending, LinkedHashSet<String> inProgress) {
        if (byName.containsKey(ml.getName())) return;
        if (!inProgress.add(ml.getName())) {
            throw new IllegalStateException("Werkstoff contents reference cycle: " + inProgress);
        }

        List<Integer> ids = ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS);

        int argb = ml.getProperty(GTMaterialProperties.ARGB);
        short[] rgba = { (short) (argb >> 16 & 0xFF), (short) (argb >> 8 & 0xFF), (short) (argb & 0xFF), 0 };

        Werkstoff.Stats stats = new Werkstoff.Stats()
            .setMeltingPoint(ml.getProperty(GTMaterialProperties.MELTING_POINT))
            .setBoilingPoint(
                COLLAPSED_BOILING_POINT_WERKSTOFFE.contains(ml.getName())
                    ? ml.getProperty(GTMaterialProperties.BOILING_POINT)
                    : 0)
            .setProtons(MaterialAtomics.protons(ml))
            .setNeutrons(0)
            .setMass(MaterialAtomics.mass(ml))
            .setMeltingVoltage(orDefault(ml.getProperty(GTMaterialProperties.MELTING_VOLTAGE), 120))
            .setDurOverride(
                DURABILITY_OVERRIDE_WERKSTOFFE.contains(ml.getName()) ? ml.getProperty(GTMaterialProperties.DURABILITY)
                    : 0)
            .setSpeedOverride(
                SPEED_OVERRIDE_WERKSTOFFE.contains(ml.getName()) ? ml.getProperty(GTMaterialProperties.TOOL_SPEED) : 0f)
            .setQualityOverride(
                (byte) (QUALITY_OVERRIDE_WERKSTOFFE.contains(ml.getName())
                    ? ml.getProperty(GTMaterialProperties.TOOL_QUALITY)
                    : 0))
            .setEnchantmentlvl((byte) 3)
            .setEbfGasRecipeTimeMultiplier(
                orDefault(ml.getProperty(GTMaterialProperties.EBF_GAS_TIME_MULTIPLIER), -1.0))
            .setEbfGasRecipeConsumedAmountMultiplier(
                orDefault(ml.getProperty(GTMaterialProperties.EBF_GAS_AMOUNT_MULTIPLIER), 1.0))
            .setToxic(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.TOXIC)))
            .setRadioactive(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.IS_RADIOACTIVE)))
            .setBlastFurnace(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.BLAST_REQUIRED)))
            .setElektrolysis(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE)))
            .setCentrifuge(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE)))
            .setGas(Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_GAS)));
        Integer tierEU = ml.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if (tierEU != null) stats.setProcessingMaterialTierEU(tierEU);
        if (Boolean.FALSE.equals(ml.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES)))
            stats.disableAutoGeneratedBlastFurnaceRecipes();
        if (Boolean.FALSE.equals(ml.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES)))
            stats.disableAutoGeneratedVacuumFreezerRecipes();

        int mixCircuit = orDefault(ml.getProperty(GTMaterialProperties.MIX_CIRCUIT), -1);
        Werkstoff.GenerationFeatures features = new Werkstoff.GenerationFeatures().disable();
        for (String prefixName : orEmptyList(ml.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES))) {
            features.addPrefix(OrePrefixes.getPrefix(prefixName));
        }
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.ENFORCE_ORE_DICT_UNIFICATION)))
            features.enforceUnification();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_CHEMICAL_RECIPE)))
            features.addChemicalRecipes();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_METAL_CRAFTING_SOLIDIFIER_RECIPE)))
            features.addMetalCraftingSolidifierRecipes();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_METAL_SOLIDIFIER_RECIPE)))
            features.addMetaSolidifierRecipes();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_SIFTER_RECIPE))) features.addSifterRecipes();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_MIXER_RECIPE))) {
            if (mixCircuit >= 1) features.addMixerRecipes((short) mixCircuit);
            else features.addMixerRecipes();
        }

        LinkedHashSet<Pair<ISubTagContainer, Integer>> contents = new LinkedHashSet<>();
        if (GT_ELEMENT_CONTENT_NAMES.contains(ml.getName())) {
            contents.add(Pair.of(requireMaterials(ml.getName(), ml.getName()), 1));
        } else if (!CONTENTS_EXCLUDED_WERKSTOFFE.contains(ml.getName())) {
            for (MaterialRefStack stack : orEmptyList(ml.getProperty(GTMaterialProperties.COMPOSITION))) {
                contents.add(
                    Pair.of(
                        resolveContentRef(
                            stack.material()
                                .name(),
                            ml.getName(),
                            built,
                            byName,
                            pending,
                            inProgress),
                        (int) stack.amount()));
            }
        }
        // Mirrors the public constructors' extension rule: a single Materials content of amount 1 marks the
        // werkstoff as an extension of that material.
        if (contents.size() == 1) {
            Pair<ISubTagContainer, Integer> first = contents.iterator()
                .next();
            if (first.getValue() == 1 && first.getKey() instanceof Materials) features.setExtension();
        }

        List<SubTag> subTags = new ArrayList<>();
        for (String tagName : orEmptyList(ml.getProperty(GTMaterialProperties.SUB_TAGS))) {
            subTags.add(SubTag.getNewSubTag(tagName));
        }

        Werkstoff werkstoff = new Werkstoff(
            rgba,
            ml.getProperty(GTMaterialProperties.LOCAL_NAME),
            orDefault(ml.getProperty(GTMaterialProperties.FORMULA), ""),
            Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.FORMULA_LOCALIZED)),
            stats,
            Werkstoff.Types.valueOf(ml.getProperty(GTMaterialProperties.WERKSTOFF_TYPE)),
            features,
            ids.get(0),
            LegacyMaterials.iconSetOf(ml),
            new ArrayList<>(),
            contents,
            subTags,
            List.of(),
            ownerOf(ml.getProperty(GTMaterialProperties.WERKSTOFF_POOL)));

        // Byproducts may self-reference (padding), so they resolve after construction.
        List<ISubTagContainer> byProducts = werkstoff.getRawOreByProducts();
        for (MaterialRefStack stack : orEmptyList(ml.getProperty(GTMaterialProperties.ORE_BYPRODUCTS))) {
            byProducts.add(
                resolveByproductRef(
                    stack.material()
                        .name(),
                    ml.getName(),
                    werkstoff,
                    built,
                    byName,
                    pending,
                    inProgress));
        }

        byName.put(ml.getName(), werkstoff);
        inProgress.remove(ml.getName());
        for (int id : ids) {
            if (id == werkstoff.getmID()) built.put(id, werkstoff);
            else {
                Werkstoff.registerAdditionalId(werkstoff, id);
                built.put(id, werkstoff);
            }
        }
    }

    private static <T> List<T> orEmptyList(List<T> value) {
        return value != null ? value : List.of();
    }

    private static int orDefault(Integer value, int fallback) {
        return value != null ? value : fallback;
    }

    private static long orDefault(Long value, long fallback) {
        return value != null ? value : fallback;
    }

    private static float orDefault(Float value, float fallback) {
        return value != null ? value : fallback;
    }

    private static double orDefault(Double value, double fallback) {
        return value != null ? value : fallback;
    }

    private static String orDefault(String value, String fallback) {
        return value != null ? value : fallback;
    }

    /// Resolves a canonical material reference (contents or byproducts): a werkstoff-backed name resolves to
    /// its `Werkstoff` (building it on demand), everything else to the legacy `Materials` constant -- except
    /// [#GT_ELEMENT_CONTENT_NAMES], whose legacy reference registry was always `Materials`.
    private static ISubTagContainer resolveContentRef(String name, String ownName, Map<Integer, Werkstoff> built,
        Map<String, Werkstoff> byName, Map<String, Material> pending, LinkedHashSet<String> inProgress) {
        if (GT_ELEMENT_CONTENT_NAMES.contains(name)) {
            return requireMaterials(ownName, name);
        }
        if (pending.containsKey(name)) {
            Werkstoff resolved = byName.get(name);
            if (resolved == null) {
                build(pending.get(name), built, byName, pending, inProgress);
                resolved = byName.get(name);
            }
            return resolved;
        }
        return requireMaterials(ownName, name);
    }

    /// Resolves a [GTMaterialProperties#ORE_BYPRODUCTS] reference for a werkstoff's byproduct list: a
    /// self-reference resolves to the werkstoff itself (the legacy constructors' self-padding convention),
    /// a [#MATERIALS_BYPRODUCT_REFS] pin to the `Materials` constant, everything else per
    /// [#resolveContentRef].
    private static ISubTagContainer resolveByproductRef(String name, String ownName, Werkstoff self,
        Map<Integer, Werkstoff> built, Map<String, Werkstoff> byName, Map<String, Material> pending,
        LinkedHashSet<String> inProgress) {
        if (name.equals(ownName)) return self;
        if (MATERIALS_BYPRODUCT_REFS.contains(ownName + ":" + name)) return requireMaterials(ownName, name);
        return resolveContentRef(name, ownName, built, byName, pending, inProgress);
    }

    private static Materials requireMaterials(String ownName, String name) {
        Materials material = Materials.get(name);
        if (material == null || material == Materials._NULL) {
            throw new IllegalStateException("Werkstoff " + ownName + " references unknown legacy material " + name);
        }
        return material;
    }

    /// The legacy `Werkstoff#getOwner` value ("added by" attribution): the declaring mod's display name, or
    /// null for bartworks' own pools -- reconstruction runs during bartworks' preInit, so
    /// `Loader#activeModContainer` can no longer distinguish the pools.
    private static String ownerOf(String pool) {
        return switch (pool) {
            case "goodgenerator" -> GREEN + "Good Generator";
            case "gtnhlanth", "gtnhlanth-bot" -> GREEN + "GTNH: Lanthanides";
            default -> null;
        };
    }
}
