package gregtech.common;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.FluidState.LIQUID;
import static gregtech.api.enums.FluidState.MOLTEN;
import static gregtech.api.enums.FluidState.PLASMA;
import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.GT_Values.debugEntityCramming;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.ElectroMagicTools;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GanysSurface;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IguanaTweaksTinkerConstruct;
import static gregtech.api.enums.Mods.MagicalCrops;
import static gregtech.api.enums.Mods.Names;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.TaintedMagic;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBoots;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.enums.Mods.WitchingGadgets;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gregtech.api.util.GT_Util.LAST_BROKEN_TILEENTITY;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.fluid.GT_FluidFactory;
import gregtech.api.interfaces.IBlockOnWalkOver;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.internal.IGT_Mod;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_UO_DimensionList;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.util.GT_BlockMap;
import gregtech.api.util.GT_CLS_Compat;
import gregtech.api.util.GT_ChunkAssociatedData;
import gregtech.api.util.GT_ClientPreference;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeRegistrator;
import gregtech.api.util.GT_Shaped_Recipe;
import gregtech.api.util.GT_Shapeless_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.entities.GT_Entity_Arrow;
import gregtech.common.items.GT_MetaGenerated_Item_98;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.misc.GlobalEnergyWorldSavedData;
import gregtech.common.misc.GlobalMetricsCoverDatabase;
import gregtech.common.misc.spaceprojects.SpaceProjectWorldSavedData;
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_DroneCentre;

public abstract class GT_Proxy implements IGT_Mod, IGuiHandler, IFuelHandler {

    private static final EnumSet<OreGenEvent.GenerateMinable.EventType> PREVENTED_ORES = EnumSet.of(
        OreGenEvent.GenerateMinable.EventType.COAL,
        OreGenEvent.GenerateMinable.EventType.IRON,
        OreGenEvent.GenerateMinable.EventType.GOLD,
        OreGenEvent.GenerateMinable.EventType.DIAMOND,
        OreGenEvent.GenerateMinable.EventType.REDSTONE,
        OreGenEvent.GenerateMinable.EventType.LAPIS,
        OreGenEvent.GenerateMinable.EventType.QUARTZ);
    public final HashSet<ItemStack> mRegisteredOres = new HashSet<>(10000);
    public final ArrayList<String> mSoundNames = new ArrayList<>();
    public final ArrayList<ItemStack> mSoundItems = new ArrayList<>();
    public final ArrayList<Integer> mSoundCounts = new ArrayList<>();
    private final Collection<OreDictEventContainer> mEvents = new HashSet<>();
    private final Collection<String> mIgnoredItems = new HashSet<>(
        Arrays.asList(
            "itemGhastTear",
            "itemFlint",
            "itemClay",
            "itemBucketSaltWater",
            "itemBucketFreshWater",
            "itemBucketWater",
            "itemRock",
            "itemReed",
            "itemArrow",
            "itemSaw",
            "itemKnife",
            "itemHammer",
            "itemChisel",
            "itemRubber",
            "itemEssence",
            "itemIlluminatedPanel",
            "itemSkull",
            "itemRawRubber",
            "itemBacon",
            "itemJetpackAccelerator",
            "itemLazurite",
            "itemIridium",
            "itemTear",
            "itemClaw",
            "itemFertilizer",
            "itemTar",
            "itemSlimeball",
            "itemCoke",
            "itemBeeswax",
            "itemBeeQueen",
            "itemForcicium",
            "itemForcillium",
            "itemRoyalJelly",
            "itemHoneydew",
            "itemHoney",
            "itemPollen",
            "itemReedTypha",
            "itemSulfuricAcid",
            "itemPotash",
            "itemCompressedCarbon",
            "itemBitumen",
            "itemBioFuel",
            "itemCokeSugar",
            "itemCokeCactus",
            "itemCharcoalSugar",
            "itemCharcoalCactus",
            "itemSludge",
            "itemEnrichedAlloy",
            "itemQuicksilver",
            "itemMercury",
            "itemOsmium",
            "itemUltimateCircuit",
            "itemEnergizedStar",
            "itemAntimatterMolecule",
            "itemAntimatterGlob",
            "itemCoal",
            "itemBoat",
            "itemHerbalMedicineCake",
            "itemCakeSponge",
            "itemFishandPumpkinCakeSponge",
            "itemSoulCleaver",
            "itemInstantCake",
            "itemWhippingCream",
            "itemGlisteningWhippingCream",
            "itemCleaver",
            "itemHerbalMedicineWhippingCream",
            "itemStrangeWhippingCream",
            "itemBlazeCleaver",
            "itemBakedCakeSponge",
            "itemMagmaCake",
            "itemGlisteningCake",
            "itemOgreCleaver",
            "itemFishandPumpkinCake",
            "itemMagmaWhippingCream",
            "itemMultimeter",
            "itemSuperconductor"));
    private final Collection<String> mIgnoredNames = new HashSet<>(
        Arrays.asList(
            "grubBee",
            "chainLink",
            "candyCane",
            "bRedString",
            "bVial",
            "bFlask",
            "anorthositeSmooth",
            "migmatiteSmooth",
            "slateSmooth",
            "travertineSmooth",
            "limestoneSmooth",
            "orthogneissSmooth",
            "marbleSmooth",
            "honeyDrop",
            "lumpClay",
            "honeyEqualssugar",
            "flourEqualswheat",
            "bluestoneInsulated",
            "blockWaterstone",
            "blockSand",
            "blockTorch",
            "blockPumpkin",
            "blockClothRock",
            "blockStainedHardenedClay",
            "blockQuartzPillar",
            "blockQuartzChiselled",
            "blockSpawner",
            "blockCloth",
            "mobHead",
            "mobEgg",
            "enderFlower",
            "enderChest",
            "clayHardened",
            "dayGemMaterial",
            "nightGemMaterial",
            "snowLayer",
            "bPlaceholder",
            "hardenedClay",
            "eternalLifeEssence",
            "sandstone",
            "wheatRice",
            "transdimBlock",
            "bambooBasket",
            "lexicaBotania",
            "livingwoodTwig",
            "redstoneCrystal",
            "pestleAndMortar",
            "glowstone",
            "whiteStone",
            "stoneSlab",
            "transdimBlock",
            "clayBowl",
            "clayPlate",
            "ceramicBowl",
            "ceramicPlate",
            "ovenRack",
            "clayCup",
            "ceramicCup",
            "batteryBox",
            "transmutationStone",
            "torchRedstoneActive",
            "coal",
            "charcoal",
            "cloth",
            "cobblestoneSlab",
            "stoneBrickSlab",
            "cobblestoneWall",
            "stoneBrickWall",
            "cobblestoneStair",
            "stoneBrickStair",
            "blockCloud",
            "blockDirt",
            "blockTyrian",
            "blockCarpet",
            "blockFft",
            "blockLavastone",
            "blockHolystone",
            "blockConcrete",
            "sunnariumPart",
            "brSmallMachineCyaniteProcessor",
            "meteoriteCoal",
            "blockCobble",
            "pressOreProcessor",
            "crusherOreProcessor",
            "grinderOreProcessor",
            "blockRubber",
            "blockHoney",
            "blockHoneydew",
            "blockPeat",
            "blockRadioactive",
            "blockSlime",
            "blockCocoa",
            "blockSugarCane",
            "blockLeather",
            "blockClayBrick",
            "solarPanelHV",
            "cableRedNet",
            "stoneBowl",
            "crafterWood",
            "taintedSoil",
            "brickXyEngineering",
            "breederUranium",
            "wireMill",
            "chunkLazurite",
            "aluminumNatural",
            "aluminiumNatural",
            "naturalAluminum",
            "naturalAluminium",
            "antimatterMilligram",
            "antimatterGram",
            "strangeMatter",
            "coalGenerator",
            "electricFurnace",
            "unfinishedTank",
            "valvePart",
            "aquaRegia",
            "leatherSeal",
            "leatherSlimeSeal",
            "hambone",
            "slimeball",
            "clay",
            "enrichedUranium",
            "camoPaste",
            "antiBlock",
            "burntQuartz",
            "salmonRaw",
            "blockHopper",
            "blockEnderObsidian",
            "blockIcestone",
            "blockMagicWood",
            "blockEnderCore",
            "blockHeeEndium",
            "oreHeeEndPowder",
            "oreHeeStardust",
            "oreHeeIgneousRock",
            "oreHeeInstabilityOrb",
            "crystalPureFluix",
            "shardNether",
            "gemFluorite",
            "stickObsidian",
            "caveCrystal",
            "shardCrystal",
            "dyeCrystal",
            "shardFire",
            "shardWater",
            "shardAir",
            "shardEarth",
            "ingotRefinedIron",
            "blockMarble",
            "ingotUnstable",
            "obsidian",
            "dirt",
            "gravel",
            "grass",
            "soulsand",
            "paper",
            "brick",
            "chest"));
    private final Collection<String> mInvalidNames = new HashSet<>(
        Arrays.asList(
            "diamondShard",
            "redstoneRoot",
            "obsidianStick",
            "bloodstoneOre",
            "universalCable",
            "bronzeTube",
            "ironTube",
            "netherTube",
            "obbyTube",
            "infiniteBattery",
            "eliteBattery",
            "advancedBattery",
            "10kEUStore",
            "blueDye",
            "MonazitOre",
            "quartzCrystal",
            "whiteLuminiteCrystal",
            "darkStoneIngot",
            "invisiumIngot",
            "demoniteOrb",
            "enderGem",
            "starconiumGem",
            "osmoniumIngot",
            "tapaziteGem",
            "zectiumIngot",
            "foolsRubyGem",
            "rubyGem",
            "meteoriteGem",
            "adamiteShard",
            "sapphireGem",
            "copperIngot",
            "ironStick",
            "goldStick",
            "diamondStick",
            "reinforcedStick",
            "draconicStick",
            "emeraldStick",
            "copperStick",
            "tinStick",
            "silverStick",
            "bronzeStick",
            "steelStick",
            "leadStick",
            "manyullynStick",
            "arditeStick",
            "cobaltStick",
            "aluminiumStick",
            "alumiteStick",
            "oilsandsOre",
            "copperWire",
            "superconductorWire",
            "sulfuricAcid",
            "conveyorBelt",
            "ironWire",
            "aluminumWire",
            "aluminiumWire",
            "silverWire",
            "tinWire",
            "dustSiliconSmall",
            "AluminumOre",
            "plateHeavyT2",
            "blockWool",
            "alloyPlateEnergizedHardened",
            "gasWood",
            "alloyPlateEnergized",
            "SilverOre",
            "LeadOre",
            "TinOre",
            "CopperOre",
            "silverOre",
            "leadOre",
            "tinOre",
            "copperOre",
            "bauxiteOre",
            "HSLivingmetalIngot",
            "oilMoving",
            "oilStill",
            "oilBucket",
            "petroleumOre",
            "dieselFuel",
            "diamondNugget",
            "planks",
            "wood",
            "stick",
            "sticks",
            "naquadah",
            "obsidianRod",
            "stoneRod",
            "thaumiumRod",
            "steelRod",
            "netherrackRod",
            "woodRod",
            "ironRod",
            "cactusRod",
            "flintRod",
            "copperRod",
            "cobaltRod",
            "alumiteRod",
            "blueslimeRod",
            "arditeRod",
            "manyullynRod",
            "bronzeRod",
            "boneRod",
            "slimeRod",
            "redalloyBundled",
            "bluestoneBundled",
            "infusedteslatiteInsulated",
            "redalloyInsulated",
            "infusedteslatiteBundled"));
    private final DateFormat mDateFormat = DateFormat.getInstance();
    public final BlockingQueue<String> mBufferedPlayerActivity = new LinkedBlockingQueue<>();
    public final GT_BlockMap<Boolean> mCTMBlockCache = new GT_BlockMap<>();
    public boolean mHardcoreCables = false;
    public boolean mDisableVanillaOres = true;
    public boolean mHardMachineCasings = true;
    public boolean mAllowSmallBoilerAutomation = false;
    public boolean mNerfDustCrafting = true;
    public boolean mSortToTheEnd = true;
    public boolean mCraftingUnification = true;
    public boolean mInventoryUnification = true;
    public boolean mIncreaseDungeonLoot = true;
    public boolean mAxeWhenAdventure = true;
    public boolean mSurvivalIntoAdventure = false;
    public boolean mNerfedWoodPlank = true;
    public boolean mNerfedVanillaTools = true;
    public boolean mHardRock = false;
    public boolean mHungerEffect = true;
    public boolean mOnline = true;
    public boolean mIgnoreTcon = true;
    public boolean mDisableIC2Cables = false;
    public boolean mAchievements = true;
    public boolean mArcSmeltIntoAnnealed = true;
    public boolean mMagneticraftRecipes = false;
    public boolean mImmersiveEngineeringRecipes = false;
    private boolean isFirstServerWorldTick = true;
    private boolean isFirstWorldTick = true;
    private boolean mOreDictActivated = false;
    public boolean mChangeHarvestLevels = false;
    public boolean mNerfedCombs = true;
    public boolean mNerfedCrops = true;
    public boolean mGTBees = true;
    public boolean mHideUnusedOres = true;
    public boolean mPollution = true;
    public boolean mExplosionItemDrop = false;
    public boolean mUseGreatlyShrukenReplacementList = true;
    public int mSkeletonsShootGTArrows = 16;
    public int mMaxEqualEntitiesAtOneSpot = 3;
    public int mFlintChance = 30;
    public int mItemDespawnTime = 6000;
    public int mUpgradeCount = 4;
    public int[] mHarvestLevel = new int[1000];
    public int mGraniteHavestLevel = 3;
    public int mMaxHarvestLevel = 7;
    public int mWireHeatingTicks = 4;
    public double replicatorExponent = 1.2D;
    public int mPollutionSmogLimit = 550000;
    public int mPollutionPoisonLimit = 750000;
    public int mPollutionVegetationLimit = 1000000;
    public int mPollutionSourRainLimit = 2000000;
    public int mPollutionOnExplosion = 100000;
    public int mPollutionPrimitveBlastFurnacePerSecond = 200;
    public int mPollutionEBFPerSecond = 400;
    public int mPollutionCharcoalPitPerSecond = 100;
    public int mPollutionLargeCombustionEnginePerSecond = 480;
    public int mPollutionExtremeCombustionEnginePerSecond = 3840;
    public int mPollutionImplosionCompressorPerSecond = 10000;
    public int mPollutionLargeBronzeBoilerPerSecond = 1000;
    public int mPollutionLargeSteelBoilerPerSecond = 2000;
    public int mPollutionLargeTitaniumBoilerPerSecond = 3000;
    public int mPollutionLargeTungstenSteelBoilerPerSecond = 4000;
    public double mPollutionReleasedByThrottle = 1.0 / 24.0; // divided by 24 because 24 circuit conf
    public int mPollutionLargeGasTurbinePerSecond = 300;
    public int mPollutionMultiSmelterPerSecond = 400;
    public int mPollutionPyrolyseOvenPerSecond = 300;
    public int mPollutionSmallCoalBoilerPerSecond = 20;
    public int mPollutionHighPressureLavaBoilerPerSecond = 20;
    public int mPollutionHighPressureCoalBoilerPerSecond = 30;
    public int mPollutionBaseDieselGeneratorPerSecond = 200;
    public double[] mPollutionDieselGeneratorReleasedByTier = new double[] { 0.1, 1.0, 0.9, 0.8 };
    public int mPollutionBaseGasTurbinePerSecond = 200;
    public double[] mPollutionGasTurbineReleasedByTier = new double[] { 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 };
    public final GT_UO_DimensionList mUndergroundOil = new GT_UO_DimensionList();
    public boolean enableUndergroundGravelGen = true;
    public boolean enableUndergroundDirtGen = true;
    public int mTicksUntilNextCraftSound = 0;
    public double mMagneticraftBonusOutputPercent = 0d;
    private World mUniverse = null;
    public boolean mTEMachineRecipes = false;
    public boolean mEnableAllMaterials = false;
    public boolean mEnableCleanroom = true;
    public boolean mLowGravProcessing = false;
    public boolean mAprilFool = false;
    public boolean mCropNeedBlock = true;
    @Deprecated
    public boolean mDisableOldChemicalRecipes = false;
    public boolean mAMHInteraction = true;
    public boolean mForceFreeFace = true;
    public boolean mBrickedBlastFurnace = true;
    @Deprecated
    public boolean mEasierIVPlusCables = false;
    public boolean mMixedOreOnlyYieldsTwoThirdsOfPureOre = false;
    public boolean mRichOreYieldMultiplier = true;
    public boolean mNetherOreYieldMultiplier = true;
    public boolean mEndOreYieldMultiplier = true;
    public boolean enableBlackGraniteOres = true;
    public boolean enableRedGraniteOres = true;
    public boolean enableMarbleOres = true;
    public boolean enableBasaltOres = true;
    public boolean gt6Pipe = true;
    public boolean gt6Cable = true;
    public boolean ic2EnergySourceCompat = true;
    public boolean costlyCableConnection = false;
    public boolean crashOnNullRecipeInput = false;

    public enum OreDropSystem {
        Block,
        PerDimBlock,
        UnifiedBlock,
        FortuneItem,
        Item
    }

    public OreDropSystem oreDropSystem = OreDropSystem.FortuneItem;

    /**
     * This enables ambient-occlusion smooth lighting on tiles
     */
    public boolean mRenderTileAmbientOcclusion = true;

    /**
     * This enables rendering of glowing textures
     */
    public boolean mRenderGlowTextures = true;

    /**
     * Render flipped textures
     */
    public boolean mRenderFlippedMachinesFlipped = true;

    /**
     * This enables indicators on input/output hatches
     */
    public boolean mRenderIndicatorsOnHatch = true;

    /**
     * This enables the rendering of dirt particles if pollution is enabled too
     */
    public boolean mRenderDirtParticles = true;

    /**
     * This enables the rendering of the pollution fog if pollution is enabled too
     */
    public boolean mRenderPollutionFog = true;

    /**
     * This enables BaseMetaTileEntity block updates handled by BlockUpdateHandler
     */
    public boolean mUseBlockUpdateHandler = false;

    /**
     * This makes cover tabs visible on GregTech machines
     */
    public boolean mCoverTabsVisible = true;

    /**
     * This controls whether cover tabs display on the left (default) or right side of the UI
     */
    public boolean mCoverTabsFlipped = false;

    /**
     * How verbose should tooltips be? 0: disabled, 1: one-line, 2: normal, 3+: extended
     */
    public int mTooltipVerbosity = 2;

    /**
     * How verbose should tooltips be when LSHIFT is held? 0: disabled, 1: one-line, 2: normal, 3+: extended
     */
    public int mTooltipShiftVerbosity = 3;

    /**
     * Which style to use for title tab on machine GUI? 0: text tab split-dark, 1: text tab unified, 2: item icon tab
     */
    public int mTitleTabStyle = 0;

    /**
     * Whether to show seconds or ticks on NEI
     */
    public boolean mNEIRecipeSecondMode = true;

    /**
     * This enables "Recipe by" display on NEI
     */
    public boolean mNEIRecipeOwner = false;

    /**
     * This enables showing stack traces where the recipe was added. Reboot needed
     */
    public boolean mNEIRecipeOwnerStackTrace = false;

    /**
     * This enables showing original voltage when overclocked
     */
    public boolean mNEIOriginalVoltage = false;

    /**
     * This enables the green->red durability for an item's damage value
     */
    public boolean mRenderItemDurabilityBar = true;

    /**
     * This enables the blue charge bar for an electric item's charge
     */
    public boolean mRenderItemChargeBar = true;

    public final Map<RecipeCategory, RecipeCategorySetting> recipeCategorySettings = new HashMap<>();

    /**
     * This enables showing voltage tier of transformer for Waila, instead of raw voltage number
     */
    public boolean mWailaTransformerVoltageTier = true;

    /**
     * What is the order of the circuits when they are selected?
     */
    public Map<String, Integer> mCircuitsOrder = new HashMap<>();

    public boolean wailaAverageNS = false;

    public static final int GUI_ID_COVER_SIDE_BASE = 10; // Takes GUI ID 10 - 15

    public static Map<String, Integer> oreDictBurnTimes = new HashMap<>();

    // Locking
    public static ReentrantLock TICK_LOCK = new ReentrantLock();

    private final ConcurrentMap<UUID, GT_ClientPreference> mClientPrefernces = new ConcurrentHashMap<>();

    static {
        oreDictBurnTimes.put("dustTinyWood", 11);
        oreDictBurnTimes.put("dustTinySodium", 44);
        oreDictBurnTimes.put("dustSmallWood", 25);
        oreDictBurnTimes.put("dustSmallSodium", 100);
        oreDictBurnTimes.put("dustWood", 100);
        oreDictBurnTimes.put("dustTinyCoal", 177);
        oreDictBurnTimes.put("dustTinyCharcoal", 177);
        oreDictBurnTimes.put("dustTinyLignite", 166);
        oreDictBurnTimes.put("plateWood", 300);
        oreDictBurnTimes.put("dustSmallLignite", 375);
        oreDictBurnTimes.put("dustSodium", 400);
        oreDictBurnTimes.put("dustSmallCoal", 400);
        oreDictBurnTimes.put("dustSmallCharcoal", 400);
        oreDictBurnTimes.put("dustTinyLithium", 888);
        oreDictBurnTimes.put("dustTinyCaesium", 888);
        oreDictBurnTimes.put("gemLignite", 1200);
        oreDictBurnTimes.put("crushedLignite", 1200);
        oreDictBurnTimes.put("dustImpureLignite", 1200);
        oreDictBurnTimes.put("dustLignite", 1200);
        oreDictBurnTimes.put("dustSulfur", 1600);
        oreDictBurnTimes.put("gemCoal", 1600);
        oreDictBurnTimes.put("crushedCoal", 1600);
        oreDictBurnTimes.put("dustImpureCoal", 1600);
        oreDictBurnTimes.put("dustCoal", 1600);
        oreDictBurnTimes.put("gemCharcoal", 1600);
        oreDictBurnTimes.put("crushedCharcoal", 1600);
        oreDictBurnTimes.put("dustImpureCharcoal", 1600);
        oreDictBurnTimes.put("dustCharcoal", 1600);
        oreDictBurnTimes.put("dustSmallLithium", 2000);
        oreDictBurnTimes.put("dustSmallCaesium", 2000);
        oreDictBurnTimes.put("gemSodium", 4000);
        oreDictBurnTimes.put("crushedSodium", 4000);
        oreDictBurnTimes.put("dustImpureSodium", 4000);
        oreDictBurnTimes.put("gemLithium", 6000);
        oreDictBurnTimes.put("crushedLithium", 6000);
        oreDictBurnTimes.put("dustImpureLithium", 6000);
        oreDictBurnTimes.put("dustLithium", 6000);
        oreDictBurnTimes.put("gemCaesium", 6000);
        oreDictBurnTimes.put("crushedCaesium", 6000);
        oreDictBurnTimes.put("dustImpureCaesium", 6000);
        oreDictBurnTimes.put("dustCaesium", 6000);
        oreDictBurnTimes.put("blockLignite", 12000);
        oreDictBurnTimes.put("blockCharcoal", 16000);
        oreDictBurnTimes.put("gemDiamond", 102400);
        oreDictBurnTimes.put("blockDiamond", 1024000);
        oreDictBurnTimes.put("crushedDiamond", 102400);
        oreDictBurnTimes.put("dustImpureDiamond", 102400);
        oreDictBurnTimes.put("dustDiamond", 102400);
        oreDictBurnTimes.put("dustSmallDiamond", 25600);
        oreDictBurnTimes.put("dustTinyDiamond", 11378);
    }

    public GT_Proxy() {
        GameRegistry.registerFuelHandler(this);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.ORE_GEN_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        GregTech_API.sThaumcraftCompat = (IThaumcraftCompat) GT_Utility
            .callConstructor("gregtech.common.GT_ThaumcraftCompat", 0, null, GT_Values.D1, new Object[0]);
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            onFluidContainerRegistration(new FluidContainerRegistry.FluidContainerRegisterEvent(tData));
        }
        try {
            for (String tOreName : OreDictionary.getOreNames()) {
                ItemStack tOreStack;
                for (Iterator<ItemStack> i$ = OreDictionary.getOres(tOreName)
                    .iterator(); i$.hasNext(); registerOre(new OreDictionary.OreRegisterEvent(tOreName, tOreStack))) {
                    tOreStack = i$.next();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    public void onPreLoad() {
        GT_Log.out.println("GT_Mod: Preload-Phase started!");
        GT_Log.ore.println("GT_Mod: Preload-Phase started!");

        GregTech_API.sPreloadStarted = true;
        this.mIgnoreTcon = GregTech_API.sOPStuff.get(ConfigCategories.general, "ignoreTConstruct", true);
        this.mWireHeatingTicks = GregTech_API.sOPStuff.get(ConfigCategories.general, "WireHeatingTicks", 4);
        this.replicatorExponent = GregTech_API.sOPStuff.get("Replicator", "Nerf Exponent", 1.2D);
        NetworkRegistry.INSTANCE.registerGuiHandler(GT_Values.GT, this);
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GT_Log.out.println("GT_Mod: Getting required Items of other Mods.");

        ItemList.RC_ShuntingWire.set(GT_ModHandler.getModItem(Railcraft.ID, "machine.delta", 1L, 0));
        ItemList.RC_ShuntingWireFrame.set(GT_ModHandler.getModItem(Railcraft.ID, "frame", 1L, 0));
        ItemList.RC_Rail_Standard.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 0));
        ItemList.RC_Rail_Adv.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 1));
        ItemList.RC_Rail_Wooden.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 2));
        ItemList.RC_Rail_HS.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 3));
        ItemList.RC_Rail_Reinforced.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 4));
        ItemList.RC_Rail_Electric.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 5));
        ItemList.RC_Tie_Wood.set(GT_ModHandler.getModItem(Railcraft.ID, "part.tie", 1L, 0));
        ItemList.RC_Tie_Stone.set(GT_ModHandler.getModItem(Railcraft.ID, "part.tie", 1L, 1));
        ItemList.RC_Bed_Wood.set(GT_ModHandler.getModItem(Railcraft.ID, "part.railbed", 1L, 0));
        ItemList.RC_Bed_Stone.set(GT_ModHandler.getModItem(Railcraft.ID, "part.railbed", 1L, 1));
        ItemList.RC_Rebar.set(GT_ModHandler.getModItem(Railcraft.ID, "part.rebar", 1L));
        ItemList.TC_Thaumometer.set(GT_ModHandler.getModItem(Thaumcraft.ID, "ItemThaumometer", 1L, 0));
        ItemList.Tool_Sword_Steel.set(GT_ModHandler.getModItem(Railcraft.ID, "tool.steel.sword", 1L));
        ItemList.Tool_Pickaxe_Steel.set(GT_ModHandler.getModItem(Railcraft.ID, "tool.steel.pickaxe", 1L));
        ItemList.Tool_Shovel_Steel.set(GT_ModHandler.getModItem(Railcraft.ID, "tool.steel.shovel", 1L));
        ItemList.Tool_Axe_Steel.set(GT_ModHandler.getModItem(Railcraft.ID, "tool.steel.axe", 1L));
        ItemList.Tool_Hoe_Steel.set(GT_ModHandler.getModItem(Railcraft.ID, "tool.steel.hoe", 1L));

        ItemList.TF_LiveRoot.set(GT_ModHandler.getModItem(TwilightForest.ID, "item.liveRoot", 1L, 0));
        ItemList.TF_Vial_FieryBlood.set(GT_ModHandler.getModItem(TwilightForest.ID, "item.fieryBlood", 1L));
        ItemList.TF_Vial_FieryTears.set(GT_ModHandler.getModItem(TwilightForest.ID, "item.fieryTears", 1L));

        ItemList.FR_Lemon.set(GT_ModHandler.getModItem(Forestry.ID, "fruits", 1L, 3));
        ItemList.FR_Mulch.set(GT_ModHandler.getModItem(Forestry.ID, "mulch", 1L));
        ItemList.FR_Fertilizer.set(GT_ModHandler.getModItem(Forestry.ID, "fertilizerCompound", 1L));
        ItemList.FR_Compost.set(GT_ModHandler.getModItem(Forestry.ID, "fertilizerBio", 1L));
        ItemList.FR_Silk.set(GT_ModHandler.getModItem(Forestry.ID, "craftingMaterial", 1L, 2));
        ItemList.FR_Wax.set(GT_ModHandler.getModItem(Forestry.ID, "beeswax", 1L));
        ItemList.FR_WaxCapsule.set(GT_ModHandler.getModItem(Forestry.ID, "waxCapsule", 1L));
        ItemList.FR_RefractoryWax.set(GT_ModHandler.getModItem(Forestry.ID, "refractoryWax", 1L));
        ItemList.FR_RefractoryCapsule.set(GT_ModHandler.getModItem(Forestry.ID, "refractoryEmpty", 1L));
        ItemList.FR_Bee_Drone.set(GT_ModHandler.getModItem(Forestry.ID, "beeDroneGE", 1L));
        ItemList.FR_Bee_Princess.set(GT_ModHandler.getModItem(Forestry.ID, "beePrincessGE", 1L));
        ItemList.FR_Bee_Queen.set(GT_ModHandler.getModItem(Forestry.ID, "beeQueenGE", 1L));
        ItemList.FR_Tree_Sapling.set(
            GT_ModHandler
                .getModItem(Forestry.ID, "sapling", 1L, GT_ModHandler.getModItem(Forestry.ID, "saplingGE", 1L)));
        ItemList.FR_Butterfly.set(GT_ModHandler.getModItem(Forestry.ID, "butterflyGE", 1L));
        ItemList.FR_Larvae.set(GT_ModHandler.getModItem(Forestry.ID, "beeLarvaeGE", 1L));
        ItemList.FR_Serum.set(GT_ModHandler.getModItem(Forestry.ID, "serumGE", 1L));
        ItemList.FR_Caterpillar.set(GT_ModHandler.getModItem(Forestry.ID, "caterpillarGE", 1L));
        ItemList.FR_PollenFertile.set(GT_ModHandler.getModItem(Forestry.ID, "pollenFertile", 1L));
        ItemList.FR_Stick.set(GT_ModHandler.getModItem(Forestry.ID, "oakStick", 1L));
        ItemList.FR_Casing_Impregnated.set(GT_ModHandler.getModItem(Forestry.ID, "impregnatedCasing", 1L));
        ItemList.FR_Casing_Sturdy.set(GT_ModHandler.getModItem(Forestry.ID, "sturdyMachine", 1L));
        ItemList.FR_Casing_Hardened.set(GT_ModHandler.getModItem(Forestry.ID, "hardenedMachine", 1L));

        ItemList.Bottle_Empty.set(new ItemStack(Items.glass_bottle, 1));

        ItemList.Cell_Universal_Fluid.set(GT_ModHandler.getIC2Item("FluidCell", 1L));
        ItemList.Cell_Empty.set(
            GT_ModHandler.getIC2Item(
                "cell",
                1L,
                GT_ModHandler.getIC2Item("cellEmpty", 1L, GT_ModHandler.getIC2Item("emptyCell", 1L))));
        ItemList.Cell_Water.set(GT_ModHandler.getIC2Item("waterCell", 1L, GT_ModHandler.getIC2Item("cellWater", 1L)));
        ItemList.Cell_Lava.set(GT_ModHandler.getIC2Item("lavaCell", 1L, GT_ModHandler.getIC2Item("cellLava", 1L)));
        ItemList.Cell_Air.set(
            GT_ModHandler.getIC2Item(
                "airCell",
                1L,
                GT_ModHandler.getIC2Item("cellAir", 1L, GT_ModHandler.getIC2Item("cellOxygen", 1L))));

        ItemList.IC2_Item_Casing_Iron.set(GT_ModHandler.getIC2Item("casingiron", 1L));
        ItemList.IC2_Item_Casing_Gold.set(GT_ModHandler.getIC2Item("casinggold", 1L));
        ItemList.IC2_Item_Casing_Bronze.set(GT_ModHandler.getIC2Item("casingbronze", 1L));
        ItemList.IC2_Item_Casing_Copper.set(GT_ModHandler.getIC2Item("casingcopper", 1L));
        ItemList.IC2_Item_Casing_Tin.set(GT_ModHandler.getIC2Item("casingtin", 1L));
        ItemList.IC2_Item_Casing_Lead.set(GT_ModHandler.getIC2Item("casinglead", 1L));
        ItemList.IC2_Item_Casing_Steel.set(GT_ModHandler.getIC2Item("casingadviron", 1L));
        ItemList.IC2_Spray_WeedEx.set(GT_ModHandler.getIC2Item("weedEx", 1L));
        ItemList.IC2_Mixed_Metal_Ingot.set(GT_ModHandler.getIC2Item("mixedMetalIngot", 1L));
        ItemList.IC2_Fertilizer.set(GT_ModHandler.getIC2Item("fertilizer", 1L));
        ItemList.IC2_CoffeeBeans.set(GT_ModHandler.getIC2Item("coffeeBeans", 1L));
        ItemList.IC2_CoffeePowder.set(GT_ModHandler.getIC2Item("coffeePowder", 1L));
        ItemList.IC2_Hops.set(GT_ModHandler.getIC2Item("hops", 1L));
        ItemList.IC2_Resin.set(GT_ModHandler.getIC2Item("resin", 1L));
        ItemList.IC2_Plantball.set(GT_ModHandler.getIC2Item("plantBall", 1L));
        ItemList.IC2_PlantballCompressed
            .set(GT_ModHandler.getIC2Item("compressedPlantBall", 1L, ItemList.IC2_Plantball.get(1L)));
        ItemList.IC2_Crop_Seeds.set(GT_ModHandler.getIC2Item("cropSeed", 1L));
        ItemList.IC2_Grin_Powder.set(GT_ModHandler.getIC2Item("grinPowder", 1L));
        ItemList.IC2_Energium_Dust.set(GT_ModHandler.getIC2Item("energiumDust", 1L));
        ItemList.IC2_Scrap.set(GT_ModHandler.getIC2Item("scrap", 1L));
        ItemList.IC2_Scrapbox.set(GT_ModHandler.getIC2Item("scrapBox", 1L));
        ItemList.IC2_Fuel_Rod_Empty.set(GT_ModHandler.getIC2Item("fuelRod", 1L));
        ItemList.IC2_Food_Can_Empty.set(GT_ModHandler.getIC2Item("tinCan", 1L));
        ItemList.IC2_Food_Can_Filled.set(GT_ModHandler.getIC2Item("filledTinCan", 1L, 0));
        ItemList.IC2_Food_Can_Spoiled.set(GT_ModHandler.getIC2Item("filledTinCan", 1L, 1));
        ItemList.IC2_Industrial_Diamond
            .set(GT_ModHandler.getIC2Item("industrialDiamond", 1L, new ItemStack(Items.diamond, 1)));
        ItemList.IC2_Compressed_Coal_Ball.set(GT_ModHandler.getIC2Item("compressedCoalBall", 1L));
        ItemList.IC2_Compressed_Coal_Chunk.set(GT_ModHandler.getIC2Item("coalChunk", 1L));
        ItemList.IC2_ShaftIron.set(GT_ModHandler.getIC2Item("ironshaft", 1L));
        ItemList.IC2_ShaftSteel.set(GT_ModHandler.getIC2Item("steelshaft", 1L));

        ItemList.IC2_SuBattery.set(GT_ModHandler.getIC2Item("suBattery", 1L));
        ItemList.IC2_ReBattery.set(GT_ModHandler.getIC2Item("reBattery", 1L));
        ItemList.IC2_AdvBattery.set(GT_ModHandler.getIC2Item("advBattery", 1L));
        ItemList.IC2_EnergyCrystal.set(GT_ModHandler.getIC2Item("energyCrystal", 1L));
        ItemList.IC2_LapotronCrystal.set(GT_ModHandler.getIC2Item("lapotronCrystal", 1L));

        ItemList.Tool_Sword_Bronze.set(GT_ModHandler.getIC2Item("bronzeSword", 1L));
        ItemList.Tool_Pickaxe_Bronze.set(GT_ModHandler.getIC2Item("bronzePickaxe", 1L));
        ItemList.Tool_Shovel_Bronze.set(GT_ModHandler.getIC2Item("bronzeShovel", 1L));
        ItemList.Tool_Axe_Bronze.set(GT_ModHandler.getIC2Item("bronzeAxe", 1L));
        ItemList.Tool_Hoe_Bronze.set(GT_ModHandler.getIC2Item("bronzeHoe", 1L));
        ItemList.IC2_ForgeHammer.set(GT_ModHandler.getIC2Item("ForgeHammer", 1L));
        ItemList.IC2_WireCutter.set(GT_ModHandler.getIC2Item("cutter", 1L));

        ItemList.Credit_Iron.set(GT_ModHandler.getIC2Item("coin", 1L));

        ItemList.Circuit_Basic.set(GT_ModHandler.getIC2Item("electronicCircuit", 1L));
        ItemList.Circuit_Advanced.set(GT_ModHandler.getIC2Item("advancedCircuit", 1L));

        ItemList.Upgrade_Overclocker.set(GT_ModHandler.getIC2Item("overclockerUpgrade", 1L));
        ItemList.Upgrade_Battery.set(GT_ModHandler.getIC2Item("energyStorageUpgrade", 1L));

        ItemList.Dye_Bonemeal.set(new ItemStack(Items.dye, 1, 15));
        ItemList.Dye_SquidInk.set(new ItemStack(Items.dye, 1, 0));
        ItemList.Dye_Cocoa.set(new ItemStack(Items.dye, 1, 3));

        ItemList.Book_Written_00.set(new ItemStack(Items.written_book, 1, 0));

        ItemList.Food_Baked_Bread.set(new ItemStack(Items.bread, 1, 0));
        ItemList.Food_Raw_Potato.set(new ItemStack(Items.potato, 1, 0));
        ItemList.Food_Baked_Potato.set(new ItemStack(Items.baked_potato, 1, 0));
        ItemList.Food_Poisonous_Potato.set(new ItemStack(Items.poisonous_potato, 1, 0));

        OrePrefixes.bottle.mContainerItem = ItemList.Bottle_Empty.get(1L);
        OrePrefixes.bucket.mContainerItem = new ItemStack(Items.bucket, 1);
        OrePrefixes.cellPlasma.mContainerItem = ItemList.Cell_Empty.get(1L);
        OrePrefixes.cellMolten.mContainerItem = ItemList.Cell_Empty.get(1L);
        OrePrefixes.cell.mContainerItem = ItemList.Cell_Empty.get(1L);

        GT_ModHandler.sNonReplaceableItems.add(new ItemStack(Items.bow, 1, 32767));
        GT_ModHandler.sNonReplaceableItems.add(new ItemStack(Items.fishing_rod, 1, 32767));
        GT_ModHandler.sNonReplaceableItems.add(ItemList.IC2_ForgeHammer.getWithDamage(1L, 32767L));
        GT_ModHandler.sNonReplaceableItems.add(ItemList.IC2_WireCutter.getWithDamage(1L, 32767L));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("painter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("blackPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("redPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("greenPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("brownPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("bluePainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("purplePainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("cyanPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("lightGreyPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("darkGreyPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("pinkPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("limePainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("yellowPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("cloudPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("magentaPainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("orangePainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("whitePainter", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("cfPack", 1L, 32767));
        // GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("jetpack", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("treetap", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("weedEx", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("staticBoots", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("compositeArmor", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("hazmatHelmet", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("hazmatChestplate", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("hazmatLeggings", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getIC2Item("hazmatBoots", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "part.turbine.disk", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "part.turbine.blade", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "part.turbine.rotor", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "borehead.diamond", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "borehead.steel", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Railcraft.ID, "borehead.iron", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.plateNaga", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(TwilightForest.ID, "item.legsNaga", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.arcticHelm", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.arcticPlate", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.arcticLegs", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.arcticBoots", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(TwilightForest.ID, "item.yetiHelm", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.yetiPlate", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(TwilightForest.ID, "item.yetiLegs", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(TwilightForest.ID, "item.yetiBoots", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ToolCertusQuartzCuttingKnife", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ToolNetherQuartzCuttingKnife", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "apiaristHelmet", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "apiaristChest", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "apiaristLegs", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "apiaristBoots", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "frameUntreated", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "frameImpregnated", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "frameProven", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems.add(GT_ModHandler.getModItem(Forestry.ID, "waxCast", 1L, 32767));
        GT_ModHandler.sNonReplaceableItems
            .add(GT_ModHandler.getModItem(GalacticraftCore.ID, "item.sensorGlasses", 1L, 32767));

        RecipeSorter.register(
            "gregtech:shaped",
            GT_Shaped_Recipe.class,
            RecipeSorter.Category.SHAPED,
            "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(
            "gregtech:shapeless",
            GT_Shapeless_Recipe.class,
            RecipeSorter.Category.SHAPELESS,
            "after:minecraft:shapeless");

        // Register chunk manager with Forge
        GT_ChunkManager.init();
    }

    public void onLoad() {
        GT_Log.out.println("GT_Mod: Beginning Load-Phase.");
        GT_Log.ore.println("GT_Mod: Beginning Load-Phase.");
        if (MagicalCrops.isModLoaded()) {
            GT_OreDictUnificator.registerOre(
                "cropChilipepper",
                GT_ModHandler.getModItem(MagicalCrops.ID, "magicalcrops_CropProduce", 1L, 2));
            GT_OreDictUnificator.registerOre(
                "cropTomato",
                GT_ModHandler.getModItem(MagicalCrops.ID, "magicalcrops_CropProduce", 1L, 8));
            GT_OreDictUnificator
                .registerOre("cropGrape", GT_ModHandler.getModItem(MagicalCrops.ID, "magicalcrops_CropProduce", 1L, 4));
        }
        if (GanysSurface.isModLoaded()) {
            GT_OreDictUnificator.registerOre("cropTea", GT_ModHandler.getModItem(GanysSurface.ID, "teaLeaves", 1L, 0));
        }

        // Clay buckets, which don't get registered until Iguana Tweaks pre-init
        if (IguanaTweaksTinkerConstruct.isModLoaded()) {
            OrePrefixes.bucketClay.mContainerItem = GT_ModHandler
                .getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0);
            GT_OreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Empty,
                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0));
            GT_OreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Water,
                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketWater", 1L, 0));
            GT_OreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Lava,
                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketLava", 1L, 0));
            GT_OreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Milk,
                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketMilk", 1L, 0));

            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(
                    Materials.Milk.getFluid(1000L),
                    GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Milk, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L)));
        }

        if (!GT_Mod.gregtechproxy.enableUndergroundGravelGen)
            PREVENTED_ORES.add(OreGenEvent.GenerateMinable.EventType.GRAVEL);
        if (!GT_Mod.gregtechproxy.enableUndergroundDirtGen)
            PREVENTED_ORES.add(OreGenEvent.GenerateMinable.EventType.DIRT);

        MinecraftForge.EVENT_BUS.register(new SpaceProjectWorldSavedData());
        MinecraftForge.EVENT_BUS.register(new GlobalEnergyWorldSavedData(""));
        MinecraftForge.EVENT_BUS.register(new GT_Worldgenerator.OregenPatternSavedData(""));
        MinecraftForge.EVENT_BUS.register(new GlobalMetricsCoverDatabase());
        FMLCommonHandler.instance()
            .bus()
            .register(new GT_Worldgenerator.OregenPatternSavedData(""));

        // IC2 Hazmat
        addFullHazmatToIC2Item("hazmatHelmet");
        addFullHazmatToIC2Item("hazmatChestplate");
        addFullHazmatToIC2Item("hazmatLeggings");
        addFullHazmatToIC2Item("hazmatBoots");
        addFullHazmatToIC2Item("nanoHelmet");
        addFullHazmatToIC2Item("nanoBoots");
        addFullHazmatToIC2Item("nanoLeggings");
        addFullHazmatToIC2Item("nanoBodyarmor");
        addFullHazmatToIC2Item("quantumHelmet");
        addFullHazmatToIC2Item("quantumBodyarmor");
        addFullHazmatToIC2Item("quantumLeggings");
        addFullHazmatToIC2Item("quantumBoots");

        // GT++ Hazmat
        addFullHazmatToGeneralItem(GTPlusPlus.ID, "itemArmorHazmatHelmetEx", 1);
        addFullHazmatToGeneralItem(GTPlusPlus.ID, "itemArmorHazmatChestplateEx", 1);
        addFullHazmatToGeneralItem(GTPlusPlus.ID, "itemArmorHazmatLeggingsEx", 1);
        addFullHazmatToGeneralItem(GTPlusPlus.ID, "itemArmorRubBootsEx", 1);

        // GraviSuite Hazmat
        addFullHazmatToGeneralItem(GraviSuite.ID, "graviChestPlate", 1L);
        addFullHazmatToGeneralItem(GraviSuite.ID, "advNanoChestPlate", 1L);

        // EMT Hazmat
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "itemArmorQuantumChestplate", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "NanoBootsTraveller", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "NanosuitGogglesRevealing", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "QuantumBootsTraveller", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "QuantumGogglesRevealing", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "SolarHelmetRevealing", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "NanosuitWing", 1L);
        addFullHazmatToGeneralItem(ElectroMagicTools.ID, "QuantumWing", 1L);

        // Thaumic Boots Hazmat
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemQuantumVoid", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemQuantumMeteor", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemQuantumComet", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemNanoVoid", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemNanoMeteor", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemNanoComet", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemElectricVoid", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemVoidMeteor", 1L);
        addFullHazmatToGeneralItem(ThaumicBoots.ID, "item.ItemVoidComet", 1L);

        // DraconicEvolution Hazmat
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "draconicBoots", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "draconicHelm", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "draconicLeggs", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "draconicChest", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "wyvernBoots", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "wyvernHelm", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "wyvernLeggs", 1L, 0);
        addFullHazmatToGeneralItem(DraconicEvolution.ID, "wyvernChest", 1L, 0);

        // AdvancedSolarPanel
        addFullHazmatToGeneralItem(AdvancedSolarPanel.ID, "advanced_solar_helmet", 1L);
        addFullHazmatToGeneralItem(AdvancedSolarPanel.ID, "hybrid_solar_helmet", 1L);
        addFullHazmatToGeneralItem(AdvancedSolarPanel.ID, "ultimate_solar_helmet", 1L);

        // TaintedMagic Hazmat
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemVoidwalkerBoots", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemShadowFortressHelmet", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemShadowFortressChestplate", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemShadowFortressLeggings", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemVoidFortressHelmet", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemVoidFortressChestplate", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemVoidFortressLeggings", 1L);
        addFullHazmatToGeneralItem(TaintedMagic.ID, "ItemVoidmetalGoggles", 1L);

        // WitchingGadgets Hazmat
        addFullHazmatToGeneralItem(WitchingGadgets.ID, "item.WG_PrimordialHelm", 1L);
        addFullHazmatToGeneralItem(WitchingGadgets.ID, "item.WG_PrimordialChest", 1L);
        addFullHazmatToGeneralItem(WitchingGadgets.ID, "item.WG_PrimordialLegs", 1L);
        addFullHazmatToGeneralItem(WitchingGadgets.ID, "item.WG_PrimordialBoots", 1L);

        // ThaumicTinkerer Hazmat
        addFullHazmatToGeneralItem(ThaumicTinkerer.ID, "ichorclothChestGem", 1L);
        addFullHazmatToGeneralItem(ThaumicTinkerer.ID, "ichorclothBootsGem", 1L);
        addFullHazmatToGeneralItem(ThaumicTinkerer.ID, "ichorclothHelmGem", 1L);
        addFullHazmatToGeneralItem(ThaumicTinkerer.ID, "ichorclothLegsGem", 1L);

        // GalaxySpace Hazmat
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_helmet", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_helmetglasses", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_plate", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_jetplate", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_leg", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_boots", 1L);
        addFullHazmatToGeneralItem(GalaxySpace.ID, "item.spacesuit_gravityboots", 1L);

        // Extra Hazmat
        GregTech_API.sElectroHazmatList.add(new ItemStack(Items.chainmail_helmet, 1, W));
        GregTech_API.sElectroHazmatList.add(new ItemStack(Items.chainmail_chestplate, 1, W));
        GregTech_API.sElectroHazmatList.add(new ItemStack(Items.chainmail_leggings, 1, W));
        GregTech_API.sElectroHazmatList.add(new ItemStack(Items.chainmail_boots, 1, W));

        // Infinity Hazmat
        addFullHazmatToGeneralItem(Avaritia.ID, "Infinity_Helm", 1L);
        addFullHazmatToGeneralItem(Avaritia.ID, "Infinity_Chest", 1L);
        addFullHazmatToGeneralItem(Avaritia.ID, "Infinity_Pants", 1L);
        addFullHazmatToGeneralItem(Avaritia.ID, "Infinity_Shoes", 1L);

        // EnderIO Hazmat
        addFullHazmatToGeneralItem(EnderIO.ID, "item.endSteel_helmet", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.endSteel_chestplate", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.endSteel_leggings", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.endSteel_boots", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.stellar_helmet", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.stellar_chestplate", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.stellar_leggings", 1L);
        addFullHazmatToGeneralItem(EnderIO.ID, "item.stellar_boots", 1L);

        GregTech_API.sLoadStarted = true;
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GT_LanguageManager.writePlaceholderStrings();
    }

    public static long tBits = GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED
        | GT_ModHandler.RecipeBits.ONLY_ADD_IF_RESULT_IS_NOT_NULL
        | GT_ModHandler.RecipeBits.NOT_REMOVABLE;

    public void onPostLoad() {
        GT_Log.out.println("GT_Mod: Beginning PostLoad-Phase.");
        GT_Log.ore.println("GT_Mod: Beginning PostLoad-Phase.");
        if (GT_Log.pal != null) {
            final Thread playerActivityLogger = new Thread(new GT_PlayerActivityLogger());
            playerActivityLogger.setDaemon(true);
            playerActivityLogger.setName("GT5U Player activity logger");
            playerActivityLogger.start();
        }
        GregTech_API.sPostloadStarted = true;

        // This needs to happen late enough that all of the fluids we need have been registered.
        // onLoad() seems to be too early, as the New Horizons Core Mod registers some fluids in post-load.
        GT_MetaGenerated_Item_98.init();

        GT_OreDictUnificator.addItemData(new ItemStack(Items.iron_door, 1), new ItemData(Materials.Iron, 21772800L));
        GT_OreDictUnificator
            .addItemData(new ItemStack(Items.wooden_door, 1, 32767), new ItemData(Materials.Wood, 21772800L));
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GT_Log.out.println("GT_Mod: Adding Configs specific for MetaTileEntities");
        try {
            for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                for (; i < GregTech_API.METATILEENTITIES.length; i++) {
                    if (GregTech_API.METATILEENTITIES[i] != null) {
                        GregTech_API.METATILEENTITIES[i].onConfigLoad(GregTech_API.sMachineFile);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        GT_Log.out.println("GT_Mod: Adding Tool Usage Crafting Recipes for OreDict Items.");
        for (Materials aMaterial : Materials.values()) {
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.crushedCentrifuged.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.crystalline.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.crystal.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.crushedPurified.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.cleanGravel.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.reduced.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.clump.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.shard.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.crushed.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                    tBits,
                    new Object[] { "h", "X", 'X', OrePrefixes.dirtyGravel.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 4L),
                    tBits,
                    new Object[] { " X ", 'X', OrePrefixes.dust.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 9L),
                    tBits,
                    new Object[] { "X  ", 'X', OrePrefixes.dust.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    tBits,
                    new Object[] { "XX", "XX", 'X', OrePrefixes.dustSmall.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    tBits,
                    new Object[] { "XXX", "XXX", "XXX", 'X', OrePrefixes.dustTiny.get(aMaterial) });
            }
        }
    }

    public void onLoadComplete() {}

    public void onServerAboutToStart() {
        dimensionWisePollution.clear(); // !!! IMPORTANT for map switching...
        GT_ChunkAssociatedData.clearAll();
    }

    public void onServerStarting() {
        GT_Log.out.println("GT_Mod: ServerStarting-Phase started!");
        GT_Log.ore.println("GT_Mod: ServerStarting-Phase started!");

        this.mUniverse = null;
        this.isFirstServerWorldTick = true;
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        try {
            for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                for (; i < GregTech_API.METATILEENTITIES.length; i++) {
                    if (GregTech_API.METATILEENTITIES[i] != null) {
                        GregTech_API.METATILEENTITIES[i].onServerStart();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    public void onServerStarted() {
        GregTech_API.sWirelessRedstone.clear();
        GT_MetaTileEntity_DroneCentre.getCentreMap()
            .clear();
        GT_Log.out.println(
            "GT_Mod: Cleaning up all OreDict Crafting Recipes, which have an empty List in them, since they are never meeting any Condition.");
        List<IRecipe> tList = CraftingManager.getInstance()
            .getRecipeList();
        for (int i = 0; i < tList.size(); i++) {
            if ((tList.get(i) instanceof ShapedOreRecipe)) {
                for (Object tObject : ((ShapedOreRecipe) tList.get(i)).getInput()) {
                    if (((tObject instanceof List)) && (((List<?>) tObject).isEmpty())) {
                        tList.remove(i--);
                        break;
                    }
                }
            } else if ((tList.get(i) instanceof ShapelessOreRecipe)) {
                for (Object tObject : ((ShapelessOreRecipe) tList.get(i)).getInput()) {
                    if (((tObject instanceof List)) && (((List<?>) tObject).isEmpty())) {
                        tList.remove(i--);
                        break;
                    }
                }
            }
        }
    }

    public void onServerStopping() {
        File tSaveDirectory = getSaveDirectory();
        GregTech_API.sWirelessRedstone.clear();
        if (tSaveDirectory != null) {
            try {
                for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                    for (; i < GregTech_API.METATILEENTITIES.length; i++) {
                        if (GregTech_API.METATILEENTITIES[i] != null) {
                            GregTech_API.METATILEENTITIES[i].onWorldSave(tSaveDirectory);
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }
        this.mUniverse = null;
        // GT_ChunkAssociatedData.saveAll(); todo: figure out if this is needed

    }

    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent aEvent) {}

    /**
     * Tells {@link gregtech.nei.GT_NEI_DefaultHandler} to reload recipes.
     */
    public void reloadNEICache() {}

    /**
     * Logging in to server or {@link #reloadNEICache} being called increases the count.
     */
    public int getNEIReloadCount() {
        return 0;
    }

    @SubscribeEvent
    public void onArrowNockEvent(ArrowNockEvent aEvent) {
        if ((!aEvent.isCanceled()) && (GT_Utility.isStackValid(aEvent.result))
            && (GT_Utility.getProjectile(SubTag.PROJECTILE_ARROW, aEvent.entityPlayer.inventory) != null)) {
            aEvent.entityPlayer.setItemInUse(
                aEvent.result,
                aEvent.result.getItem()
                    .getMaxItemUseDuration(aEvent.result));
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onArrowLooseEvent(ArrowLooseEvent aEvent) {
        ItemStack aArrow = GT_Utility.getProjectile(SubTag.PROJECTILE_ARROW, aEvent.entityPlayer.inventory);
        if ((!aEvent.isCanceled()) && (GT_Utility.isStackValid(aEvent.bow))
            && (aArrow != null)
            && ((aEvent.bow.getItem() instanceof ItemBow))) {
            float tSpeed = aEvent.charge / 20.0F;
            tSpeed = (tSpeed * tSpeed + tSpeed * 2.0F) / 3.0F;
            if (tSpeed < 0.1D) {
                return;
            }
            if (tSpeed > 1.0D) {
                tSpeed = 1.0F;
            }
            EntityArrow tArrowEntity = ((IProjectileItem) aArrow.getItem()).getProjectile(
                SubTag.PROJECTILE_ARROW,
                aArrow,
                aEvent.entityPlayer.worldObj,
                aEvent.entityPlayer,
                tSpeed * 2.0F);
            if (tSpeed >= 1.0F) {
                tArrowEntity.setIsCritical(true);
            }
            int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, aEvent.bow);
            if (tLevel > 0) {
                tArrowEntity.setDamage(tArrowEntity.getDamage() + tLevel * 0.5D + 0.5D);
            }
            tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, aEvent.bow);
            if (tLevel > 0) {
                tArrowEntity.setKnockbackStrength(tLevel);
            }
            tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, aEvent.bow);
            if (tLevel > 0) {
                tArrowEntity.setFire(tLevel * 100);
            }
            aEvent.bow.damageItem(1, aEvent.entityPlayer);
            aEvent.bow.getItem();

            new WorldSpawnedEventBuilder.SoundAtEntityEventBuilder().setPitch(0.64893958288F + tSpeed * 0.5F)
                .setVolume(1f)
                .setIdentifier(SoundResource.RANDOM_BOW)
                .setEntity(aEvent.entityPlayer)
                .setWorld(aEvent.entityPlayer.worldObj)
                .run();

            tArrowEntity.canBePickedUp = 1;
            if (!aEvent.entityPlayer.capabilities.isCreativeMode) {
                aArrow.stackSize -= 1;
            }
            if (aArrow.stackSize == 0) {
                GT_Utility.removeNullStacksFromInventory(aEvent.entityPlayer.inventory);
            }
            if (!aEvent.entityPlayer.worldObj.isRemote) {
                aEvent.entityPlayer.worldObj.spawnEntityInWorld(tArrowEntity);
            }
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEndermanTeleportEvent(EnderTeleportEvent aEvent) {
        if (((aEvent.entityLiving instanceof EntityEnderman))
            && (aEvent.entityLiving.getActivePotionEffect(Potion.weakness) != null)) {
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntitySpawningEvent(EntityJoinWorldEvent aEvent) {
        if ((aEvent.entity != null) && (!aEvent.entity.worldObj.isRemote)) {
            if ((aEvent.entity instanceof EntityItem)) {
                ((EntityItem) aEvent.entity).setEntityItemStack(
                    GT_OreDictUnificator.get(true, ((EntityItem) aEvent.entity).getEntityItem(), true));
            }
            if ((this.mSkeletonsShootGTArrows > 0) && (aEvent.entity.getClass() == EntityArrow.class)
                && (aEvent.entity.worldObj.rand.nextInt(this.mSkeletonsShootGTArrows) == 0)
                && ((((EntityArrow) aEvent.entity).shootingEntity instanceof EntitySkeleton))) {
                aEvent.entity.worldObj.spawnEntityInWorld(
                    new GT_Entity_Arrow(
                        (EntityArrow) aEvent.entity,
                        OrePrefixes.arrowGtWood.mPrefixedItems
                            .get(aEvent.entity.worldObj.rand.nextInt(OrePrefixes.arrowGtWood.mPrefixedItems.size()))));
                aEvent.entity.setDead();
            }
        }
    }

    @SubscribeEvent
    public void onOreGenEvent(OreGenEvent.GenerateMinable aGenerator) {
        if ((this.mDisableVanillaOres) && ((aGenerator.generator instanceof WorldGenMinable))
            && (PREVENTED_ORES.contains(aGenerator.type))) {
            aGenerator.setResult(Result.DENY);
        }
    }

    private String getDataAndTime() {
        return this.mDateFormat.format(new Date());
    }

    @SubscribeEvent
    public void onPlayerInteraction(PlayerInteractEvent aEvent) {
        if ((aEvent.entityPlayer == null) || (aEvent.entityPlayer.worldObj == null)
            || (aEvent.action == null)
            || (aEvent.world.provider == null)) {
            return;
        }
        if ((!aEvent.entityPlayer.worldObj.isRemote) && (aEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
            && (GT_Log.pal != null)) {
            this.mBufferedPlayerActivity.offer(
                getDataAndTime() + ";"
                    + aEvent.action.name()
                    + ";"
                    + aEvent.entityPlayer.getDisplayName()
                    + ";DIM:"
                    + aEvent.world.provider.dimensionId
                    + ";"
                    + aEvent.x
                    + ";"
                    + aEvent.y
                    + ";"
                    + aEvent.z
                    + ";|;"
                    + aEvent.x / 10
                    + ";"
                    + aEvent.y / 10
                    + ";"
                    + aEvent.z / 10);
        }
        ItemStack aStack = aEvent.entityPlayer.getCurrentEquippedItem();
        if ((aStack != null) && (aEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            && (aStack.getItem() == Items.flint_and_steel)) {
            if ((!aEvent.world.isRemote) && (!aEvent.entityPlayer.capabilities.isCreativeMode)
                && (aEvent.world.rand.nextInt(100) >= this.mFlintChance)) {
                aEvent.setCanceled(true);
                aStack.damageItem(1, aEvent.entityPlayer);
                if (aStack.getItemDamage() >= aStack.getMaxDamage()) {
                    aStack.stackSize -= 1;
                }
                if (aStack.stackSize <= 0) {
                    ForgeEventFactory.onPlayerDestroyItem(aEvent.entityPlayer, aStack);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreakingEvent(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player == null) return;

        ItemStack item = event.getPlayer()
            .getCurrentEquippedItem();
        if (item == null) return;

        if (!(item.getItem() instanceof GT_MetaGenerated_Tool tool)) return;

        IToolStats stats = tool.getToolStats(item);
        if (stats == null) return;

        TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
        stats.onBreakBlock(player, event.x, event.y, event.z, event.block, (byte) event.blockMetadata, tile, event);
    }

    @SubscribeEvent
    public void onBlockHarvestingEvent(BlockEvent.HarvestDropsEvent aEvent) {
        if (aEvent.harvester == null) return;

        if ((!aEvent.world.isRemote) && (GT_Log.pal != null)) {
            this.mBufferedPlayerActivity.offer(
                getDataAndTime() + ";HARVEST_BLOCK;"
                    + aEvent.harvester.getDisplayName()
                    + ";DIM:"
                    + aEvent.world.provider.dimensionId
                    + ";"
                    + aEvent.x
                    + ";"
                    + aEvent.y
                    + ";"
                    + aEvent.z
                    + ";|;"
                    + aEvent.x / 10
                    + ";"
                    + aEvent.y / 10
                    + ";"
                    + aEvent.z / 10);
        }

        ItemStack aStack = aEvent.harvester.getCurrentEquippedItem();
        if (aStack == null) return;

        if ((aStack.getItem() instanceof GT_MetaGenerated_Tool tool)) {
            tool.onHarvestBlockEvent(
                aEvent.drops,
                aStack,
                aEvent.harvester,
                aEvent.block,
                aEvent.x,
                aEvent.y,
                aEvent.z,
                (byte) aEvent.blockMetadata,
                aEvent.fortuneLevel,
                aEvent.isSilkTouching,
                aEvent);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, aStack) > 2) {
            try {
                for (ItemStack tDrop : aEvent.drops) {
                    ItemStack tSmeltingOutput = GT_ModHandler.getSmeltingOutput(tDrop, false, null);
                    if (tSmeltingOutput != null) {
                        tDrop.stackSize *= tSmeltingOutput.stackSize;
                        tSmeltingOutput.stackSize = tDrop.stackSize;
                        GT_Utility.setStack(tDrop, tSmeltingOutput);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

    }

    @SubscribeEvent
    public void registerOre(OreDictionary.OreRegisterEvent aEvent) {
        ModContainer tContainer = Loader.instance()
            .activeModContainer();
        String aMod = tContainer == null ? "UNKNOWN" : tContainer.getModId();
        String aOriginalMod = aMod;
        if (GT_OreDictUnificator.isRegisteringOres()) {
            aMod = GregTech.ID;
        } else if (aMod.equals(GregTech.ID)) {
            aMod = "UNKNOWN";
        }
        if ((aEvent == null) || (aEvent.Ore == null)
            || (aEvent.Ore.getItem() == null)
            || (aEvent.Name == null)
            || (aEvent.Name.isEmpty())
            || (aEvent.Name.replaceAll("_", "")
                .length() - aEvent.Name.length() == 9)) {
            if (aOriginalMod.equals(GregTech.ID)) {
                aOriginalMod = "UNKNOWN";
            }
            GT_Log.ore.println(
                aOriginalMod
                    + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
            throw new IllegalArgumentException(
                aOriginalMod
                    + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
        }
        try {
            aEvent.Ore.stackSize = 1;
            if (this.mIgnoreTcon || aEvent.Ore.getUnlocalizedName()
                .startsWith("item.oreberry")) {
                if ((aOriginalMod.toLowerCase(Locale.ENGLISH)
                    .contains("xycraft"))
                    || (aOriginalMod.toLowerCase(Locale.ENGLISH)
                        .contains("tconstruct"))) {
                    if (GT_Values.D1) {
                        GT_Log.ore.println(aMod + " -> " + aEvent.Name + " is getting ignored, because of racism. :P");
                    }
                    return;
                }
            }
            String tModToName = aMod + " -> " + aEvent.Name;
            if ((this.mOreDictActivated) || (GregTech_API.sPostloadStarted)
                || ((this.mSortToTheEnd) && (GregTech_API.sLoadFinished))) {
                tModToName = aOriginalMod + " --Late--> " + aEvent.Name;
            }
            if (((aEvent.Ore.getItem() instanceof ItemBlock))
                || (GT_Utility.getBlockFromStack(aEvent.Ore) != Blocks.air)) {
                GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
            }
            this.mRegisteredOres.add(aEvent.Ore);
            if (this.mIgnoredItems.contains(aEvent.Name)) {
                if ((aEvent.Name.startsWith("item"))) {
                    GT_Log.ore.println(tModToName);
                    if (aEvent.Name.equals("itemCopperWire")) {
                        GT_OreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                    }
                    if (aEvent.Name.equals("itemRubber")) {
                        GT_OreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Rubber, aEvent.Ore);
                    }
                    return;
                }
            } else if (this.mIgnoredNames.contains(aEvent.Name)) {
                GT_Log.ore.println(tModToName + " is getting ignored via hardcode.");
                return;
            } else if (aEvent.Name.equals("stone")) {
                GT_OreDictUnificator.registerOre("stoneSmooth", aEvent.Ore);
                return;
            } else if (aEvent.Name.equals("cobblestone")) {
                GT_OreDictUnificator.registerOre("stoneCobble", aEvent.Ore);
                return;
            } else if ((aEvent.Name.contains("|")) || (aEvent.Name.contains("*"))
                || (aEvent.Name.contains(":"))
                || (aEvent.Name.contains("."))
                || (aEvent.Name.contains("$"))) {
                    GT_Log.ore
                        .println(tModToName + " is using a private Prefix and is therefor getting ignored properly.");
                    return;
                } else if (aEvent.Name.equals("copperWire")) {
                    GT_OreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                } else if (aEvent.Name.equals("oreHeeEndrium")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.ore, Materials.HeeEndium, aEvent.Ore);
                } else if (aEvent.Name.equals("sheetPlastic")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.plate, Materials.Plastic, aEvent.Ore);
                } else if (aEvent.Name.startsWith("shard")) {
                    switch (aEvent.Name) {
                        case "shardAir" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedAir, aEvent.Ore);
                            return;
                        }
                        case "shardWater" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedWater, aEvent.Ore);
                            return;
                        }
                        case "shardFire" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedFire, aEvent.Ore);
                            return;
                        }
                        case "shardEarth" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEarth, aEvent.Ore);
                            return;
                        }
                        case "shardOrder" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedOrder, aEvent.Ore);
                            return;
                        }
                        case "shardEntropy" -> {
                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEntropy, aEvent.Ore);
                            return;
                        }
                    }
                } else if (aEvent.Name.equals("fieryIngot")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.ingot, Materials.FierySteel, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("ironwood")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.ingot, Materials.IronWood, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("steeleaf")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Steeleaf, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("knightmetal")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Knightmetal, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("compressedAluminum")) {
                    GT_OreDictUnificator.registerOre(OrePrefixes.compressed, Materials.Aluminium, aEvent.Ore);
                    return;
                } else if (aEvent.Name.contains(" ")) {
                    GT_Log.ore.println(
                        tModToName + " is getting re-registered because the OreDict Name containing invalid spaces.");
                    GT_OreDictUnificator
                        .registerOre(aEvent.Name.replaceAll(" ", ""), GT_Utility.copyAmount(1, aEvent.Ore));
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                } else if (this.mInvalidNames.contains(aEvent.Name)) {
                    GT_Log.ore.println(tModToName + " is wrongly registered and therefor getting ignored.");

                    return;
                }
            OrePrefixes aPrefix = OrePrefixes.getOrePrefix(aEvent.Name);
            Materials aMaterial = Materials._NULL;
            if ((aPrefix == OrePrefixes.nugget) && (aMod.equals(Thaumcraft.ID))
                && (aEvent.Ore.getItem()
                    .getUnlocalizedName()
                    .contains("ItemResource"))) {
                return;
            }
            if (aPrefix == null) {
                if (aEvent.Name.toLowerCase()
                    .equals(aEvent.Name)) {
                    GT_Log.ore.println(tModToName + " is invalid due to being solely lowercased.");
                    return;
                } else if (aEvent.Name.toUpperCase()
                    .equals(aEvent.Name)) {
                        GT_Log.ore.println(tModToName + " is invalid due to being solely uppercased.");
                        return;
                    } else if (Character.isUpperCase(aEvent.Name.charAt(0))) {
                        GT_Log.ore.println(tModToName + " is invalid due to the first character being uppercased.");
                    }
            } else {
                if (aPrefix.mDontUnificateActively) {
                    GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                }
                if (aPrefix != aPrefix.mPrefixInto) {
                    String tNewName = aEvent.Name.replaceFirst(aPrefix.toString(), aPrefix.mPrefixInto.toString());
                    if (!GT_OreDictUnificator.isRegisteringOres()) {
                        GT_Log.ore.println(
                            tModToName + " uses a depricated Prefix, and is getting re-registered as " + tNewName);
                    }
                    GT_OreDictUnificator.registerOre(tNewName, aEvent.Ore);
                    return;
                }
                String tName = aEvent.Name.replaceFirst(aPrefix.toString(), "");
                if (tName.length() > 0) {
                    char firstChar = tName.charAt(0);
                    if (Character.isUpperCase(firstChar) || Character.isLowerCase(firstChar)
                        || firstChar == '_'
                        || Character.isDigit(firstChar)) {
                        if (aPrefix.mIsMaterialBased) {
                            aMaterial = Materials.get(tName);
                            if (aMaterial != aMaterial.mMaterialInto) {
                                GT_OreDictUnificator.registerOre(aPrefix, aMaterial.mMaterialInto, aEvent.Ore);
                                if (!GT_OreDictUnificator.isRegisteringOres()) {
                                    GT_Log.ore.println(
                                        tModToName + " uses a deprecated Material and is getting re-registered as "
                                            + aPrefix.get(aMaterial.mMaterialInto));
                                }
                                return;
                            }
                            if (!aPrefix.isIgnored(aMaterial)) {
                                aPrefix.add(GT_Utility.copyAmount(1, aEvent.Ore));
                            }
                            if (aMaterial != Materials._NULL) {
                                Materials tReRegisteredMaterial;
                                for (Iterator<Materials> i$ = aMaterial.mOreReRegistrations.iterator(); i$
                                    .hasNext(); GT_OreDictUnificator
                                        .registerOre(aPrefix, tReRegisteredMaterial, aEvent.Ore)) {
                                    tReRegisteredMaterial = i$.next();
                                }
                                aMaterial.add(GT_Utility.copyAmount(1, aEvent.Ore));

                                if (GregTech_API.sThaumcraftCompat != null && aPrefix.doGenerateItem(aMaterial)
                                    && !aPrefix.isIgnored(aMaterial)) {
                                    List<TC_AspectStack> tAspects = new ArrayList<>();
                                    for (TC_AspectStack tAspect : aPrefix.mAspects) tAspect.addToAspectList(tAspects);
                                    if (aPrefix.mMaterialAmount >= 3628800 || aPrefix.mMaterialAmount < 0)
                                        for (TC_AspectStack tAspect : aMaterial.mAspects)
                                            tAspect.addToAspectList(tAspects);
                                    GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(
                                        GT_Utility.copyAmount(1, aEvent.Ore),
                                        tAspects,
                                        aEvent.Name);
                                }

                                switch (aPrefix) {
                                    case crystal -> {
                                        if ((aMaterial == Materials.CertusQuartz)
                                            || (aMaterial == Materials.NetherQuartz)
                                            || (aMaterial == Materials.Fluix)) {
                                            GT_OreDictUnificator.registerOre(OrePrefixes.gem, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case gem -> {
                                        if (aMaterial == Materials.Lapis || aMaterial == Materials.Sodalite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else
                                            if (aMaterial == Materials.InfusedAir || aMaterial == Materials.InfusedWater
                                                || aMaterial == Materials.InfusedFire
                                                || aMaterial == Materials.InfusedEarth
                                                || aMaterial == Materials.InfusedOrder
                                                || aMaterial == Materials.InfusedEntropy) {
                                                    GT_OreDictUnificator.registerOre(
                                                        aMaterial.mName.replaceFirst("Infused", "shard"),
                                                        aEvent.Ore);
                                                } else if (aMaterial == Materials.Chocolate) {
                                                    GT_OreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                                } else if (aMaterial == Materials.CertusQuartz
                                                    || aMaterial == Materials.NetherQuartz) {
                                                        GT_OreDictUnificator
                                                            .registerOre(OrePrefixes.item.get(aMaterial), aEvent.Ore);
                                                        GT_OreDictUnificator
                                                            .registerOre(OrePrefixes.crystal, aMaterial, aEvent.Ore);
                                                        GT_OreDictUnificator
                                                            .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                    } else
                                                    if (aMaterial == Materials.Fluix || aMaterial == Materials.Quartz
                                                        || aMaterial == Materials.Quartzite) {
                                                            GT_OreDictUnificator.registerOre(
                                                                OrePrefixes.crystal,
                                                                aMaterial,
                                                                aEvent.Ore);
                                                            GT_OreDictUnificator
                                                                .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                        }
                                    }
                                    case cableGt01 -> {
                                        if (aMaterial == Materials.Tin) {
                                            GT_OreDictUnificator.registerOre(OreDictNames.craftingWireTin, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyCopper) {
                                            GT_OreDictUnificator
                                                .registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                                        } else if (aMaterial == Materials.Gold) {
                                            GT_OreDictUnificator.registerOre(OreDictNames.craftingWireGold, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyIron) {
                                            GT_OreDictUnificator.registerOre(OreDictNames.craftingWireIron, aEvent.Ore);
                                        }
                                    }
                                    case lens -> {
                                        if ((aMaterial.contains(SubTag.TRANSPARENT))
                                            && (aMaterial.mColor != Dyes._NULL)) {
                                            GT_OreDictUnificator.registerOre(
                                                "craftingLens" + aMaterial.mColor.toString()
                                                    .replaceFirst("dye", ""),
                                                aEvent.Ore);
                                        }
                                    }
                                    case plate -> {
                                        if ((aMaterial == Materials.Plastic) || (aMaterial == Materials.Rubber)) {
                                            GT_OreDictUnificator.registerOre(OrePrefixes.sheet, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Silicon) {
                                            GT_OreDictUnificator.registerOre(OrePrefixes.item, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                                            GT_OreDictUnificator.registerOre(OrePrefixes.plank, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case cell -> {
                                        if (aMaterial == Materials.Empty) {
                                            GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                                        }
                                    }
                                    case gearGt -> GT_OreDictUnificator
                                        .registerOre(OrePrefixes.gear, aMaterial, aEvent.Ore);
                                    case stick -> {
                                        if (!GT_RecipeRegistrator.sRodMaterialList.contains(aMaterial)) {
                                            GT_RecipeRegistrator.sRodMaterialList.add(aMaterial);
                                        } else if (aMaterial == Materials.Wood) {
                                            GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                                        } else if ((aMaterial == Materials.Tin) || (aMaterial == Materials.Lead)
                                            || (aMaterial == Materials.SolderingAlloy)) {
                                                GT_OreDictUnificator
                                                    .registerOre(ToolDictNames.craftingToolSolderingMetal, aEvent.Ore);
                                            }
                                    }
                                    case dust -> {
                                        if (aMaterial == Materials.Salt) {
                                            GT_OreDictUnificator.registerOre("itemSalt", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GT_OreDictUnificator.registerOre("pulpWood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wheat) {
                                            GT_OreDictUnificator.registerOre("foodFlour", aEvent.Ore);
                                        } else if (aMaterial == Materials.Lapis) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else if (aMaterial == Materials.Sodalite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Cocoa) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                            GT_OreDictUnificator.registerOre("foodCocoapowder", aEvent.Ore);
                                        } else if (aMaterial == Materials.Coffee) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.BrownLimonite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.YellowLimonite) {
                                            GT_OreDictUnificator.registerOre(Dyes.dyeYellow, aEvent.Ore);
                                        }
                                    }
                                    case ingot -> {
                                        if (aMaterial == Materials.Rubber) {
                                            GT_OreDictUnificator.registerOre("itemRubber", aEvent.Ore);
                                        } else if (aMaterial == Materials.FierySteel) {
                                            GT_OreDictUnificator.registerOre("fieryIngot", aEvent.Ore);
                                        } else if (aMaterial == Materials.IronWood) {
                                            GT_OreDictUnificator.registerOre("ironwood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Steeleaf) {
                                            GT_OreDictUnificator.registerOre("steeleaf", aEvent.Ore);
                                        } else if (aMaterial == Materials.Knightmetal) {
                                            GT_OreDictUnificator.registerOre("knightmetal", aEvent.Ore);
                                        } else if ((aMaterial == Materials.Brass) && (aEvent.Ore.getItemDamage() == 2)
                                            && (aEvent.Ore.getUnlocalizedName()
                                                .equals("item.ingotBrass"))
                                            && (new ItemStack(aEvent.Ore.getItem(), 1, 0).getUnlocalizedName()
                                                .contains("red"))) {
                                                    GT_OreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.RedAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 0));
                                                    GT_OreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.BlueAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 1));
                                                    GT_OreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.Brass,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 2));
                                                    if (!mDisableIC2Cables) {
                                                        GT_Values.RA.stdBuilder()
                                                            .itemInputs(GT_ModHandler.getIC2Item("copperCableItem", 3L))
                                                            .itemOutputs(new ItemStack(aEvent.Ore.getItem(), 1, 8))
                                                            .duration(20 * SECONDS)
                                                            .eut(1)
                                                            .addTo(wiremillRecipes);
                                                        GT_Values.RA.stdBuilder()
                                                            .itemInputs(GT_ModHandler.getIC2Item("ironCableItem", 6L))
                                                            .itemOutputs(new ItemStack(aEvent.Ore.getItem(), 1, 9))
                                                            .duration(20 * SECONDS)
                                                            .eut(2)
                                                            .addTo(wiremillRecipes);
                                                    }

                                                    GT_Values.RA.stdBuilder()
                                                        .itemInputs(new ItemStack(aEvent.Ore.getItem(), 1, 3))
                                                        .itemOutputs(new ItemStack(aEvent.Ore.getItem(), 16, 4))
                                                        .duration(20 * SECONDS)
                                                        .eut(8)
                                                        .addTo(cutterRecipes);
                                                }
                                    }
                                    default -> {}
                                }
                                if (aPrefix.mIsUnificatable && !aMaterial.mUnificatable) {
                                    return;
                                }
                            } else {
                                for (Dyes tDye : Dyes.VALUES) {
                                    if (aEvent.Name.endsWith(
                                        tDye.name()
                                            .replaceFirst("dye", ""))) {
                                        GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                                        GT_Log.ore.println(
                                            tModToName
                                                + " Oh man, why the fuck would anyone need a OreDictified Color for this, that is even too much for GregTech... do not report this, this is just a random Comment about how ridiculous this is.");
                                        return;
                                    }
                                }
                                // GT_FML_LOGGER.info("Material Name: "+aEvent.Name+ "
                                // !!!Unknown Material detected!!! Please report to GregTech Intergalactical for
                                // additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just
                                // an Information, which you should pass to me.");
                                // GT_Log.ore.println(tModToName + " uses an unknown
                                // Material. Report this to GregTech.");
                                return;
                            }
                        } else {
                            aPrefix.add(GT_Utility.copyAmount(1, aEvent.Ore));
                        }
                    }
                } else if (aPrefix.mIsSelfReferencing) {
                    aPrefix.add(GT_Utility.copyAmount(1, aEvent.Ore));
                } else {
                    GT_Log.ore.println(tModToName + " uses a Prefix as full OreDict Name, and is therefor invalid.");
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                }
                switch (aPrefix) {
                    case dye -> {
                        if (GT_Utility.isStringValid(tName)) {
                            GT_OreDictUnificator.registerOre(OrePrefixes.dye, aEvent.Ore);
                        }
                    }
                    case stoneSmooth -> GT_OreDictUnificator.registerOre("stone", aEvent.Ore);
                    case stoneCobble -> GT_OreDictUnificator.registerOre("cobblestone", aEvent.Ore);
                    case plank -> {
                        if (tName.equals("Wood")) {
                            GT_OreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 3628800L));
                        }
                    }
                    case slab -> {
                        if (tName.equals("Wood")) {
                            GT_OreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 1814400L));
                        }
                    }
                    case sheet -> {
                        if (tName.equals("Plastic")) {
                            GT_OreDictUnificator.registerOre(OrePrefixes.plate, Materials.Plastic, aEvent.Ore);
                        } else if (tName.equals("Rubber")) {
                            GT_OreDictUnificator.registerOre(OrePrefixes.plate, Materials.Rubber, aEvent.Ore);
                        }
                    }
                    case crafting -> {
                        switch (tName) {
                            case "ToolSolderingMetal" -> GregTech_API.registerSolderingMetal(aEvent.Ore);
                            case "IndustrialDiamond" -> GT_OreDictUnificator.addToBlacklist(aEvent.Ore);
                            case "WireCopper" -> GT_OreDictUnificator
                                .registerOre(OrePrefixes.wire, Materials.Copper, aEvent.Ore);
                        }
                    }
                    case wood -> {
                        if (tName.equals("Rubber")) {
                            GT_OreDictUnificator.registerOre("logRubber", aEvent.Ore);
                        }
                    }
                    case food -> {
                        if (tName.equals("Cocoapowder")) {
                            GT_OreDictUnificator.registerOre(OrePrefixes.dust, Materials.Cocoa, aEvent.Ore);
                        }
                    }
                    default -> {}
                }
            }
            GT_Log.ore.println(tModToName);

            OreDictEventContainer tOre = new OreDictEventContainer(aEvent, aPrefix, aMaterial, aMod);
            if ((!this.mOreDictActivated) || (!GregTech_API.sUnificationEntriesRegistered)) {
                this.mEvents.add(tOre);
            } else {
                this.mEvents.clear();
            }
            if (this.mOreDictActivated) {
                registerRecipes(tOre);
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @SuppressWarnings("deprecated")
    public static void stepMaterialsVanilla(Collection<GT_Proxy.OreDictEventContainer> mEvents,
        ProgressManager.ProgressBar progressBar) {
        int size = 5;
        int sizeStep = mEvents.size() / 20 - 1;
        GT_Proxy.OreDictEventContainer tEvent;
        for (Iterator<GT_Proxy.OreDictEventContainer> i$ = mEvents.iterator(); i$.hasNext(); GT_Proxy
            .registerRecipes(tEvent)) {
            tEvent = i$.next();
            sizeStep--;
            if (sizeStep == 0) {
                GT_FML_LOGGER.info("Baking : " + size + "%", new Object[0]);
                sizeStep = mEvents.size() / 20 - 1;
                size += 5;
            }
            progressBar.step(tEvent.mMaterial == null ? "" : tEvent.mMaterial.toString());
        }
        ProgressManager.pop(progressBar);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent aEvent) {
        if (aEvent.entityLiving.onGround) {
            int tX = MathHelper.floor_double(aEvent.entityLiving.posX),
                tY = MathHelper.floor_double(aEvent.entityLiving.boundingBox.minY - 0.001F),
                tZ = MathHelper.floor_double(aEvent.entityLiving.posZ);
            Block tBlock = aEvent.entityLiving.worldObj.getBlock(tX, tY, tZ);
            if (tBlock instanceof IBlockOnWalkOver)
                ((IBlockOnWalkOver) tBlock).onWalkOver(aEvent.entityLiving, aEvent.entityLiving.worldObj, tX, tY, tZ);
        }
    }

    @SubscribeEvent
    public void onFluidContainerRegistration(FluidContainerRegistry.FluidContainerRegisterEvent aFluidEvent) {
        if ((aFluidEvent.data.filledContainer.getItem() == Items.potionitem)
            && (aFluidEvent.data.filledContainer.getItemDamage() == 0)) {
            aFluidEvent.data.fluid.amount = 0;
        }
        GT_OreDictUnificator.addToBlacklist(aFluidEvent.data.emptyContainer);
        GT_OreDictUnificator.addToBlacklist(aFluidEvent.data.filledContainer);
        GT_Utility.addFluidContainerData(aFluidEvent.data);
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent aEvent) {
        if (aEvent.side.isServer()) {
            if (aEvent.phase == TickEvent.Phase.START) {
                TICK_LOCK.lock();

            } else {
                TICK_LOCK.unlock();
            }

            // Making sure it is being freed up in order to prevent exploits or Garbage Collection mishaps.
            LAST_BROKEN_TILEENTITY.set(null);
        }
    }

    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent aEvent) {
        if (aEvent.world.provider.dimensionId == 0) mTicksUntilNextCraftSound--;
        if (isFirstWorldTick) {
            for (Runnable runnable : GregTech_API.sFirstWorldTick) runnable.run();
            isFirstWorldTick = false;
            GT_Values.worldTickHappened = true;
        }
        if (aEvent.side.isServer()) {
            if (this.mUniverse == null) {
                this.mUniverse = aEvent.world;
            }
            if (this.isFirstServerWorldTick) {
                File tSaveDiretory = getSaveDirectory();
                if (tSaveDiretory != null) {
                    this.isFirstServerWorldTick = false;
                    try {
                        for (IMetaTileEntity tMetaTileEntity : GregTech_API.METATILEENTITIES) {
                            if (tMetaTileEntity != null) {
                                tMetaTileEntity.onWorldLoad(tSaveDiretory);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace(GT_Log.err);
                    }
                }
            }
            if ((aEvent.world.getTotalWorldTime() % 100L == 0L)
                && ((this.mItemDespawnTime != 6000) || (this.mMaxEqualEntitiesAtOneSpot > 0))) {
                long startTime = System.nanoTime();
                double oldX = 0, oldY = 0, oldZ = 0;
                if (debugEntityCramming && (aEvent.world.loadedEntityList.size() != 0)) {
                    GT_Log.out.println("CRAM: Entity list size " + aEvent.world.loadedEntityList.size());
                }
                for (int i = 0; i < aEvent.world.loadedEntityList.size(); i++) {
                    if ((aEvent.world.loadedEntityList.get(i) instanceof Entity)) {
                        Entity tEntity = aEvent.world.loadedEntityList.get(i);
                        if (((tEntity instanceof EntityItem)) && (this.mItemDespawnTime != 6000)
                            && (((EntityItem) tEntity).lifespan == 6000)) {
                            ((EntityItem) tEntity).lifespan = this.mItemDespawnTime;
                        } else if (((tEntity instanceof EntityLivingBase)) && (this.mMaxEqualEntitiesAtOneSpot > 0)
                            && (!(tEntity instanceof EntityPlayer))
                            && (tEntity.canBePushed())
                            && (((EntityLivingBase) tEntity).getHealth() > 0.0F)) {
                                List<Entity> tList = tEntity.worldObj.getEntitiesWithinAABBExcludingEntity(
                                    tEntity,
                                    tEntity.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
                                Class<? extends Entity> tClass = tEntity.getClass();
                                int tEntityCount = 1;
                                if (tList != null) {
                                    for (Object o : tList) {
                                        if ((o != null) && (o.getClass() == tClass)) {
                                            tEntityCount++;
                                        }
                                    }
                                }
                                if (tEntityCount > this.mMaxEqualEntitiesAtOneSpot) {
                                    if (debugEntityCramming) {
                                        // Cheeseball way of not receiving a bunch of spam caused by 1 location
                                        // obviously fails if there are crammed entities in more than one spot.
                                        if (tEntity.posX != oldX && tEntity.posY != oldY && tEntity.posZ != oldZ) {
                                            GT_Log.out.println(
                                                "CRAM: Excess entities: " + tEntityCount
                                                    + " at X "
                                                    + tEntity.posX
                                                    + " Y "
                                                    + tEntity.posY
                                                    + " Z "
                                                    + tEntity.posZ);
                                            oldX = tEntity.posX;
                                            oldY = tEntity.posY;
                                            oldZ = tEntity.posZ;
                                        }
                                    }
                                    tEntity.attackEntityFrom(
                                        DamageSource.inWall,
                                        tEntityCount - this.mMaxEqualEntitiesAtOneSpot);
                                }
                            }
                    }
                }
                if (debugEntityCramming && (aEvent.world.loadedEntityList.size() != 0)) {
                    GT_Log.out.println(
                        "CRAM: Time spent checking " + (System.nanoTime() - startTime) / 1000 + " microseconds");
                }
            }

            GT_Pollution.onWorldTick(aEvent);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        for (TileEntity tileEntity : event.world.loadedTileEntityList) {
            if (tileEntity instanceof IGregTechTileEntity) {
                tileEntity.onChunkUnload();
            }
        }
    }

    public static void registerRecipes(GT_Proxy.OreDictEventContainer aOre) {
        if ((aOre.mEvent.Ore == null) || (aOre.mEvent.Ore.getItem() == null)) {
            return;
        }
        if (aOre.mEvent.Ore.stackSize != 1) {
            aOre.mEvent.Ore.stackSize = 1;
        }
        if (aOre.mPrefix != null) {
            if (!aOre.mPrefix.isIgnored(aOre.mMaterial)) {
                aOre.mPrefix.processOre(
                    aOre.mMaterial == null ? Materials._NULL : aOre.mMaterial,
                    aOre.mEvent.Name,
                    aOre.mModID,
                    GT_Utility.copyAmount(1, aOre.mEvent.Ore));
            }
        } else {
            // GT_FML_LOGGER.info("Thingy Name: "+ aOre.mEvent.Name+ " !!!Unknown 'Thingy' detected!!! This
            // Object seems to probably not follow a valid OreDictionary Convention, or I missed a Convention. Please
            // report to GregTech Intergalactical for additional compatiblity. This is not an Error, an Issue nor a Lag
            // Source, it is just an Information, which you should pass to me.");
        }
    }

    @SubscribeEvent
    public void onPlayerTickEventServer(TickEvent.PlayerTickEvent aEvent) {
        if ((aEvent.side.isServer()) && (aEvent.phase == TickEvent.Phase.END) && (!aEvent.player.isDead)) {
            if ((aEvent.player.ticksExisted % 200 == 0) && (aEvent.player.capabilities.allowEdit)
                && (!aEvent.player.capabilities.isCreativeMode)
                && (this.mSurvivalIntoAdventure)) {
                aEvent.player.setGameType(GameType.ADVENTURE);
                aEvent.player.capabilities.allowEdit = false;
                if (this.mAxeWhenAdventure) {
                    GT_Utility.sendChatToPlayer(
                        aEvent.player,
                        GT_LanguageManager.addStringLocalization(
                            "Interaction_DESCRIPTION_Index_097",
                            "It's dangerous to go alone! Take this."));
                    aEvent.player.worldObj.spawnEntityInWorld(
                        new EntityItem(
                            aEvent.player.worldObj,
                            aEvent.player.posX,
                            aEvent.player.posY,
                            aEvent.player.posZ,
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.AXE,
                                1,
                                Materials.Flint,
                                Materials.Wood,
                                null)));
                }
            }
            final boolean tHungerEffect = (this.mHungerEffect) && (aEvent.player.ticksExisted % 2400 == 1200);
            if (aEvent.player.ticksExisted % 120 == 0) {
                int tCount = 64;
                for (int i = 0; i < 36; i++) {
                    final ItemStack tStack;
                    if ((tStack = aEvent.player.inventory.getStackInSlot(i)) != null) {
                        if (!aEvent.player.capabilities.isCreativeMode) {
                            GT_Utility.applyRadioactivity(
                                aEvent.player,
                                GT_Utility.getRadioactivityLevel(tStack),
                                tStack.stackSize);
                            final float tHeat = GT_Utility.getHeatDamageFromItem(tStack);
                            if (tHeat != 0.0F) {
                                if (tHeat > 0.0F) {
                                    GT_Utility.applyHeatDamageFromItem(aEvent.player, tHeat, tStack);
                                } else {
                                    GT_Utility.applyFrostDamage(aEvent.player, -tHeat);
                                }
                            }
                        }
                        if (tHungerEffect) {
                            tCount += tStack.stackSize * 64 / Math.max(1, tStack.getMaxStackSize());
                        }
                        if (this.mInventoryUnification) {
                            GT_OreDictUnificator.setStack(true, tStack);
                        }
                    }
                }
                for (int i = 0; i < 4; i++) {
                    final ItemStack tStack;
                    if ((tStack = aEvent.player.inventory.armorInventory[i]) != null) {
                        if (!aEvent.player.capabilities.isCreativeMode) {
                            GT_Utility.applyRadioactivity(
                                aEvent.player,
                                GT_Utility.getRadioactivityLevel(tStack),
                                tStack.stackSize);
                            final float tHeat = GT_Utility.getHeatDamageFromItem(tStack);
                            if (tHeat != 0.0F) {
                                if (tHeat > 0.0F) {
                                    GT_Utility.applyHeatDamageFromItem(aEvent.player, tHeat, tStack);
                                } else {
                                    GT_Utility.applyFrostDamage(aEvent.player, -tHeat);
                                }
                            }
                        }
                        if (tHungerEffect) {
                            tCount += 256;
                        }
                    }
                }
                if (tHungerEffect) {
                    aEvent.player.addExhaustion(Math.max(1.0F, tCount / 666.6F));
                }
            }
        }
    }

    public GT_ClientPreference getClientPreference(UUID aPlayerID) {
        return mClientPrefernces.get(aPlayerID);
    }

    public void setClientPreference(UUID aPlayerID, GT_ClientPreference aPreference) {
        mClientPrefernces.put(aPlayerID, aPreference);
    }

    @Override
    public Object getServerGuiElement(int aID, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
        if (aID >= 1000) {
            return null;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof IGregTechTileEntity)) {
            if (GUI_ID_COVER_SIDE_BASE <= aID && aID < GUI_ID_COVER_SIDE_BASE + 6) {
                return null;
            }
            final IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            if (tMetaTileEntity != null && !tMetaTileEntity.useModularUI()) {
                return tMetaTileEntity.getServerGUI(aID, aPlayer.inventory, (IGregTechTileEntity) tTileEntity);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int aID, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
        if (aID >= 1000) {
            return null;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof IGregTechTileEntity tile)) {

            if (GUI_ID_COVER_SIDE_BASE <= aID && aID < GUI_ID_COVER_SIDE_BASE + 6) {
                final ForgeDirection side = ForgeDirection
                    .getOrientation((byte) (aID - GT_Proxy.GUI_ID_COVER_SIDE_BASE));
                GT_CoverBehaviorBase<?> cover = tile.getCoverBehaviorAtSideNew(side);

                if (cover.hasCoverGUI() && !cover.useModularUI()) {
                    return cover.getClientGUI(
                        side,
                        tile.getCoverIDAtSide(side),
                        tile.getComplexCoverDataAtSide(side),
                        tile,
                        aPlayer,
                        aWorld);
                }
                return null;
            }
            final IMetaTileEntity tMetaTileEntity = tile.getMetaTileEntity();
            if (tMetaTileEntity != null && !tMetaTileEntity.useModularUI()) {
                return tMetaTileEntity.getClientGUI(aID, aPlayer.inventory, tile);
            }
        }
        return null;
    }

    private static List<String> getOreDictNames(ItemStack stack) {
        return Arrays.stream(OreDictionary.getOreIDs(stack))
            .mapToObj(OreDictionary::getOreName)
            .collect(Collectors.toList());
    }

    @Override
    public int getBurnTime(ItemStack aFuel) {
        if ((aFuel == null) || (aFuel.getItem() == null)) {
            return 0;
        }
        int rFuelValue = 0;
        if ((aFuel.getItem() instanceof GT_MetaGenerated_Item)) {
            final Short tFuelValue = ((GT_MetaGenerated_Item) aFuel.getItem()).mBurnValues
                .get((short) aFuel.getItemDamage());
            if (tFuelValue != null) {
                rFuelValue = Math.max(rFuelValue, tFuelValue);
            }
        }
        final NBTTagCompound tNBT = aFuel.getTagCompound();
        if (tNBT != null) {
            // See if we have something defined by NBT
            final short tValue = tNBT.getShort("GT.ItemFuelValue");
            rFuelValue = Math.max(rFuelValue, tValue);
        } else {
            // If not check the ore dict
            rFuelValue = Math.max(
                rFuelValue,
                getOreDictNames(aFuel).stream()
                    .mapToInt(f -> oreDictBurnTimes.getOrDefault(f, 0))
                    .max()
                    .orElse(0));
        }

        // If we have something from the GT MetaGenerated_Item, ItemFuelValue, or OreDict return
        if (rFuelValue > 0) return rFuelValue;

        // Otherwise, a few more checks
        if (GT_Utility.areStacksEqual(aFuel, new ItemStack(Blocks.wooden_button, 1))) return 150;
        else if (GT_Utility.areStacksEqual(aFuel, new ItemStack(Blocks.ladder, 1))) return 100;
        else if (GT_Utility.areStacksEqual(aFuel, new ItemStack(Items.sign, 1))) return 600;
        else if (GT_Utility.areStacksEqual(aFuel, new ItemStack(Items.wooden_door, 1))) return 600;
        else if (GT_Utility.areStacksEqual(aFuel, ItemList.Block_MSSFUEL.get(1))) return 150000;
        else if (GT_Utility.areStacksEqual(aFuel, ItemList.Block_SSFUEL.get(1))) return 100000;

        return 0;
    }

    // ------------------------ Adds all fluids corresponding to materials ------------------------

    public Fluid addAutoGeneratedCorrespondingFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.fluid.{unlocalised_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? "fluid." + aMaterial.mName.toLowerCase()
            : "autogenerated";

        return GT_FluidFactory.builder(aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withLocalizedName(aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mRGBa)
            .withStateAndTemperature(LIQUID, aMaterial.getLiquidTemperature())
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerBContainers(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), ItemList.Cell_Empty.get(1L))
            .asFluid();
    }

    public Fluid addAutoGeneratedCorrespondingGas(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.gas.{unlocalised_name}.png. All
        // lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? ("gas." + aMaterial.mName.toLowerCase())
            : "autogenerated";

        return GT_FluidFactory.builder(aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withLocalizedName(aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mRGBa)
            .withStateAndTemperature(GAS, aMaterial.getGasTemperature())
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerBContainers(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), ItemList.Cell_Empty.get(1L))
            .asFluid();
    }

    public Fluid addAutogeneratedPlasmaFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.plasma.{unlocalised_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? ("plasma." + aMaterial.mName.toLowerCase())
            : "plasma.autogenerated";

        return GT_FluidFactory.builder("plasma." + aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withLocalizedName(aMaterial.mDefaultLocalName + " Plasma")
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mMoltenRGBa)
            .withStateAndTemperature(PLASMA, 10000)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellPlasma, aMaterial, 1L),
                ItemList.Cell_Empty.get(1L),
                aMaterial.getMolten(1) != null ? 144 : 1000)
            .asFluid();
    }

    public Fluid addAutogeneratedMoltenFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.molten.{unlocalised_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? ("molten." + aMaterial.mName.toLowerCase())
            : "molten.autogenerated";

        return GT_FluidFactory.builder("molten." + aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withLocalizedName("Molten " + aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mMoltenRGBa)
            .withStateAndTemperature(MOLTEN, aMaterial.mMeltingPoint < 0 ? 1000 : aMaterial.mMeltingPoint)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, aMaterial, 1L),
                ItemList.Cell_Empty.get(1L),
                144)
            .asFluid();
    }

    // ------------------------------------------------------------------------------------------------------------

    public void addAutoGeneratedHydroCrackedFluids(Materials aMaterial) {
        Fluid[] crackedFluids = new Fluid[3];
        String[] namePrefixes = { "lightlyhydrocracked.", "moderatelyhydrocracked.", "severelyhydrocracked." };
        OrePrefixes[] orePrefixes = { OrePrefixes.cellHydroCracked1, OrePrefixes.cellHydroCracked2,
            OrePrefixes.cellHydroCracked3 };
        final Fluid uncrackedFluid;
        if (aMaterial.mFluid != null) {
            uncrackedFluid = aMaterial.mFluid;
        } else if (aMaterial.mGas != null) {
            uncrackedFluid = aMaterial.mGas;
        } else return;
        for (int i = 0; i < 3; i++) {
            crackedFluids[i] = GT_FluidFactory.builder(namePrefixes[i] + aMaterial.mName.toLowerCase(Locale.ENGLISH))
                .withIconsFrom(uncrackedFluid)
                .withLocalizedName(orePrefixes[i].mLocalizedMaterialPre + aMaterial.mDefaultLocalName)
                .withColorRGBA(aMaterial.mRGBa)
                .withStateAndTemperature(GAS, 775)
                .buildAndRegister()
                .registerBContainers(
                    GT_OreDictUnificator.get(orePrefixes[i], aMaterial, 1L),
                    ItemList.Cell_Empty.get(1L))
                .asFluid();

            int hydrogenAmount = 2 * i + 2;
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(i + 1))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000), Materials.Hydrogen.getGas(hydrogenAmount * 800))
                .fluidOutputs(new FluidStack(crackedFluids[i], 1000))
                .duration((1 + i) * SECONDS)
                .eut(240)
                .addTo(crackingRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(Materials.Hydrogen.getCells(hydrogenAmount), GT_Utility.getIntegratedCircuit(i + 1))
                .itemOutputs(Materials.Empty.getCells(hydrogenAmount))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1), GT_Utility.getIntegratedCircuit(i + 1))
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(Materials.Hydrogen.getGas(hydrogenAmount * 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);
        }
        aMaterial.setHydroCrackedFluids(crackedFluids);
    }

    public void addAutoGeneratedSteamCrackedFluids(Materials aMaterial) {
        Fluid[] crackedFluids = new Fluid[3];
        String[] namePrefixes = { "lightlysteamcracked.", "moderatelysteamcracked.", "severelysteamcracked." };
        OrePrefixes[] orePrefixes = { OrePrefixes.cellSteamCracked1, OrePrefixes.cellSteamCracked2,
            OrePrefixes.cellSteamCracked3 };
        final Fluid uncrackedFluid;
        if (aMaterial.mFluid != null) {
            uncrackedFluid = aMaterial.mFluid;
        } else if (aMaterial.mGas != null) {
            uncrackedFluid = aMaterial.mGas;
        } else return;
        for (int i = 0; i < 3; i++) {
            crackedFluids[i] = GT_FluidFactory.builder(namePrefixes[i] + aMaterial.mName.toLowerCase(Locale.ENGLISH))
                .withIconsFrom(uncrackedFluid)
                .withLocalizedName(orePrefixes[i].mLocalizedMaterialPre + aMaterial.mDefaultLocalName)
                .withColorRGBA(aMaterial.mRGBa)
                .withStateAndTemperature(GAS, 775)
                .buildAndRegister()
                .registerBContainers(
                    GT_OreDictUnificator.get(orePrefixes[i], aMaterial, 1L),
                    ItemList.Cell_Empty.get(1L))
                .asFluid();

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(i + 1))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000), GT_ModHandler.getSteam(1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 1200))
                .duration((1 + i) * SECONDS)
                .eut(240)
                .addTo(crackingRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("steamCell", 1L), GT_Utility.getIntegratedCircuit(i + 1))
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1), GT_Utility.getIntegratedCircuit(i + 1))
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(GT_ModHandler.getSteam(1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1), GT_Utility.getIntegratedCircuit(i + 1))
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(getFluidStack("ic2steam", 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);
        }
        aMaterial.setSteamCrackedFluids(crackedFluids);
    }

    /**
     * @see GT_FluidFactory#of(String, String, Materials, FluidState, int)
     * @see GT_FluidFactory#of(String, String, FluidState, int)
     * @deprecated use {@link GT_FluidFactory#builder}
     */
    @Deprecated
    public Fluid addFluid(String aName, String aLocalized, Materials aMaterial, int aState, int aTemperatureK) {
        return GT_FluidFactory.of(aName, aLocalized, aMaterial, FluidState.VALID_STATES[aState], aTemperatureK);
    }

    /**
     * @deprecated use {@link GT_FluidFactory#builder}
     */
    @SuppressWarnings({ "MethodWithTooManyParameters" }) // Deprecated method
    @Deprecated
    public Fluid addFluid(String aName, String aLocalized, Materials aMaterial, int aState, int aTemperatureK,
        ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount) {
        return GT_FluidFactory.builder(aName)
            .withLocalizedName(aLocalized)
            .withStateAndTemperature(FluidState.fromValue(aState), aTemperatureK)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerContainers(aFullContainer, aEmptyContainer, aFluidAmount)
            .asFluid();
    }

    /**
     * @deprecated use {@link GT_FluidFactory#builder}
     */
    @Deprecated
    @SuppressWarnings({ "MethodWithTooManyParameters" }) // Deprecated method
    public Fluid addFluid(String aName, String aTexture, String aLocalized, Materials aMaterial, short[] aRGBa,
        int aState, int aTemperatureK, ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount) {
        return GT_FluidFactory.builder(aName)
            .withLocalizedName(aLocalized)
            .withStillIconResourceLocation(new ResourceLocation(GregTech.ID, "fluids/fluid." + aTexture))
            .withColorRGBA(aRGBa)
            .withStateAndTemperature(FluidState.fromValue(aState), aTemperatureK)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .registerContainers(aFullContainer, aEmptyContainer, aFluidAmount)
            .asFluid();
    }

    public File getSaveDirectory() {
        return this.mUniverse == null ? null
            : this.mUniverse.getSaveHandler()
                .getWorldDirectory();
    }

    public long getAnimationTicks() {
        return 0L;
    }

    public float getPartialRenderTicks() {
        return 0;
    }

    public void registerUnificationEntries() {
        GregTech_API.sUnification.mConfig.save();
        GregTech_API.sUnification.mConfig.load();
        GT_OreDictUnificator.resetUnificationEntries();
        for (OreDictEventContainer tOre : this.mEvents) {
            if ((!(tOre.mEvent.Ore.getItem() instanceof GT_MetaGenerated_Item)) && (tOre.mPrefix != null)
                && (tOre.mPrefix.mIsUnificatable)
                && (tOre.mMaterial != null)) {
                boolean checkModID = tOre.mModID != null;

                if (checkModID) {
                    switch (tOre.mModID) {
                        case Names.ENDER_I_O -> {
                            if (tOre.mPrefix == OrePrefixes.ingot && tOre.mMaterial == Materials.DarkSteel) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            }
                        }
                        case Names.THERMAL_FONDATION -> {
                            if (tOre.mPrefix == OrePrefixes.dust && tOre.mMaterial == Materials.Blizz) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.dust && tOre.mMaterial == Materials.Pyrotheum) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            }
                        }
                        case Names.ARS_MAGICA2 -> {
                            if (tOre.mPrefix == OrePrefixes.dust && tOre.mMaterial == Materials.Vinteum) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.gem && tOre.mMaterial == Materials.BlueTopaz) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.gem && tOre.mMaterial == Materials.Chimerite) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.gem && tOre.mMaterial == Materials.Moonstone) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.gem && tOre.mMaterial == Materials.Sunstone) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            }
                        }
                        case Names.ROTARY_CRAFT -> {
                            if (tOre.mPrefix == OrePrefixes.ingot && tOre.mMaterial == Materials.HSLA) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            }
                        }
                        case Names.APPLIED_ENERGISTICS2 -> {
                            if (tOre.mPrefix == OrePrefixes.gem && tOre.mMaterial == Materials.CertusQuartz) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            } else if (tOre.mPrefix == OrePrefixes.dust && tOre.mMaterial == Materials.CertusQuartz) {
                                GT_OreDictUnificator
                                    .addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                                GT_OreDictUnificator.set(
                                    tOre.mPrefix,
                                    tOre.mMaterial,
                                    tOre.mEvent.Ore,
                                    (GregTech_API.sUnification.get(
                                        ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                                        tOre.mEvent.Name,
                                        true)),
                                    true);
                                continue;
                            }
                        }
                        default -> {}
                    }
                }
                if (GT_OreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
                    GT_OreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
                } else {
                    GT_OreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                    GT_OreDictUnificator.set(
                        tOre.mPrefix,
                        tOre.mMaterial,
                        tOre.mEvent.Ore,
                        (checkModID) && (GregTech_API.sUnification.get(
                            ConfigCategories.specialunificationtargets + "." + tOre.mModID,
                            tOre.mEvent.Name,
                            false)),
                        true);
                }
            }
        }

        for (OreDictEventContainer tOre : this.mEvents) {
            if (((tOre.mEvent.Ore.getItem() instanceof GT_MetaGenerated_Item)) && (tOre.mPrefix != null)
                && (tOre.mPrefix.mIsUnificatable)
                && (tOre.mMaterial != null)) {
                if (GT_OreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
                    GT_OreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
                } else {
                    GT_OreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                    GT_OreDictUnificator.set(
                        tOre.mPrefix,
                        tOre.mMaterial,
                        tOre.mEvent.Ore,
                        (tOre.mModID != null) && (GregTech_API.sUnification.get(
                            new StringBuilder().append(ConfigCategories.specialunificationtargets)
                                .append(".")
                                .append(tOre.mModID),
                            tOre.mEvent.Name,
                            false)),
                        true);
                }
            }
        }
        GregTech_API.sUnificationEntriesRegistered = true;
        GregTech_API.sUnification.mConfig.save();
        GT_Recipe.reInit();
    }

    @SuppressWarnings("deprecation")
    public void activateOreDictHandler() {
        this.mOreDictActivated = true;
        ProgressManager.ProgressBar progressBar = ProgressManager.push("Register materials", mEvents.size());

        if (BetterLoadingScreen.isModLoaded()) {
            GT_Values.cls_enabled = true;
            try {
                GT_CLS_Compat.stepMaterialsCLS(mEvents, progressBar);
            } catch (IllegalAccessException e) {
                GT_FML_LOGGER.catching(e);
            }
        } else GT_Proxy.stepMaterialsVanilla(this.mEvents, progressBar);
    }

    @Deprecated
    public static final HashMap<Integer, HashMap<ChunkCoordIntPair, int[]>> dimensionWiseChunkData = new HashMap<>(16); // stores
    // chunk
    // data
    // that
    // is
    // loaded/saved

    public static final HashMap<Integer, GT_Pollution> dimensionWisePollution = new HashMap<>(16); // stores
    // GT_Polluttors
    // objects
    public static final byte GTOIL = 3, GTOILFLUID = 2, GTPOLLUTION = 1, GTMETADATA = 0, NOT_LOADED = 0, LOADED = 1; // consts

    // TO get default's fast
    @Deprecated
    public static int[] getDefaultChunkDataOnCreation() {
        return new int[] { NOT_LOADED, 0, -1, -1 };
    }

    @Deprecated
    public static int[] getDefaultChunkDataOnLoad() {
        return new int[] { LOADED, 0, -1, -1 };
    }

    @SubscribeEvent
    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        GT_UndergroundOil.migrate(event);
        GT_Pollution.migrate(event);
    }

    @SubscribeEvent
    public void onBlockBreakSpeedEvent(PlayerEvent.BreakSpeed aEvent) {
        if (aEvent.entityPlayer != null) {
            ItemStack aStack = aEvent.entityPlayer.getCurrentEquippedItem();
            if ((aStack != null) && ((aStack.getItem() instanceof GT_MetaGenerated_Tool))) {
                aEvent.newSpeed = ((GT_MetaGenerated_Tool) aStack.getItem()).onBlockBreakSpeedEvent(
                    aEvent.newSpeed,
                    aStack,
                    aEvent.entityPlayer,
                    aEvent.block,
                    aEvent.x,
                    aEvent.y,
                    aEvent.z,
                    (byte) aEvent.metadata,
                    aEvent);
            }
        }
    }

    public static class OreDictEventContainer {

        public final OreDictionary.OreRegisterEvent mEvent;
        public final OrePrefixes mPrefix;
        public final Materials mMaterial;
        public final String mModID;

        public OreDictEventContainer(OreDictionary.OreRegisterEvent aEvent, OrePrefixes aPrefix, Materials aMaterial,
            String aModID) {
            this.mEvent = aEvent;
            this.mPrefix = aPrefix;
            this.mMaterial = aMaterial;
            this.mModID = ((aModID == null) || (aModID.equals("UNKNOWN")) ? null : aModID);
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (event.block.getUnlocalizedName()
            .equals("blockAlloyGlass")) GregTech_API.causeMachineUpdate(event.world, event.x, event.y, event.z);
    }

    public static void addFullHazmatToGeneralItem(String aModID, String aItem, long aAmount, int aMeta) {
        ItemStack item = GT_ModHandler.getModItem(aModID, aItem, aAmount, aMeta);
        addItemToHazmatLists(item);
    }

    public static void addFullHazmatToGeneralItem(String aModID, String aItem, long aAmount) {
        ItemStack item = GT_ModHandler.getModItem(aModID, aItem, aAmount, W);
        addItemToHazmatLists(item);
    }

    public static void addFullHazmatToIC2Item(String aItem) {
        ItemStack item = GT_ModHandler.getIC2Item(aItem, 1L, W);
        addItemToHazmatLists(item);
    }

    private static void addItemToHazmatLists(ItemStack item) {
        GregTech_API.sGasHazmatList.add(item);
        GregTech_API.sBioHazmatList.add(item);
        GregTech_API.sFrostHazmatList.add(item);
        GregTech_API.sHeatHazmatList.add(item);
        GregTech_API.sRadioHazmatList.add(item);
        GregTech_API.sElectroHazmatList.add(item);
    }

    public static boolean providesProtection(ItemStack aStack) {

        if (GT_Utility.hasHazmatEnchant(aStack)) return true;

        boolean isGas = GT_Utility.isStackInList(aStack, GregTech_API.sGasHazmatList);
        boolean isBio = GT_Utility.isStackInList(aStack, GregTech_API.sBioHazmatList);
        boolean isFrost = GT_Utility.isStackInList(aStack, GregTech_API.sFrostHazmatList);
        boolean isHeat = GT_Utility.isStackInList(aStack, GregTech_API.sHeatHazmatList);
        boolean isRadio = GT_Utility.isStackInList(aStack, GregTech_API.sRadioHazmatList);
        boolean isElectro = GT_Utility.isStackInList(aStack, GregTech_API.sElectroHazmatList);
        return isGas && isBio && isFrost && isHeat && isRadio && isElectro;
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.itemStack != null) {
            ItemStack aStackTemp = event.itemStack;
            GT_ItemStack aStack = new GT_ItemStack(aStackTemp);
            if (providesProtection(aStackTemp)) {
                event.toolTip.add(EnumChatFormatting.LIGHT_PURPLE + "Provides full hazmat protection.");
            }
        }
    }
}
