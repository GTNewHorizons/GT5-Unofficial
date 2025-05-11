package gregtech.api.modularui2;

import static com.cleanroommc.modularui.drawable.UITexture.fullImage;
import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static tectech.Reference.MODID;

import java.util.stream.IntStream;

import com.cleanroommc.modularui.drawable.ColorType;
import com.cleanroommc.modularui.drawable.UITexture;

import bartworks.MainMod;
import gregtech.common.modularui2.util.SteamTextureRegisterer;
import kekztech.KekzCore;

/**
 * Holds all the references to GUI textures used within GregTech.
 */
public final class GTGuiTextures {

    public static void init() {}

    public static final UITexture TRANSPARENT = UITexture.fullImage(GregTech.ID, "gui/picture/transparent");

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
    public static final UITexture BACKGROUND_FOUNDRY = UITexture.builder()
        .location(GregTech.ID, "gui/background/foundry_default")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_FOUNDRY)
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
    public static final UITexture BACKGROUND_TITLE_FOUNDRY = UITexture.builder()
        .location(GregTech.ID, "gui/tab/title_foundry")
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_TITLE_FOUNDRY)
        .build();

    public static final UITexture BACKGROUND_TEXT_FIELD_LIGHT_GRAY = UITexture.builder()
        .location(GregTech.ID, "gui/background/text_field_light_gray")
        .adaptable(1)
        .name(GTTextureIds.BACKGROUND_TEXT_FIELD_LIGHT_GRAY)
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

    public static final UITexture TT_BACKGROUND_TEXT_FIELD = UITexture.builder()
        .location(MODID, "gui/background/screen_blue")
        .imageSize(90, 72)
        .adaptable(2)
        .name(GTTextureIds.BACKGROUND_TERMINAL_TECTECH)
        .build();

    public static final UITexture FOUNDRY_BACKGROUND_TEXT_FIELD = UITexture.builder()
        .location(GregTech.ID, "gui/background/popup_foundry")
        .imageSize(195, 136)
        .adaptable(4)
        .name(GTTextureIds.BACKGROUND_POPUP_FOUNDRY)
        .build();

    public static final UITexture FOUNDRY_BACKGROUND_CONTRIBUTORS = UITexture.builder()
        .location(GregTech.ID, "gui/background/contributors_foundry")
        .imageSize(298, 298)
        .name(GTTextureIds.BACKGROUND_CONTRIBUTORS_FOUNDRY)
        .adaptable(1)
        .build();

    public static final UITexture BACKGROUND_REDSTONE_SNIFFER = UITexture.builder()
        .location(GregTech.ID, "gui/background/redstone_sniffer")
        .imageSize(195, 136)
        .adaptable(1)
        .name(GTTextureIds.BACKGROUND_REDSTONE_SNIFFER)
        .build();

    public static final UITexture BACKGROUND_CHAOS_LOCATOR = UITexture.builder()
        .location(GregTech.ID, "gui/background/chaos_locator")
        .imageSize(176, 166)
        .adaptable(1)
        .name(GTTextureIds.BACKGROUND_CHAOS_LOCATOR)
        .build();

    public static final UITexture BACKGROUND_TESLA_TOWER_CHART = UITexture.builder()
        .location(MODID, "gui/tesla_tower_chart_background")
        .nonOpaque()
        .build();
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
    public static final UITexture BACKGROUND_GRAY_BORDER = UITexture.builder()
        .adaptable(1)
        .canApplyTheme()
        .location(GregTech.ID, "gui/picture/gray_rectangle")
        .build();
    // endregion background

    // region overlay
    public static final UITexture OVERLAY_GREGTECH_LOGO = fullImageGT("picture/gt_logo_standard");

    public static final UITexture OVERLAY_NEEDS_CROWBAR = UITexture.fullImage(GregTech.ID, "gui/icons/needsCrowbar");
    public static final UITexture OVERLAY_NEEDS_HARDHAMMER = UITexture
        .fullImage(GregTech.ID, "gui/icons/needsHardhammer");
    public static final UITexture OVERLAY_NEEDS_SCREWDRIVER = UITexture
        .fullImage(GregTech.ID, "gui/icons/needsScrewdriver");
    public static final UITexture OVERLAY_NEEDS_SOFTHAMMER = UITexture
        .fullImage(GregTech.ID, "gui/icons/needsSofthammer");
    public static final UITexture OVERLAY_NEEDS_SOLDERING = UITexture
        .fullImage(GregTech.ID, "gui/icons/needsSoldering");
    public static final UITexture OVERLAY_NEEDS_WRENCH = UITexture.fullImage(GregTech.ID, "gui/icons/needsWrench");

    public static final UITexture OVERLAY_TOO_DAMAGED = UITexture.fullImage(GregTech.ID, "gui/icons/needsWrench");
    public static final UITexture OVERLAY_POWER_LOSS = UITexture
        .fullImage(GregTech.ID, "gui/picture/stalled_electricity");
    public static final UITexture OVERLAY_STRUCTURE_INCOMPLETE = UITexture
        .fullImage(GregTech.ID, "gui/icons/structureIncomplete");
    public static final UITexture OVERLAY_MANUAL_SHUTDOWN = UITexture
        .fullImage(GregTech.ID, "gui/icons/manualShutdown");
    public static final UITexture OVERLAY_COMPUTATION_LOSS = UITexture
        .fullImage(GregTech.ID, "gui/icons/stalled_computation");
    public static final UITexture OVERLAY_UNPOWERED = UITexture.fullImage(GregTech.ID, "gui/icons/unpowered");

    public static final UITexture OVERLAY_NO_MAINTENANCE_ISSUES = UITexture
        .fullImage(GregTech.ID, "gui/icons/maintenance_none");
    public static final UITexture OVERLAY_SOME_MAINTENANCE_ISSUES = UITexture
        .fullImage(GregTech.ID, "gui/icons/maintenance_some");
    public static final UITexture OVERLAY_ALL_MAINTENANCE_ISSUES = UITexture
        .fullImage(GregTech.ID, "gui/icons/maintenance_all");

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

    public static final UITexture SLOT_ITEM_FOUNDRY = UITexture.builder()
        .location(GregTech.ID, "gui/slot/item_foundry")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_ITEM_FOUNDRY)
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

    public static final UITexture SLOT_EMPTY = UITexture.builder()
        .location(GregTech.ID, "gui/slot/empty")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.SLOT_EMPTY)
        .build();

    public static final UITexture SLOT_OUTLINE_GREEN = UITexture.fullImage(MODID, "gui/picture/green_selector");
    public static final UITexture UNSELECTED_OPTION = UITexture.fullImage(MODID, "gui/picture/unselected_option");

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
    public static final UITexture OVERLAY_SLOT_DATA_ORB = fullImage(GregTech.ID, "gui/overlay_slot/data_orb");
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
    public static final UITexture OVERLAY_SLOT_CIRCUIT = fullImage(GregTech.ID, "gui/overlay_slot/circuit");
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
    public static final UITexture OVERLAY_SLOT_EXTRUDER_SHAPE = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_slot/extruder_shape")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.SLOT_EXTRUDER_SHAPE)
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

    public static final UITexture OVERLAY_SLOT_ARROW_4 = UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_slot/arrow_4");

    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_NONE = UITexture.builder()
        .canApplyTheme()
        .location(GregTech.ID, "gui/overlay_button/void_excess_none")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ITEM = UITexture.builder()
        .canApplyTheme()
        .location(GregTech.ID, "gui/overlay_button/void_excess_item")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_FLUID = UITexture.builder()
        .canApplyTheme()
        .location(GregTech.ID, "gui/overlay_button/void_excess_fluid")
        .build();
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ALL = UITexture.builder()
        .canApplyTheme()
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
    public static final UITexture OVERLAY_BUTTON_MUFFLE_ON = fullImage(GregTech.ID, "gui/overlay_button/muffle_on");
    public static final UITexture OVERLAY_BUTTON_MUFFLE_OFF = fullImage(GregTech.ID, "gui/overlay_button/muffle_off");

    public static final UITexture OVERLAY_EXPORT = fullImage(GregTech.ID, "gui/overlay_button/export");
    public static final UITexture OVERLAY_IMPORT = fullImage(GregTech.ID, "gui/overlay_button/import");
    public static final UITexture OVERLAY_BUTTON_BOUNDING_BOX = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_button/bounding_box")
        .fullImage()
        .canApplyTheme()
        .build();

    public static final UITexture[] OVERLAY_BUTTON_THROUGHPUT = IntStream.range(0, 4) // MTEElectricAutoWorkbench#MAX_THROUGHPUT
        .mapToObj(i -> UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/throughput_" + i))
        .toArray(UITexture[]::new);

    public static final UITexture[] OVERLAY_BUTTON_MODE = IntStream.range(0, 10) // MTEElectricAutoWorkbench#MAX_MODES
        .mapToObj(i -> UITexture.fullImage(GTPlusPlus.ID, "gui/overlay_button/mode_" + i))
        .toArray(UITexture[]::new);

    public static final UITexture TT_OVERLAY_SLOT_MESH = UITexture.builder()
        .location(MODID, "gui/overlay_slot/mesh")
        .canApplyTheme()
        .build();

    public static final UITexture OVERLAY_BUTTON_TESLA_TOWER_CHART = UITexture.builder()
        .location(MODID, "gui/overlay_button/tesla_tower_chart")
        .build();
    public static final UITexture OVERLAY_BUTTON_TESLA_TOWER_HEAT_MAP = UITexture.builder()
        .location(MODID, "gui/overlay_button/tesla_tower_minimap")
        .build();
    public static final UITexture TT_SAFE_VOID_OFF = UITexture.builder()
        .location(MODID, "gui/overlay_button/safe_void_off")
        .canApplyTheme()
        .build();

    public static final UITexture TT_SAFE_VOID_ON = UITexture.builder()
        .location(MODID, "gui/overlay_button/safe_void_on")
        .canApplyTheme()
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
    public static final UITexture OVERLAY_SLOT_CHARGER = UITexture.builder()
        .canApplyTheme()
        .fullImage()
        .location(GregTech.ID, "gui/overlay_slot/charger")
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

    public static final UITexture STEAM_GAUGE_BG = UITexture.fullImage(GregTech.ID, "gui/background/steam_dial");
    public static final UITexture STEAM_GAUGE_BG_STEEL = UITexture
        .fullImage(GregTech.ID, "gui/background/steam_dial_steel");

    public static final UITexture PROGRESSBAR_TESLA_TOWER_CURRENT = UITexture
        .fullImage(MODID, "gui/tesla_tower_current");

    public static final UITexture PROGRESSBAR_METER_MINT = UITexture.builder()
        .location(GregTech.ID, "gui/progressbar/meter_mint")
        .fullImage()
        .name(GTTextureIds.PROGRESSBAR_METER_MINT)
        .build();
    public static final UITexture PROGRESSBAR_METER_ORANGE = UITexture.builder()
        .location(GregTech.ID, "gui/progressbar/meter_orange")
        .fullImage()
        .name(GTTextureIds.PROGRESSBAR_METER_ORANGE)
        .build();
    public static final UITexture PROGRESSBAR_METER_ROSE = UITexture.builder()
        .location(GregTech.ID, "gui/progressbar/meter_rose")
        .fullImage()
        .name(GTTextureIds.PROGRESSBAR_METER_ROSE)
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
    public static final UITexture BUTTON_STANDARD_DISABLED = UITexture.builder()
        .location(GregTech.ID, "gui/button/standard_disabled")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_STANDARD_DISABLED)
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
    public static final UITexture BUTTON_FOUNDRY = UITexture.builder()
        .location(GregTech.ID, "gui/button/foundry")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_FOUNDRY)
        .build();
    public static final UITexture BUTTON_FOUNDRY_PRESSED = UITexture.builder()
        .location(GregTech.ID, "gui/button/foundry_pressed")
        .imageSize(18, 18)
        .adaptable(1)
        .canApplyTheme()
        .name(GTTextureIds.BUTTON_FOUNDRY_PRESSED)
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

    public static final UITexture BUTTON_SPACE_32x16 = UITexture.fullImage(MODID, "gui/button/purple");
    public static final UITexture BUTTON_SPACE_PRESSED_32x16 = UITexture.fullImage(MODID, "gui/button/purple_pressed");
    public static final UITexture BUTTON_BOXED_CHECKMARK_18x18 = UITexture
        .fullImage(MODID, "gui/button/boxed_checkmark");
    public static final UITexture BUTTON_BOXED_EXCLAMATION_POINT_18x18 = UITexture
        .fullImage(MODID, "gui/button/boxed_exclamation_point");

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
    public static final UITexture OVERLAY_BUTTON_GATE_AND = fullImageGT("overlay_button/gate_and");
    public static final UITexture OVERLAY_BUTTON_GATE_NAND = fullImageGT("overlay_button/gate_nand");
    public static final UITexture OVERLAY_BUTTON_GATE_OR = fullImageGT("overlay_button/gate_or");
    public static final UITexture OVERLAY_BUTTON_GATE_NOR = fullImageGT("overlay_button/gate_nor");
    public static final UITexture OVERLAY_BUTTON_ANALOG = fullImageGT("overlay_button/analog");

    public static final UITexture OVERLAY_BUTTON_WIRELESS_ON = fullImage(
        KekzCore.MODID,
        "gui/overlay_button/wireless_on");
    public static final UITexture OVERLAY_BUTTON_WIRELESS_OFF = fullImage(
        KekzCore.MODID,
        "gui/overlay_button/wireless_off");
    public static final UITexture OVERLAY_BUTTON_WIRELESS_DISABLED = fullImage(
        KekzCore.MODID,
        "gui/overlay_button/wireless_off_disabled");
    public static final UITexture OVERLAY_BUTTON_WIRELESS_REBALANCE = fullImage(
        KekzCore.MODID,
        "gui/overlay_button/wireless_rebalance");
    public static final UITexture TT_OVERLAY_BUTTON_POWER_SWITCH_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_on");
    public static final UITexture TT_OVERLAY_BUTTON_POWER_SWITCH_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_off");
    public static final UITexture TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED = UITexture
        .fullImage(MODID, "gui/overlay_button/power_switch_disabled");
    public static final UITexture TT_OVERLAY_BUTTON_ARROW_BLUE_UP = UITexture
        .fullImage(MODID, "gui/overlay_button/arrow_blue_up");
    public static final UITexture TT_OVERLAY_BUTTON_BATTERY_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/battery_on");
    public static final UITexture TT_OVERLAY_BUTTON_BATTERY_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/battery_off");
    public static final UITexture TT_OVERLAY_BUTTON_FLAG = UITexture.fullImage(MODID, "gui/overlay_button/flag");
    public static final UITexture TT_OVERLAY_BUTTON_HEART = UITexture.fullImage(MODID, "gui/overlay_button/heart");
    public static final UITexture TT_OVERLAY_BUTTON_RAINBOW_SPIRAL = UITexture
        .fullImage(MODID, "gui/overlay_button/rainbow_spiral");
    public static final UITexture TT_OVERLAY_BUTTON_STATISTICS = UITexture
        .fullImage(MODID, "gui/overlay_button/statistics");
    public static final UITexture TT_OVERLAY_BUTTON_HEAT_ON = UITexture.fullImage(MODID, "gui/overlay_button/heat_on");
    public static final UITexture TT_OVERLAY_BUTTON_INPUT_SEPARATION = UITexture
        .fullImage(MODID, "gui/overlay_button/input_separation_on");
    public static final UITexture TT_OVERLAY_BUTTON_INPUT_SEPARATION_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/input_separation_off");
    public static final UITexture TT_OVERLAY_BUTTON_BATCH_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/batch_mode_on");
    public static final UITexture TT_OVERLAY_BUTTON_BATCH_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/batch_mode_off");
    public static final UITexture TT_OVERLAY_BUTTON_LOAF_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/loaf_mode_on");
    public static final UITexture TT_OVERLAY_BUTTON_LOAF_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/loaf_mode_off");
    public static final UITexture TT_OVERLAY_BUTTON_RECIPE_LOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/recipe_locked");
    public static final UITexture TT_OVERLAY_BUTTON_RECIPE_UNLOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/recipe_unlocked");
    public static final UITexture TT_OVERLAY_BUTTON_VOIDING_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_disabled");
    public static final UITexture TT_OVERLAY_BUTTON_VOIDING_ITEMS = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_items");
    public static final UITexture TT_OVERLAY_BUTTON_VOIDING_FLUIDS = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_fluids");
    public static final UITexture TT_OVERLAY_BUTTON_VOIDING_BOTH = UITexture
        .fullImage(MODID, "gui/overlay_button/voiding_both");
    public static final UITexture TT_OVERLAY_BUTTON_STRUCTURE_CHECK = UITexture
        .fullImage(MODID, "gui/overlay_button/structure_check_on");
    public static final UITexture TT_OVERLAY_BUTTON_STRUCTURE_CHECK_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/structure_check_off");
    public static final UITexture TT_OVERLAY_BUTTON_FURNACE_MODE = UITexture
        .fullImage(MODID, "gui/overlay_button/furnace_mode_on");
    public static final UITexture TT_OVERLAY_BUTTON_FURNACE_MODE_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/furnace_mode_off");
    public static final UITexture TT_OVERLAY_BUTTON_POWER_PANEL = UITexture
        .fullImage(MODID, "gui/overlay_button/power_panel");
    public static final UITexture TT_OVERLAY_CYCLIC_BLUE = UITexture.fullImage(MODID, "gui/overlay_button/cyclic_blue");
    public static final UITexture TT_OVERLAY_EJECTION_LOCKED = UITexture
        .fullImage(MODID, "gui/overlay_button/eject_disabled");
    public static final UITexture TT_OVERLAY_EJECTION_ON = UITexture.fullImage(MODID, "gui/overlay_button/eject");

    public static final UITexture TT_BUTTON_CELESTIAL_32x32 = UITexture.fullImage(MODID, "gui/button/celestial");

    public static final UITexture OVERLAY_BUTTON_POWER_PASS_ON = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_on");
    public static final UITexture OVERLAY_BUTTON_POWER_PASS_OFF = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_off");
    public static final UITexture OVERLAY_BUTTON_POWER_PASS_DISABLED = UITexture
        .fullImage(MODID, "gui/overlay_button/power_pass_disabled");
    public static final UITexture OVERLAY_BUTTON_EDIT_PARAMETERS_ENABLED = UITexture
        .fullImage(GTNHIntergalactic.ID, "gui/overlay_button/options");
    public static final UITexture OVERLAY_BUTTON_EDIT_PARAMETERS_DISABLED = UITexture
        .fullImage(GTNHIntergalactic.ID, "gui/overlay_button/options_disabled");
    public static final UITexture OVERLAY_BUTTON_ASSEMBLER_MODE = UITexture
        .fullImage("goodgenerator", "gui/overlay_button/assembler_mode");
    public static final UITexture OVERLAY_BUTTON_PRECISE_MODE = UITexture
        .fullImage("goodgenerator", "gui/overlay_button/precise_mode");
    public static final UITexture OVERLAY_BUTTON_SORTING_MODE = UITexture
        .fullImage(GregTech.ID, "gui/overlay_button/sorting_mode");
    public static final UITexture OVERLAY_BUTTON_ONE_STACK_LIMIT = UITexture
        .fullImage(GregTech.ID, "gui/overlay_button/one_stack_limit");
    public static final UITexture OVERLAY_BUTTON_LMA_ANIMATION_ON = UITexture
        .fullImage(GregTech.ID, "gui/overlay_button/lma_animation_on");
    public static final UITexture OVERLAY_BUTTON_LMA_ANIMATION_OFF = UITexture
        .fullImage(GregTech.ID, "gui/overlay_button/lma_animation_off");
    public static final UITexture FOUNDRY_CALCULATOR = UITexture.builder()
        .location(GregTech.ID, "gui/overlay_button/foundry_calculator")
        .fullImage()
        .canApplyTheme()
        .build();

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

    public static final UITexture PICTURE_TECTECH_LOGO_DARK = UITexture.builder()
        .location(MODID, "gui/picture/tectech_logo_dark")
        .fullImage()
        .name(GTTextureIds.PICTURE_TECTECH_LOGO_DARK)
        .build();
    public static final UITexture PICTURE_BARTWORKS_LOGO_STANDARD = UITexture.builder()
        .location(MainMod.MOD_ID, "GUI/picture/bw_logo_47x21")
        .fullImage()
        .name(GTTextureIds.PICTURE_BW_LOGO_STANDARD)
        .build();
    public static final UITexture PICTURE_EXOFOUNDRY_LOGO = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_logo")
        .fullImage()
        .name(GTTextureIds.PICTURE_LOGO_EXOFOUNDRY)
        .build();

    public static final UITexture PICTURE_BRAIN = UITexture.builder()
        .location(GregTech.ID, "gui/picture/brain")
        .fullImage()
        .name(GTTextureIds.PICTURE_BRAIN)
        .build();
    public static final UITexture PICTURE_ELECRICITY = UITexture.builder()
        .location(GregTech.ID, "gui/picture/electricity")
        .fullImage()
        .name(GTTextureIds.PICTURE_ELECRICITY)
        .build();

    public static final UITexture PICTURE_TRANSPARENT = UITexture.builder()
        .location(GregTech.ID, "gui/picture/transparent")
        .fullImage()
        .name(GTTextureIds.PICTURE_TRANSPARENT)
        .build();
    public static final UITexture INFORMATION_SYMBOL = UITexture.builder()
        .location(GregTech.ID, "gui/picture/information_symbol")
        .fullImage()
        .name(GTTextureIds.PICTURE_INFORMATION_SYMBOL)
        .build();
    // ripped from mui1
    public static final UITexture INFORMATION_BUBBLE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/information_bubble")
        .fullImage()
        .name(GTTextureIds.PICTURE_INFORMATION_BUBBLE)
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
    public static final UITexture EXOFOUNDRY_BASE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_base")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_BASE)
        .build();
    public static final UITexture EXOFOUNDRY_UNSET = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_unset")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_UNSET)
        .build();
    public static final UITexture EXOFOUNDRY_HR = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_heliocast_reinforcement")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_HR)
        .build();
    public static final UITexture EXOFOUNDRY_HC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_hypercooler")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_HC)
        .build();
    public static final UITexture EXOFOUNDRY_TDS = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_tds")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_TDS)
        .build();
    public static final UITexture EXOFOUNDRY_EFF_OC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_efficient_oc")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_EOC)
        .build();
    public static final UITexture EXOFOUNDRY_ECB = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_extra_casting_basins")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_ECB)
        .build();
    public static final UITexture EXOFOUNDRY_SLC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_streamlined_casters")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_SLC)
        .build();
    public static final UITexture EXOFOUNDRY_PES = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_power_efficient_subsystems")
        .fullImage()
        .canApplyTheme()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PES)
        .build();

    public static final UITexture EXOFOUNDRY_PAIR_PES_EOC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_pes_eoc_pair")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_PES_EOC)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_ECB_SLC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_ecb_slc_pair")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_ECB_SLC)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_UC_HC = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_uc_hc_pair")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_UC_HC)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_HR_SELF = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_hr_self_pair")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_HR_SELF)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_PES_EOC_ACTIVE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_pes_eoc_pair_active")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_PES_EOC_ACTIVE)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_ECB_SLC_ACTIVE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_ecb_slc_pair_active")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_ECB_SLC_ACTIVE)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_UC_HC_ACTIVE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_uc_hc_pair_active")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_UC_HC_ACTIVE)
        .build();
    public static final UITexture EXOFOUNDRY_PAIR_HR_SELF_ACTIVE = UITexture.builder()
        .location(GregTech.ID, "gui/picture/exofoundry_hr_self_pair_active")
        .fullImage()
        .canApplyTheme()
        .nonOpaque()
        .name(GTTextureIds.PICTURE_EXOFOUNDRY_PAIR_HR_SELF_ACTIVE)
        .build();

    public static final UITexture PICTURE_GODFORGE_MILESTONE_CHARGE = UITexture
        .fullImage(MODID, "gui/picture/milestone_charge");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CONVERSION = UITexture
        .fullImage(MODID, "gui/picture/milestone_conversion");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CATALYST = UITexture
        .fullImage(MODID, "gui/picture/milestone_catalyst");
    public static final UITexture PICTURE_GODFORGE_MILESTONE_COMPOSITION = UITexture
        .fullImage(MODID, "gui/picture/milestone_composition");
    public static final UITexture PICTURE_GODFORGE_THANKS = UITexture.builder()
        .location(MODID, "gui/picture/thanks")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW = UITexture.builder()
        .location(MODID, "gui/picture/milestone_charge_glow")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CONVERSION_GLOW = UITexture.builder()
        .location(MODID, "gui/picture/milestone_conversion_glow")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_GODFORGE_MILESTONE_CATALYST_GLOW = UITexture.builder()
        .location(MODID, "gui/picture/milestone_catalyst_glow")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_GODFORGE_MILESTONE_COMPOSITION_GLOW = UITexture.builder()
        .location(MODID, "gui/picture/milestone_composition_glow")
        .nonOpaque()
        .build();

    public static final UITexture PICTURE_INFO = fullImage(GregTech.ID, "gui/picture/information_round");

    public static final UITexture PICTURE_OVERLAY_BLUE = UITexture.builder()
        .location(MODID, "gui/picture/overlay_blue")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_OVERLAY_ORANGE = UITexture.builder()
        .location(MODID, "gui/picture/overlay_orange")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_OVERLAY_GREEN = UITexture.builder()
        .location(MODID, "gui/picture/overlay_green")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_OVERLAY_PURPLE = UITexture.builder()
        .location(MODID, "gui/picture/overlay_purple")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_OVERLAY_RED = UITexture.builder()
        .location(MODID, "gui/picture/overlay_red")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_RAINBOW_SQUARE = UITexture.fullImage(MODID, "gui/picture/rainbow_square");
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_PURPLE = UITexture.builder()
        .location(MODID, "gui/picture/connector_purple")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_GREEN = UITexture.builder()
        .location(MODID, "gui/picture/connector_green")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_ORANGE = UITexture.builder()
        .location(MODID, "gui/picture/connector_orange")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_BLUE = UITexture.builder()
        .location(MODID, "gui/picture/connector_blue")
        .nonOpaque()
        .build();
    public static final UITexture PICTURE_UPGRADE_CONNECTOR_RED = UITexture.builder()
        .location(MODID, "gui/picture/connector_red")
        .nonOpaque()
        .build();
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
    public static final UITexture PICTURE_HEAT_SINK_16x8 = UITexture.fullImage(MODID, "gui/picture/heat_sink_16x8");
    public static final UITexture GREEN_CHECKMARK_11x9 = UITexture.fullImage(MODID, "gui/picture/green_checkmark");
    public static final UITexture BUTTON_OUTLINE_HOLLOW = UITexture.builder()
        .location(MODID, "gui/button/transparent_16x16")
        .imageSize(16, 16)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_OUTLINE_HOLLOW_PRESSED = UITexture.builder()
        .location(MODID, "gui/button/transparent_pressed_16x16")
        .imageSize(16, 16)
        .adaptable(1)
        .build();
    public static final UITexture CLOSE_BUTTON_HOLLOW = UITexture.fullImage(MODID, "gui/button/transparent_x_10x10");
    public static final UITexture GODFORGE_SOUND_ON = UITexture.fullImage(MODID, "gui/overlay_button/sound_on");
    public static final UITexture GODFORGE_SOUND_OFF = UITexture.fullImage(MODID, "gui/overlay_button/sound_off");

    public static final UITexture TT_CONTROLLER_SLOT_HEAT_SINK = UITexture.builder()
        .location(MODID, "gui/picture/heat_sink_small")
        .canApplyTheme()
        .build();

    public static final UITexture PICTURE_SLOTS_HOLO_3BY3 = UITexture
        .fullImage(GregTech.ID, "gui/picture/slots_holo_3by3");
    public static final UITexture PICTURE_WORKBENCH_CIRCLE = UITexture.builder()
        .location(GTPlusPlus.ID, "gui/picture/workbench_circle")
        .imageSize(16, 16)
        .canApplyTheme()
        .build();
    public static final UITexture PICTURE_ARROW_WHITE_DOWN = UITexture.builder()
        .location(GTPlusPlus.ID, "gui/picture/arrow_white_down")
        .imageSize(10, 16)
        .canApplyTheme()
        .build();
    public static final UITexture PICTURE_PLUS_RED = UITexture.fullImage(GregTech.ID, "gui/picture/plus_red");
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
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_MASS_FABRICATING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_mass_fabrication");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_DISTILLING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_distilling");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_DISTILLATION_TOWER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_tower");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_SCANNER = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_scanner");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_ARC = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_arc");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_PLASMA_ARC = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_plasma_arc");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_RESEARCH = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_research");
    public static final UITexture OVERLAY_BUTTON_MACHINEMODE_RECYCLING = fullImage(
        GregTech.ID,
        "gui/overlay_button/machine_mode_recycling");
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

    public static final UITexture OVERLAY_COKE_OVEN_FLUID_SLOT_GAUGE = fullImageColorableGT(
        "overlay_slot/overlay_coke_oven_fluid_slot_gauge");
    public static final UITexture BACKGROUND_COKE_OVEN_FLUID_SLOT = fullImageColorableGT(
        "overlay_slot/background_coke_oven_fluid_slot.png");
    public static final UITexture BACKGROUND_COKE_OVEN_COAL = fullImageColorableGT("overlay_slot/coke_oven_coal.png");
    public static final UITexture BACKGROUND_COKE_OVEN = UITexture.builder()
        .location(GregTech.ID, "gui/background/coke_oven")
        .imageSize(176, 166)
        .adaptable(4)
        .canApplyTheme()
        .name(GTTextureIds.BACKGROUND_COKE_OVEN)
        .build();

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
        return fullImage(GregTech.ID, "gui/" + path, ColorType.DEFAULT);
    }
}
