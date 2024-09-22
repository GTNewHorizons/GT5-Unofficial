package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", category = "asm", filename = "ASM")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("ASM section")
@Config.RequiresMcRestart
public class ASMConfiguration {

    public static Debug debug = new Debug();
    public static General general = new General();

    // Debug
    @Config.Comment("Debug section")
    public static class Debug {

        @Config.Comment("Disables ALL logging from GT++.")
        @Config.DefaultBoolean(true)
        public boolean disableAllLogging;

        @Config.Comment("Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)")
        @Config.DefaultBoolean(false)
        public boolean debugMode;

    }

    @Config.Comment("General section")
    public static class General {
        // General Features

        @Config.Comment("Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.")
        @Config.DefaultInt(0)
        @Config.RangeInt(min = 0, max = Short.MAX_VALUE)
        public int enableWatchdogBGM;

    }
}
