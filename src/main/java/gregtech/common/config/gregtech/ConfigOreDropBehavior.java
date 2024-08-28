package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "ore_drop_behavior",
    configSubDirectory = "GregTech",
    filename = "GregTech")
public class ConfigOreDropBehavior {

    @Config.Comment({ "Settings:",
        " - 'PerDimBlock': Sets the drop to the block variant of the ore block based on dimension, defaults to stone type",
        " - 'UnifiedBlock': Sets the drop to the stone variant of the ore block",
        " - 'Block': Sets the drop to the ore  mined",
        " - 'FortuneItem': Sets the drop to the new ore item and makes it affected by fortune"
            + " - 'Item': Sets the drop to the new ore item" })

    @Config.DefaultString("FortuneItem")
    @Config.RequiresMcRestart
    public static String setting;
}
