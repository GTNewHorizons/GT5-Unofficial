package gregtech.api.enums;

import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.rod;
import static gregtech.api.enums.OrePrefixes.rotor;

public class MaterialsBotania {

    public static Materials Manasteel;
    public static Materials Terrasteel;
    public static Materials ElvenElementium;
    public static Materials Livingrock;
    public static Materials GaiaSpirit;
    public static Materials Livingwood;
    public static Materials Dreamwood;
    public static Materials ManaDiamond;
    public static Materials BotaniaDragonstone;

    public static void load() {
        MaterialsBotania.Manasteel = loadManasteel();
        MaterialsBotania.Terrasteel = loadTerrasteel();
        MaterialsBotania.ElvenElementium = loadElvenElementium();
        MaterialsBotania.Livingrock = loadLivingrock();
        MaterialsBotania.GaiaSpirit = loadGaiaSpirit();
        MaterialsBotania.Livingwood = loadLivingwood();
        MaterialsBotania.Dreamwood = loadDreamwood();
        MaterialsBotania.ManaDiamond = loadManaDiamond();
        MaterialsBotania.BotaniaDragonstone = loadBotaniaDragonstone();

        Livingrock.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Livingwood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        Dreamwood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        ManaDiamond.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        BotaniaDragonstone.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        GaiaSpirit.add(SubTag.SOFT);

        // Botania native items
        ingot.mNotGeneratedItems.add(Manasteel);
        ingot.mNotGeneratedItems.add(Terrasteel);
        ingot.mNotGeneratedItems.add(ElvenElementium);
        ingot.mNotGeneratedItems.add(GaiaSpirit);
        nugget.mNotGeneratedItems.add(Manasteel);
        nugget.mNotGeneratedItems.add(Terrasteel);
        nugget.mNotGeneratedItems.add(ElvenElementium);
        gem.mNotGeneratedItems.add(ManaDiamond);
        gem.mNotGeneratedItems.add(BotaniaDragonstone);

        // other stuff we don't want
        ingot.mNotGeneratedItems.add(Livingwood);
        ingot.mNotGeneratedItems.add(Dreamwood);
        nugget.mNotGeneratedItems.add(Livingwood);
        nugget.mNotGeneratedItems.add(Dreamwood);
        rotor.mNotGeneratedItems.add(Livingrock);

        // stuff we want
        plate.mGeneratedItems.add(Livingrock);
        rod.mGeneratedItems.add(Livingrock); // this is not working
    }

    private static Materials loadManasteel() {
        return new MaterialBuilder().setName("Manasteel")
            .setDefaultLocalName("Manasteel")
            .setChemicalFormula("Ms")
            .setMetaItemSubID(201)
            .setIconSet(new TextureSet("Manasteel", false))
            .setARGB(0xff46aae6)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(5_120, 4, 8.0f)
            .setMeltingPoint(1_500)
            .setBlastFurnaceTemp(1_500)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadTerrasteel() {
        return new MaterialBuilder().setName("Terrasteel")
            .setDefaultLocalName("Terrasteel")
            .setChemicalFormula("Tr")
            .setMetaItemSubID(202)
            .setIconSet(new TextureSet("Manasteel", false))
            .setARGB(0xff46c800)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(10_240, 5, 32.0f)
            .setMeltingPoint(5_400)
            .setBlastFurnaceTemp(5_400)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadElvenElementium() {
        return new MaterialBuilder().setName("ElvenElementium")
            .setDefaultLocalName("Elven Elementium")
            .setChemicalFormula("Ef")
            .setMetaItemSubID(203)
            .setIconSet(new TextureSet("Manasteel", false))
            .setARGB(0xffff2df0)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setTool(32_768, 7, 20.0f)
            .setMeltingPoint(7_200)
            .setBlastFurnaceTemp(7_200)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.AURAM, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadLivingrock() {
        return new MaterialBuilder().setName("Livingrock")
            .setDefaultLocalName("Livingrock")
            .setChemicalFormula("Lv")
            .setMetaItemSubID(204)
            .setIconSet(new TextureSet("Livingrock", true))
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.TERRA, 2)
            .addAspect(TCAspects.VICTUS, 2)
            .constructMaterial();
    }

    private static Materials loadGaiaSpirit() {
        return new MaterialBuilder().setName("GaiaSpirit")
            .setDefaultLocalName("Gaia Spirit")
            .setChemicalFormula("Gs")
            .setMetaItemSubID(205)
            .setIconSet(new TextureSet("GaiaSpirit", true))
            .setTool(850_000, 12, 32.0f)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadLivingwood() {
        return new MaterialBuilder().setName("Livingwood")
            .setDefaultLocalName("Livingwood")
            .setChemicalFormula("Lw")
            .setMetaItemSubID(206)
            .setIconSet(new TextureSet("Livingwood", true))
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.ARBOR, 4)
            .addAspect(TCAspects.VICTUS, 2)
            .constructMaterial();
    }

    private static Materials loadDreamwood() {
        return new MaterialBuilder().setName("Dreamwood")
            .setDefaultLocalName("Dreamwood")
            .setChemicalFormula("Dw")
            .setMetaItemSubID(207)
            .setIconSet(new TextureSet("Dreamwood", true))
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.ARBOR, 4)
            .addAspect(TCAspects.AURAM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadManaDiamond() {
        return new MaterialBuilder().setName("ManaDiamond")
            .setDefaultLocalName("Mana Diamond")
            .setChemicalFormula("Ma₄C")
            .setMetaItemSubID(208)
            .setIconSet(new TextureSet("ManaDiamond", true))
            .setTool(2_560, 8, 16.0f)
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadBotaniaDragonstone() {
        return new MaterialBuilder().setName("BotaniaDragonstone")
            .setDefaultLocalName("Dragonstone")
            .setChemicalFormula("Dg")
            .setMetaItemSubID(209)
            .setIconSet(new TextureSet("Dragonstone", true))
            .setTool(3_840, 12, 24.0f)
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }
}
