package gregtech.common.tileentities.machines.multi.foundry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum FoundryModules {

    // please dont hate me for the arbitrary transparent rectangle image
    UNSET("Unset", "UN.", "", ItemList.Display_ITS_FREE.get(1), GTGuiTextures.EXOFOUNDRY_UNSET,
        new float[] { 0f, 0f, 0f }, EnumChatFormatting.GRAY, 0),
    ACTIVE_TIME_DILATION_SYSTEM("Time Dilation System", "T.D.S", "tds",
        ItemList.Active_Time_Dilation_System_ExoFoundry.get(0), GTGuiTextures.EXOFOUNDRY_TDS,
        new float[] { 10f / 255f, 24f / 255f, 43f / 255f }, 5, EnumChatFormatting.DARK_PURPLE, 13),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc", ItemList.Efficient_Overclocking_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_EFF_OC, new float[] { 107f / 255f, 33f / 255f, 196f / 255f },
        EnumChatFormatting.DARK_AQUA, 12),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsystems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1), GTGuiTextures.EXOFOUNDRY_PES,
        new float[] { 10f / 255f, 143f / 255f, 38f / 255f }, 0.9f, EnumChatFormatting.GREEN, 10),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "T.R", "transcendent_reinforcement",
        ItemList.Transcendent_Reinforcement_ExoFoundry.get(1), GTGuiTextures.EXOFOUNDRY_TR_RE,
        new float[] { 150f / 255f, 10 / 255f, 150f / 255f }, 1.5f, EnumChatFormatting.LIGHT_PURPLE, 12),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_ExoFoundry.get(1), GTGuiTextures.EXOFOUNDRY_ECB,
        new float[] { 58f / 255f, 58f / 255f, 34f / 255f }, 3, EnumChatFormatting.YELLOW, 10),
    HYPERCOOLER("Hypercooler", "H.C", "hypercooler", ItemList.Hypercooler_ExoFoundry.get(1),
        GTGuiTextures.EXOFOUNDRY_HC, new float[] { 40f / 255f, 0.5f, 0.6f, }, EnumChatFormatting.AQUA, 11),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_ExoFoundry.get(1), GTGuiTextures.EXOFOUNDRY_SLC,
        new float[] { 130f / 255f, 30f / 255f, 30f / 255f }, 2, EnumChatFormatting.RED, 10);

    public final String displayName;
    public final String shorthand;
    public final String structureID;
    private final ItemStack icon;
    public final UITexture texture;
    public float[] rgbArr;
    public float[] gammaCorrectedRGB;
    public final EnumChatFormatting color;
    public final int voltageTier;

    // declaring it once here, instead of on every call
    private static final FoundryModules[] lookupArray = values();

    private FoundryModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float[] rgbArr, EnumChatFormatting color, int voltageTier) {
        this(display, shortname, structid, icon, texture, rgbArr, 1, color, voltageTier);
    }

    private FoundryModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float[] rgbArr, float multiplier, EnumChatFormatting color, int voltageTier) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.rgbArr = rgbArr;
        this.color = color;
        this.voltageTier = voltageTier;
        for (int i = 0; i < rgbArr.length; i++) {
            this.rgbArr[i] *= multiplier * 4f;
        }
        // this.gammaCorrectedRGB = acesFilter(rgbArr);
        this.gammaCorrectedRGB = new float[] { 0, 0, 0, 0 }; // TODO idk this is so scuffed
    }

    private float[] getBrightnessColor(float[] x) {
        float brightness = 0.2126f * x[0] + 0.7152f * x[1] + 0.0722f * x[2];
        return new float[] { brightness, brightness, brightness };
    }

    // Clamp helper
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    // ACES tone mapping + gamma correction (1/2.2)
    public static float[] acesFilter(float[] x) {
        // x = {r, g, b} in linear HDR space
        final float a = 2.51f;
        final float b = 0.03f;
        final float c = 2.43f;
        final float d = 0.59f;
        final float e = 0.14f;

        float gamma = 1.0f / 2.2f;
        float[] result = new float[3];

        for (int i = 0; i < 3; i++) {
            // ACES tone mapping
            float numerator = x[i] * (a * x[i] + b);
            float denominator = x[i] * (c * x[i] + d) + e;
            float toneMapped = clamp(numerator / denominator, 0.0f, 1.0f);

            result[i] = toneMapped;
            // Gamma correction to sRGB
            // result[i] = (float)Math.pow(toneMapped, gamma);
        }

        return result;
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
