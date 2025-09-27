package gregtech.common.tileentities.machines.multi.solidifier;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;

public enum SolidifierModules {

    // please dont hate me for the arbitrary transparent rectangle image
    UNSET("Unset", "UN.", "", ItemList.Display_ITS_FREE.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_UNSET,
        new float[] { 0f, 0f, 0f, 0f }),
    ACTIVE_TIME_DILATION_SYSTEM("Time Dilation System", "T.D.S", "tds",
        ItemList.Active_Time_Dilation_System_Solidifier_Modular.get(0), GTGuiTextures.MODULAR_SOLIDIFIER_TDS,
        new float[] { 0f / 255f, 24f / 255f, 43f / 255f, 0.7f }),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_Solidifier_Modular.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_EFF_OC,
        new float[] { 0f / 255f, 0f / 255f, 0f / 255f, 0.7f }),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsytems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 0f / 255f, 255f / 255f, 0f / 255f, 0.7f }),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "T.R", "transcendent_reinforcement",
        ItemList.Transcendent_Reinforcement_Solidifier_Modular.get(1), GTGuiTextures.MODULAR_SOLIDIFIER_TR_RE,
        new float[] { 39f / 255f, 9f / 255f, 26f / 255f, 0.7f }),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 58f / 255f, 58f / 255f, 34f / 255f, 0.7f }),
    HYPERCOOLER("Hypercooler", "H.C", "hypercooler", ItemList.Hypercooler_Solidifier_Modular.get(1),
        GTGuiTextures.MODULAR_SOLIDIFIER_HC, new float[] {0,1,1, 0.7f }),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_Solidifier_Modular.get(1), GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
        new float[] { 255f / 255f, 0f / 255f, 0f / 255f, 0.7f });

    public final String displayName;
    public final String shorthand;
    public final String structureID;
    private final ItemStack icon;
    public final UITexture texture;
    public final float[] rgbArr;

    // declaring it once here, instead of on every call
    private static final SolidifierModules[] lookupArray = values();

    private SolidifierModules(String display, String shortname, String structid, ItemStack icon, UITexture texture,
        float[] rgbArr) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
        this.texture = texture;
        this.rgbArr = rgbArr;
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
