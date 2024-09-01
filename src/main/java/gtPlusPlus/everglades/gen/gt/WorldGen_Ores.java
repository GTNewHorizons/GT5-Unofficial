package gtPlusPlus.everglades.gen.gt;

import java.util.Hashtable;

import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

public class WorldGen_Ores {

    public static WorldGen_GT_Ore_Layer BaseVein = new WorldGen_GT_Ore_Layer(
        "veinA",
        20,
        40,
        1,
        1,
        128,
        MaterialsElements.getInstance().IRON,
        MaterialsElements.getInstance().IRON,
        MaterialsElements.getInstance().IRON,
        MaterialsElements.getInstance().IRON);

    /**
     * Custom ore Veins
     */
    public static WorldGen_GT_Ore_Layer Vein1 = new WorldGen_GT_Ore_Layer(
        "vein1",
        0,
        60,
        30,
        2,
        16,
        MaterialsOres.AGARDITE_CD,
        MaterialsOres.AGARDITE_LA,
        MaterialsOres.DEMICHELEITE_BR,
        MaterialsOres.IRARSITE);

    public static WorldGen_GT_Ore_Layer Vein2 = new WorldGen_GT_Ore_Layer(
        "vein2",
        0,
        60,
        30,
        2,
        16,
        MaterialsOres.AGARDITE_ND,
        MaterialsOres.AGARDITE_Y,
        MaterialsOres.KASHINITE,
        MaterialsOres.CERITE);

    public static WorldGen_GT_Ore_Layer Vein3 = new WorldGen_GT_Ore_Layer(
        "vein3",
        0,
        60,
        30,
        3,
        32,
        MaterialsOres.CERITE,
        MaterialsOres.NICHROMITE,
        MaterialsOres.XENOTIME,
        MaterialsOres.HIBONITE);

    public static WorldGen_GT_Ore_Layer Vein4 = new WorldGen_GT_Ore_Layer(
        "vein4",
        0,
        60,
        40,
        3,
        32,
        MaterialsOres.GEIKIELITE,
        MaterialsOres.CRYOLITE,
        MaterialsOres.GADOLINITE_CE,
        MaterialsOres.AGARDITE_ND);

    public static WorldGen_GT_Ore_Layer Vein5 = new WorldGen_GT_Ore_Layer(
        "vein5",
        30,
        128,
        20,
        2,
        48,
        MaterialsOres.HIBONITE,
        MaterialsOres.YTTRIALITE,
        MaterialsOres.ZIRCONILITE,
        MaterialsOres.CERITE);
    public static WorldGen_GT_Ore_Layer Vein6 = new WorldGen_GT_Ore_Layer(
        "vein6",
        0,
        40,
        20,
        2,
        48,
        MaterialsOres.XENOTIME,
        MaterialsOres.ZIRKELITE,
        MaterialsOres.CROCROITE,
        MaterialsOres.IRARSITE);
    public static WorldGen_GT_Ore_Layer Vein7 = new WorldGen_GT_Ore_Layer(
        "vein7",
        40,
        128,
        20,
        2,
        48,
        MaterialsOres.HONEAITE,
        MaterialsOres.MIESSIITE,
        MaterialsOres.SAMARSKITE_Y,
        MaterialsOres.SAMARSKITE_YB);
    public static WorldGen_GT_Ore_Layer Vein8 = new WorldGen_GT_Ore_Layer(
        "vein8",
        0,
        40,
        20,
        2,
        48,
        MaterialsOres.TITANITE,
        MaterialsOres.ZIMBABWEITE,
        MaterialsOres.ZIRCON,
        MaterialsOres.FLORENCITE);

    public static WorldGen_GT_Ore_Layer Vein9 = new WorldGen_GT_Ore_Layer(
        "vein9",
        10,
        30,
        20,
        1,
        48,
        MaterialsOres.LANTHANITE_CE,
        MaterialsFluorides.FLUORITE,
        MaterialsOres.LAFOSSAITE,
        MaterialsOres.FLORENCITE);
    public static WorldGen_GT_Ore_Layer Vein10 = new WorldGen_GT_Ore_Layer(
        "vein10",
        20,
        50,
        20,
        2,
        32,
        MaterialsOres.GEIKIELITE,
        MaterialsOres.YTTROCERITE,
        MaterialsOres.LANTHANITE_LA,
        MaterialsOres.RADIOBARITE);
    public static WorldGen_GT_Ore_Layer Vein11 = new WorldGen_GT_Ore_Layer(
        "vein11",
        30,
        70,
        20,
        1,
        48,
        MaterialsFluorides.FLUORITE,
        MaterialsOres.KASHINITE,
        MaterialsOres.ZIRCON,
        MaterialsOres.CRYOLITE);
    public static WorldGen_GT_Ore_Layer Vein12 = new WorldGen_GT_Ore_Layer(
        "vein12",
        40,
        80,
        20,
        3,
        32,
        MaterialsOres.CERITE,
        MaterialsOres.ALBURNITE,
        MaterialsOres.MIESSIITE,
        MaterialsOres.HIBONITE);

    /**
     * Best Rarest Veins 2017
     */
    public static WorldGen_GT_Ore_Layer Vein13 = new WorldGen_GT_Ore_Layer(
        "vein13",
        5,
        15,
        5,
        1,
        16,
        MaterialsOres.CRYOLITE,
        MaterialsOres.RADIOBARITE,
        MaterialsOres.HONEAITE,
        MaterialsOres.FLORENCITE);

    public static WorldGen_GT_Ore_Layer Vein14 = new WorldGen_GT_Ore_Layer(
        "vein14",
        10,
        20,
        8,
        2,
        16,
        MaterialsOres.DEMICHELEITE_BR,
        MaterialsOres.PERROUDITE,
        MaterialsOres.IRARSITE,
        MaterialsOres.RADIOBARITE);

    public static WorldGen_GT_Ore_Layer Vein15 = new WorldGen_GT_Ore_Layer(
        "vein15",
        5,
        25,
        5,
        3,
        24,
        MaterialsOres.FLUORCAPHITE,
        MaterialsOres.LAFOSSAITE,
        MaterialsOres.GADOLINITE_CE,
        MaterialsOres.GADOLINITE_Y);

    public static WorldGen_GT_Ore_Layer Vein16 = new WorldGen_GT_Ore_Layer(
        "vein16",
        0,
        25,
        4,
        2,
        32,
        MaterialsOres.YTTROCERITE,
        MaterialsOres.LEPERSONNITE,
        MaterialsOres.LAUTARITE,
        MaterialsFluorides.FLUORITE);

    public static WorldGen_GT_Ore_Layer Vein17 = new WorldGen_GT_Ore_Layer(
        "vein17",
        10,
        35,
        4,
        1,
        32,
        MaterialsOres.FLORENCITE,
        MaterialsOres.LAUTARITE,
        MaterialsOres.SAMARSKITE_YB,
        MaterialsOres.POLYCRASE);
    public static WorldGen_GT_Ore_Layer Vein18 = new WorldGen_GT_Ore_Layer(
        "vein18",
        15,
        40,
        4,
        1,
        48,
        MaterialsOres.GADOLINITE_CE,
        MaterialsOres.GADOLINITE_Y,
        MaterialsOres.AGARDITE_LA,
        MaterialsOres.AGARDITE_CD);

    public static Hashtable<Long, WorldGen_GT_Ore_Layer> validOreveins = new Hashtable<>(1024);

    static long ID = 0;

    public static void generateValidOreVeins() {
        validOreveins.put(ID++, BaseVein);
        validOreveins.put(ID++, Vein1);
        validOreveins.put(ID++, Vein2);
        validOreveins.put(ID++, Vein3);
        validOreveins.put(ID++, Vein4);
        validOreveins.put(ID++, Vein5);
        validOreveins.put(ID++, Vein6);
        validOreveins.put(ID++, Vein7);
        validOreveins.put(ID++, Vein8);
        validOreveins.put(ID++, Vein9);
        validOreveins.put(ID++, Vein10);
        validOreveins.put(ID++, Vein11);
        validOreveins.put(ID++, Vein12);
        validOreveins.put(ID++, Vein13);
        validOreveins.put(ID++, Vein14);
        validOreveins.put(ID++, Vein15);
        validOreveins.put(ID++, Vein16);
        validOreveins.put(ID++, Vein17);
        validOreveins.put(ID++, Vein18);
    }
}
