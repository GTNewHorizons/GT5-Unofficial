package gregtech.api.enums;

import static gregtech.api.enums.OrePrefixes.*;

import java.util.Arrays;

import gregtech.api.enums.TC_Aspects.TC_AspectStack;

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
            Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 3), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)))
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
                new TC_AspectStack(TC_Aspects.METALLUM, 2),
                new TC_AspectStack(TC_Aspects.TERRA, 1),
                new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)))
        .constructMaterial();
    public static Materials MagicMithrill = new MaterialBuilder(210, TextureSet.SET_METALLIC, "Magic Mithrill")
        .setName("MagicMithrill")
        .setRGBA(15, 232, 198, 1)
        .addDustItems()
        .addMetalItems()
        .addToolHeadItems()
        .addGearItems()
        .setToolSpeed(40.0F)
        .setDurability(1000000)
        .setToolQuality(12)
        .setMeltingPoint(5400)
        .setBlastFurnaceTemp(5400)
        .setBlastFurnaceRequired(true)
        .setAspects(
            Arrays.asList(
                new TC_AspectStack(TC_Aspects.METALLUM, 12),
                new TC_AspectStack(TC_Aspects.TERRA, 10),
                new TC_AspectStack(TC_Aspects.GELUM, 10),
                new TC_AspectStack(TC_Aspects.PRAECANTATIO, 10)))
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
                new TC_AspectStack(TC_Aspects.METALLUM, 3),
                new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2),
                new TC_AspectStack(TC_Aspects.AURAM, 1)))
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
        .setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.TERRA, 2), new TC_AspectStack(TC_Aspects.VICTUS, 2)))
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
            new TC_AspectStack(TC_Aspects.PRAECANTATIO, 27),
            new TC_AspectStack(TC_Aspects.AURAM, 24),
            new TC_AspectStack(TC_Aspects.VICTUS, 24),
            new TC_AspectStack(TC_Aspects.METALLUM, 1)));
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
        .setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.ARBOR, 4), new TC_AspectStack(TC_Aspects.VICTUS, 2)))
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
                new TC_AspectStack(TC_Aspects.ARBOR, 4),
                new TC_AspectStack(TC_Aspects.AURAM, 2),
                new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)))
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
            new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4),
            new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4),
            new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 4)));
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
            new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 4)));

    public static void init() {
        GaiaSpirit.mChemicalFormula = "Gs";
        Manasteel.mChemicalFormula = "Ms";
        Livingwood.mChemicalFormula = "Lw";
        Dreamwood.mChemicalFormula = "Dw";
        BotaniaDragonstone.mChemicalFormula = "Dg";
        Livingrock.mChemicalFormula = "Lv";
        Terrasteel.mChemicalFormula = "Tr";
        ElvenElementium.mChemicalFormula = "Ef";
        MagicMithrill.mChemicalFormula = "Mi";

        Livingrock.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Livingwood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        Dreamwood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        ManaDiamond.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        BotaniaDragonstone.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);

        // Botania native items
        ingot.mNotGeneratedItems.add(Manasteel);
        ingot.mNotGeneratedItems.add(Terrasteel);
        ingot.mNotGeneratedItems.add(ElvenElementium);
        ingot.mNotGeneratedItems.add(GaiaSpirit);
        ingot.mNotGeneratedItems.add(MagicMithrill);
        nugget.mNotGeneratedItems.add(Manasteel);
        nugget.mNotGeneratedItems.add(Terrasteel);
        nugget.mNotGeneratedItems.add(ElvenElementium);
        nugget.mNotGeneratedItems.add(MagicMithrill);
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
