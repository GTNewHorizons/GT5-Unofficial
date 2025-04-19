package tectech.thing.gui;

import static tectech.Reference.MODID;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

@SuppressWarnings("SimplifyStreamApiCallChains")
public class TecTechUITextures {

    public static final AdaptableUITexture BACKGROUND_SCREEN_BLUE = AdaptableUITexture
        .of(MODID, "gui/background/screen_blue", 90, 72, 2);
    public static final UITexture BACKGROUND_SCREEN_BLUE_NO_INVENTORY = UITexture
        .fullImage(MODID, "gui/background/screen_blue_no_inventory");
    public static final UITexture BACKGROUND_STAR = UITexture.fullImage(MODID, "gui/background/star");
    public static final UITexture BACKGROUND_GLOW_ORANGE = UITexture.fullImage(MODID, "gui/background/orange_glow");
    public static final UITexture BACKGROUND_GLOW_PURPLE = UITexture.fullImage(MODID, "gui/background/purple_glow");
    public static final UITexture BACKGROUND_GLOW_BLUE = UITexture.fullImage(MODID, "gui/background/blue_glow");
    public static final UITexture BACKGROUND_GLOW_GREEN = UITexture.fullImage(MODID, "gui/background/green_glow");
    public static final UITexture BACKGROUND_GLOW_RED = UITexture.fullImage(MODID, "gui/background/red_glow");
    public static final UITexture BACKGROUND_GLOW_WHITE = UITexture.fullImage(MODID, "gui/background/white_glow");
    public static final UITexture BACKGROUND_GLOW_WHITE_HALF = UITexture
        .fullImage(MODID, "gui/background/white_glow_half");
    public static final UITexture BACKGROUND_GLOW_RAINBOW = UITexture.fullImage(MODID, "gui/background/rainbow_glow");
    public static final UITexture BACKGROUND_SPACE = UITexture.fullImage(MODID, "gui/background/space");

    public static final UITexture BUTTON_STANDARD_16x16 = UITexture.fullImage(MODID, "gui/button/standard_16x16");
    public static final UITexture BUTTON_STANDARD_LIGHT_16x16 = UITexture
        .fullImage(MODID, "gui/button/standard_light_16x16");
    public static final UITexture BUTTON_CELESTIAL_32x32 = UITexture.fullImage(MODID, "gui/button/celestial");
    public static final UITexture BUTTON_SPACE_32x16 = UITexture.fullImage(MODID, "gui/button/purple");
    public static final UITexture BUTTON_SPACE_PRESSED_32x16 = UITexture.fullImage(MODID, "gui/button/purple_pressed");
    public static final UITexture BUTTON_BOXED_CHECKMARK_18x18 = UITexture
        .fullImage(MODID, "gui/button/boxed_checkmark");
    public static final UITexture BUTTON_BOXED_EXCLAMATION_POINT_18x18 = UITexture
        .fullImage(MODID, "gui/button/boxed_exclamation_point");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_DISABLED = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_disabled");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_off");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_on");
    public static final UITexture OVERLAY_BUTTON_HEAT_OFF = UITexture.fullImage(MODID, "gui/overlay_button/heat_off");
    public static final UITexture OVERLAY_BUTTON_HEAT_ON = UITexture.fullImage(MODID, "gui/overlay_button/heat_on");
    public static final UITexture[] OVERLAY_BUTTON_UNCERTAINTY = IntStream.range(0, 16)
        .mapToObj(i -> UITexture.fullImage(MODID, "gui/overlay_button/uncertainty/" + i))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture OVERLAY_BUTTON_SAFE_VOID_DISABLED = UITexture
        .fullImage(MODID, "gui/overlay_button/safe_void_disabled");
    public static final UITexture OVERLAY_BUTTON_SAFE_VOID_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/safe_void_off");
    public static final UITexture OVERLAY_BUTTON_SAFE_VOID_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/safe_void_on");
    public static final UITexture OVERLAY_BUTTON_POWER_PASS_DISABLED = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_disabled");
    public static final UITexture OVERLAY_BUTTON_POWER_PASS_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_off");
    public static final UITexture OVERLAY_BUTTON_POWER_PASS_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_on");
    public static final UITexture OVERLAY_BUTTON_ARROW_BLUE_UP = UITexture
        .fullImage(MODID, "gui/overlay_button/arrow_blue_up");
    public static final UITexture OVERLAY_BUTTON_BATTERY_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/battery_on");
    public static final UITexture OVERLAY_BUTTON_BATTERY_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/battery_off");
    public static final UITexture OVERLAY_BUTTON_FLAG = UITexture.fullImage(MODID, "gui/overlay_button/flag");
    public static final UITexture OVERLAY_BUTTON_HEART = UITexture.fullImage(MODID, "gui/overlay_button/heart");
    public static final UITexture OVERLAY_BUTTON_RAINBOW_SPIRAL = UITexture
        .fullImage(MODID, "gui/overlay_button/rainbow_spiral");
    public static final UITexture OVERLAY_BUTTON_RAINBOW_SPIRAL_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/rainbow_spiral_off");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION = UITexture
        .fullImage(MODID, "gui/overlay_button/input_separation_on");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/input_separation_off");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/batch_mode_on");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/batch_mode_off");
    public static final UITexture OVERLAY_BUTTON_LOAF_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/loaf_mode_on");
    public static final UITexture OVERLAY_BUTTON_LOAF_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/loaf_mode_off");
    public static final UITexture OVERLAY_BUTTON_RECIPE_LOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/recipe_locked");
    public static final UITexture OVERLAY_BUTTON_RECIPE_UNLOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/recipe_unlocked");
    public static final UITexture OVERLAY_BUTTON_VOIDING_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_disabled");
    public static final UITexture OVERLAY_BUTTON_VOIDING_ITEMS = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_items");
    public static final UITexture OVERLAY_BUTTON_VOIDING_FLUIDS = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_fluids");
    public static final UITexture OVERLAY_BUTTON_VOIDING_BOTH = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_both");
    public static final UITexture OVERLAY_BUTTON_STRUCTURE_CHECK = UITexture
        .fullImage(MODID, "gui/overlay_button/structure_check_on");
    public static final UITexture OVERLAY_BUTTON_STRUCTURE_CHECK_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/structure_check_off");
    public static final UITexture OVERLAY_BUTTON_FURNACE_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/furnace_mode_on");
    public static final UITexture OVERLAY_BUTTON_FURNACE_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/furnace_mode_off");
    public static final UITexture OVERLAY_BUTTON_STATISTICS = UITexture
        .fullImage(MODID, "gui/overlay_button/statistics");
    public static final UITexture OVERLAY_BUTTON_POWER_PANEL = UITexture
        .fullImage(MODID, "gui/overlay_button/power_panel");
    public static final UITexture OVERLAY_CYCLIC_BLUE = UITexture.fullImage(MODID, "gui/overlay_button/cyclic_blue");
    public static final UITexture OVERLAY_EJECTION_LOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/eject_disabled");
    public static final UITexture OVERLAY_EJECTION_ON = UITexture.fullImage(MODID, "gui/overlay_button/eject");

    public static final UITexture OVERLAY_SLOT_RACK = UITexture.fullImage(MODID, "gui/overlay_slot/rack");
    public static final UITexture OVERLAY_SLOT_MESH = UITexture.fullImage(MODID, "gui/overlay_slot/mesh");

    public static final UITexture PROGRESSBAR_RESEARCH_STATION_1 = UITexture
        .fullImage(MODID, "gui/progressbar/research_station_1");
    public static final UITexture PROGRESSBAR_RESEARCH_STATION_2 = UITexture
        .fullImage(MODID, "gui/progressbar/research_station_2");
    public static final UITexture PROGRESSBAR_RESEARCH_STATION_3 = UITexture
        .fullImage(MODID, "gui/progressbar/research_station_3");
    public static final UITexture PROGRESSBAR_GODFORGE_PLASMA = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_plasma");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_background");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_BLUE = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_blue");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_RED = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_red");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_PURPLE = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_purple");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_rainbow");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_BLUE_INVERTED = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_blue_inverted");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_RED_INVERTED = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_red_inverted");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_PURPLE_INVERTED = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_purple_inverted");
    public static final UITexture PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW_INVERTED = UITexture
        .fullImage(MODID, "gui/progressbar/godforge_progressbar_rainbow_inverted");

    public static final UITexture PICTURE_TECTECH_LOGO = UITexture.fullImage(MODID, "gui/picture/tectech_logo");
    public static final UITexture PICTURE_TECTECH_LOGO_DARK = UITexture
        .fullImage(MODID, "gui/picture/tectech_logo_dark");
    public static final UITexture PICTURE_GODFORGE_LOGO = UITexture.fullImage(MODID, "gui/picture/gorge_logo");
    public static final UITexture PICTURE_RACK_LARGE = UITexture.fullImage(MODID, "gui/picture/rack_large");
    public static final UITexture PICTURE_HEAT_SINK = UITexture.fullImage(MODID, "gui/picture/heat_sink");
    public static final UITexture PICTURE_UNCERTAINTY_MONITOR = UITexture
        .fullImage(MODID, "gui/picture/uncertainty/monitor");
    public static final UITexture PICTURE_UNCERTAINTY_INDICATOR = UITexture
        .fullImage(MODID, "gui/picture/uncertainty/indicator");
    public static final UITexture PICTURE_UNCERTAINTY_SELECTED = UITexture
        .fullImage(MODID, "gui/picture/uncertainty/selected");
    public static final UITexture[] PICTURE_UNCERTAINTY_VALID = IntStream.range(0, 9)
        .mapToObj(i -> UITexture.fullImage(MODID, "gui/picture/uncertainty/valid_" + i))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_UNCERTAINTY_INVALID = IntStream.range(0, 9)
        .mapToObj(i -> UITexture.fullImage(MODID, "gui/picture/uncertainty/invalid_" + i))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture PICTURE_HEAT_SINK_SMALL = UITexture.fullImage(MODID, "gui/picture/heat_sink_small");
    public static final UITexture PICTURE_HEAT_SINK_16x8 = UITexture.fullImage(MODID, "gui/picture/heat_sink_16x8");
    public static final UITexture PICTURE_PARAMETER_BLANK = UITexture.fullImage(MODID, "gui/picture/parameter_blank");
    public static final UITexture[] PICTURE_PARAMETER_BLUE = IntStream.range(0, 20)
        .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_blue", 158, 4, i * 8, 0, i * 8 + 6, 4))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_CYAN = IntStream.range(0, 20)
        .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_cyan", 158, 4, i * 8, 0, i * 8 + 6, 4))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_GREEN = IntStream.range(0, 20)
        .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_green", 158, 4, i * 8, 0, i * 8 + 6, 4))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_ORANGE = IntStream.range(0, 20)
        .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_orange", 158, 4, i * 8, 0, i * 8 + 6, 4))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_RED = IntStream.range(0, 20)
        .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_red", 158, 4, i * 8, 0, i * 8 + 6, 4))
        .collect(Collectors.toList())
        .toArray(new UITexture[0]);
    public static final UITexture PICTURE_PARAMETER_GRAY = UITexture.fullImage(MODID, "gui/picture/parameter_gray");
    public static final UITexture PICTURE_UNCERTAINTY_MONITOR_MULTIMACHINE = UITexture
        .fullImage(MODID, "gui/picture/uncertainty/monitor_multimachine");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_PURPLE = UITexture
        .fullImage(MODID, "gui/picture/connector_purple");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_GREEN = UITexture
        .fullImage(MODID, "gui/picture/connector_green");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_ORANGE = UITexture
        .fullImage(MODID, "gui/picture/connector_orange");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_BLUE = UITexture
        .fullImage(MODID, "gui/picture/connector_blue");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_RED = UITexture
        .fullImage(MODID, "gui/picture/connector_red");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_PURPLE_OPAQUE = UITexture
        .fullImage(MODID, "gui/picture/connector_purple_opaque");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_GREEN_OPAQUE = UITexture
        .fullImage(MODID, "gui/picture/connector_green_opaque");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_ORANGE_OPAQUE = UITexture
        .fullImage(MODID, "gui/picture/connector_orange_opaque");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_BLUE_OPAQUE = UITexture
        .fullImage(MODID, "gui/picture/connector_blue_opaque");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_RED_OPAQUE = UITexture
        .fullImage(MODID, "gui/picture/connector_red_opaque");
    public static final UITexture SLOT_OUTLINE_GREEN = UITexture.fullImage(MODID, "gui/picture/green_selector");
    public static final UITexture UNSELECTED_OPTION = UITexture.fullImage(MODID, "gui/picture/unselected_option");
    public static final UITexture GREEN_CHECKMARK_11x9 = UITexture.fullImage(MODID, "gui/picture/green_checkmark");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CHARGE = UITexture
        .fullImage(MODID, "gui/picture/milestone_charge");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CONVERSION = UITexture
        .fullImage(MODID, "gui/picture/milestone_conversion");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CATALYST = UITexture
        .fullImage(MODID, "gui/picture/milestone_catalyst");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_COMPOSITION = UITexture
        .fullImage(MODID, "gui/picture/milestone_composition");
    public static final UITexture PICTURE_GODFORGE_THANKS = UITexture.fullImage(MODID, "gui/picture/thanks");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW = UITexture
        .fullImage(MODID, "gui/picture/milestone_charge_glow");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CONVERSION_GLOW = UITexture
        .fullImage(MODID, "gui/picture/milestone_conversion_glow");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CATALYST_GLOW = UITexture
        .fullImage(MODID, "gui/picture/milestone_catalyst_glow");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_COMPOSITION_GLOW = UITexture
        .fullImage(MODID, "gui/picture/milestone_composition_glow");
    public static final UITexture PICTURE_OVERLAY_BLUE = UITexture.fullImage(MODID, "gui/picture/overlay_blue");
    public static final UITexture PICTURE_OVERLAY_ORANGE = UITexture.fullImage(MODID, "gui/picture/overlay_orange");
    public static final UITexture PICTURE_OVERLAY_GREEN = UITexture.fullImage(MODID, "gui/picture/overlay_green");
    public static final UITexture PICTURE_OVERLAY_PURPLE = UITexture.fullImage(MODID, "gui/picture/overlay_purple");
    public static final UITexture PICTURE_OVERLAY_RED = UITexture.fullImage(MODID, "gui/picture/overlay_red");
    public static final UITexture PICTURE_RAINBOW_SQUARE = UITexture.fullImage(MODID, "gui/picture/rainbow_square");

}
