package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("General section")
@Config.RequiresMcRestart
public class Configuration {

    public static final Debug debug = new Debug();
    public static final Features features = new Features();
    public static final Visual visual = new Visual();
    public static final Worldgen worldgen = new Worldgen();

    @Config.Comment("Debug section")
    public static class Debug {

        @Config.Comment("Makes many machines display lots of debug logging.")
        @Config.DefaultBoolean(false)
        public boolean MachineInfo;

        @Config.Comment("Dumps all GT++ and Toxic Everglade Data to en_US.lang in the config folder. This config option can be used by foreign players to generate blank .lang files, which they can populate with their language of choice.")
        @Config.DefaultBoolean(false)
        public boolean dumpItemAndBlockData;
    }

    @Config.Comment("Features section")
    public static class Features {

        @Config.Comment("Hides every filled IC2 Universal Cell from NEI.")
        @Config.DefaultBoolean(true)
        public boolean hideUniversalCells;
    }

    @Config.Comment("Visual section")
    public static class Visual {

        @Config.Comment("Enables Animated GT++ Textures")
        @Config.DefaultBoolean(true)
        public boolean enableAnimatedTextures;
    }

    @Config.Comment("Worldgen section")
    public static class Worldgen {

        @Config.Comment("The ID of the Toxic Everglades.")
        @Config.DefaultInt(227)
        public int EVERGLADES_ID;
        @Config.Comment("The biome within the Toxic Everglades.")
        @Config.DefaultInt(199)
        public int EVERGLADESBIOME_ID;
    }
}
