package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MaterialsAlloy {

    // Just some GT Alloys that I need within mine.
    public static final Material BRONZE = MaterialUtils.generateMaterialFromGtENUM(Materials.Bronze);
    public static final Material STEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.Steel);
    public static final Material STEEL_BLACK = MaterialUtils.generateMaterialFromGtENUM(Materials.BlackSteel);
    public static final Material INVAR = MaterialUtils.generateMaterialFromGtENUM(Materials.Invar);
    public static final Material KANTHAL = MaterialUtils.generateMaterialFromGtENUM(Materials.Kanthal);
    public static final Material NICHROME = MaterialUtils.generateMaterialFromGtENUM(Materials.Nichrome);
    public static final Material TUNGSTENSTEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.TungstenSteel);
    public static final Material STAINLESS_STEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.StainlessSteel);
    public static final Material OSMIRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmiridium);
    public static final Material POLYETHYLENE = MaterialUtils.generateMaterialFromGtENUM(Materials.Polyethylene);
    public static final Material POLYTETRAFLUOROETHYLENE = MaterialUtils
        .generateMaterialFromGtENUM(Materials.Polytetrafluoroethylene);
    public static final Material ENERGYCRYSTAL = new Material(
        "Energy Crystal", // Material Name
        MaterialState.SOLID, // State
        new short[] { 228, 255, 0, 0 }, // Material Colour
        4660, // Melting Point in C
        5735, // Boiling Point in C
        90, // Protons
        40, // Neutrons
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().AER, 25),
        new MaterialStack(MaterialsElements.getInstance().TERRA, 25),
        new MaterialStack(MaterialsElements.getInstance().IGNIS, 25),
        new MaterialStack(MaterialsElements.getInstance().AQUA, 25));

    public static final Material BLOODSTEEL = new Material(
        "Blood Steel", // Material Name
        MaterialState.SOLID, // State
        new short[] { 142, 28, 0, 0 }, // Material Colour
        2500, // Melting Point in C
        0, // Boiling Point in C
        100, // Protons
        100, // Neutrons
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STEEL, 5),
        new MaterialStack(MaterialsElements.getInstance().IGNIS, 5));

    public static final Material STABALLOY = new Material(
        "Staballoy", // Material Name
        MaterialState.SOLID, // State
        new short[] { 68, 75, 66, 0 }, // Material Colour
        3450, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().URANIUM238, 9),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 1));

    public static final Material TANTALLOY_60 = new Material(
        "Tantalloy-60", // Material Name
        MaterialState.SOLID, // State
        new short[] { 213, 231, 237, 0 }, // Material Colour
        3025, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 4),
        new MaterialStack(MaterialsElements.getInstance().TANTALUM, 46));

    public static final Material TANTALLOY_61 = new Material(
        "Tantalloy-61", // Material Name
        MaterialState.SOLID, // State
        new short[] { 193, 211, 217, 0 }, // Material Colour
        3030, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.TANTALLOY_60, 2),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 12),
        new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 8));

    public static final Material TUMBAGA = new Material(
        "Tumbaga", // Material Name
        MaterialState.SOLID, // State
        new short[] { 255, 178, 15, 0 }, // Material Colour
        1300,
        -1,
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().GOLD, 70),
        new MaterialStack(MaterialsElements.getInstance().COPPER, 30));

    public static final Material POTIN = new Material(
        "Potin", // Material Name
        MaterialState.SOLID, // State
        new short[] { 201, 151, 129, 0 }, // Material Colour
        1300,
        -1,
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().LEAD, 40),
        new MaterialStack(MaterialsAlloy.BRONZE, 40),
        new MaterialStack(MaterialsElements.getInstance().TIN, 20));

    /*
     * public static final Material BEDROCKIUM = new Material( "Bedrockium", //Material Name new short[]{32, 32, 32, 0},
     * //Material Colour 7735, //Melting Point in C 0, //Boiling Point in C 100, //Protons 100, //Neutrons false, //Uses
     * Blast furnace? //Material Stacks with Percentage of required elements. null);
     */

    public static final Material INCONEL_625 = new Material(
        "Inconel-625", // Material Name
        MaterialState.SOLID, // State
        new short[] { 128, 200, 128, 0 }, // Material Colour
        2425, // Melting Point in C
        3758,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 3),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 7),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 10),
        new MaterialStack(INVAR, 10),
        new MaterialStack(NICHROME, 13));

    public static final Material INCONEL_690 = new Material(
        "Inconel-690", // Material Name
        MaterialState.SOLID, // State
        new short[] { 118, 220, 138, 0 }, // Material Colour
        3425, // Melting Point in C
        4895,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 10),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 10),
        new MaterialStack(NICHROME, 15));

    public static final Material INCONEL_792 = new Material(
        "Inconel-792", // Material Name
        MaterialState.SOLID, // State
        new short[] { 108, 240, 118, 0 }, // Material Colour
        3425, // Melting Point in C
        6200,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 20),
        new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 10),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 20),
        new MaterialStack(NICHROME, 10));

    public static final Material NITINOL_60 = new Material(
        "Nitinol 60", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        5651, // Melting Point in C
        8975,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 40),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 60));

    public static final Material ZERON_100 = new Material(
        "Zeron-100", // Material Name
        MaterialState.SOLID, // State
        new short[] { 180, 180, 20, 0 }, // Material Colour
        6100,
        9785,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 26),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 6),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 4),
        new MaterialStack(MaterialsElements.getInstance().COPPER, 20),
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 4),
        new MaterialStack(MaterialsAlloy.STEEL, 40));

    public static final Material MARAGING250 = new Material(
        "Maraging Steel 250", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        2413, // Melting Point in C
        4555,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STEEL, 64),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 4),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 4),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 16),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 8));

    public static final Material MARAGING300 = new Material(
        "Maraging Steel 300", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        2413, // Melting Point in C
        4555,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STEEL, 64),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 4),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 4),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 16),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 8));

    public static final Material MARAGING350 = new Material(
        "Maraging Steel 350", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        2413, // Melting Point in C
        4555,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STEEL, 64),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 4),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 4),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 16),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 8));

    public static final Material AQUATIC_STEEL = new Material(
        "Watertight Steel", // Material Name
        MaterialState.SOLID, // State
        new short[] { 120, 120, 180 }, // Material Colour
        2673, // Melting Point in C
        4835,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STEEL, 60),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 10),
        new MaterialStack(MaterialsElements.getInstance().MANGANESE, 5),
        new MaterialStack(MaterialsElements.getInstance().SILICON, 10),
        new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 5),
        new MaterialStack(MaterialsElements.getInstance().SULFUR, 5),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 5));

    public static final Material STELLITE = new Material(
        "Stellite", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4310, // Melting Point in C
        6250,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().COBALT, 35),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 35),
        new MaterialStack(MaterialsElements.getInstance().MANGANESE, 20),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 10));

    public static final Material TALONITE = new Material(
        "Talonite", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3454, // Melting Point in C
        5500,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().COBALT, 40),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 30),
        new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 20),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 10));

    public static final Material HASTELLOY_W = new Material(
        "Hastelloy-W", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3350, // Melting Point in C
        5755,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 6),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 2),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 24),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 6),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 62));

    public static final Material HASTELLOY_X = new Material(
        "Hastelloy-X", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3350, // Melting Point in C
        5755,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 18),
        new MaterialStack(MaterialsElements.getInstance().MANGANESE, 2),
        new MaterialStack(MaterialsElements.getInstance().SILICON, 2),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 8),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 22),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 48));

    public static final Material HASTELLOY_N = new Material(
        "Hastelloy-N", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4350, // Melting Point in C
        6875,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 8),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 16),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 8),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 8),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 60));

    public static final Material HASTELLOY_C276 = new Material(
        "Hastelloy-C276", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4350, // Melting Point in C
        6520,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().COBALT, 2),
        new MaterialStack(MaterialsElements.getInstance().MOLYBDENUM, 16),
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 2),
        new MaterialStack(MaterialsElements.getInstance().COPPER, 2),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 14),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 64));

    public static final Material INCOLOY_020 = new Material(
        "Incoloy-020", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3425, // Melting Point in C
        5420,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 40),
        new MaterialStack(MaterialsElements.getInstance().COPPER, 4),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 20),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 36));

    public static final Material INCOLOY_DS = new Material(
        "Incoloy-DS", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3425, // Melting Point in C
        5420,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 46),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 18),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 18),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 18));

    public static final Material INCOLOY_MA956 = new Material(
        "Incoloy-MA956", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4425, // Melting Point in C
        6875,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 64),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 12),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 20),
        new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 4));

    public static final Material TUNGSTEN_CARBIDE = new Material(
        "Tungstencarbide", // Material Name
        MaterialState.SOLID, // State
        new short[] { 44, 44, 44, 0 }, // Material Colour
        3422, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        false, // Generate cells
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().CARBON, 50),
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 50));

    public static final Material TUNGSTEN_TITANIUM_CARBIDE = new Material(
        "Tungsten Titanium Carbide", // Material Name
        MaterialState.SOLID, // State
        null,
        4422, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(TUNGSTEN_CARBIDE, 70),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 30));

    public static final Material SILICON_CARBIDE = new Material(
        "Silicon Carbide", // Material Name
        MaterialState.SOLID, // State
        new short[] { 40, 48, 36, 0 }, // Material Colour
        1414, // Melting Point in C
        -1,
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().SILICON, 50),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 50));

    public static final Material TANTALUM_CARBIDE = new Material(
        "Tantalum Carbide", // Material Name
        MaterialState.SOLID, // State
        new short[] { 139, 136, 120, 0 }, // Material Colour
        2980, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().TANTALUM, 50),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 50));

    public static final Material ZIRCONIUM_CARBIDE = new Material(
        "Zirconium Carbide", // Material Name
        MaterialState.SOLID, // State
        new short[] { 222, 202, 180, 0 }, // Material Colour
        1555, // Melting Point in C
        -1,
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().ZIRCONIUM, 50),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 50));

    public static final Material NIOBIUM_CARBIDE = new Material(
        "Niobium Carbide", // Material Name
        MaterialState.SOLID, // State
        new short[] { 205, 197, 191, 0 }, // Material Colour
        2477, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 50),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 50));

    public static final Material ARCANITE = new Material(
        "Arcanite", // Material Name
        MaterialState.SOLID, // State
        null,
        5666, // Melting Point in C
        9875,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().THORIUM232, 40),
        new MaterialStack(ENERGYCRYSTAL, 40),
        new MaterialStack(MaterialsElements.getInstance().ORDO, 10),
        new MaterialStack(MaterialsElements.getInstance().PERDITIO, 10));

    public static final Material LEAGRISIUM = new Material(
        "Grisium", // Material Name
        MaterialState.SOLID, // State
        new short[] { 53, 93, 106, 0 }, // Material Colour
        3850, // Melting Point in C
        5550, // Boiling Point in C
        96, // Protons
        128, // Neutrons
        true, // Uses Blast furnace?
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 18),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 18),
        new MaterialStack(MaterialsElements.getInstance().POTASSIUM, 18),
        new MaterialStack(MaterialsElements.getInstance().LITHIUM, 18),
        new MaterialStack(MaterialsElements.getInstance().SULFUR, 18),
        new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 10)); // Material Stacks with Percentage of
    // required elements.

    public static final Material EGLIN_STEEL_BASE = new Material(
        "Eglin Steel Base Compound", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().IRON, 12),
        new MaterialStack(KANTHAL, 3),
        new MaterialStack(INVAR, 15));

    public static final Material EGLIN_STEEL = new Material(
        "Eglin Steel", // Material Name
        MaterialState.SOLID, // State
        new short[] { 139, 69, 19, 0 }, // Material Colour
        1048, // Melting Point in C
        1973, // Boiling Point in C
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.EGLIN_STEEL_BASE, 10),
        new MaterialStack(MaterialsElements.getInstance().SULFUR, 1),
        new MaterialStack(MaterialsElements.getInstance().SILICON, 4),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 1));

    public static final Material HG1223 = new Material(
        "HG-1223", // Material Name
        MaterialState.LIQUID, // State
        new short[] { 39, 85, 159, 0 }, // Material Colour
        6357, // Melting Point in C
        8563, // Boiling Point in C
        -1,
        -1,
        false, // Uses Blast furnace?
        new MaterialStack(MaterialsElements.getInstance().MERCURY, 1),
        new MaterialStack(MaterialsElements.getInstance().BARIUM, 2),
        new MaterialStack(MaterialsElements.getInstance().CALCIUM, 2),
        new MaterialStack(MaterialsElements.getInstance().COPPER, 3),
        new MaterialStack(MaterialsElements.getInstance().OXYGEN, 8));

    public static final Material HS188A = new Material(
        "HS188-A", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4870, // Melting Point in C
        7550, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        true, // Uses Blast furnace?
        new MaterialStack(MaterialsElements.getInstance().COBALT, 20),
        new MaterialStack(MaterialsElements.getInstance().HAFNIUM, 20),
        new MaterialStack(TALONITE, 16),
        new MaterialStack(MaterialsElements.getInstance().RHENIUM, 10),
        new MaterialStack(NIOBIUM_CARBIDE, 10),
        new MaterialStack(HASTELLOY_X, 8),
        new MaterialStack(TUNGSTENSTEEL, 8),
        new MaterialStack(ZIRCONIUM_CARBIDE, 8)); // Material Stacks with Percentage of required
                                                  // elements.

    /**
     * Stargate Materials - #D2FFA9 210, 255, 170
     */
    public static final Material TRINIUM_TITANIUM = new Material(
        "Trinium Titanium Alloy", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        3750, // Melting Point in C
        7210, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        new MaterialStack(MaterialsElements.getInstance().TRINIUM_REFINED, 3),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 7));

    public static final Material TRINIUM_NAQUADAH = new Material(
        "Trinium Naquadah Alloy", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        4200, // Melting Point in C
        7400, // Boiling Point in C
        -1,
        -1,
        false, // Uses Blast furnace?
        new MaterialStack(MaterialsElements.getInstance().TRINIUM_REFINED, 5),
        new MaterialStack(MaterialsElements.getInstance().NAQUADAH, 9));
    public static final Material TRINIUM_NAQUADAH_CARBON = new Material(
        "Trinium Naquadah Carbonite", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        6500, // Melting Point in C
        9000, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        new MaterialStack(TRINIUM_NAQUADAH, 9),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 1));

    public static final Material TRINIUM_REINFORCED_STEEL = new Material(
        "Arceus Alloy 2B", // Material Name
        MaterialState.SOLID, // State
        new short[] { 205, 197, 23, 0 }, // Material Colour
        7555, // Melting Point in C
        12350,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().TRINIUM_REFINED, 30),
        new MaterialStack(MaterialsAlloy.MARAGING350, 40),
        new MaterialStack(MaterialsAlloy.TUNGSTENSTEEL, 20),
        new MaterialStack(MaterialsAlloy.OSMIRIDIUM, 10),
        new MaterialStack(MaterialsElements.getInstance().STRONTIUM, 10));

    /*
     * Witchery Material
     */

    public static final Material KOBOLDITE = new Material(
        "Koboldite", // Material Name
        MaterialState.SOLID, // State
        new short[] { 80, 210, 255, 0 }, // Material Colour
        -1, // Melting Point in C
        -1,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 35),
        new MaterialStack(MaterialsElements.getInstance().THAUMIUM, 30),
        new MaterialStack(MaterialsElements.getInstance().IRON, 35));

    /*
     * Top Tier Alloys
     */

    public static final Material HELICOPTER = new Material(
        "HeLiCoPtEr", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        5763,
        8192,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().HELIUM, 20),
        new MaterialStack(MaterialsElements.getInstance().LITHIUM, 20),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 20),
        new MaterialStack(MaterialsElements.getInstance().PLATINUM, 20),
        new MaterialStack(MaterialsElements.getInstance().ERBIUM, 20));

    // 0lafe Compound
    public static final Material LAFIUM = new Material(
        "Lafium Compound", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        6350, // Melting Point in C
        9865, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.HASTELLOY_N, 8),
        new MaterialStack(MaterialsElements.getInstance().NAQUADAH, 4),
        new MaterialStack(MaterialsElements.getInstance().SAMARIUM, 2),
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 4),
        new MaterialStack(MaterialsElements.getInstance().ARGON, 2),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 6),
        new MaterialStack(MaterialsElements.getInstance().NICKEL, 8),
        new MaterialStack(MaterialsElements.getInstance().CARBON, 2));

    // Cinobi Alloy
    public static final Material CINOBITE = new Material(
        "Cinobite A243", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        7350, // Melting Point in C
        12565, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.ZERON_100, 16),
        new MaterialStack(MaterialsElements.getInstance().NAQUADRIA, 7),
        new MaterialStack(MaterialsElements.getInstance().GADOLINIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 3),
        new MaterialStack(MaterialsElements.getInstance().MERCURY, 2),
        new MaterialStack(MaterialsElements.getInstance().TIN, 2),
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 12),
        new MaterialStack(MaterialsAlloy.OSMIRIDIUM, 6));

    // Piky Alloy
    public static final Material PIKYONIUM = new Material(
        "Pikyonium 64B", // Material Name
        MaterialState.SOLID, // State
        new short[] { 52, 103, 186, 0 }, // Material Colour
        6850, // Melting Point in C
        11765, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.INCONEL_792, 16),
        new MaterialStack(MaterialsAlloy.EGLIN_STEEL, 10),
        new MaterialStack(MaterialsElements.getInstance().NAQUADAH_ENRICHED, 8),
        new MaterialStack(MaterialsElements.getInstance().CERIUM, 6),
        new MaterialStack(MaterialsElements.getInstance().ANTIMONY, 4),
        new MaterialStack(MaterialsElements.getInstance().PLATINUM, 4),
        new MaterialStack(MaterialsElements.getInstance().YTTERBIUM, 2),
        new MaterialStack(MaterialsAlloy.TUNGSTENSTEEL, 8));

    // Piky Alloy
    public static final Material ABYSSAL = new Material(
        "Abyssal Alloy", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        9650, // Melting Point in C
        13765, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STAINLESS_STEEL, 10),
        new MaterialStack(MaterialsAlloy.TUNGSTEN_CARBIDE, 10),
        new MaterialStack(MaterialsAlloy.NICHROME, 10),
        new MaterialStack(MaterialsAlloy.BRONZE, 10),
        new MaterialStack(MaterialsAlloy.INCOLOY_MA956, 10),
        new MaterialStack(MaterialsElements.getInstance().IODINE, 2),
        new MaterialStack(MaterialsElements.getInstance().RADON, 2),
        new MaterialStack(MaterialsElements.getInstance().GERMANIUM, 2));

    // Alkalus Alloy
    public static final Material LAURENIUM = new Material(
        "Laurenium", // Material Name
        MaterialState.SOLID, // State
        new short[] { 244, 168, 255, 0 }, // Material Colour
        6825, // Melting Point in C
        11355, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.EGLIN_STEEL, 40),
        new MaterialStack(MaterialsElements.getInstance().INDIUM, 10),
        new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 20),
        new MaterialStack(MaterialsElements.getInstance().DYSPROSIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().RHENIUM, 5));

    // Bot Alloy
    public static final Material BOTMIUM = new Material(
        "Botmium", // Material Name
        MaterialState.SOLID, // State
        new short[] { 80, 160, 80, 0 }, // Material Colour
        8220, // Melting Point in C
        10540, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.NITINOL_60, 2),
        new MaterialStack(MaterialsElements.getInstance().OSMIUM, 12),
        new MaterialStack(MaterialsElements.getInstance().RUTHENIUM, 12),
        new MaterialStack(MaterialsElements.getInstance().THALLIUM, 6));

    // Titansteel
    public static final Material TITANSTEEL = new Material(
        "Titansteel", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        8250, // Melting Point in C
        11765, // Boiling Point in C
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE, 3),
        new MaterialStack(MaterialsElements.getInstance().IGNIS, 1),
        new MaterialStack(MaterialsElements.getInstance().TERRA, 1),
        new MaterialStack(MaterialsElements.getInstance().PERDITIO, 1));

    public static final Material OCTIRON = new Material(
        "Octiron", // Material Name
        MaterialState.SOLID, // State
        null,
        9120, // Melting Point in C
        14200,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(ARCANITE, 30),
        new MaterialStack(TITANSTEEL, 30),
        new MaterialStack(ENERGYCRYSTAL, 5),
        new MaterialStack(STEEL_BLACK, 10),
        new MaterialStack(MaterialsElements.getInstance().THAUMIUM, 25));

    public static final Material BLACK_TITANIUM = new Material(
        "Black Titanium", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        Materials.Titanium.mMeltingPoint * 4, // Melting Point in C
        Materials.Titanium.mMeltingPoint * 16,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().TITANIUM, 55),
        new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 12),
        new MaterialStack(MaterialsElements.getInstance().TUNGSTEN, 8),
        new MaterialStack(MaterialsElements.getInstance().COBALT, 6),
        new MaterialStack(MaterialsElements.getInstance().MANGANESE, 4),
        new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 4),
        new MaterialStack(MaterialsElements.getInstance().PALLADIUM, 4),
        new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 2),
        new MaterialStack(MaterialsElements.getInstance().ARGON, 5));

    public static final Material BABBIT_ALLOY = new Material(
        "Babbit Alloy", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        268, // Melting Point in C
        589,
        -1,
        -1,
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().TIN, 10),
        new MaterialStack(MaterialsElements.getInstance().LEAD, 72),
        new MaterialStack(MaterialsElements.getInstance().ANTIMONY, 16),
        new MaterialStack(MaterialsElements.getInstance().ARSENIC, 2));

    public static final Material INDALLOY_140 = new Material(
        "Indalloy 140", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        5200, // Melting Point in C
        6500,
        -1,
        -1,
        false, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().BISMUTH, 47),
        new MaterialStack(MaterialsElements.getInstance().LEAD, 25),
        new MaterialStack(MaterialsElements.getInstance().TIN, 13),
        new MaterialStack(MaterialsElements.getInstance().CADMIUM, 10),
        new MaterialStack(MaterialsElements.getInstance().INDIUM, 5));

    // Quantum
    public static final Material QUANTUM = new Material(
        "Quantum", // Material Name
        MaterialState.SOLID, // State
        null, // Material Colour
        10500, // Melting Point in C
        25000, // Boiling Point in C
        150, // Protons
        200, // Neutrons
        true, // Uses Blast furnace?
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsAlloy.STELLITE, 15),
        new MaterialStack(MaterialsAlloy.ENERGYCRYSTAL, 5),
        new MaterialStack(MaterialsAlloy.SILICON_CARBIDE, 5),
        new MaterialStack(MaterialsElements.getInstance().GALLIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().AMERICIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().PALLADIUM, 5),
        new MaterialStack(MaterialsElements.getInstance().BISMUTH, 5),
        new MaterialStack(MaterialsElements.getInstance().GERMANIUM, 5));
}
