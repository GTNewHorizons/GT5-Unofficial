package gregtech.api.enums;

import static bartworks.util.BWTooltipReference.TT;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.AQUA;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_AQUA;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_PURPLE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.ITALIC;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.LIGHT_PURPLE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.OBFUSCATED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RESET;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.UNDERLINE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.WHITE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.YELLOW;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.animatedText;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.chain;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.text;
import static gregtech.api.util.CustomGlyphs.AIR;
import static gregtech.api.util.CustomGlyphs.CHAOS;
import static gregtech.api.util.CustomGlyphs.EARTH;
import static gregtech.api.util.CustomGlyphs.ORDER;
import static gregtech.api.util.CustomGlyphs.STAR;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.fluid.GTFluidTank;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.internal.IGTRecipeAdder;
import gregtech.api.net.IGT_NetworkHandler;
import gregtech.api.objects.XSTR;
import gregtech.api.util.CustomGlyphs;
import gregtech.api.util.GTChunkAssociatedData;

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
public class GTValues {
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
    public static final int[] B;

    static {
        B = new int[32];
        for (int i = 0; i < B.length; i++) B[i] = 1 << i;
    }

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
     *
     * @deprecated Use {@link gregtech.api.util.GTRecipeBuilder#INGOTS} instead.
     */
    @Deprecated
    public static final long L = 144;

    /**
     * The Item WildCard Tag. Even shorter than the "-1" of the past
     *
     * @deprecated Use {@link gregtech.api.util.GTRecipeBuilder#WILDCARD} instead.
     */
    @Deprecated
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
        "MAX+" // 15
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
        EnumChatFormatting.DARK_GREEN.toString(), // LV, 1
        EnumChatFormatting.GOLD.toString(), // MV, 2
        EnumChatFormatting.YELLOW.toString(), // HV, 3
        EnumChatFormatting.DARK_GRAY.toString(), // EV, 4
        EnumChatFormatting.BLUE.toString(), // IV, 5
        EnumChatFormatting.LIGHT_PURPLE.toString(), // LuV, 6
        EnumChatFormatting.AQUA.toString(), // ZPM, 7
        EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.UNDERLINE, // UV, 8
        EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.UNDERLINE, // UHV, 9
        EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.UNDERLINE, // UEV, 10
        EnumChatFormatting.DARK_BLUE.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // UIV, 11
        EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // UMV, 12
        EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // UXV, 13
        EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE, // MAX, 14
        EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD
            + EnumChatFormatting.UNDERLINE
            + EnumChatFormatting.ITALIC, // MAX+, 15
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
     * NBT String Keys
     */
    public static final class NBT {

        public static final String COVERS = "gt.covers"; // String
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
     */
    public static final byte[] OFFX = { 0, 0, 0, 0, -1, +1, 0 }, OFFY = { -1, +1, 0, 0, 0, 0, 0 },
        OFFZ = { 0, 0, -1, +1, 0, 0, 0 };

    /**
     * Side->Opposite Mappings.
     **/
    public static final byte[] OPOS = { 1, 0, 3, 2, 5, 4, 6 };

    /**
     * [Facing,Side]->Side Mappings for Blocks, which don't face up- and downwards. 0 = bottom, 1 = top, 2 = left, 3 =
     * front, 4 = right, 5 = back, 6 = undefined.
     */
    public static final byte[][] FACING_ROTATIONS = { { 0, 1, 2, 3, 4, 5, 6 }, { 0, 1, 2, 3, 4, 5, 6 },
        { 0, 1, 3, 5, 4, 2, 6 }, { 0, 1, 5, 3, 2, 4, 6 }, { 0, 1, 2, 4, 3, 5, 6 }, { 0, 1, 4, 2, 5, 3, 6 },
        { 0, 1, 2, 3, 4, 5, 6 } };

    /**
     * Use this Object to add Recipes. (Recipe Adder)
     */
    public static IGTRecipeAdder RA;
    /**
     * For Internal Usage (Network)
     */
    public static IGT_NetworkHandler NW;
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
     * Debug parameter for world generation. Measures amount of time taken to generate oreveins, stone, etc.
     */
    public static boolean profileWorldGen = false;
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
     * Debug parameter for {@link GTChunkAssociatedData}
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
     * This will set the blacklist for the world accelerator in TE mode.
     */
    public static String[] blacklistedTileEntiyClassNamesForWA = new String[] {
        "com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry",
        "advsolar.common.tiles.TileEntityUltimateSolarPanel", "advsolar.common.tiles.TileEntitySolarPanel",
        "advsolar.common.tiles.TileEntityQuantumSolarPanel", "advsolar.common.tiles.TileEntityHybridSolarPanel",
        "advsolar.common.tiles.TileEntityAdvancedSolarPanel", "com.supsolpans.tiles.TileAdminSolarPanel",
        "com.supsolpans.tiles.TilePhotonicSolarPanel", "com.supsolpans.tiles.TileSingularSolarPanel",
        "com.supsolpans.tiles.TileSpectralSolarPanel", "emt.tile.solar.air.TileEntityAirSolar",
        "emt.tile.solar.air.TileEntityDoubleAirSolar", "emt.tile.solar.air.TileEntityTripleAirSolar",
        "emt.tile.solar.air.TileEntityQuadrupleAirSolar", "emt.tile.solar.air.TileEntityQuintupleAirSolar",
        "emt.tile.solar.air.TileEntitySextupleAirSolar", "emt.tile.solar.air.TileEntitySeptupleAirSolar",
        "emt.tile.solar.air.TileEntityOctupleAirSolar", "emt.tile.solar.compressed.TileEntityCompressedSolar",
        "emt.tile.solar.compressed.TileEntityDoubleCompressedSolar",
        "emt.tile.solar.compressed.TileEntityTripleCompressedSolar",
        "emt.tile.solar.compressed.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.compressed.TileEntityQuintupleAirSolar", "emt.tile.solar.compressed.TileEntitySextupleAirSolar",
        "emt.tile.solar.compressed.TileEntitySeptupleAirSolar", "emt.tile.solar.compressed.TileEntityOctupleAirSolar",
        "emt.tile.solar.dark.TileEntityDarkSolar", "emt.tile.solar.dark.TileEntityDoubleDarkSolar",
        "emt.tile.solar.dark.TileEntityTripleDarkSolar", "emt.tile.solar.dark.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.dark.TileEntityQuintupleAirSolar", "emt.tile.solar.dark.TileEntitySextupleAirSolar",
        "emt.tile.solar.dark.TileEntitySeptupleAirSolar", "emt.tile.solar.dark.TileEntityOctupleAirSolar",
        "emt.tile.solar.earth.TileEntityDoubleEarthSolar", "emt.tile.solar.earth.TileEntityEarthSolar",
        "emt.tile.solar.earth.TileEntityTripleEarthSolar", "emt.tile.solar.earth.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.earth.TileEntityQuintupleAirSolar", "emt.tile.solar.earth.TileEntitySextupleAirSolar",
        "emt.tile.solar.earth.TileEntitySeptupleAirSolar", "emt.tile.solar.earth.TileEntityOctupleAirSolar",
        "emt.tile.solar.fire.TileEntityDoubleFireSolar", "emt.tile.solar.fire.TileEntityFireSolar",
        "emt.tile.solar.fire.TileEntityTripleFireSolar", "emt.tile.solar.fire.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.fire.TileEntityQuintupleAirSolar", "emt.tile.solar.fire.TileEntitySextupleAirSolar",
        "emt.tile.solar.fire.TileEntitySeptupleAirSolar", "emt.tile.solar.fire.TileEntityOctupleAirSolar",
        "emt.tile.solar.order.TileEntityDoubleOrderSolar", "emt.tile.solar.order.TileEntityOrderSolar",
        "emt.tile.solar.order.TileEntityTripleOrderSolar", "emt.tile.solar.order.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.order.TileEntityQuintupleAirSolar", "emt.tile.solar.order.TileEntitySextupleAirSolar",
        "emt.tile.solar.order.TileEntitySeptupleAirSolar", "emt.tile.solar.order.TileEntityOctupleAirSolar",
        "emt.tile.solar.water.TileEntityDoubleWaterSolar", "emt.tile.solar.water.TileEntityTripleWaterSolar",
        "emt.tile.solar.water.TileEntityWaterSolar", "emt.tile.solar.water.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.water.TileEntityQuintupleAirSolar", "emt.tile.solar.water.TileEntitySextupleAirSolar",
        "emt.tile.solar.water.TileEntitySeptupleAirSolar", "emt.tile.solar.water.TileEntityOctupleAirSolar",
        "com.lulan.compactkineticgenerators.tileentity.TileCkgE",
        "com.lulan.compactkineticgenerators.tileentity.TileCkgH",
        "com.lulan.compactkineticgenerators.tileentity.TileCkgL",
        "com.lulan.compactkineticgenerators.tileentity.TileCkgM",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwaE",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwaH",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwaL",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwaM",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwmE",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwmH",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwmL",
        "com.lulan.compactkineticgenerators.tileentity.TileCkwmM", "com.supsolpans.tiles.TileSpectralSolarPanel",
        "com.supsolpans.tiles.TileSingularSolarPanel", "com.supsolpans.tiles.TileAdminSolarPanel",
        "com.supsolpans.tiles.TilePhotonicSolarPanel", "gtPlusPlus.core.tileentities.general.TileEntityFishTrap",
        "gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest",
        "net.bdew.gendustry.machines.apiary.TileApiary", "goodgenerator.blocks.tileEntity.EssentiaHatch",
        "magicbees.tileentity.TileEntityApimancersDrainerCommon",
        "magicbees.tileentity.TileEntityApimancersDrainerGT" };
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
    public static final Set<String> mCTMEnabledBlock = new HashSet<>();
    public static final Set<String> mCTMDisabledBlock = new HashSet<>();

    public static final int STEAM_PER_WATER = 160;
    /**
     * If true, then digital chest with AE2 storage bus will be accessible only through AE2
     */
    public static boolean disableDigitalChestsExternalAccess = false;

    public static boolean lateConfigSave = true;
    public static boolean worldTickHappened = false;

    public static final int[] emptyIntArray = new int[0];
    public static final long[] emptyLongArray = new long[0];

    public static final IFluidTank[] emptyFluidTank = new IFluidTank[0];
    public static final GTFluidTank[] emptyFluidTankGT = new GTFluidTank[0];
    public static final FluidTankInfo[] emptyFluidTankInfo = new FluidTankInfo[0];
    public static final FluidStack[] emptyFluidStackArray = new FluidStack[0];
    public static final ItemStack[] emptyItemStackArray = new ItemStack[0];
    public static final String[] emptyStringArray = new String[0];
    public static final Object[] emptyObjectArray = new Object[0];
    public static final IIconContainer[] emptyIconContainerArray = new IIconContainer[3];
    @SuppressWarnings("rawtypes")
    public static final Iterator[] EMPTY_ITERATOR_ARRAY = new Iterator[0];

    /**
     * Detects if we're in a deobfuscated environment, meaning that additional sanity checks should be ran. If the
     * blackboard is null, we're in a unit test that hasn't set its env up properly and also want those checks to be
     * ran.
     */
    public static boolean DEVENV = Launch.blackboard == null ? true
        : (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

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

    public static final String AuthorPxx500 = "Author: " + EnumChatFormatting.DARK_BLUE
        + EnumChatFormatting.BOLD
        + "Pxx500";

    public static final String AuthorBlueWeabo = "Author: " + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + "Blue"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "Weabo";

    public static final String Authorguid118 = "Author: " + EnumChatFormatting.WHITE
        + EnumChatFormatting.BOLD
        + "gu"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "id"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + "118";

    public static final String Authorminecraft7771 = "Author: " + EnumChatFormatting.BLUE
        + EnumChatFormatting.LIGHT_PURPLE
        + "minecraft7771";

    public static final Supplier<String> AuthorCloud = chain(
        text("Author: " + EnumChatFormatting.AQUA + EnumChatFormatting.BOLD),
        animatedText(
            "C",
            1,
            500,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + "\u238B" + RESET + AQUA + BOLD,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + "\u0B83" + RESET + AQUA + BOLD,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + BOLD + "\u29BC" + RESET + AQUA + BOLD),
        text(EnumChatFormatting.AQUA + EnumChatFormatting.BOLD.toString() + "loud" + EnumChatFormatting.RESET),
        animatedText(
            " ",
            1,
            500,
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + "\u238B",
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + "\u0B83",
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + BOLD + "\u29BC"));

    public static final String AuthorQuerns = "Author: " + EnumChatFormatting.RED + "Querns";
    public static final String AuthorSilverMoon = "Author: " + EnumChatFormatting.AQUA + "SilverMoon";
    public static final String AuthorTheEpicGamer274 = "Author: " + EnumChatFormatting.DARK_AQUA + "TheEpicGamer274";
    public static final String Ollie = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Ollie";
    public static final String authorBaps = "Author: " + EnumChatFormatting.GOLD
        + "Ba"
        + EnumChatFormatting.LIGHT_PURPLE
        + "ps";
    public static final String AuthorOmdaCZ = "Author: " + EnumChatFormatting.BLUE
        + "Omda"
        + EnumChatFormatting.RED
        + "CZ";

    public static final String AuthorEvgenWarGold = "" + EnumChatFormatting.RED
        + EnumChatFormatting.BOLD
        + "Evgen"
        + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + "War"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + "Gold";
    public static final String AuthorVolence = "Author: " + EnumChatFormatting.AQUA + "Volence";

    public static final String AuthorEigenRaven = "Author: " + EnumChatFormatting.DARK_PURPLE
        + "Eigen"
        + EnumChatFormatting.BOLD
        + "Raven";

    public static final String AuthorPineapple = "Author: " + EnumChatFormatting.BLUE + "Recursive Pineapple";

    public static final Supplier<String> AuthorNoc = chain(
        animatedText(
            "Noc",
            0,
            500,
            EnumChatFormatting.GOLD + BOLD,
            EnumChatFormatting.DARK_GREEN + BOLD,
            EnumChatFormatting.GOLD + BOLD,
            EnumChatFormatting.DARK_GREEN + BOLD,
            EnumChatFormatting.DARK_GREEN + OBFUSCATED + BOLD));

    public static final String AuthorJulia =
        // spotless:off
        EnumChatFormatting.BOLD.toString() +
        EnumChatFormatting.GOLD            + CustomGlyphs.SPARKLES +
        EnumChatFormatting.AQUA            + "J"                   +
        EnumChatFormatting.LIGHT_PURPLE    + "u"                   +
        EnumChatFormatting.WHITE           + "l"                   +
        EnumChatFormatting.LIGHT_PURPLE    + "i"                   +
        EnumChatFormatting.AQUA            + "a"                   +
        EnumChatFormatting.GOLD            + CustomGlyphs.SPARKLES ;
        // spotless:on

    public static final String TecTechHatches = "Supports " + TT + " laser and multi-amp hatches";

    public static final String StandalonePureBluez = EnumChatFormatting.WHITE + "Pure"
        + EnumChatFormatting.AQUA
        + "B"
        + EnumChatFormatting.DARK_AQUA
        + "l"
        + EnumChatFormatting.BLUE
        + "u"
        + EnumChatFormatting.DARK_BLUE
        + "ez";

    public static final String AuthorPureBluez = "Author: " + StandalonePureBluez;

    // for use with the chain
    public static final Supplier<String> AUTHOR_SUPPLIER = () -> "Author: ";
    public static final Supplier<String> AUTHORS_SUPPLIER = () -> "Authors: ";
    public static final Supplier<String> AND_SUPPLIER = () -> EnumChatFormatting.RESET + " & ";

    // a list specifically for random selection of formatting codes.
    public static final String[] formattingCodes = new String[] { DARK_GREEN, DARK_AQUA, DARK_PURPLE, GOLD, BLUE, GREEN,
        AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, OBFUSCATED, UNDERLINE };

    public static final Supplier<String> fancyAuthorChrom = chain(
        createChromLetter("C", ORDER),
        createChromLetter("h", EARTH),
        createChromLetter("r", CHAOS),
        createChromLetter("o", AIR),
        createChromLetter("m", STAR));

    public static final Supplier<String> AuthorThree = chain(
        animatedText(
            "Three",
            0,
            1000,
            EnumChatFormatting.BLUE + BOLD,
            EnumChatFormatting.RED + BOLD,
            EnumChatFormatting.YELLOW + BOLD));

    private static Supplier<String> createChromLetter(String letter, String... injectedUnicode) {

        XSTR random = XSTR.XSTR_INSTANCE;
        // calculates the amount of cycles
        int length = 4 + injectedUnicode.length * 2;
        String[] colorList = new String[length];

        int currentUnicodeIndex = 0;
        for (int i = 0; i < colorList.length; i++) {
            StringBuilder builder = new StringBuilder();
            int prependedFormattingCodes = 1 + random.nextInt(2);
            for (int codeStep = 0; codeStep < prependedFormattingCodes; codeStep++) {
                // adds fun formatting codes
                int randIndex = random.nextInt(formattingCodes.length);
                builder.append(formattingCodes[randIndex]);
            }
            // checks if its the correct positon to insert a special unicode character, injects if so, otherwise adds
            // the letter
            if (currentUnicodeIndex < injectedUnicode.length && ((i + 1) % injectedUnicode.length == 0)) {
                builder.append(injectedUnicode[currentUnicodeIndex]);
                currentUnicodeIndex += 1;
            } else {
                builder.append(letter);
            }
            colorList[i] = builder.toString();
        }
        return emptyAnimatedText(1, 1000, colorList);
    }

    // special version of the animated text that strips the return value of spaces, don't bother using this elsewhere
    private static Supplier<String> emptyAnimatedText(int posstep, int delay, String... formattingArray) {
        String text = " ";
        if (text == null || formattingArray == null || formattingArray.length == 0) return () -> "";

        final int finalDelay = Math.max(delay, 1);
        final int finalPosstep = Math.max(posstep, 0);

        return () -> {
            StringBuilder sb = new StringBuilder(text.length() * 3);
            int offset = (int) ((System.currentTimeMillis() / finalDelay) % formattingArray.length);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int indexColorArray = (i * finalPosstep + formattingArray.length - offset) % formattingArray.length;
                sb.append(formattingArray[indexColorArray]);
                sb.append(c);
            }
            return sb.toString()
                .replaceAll("\\s", "");
        };
    }

    public static final Supplier<String> AuthorAuynonymous = chain(
        createAuynonymousLetter(0),
        createAuynonymousLetter(1),
        createAuynonymousLetter(2),
        createAuynonymousLetter(3),
        createAuynonymousLetter(4),
        createAuynonymousLetter(5),
        createAuynonymousLetter(6),
        createAuynonymousLetter(7),
        createAuynonymousLetter(8),
        createAuynonymousLetter(9),
        createAuynonymousLetter(10));

    private static Supplier<String> createAuynonymousLetter(int index) {
        final String[] letters = new String[] { "A", "u", "y", "n", "o", "n", "y", "m", "o", "u", "s" };
        String[] colorList = new String[letters.length];
        final String letter = letters[index];
        for (int i = 0; i < letters.length; i++) {
            colorList[i] = LIGHT_PURPLE
                + (i == (letters.length - index - 1) ? EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + "<3"
                    : letter);
        }
        return emptyAnimatedText(1, 1000, colorList);
    }

    public static final String StandaloneNotAPenguin = EnumChatFormatting.WHITE + "Not"
        + EnumChatFormatting.AQUA
        + "APenguin";

    public static final String AuthorNotAPenguinStatic = "Author: " + EnumChatFormatting.BOLD + StandaloneNotAPenguin;

    public static final Supplier<String> AuthorNotAPenguinAnimated = chain(
        createNotAPenguinLetter(0),
        createNotAPenguinLetter(1),
        createNotAPenguinLetter(2),
        createNotAPenguinLetter(3),
        createNotAPenguinLetter(4),
        createNotAPenguinLetter(5),
        createNotAPenguinLetter(6),
        createNotAPenguinLetter(7),
        createNotAPenguinLetter(8),
        createNotAPenguinLetter(9),
        createNotAPenguinLetter(10));

    private static Supplier<String> createNotAPenguinLetter(int index) {
        final String[] letters = new String[] { "N", "o", "t", "A", "P", "e", "n", "g", "u", "i", "n" };
        String[] colorList = new String[letters.length];
        for (int i = 0; i < letters.length; ++i) {
            int[] whiteIndices = new int[] { (letters.length - index - 1 + letters.length) % letters.length,
                (letters.length - index + letters.length) % letters.length,
                (letters.length - index + 1 + letters.length) % letters.length };
            if (i == whiteIndices[0] || i == whiteIndices[1] || i == whiteIndices[2]) {
                colorList[i] = WHITE + ITALIC + letters[index];
            } else colorList[i] = AQUA + letters[index];
        }
        return emptyAnimatedText(1, 100, colorList);
    }

    public static final Supplier<String> AuthorSerenibyss = chain(
        getAuthorSerenibyssLetter("S", 3, LIGHT_PURPLE, 11, WHITE, 25, AQUA),
        getAuthorSerenibyssLetter("e", 12, AQUA, 18, LIGHT_PURPLE, 29, WHITE),
        getAuthorSerenibyssLetter("r", 0, WHITE, 10, LIGHT_PURPLE, 20, AQUA),
        getAuthorSerenibyssLetter("e", 9, LIGHT_PURPLE, 17, AQUA, 22, WHITE),
        getAuthorSerenibyssLetter("n", 6, WHITE, 14, AQUA, 27, LIGHT_PURPLE),
        getAuthorSerenibyssLetter("i", 1, AQUA, 15, WHITE, 21, LIGHT_PURPLE),
        getAuthorSerenibyssLetter("b", 13, WHITE, 19, LIGHT_PURPLE, 23, WHITE),
        getAuthorSerenibyssLetter("y", 2, AQUA, 8, LIGHT_PURPLE, 24, WHITE),
        getAuthorSerenibyssLetter("s", 5, AQUA, 16, WHITE, 26, LIGHT_PURPLE),
        getAuthorSerenibyssLetter("s", 4, LIGHT_PURPLE, 7, WHITE, 28, AQUA));

    private static Supplier<String> getAuthorSerenibyssLetter(String letter, Object... switchParams) {
        int[] switchIntervals = new int[switchParams.length / 2];
        String[] colors = new String[switchParams.length / 2];
        for (int i = 0; i < switchParams.length; i += 2) {
            switchIntervals[i / 2] = (int) switchParams[i];
            colors[i / 2] = (String) switchParams[i + 1];
        }

        String[] colorAlternator = new String[30];
        int index = switchIntervals[0];
        int switchIndex = 0;
        boolean obfuscated = false;
        do {
            String color;
            if (ArrayUtils.contains(switchIntervals, index)) {
                obfuscated = true;
                color = colors[switchIndex] + OBFUSCATED;
            } else if (obfuscated) {
                obfuscated = false;
                switchIndex++;
                if (switchIndex == colors.length) switchIndex = 0;
                color = colors[switchIndex];
            } else {
                color = colors[switchIndex];
            }
            colorAlternator[index] = color;
            index++;
            if (index == 30) index = 0;
        } while (index != switchIntervals[0]);

        return animatedText(letter, 1, 250, colorAlternator);
    }

    public static final String AuthorJL2210 = "" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "JL2210";

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

    public static String getLocalizedLongVoltageName(int voltage) {
        if (voltage >= VOLTAGE_NAMES.length) {
            return StatCollector.translateToLocal("GT5U.voltage_names.error_voltage_report_this");
        }
        String unlocalizedName = "GT5U.voltage_names." + VOLTAGE_NAMES[voltage].toLowerCase()
            .replace(",", "")
            .replace(' ', '_');
        if (StatCollector.canTranslate(unlocalizedName)) {
            return StatCollector.translateToLocal(unlocalizedName);
        }
        return StatCollector.translateToLocal("GT5U.voltage_names.error_voltage_report_this");
    }

    public static <T> Iterator<T>[] emptyIteratorArray() {
        // noinspection unchecked
        return EMPTY_ITERATOR_ARRAY;
    }
}
