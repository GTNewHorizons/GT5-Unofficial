package gregtech.api.enums;

import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.rod;
import static gregtech.api.enums.OrePrefixes.rotor;

import java.util.Arrays;

import gregtech.api.enums.TCAspects.TC_AspectStack;

public class MaterialsBotania {

    // Botania materials.
    public static Materials Manasteel = new MaterialBuilder(201, TextureSet.SET_METALLIC, "Manasteel")
        .setName("Manasteel")
        .setRGBA(150, 219, 252, 255)
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(8.0F)
        .setDurability(5120)
        .setToolQuality(4)
        .setMeltingPoint(1500)
        .setBlastFurnaceTemp(1500)
        .setBlastFurnaceRequired(true)
        .setAspects(
            Arrays.asList(new TC_AspectStack(TCAspects.METALLUM, 3), new TC_AspectStack(TCAspects.PRAECANTATIO, 1)))
        .constructMaterial();
    public static Materials Terrasteel = new MaterialBuilder(202, TextureSet.SET_METALLIC, "Terrasteel")
        .setName("Terrasteel")
        .setRGBA(76, 191, 38, 255)
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(32.0F)
        .setDurability(10240)
        .setToolQuality(5)
        .setMeltingPoint(5400)
        .setBlastFurnaceTemp(5400)
        .setBlastFurnaceRequired(true)
        .setAspects(
            Arrays.asList(
                new TC_AspectStack(TCAspects.METALLUM, 2),
                new TC_AspectStack(TCAspects.TERRA, 1),
                new TC_AspectStack(TCAspects.PRAECANTATIO, 1)))
        .constructMaterial();
    public static Materials ElvenElementium = new MaterialBuilder(203, TextureSet.SET_METALLIC, "Elven Elementium")
        .setName("ElvenElementium")
        .setRGBA(219, 37, 205, 255)
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(20.0F)
        .setDurability(32768)
        .setToolQuality(7)
        .setMeltingPoint(7200)
        .setBlastFurnaceTemp(7200)
        .setBlastFurnaceRequired(true)
        .setAspects(
            Arrays.asList(
                new TC_AspectStack(TCAspects.METALLUM, 3),
                new TC_AspectStack(TCAspects.PRAECANTATIO, 2),
                new TC_AspectStack(TCAspects.AURAM, 1)))
        .constructMaterial();
    public static Materials Livingrock = new MaterialBuilder(204, new TextureSet("Livingrock", true), "Livingrock")
        .setName("Livingrock")
        .addDustItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(1.0F)
        .setDurability(0)
        .setToolQuality(3)
        .setOreValue(3)
        .setDensityMultiplier(1)
        .setDensityDivider(1)
        .setAspects(Arrays.asList(new TC_AspectStack(TCAspects.TERRA, 2), new TC_AspectStack(TCAspects.VICTUS, 2)))
        .constructMaterial();
    public static Materials GaiaSpirit = new Materials(
        205,
        TextureSet.SET_METALLIC.withBlockTextures("GaiaSpirit"),
        32.0F,
        850000,
        12,
        1 | 2 | 64 | 128,
        255,
        255,
        255,
        0,
        "GaiaSpirit",
        "Gaia Spirit",
        -1,
        -1,
        0,
        0,
        false,
        true,
        2,
        1,
        1,
        Dyes._NULL,
        Arrays.asList(
            new TC_AspectStack(TCAspects.PRAECANTATIO, 27),
            new TC_AspectStack(TCAspects.AURAM, 24),
            new TC_AspectStack(TCAspects.VICTUS, 24),
            new TC_AspectStack(TCAspects.METALLUM, 1)));
    public static Materials Livingwood = new MaterialBuilder(206, new TextureSet("Livingwood", true), "Livingwood")
        .setName("Livingwood")
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(1.0F)
        .setDurability(0)
        .setToolQuality(3)
        .setOreValue(3)
        .setDensityMultiplier(1)
        .setDensityDivider(1)
        .setAspects(Arrays.asList(new TC_AspectStack(TCAspects.ARBOR, 4), new TC_AspectStack(TCAspects.VICTUS, 2)))
        .constructMaterial();
    public static Materials Dreamwood = new MaterialBuilder(207, new TextureSet("Dreamwood", true), "Dreamwood")
        .setName("Dreamwood")
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(1.0F)
        .setDurability(0)
        .setToolQuality(3)
        .setOreValue(3)
        .setDensityMultiplier(1)
        .setDensityDivider(1)
        .setAspects(
            Arrays.asList(
                new TC_AspectStack(TCAspects.ARBOR, 4),
                new TC_AspectStack(TCAspects.AURAM, 2),
                new TC_AspectStack(TCAspects.PRAECANTATIO, 1)))
        .constructMaterial();
    public static Materials ManaDiamond = new Materials(
        208,
        TextureSet.SET_DIAMOND,
        16.0F,
        2560,
        8,
        1 | 4,
        38,
        237,
        224,
        255,
        "ManaDiamond",
        "Mana Diamond",
        -1,
        -1,
        0,
        0,
        false,
        true,
        2,
        1,
        1,
        Dyes._NULL,
        Arrays.asList(
            new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 4),
            new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4),
            new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 4)));
    public static Materials BotaniaDragonstone = new Materials(
        209,
        TextureSet.SET_DIAMOND,
        24.0F,
        3840,
        12,
        1 | 4,
        242,
        44,
        239,
        255,
        "BotaniaDragonstone",
        "Dragonstone",
        -1,
        -1,
        0,
        0,
        false,
        true,
        2,
        1,
        1,
        Dyes._NULL,
        Arrays.asList(
            new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 6),
            new TCAspects.TC_AspectStack(TCAspects.VITREUS, 6),
            new TCAspects.TC_AspectStack(TCAspects.AURAM, 4)));

    public static void init() {
        GaiaSpirit.mChemicalFormula = "Gs";
        Manasteel.mChemicalFormula = "Ms";
        Livingwood.mChemicalFormula = "Lw";
        Dreamwood.mChemicalFormula = "Dw";
        BotaniaDragonstone.mChemicalFormula = "Dg";
        Livingrock.mChemicalFormula = "Lv";
        Terrasteel.mChemicalFormula = "Tr";
        ElvenElementium.mChemicalFormula = "Ef";

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
}
