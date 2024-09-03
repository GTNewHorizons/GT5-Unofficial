package gregtech;

import static gregtech.GT_Version.VERSION_MAJOR;
import static gregtech.GT_Version.VERSION_MINOR;
import static gregtech.GT_Version.VERSION_PATCH;
import static gregtech.api.GregTechAPI.registerCircuitProgrammer;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.util.GTRecipe.setItemStacks;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;
import com.google.common.collect.SetMultimap;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import appeng.api.AEApi;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import galacticgreg.SpaceDimRegisterer;
import gregtech.api.GregTechAPI;
import gregtech.api.enchants.EnchantmentEnderDamage;
import gregtech.api.enchants.EnchantmentHazmat;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.internal.IGTMod;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.registries.LHECoolantRegistry;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.AssemblyLineServer;
import gregtech.api.util.GTForestryCompat;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTSpawnEventHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.ItemHolder;
import gregtech.common.GTDummyWorld;
import gregtech.common.GTNetwork;
import gregtech.common.GTProxy;
import gregtech.common.RecipeAdder;
import gregtech.common.config.client.ConfigColorModulation;
import gregtech.common.config.client.ConfigInterface;
import gregtech.common.config.client.ConfigPreference;
import gregtech.common.config.client.ConfigRender;
import gregtech.common.config.client.ConfigWaila;
import gregtech.common.config.gregtech.ConfigDebug;
import gregtech.common.config.gregtech.ConfigFeatures;
import gregtech.common.config.gregtech.ConfigGeneral;
import gregtech.common.config.gregtech.ConfigHarvestLevel;
import gregtech.common.config.gregtech.ConfigMachines;
import gregtech.common.config.gregtech.ConfigOreDropBehavior;
import gregtech.common.config.gregtech.ConfigPollution;
import gregtech.common.config.machinestats.ConfigBronzeSolarBoiler;
import gregtech.common.config.machinestats.ConfigMassFabricator;
import gregtech.common.config.machinestats.ConfigMicrowaveEnergyTransmitter;
import gregtech.common.config.machinestats.ConfigSteelSolarBoiler;
import gregtech.common.config.machinestats.ConfigTeleporter;
import gregtech.common.config.worldgen.ConfigEndAsteroids;
import gregtech.common.covers.CoverFacadeAE;
import gregtech.common.misc.GTCommand;
import gregtech.common.misc.spaceprojects.commands.SPCommand;
import gregtech.common.misc.spaceprojects.commands.SPMCommand;
import gregtech.common.misc.spaceprojects.commands.SpaceProjectCommand;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import gregtech.crossmod.holoinventory.HoloInventory;
import gregtech.crossmod.waila.Waila;
import gregtech.loaders.load.CoverBehaviorLoader;
import gregtech.loaders.load.FuelLoader;
import gregtech.loaders.load.GTItemIterator;
import gregtech.loaders.load.MTERecipeLoader;
import gregtech.loaders.load.SonictronLoader;
import gregtech.loaders.misc.CoverLoader;
import gregtech.loaders.misc.GTAchievements;
import gregtech.loaders.misc.GTBees;
import gregtech.loaders.postload.BlockResistanceLoader;
import gregtech.loaders.postload.BookAndLootLoader;
import gregtech.loaders.postload.CraftingRecipeLoader;
import gregtech.loaders.postload.CropLoader;
import gregtech.loaders.postload.FakeRecipeLoader;
import gregtech.loaders.postload.GTPostLoad;
import gregtech.loaders.postload.GTWorldgenloader;
import gregtech.loaders.postload.ItemMaxStacksizeLoader;
import gregtech.loaders.postload.MachineRecipeLoader;
import gregtech.loaders.postload.MachineTooltipsLoader;
import gregtech.loaders.postload.MinableRegistrator;
import gregtech.loaders.postload.PosteaTransformers;
import gregtech.loaders.postload.RecyclerBlacklistLoader;
import gregtech.loaders.postload.ScrapboxDropLoader;
import gregtech.loaders.preload.GTPreLoad;
import gregtech.loaders.preload.GT_Loader_MultiTileEntities;
import gregtech.loaders.preload.LoaderCircuitBehaviors;
import gregtech.loaders.preload.LoaderGTBlockFluid;
import gregtech.loaders.preload.LoaderGTItemData;
import gregtech.loaders.preload.LoaderGTOreDictionary;
import gregtech.loaders.preload.LoaderMetaTileEntities;
import gregtech.loaders.preload.LoaderOreProcessing;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

@Mod(
    modid = Mods.Names.GREG_TECH,
    name = "GregTech",
    version = "MC1710",
    guiFactory = "gregtech.client.GTGuiFactory",
    dependencies = " required-after:IC2;" + " required-after:structurelib;"
        + " required-after:gtnhlib@[0.2.1,);"
        + " required-after:modularui@[1.1.12,);"
        + " required-after:appliedenergistics2@[rv3-beta-258,);"
        + " after:dreamcraft;"
        + " after:Forestry;"
        + " after:PFAAGeologica;"
        + " after:Thaumcraft;"
        + " after:Railcraft;"
        + " after:ThermalExpansion;"
        + " after:TwilightForest;"
        + " after:harvestcraft;"
        + " after:magicalcrops;"
        + " after:Botania;"
        + " after:BuildCraft|Transport;"
        + " after:BuildCraft|Silicon;"
        + " after:BuildCraft|Factory;"
        + " after:BuildCraft|Energy;"
        + " after:BuildCraft|Core;"
        + " after:BuildCraft|Builders;"
        + " after:GalacticraftCore;"
        + " after:GalacticraftMars;"
        + " after:GalacticraftPlanets;"
        + " after:ThermalExpansion|Transport;"
        + " after:ThermalExpansion|Energy;"
        + " after:ThermalExpansion|Factory;"
        + " after:RedPowerCore;"
        + " after:RedPowerBase;"
        + " after:RedPowerMachine;"
        + " after:RedPowerCompat;"
        + " after:RedPowerWiring;"
        + " after:RedPowerLogic;"
        + " after:RedPowerLighting;"
        + " after:RedPowerWorld;"
        + " after:RedPowerControl;"
        + " after:UndergroundBiomes;"
        + " after:TConstruct;"
        + " after:Translocator;"
        + " after:gendustry;")
public class GTMod implements IGTMod {

    static {
        try {
            // Client
            ConfigurationManager.registerConfig(ConfigColorModulation.class);
            ConfigurationManager.registerConfig(ConfigInterface.class);
            ConfigurationManager.registerConfig(ConfigPreference.class);
            ConfigurationManager.registerConfig(ConfigRender.class);
            ConfigurationManager.registerConfig(ConfigWaila.class);

            // GregTech.cfg
            ConfigurationManager.registerConfig(ConfigDebug.class);
            ConfigurationManager.registerConfig(ConfigFeatures.class);
            ConfigurationManager.registerConfig(ConfigGeneral.class);
            ConfigurationManager.registerConfig(ConfigHarvestLevel.class);
            ConfigurationManager.registerConfig(ConfigMachines.class);
            ConfigurationManager.registerConfig(ConfigOreDropBehavior.class);
            ConfigurationManager.registerConfig(ConfigPollution.class);

            // MachineStats.cfg
            ConfigurationManager.registerConfig(ConfigBronzeSolarBoiler.class);
            ConfigurationManager.registerConfig(gregtech.common.config.machinestats.ConfigMachines.class);
            ConfigurationManager.registerConfig(ConfigMassFabricator.class);
            ConfigurationManager.registerConfig(ConfigMicrowaveEnergyTransmitter.class);
            ConfigurationManager.registerConfig(ConfigSteelSolarBoiler.class);
            ConfigurationManager.registerConfig(ConfigTeleporter.class);

            // OverPoweredStuff
            ConfigurationManager.registerConfig(gregtech.common.config.opstuff.ConfigGeneral.class);

            // Other
            ConfigurationManager.registerConfig(gregtech.common.config.other.ConfigGeneral.class);

            // WorldGeneration
            ConfigurationManager.registerConfig(ConfigEndAsteroids.class);
            ConfigurationManager.registerConfig(gregtech.common.config.worldgen.ConfigGeneral.class);

        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int NBT_VERSION = calculateTotalGTVersion(VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);

    @Mod.Instance(Mods.Names.GREG_TECH)
    public static GTMod instance;

    @SidedProxy(
        modId = Mods.Names.GREG_TECH,
        clientSide = "gregtech.common.GTClient",
        serverSide = "gregtech.common.GTServer")
    public static GTProxy gregtechproxy;
    public static final boolean DEBUG = Boolean.getBoolean("gt.debug");;

    public static int MAX_IC2 = 2147483647;
    public static GTAchievements achievements;
    @Deprecated
    public static final String aTextGeneral = "general";
    public static final String aTextIC2 = "ic2_";
    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");

    @SuppressWarnings("deprecation")
    public GTMod() {
        GTValues.GT = this;
        GTValues.DW = new GTDummyWorld();
        GTValues.NW = new GTNetwork();
        GTValues.RA = new RecipeAdder();

        for (int i = 0; i < 4; i++) {
            GregTechAPI.registerTileEntityConstructor(i, i2 -> GregTechAPI.constructBaseMetaTileEntity());
        }
        for (int i = 4; i < 12; i++) {
            GregTechAPI.registerTileEntityConstructor(i, i2 -> new BaseMetaPipeEntity());
        }

        // noinspection deprecation// Need run-time initialization
        GregTechAPI.sRecipeAdder = GTValues.RA;

        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.BlockIcons.VOID.name();
        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.ItemIcons.VOID.name();
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion) {
        return calculateTotalGTVersion(majorVersion, minorVersion, 0);
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion, int patchVersion) {
        return majorVersion * 1000000 + minorVersion * 1000 + patchVersion;
    }

    @Mod.EventHandler
    public void onPreLoad(FMLPreInitializationEvent aEvent) {
        Locale.setDefault(Locale.ENGLISH);
        if (GregTechAPI.sPreloadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTechAPI.sBeforeGTPreload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        GTPreLoad.getConfiguration(aEvent.getModConfigurationDirectory());
        GTPreLoad.createLogFiles(
            aEvent.getModConfigurationDirectory()
                .getParentFile());

        gregtechproxy.onPreLoad();

        GTLog.out.println("GTMod: Setting Configs");

        GTPreLoad.loadConfig();

        new EnchantmentHazmat();
        new EnchantmentEnderDamage();
        new EnchantmentRadioactivity();

        Materials.init();

        GTPreLoad.initLocalization(
            aEvent.getModConfigurationDirectory()
                .getParentFile());
        GTPreLoad.adjustScrap();

        AEApi.instance()
            .registries()
            .interfaceTerminal()
            .register(MTEHatchCraftingInputME.class);

        GTPreLoad.runMineTweakerCompat();

        new LoaderOreProcessing().run();
        new LoaderGTOreDictionary().run();
        new LoaderGTItemData().run();
        new LoaderGTBlockFluid().run();
        new LoaderMetaTileEntities().run();
        if (GTValues.enableMultiTileEntities) {
            new GT_Loader_MultiTileEntities().run();
        }

        new LoaderCircuitBehaviors().run();
        new CoverBehaviorLoader().run();
        new SonictronLoader().run();
        new GTSpawnEventHandler();

        // populate itemstack instance for NBT check in GTRecipe
        setItemStacks();

        GTPreLoad.sortToTheEnd();
        GregTechAPI.sPreloadFinished = true;
        GTLog.out.println("GTMod: Preload-Phase finished!");
        GTLog.ore.println("GTMod: Preload-Phase finished!");

        GTUIInfos.init();

        for (Runnable tRunnable : GregTechAPI.sAfterGTPreload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) AssemblyLineServer.fillMap(aEvent);
    }

    @Mod.EventHandler
    public void onLoad(FMLInitializationEvent aEvent) {
        if (GregTechAPI.sLoadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTechAPI.sBeforeGTLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        if (Forestry.isModLoaded())
            // noinspection InstantiationOfUtilityClass//TODO: Refactor GTBees with proper state handling
            new GTBees();

        // Disable Low Grav regardless of config if Cleanroom is disabled.
        if (!gregtechproxy.mEnableCleanroom) {
            gregtechproxy.mLowGravProcessing = false;
        }

        gregtechproxy.onLoad();

        registerCircuitProgrammer(new Predicate<>() {

            private final int screwdriverOreId = OreDictionary.getOreID("craftingToolScrewdriver");

            @Override
            public boolean test(ItemStack stack) {
                for (int i : OreDictionary.getOreIDs(stack)) if (i == screwdriverOreId) return true;
                return false;
            }
        }, true);

        new MTERecipeLoader().run();

        new GTItemIterator().run();
        gregtechproxy.registerUnificationEntries();
        new FuelLoader().run();

        if (Mods.Waila.isModLoaded()) {
            Waila.init();
        }
        if (Mods.HoloInventory.isModLoaded()) {
            HoloInventory.init();
        }

        LHECoolantRegistry.registerBaseCoolants();

        GT_FML_LOGGER.debug("Registering SpaceDimensions");
        SpaceDimRegisterer.register();

        GregTechAPI.sLoadFinished = true;
        GTLog.out.println("GTMod: Load-Phase finished!");
        GTLog.ore.println("GTMod: Load-Phase finished!");

        for (Runnable tRunnable : GregTechAPI.sAfterGTLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }
    }

    @Mod.EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent) {
        if (GregTechAPI.sPostloadStarted) {
            return;
        }

        // Seems only used by GGFab so far
        for (Runnable tRunnable : GregTechAPI.sBeforeGTPostload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        gregtechproxy.onPostLoad();

        if (DEBUG) {
            // Prints all the used MTE id and their associated TE name, turned on with -Dgt.debug=true in jvm args
            final int bound = GregTechAPI.METATILEENTITIES.length;
            for (int i1 = 1; i1 < bound; i1++) {
                if (GregTechAPI.METATILEENTITIES[i1] != null) {
                    GTLog.out.printf("META %d %s\n", i1, GregTechAPI.METATILEENTITIES[i1].getMetaName());
                }
            }
        }

        gregtechproxy.registerUnificationEntries();

        new BookAndLootLoader().run();
        new ItemMaxStacksizeLoader().run();
        new BlockResistanceLoader().run();
        new RecyclerBlacklistLoader().run();
        new MinableRegistrator().run();
        new FakeRecipeLoader().run();
        new MachineRecipeLoader().run();
        new ScrapboxDropLoader().run();
        new CropLoader().run();
        new GTWorldgenloader().run();
        new CoverLoader().run();

        GTRecipeRegistrator.registerUsagesForMaterials(
            null,
            false,
            new ItemStack(Blocks.planks, 1),
            new ItemStack(Blocks.cobblestone, 1),
            new ItemStack(Blocks.stone, 1),
            new ItemStack(Items.leather, 1));

        GTOreDictUnificator.addItemData(
            GTModHandler.getRecipeOutput(
                null,
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                null,
                null),
            new ItemData(Materials.Tin, 10886400L));
        GTModHandler.removeRecipe(
            new ItemStack(Items.glowstone_dust, 1),
            new ItemStack(Items.glowstone_dust, 1),
            null,
            new ItemStack(Items.glowstone_dust, 1),
            new ItemStack(Items.glowstone_dust, 1));
        GTModHandler.removeRecipeDelayed(
            new ItemStack(Blocks.wooden_slab, 1, 0),
            new ItemStack(Blocks.wooden_slab, 1, 1),
            new ItemStack(Blocks.wooden_slab, 1, 2));
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.wooden_slab, 6, 0),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WWW", 'W', new ItemStack(Blocks.planks, 1, 0) });

        // Save a copy of these list before activateOreDictHandler(), then loop over them.
        Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList = GTModHandler.getMaceratorRecipeList();
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList = GTModHandler.getCompressorRecipeList();
        Map<IRecipeInput, RecipeOutput> aExtractorRecipeList = GTModHandler.getExtractorRecipeList();
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList = GTModHandler.getOreWashingRecipeList();
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList = GTModHandler.getThermalCentrifugeRecipeList();

        GTLog.out.println(
            "GTMod: Activating OreDictionary Handler, this can take some time, as it scans the whole OreDictionary");
        GT_FML_LOGGER.info(
            "If your Log stops here, you were too impatient. Wait a bit more next time, before killing Minecraft with the Task Manager.");

        GTPostLoad.activateOreDictHandler();
        GTPostLoad.replaceVanillaMaterials();
        GTPostLoad.removeIc2Recipes(
            aMaceratorRecipeList,
            aCompressorRecipeList,
            aExtractorRecipeList,
            aOreWashingRecipeList,
            aThermalCentrifugeRecipeList);

        if (GTValues.D1) {
            GTModHandler.sSingleNonBlockDamagableRecipeList.forEach(
                iRecipe -> GTLog.out.println(
                    "=> " + iRecipe.getRecipeOutput()
                        .getDisplayName()));
        }
        new CraftingRecipeLoader().run();
        GTModHandler.removeRecipeByOutput(ItemList.IC2_ForgeHammer.getWildcard(1L));
        GTModHandler.removeRecipeByOutput(GTModHandler.getIC2Item("machine", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("machine", 1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "RwR", "RRR", 'R', OrePrefixes.plate.get(Materials.Iron) });

        GTPostLoad.registerFluidCannerRecipes();

        if (Forestry.isModLoaded()) {
            GTForestryCompat.transferCentrifugeRecipes();
            GTForestryCompat.transferSqueezerRecipes();
        }
        MTEDigitalChestBase.registerAEIntegration();
        ItemStack facade = AEApi.instance()
            .definitions()
            .items()
            .facade()
            .maybeItem()
            .transform(i -> new ItemStack(i, 1, GTValues.W))
            .orNull();
        if (facade != null) {
            GregTechAPI.registerCover(facade, null, new CoverFacadeAE());
        }

        Arrays
            .stream(
                new String[] { "blastfurnace", "blockcutter", "inductionFurnace", "generator", "windMill", "waterMill",
                    "solarPanel", "centrifuge", "electrolyzer", "compressor", "electroFurnace", "extractor",
                    "macerator", "recycler", "metalformer", "orewashingplant", "massFabricator", "replicator", })
            .map(tName -> GTModHandler.getIC2Item(tName, 1L))
            .forEach(GTModHandler::removeRecipeByOutputDelayed);

        GTPostLoad.nerfVanillaTools();

        // Register postea transformers
        new PosteaTransformers().run();

        /*
         * Until this point most crafting recipe additions, and removals, have been buffered. Go through, execute the
         * removals in bulk, and then any deferred additions. The bulk removals in particular significantly speed up the
         * recipe list modifications.
         */

        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GTLog.out.println("GTMod: Adding buffered Recipes.");
        GTModHandler.stopBufferingCraftingRecipes();
        // noinspection UnstableApiUsage// Stable enough for this project
        GT_FML_LOGGER.info("Executed delayed Crafting Recipes (" + stopwatch.stop() + "). Have a Cake.");

        GTLog.out.println("GTMod: Saving Lang File.");
        new MachineTooltipsLoader().run();
        GTLanguageManager.sEnglishFile.save();
        GregTechAPI.sPostloadFinished = true;
        GTLog.out.println("GTMod: PostLoad-Phase finished!");
        GTLog.ore.println("GTMod: PostLoad-Phase finished!");
        for (Runnable tRunnable : GregTechAPI.sAfterGTPostload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }
        GTPostLoad.addFakeRecipes();

        if (GregTechAPI.mOutputRF || GregTechAPI.mInputRF) {
            GTUtility.checkAvailabilities();
            if (!GTUtility.RF_CHECK) {
                GregTechAPI.mOutputRF = false;
                GregTechAPI.mInputRF = false;
            }
        }

        GTPostLoad.addSolidFakeLargeBoilerFuels();
        GTPostLoad.identifyAnySteam();

        achievements = new GTAchievements();

        GTRecipe.GTppRecipeHelper = true;
        GTLog.out.println("GTMod: Loading finished, de-allocating temporary Init Variables.");
        GregTechAPI.sBeforeGTPreload = null;
        GregTechAPI.sAfterGTPreload = null;
        GregTechAPI.sBeforeGTLoad = null;
        GregTechAPI.sAfterGTLoad = null;
        GregTechAPI.sBeforeGTPostload = null;
        GregTechAPI.sAfterGTPostload = null;

        GTPostLoad.createGTtoolsCreativeTab();
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent aEvent) {
        gregtechproxy.onLoadComplete();
        for (Runnable tRunnable : GregTechAPI.sGTCompleteLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }
        GregTechAPI.sGTCompleteLoad = null;
        GregTechAPI.sFullLoadFinished = true;
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent aEvent) {
        gregtechproxy.onServerStarted();
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent aEvent) {
        gregtechproxy.onServerAboutToStart();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent aEvent) {

        for (Runnable tRunnable : GregTechAPI.sBeforeGTServerstart) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        gregtechproxy.onServerStarting();
        GTModHandler.removeAllIC2Recipes();
        GTLog.out.println("GTMod: Unificating outputs of all known Recipe Types.");
        ArrayList<ItemStack> tStacks = new ArrayList<>(10000);
        GTLog.out.println("GTMod: IC2 Machines");

        ic2.api.recipe.Recipes.cannerBottle.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.centrifuge.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.compressor.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.extractor.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.macerator.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerCutting.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerExtruding.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerRolling.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.matterAmplifier.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.oreWashing.getRecipes()
            .values()
            .stream()
            .map(t -> t.items)
            .forEach(tStacks::addAll);

        GTLog.out.println("GTMod: Dungeon Loot");
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("dungeonChest")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("bonusChest")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("villageBlacksmith")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdCrossing")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdLibrary")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdCorridor")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidJungleDispenser")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidJungleChest")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidDesertyChest")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("mineshaftCorridor")
            .getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        GTLog.out.println("GTMod: Smelting");

        // Deal with legacy Minecraft raw types
        tStacks.addAll(
            FurnaceRecipes.smelting()
                .getSmeltingList()
                .values());

        if (gregtechproxy.mCraftingUnification) {
            GTLog.out.println("GTMod: Crafting Recipes");
            for (IRecipe tRecipe : CraftingManager.getInstance()
                .getRecipeList()) {
                if ((tRecipe instanceof IRecipe)) {
                    tStacks.add(tRecipe.getRecipeOutput());
                }
            }
        }
        for (ItemStack tOutput : tStacks) {
            if (!gregtechproxy.mRegisteredOres.contains(tOutput)) {
                GTOreDictUnificator.setStack(tOutput);
            } else {
                logMultilineError(GT_FML_LOGGER, generateGTErr01Message(tOutput));
                tOutput.setStackDisplayName("ERROR! PLEASE CHECK YOUR LOG FOR 'GT-ERR-01'!");
            }
        }
        GregTechAPI.mServerStarted = true;
        GTLog.out.println("GTMod: ServerStarting-Phase finished!");
        GTLog.ore.println("GTMod: ServerStarting-Phase finished!");

        for (Runnable tRunnable : GregTechAPI.sAfterGTServerstart) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        aEvent.registerServerCommand(new GTCommand());
        aEvent.registerServerCommand(new SPCommand());
        aEvent.registerServerCommand(new SPMCommand());
        aEvent.registerServerCommand(new SpaceProjectCommand());
        // Sets a new Machine Block Update Thread everytime a world is loaded
        RunnableMachineUpdate.initExecutorService();
    }

    public boolean isServerSide() {
        return gregtechproxy.isServerSide();
    }

    public boolean isClientSide() {
        return gregtechproxy.isClientSide();
    }

    public boolean isBukkitSide() {
        return gregtechproxy.isBukkitSide();
    }

    public EntityPlayer getThePlayer() {
        return gregtechproxy.getThePlayer();
    }

    public int addArmor(String aArmorPrefix) {
        return gregtechproxy.addArmor(aArmorPrefix);
    }

    public void doSonictronSound(ItemStack aStack, World aWorld, double aX, double aY, double aZ) {
        gregtechproxy.doSonictronSound(aStack, aWorld, aX, aY, aZ);
    }

    @Mod.EventHandler
    public void onIDChangingEvent(FMLModIdMappingEvent aEvent) {
        GTUtility.reInit();
        GTRecipe.reInit();
        try {
            for (Map<?, ?> gt_itemStackMap : GregTechAPI.sItemStackMappings) {
                GTUtility.reMap(gt_itemStackMap);
            }
            for (SetMultimap<? extends ItemHolder, ?> gt_itemStackMap : GregTechAPI.itemStackMultiMaps) {
                GTUtility.reMap(gt_itemStackMap);
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent aEvent) {
        for (Runnable tRunnable : GregTechAPI.sBeforeGTServerstop) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }

        gregtechproxy.onServerStopping();

        for (Runnable tRunnable : GregTechAPI.sAfterGTServerstop) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
        }
        // Interrupt IDLE Threads to close down cleanly
        RunnableMachineUpdate.shutdownExecutorService();
    }

    public static void logStackTrace(Throwable t) {
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        GT_FML_LOGGER.error(sw);
    }

    private static String[] generateGTErr01Message(ItemStack stack) {
        // The message is presented on a per-line basis to make possible formatting in the future easier.
        return new String[] { "GT-ERR-01 at " + stack.getUnlocalizedName() + "   " + stack.getDisplayName(),
            "A recipe used an OreDict item as output directly, without copying the item before that. This is a typical CallByReference/CallByValue error.",
            "The said item will be renamed to make the invalid recipe visible.",
            "Please check all recipes that output this item, and report them to the mod that introduced the recipes.", };
    }

    @SuppressWarnings("SameParameterValue") // The method is used with one logger, but that might change in the future.
    private static void logMultilineError(Logger logger, String[] errorMessageLines) {
        for (String errorMessage : errorMessageLines) {
            logger.error(errorMessage);
        }
    }
}
