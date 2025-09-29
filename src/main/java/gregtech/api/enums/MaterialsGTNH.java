package gregtech.api.enums;

import static gregtech.GTMod.GT_FML_LOGGER;

import gregtech.api.interfaces.IMaterialHandler;

public class MaterialsGTNH implements IMaterialHandler {

    public MaterialsGTNH() {
        GT_FML_LOGGER.info("Registering GTNH-Materials (post Java 64kb limit)");
        Materials.add(this);
    }

    /**
     * This Class is for adding new Materials since Java has a Limiation of 64kb per Method / Class header
     */
    public static Materials Signalum = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "Signalum",
        "Signalum",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);

    public static Materials Lumium = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "Lumium",
        "Lumium",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials EnrichedCopper = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "EnrichedCopper",
        "Enriched Copper",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials DiamondCopper = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "DiamondCopper",
        "Diamond Copper",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials TarPitch = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "TarPitch",
        "Tar Pitch",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials LimePure = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        0,
        0,
        255,
        255,
        255,
        0,
        "LimePure",
        "Pure Lime",
        0,
        0,
        -1,
        0,
        false,
        false,
        1,
        1,
        1,
        Dyes.dyeLime);
    public static Materials Wimalite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        8,
        255,
        255,
        255,
        0,
        "Wimalite",
        "Wimalite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes.dyeYellow);
    public static Materials Yellorite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        8,
        255,
        255,
        255,
        0,
        "Yellorite",
        "Yellorite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes.dyeYellow);
    public static Materials Turquoise = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1,
        255,
        255,
        255,
        0,
        "Turquoise",
        "Turquoise",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Tapazite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1,
        255,
        255,
        255,
        0,
        "Tapazite",
        "Tapazite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes.dyeGreen);
    public static Materials Thyrium = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1 | 2 | 8,
        255,
        255,
        255,
        0,
        "Thyrium",
        "Thyrium",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Tourmaline = new Materials(
        -1,
        TextureSet.SET_RUBY,
        1.0F,
        0,
        1,
        1,
        255,
        255,
        255,
        0,
        "Tourmaline",
        "Tourmaline",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Spinel = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        0,
        255,
        255,
        255,
        0,
        "Spinel",
        "Spinel",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Starconium = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1 | 2 | 8,
        255,
        255,
        255,
        0,
        "Starconium",
        "Starconium",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Sugilite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1,
        255,
        255,
        255,
        0,
        "Sugilite",
        "Sugilite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Prismarine = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 4,
        255,
        255,
        255,
        0,
        "Prismarine",
        "Prismarine",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials GraveyardDirt = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1,
        255,
        255,
        255,
        0,
        "GraveyardDirt",
        "Graveyard Dirt",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Tennantite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1,
        255,
        255,
        255,
        0,
        "Tennantite",
        "Tennantite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Fairy = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "Fairy",
        "Fairy",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Ludicrite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        1 | 2,
        255,
        255,
        255,
        0,
        "Ludicrite",
        "Ludicrite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials AquaRegia = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        0,
        255,
        255,
        255,
        0,
        "AquaRegia",
        "Aqua Regia",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials SolutionBlueVitriol = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        0,
        255,
        255,
        255,
        0,
        "SolutionBlueVitriol",
        "Blue Vitriol Solution",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials SolutionNickelSulfate = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        2,
        0,
        255,
        255,
        255,
        0,
        "SolutionNickelSulfate",
        "Nickel Sulfate Solution",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes._NULL);
    public static Materials Lodestone = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1 | 8,
        255,
        255,
        255,
        0,
        "Lodestone",
        "Lodestone",
        0,
        0,
        -1,
        0,
        false,
        false,
        1,
        1,
        1,
        Dyes._NULL);
    public static Materials Luminite = new Materials(
        -1,
        TextureSet.SET_NONE,
        1.0F,
        0,
        1,
        1 | 8,
        250,
        250,
        250,
        0,
        "Luminite",
        "Luminite",
        0,
        0,
        -1,
        0,
        false,
        false,
        3,
        1,
        1,
        Dyes.dyeWhite);

    // Deepslate stuff
    public static Materials Chlorite = new MaterialBuilder().setName("Chlorite")
        .setDefaultLocalName("Chlorite")
        .setMetaItemSubID(167)
        .setIconSet(TextureSet.SET_SHINY)
        .addDustItems()
        .setARGB(0x00607d6c)
        .setColor(Dyes.dyeLime)
        .addMaterial(Materials.Iron, 3)
        .addMaterial(Materials.Magnesium, 2)
        .addMaterial(Materials.Aluminiumoxide, 1)
        .addMaterial(Materials.SiliconDioxide, 3)
        .addMaterial(Materials.Water, 4)
        .addMaterial(Materials.Oxygen, 5)
        .constructMaterial();
    public static Materials Staurolite = new MaterialBuilder().setName("Staurolite")
        .setDefaultLocalName("Staurolite")
        .setMetaItemSubID(168)
        .setIconSet(TextureSet.SET_DULL)
        .addDustItems()
        .setARGB(0x003f2816)
        .setColor(Dyes.dyeBrown)
        .addMaterial(Materials.Iron, 2)
        .addMaterial(Materials.Aluminium, 9)
        .addMaterial(Materials.SiliconDioxide, 4)
        .addMaterial(Materials.Oxygen, 16)
        .addMaterial(Materials.Hydrogen, 1)
        .constructMaterial();
    public static Materials Cordierite = new MaterialBuilder().setName("Cordierite")
        .setDefaultLocalName("Cordierite")
        .setMetaItemSubID(169)
        .setIconSet(TextureSet.SET_SHINY)
        .addDustItems()
        .setARGB(0x00434b82)
        .setColor(Dyes.dyePurple)
        .addMaterial(Materials.Iron, 1)
        .addMaterial(Materials.Magnesium, 1)
        .addMaterial(Materials.SiliconDioxide, 5)
        .addMaterial(Materials.Aluminiumoxide, 1)
        .addMaterial(Materials.Oxygen, 5)
        .constructMaterial();
    public static Materials Datolite = new MaterialBuilder().setName("Datolite")
        .setDefaultLocalName("Datolite")
        .setMetaItemSubID(170)
        .setIconSet(TextureSet.SET_SHINY)
        .addDustItems()
        .setARGB(0x00eac4ce)
        .setColor(Dyes.dyeWhite)
        .addMaterial(Materials.Calcium, 1)
        .addMaterial(Materials.Boron, 1)
        .addMaterial(Materials.SiliconDioxide, 1)
        .addMaterial(Materials.Oxygen, 3)
        .addMaterial(Materials.Hydrogen, 1)
        .constructMaterial();
    public static Materials MetamorphicMineralMixture = new MaterialBuilder().setName("MetamorphicMineralMixture")
        .setDefaultLocalName("Metamorphic Mineral Mixture")
        .setMetaItemSubID(171)
        .setIconSet(TextureSet.SET_DULL)
        .addDustItems()
        .setARGB(0x006c8294)
        .setColor(Dyes.dyeCyan)
        .constructMaterial();
    public static Materials Plagioclase = new MaterialBuilder().setName("Plagioclase")
        .setDefaultLocalName("Plagioclase")
        .setMetaItemSubID(172)
        .setIconSet(TextureSet.SET_SHINY)
        .addDustItems()
        .setARGB(0x00c1bab2)
        .setColor(Dyes.dyeWhite)
        .addMaterial(Materials.Sodium, 1)
        .addMaterial(Materials.Aluminium, 1)
        .addMaterial(Materials.Silicon, 3)
        .addMaterial(Materials.Oxygen, 8)
        .constructMaterial();
    public static Materials Epidote = new MaterialBuilder().setName("Epidote")
        .setDefaultLocalName("Epidote")
        .setMetaItemSubID(862)
        .setIconSet(TextureSet.SET_DULL)
        .addDustItems()
        .setARGB(0x008cb121)
        .setColor(Dyes.dyeLime)
        .addMaterial(Materials.Calcium, 2)
        .addMaterial(Materials.Aluminium, 3)
        .addMaterial(Materials.SiliconDioxide, 3)
        .addMaterial(Materials.Oxygen, 1)
        .addMaterial(Materials.Hydrogen, 1)
        .constructMaterial();

    private static void initSubTags() {
        SubTag.METAL.addTo(Signalum, Lumium, EnrichedCopper, DiamondCopper);
        SubTag.NO_SMASHING.addTo(TarPitch);
    }

    @Override
    public void onMaterialsInit() {
        initSubTags();
    }
}
