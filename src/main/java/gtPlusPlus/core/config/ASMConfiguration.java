package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", category = "asm", filename = "ASM")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("ASM section")
@Config.RequiresMcRestart
public class ASMConfiguration {

    public static Debug debug = new Debug();

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

}
