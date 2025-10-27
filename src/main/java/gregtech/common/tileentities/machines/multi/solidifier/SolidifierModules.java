package gregtech.common.tileentities.machines.multi.solidifier;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum SolidifierModules {

    // please dont hate me for the arbitrary transparent rectangle image
    UNSET("Unset", "UN.", "", ItemList.Display_ITS_FREE.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_UNSET,
        new float[] { 0f, 0f, 0f }),
    ACTIVE_TIME_DILATION_SYSTEM("Time Dilation System", "T.D.S", "tds",
        ItemList.Active_Time_Dilation_System_Solidifier_Modular.get(0), GTGuiTextures.MODULAR_SOLIDIFIER_TDS,
        new float[] { 10f / 255f, 24f / 255f, 43f / 255f }, 5),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_Solidifier_Modular.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_EFF_OC,
        new float[] { 107f / 255f, 33f / 255f, 196f / 255f }),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsytems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 10f / 255f, 143f / 255f, 38f / 255f }, 0.9f),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "T.R", "transcendent_reinforcement",
        ItemList.Transcendent_Reinforcement_Solidifier_Modular.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_TR_RE,
        new float[] { 150f / 255f, 10 / 255f, 150f / 255f }, 1.5f),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 58f / 255f, 58f / 255f, 34f / 255f }, 3),
    HYPERCOOLER("Hypercooler", "H.C", "hypercooler", ItemList.Hypercooler_Solidifier_Modular.get(1),
        GTGuiTextures.MODULAR_SOLIDIFIER_HC, new float[] { 40f / 255f, 0.5f, 0.6f, }),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 130f / 255f, 30f / 255f, 30f / 255f }, 2);

    public final String displayName;
    public final String shorthand;
    public final String structureID;
    private final ItemStack icon;
    public final UITexture texture;
    public float[] rgbArr;
    public float[] gammaCorrectedRGB;

    // declaring it once here, instead of on every call
    private static final SolidifierModules[] lookupArray = values();

    private SolidifierModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float[] rgbArr) {
        this(display, shortname, structid, icon, texture, rgbArr, 1);
    }

    private SolidifierModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float[] rgbArr, float multiplier) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.rgbArr = rgbArr;
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

    public static SolidifierModules getModule(int ordinal) {
        return lookupArray[ordinal];
    }

    public static int size() {
        return lookupArray.length;
    }

    public ItemStack getItemIcon() {
        return this.icon;
    }
}
