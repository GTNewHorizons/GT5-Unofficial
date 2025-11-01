package gregtech.common.tileentities.machines.multi.foundry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum FoundryModules {

    // please dont hate me for the arbitrary transparent rectangle image
    // TODO adjust each color value (some are too bright and some are too "boring")

    // spotless:off
    UNSET(
        "Unset", "UN.", "",
        ItemList.Display_ITS_FREE.get(1),
        GTGuiTextures.EXOFOUNDRY_UNSET,
        0, 0, 0,
        EnumChatFormatting.GRAY,
        0
    ),
    ACTIVE_TIME_DILATION_SYSTEM(
        "Time Dilation System", "T.D.S", "tds",
        ItemList.Active_Time_Dilation_System_ExoFoundry.get(0),
        GTGuiTextures.EXOFOUNDRY_TDS,
        10f / 255f * 5, 24f / 255f * 5, 43f / 255f * 5,
        EnumChatFormatting.DARK_PURPLE,
        13
    ),
    EFFICIENT_OC(
        "Efficient Overclocking System", "E.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_EFF_OC,
        107f / 255f, 33f / 255f, 196f / 255f,
        EnumChatFormatting.DARK_AQUA,
        12
    ),
    POWER_EFFICIENT_SUBSYSTEMS(
        "Power Efficient Subsystems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_PES,
        10f / 255f, 143f / 255f, 38f / 255f,
        EnumChatFormatting.GREEN,
        10
    ),
    HARMONIC_REINFORCEMENT(
        "Harmonic Reinforcement", "H.R", "harmonic_reinforcement",
        ItemList.Harmonic_Reinforcement_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HR,
        150f / 255f * 1.5f, 10 / 255f * 1.5f, 150f / 255f * 1.5f,
        EnumChatFormatting.LIGHT_PURPLE,
        11
    ),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_ECB,
        58f / 255f * 3, 58f / 255f * 3, 34f / 255f * 3,
        EnumChatFormatting.YELLOW,
        10
    ),
    HYPERCOOLER(
        "Hypercooler", "H.C", "hypercooler",
        ItemList.Hypercooler_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HC,
        40f / 255f, 0.5f, 0.6f,
        EnumChatFormatting.AQUA,
        11
    ),
    STREAMLINED_CASTERS(
        "Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_SLC,
        130f / 255f * 2, 30f / 255f * 2, 30f / 255f * 2,
        EnumChatFormatting.RED,
        10
    );
    //spotless:on

    public final String displayName;
    public final String shorthand;
    public final String structureID;
    private final ItemStack icon;
    public final UITexture texture;
    // HDR Colors, mapped to
    public final float red;
    public final float green;
    public final float blue;
    public final EnumChatFormatting color;
    public final int voltageTier;

    // declaring it once here, instead of on every call
    private static final FoundryModules[] lookupArray = values();

    private FoundryModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float red, float green, float blue, EnumChatFormatting color, int voltageTier) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.color = color;
        this.voltageTier = voltageTier;
        this.red = red * 8 * 3;
        this.green = green * 8 * 3;
        this.blue = blue * 8 * 3;
    }

    // ACES tonemapping filter
    public static float tonemap(float color) {
        final float a = 2.51f;
        final float b = 0.03f;
        final float c = 2.43f;
        final float d = 0.59f;
        final float e = 0.14f;

        final float numerator = color * (a * color + b);
        final float denominator = color * (c * color + d) + e;

        return MathHelper.clamp_float(numerator / denominator, 0, 1);
    }

    public static FoundryModules getModule(int ordinal) {
        return lookupArray[ordinal];
    }

    public static int size() {
        return lookupArray.length;
    }

    public ItemStack getItemIcon() {
        return this.icon;
    }

}
