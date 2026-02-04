package gregtech.common.tileentities.machines.multi.foundry;

import java.awt.Color;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum FoundryModule {

    // please dont hate me for the arbitrary transparent rectangle image
    // The colors are in HDR, higher values will result in a higher brightness.
    // spotless:off
    UNSET(
        "Unset", "UN.", "",
        ItemList.Primary_Casing_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_UNSET,
        new Color(0),
        EnumChatFormatting.GRAY,
        0, foundryData -> {}),
    EXTRA_CASTING_BASINS("Superdense Casting Basins", "S.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_ECB,
        new Color(174, 174, 102),
        EnumChatFormatting.YELLOW,
        10, foundryData -> {
            foundryData.parallelScaleAdditive+=12;
    }
    ),
    UNIVERSAL_COLLAPSER(
        "Universal Collapser", "U.C", "uc",
        ItemList.Universal_Collapser_ExoFoundry.get(0),
        GTGuiTextures.EXOFOUNDRY_TDS,
        new Color(20, 48, 86),
        EnumChatFormatting.DARK_PURPLE,
        13, foundryData -> {
            if(foundryData.tdsPresent) return;
            foundryData.tdsPresent = true;
            foundryData.euEffMultiplier*=4;
            foundryData.speedMultiplier *= 2;
        }
    ),
    POWER_EFFICIENT_SUBSYSTEMS(
        "Proto-Volt Stabilizer", "P.V.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_PES,
        new Color(10, 143, 38),
        EnumChatFormatting.GREEN,
        10, foundryData -> {
            foundryData.euEffAdditive-=0.1f;
            foundryData.euEffMultiplier *= 0.8f;
        }
    ),
    EFFICIENT_OC(
        "Sentient Overclocker", "S.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_EFF_OC,
        new Color(107, 33, 196),
        EnumChatFormatting.DARK_AQUA,
        12, foundryData -> {
            foundryData.effOCPresent = true;
            foundryData.ocFactorAdditive += 0.35f;
    }
    ),
    STREAMLINED_CASTERS(
        "Streamlined Casting", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_SLC,
        new Color(250, 60, 60),
        EnumChatFormatting.RED,
        10, foundryData -> {
            foundryData.speedAdditive += 1.5f;
        }
    ),
    HELIOCAST_REINFORCEMENT(
        "Heliocast Reinforcement", "H.R", "heliocast_reinforcement",
        ItemList.Heliocast_Reinforcement_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HR,
        new Color(225, 45, 225),
        EnumChatFormatting.LIGHT_PURPLE,
        11, foundryData -> {
            foundryData.UIVRecipesEnabled=true;
        }
    ),
    HYPERCOOLER(
        "Hypercooler", "H.C", "hypercooler",
        ItemList.Hypercooler_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HC,
        new Color(40, 128, 153),
        EnumChatFormatting.AQUA,
        11, foundryData -> {
            foundryData.hypercoolerPresent = true;
        }
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
    public final Consumer<FoundryData> statFunction;

    // This value changes the brightness of all rings
    private static final int HDR_MULTIPLIER = 12;

    private FoundryModule(String display, String shortname, String structid, ItemStack icon, UITexture texture, Color c,
        EnumChatFormatting color, int voltageTier, Consumer<FoundryData> statFunction) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.color = color;
        this.voltageTier = voltageTier;
        this.statFunction = statFunction;
        final float multiplier = HDR_MULTIPLIER / 255f;
        this.red = c.getRed() * multiplier;
        this.green = c.getGreen() * multiplier;
        this.blue = c.getBlue() * multiplier;
    }

    public static int size() {
        return FoundryModule.values().length;
    }

    public ItemStack getItemIcon() {
        return this.icon;
    }

}
