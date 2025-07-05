package gregtech.api.modularui2;

import static com.cleanroommc.modularui.drawable.UITexture.fullImage;
import static gregtech.api.enums.Mods.GregTech;

import com.cleanroommc.modularui.drawable.UITexture;

import bartworks.MainMod;
import gregtech.common.modularui2.util.SteamTextureRegisterer;

/**
 * Holds all the references to GUI textures used within GregTech.
 */
public final class GTGuiTextures {

    public static void init() {}

    // region background

    public static final UITexture BACKGROUND_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/background/singleblock_default")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_STANDARD)
        .build();
    private static final SteamTextureRegisterer BACKGROUND_STEAM = SteamTextureRegisterer.builder()
        .location("gui/background/%s")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_BRONZE, GTTextureIds.BACKGROUND_STEEL, GTTextureIds.BACKGROUND_PRIMITIVE)
        .build();
    public static final UITexture BACKGROUND_TITLE_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/tab/title_dark")
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_TITLE_STANDARD)
        .build();
    private static final SteamTextureRegisterer BACKGROUND_TITLE_STEAM = SteamTextureRegisterer.builder()
        .location("gui/tab/title_dark_%s")
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .name(
            GTTextureIds.BACKGROUND_TITLE_BRONZE,
            GTTextureIds.BACKGROUND_TITLE_STEEL,
            GTTextureIds.BACKGROUND_TITLE_PRIMITIVE)
        .build();

    public static final UITexture BACKGROUND_POPUP_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/background/popup")
        .imageSize(195, 136)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_POPUP_STANDARD)
        .build();
    private static final SteamTextureRegisterer BACKGROUND_POPUP_STEAM = SteamTextureRegisterer.builder()
        .location("gui/background/popup_%s")
        .imageSize(195, 136)
        .adaptable(4)
        .canApplyTheme()
        .name(
            GTTextureIds.BACKGROUND_POPUP_BRONZE,
            GTTextureIds.BACKGROUND_POPUP_STEEL,
            GTTextureIds.BACKGROUND_POPUP_PRIMITIVE)
        .build();
    public static final UITexture BACKGROUND_TEXT_FIELD = UITexture.builder()
        .location(GregTech.ID, "gui/background/text_field")
        .imageSize(142, 28)
        .adaptable(1)
        .name(GTTextureIds.BACKGROUND_TERMINAL_STANDARD)
        .build();
    // endregion background

    // region overlay
    public static final UITexture OVERLAY_GREGTECH_LOGO = fullImageGT("picture/gt_logo_standard");

    // endregion overlay
    // region slot

    public static final UITexture SLOT_ITEM_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_ITEM_STANDARD)
        .build();
    public static final UITexture SLOT_ITEM_BRONZE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_bronze")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_ITEM_BRONZE)
        .build();
    public static final UITexture SLOT_ITEM_STEEL = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_steel")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_ITEM_STEEL)
        .build();
    public static final UITexture SLOT_ITEM_PRIMITIVE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_primitive")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_ITEM_PRIMITIVE)
        .build();
    public static final UITexture SLOT_ITEM_DARK = UITexture.builder()
        .location(GregTech.ID, "gui/slot/dark_gray")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .build();
    public static final UITexture SLOT_FLUID_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_FLUID_STANDARD)
        .build();
    public static final UITexture SLOT_FLUID_BRONZE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_bronze")
        .imageSize(18, 18)
        .adaptable(1)
        .name(GTTextureIds.SLOT_FLUID_BRONZE)
        .build();
    public static final UITexture SLOT_FLUID_STEEL = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_steel")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_FLUID_STEEL)
        .build();
    public static final UITexture SLOT_FLUID_PRIMITIVE = UITexture.builder()
        .location(GregTech.ID, "gui/slot/fluid_primitive")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_FLUID_PRIMITIVE)
        .build();

    // endregion slot

    // region slot overlay

    public static final UITexture OVERLAY_SLOT_CANISTER_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/canister")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_CANISTER_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_CANISTER_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/canister_%s")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_CANISTER_BRONZE, GTTextureIds.OVERLAY_SLOT_CANISTER_STEEL, null)
        .build();
    public static final UITexture OVERLAY_SLOT_DATA_ORB = fullImageColorableGT("overlay_slot/data_orb");
    public static final UITexture OVERLAY_SLOT_DUST_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/dust")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_DUST_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_DUST_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/dust_%s")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.OVERLAY_SLOT_DUST_BRONZE,
            GTTextureIds.OVERLAY_SLOT_DUST_STEEL,
            GTTextureIds.OVERLAY_SLOT_DUST_PRIMITIVE)
        .build();
    public static final UITexture OVERLAY_SLOT_INGOT_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/ingot")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_INGOT_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_INGOT_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/ingot_%s")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.OVERLAY_SLOT_INGOT_BRONZE,
            GTTextureIds.OVERLAY_SLOT_INGOT_STEEL,
            GTTextureIds.OVERLAY_SLOT_INGOT_PRIMITIVE)
        .build();
    public static final UITexture OVERLAY_SLOT_INT_CIRCUIT = fullImageColorableGT("overlay_slot/int_circuit");
    public static final UITexture OVERLAY_SLOT_FURNACE_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/furnace")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_FURNACE_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_FURNACE_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/furnace_%s")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.OVERLAY_SLOT_FURNACE_BRONZE,
            GTTextureIds.OVERLAY_SLOT_FURNACE_STEEL,
            GTTextureIds.OVERLAY_SLOT_FURNACE_PRIMITIVE)
        .build();
    public static final UITexture OVERLAY_SLOT_IN_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/in")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_IN_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_IN_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/in_%s")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_IN_BRONZE, GTTextureIds.OVERLAY_SLOT_IN_STEEL, null)
        .build();
    public static final UITexture OVERLAY_SLOT_OUT_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/out")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_OUT_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_OUT_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/out_%s")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_OUT_BRONZE, GTTextureIds.OVERLAY_SLOT_OUT_STEEL, null)
        .build();
    public static final UITexture OVERLAY_SLOT_COAL_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/coal")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_COAL_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_COAL_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/coal_%s")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_COAL_BRONZE, GTTextureIds.OVERLAY_SLOT_COAL_STEEL, null)
        .build();
    public static final UITexture OVERLAY_SLOT_BLOCK_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/block")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.OVERLAY_SLOT_BLOCK_STANDARD)
        .build();
    private static final SteamTextureRegisterer OVERLAY_SLOT_BLOCK_STEAM = SteamTextureRegisterer.builder()
        .location("gui/overlay_slot/block_%s")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.OVERLAY_SLOT_BLOCK_BRONZE,
            GTTextureIds.OVERLAY_SLOT_BLOCK_STEEL,
            GTTextureIds.OVERLAY_SLOT_BLOCK_PRIMITIVE)
        .build();

    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_NONE = UITexture.builder()
        .canApplyTheme(true)
        .location(GregTech.ID, "gui/overlay_button/void_excess_none")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ITEM = UITexture.builder()
        .canApplyTheme(true)
        .location(GregTech.ID, "gui/overlay_button/void_excess_item")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_FLUID = UITexture.builder()
        .canApplyTheme(true)
        .location(GregTech.ID, "gui/overlay_button/void_excess_fluid")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ALL = UITexture.builder()
        .canApplyTheme(true)
        .location(GregTech.ID, "gui/overlay_button/void_excess_all")
        .build();
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_ON = fullImage(
        GregTech.ID,
        "gui/overlay_button/input_separation_on");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/input_separation_on_disabled");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_OFF = fullImage(
        GregTech.ID,
        "gui/overlay_button/input_separation_off");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/input_separation_off_disabled");
    public static final UITexture OVERLAY_BUTTON_RECIPE_UNLOCKED = fullImage(
        GregTech.ID,
        "gui/overlay_button/recipe_unlocked");
    public static final UITexture OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/recipe_unlocked_disabled");
    public static final UITexture OVERLAY_BUTTON_RECIPE_LOCKED = fullImage(
        GregTech.ID,
        "gui/overlay_button/recipe_locked");
    public static final UITexture OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/recipe_locked_disabled");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_ON = fullImage(
        GregTech.ID,
        "gui/overlay_button/batch_mode_on");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/batch_mode_on_disabled");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_OFF = fullImage(
        GregTech.ID,
        "gui/overlay_button/batch_mode_off");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED = fullImage(
        GregTech.ID,
        "gui/overlay_button/batch_mode_off_disabled");
    public static final UITexture OVERLAY_BUTTON_STRUCTURE_UPDATE = fullImage(
        GregTech.ID,
        "gui/overlay_button/structure_update");
    public static final UITexture OVERLAY_BUTTON_FORBIDDEN = fullImage(GregTech.ID, "gui/overlay_button/forbidden");

    public static final UITexture OVERLAY_BUTTON_BOUNDING_BOX = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_button/bounding_box")
        .fullImage()
        .canApplyTheme(true)
        .build();

    // endregion slot overlay

    // region progressbar

    public static final UITexture PROGRESSBAR_ARROW_STANDARD = fullImageColorableGT("progressbar/arrow");
    public static final UITexture PROGRESSBAR_ARROW_BRONZE = fullImageColorableGT("progressbar/arrow_bronze");
    public static final UITexture PROGRESSBAR_ARROW_STEEL = fullImageColorableGT("progressbar/arrow_steel");
    public static final UITexture PROGRESSBAR_ARROW_BBF = fullImageColorableGT("progressbar/arrow_bbf");
    public static final UITexture PROGRESSBAR_BOILER_HEAT = UITexture.builder()
        .location(GregTech.ID, "gui/progressbar/boiler_heat")
        .fullImage()
        .name(GTTextureIds.PROGRESSBAR_BOILER_HEAT)
        .build();
    public static final UITexture PROGRESSBAR_FUEL_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/progressbar/fuel")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PROGRESSBAR_FUEL_STANDARD)
        .build();
    private static final SteamTextureRegisterer PROGRESSBAR_FUEL_STEAM = SteamTextureRegisterer.builder()
        .location("gui/progressbar/fuel_%s")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PROGRESSBAR_FUEL_BRONZE, GTTextureIds.PROGRESSBAR_FUEL_STEEL, null)
        .build();
    public static final UITexture PROGRESSBAR_SIEVERT = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/progressbar/sievert")
        .fullImage()
        .name(GTTextureIds.PROGRESSBAR_SIEVERT)
        .build();

    // endregion progressbar

    // region button

    public static final UITexture BUTTON_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/button/standard")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_STANDARD)
        .build();
    private static final SteamTextureRegisterer BUTTON_STEAM = SteamTextureRegisterer.builder()
        .location("gui/button/%s")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_BRONZE, GTTextureIds.BUTTON_STEEL, GTTextureIds.BUTTON_PRIMITIVE)
        .build();
    public static final UITexture BUTTON_STANDARD_PRESSED = UITexture.builder()
        .location(GregTech.ID, "gui/button/standard_pressed")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_STANDARD_PRESSED)
        .build();
    private static final SteamTextureRegisterer BUTTON_STEAM_PRESSED = SteamTextureRegisterer.builder()
        .location("gui/button/%s_pressed")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(
            GTTextureIds.BUTTON_BRONZE_PRESSED,
            GTTextureIds.BUTTON_STEEL_PRESSED,
            GTTextureIds.BUTTON_PRIMITIVE_PRESSED)
        .build();
    public static final UITexture BUTTON_COVER_TAB_NORMAL_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/tab/cover_normal")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_COVER_TAB_NORMAL_STANDARD)
        .build();
    public static final UITexture BUTTON_COVER_TAB_HIGHLIGHT_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/tab/cover_highlight")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STANDARD)
        .build();
    public static final UITexture BUTTON_COVER_TAB_DISABLED_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/tab/cover_disabled")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_COVER_TAB_DISABLED_STANDARD)
        .build();
    private static final SteamTextureRegisterer BUTTON_COVER_TAB_NORMAL_STEAM = SteamTextureRegisterer.builder()
        .location("gui/tab/cover_%s_normal")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_BRONZE,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_STEEL,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_PRIMITIVE)
        .build();
    private static final SteamTextureRegisterer BUTTON_COVER_TAB_HIGHLIGHT_STEAM = SteamTextureRegisterer.builder()
        .location("gui/tab/cover_%s_highlight")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_BRONZE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STEEL,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_PRIMITIVE)
        .build();
    private static final SteamTextureRegisterer BUTTON_COVER_TAB_DISABLED_STEAM = SteamTextureRegisterer.builder()
        .location("gui/tab/cover_%s_disabled")
        .fullImage()
        .canApplyTheme()
        .name(
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_BRONZE,
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_STEEL,
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_PRIMITIVE)
        .build();

    // endregion button

    // region button overlay

    public static final UITexture OVERLAY_BUTTON_ALLOW_INPUT = fullImageGT("overlay_button/allow_input");
    public static final UITexture OVERLAY_BUTTON_ALLOW_OUTPUT = fullImageGT("overlay_button/allow_output");
    public static final UITexture OVERLAY_BUTTON_BLACKLIST = fullImageGT("overlay_button/blacklist");
    public static final UITexture OVERLAY_BUTTON_BLOCK_INPUT = fullImageGT("overlay_button/block_input");
    public static final UITexture OVERLAY_BUTTON_BLOCK_OUTPUT = fullImageGT("overlay_button/block_output");
    public static final UITexture OVERLAY_BUTTON_CHECKMARK = fullImageGT("overlay_button/checkmark");
    public static final UITexture OVERLAY_BUTTON_CROSS = fullImageGT("overlay_button/cross");
    public static final UITexture OVERLAY_BUTTON_CYCLIC = fullImageGT("overlay_button/cyclic");
    public static final UITexture OVERLAY_BUTTON_EXPORT = fullImageGT("overlay_button/export");
    public static final UITexture OVERLAY_BUTTON_HOURGLASS = fullImageGT("overlay_button/hourglass");
    public static final UITexture OVERLAY_BUTTON_IMPORT = fullImageGT("overlay_button/import");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_ON = fullImageGT("overlay_button/power_switch_on");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_OFF = fullImageGT("overlay_button/power_switch_off");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_DISABLED = fullImageGT(
        "overlay_button/power_switch_disabled");
    public static final UITexture OVERLAY_BUTTON_PROGRESS = fullImageGT("overlay_button/progress");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_OFF = fullImageGT("overlay_button/redstone_off");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_ON = fullImageGT("overlay_button/redstone_on");
    public static final UITexture OVERLAY_BUTTON_SCREWDRIVER = fullImageGT("overlay_button/screwdriver");
    public static final UITexture OVERLAY_BUTTON_USE_PROCESSING_STATE = fullImageGT(
        "overlay_button/use_processing_state");
    public static final UITexture OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE = fullImageGT(
        "overlay_button/use_inverted_processing_state");
    public static final UITexture OVERLAY_BUTTON_WHITELIST = fullImageGT("overlay_button/whitelist");

    // endregion button overlay

    // region picture

    public static final UITexture PICTURE_GT_LOGO_STANDARD = UITexture.builder()
        .location(GregTech.ID, "gui/picture/gt_logo_standard")
        .fullImage()
        .name(GTTextureIds.PICTURE_GT_LOGO_STANDARD)
        .build();
    public static final UITexture PICTURE_GT_LOGO_GRAY = UITexture.builder()
        .location(GregTech.ID, "gui/picture/gt_logo_gray")
        .fullImage()
        .name(GTTextureIds.PICTURE_GT_LOGO_GRAY)
        .build();
    private static final SteamTextureRegisterer PICTURE_GT_LOGO_STEAM = SteamTextureRegisterer.builder()
        .location("gui/picture/gt_logo_%s")
        .fullImage()
        .name(
            GTTextureIds.PICTURE_GT_LOGO_BRONZE,
            GTTextureIds.PICTURE_GT_LOGO_STEEL,
            GTTextureIds.PICTURE_GT_LOGO_PRIMITIVE)
        .build();
    public static final UITexture PICTURE_BARTWORKS_LOGO_STANDARD = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/bw_logo_47x21")
        .fullImage()
        .name(GTTextureIds.PICTURE_BW_LOGO_STANDARD)
        .build();

    public static final UITexture PICTURE_TRANSPARENT = UITexture.builder()
        .location(GregTech.ID, "gui/picture/transparent")
        .fullImage()
        .name(GTTextureIds.PICTURE_TRANSPARENT)
        .build();
    public static final UITexture PICTURE_SIEVERT_CONTAINER = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/sievert_container")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_SIEVERT_CONTAINER)
        .build();
    public static final UITexture PICTURE_DECAY_TIME_CONTAINER = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/decay_time_container")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_DECAY_TIME_CONTAINER)
        .build();
    public static final UITexture PICTURE_DECAY_TIME_INSIDE = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/decay_time_inside")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_DECAY_TIME_INSIDE)
        .build();
    public static final UITexture PICTURE_RADIATION_SHUTTER_FRAME = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/radiation_shutter_frame")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_RADIATION_SHUTTER_FRAME)
        .build();
    public static final UITexture PICTURE_RADIATION_SHUTTER_INSIDE = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/radiation_shutter_inside")
        .adaptable(1, 1, 1, 1)
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_RADIATION_SHUTTER_INSIDE)
        .build();

    // endregion picture

    // region machine modes

    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_DEFAULT = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_default");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_CHEMBATH = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_chembath");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_WASHPLANT = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_washplant");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_simplewasher");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_PACKAGER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_packager");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_unpackager");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_SEPARATOR = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_separator");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_POLARIZER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_polarizer");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_lpf_fluid");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_LPF_METAL = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_lpf_metal");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_BENDING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_bending");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_FORMING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_forming");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_SLICING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_slicing");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_CUTTING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_cutting");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_COMPRESSING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_compressing");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_SINGULARITY = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_singularity");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_STEAM = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_dehp_steam");

    // endregion machine modes
    /**
     * Creates texture of full image. Theme of GUI does not affect how the texture is drawn.
     *
     * @param path Path to the image, excluding gregtech/textures/gui
     * @return Texture of full image
     */
    private static UITexture fullImageGT(String path) {
        return fullImage(GregTech.ID, "gui/" + path);
    }

    /**
     * Creates texture of full image. Theme color gets applied to the texture.
     *
     * @param path Path to the image, excluding gregtech/textures/gui
     * @return Texture of full image
     */
    private static UITexture fullImageColorableGT(String path) {
        return fullImage(GregTech.ID, "gui/" + path, true);
    }
}
