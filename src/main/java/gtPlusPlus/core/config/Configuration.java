package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS,configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class Configuration {
    public static final Debug debug = new Debug();
    public static final Machines machines = new Machines();

    public static class Debug{

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

    public static class Machines{
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
}
