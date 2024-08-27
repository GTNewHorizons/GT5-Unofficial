package gregtech.common.config.opstuff;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "general",
    configSubDirectory = "GregTech",
    filename = "OverpoweredStuff")
public class ConfigGeneral {

    @Config.Comment("How much RF you get with 100 EU in input.")
    @Config.DefaultInt(360)
    @Config.RequiresMcRestart
    public static int howMuchRFWith100EUInInput;

    @Config.Comment("How much EU you get with 100 RF in input.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int howMuchEUWith100RFInInput;

    @Config.Comment("if true, enables RF -> EU conversion.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean inputRF;

    @Config.Comment("if true, enables EU -> RF conversion.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean outputRF;

    @Config.Comment("If true, machines will explode if RFs injected to a GT machine are above 600 * the max energy they can store.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean RFExplosions;

    @Config.Comment("if true, ignores TinkerConstruct in ore registration.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean ignoreTinkerConstruct;

    @Config.Comment("Controls the exposant used in the computation of the UUM required to replicate an element (uum = mass^replicatorExponent)")
    @Config.DefaultFloat(1.2f)
    @Config.RequiresMcRestart
    public static float replicatorExponent;
}
