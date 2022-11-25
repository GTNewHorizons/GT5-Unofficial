package gtPlusPlus.xmod.gregtech.api.gui;

import static gtPlusPlus.core.lib.CORE.MODID;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GTPP_UITextures {

    public static final AdaptableUITexture BACKGROUND_YELLOW =
            AdaptableUITexture.of(MODID, "gui/background/yellow", 176, 166, 4);

    public static final AdaptableUITexture SLOT_ITEM_YELLOW =
            AdaptableUITexture.of(MODID, "gui/slot/item_yellow", 18, 18, 1);
    public static final AdaptableUITexture[] SLOT_INVENTORY_MANAGER = new AdaptableUITexture[] {
        AdaptableUITexture.of(MODID, "gui/slot/red", 18, 18, 1),
        AdaptableUITexture.of(MODID, "gui/slot/green", 18, 18, 1),
        AdaptableUITexture.of(MODID, "gui/slot/blue", 18, 18, 1),
        AdaptableUITexture.of(MODID, "gui/slot/cyan", 18, 18, 1),
        AdaptableUITexture.of(MODID, "gui/slot/magenta", 18, 18, 1),
        AdaptableUITexture.of(MODID, "gui/slot/yellow", 18, 18, 1),
    };

    public static final UITexture BUTTON_STANDARD_BRONZE = UITexture.fullImage(MODID, "gui/button/standard_bronze");
    public static final UITexture BUTTON_STANDARD_16x16 = UITexture.fullImage(MODID, "gui/button/standard_16x16");

    public static final UITexture OVERLAY_SLOT_COAL = UITexture.fullImage(MODID, "gui/overlay_slot/coal");
    public static final UITexture OVERLAY_SLOT_CANISTER_DARK =
            UITexture.fullImage(MODID, "gui/overlay_slot/canister_dark");
    public static final UITexture OVERLAY_SLOT_WEED_EX = UITexture.fullImage(MODID, "gui/overlay_slot/weed_ex");
    public static final UITexture OVERLAY_SLOT_FERTILIZER = UITexture.fullImage(MODID, "gui/overlay_slot/fertilizer");
    public static final UITexture OVERLAY_SLOT_ELECTRIC_TOOL =
            UITexture.fullImage(MODID, "gui/overlay_slot/electric_tool");
    public static final UITexture OVERLAY_SLOT_PAGE_PRINTED_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/page_printed_bronze");
    public static final UITexture OVERLAY_SLOT_ARROW = UITexture.fullImage(MODID, "gui/overlay_slot/arrow");
    public static final UITexture OVERLAY_SLOT_ARROW_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/arrow_bronze");
    public static final UITexture OVERLAY_SLOT_CRAFT_OUTPUT =
            UITexture.fullImage(MODID, "gui/overlay_slot/craft_output");
    public static final UITexture OVERLAY_SLOT_CRAFT_OUTPUT_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/craft_output_bronze");
    public static final UITexture OVERLAY_SLOT_PARK = UITexture.fullImage(MODID, "gui/overlay_slot/park");
    public static final UITexture OVERLAY_SLOT_PARK_BRONZE = UITexture.fullImage(MODID, "gui/overlay_slot/park_bronze");
    public static final UITexture OVERLAY_SLOT_INGOT = UITexture.fullImage(MODID, "gui/overlay_slot/ingot");
    public static final UITexture OVERLAY_SLOT_ARROW_4 = UITexture.fullImage(MODID, "gui/overlay_slot/arrow_4");
    public static final UITexture OVERLAY_SLOT_TURBINE = UITexture.fullImage(MODID, "gui/overlay_slot/turbine");
    public static final UITexture OVERLAY_SLOT_CHEST = UITexture.fullImage(MODID, "gui/overlay_slot/chest");
    public static final UITexture[] OVERLAY_SLOT_INVENTORY_MANAGER_COLOR = new UITexture[] {
        UITexture.fullImage(MODID, "gui/overlay_slot/red"),
        UITexture.fullImage(MODID, "gui/overlay_slot/green"),
        UITexture.fullImage(MODID, "gui/overlay_slot/blue"),
        UITexture.fullImage(MODID, "gui/overlay_slot/cyan"),
        UITexture.fullImage(MODID, "gui/overlay_slot/magenta"),
        UITexture.fullImage(MODID, "gui/overlay_slot/yellow"),
    };
    public static final UITexture[] OVERLAY_SLOT_INVENTORY_MANAGER_ARROW = new UITexture[] {
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_red"),
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_green"),
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_blue"),
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_cyan"),
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_magenta"),
        UITexture.fullImage(MODID, "gui/overlay_slot/arrow_yellow"),
    };

    public static final UITexture PROGRESSBAR_FLUID_REACTOR =
            UITexture.fullImage(MODID, "gui/progressbar/fluid_reactor");
    public static final UITexture PROGRESSBAR_BOILER_EMPTY = UITexture.fullImage(MODID, "gui/progressbar/boiler_empty");
    public static final UITexture PROGRESSBAR_FUEL = UITexture.fullImage(MODID, "gui/progressbar/fuel");
    public static final UITexture PROGRESSBAR_ARROW_2 = UITexture.fullImage(MODID, "gui/progressbar/arrow_2");
    public static final UITexture PROGRESSBAR_COMPUTER_ENERGY =
            UITexture.fullImage(MODID, "gui/progressbar/computer_energy");
    public static final UITexture PROGRESSBAR_PSS_ENERGY = UITexture.fullImage(MODID, "gui/progressbar/pss_energy");

    public static final AdaptableUITexture TAB_TITLE_YELLOW =
            AdaptableUITexture.of(MODID, "gui/tab/title_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_ANGULAR_YELLOW =
            AdaptableUITexture.of(MODID, "gui/tab/title_angular_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_DARK_YELLOW =
            AdaptableUITexture.of(MODID, "gui/tab/title_dark_yellow", 28, 28, 4);

    public static final UITexture OVERLAY_BUTTON_HARVESTER_MODE =
            UITexture.fullImage(MODID, "gui/overlay_button/harvester_mode");
    public static final UITexture OVERLAY_BUTTON_FLUSH = UITexture.fullImage(MODID, "gui/overlay_button/flush");
    public static final UITexture OVERLAY_BUTTON_FLUSH_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_button/flush_bronze");
    public static final UITexture OVERLAY_BUTTON_AUTOMATION =
            UITexture.fullImage(MODID, "gui/overlay_button/automation");
    public static final UITexture OVERLAY_BUTTON_LOCK = UITexture.fullImage(MODID, "gui/overlay_button/lock");
    public static final UITexture[] OVERLAY_BUTTON_THROUGHPUT = IntStream.range(
                    0, 4) // GT_MetaTileEntity_ElectricAutoWorkbench#MAX_THROUGHPUT
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/overlay_button/throughput_" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[0]);
    public static final UITexture[] OVERLAY_BUTTON_MODE = IntStream.range(
                    0, 10) // GT_MetaTileEntity_ElectricAutoWorkbench#MAX_MODES
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/overlay_button/mode_" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[0]);
    public static final UITexture OVERLAY_BUTTON_COMPUTER_MODE =
            UITexture.fullImage(MODID, "gui/overlay_button/computer_mode");
    public static final UITexture OVERLAY_BUTTON_SAVE = UITexture.fullImage(MODID, "gui/overlay_button/save");
    public static final UITexture OVERLAY_BUTTON_LOAD = UITexture.fullImage(MODID, "gui/overlay_button/load");
    public static final UITexture OVERLAY_BUTTON_NUCLEAR_SWITCH =
            UITexture.fullImage(MODID, "gui/overlay_button/nuclear_switch");
    public static final UITexture OVERLAY_BUTTON_ARROW_LEFT =
            UITexture.fullImage(MODID, "gui/overlay_button/arrow_left");
    public static final UITexture OVERLAY_BUTTON_ARROW_RIGHT =
            UITexture.fullImage(MODID, "gui/overlay_button/arrow_right");
    public static final UITexture[] OVERLAY_BUTTON_DIRECTION = new UITexture[] {
        UITexture.fullImage(MODID, "gui/overlay_button/bottom"),
        UITexture.fullImage(MODID, "gui/overlay_button/top"),
        UITexture.fullImage(MODID, "gui/overlay_button/north"),
        UITexture.fullImage(MODID, "gui/overlay_button/south"),
        UITexture.fullImage(MODID, "gui/overlay_button/west"),
        UITexture.fullImage(MODID, "gui/overlay_button/east"),
    };
    public static final UITexture[] OVERLAY_BUTTON_DIRECTION_GRAY = new UITexture[] {
        UITexture.fullImage(MODID, "gui/overlay_button/bottom_gray"),
        UITexture.fullImage(MODID, "gui/overlay_button/top_gray"),
        UITexture.fullImage(MODID, "gui/overlay_button/north_gray"),
        UITexture.fullImage(MODID, "gui/overlay_button/south_gray"),
        UITexture.fullImage(MODID, "gui/overlay_button/west_gray"),
        UITexture.fullImage(MODID, "gui/overlay_button/east_gray"),
    };
    public static final UITexture OVERLAY_BUTTON_TIP_GREEN = UITexture.fullImage(MODID, "gui/overlay_button/tip_green");
    public static final UITexture OVERLAY_BUTTON_TIP_RED = UITexture.fullImage(MODID, "gui/overlay_button/tip_red");
    public static final UITexture OVERLAY_BUTTON_ACTIVE_STATE =
            UITexture.fullImage(MODID, "gui/overlay_button/active_state");
    public static final UITexture OVERLAY_BUTTON_CHANGE_MODE =
            UITexture.fullImage(MODID, "gui/overlay_button/change_mode");
    public static final UITexture OVERLAY_BUTTON_PLUS_MINUS =
            UITexture.fullImage(MODID, "gui/overlay_button/plus_minus");

    public static final UITexture PICTURE_WORKBENCH_CIRCLE = UITexture.fullImage(MODID, "gui/picture/workbench_circle");
    public static final UITexture PICTURE_ARROW_WHITE_DOWN = UITexture.fullImage(MODID, "gui/picture/arrow_white_down");
    public static final UITexture PICTURE_V202 = UITexture.fullImage(MODID, "gui/picture/v202");
    public static final UITexture PICTURE_COMPUTER_TOP = UITexture.fullImage(MODID, "gui/picture/computer_top");
    public static final UITexture PICTURE_COMPUTER_GRID = UITexture.fullImage(MODID, "gui/picture/computer_grid");
    public static final UITexture PICTURE_ARROWS_SEPARATE = UITexture.fullImage(MODID, "gui/picture/arrows_separate");
    public static final UITexture PICTURE_ARROWS_FUSION = UITexture.fullImage(MODID, "gui/picture/arrows_fusion");
    public static final UITexture PICTURE_REDSTONE_CIRCUIT_SCREEN =
            UITexture.fullImage(MODID, "gui/picture/redstone_circuit_screen");
    public static final UITexture PICTURE_ELECTRICITY_ERROR =
            UITexture.fullImage(MODID, "gui/picture/electricity_error");
    public static final UITexture PICTURE_ELECTRICITY_FINE = UITexture.fullImage(MODID, "gui/picture/electricity_fine");
    public static final UITexture PICTURE_ENERGY_FRAME = UITexture.fullImage(MODID, "gui/picture/energy_frame");
}
