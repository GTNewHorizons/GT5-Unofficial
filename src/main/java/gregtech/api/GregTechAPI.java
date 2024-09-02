package gregtech.api;

import static gregtech.api.enums.GTValues.B;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.internal.IGTRecipeAdder;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.ItemCoolantCell;
import gregtech.api.items.ItemCoolantCellIC;
import gregtech.api.items.ItemTool;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.objects.GTCoverDefault;
import gregtech.api.objects.GTCoverNone;
import gregtech.api.objects.GTHashSet;
import gregtech.api.objects.GTItemStack;
import gregtech.api.threads.RunnableCableUpdate;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.CircuitryBehavior;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTCreativeTab;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.ItemHolder;
import gregtech.api.world.GTWorldgen;
import gregtech.common.GTDummyWorld;
import gregtech.common.items.ItemIntegratedCircuit;

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
     * Fixes the HashMap Mappings for ItemStacks once the Server started
     * <br>
     * <br>
     * NOTE: We use wildcards generics for the key because it could be for example {@link ItemStack} or
     * {@link GTItemStack}
     */
    public static final Collection<Map<?, ?>> sItemStackMappings = new ArrayList<>();
    public static final Collection<SetMultimap<? extends ItemHolder, ?>> itemStackMultiMaps = new ArrayList<>();

    /**
     * The MetaTileEntity-ID-List-Length
     */
    public static final short MAXIMUM_METATILE_IDS = Short.MAX_VALUE - 1;
    /**
     * My Creative Tab
     */
    public static final CreativeTabs TAB_GREGTECH = new GTCreativeTab("Main", "Main"),
        TAB_GREGTECH_MATERIALS = new GTCreativeTab("Materials", "Materials"),
        TAB_GREGTECH_ORES = new GTCreativeTab("Ores", "Ores");

    public static final IMetaTileEntity[] METATILEENTITIES = new IMetaTileEntity[MAXIMUM_METATILE_IDS];
    /**
     * The Icon List for Covers
     */
    public static final Map<GTItemStack, ITexture> sCovers = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    public static final Map<GTItemStack, CoverBehaviorBase<?>> sCoverBehaviors = new ConcurrentHashMap<>();
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
    public static final Map<String, Map<Integer, Map<Long, Byte>>> sAdvancedWirelessRedstone = new ConcurrentHashMap<>();

    /**
     * The IDSU Frequencies
     */
    public static final Map<Integer, Integer> sIDSUList = new ConcurrentHashMap<>();
    /**
     * A List of all Books, which were created using @GT_Utility.getWrittenBook the original Title is the Key Value
     */
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();
    /**
     * The List of all Sounds used in GT, indices are in the static Block at the bottom
     *
     * @deprecated Use {@link SoundResource}
     */
    @Deprecated
    public static final Map<Integer, String> sSoundList = SoundResource.asSoundList();
    /**
     * The List of Tools, which can be used. Accepts regular damageable Items and Electric Items
     */
    public static final GTHashSet<GTItemStack> sToolList = new GTHashSet<>(), sCrowbarList = new GTHashSet<>(),
        sScrewdriverList = new GTHashSet<>(), sWrenchList = new GTHashSet<>(), sSoftHammerList = new GTHashSet<>(),
        sHardHammerList = new GTHashSet<>(), sWireCutterList = new GTHashSet<>(),
        sSolderingToolList = new GTHashSet<>(), sSolderingMetalList = new GTHashSet<>(),
        sJackhammerList = new GTHashSet<>();
    /**
     * The List of Hazmat Armors
     */
    public static final GTHashSet<GTItemStack> sGasHazmatList = new GTHashSet<>(), sBioHazmatList = new GTHashSet<>(),
        sFrostHazmatList = new GTHashSet<>(), sHeatHazmatList = new GTHashSet<>(), sRadioHazmatList = new GTHashSet<>(),
        sElectroHazmatList = new GTHashSet<>();

    private static final Multimap<Integer, ItemStack> sRealConfigurationList = Multimaps
        .newListMultimap(new TreeMap<>(), ArrayList::new);
    private static final Map<Integer, List<ItemStack>> sConfigurationLists = new ConcurrentHashMap<>();
    private static final Map<Predicate<ItemStack>, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> sRealCircuitProgrammerList = new LinkedHashMap<>();
    public static final Map<Predicate<ItemStack>, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> sCircuitProgrammerList = Collections
        .unmodifiableMap(sRealCircuitProgrammerList);

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
     * This is the generic Cover behavior. Used for the default Covers, which have no Behavior.
     */
    public static final CoverBehavior sDefaultBehavior = new GTCoverDefault(), sNoBehavior = new GTCoverNone();
    /**
     * For the API Version check
     */
    public static volatile int VERSION = 509;

    /**
     * @deprecated Use {@link GTValues#RA}
     */
    @SuppressWarnings("DeprecatedIsStillUsed") // Still need be initialized for backward compat
    @Deprecated
    public static IGTRecipeAdder sRecipeAdder;
    /**
     * Registers Aspects to Thaumcraft. This Object might be {@code null} if Thaumcraft isn't installed.
     */
    public static IThaumcraftCompat sThaumcraftCompat;
    /**
     * The Lists below are executed at their respective timings. Useful to do things at a particular moment in time.
     * The Lists are not Threaded - a native Java interface is used for their execution.
     * Add your "commands" in the constructor or in the static-code-block of your mod's Main class.
     * Implement the method {@code run()}, and everything should work.
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
    /**
     * The Configuration Objects
     */
    public static GTConfig NEIClientFIle;

    public static int TICKS_FOR_LAG_AVERAGING = 25, MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = 100;
    /**
     * Initialized by the Block creation.
     */
    public static Block sBlockMachines;

    public static Block sBlockOres1, sBlockOresUb1, sBlockOresUb2, sBlockOresUb3,
        /* sBlockGem, */
        sBlockMetal1, sBlockMetal2, sBlockMetal3, sBlockMetal4, sBlockMetal5, sBlockMetal6, sBlockMetal7, sBlockMetal8,
        sBlockMetal9, sBlockGem1, sBlockGem2, sBlockGem3, sBlockReinforced;
    public static Block sBlockGranites, sBlockConcretes, sBlockStones;
    public static Block sBlockCasings1, sBlockCasings2, sBlockCasings3, sBlockCasings4, sBlockCasings5, sBlockCasings6,
        sBlockCasings8, sBlockCasings9, sBlockCasings10, sBlockCasings11, sSolenoidCoilCasings;
    public static Block sBlockLongDistancePipes;
    public static Block sDroneRender;
    public static Block sBlockFrames;
    public static Block sBlockGlass1;
    public static Block sBlockTintedGlass;
    public static Block sLaserRender;
    public static Block sWormholeRender;
    /**
     * Getting assigned by the Config
     */
    public static boolean sTimber = true, sDrinksAlwaysDrinkable = false, sMultiThreadedSounds = false,
        sDoShowAllItemsInCreative = false, sColoredGUI = true, sMachineMetalGUI = false, sMachineExplosions = true,
        sMachineFlammable = true, sMachineNonWrenchExplosions = true, sMachineRainExplosions = true,
        sMachineThunderExplosions = true, sMachineFireExplosions = true, sMachineWireFire = true, mOutputRF = false,
        mInputRF = false, mRFExplosions = false, mServerStarted = false;

    public static int mEUtoRF = 360, mRFtoEU = 20;

    /**
     * Option to not use MACHINE_METAL mixing into colors
     */
    public static boolean sUseMachineMetal = false;

    public static boolean mUseOnlyGoodSolderingMaterials = false;

    private static final String aTextIC2Lower = IndustrialCraft2.ID.toLowerCase(Locale.ENGLISH);
    /**
     * Getting assigned by the Mod loading
     */
    public static boolean sUnificationEntriesRegistered = false, sPreloadStarted = false, sPreloadFinished = false,
        sLoadStarted = false, sLoadFinished = false, sPostloadStarted = false, sPostloadFinished = false,
        sFullLoadFinished = false;

    private static Class<BaseMetaTileEntity> sBaseMetaTileEntityClass = null;

    @SuppressWarnings("unchecked")
    private static final IntFunction<TileEntity>[] teCreators = new IntFunction[16];

    private static final Set<Class<?>> dummyWorlds = new HashSet<>();

    static {
        sItemStackMappings.add(sCovers);
        sItemStackMappings.add(sCoverBehaviors);

        dummyWorlds.add(GTDummyWorld.class);
        tryAddDummyWorld("blockrenderer6343.client.world.DummyWorld");
    }

    private static void tryAddDummyWorld(String className) {
        ClassLoader cl = GregTechAPI.class.getClassLoader();
        Class<?> clazz;
        try {
            clazz = Class.forName(className, false, cl);
        } catch (ReflectiveOperationException ex) {
            return;
        }
        dummyWorlds.add(clazz);
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
            return id != null && (id & B[aMeta]) != 0;
        }
        return false;
    }

    /**
     * Creates a new Coolant Cell Item for your Nuclear Reactor
     */
    public static Item constructCoolantCellItem(String aUnlocalized, String aEnglish, int aMaxStore) {
        try {
            return new ItemCoolantCellIC(aUnlocalized, aEnglish, aMaxStore);
        } catch (Throwable e) {
            /* Do nothing */
        }
        try {
            return new ItemCoolantCell(aUnlocalized, aEnglish, aMaxStore);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new GTGenericItem(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug");
    }

    /**
     * Creates a new Energy Armor Item
     */
    public static Item constructElectricArmorItem(String aUnlocalized, String aEnglish, int aCharge, int aTransfer,
        int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider,
        int aType, int aArmorIndex) {
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyArmorIC_Item")
                .getConstructors()[0].newInstance(
                    aUnlocalized,
                    aEnglish,
                    aCharge,
                    aTransfer,
                    aTier,
                    aDamageEnergyCost,
                    aSpecials,
                    aArmorAbsorbtionPercentage,
                    aChargeProvider,
                    aType,
                    aArmorIndex);
        } catch (Throwable e) {
            /* Do nothing */
        }
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyArmor_Item")
                .getConstructors()[0].newInstance(
                    aUnlocalized,
                    aEnglish,
                    aCharge,
                    aTransfer,
                    aTier,
                    aDamageEnergyCost,
                    aSpecials,
                    aArmorAbsorbtionPercentage,
                    aChargeProvider,
                    aType,
                    aArmorIndex);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new GTGenericItem(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug");
    }

    /**
     * Creates a new Energy Battery Item
     */
    public static Item constructElectricEnergyStorageItem(String aUnlocalized, String aEnglish, int aCharge,
        int aTransfer, int aTier, int aEmptyID, int aFullID) {
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyStoreIC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyStore_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new GTGenericItem(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug");
    }

    /**
     * Creates a new Hard Hammer Item
     */
    public static ItemTool constructHardHammerItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_HardHammer_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new Crowbar Item
     */
    public static ItemTool constructCrowbarItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_CrowbarRC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {
            /* Do nothing */
        }
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_Crowbar_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new Wrench Item
     */
    public static ItemTool constructWrenchItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage,
        int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_Wrench_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new electric Screwdriver Item
     */
    public static ItemTool constructElectricScrewdriverItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage, int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_ScrewdriverIC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new electric Wrench Item
     */
    public static ItemTool constructElectricWrenchItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage, int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_WrenchIC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new electric Saw Item
     */
    public static ItemTool constructElectricSawItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak,
        int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_SawIC_Item")
                .getConstructors()[0].newInstance(
                    aUnlocalized,
                    aEnglish,
                    aMaxDamage,
                    aEntityDamage,
                    aToolQuality,
                    aToolStrength,
                    aEnergyConsumptionPerBlockBreak,
                    aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new electric Drill Item
     */
    public static ItemTool constructElectricDrillItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak,
        int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_DrillIC_Item")
                .getConstructors()[0].newInstance(
                    aUnlocalized,
                    aEnglish,
                    aMaxDamage,
                    aEntityDamage,
                    aToolQuality,
                    aToolStrength,
                    aEnergyConsumptionPerBlockBreak,
                    aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new electric Soldering Tool
     */
    public static ItemTool constructElectricSolderingToolItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aEntityDamage, int aDisChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_SolderingToolIC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(
            aUnlocalized,
            aEnglish,
            "Doesn't work as intended, this is a Bug",
            aMaxDamage,
            aEntityDamage,
            false);
    }

    /**
     * Creates a new empty electric Tool
     */
    public static ItemTool constructEmptyElectricToolItem(String aUnlocalized, String aEnglish, int aMaxDamage,
        int aChargedGTID) {
        try {
            return (ItemTool) Class.forName("gregtechmod.api.items.GT_EmptyToolIC_Item")
                .getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aChargedGTID);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return new ItemTool(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, 0, false);
    }

    /**
     * Provides a new BaseMetaTileEntity. Because some interfaces are not always loaded (Buildcraft, Universal
     * Electricity) we have to use invocation at the constructor of the BaseMetaTileEntity.
     */
    public static BaseMetaTileEntity constructBaseMetaTileEntity() {
        if (sBaseMetaTileEntityClass == null) {
            try {
                return (sBaseMetaTileEntityClass = BaseMetaTileEntity.class).getDeclaredConstructor()
                    .newInstance();
            } catch (Throwable ignored) {}
        }

        try {
            return sBaseMetaTileEntityClass.getDeclaredConstructor()
                .newInstance();
        } catch (Throwable e) {
            GTLog.err.println("GTMod: Fatal Error occurred while initializing TileEntities, crashing Minecraft.");
            e.printStackTrace(GTLog.err);
            throw new RuntimeException(e);
        }
    }

    /**
     * Register a new ItemStack as configuration circuits. Duplicates or invalid stacks will be silently ignored.
     */
    public static void registerConfigurationCircuit(ItemStack aStack) {
        registerConfigurationCircuit(aStack, 0);
    }

    /**
     * Register a new ItemStack as configuration circuits. Duplicates or invalid stacks will be silently ignored.
     *
     * @param minTier the minimal tier this circuit can be offered for free, e.g. normal configuration circuit is
     *                available in LV+ single blocks, GT++ breakthrough circuit is offered in HV+ single blocks
     */
    public static void registerConfigurationCircuit(ItemStack aStack, int minTier) {
        if (GTUtility.isStackInvalid(aStack)) return;
        for (ItemStack tRegistered : sRealConfigurationList.values())
            if (GTUtility.areStacksEqual(tRegistered, aStack)) return;
        ItemStack stack = GTUtility.copyAmount(0, aStack);
        sRealConfigurationList.put(minTier, stack);
        for (Map.Entry<Integer, List<ItemStack>> e : sConfigurationLists.entrySet()) {
            if (e.getKey() >= minTier) {
                e.getValue()
                    .add(stack);
                e.getValue()
                    .sort(getConfigurationCircuitsComparator());
            }
        }
    }

    /**
     * Get a list of Configuration circuits. These stacks will have a stack size of 0. Use
     * {@link #registerConfigurationCircuit(ItemStack, int)} or its overload to add to this list.
     *
     * @param machineTier The voltage tier where this list will be used. use Integer.MAX_VALUE to get all circuits
     * @return An unmodifiable view of actual list. DO NOT MODIFY THE ItemStacks!
     */
    public static List<ItemStack> getConfigurationCircuitList(int machineTier) {
        return Collections.unmodifiableList(
            sConfigurationLists.computeIfAbsent(
                machineTier,
                (t) -> sRealConfigurationList.entries()
                    .stream()
                    .filter(e -> e.getKey() <= machineTier)
                    .map(Map.Entry::getValue)
                    .sorted(getConfigurationCircuitsComparator())
                    .collect(Collectors.toList())));
    }

    public static Comparator<ItemStack> getConfigurationCircuitsComparator() {
        return Comparator.comparingInt((ItemStack is) -> is.getItem() instanceof ItemIntegratedCircuit ? 0 : 1)
            .thenComparing(ItemStack::getUnlocalizedName)
            .thenComparing(ItemStack::getItemDamage);
    }

    public static void registerCircuitProgrammer(ItemStack stack, boolean ignoreNBT, boolean useContainer) {
        registerCircuitProgrammer(rhs -> GTUtility.areStacksEqual(stack, rhs, ignoreNBT), useContainer);
    }

    public static void registerCircuitProgrammer(Predicate<ItemStack> predicate, boolean useContainer) {
        sRealCircuitProgrammerList.put(
            predicate,
            useContainer ? (s, p) -> s.getItem()
                .getContainerItem(s) : (s, p) -> s);
    }

    public static void registerCircuitProgrammer(Predicate<ItemStack> predicate,
        BiFunction<ItemStack, EntityPlayerMP, ItemStack> doDamage) {
        sRealCircuitProgrammerList.put(predicate, doDamage);
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, CoverBehavior aBehavior) {
        registerCover(aStack, aCover, (CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, CoverBehaviorBase<?> aBehavior) {
        if (!sCovers.containsKey(new GTItemStack(aStack))) sCovers.put(
            new GTItemStack(aStack),
            aCover == null || !aCover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : aCover);
        if (aBehavior != null) sCoverBehaviors.put(new GTItemStack(aStack), aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, CoverBehavior aBehavior) {
        registerCoverBehavior(aStack, (CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, CoverBehaviorBase<?> aBehavior) {
        sCoverBehaviors.put(new GTItemStack(aStack), aBehavior == null ? sDefaultBehavior : aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover, CoverBehavior aBehavior) {
        registerCover(aStackList, aCover, (CoverBehaviorBase<?>) aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover,
        CoverBehaviorBase<?> aBehavior) {
        if (aCover.isValidTexture()) aStackList.forEach(tStack -> GregTechAPI.registerCover(tStack, aCover, aBehavior));
    }

    /**
     * returns a Cover behavior, guaranteed to not return null after preload
     *
     * @return The Cover behavior
     */
    public static CoverBehaviorBase<?> getCoverBehaviorNew(ItemStack aStack) {
        if (aStack == null || aStack.getItem() == null) return sNoBehavior;
        CoverBehaviorBase<?> rCover = sCoverBehaviors.get(new GTItemStack(aStack));
        if (rCover != null) return rCover;
        rCover = sCoverBehaviors.get(new GTItemStack(aStack, true));
        if (rCover != null) return rCover;
        return sDefaultBehavior;
    }

    /**
     * returns a Cover behavior, guaranteed to not return null
     */
    public static CoverBehaviorBase<?> getCoverBehaviorNew(int aStack) {
        if (aStack == 0) return sNoBehavior;
        return getCoverBehaviorNew(GTUtility.intToStack(aStack));
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
    public static boolean registerSoftHammer(ItemStack aTool) {
        return registerTool(aTool, sSoftHammerList);
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
