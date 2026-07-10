package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MaterialsElements {

    // First 50 Elements
    public final Material HYDROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Hydrogen);
    public final Material HELIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Helium);
    public final Material LITHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lithium);
    public final Material BERYLLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Beryllium);
    public final Material BORON = MaterialUtils.generateMaterialFromGtENUM(Materials.Boron);
    public final Material CARBON = MaterialUtils.generateMaterialFromGtENUM(Materials.Carbon);
    public final Material NITROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Nitrogen);
    public final Material OXYGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Oxygen);
    public final Material FLUORINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Fluorine);
    public final Material NEON = MaterialReconstruction.byName("Neon"); // Not a GT Inherited Material
    public final Material SODIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Sodium);
    public final Material MAGNESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Magnesium);
    public final Material ALUMINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Aluminium);
    public final Material ALUMINIUMOXIDE = MaterialUtils.generateMaterialFromGtENUM(Materials.Aluminiumoxide);
    public final Material SILICON = MaterialUtils.generateMaterialFromGtENUM(Materials.Silicon);
    public final Material SILICONDIOXIDE = MaterialUtils.generateMaterialFromGtENUM(Materials.SiliconDioxide);
    public final Material PHOSPHORUS = MaterialUtils.generateMaterialFromGtENUM(Materials.Phosphorus);
    public final Material SULFUR = MaterialUtils.generateMaterialFromGtENUM(Materials.Sulfur);
    public final Material CHLORINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Chlorine);
    public final Material ARGON = MaterialUtils.generateMaterialFromGtENUM(Materials.Argon);
    public final Material POTASSIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Potassium);
    public final Material CALCIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Calcium);
    public final Material SCANDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Scandium);
    public final Material TITANIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Titanium);
    public final Material VANADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Vanadium);
    public final Material CHROMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Chrome);
    public final Material MANGANESE = MaterialUtils.generateMaterialFromGtENUM(Materials.Manganese);
    public final Material IRON = MaterialUtils.generateMaterialFromGtENUM(Materials.Iron);
    public final Material COBALT = MaterialUtils.generateMaterialFromGtENUM(Materials.Cobalt);
    public final Material NICKEL = MaterialUtils.generateMaterialFromGtENUM(Materials.Nickel);
    public final Material COPPER = MaterialUtils.generateMaterialFromGtENUM(Materials.Copper);
    public final Material ZINC = MaterialUtils.generateMaterialFromGtENUM(Materials.Zinc);
    public final Material GALLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Gallium);
    public final Material GERMANIUM = MaterialReconstruction.byName("Germanium"); // Not a GT Inherited Material
    public final Material ARSENIC = MaterialUtils.generateMaterialFromGtENUM(Materials.Arsenic);
    public final Material SELENIUM = MaterialReconstruction.byName("Selenium"); // Not a GT Inherited Material
    public final Material BROMINE = MaterialReconstruction.byName("Bromine"); // Not a GT Inherited Material
    public final Material KRYPTON = MaterialReconstruction.byName("Krypton"); // Not a GT Inherited Material
    public final Material RUBIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Rubidium);
    // Custom rgb/texture-set arguments in this class are mirrored by MaterialReconstruction#generateGtEnum;
    // keep the two in sync.
    public final Material STRONTIUM = MaterialUtils
        .generateMaterialFromGtENUM(Materials.Strontium, new short[] { 230, 210, 110 }, TextureSet.SET_FLINT);
    public final Material YTTRIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Yttrium);
    public final Material ZIRCONIUM = MaterialReconstruction.byName("Zirconium"); // Not a GT Inherited Material
    public final Material NIOBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Niobium);
    public final Material MOLYBDENUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Molybdenum);

    public final Material RUTHENIUM = MaterialReconstruction.byName("Ruthenium"); // Not a GT Inherited Material
    public final Material RHODIUM = MaterialReconstruction.byName("Rhodium"); // Not a GT Inherited Material
    public final Material AMERICIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Americium);
    public final Material TECHNETIUM = MaterialReconstruction.byName("Technetium"); // Not a GT Inherited Material
    public final Material NEPTUNIUM = MaterialReconstruction.byName("Neptunium"); // Not a GT Inherited Material

    public final Material PALLADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Palladium);
    public final Material SILVER = MaterialUtils.generateMaterialFromGtENUM(Materials.Silver);
    public final Material CADMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cadmium);
    public final Material INDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Indium);
    public final Material TIN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tin);
    public final Material ANTIMONY = MaterialUtils.generateMaterialFromGtENUM(Materials.Antimony);
    public final Material TELLURIUM = MaterialReconstruction.byName("Tellurium"); // Not a GT Inherited Material
    public final Material IODINE = MaterialReconstruction.byName("Iodine"); // Not a GT Inherited Material
    public final Material XENON = MaterialReconstruction.byName("Xenon"); // Not a GT Inherited Material
    public final Material CAESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Caesium);
    public final Material BARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Barium);
    public final Material LANTHANUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lanthanum);
    public final Material CERIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cerium);
    public final Material PRASEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Praseodymium);
    public final Material NEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Neodymium);
    public final Material PROMETHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Promethium);
    public final Material SAMARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Samarium);
    public final Material EUROPIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Europium);
    public final Material GADOLINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Gadolinium);
    public final Material TERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Terbium);
    public final Material DYSPROSIUM = MaterialReconstruction.byName("Dysprosium"); // Not a GT Inherited Material
    public final Material HOLMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Holmium);
    public final Material ERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Erbium);
    public final Material THULIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Thulium);
    public final Material YTTERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Ytterbium);
    public final Material LUTETIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lutetium);
    public final Material HAFNIUM = MaterialReconstruction.byName("Hafnium"); // Not a GT Inherited Material

    // Second 50 elements
    public final Material TANTALUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Tantalum);
    public final Material TUNGSTEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tungsten);
    public final Material RHENIUM = MaterialReconstruction.byName("Rhenium"); // Not a GT Inherited Material
    public final Material OSMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmium);
    public final Material IRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Iridium);
    public final Material PLATINUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);
    public final Material GOLD = MaterialUtils.generateMaterialFromGtENUM(Materials.Gold);
    public final Material MERCURY = MaterialUtils.generateMaterialFromGtENUM(Materials.Mercury); // Mercury
    public final Material THALLIUM = MaterialReconstruction.byName("Thallium"); // Not a GT Inherited Material
    public final Material LEAD = MaterialUtils.generateMaterialFromGtENUM(Materials.Lead);
    public final Material BISMUTH = MaterialUtils.generateMaterialFromGtENUM(Materials.Bismuth);
    public final Material POLONIUM = MaterialReconstruction.byName("Polonium"); // Not a GT Inherited Material

    public final Material RADON = MaterialUtils.generateMaterialFromGtENUM(Materials.Radon);

    public final Material RADIUM = MaterialReconstruction.byName("Radium"); // Not a GT Inherited Material

    public final Material THORIUM = MaterialReconstruction.byName("Thorium");
    public final Material PROTACTINIUM = MaterialReconstruction.byName("Protactinium"); // Not a GT Inherited Material
    public final Material URANIUM238 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium);
    public final Material URANIUM235 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium235);
    public final Material PLUTONIUM241 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium241);
    public final Material CURIUM = MaterialReconstruction.byName("Curium"); // Not a GT Inherited Material

    public final Material CALIFORNIUM = MaterialReconstruction.byName("Californium"); // Not a GT Inherited Material

    public final Material FERMIUM = MaterialReconstruction.byName("Fermium"); // Not a GT Inherited Material //Boiling
                                                                              // Point is made up

    // Misc
    public final Material AER = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedAir, TextureSet.SET_GEM_A);
    public final Material IGNIS = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedFire, TextureSet.SET_GEM_A);
    public final Material TERRA = MaterialUtils
        .generateMaterialFromGtENUM(Materials.InfusedEarth, TextureSet.SET_GEM_A);
    public final Material AQUA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedWater, TextureSet.SET_GEM_A);
    public final Material PERDITIO = MaterialUtils
        .generateMaterialFromGtENUM(Materials.InfusedEntropy, TextureSet.SET_GEM_A);
    public final Material ORDO = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedOrder, TextureSet.SET_GEM_A);

    public final Material NAQUADAH = MaterialUtils.generateMaterialFromGtENUM(Materials.Naquadah);
    public final Material NAQUADAH_ENRICHED = MaterialUtils.generateMaterialFromGtENUM(Materials.NaquadahEnriched);
    public final Material NAQUADRIA = MaterialUtils.generateMaterialFromGtENUM(Materials.Naquadria);
    public final Material TRINIUM;
    public final Material TRINIUM_REFINED;
    // https://github.com/Blood-Asp/GT5-Unofficial/issues/609

    // Custom Isotopes
    public final Material LITHIUM7 = MaterialReconstruction.byName("Lithium7"); // Not a GT Inherited Material
    public final Material URANIUM232 = MaterialReconstruction.byName("Uranium232"); // Not a GT Inherited Material
    public final Material URANIUM233 = MaterialReconstruction.byName("Uranium233"); // Not a GT Inherited Material
    public final Material THORIUM232 = MaterialReconstruction.byName("Thorium232"); // Not a GT Inherited Material

    // RTG Fuels
    public final Material PLUTONIUM238 = MaterialReconstruction.byName("Plutonium238"); // Not a GT Inherited Material

    public final Material MAGIC = MaterialUtils
        .generateMaterialFromGtENUM(Materials.Magic, new short[] { 10, 185, 140 });
    public final Material THAUMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Thaumium);

    private static final MaterialsElements INSTANCE = new MaterialsElements();

    public MaterialsElements() {
        // GTNH Trinium Handling
        TRINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Trinium);
        TRINIUM_REFINED = TRINIUM;
    }

    public static MaterialsElements getInstance() {
        return INSTANCE;
    }

    public static class STANDALONE {

        public static final Material CELESTIAL_TUNGSTEN = MaterialReconstruction.byName("CelestialTungsten"); // Not a
                                                                                                              // GT
                                                                                                              // Inherited
                                                                                                              // Material
        public static final Material ASTRAL_TITANIUM = MaterialReconstruction.byName("AstralTitanium"); // Not a GT
                                                                                                        // Inherited
                                                                                                        // Material
        public static final Material CHRONOMATIC_GLASS = MaterialReconstruction.byName("ChromaticGlass"); // Not a GT
                                                                                                          // Inherited
                                                                                                          // Material
        public static final Material ADVANCED_NITINOL = MaterialReconstruction.byName("AdvancedNitinol"); // Not a GT
                                                                                                          // Inherited
                                                                                                          // Material
        public static final Material HYPOGEN = MaterialReconstruction.byName("Hypogen"); // Not a GT Inherited Material
        public static final Material RHUGNOR = MaterialReconstruction.byName("Rhugnor"); // Not a GT Inherited Material
                                                                                         // //funeris
        public static final Material FORCE = MaterialReconstruction.byName("Force"); // Not a GT Inherited Material

        // Runescape materials
        public static final Material BLACK_METAL = MaterialReconstruction.byName("BlackMetal"); // Not
        // a
        // GT
        // Inherited
        // Material

        public static final Material GRANITE = MaterialReconstruction.byName("AncientGranite"); // Not
        // a
        // GT
        // Inherited
        // Material

        public static final Material RUNITE = MaterialReconstruction.byName("Runite"); // Not a GT Inherited Material
        public static final Material DRAGON_METAL = MaterialReconstruction.byName("Dragonblood"); // Not a GT Inherited
                                                                                                  // Material
    }
}
