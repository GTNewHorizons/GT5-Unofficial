package gtPlusPlus.core.material;

import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2Materials;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MaterialsElements {

    // First 50 Elements
    public final Material HYDROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Hydrogen);
    public final Material HELIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Helium);
    public final Material LITHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Lithium);
    public final Material BERYLLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Beryllium);
    public final Material BORON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Boron);
    public final Material CARBON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Carbon);
    public final Material NITROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Nitrogen);
    public final Material OXYGEN = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Oxygen);
    public final Material FLUORINE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Fluorine);
    public final Material NEON = MaterialReconstruction.byName("Neon"); // Not a GT Inherited Material
    public final Material SODIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Sodium);
    public final Material MAGNESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Magnesium);
    public final Material ALUMINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Aluminium);
    public final Material ALUMINIUMOXIDE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Alumina);
    public final Material SILICON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Silicon);
    public final Material SILICONDIOXIDE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.SiliconDioxide);
    public final Material PHOSPHORUS = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Phosphorus);
    public final Material SULFUR = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Sulfur);
    public final Material CHLORINE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Chlorine);
    public final Material ARGON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Argon);
    public final Material POTASSIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Potassium);
    public final Material CALCIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Calcium);
    public final Material SCANDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Scandium);
    public final Material TITANIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Titanium);
    public final Material VANADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Vanadium);
    public final Material CHROMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Chrome);
    public final Material MANGANESE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Manganese);
    public final Material IRON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Iron);
    public final Material COBALT = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Cobalt);
    public final Material NICKEL = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Nickel);
    public final Material COPPER = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Copper);
    public final Material ZINC = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Zinc);
    public final Material GALLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Gallium);
    public final Material GERMANIUM = MaterialReconstruction.byName("Germanium"); // Not a GT Inherited Material
    public final Material ARSENIC = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Arsenic);
    public final Material SELENIUM = MaterialReconstruction.byName("Selenium"); // Not a GT Inherited Material
    public final Material BROMINE = MaterialReconstruction.byName("Bromine"); // Not a GT Inherited Material
    public final Material KRYPTON = MaterialReconstruction.byName("Krypton"); // Not a GT Inherited Material
    public final Material RUBIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Rubidium);
    // Custom rgb/texture-set arguments in this class are mirrored by MaterialReconstruction#generateGtEnum;
    // keep the two in sync.
    public final Material STRONTIUM = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.Strontium, new short[] { 230, 210, 110 }, TextureSet.SET_FLINT);
    public final Material YTTRIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Yttrium);
    public final Material ZIRCONIUM = MaterialReconstruction.byName("Zirconium"); // Not a GT Inherited Material
    public final Material NIOBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Niobium);
    public final Material MOLYBDENUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Molybdenum);

    public final Material RUTHENIUM = MaterialReconstruction.byName("Ruthenium"); // Not a GT Inherited Material
    public final Material RHODIUM = MaterialReconstruction.byName("Rhodium"); // Not a GT Inherited Material
    public final Material AMERICIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Americium);
    public final Material TECHNETIUM = MaterialReconstruction.byName("Technetium"); // Not a GT Inherited Material
    public final Material NEPTUNIUM = MaterialReconstruction.byName("Neptunium"); // Not a GT Inherited Material

    public final Material PALLADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Palladium);
    public final Material SILVER = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Silver);
    public final Material CADMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Cadmium);
    public final Material INDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Indium);
    public final Material TIN = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Tin);
    public final Material ANTIMONY = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Antimony);
    public final Material TELLURIUM = MaterialReconstruction.byName("Tellurium"); // Not a GT Inherited Material
    public final Material IODINE = MaterialReconstruction.byName("Iodine"); // Not a GT Inherited Material
    public final Material XENON = MaterialReconstruction.byName("Xenon"); // Not a GT Inherited Material
    public final Material CAESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Caesium);
    public final Material BARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Barium);
    public final Material LANTHANUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Lanthanum);
    public final Material CERIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Cerium);
    public final Material PRASEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Praseodymium);
    public final Material NEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Neodymium);
    public final Material PROMETHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Promethium);
    public final Material SAMARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Samarium);
    public final Material EUROPIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Europium);
    public final Material GADOLINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Gadolinium);
    public final Material TERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Terbium);
    public final Material DYSPROSIUM = MaterialReconstruction.byName("Dysprosium"); // Not a GT Inherited Material
    public final Material HOLMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Holmium);
    public final Material ERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Erbium);
    public final Material THULIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Thulium);
    public final Material YTTERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Ytterbium);
    public final Material LUTETIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Lutetium);
    public final Material HAFNIUM = MaterialReconstruction.byName("Hafnium"); // Not a GT Inherited Material

    // Second 50 elements
    public final Material TANTALUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Tantalum);
    public final Material TUNGSTEN = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Tungsten);
    public final Material RHENIUM = MaterialReconstruction.byName("Rhenium"); // Not a GT Inherited Material
    public final Material OSMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Osmium);
    public final Material IRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Iridium);
    public final Material PLATINUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Platinum);
    public final Material GOLD = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Gold);
    public final Material MERCURY = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Mercury); // Mercury
    public final Material THALLIUM = MaterialReconstruction.byName("Thallium"); // Not a GT Inherited Material
    public final Material LEAD = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Lead);
    public final Material BISMUTH = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Bismuth);
    public final Material POLONIUM = MaterialReconstruction.byName("Polonium"); // Not a GT Inherited Material

    public final Material RADON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Radon);

    public final Material RADIUM = MaterialReconstruction.byName("Radium"); // Not a GT Inherited Material

    public final Material THORIUM = MaterialReconstruction.byName("Thorium");
    public final Material PROTACTINIUM = MaterialReconstruction.byName("Protactinium"); // Not a GT Inherited Material
    public final Material URANIUM238 = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Uranium);
    public final Material URANIUM235 = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Uranium235);
    public final Material PLUTONIUM241 = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Plutonium241);
    public final Material CURIUM = MaterialReconstruction.byName("Curium"); // Not a GT Inherited Material

    public final Material CALIFORNIUM = MaterialReconstruction.byName("Californium"); // Not a GT Inherited Material

    public final Material FERMIUM = MaterialReconstruction.byName("Fermium"); // Not a GT Inherited Material //Boiling
                                                                              // Point is made up

    // Misc
    public final Material AER = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedAir, TextureSet.SET_GEM_A);
    public final Material IGNIS = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedFire, TextureSet.SET_GEM_A);
    public final Material TERRA = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedEarth, TextureSet.SET_GEM_A);
    public final Material AQUA = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedWater, TextureSet.SET_GEM_A);
    public final Material PERDITIO = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedEntropy, TextureSet.SET_GEM_A);
    public final Material ORDO = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.InfusedOrder, TextureSet.SET_GEM_A);

    public final Material NAQUADAH = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Naquadah);
    public final Material NAQUADAH_ENRICHED = MaterialUtils
        .generateMaterialFromGtENUM(Materials2Materials.NaquadahEnriched);
    public final Material NAQUADRIA = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Naquadria);
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
        .generateMaterialFromGtENUM(Materials2Materials.Magic, new short[] { 10, 185, 140 });
    public final Material THAUMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Thaumium);

    private static final MaterialsElements INSTANCE = new MaterialsElements();

    public MaterialsElements() {
        // GTNH Trinium Handling
        TRINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Trinium);
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
