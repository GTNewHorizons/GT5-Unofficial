package com.github.technus.tectech.thing.gui;

import static com.github.technus.tectech.Reference.MODID;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

@SuppressWarnings("SimplifyStreamApiCallChains")
public class TecTechUITextures {

    public static final AdaptableUITexture BACKGROUND_SCREEN_BLUE = AdaptableUITexture
            .of(MODID, "gui/background/screen_blue", 90, 72, 2);
    public static final UITexture BACKGROUND_SCREEN_BLUE_PARAMETRIZER_TXT = UITexture
            .fullImage(MODID, "gui/background/screen_blue_parametrizer_txt");
    public static final UITexture BACKGROUND_SCREEN_BLUE_NO_INVENTORY = UITexture
            .fullImage(MODID, "gui/background/screen_blue_no_inventory");

    public static final UITexture BUTTON_STANDARD_16x16 = UITexture.fullImage(MODID, "gui/button/standard_16x16");
    public static final UITexture BUTTON_STANDARD_LIGHT_16x16 = UITexture
            .fullImage(MODID, "gui/button/standard_light_16x16");

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
            .collect(Collectors.toList()).toArray(new UITexture[0]);
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
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_ID = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_id");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_0 = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_0");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_1 = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_1");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_X = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_x");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_S = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_s");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_T = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_t");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_C = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_c");
    public static final UITexture OVERLAY_BUTTON_PARAMETRIZER_IF = UITexture
            .fullImage(MODID, "gui/overlay_button/parametrizer_if");

    public static final UITexture OVERLAY_SLOT_RACK = UITexture.fullImage(MODID, "gui/overlay_slot/rack");
    public static final UITexture OVERLAY_SLOT_MESH = UITexture.fullImage(MODID, "gui/overlay_slot/mesh");

    public static final UITexture PROGRESSBAR_RESEARCH_STATION_1 = UITexture
            .fullImage(MODID, "gui/progressbar/research_station_1");
    public static final UITexture PROGRESSBAR_RESEARCH_STATION_2 = UITexture
            .fullImage(MODID, "gui/progressbar/research_station_2");
    public static final UITexture PROGRESSBAR_RESEARCH_STATION_3 = UITexture
            .fullImage(MODID, "gui/progressbar/research_station_3");

    public static final UITexture PICTURE_TECTECH_LOGO = UITexture.fullImage(MODID, "gui/picture/tectech_logo");
    public static final UITexture PICTURE_TECTECH_LOGO_DARK = UITexture
            .fullImage(MODID, "gui/picture/tectech_logo_dark");
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
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_UNCERTAINTY_INVALID = IntStream.range(0, 9)
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/picture/uncertainty/invalid_" + i))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture PICTURE_HEAT_SINK_SMALL = UITexture.fullImage(MODID, "gui/picture/heat_sink_small");
    public static final UITexture PICTURE_PARAMETER_BLANK = UITexture.fullImage(MODID, "gui/picture/parameter_blank");
    public static final UITexture[] PICTURE_PARAMETER_BLUE = IntStream.range(0, 20)
            .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_blue", 158, 4, i * 8, 0, i * 8 + 6, 4))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_CYAN = IntStream.range(0, 20)
            .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_cyan", 158, 4, i * 8, 0, i * 8 + 6, 4))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_GREEN = IntStream.range(0, 20)
            .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_green", 158, 4, i * 8, 0, i * 8 + 6, 4))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_ORANGE = IntStream.range(0, 20)
            .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_orange", 158, 4, i * 8, 0, i * 8 + 6, 4))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture[] PICTURE_PARAMETER_RED = IntStream.range(0, 20)
            .mapToObj(i -> UITexture.partly(MODID, "gui/picture/parameter_red", 158, 4, i * 8, 0, i * 8 + 6, 4))
            .collect(Collectors.toList()).toArray(new UITexture[0]);
    public static final UITexture PICTURE_PARAMETER_GRAY = UITexture.fullImage(MODID, "gui/picture/parameter_gray");
    public static final UITexture PICTURE_UNCERTAINTY_MONITOR_MULTIMACHINE = UITexture
            .fullImage(MODID, "gui/picture/uncertainty/monitor_multimachine");
}
