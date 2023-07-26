package gregtech.api.gui.modularui;

import static gregtech.api.enums.Mods.GregTech;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.drawable.UITexture;

public class GT_UITextures {

    public static final Function<String, ResourceLocation> GUI_PATH = location -> GregTech
        .getResourceLocation("gui/" + location);

    /**
     * Simple texture that accepts application of themes.
     */
    public static UITexture simpleColorable(String location) {
        return UITexture.fullImage(GUI_PATH.apply(location), true);
    }

    /**
     * Simple texture that doesn't accept application of themes.
     */
    public static UITexture simple(String location) {
        return UITexture.fullImage(GUI_PATH.apply(location), false);
    }

    public static final UITexture TRANSPARENT = simple("picture/transparent");

    public static final UITexture BACKGROUND_SINGLEBLOCK_DEFAULT = UITexture.builder()
        .location(GUI_PATH.apply("background/singleblock_default"))
        .imageSize(176, 166)
        .adaptable(4)
        .build();
    public static final SteamTexture BACKGROUND_STEAM = SteamTexture.adaptableTexture("background/%s", 176, 166, 4);
    public static final UITexture BACKGROUND_FUSION_COMPUTER = simple("background/fusion_computer");
    public static final UITexture BACKGROUND_TEXT_FIELD = UITexture.builder()
        .location(GUI_PATH.apply("background/text_field"))
        .imageSize(142, 28)
        .adaptable(1)
        .build();
    public static final UITexture BACKGROUND_TEXT_FIELD_LIGHT_GRAY = UITexture.builder()
        .location(GUI_PATH.apply("background/text_field_light_gray"))
        .imageSize(61, 12)
        .adaptable(1)
        .build();

    public static final SteamTexture SLOT_ITEM_STEAM = SteamTexture.fullImage("slot/item_%s", true);
    public static final SteamTexture SLOT_FLUID_STEAM = SteamTexture.fullImage("slot/fluid_%s", true);
    public static final UITexture SLOT_DARK_GRAY = UITexture.builder()
        .location(GUI_PATH.apply("slot/dark_gray"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture SLOT_MAINTENANCE = UITexture.builder()
        .location(GUI_PATH.apply("slot/maintenance"))
        .imageSize(20, 20)
        .adaptable(1)
        .build();
    public static final UITexture SLOT_UPLIFTED = UITexture.builder()
        .location(GUI_PATH.apply("slot/uplifted"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();

    public static final UITexture OVERLAY_SLOT_ARROW_ME = simpleColorable("overlay_slot/arrow_me");
    public static final UITexture OVERLAY_SLOT_BEAKER_1 = simpleColorable("overlay_slot/beaker_1");
    public static final UITexture OVERLAY_SLOT_BEAKER_2 = simpleColorable("overlay_slot/beaker_2");
    public static final UITexture OVERLAY_SLOT_BEE_DRONE = simpleColorable("overlay_slot/bee_drone");
    public static final UITexture OVERLAY_SLOT_BEE_QUEEN = simpleColorable("overlay_slot/bee_queen");
    public static final UITexture OVERLAY_SLOT_BENDER = simpleColorable("overlay_slot/bender");
    public static final UITexture OVERLAY_SLOT_BOX = simpleColorable("overlay_slot/box");
    public static final UITexture OVERLAY_SLOT_BOXED = simpleColorable("overlay_slot/boxed");
    public static final UITexture OVERLAY_SLOT_CANISTER = simpleColorable("overlay_slot/canister");
    public static final SteamTexture OVERLAY_SLOT_CANISTER_STEAM = SteamTexture
        .fullImage("overlay_slot/canister_%s", true);
    public static final UITexture OVERLAY_SLOT_CANNER = simpleColorable("overlay_slot/canner");
    public static final UITexture OVERLAY_SLOT_CAULDRON = simpleColorable("overlay_slot/cauldron");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE = simpleColorable("overlay_slot/centrifuge");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE_FLUID = simpleColorable("overlay_slot/centrifuge_fluid");
    public static final SteamTexture OVERLAY_SLOT_CENTRIFUGE_STEAM = SteamTexture
        .fullImage("overlay_slot/centrifuge_%s", true);
    public static final UITexture OVERLAY_SLOT_CHARGER = simpleColorable("overlay_slot/charger");
    public static final UITexture OVERLAY_SLOT_CHARGER_FLUID = simpleColorable("overlay_slot/charger_fluid");
    public static final UITexture OVERLAY_SLOT_CIRCUIT = simpleColorable("overlay_slot/circuit");
    public static final SteamTexture OVERLAY_SLOT_COAL_STEAM = SteamTexture.fullImage("overlay_slot/coal_%s", true);
    public static final UITexture OVERLAY_SLOT_COMPRESSOR = simpleColorable("overlay_slot/compressor");
    public static final SteamTexture OVERLAY_SLOT_COMPRESSOR_STEAM = SteamTexture
        .fullImage("overlay_slot/compressor_%s", true);
    public static final UITexture OVERLAY_SLOT_CRUSHED_ORE = simpleColorable("overlay_slot/crushed_ore");
    public static final SteamTexture OVERLAY_SLOT_CRUSHED_ORE_STEAM = SteamTexture
        .fullImage("overlay_slot/crushed_ore_%s", true);
    public static final UITexture OVERLAY_SLOT_CUTTER_SLICED = simpleColorable("overlay_slot/cutter_sliced");
    public static final UITexture OVERLAY_SLOT_DATA_ORB = simpleColorable("overlay_slot/data_orb");
    public static final UITexture OVERLAY_SLOT_DATA_STICK = simpleColorable("overlay_slot/data_stick");
    public static final UITexture OVERLAY_SLOT_DUST = simpleColorable("overlay_slot/dust");
    public static final SteamTexture OVERLAY_SLOT_DUST_STEAM = SteamTexture.fullImage("overlay_slot/dust_%s", true);
    public static final SteamTexture OVERLAY_SLOT_BLOCK_STEAM = SteamTexture.fullImage("overlay_slot/block_%s", true);
    public static final UITexture OVERLAY_SLOT_EXPLOSIVE = simpleColorable("overlay_slot/explosive");
    public static final UITexture OVERLAY_SLOT_EXTRUDER_SHAPE = simpleColorable("overlay_slot/extruder_shape");
    public static final UITexture OVERLAY_SLOT_FURNACE = simpleColorable("overlay_slot/furnace");
    public static final SteamTexture OVERLAY_SLOT_FURNACE_STEAM = SteamTexture
        .fullImage("overlay_slot/furnace_%s", true);
    public static final UITexture OVERLAY_SLOT_GEM = simpleColorable("overlay_slot/gem");
    public static final UITexture OVERLAY_SLOT_HAMMER = simpleColorable("overlay_slot/hammer");
    public static final SteamTexture OVERLAY_SLOT_HAMMER_STEAM = SteamTexture.fullImage("overlay_slot/hammer_%s", true);
    public static final UITexture OVERLAY_SLOT_HEATER_1 = simpleColorable("overlay_slot/heater_1");
    public static final UITexture OVERLAY_SLOT_HEATER_2 = simpleColorable("overlay_slot/heater_2");
    public static final UITexture OVERLAY_SLOT_IMPLOSION = simpleColorable("overlay_slot/implosion");
    public static final UITexture OVERLAY_SLOT_IN = simpleColorable("overlay_slot/in");
    public static final SteamTexture OVERLAY_SLOT_IN_STEAM = SteamTexture.fullImage("overlay_slot/in_%s", true);
    public static final SteamTexture OVERLAY_SLOT_INGOT_STEAM = SteamTexture.fullImage("overlay_slot/ingot_%s", true);
    public static final UITexture OVERLAY_SLOT_INT_CIRCUIT = simpleColorable("overlay_slot/int_circuit");
    public static final UITexture OVERLAY_SLOT_LENS = simpleColorable("overlay_slot/lens");
    public static final UITexture OVERLAY_SLOT_MICROSCOPE = simpleColorable("overlay_slot/microscope");
    public static final UITexture OVERLAY_SLOT_MOLD = simpleColorable("overlay_slot/mold");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_1 = simpleColorable("overlay_slot/molecular_1");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_2 = simpleColorable("overlay_slot/molecular_2");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_3 = simpleColorable("overlay_slot/molecular_3");
    public static final UITexture OVERLAY_SLOT_OUT = simpleColorable("overlay_slot/out");
    public static final SteamTexture OVERLAY_SLOT_OUT_STEAM = SteamTexture.fullImage("overlay_slot/out_%s", true);
    public static final UITexture OVERLAY_SLOT_PAGE_BLANK = simpleColorable("overlay_slot/page_blank");
    public static final UITexture OVERLAY_SLOT_PAGE_PRINTED = simpleColorable("overlay_slot/page_printed");
    public static final UITexture OVERLAY_SLOT_PRESS_1 = simpleColorable("overlay_slot/press_1");
    public static final UITexture OVERLAY_SLOT_PRESS_2 = simpleColorable("overlay_slot/press_2");
    public static final UITexture OVERLAY_SLOT_PRESS_3 = simpleColorable("overlay_slot/press_3");
    public static final UITexture OVERLAY_SLOT_RECYCLE = simpleColorable("overlay_slot/recycle");
    public static final UITexture OVERLAY_SLOT_ROD_1 = simpleColorable("overlay_slot/rod_1");
    public static final UITexture OVERLAY_SLOT_ROD_2 = simpleColorable("overlay_slot/rod_2");
    public static final UITexture OVERLAY_SLOT_SLICE_SHAPE = simpleColorable("overlay_slot/slice_shape");
    public static final UITexture OVERLAY_SLOT_SLICER_SLICED = simpleColorable("overlay_slot/slicer_sliced");
    public static final UITexture OVERLAY_SLOT_SQUARE = simpleColorable("overlay_slot/square");
    public static final UITexture OVERLAY_SLOT_UUA = simpleColorable("overlay_slot/uua");
    public static final UITexture OVERLAY_SLOT_UUM = simpleColorable("overlay_slot/uum");
    public static final UITexture OVERLAY_SLOT_VIAL_1 = simpleColorable("overlay_slot/vial_1");
    public static final UITexture OVERLAY_SLOT_VIAL_2 = simpleColorable("overlay_slot/vial_2");
    public static final UITexture OVERLAY_SLOT_WIREMILL = simpleColorable("overlay_slot/wiremill");
    public static final UITexture OVERLAY_SLOT_WRENCH = simpleColorable("overlay_slot/wrench");
    public static final UITexture[] OVERLAY_SLOTS_NUMBER = IntStream.range(0, 12)
        .mapToObj(i -> UITexture.fullImage(GregTech.ID, "gui/overlay_slot/number_" + i, true))
        .toArray(UITexture[]::new);

    public static final UITexture PROGRESSBAR_ARROW = simpleColorable("progressbar/arrow");
    public static final SteamTexture PROGRESSBAR_ARROW_STEAM = SteamTexture.fullImage("progressbar/arrow_%s", true);
    public static final SteamTexture PROGRESSBAR_ARROW_2_STEAM = SteamTexture.fullImage("progressbar/arrow_2_%s", true);
    public static final UITexture PROGRESSBAR_ARROW_MULTIPLE = simpleColorable("progressbar/arrow_multiple");
    public static final UITexture PROGRESSBAR_ASSEMBLE = simpleColorable("progressbar/assemble");
    public static final UITexture PROGRESSBAR_ASSEMBLY_LINE_1 = simpleColorable("progressbar/assemblyline_1");
    public static final UITexture PROGRESSBAR_ASSEMBLY_LINE_2 = simpleColorable("progressbar/assemblyline_2");
    public static final UITexture PROGRESSBAR_ASSEMBLY_LINE_3 = simpleColorable("progressbar/assemblyline_3");
    public static final UITexture PROGRESSBAR_BATH = simpleColorable("progressbar/bath");
    public static final UITexture PROGRESSBAR_BENDING = simpleColorable("progressbar/bending");
    public static final SteamTexture PROGRESSBAR_BOILER_EMPTY_STEAM = SteamTexture
        .fullImage("progressbar/boiler_empty_%s", true);
    public static final UITexture PROGRESSBAR_BOILER_HEAT = simpleColorable("progressbar/boiler_heat");
    public static final UITexture PROGRESSBAR_BOILER_STEAM = simpleColorable("progressbar/boiler_steam");
    public static final UITexture PROGRESSBAR_BOILER_WATER = simpleColorable("progressbar/boiler_water");
    public static final UITexture PROGRESSBAR_CANNER = simpleColorable("progressbar/canner");
    public static final UITexture PROGRESSBAR_CIRCUIT_ASSEMBLER = simpleColorable("progressbar/circuit_assembler");
    public static final UITexture PROGRESSBAR_COMPRESS = simpleColorable("progressbar/compress");
    public static final SteamTexture PROGRESSBAR_COMPRESS_STEAM = SteamTexture
        .fullImage("progressbar/compress_%s", true);
    public static final UITexture PROGRESSBAR_CUT = simpleColorable("progressbar/cut");
    public static final UITexture PROGRESSBAR_EXTRACT = simpleColorable("progressbar/extract");
    public static final SteamTexture PROGRESSBAR_EXTRACT_STEAM = SteamTexture.fullImage("progressbar/extract_%s", true);
    public static final UITexture PROGRESSBAR_EXTRUDE = simpleColorable("progressbar/extrude");
    public static final SteamTexture PROGRESSBAR_FUEL_STEAM = SteamTexture.fullImage("progressbar/fuel_%s", true);
    public static final UITexture PROGRESSBAR_HAMMER = simpleColorable("progressbar/hammer");
    public static final UITexture PROGRESSBAR_HAMMER_BASE = simpleColorable("progressbar/hammer_base");
    public static final SteamTexture PROGRESSBAR_HAMMER_STEAM = SteamTexture.fullImage("progressbar/hammer_%s", true);
    public static final SteamTexture PROGRESSBAR_HAMMER_BASE_STEAM = SteamTexture
        .fullImage("progressbar/hammer_base_%s", true);
    public static final UITexture PROGRESSBAR_LATHE = simpleColorable("progressbar/lathe");
    public static final UITexture PROGRESSBAR_LATHE_BASE = simpleColorable("progressbar/lathe_base");
    public static final UITexture PROGRESSBAR_MACERATE = simpleColorable("progressbar/macerate");
    public static final SteamTexture PROGRESSBAR_MACERATE_STEAM = SteamTexture
        .fullImage("progressbar/macerate_%s", true);
    public static final UITexture PROGRESSBAR_MAGNET = simpleColorable("progressbar/magnet");
    public static final UITexture PROGRESSBAR_MIXER = simpleColorable("progressbar/mixer");
    public static final UITexture PROGRESSBAR_RECYCLE = simpleColorable("progressbar/recycle");
    public static final UITexture PROGRESSBAR_SIFT = simpleColorable("progressbar/sift");
    public static final UITexture PROGRESSBAR_SLICE = simpleColorable("progressbar/slice");
    public static final UITexture PROGRESSBAR_STORED_EU = simpleColorable("progressbar/stored_eu");
    public static final UITexture PROGRESSBAR_WIREMILL = simpleColorable("progressbar/wiremill");

    public static final UITexture TAB_COVER_NORMAL = simpleColorable("tab/cover_normal");
    public static final UITexture TAB_COVER_HIGHLIGHT = simpleColorable("tab/cover_highlight");
    public static final UITexture TAB_COVER_DISABLED = simpleColorable("tab/cover_disabled");
    public static final SteamTexture TAB_COVER_STEAM_NORMAL = SteamTexture.fullImage("tab/cover_%s_normal", true);
    public static final SteamTexture TAB_COVER_STEAM_HIGHLIGHT = SteamTexture.fullImage("tab/cover_%s_highlight", true);
    public static final SteamTexture TAB_COVER_STEAM_DISABLED = SteamTexture.fullImage("tab/cover_%s_disabled", true);
    public static final UITexture TAB_TITLE = UITexture.builder()
        .location(GUI_PATH.apply("tab/title"))
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .build();
    public static final UITexture TAB_TITLE_DARK = UITexture.builder()
        .location(GUI_PATH.apply("tab/title_dark"))
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .build();
    public static final SteamTexture TAB_TITLE_STEAM = SteamTexture.adaptableTexture("tab/title_%s", 28, 28, 4);
    public static final SteamTexture TAB_TITLE_DARK_STEAM = SteamTexture
        .adaptableTexture("tab/title_dark_%s", 28, 28, 4);
    public static final UITexture TAB_TITLE_ANGULAR = UITexture.builder()
        .location(GUI_PATH.apply("tab/title_angular"))
        .imageSize(28, 28)
        .adaptable(4)
        .canApplyTheme()
        .build();
    public static final SteamTexture TAB_TITLE_ANGULAR_STEAM = SteamTexture
        .adaptableTexture("tab/title_angular_%s", 28, 28, 4);

    public static final UITexture BUTTON_STANDARD = UITexture.builder()
        .location(GUI_PATH.apply("button/standard"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_STANDARD_PRESSED = UITexture.builder()
        .location(GUI_PATH.apply("button/standard_pressed"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_STANDARD_DISABLED = UITexture.builder()
        .location(GUI_PATH.apply("button/standard_disabled"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_STANDARD_TOGGLE = UITexture.builder()
        .location(GUI_PATH.apply("button/standard_toggle"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_STANDARD_TOGGLE_DISABLED = UITexture.builder()
        .location(GUI_PATH.apply("button/standard_toggle_disabled"))
        .imageSize(18, 18)
        .adaptable(1)
        .build();
    public static final UITexture BUTTON_COVER_NORMAL = simple("button/cover_normal");
    public static final UITexture BUTTON_COVER_NORMAL_HOVERED = simple("button/cover_normal_hovered");
    public static final UITexture BUTTON_COVER_NORMAL_DISABLED = simple("button/cover_normal_disabled");

    public static final UITexture OVERLAY_BUTTON_DISABLE = simple("overlay_button/disable");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_OFF = simple("overlay_button/redstone_off");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_ON = simple("overlay_button/redstone_on");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_ON = simple("overlay_button/power_switch_on");
    public static final UITexture OVERLAY_BUTTON_POWER_SWITCH_OFF = simple("overlay_button/power_switch_off");
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_NONE = simple("overlay_button/void_excess_none");
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ITEM = simple("overlay_button/void_excess_item");
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_FLUID = simple("overlay_button/void_excess_fluid");
    public static final UITexture OVERLAY_BUTTON_VOID_EXCESS_ALL = simple("overlay_button/void_excess_all");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_ON = simple("overlay_button/input_separation_on");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED = simple(
        "overlay_button/input_separation_on_disabled");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_OFF = simple("overlay_button/input_separation_off");
    public static final UITexture OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED = simple(
        "overlay_button/input_separation_off_disabled");
    public static final UITexture OVERLAY_BUTTON_RECIPE_LOCKED = simple("overlay_button/recipe_locked");
    public static final UITexture OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED = simple(
        "overlay_button/recipe_locked_disabled");
    public static final UITexture OVERLAY_BUTTON_RECIPE_UNLOCKED = simple("overlay_button/recipe_unlocked");
    public static final UITexture OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED = simple(
        "overlay_button/recipe_unlocked_disabled");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_ON = simple("overlay_button/batch_mode_on");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED = simple(
        "overlay_button/batch_mode_on_disabled");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_OFF = simple("overlay_button/batch_mode_off");
    public static final UITexture OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED = simple(
        "overlay_button/batch_mode_off_disabled");
    public static final UITexture OVERLAY_BUTTON_FORBIDDEN = simple("overlay_button/forbidden");
    public static final UITexture OVERLAY_BUTTON_DOWN_TIERING_ON = simple("overlay_button/down_tiering_on");
    public static final UITexture OVERLAY_BUTTON_DOWN_TIERING_OFF = simple("overlay_button/down_tiering_off");
    public static final UITexture OVERLAY_BUTTON_CHECKMARK = simple("overlay_button/checkmark");
    public static final UITexture OVERLAY_BUTTON_CROSS = simple("overlay_button/cross");
    public static final UITexture OVERLAY_BUTTON_WHITELIST = simple("overlay_button/whitelist");
    public static final UITexture OVERLAY_BUTTON_BLACKLIST = simple("overlay_button/blacklist");
    public static final UITexture OVERLAY_BUTTON_PROGRESS = simple("overlay_button/progress");
    public static final UITexture OVERLAY_BUTTON_EXPORT = simple("overlay_button/export");
    public static final UITexture OVERLAY_BUTTON_IMPORT = simple("overlay_button/import");
    public static final UITexture OVERLAY_BUTTON_AUTOOUTPUT_ITEM = simple("overlay_button/autooutput_item");
    public static final UITexture OVERLAY_BUTTON_AUTOOUTPUT_FLUID = simple("overlay_button/autooutput_fluid");
    public static final UITexture OVERLAY_BUTTON_ALLOW_INPUT = simple("overlay_button/allow_input");
    public static final UITexture OVERLAY_BUTTON_AUTOPULL_ME = simple("overlay_button/auto_pull_me");
    public static final UITexture OVERLAY_BUTTON_AUTOPULL_ME_DISABLED = simple("overlay_button/auto_pull_me_disabled");
    public static final UITexture OVERLAY_BUTTON_BLOCK_INPUT = simple("overlay_button/block_input");
    public static final UITexture OVERLAY_BUTTON_ARROW_GREEN_UP = simple("overlay_button/arrow_green_up");
    public static final UITexture OVERLAY_BUTTON_ARROW_GREEN_DOWN = simple("overlay_button/arrow_green_down");
    public static final UITexture OVERLAY_BUTTON_CYCLIC = simple("overlay_button/cyclic");
    public static final UITexture OVERLAY_BUTTON_EMIT_ENERGY = simple("overlay_button/emit_energy");
    public static final UITexture OVERLAY_BUTTON_EMIT_REDSTONE = simple("overlay_button/emit_redstone");
    public static final UITexture OVERLAY_BUTTON_INVERT_REDSTONE = simple("overlay_button/invert_redstone");
    public static final UITexture OVERLAY_BUTTON_STOCKING_MODE = simple("overlay_button/stocking_mode");
    public static final UITexture OVERLAY_BUTTON_INVERT_FILTER = simple("overlay_button/invert_filter");
    public static final UITexture OVERLAY_BUTTON_NBT = simple("overlay_button/nbt");
    public static final UITexture OVERLAY_BUTTON_PRINT = simple("overlay_button/print");
    public static final UITexture OVERLAY_BUTTON_TRANSPOSE = simple("overlay_button/transpose");
    public static final UITexture OVERLAY_BUTTON_BOUNDING_BOX = simple("overlay_button/bounding_box");
    public static final UITexture OVERLAY_BUTTON_MINUS_SMALL = simple("overlay_button/minus_small");
    public static final UITexture OVERLAY_BUTTON_MINUS_LARGE = simple("overlay_button/minus_large");
    public static final UITexture OVERLAY_BUTTON_PLUS_SMALL = simple("overlay_button/plus_small");
    public static final UITexture OVERLAY_BUTTON_PLUS_LARGE = simple("overlay_button/plus_large");
    public static final UITexture OVERLAY_BUTTON_GATE_AND = simple("overlay_button/gate_and");
    public static final UITexture OVERLAY_BUTTON_GATE_NAND = simple("overlay_button/gate_nand");
    public static final UITexture OVERLAY_BUTTON_GATE_OR = simple("overlay_button/gate_or");
    public static final UITexture OVERLAY_BUTTON_GATE_NOR = simple("overlay_button/gate_nor");
    public static final UITexture OVERLAY_BUTTON_ANALOG = simple("overlay_button/analog");
    public static final UITexture OVERLAY_BUTTON_LOCK = simple("overlay_button/lock");
    public static final UITexture OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE = simple(
        "overlay_button/input_from_output_side");
    public static final UITexture OVERLAY_BUTTON_TANK_VOID_EXCESS = simple("overlay_button/tank_void_excess");
    public static final UITexture OVERLAY_BUTTON_TANK_VOID_ALL = simple("overlay_button/tank_void_all");
    public static final UITexture OVERLAY_BUTTON_NEI = simple("overlay_button/nei");

    public static final UITexture PICTURE_SCREEN_BLACK = UITexture.builder()
        .location(GUI_PATH.apply("picture/screen_black"))
        .imageSize(16, 16)
        .adaptable(2)
        .build();
    public static final UITexture PICTURE_RADIATION_WARNING = simple("picture/radiation_warning");
    public static final UITexture PICTURE_GT_LOGO_17x17_TRANSPARENT = simple("picture/gt_logo_17x17_transparent");
    public static final UITexture PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY = simple(
        "picture/gt_logo_17x17_transparent_gray");
    public static final SteamTexture PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM = SteamTexture
        .fullImage("picture/gt_logo_17x17_transparent_%s", false);
    public static final UITexture PICTURE_GT_LOGO_18x18 = simple("picture/gt_logo_18x18");
    public static final UITexture PICTURE_GT_LOGO_19x19 = simple("picture/gt_logo_19x19");
    public static final UITexture PICTURE_INFORMATION = simple("picture/information");
    public static final UITexture PICTURE_STALLED_ELECTRICITY = simple("picture/stalled_electricity");
    public static final UITexture PICTURE_STALLED_STEAM = simple("picture/stalled_steam");
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_22_RED = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_22_red"))
        .imageSize(87, 22)
        .uv(fromRight ? 87 - width : 0, 0, fromRight ? 87 : width, 22)
        .build();
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_22_BLUE = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_22_blue"))
        .imageSize(87, 22)
        .uv(fromRight ? 87 - width : 0, 0, fromRight ? 87 : width, 22)
        .build();
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_22_WHITE = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_22_white"))
        .imageSize(87, 22)
        .uv(fromRight ? 87 - width : 0, 0, fromRight ? 87 : width, 22)
        .build();
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_24_RED = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_24_red"))
        .imageSize(69, 24)
        .uv(fromRight ? 69 - width : 0, 0, fromRight ? 69 : width, 24)
        .build();
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_24_BLUE = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_24_blue"))
        .imageSize(69, 24)
        .uv(fromRight ? 69 - width : 0, 0, fromRight ? 69 : width, 24)
        .build();
    public static final BiFunction<Integer, Boolean, UITexture> PICTURE_ARROW_24_WHITE = (width, fromRight) -> UITexture
        .builder()
        .location(GUI_PATH.apply("picture/arrow_24_white"))
        .imageSize(69, 24)
        .uv(fromRight ? 69 - width : 0, 0, fromRight ? 69 : width, 24)
        .build();
    public static final UITexture PICTURE_FLUID_WINDOW = simple("picture/fluid_window");
    public static final UITexture PICTURE_FLUID_TANK = simple("picture/fluid_tank");
    public static final UITexture PICTURE_SLOTS_HOLO_3BY3 = simple("picture/slots_holo_3by3");
    public static final UITexture PICTURE_ARROW_DOUBLE = simple("picture/arrow_double");
    public static final UITexture PICTURE_SUPER_BUFFER = simple("picture/super_buffer");
    public static final UITexture PICTURE_SQUARE_LIGHT_GRAY = simple("picture/square_light_gray");
    public static final UITexture PICTURE_GAUGE = simple("picture/gauge");
    public static final UITexture PICTURE_ITEM_IN = simple("picture/item_in");
    public static final UITexture PICTURE_ITEM_OUT = simple("picture/item_out");
    public static final UITexture PICTURE_FLUID_IN = simple("picture/fluid_in");
    public static final UITexture PICTURE_FLUID_OUT = simple("picture/fluid_out");
}
