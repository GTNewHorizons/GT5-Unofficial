package gregtech.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.ModIDs.GREG_TECH,
    category = "worldgen",
    configSubDirectory = "GregTech",
    filename = "WorldGeneration")
@Config.LangKey("GT5U.gui.config.worldgen")
public class Worldgen {

    public static General general = new General();

    @Config.LangKey("GT5U.gui.config.worldgen.general")
    public static class General {

        @Config.Comment("if true, enables basalt ore gen.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateBasaltOres;

        @Config.Comment("if true, enables black granite ore gen.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateBlackGraniteOres;

        @Config.Comment("if true, enables marble ore gen.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateMarbleOres;

        @Config.Comment("if true, enables red granite ore gen.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateRedGraniteOres;

        @Config.Comment("If true, disables vanilla oregen.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean disableVanillaOres;

        @Config.Comment("if true, enables underground dirt gen. Does nothing if the vanilla oregen is enabled!")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateUndergroundDirtGen;

        @Config.Comment("if true, enables underground gravel gen. Does nothing if the vanilla oregen is enabled!")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean generateUndergroundGravelGen;
    }
}
