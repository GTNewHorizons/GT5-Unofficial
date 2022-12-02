package com.github.bartimaeusnek.bartworks.API.modularUI;

import static com.github.bartimaeusnek.bartworks.MainMod.MOD_ID;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BW_UITextures {

    public static final AdaptableUITexture BACKGROUND_BROWN =
            AdaptableUITexture.of(MOD_ID, "GUI/background/brown", 176, 166, 3);
    public static final UITexture BACKGROUND_CIRCUIT_PROGRAMMER =
            UITexture.fullImage(MOD_ID, "GUI/background/circuit_programmer");

    public static final AdaptableUITexture SLOT_BROWN = AdaptableUITexture.of(MOD_ID, "GUI/slot/brown", 18, 18, 1);

    public static final UITexture OVERLAY_SLOT_DISH = UITexture.fullImage(MOD_ID, "GUI/overlay_slot/dish");
    public static final UITexture OVERLAY_SLOT_DNA_FLASK = UITexture.fullImage(MOD_ID, "GUI/overlay_slot/dna_flask");
    public static final UITexture OVERLAY_SLOT_MODULE = UITexture.fullImage(MOD_ID, "GUI/overlay_slot/module");
    public static final UITexture OVERLAY_SLOT_ROD = UITexture.fullImage(MOD_ID, "GUI/overlay_slot/rod");
    public static final UITexture OVERLAY_SLOT_CROSS = UITexture.fullImage(MOD_ID, "GUI/overlay_slot/cross");

    public static final UITexture PROGRESSBAR_SIEVERT = UITexture.fullImage(MOD_ID, "GUI/progressbar/sievert");
    public static final UITexture PROGRESSBAR_STORED_EU_116 =
            UITexture.fullImage(MOD_ID, "GUI/progressbar/stored_eu_116");
    public static final UITexture PROGRESSBAR_FUEL = UITexture.fullImage(MOD_ID, "GUI/progressbar/fuel");

    public static final UITexture PICTURE_BW_LOGO_47X21 = UITexture.fullImage(MOD_ID, "GUI/picture/bw_logo_47x21");
    public static final UITexture PICTURE_SIEVERT_CONTAINER =
            UITexture.fullImage(MOD_ID, "GUI/picture/sievert_container");
    public static final UITexture PICTURE_DECAY_TIME_CONTAINER =
            UITexture.fullImage(MOD_ID, "GUI/picture/decay_time_container");
    public static final UITexture PICTURE_DECAY_TIME_INSIDE =
            UITexture.fullImage(MOD_ID, "GUI/picture/decay_time_inside");
    public static final UITexture PICTURE_RADIATION = UITexture.fullImage(MOD_ID, "GUI/picture/radiation");
    public static final UITexture PICTURE_WINDMILL_EMPTY = UITexture.fullImage(MOD_ID, "GUI/picture/windmill_empty");
    public static final UITexture[] PICTURE_WINDMILL_ROTATING = IntStream.range(0, 4)
            .mapToObj(i ->
                    UITexture.partly(MOD_ID, "GUI/picture/windmill_rotating", 32, 128, 0, i * 32, 32, (i + 1) * 32))
            .collect(Collectors.toList())
            .toArray(new UITexture[0]);
    public static final UITexture PICTURE_STORED_EU_FRAME = UITexture.fullImage(MOD_ID, "GUI/picture/stored_eu_frame");
    public static final UITexture PICTURE_RADIATION_SHUTTER_FRAME =
            UITexture.fullImage(MOD_ID, "GUI/picture/radiation_shutter_frame");
    public static final AdaptableUITexture PICTURE_RADIATION_SHUTTER_INSIDE =
            AdaptableUITexture.of(MOD_ID, "GUI/picture/radiation_shutter_inside", 51, 48, 1);

    public static final AdaptableUITexture TAB_TITLE_BROWN =
            AdaptableUITexture.of(MOD_ID, "GUI/tab/title_brown", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_DARK_BROWN =
            AdaptableUITexture.of(MOD_ID, "GUI/tab/title_dark_brown", 28, 28, 4);
    public static final AdaptableUITexture TAB_TITLE_ANGULAR_BROWN =
            AdaptableUITexture.of(MOD_ID, "GUI/tab/title_angular_brown", 28, 28, 4);
}
