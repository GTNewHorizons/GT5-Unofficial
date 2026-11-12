package gtPlusPlus.xmod.gregtech.api.gui;

import static gregtech.api.enums.Mods.GTPlusPlus;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

public class GTPPUITextures {

    // spotless:off
    public static final AdaptableUITexture BACKGROUND_YELLOW = AdaptableUITexture.of(GTPlusPlus.ID, "gui/background/yellow", 176, 166, 4);
    public static final AdaptableUITexture SLOT_ITEM_YELLOW = AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/item_yellow", 18, 18, 1);

    public static final UITexture OVERLAY_SLOT_TURBINE = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/turbine");

    public static final UITexture PROGRESSBAR_FLUID_REACTOR = UITexture.fullImage(GTPlusPlus.ID, "gui/progressbar/fluid_reactor");
    public static final UITexture PROGRESSBAR_BOILER_EMPTY = UITexture.fullImage(GTPlusPlus.ID, "gui/progressbar/boiler_empty"); // used in cropsnh
    public static final UITexture PROGRESSBAR_PSS_ENERGY = UITexture.fullImage(GTPlusPlus.ID, "gui/progressbar/pss_energy");

    public static final AdaptableUITexture TAB_TITLE_YELLOW = AdaptableUITexture.of(GTPlusPlus.ID, "gui/tab/title_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_ANGULAR_YELLOW = AdaptableUITexture.of(GTPlusPlus.ID, "gui/tab/title_angular_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_DARK_YELLOW = AdaptableUITexture.of(GTPlusPlus.ID, "gui/tab/title_dark_yellow", 28, 28, 4);

    public static final UITexture PICTURE_ENERGY_FRAME = UITexture.fullImage(GTPlusPlus.ID, "gui/picture/energy_frame");
    // spotless:on
}
