package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "harvest_level",
    configSubDirectory = "GregTech",
    filename = "GregTech")
public class ConfigHarvestLevel {

    @Config.Comment("Activate Harvest Level Change")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean activateHarvestLevelChange;

    @Config.Comment("Maximum harvest level")
    @Config.DefaultInt(7)
    @Config.RequiresMcRestart
    public static int maxHarvestLevel;

    @Config.Comment("GraniteHarvestLevel harvest level")
    @Config.DefaultInt(3)
    @Config.RequiresMcRestart
    public static int graniteHarvestLevel;
}
