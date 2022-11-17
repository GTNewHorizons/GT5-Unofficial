package com.github.technus.tectech.thing.gui;

import static com.github.technus.tectech.Reference.MODID;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TecTechUITextures {

    public static final AdaptableUITexture BACKGROUND_SCREEN_BLUE =
            AdaptableUITexture.of(MODID, "gui/background/screen_blue", 90, 72, 2);

    public static final UITexture BUTTON_STANDARD_16x16 = UITexture.fullImage(MODID, "gui/button/standard_16x16");

    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_OFF =
            UITexture.fullImage(MODID, "gui/overlay_button/power_switch_off");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_ON =
            UITexture.fullImage(MODID, "gui/overlay_button/power_switch_on");
    public static final UITexture OVERLAY_BUTTON_HEAT_OFF = UITexture.fullImage(MODID, "gui/overlay_button/heat_off");
    public static final UITexture OVERLAY_BUTTON_HEAT_ON = UITexture.fullImage(MODID, "gui/overlay_button/heat_on");
    public static final UITexture[] OVERLAY_BUTTON_UNCERTAINTY = IntStream.range(0, 16)
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/overlay_button/uncertainty/" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[] {});

    public static final UITexture OVERLAY_SLOT_RACK = UITexture.fullImage(MODID, "gui/overlay_slot/rack");

    public static final UITexture PICTURE_TECTECH_LOGO = UITexture.fullImage(MODID, "gui/picture/tectech_logo");
    public static final UITexture PICTURE_TECTECH_LOGO_DARK =
            UITexture.fullImage(MODID, "gui/picture/tectech_logo_dark");
    public static final UITexture PICTURE_RACK_LARGE = UITexture.fullImage(MODID, "gui/picture/rack_large");
    public static final UITexture PICTURE_HEAT_SINK = UITexture.fullImage(MODID, "gui/picture/heat_sink");
    public static final UITexture PICTURE_UNCERTAINTY_MONITOR =
            UITexture.fullImage(MODID, "gui/picture/uncertainty/monitor");
    public static final UITexture PICTURE_UNCERTAINTY_INDICATOR =
            UITexture.fullImage(MODID, "gui/picture/uncertainty/indicator");
    public static final UITexture PICTURE_UNCERTAINTY_SELECTED =
            UITexture.fullImage(MODID, "gui/picture/uncertainty/selected");
    public static final UITexture[] PICTURE_UNCERTAINTY_VALID = IntStream.range(0, 9)
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/picture/uncertainty/valid_" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[] {});
    public static final UITexture[] PICTURE_UNCERTAINTY_INVALID = IntStream.range(0, 9)
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/picture/uncertainty/invalid_" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[] {});
}
