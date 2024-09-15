package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", category = "asm", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("ASM section")
@Config.RequiresMcRestart
public class ASMConfiguration {
    public static Debug debug = new Debug();
    public static General general = new General();

    // Debug
    @Config.Comment("Debug section")
    public static class Debug{
        @Config.Comment("Disables ALL logging from GT++.")
        @Config.DefaultBoolean(true)
        public boolean disableAllLogging;

        @Config.Comment("Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)")
        @Config.DefaultBoolean(false)
        public boolean debugMode;

        @Config.Comment("Enable/Disable COFH OreDictionaryArbiter Patch (Useful for Development)")
        @Config.DefaultBoolean(true)
        public boolean enableCofhPatch;

        @Config.Comment("Enable/Disable Forge OreDictionary Patch (Useful for Development)")
        @Config.DefaultBoolean(false)
        public boolean enableOreDictPatch;
    }

    @Config.Comment("General section")
    public static class General{
        // General Features

        @Config.Comment("Prevents the game crashing from having invalid keybinds. https://github.com/alkcorp/GTplusplus/issues/544")
        @Config.DefaultBoolean(true)
        public boolean enabledLwjglKeybindingFix;

        @Config.Comment("Fixes small oversights in Thaumcraft 4.")
        @Config.DefaultBoolean(true)
        public boolean enableTcAspectSafety;
    }
}
