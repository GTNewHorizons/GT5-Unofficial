package gregtech.api.enums;

import gregtech.api.objects.MaterialStack;

import java.util.Arrays;

public class MaterialsBotania {

    // Botania materials.
    public static Materials Manasteel               = new MaterialBuilder(201, TextureSet.SET_METALLIC, "Manasteel").setName("Manasteel").setRGBA(150,219,252,255).addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(8.0F).setDurability(512).setToolQuality(3).setMeltingPoint(1500).setBlastFurnaceTemp(1500).setBlastFurnaceRequired(true).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4))).constructMaterial();
    public static Materials Terrasteel              = new MaterialBuilder(202, TextureSet.SET_METALLIC, "Terrasteel").setName("Terrasteel").setRGBA(76, 191, 38, 255).addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(32.0F).setDurability(10240).setToolQuality(5).setMeltingPoint(5400).setBlastFurnaceTemp(5400).setBlastFurnaceRequired(true).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 8))).constructMaterial();
    public static Materials ElvenElementium         = new MaterialBuilder(203, TextureSet.SET_METALLIC, "Elementium").setName("Elementium").setRGBA(219, 37, 205, 255).addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(20.0F).setDurability(32768).setToolQuality(7).setMeltingPoint(7200).setBlastFurnaceTemp(7200).setBlastFurnaceRequired(true).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 16))).constructMaterial();
    public static Materials Livingrock              = new MaterialBuilder(204, new TextureSet("Livingrock", true) , "Livingrock").setName("Livingrock").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials GaiaSpirit              = new Materials(     205, new TextureSet("GaiaSpirit", true), 320.0F,      4*2621440,  25, 1|2|64|128,   255,   255, 255,  0,   "GaiaSpirit"                ,   "Gaia Spirit"                      ,    -1,      -1,         0,    0, false,  true,   2,   1,   1, Dyes._NULL         , Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Livingwood              = new MaterialBuilder(206, new TextureSet("Livingwood", true) , "Livingwood").setName("Livingwood").addDustItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials Dreamwood               = new MaterialBuilder(207, new TextureSet("Dreamwood", true) , "Dreamwood").setName("Dreamwood").addDustItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials ManaDiamond             = new Materials(     208, TextureSet.SET_DIAMOND, 320.0F,      4*2621440,  25, 1|4,   38, 237, 224,  255,   "ManaDiamond"                ,   "Mana Diamond"                      ,    -1,      -1,         0,    0, false,  true,   2,   1,   1, Dyes._NULL         , Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1), new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1)));
    public static Materials BotaniaDragonstone      = new Materials(     209, TextureSet.SET_DIAMOND, 320.0F,      4*2621440,  25, 1|4,   242, 44, 239, 255,   "BotaniaDragonstone"                ,   "Dragonstone"                      ,    -1,      -1,         0,    0, false,  true,   2,   1,   1, Dyes._NULL         , Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1), new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1)));

    public static void init() {

    }
}
