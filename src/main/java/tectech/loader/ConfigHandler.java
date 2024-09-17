package tectech.loader;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.TECTECH, filename = "tectech")
@Config.LangKeyPattern(pattern = "GT5U.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class ConfigHandler {
    public static Debug debug = new Debug();
    public static Features features = new Features();
    public static TeslaTweaks teslaTweaks = new TeslaTweaks();

    @Config.Comment("Debug section")
    public static class Debug{
        @Config.Comment("Enables logging and other purely debug features")
        @Config.DefaultBoolean(false)
        public boolean DEBUG_MODE;
    }

    @Config.Comment("Features section")
    public static class Features{
        @Config.Comment("Set to false to disable explosions on everything bad that you can do")
        @Config.DefaultBoolean(true)
        public boolean BOOM_ENABLE;
        @Config.Comment("Set to true to disable the block hardness nerf")
        @Config.DefaultBoolean(false)
        public boolean DISABLE_BLOCK_HARDNESS_NERF;
        @Config.Comment("Set to true to enable removal of plasmas heavier than Fe and other weird ones")
        @Config.DefaultBoolean(false)
        public boolean NERF_FUSION;
    }

    @Config.Comment("Tesla tweaks section")
    public static class TeslaTweaks{
        @Config.Comment("Set to true to enable outputting plasmas as gasses from the tesla tower with a 1:1 ratio")
        @Config.DefaultBoolean(false)
        public boolean TESLA_MULTI_GAS_OUTPUT;
        @Config.Comment("Additional Tesla Tower power loss per amp as a factor of the tier voltage")
        @Config.DefaultFloat(0.25f)
        public float TESLA_MULTI_LOSS_FACTOR_OVERDRIVE;
        @Config.Comment("Tesla Tower power transmission loss per block per amp using no plasmas")
        @Config.DefaultInt(1)
        public int TESLA_MULTI_LOSS_PER_BLOCK_T0;
        @Config.Comment("Tesla Tower power transmission loss per block per amp using helium or nitrogen plasma")
        @Config.DefaultInt(1)
        public int TESLA_MULTI_LOSS_PER_BLOCK_T1;
        @Config.Comment("Tesla Tower power transmission loss per block per amp using radon plasma")
        @Config.DefaultInt(1)
        public int TESLA_MULTI_LOSS_PER_BLOCK_T2;
        @Config.Comment("Tesla Tower helium plasma consumed each second the tesla tower is active")
        @Config.DefaultInt(100)
        public int TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM;
        @Config.Comment("Tesla Tower nitrogen plasma consumed each second the tesla tower is active")
        @Config.DefaultInt(50)
        public int TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN;
        @Config.Comment("Tesla Tower radon plasma consumed each second the tesla tower is active")
        @Config.DefaultInt(50)
        public int TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON;
        @Config.Comment("Tesla Tower T1 Plasmas Range Multiplier")
        @Config.DefaultInt(2)
        public int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1;
        @Config.Comment("Tesla Tower T2 Plasmas Range Multiplier")
        @Config.DefaultInt(4)
        public int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2;
        @Config.Comment("Tesla Tower to Tesla Coil Rich Edition Cover max range")
        @Config.DefaultInt(16)
        public int TESLA_MULTI_RANGE_COVER;
        @Config.Comment("Tesla Tower to Tower max range")
        @Config.DefaultInt(32)
        public int TESLA_MULTI_RANGE_TOWER;
        @Config.Comment("Tesla Tower to Transceiver max range")
        @Config.DefaultInt(16)
        public int TESLA_MULTI_RANGE_TRANSCEIVER;
        @Config.Comment("Additional Tesla Transceiver power loss per amp as a factor of the tier voltage")
        @Config.DefaultFloat(0.25f)
        public float TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE;
        @Config.Comment("Tesla Transceiver power transmission loss per block per amp")
        @Config.DefaultInt(1)
        public int TESLA_SINGLE_LOSS_PER_BLOCK;
        @Config.Comment("Tesla Transceiver to max range")
        @Config.DefaultInt(20)
        public int TESLA_SINGLE_RANGE;
        @Config.Comment("Set true to enable the cool visual effect when tesla tower running.")
        @Config.DefaultBoolean(true)
        public boolean TESLA_VISUAL_EFFECT;
    }
}
