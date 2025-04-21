package toxiceverglades.gen;

import java.util.Hashtable;

import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

public class WorldGenEvergladesOres {

    public static WorldGenEvergladesOreLayer BaseVein = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein1 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein2 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein3 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein4 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein5 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein6 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein7 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein8 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein9 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein10 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein11 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein12 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein13 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein14 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein15 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein16 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein17 = new WorldGenEvergladesOreLayer(
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
    public static WorldGenEvergladesOreLayer Vein18 = new WorldGenEvergladesOreLayer(
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

    public static WorldGenEvergladesOreLayer Vein19 = new WorldGenEvergladesOreLayer(
        "vein19",
        0,
        20,
        4,
        1,
        16,
        MaterialsElements.STANDALONE.RUNITE,
        MaterialsElements.STANDALONE.RUNITE,
        MaterialsElements.STANDALONE.RUNITE,
        MaterialsElements.STANDALONE.RUNITE);
    public static Hashtable<Long, WorldGenEvergladesOreLayer> validOreveins = new Hashtable<>(1024);

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
        validOreveins.put(ID++, Vein19);
    }
}
