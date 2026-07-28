package gregtech.common.misc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/// Per-material values that only [MaterialDataDump] reads, keyed by MaterialLib registration name.
///
/// These are dumped for the material unification tooling, which verifies the ported material data against the
/// pinned bartworks and gtPlusPlus dumps. No recipe generator or gameplay path consults them, so they live
/// here rather than as [gregtech.api.material.GTMaterialProperties] keys, keeping every property key on a
/// material one that a live reader depends on.
public final class MaterialDumpData {

    private MaterialDumpData() {}

    /// The bartworks-side pool a werkstoff-originated material was declared in.
    private static final Map<String, String> WERKSTOFF_POOL = buildWerkstoffPool();

    /// Materials whose gtPlusPlus declaration generated cells.
    private static final Set<String> GTPP_GENERATES_CELLS = Set.of(
        "AbyssalAlloy",
        "AceticAnhydride",
        "AdvancedNitinol",
        "Ammonia",
        "Ammonium",
        "AmmoniumBifluoride",
        "AmmoniumTetrafluoroberyllate",
        "Arcanite",
        "ArceusAlloy2B",
        "Argon",
        "AstralTitanium",
        "BabbitAlloy",
        "BerylliumFluoride",
        "BerylliumHydroxide",
        "BlackMetal",
        "BlackTitanium",
        "BloodSteel",
        "Botmium",
        "Bromine",
        "BurntReactorFuelI",
        "BurntReactorFuelII",
        "Californium",
        "CelestialTungsten",
        "Chlorine",
        "ChloroaceticAcid",
        "ChloroaceticMixture",
        "ChromaticGlass",
        "CinobiteA243",
        "Curium",
        "CyanoacrylatePolymer",
        "DichloroaceticAcid",
        "Dragonblood",
        "Dysprosium",
        "EglinSteel",
        "EglinSteelBaseCompound",
        "EnergyCrystal",
        "EthylCyanoacetate",
        "EthylCyanoacrylateSuperGlue",
        "Fermium",
        "FluorinatedUraniumHexafluorideFUF6",
        "Fluorine",
        "FluorineSpargedTBSalt",
        "FluorineSpargedTSalt",
        "Force",
        "Germanium",
        "Grisium",
        "HG1223",
        "HS188A",
        "Hafnium",
        "HastelloyC276",
        "HastelloyN",
        "HastelloyW",
        "HastelloyX",
        "HeLiCoPtEr",
        "Helium",
        "HeliumSpargedUSalt",
        "Hydrogen",
        "HydrogenChlorideMix",
        "HydrogenCyanide",
        "Hypogen",
        "Incoloy020",
        "IncoloyDS",
        "IncoloyMA956",
        "Inconel625",
        "Inconel690",
        "Inconel792",
        "Indalloy140",
        "Iodine",
        "Koboldite",
        "Krypton",
        "LFTRFuel1",
        "LFTRFuel2",
        "LFTRFuel3",
        "LFTRFuelBase",
        "LafiumCompound",
        "Laurenium",
        "LithiumFluoride",
        "LithiumTetrafluoroberyllateLFTB",
        "MaragingSteel250",
        "MaragingSteel300",
        "MaragingSteel350",
        "Mercury",
        "MutatedLivingSolder",
        "Neon",
        "Neptunium",
        "NeptuniumHexafluoride",
        "NiobiumCarbide",
        "Nitinol60",
        "Nitrogen",
        "Octiron",
        "Oxygen",
        "PhosphorousUraniumHexafluoridePUF6",
        "Pikyonium64B",
        "Plutonium238",
        "Polonium",
        "Potin",
        "Protactinium",
        "Quantum",
        "Radium",
        "Radon",
        "Rhenium",
        "Rhodium",
        "Rhugnor",
        "Runite",
        "Ruthenium",
        "SeleniousAcid",
        "Selenium",
        "SeleniumDioxide",
        "SeleniumHexafluoride",
        "SiliconCarbide",
        "Sodium",
        "SodiumFluoride",
        "SolarSaltCold",
        "SolarSaltHot",
        "SolidAcidCatalystMixture",
        "Staballoy",
        "StableMoltenSaltBase",
        "Stellite",
        "Talonite",
        "Tantalloy60",
        "Tantalloy61",
        "TantalumCarbide",
        "Technetium",
        "TechnetiumHexafluoride",
        "Tellurium",
        "Thallium",
        "Thorium",
        "Thorium232",
        "ThoriumBerylliumDepletedMoltenSaltTBSalt",
        "ThoriumDepletedMoltenSaltTSalt",
        "ThoriumHexafluoride",
        "ThoriumTetrafluoride",
        "Titansteel",
        "TrichloroaceticAcid",
        "TriniumNaquadahAlloy",
        "TriniumNaquadahCarbonite",
        "TriniumTitaniumAlloy",
        "Tumbaga",
        "TungstenTitaniumCarbide",
        "Uranium232",
        "Uranium233",
        "UraniumDepletedMoltenSaltUSalt",
        "UraniumHexafluoride",
        "UraniumTetrafluoride",
        "WatertightSteel",
        "Xenon",
        "Zeron100",
        "Zirconium",
        "ZirconiumCarbide",
        "ZirconiumTetrafluoride");

    /// Materials whose gtPlusPlus declaration generated a fluid. Also gates the dumped gtPlusPlus fluid and
    /// plasma names.
    private static final Set<String> GTPP_GENERATES_FLUID = Set.of(
        "AbyssalAlloy",
        "AceticAnhydride",
        "AdvancedNitinol",
        "Aluminium",
        "Americium",
        "Ammonia",
        "Ammonium",
        "AmmoniumBifluoride",
        "AmmoniumTetrafluoroberyllate",
        "AncientGranite",
        "Antimony",
        "Arcanite",
        "ArceusAlloy2B",
        "Argon",
        "Arsenic",
        "AstralTitanium",
        "BabbitAlloy",
        "Barium",
        "Beryllium",
        "BerylliumFluoride",
        "BerylliumHydroxide",
        "Bismuth",
        "BlackMetal",
        "BlackSteel",
        "BlackTitanium",
        "BloodSteel",
        "Boron",
        "Botmium",
        "Brine",
        "Bromine",
        "Bronze",
        "BurntReactorFuelI",
        "BurntReactorFuelII",
        "Caesium",
        "Calcium",
        "Californium",
        "CastIron",
        "CelestialTungsten",
        "Cerium",
        "Chlorine",
        "ChloroaceticAcid",
        "ChloroaceticMixture",
        "ChromaticGlass",
        "Chrome",
        "CinobiteA243",
        "Cobalt",
        "Copper",
        "CopperIISulfate",
        "CopperIISulfatePentahydrate",
        "Curium",
        "CyanoaceticAcid",
        "CyanoacrylatePolymer",
        "DichloroaceticAcid",
        "Dragonblood",
        "Dysprosium",
        "EglinSteel",
        "EglinSteelBaseCompound",
        "EnergyCrystal",
        "Erbium",
        "EthylCyanoacetate",
        "EthylCyanoacrylateSuperGlue",
        "Europium",
        "Fermium",
        "FluorinatedUraniumHexafluorideFUF6",
        "Fluorine",
        "FluorineSpargedTBSalt",
        "FluorineSpargedTSalt",
        "FluoriteF",
        "Force",
        "Gadolinium",
        "Gallium",
        "Germanium",
        "Glowstone",
        "Gold",
        "Grisium",
        "HG1223",
        "HS188A",
        "Hafnium",
        "HastelloyC276",
        "HastelloyN",
        "HastelloyW",
        "HastelloyX",
        "HeLiCoPtEr",
        "Helium",
        "HeliumSpargedUSalt",
        "Holmium",
        "Hydrogen",
        "HydrogenChlorideMix",
        "HydrogenCyanide",
        "Hypogen",
        "Incoloy020",
        "IncoloyDS",
        "IncoloyMA956",
        "Inconel625",
        "Inconel690",
        "Inconel792",
        "Indalloy140",
        "Indium",
        "Invar",
        "Iodine",
        "Iridium",
        "Iron",
        "Kanthal",
        "Koboldite",
        "Krypton",
        "LFTRFuel1",
        "LFTRFuel2",
        "LFTRFuel3",
        "LFTRFuelBase",
        "LafiumCompound",
        "Lanthanum",
        "Laurenium",
        "Lead",
        "Lithium",
        "Lithium7",
        "LithiumFluoride",
        "LithiumTetrafluoroberyllateLFTB",
        "Lutetium",
        "Magic",
        "Magnesium",
        "Manganese",
        "MaragingSteel250",
        "MaragingSteel300",
        "MaragingSteel350",
        "Mercury",
        "Molybdenum",
        "MutatedLivingSolder",
        "Naquadah",
        "NaquadahEnriched",
        "Naquadria",
        "Neodymium",
        "Neon",
        "Neptunium",
        "NeptuniumHexafluoride",
        "Nichrome",
        "Nickel",
        "Niobium",
        "NiobiumCarbide",
        "Nitinol60",
        "Nitrogen",
        "Octiron",
        "Osmiridium",
        "Osmium",
        "Oxygen",
        "Palladium",
        "PhosphorousUraniumHexafluoridePUF6",
        "Pikyonium64B",
        "Plastic",
        "Platinum",
        "Plutonium238",
        "Plutonium241",
        "Polonium",
        "Polytetrafluoroethylene",
        "Potassium",
        "PotassiumNitrate",
        "Potin",
        "Praseodymium",
        "Promethium",
        "Protactinium",
        "Quantum",
        "Radium",
        "Radon",
        "Redstone",
        "Rhenium",
        "Rhodium",
        "Rhugnor",
        "Rubidium",
        "Runite",
        "Ruthenium",
        "Samarium",
        "Scandium",
        "SeleniousAcid",
        "Selenium",
        "SeleniumDioxide",
        "SeleniumHexafluoride",
        "Silicon",
        "SiliconCarbide",
        "Silver",
        "Sodium",
        "SodiumCyanide",
        "SodiumFluoride",
        "SodiumNitrate",
        "SolarSaltCold",
        "SolarSaltHot",
        "SolidAcidCatalystMixture",
        "Staballoy",
        "StableMoltenSaltBase",
        "StainlessSteel",
        "Steel",
        "Stellite",
        "Strontium",
        "StrontiumHydroxide",
        "StrontiumOxide",
        "Talonite",
        "Tantalloy60",
        "Tantalloy61",
        "Tantalum",
        "TantalumCarbide",
        "Technetium",
        "TechnetiumHexafluoride",
        "Tellurium",
        "Terbium",
        "Thallium",
        "Thaumium",
        "Thorium",
        "Thorium232",
        "ThoriumBerylliumDepletedMoltenSaltTBSalt",
        "ThoriumDepletedMoltenSaltTSalt",
        "ThoriumHexafluoride",
        "ThoriumTetrafluoride",
        "Thulium",
        "Tin",
        "Titanium",
        "Titansteel",
        "TrichloroaceticAcid",
        "Trinium",
        "TriniumNaquadahAlloy",
        "TriniumNaquadahCarbonite",
        "TriniumTitaniumAlloy",
        "Tumbaga",
        "Tungsten",
        "TungstenCarbide",
        "TungstenSteel",
        "TungstenTitaniumCarbide",
        "Uranium",
        "Uranium232",
        "Uranium233",
        "Uranium235",
        "UraniumDepletedMoltenSaltUSalt",
        "UraniumHexafluoride",
        "UraniumTetrafluoride",
        "Vanadium",
        "Water",
        "WatertightSteel",
        "Wood'sGlass",
        "Xenon",
        "Ytterbium",
        "Yttrium",
        "Zeron100",
        "Zinc",
        "Zirconium",
        "ZirconiumCarbide");

    /// Materials carrying the werkstoff stickLong/stick/plate solidifier marker.
    private static final Set<String> METAL_CRAFTING_SOLIDIFIER = Set.of(
        "AdamantiumAlloy",
        "AdemicSteel",
        "Artherium-Sn",
        "AtomicSeparationCatalyst",
        "Californium",
        "Dalisenite",
        "EnrichedNaquadahAlloy",
        "ExtremelyUnstableNaquadah",
        "HighDurabilityCompoundSteel",
        "Hikarium",
        "Incoloy-903",
        "Lumiium",
        "MAR-Ce-M200Steel",
        "MAR-M200Steel",
        "MetastableOganesson",
        "PreciousMetalsAlloy",
        "Rhodium",
        "Rhodium-PlatedPalladium",
        "Ruridit",
        "Ruthenium",
        "Shirabon",
        "Signalium",
        "Tairitsu",
        "TanmolyiumBeta-C",
        "Zircaloy-2",
        "Zircaloy-4",
        "Zn-ThAlloy");

    /// Materials carrying the werkstoff screw/gear/gearSmall/bolt/ring/rotor solidifier marker.
    private static final Set<String> METAL_SOLIDIFIER = Set.of(
        "AdamantiumAlloy",
        "AdemicSteel",
        "Artherium-Sn",
        "AtomicSeparationCatalyst",
        "Dalisenite",
        "EnrichedNaquadahAlloy",
        "ExtremelyUnstableNaquadah",
        "HighDurabilityCompoundSteel",
        "Hikarium",
        "Incoloy-903",
        "Lumiium",
        "MAR-Ce-M200Steel",
        "MAR-M200Steel",
        "MetastableOganesson",
        "PreciousMetalsAlloy",
        "Rhodium",
        "Rhodium-PlatedPalladium",
        "Ruridit",
        "Ruthenium",
        "Shirabon",
        "Signalium",
        "Tairitsu",
        "TanmolyiumBeta-C",
        "Zircaloy-2",
        "Zircaloy-4");

    public static String werkstoffPool(String materialName) {
        return WERKSTOFF_POOL.get(materialName);
    }

    public static boolean gtppGeneratesCells(String materialName) {
        return GTPP_GENERATES_CELLS.contains(materialName);
    }

    public static boolean gtppGeneratesFluid(String materialName) {
        return GTPP_GENERATES_FLUID.contains(materialName);
    }

    /// Null rather than `false` when unset, matching the property read it replaces: the pinned dumps carry the
    /// marker only where it was declared.
    public static Boolean hasMetalCraftingSolidifierRecipe(String materialName) {
        return METAL_CRAFTING_SOLIDIFIER.contains(materialName) ? Boolean.TRUE : null;
    }

    /// As [#hasMetalCraftingSolidifierRecipe].
    public static Boolean hasMetalSolidifierRecipe(String materialName) {
        return METAL_SOLIDIFIER.contains(materialName) ? Boolean.TRUE : null;
    }

    private static Map<String, String> buildWerkstoffPool() {
        Map<String, String> m = new HashMap<>(784);
        m.put("1,4-Butanediol", "gtnhlanth");
        m.put("2-Ethyl-1-Hexanol", "goodgenerator");
        m.put("Acid-LeachedBastnasiteRareEarthOxides", "gtnhlanth");
        m.put("AcidNaquadahEmulsion", "goodgenerator");
        m.put("AcidicIridiumSolution", "bartworks");
        m.put("AcidicMonazitePowder", "gtnhlanth");
        m.put("AcidicOsmiumSolution", "bartworks");
        m.put("Adamantine", "goodgenerator");
        m.put("AdamantiumAlloy", "goodgenerator");
        m.put("AdemicSteel", "bartworks");
        m.put("Alumina", "bartworks");
        m.put("AmmoniumChloride", "bartworks");
        m.put("AmmoniumNitrate", "gtnhlanth-bot");
        m.put("AmmoniumNitrateSolution", "gtnhlanth");
        m.put("AntimonyPentachloride", "goodgenerator");
        m.put("AntimonyPentachlorideSolution", "goodgenerator");
        m.put("AntimonyPentafluoride", "goodgenerator");
        m.put("AntimonyPentafluorideSolution", "goodgenerator");
        m.put("AntimonyTrichlorideSolution", "goodgenerator");
        m.put("AquaRegia", "bartworks");
        m.put("Arsenopyrite", "bartworks");
        m.put("Artherium-Sn", "goodgenerator");
        m.put("Atheneite", "bartworks");
        m.put("AtomicSeparationCatalyst", "goodgenerator");
        m.put("BArTiMaEuSNeK", "bartworks");
        m.put("BastnasiteRareEarthOxides", "gtnhlanth");
        m.put("BastnasiteRarerEarthOxideSuspension", "gtnhlanth");
        m.put("BastnasiteRarerEarthOxides", "gtnhlanth");
        m.put("BismuthTelluride", "bartworks");
        m.put("Bismuthinite", "bartworks");
        m.put("Bismutite", "bartworks");
        m.put("Bornite", "bartworks");
        m.put("BoronTrichloride", "gtnhlanth");
        m.put("BoronTrioxide", "gtnhlanth");
        m.put("Calcium", "bartworks");
        m.put("CalciumChloride", "bartworks");
        m.put("CalciumFluoride", "gtnhlanth");
        m.put("Californium", "bartworks");
        m.put("Cerium(III)Oxide", "gtnhlanth");
        m.put("Cerium-OxidisedRareEarthOxides", "gtnhlanth");
        m.put("Cerium-RichMixture", "gtnhlanth");
        m.put("Cerium-dopedLutetiumAluminiumGarnet(CeLuAG)", "gtnhlanth");
        m.put("Cerium-dopedLutetiumAluminiumOxygenBlend", "gtnhlanth");
        m.put("CeriumChloride", "gtnhlanth");
        m.put("CeriumChlorideConcentrate", "gtnhlanth");
        m.put("CeriumDioxide", "gtnhlanth");
        m.put("CeriumExtractingNanoResin", "gtnhlanth");
        m.put("CeriumOxalate", "gtnhlanth");
        m.put("ChlorinatedRareEarthConcentrate", "gtnhlanth");
        m.put("ChlorinatedRareEarthDilutedSolution", "gtnhlanth");
        m.put("ChlorinatedRareEarthEnrichedSolution", "gtnhlanth");
        m.put("Chromo-Alumino-Povondraite", "bartworks");
        m.put("CircuitCompoundMK3", "bartworks");
        m.put("ConcentratedEnriched-NaquadahSludge", "goodgenerator");
        m.put("ConditionedBastnasiteMud", "gtnhlanth");
        m.put("CooledMonaziteRareEarthConcentrate", "gtnhlanth");
        m.put("CrudeRhodiumMetal", "bartworks");
        m.put("CubicZirconia", "bartworks");
        m.put("Cyclopentadiene", "goodgenerator");
        m.put("Dalisenite", "goodgenerator");
        m.put("DephosphatedSamariumConcentrate", "gtnhlanth");
        m.put("Diaminotoluene", "gtnhlanth");
        m.put("Dibismuthhydroborat", "bartworks");
        m.put("Diethylamine", "goodgenerator");
        m.put("DilutedAcetone", "gtnhlanth");
        m.put("DilutedBastnasiteMud", "gtnhlanth");
        m.put("DilutedMonaziteRareEarthMud", "gtnhlanth");
        m.put("DilutedMonaziteSulfate", "gtnhlanth");
        m.put("DilutedSamariumRareEarthSolution", "gtnhlanth");
        m.put("Dinitrotoluene", "gtnhlanth");
        m.put("Djurleite", "bartworks");
        m.put("DriedMonaziteRareEarthConcentrate", "gtnhlanth");
        m.put("DysprosiumChlorideConcentrate", "gtnhlanth");
        m.put("DysprosiumExtractingNanoResin", "gtnhlanth");
        m.put("DysprosiumOreConcentrate", "gtnhlanth");
        m.put("Enriched-Naquadah-RichSolution", "goodgenerator");
        m.put("Enriched-NaquadahOxideMixture", "goodgenerator");
        m.put("Enriched-NaquadahSulphate", "goodgenerator");
        m.put("EnrichedNaquadahAlloy", "goodgenerator");
        m.put("EnrichedNaquadahGoo", "goodgenerator");
        m.put("ErbiumChlorideConcentrate", "gtnhlanth");
        m.put("ErbiumExtractingNanoResin", "gtnhlanth");
        m.put("ErbiumOreConcentrate", "gtnhlanth");
        m.put("EthanolGasoline", "goodgenerator");
        m.put("Ether", "goodgenerator");
        m.put("EuropiumChlorideConcentrate", "gtnhlanth");
        m.put("EuropiumExtractingNanoResin", "gtnhlanth");
        m.put("EuropiumIIIOxide", "gtnhlanth");
        m.put("EuropiumOreConcentrate", "gtnhlanth");
        m.put("EuropiumOxide", "gtnhlanth");
        m.put("EuropiumSulfide", "gtnhlanth");
        m.put("ExtremelyUnstableNaquadah", "goodgenerator");
        m.put("Fayalite", "bartworks");
        m.put("Ferberite", "bartworks");
        m.put("Ferrocene", "goodgenerator");
        m.put("FerroceneSolution", "goodgenerator");
        m.put("FerroceneWaste", "goodgenerator");
        m.put("FilledCeriumExtractingNanoResin", "gtnhlanth");
        m.put("FilledDysprosiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledErbiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledEuropiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledGadoliniumExtractingNanoResin", "gtnhlanth");
        m.put("FilledHolmiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledLanthanumExtractingNanoResin", "gtnhlanth");
        m.put("FilledLutetiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledNeodymiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledPraseodymiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledSamariumExtractingNanoResin", "gtnhlanth");
        m.put("FilledTerbiumExtractingNanoResin", "gtnhlanth");
        m.put("FilledThuliumExtractingNanoResin", "gtnhlanth");
        m.put("FilledYtterbiumExtractingNanoResin", "gtnhlanth");
        m.put("FilteredBastnasiteMud", "gtnhlanth");
        m.put("Fluor-Buergerite", "bartworks");
        m.put("FluorinatedSamaricConcentrate", "gtnhlanth");
        m.put("Fluorine-RichWasteLiquid", "goodgenerator");
        m.put("FluoroantimonicAcid", "goodgenerator");
        m.put("Fluoroform", "gtnhlanth");
        m.put("Fluorophlogopite", "bartworks");
        m.put("Fluorspar", "bartworks");
        m.put("FormicAcid", "bartworks");
        m.put("Forsterite", "bartworks");
        m.put("GadoliniumChlorideConcentrate", "gtnhlanth");
        m.put("GadoliniumExtractingNanoResin", "gtnhlanth");
        m.put("GadoliniumOreConcentrate", "gtnhlanth");
        m.put("GalliumHydroxide", "goodgenerator");
        m.put("Gangue", "gtnhlanth");
        m.put("Graphite-UraniumMixture", "goodgenerator");
        m.put("GreenFuchsite", "bartworks");
        m.put("Hafnia", "gtnhlanth");
        m.put("Hafnia-ZirconiaBlend", "gtnhlanth");
        m.put("Hafnium", "gtnhlanth");
        m.put("HafniumCarbide", "bartworks");
        m.put("HafniumIodide", "gtnhlanth");
        m.put("HafniumRunoff", "gtnhlanth");
        m.put("HafniumTetrachloride", "gtnhlanth");
        m.put("HafniumTetrachlorideSolution", "gtnhlanth");
        m.put("HeavyNaquadahFuel", "goodgenerator");
        m.put("Hedenbergite", "bartworks");
        m.put("HeterogenousHalogenicMonaziteRareEarthMixture", "gtnhlanth");
        m.put("HexafluorosilicicAcid", "bartworks");
        m.put("HighDurabilityCompoundSteel", "bartworks");
        m.put("Hikarium", "goodgenerator");
        m.put("HolmiumChlorideConcentrate", "gtnhlanth");
        m.put("HolmiumExtractingNanoResin", "gtnhlanth");
        m.put("HolmiumOreConcentrate", "gtnhlanth");
        m.put("HotRutheniumTetroxideSolution", "bartworks");
        m.put("HotSuperCoolant", "gtnhlanth");
        m.put("Huebnerite", "bartworks");
        m.put("ImpureFerroceneMixture", "goodgenerator");
        m.put("ImpureLanthanumChloride", "gtnhlanth");
        m.put("Incoloy-903", "goodgenerator");
        m.put("IndiumPhosphate", "goodgenerator");
        m.put("InertEnrichedNaquadah", "goodgenerator");
        m.put("InertNaquadah", "goodgenerator");
        m.put("InertNaquadria", "goodgenerator");
        m.put("Iodine", "gtnhlanth");
        m.put("IridiumChloride", "bartworks");
        m.put("IridiumDioxide", "bartworks");
        m.put("IridiumMetalResidue", "bartworks");
        m.put("IronIIChloride", "goodgenerator");
        m.put("JetFuelA", "goodgenerator");
        m.put("JetFuelNo.3", "goodgenerator");
        m.put("Krypton", "bartworks");
        m.put("LanthaniumChloride", "gtnhlanth");
        m.put("LanthanumChlorideConcentrate", "gtnhlanth");
        m.put("LanthanumExtractingNanoResin", "gtnhlanth");
        m.put("LanthanumHexaboride", "gtnhlanth");
        m.put("LanthanumOreConcentrate", "gtnhlanth");
        m.put("LanthanumOxide", "gtnhlanth");
        m.put("LeachResidue", "bartworks");
        m.put("LightNaquadahFuel", "goodgenerator");
        m.put("LiquidHelium", "bartworks");
        m.put("LithiumChloride", "goodgenerator");
        m.put("Loellingite", "bartworks");
        m.put("Low-PurityHafnium", "gtnhlanth");
        m.put("LowQualityNaquadahEmulsion", "goodgenerator");
        m.put("LowQualityNaquadahSolution", "goodgenerator");
        m.put("LowQualityNaquadriaPhosphate", "goodgenerator");
        m.put("LowQualityNaquadriaSulphate", "goodgenerator");
        m.put("Lumiinessence", "goodgenerator");
        m.put("Lumiium", "goodgenerator");
        m.put("LutetiumChlorideConcentrate", "gtnhlanth");
        m.put("LutetiumExtractingNanoResin", "gtnhlanth");
        m.put("LutetiumOreConcentrate", "gtnhlanth");
        m.put("MAR-Ce-M200Steel", "goodgenerator");
        m.put("MAR-M200Steel", "goodgenerator");
        m.put("MagnesiumSulphate", "goodgenerator");
        m.put("MagnetoResonatic", "bartworks");
        m.put("MetallicSludgeDustResidue", "bartworks");
        m.put("MetastableOganesson", "goodgenerator");
        m.put("Molybdenum(IV)Oxide", "gtnhlanth");
        m.put("MolybdenumTrioxide", "gtnhlanth");
        m.put("MonaziteRareEarthFiltrate", "gtnhlanth");
        m.put("MonaziteRareEarthHydroxideConcentrate", "gtnhlanth");
        m.put("MonaziteRarerEarthSediment", "gtnhlanth");
        m.put("MonaziteSulfate", "gtnhlanth");
        m.put("Mu-metal", "gtnhlanth");
        m.put("MuddyBastnasiteRareEarthSolution", "gtnhlanth");
        m.put("MuddyMonaziteRareEarthSolution", "gtnhlanth");
        m.put("MuddySamariumRareEarthSolution", "gtnhlanth");
        m.put("Naquadah-AdamantiumSolution", "goodgenerator");
        m.put("Naquadah-RichSolution", "goodgenerator");
        m.put("NaquadahAsphalt", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkI", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkI(Depleted)", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkII", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkII(Depleted)", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkIII", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkIII(Depleted)", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkIV", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkIV(Depleted)", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkV", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkV(Depleted)", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkVI", "goodgenerator");
        m.put("NaquadahBasedLiquidFuelMkVI(Depleted)", "goodgenerator");
        m.put("NaquadahEmulsion", "goodgenerator");
        m.put("NaquadahGas", "goodgenerator");
        m.put("NaquadahGoo", "goodgenerator");
        m.put("NaquadahOxideMixture", "goodgenerator");
        m.put("NaquadahSolution", "goodgenerator");
        m.put("Naquadahine", "goodgenerator");
        m.put("Naquadria-RichSolution", "goodgenerator");
        m.put("NaquadriaGoo", "goodgenerator");
        m.put("NaquadriaOxideMixture", "goodgenerator");
        m.put("NaquadriaSulphate", "goodgenerator");
        m.put("NeodymiumChlorideConcentrate", "gtnhlanth");
        m.put("NeodymiumExtractingNanoResin", "gtnhlanth");
        m.put("NeodymiumOreConcentrate", "gtnhlanth");
        m.put("NeodymiumOxide", "gtnhlanth");
        m.put("NeodymiumRareEarthConcentrate", "gtnhlanth");
        m.put("Neon", "bartworks");
        m.put("NeutralizedMonaziteRareEarthFiltrate", "gtnhlanth");
        m.put("NeutralizedUraniumFiltrate", "gtnhlanth");
        m.put("Nitric-LeachedMonaziteMixture", "gtnhlanth");
        m.put("NitricMonaziteLeachedConcentrate", "gtnhlanth");
        m.put("NitrogenatedBastnasiteRarerEarthOxides", "gtnhlanth");
        m.put("NitrogenatedMonaziteRareEarthConcentrate", "gtnhlanth");
        m.put("NitrogenatedSamarium-TerbiumMixture", "gtnhlanth");
        m.put("Nitromethane", "gtnhlanth-bot");
        m.put("Oganesson", "bartworks");
        m.put("Olenite", "bartworks");
        m.put("OrangeDescloizite", "bartworks");
        m.put("Orundum", "goodgenerator");
        m.put("OsmiumSolution", "bartworks");
        m.put("OxalicAcid", "goodgenerator");
        m.put("P-507", "goodgenerator");
        m.put("PalladiumEnrichedAmmonia", "bartworks");
        m.put("PalladiumMetallicPowder", "bartworks");
        m.put("PalladiumSalt", "bartworks");
        m.put("Permalloy", "gtnhlanth");
        m.put("Phosgene", "gtnhlanth-bot");
        m.put("PlatinumConcentrate", "bartworks");
        m.put("PlatinumMetallicPowder", "bartworks");
        m.put("PlatinumResidue", "bartworks");
        m.put("PlatinumSalt", "bartworks");
        m.put("PlutoniumBasedLiquidFuel", "goodgenerator");
        m.put("PlutoniumBasedLiquidFuel(Depleted)", "goodgenerator");
        m.put("PlutoniumBasedLiquidFuel(ExcitedState)", "goodgenerator");
        m.put("PlutoniumOxide-UraniumMixture", "goodgenerator");
        m.put("PotassiumCarbonate", "bartworks");
        m.put("PotassiumChlorate", "gtnhlanth");
        m.put("PotassiumDisulfate", "bartworks");
        m.put("PotassiumPermanganate", "gtnhlanth");
        m.put("PotassiumPermanganateSolution", "gtnhlanth");
        m.put("Potassiumfluorosilicate", "bartworks");
        m.put("PraseodymiumChlorideConcentrate", "gtnhlanth");
        m.put("PraseodymiumExtractingNanoResin", "gtnhlanth");
        m.put("PraseodymiumOreConcentrate", "gtnhlanth");
        m.put("Prasiolite", "bartworks");
        m.put("PreciousMetalsAlloy", "goodgenerator");
        m.put("PromethiumChlorideConcentrate", "gtnhlanth");
        m.put("PromethiumOreConcentrate", "gtnhlanth");
        m.put("RadioactiveSludge", "goodgenerator");
        m.put("RarestEarthResidue", "gtnhlanth");
        m.put("RarestMetalResidue", "bartworks");
        m.put("RawAdemicSteel", "bartworks");
        m.put("RawFluorophlogopite", "bartworks");
        m.put("Reactive-IonEtchingMixture", "gtnhlanth");
        m.put("RedDescloizite", "bartworks");
        m.put("RedFuchsite", "bartworks");
        m.put("RedZircon", "bartworks");
        m.put("RefinedPlatinumSalt", "bartworks");
        m.put("ReprecipitatedPalladium", "bartworks");
        m.put("ReprecipitatedPlatinum", "bartworks");
        m.put("ReprecipitatedRhodium", "bartworks");
        m.put("Rhodium", "bartworks");
        m.put("Rhodium-PlatedPalladium", "bartworks");
        m.put("RhodiumFilterCake", "bartworks");
        m.put("RhodiumFilterCakeSolution", "bartworks");
        m.put("RhodiumNitrate", "bartworks");
        m.put("RhodiumSalt", "bartworks");
        m.put("RhodiumSaltSolution", "bartworks");
        m.put("RhodiumSulfate", "bartworks");
        m.put("RhodiumSulfateSolution", "bartworks");
        m.put("RoastedRareEarthOxides", "gtnhlanth");
        m.put("RockSalt", "bartworks");
        m.put("Roquesite", "bartworks");
        m.put("Ruridit", "bartworks");
        m.put("Ruthenium", "bartworks");
        m.put("RutheniumTetroxide", "bartworks");
        m.put("RutheniumTetroxideSolution", "bartworks");
        m.put("Salt", "bartworks");
        m.put("SamaricRareEarthConcentrate", "gtnhlanth");
        m.put("SamaricResidue", "gtnhlanth");
        m.put("Samarium(III)-Chloride", "gtnhlanth");
        m.put("Samarium(III)Oxalate", "gtnhlanth");
        m.put("Samarium-TerbiumMixture", "gtnhlanth");
        m.put("SamariumChloride-SodiumChlorideBlend", "gtnhlanth");
        m.put("SamariumChlorideConcentrate", "gtnhlanth");
        m.put("SamariumExtractingNanoResin", "gtnhlanth");
        m.put("SamariumOreConcentrate", "gtnhlanth");
        m.put("SamariumOxide", "gtnhlanth");
        m.put("SamariumRareEarthMud", "gtnhlanth");
        m.put("SaturatedMonaziteRareEarth", "gtnhlanth");
        m.put("SeaweedAsh", "gtnhlanth");
        m.put("SeaweedByproducts", "gtnhlanth");
        m.put("SeaweedConcentrate", "gtnhlanth");
        m.put("Shirabon", "goodgenerator");
        m.put("Signalium", "goodgenerator");
        m.put("Silane-NitrogenPlasmaMixture", "gtnhlanth");
        m.put("SiliconNitride", "gtnhlanth");
        m.put("SludgeDustResidue", "bartworks");
        m.put("SodiumFormate", "bartworks");
        m.put("SodiumNitrate", "bartworks");
        m.put("SodiumOxalate", "goodgenerator");
        m.put("SodiumRuthenate", "bartworks");
        m.put("SodiumSulfate", "bartworks");
        m.put("SodiumTungstate", "gtnhlanth-bot");
        m.put("Sodiumfluorosilicate", "gtnhlanth");
        m.put("Spodumene", "bartworks");
        m.put("Steam-CrackedBastnasiteMud", "gtnhlanth");
        m.put("Tairitsu", "goodgenerator");
        m.put("TanmolyiumBeta-C", "goodgenerator");
        m.put("TantalumCarbide/HafniumCarbideMixture", "bartworks");
        m.put("TantalumHafniumCarbide", "bartworks");
        m.put("Tellurium", "bartworks");
        m.put("Tellurium(IV)Oxide", "gtnhlanth");
        m.put("Tellurium-Molybdenum-OxideCatalyst", "gtnhlanth");
        m.put("Temagamite", "bartworks");
        m.put("TerbiumChlorideConcentrate", "gtnhlanth");
        m.put("TerbiumExtractingNanoResin", "gtnhlanth");
        m.put("TerbiumNitrate", "gtnhlanth");
        m.put("TerbiumOreConcentrate", "gtnhlanth");
        m.put("Terlinguaite", "bartworks");
        m.put("Thorianite", "bartworks");
        m.put("Thorium-232Tetrafluoride", "goodgenerator");
        m.put("Thorium-PhosphateCake", "gtnhlanth");
        m.put("Thorium-PhosphateConcentrate", "gtnhlanth");
        m.put("Thorium232", "bartworks");
        m.put("Thorium234", "gtnhlanth");
        m.put("ThoriumBasedLiquidFuel", "goodgenerator");
        m.put("ThoriumBasedLiquidFuel(Depleted)", "goodgenerator");
        m.put("ThoriumBasedLiquidFuel(ExcitedState)", "goodgenerator");
        m.put("ThoriumHydroxide", "goodgenerator");
        m.put("ThoriumNitrate", "goodgenerator");
        m.put("ThoriumOxalate", "goodgenerator");
        m.put("ThoriumTetrachloride", "goodgenerator");
        m.put("ThoriumTetrafluoride", "goodgenerator");
        m.put("ThuliumChlorideConcentrate", "gtnhlanth");
        m.put("ThuliumExtractingNanoResin", "gtnhlanth");
        m.put("ThuliumOreConcentrate", "gtnhlanth");
        m.put("Tiberium", "bartworks");
        m.put("TitaniumTrifluoride", "goodgenerator");
        m.put("TriniumSulphate", "goodgenerator");
        m.put("TungstenTrioxide", "gtnhlanth-bot");
        m.put("TungsticAcid", "gtnhlanth-bot");
        m.put("UnformedFluorophlogopite", "bartworks");
        m.put("UnknownBlend", "gtnhlanth");
        m.put("UraniumBasedLiquidFuel", "goodgenerator");
        m.put("UraniumBasedLiquidFuel(Depleted)", "goodgenerator");
        m.put("UraniumBasedLiquidFuel(ExcitedState)", "goodgenerator");
        m.put("UraniumCarbide-ThoriumMixture", "goodgenerator");
        m.put("UraniumFiltrate", "gtnhlanth");
        m.put("Vanadio-Oxy-Dravite", "bartworks");
        m.put("VanadiumPentoxide", "goodgenerator");
        m.put("WasteLiquid", "goodgenerator");
        m.put("WetRareEarthOxides", "gtnhlanth");
        m.put("Wittichenite", "bartworks");
        m.put("Xenon", "bartworks");
        m.put("YtterbiumChlorideConcentrate", "gtnhlanth");
        m.put("YtterbiumExtractingNanoResin", "gtnhlanth");
        m.put("YtterbiumOreConcentrate", "gtnhlanth");
        m.put("YttriumOxide", "bartworks");
        m.put("ZincChloride", "goodgenerator");
        m.put("ZincSulfate", "bartworks");
        m.put("Zircaloy-2", "goodgenerator");
        m.put("Zircaloy-4", "goodgenerator");
        m.put("Zirconia", "gtnhlanth");
        m.put("Zirconium", "bartworks");
        m.put("ZirconiumTetrachloride", "gtnhlanth");
        m.put("ZirconiumTetrachlorideSolution", "gtnhlanth");
        m.put("Zn-ThAlloy", "goodgenerator");
        return Collections.unmodifiableMap(m);
    }
}
