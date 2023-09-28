package gtPlusPlus.xmod.gregtech.api.gui;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

public class GTPP_UITextures {

    public static final UITexture OVERLAY_SLOT_COAL = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/coal");
    public static final UITexture OVERLAY_SLOT_CANISTER_DARK = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/canister_dark");

    public static final AdaptableUITexture BACKGROUND_YELLOW = AdaptableUITexture
            .of(GTPlusPlus.ID, "gui/background/yellow", 176, 166, 4);

    public static final AdaptableUITexture SLOT_ITEM_YELLOW = AdaptableUITexture
            .of(GTPlusPlus.ID, "gui/slot/item_yellow", 18, 18, 1);
    public static final AdaptableUITexture[] SLOT_INVENTORY_MANAGER = new AdaptableUITexture[] {
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/red", 18, 18, 1),
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/green", 18, 18, 1),
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/blue", 18, 18, 1),
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/cyan", 18, 18, 1),
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/magenta", 18, 18, 1),
            AdaptableUITexture.of(GTPlusPlus.ID, "gui/slot/yellow", 18, 18, 1), };

    public static final UITexture BUTTON_STANDARD_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/button/standard_bronze");

    public static final UITexture OVERLAY_SLOT_WEED_EX = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/weed_ex");
    public static final UITexture OVERLAY_SLOT_FERTILIZER = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/fertilizer");
    public static final UITexture OVERLAY_SLOT_ELECTRIC_TOOL = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/electric_tool");
    public static final UITexture OVERLAY_SLOT_PAGE_PRINTED_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/page_printed_bronze");
    public static final UITexture OVERLAY_SLOT_ARROW = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow");
    public static final UITexture OVERLAY_SLOT_ARROW_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_bronze");
    public static final UITexture OVERLAY_SLOT_CRAFT_OUTPUT = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/craft_output");
    public static final UITexture OVERLAY_SLOT_CRAFT_OUTPUT_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/craft_output_bronze");
    public static final UITexture OVERLAY_SLOT_PARK = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/park");
    public static final UITexture OVERLAY_SLOT_PARK_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_slot/park_bronze");
    public static final UITexture OVERLAY_SLOT_INGOT = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/ingot");
    public static final UITexture OVERLAY_SLOT_ARROW_4 = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_4");
    public static final UITexture OVERLAY_SLOT_TURBINE = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/turbine");
    public static final UITexture OVERLAY_SLOT_CHEST = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/chest");
    public static final UITexture[] OVERLAY_SLOT_INVENTORY_MANAGER_COLOR = new UITexture[] {
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/red"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/green"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/blue"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/cyan"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/magenta"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/yellow"), };
    public static final UITexture[] OVERLAY_SLOT_INVENTORY_MANAGER_ARROW = new UITexture[] {
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_red"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_green"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_blue"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_cyan"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_magenta"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_yellow"), };

    public static final UITexture PROGRESSBAR_FLUID_REACTOR = UITexture
            .fullImage(GTPlusPlus.ID, "gui/progressbar/fluid_reactor");
    public static final UITexture PROGRESSBAR_BOILER_EMPTY = UITexture
            .fullImage(GTPlusPlus.ID, "gui/progressbar/boiler_empty");
    public static final UITexture PROGRESSBAR_FUEL = UITexture.fullImage(GTPlusPlus.ID, "gui/progressbar/fuel");
    public static final UITexture PROGRESSBAR_ARROW_2 = UITexture.fullImage(GTPlusPlus.ID, "gui/progressbar/arrow_2");
    public static final UITexture PROGRESSBAR_PSS_ENERGY = UITexture
            .fullImage(GTPlusPlus.ID, "gui/progressbar/pss_energy");

    public static final AdaptableUITexture TAB_TITLE_YELLOW = AdaptableUITexture
            .of(GTPlusPlus.ID, "gui/tab/title_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_ANGULAR_YELLOW = AdaptableUITexture
            .of(GTPlusPlus.ID, "gui/tab/title_angular_yellow", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_DARK_YELLOW = AdaptableUITexture
            .of(GTPlusPlus.ID, "gui/tab/title_dark_yellow", 28, 28, 4);

    public static final UITexture OVERLAY_BUTTON_HARVESTER_MODE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/harvester_mode");
    public static final UITexture OVERLAY_BUTTON_HARVESTER_TOGGLE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/harvester_toggle");
    public static final UITexture OVERLAY_BUTTON_FLUSH = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/flush");
    public static final UITexture OVERLAY_BUTTON_FLUSH_BRONZE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/flush_bronze");
    public static final UITexture OVERLAY_BUTTON_AUTOMATION = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/automation");
    public static final UITexture OVERLAY_BUTTON_LOCK = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/lock");
    public static final UITexture[] OVERLAY_BUTTON_THROUGHPUT = IntStream.range(0, 4) // GT_MetaTileEntity_ElectricAutoWorkbench#MAX_THROUGHPUT
            .mapToObj(i -> UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/throughput_" + i))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] OVERLAY_BUTTON_MODE = IntStream.range(0, 10) // GT_MetaTileEntity_ElectricAutoWorkbench#MAX_MODES
            .mapToObj(i -> UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/mode_" + i))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] OVERLAY_BUTTON_DIRECTION = new UITexture[] {
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/bottom"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/top"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/north"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/south"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/west"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/east"), };
    public static final UITexture[] OVERLAY_BUTTON_DIRECTION_GRAY = new UITexture[] {
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/bottom_gray"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/top_gray"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/north_gray"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/south_gray"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/west_gray"),
            UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/east_gray"), };
    public static final UITexture OVERLAY_BUTTON_TIP_GREEN = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/tip_green");
    public static final UITexture OVERLAY_BUTTON_TIP_RED = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/tip_red");
    public static final UITexture OVERLAY_BUTTON_ACTIVE_STATE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/active_state");
    public static final UITexture OVERLAY_BUTTON_CHANGE_MODE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/change_mode");
    public static final UITexture OVERLAY_BUTTON_PLUS_MINUS = UITexture
            .fullImage(GTPlusPlus.ID, "gui/overlay_button/plus_minus");

    public static final UITexture PICTURE_WORKBENCH_CIRCLE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/picture/workbench_circle");
    public static final UITexture PICTURE_ARROW_WHITE_DOWN = UITexture
            .fullImage(GTPlusPlus.ID, "gui/picture/arrow_white_down");
    public static final UITexture PICTURE_REDSTONE_CIRCUIT_SCREEN = UITexture
            .fullImage(GTPlusPlus.ID, "gui/picture/redstone_circuit_screen");
    public static final UITexture PICTURE_ELECTRICITY_ERROR = UITexture
            .fullImage(GTPlusPlus.ID, "gui/picture/electricity_error");
    public static final UITexture PICTURE_ELECTRICITY_FINE = UITexture
            .fullImage(GTPlusPlus.ID, "gui/picture/electricity_fine");
    public static final UITexture PICTURE_ENERGY_FRAME = UITexture.fullImage(GTPlusPlus.ID, "gui/picture/energy_frame");
}
