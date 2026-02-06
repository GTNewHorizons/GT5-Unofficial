package gregtech.api;

import static gregtech.api.enums.GTValues.B;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.objects.GTHashSet;
import gregtech.api.objects.GTItemStack;
import gregtech.api.threads.RunnableCableUpdate;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.CircuitryBehavior;
import gregtech.api.util.GTCreativeTab;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.world.GTWorldgen;
import gregtech.common.GTDummyWorld;
import gregtech.common.covers.CoverPosition;

/**
 * Please do not include this File in your Mod-download as it ruins compatibility, like with the IC2-API You may just
 * copy those Functions into your Code, or better call them via reflection.
 * <p/>
 * The whole API is the basic construct of my Mod. Everything is dependent on it. I change things quite often so please
 * don't include any File inside your Mod, even if it is an Interface. Since some Authors were stupid enough to break
 * this simple Rule, I added Version checks to enforce it.
 * <p/>
 * In these Folders are many useful Functions. You can use them via reflection if you want. I know not everything is
 * compilable due to APIs of other Mods, but these are easy to fix in your Setup.
 * <p/>
 * You can use this to learn about Modding, but I would recommend simpler Mods. You may even copy-paste Code from these
 * API-Files into your Mod, as I have nothing against that, but you should look exactly at what you are copying.
 *
 * @author Gregorius Techneticies
 */
@SuppressWarnings("unused") // API class has legitimately unused methods and members
public class GregTechAPI {

    /**
     * The MetaTileEntity-ID-List-Length
     */
    public static final short MAXIMUM_METATILE_IDS = Short.MAX_VALUE - 1;
    /**
     * My Creative Tab
     */
    public static final CreativeTabs TAB_GREGTECH = new GTCreativeTab("Main", "Main"),
        TAB_GREGTECH_MATERIALS = new GTCreativeTab("Materials", "Materials"),
        TAG_GREGTECH_CASINGS = new GTCreativeTab("Casings", "Casings") {

            @Override
            public ItemStack getIconItemStack() {
                return ItemList.Casing_RobustTungstenSteel.get(1);
            }
        }, TAB_GREGTECH_ORES = new GTCreativeTab("Ores", "Ores");

    public static final IMetaTileEntity[] METATILEENTITIES = new IMetaTileEntity[MAXIMUM_METATILE_IDS];

    /**
     * The List of Circuit Behaviors for the Redstone Circuit Block
     */
    public static final Map<Integer, CircuitryBehavior> sCircuitryBehaviors = new ConcurrentHashMap<>();
    /**
     * The List of Blocks, which can conduct Machine Block Updates
     */
    public static final Map<Block, Integer> sMachineIDs = new ConcurrentHashMap<>();
    /**
     * The Redstone Frequencies
     */
    public static final Map<Integer, Byte> sWirelessRedstone = new ConcurrentHashMap<>();
    /**
     * The Advanced Redstone Frequencies
     */
    public static final Map<String, Map<String, Map<CoverPosition, Byte>>> sAdvancedWirelessRedstone = new ConcurrentHashMap<>();

    /**
     * The IDSU Frequencies
     */
    public static final Map<Integer, Integer> sIDSUList = new ConcurrentHashMap<>();
    /**
     * A List of all Books, which were created using @GT_Utility.getWrittenBook the original Title is the Key Value
     */
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();
    /**
     * The List of Tools, which can be used. Accepts regular damageable Items and Electric Items
     */
    public static final GTHashSet sToolList = new GTHashSet(), sCrowbarList = new GTHashSet(),
        sScrewdriverList = new GTHashSet(), sWrenchList = new GTHashSet(), sSoftMalletList = new GTHashSet(),
        sHardHammerList = new GTHashSet(), sWireCutterList = new GTHashSet(), sSolderingToolList = new GTHashSet(),
        sSolderingMetalList = new GTHashSet(), sJackhammerList = new GTHashSet();

    /**
     * The List of Dimensions, which are Whitelisted for the Teleporter. This list should not contain other Planets.
     * Mystcraft Dimensions and other Dimensional Things should be allowed. Mystcraft and Twilight Forest are
     * automatically considered a Dimension, without being in this List.
     */
    public static final Collection<Integer> sDimensionalList = new HashSet<>();
    /**
     * Lists of all the active World generation Features, these are getting Initialized in Postload!
     */
    public static final List<GTWorldgen> sWorldgenList = new ArrayList<>();
    /**
     * A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items.
     */
    public static final Materials[] sGeneratedMaterials = new Materials[1000];
    /**
     * For the API Version check
     */
    public static volatile int VERSION = 509;

    /**
     * Registers Aspects to Thaumcraft. This Object might be {@code null} if Thaumcraft isn't installed.
     */
    public static IThaumcraftCompat sThaumcraftCompat;
    /**
     * The Lists below are executed at their respective timings. Useful to do things at a particular moment in time. The
     * Lists are not Threaded - a native Java interface is used for their execution. Add your "commands" in the
     * constructor or in the static-code-block of your mod's Main class. Implement the method {@code run()}, and
     * everything should work.
     */
    public static List<Runnable> sBeforeGTPreload = new ArrayList<>(), sAfterGTPreload = new ArrayList<>(),
        sBeforeGTLoad = new ArrayList<>(), sAfterGTLoad = new ArrayList<>(), sBeforeGTPostload = new ArrayList<>(),
        sAfterGTPostload = new ArrayList<>(), sFirstWorldTick = new ArrayList<>(),
        sBeforeGTServerstart = new ArrayList<>(), sAfterGTServerstart = new ArrayList<>(),
        sBeforeGTServerstop = new ArrayList<>(), sAfterGTServerstop = new ArrayList<>(),
        sGTBlockIconload = new ArrayList<>(), sGTItemIconload = new ArrayList<>(), sGTCompleteLoad = new ArrayList<>();
    /**
     * The Icon Registers from Blocks and Items. They will get set right before the corresponding Icon Load Phase as
     * executed in the Runnable List above.
     */
    @SideOnly(Side.CLIENT)
    public static IIconRegister sBlockIcons, sItemIcons;

    public static int TICKS_FOR_LAG_AVERAGING = 25, MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = 100;
    /**
     * Initialized by the Block creation.
     */
    public static Block sBlockMachines;

    public static Block sBlockOres1,
        /* sBlockGem, */
        sBlockMetal1, sBlockMetal2, sBlockMetal3, sBlockMetal4, sBlockMetal5, sBlockMetal6, sBlockMetal7, sBlockMetal8,
        sBlockMetal9, sBlockGem1, sBlockGem2, sBlockGem3, sBlockReinforced, sBlockSheetmetalGT, sBlockSheetmetalBW;
    public static Block sBlockGranites, sBlockConcretes, sBlockStones;
    public static Block sBlockCasings1, sBlockCasings2, sBlockCasings3, sBlockCasings4, sBlockCasings5, sBlockCasings6,
        sBlockCasings8, sBlockCasings9, sBlockCasings10, sBlockCasings11, sBlockCasings12, sBlockCasings13,
        sSolenoidCoilCasings, sBlockCasingsNH, sBlockCasingsFoundry, sBlockCasingsSE, sBlockCasingsSEMotor,
        sBlockCasingsDyson, sBlockCasingsSiphon;
    public static Block sBlockLongDistancePipes;
    public static Block sDroneRender;
    public static Block sBlockFrames;
    public static Block sBlockGlass1;
    public static Block sBlockTintedGlass;
    public static Block sLaserRender;
    public static Block sWormholeRender;
    public static Block sBlackholeRender;
    public static Block sSpaceElevatorCable;
    public static Block nanoForgeRender;
    /**
     * Getting assigned by the Config
     */
    public static boolean sTimber = true, sDrinksAlwaysDrinkable = false, sMultiThreadedSounds = false,
        sDoShowAllItemsInCreative = false, sColoredGUI = true, sMachineMetalGUI = false, sMachineExplosions = true,
        sMachineFlammable = true, sMachineNonWrenchExplosions = true, sMachineRainExplosions = true,
        sMachineThunderExplosions = true, sMachineFireExplosions = true, sMachineWireFire = true, mOutputRF = false,
        mInputRF = false, mRFExplosions = false, mServerStarted = false;

    public static int mEUtoRF = 360, mRFtoEU = 20;

    public static boolean mUseOnlyGoodSolderingMaterials = false;

    private static final String aTextIC2Lower = IndustrialCraft2.ID.toLowerCase(Locale.ENGLISH);
    /**
     * Getting assigned by the Mod loading
     */
    public static boolean sUnificationEntriesRegistered = false, sPreloadStarted = false, sPreloadFinished = false,
        sLoadStarted = false, sLoadFinished = false, sPostloadStarted = false, sPostloadFinished = false,
        sFullLoadFinished = false;

    @SuppressWarnings("unchecked")
    private static final IntFunction<TileEntity>[] teCreators = new IntFunction[16];

    private static final Set<Class<?>> dummyWorlds = new HashSet<>();

    static {
        dummyWorlds.add(GTDummyWorld.class);
    }

    public static void addDummyWorld(Class<?> clazz) {
        dummyWorlds.add(clazz);
    }

    public static boolean isDummyWorld(@Nonnull World w) {
        return dummyWorlds.contains(w.getClass());
    }

    /**
     * You want OreDict-Unification for YOUR Mod/Addon, when GregTech is installed? This Function is especially for YOU.
     * Call this Function after the load-Phase, as I register the most of the Unification at that Phase (Redpowers
     * Storageblocks are registered at postload). A recommended use of this Function is inside your Recipe-System itself
     * (if you have one), as the unification then makes 100% sure, that every added non-unificated Output gets
     * automatically unificated.
     * <p/>
     * I will personally make sure, that only common prefixes of Ores get registered at the Unificator, as of now there
     * are: pulp, dust, dustSmall, ingot, nugget, gem, ore and block If another Mod-Author messes these up, then it's
     * not my fault, and it's especially not your fault. As these are commonly used prefixes.
     * <p/>
     * This Unificator-API-Function uses the same Functions I use, for unificating Items. So if there is something
     * messed up (very unlikely), then everything is messed up.
     * <p/>
     * You shouldn't use this to unificate the Inputs of your Recipes, this is only meant for the Outputs.
     *
     * @param aOreStack the Stack you want to get unificated. It is stackSize Sensitive.
     * @return Either an unificated Stack or the stack you toss in, but it should never be null, unless you throw a
     *         Null-Pointer into it.
     */
    public static ItemStack getUnificatedOreDictStack(ItemStack aOreStack) {
        if (!GregTechAPI.sPreloadFinished) GTLog.err.println(
            "GregTechAPI ERROR: " + aOreStack.getItem()
                + "."
                + aOreStack.getItemDamage()
                + " - OreDict Unification Entries are not registered now, please call it in the postload phase.");
        return GTOreDictUnificator.get(true, aOreStack);
    }

    /**
     * Causes a Machineblock Update This update will cause surrounding MultiBlock Machines to update their
     * Configuration. You should call this Function in @Block.breakBlock and in @Block.onBlockAdded of your Machine.
     *
     * @param aWorld is being the World
     * @param aX     is the X-Coord of the update causing Block
     * @param aY     is the Y-Coord of the update causing Block
     * @param aZ     is the Z-Coord of the update causing Block
     */
    public static boolean causeMachineUpdate(World aWorld, int aX, int aY, int aZ) {
        if (aWorld != null && !aWorld.isRemote && !isDummyWorld(aWorld)) { // World might be null during World-gen
            RunnableMachineUpdate.setMachineUpdateValues(aWorld, aX, aY, aZ);
            return true;
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue") // Retains API method signature
    public static boolean causeCableUpdate(World aWorld, int aX, int aY, int aZ) {
        if (aWorld == null || aWorld.isRemote || isDummyWorld(aWorld)) {
            return false;
        } // World might be null during World-gen
        RunnableCableUpdate.setCableUpdateValues(aWorld, aX, aY, aZ);
        return true;
    }

    /**
     * Adds a Multi-Machine Block, like my Machine Casings for example. You should call @causeMachineUpdate
     * in @Block.breakBlock and in {@link Block#onBlockAdded} of your registered Block. You don't need to register
     * TileEntities which implement {@link IMachineBlockUpdateable}
     *
     * @param aBlock the Block
     * @param aMeta  the Metadata of the Blocks as Bitmask! -1 or ~0 for all Meta-values
     */
    @SuppressWarnings("UnusedReturnValue") // Retains API method signature
    public static boolean registerMachineBlock(Block aBlock, int aMeta) {
        if (aBlock == null) return false;
        if (GregTechAPI.sThaumcraftCompat != null)
            GregTechAPI.sThaumcraftCompat.registerPortholeBlacklistedBlock(aBlock);
        sMachineIDs.put(aBlock, aMeta);
        return true;
    }

    /**
     * Like above but with boolean Parameters instead of a BitMask
     */
    public static boolean registerMachineBlock(Block aBlock, boolean... aMeta) {
        if (aBlock == null || aMeta == null || aMeta.length == 0) return false;
        if (GregTechAPI.sThaumcraftCompat != null)
            GregTechAPI.sThaumcraftCompat.registerPortholeBlacklistedBlock(aBlock);
        int rMeta = 0;
        for (byte i = 0; i < aMeta.length && i < 16; i++) if (aMeta[i]) rMeta |= B[i];
        sMachineIDs.put(aBlock, rMeta);
        return true;
    }

    /**
     * if this Block is a Machine Update Conducting Block
     */
    public static boolean isMachineBlock(Block aBlock, int aMeta) {
        if (aBlock != null) {
            Integer id = sMachineIDs.get(aBlock);
            if (id != null) {
                if (id == -1) // for all-meta registrations, also with meta > 32
                    return true;
                return (id & B[aMeta]) != 0;
            }
        }
        return false;
    }

    /**
     * Provides a new BaseMetaTileEntity. Because some interfaces are not always loaded (Buildcraft, Universal
     * Electricity) we have to use invocation at the constructor of the BaseMetaTileEntity.
     */
    public static BaseMetaTileEntity constructBaseMetaTileEntity() {
        try {
            return new BaseMetaTileEntity();
        } catch (Exception e) {
            GTLog.err.println("GTMod: Fatal Error occurred while initializing TileEntities, crashing Minecraft.");
            e.printStackTrace(GTLog.err);
            throw new RuntimeException(e);
        }
    }

    /**
     * Register a Wrench to be usable on GregTech Machines. The Wrench MUST have some kind of Durability unlike certain
     * Buildcraft Wrenches.
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     * <p/>
     * -----
     * <p/>
     * Returning true at isDamageable was a great Idea, KingLemming. Well played. Since the OmniWrench is just a
     * Single-Item-Mod, people can choose if they want your infinite durability or not. So that's not really a Problem.
     * I even have a new Config to auto-disable most infinite BC Wrenches (but that one is turned off).
     * <p/>
     * One last Bug for you to fix: My Auto-registration detects Railcraft's Crowbars, Buildcraft's Wrenches and alike,
     * due to their Interfaces. Guess what now became a Crowbar by accident. Try registering the Wrench at the load
     * phase to prevent things like that from happening. Yes, I know that "You need to register Tools in the Load
     * Phase"-Part wasn't there before this. Sorry about that.
     */
    public static boolean registerWrench(ItemStack aTool) {
        return registerTool(aTool, sWrenchList);
    }

    /**
     * Register a Crowbar to extract Covers from Machines Crowbars are NOT Wrenches btw.
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerCrowbar(ItemStack aTool) {
        return registerTool(aTool, sCrowbarList);
    }

    /**
     * Register a Screwdriver to interact directly with Machines and Covers Did I mention, that it is intentionally not
     * possible to make a Multi-tool, which doesn't switch ItemID (like a Mode) all the time?
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    @SuppressWarnings("UnusedReturnValue") // Retains API method signature
    public static boolean registerScrewdriver(ItemStack aTool) {
        return registerTool(aTool, sScrewdriverList);
    }

    /**
     * Register a Soft Hammer to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerSoftMallet(ItemStack aTool) {
        return registerTool(aTool, sSoftMalletList);
    }

    /**
     * Register a Hard Hammer to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerHardHammer(ItemStack aTool) {
        return registerTool(aTool, sHardHammerList);
    }

    /**
     * Register a Wire Cutter to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerWireCutter(ItemStack aTool) {
        return registerTool(aTool, sWireCutterList);
    }

    /**
     * Register a Soldering Tool to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    @SuppressWarnings("UnusedReturnValue") // Retains API method signature
    public static boolean registerSolderingTool(ItemStack aTool) {
        return registerTool(aTool, sSolderingToolList);
    }

    /**
     * Register a Soldering Tin to interact with Soldering Tools
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Auto-detection will assign a Tool Type in
     * certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    @SuppressWarnings("UnusedReturnValue") // Retains API method signature
    public static boolean registerSolderingMetal(ItemStack aTool) {
        return registerTool(aTool, sSolderingMetalList);
    }

    /**
     * Generic Function to add Tools to the Lists. Contains all sanity Checks for Tools, like preventing one Tool from
     * being registered for multiple purposes as controls would override each other.
     */
    public static boolean registerTool(ItemStack aTool, Collection<GTItemStack> aToolList) {
        if (aTool == null || GTUtility.isStackInList(aTool, sToolList)
            || (!aTool.getItem()
                .isDamageable() && !GTModHandler.isElectricItem(aTool) && !(aTool.getItem() instanceof IDamagableItem)))
            return false;
        aToolList.add(new GTItemStack(GTUtility.copyAmount(1, aTool)));
        sToolList.add(new GTItemStack(GTUtility.copyAmount(1, aTool)));
        return true;
    }

    /**
     * Sets the {@link IIconRegister} for Block Icons
     *
     * @param aIconRegister The {@link IIconRegister} Icon Register
     */
    @SideOnly(Side.CLIENT)
    public static void setBlockIconRegister(IIconRegister aIconRegister) {
        sBlockIcons = aIconRegister;
    }

    /**
     * Sets the {@link IIconRegister} for Items Icons
     *
     * @param aIconRegister The {@link IIconRegister} Icon Register
     */
    @SideOnly(Side.CLIENT)
    public static void setItemIconRegister(IIconRegister aIconRegister) {
        GregTechAPI.sItemIcons = aIconRegister;
    }

    public static void registerTileEntityConstructor(int meta, IntFunction<TileEntity> constructor) {
        if (meta < 0 || meta > 15 || constructor == null) throw new IllegalArgumentException();
        if (teCreators[meta] != null) throw new IllegalStateException(
            "previous constructor: " + teCreators[meta] + " new constructor: " + constructor + " meta:" + meta);
        teCreators[meta] = constructor;
    }

    public static TileEntity createTileEntity(int meta) {
        meta = GTUtility.clamp(meta, 0, 15);
        if (teCreators[meta] == null) return null;
        return teCreators[meta].apply(meta);
    }
}
