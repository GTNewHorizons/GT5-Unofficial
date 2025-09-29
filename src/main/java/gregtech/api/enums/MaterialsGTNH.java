package gregtech.api.enums;

import static gregtech.GTMod.GT_FML_LOGGER;

import gregtech.api.interfaces.IMaterialHandler;

public class MaterialsGTNH implements IMaterialHandler {

    public static Materials Signalum;
    public static Materials Lumium;
    public static Materials EnrichedCopper;
    public static Materials DiamondCopper;
    public static Materials TarPitch;
    public static Materials LimePure;
    public static Materials Wimalite;
    public static Materials Yellorite;
    public static Materials Turquoise;
    public static Materials Tapazite;
    public static Materials Thyrium;
    public static Materials Tourmaline;
    public static Materials Spinel;
    public static Materials Starconium;
    public static Materials Sugilite;
    public static Materials Prismarine;
    public static Materials GraveyardDirt;
    public static Materials Tennantite;
    public static Materials Fairy;
    public static Materials Ludicrite;
    public static Materials AquaRegia;
    public static Materials SolutionBlueVitriol;
    public static Materials SolutionNickelSulfate;
    public static Materials Lodestone;
    public static Materials Luminite;
    public static Materials Chlorite;
    public static Materials Staurolite;
    public static Materials Cordierite;
    public static Materials Datolite;
    public static Materials MetamorphicMineralMixture;
    public static Materials Plagioclase;
    public static Materials Epidote;

    public MaterialsGTNH() {
        GT_FML_LOGGER.info("Registering GTNH-Materials (post Java 64kb limit)");
        Materials.add(this);
    }

    @Override
    public void onMaterialsInit() {
        MaterialsGTNH.Signalum = loadSignalum();
        MaterialsGTNH.Lumium = loadLumium();
        MaterialsGTNH.EnrichedCopper = loadEnrichedCopper();
        MaterialsGTNH.DiamondCopper = loadDiamondCopper();
        MaterialsGTNH.TarPitch = loadTarPitch();
        MaterialsGTNH.LimePure = loadLimePure();
        MaterialsGTNH.Wimalite = loadWimalite();
        MaterialsGTNH.Yellorite = loadYellorite();
        MaterialsGTNH.Turquoise = loadTurquoise();
        MaterialsGTNH.Tapazite = loadTapazite();
        MaterialsGTNH.Thyrium = loadThyrium();
        MaterialsGTNH.Tourmaline = loadTourmaline();
        MaterialsGTNH.Spinel = loadSpinel();
        MaterialsGTNH.Starconium = loadStarconium();
        MaterialsGTNH.Sugilite = loadSugilite();
        MaterialsGTNH.Prismarine = loadPrismarine();
        MaterialsGTNH.GraveyardDirt = loadGraveyardDirt();
        MaterialsGTNH.Tennantite = loadTennantite();
        MaterialsGTNH.Fairy = loadFairy();
        MaterialsGTNH.Ludicrite = loadLudicrite();
        MaterialsGTNH.AquaRegia = loadAquaRegia();
        MaterialsGTNH.SolutionBlueVitriol = loadSolutionBlueVitriol();
        MaterialsGTNH.SolutionNickelSulfate = loadSolutionNickelSulfate();
        MaterialsGTNH.Lodestone = loadLodestone();
        MaterialsGTNH.Luminite = loadLuminite();
        MaterialsGTNH.Chlorite = loadChlorite();
        MaterialsGTNH.Staurolite = loadStaurolite();
        MaterialsGTNH.Cordierite = loadCordierite();
        MaterialsGTNH.Datolite = loadDatolite();
        MaterialsGTNH.MetamorphicMineralMixture = loadMetamorphicMineralMixture();
        MaterialsGTNH.Plagioclase = loadPlagioclase();
        MaterialsGTNH.Epidote = loadEpidote();

        SubTag.METAL.addTo(Signalum, Lumium, EnrichedCopper, DiamondCopper);
        SubTag.NO_SMASHING.addTo(TarPitch);
    }

    private static Materials loadSignalum() {
        return new MaterialBuilder().setName("Signalum")
            .setDefaultLocalName("Signalum")
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadLumium() {
        return new MaterialBuilder().setName("Lumium")
            .setDefaultLocalName("Lumium")
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadEnrichedCopper() {
        return new Materials(
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
    }

    private static Materials loadDiamondCopper() {
        return new Materials(
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
    }

    private static Materials loadTarPitch() {
        return new Materials(
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
    }

    private static Materials loadLimePure() {
        return new Materials(
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
    }

    private static Materials loadWimalite() {
        return new Materials(
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
    }

    private static Materials loadYellorite() {
        return new Materials(
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
    }

    private static Materials loadTurquoise() {
        return new Materials(
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
    }

    private static Materials loadTapazite() {
        return new Materials(
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
    }

    private static Materials loadThyrium() {
        return new Materials(
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
    }

    private static Materials loadTourmaline() {
        return new Materials(
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
    }

    private static Materials loadSpinel() {
        return new Materials(
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
    }

    private static Materials loadStarconium() {
        return new Materials(
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
    }

    private static Materials loadSugilite() {
        return new Materials(
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
    }

    private static Materials loadPrismarine() {
        return new Materials(
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
    }

    private static Materials loadGraveyardDirt() {
        return new Materials(
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
    }

    private static Materials loadTennantite() {
        return new Materials(
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
    }

    private static Materials loadFairy() {
        return new Materials(
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
    }

    private static Materials loadLudicrite() {
        return new Materials(
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
    }

    private static Materials loadAquaRegia() {
        return new Materials(
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
    }

    private static Materials loadSolutionBlueVitriol() {
        return new Materials(
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
    }

    private static Materials loadSolutionNickelSulfate() {
        return new Materials(
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
    }

    private static Materials loadLodestone() {
        return new Materials(
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
    }

    private static Materials loadLuminite() {
        return new Materials(
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
    }

    private static Materials loadChlorite() {
        return new MaterialBuilder().setName("Chlorite")
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
    }

    private static Materials loadStaurolite() {
        return new MaterialBuilder().setName("Staurolite")
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
    }

    private static Materials loadCordierite() {
        return new MaterialBuilder().setName("Cordierite")
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
    }

    private static Materials loadDatolite() {
        return new MaterialBuilder().setName("Datolite")
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
    }

    private static Materials loadMetamorphicMineralMixture() {
        return new MaterialBuilder().setName("MetamorphicMineralMixture")
            .setDefaultLocalName("Metamorphic Mineral Mixture")
            .setMetaItemSubID(171)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .setARGB(0x006c8294)
            .setColor(Dyes.dyeCyan)
            .constructMaterial();
    }

    private static Materials loadPlagioclase() {
        return new MaterialBuilder().setName("Plagioclase")
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
    }

    private static Materials loadEpidote() {
        return new MaterialBuilder().setName("Epidote")
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
    }
}
