package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "waila", configSubDirectory = "GregTech", filename = "Client")

public class ConfigWaila {

    /**
     * This enables showing voltage tier of transformer for Waila, instead of raw voltage number
     */
    @Config.Comment("if true, enables showing voltage tier of transformer for Waila, instead of raw voltage number.")
    @Config.DefaultBoolean(true)
    public static boolean wailaTransformerVoltageTier;

    @Config.Comment("if true, enables showing voltage tier of transformer for Waila, instead of raw voltage number.")
    @Config.DefaultBoolean(false)
    public static boolean wailaAverageNS;
}
