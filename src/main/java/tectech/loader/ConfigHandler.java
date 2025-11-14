package tectech.loader;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.TECTECH, filename = "tectech")
@Config.LangKeyPattern(pattern = "GT5U.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class ConfigHandler {

    public static Debug debug = new Debug();
    public static TeslaTweaks teslaTweaks = new TeslaTweaks();

    public static Visual visual = new Visual();

    @Config.Comment("Debug section")
    public static class Debug {

        @Config.Comment("Enables logging and other purely debug features")
        @Config.DefaultBoolean(false)
        public boolean DEBUG_MODE;
    }

    @Config.Comment("Tesla tweaks section")
    public static class TeslaTweaks {

        @Config.Ignore()
        public static final float TESLA_MULTI_LOSS_FACTOR_OVERDRIVE = 0.25f;
        @Config.Ignore()
        public static final int TESLA_MULTI_LOSS_PER_BLOCK_T0 = 1;
        @Config.Ignore()
        public static final int TESLA_MULTI_LOSS_PER_BLOCK_T1 = 1;
        @Config.Ignore()
        public static final int TESLA_MULTI_LOSS_PER_BLOCK_T2 = 1;
        @Config.Ignore()
        public static final int TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM = 100;
        @Config.Ignore()
        public static final int TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN = 50;
        @Config.Ignore()
        public static final int TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON = 50;
        @Config.Ignore()
        public static final int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1 = 2;
        @Config.Ignore()
        public static final int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2 = 4;
        @Config.Ignore()
        public static final int TESLA_MULTI_RANGE_COVER = 16;
        @Config.Ignore()
        public static final int TESLA_MULTI_RANGE_TOWER = 32;
        @Config.Ignore()
        public static final int TESLA_MULTI_RANGE_TRANSCEIVER = 16;
        @Config.Ignore()
        public static final float TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE = 0.25f;
        @Config.Ignore()
        public static final int TESLA_SINGLE_LOSS_PER_BLOCK = 1;

        @Config.Ignore()
        public static final int TESLA_SINGLE_RANGE = 20;
        @Config.Comment("Set true to enable the cool visual effect when tesla tower running.")
        @Config.DefaultBoolean(true)
        public boolean TESLA_VISUAL_EFFECT;
    }

    @Config.Comment("Visual section")
    public static class Visual {

        @Config.Comment({ "Eye of Harmony energy input and output display:", " - 'Numerical': Shows the entire number",
            " - 'Scientific': Uses scientific notation", " - 'SI': Uses the SI notation", })

        @Config.DefaultEnum("Scientific")
        @Config.RequiresMcRestart
        public EOHNumberFormat EOH_NOTATION = EOHNumberFormat.Scientific;

        public enum EOHNumberFormat {
            Numerical,
            Scientific,
            SI
        }
    }

}
