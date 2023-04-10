package gregtech.api.enums;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import java.math.BigInteger;
import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.internal.IGT_Mod;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.net.IGT_NetworkHandler;

/**
 * Made for static imports, this Class is just a Helper.
 * <p/>
 * I am doing this to have a better Table alike view on my Code, so I can change things faster using the Block Selection
 * Mode of eclipse.
 * <p/>
 * Go to "Window > Preferences > Java > Editor > Content Assist > Favorites" to set static importable Constant Classes
 * such as this one as AutoCompleteable.
 */
@SuppressWarnings("unused") // API Legitimately has unused fields and methods
public class GT_Values {
    // unused: A, C, D, G, H, I, J, K, N, O, Q, R, S, T

    // TODO: Rename Material Units to 'U'
    // TODO: Rename OrePrefixes Class to 'P'
    // TODO: Rename Materials Class to 'M'

    /**
     * Empty String for an easier Call Hierarchy
     */
    public static final String E = "";

    /**
     * The first 32 Bits
     */
    @SuppressWarnings("PointlessBitwiseExpression") // Nicer source layout this way
    public static final int[] B = new int[] { 1 << 0, 1 << 1, 1 << 2, 1 << 3, 1 << 4, 1 << 5, 1 << 6, 1 << 7, 1 << 8,
        1 << 9, 1 << 10, 1 << 11, 1 << 12, 1 << 13, 1 << 14, 1 << 15, 1 << 16, 1 << 17, 1 << 18, 1 << 19, 1 << 20,
        1 << 21, 1 << 22, 1 << 23, 1 << 24, 1 << 25, 1 << 26, 1 << 27, 1 << 28, 1 << 29, 1 << 30, 1 << 31 };

    /**
     * Renamed from "MATERIAL_UNIT" to just "M"
     * <p/>
     * This is worth exactly one normal Item. This Constant can be divided by many commonly used Numbers such as 1, 2,
     * 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 24, ... 64 or 81 without losing precision and is for that
     * reason used as Unit of Amount. But it is also small enough to be multiplied with larger Numbers.
     * <p/>
     * This is used to determine the amount of Material contained inside a prefixed Ore. For example Nugget = M / 9 as
     * it contains out of 1/9 of an Ingot.
     */
    public static final long M = 3628800;

    /**
     * Renamed from "FLUID_MATERIAL_UNIT" to just "L"
     * <p/>
     * Fluid per Material Unit (Prime Factors: 3 * 3 * 2 * 2 * 2 * 2)
     */
    public static final long L = 144;

    /**
     * The Item WildCard Tag. Even shorter than the "-1" of the past
     */
    public static final short W = OreDictionary.WILDCARD_VALUE;

    /**
     * The Voltage Tiers. Use this Array instead of the old named Voltage Variables
     */
    public static final long[] V = new long[] { 8L, 32L, 128L, 512L, 2048L, 8192L, 32_768L, 131_072L, 524_288L,
        2_097_152L, 8_388_608L, 33_554_432L, 134_217_728L, 536_870_912L, Integer.MAX_VALUE - 7,
        // Error tier to prevent out of bounds errors. Not really a real tier (for now).
        8_589_934_592L };

    /**
     * The Voltage Practical. These are recipe voltage you should use if you expect the recipe to use a full amp of that
     * tier. These leave a bit of headroom for cable and transformer losses, but not enough to make it a great gain.
     */
    // this will correctly map ULV to 7.
    public static final long[] VP = Arrays.stream(V)
        .map(
            i -> BigInteger.valueOf(i)
                .multiply(BigInteger.valueOf(30))
                .divide(BigInteger.valueOf(32))
                .longValueExact())
        .toArray();
    // Why -7? Mystery of the universe. Something may break if you change this so please do not without extensive
    // testing.
    // TODO:Adding that in coremod!!!
    // TODO:tier 14,15 wires and transformers only (not even cables !!!)
    // TODO:tier 12,13 the above + batteries, battery buffers, (maybe cables,12 also works for machines)
    // TODO:tier 10,11 the above + chargers and other machines, (cables would be nice)
    // TODO:tier 9 machines and batteries

    // TODO:AND ALL THE MATERIALS... for that
    // TODO:LIST OF MACHINES WITH POINTLESS TIERS (unless you implement some other tiering mechanism like reducing eu
    // cost if time=1tick)
    // Macerator/Compressor/Furnace... and for cheap recipes any

    /**
     * Array of Maximum Amperes at given Tier index
     * <p>
     * keeping Voltage*Amps < Integer.MAX_VALUE-7 for machines (and tier logic 4x EUt 2/ time)
     * </p>
     * <p>
     * AMV[4]= max amps at tier 4
     * </p>
     */
    public static final long[] AatV = new long[] { 268435455, 67108863, 16777215, 4194303, 1048575, 262143, 65535,
        16383, 4095, 1023, 255, 63, 15, 3, 1, 1 };
    /**
     * The short Names for the Voltages
     */
    public static final String[] VN = new String[] { "ULV", // 0
        "LV", // 1
        "MV", // 2
        "HV", // 3
        "EV", // 4
        "IV", // 5
        "LuV", // 6
        "ZPM", // 7
        "UV", // 8
        "UHV", // 9
        "UEV", // 10
        "UIV", // 11
        "UMV", // 12
        "UXV", // 13
        "MAX", // 14
        "ERROR VOLTAGE" // 15
    };

    /**
     * The long Names for the Voltages
     */
    public static final String[] VOLTAGE_NAMES = new String[] { "Ultra Low Voltage", // 0
        "Low Voltage", // 1
        "Medium Voltage", // 2
        "High Voltage", // 3
        "Extreme Voltage", // 4
        "Insane Voltage", // 5
        "Ludicrous Voltage", // 6
        "ZPM Voltage", // 7
        "Ultimate Voltage", // 8
        "Ultimate High Voltage", // 9
        "Ultimate Extreme Voltage", // 10
        "Ultimate Insane Voltage", // 11
        "Ultimate Mega Voltage", // 12
        "Ultimate Extended Mega Voltage", // 13
        "Maximum Voltage", // 14
        "Error Voltage, report this" // 15
    };

    public static final String[] TIER_COLORS = new String[] { EnumChatFormatting.RED.toString(), // ULV, 0
        EnumChatFormatting.GRAY.toString(), // LV, 1
        EnumChatFormatting.GOLD.toString(), // MV, 2
        EnumChatFormatting.YELLOW.toString(), // HV, 3
        EnumChatFormatting.DARK_GRAY.toString(), // EV, 4
        EnumChatFormatting.GREEN.toString(), // IV, 5
        EnumChatFormatting.LIGHT_PURPLE.toString(), // LuV, 6
        EnumChatFormatting.AQUA.toString(), // ZPM, 7
        EnumChatFormatting.DARK_GREEN.toString(), // UV, 8
        EnumChatFormatting.DARK_RED.toString(), // UHV, 9
        EnumChatFormatting.DARK_PURPLE.toString(), // UEV, 10
        EnumChatFormatting.DARK_BLUE.toString() + EnumChatFormatting.BOLD, // UIV, 11
        EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // UMV, 12
        EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // UXV, 13
        EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // MAX, 14
        EnumChatFormatting.OBFUSCATED.toString() // ~~~, 15
    };

    /**
     * This way it is possible to have a Call Hierarchy of NullPointers in ItemStack based Functions, and also because
     * most of the time I don't know what kind of Data Type the "null" stands for
     */
    public static final ItemStack NI = null;
    /**
     * This way it is possible to have a Call Hierarchy of NullPointers in FluidStack based Functions, and also because
     * most of the time I don't know what kind of Data Type the "null" stands for
     */
    public static final FluidStack NF = null;
    /**
     * MOD ID Strings, since they are very common Parameters.
     */
    @Deprecated
    public static final String MOD_ID = "gregtech";
    @Deprecated
    public static final String MOD_ID_IC2 = "IC2";
    @Deprecated
    public static final String MOD_ID_NC = "IC2NuclearControl";
    @Deprecated
    public static final String MOD_ID_TC = "Thaumcraft";
    @Deprecated
    public static final String MOD_ID_TF = "TwilightForest";
    @Deprecated
    public static final String MOD_ID_RC = "Railcraft";
    @Deprecated
    public static final String MOD_ID_TE = "ThermalExpansion";
    @Deprecated
    public static final String MOD_ID_AE = "appliedenergistics2";
    @Deprecated
    public static final String MOD_ID_TFC = "terrafirmacraft";
    @Deprecated
    public static final String MOD_ID_PFAA = "PFAAGeologica";
    @Deprecated
    public static final String MOD_ID_FR = "Forestry";
    @Deprecated
    public static final String MOD_ID_HaC = "harvestcraft";
    @Deprecated
    public static final String MOD_ID_APC = "AppleCore";
    @Deprecated
    public static final String MOD_ID_MaCr = "magicalcrops";
    @Deprecated
    public static final String MOD_ID_GaEn = "ganysend";
    @Deprecated
    public static final String MOD_ID_GaSu = "ganyssurface";
    @Deprecated
    public static final String MOD_ID_GaNe = "ganysnether";
    @Deprecated
    public static final String MOD_ID_BC_SILICON = "BuildCraft|Silicon";
    @Deprecated
    public static final String MOD_ID_BC_TRANSPORT = "BuildCraft|Transport";
    @Deprecated
    public static final String MOD_ID_BC_FACTORY = "BuildCraft|Factory";
    @Deprecated
    public static final String MOD_ID_BC_ENERGY = "BuildCraft|Energy";
    @Deprecated
    public static final String MOD_ID_BC_BUILDERS = "BuildCraft|Builders";
    @Deprecated
    public static final String MOD_ID_BC_CORE = "BuildCraft|Core";
    @Deprecated
    public static final String MOD_ID_GC_CORE = "GalacticraftCore";
    @Deprecated
    public static final String MOD_ID_GC_MARS = "GalacticraftMars";
    @Deprecated
    public static final String MOD_ID_GC_PLANETS = "GalacticraftPlanets";
    @Deprecated
    public static final String MOD_ID_DC = "dreamcraft";
    @Deprecated
    public static final String MOD_ID_GTPP = "miscutils";
    /**
     * File Paths and Resource Paths
     */
    @Deprecated
    public static final String TEX_DIR = "textures/";
    @Deprecated
    public static final String TEX_DIR_GUI = TEX_DIR + "gui/";
    @Deprecated
    public static final String TEX_DIR_ITEM = TEX_DIR + "items/";
    @Deprecated
    public static final String TEX_DIR_BLOCK = TEX_DIR + "blocks/";
    @Deprecated
    public static final String TEX_DIR_ENTITY = TEX_DIR + "entity/";
    @Deprecated
    public static final String TEX_DIR_ASPECTS = TEX_DIR + "aspects/";
    @Deprecated
    public static final String RES_PATH = GregTech.getResourcePath(TEX_DIR);
    @Deprecated
    public static final String RES_PATH_GUI = GregTech.getResourcePath("textures", "gui/");
    @Deprecated
    public static final String RES_PATH_ITEM = GregTech.getResourcePath();
    @Deprecated
    public static final String RES_PATH_BLOCK = GregTech.getResourcePath();
    @Deprecated
    public static final String RES_PATH_ENTITY = GregTech.getResourcePath("textures", "entity/");
    @Deprecated
    public static final String RES_PATH_ASPECTS = GregTech.getResourcePath("textures", "aspects/");
    @Deprecated
    public static final String RES_PATH_MODEL = GregTech.getResourcePath("textures", "models/");
    @Deprecated
    public static final String RES_PATH_IC2 = IndustrialCraft2.getResourcePath();

    /**
     * NBT String Keys
     */
    public static final class NBT {

        public static final String COLOR = "gt.color", // Integer
            COVERS = "gt.covers", // String
            CUSTOM_NAME = "name", // String
            DISPAY = "gt.display", // String
            TIER = "gt.tier", // Number
            FACING = "gt.facing", // Byte
            LOCK_UPGRADE = "gt.locked", // Boolean
            MATERIAL = "gt.material", // String containing the Material Name.
            MODE = "gt.mode", // Number
            ALLOWED_MODES = "gt.amode", // Number
            MTE_ID = "gt.mte.id", // Containing the MTE ID
            MTE_REG = "gt.mte.reg", // Containing the MTE Registry ID
            OWNER = "gt.owner", // String
            OWNER_UUID = "gt.ownerUuid", // UUID (String)

            // Machines
            ACTIVE = "gt.active", // Boolean
            FLUID_OUT = "gt.fluidout", // Output Fluid
            ITEM_OUT = "gt.itemout", // Output Item
            PARALLEL = "gt.parallel", // Number
            TANK_CAPACITY = "gt.tankcap", // Number
            TANK_IN = "gt.tank.in.", // FluidStack
            TANK_OUT = "gt.tank.out.", // FluidStack
            TEXTURE = "gt.texture", // String
            INV_INPUT_SIZE = "gt.invsize.in", // Number
            INV_OUTPUT_SIZE = "gt.invsize.out", // Number
            INV_INPUT_LIST = "gt.invlist.in", // NBT List
            INV_OUTPUT_LIST = "gt.invlist.out", // NBT List
            VOLTAGE = "gt.voltage", // Number
            AMPERAGE = "gt.amperage", // Number
            STORED_ENERGY = "gt.stored.energy", // Number
            MAXIMUM_ENERGY = "gt.maximum.energy", // Number
            EUT_CONSUMPTION = "gt.eut.consumption", // Number
            BURN_TIME_LEFT = "gt.burn.time.left", // Number
            TOTAL_BURN_TIME = "gt.total.burn.time", // Number
            ALLOWED_WORK = "gt.allowed.work", // Boolean

            // MultiBlock
            STRUCTURE_OK = "gt.structure.ok", ROTATION = "gt.eRotation", FLIP = "gt.eFlip", TARGET = "gt.target", // Boolean
            TARGET_X = "gt.target.x", // Number
            TARGET_Y = "gt.target.y", // Number
            TARGET_Z = "gt.target.z", // Number
            LOCKED_INVENTORY = "gt.locked.inventory", // String
            LOCKED_INVENTORY_INDEX = "gt.locked.inventory.index", // Number
            UPGRADE_INVENTORY_SIZE = "gt.invsize.upg", // String
            UPGRADE_INVENTORY_UUID = "gt.invuuid.upg", // String
            UPGRADE_INVENTORY_NAME = "gt.invname.upg", // String
            UPGRADE_INVENTORIES_INPUT = "gt.invlist.upg.in", // NBT List
            UPGRADE_INVENTORIES_OUTPUT = "gt.invlist.upg.out", // NBT List
            SEPARATE_INPUTS = "gt.separate.inputs", // Boolean

            // Logic
            POWER_LOGIC = "gt.power.logic", // NBT Tag
            POWER_LOGIC_STORED_ENERGY = "gt.power.logic.stored.energy", // Number
            POWER_LOGIC_ENERGY_CAPACITY = "gt.power.logic.energy.capacity", // Number
            POWER_LOGIC_VOLTAGE = "gt.power.logic.voltage", // Number
            POWER_LOGIC_AMPERAGE = "gt.power.logic.voltage", // Number
            POWER_LOGIC_TYPE = "gt.power.logic.type", // Number
            empty_ = "";
    }

    /** The Color White as RGB Short Array. */
    public static final short[] UNCOLORED_RGBA = { 255, 255, 255, 255 };
    /** The Color White as simple Integer (0x00ffffff). */
    public static final int UNCOLORED = 0x00ffffff;

    /**
     * Sides
     */
    public static final byte SIDE_BOTTOM = 0, SIDE_DOWN = 0, SIDE_TOP = 1, SIDE_UP = 1, SIDE_NORTH = 2, // Also a Side
                                                                                                        // with a
                                                                                                        // stupidly
                                                                                                        // mirrored
                                                                                                        // Texture
        SIDE_SOUTH = 3, SIDE_WEST = 4, SIDE_EAST = 5, // Also a Side with a stupidly mirrored Texture
        SIDE_ANY = 6, SIDE_UNKNOWN = 6, SIDE_INVALID = 6, SIDE_INSIDE = 6, SIDE_UNDEFINED = 6;

    /** Compass alike Array for the proper ordering of North, East, South and West. */
    public static final byte[] COMPASS_DIRECTIONS = { SIDE_NORTH, SIDE_EAST, SIDE_SOUTH, SIDE_WEST };

    /**
     * An Array containing all Sides which follow the Condition, in order to iterate over them for example.
     */
    public static final byte[] ALL_SIDES = { 0, 1, 2, 3, 4, 5, 6 }, ALL_VALID_SIDES = { 0, 1, 2, 3, 4, 5 };

    /**
     * For Facing Checks.
     */
    public static final boolean[] INVALID_SIDES = { false, false, false, false, false, false, true },
        VALID_SIDES = { true, true, true, true, true, true, false };

    /**
     * Side->Offset Mappings.
     * 
     * @deprecated Use {@link ForgeDirection#VALID_DIRECTIONS} {@link ForgeDirection#offsetX}
     */
    @Deprecated
    // spotless:off
    public static final byte[]
            OFFX = {
                (byte) ForgeDirection.DOWN.offsetX,     (byte) ForgeDirection.UP.offsetX,
                (byte) ForgeDirection.NORTH.offsetX,    (byte) ForgeDirection.SOUTH.offsetX,
                (byte) ForgeDirection.WEST.offsetX,     (byte) ForgeDirection.EAST.offsetX,
                (byte) ForgeDirection.UNKNOWN.offsetX },
    /**
     * @deprecated Use {@link ForgeDirection#VALID_DIRECTIONS} {@link ForgeDirection#offsetY}
     */
            OFFY = {
                (byte) ForgeDirection.DOWN.offsetY,     (byte) ForgeDirection.UP.offsetY,
                (byte) ForgeDirection.NORTH.offsetY,    (byte) ForgeDirection.SOUTH.offsetY,
                (byte) ForgeDirection.WEST.offsetY,     (byte) ForgeDirection.EAST.offsetY,
                (byte) ForgeDirection.UNKNOWN.offsetY },
    /**
     * @deprecated Use {@link ForgeDirection#VALID_DIRECTIONS} {@link ForgeDirection#offsetZ}
     */
            OFFZ = {
                (byte) ForgeDirection.DOWN.offsetZ,     (byte) ForgeDirection.UP.offsetZ,
                (byte) ForgeDirection.NORTH.offsetZ,    (byte) ForgeDirection.SOUTH.offsetZ,
                (byte) ForgeDirection.WEST.offsetZ,     (byte) ForgeDirection.EAST.offsetZ,
                (byte) ForgeDirection.UNKNOWN.offsetZ };

    /**
     * Side->Opposite Mappings.
     * 
     * @deprecated Use {@link ForgeDirection#OPPOSITES}
     **/
    public static final byte[] OPOS = {
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.DOWN.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.UP.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.NORTH.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.SOUTH.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.WEST.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.EAST.ordinal()],
            (byte) ForgeDirection.OPPOSITES[ForgeDirection.UNKNOWN.ordinal()] };

    // spotless:off
    /**
     * [Facing,Side]->Side Mappings for Blocks, which don't face up- and downwards. 0 = bottom, 1 = top, 2 = left, 3 =
     * front, 4 = right, 5 = back, 6 = undefined.
     */
    public static final byte[][] FACING_ROTATIONS = {
            { 0, 1, 2, 3, 4, 5, 6 }, // bottom
            { 0, 1, 2, 3, 4, 5, 6 }, // top
            { 0, 1, 3, 5, 4, 2, 6 }, // left
            { 0, 1, 5, 3, 2, 4, 6 }, // front
            { 0, 1, 2, 4, 3, 5, 6 }, // right
            { 0, 1, 4, 2, 5, 3, 6 }, // back
            { 0, 1, 2, 3, 4, 5, 6 }  // undefined
    };
    // spotless:on

    /**
     * The Mod Object itself. That is the GT_Mod-Object. It's needed to open GUI's and similar.
     */
    public static IGT_Mod GT;
    /**
     * Use this Object to add Recipes. (Recipe Adder)
     */
    public static IGT_RecipeAdder RA;
    /**
     * For Internal Usage (Network)
     */
    public static IGT_NetworkHandler NW;
    /**
     * Control percentage of filled 3x3 chunks. Lower number means less oreveins spawn
     */
    public static int oreveinPercentage;
    /**
     * Control number of attempts to find a valid orevein. Generally this maximum limit isn't hit, selecting a vein is
     * cheap
     */
    public static int oreveinAttempts;
    /**
     * Control number of attempts to place a valid ore vein.
     * <p>
     * If a vein wasn't placed due to height restrictions, completely in the water, etc, another attempt is tried.
     * </p>
     */
    public static int oreveinMaxPlacementAttempts;
    /**
     * Whether to place small ores as placer ores for an orevein
     */
    public static boolean oreveinPlacerOres;
    /**
     * Multiplier to control how many placer ores get generated.
     */
    public static int oreveinPlacerOresMultiplier;
    /**
     * Not really Constants, but they set using the Config and therefore should be constant (those are for the Debug
     * Mode)
     */
    public static boolean D1 = false, D2 = false;
    /**
     * Debug parameter for cleanroom testing.
     */
    public static boolean debugCleanroom = false;
    /**
     * Debug parameter for driller testing.
     */
    public static boolean debugDriller = false;
    /**
     * Debug parameter for world generation. Tracks chunks added/removed from run queue.
     */
    public static boolean debugWorldGen = false;
    /**
     * Debug parameter for orevein generation.
     */
    public static boolean debugOrevein = false;
    /**
     * Debug parameter for small ore generation.
     */
    public static boolean debugSmallOres = false;
    /**
     * Debug parameter for stones generation.
     */
    public static boolean debugStones = false;
    /**
     * Debug parameter for single block pump
     */
    public static boolean debugBlockPump = false;
    /**
     * Debug parameter for single block miner
     */
    public static boolean debugBlockMiner = false;
    /**
     * Debug parameter for entity cramming reduction
     */
    public static boolean debugEntityCramming = false;
    /**
     * Debug parameter for {@link gregtech.api.util.GT_ChunkAssociatedData}
     */
    public static boolean debugWorldData = false;
    /**
     * Number of ticks between sending sound packets to clients for electric machines. Default is 1.5 seconds. Trying to
     * mitigate lag and FPS drops.
     */
    public static int ticksBetweenSounds = 30;
    /**
     * If you have to give something a World Parameter but there is no World... (Dummy World)
     */
    public static World DW;

    /**
     * This will prevent NEI from crashing but spams the Log.
     */
    public static boolean allow_broken_recipemap = false;
    /**
     * This will set the percentage how much ReinforcedGlass is Allowed in Cleanroom Walls.
     */
    public static float cleanroomGlass = 5.0f;
    /**
     * This will let machines such as drills and pumps chunkload their work area.
     */
    public static boolean enableChunkloaders = true;
    /**
     * This will make all chunkloading machines act as World Anchors (true) or Passive Anchors (false)
     */
    public static boolean alwaysReloadChunkloaders = false;

    public static boolean debugChunkloaders = false;
    public static boolean cls_enabled;
    public static final Set<String> mCTMEnabledBlock = new HashSet<>();
    public static final Set<String> mCTMDisabledBlock = new HashSet<>();

    public static boolean updateFluidDisplayItems = true;
    public static final int STEAM_PER_WATER = 160;
    /**
     * If true, then digital chest with AE2 storage bus will be accessible only through AE2
     */
    public static boolean disableDigitalChestsExternalAccess = false;

    public static boolean lateConfigSave = true;
    public static boolean worldTickHappened = false;

    public static final int[] emptyIntArray = new int[0];

    public static final IFluidTank[] emptyFluidTank = new IFluidTank[0];
    public static final FluidTankGT[] emptyFluidTankGT = new FluidTankGT[0];
    public static final FluidTankInfo[] emptyFluidTankInfo = new FluidTankInfo[0];
    public static final FluidStack[] emptyFluidStack = new FluidStack[0];
    public static final ItemStack[] emptyItemStackArray = new ItemStack[0];
    public static final IIconContainer[] emptyIconContainerArray = new IIconContainer[3];

    /**
     * Pretty formatting for author names.
     */
    public static final String Colen = "" + EnumChatFormatting.DARK_RED
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "C"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "o"
        + EnumChatFormatting.GREEN
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "l"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "e"
        + EnumChatFormatting.DARK_PURPLE
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "n";

    public static final String AuthorColen = "Author: " + Colen;
    public static final String AuthorKuba = "Author: " + EnumChatFormatting.DARK_RED
        + EnumChatFormatting.BOLD
        + "k"
        + EnumChatFormatting.RED
        + EnumChatFormatting.BOLD
        + "u"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + "b"
        + EnumChatFormatting.YELLOW
        + EnumChatFormatting.BOLD
        + "a"
        + EnumChatFormatting.DARK_GREEN
        + EnumChatFormatting.BOLD
        + "6"
        + EnumChatFormatting.GREEN
        + EnumChatFormatting.BOLD
        + "0"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "0"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + "0";

    public static final String AuthorBlueWeabo = "Author: " + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + "Blue"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "Weabo";

    // 7.5F comes from GT_Tool_Turbine_Large#getBaseDamage() given huge turbines are the most efficient now.
    public static double getMaxPlasmaTurbineEfficiencyFromMaterial(Materials material) {
        return (5F + (7.5F + material.mToolQuality)) / 10.0;
    }

    // Called once in GT_Client on world load, has to be called late so that Materials is populated.
    public static void calculateMaxPlasmaTurbineEfficiency() {

        ArrayList<Double> effArray = new ArrayList<>();

        // Iteration seems to work but need to check turbine as all items appear null.
        for (Materials material : Materials.values()) {
            effArray.add(getMaxPlasmaTurbineEfficiencyFromMaterial(material));
        }

        maxPlasmaTurbineEfficiency = Collections.max(effArray);
    }

    private static double maxPlasmaTurbineEfficiency;

    public static double getMaxPlasmaTurbineEfficiency() {
        return maxPlasmaTurbineEfficiency;
    }

    private static final long[] EXPLOSION_LOOKUP_V = new long[] { V[0], V[1], V[2], V[3], V[4], V[4] * 2, V[5], V[6],
        V[7], V[8], V[8] * 2, V[9], V[10], V[11], V[12], V[12] * 2, V[13], V[14], V[15] };
    private static final float[] EXPLOSION_LOOKUP_POWER = new float[] { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F,
        9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F, 17.0F, 18.0F, 19.0F, 20.0F };

    public static float getExplosionPowerForVoltage(long voltage) {
        for (int i = 0; i < EXPLOSION_LOOKUP_V.length; i++) {
            if (voltage < EXPLOSION_LOOKUP_V[i]) {
                return EXPLOSION_LOOKUP_POWER[i];
            }
        }
        return EXPLOSION_LOOKUP_POWER[EXPLOSION_LOOKUP_POWER.length - 1];
    }
}
