package gregtech.common.tileentities.machines.multi.foundry;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum FoundryModules {

    // please dont hate me for the arbitrary transparent rectangle image
    // TODO adjust each color value (some are too bright and some are too "boring")
    // The colors are in HDR, higher values will result in a higher brightness.
    // spotless:off
    UNSET(
        "Unset", "UN.", "",
        ItemList.Display_ITS_FREE.get(1),
        GTGuiTextures.EXOFOUNDRY_UNSET,
        new Color(0),
        EnumChatFormatting.GRAY,
        0
    ),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_ECB,
        new Color(174, 174, 102),
        EnumChatFormatting.YELLOW,
        10
    ),
    UNIVERSAL_COLLAPSER(
        "Universal Collapser", "U.C", "uc",
        ItemList.Universal_Collapser_ExoFoundry.get(0),
        GTGuiTextures.EXOFOUNDRY_TDS,
        new Color(20, 48, 86),
        EnumChatFormatting.DARK_PURPLE,
        13
    ),
    POWER_EFFICIENT_SUBSYSTEMS(
        "Power Efficient Subsystems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_PES,
        new Color(10, 143, 38),
        EnumChatFormatting.GREEN,
        10
    ),
    EFFICIENT_OC(
        "Efficient Overclocking System", "E.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_EFF_OC,
        new Color(107, 33, 196),
        EnumChatFormatting.DARK_AQUA,
        12
    ),
    STREAMLINED_CASTERS(
        "Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_SLC,
        new Color(250, 60, 60),
        EnumChatFormatting.RED,
        10
    ),
    HELIOCAST_REINFORCEMENT(
        "Heliocast Reinforcement", "H.R", "heliocast_reinforcement",
        ItemList.Heliocast_Reinforcement_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HR,
        new Color(225, 45, 225),
        EnumChatFormatting.LIGHT_PURPLE,
        11
    ),
    HYPERCOOLER(
        "Hypercooler", "H.C", "hypercooler",
        ItemList.Hypercooler_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HC,
        new Color(40, 128, 153),
        EnumChatFormatting.AQUA,
        11
    ),

    ;

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

    // This value changes the brightness of all rings
    private static final int HDR_MULTIPLIER = 12;

    // declaring it once here, instead of on every call
    private static final FoundryModules[] lookupArray = values();

    private FoundryModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        Color c, EnumChatFormatting color, int voltageTier) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.color = color;
        this.voltageTier = voltageTier;
        final float multiplier = HDR_MULTIPLIER / 255f;
        this.red = c.getRed() * multiplier;
        this.green = c.getGreen() * multiplier;
        this.blue = c.getBlue() * multiplier;
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
