package bartworks.system.material;

import static net.minecraft.util.EnumChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
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
import gregtech.api.material.GTWerkstoffFlag;
import gregtech.api.material.WerkstoffRefStack;
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
/// Contents/byproduct references resolve against the registry the legacy declaration used (see
/// `WerkstoffRefStack#werkstoff`); a werkstoff-kind byproduct naming this material itself resolves to the
/// instance under construction (byproduct self-padding). `bridgeMaterial` stays null here --
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
        EnumSet<GTWerkstoffFlag> flags = orEmpty(ml.getProperty(GTMaterialProperties.WERKSTOFF_FLAGS));

        int argb = ml.getProperty(GTMaterialProperties.ARGB);
        short[] rgba = { (short) (argb >> 16 & 0xFF), (short) (argb >> 8 & 0xFF), (short) (argb & 0xFF), 0 };

        Werkstoff.Stats stats = new Werkstoff.Stats()
            .setMeltingPoint(ml.getProperty(GTMaterialProperties.MELTING_POINT))
            .setBoilingPoint(
                COLLAPSED_BOILING_POINT_WERKSTOFFE.contains(ml.getName())
                    ? ml.getProperty(GTMaterialProperties.BOILING_POINT)
                    : 0)
            .setProtons(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_PROTONS), 0L))
            .setNeutrons(0)
            .setMass(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_MASS), 0L))
            .setMeltingVoltage(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_MELTING_VOLTAGE), 120))
            .setDurOverride(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_DURABILITY_OVERRIDE), 0))
            .setSpeedOverride(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_SPEED_OVERRIDE), 0f))
            .setQualityOverride(
                (byte) (int) orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_QUALITY_OVERRIDE), 0))
            .setEnchantmentlvl((byte) 3)
            .setEbfGasRecipeTimeMultiplier(
                orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_EBF_GAS_TIME_MULTIPLIER), -1.0))
            .setEbfGasRecipeConsumedAmountMultiplier(
                orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_EBF_GAS_AMOUNT_MULTIPLIER), 1.0))
            .setSublimation(flags.contains(GTWerkstoffFlag.SUBLIMATION))
            .setToxic(flags.contains(GTWerkstoffFlag.TOXIC))
            .setRadioactive(flags.contains(GTWerkstoffFlag.RADIOACTIVE))
            .setBlastFurnace(flags.contains(GTWerkstoffFlag.BLAST_FURNACE))
            .setElektrolysis(flags.contains(GTWerkstoffFlag.ELECTROLYSIS))
            .setCentrifuge(flags.contains(GTWerkstoffFlag.CENTRIFUGE))
            .setGas(flags.contains(GTWerkstoffFlag.GAS));
        stats.setDurMod(orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_DURABILITY_MODIFIER), 1.0f));
        Integer tierEU = ml.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if (tierEU != null) stats.setProcessingMaterialTierEU(tierEU);
        if (flags.contains(GTWerkstoffFlag.NO_AUTO_BLAST_FURNACE_RECIPES))
            stats.disableAutoGeneratedBlastFurnaceRecipes();
        if (flags.contains(GTWerkstoffFlag.NO_AUTO_VACUUM_FREEZER_RECIPES))
            stats.disableAutoGeneratedVacuumFreezerRecipes();

        int mixCircuit = orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_MIX_CIRCUIT), -1);
        Werkstoff.GenerationFeatures features = new Werkstoff.GenerationFeatures().disable();
        for (String prefixName : orEmptyList(ml.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES))) {
            features.addPrefix(OrePrefixes.getPrefix(prefixName));
        }
        if (flags.contains(GTWerkstoffFlag.ENFORCE_UNIFICATION)) features.enforceUnification();
        if (flags.contains(GTWerkstoffFlag.CHEMICAL_SYNTHESIS)) features.addChemicalRecipes();
        if (flags.contains(GTWerkstoffFlag.METAL_CRAFTING_SOLIDIFICATION)) features.addMetalCraftingSolidifierRecipes();
        if (flags.contains(GTWerkstoffFlag.METAL_SOLIDIFICATION)) features.addMetaSolidifierRecipes();
        if (flags.contains(GTWerkstoffFlag.SIFTING)) features.addSifterRecipes();
        if (flags.contains(GTWerkstoffFlag.MIXING)) {
            if (mixCircuit >= 1) features.addMixerRecipes((short) mixCircuit);
            else features.addMixerRecipes();
        }

        LinkedHashSet<Pair<ISubTagContainer, Integer>> contents = new LinkedHashSet<>();
        for (WerkstoffRefStack stack : orEmptyList(ml.getProperty(GTMaterialProperties.WERKSTOFF_CONTENTS))) {
            contents.add(
                Pair.of(
                    resolveRef(stack, ml.getName(), null, built, byName, pending, inProgress),
                    (int) stack.amount()));
        }
        // Mirrors the public constructors' extension rule: a single Materials content of amount 1 marks the
        // werkstoff as an extension of that material.
        if (contents.size() == 1) {
            Pair<ISubTagContainer, Integer> first = contents.iterator()
                .next();
            if (first.getValue() == 1 && first.getKey() instanceof Materials) features.setExtension();
        }

        List<SubTag> subTags = new ArrayList<>();
        for (String tagName : orEmptyList(ml.getProperty(GTMaterialProperties.WERKSTOFF_SUB_TAGS))) {
            subTags.add(SubTag.getNewSubTag(tagName));
        }

        Werkstoff werkstoff = new Werkstoff(
            rgba,
            ml.getProperty(GTMaterialProperties.LOCAL_NAME),
            orDefault(ml.getProperty(GTMaterialProperties.WERKSTOFF_FORMULA), ""),
            flags.contains(GTWerkstoffFlag.LOCALIZED_FORMULA),
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
        for (WerkstoffRefStack stack : orEmptyList(ml.getProperty(GTMaterialProperties.WERKSTOFF_ORE_BYPRODUCTS))) {
            byProducts.add(resolveRef(stack, ml.getName(), werkstoff, built, byName, pending, inProgress));
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

    private static EnumSet<GTWerkstoffFlag> orEmpty(EnumSet<GTWerkstoffFlag> value) {
        return value != null ? value : EnumSet.noneOf(GTWerkstoffFlag.class);
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

    private static ISubTagContainer resolveRef(WerkstoffRefStack stack, String ownName, Werkstoff self,
        Map<Integer, Werkstoff> built, Map<String, Werkstoff> byName, Map<String, Material> pending,
        LinkedHashSet<String> inProgress) {
        String name = stack.material()
            .name();
        if (stack.werkstoff()) {
            if (name.equals(ownName)) {
                if (self == null) throw new IllegalStateException(
                    "Werkstoff " + ownName + " has a self-referencing contents entry, which no declaration produced");
                return self;
            }
            Werkstoff resolved = byName.get(name);
            if (resolved == null) {
                Material dependency = pending.get(name);
                if (dependency == null)
                    throw new IllegalStateException("Werkstoff " + ownName + " references unknown werkstoff " + name);
                build(dependency, built, byName, pending, inProgress);
                resolved = byName.get(name);
            }
            return resolved;
        }
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
