package gregtech.common;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.FluidState.LIQUID;
import static gregtech.api.enums.FluidState.MOLTEN;
import static gregtech.api.enums.FluidState.PLASMA;
import static gregtech.api.enums.GTValues.debugEntityCramming;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IguanaTweaksTinkerConstruct;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
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

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.ManualOreDictTweaks;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.IBlockOnWalkOver;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.items.armor.ArmorEventHandlers;
import gregtech.api.items.armor.ArmorKeybinds;
import gregtech.api.net.GTPacketMusicSystemData;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.objects.GTUODimensionList;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.GTBlockMap;
import gregtech.api.util.GTCLSCompat;
import gregtech.api.util.GTChunkAssociatedData;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTMusicSystem;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTShapedRecipe;
import gregtech.api.util.GTShapelessRecipe;
import gregtech.api.util.GTSpawnEventHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.client.renderer.waila.TTRenderGTProgressBar;
import gregtech.common.config.OPStuff;
import gregtech.common.data.GTPowerfailTracker;
import gregtech.common.data.maglev.TetherManager;
import gregtech.common.handlers.OffhandToolFunctionalityHandler;
import gregtech.common.items.ItemGTToolbox;
import gregtech.common.items.MetaGeneratedItem98;
import gregtech.common.misc.GlobalEnergyWorldSavedData;
import gregtech.common.misc.GlobalMetricsCoverDatabase;
import gregtech.common.misc.WirelessChargerManager;
import gregtech.common.misc.spaceprojects.SpaceProjectWorldSavedData;
import gregtech.common.pollution.Pollution;
import gregtech.common.powergoggles.PowerGogglesWorldSavedData;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;
import gregtech.common.recipes.CALImprintRecipe;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.worldgen.HEEIslandScanner;
import gregtech.nei.GTNEIDefaultHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class GTProxy implements IFuelHandler {

    private static final EnumSet<OreGenEvent.GenerateMinable.EventType> PREVENTED_ORES = EnumSet.of(
        OreGenEvent.GenerateMinable.EventType.COAL,
        OreGenEvent.GenerateMinable.EventType.IRON,
        OreGenEvent.GenerateMinable.EventType.GOLD,
        OreGenEvent.GenerateMinable.EventType.DIAMOND,
        OreGenEvent.GenerateMinable.EventType.REDSTONE,
        OreGenEvent.GenerateMinable.EventType.LAPIS,
        OreGenEvent.GenerateMinable.EventType.QUARTZ);
    public final HashSet<ItemStack> mRegisteredOres = new HashSet<>(10000);
    private final Collection<OreDictEventContainer> oreDictEvents = new HashSet<>();
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
    public final GTBlockMap<Boolean> mCTMBlockCache = new GTBlockMap<>();
    public boolean mDisableVanillaOres = true;
    public boolean mAllowSmallBoilerAutomation = false;
    public boolean mCraftingUnification = true;
    public boolean mInventoryUnification = true;
    public boolean mIncreaseDungeonLoot = true;
    public boolean mAxeWhenAdventure = true;
    public boolean mSurvivalIntoAdventure = false;
    public boolean mNerfedWoodPlank = true;
    public boolean mChangeWoodenVanillaTools = true;
    public boolean mHungerEffect = true;
    public boolean mIgnoreTcon = true;
    public boolean mAchievements = true;
    private boolean mOreDictActivated = false;
    public boolean mChangeHarvestLevels = false;
    public boolean mGTBees = true;
    public boolean mPollution = true;
    public boolean mExplosionItemDrop = false;
    public int mMaxEqualEntitiesAtOneSpot = 3;
    public int mFlintChance = 30;
    public int mItemDespawnTime = 6000;
    public int mUpgradeCount = 4;
    public int[] mHarvestLevel = new int[1000];
    public int mGraniteHavestLevel = 3;
    public int mMaxHarvestLevel = 7;
    public double replicatorExponent = 1.2D;
    public int mPollutionSmogLimit = 550000;
    public int mPollutionPoisonLimit = 750000;
    public int mPollutionVegetationLimit = 1000000;
    public int mPollutionSourRainLimit = 2000000;
    public int mPollutionOnExplosion = 100000;
    public int mPollutionCokeOvenPerSecond = 10;
    public int mPollutionPrimitveBlastFurnacePerSecond = 200;
    public int mPollutionEBFPerSecond = 400;
    public int mPollutionCharcoalPitPerSecond = 100;
    public int mPollutionLargeCombustionEnginePerSecond = 480;
    public int mPollutionExtremeCombustionEnginePerSecond = 3840;
    public int mPollutionImplosionCompressorPerSecond = 10000;
    public int mPollutionLargeBronzeBoilerPerSecond = 200;
    public int mPollutionLargeSteelBoilerPerSecond = 400;
    public int mPollutionLargeTitaniumBoilerPerSecond = 800;
    public int mPollutionLargeTungstenSteelBoilerPerSecond = 1600;
    public double mPollutionReleasedByThrottle = 1.0 / 24.0; // divided by 24 because 24 circuit conf
    public int mPollutionLargeGasTurbinePerSecond = 300;
    public int mPollutionMultiSmelterPerSecond = 400;
    public int mPollutionPyrolyseOvenPerSecond = 300;
    public int mPollutionSmallCoalBoilerPerSecond = 20;
    public int mPollutionHighPressureLavaBoilerPerSecond = 20;
    public int mPollutionHighPressureCoalBoilerPerSecond = 30;
    public int mPollutionBaseDieselGeneratorPerSecond = 200;
    public double[] mPollutionDieselGeneratorReleasedByTier = new double[] { 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 };
    public int mPollutionBaseGasTurbinePerSecond = 200;
    public double[] mPollutionGasTurbineReleasedByTier = new double[] { 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 };
    public final GTUODimensionList mUndergroundOil = new GTUODimensionList();
    public boolean enableUndergroundGravelGen = true;
    public boolean enableUndergroundDirtGen = true;
    public boolean mEnableAllMaterials = false;
    public boolean mEnableCleanroom = true;
    public boolean mLowGravProcessing = false;
    public boolean mCropNeedBlock = true;
    public boolean mAMHInteraction = true;
    public boolean mForceFreeFace = true;
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
        /** Will always drop the block version. */
        Block,
        /** Will drop the dimension-specific block version, or stone. */
        PerDimBlock,
        /** Will always drop the stone block version. */
        UnifiedBlock,
        /** Drops raw ore, and is affected by fortune. */
        FortuneItem,
        /** Drops raw ore, and is not affected by fortune. */
        Item
    }

    public OreDropSystem oreDropSystem = OreDropSystem.FortuneItem;

    /** This enables ambient-occlusion smooth lighting on tiles */
    public boolean mRenderTileAmbientOcclusion = true;
    /** This enables rendering of glowing textures */
    public boolean mRenderGlowTextures = true;
    /** Render flipped textures */
    public boolean mRenderFlippedMachinesFlipped = true;
    /** This enables indicators on input/output hatches */
    public boolean mRenderIndicatorsOnHatch = true;
    /** This enables the rendering of dirt particles if pollution is enabled too */
    public boolean mRenderDirtParticles = true;
    /** This enables the rendering of the pollution fog if pollution is enabled too */
    public boolean mRenderPollutionFog = true;
    /** This enables BaseMetaTileEntity block updates handled by BlockUpdateHandler */
    public boolean mUseBlockUpdateHandler = false;
    /** This makes cover tabs visible on GregTech machines */
    public boolean mCoverTabsVisible = true;
    /** This controls whether cover tabs display on the left (default) or right side of the UI */
    public boolean mCoverTabsFlipped = false;
    /** How verbose should tooltips be? 0: disabled, 1: one-line, 2: normal, 3+: extended */
    public int mTooltipVerbosity = 2;
    /** How verbose should tooltips be when LSHIFT is held? 0: disabled, 1: one-line, 2: normal, 3+: extended */
    public int mTooltipShiftVerbosity = 3;
    /**
     * Which style to use for title tab on machine GUI? 0: text tab split-dark, 1: text tab unified, 2: item icon tab
     */
    public int mTitleTabStyle = 0;
    /** Which style should tooltip separator lines have? 0: empty line, 1: dashed line, 2+: continuous line */
    public int separatorStyle = 2;
    /**
     * Which style should tooltip finisher separator lines have? 0: no line, 1: empty line, 2: dashed line, 3+:
     * continuous line
     */
    public int tooltipFinisherStyle = 1;
    /**
     * Enables scrolling up while hovering the ghost circuit of a machine UI to increment the circuit number instead of
     * decrement
     */
    public boolean invertCircuitScrollDirection = false;
    /** Whether to show seconds or ticks on NEI */
    public boolean mNEIRecipeSecondMode = true;
    /** This enables "Recipe by" display on NEI */
    public boolean mNEIRecipeOwner = false;
    /** This enables showing stack traces where the recipe was added. Reboot needed */
    public boolean mNEIRecipeOwnerStackTrace = false;
    /** This enables showing original voltage when overclocked */
    public boolean mNEIOriginalVoltage = false;
    /** This enables the green->red durability for an item's damage value */
    public boolean mRenderItemDurabilityBar = true;
    /** This enables the blue charge bar for an electric item's charge */
    public boolean mRenderItemChargeBar = true;
    /** This enables showing voltage tier of transformer for Waila, instead of raw voltage number */
    public boolean mWailaTransformerVoltageTier = true;
    /** This enables showing the cpu load in nanoseconds via waila. */
    public boolean wailaAverageNS = false;
    /** Color preset of the waila progress bar. */
    public TTRenderGTProgressBar.ProgressBarColor wailaProgressBarColorPreset = TTRenderGTProgressBar.ProgressBarColor.Green;
    /** First color of the waila progress bar. */
    public int wailaProgressBarColor1 = 0xFFDD5555;
    /** Second color of the waila progress bar. */
    public int wailaProgressBarColor2 = 0xFFDD2222;
    /** First color of the waila progress bar's border (top and left). */
    public int wailaProgressBorderColor1 = 0xFF505050;
    /** Second color of the waila progress bar's border (bottom and right). */
    public int wailaProgressBorderColor2 = 0xFF505050;

    public static final int GUI_ID_COVER_SIDE_BASE = 10; // Takes GUI ID 10 - 15

    private static final Map<String, Integer> oreDictBurnTimes = new Object2IntOpenHashMap<>();

    // Locking
    public final ReentrantLock TICK_LOCK = new ReentrantLock();

    private final ConcurrentMap<UUID, GTClientPreference> mClientPrefernces = new ConcurrentHashMap<>();
    public final Int2ObjectOpenHashMap<Pollution> dimensionWisePollution = new Int2ObjectOpenHashMap<>(16);
    /** A fast lookup for players. */
    private Map<UUID, EntityPlayerMP> PLAYERS_BY_UUID;
    private Map<String, UUID> UUID_BY_NAME;
    public WirelessChargerManager wirelessChargerManager;
    public GTSpawnEventHandler spawnEventHandler;
    public GTPowerfailTracker powerfailTracker;
    public TetherManager tetherManager;

    public SyncedKeybind TOOL_MODE_SWITCH_KEYBIND;

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

    public GTProxy() {
        // This is a hack to remain backward
        // compatible after renaming the proxy
        // for mods still refering the old name
        // noinspection deprecation
        GTMod.gregtechproxy = this;

        GameRegistry.registerFuelHandler(this);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.ORE_GEN_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        if (Thaumcraft.isModLoaded()) {
            GregTechAPI.sThaumcraftCompat = new GTThaumcraftCompat();
        }
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            onFluidContainerRegistration(new FluidContainerRegistry.FluidContainerRegisterEvent(tData));
        }
        // Register previously registered oredict entries (from Forge) with the GT oredict handler.
        for (String tOreName : OreDictionary.getOreNames()) {
            for (ItemStack itemStack : OreDictionary.getOres(tOreName)) {
                registerOre(new OreDictionary.OreRegisterEvent(tOreName, itemStack));
            }
        }
    }

    public boolean isClientSide() {
        return false;
    }

    public EntityPlayer getThePlayer() {
        return null;
    }

    public void onPreInitialization(FMLPreInitializationEvent event) {
        // spotless:off
        GTLog.out.println("GTMod: Preload-Phase started!");

        GregTechAPI.sPreloadStarted = true;
        this.mIgnoreTcon = OPStuff.ignoreTinkerConstruct;
        this.replicatorExponent = OPStuff.replicatorExponent;
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GTLog.out.println("GTMod: Getting required Items of other Mods.");

        ItemList.RC_ShuntingWire.set(GTModHandler.getModItem(Railcraft.ID, "machine.delta", 1L, 0));
        ItemList.RC_ShuntingWireFrame.set(GTModHandler.getModItem(Railcraft.ID, "frame", 1L, 0));
        ItemList.RC_Rail_Standard.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 0));
        ItemList.RC_Rail_Adv.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 1));
        ItemList.RC_Rail_Wooden.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 2));
        ItemList.RC_Rail_HS.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 3));
        ItemList.RC_Rail_Reinforced.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 4));
        ItemList.RC_Rail_Electric.set(GTModHandler.getModItem(Railcraft.ID, "part.rail", 1L, 5));
        ItemList.RC_Tie_Wood.set(GTModHandler.getModItem(Railcraft.ID, "part.tie", 1L, 0));
        ItemList.RC_Tie_Stone.set(GTModHandler.getModItem(Railcraft.ID, "part.tie", 1L, 1));
        ItemList.RC_Bed_Wood.set(GTModHandler.getModItem(Railcraft.ID, "part.railbed", 1L, 0));
        ItemList.RC_Bed_Stone.set(GTModHandler.getModItem(Railcraft.ID, "part.railbed", 1L, 1));
        ItemList.RC_Rebar.set(GTModHandler.getModItem(Railcraft.ID, "part.rebar", 1L));
        ItemList.TC_Thaumometer.set(GTModHandler.getModItem(Thaumcraft.ID, "ItemThaumometer", 1L, 0));
        ItemList.Tool_Sword_Steel.set(GTModHandler.getModItem(Railcraft.ID, "tool.steel.sword", 1L));
        ItemList.Tool_Pickaxe_Steel.set(GTModHandler.getModItem(Railcraft.ID, "tool.steel.pickaxe", 1L));
        ItemList.Tool_Shovel_Steel.set(GTModHandler.getModItem(Railcraft.ID, "tool.steel.shovel", 1L));
        ItemList.Tool_Axe_Steel.set(GTModHandler.getModItem(Railcraft.ID, "tool.steel.axe", 1L));
        ItemList.Tool_Hoe_Steel.set(GTModHandler.getModItem(Railcraft.ID, "tool.steel.hoe", 1L));

        ItemList.TF_LiveRoot.set(GTModHandler.getModItem(TwilightForest.ID, "item.liveRoot", 1L, 0));
        ItemList.TF_Vial_FieryBlood.set(GTModHandler.getModItem(TwilightForest.ID, "item.fieryBlood", 1L));
        ItemList.TF_Vial_FieryTears.set(GTModHandler.getModItem(TwilightForest.ID, "item.fieryTears", 1L));

        ItemList.FR_Lemon.set(GTModHandler.getModItem(Forestry.ID, "fruits", 1L, 3));
        ItemList.FR_Mulch.set(GTModHandler.getModItem(Forestry.ID, "mulch", 1L));
        ItemList.FR_Fertilizer.set(GTModHandler.getModItem(Forestry.ID, "fertilizerCompound", 1L));
        ItemList.FR_Compost.set(GTModHandler.getModItem(Forestry.ID, "fertilizerBio", 1L));
        ItemList.FR_Silk.set(GTModHandler.getModItem(Forestry.ID, "craftingMaterial", 1L, 2));
        ItemList.FR_Wax.set(GTModHandler.getModItem(Forestry.ID, "beeswax", 1L));
        ItemList.FR_WaxCapsule.set(GTModHandler.getModItem(Forestry.ID, "waxCapsule", 1L));
        ItemList.FR_RefractoryWax.set(GTModHandler.getModItem(Forestry.ID, "refractoryWax", 1L));
        ItemList.FR_RefractoryCapsule.set(GTModHandler.getModItem(Forestry.ID, "refractoryEmpty", 1L));
        ItemList.FR_Bee_Drone.set(GTModHandler.getModItem(Forestry.ID, "beeDroneGE", 1L));
        ItemList.FR_Bee_Princess.set(GTModHandler.getModItem(Forestry.ID, "beePrincessGE", 1L));
        ItemList.FR_Bee_Queen.set(GTModHandler.getModItem(Forestry.ID, "beeQueenGE", 1L));
        ItemList.FR_Tree_Sapling.set(GTModHandler.getModItem(Forestry.ID, "sapling", 1L, GTModHandler.getModItem(Forestry.ID, "saplingGE", 1L)));
        ItemList.FR_Butterfly.set(GTModHandler.getModItem(Forestry.ID, "butterflyGE", 1L));
        ItemList.FR_Larvae.set(GTModHandler.getModItem(Forestry.ID, "beeLarvaeGE", 1L));
        ItemList.FR_Serum.set(GTModHandler.getModItem(Forestry.ID, "serumGE", 1L));
        ItemList.FR_Caterpillar.set(GTModHandler.getModItem(Forestry.ID, "caterpillarGE", 1L));
        ItemList.FR_PollenFertile.set(GTModHandler.getModItem(Forestry.ID, "pollenFertile", 1L));
        ItemList.FR_Stick.set(GTModHandler.getModItem(Forestry.ID, "oakStick", 1L));
        ItemList.FR_Casing_Impregnated.set(GTModHandler.getModItem(Forestry.ID, "impregnatedCasing", 1L));
        ItemList.FR_Casing_Sturdy.set(GTModHandler.getModItem(Forestry.ID, "sturdyMachine", 1L));
        ItemList.FR_Casing_Hardened.set(GTModHandler.getModItem(Forestry.ID, "hardenedMachine", 1L));

        ItemList.Bottle_Empty.set(new ItemStack(Items.glass_bottle, 1));

        ItemList.Cell_Universal_Fluid.set(GTModHandler.getIC2Item("FluidCell", 1L));
        ItemList.Cell_Empty.set(GTModHandler.getIC2Item("cell", 1L, GTModHandler.getIC2Item("cellEmpty", 1L, GTModHandler.getIC2Item("emptyCell", 1L))));
        ItemList.Cell_Water.set(GTModHandler.getIC2Item("waterCell", 1L, GTModHandler.getIC2Item("cellWater", 1L)));
        ItemList.Cell_Lava.set(GTModHandler.getIC2Item("lavaCell", 1L, GTModHandler.getIC2Item("cellLava", 1L)));
        ItemList.Cell_Air.set(GTModHandler.getIC2Item("airCell", 1L, GTModHandler.getIC2Item("cellAir", 1L, GTModHandler.getIC2Item("cellOxygen", 1L))));

        ItemList.IC2_Item_Casing_Iron.set(GTModHandler.getIC2Item("casingiron", 1L));
        ItemList.IC2_Item_Casing_Gold.set(GTModHandler.getIC2Item("casinggold", 1L));
        ItemList.IC2_Item_Casing_Bronze.set(GTModHandler.getIC2Item("casingbronze", 1L));
        ItemList.IC2_Item_Casing_Copper.set(GTModHandler.getIC2Item("casingcopper", 1L));
        ItemList.IC2_Item_Casing_Tin.set(GTModHandler.getIC2Item("casingtin", 1L));
        ItemList.IC2_Item_Casing_Lead.set(GTModHandler.getIC2Item("casinglead", 1L));
        ItemList.IC2_Item_Casing_Steel.set(GTModHandler.getIC2Item("casingadviron", 1L));
        ItemList.IC2_Spray_WeedEx.set(GTModHandler.getIC2Item("weedEx", 1L));
        ItemList.IC2_Mixed_Metal_Ingot.set(GTModHandler.getIC2Item("mixedMetalIngot", 1L));
        ItemList.IC2_Fertilizer.set(GTModHandler.getIC2Item("fertilizer", 1L));
        ItemList.IC2_CoffeeBeans.set(GTModHandler.getIC2Item("coffeeBeans", 1L));
        ItemList.IC2_CoffeePowder.set(GTModHandler.getIC2Item("coffeePowder", 1L));
        ItemList.IC2_Hops.set(GTModHandler.getIC2Item("hops", 1L));
        ItemList.IC2_Resin.set(GTModHandler.getIC2Item("resin", 1L));
        ItemList.IC2_Plantball.set(GTModHandler.getIC2Item("plantBall", 1L));
        ItemList.IC2_PlantballCompressed.set(GTModHandler.getIC2Item("compressedPlantBall", 1L, ItemList.IC2_Plantball.get(1L)));
        ItemList.IC2_Crop_Seeds.set(GTModHandler.getIC2Item("cropSeed", 1L));
        ItemList.IC2_Grin_Powder.set(GTModHandler.getIC2Item("grinPowder", 1L));
        ItemList.IC2_Energium_Dust.set(GTModHandler.getIC2Item("energiumDust", 1L));
        ItemList.IC2_Scrap.set(GTModHandler.getIC2Item("scrap", 1L));
        ItemList.IC2_Scrapbox.set(GTModHandler.getIC2Item("scrapBox", 1L));
        ItemList.IC2_Fuel_Rod_Empty.set(GTModHandler.getIC2Item("fuelRod", 1L));
        ItemList.IC2_Uranium_238.set(GTModHandler.getIC2Item("Uran238", 1L));
        ItemList.IC2_Uranium_235.set(GTModHandler.getIC2Item("Uran235", 1L));
        ItemList.IC2_Uranium_235_Small.set(GTModHandler.getIC2Item("smallUran235", 1L));
        ItemList.IC2_Plutonium.set(GTModHandler.getIC2Item("Plutonium", 1L));
        ItemList.IC2_Plutonium_Small.set(GTModHandler.getIC2Item("smallPlutonium", 1L));
        ItemList.IC2_Uranium_Fuel.set(GTModHandler.getIC2Item("UranFuel", 1L));
        ItemList.IC2_MOX_Fuel.set(GTModHandler.getIC2Item("MOXFuel", 1L));
        ItemList.IC2_Food_Can_Empty.set(GTModHandler.getIC2Item("tinCan", 1L));
        ItemList.IC2_Food_Can_Filled.set(GTModHandler.getIC2Item("filledTinCan", 1L, 0));
        ItemList.IC2_Food_Can_Spoiled.set(GTModHandler.getIC2Item("filledTinCan", 1L, 1));
        ItemList.IC2_Industrial_Diamond.set(GTModHandler.getIC2Item("industrialDiamond", 1L, new ItemStack(Items.diamond, 1)));
        ItemList.IC2_Compressed_Coal_Ball.set(GTModHandler.getIC2Item("compressedCoalBall", 1L));
        ItemList.IC2_Compressed_Coal_Chunk.set(GTModHandler.getIC2Item("coalChunk", 1L));
        ItemList.IC2_ShaftIron.set(GTModHandler.getIC2Item("ironshaft", 1L));
        ItemList.IC2_ShaftSteel.set(GTModHandler.getIC2Item("steelshaft", 1L));

        ItemList.IC2_SuBattery.set(GTModHandler.getIC2Item("suBattery", 1L));
        ItemList.IC2_ReBattery.set(GTModHandler.getIC2Item("reBattery", 1L));
        ItemList.IC2_AdvBattery.set(GTModHandler.getIC2Item("advBattery", 1L));
        ItemList.IC2_EnergyCrystal.set(GTModHandler.getIC2Item("energyCrystal", 1L));
        ItemList.IC2_LapotronCrystal.set(GTModHandler.getIC2Item("lapotronCrystal", 1L));

        ItemList.Tool_Sword_Bronze.set(GTModHandler.getIC2Item("bronzeSword", 1L));
        ItemList.Tool_Pickaxe_Bronze.set(GTModHandler.getIC2Item("bronzePickaxe", 1L));
        ItemList.Tool_Shovel_Bronze.set(GTModHandler.getIC2Item("bronzeShovel", 1L));
        ItemList.Tool_Axe_Bronze.set(GTModHandler.getIC2Item("bronzeAxe", 1L));
        ItemList.Tool_Hoe_Bronze.set(GTModHandler.getIC2Item("bronzeHoe", 1L));
        ItemList.IC2_ForgeHammer.set(GTModHandler.getIC2Item("ForgeHammer", 1L));
        ItemList.IC2_WireCutter.set(GTModHandler.getIC2Item("cutter", 1L));

        ItemList.Credit_Iron.set(GTModHandler.getIC2Item("coin", 1L));

        ItemList.Circuit_Basic.set(GTModHandler.getIC2Item("electronicCircuit", 1L));
        ItemList.Circuit_Advanced.set(GTModHandler.getIC2Item("advancedCircuit", 1L));

        ItemList.Upgrade_Overclocker.set(GTModHandler.getIC2Item("overclockerUpgrade", 1L));
        ItemList.Upgrade_Battery.set(GTModHandler.getIC2Item("energyStorageUpgrade", 1L));

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

        GTModHandler.sNonReplaceableItems.add(new ItemStack(Items.bow, 1, 32767));
        GTModHandler.sNonReplaceableItems.add(new ItemStack(Items.fishing_rod, 1, 32767));
        GTModHandler.sNonReplaceableItems.add(ItemList.IC2_ForgeHammer.getWithDamage(1L, 32767L));
        GTModHandler.sNonReplaceableItems.add(ItemList.IC2_WireCutter.getWithDamage(1L, 32767L));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("painter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("blackPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("redPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("greenPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("brownPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("bluePainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("purplePainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("cyanPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("lightGreyPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("darkGreyPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("pinkPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("limePainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("yellowPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("cloudPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("magentaPainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("orangePainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("whitePainter", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("cfPack", 1L, 32767));
        // GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("jetpack", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("treetap", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(ItemList.IC2_Spray_WeedEx.getWithDamage(1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("staticBoots", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("compositeArmor", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("hazmatHelmet", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("hazmatChestplate", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("hazmatLeggings", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getIC2Item("hazmatBoots", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "part.turbine.disk", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "part.turbine.blade", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "part.turbine.rotor", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "borehead.diamond", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "borehead.steel", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Railcraft.ID, "borehead.iron", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.plateNaga", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.legsNaga", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.arcticHelm", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.arcticPlate", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.arcticLegs", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.arcticBoots", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.yetiHelm", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.yetiPlate", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.yetiLegs", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(TwilightForest.ID, "item.yetiBoots", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ToolCertusQuartzCuttingKnife", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ToolNetherQuartzCuttingKnife", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "apiaristHelmet", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "apiaristChest", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "apiaristLegs", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "apiaristBoots", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "frameUntreated", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "frameImpregnated", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "frameProven", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(Forestry.ID, "waxCast", 1L, 32767));
        GTModHandler.sNonReplaceableItems.add(GTModHandler.getModItem(GalacticraftCore.ID, "item.sensorGlasses", 1L, 32767));

        RecipeSorter.register("gregtech:shaped", GTShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register("gregtech:shapeless", GTShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        // Register chunk manager with Forge
        GTChunkManager.init();
        // spotless:on
    }

    public void onInitialization(FMLInitializationEvent event) {
        GTLog.out.println("GTMod: Beginning Load-Phase.");

        // Clay buckets, which don't get registered until Iguana Tweaks pre-init
        if (IguanaTweaksTinkerConstruct.isModLoaded()) {
            OrePrefixes.bucketClay.mContainerItem = GTModHandler
                .getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0);
            GTOreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Empty,
                GTModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0));
            GTOreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Water,
                GTModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketWater", 1L, 0));
            GTOreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Lava,
                GTModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketLava", 1L, 0));
            GTOreDictUnificator.set(
                OrePrefixes.bucketClay,
                Materials.Milk,
                GTModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketMilk", 1L, 0));

            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(
                    Materials.Milk.getFluid(1_000),
                    GTOreDictUnificator.get(OrePrefixes.bucketClay, Materials.Milk, 1L),
                    GTOreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L)));
        }

        if (!this.enableUndergroundGravelGen) PREVENTED_ORES.add(OreGenEvent.GenerateMinable.EventType.GRAVEL);
        if (!this.enableUndergroundDirtGen) PREVENTED_ORES.add(OreGenEvent.GenerateMinable.EventType.DIRT);

        ArmorEventHandlers ArmorEvents = new ArmorEventHandlers();

        MinecraftForge.EVENT_BUS.register(new SpaceProjectWorldSavedData());
        MinecraftForge.EVENT_BUS.register(new GlobalEnergyWorldSavedData(""));
        MinecraftForge.EVENT_BUS.register(new GTWorldgenerator.OregenPatternSavedData(""));
        MinecraftForge.EVENT_BUS.register(new GlobalMetricsCoverDatabase());
        MinecraftForge.EVENT_BUS.register(new PowerGogglesWorldSavedData());
        MinecraftForge.EVENT_BUS.register(ArmorEvents);
        FMLCommonHandler.instance()
            .bus()
            .register(ArmorEvents);
        FMLCommonHandler.instance()
            .bus()
            .register(new GTWorldgenerator.OregenPatternSavedData(""));
        FMLCommonHandler.instance()
            .bus()
            .register(PowerGogglesEventHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(PowerGogglesEventHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new OffhandToolFunctionalityHandler());
        TOOL_MODE_SWITCH_KEYBIND = SyncedKeybind
            .createConfigurable("key.gt.tool_mode_switch", "Gregtech", Keyboard.KEY_PERIOD)
            .registerGlobalListener(MetaGeneratedTool::switchCurrentToolMode)
            .registerGlobalListener(ItemGTToolbox::switchToolMode);

        GregTechAPI.sLoadStarted = true;
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GTLanguageManager.writePlaceholderStrings();
    }

    /**
     * @deprecated use {@link gregtech.api.util.GTModHandler.RecipeBits#BITS_STD}
     */
    @Deprecated
    public static long tBits = GTModHandler.RecipeBits.BITS_STD;

    public void onPostInitialization(FMLPostInitializationEvent event) {
        GTLog.out.println("GTMod: Beginning PostLoad-Phase.");
        GregTechAPI.sPostloadStarted = true;

        new ArmorKeybinds();

        // This needs to happen late enough that all of the fluids we need have been registered.
        // onInitialization() seems to be too early, as the New Horizons Core Mod registers some fluids in post-load.
        MetaGeneratedItem98.init();

        GTOreDictUnificator.addItemData(new ItemStack(Items.iron_door, 1), new ItemData(Materials.Iron, 21772800L));
        GTOreDictUnificator
            .addItemData(new ItemStack(Items.wooden_door, 1, 32767), new ItemData(Materials.Wood, 21772800L));
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        GTLog.out.println("GTMod: Adding Configs specific for MetaTileEntities");
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                try {
                    GregTechAPI.METATILEENTITIES[i].onConfigLoad();
                } catch (Exception e) {
                    GT_FML_LOGGER.error("Could not load config for MTE " + GregTechAPI.METATILEENTITIES[i], e);
                }
            }
        }
        GTLog.out.println("GTMod: Adding Tool Usage Crafting Recipes for OreDict Items.");
        for (Materials aMaterial : Materials.values()) {
            if ((aMaterial.mUnifiable) && (aMaterial.mMaterialInto == aMaterial)) {
                if (!aMaterial.contains(SubTag.NO_ORE_PROCESSING)) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.crushedCentrifuged.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.crystalline.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.crystal.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.crushedPurified.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.cleanGravel.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.reduced.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.clump.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.shard.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.crushed.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L),
                        tBits,
                        new Object[] { "h", "X", 'X', OrePrefixes.dirtyGravel.get(aMaterial) });
                }
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 4L),
                    tBits,
                    new Object[] { " X ", "   ", "   ", 'X', OrePrefixes.dust.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 9L),
                    tBits,
                    new Object[] { "X  ", "   ", "   ", 'X', OrePrefixes.dust.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    tBits,
                    new Object[] { "XX", "XX", 'X', OrePrefixes.dustSmall.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    tBits,
                    new Object[] { "XXX", "XXX", "XXX", 'X', OrePrefixes.dustTiny.get(aMaterial) });
            }
        }
        // Cover GUI needs to know text color which can be configured with resource packs. In theory it's not needed on
        // server, but it's just convenient to be able to write GUI code without side check. This will be reworked with
        // MUI2, but for the time being it stays here. -- miozune
        CoverRegistry.reloadCoverColorOverrides();
        CALImprintRecipe.register();
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {}

    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        // spotless:off
        GTLog.out.println("GTMod: firing FMLServerAboutToStartEvent !");
        GTChunkAssociatedData.clearAll();
        PLAYERS_BY_UUID = new HashMap<>();
        UUID_BY_NAME = new HashMap<>();
        isFirstWorldTick = true;
        GTMusicSystem.ServerSystem.reset();
        wirelessChargerManager = new WirelessChargerManager();
        spawnEventHandler = new GTSpawnEventHandler();
        powerfailTracker = new GTPowerfailTracker();
        tetherManager = new TetherManager();
        FMLCommonHandler.instance().bus().register(wirelessChargerManager);
        MinecraftForge.EVENT_BUS.register(spawnEventHandler);
        FMLCommonHandler.instance().bus().register(powerfailTracker);
        MinecraftForge.EVENT_BUS.register(powerfailTracker);
        FMLCommonHandler.instance().bus().register(tetherManager);
        MinecraftForge.EVENT_BUS.register(tetherManager);
        // spotless:off
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        GTLog.out.println("GTMod: firing FMLServerStartingEvent !");
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                tData.fluid.amount = 0;
                break;
            }
        }
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                try {
                    GregTechAPI.METATILEENTITIES[i].onServerStart();
                } catch (Exception e) {
                    throw new RuntimeException("Could not call onServerStart MTE " + GregTechAPI.METATILEENTITIES[i], e);
                }
            }
        }
    }

    public void onServerStarted(FMLServerStartedEvent event) {
        MTEDroneCentre.getCentreMap().clear();
        GTLog.out.println("GTMod: Cleaning up all OreDict Crafting Recipes, which have an empty List in them, since they are never meeting any Condition.");
        List<IRecipe> tList = CraftingManager.getInstance().getRecipeList();
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

    public void onServerStopping(FMLServerStoppingEvent event) {
        GTMusicSystem.ServerSystem.reset();
        GregTechAPI.sWirelessRedstone.clear();
        GregTechAPI.sAdvancedWirelessRedstone.clear();
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            HEEIslandScanner.clearCache();
        }
    }

    public void onServerStopped(FMLServerStoppedEvent event) {
        // spotless:off
        if (wirelessChargerManager != null) {
            FMLCommonHandler.instance().bus().unregister(wirelessChargerManager);
        }
        if (spawnEventHandler != null) {
            MinecraftForge.EVENT_BUS.unregister(spawnEventHandler);
        }
        if (powerfailTracker != null) {
            FMLCommonHandler.instance().bus().unregister(powerfailTracker);
            MinecraftForge.EVENT_BUS.unregister(powerfailTracker);
        }
        if (tetherManager != null) {
            FMLCommonHandler.instance().bus().unregister(tetherManager);
            MinecraftForge.EVENT_BUS.unregister(tetherManager);
        }
        wirelessChargerManager = null;
        spawnEventHandler = null;
        powerfailTracker = null;
        tetherManager = null;
        PLAYERS_BY_UUID = null;
        UUID_BY_NAME = null;
        // spotless:on

        GTCapesLoader.clearSelectedCapes();
        PowerGogglesEventHandler.getInstance()
            .onServerStopped(event);
    }

    /**
     * Tells {@link GTNEIDefaultHandler} to reload recipes.
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
        if (GTUtility.isStackValid(aEvent.result)
            && (GTUtility.getProjectile(SubTag.PROJECTILE_ARROW, aEvent.entityPlayer.inventory) != null)) {
            aEvent.entityPlayer.setItemInUse(
                aEvent.result,
                aEvent.result.getItem()
                    .getMaxItemUseDuration(aEvent.result));
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onArrowLooseEvent(ArrowLooseEvent aEvent) {
        ItemStack aArrow = GTUtility.getProjectile(SubTag.PROJECTILE_ARROW, aEvent.entityPlayer.inventory);
        if (GTUtility.isStackValid(aEvent.bow) && (aArrow != null) && ((aEvent.bow.getItem() instanceof ItemBow))) {
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
                GTUtility.removeNullStacksFromInventory(aEvent.entityPlayer.inventory);
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
        if (aEvent.entity == null) {
            return;
        }

        if (aEvent.entity.worldObj.isRemote) {
            return;
        }

        if ((aEvent.entity instanceof EntityItem)) {
            ((EntityItem) aEvent.entity)
                .setEntityItemStack(GTOreDictUnificator.get(true, ((EntityItem) aEvent.entity).getEntityItem(), true));
        }
    }

    @SubscribeEvent
    public void onPlayerJoinEvent(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player == null || event.player.isClientWorld()
            || !(event.player instanceof EntityPlayerMP mpPlayer)) {
            return;
        }
        GTValues.NW.sendToPlayer(new GTPacketMusicSystemData(GTMusicSystem.ServerSystem.serialize()), mpPlayer);
    }

    @SubscribeEvent
    public void onOreGenEvent(OreGenEvent.GenerateMinable aGenerator) {
        if ((this.mDisableVanillaOres) && ((aGenerator.generator instanceof WorldGenMinable))
            && (PREVENTED_ORES.contains(aGenerator.type))) {
            aGenerator.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public void onPlayerInteraction(PlayerInteractEvent aEvent) {
        if ((aEvent.entityPlayer == null) || (aEvent.entityPlayer.worldObj == null)
            || (aEvent.action == null)
            || (aEvent.world.provider == null)
            || aEvent.world.isRemote) {
            return;
        }
        ItemStack aStack = aEvent.entityPlayer.getCurrentEquippedItem();
        if ((aStack != null) && (aEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            && (aStack.getItem() == Items.flint_and_steel)) {
            if ((!aEvent.entityPlayer.capabilities.isCreativeMode)
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

        if (!(item.getItem() instanceof MetaGeneratedTool tool)) return;

        IToolStats stats = tool.getToolStats(item);
        if (stats == null) return;

        TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
        stats.onBreakBlock(player, event.x, event.y, event.z, event.block, event.blockMetadata, tile, event);
    }

    @SubscribeEvent
    public void onBlockHarvestingEvent(BlockEvent.HarvestDropsEvent aEvent) {
        if (aEvent.harvester == null) return;

        ItemStack aStack = aEvent.harvester.getCurrentEquippedItem();
        if (aStack == null) return;

        if ((aStack.getItem() instanceof MetaGeneratedTool tool)) {
            tool.onHarvestBlockEvent(
                aEvent.drops,
                aStack,
                aEvent.harvester,
                aEvent.block,
                aEvent.x,
                aEvent.y,
                aEvent.z,
                aEvent.blockMetadata,
                aEvent.fortuneLevel,
                aEvent.isSilkTouching,
                aEvent);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, aStack) > 2) {
            try {
                for (ItemStack tDrop : aEvent.drops) {
                    ItemStack tSmeltingOutput = GTModHandler.getSmeltingOutput(tDrop, false, null);
                    if (tSmeltingOutput != null) {
                        tDrop.stackSize *= tSmeltingOutput.stackSize;
                        tSmeltingOutput.stackSize = tDrop.stackSize;
                        GTUtility.setStack(tDrop, tSmeltingOutput);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(GTLog.err);
            }
        }

    }

    @SubscribeEvent
    public void registerOre(OreDictionary.OreRegisterEvent aEvent) {
        ModContainer tContainer = Loader.instance()
            .activeModContainer();
        String aMod = tContainer == null ? "UNKNOWN" : tContainer.getModId();
        String aOriginalMod = aMod;
        if (GTOreDictUnificator.isRegisteringOres()) {
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
            GTLog.ore.println(
                aOriginalMod
                    + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
            throw new IllegalArgumentException(
                aOriginalMod
                    + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
        }
        try {
            aEvent.Ore.stackSize = 1;

            // skipping TinkerConstruct ore registration except for blocks
            if (this.mIgnoreTcon && aOriginalMod.equals(TinkerConstruct.ID)
                && !(aEvent.Ore.getItem() instanceof ItemBlock)) {
                return;
            }
            String tModToName = aMod + " -> " + aEvent.Name;
            if (this.mOreDictActivated || GregTechAPI.sPostloadStarted || GregTechAPI.sLoadFinished) {
                tModToName = aOriginalMod + " --Late--> " + aEvent.Name;
            }
            if (((aEvent.Ore.getItem() instanceof ItemBlock))
                || (GTUtility.getBlockFromStack(aEvent.Ore) != Blocks.air)) {
                GTOreDictUnificator.addToBlacklist(aEvent.Ore);
            }
            this.mRegisteredOres.add(aEvent.Ore);
            if (this.mIgnoredItems.contains(aEvent.Name)) {
                if ((aEvent.Name.startsWith("item"))) {
                    GTLog.ore.println(tModToName);
                    if (aEvent.Name.equals("itemCopperWire")) {
                        GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                    }
                    if (aEvent.Name.equals("itemRubber")) {
                        GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Rubber, aEvent.Ore);
                    }
                    return;
                }
            } else if (this.mIgnoredNames.contains(aEvent.Name)) {
                GTLog.ore.println(tModToName + " is getting ignored via hardcode.");
                return;
            } else if (aEvent.Name.equals("stone")) {
                GTOreDictUnificator.registerOre("stoneSmooth", aEvent.Ore);
                return;
            } else if (aEvent.Name.equals("cobblestone")) {
                GTOreDictUnificator.registerOre("stoneCobble", aEvent.Ore);
                return;
            } else if ((aEvent.Name.contains("|")) || (aEvent.Name.contains("*"))
                || (aEvent.Name.contains(":"))
                || (aEvent.Name.contains("."))
                || (aEvent.Name.contains("$"))) {
                    GTLog.ore
                        .println(tModToName + " is using a private Prefix and is therefor getting ignored properly.");
                    return;
                } else if (aEvent.Name.equals("copperWire")) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                } else if (aEvent.Name.equals("oreHeeEndrium")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ore, Materials.Endium, aEvent.Ore);
                } else if (aEvent.Name.equals("sheetPlastic")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, aEvent.Ore);
                } else if (aEvent.Name.startsWith("shard")) {
                    switch (aEvent.Name) {
                        case "shardAir" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedAir, aEvent.Ore);
                            return;
                        }
                        case "shardWater" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedWater, aEvent.Ore);
                            return;
                        }
                        case "shardFire" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedFire, aEvent.Ore);
                            return;
                        }
                        case "shardEarth" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEarth, aEvent.Ore);
                            return;
                        }
                        case "shardOrder" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedOrder, aEvent.Ore);
                            return;
                        }
                        case "shardEntropy" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEntropy, aEvent.Ore);
                            return;
                        }
                    }
                } else if (aEvent.Name.equals("fieryIngot")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.FierySteel, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("ironwood")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.IronWood, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("steeleaf")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Steeleaf, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("knightmetal")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Knightmetal, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("compressedAluminum")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.compressed, Materials.Aluminium, aEvent.Ore);
                    return;
                } else if (aEvent.Name.contains(" ")) {
                    GTLog.ore.println(
                        tModToName + " is getting re-registered because the OreDict Name containing invalid spaces.");
                    GTOreDictUnificator
                        .registerOre(aEvent.Name.replaceAll(" ", ""), GTUtility.copyAmount(1, aEvent.Ore));
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                } else if (this.mInvalidNames.contains(aEvent.Name)) {
                    GTLog.ore.println(tModToName + " is wrongly registered and therefor getting ignored.");

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
                    GTLog.ore.println(tModToName + " is invalid due to being solely lowercased.");
                    return;
                } else if (aEvent.Name.toUpperCase()
                    .equals(aEvent.Name)) {
                        GTLog.ore.println(tModToName + " is invalid due to being solely uppercased.");
                        return;
                    } else if (Character.isUpperCase(aEvent.Name.charAt(0))) {
                        GTLog.ore.println(tModToName + " is invalid due to the first character being uppercased.");
                    }
            } else {
                if (aPrefix.skipActiveUnification()) {
                    GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                }
                if (aPrefix != aPrefix.mPrefixInto) {
                    String tNewName = aEvent.Name.replaceFirst(aPrefix.toString(), aPrefix.mPrefixInto.toString());
                    if (!GTOreDictUnificator.isRegisteringOres()) {
                        GTLog.ore.println(
                            tModToName + " uses a depricated Prefix, and is getting re-registered as " + tNewName);
                    }
                    GTOreDictUnificator.registerOre(tNewName, aEvent.Ore);
                    return;
                }
                String tName = aEvent.Name.replaceFirst(aPrefix.toString(), "");
                if (!tName.isEmpty()) {
                    char firstChar = tName.charAt(0);
                    if (Character.isUpperCase(firstChar) || Character.isLowerCase(firstChar)
                        || firstChar == '_'
                        || Character.isDigit(firstChar)) {
                        if (aPrefix.isMaterialBased()) {
                            aMaterial = Materials.get(tName);
                            if (aMaterial != aMaterial.mMaterialInto) {
                                GTOreDictUnificator.registerOre(aPrefix, aMaterial.mMaterialInto, aEvent.Ore);
                                if (!GTOreDictUnificator.isRegisteringOres()) {
                                    GTLog.ore.println(
                                        tModToName + " uses a deprecated Material and is getting re-registered as "
                                            + aPrefix.get(aMaterial.mMaterialInto));
                                }
                                return;
                            }
                            if (!aPrefix.isIgnored(aMaterial)) {
                                aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                            }
                            if (aMaterial != Materials._NULL) {
                                Materials tReRegisteredMaterial;
                                for (Iterator<Materials> i$ = aMaterial.mOreReRegistrations.iterator(); i$
                                    .hasNext(); GTOreDictUnificator
                                        .registerOre(aPrefix, tReRegisteredMaterial, aEvent.Ore)) {
                                    tReRegisteredMaterial = i$.next();
                                }
                                aMaterial.add(GTUtility.copyAmount(1, aEvent.Ore));

                                if (GregTechAPI.sThaumcraftCompat != null && aPrefix.doGenerateItem(aMaterial)
                                    && !aPrefix.isIgnored(aMaterial)) {
                                    List<TC_AspectStack> tAspects = new ArrayList<>();
                                    for (TC_AspectStack tAspect : aPrefix.mAspects) tAspect.addToAspectList(tAspects);
                                    if (aPrefix.getMaterialAmount() >= 3628800 || aPrefix.getMaterialAmount() < 0)
                                        for (TC_AspectStack tAspect : aMaterial.mAspects)
                                            tAspect.addToAspectList(tAspects);
                                    GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(
                                        GTUtility.copyAmount(1, aEvent.Ore),
                                        tAspects,
                                        aEvent.Name);
                                }

                                switch (aPrefix.getName()) {
                                    case "crystal" -> {
                                        if ((aMaterial == Materials.CertusQuartz)
                                            || (aMaterial == Materials.NetherQuartz)
                                            || (aMaterial == Materials.Fluix)) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.gem, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case "gem" -> {
                                        if (aMaterial == Materials.Lapis || aMaterial == Materials.Sodalite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else
                                            if (aMaterial == Materials.InfusedAir || aMaterial == Materials.InfusedWater
                                                || aMaterial == Materials.InfusedFire
                                                || aMaterial == Materials.InfusedEarth
                                                || aMaterial == Materials.InfusedOrder
                                                || aMaterial == Materials.InfusedEntropy) {
                                                    GTOreDictUnificator.registerOre(
                                                        aMaterial.mName.replaceFirst("Infused", "shard"),
                                                        aEvent.Ore);
                                                } else if (aMaterial == Materials.Chocolate) {
                                                    GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                                } else if (aMaterial == Materials.CertusQuartz
                                                    || aMaterial == Materials.NetherQuartz) {
                                                        GTOreDictUnificator
                                                            .registerOre(OrePrefixes.item.get(aMaterial), aEvent.Ore);
                                                        GTOreDictUnificator
                                                            .registerOre(OrePrefixes.crystal, aMaterial, aEvent.Ore);
                                                        GTOreDictUnificator
                                                            .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                    } else
                                                    if (aMaterial == Materials.Fluix || aMaterial == Materials.Quartz
                                                        || aMaterial == Materials.Quartzite) {
                                                            GTOreDictUnificator.registerOre(
                                                                OrePrefixes.crystal,
                                                                aMaterial,
                                                                aEvent.Ore);
                                                            GTOreDictUnificator
                                                                .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                        }
                                    }
                                    case "cableGt01" -> {
                                        if (aMaterial == Materials.Tin) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireTin, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyCopper) {
                                            GTOreDictUnificator
                                                .registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                                        } else if (aMaterial == Materials.Gold) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireGold, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyIron) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireIron, aEvent.Ore);
                                        }
                                    }
                                    case "lens" -> {
                                        if ((aMaterial.contains(SubTag.TRANSPARENT))
                                            && (aMaterial.mColor != Dyes._NULL)) {
                                            GTOreDictUnificator.registerOre(
                                                "craftingLens" + aMaterial.mColor.toString()
                                                    .replaceFirst("dye", ""),
                                                aEvent.Ore);
                                        }
                                    }
                                    case "plate" -> {
                                        if ((aMaterial == Materials.Polyethylene) || (aMaterial == Materials.Rubber)) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.sheet, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Silicon) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.item, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                            GTOreDictUnificator.registerOre(OrePrefixes.plank, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case "cell" -> {
                                        if (aMaterial == Materials.Empty) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        }
                                    }
                                    case "gearGt" -> GTOreDictUnificator
                                        .registerOre(OrePrefixes.gear, aMaterial, aEvent.Ore);
                                    case "stick" -> {
                                        if (!GTRecipeRegistrator.sRodMaterialList.contains(aMaterial)) {
                                            GTRecipeRegistrator.sRodMaterialList.add(aMaterial);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        } else if ((aMaterial == Materials.Tin) || (aMaterial == Materials.Lead)
                                            || (aMaterial == Materials.SolderingAlloy)) {
                                                GTOreDictUnificator
                                                    .registerOre(ToolDictNames.craftingToolSolderingMetal, aEvent.Ore);
                                            }
                                    }
                                    case "dust" -> {
                                        if (aMaterial == Materials.Salt) {
                                            GTOreDictUnificator.registerOre("itemSalt", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.registerOre("pulpWood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wheat) {
                                            GTOreDictUnificator.registerOre("foodFlour", aEvent.Ore);
                                        } else if (aMaterial == Materials.Lapis) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else if (aMaterial == Materials.Sodalite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Cocoa) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                            GTOreDictUnificator.registerOre("foodCocoapowder", aEvent.Ore);
                                        } else if (aMaterial == Materials.Coffee) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.BrownLimonite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.YellowLimonite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeYellow, aEvent.Ore);
                                        }
                                    }
                                    case "ingot" -> {
                                        if (aMaterial == Materials.Rubber) {
                                            GTOreDictUnificator.registerOre("itemRubber", aEvent.Ore);
                                        } else if (aMaterial == Materials.FierySteel) {
                                            GTOreDictUnificator.registerOre("fieryIngot", aEvent.Ore);
                                        } else if (aMaterial == Materials.IronWood) {
                                            GTOreDictUnificator.registerOre("ironwood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Steeleaf) {
                                            GTOreDictUnificator.registerOre("steeleaf", aEvent.Ore);
                                        } else if (aMaterial == Materials.Knightmetal) {
                                            GTOreDictUnificator.registerOre("knightmetal", aEvent.Ore);
                                        } else if ((aMaterial == Materials.Brass) && (aEvent.Ore.getItemDamage() == 2)
                                            && (aEvent.Ore.getUnlocalizedName()
                                                .equals("item.ingotBrass"))
                                            && (new ItemStack(aEvent.Ore.getItem(), 1, 0).getUnlocalizedName()
                                                .contains("red"))) {
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.RedAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 0));
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.BlueAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 1));
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.Brass,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 2));

                                                    GTValues.RA.stdBuilder()
                                                        .itemInputs(new ItemStack(aEvent.Ore.getItem(), 1, 3))
                                                        .itemOutputs(new ItemStack(aEvent.Ore.getItem(), 16, 4))
                                                        .duration(20 * SECONDS)
                                                        .eut(8)
                                                        .addTo(cutterRecipes);
                                                }
                                    }
                                    default -> {}
                                }
                                if (aPrefix.isUnifiable() && !aMaterial.mUnifiable) {
                                    return;
                                }
                            } else {
                                for (Dyes tDye : Dyes.VALUES) {
                                    if (aEvent.Name.endsWith(
                                        tDye.name()
                                            .replaceFirst("dye", ""))) {
                                        GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        GTLog.ore.println(
                                            tModToName
                                                + " Oh man, why the fuck would anyone need a OreDictified Color for this, that is even too much for GregTech... do not report this, this is just a random Comment about how ridiculous this is.");
                                        return;
                                    }
                                }
                                // GT_FML_LOGGER.info("Material Name: "+aEvent.Name+ "
                                // !!!Unknown Material detected!!! Please report to GregTech Intergalactical for
                                // additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just
                                // an Information, which you should pass to me.");
                                // GTLog.ore.println(tModToName + " uses an unknown
                                // Material. Report this to GregTech.");
                                return;
                            }
                        } else {
                            aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                        }
                    }
                } else if (aPrefix.isSelfReferencing()) {
                    aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                } else {
                    GTLog.ore.println(tModToName + " uses a Prefix as full OreDict Name, and is therefor invalid.");
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                }
                switch (aPrefix.getName()) {
                    case "dye" -> {
                        if (GTUtility.isStringValid(tName)) {
                            GTOreDictUnificator.registerOre(OrePrefixes.dye, aEvent.Ore);
                        }
                    }
                    case "stoneSmooth" -> GTOreDictUnificator.registerOre("stone", aEvent.Ore);
                    case "stoneCobble" -> GTOreDictUnificator.registerOre("cobblestone", aEvent.Ore);
                    case "plank" -> {
                        if (tName.equals("Wood")) {
                            GTOreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 3628800L));
                        }
                    }
                    case "slab" -> {
                        if (tName.equals("Wood")) {
                            GTOreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 1814400L));
                        }
                    }
                    case "sheet" -> {
                        if (tName.equals("Plastic")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, aEvent.Ore);
                        } else if (tName.equals("Rubber")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Rubber, aEvent.Ore);
                        }
                    }
                    case "crafting" -> {
                        switch (tName) {
                            case "ToolSolderingMetal" -> GregTechAPI.registerSolderingMetal(aEvent.Ore);
                            case "IndustrialDiamond" -> GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                            case "WireCopper" -> GTOreDictUnificator
                                .registerOre(OrePrefixes.wire, Materials.Copper, aEvent.Ore);
                        }
                    }
                    case "wood" -> {
                        if (tName.equals("Rubber")) {
                            GTOreDictUnificator.registerOre("logRubber", aEvent.Ore);
                        }
                    }
                    case "food" -> {
                        if (tName.equals("Cocoapowder")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.dust, Materials.Cocoa, aEvent.Ore);
                        }
                    }
                    default -> {}
                }
            }
            GTLog.ore.println(tModToName);

            OreDictEventContainer tOre = new OreDictEventContainer(aEvent, aPrefix, aMaterial, aMod);
            if ((!this.mOreDictActivated) || (!GregTechAPI.sUnificationEntriesRegistered)) {
                this.oreDictEvents.add(tOre);
            } else {
                this.oreDictEvents.clear();
            }
            if (this.mOreDictActivated) {
                OreDictEventContainer.registerRecipes(tOre);
            }
        } catch (Exception e) {
            GT_FML_LOGGER
                .error("Could not register ore (oredict name=" + aEvent.Name + ", item stack=" + aEvent.Ore + ")", e);
        }
    }

    @SubscribeEvent
    public void applyBlockWalkOverEffects(LivingUpdateEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        // the server should handle the movement of all entities except the players
        if (!entity.worldObj.isRemote && entity.onGround && !(entity instanceof EntityPlayerMP)) {
            int tX = MathHelper.floor_double(entity.posX),
                tY = MathHelper.floor_double(entity.boundingBox.minY - 0.001F),
                tZ = MathHelper.floor_double(entity.posZ);
            Block tBlock = entity.worldObj.getBlock(tX, tY, tZ);
            if (tBlock instanceof IBlockOnWalkOver)
                ((IBlockOnWalkOver) tBlock).onWalkOver(entity, entity.worldObj, tX, tY, tZ);
        }
    }

    @SubscribeEvent
    public void onFluidContainerRegistration(FluidContainerRegistry.FluidContainerRegisterEvent aFluidEvent) {
        if ((aFluidEvent.data.filledContainer.getItem() == Items.potionitem)
            && (aFluidEvent.data.filledContainer.getItemDamage() == 0)) {
            aFluidEvent.data.fluid.amount = 0;
        }
        GTOreDictUnificator.addToBlacklist(aFluidEvent.data.emptyContainer);
        GTOreDictUnificator.addToBlacklist(aFluidEvent.data.filledContainer);
        GTUtility.insertInFluidToContainersMap(aFluidEvent.data);
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent aEvent) {
        if (aEvent.side.isServer()) {
            if (aEvent.phase == TickEvent.Phase.START) {
                RunnableMachineUpdate.onBeforeTickLockLocked();
                TICK_LOCK.lock();
            } else {
                TICK_LOCK.unlock();
                RunnableMachineUpdate.onAfterTickLockReleased();
                GTMusicSystem.ServerSystem.tick();
            }
        }
    }

    private boolean isFirstWorldTick = true;

    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent aEvent) {
        if (!aEvent.side.isServer()) return;
        if (isFirstWorldTick) {
            for (Runnable runnable : GregTechAPI.sFirstWorldTick) runnable.run();
            isFirstWorldTick = false;
            GTValues.worldTickHappened = true;
        }
        if ((aEvent.world.getTotalWorldTime() % 100L == 0L)
            && ((this.mItemDespawnTime != 6000) || (this.mMaxEqualEntitiesAtOneSpot > 0))) {
            long startTime = System.nanoTime();
            double oldX = 0, oldY = 0, oldZ = 0;
            if (debugEntityCramming && (!aEvent.world.loadedEntityList.isEmpty())) {
                GTLog.out.println("CRAM: Entity list size " + aEvent.world.loadedEntityList.size());
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
                                        GTLog.out.println(
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
            if (debugEntityCramming && (!aEvent.world.loadedEntityList.isEmpty())) {
                GTLog.out
                    .println("CRAM: Time spent checking " + (System.nanoTime() - startTime) / 1000 + " microseconds");
            }
        }

        Pollution.onWorldTick(aEvent);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        for (TileEntity tileEntity : event.world.loadedTileEntityList) {
            if (tileEntity instanceof IGregTechTileEntity) {
                tileEntity.onChunkUnload();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickEventServer(TickEvent.PlayerTickEvent aEvent) {
        if ((!aEvent.side.isServer()) || (aEvent.phase == TickEvent.Phase.END) || (aEvent.player.isDead)) {
            return;
        }

        final boolean tHungerEffect = (this.mHungerEffect) && (aEvent.player.ticksExisted % 2400 == 1200);

        if (aEvent.player.ticksExisted % 120 != 0) {
            return;
        }

        int tCount = 64;
        final ItemStack[] mainInventory = aEvent.player.inventory.mainInventory;
        for (ItemStack tStack : mainInventory) {
            if (tStack == null) {
                continue;
            }
            if (!aEvent.player.capabilities.isCreativeMode) {
                GTUtility.applyRadioactivity(aEvent.player, GTUtility.getRadioactivityLevel(tStack), tStack.stackSize);
                final float tHeat = GTUtility.getHeatDamageFromItem(tStack);
                if (tHeat != 0.0F) {
                    if (tHeat > 0.0F) {
                        GTUtility.applyHeatDamageFromItem(aEvent.player, tHeat, tStack);
                    } else {
                        GTUtility.applyFrostDamage(aEvent.player, -tHeat);
                    }
                }
            }
            if (tHungerEffect) {
                tCount += tStack.stackSize * 64 / Math.max(1, tStack.getMaxStackSize());
            }
            if (this.mInventoryUnification) {
                GTOreDictUnificator.setStack(true, tStack);
            }

        }
        final ItemStack[] armorInventory = aEvent.player.inventory.armorInventory;
        for (final ItemStack tStack : armorInventory) {
            if (tStack == null) {
                continue;
            }
            if (!aEvent.player.capabilities.isCreativeMode) {
                GTUtility.applyRadioactivity(aEvent.player, GTUtility.getRadioactivityLevel(tStack), tStack.stackSize);
                final float tHeat = GTUtility.getHeatDamageFromItem(tStack);
                if (tHeat != 0.0F) {
                    if (tHeat > 0.0F) {
                        GTUtility.applyHeatDamageFromItem(aEvent.player, tHeat, tStack);
                    } else {
                        GTUtility.applyFrostDamage(aEvent.player, -tHeat);
                    }
                }
            }
            if (tHungerEffect) {
                tCount += 256;
            }
        }
        if (tHungerEffect) {
            aEvent.player.addExhaustion(Math.max(1.0F, tCount / 666.6F));
        }
    }

    public GTClientPreference getClientPreference(UUID aPlayerID) {
        return mClientPrefernces.get(aPlayerID);
    }

    public void setClientPreference(UUID aPlayerID, GTClientPreference aPreference) {
        mClientPrefernces.put(aPlayerID, aPreference);
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return 0;
        }
        int burnTime = 0;
        if (stack.getItem() instanceof MetaGeneratedItem) {
            final Short burnValue = ((MetaGeneratedItem) stack.getItem()).mBurnValues
                .get((short) stack.getItemDamage());
            if (burnValue != null) {
                burnTime = Math.max(burnTime, burnValue);
            }
        }
        final NBTTagCompound nbtTag = stack.getTagCompound();
        if (nbtTag != null) {
            // See if we have something defined by NBT
            final short gtBurnTime = nbtTag.getShort("GT.ItemFuelValue");
            burnTime = Math.max(burnTime, gtBurnTime);
        } else {
            // If not check the ore dict
            burnTime = Math.max(burnTime, burnTimeFromOreDict(stack));
        }

        // If we have something from the GT MetaGenerated_Item, ItemFuelValue, or OreDict return
        if (burnTime > 0) return burnTime;

        // Otherwise, a few more checks
        if (GTUtility.areStacksEqual(stack, new ItemStack(Blocks.wooden_button, 1))) return 150;
        else if (GTUtility.areStacksEqual(stack, new ItemStack(Blocks.ladder, 1))) return 100;
        else if (GTUtility.areStacksEqual(stack, new ItemStack(Items.sign, 1))) return 600;
        else if (GTUtility.areStacksEqual(stack, new ItemStack(Items.wooden_door, 1))) return 600;
        else if (GTUtility.areStacksEqual(stack, ItemList.Block_MSSFUEL.get(1))) return 150000;
        else if (GTUtility.areStacksEqual(stack, ItemList.Block_SSFUEL.get(1))) return 100000;

        return 0;
    }

    private static int burnTimeFromOreDict(ItemStack stack) {
        List<String> list = new ArrayList<>();
        for (int i : OreDictionary.getOreIDs(stack)) {
            String oreName = OreDictionary.getOreName(i);
            list.add(oreName);
        }
        boolean seen = false;
        int best = 0;
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            int gtValue = oreDictBurnTimes.getOrDefault(list.get(i), 0);
            if (!seen || gtValue > best) {
                seen = true;
                best = gtValue;
            }
        }
        return seen ? best : 0;
    }

    // ------------------------ Adds all fluids corresponding to materials ------------------------

    public Fluid addAutoGeneratedCorrespondingFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.fluid.{unlocalised_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? "fluid." + aMaterial.mName.toLowerCase()
            : "autogenerated";

        return GTFluidFactory.builder(aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName(aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mRGBa)
            .withStateAndTemperature(LIQUID, aMaterial.getLiquidTemperature())
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .addLocalizedName(aMaterial)
            .registerBContainers(GTOreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), ItemList.Cell_Empty.get(1L))
            .asFluid();
    }

    public Fluid addAutoGeneratedCorrespondingGas(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.gas.{unlocalised_name}.png. All
        // lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom ? ("gas." + aMaterial.mName.toLowerCase())
            : "autogenerated";

        return GTFluidFactory.builder(aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName(aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mRGBa)
            .withStateAndTemperature(GAS, aMaterial.getGasTemperature())
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .addLocalizedName(aMaterial)
            .registerBContainers(GTOreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), ItemList.Cell_Empty.get(1L))
            .asFluid();
    }

    public Fluid addAutogeneratedPlasmaFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.plasma.{texture_set_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom
            ? ("plasma." + aMaterial.mIconSet.aTextCustomAutogenerated.toLowerCase())
            : "plasma.autogenerated";

        return GTFluidFactory.builder("plasma." + aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName(aMaterial.mDefaultLocalName + " Plasma")
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mMoltenRGBa)
            .withStateAndTemperature(PLASMA, 10000)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .addLocalizedName(aMaterial)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellPlasma, aMaterial, 1L),
                ItemList.Cell_Empty.get(1L),
                aMaterial.getMolten(1) != null ? 1 * INGOTS : 1_000)
            .asFluid();
    }

    public Fluid addAutogeneratedMoltenFluid(Materials aMaterial) {
        // If the fluid is registered as custom inside the Material's constructor then to add custom fluid
        // textures go to blocks/fluids and place the .png. File should be called fluid.molten.{texture_set_name}.png.
        // All lower case.
        final String fluidTexture = aMaterial.mIconSet.is_custom
            ? ("molten." + aMaterial.mIconSet.aTextCustomAutogenerated.toLowerCase())
            : "molten.autogenerated";

        return GTFluidFactory.builder("molten." + aMaterial.mName.toLowerCase(Locale.ENGLISH))
            .withDefaultLocalName("Molten " + aMaterial.mDefaultLocalName)
            .withTextureName(fluidTexture)
            .withColorRGBA(aMaterial.mMoltenRGBa)
            .withStateAndTemperature(MOLTEN, aMaterial.mMeltingPoint < 0 ? 1000 : aMaterial.mMeltingPoint)
            .buildAndRegister()
            .configureMaterials(aMaterial)
            .addLocalizedName(aMaterial)
            .registerContainers(
                GTOreDictUnificator.get(OrePrefixes.cellMolten, aMaterial, 1L),
                ItemList.Cell_Empty.get(1L),
                1 * INGOTS)
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
            crackedFluids[i] = GTFluidFactory.builder(namePrefixes[i] + aMaterial.mName.toLowerCase(Locale.ENGLISH))
                .withIconsFrom(uncrackedFluid)
                .withDefaultLocalName(orePrefixes[i].getMaterialPrefix() + aMaterial.mDefaultLocalName)
                .withColorRGBA(aMaterial.mRGBa)
                .withStateAndTemperature(GAS, 775)
                .buildAndRegister()
                .addLocalizedName(aMaterial)
                .registerBContainers(
                    GTOreDictUnificator.get(orePrefixes[i], aMaterial, 1L),
                    ItemList.Cell_Empty.get(1L))
                .asFluid();

            int hydrogenAmount = 2 * i + 2;
            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(new FluidStack(uncrackedFluid, 1000), Materials.Hydrogen.getGas(hydrogenAmount * 800))
                .fluidOutputs(new FluidStack(crackedFluids[i], 1000))
                .duration((1 + i) * SECONDS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(crackingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Hydrogen.getCells(hydrogenAmount))
                .circuit(i + 1)
                .itemOutputs(Materials.Empty.getCells(hydrogenAmount))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1))
                .circuit(i + 1)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(Materials.Hydrogen.getGas(hydrogenAmount * 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(new FluidStack(uncrackedFluid, 1000), Materials.Hydrogen.getGas(hydrogenAmount * 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((4 + 2 * i) * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
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
            crackedFluids[i] = GTFluidFactory.builder(namePrefixes[i] + aMaterial.mName.toLowerCase(Locale.ENGLISH))
                .withIconsFrom(uncrackedFluid)
                .withDefaultLocalName(orePrefixes[i].getMaterialPrefix() + aMaterial.mDefaultLocalName)
                .withColorRGBA(aMaterial.mRGBa)
                .withStateAndTemperature(GAS, 775)
                .buildAndRegister()
                .addLocalizedName(aMaterial)
                .registerBContainers(
                    GTOreDictUnificator.get(orePrefixes[i], aMaterial, 1L),
                    ItemList.Cell_Empty.get(1L))
                .asFluid();

            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(new FluidStack(uncrackedFluid, 1_000), Materials.Steam.getGas(1_000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 1_200))
                .duration((1 + i) * SECONDS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(crackingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getIC2Item("steamCell", 1L))
                .circuit(i + 1)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(new FluidStack(uncrackedFluid, 1_000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1))
                .circuit(i + 1)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(Materials.Steam.getGas(1_000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aMaterial.getCells(1))
                .circuit(i + 1)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(getFluidStack("ic2steam", 1000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(new FluidStack(uncrackedFluid, 1_000), Materials.Steam.getGas(1_000))
                .fluidOutputs(new FluidStack(crackedFluids[i], 800))
                .duration((4 + 2 * i) * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        }
        aMaterial.setSteamCrackedFluids(crackedFluids);
    }

    public void registerUnificationEntries() {
        GTOreDictUnificator.resetUnificationEntries();
        for (OreDictEventContainer tOre : this.oreDictEvents) {
            if ((tOre.mPrefix != null) && (tOre.mPrefix.isUnifiable()) && (tOre.mMaterial != null)) {
                if (GTOreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
                    GTOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
                } else {
                    GTOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                    GTOreDictUnificator.set(
                        tOre.mPrefix,
                        tOre.mMaterial,
                        tOre.mEvent.Ore,
                        (tOre.mModID != null)
                            && ManualOreDictTweaks.shouldOredictBeOverwritten(tOre.mModID, tOre.mEvent.Name),
                        true);
                }
            }
        }
        GregTechAPI.sUnificationEntriesRegistered = true;
        GTRecipe.reInit();
    }

    @SuppressWarnings("deprecation")
    public void activateOreDictHandler() {
        this.mOreDictActivated = true;
        ProgressManager.ProgressBar progressBar;
        if (BetterLoadingScreen.isModLoaded()) {
            progressBar = ProgressManager.push("Register materials", oreDictEvents.size());
            GTCLSCompat.stepMaterialsCLS(oreDictEvents, progressBar);
        } else {
            if (isClientSide()) {
                progressBar = ProgressManager.push("Register materials", oreDictEvents.size());
            } else progressBar = null;
            this.stepMaterialsVanilla(progressBar);
        }
    }

    @SuppressWarnings("deprecation")
    private void stepMaterialsVanilla(@Nullable ProgressManager.ProgressBar progressBar) {
        int size = 5;
        int sizeStep = oreDictEvents.size() / 20 - 1;
        OreDictEventContainer event;
        for (Iterator<OreDictEventContainer> i$ = oreDictEvents.iterator(); i$.hasNext(); OreDictEventContainer
            .registerRecipes(event)) {
            event = i$.next();
            sizeStep--;
            if (sizeStep == 0) {
                GT_FML_LOGGER.info("Baking : " + size + "%");
                sizeStep = oreDictEvents.size() / 20 - 1;
                size += 5;
            }
            if (progressBar != null) {
                progressBar.step(event.mMaterial == null ? "" : event.mMaterial.toString());
            }
        }
        if (progressBar != null) {
            ProgressManager.pop(progressBar);
        }
    }

    @SubscribeEvent
    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        UndergroundOil.migrate(event);
        Pollution.migrate(event);
    }

    @SubscribeEvent
    public void onBlockBreakSpeedEvent(PlayerEvent.BreakSpeed aEvent) {
        if (aEvent.entityPlayer != null) {
            ItemStack aStack = aEvent.entityPlayer.getCurrentEquippedItem();
            if ((aStack != null) && ((aStack.getItem() instanceof MetaGeneratedTool))) {
                aEvent.newSpeed = ((MetaGeneratedTool) aStack.getItem()).onBlockBreakSpeedEvent(
                    aEvent.newSpeed,
                    aStack,
                    aEvent.entityPlayer,
                    aEvent.block,
                    aEvent.x,
                    aEvent.y,
                    aEvent.z,
                    aEvent.metadata,
                    aEvent);
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (event.block.getUnlocalizedName()
            .equals("blockAlloyGlass")) GregTechAPI.causeMachineUpdate(event.world, event.x, event.y, event.z);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerMap$onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) {
            // this should never happen
            return;
        }
        final UUID UUID = player.getGameProfile()
            .getId();
        if (UUID != null) {
            PLAYERS_BY_UUID.put(UUID, player);
            UUID_BY_NAME.put(player.getCommandSenderName(), UUID);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerMap$onPlayerLeft(PlayerLoggedOutEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) {
            // this should never happen
            return;
        }
        final UUID UUID = player.getGameProfile()
            .getId();
        if (UUID != null) {
            PLAYERS_BY_UUID.remove(UUID);
            UUID_BY_NAME.remove(UUID);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerMap$onPlayerChangeDim(PlayerChangedDimensionEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) {
            // this should never happen
            return;
        }
        final UUID UUID = player.getGameProfile()
            .getId();
        if (UUID != null) {
            PLAYERS_BY_UUID.put(UUID, player);
            UUID_BY_NAME.put(player.getCommandSenderName(), UUID);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerMap$onPlayerRespawn(PlayerRespawnEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) {
            // this should never happen
            return;
        }
        final UUID UUID = player.getGameProfile()
            .getId();
        if (UUID != null) {
            PLAYERS_BY_UUID.put(UUID, player);
            UUID_BY_NAME.put(player.getCommandSenderName(), UUID);
        }
    }

    /**
     * This method allows fast lookup of EntityPlayerMp from UUID. It should only ever be called from the ServerThread
     * and while the Server is running.
     *
     * @param uuid - uuid of the EntityPlayerMP
     */
    @Nullable
    public EntityPlayerMP getPlayerMP(UUID uuid) {
        if (!FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) {
            throw new RuntimeException("Tried to retrieve an EntityPlayerMP from outside of the server thread!");
        }
        if (PLAYERS_BY_UUID != null) {
            return PLAYERS_BY_UUID.get(uuid);
        } else {
            throw new NullPointerException("PLAYERS_BY_ID is null because the server is not running!");
        }
    }

    /**
     * This method allows fast lookup of player UUID from their name. It should only ever be called from the
     * ServerThread and while the Server is running.
     *
     * @param playername - the name of the player as returned by getCommandSenderName()
     */
    public UUID getPlayersUUID(String playername) {
        if (!FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) {
            throw new RuntimeException("Tried to retrieve a player UUID from outside of the server thread!");
        }
        if (UUID_BY_NAME != null) {
            return UUID_BY_NAME.get(playername);
        } else {
            throw new NullPointerException("UUID_BY_NAME is null because the server is not running!");
        }
    }

}
