package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "color_modulation",
    configSubDirectory = "GregTech",
    filename = "Client")
public class ConfigColorModulation {

    @Config.Comment("hex value for the cable insulation color modulation.")
    @Config.DefaultString("#404040")
    public static String cableInsulation;

    @Config.Comment("hex value for the construction foam color modulation.")
    @Config.DefaultString("#404040")
    public static String constructionFoam;

    @Config.Comment("hex value for the machine metal color modulation.")
    @Config.DefaultString("#D2DCFF")
    public static String machineMetal;
}
