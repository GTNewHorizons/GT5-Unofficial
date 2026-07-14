package gtPlusPlus.core.material;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.FluidNames;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialAtomics;
import gregtech.api.material.MaterialRefStack;
import gregtech.loaders.materials.LegacyMaterials;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

/// Rebuilds every legacy `gtPlusPlus.core.material.Material` the gtPlusPlus material port carries from
/// MaterialLib data (the gtPlusPlus counterpart of `bartworks.system.material.WerkstoffReconstruction`):
/// the pool declaration classes (`MaterialsAlloy`, `MaterialsElements`, `MaterialsOres`, `MaterialMisc`,
/// `MaterialsFluorides`, `MaterialsNuclides`) initialize every `new Material(...)`-declared field via [#byName],
/// building each material (and its composition dependencies) on first request.
///
/// Fields sourced from a `MaterialUtils#generateMaterialFromGtENUM(Materials.X)`-declared pool field are
/// **not** reconstructed here -- that legacy factory already passes fully concrete values (the live gregtech
/// `Materials` constant's own rgba/melting/boiling/protons/neutrons/durability/texture set) into the ordinary
/// constructor, never the `-1`/`null` sentinels that trigger a heuristic, so it is already exactly as
/// pinned-data-driven as reconstruction would be; leaving those declarations untouched is equivalent, not a
/// gap.
///
/// [#RECONSTRUCTED_NAMES] is the resulting set of names reconstruction actually owns -- necessarily an
/// explicit list rather than "every MaterialLib material carrying a [GTMaterialProperties#GTPP_STATE]
/// property", because carrying that property is not enough to tell the two cases apart: 21 of these 203
/// names (e.g. `Zirconium`, `Ammonium`, `Thorium232`) are *also* the pool's own facade for a same-named
/// gregtech element that the port folded onto the same MaterialLib material, so `Materials.get(name) != null`
/// is true for them too. A composition reference outside this set resolves through
/// `MaterialUtils#generateMaterialFromGtENUM` instead -- the same lookup (and result cache) the referencing
/// pool field would have used directly had it named the gregtech material inline instead of through a
/// composite.
public final class MaterialReconstruction {

    // spotless:off
    private static final Set<String> RECONSTRUCTED_NAMES = Set.of(
        "AbyssalAlloy", "AceticAnhydride", "AdvancedNitinol", "AgarditeCd", "AgarditeLa", "AgarditeNd", "AgarditeY",
        "Alburnite", "Ammonia", "Ammonium", "AmmoniumBifluoride", "AmmoniumTetrafluoroberyllate", "AncientGranite",
        "Arcanite", "ArceusAlloy2B", "AstralTitanium", "BabbitAlloy", "BariteRa", "BerylliumFluoride",
        "BerylliumHydroxide", "BlackMetal", "BlackTitanium", "BloodSteel", "Botmium", "Brine", "Bromine",
        "BurntReactorFuelI", "BurntReactorFuelII", "Californium", "CelestialTungsten", "Cerite", "ChloroaceticAcid",
        "ChloroaceticMixture", "ChromaticGlass", "CinobiteA243", "Comancheite", "CopperIISulfate",
        "CopperIISulfatePentahydrate", "Crocoite", "CryoliteF", "Curium", "CyanoaceticAcid", "CyanoacrylatePolymer",
        "DemicheleiteBr", "DichloroaceticAcid", "Dragonblood", "Dysprosium", "EglinSteel", "EglinSteelBaseCompound",
        "EnergyCrystal", "EthylCyanoacetate", "EthylCyanoacrylateSuperGlue", "Fermium", "Florencite", "Fluorcaphite",
        "FluorinatedUraniumHexafluorideFUF6", "FluorineSpargedTBSalt", "FluorineSpargedTSalt", "FluoriteF", "Force",
        "GadoliniteCe", "GadoliniteY", "Geikielite", "Germanium", "Greenockite", "Grisium", "HG1223", "HS188A", "Hafnium",
        "HastelloyC276", "HastelloyN", "HastelloyW", "HastelloyX", "HeLiCoPtEr", "HeliumSpargedUSalt", "Hibonite",
        "Honeaite", "HydrogenChlorideMix", "HydrogenCyanide", "Hypogen", "Incoloy020", "IncoloyDS", "IncoloyMA956",
        "Inconel625", "Inconel690", "Inconel792", "Indalloy140", "Iodine", "Irarsite", "Kashinite", "Koboldite", "Krypton",
        "LFTRFuel1", "LFTRFuel2", "LFTRFuel3", "LFTRFuelBase", "LafiumCompound", "Lafossaite", "LanthaniteCe",
        "LanthaniteLa", "LanthaniteNd", "Laurenium", "Lautarite", "Lepersonnite", "Lithium7", "LithiumFluoride",
        "LithiumTetrafluoroberyllateLFTB", "MaragingSteel250", "MaragingSteel300", "MaragingSteel350", "Miessiite",
        "MutatedLivingSolder", "Neon", "Neptunium", "NeptuniumHexafluoride", "Nichromite", "NiobiumCarbide", "Nitinol60",
        "Octiron", "Perroudite", "PhosphorousUraniumHexafluoridePUF6", "Pikyonium64B", "Plutonium238", "Polonium",
        "Polycrase", "PotassiumNitrate", "Potin", "Protactinium", "Quantum", "RadioactiveMineralMix", "Radium",
        "RareEarthI", "RareEarthII", "RareEarthIII", "Rhenium", "Rhodium", "Rhugnor", "Runite", "Ruthenium", "SaltWater",
        "SamarskiteY", "SamarskiteYb", "SeleniousAcid", "Selenium", "SeleniumDioxide", "SeleniumHexafluoride",
        "SiliconCarbide", "SodiumChloride", "SodiumCyanide", "SodiumFluoride", "SodiumNitrate", "SolarSaltCold",
        "SolarSaltHot", "SolidAcidCatalystMixture", "Staballoy", "StableMoltenSaltBase", "Stellite", "StrontiumHydroxide",
        "StrontiumOxide", "Talonite", "Tantalloy60", "Tantalloy61", "TantalumCarbide", "Technetium",
        "TechnetiumHexafluoride", "Tellurium", "Thallium", "Thorium", "Thorium232",
        "ThoriumBerylliumDepletedMoltenSaltTBSalt", "ThoriumDepletedMoltenSaltTSalt", "ThoriumHexafluoride",
        "ThoriumTetrafluoride", "Titanite", "Titansteel", "TrichloroaceticAcid", "TriniumNaquadahAlloy",
        "TriniumNaquadahCarbonite", "TriniumTitaniumAlloy", "Tumbaga", "TungstenTitaniumCarbide", "Uranium232",
        "Uranium233", "UraniumDepletedMoltenSaltUSalt", "UraniumHexafluoride", "UraniumTetrafluoride", "Water",
        "WatertightSteel", "Wood'sGlass", "Xenon", "Xenotime", "Yttriaite", "Yttrialite", "Yttrocerite", "Zeron100",
        "Zimbabweite", "Zircon", "Zirconium", "ZirconiumCarbide", "ZirconiumTetrafluoride", "Zirconolite", "Zircophyllite",
        "Zirkelite");
    // spotless:on

    /// Part prefixes cut over from the legacy per-material `Base*` item to the MaterialLib shape so far -- see
    /// [#isPartCutOver]. Grows commit by commit as each part family's MaterialLib shape membership and Postea
    /// migration land (block-kind prefixes still need a placed-block migration and an identity-reference sweep);
    /// a prefix absent here still resolves through the legacy path even for a reconstructed material.
    /// `cell`/`cellPlasma` resolve through [#cellStack] rather than plain `MU.stack` -- see its javadoc.
    // spotless:off
    private static final Set<OrePrefixes> CUT_OVER_PART_PREFIXES = Set.of(
        OrePrefixes.dust, OrePrefixes.dustSmall, OrePrefixes.dustTiny, OrePrefixes.ingot, OrePrefixes.ingotHot,
        OrePrefixes.nugget, OrePrefixes.plate, OrePrefixes.plateDouble, OrePrefixes.plateDense,
        OrePrefixes.plateSuperdense, OrePrefixes.gearGt, OrePrefixes.gearGtSmall, OrePrefixes.bolt,
        OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.foil, OrePrefixes.wireFine,
        OrePrefixes.spring, OrePrefixes.springSmall, OrePrefixes.stick, OrePrefixes.stickLong,
        OrePrefixes.crushed, OrePrefixes.crushedCentrifuged, OrePrefixes.crushedPurified, OrePrefixes.dustImpure,
        OrePrefixes.dustPure, OrePrefixes.milled, OrePrefixes.rawOre, OrePrefixes.cell, OrePrefixes.cellPlasma);
    // spotless:on

    /// Reconstructed materials whose `block` part stays legacy-canonical despite otherwise clearing
    /// [#CUT_OVER_PART_PREFIXES]'s bar: each has its own client-side render hook
    /// (`gtPlusPlus.core.handler.events.AnimatedBlockTextureHandler`) that forces icon-cycle synchronization on
    /// the legacy `BlockBaseModular` instance specifically -- MaterialLib's `GTStorageShapeBlock` has no
    /// equivalent per-material animated-icon path, so cutting these over would silently stop the animation
    /// on the block a player actually sees in world (the handler would keep animating an instance nobody looks
    /// at).
    private static final Set<String> BLOCK_CUTOVER_EXCLUDED = Set
        .of("AstralTitanium", "CelestialTungsten", "ChromaticGlass", "Hypogen");

    private static final Map<String, Material> built = new HashMap<>();
    private static final LinkedHashSet<String> inProgress = new LinkedHashSet<>();

    private MaterialReconstruction() {}

    public static synchronized Material byName(String unlocalizedName) {
        if (!RECONSTRUCTED_NAMES.contains(unlocalizedName)) {
            throw new IllegalStateException(
                "MaterialReconstruction.byName called for a name reconstruction does not own: " + unlocalizedName);
        }
        return resolve(unlocalizedName);
    }

    /// Whether `name` is a reconstructed gtPlusPlus material -- the item/fluid/block cutover gate: legacy
    /// per-material item/block construction (`MaterialGenerator`) and part resolution
    /// (`Material#getComponentByPrefix`) skip their legacy path for these names in favor of MaterialLib, while
    /// third-party runtime-constructed materials (unknown to this class) keep the legacy path unconditionally.
    public static boolean isReconstructed(String name) {
        return RECONSTRUCTED_NAMES.contains(name);
    }

    /// Whether a reconstructed material's `prefix` part has cut over to MaterialLib -- see
    /// [#CUT_OVER_PART_PREFIXES]. `MaterialGenerator` skips legacy construction, and
    /// `Material#getComponentByPrefix` resolves through MaterialLib instead of the legacy path, exactly when
    /// this is true.
    public static boolean isPartCutOver(String name, OrePrefixes prefix) {
        if (prefix == OrePrefixes.block) return isBlockCutOver(name);
        return RECONSTRUCTED_NAMES.contains(name) && CUT_OVER_PART_PREFIXES.contains(prefix);
    }

    /// Whether a reconstructed material's storage block (`BlockBaseModular` `BlockTypes.STANDARD`) has cut
    /// over to MaterialLib's [gregtech.api.enums.materials2.Materials2BlockShapes#block] -- block-kind is
    /// handled separately from [#CUT_OVER_PART_PREFIXES] because the cutover happens inside
    /// `BlockBaseModular`'s own constructor (the legacy instance still gets built and registered for every
    /// material, cut over or not -- see that class), not by skipping construction the way item parts do. See
    /// [#BLOCK_CUTOVER_EXCLUDED] for the materials this stays false for regardless.
    public static boolean isBlockCutOver(String name) {
        return RECONSTRUCTED_NAMES.contains(name) && !BLOCK_CUTOVER_EXCLUDED.contains(name);
    }

    /// Whether `prefix` is one of the part families [#CUT_OVER_PART_PREFIXES] has cleared for cutover at all,
    /// independent of any specific material -- the allow-list [Material#getComponentByPrefix] consults for
    /// materials backed by a live gregtech `Materials` constant instead of gtpp reconstruction (a name outside
    /// [#RECONSTRUCTED_NAMES] that nonetheless picked up a MaterialLib shape for `prefix` from the gtpp
    /// name-merge, e.g. milled ore for Sphalerite). Excludes block-kind and cell/cellPlasma prefixes for
    /// the same reason [#CUT_OVER_PART_PREFIXES] does: those need their own dedicated cutover work (identity-
    /// reference sweep, fluid-in-container membership) regardless of which material asks.
    public static boolean isPrefixEligibleForCutover(OrePrefixes prefix) {
        return CUT_OVER_PART_PREFIXES.contains(prefix);
    }

    /// The MaterialLib material backing a reconstructed name, or null if `name` is not reconstructed. Callers
    /// that already know `isReconstructed(name)` is true (e.g. after the [#isReconstructed] gate) can treat a
    /// null result as "MaterialLib data disappeared after material construction", which should not happen.
    public static com.ruling_0.materiallib.api.Material materialLibOf(String name) {
        if (!RECONSTRUCTED_NAMES.contains(name)) return null;
        return MaterialLibAPI.getMaterial("gregtech", name);
    }

    /// The MaterialLib stack for a reconstructed material's `cell`/`cellPlasma` slot, or null if it has none.
    /// Unlike every other cut-over prefix, `cell`/`cellPlasma` cannot resolve through plain `MU.stack`: a gtpp
    /// material's single fluid may have claimed `Materials2FluidShapes.fluidMolten` instead of the
    /// liquid/gas slots `Materials2CellShapes.cell` requires (every `SOLID`- and `LIQUID`/`PURE_LIQUID`-
    /// state material, whose legacy fluid was always registered `molten.<name>` -- see
    /// `scripts/mu/gen_materials.py`'s `gtpp_fluid_and_cell_shape_lines`), in which case its `cell` item is
    /// `cellMolten` instead. `cellPlasma` always resolves to the single `cellPlasmaLight` candidate
    /// gtpp claims (never gregtech's 144 mB `cellPlasma`), so it needs no fallback of its own, but is
    /// routed through this method too for a single call site.
    public static @Nullable ItemStack cellStack(String name, OrePrefixes prefix, long amount) {
        com.ruling_0.materiallib.api.Material ml = materialLibOf(name);
        if (ml == null) return null;
        if (prefix == OrePrefixes.cell) {
            ItemStack stack = MU.stack(OrePrefixes.cell, ml, amount);
            return stack != null ? stack : MU.stack(OrePrefixes.cellMolten, ml, amount);
        }
        if (prefix == OrePrefixes.cellPlasma) {
            return MU.stack(OrePrefixes.cellPlasma, ml, amount);
        }
        return null;
    }

    /// Resolves a name to a `Material`, whether or not reconstruction owns it -- the composition-reference
    /// counterpart of [#byName]. See the class javadoc for why ownership (not `GTPP_STATE` property presence)
    /// is the deciding signal.
    private static synchronized Material resolve(String name) {
        Material already = built.get(name);
        if (already != null) return already;
        if (!RECONSTRUCTED_NAMES.contains(name)) {
            Materials gt = Materials.get(name);
            if (gt == null || gt == Materials._NULL) {
                throw new IllegalStateException("gtPlusPlus material composition references unknown material " + name);
            }
            return generateGtEnum(name, gt);
        }
        return build(name);
    }

    /// `MaterialUtils#generateMaterialFromGtENUM` with the same custom rgb/texture-set arguments the
    /// `MaterialsElements` pool declaration for `name` passes. The factory caches by name and the first call
    /// wins, so a composition reference resolved here before the pool field initializes must pass identical
    /// overrides or the pool field would silently receive a default-textured instance (legacy never had this
    /// hazard -- its `MaterialStack` declarations referenced the pool fields directly, class-loading
    /// `MaterialsElements` first).
    private static Material generateGtEnum(String name, Materials gt) {
        return switch (name) {
            case "InfusedAir", "InfusedFire", "InfusedEarth", "InfusedWater", "InfusedEntropy", "InfusedOrder" -> MaterialUtils
                .generateMaterialFromGtENUM(gt, TextureSet.SET_GEM_A);
            case "Magic" -> MaterialUtils.generateMaterialFromGtENUM(gt, new short[] { 10, 185, 140 });
            case "Strontium" -> MaterialUtils
                .generateMaterialFromGtENUM(gt, new short[] { 230, 210, 110 }, TextureSet.SET_FLINT);
            default -> MaterialUtils.generateMaterialFromGtENUM(gt);
        };
    }

    private static Material build(String name) {
        if (!inProgress.add(name)) {
            throw new IllegalStateException("gtPlusPlus material composition reference cycle: " + inProgress);
        }

        com.ruling_0.materiallib.api.Material ml = MaterialLibAPI.getMaterial("gregtech", name);
        String gtppState = ml == null ? null : ml.getProperty(GTMaterialProperties.GTPP_STATE);
        if (gtppState == null) {
            throw new IllegalStateException("No MaterialLib gtpp data for reconstructed material " + name);
        }

        int argb = ml.getProperty(GTMaterialProperties.ARGB);
        short[] rgba = { (short) (argb >> 16 & 0xFF), (short) (argb >> 8 & 0xFF), (short) (argb & 0xFF), 0 };
        String localName = ml.getProperty(GTMaterialProperties.LOCAL_NAME);
        TextureSet textureSet = LegacyMaterials.iconSetOf(ml);
        MaterialState state = MaterialState.valueOf(gtppState);

        List<MaterialRefStack> composition = ml.getProperty(GTMaterialProperties.COMPOSITION);
        MaterialStack[] composites;
        if (composition == null || composition.isEmpty()) {
            composites = new MaterialStack[0];
        } else {
            composites = new MaterialStack[composition.size()];
            for (int i = 0; i < composition.size(); i++) {
                MaterialRefStack ref = composition.get(i);
                Material child = resolve(
                    ref.material()
                        .name());
                composites[i] = new MaterialStack(child, (double) ref.amount());
            }
        }

        Material.GtppScalars scalars = gtppScalars(ml);
        String fluidName = gtppFluidName(ml, scalars.generatesFluid());
        String plasmaName = ml.getProperty(GTMaterialProperties.GTPP_PLASMA_NAME);

        Material material = new Material(
            name,
            localName,
            state,
            textureSet,
            rgba,
            scalars,
            fluidName,
            plasmaName,
            composites);

        built.put(name, material);
        inProgress.remove(name);

        Materials gtEquivalent = Material.tryFindGregtechMaterialEquivalent(material);
        if (gtEquivalent != null && gtEquivalent != Materials._NULL) {
            MaterialUtils.seedGeneratedMaterial(gtEquivalent, material);
        }

        return material;
    }

    /// The legacy gtpp fluid name for `ml`'s non-plasma fluid, or null when `generatesFluid` is false --
    /// [GTMaterialProperties#LEGACY_FLUIDS]'s molten/fluid/gas slot reliably reconstructs it (unlike the
    /// plasma slot, see [GTMaterialProperties#GTPP_PLASMA_NAME]) only when gtpp actually generated a fluid,
    /// since a material with no gtpp fluid of its own can still carry a `LEGACY_FLUIDS` populated entirely by
    /// its gregtech/werkstoff side.
    private static String gtppFluidName(com.ruling_0.materiallib.api.Material ml, boolean generatesFluid) {
        if (!generatesFluid) return null;
        FluidNames legacyFluids = ml.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
        return legacyFluids != null ? legacyFluids.legacyGtppFluidName() : null;
    }

    /// Assembles [Material.GtppScalars] for `ml` from the decomposed `GTMaterialProperties#GTPP_*`
    /// properties, resolving each QUIRK property's fallback to its canonical counterpart (see each property's
    /// javadoc) and each elided-default property back to the default it was elided for.
    private static Material.GtppScalars gtppScalars(com.ruling_0.materiallib.api.Material ml) {
        int meltingPointK = ml.getProperty(GTMaterialProperties.MELTING_POINT);
        int boilingPointK = ml.getProperty(GTMaterialProperties.BOILING_POINT);

        int durability = ml.getProperty(GTMaterialProperties.DURABILITY);

        boolean usesBlastFurnace = Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.BLAST_REQUIRED));

        return new Material.GtppScalars(
            orDefault(ml.getProperty(GTMaterialProperties.TIER), 0),
            orDefault(ml.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER), 16L),
            meltingPointK,
            boilingPointK,
            durability,
            usesBlastFurnace,
            Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.IS_RADIOACTIVE)),
            orDefault(ml.getProperty(GTMaterialProperties.RADIATION_LEVEL), 0),
            orDefault(ml.getProperty(GTMaterialProperties.FORMULA), ""),
            MaterialAtomics.protons(ml),
            MaterialAtomics.neutrons(ml),
            Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.GTPP_GENERATES_FLUID)),
            Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.GTPP_GENERATES_CELLS)));
    }

    private static int orDefault(Integer value, int fallback) {
        return value != null ? value : fallback;
    }

    private static long orDefault(Long value, long fallback) {
        return value != null ? value : fallback;
    }

    private static String orDefault(String value, String fallback) {
        return value != null ? value : fallback;
    }
}
