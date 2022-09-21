package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GT_UITextures {

    private static final String MODID = "gregtech";

    public static final UITexture BACKGROUND_SINGLEBLOCK_DEFAULT =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_default", 176, 166, 4);
    public static final SteamTexture BACKGROUND_STEAM =
            SteamTexture.adaptableTexture(MODID, "gui/background/%s", 176, 166, 4);
    public static final UITexture BACKGROUND_SINGLEBLOCK_BRONZE =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_bronze", 176, 166, 4);
    public static final UITexture BACKGROUND_SINGLEBLOCK_STEEL =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_steel", 176, 166, 4);
    public static final UITexture BACKGROUND_PRIMITIVE =
            AdaptableUITexture.of(MODID, "gui/background/primitive", 176, 166, 4);
    public static final UITexture BACKGROUND_MULTIBLOCK_DEFAULT =
            AdaptableUITexture.of(MODID, "gui/background/multiblock_default", 176, 166, 4);
    public static final UITexture BACKGROUND_MULTIBLOCK_NEI_DEFAULT =
            AdaptableUITexture.of(MODID, "gui/background/multiblock_nei_default", 176, 166, 4);
    public static final UITexture BACKGROUND_PLASMA_FORGE_NEI =
            AdaptableUITexture.of(MODID, "gui/background/PlasmaForge_nei", 176, 166, 4);
    public static final UITexture BACKGROUND_FUSION_COMPUTER =
            AdaptableUITexture.of(MODID, "gui/background/FusionComputer", 176, 166, 4);

    public static final SteamTexture SLOT_ITEM_STEAM = SteamTexture.fullImage(MODID, "gui/slot/item_%s");
    public static final AdaptableUITexture SLOT_TRANSPARENT =
            AdaptableUITexture.of(MODID, "gui/slot/transparent", 18, 18, 1);
    public static final AdaptableUITexture SLOT_GRAY = AdaptableUITexture.of(MODID, "gui/slot/gray", 18, 18, 1);
    public static final AdaptableUITexture SLOT_MAINTENANCE =
            AdaptableUITexture.of(MODID, "gui/slot/maintenance", 20, 20, 1);
    public static final AdaptableUITexture SLOT_UPLIFTED = AdaptableUITexture.of(MODID, "gui/slot/uplifted", 18, 18, 1);

    public static final UITexture OVERLAY_SLOT_ARROW_ME = UITexture.fullImage(MODID, "gui/overlay_slot/arrow_me");
    public static final UITexture OVERLAY_SLOT_BEAKER_1 = UITexture.fullImage(MODID, "gui/overlay_slot/beaker_1");
    public static final UITexture OVERLAY_SLOT_BEAKER_2 = UITexture.fullImage(MODID, "gui/overlay_slot/beaker_2");
    public static final UITexture OVERLAY_SLOT_BEE_DRONE = UITexture.fullImage(MODID, "gui/overlay_slot/bee_drone");
    public static final UITexture OVERLAY_SLOT_BEE_QUEEN = UITexture.fullImage(MODID, "gui/overlay_slot/bee_queen");
    public static final UITexture OVERLAY_SLOT_BENDER = UITexture.fullImage(MODID, "gui/overlay_slot/bender");
    public static final UITexture OVERLAY_SLOT_BOX = UITexture.fullImage(MODID, "gui/overlay_slot/box");
    public static final UITexture OVERLAY_SLOT_BOXED = UITexture.fullImage(MODID, "gui/overlay_slot/boxed");
    public static final UITexture OVERLAY_SLOT_CANISTER = UITexture.fullImage(MODID, "gui/overlay_slot/canister");
    public static final UITexture OVERLAY_SLOT_CANISTER_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/canister_bronze");
    public static final UITexture OVERLAY_SLOT_CANISTER_STEEL =
            UITexture.fullImage(MODID, "gui/overlay_slot/canister_steel");
    public static final UITexture OVERLAY_SLOT_CANNER = UITexture.fullImage(MODID, "gui/overlay_slot/canner");
    public static final UITexture OVERLAY_SLOT_CAULDRON = UITexture.fullImage(MODID, "gui/overlay_slot/cauldron");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE = UITexture.fullImage(MODID, "gui/overlay_slot/centrifuge");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE_FLUID =
            UITexture.fullImage(MODID, "gui/overlay_slot/centrifuge_fluid");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/centrifuge_bronze");
    public static final UITexture OVERLAY_SLOT_CENTRIFUGE_STEEL =
            UITexture.fullImage(MODID, "gui/overlay_slot/centrifuge_steel");
    public static final UITexture OVERLAY_SLOT_CHARGER = UITexture.fullImage(MODID, "gui/overlay_slot/charger");
    public static final UITexture OVERLAY_SLOT_CHARGER_FLUID =
            UITexture.fullImage(MODID, "gui/overlay_slot/charger_fluid");
    public static final UITexture OVERLAY_SLOT_CIRCUIT = UITexture.fullImage(MODID, "gui/overlay_slot/circuit");
    public static final UITexture OVERLAY_SLOT_COAL_BRONZE = UITexture.fullImage(MODID, "gui/overlay_slot/coal_bronze");
    public static final UITexture OVERLAY_SLOT_COAL_STEEL = UITexture.fullImage(MODID, "gui/overlay_slot/coal_steel");
    public static final UITexture OVERLAY_SLOT_COMPRESSOR = UITexture.fullImage(MODID, "gui/overlay_slot/compressor");
    public static final UITexture OVERLAY_SLOT_COMPRESSOR_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/compressor_bronze");
    public static final UITexture OVERLAY_SLOT_COMPRESSOR_STEEL =
            UITexture.fullImage(MODID, "gui/overlay_slot/compressor_steel");
    public static final UITexture OVERLAY_SLOT_CRUSHED_ORE = UITexture.fullImage(MODID, "gui/overlay_slot/crushed_ore");
    public static final UITexture OVERLAY_SLOT_CRUSHED_ORE_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/crushed_ore_bronze");
    public static final UITexture OVERLAY_SLOT_CRUSHED_ORE_STEEL =
            UITexture.fullImage(MODID, "gui/overlay_slot/crushed_ore_steel");
    public static final UITexture OVERLAY_SLOT_CUTTER_SLICED =
            UITexture.fullImage(MODID, "gui/overlay_slot/cutter_sliced");
    public static final UITexture OVERLAY_SLOT_DATA_ORB = UITexture.fullImage(MODID, "gui/overlay_slot/data_orb");
    public static final UITexture OVERLAY_SLOT_DATA_STICK = UITexture.fullImage(MODID, "gui/overlay_slot/data_stick");
    public static final UITexture OVERLAY_SLOT_DUST = UITexture.fullImage(MODID, "gui/overlay_slot/dust");
    public static final SteamTexture OVERLAY_SLOT_DUST_STEAM =
            SteamTexture.fullImage(MODID, "gui/overlay_slot/dust_%s");
    public static final UITexture OVERLAY_SLOT_EXPLOSIVE = UITexture.fullImage(MODID, "gui/overlay_slot/explosive");
    public static final UITexture OVERLAY_SLOT_EXTRUDER_SHAPE =
            UITexture.fullImage(MODID, "gui/overlay_slot/extruder_shape");
    public static final UITexture OVERLAY_SLOT_FURNACE = UITexture.fullImage(MODID, "gui/overlay_slot/furnace");
    public static final SteamTexture OVERLAY_SLOT_FURNACE_STEAM =
            SteamTexture.fullImage(MODID, "gui/overlay_slot/furnace_%s");
    public static final UITexture OVERLAY_SLOT_GEM = UITexture.fullImage(MODID, "gui/overlay_slot/gem");
    public static final UITexture OVERLAY_SLOT_HAMMER = UITexture.fullImage(MODID, "gui/overlay_slot/hammer");
    public static final UITexture OVERLAY_SLOT_HAMMER_BRONZE =
            UITexture.fullImage(MODID, "gui/overlay_slot/hammer_bronze");
    public static final UITexture OVERLAY_SLOT_HAMMER_STEEL =
            UITexture.fullImage(MODID, "gui/overlay_slot/hammer_steel");
    public static final UITexture OVERLAY_SLOT_HEATER_1 = UITexture.fullImage(MODID, "gui/overlay_slot/heater_1");
    public static final UITexture OVERLAY_SLOT_HEATER_2 = UITexture.fullImage(MODID, "gui/overlay_slot/heater_2");
    public static final UITexture OVERLAY_SLOT_IMPLOSION = UITexture.fullImage(MODID, "gui/overlay_slot/implosion");
    public static final UITexture OVERLAY_SLOT_IN = UITexture.fullImage(MODID, "gui/overlay_slot/in");
    public static final UITexture OVERLAY_SLOT_IN_BRONZE = UITexture.fullImage(MODID, "gui/overlay_slot/in_bronze");
    public static final UITexture OVERLAY_SLOT_IN_STEEL = UITexture.fullImage(MODID, "gui/overlay_slot/in_steel");
    public static final SteamTexture OVERLAY_SLOT_INGOT_STEAM =
            SteamTexture.fullImage(MODID, "gui/overlay_slot/ingot_%s");
    public static final UITexture OVERLAY_SLOT_INT_CIRCUIT = UITexture.fullImage(MODID, "gui/overlay_slot/int_circuit");
    public static final UITexture OVERLAY_SLOT_LENS = UITexture.fullImage(MODID, "gui/overlay_slot/lens");
    public static final UITexture OVERLAY_SLOT_MICROSCOPE = UITexture.fullImage(MODID, "gui/overlay_slot/microscope");
    public static final UITexture OVERLAY_SLOT_MOLD = UITexture.fullImage(MODID, "gui/overlay_slot/mold");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_1 = UITexture.fullImage(MODID, "gui/overlay_slot/molecular_1");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_2 = UITexture.fullImage(MODID, "gui/overlay_slot/molecular_2");
    public static final UITexture OVERLAY_SLOT_MOLECULAR_3 = UITexture.fullImage(MODID, "gui/overlay_slot/molecular_3");
    public static final UITexture OVERLAY_SLOT_OUT = UITexture.fullImage(MODID, "gui/overlay_slot/out");
    public static final UITexture OVERLAY_SLOT_OUT_BRONZE = UITexture.fullImage(MODID, "gui/overlay_slot/out_bronze");
    public static final UITexture OVERLAY_SLOT_OUT_STEEL = UITexture.fullImage(MODID, "gui/overlay_slot/out_steel");
    public static final UITexture OVERLAY_SLOT_PAGE_BLANK = UITexture.fullImage(MODID, "gui/overlay_slot/page_blank");
    public static final UITexture OVERLAY_SLOT_PAGE_PRINTED =
            UITexture.fullImage(MODID, "gui/overlay_slot/page_printed");
    public static final UITexture OVERLAY_SLOT_PRESS_1 = UITexture.fullImage(MODID, "gui/overlay_slot/press_1");
    public static final UITexture OVERLAY_SLOT_PRESS_2 = UITexture.fullImage(MODID, "gui/overlay_slot/press_2");
    public static final UITexture OVERLAY_SLOT_PRESS_3 = UITexture.fullImage(MODID, "gui/overlay_slot/press_3");
    public static final UITexture OVERLAY_SLOT_RECYCLE = UITexture.fullImage(MODID, "gui/overlay_slot/recycle");
    public static final UITexture OVERLAY_SLOT_ROD_1 = UITexture.fullImage(MODID, "gui/overlay_slot/rod_1");
    public static final UITexture OVERLAY_SLOT_ROD_2 = UITexture.fullImage(MODID, "gui/overlay_slot/rod_2");
    public static final UITexture OVERLAY_SLOT_SLICE_SHAPE = UITexture.fullImage(MODID, "gui/overlay_slot/slice_shape");
    public static final UITexture OVERLAY_SLOT_SLICER_SLICED =
            UITexture.fullImage(MODID, "gui/overlay_slot/slicer_sliced");
    public static final UITexture OVERLAY_SLOT_SQUARE = UITexture.fullImage(MODID, "gui/overlay_slot/square");
    public static final UITexture OVERLAY_SLOT_UUA = UITexture.fullImage(MODID, "gui/overlay_slot/uua");
    public static final UITexture OVERLAY_SLOT_UUM = UITexture.fullImage(MODID, "gui/overlay_slot/uum");
    public static final UITexture OVERLAY_SLOT_VIAL_1 = UITexture.fullImage(MODID, "gui/overlay_slot/vial_1");
    public static final UITexture OVERLAY_SLOT_VIAL_2 = UITexture.fullImage(MODID, "gui/overlay_slot/vial_2");
    public static final UITexture OVERLAY_SLOT_WIREMILL = UITexture.fullImage(MODID, "gui/overlay_slot/wiremill");
    public static final UITexture OVERLAY_SLOT_WRENCH = UITexture.fullImage(MODID, "gui/overlay_slot/wrench");
    public static final UITexture[] OVERLAY_SLOTS_NUMBER = IntStream.range(0, 11)
            .mapToObj(i -> UITexture.fullImage(MODID, "gui/overlay_slot/number_" + i))
            .collect(Collectors.toList())
            .toArray(new UITexture[] {});

    public static final UITexture PROGRESSBAR_ARROW = UITexture.fullImage(MODID, "gui/progressbar/arrow");
    public static final SteamTexture PROGRESSBAR_ARROW_STEAM =
            SteamTexture.fullImage(MODID, "gui/progressbar/arrow_%s");
    public static final SteamTexture PROGRESSBAR_ARROW_2_STEAM =
            SteamTexture.fullImage(MODID, "gui/progressbar/arrow_2_%s");
    public static final UITexture PROGRESSBAR_ARROW_MULTIPLE =
            UITexture.fullImage(MODID, "gui/progressbar/arrow_multiple");
    public static final UITexture PROGRESSBAR_ASSEMBLE = UITexture.fullImage(MODID, "gui/progressbar/assemble");
    public static final UITexture PROGRESSBAR_ASSEMBLYLINE = UITexture.fullImage(MODID, "gui/progressbar/assemblyline");
    public static final UITexture PROGRESSBAR_ASSEMBLYLINE_ARROW =
            UITexture.fullImage(MODID, "gui/progressbar/assemblyline_arrow");
    public static final UITexture PROGRESSBAR_BATH = UITexture.fullImage(MODID, "gui/progressbar/bath");
    public static final UITexture PROGRESSBAR_BENDING = UITexture.fullImage(MODID, "gui/progressbar/bending");
    public static final UITexture PROGRESSBAR_BOILER_EMPTY_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/boiler_empty_bronze");
    public static final UITexture PROGRESSBAR_BOILER_EMPTY_STEEL =
            UITexture.fullImage(MODID, "gui/progressbar/boiler_empty_steel");
    public static final UITexture PROGRESSBAR_BOILER_HEAT = UITexture.fullImage(MODID, "gui/progressbar/boiler_heat");
    public static final UITexture PROGRESSBAR_BOILER_STEAM = UITexture.fullImage(MODID, "gui/progressbar/boiler_steam");
    public static final UITexture PROGRESSBAR_BOILER_WATER = UITexture.fullImage(MODID, "gui/progressbar/boiler_water");
    public static final UITexture PROGRESSBAR_CANNER = UITexture.fullImage(MODID, "gui/progressbar/canner");
    public static final UITexture PROGRESSBAR_CIRCUIT_ASSEMBLER =
            UITexture.fullImage(MODID, "gui/progressbar/circuit_assembler");
    public static final UITexture PROGRESSBAR_COMPRESS = UITexture.fullImage(MODID, "gui/progressbar/compress");
    public static final UITexture PROGRESSBAR_COMPRESS_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/compress_bronze");
    public static final UITexture PROGRESSBAR_COMPRESS_STEEL =
            UITexture.fullImage(MODID, "gui/progressbar/compress_steel");
    public static final UITexture PROGRESSBAR_CUT = UITexture.fullImage(MODID, "gui/progressbar/cut");
    public static final UITexture PROGRESSBAR_EXTRACT = UITexture.fullImage(MODID, "gui/progressbar/extract");
    public static final UITexture PROGRESSBAR_EXTRACT_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/extract_bronze");
    public static final UITexture PROGRESSBAR_EXTRACT_STEEL =
            UITexture.fullImage(MODID, "gui/progressbar/extract_steel");
    public static final UITexture PROGRESSBAR_EXTRUDE = UITexture.fullImage(MODID, "gui/progressbar/extrude");
    public static final UITexture PROGRESSBAR_FUEL_BRONZE = UITexture.fullImage(MODID, "gui/progressbar/fuel_bronze");
    public static final UITexture PROGRESSBAR_FUEL_STEEL = UITexture.fullImage(MODID, "gui/progressbar/fuel_steel");
    public static final UITexture PROGRESSBAR_HAMMER = UITexture.fullImage(MODID, "gui/progressbar/hammer");
    public static final UITexture PROGRESSBAR_HAMMER_BASE = UITexture.fullImage(MODID, "gui/progressbar/hammer_base");
    public static final UITexture PROGRESSBAR_HAMMER_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/hammer_bronze");
    public static final UITexture PROGRESSBAR_HAMMER_BASE_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/hammer_base_bronze");
    public static final UITexture PROGRESSBAR_HAMMER_STEEL = UITexture.fullImage(MODID, "gui/progressbar/hammer_steel");
    public static final UITexture PROGRESSBAR_HAMMER_BASE_STEEL =
            UITexture.fullImage(MODID, "gui/progressbar/hammer_base_steel");
    public static final UITexture PROGRESSBAR_LATHE = UITexture.fullImage(MODID, "gui/progressbar/lathe");
    public static final UITexture PROGRESSBAR_LATHE_BASE = UITexture.fullImage(MODID, "gui/progressbar/lathe_base");
    public static final UITexture PROGRESSBAR_MACERATE = UITexture.fullImage(MODID, "gui/progressbar/macerate");
    public static final UITexture PROGRESSBAR_MACERATE_BRONZE =
            UITexture.fullImage(MODID, "gui/progressbar/macerate_bronze");
    public static final UITexture PROGRESSBAR_MACERATE_STEEL =
            UITexture.fullImage(MODID, "gui/progressbar/macerate_steel");
    public static final UITexture PROGRESSBAR_MAGNET = UITexture.fullImage(MODID, "gui/progressbar/magnet");
    public static final UITexture PROGRESSBAR_MIXER = UITexture.fullImage(MODID, "gui/progressbar/mixer");
    public static final UITexture PROGRESSBAR_RECYCLE = UITexture.fullImage(MODID, "gui/progressbar/recycle");
    public static final UITexture PROGRESSBAR_SIFT = UITexture.fullImage(MODID, "gui/progressbar/sift");
    public static final UITexture PROGRESSBAR_SLICE = UITexture.fullImage(MODID, "gui/progressbar/slice");
    public static final UITexture PROGRESSBAR_STORED_EU = UITexture.fullImage(MODID, "gui/progressbar/stored_eu");
    public static final UITexture PROGRESSBAR_WIREMILL = UITexture.fullImage(MODID, "gui/progressbar/wiremill");

    public static final UITexture TAB_COVER_NORMAL = UITexture.fullImage(MODID, "gui/tab/cover_normal");
    public static final UITexture TAB_COVER_HIGHLIGHT = UITexture.fullImage(MODID, "gui/tab/cover_highlight");
    public static final UITexture TAB_COVER_DISABLED = UITexture.fullImage(MODID, "gui/tab/cover_disabled");

    public static final UITexture BUTTON_STANDARD = UITexture.fullImage(MODID, "gui/button/standard");
    public static final UITexture BUTTON_STANDARD_TOGGLE = UITexture.fullImage(MODID, "gui/button/standard_toggle");
    public static final UITexture BUTTON_AUTOOUTPUT_ITEM = UITexture.fullImage(MODID, "gui/button/autooutput_item");
    public static final UITexture BUTTON_AUTOOUTPUT_FLUID = UITexture.fullImage(MODID, "gui/button/autooutput_fluid");
    public static final UITexture BUTTON_CROSS = UITexture.fullImage(MODID, "gui/button/cross");
    public static final UITexture BUTTON_PRINT = UITexture.fullImage(MODID, "gui/button/print");
    public static final UITexture BUTTON_TRANSPOSE = UITexture.fullImage(MODID, "gui/button/transpose");
    public static final UITexture BUTTON_BOUNDINGBOX = UITexture.fullImage(MODID, "gui/button/boundingbox");
    public static final UITexture BUTTON_EMIT_ENERGY = UITexture.fullImage(MODID, "gui/button/emit_energy");
    public static final UITexture BUTTON_EMIT_REDSTONE = UITexture.fullImage(MODID, "gui/button/emit_redstone");
    public static final UITexture BUTTON_INVERT_REDSTONE = UITexture.fullImage(MODID, "gui/button/invert_redstone");
    public static final UITexture BUTTON_BUFFER_TRANSFERMODE =
            UITexture.fullImage(MODID, "gui/button/buffer_transfermode");
    public static final UITexture BUTTON_LOCK = UITexture.fullImage(MODID, "gui/button/lock");
    public static final UITexture BUTTON_INPUT_FROM_OUTPUT_SIDE =
            UITexture.fullImage(MODID, "gui/button/input_from_output_side");
    public static final UITexture BUTTON_VOID_EXCESS = UITexture.fullImage(MODID, "gui/button/void_excess");
    public static final UITexture BUTTON_VOID_ALL = UITexture.fullImage(MODID, "gui/button/void_all");
    public static final UITexture BUTTON_INVERT_FILTER = UITexture.fullImage(MODID, "gui/button/invert_filter");
    public static final UITexture BUTTON_NBT = UITexture.fullImage(MODID, "gui/button/nbt");
    public static final UITexture BUTTON_COVER_NORMAL = UITexture.fullImage(MODID, "gui/button/cover_normal");
    public static final UITexture BUTTON_COVER_NORMAL_HOVERED =
            UITexture.fullImage(MODID, "gui/button/cover_normal_hovered");
    public static final UITexture BUTTON_COVER_NORMAL_DISABLED =
            UITexture.fullImage(MODID, "gui/button/cover_normal_disabled");

    public static final UITexture OVERLAY_BUTTON_DISABLE = UITexture.fullImage(MODID, "gui/overlay_button/disable");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_OFF =
            UITexture.fullImage(MODID, "gui/overlay_button/redstone_off");
    public static final UITexture OVERLAY_BUTTON_REDSTONE_ON =
            UITexture.fullImage(MODID, "gui/overlay_button/redstone_on");
    public static final UITexture OVERLAY_BUTTON_CHECKMARK = UITexture.fullImage(MODID, "gui/overlay_button/checkmark");
    public static final UITexture OVERLAY_BUTTON_CROSS = UITexture.fullImage(MODID, "gui/overlay_button/cross");
    public static final UITexture OVERLAY_BUTTON_WHITELIST = UITexture.fullImage(MODID, "gui/overlay_button/whitelist");
    public static final UITexture OVERLAY_BUTTON_BLACKLIST = UITexture.fullImage(MODID, "gui/overlay_button/blacklist");
    public static final UITexture OVERLAY_BUTTON_PROGRESS = UITexture.fullImage(MODID, "gui/overlay_button/progress");
    public static final UITexture OVERLAY_BUTTON_EXPORT = UITexture.fullImage(MODID, "gui/overlay_button/export");
    public static final UITexture OVERLAY_BUTTON_IMPORT = UITexture.fullImage(MODID, "gui/overlay_button/import");
    public static final UITexture OVERLAY_BUTTON_ALLOW_INPUT =
            UITexture.fullImage(MODID, "gui/overlay_button/allow_input");
    public static final UITexture OVERLAY_BUTTON_BLOCK_INPUT =
            UITexture.fullImage(MODID, "gui/overlay_button/block_input");
    public static final UITexture OVERLAY_BUTTON_ARROW_GREEN_UP =
            UITexture.fullImage(MODID, "gui/overlay_button/arrow_green_up");
    public static final UITexture OVERLAY_BUTTON_ARROW_GREEN_DOWN =
            UITexture.fullImage(MODID, "gui/overlay_button/arrow_green_down");
    public static final UITexture OVERLAY_BUTTON_CYCLIC = UITexture.fullImage(MODID, "gui/overlay_button/cyclic");
    public static final UITexture OVERLAY_BUTTON_PRINT = UITexture.fullImage(MODID, "gui/overlay_button/print");
    public static final UITexture OVERLAY_BUTTON_TRANSPOSE = UITexture.fullImage(MODID, "gui/overlay_button/transpose");
    public static final UITexture OVERLAY_BUTTON_BOUNDING_BOX =
            UITexture.fullImage(MODID, "gui/overlay_button/bounding_box");
    public static final UITexture OVERLAY_BUTTON_MINUS_SMALL =
            UITexture.fullImage(MODID, "gui/overlay_button/minus_small");
    public static final UITexture OVERLAY_BUTTON_MINUS_LARGE =
            UITexture.fullImage(MODID, "gui/overlay_button/minus_large");
    public static final UITexture OVERLAY_BUTTON_PLUS_SMALL =
            UITexture.fullImage(MODID, "gui/overlay_button/plus_small");
    public static final UITexture OVERLAY_BUTTON_PLUS_LARGE =
            UITexture.fullImage(MODID, "gui/overlay_button/plus_large");

    /**
     * Can adjust size as needed.
     */
    public static final AdaptableUITexture PICTURE_SCREEN_BLACK =
            AdaptableUITexture.of(MODID, "gui/picture/screen_black", 16, 16, 2);

    public static final UITexture PICTURE_GT_LOGO_17x17_TRANSPARENT =
            UITexture.fullImage(MODID, "gui/picture/gt_logo_17x17_transparent");
    public static final UITexture PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY =
            UITexture.fullImage(MODID, "gui/picture/gt_logo_17x17_transparent_gray");
    public static final SteamTexture PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM =
            SteamTexture.fullImage(MODID, "gui/picture/gt_logo_17x17_transparent_%s");
    public static final UITexture PICTURE_GT_LOGO_18x18 = UITexture.fullImage(MODID, "gui/picture/gt_logo_18x18");
    public static final UITexture PICTURE_GT_LOGO_19x19 = UITexture.fullImage(MODID, "gui/picture/gt_logo_19x19");
    public static final UITexture PICTURE_INFORMATION = UITexture.fullImage(MODID, "gui/picture/information");
    public static final UITexture PICTURE_SHORTAGE_ELECTRICITY =
            UITexture.fullImage(MODID, "gui/picture/shortage_electricity");
    public static final UITexture PICTURE_SHORTAGE_STEAM = UITexture.fullImage(MODID, "gui/picture/shortage_steam");
    public static final UITexture PICTURE_ARROW_RED = UITexture.fullImage(MODID, "gui/picture/arrow_red");
    public static final UITexture PICTURE_ARROW_BLUE = UITexture.fullImage(MODID, "gui/picture/arrow_blue");
    public static final UITexture PICTURE_ARROW_WHITE = UITexture.fullImage(MODID, "gui/picture/arrow_white");
    public static final UITexture PICTURE_FLUID_WINDOW = UITexture.fullImage(MODID, "gui/picture/fluid_window");
    public static final UITexture PICTURE_FLUID_TANK = UITexture.fullImage(MODID, "gui/picture/fluid_tank");
    public static final UITexture PICTURE_SLOTS_HOLO_3BY3 = UITexture.fullImage(MODID, "gui/picture/slots_holo_3by3");
    public static final UITexture PICTURE_ARROW_DOUBLE = UITexture.fullImage(MODID, "gui/picture/arrow_double");
    public static final UITexture PICTURE_SUPER_BUFFER = UITexture.fullImage(MODID, "gui/picture/super_buffer");
    public static final SteamTexture PICTURE_BLAST_FURNACE_STRUCTURE_STEAM =
            SteamTexture.fullImage(MODID, "gui/picture/blast_furnace_structure_%s");
}
