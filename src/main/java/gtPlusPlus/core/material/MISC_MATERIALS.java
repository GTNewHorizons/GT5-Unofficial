package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.client.CustomTextureSet.TextureSets;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MISC_MATERIALS {

    /*
     * Some of these materials purely exist as data objects, items will most likely be assigned separately. Most are
     * just compositions which will have dusts assigned to them.
     */

    public static void run() {
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(POTASSIUM_NITRATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_NITRATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_OXIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_HYDROXIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(CYANOACETIC_ACID, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_CYANIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(CALCIUM_CHLORIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(COPPER_SULFATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(COPPER_SULFATE_HYDRATED, false);
        WATER.registerComponentForMaterial(FluidUtils.getWater(1000));
    }

    public static final Material POTASSIUM_NITRATE = new Material(
            "Potassium Nitrate",
            MaterialState.SOLID, // State
            null,
            null,
            -1,
            -1,
            -1,
            -1,
            false,
            "KNO3",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().POTASSIUM, 1),
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 3));
    public static final Material SODIUM_NITRATE = new Material(
            "Sodium Nitrate",
            MaterialState.SOLID, // State
            null,
            null,
            -1,
            -1,
            -1,
            -1,
            false,
            "NaNO3",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().SODIUM, 1),
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 3));

    public static final Material SOLAR_SALT_COLD = new Material(
            "Solar Salt (Cold)",
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(POTASSIUM_NITRATE, 4),
            new MaterialStack(SODIUM_NITRATE, 6));

    public static final Material SOLAR_SALT_HOT = new Material(
            "Solar Salt (Hot)",
            MaterialState.PURE_LIQUID, // State
            new short[] { 200, 25, 25 }, // Material Colour
            1200, // Melting Point in C
            3300, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(POTASSIUM_NITRATE, 4),
            new MaterialStack(SODIUM_NITRATE, 6));

    public static final Material STRONTIUM_OXIDE = new Material(
            "Strontium Oxide",
            MaterialState.SOLID,
            TextureSet.SET_METALLIC,
            null,
            -1,
            -1,
            -1,
            -1,
            false,
            "SrO",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 1));

    public static final Material SELENIUM_DIOXIDE = new Material(
            "Selenium Dioxide",
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().SELENIUM, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2));

    public static final Material SELENIOUS_ACID = new Material(
            "Selenious Acid",
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(SELENIUM_DIOXIDE, 1),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 8),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 4));

    public static final Material HYDROGEN_CYANIDE = new Material(
            "Hydrogen Cyanide",
            MaterialState.PURE_GAS, // State
            null, // Material Colour
            4, // Melting Point in C
            26, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().CARBON, 1),
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1));

    public static final Material CARBON_MONOXIDE = new Material(
            "Carbon Monoxide",
            MaterialState.PURE_GAS, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().CARBON, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 1));

    public static final Material CARBON_DIOXIDE = new Material(
            "Carbon Dioxide",
            MaterialState.PURE_GAS, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().CARBON, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2));

    public static final Material WOODS_GLASS = new Material(
            "Wood's Glass", // Material Name
            MaterialState.SOLID, // State
            TextureSets.GEM_A.get(), // Texture Set
            new short[] { 220, 60, 255 }, // Material Colour
            -1,
            -1,
            -1,
            -1,
            false,
            "Si4Ba3Na2Ni",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().SILICON, 40),
            new MaterialStack(ELEMENT.getInstance().BARIUM, 30),
            new MaterialStack(ELEMENT.getInstance().SODIUM, 20),
            new MaterialStack(ELEMENT.getInstance().NICKEL, 10));

    /*
     * Rare Earth Materials
     */

    public static final Material RARE_EARTH_LOW = new Material(
            "Rare Earth (I)", // Material Name
            MaterialState.ORE, // State
            TextureSets.GEM_A.get(), // Texture Set
            null, // Material Colour
            1200,
            2500,
            -1,
            -1,
            -1, // Radiation
            new MaterialStack[] { new MaterialStack(ORES.GREENOCKITE, 1), new MaterialStack(ORES.LANTHANITE_CE, 1),
                    new MaterialStack(ORES.AGARDITE_CD, 1), new MaterialStack(ORES.YTTRIALITE, 1),
                    new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.NetherQuartz), 1),
                    new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Galena), 1),
                    new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Chalcopyrite), 1),
                    new MaterialStack(ORES.CRYOLITE, 1), new MaterialStack(ELEMENT.getInstance().YTTRIUM, 1) });

    public static final Material RARE_EARTH_MID = new Material(
            "Rare Earth (II)", // Material Name
            MaterialState.ORE, // State
            TextureSets.ENRICHED.get(), // Texture Set
            null, // Material Colour
            3500,
            5000,
            -1,
            -1,
            -1, // Radiation
            new MaterialStack[] { new MaterialStack(ORES.LANTHANITE_ND, 1), new MaterialStack(ORES.AGARDITE_ND, 1),
                    new MaterialStack(ORES.YTTRIAITE, 1), new MaterialStack(ORES.CROCROITE, 1),
                    new MaterialStack(ORES.NICHROMITE, 1), new MaterialStack(ORES.ZIRCON, 1),
                    new MaterialStack(ELEMENT.STANDALONE.GRANITE, 1),
                    new MaterialStack(ELEMENT.STANDALONE.BLACK_METAL, 1),
                    new MaterialStack(ELEMENT.STANDALONE.RUNITE, 1) });

    public static final Material RARE_EARTH_HIGH = new Material(
            "Rare Earth (III)", // Material Name
            MaterialState.ORE, // State
            TextureSets.REFINED.get(), // Texture Set
            null, // Material Colour
            5200,
            7500,
            -1,
            -1,
            -1, // Radiation
            new MaterialStack[] { new MaterialStack(ORES.GADOLINITE_Y, 1), new MaterialStack(ORES.LEPERSONNITE, 1),
                    new MaterialStack(ORES.FLORENCITE, 1), new MaterialStack(ORES.FLUORCAPHITE, 1),
                    new MaterialStack(ORES.LAUTARITE, 1), new MaterialStack(ORES.DEMICHELEITE_BR, 1),
                    new MaterialStack(ORES.ALBURNITE, 1), new MaterialStack(ORES.SAMARSKITE_Y, 1),
                    new MaterialStack(ORES.AGARDITE_LA, 1), });

    public static final Material WATER = new Material(
            "Water",
            MaterialState.PURE_LIQUID,
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 2),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 1));

    // OH
    public static final Material HYDROXIDE = new Material(
            "Hydroxide", // Material Name
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 1),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1));

    // NH3
    public static final Material AMMONIA = new Material(
            "Ammonia", // Material Name
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -77, // Melting Point in C
            -33, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 3));

    // NH4
    public static final Material AMMONIUM = new Material(
            "Ammonium", // Material Name
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 4));

    public static final Material HYDROGEN_CHLORIDE = new Material(
            "Hydrogen Chloride",
            MaterialState.PURE_GAS,
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 1));

    public static final Material HYDROGEN_CHLORIDE_MIX = new Material(
            "Hydrogen Chloride Mix",
            MaterialState.PURE_LIQUID, // State
            null, // Material Colour
            -1, // Melting Point in C
            -1, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            // Material Stacks with Percentage of required elements.
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 1));

    public static final Material SODIUM_CHLORIDE = new Material(
            "Sodium Chloride",
            MaterialState.PURE_GAS,
            new MaterialStack(ELEMENT.getInstance().SODIUM, 1),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 1));

    public static final Material SALT_WATER = new Material(
            "Salt Water",
            MaterialState.PURE_LIQUID,
            new MaterialStack(WATER, 3),
            new MaterialStack(SODIUM_CHLORIDE, 1));

    public static final Material BRINE = new Material(
            "Brine",
            MaterialState.PURE_LIQUID,
            new MaterialStack(SALT_WATER, 1),
            new MaterialStack(SODIUM_CHLORIDE, 2));

    public static final Material STRONTIUM_HYDROXIDE = new Material(
            "Strontium Hydroxide",
            MaterialState.SOLID,
            TextureSet.SET_METALLIC,
            null,
            -1,
            -1,
            -1,
            -1,
            false,
            "Sr(OH)2",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
            new MaterialStack(MISC_MATERIALS.HYDROXIDE, 2));

    // Glue Chemicals

    public static final Material ACETIC_ANHYDRIDE = new Material(
            "Acetic Anhydride",
            MaterialState.PURE_LIQUID, // State
            new short[] { 250, 240, 110 }, // Material Colour
            -73, // Melting Point in C
            139, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "(CH3CO)2O",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 4),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 6),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 3));

    public static final Material CHLOROACETIC_ACID = new Material(
            "Chloroacetic Acid",
            MaterialState.PURE_LIQUID, // State
            new short[] { 230, 200, 90 }, // Material Colour
            63, // Melting Point in C
            189, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "ClCH2-COOH",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 3),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 1));

    public static final Material DICHLOROACETIC_ACID = new Material(
            "Dichloroacetic Acid",
            MaterialState.PURE_LIQUID, // State
            new short[] { 190, 160, 60 }, // Material Colour
            13, // Melting Point in C
            194, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "Cl2CH-COOH",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 2),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 2));

    public static final Material TRICHLOROACETIC_ACID = new Material(
            "Trichloroacetic Acid",
            MaterialState.PURE_LIQUID, // State
            new short[] { 120, 100, 30 }, // Material Colour
            57, // Melting Point in C
            196, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "Cl3C-COOH",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 3));

    public static final Material CHLOROACETIC_MIXTURE = new Material(
            "Chloroacetic Mixture",
            MaterialState.LIQUID, // State
            null,
            new short[] { 210, 160, 10 },
            40,
            192,
            -1,
            -1,
            false,
            "Cl?H?C-COOH",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 6),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 6),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 6),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 6));

    public static final Material SODIUM_CYANIDE = new Material(
            "Sodium Cyanide",
            MaterialState.SOLID, // State
            new short[] { 180, 190, 255 }, // Material Colour
            563, // Melting Point in C
            1496, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "NaCN",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().SODIUM, 1),
            new MaterialStack(ELEMENT.getInstance().CARBON, 1),
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1));

    public static final Material CALCIUM_CHLORIDE = new Material(
            "Calcium Chloride",
            MaterialState.SOLID, // State
            new short[] { 180, 190, 255 }, // Material Colour
            563, // Melting Point in C
            1496, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "CaCl2",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().CALCIUM, 1),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 2));

    public static final Material CYANOACETIC_ACID = new Material(
            "Cyanoacetic Acid",
            MaterialState.SOLID, // State
            new short[] { 130, 130, 40 }, // Material Colour
            66, // Melting Point in C
            108, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "C3H3NO2",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().CARBON, 3),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 3),
            new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2));

    public static final Material SOLID_ACID_MIXTURE = new Material(
            "Solid Acid Catalyst Mixture",
            MaterialState.LIQUID, // State
            new short[] { 80, 40, 0 }, // Material Colour
            -10, // Melting Point in C
            337, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "?H2SO4?",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 2),
            new MaterialStack(ELEMENT.getInstance().SULFUR, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 4));

    public static final Material COPPER_SULFATE = new Material(
            "Copper(II) Sulfate",
            MaterialState.SOLID, // State
            new short[] { 200, 200, 200 }, // Material Colour
            590, // Melting Point in C
            650, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "CuSO4",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().COPPER, 1),
            new MaterialStack(ELEMENT.getInstance().SULFUR, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 4));

    public static final Material COPPER_SULFATE_HYDRATED = new Material(
            "Copper(II) Sulfate Pentahydrate",
            MaterialState.SOLID, // State
            new short[] { 90, 170, 255 }, // Material Colour
            590, // Melting Point in C
            650, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "CuSO4∙(H2O)5",
            0,
            false,
            new MaterialStack(ELEMENT.getInstance().COPPER, 1),
            new MaterialStack(ELEMENT.getInstance().SULFUR, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 4));

    public static final Material ETHYL_CYANOACETATE = new Material(
            "Ethyl Cyanoacetate",
            MaterialState.PURE_LIQUID, // State
            new short[] { 0, 75, 160 }, // Material Colour
            -22, // Melting Point in C
            210, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "C5H7NO2",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 3));

    public static final Material CYANOACRYLATE_POLYMER = new Material(
            "Cyanoacrylate Polymer",
            MaterialState.LIQUID, // State
            new short[] { 140, 150, 160 }, // Material Colour
            -25, // Melting Point in C
            55, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "(-C6H7NO2-)n",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 3));

    public static final Material ETHYL_CYANOACRYLATE = new Material(
            "Ethyl Cyanoacrylate (Super Glue)",
            MaterialState.PURE_LIQUID, // State
            new short[] { 170, 190, 200 }, // Material Colour
            -25, // Melting Point in C
            55, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "C6H7NO2",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().CARBON, 2),
            new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
            new MaterialStack(ELEMENT.getInstance().OXYGEN, 2),
            new MaterialStack(ELEMENT.getInstance().CHLORINE, 3));

    public static final Material MUTATED_LIVING_SOLDER = new Material(
            "Mutated Living Solder",
            MaterialState.PURE_LIQUID, // State
            new short[] { 147, 109, 155 }, // Material Colour
            -200, // Melting Point in C
            500, // Boiling Point in C
            -1, // Protons
            -1,
            false, // Uses Blast furnace?
            "?Sn?Bi?",
            0,
            true,
            new MaterialStack(ELEMENT.getInstance().TIN, 1),
            new MaterialStack(ELEMENT.getInstance().BISMUTH, 1));
}
