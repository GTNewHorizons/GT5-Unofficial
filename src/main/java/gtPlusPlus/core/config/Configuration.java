package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("General section")
@Config.RequiresMcRestart
public class Configuration {

    public static final Debug debug = new Debug();
    public static final Machines machines = new Machines();
    public static final Gregtech gregtech = new Gregtech();
    public static final Features features = new Features();
    public static final Visual visual = new Visual();
    public static final Worldgen worldgen = new Worldgen();

    @Config.Comment("Debug section")
    public static class Debug {

        @Config.Comment("Makes many machines display lots of debug logging.")
        @Config.DefaultBoolean(false)
        public boolean MachineInfo;

        @Config.Comment("Makes all items hidden from NEI display.")
        @Config.DefaultBoolean(false)
        public boolean showHiddenNEIItems;

        @Config.Comment("Dumps all GT++ and Toxic Everglade Data to en_US.lang in the config folder. This config option can be used by foreign players to generate blank .lang files, which they can populate with their language of choice.")
        @Config.DefaultBoolean(false)
        public boolean dumpItemAndBlockData;

    }

    @Config.Comment("Machines section")
    public static class Machines {

        @Config.Comment("Allows the use of TC shards across many recipes by oreDicting them into a common group.")
        @Config.DefaultBoolean(false)
        public boolean enableThaumcraftShardUnification;

        @Config.Comment("Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.")
        @Config.DefaultBoolean(false)
        public boolean disableIC2Recipes;
        @Config.Comment("Sets the steam per second value in LV,MV,HV boilers (respectively 1x,2x,3x this number for the tiers)")
        @Config.DefaultInt(750)
        public int boilerSteamPerSecond;
    }

    @Config.Comment("GregTech section")
    public static class Gregtech {

        @Config.Comment("Rotors below this durability will be removed, prevents NEI clutter. Minimum Durability is N * x, where N is the new value set and x is the turbine size, where 1 is Tiny and 4 is Huge. Set to 0 to disable.")
        @Config.DefaultInt(25_500)
        public int turbineCutoffBase;
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

        @Config.Comment("Use GT textures")
        @Config.DefaultBoolean(true)
        public boolean useGregtechTextures;
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
