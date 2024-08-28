package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "interface", configSubDirectory = "GregTech", filename = "Client")
public class ConfigInterface {

    @Config.Comment("if true, makes cover tabs visible on GregTech machines.")
    @Config.DefaultBoolean(true)
    public static boolean coverTabsVisible;

    @Config.Comment("if true, puts the cover tabs display on the right of the UI instead of the left.")
    @Config.DefaultBoolean(false)
    public static boolean coverTabsFlipped;

    @Config.Comment("How verbose should tooltips be? 0: disabled, 1: one-line, 2: normal, 3+: extended.")
    @Config.DefaultInt(2)
    public static int tooltipVerbosity;

    @Config.Comment("How verbose should tooltips be when LSHIFT is held? 0: disabled, 1: one-line, 2: normal, 3+: extended.")
    @Config.DefaultInt(3)
    public static int tooltipShiftVerbosity;

    @Config.Comment("Which style to use for title tab on machine GUI? 0: text tab split-dark, 1: text tab unified, 2: item icon tab.")
    @Config.DefaultInt(0)
    public static int titleTabStyle;
}
