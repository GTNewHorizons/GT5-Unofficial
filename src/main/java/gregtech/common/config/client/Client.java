package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "client",
    configSubDirectory = "GregTech",
    filename = "Client")
public class Client {

    @Config.Comment("Color Modulation section")
    @Config.LangKey("GT5U.gui.config.client.color_modulation")
    public static final ColorModulation colorModulation = new ColorModulation();

    @Config.Comment("Interface section")
    @Config.LangKey("GT5U.gui.config.client.interface")
    public static final Interface iface = new Interface();

    @Config.Comment("Preference section")
    @Config.LangKey("GT5U.gui.config.client.preference")
    public static final Preference preference = new Preference();

    @Config.Comment("Render section")
    @Config.LangKey("GT5U.gui.config.client.render")
    public static final Render render = new Render();

    @Config.Comment("Waila section")
    @Config.LangKey("GT5U.gui.config.client.waila")
    public static final Waila waila = new Waila();

    public static class ColorModulation {

        @Config.Comment("hex value for the cable insulation color modulation.")
        @Config.DefaultString("#404040")
        public String cableInsulation;

        @Config.Comment("hex value for the construction foam color modulation.")
        @Config.DefaultString("#404040")
        public String constructionFoam;

        @Config.Comment("hex value for the machine metal color modulation.")
        @Config.DefaultString("#D2DCFF")
        public String machineMetal;
    }

    public static class Interface {

        @Config.Comment("if true, makes cover tabs visible on GregTech machines.")
        @Config.DefaultBoolean(true)
        public boolean coverTabsVisible;

        @Config.Comment("if true, puts the cover tabs display on the right of the UI instead of the left.")
        @Config.DefaultBoolean(false)
        public boolean coverTabsFlipped;

        @Config.Comment("How verbose should tooltips be? 0: disabled, 1: one-line, 2: normal, 3+: extended.")
        @Config.DefaultInt(2)
        public int tooltipVerbosity;

        @Config.Comment("How verbose should tooltips be when LSHIFT is held? 0: disabled, 1: one-line, 2: normal, 3+: extended.")
        @Config.DefaultInt(3)
        public int tooltipShiftVerbosity;

        @Config.Comment("Which style to use for title tab on machine GUI? 0: text tab split-dark, 1: text tab unified, 2: item icon tab.")
        @Config.DefaultInt(0)
        public int titleTabStyle;
    }
    public static class Preference {

        @Config.Comment("if true, input filter will initially be on when input buses are placed in the world.")
        @Config.DefaultBoolean(false)
        public boolean inputBusInitialFilter;

        @Config.Comment("if true, allow multistacks on single blocks by default when they are first placed in the world.")
        @Config.DefaultBoolean(false)
        public boolean singleBlockInitialAllowMultiStack;

        @Config.Comment("if true, input filter will initially be on when machines are placed in the world.")
        @Config.DefaultBoolean(false)
        public boolean singleBlockInitialFilter;
    }

    public static class Render {

        @Config.Comment("if true, enables ambient-occlusion smooth lighting on tiles.")
        @Config.DefaultBoolean(true)
        public boolean renderTileAmbientOcclusion;

        @Config.Comment("if true, enables glowing of the machine controllers.")
        @Config.DefaultBoolean(true)
        public boolean renderGlowTextures;

        @Config.Comment("if true, render flipped machine with flipped textures.")
        @Config.DefaultBoolean(true)
        public boolean renderFlippedMachinesFlipped;

        @Config.Comment("if true, render indicators on hatches.")
        @Config.DefaultBoolean(true)
        public boolean renderIndicatorsOnHatch;

        @Config.Comment("if true, enables dirt particles when pollution reaches the threshold.")
        @Config.DefaultBoolean(true)
        public boolean renderDirtParticles;

        @Config.Comment("if true, enables pollution fog when pollution reaches the threshold.")
        @Config.DefaultBoolean(true)
        public boolean renderPollutionFog;

        @Config.Comment("if true, enables the green -> red durability for an item's damage value.")
        @Config.DefaultBoolean(true)
        public boolean renderItemDurabilityBar;

        @Config.Comment("if true, enables the blue charge bar for an electric item's charge.")
        @Config.DefaultBoolean(true)
        public boolean renderItemChargeBar;

        @Config.Comment("enables BaseMetaTileEntity block updates handled by BlockUpdateHandler.")
        @Config.DefaultBoolean(false)
        public boolean useBlockUpdateHandler;

    }

    public static class Waila {

        /**
         * This enables showing voltage tier of transformer for Waila, instead of raw voltage number
         */
        @Config.Comment("if true, enables showing voltage tier of transformer for Waila, instead of raw voltage number.")
        @Config.DefaultBoolean(true)
        public boolean wailaTransformerVoltageTier;

        @Config.Comment("if true, enables showing voltage tier of transformer for Waila, instead of raw voltage number.")
        @Config.DefaultBoolean(false)
        public boolean wailaAverageNS;
    }
}
