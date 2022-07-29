package gregtech.api.enums;

import gregtech.api.objects.MaterialStack;

import java.util.Arrays;

public class MaterialsBotania {

    // Botania materials.
    public static Materials Manasteel               = new MaterialBuilder(201, new TextureSet("Manasteel", true), "Manasteel").setName("Manasteel").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(8.0F).setDurability(512).setToolQuality(3).setMeltingPoint(1500).setBlastFurnaceTemp(1500).setBlastFurnaceRequired(true).setMaterialList(new MaterialStack(Materials.Iron, 1), new MaterialStack(Materials.Magic, 1)).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4))).constructMaterial();
    public static Materials Terrasteel              = new MaterialBuilder(202, new TextureSet("Terrasteel", true), "Terrasteel").setName("Terrasteel").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(32.0F).setDurability(10240).setToolQuality(5).setMeltingPoint(5400).setBlastFurnaceTemp(5400).setBlastFurnaceRequired(true).setMaterialList(new MaterialStack(Materials.Iron, 1), new MaterialStack(Materials.Magic, 2)).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 8))).constructMaterial();
    public static Materials ElvenElementium         = new MaterialBuilder(203, new TextureSet("ElvenElementium", true), "Elementium").setName("Elementium").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(20.0F).setDurability(32768).setToolQuality(7).setMeltingPoint(7200).setBlastFurnaceTemp(7200).setBlastFurnaceRequired(true).setMaterialList(new MaterialStack(Materials.Magic, 4)).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 16))).constructMaterial();
    public static Materials Livingrock              = new MaterialBuilder(204, new TextureSet("Livingrock", true) , "Livingrock").setName("Livingrock").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
//    public static Materials Botania_Gaia_Spirit     = new MaterialBuilder(205, , "Gaia Spirit").setName("Gaia Spirit").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(8.0F).setDurability(512).setToolQuality(3).setMeltingPoint(1500).setBlastFurnaceTemp(1500).setBlastFurnaceRequired(true).setMaterialList(new MaterialStack(Materials.Iron, 1), new MaterialStack(Materials.Magic, 1)).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4))).constructMaterial();
    public static Materials Botania_Gaia_Spirit                = new Materials(205, new TextureSet("Gaia Spirit", true), 320.0F,      4*2621440,  25, 1|2|64|128,   255,   255, 255,  0,   "GaiaSpirit"                ,   "Gaia Spirit"                      ,    -1,      -1,         0,    0, false,  true,   2,   1,   1, Dyes._NULL         , Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1)));

    public static Materials Livingwood              = new MaterialBuilder(206, new TextureSet("Livingwood", true) , "Livingwood").setName("Livingwood").addDustItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials Dreamwood               = new MaterialBuilder(207, new TextureSet("Dreamwood", true) , "Dreamwood").setName("Dreamwood").addDustItems().addToolHeadItems().addGearItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials ManaDiamond             = new MaterialBuilder(208, new TextureSet("ManaDiamond", true) , "Mana Diamond").setName("Mana Diamond").addDustItems().addGemItems().addToolHeadItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();
    public static Materials Botania_Dragonstone     = new MaterialBuilder(209, new TextureSet("Botania_Dragonstone", true) , "Dragonstone").setName("Dragonstone").addDustItems().addGemItems().addToolHeadItems().setToolSpeed(1.0F).setDurability(0).setToolQuality(3).setOreValue(3).setDensityMultiplier(1).setDensityDivider(1).constructMaterial();

    public static void init() {
        // no-op. all work is done by <clinit>
    }
}
