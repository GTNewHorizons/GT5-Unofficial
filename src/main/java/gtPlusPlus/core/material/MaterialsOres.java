package gtPlusPlus.core.material;

import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.client.CustomTextureSet.TextureSets;
import gtPlusPlus.core.material.state.MaterialState;

public final class MaterialsOres {

    public static final Material AGARDITE_CD = new Material(
        "Agardite (Cd)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 170, 188, 33, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (CdCa)Cu7(AsO2)4(O2H)5·3H2O
            new MaterialStack(MaterialsElements.getInstance().CADMIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().COPPER, 7),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 4),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 21),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 11) });

    public static final Material AGARDITE_LA = new Material(
        "Agardite (La)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 206, 232, 9, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (LaCa)Cu5(AsO6)2(OH)4·3H2O
            new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().COPPER, 5),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 19),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 10) });

    public static final Material AGARDITE_ND = new Material(
        "Agardite (Nd)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 225, 244, 78, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (NdCa)Cu6(As3O3)2(O2H)6·3H2O
            new MaterialStack(MaterialsElements.getInstance().NEODYMIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().COPPER, 6),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 6),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 21),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 12) });

    public static final Material AGARDITE_Y = new Material(
        "Agardite (Y)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 210, 232, 44, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (YCa)Cu5(As2O4)3(OH)6·3H2O
            new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().COPPER, 5),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 6),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 21),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 12) });

    // Alburnite
    // Ag8GeTe2S4
    public static final Material ALBURNITE = new Material(
        "Alburnite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 16, 5, 105, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().GOLD, 8),
            new MaterialStack(MaterialsElements.getInstance().GERMANIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().TELLURIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 4) });

    public static final Material CERITE = new Material(
        "Cerite", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 68, 13, 0, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (Ce,La,Ca)9(Mg,Fe+3)(SiO4)6(SiO3OH)(OH)3
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 9),
            new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 9),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 9),
            new MaterialStack(MaterialsElements.getInstance().MAGNESIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().IRON, 3),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 7),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 20),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 4) });

    // Comancheite
    // Hg55N24(NH2,OH)4(Cl,Br)34
    public static final Material COMANCHEITE = new Material(
        "Comancheite", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 65, 205, 105, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().MERCURY, 54 / 4),
            new MaterialStack(MaterialsElements.getInstance().NITROGEN, 28 / 4),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 12 / 4),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 8 / 4),
            new MaterialStack(MaterialsElements.getInstance().CHLORINE, 34 / 4),
            new MaterialStack(MaterialsElements.getInstance().BROMINE, 34 / 4) });

    public static final Material CROCROITE = new Material(
        "Crocoite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_GEM_VERTICAL, // Texture Set
        new short[] { 255, 143, 84, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().LEAD, 2),
            new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3),
            new MaterialStack(MaterialsElements.getInstance().CAESIUM, 1), });

    public static final Material CRYOLITE = new Material(
        "Cryolite (F)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_SHINY, // Texture Set
        new short[] { 205, 205, 255, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().SODIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().FLUORINE, 6) });

    // Demicheleite-(Br)
    // BiSBr
    public static final Material DEMICHELEITE_BR = new Material(
        "Demicheleite (Br)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_SHINY, // Texture Set
        new short[] { 165, 75, 75, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().BISMUTH, 13),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 11),
            new MaterialStack(MaterialsElements.getInstance().BROMINE, 1) });

    public static final Material FLORENCITE = new Material(
        "Florencite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 249, 249, 124, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // SmAl3(PO4)2(OH)6
            new MaterialStack(MaterialsElements.getInstance().SAMARIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 10),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 6) });

    public static final Material FLUORCAPHITE = new Material(
        "Fluorcaphite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 255, 255, 30, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (Ca,Sr,Ce,Na)5(PO4)3F
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 5),
            new MaterialStack(MaterialMisc.STRONTIUM_OXIDE, 5),
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 5),
            new MaterialStack(MaterialsElements.getInstance().SODIUM, 5),
            new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 3),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 12),
            new MaterialStack(MaterialsElements.getInstance().FLUORINE, 6), });

    // Gadolinite_Ce
    public static final Material GADOLINITE_CE = new Material(
        "Gadolinite (Ce)", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 15, 159, 59, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CERIUM, 4),
            new MaterialStack(MaterialsElements.getInstance().ERBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NEODYMIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().GADOLINIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().BERYLLIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 7),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 14), });

    // Gadolinite_Y
    public static final Material GADOLINITE_Y = new Material(
        "Gadolinite (Y)", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 35, 189, 99, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CERIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().ERBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NEODYMIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 4),
            new MaterialStack(MaterialsElements.getInstance().GADOLINIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().BERYLLIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 4),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 9), });

    public static final Material GEIKIELITE = new Material(
        "Geikielite", // Material Name
        MaterialState.ORE, // State
        TextureSets.GEM_A.get(), // Texture Set
        new short[] { 187, 193, 204, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().MAGNESIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3) });

    public static final Material GREENOCKITE = new Material(
        "Greenockite", // Material Name
        MaterialState.ORE, // State
        TextureSets.GEM_A.get(), // Texture Set
        new short[] { 110, 193, 25, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CADMIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 1), });

    public static final Material HIBONITE = new Material(
        "Hibonite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 58, 31, 0, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // ((Ca,Ce)(Al,Ti,Mg)12O19)
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 12),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 12),
            new MaterialStack(MaterialsElements.getInstance().MAGNESIUM, 12),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 19), });

    // Honeaite
    // Au3TlTe2
    public static final Material HONEAITE = new Material(
        "Honeaite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 165, 165, 5, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().GOLD, 3),
            new MaterialStack(MaterialsElements.getInstance().THALLIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().TELLURIUM, 2) });

    // Irarsite
    // (Ir,Ru,Rh,Pt)AsS
    public static final Material IRARSITE = new Material(
        "Irarsite", // Material Name
        MaterialState.ORE, // State
        TextureSets.ENRICHED.get(), // Texture Set
        new short[] { 125, 105, 105, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().IRIDIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().RUTHENIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().RHODIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().PLATINUM, 1),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 1),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 1) });

    // Kashinite
    // (Ir,Rh)2S3
    public static final Material KASHINITE = new Material(
        "Kashinite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_SHINY, // Texture Set
        new short[] { 75, 105, 75, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().IRIDIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().RHODIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 3) });

    // Tl(Cl,Br)
    public static final Material LAFOSSAITE = new Material(
        "Lafossaite", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 165, 105, 205, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().CHLORINE, 1),
            new MaterialStack(MaterialsElements.getInstance().BROMINE, 1),
            new MaterialStack(MaterialsElements.getInstance().THALLIUM, 1) });

    public static final Material LANTHANITE_CE = new Material(
        "Lanthanite (Ce)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 186, 113, 179, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (Ce)2(CO3)3·8(H2O)
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 1), });

    public static final Material LANTHANITE_LA = new Material(
        "Lanthanite (La)", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 219, 160, 214, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (La)2(CO3)3·8(H2O)
            new MaterialStack(MaterialsElements.getInstance().LANTHANUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 1), });

    public static final Material LANTHANITE_ND = new Material(
        "Lanthanite (Nd)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 153, 76, 145, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (Nd)2(CO3)3·8(H2O)
            new MaterialStack(MaterialsElements.getInstance().NEODYMIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 1), });

    // Iodine Source
    public static final Material LAUTARITE = new Material(
        "Lautarite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 165, 105, 205, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().IODINE, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 6) });

    public static final Material LEPERSONNITE = new Material(
        "Lepersonnite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_EMERALD, // Texture Set
        new short[] { 175, 175, 20, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().YTTERBIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().GADOLINIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().DYSPROSIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().URANIUM235, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 29),
            new MaterialStack(MaterialsElements.getInstance().HYDROGEN, 24) });

    // Miessiite
    // Pd11Te2Se2
    public static final Material MIESSIITE = new Material(
        "Miessiite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 75, 75, 75, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().PALLADIUM, 11),
            new MaterialStack(MaterialsElements.getInstance().TELLURIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SELENIUM, 2) });

    public static final Material NICHROMITE = new Material(
        "Nichromite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 22, 19, 19, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().NICKEL, 1),
            new MaterialStack(MaterialsElements.getInstance().COBALT, 1),
            new MaterialStack(MaterialsElements.getInstance().IRON, 3),
            new MaterialStack(MaterialsElements.getInstance().ALUMINIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().CHROMIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 8) });

    // Perroudite
    // Hg5Ag4S5(I,Br)2Cl2
    public static final Material PERROUDITE = new Material(
        "Perroudite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 77, 165, 174, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().SULFUR, 5),
            new MaterialStack(MaterialsElements.getInstance().SILVER, 4),
            new MaterialStack(MaterialsElements.getInstance().IODINE, 2),
            new MaterialStack(MaterialsElements.getInstance().MERCURY, 5),
            new MaterialStack(MaterialsElements.getInstance().BROMINE, 2),
            new MaterialStack(MaterialsElements.getInstance().CHLORINE, 2) });

    public static final Material POLYCRASE = new Material(
        "Polycrase", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_ROUGH, // Texture Set
        new short[] { 51, 0, 11, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().URANIUM235, 1),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().TANTALUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 6) });

    // Radiobarite
    // Radium, Barium, Barite?
    public static final Material RADIOBARITE = new Material(
        "Barite (Ra)", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FLINT, // Texture Set
        new short[] { 205, 205, 205, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        0, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().BARIUM, 32),
            new MaterialStack(MaterialsElements.getInstance().RADIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().SULFUR, 16),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 15) });

    // Samarskite_Y
    public static final Material SAMARSKITE_Y = new Material(
        "Samarskite (Y)", // Material Name
        MaterialState.ORE, // State
        TextureSets.ENRICHED.get(), // Texture Set
        new short[] { 65, 163, 164, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 2), // Y not YT/YB
            new MaterialStack(MaterialsElements.getInstance().IRON, 10),
            new MaterialStack(MaterialsElements.getInstance().URANIUM235, 2),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().TANTALUM, 3) });

    // Samarskite_YB
    public static final Material SAMARSKITE_YB = new Material(
        "Samarskite (Yb)", // Material Name
        MaterialState.ORE, // State
        TextureSets.ENRICHED.get(), // Texture Set
        new short[] { 95, 193, 194, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTERBIUM, 2), // Y not YT/YB
            new MaterialStack(MaterialsElements.getInstance().IRON, 9),
            new MaterialStack(MaterialsElements.getInstance().URANIUM235, 3),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().TANTALUM, 2) });

    public static final Material TITANITE = new Material(
        "Titanite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 184, 198, 105, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CALCIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 2),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 10) });

    public static final Material XENOTIME = new Material(
        "Xenotime", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_OPAL, // Texture Set
        new short[] { 235, 89, 199, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().YTTERBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().ERBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().EUROPIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 8) });

    public static final Material YTTRIAITE = new Material( // TODO
        "Yttriaite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_METALLIC, // Texture Set
        new short[] { 255, 143, 84, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 1), // Y not YT/YB
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 3),
            new MaterialStack(MaterialsElements.getInstance().IRON, 4),
            new MaterialStack(MaterialsElements.getInstance().TIN, 1),
            new MaterialStack(MaterialsElements.getInstance().NITROGEN, 2) });

    public static final Material YTTRIALITE = new Material(
        "Yttrialite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_RUBY, // Texture Set
        new short[] { 35, 189, 99, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 7), });

    public static final Material YTTROCERITE = new Material(
        "Yttrocerite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_DIAMOND, // Texture Set
        new short[] { 35, 19, 199, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CERIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().FLUORINE, 5),
            new MaterialStack(MaterialsElements.getInstance().YTTRIUM, 1), });

    public static final Material ZIMBABWEITE = new Material(
        "Zimbabweite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 193, 187, 131, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CALCIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().POTASSIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().LEAD, 1),
            new MaterialStack(MaterialsElements.getInstance().ARSENIC, 4),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 4),
            new MaterialStack(MaterialsElements.getInstance().TANTALUM, 4),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 4),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 18) });

    public static final Material ZIRCON = new Material(
        "Zircon", // Material Name
        MaterialState.ORE, // State
        TextureSets.GEM_A.get(), // Texture Set
        new short[] { 195, 19, 19, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().ZIRCONIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 4), });

    public static final Material ZIRCONILITE = new Material(
        "Zirconolite", // Material Name
        MaterialState.ORE, // State
        TextureSet.SET_FINE, // Texture Set
        new short[] { 45, 26, 0, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().CALCIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().ZIRCONIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 4),
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 14) });

    public static final Material ZIRCOPHYLLITE = new Material(
        "Zircophyllite", // Material Name
        MaterialState.ORE, // State
        TextureSets.REFINED.get(), // Texture Set
        new short[] { 30, 0, 6, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { new MaterialStack(MaterialsElements.getInstance().POTASSIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().SODIUM, 3),
            new MaterialStack(MaterialsElements.getInstance().MANGANESE, 7),
            new MaterialStack(MaterialsElements.getInstance().IRON, 7),
            new MaterialStack(MaterialsElements.getInstance().ZIRCONIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().SILICON, 8),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 13),
            new MaterialStack(MaterialsElements.getInstance().FLUORINE, 7), });

    public static final Material ZIRKELITE = new Material(
        "Zirkelite", // Material Name
        MaterialState.ORE, // State
        TextureSets.GEM_A.get(), // Texture Set
        new short[] { 229, 208, 48, 0 }, // Material Colour
        -1,
        -1,
        -1,
        -1,
        -1, // Radiation
        new MaterialStack[] { // (Ca,Th,Ce)Zr(Ti,Nb)2O7
            new MaterialStack(MaterialsElements.getInstance().CALCIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().CERIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().ZIRCONIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().TITANIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().NIOBIUM, 2),
            new MaterialStack(MaterialsElements.getInstance().OXYGEN, 7) });

    public static final Material DEEP_EARTH_REACTOR_FUEL_DEPOSIT = new Material(
        "Radioactive Mineral Mix", // Material Name
        MaterialState.ORE, // State
        TextureSets.NUCLEAR.get(), // Texture Set
        null, // Material Colour
        -1,
        -1,
        -1,
        -1,
        4, // Radiation
        new MaterialStack[] { // Na3AlF6
            new MaterialStack(MaterialsElements.getInstance().RADON, 2),
            new MaterialStack(MaterialsElements.getInstance().RADIUM, 1),
            new MaterialStack(MaterialsElements.getInstance().URANIUM235, 1),
            new MaterialStack(MaterialsElements.getInstance().URANIUM238, 10),
            new MaterialStack(MaterialsElements.getInstance().THORIUM, 25),
            new MaterialStack(MaterialsElements.getInstance().THORIUM232, 4), new MaterialStack(FLUORCAPHITE, 6),
            new MaterialStack(SAMARSKITE_Y, 8), new MaterialStack(TITANITE, 4) });
}
