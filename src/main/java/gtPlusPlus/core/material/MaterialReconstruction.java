package gtPlusPlus.core.material;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.GTppData;
import gregtech.api.material.MaterialRefStack;
import gregtech.loaders.materials.LegacyMaterials;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

/// Rebuilds every legacy `gtPlusPlus.core.material.Material` the gtPlusPlus material port carries from
/// MaterialLib data (the stage-11 counterpart of stage 10's `bartworks.system.material.WerkstoffReconstruction`):
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
/// explicit list rather than "every MaterialLib material carrying a [GTMaterialProperties#GTPP] property",
/// because carrying that property is not enough to tell the two cases apart: 21 of these 203 names (e.g.
/// `Zirconium`, `Ammonium`, `Thorium232`) are *also* the pool's own facade for a same-named gregtech element
/// that the stage-11 commit-2 merge folded onto the same MaterialLib material, so `Materials.get(name) != null`
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

    /// Resolves a name to a `Material`, whether or not reconstruction owns it -- the composition-reference
    /// counterpart of [#byName]. See the class javadoc for why ownership (not `GTPP` property presence) is
    /// the deciding signal.
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
        GTppData data = ml == null ? null : ml.getProperty(GTMaterialProperties.GTPP);
        if (data == null) {
            throw new IllegalStateException("No MaterialLib gtpp data for reconstructed material " + name);
        }

        int argb = ml.getProperty(GTMaterialProperties.ARGB);
        short[] rgba = { (short) (argb >> 16 & 0xFF), (short) (argb >> 8 & 0xFF), (short) (argb & 0xFF), 0 };
        String localName = ml.getProperty(GTMaterialProperties.LOCAL_NAME);
        TextureSet textureSet = LegacyMaterials.iconSetOf(ml);
        MaterialState state = MaterialState.valueOf(data.state());

        List<MaterialRefStack> composition = data.composition();
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

        Material material = new Material(name, localName, state, textureSet, rgba, data, composites);

        built.put(name, material);
        inProgress.remove(name);

        Materials gtEquivalent = Material.tryFindGregtechMaterialEquivalent(material);
        if (gtEquivalent != null && gtEquivalent != Materials._NULL) {
            MaterialUtils.seedGeneratedMaterial(gtEquivalent, material);
        }

        return material;
    }
}
