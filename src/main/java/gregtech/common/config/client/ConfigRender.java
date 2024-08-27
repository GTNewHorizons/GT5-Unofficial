package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "render", configSubDirectory = "GregTech", filename = "Client")
public class ConfigRender {

    @Config.Comment("if true, enables ambient-occlusion smooth lighting on tiles.")
    @Config.DefaultBoolean(true)
    public static boolean renderTileAmbientOcclusion;

    @Config.Comment("if true, enables glowing of the machine controllers.")
    @Config.DefaultBoolean(true)
    public static boolean renderGlowTextures;

    @Config.Comment("if true, render flipped machine with flipped textures.")
    @Config.DefaultBoolean(true)
    public static boolean renderFlippedMachinesFlipped;

    @Config.Comment("if true, render indicators on hatches.")
    @Config.DefaultBoolean(true)
    public static boolean renderIndicatorsOnHatch;

    @Config.Comment("if true, enables dirt particles when pollution reaches the threshold.")
    @Config.DefaultBoolean(true)
    public static boolean renderDirtParticles;

    @Config.Comment("if true, enables pollution fog when pollution reaches the threshold.")
    @Config.DefaultBoolean(true)
    public static boolean renderPollutionFog;

    @Config.Comment("if true, enables the green -> red durability for an item's damage value.")
    @Config.DefaultBoolean(true)
    public static boolean renderItemDurabilityBar;

    @Config.Comment("if true, enables the blue charge bar for an electric item's charge.")
    @Config.DefaultBoolean(true)
    public static boolean renderItemChargeBar;

    @Config.Comment("enables BaseMetaTileEntity block updates handled by BlockUpdateHandler.")
    @Config.DefaultBoolean(false)
    public static boolean useBlockUpdateHandler;

}
