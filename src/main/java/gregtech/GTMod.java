package gregtech;

import static gregtech.GT_Version.VERSION_MAJOR;
import static gregtech.GT_Version.VERSION_MINOR;
import static gregtech.GT_Version.VERSION_PATCH;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.util.GTRecipe.setItemStacks;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import bwcrossmod.galacticgreg.VoidMinerLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
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
import gregtech.api.enums.StoneType;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.IBlockWithClientMeta;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.registries.LHECoolantRegistry;
import gregtech.api.registries.RemovedMetaRegistry;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.AssemblyLineServer;
import gregtech.api.util.GTForestryCompat;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTCapesLoader;
import gregtech.common.GTClient;
import gregtech.common.GTDummyWorld;
import gregtech.common.GTNetwork;
import gregtech.common.GTProxy;
import gregtech.common.RecipeAdder;
import gregtech.common.config.Client;
import gregtech.common.config.Gregtech;
import gregtech.common.config.MachineStats;
import gregtech.common.config.OPStuff;
import gregtech.common.config.Other;
import gregtech.common.config.Worldgen;
import gregtech.common.misc.GTMiscCommand;
import gregtech.common.misc.GTPowerfailCommand;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.misc.spaceprojects.commands.SPCommand;
import gregtech.common.misc.spaceprojects.commands.SPMCommand;
import gregtech.common.misc.spaceprojects.commands.SpaceProjectCommand;
import gregtech.common.ores.UnificationOreAdapter;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.multi.nanochip.util.RecipeHandlers;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import gregtech.crossmod.ae2.AE2Compat;
import gregtech.crossmod.holoinventory.HoloInventory;
import gregtech.crossmod.waila.Waila;
import gregtech.loaders.load.FissionFuelLoader;
import gregtech.loaders.load.FuelLoader;
import gregtech.loaders.load.GTItemIterator;
import gregtech.loaders.load.MTERecipeLoader;
import gregtech.loaders.misc.CoverLoader;
import gregtech.loaders.misc.GTAchievements;
import gregtech.loaders.misc.GTBees;
import gregtech.loaders.postload.BlockResistanceLoader;
import gregtech.loaders.postload.BookAndLootLoader;
import gregtech.loaders.postload.CraftingRecipeLoader;
import gregtech.loaders.postload.CropLoader;
import gregtech.loaders.postload.GTPostLoad;
import gregtech.loaders.postload.GTWorldgenloader;
import gregtech.loaders.postload.ItemMaxStacksizeLoader;
import gregtech.loaders.postload.MachineRecipeLoader;
import gregtech.loaders.postload.MachineTooltipsLoader;
import gregtech.loaders.postload.MissingMappingsHandler;
import gregtech.loaders.postload.PosteaTransformers;
import gregtech.loaders.postload.RecyclerBlacklistLoader;
import gregtech.loaders.postload.ScrapboxDropLoader;
import gregtech.loaders.preload.GTPreLoad;
import gregtech.loaders.preload.LoaderCircuitBehaviors;
import gregtech.loaders.preload.LoaderGTBlockFluid;
import gregtech.loaders.preload.LoaderGTItemData;
import gregtech.loaders.preload.LoaderGTOreDictionary;
import gregtech.loaders.preload.LoaderMetaPipeEntities;
import gregtech.loaders.preload.LoaderMetaTileEntities;
import gregtech.loaders.preload.LoaderOreProcessing;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

@Mod(
    modid = "gregtech",
    name = "GregTech",
    version = "MC1710",
    guiFactory = "gregtech.client.GTGuiFactory",
    dependencies = "required-after:IC2;" + "required-after:structurelib;"
        + "required-after:gtnhlib@[0.6.35,);"
        + "required-after:modularui@[1.1.12,);"
        + "required-after:appliedenergistics2@[rv3-beta-258,);"
        + "after:dreamcraft;"
        + "after:Forestry;"
        + "after:PFAAGeologica;"
        + "after:Thaumcraft;"
        + "after:Railcraft;"
        + "after:ThermalExpansion;"
        + "after:TwilightForest;"
        + "after:harvestcraft;"
        + "after:magicalcrops;"
        + "after:Botania;"
        + "after:BuildCraft|Transport;"
        + "after:BuildCraft|Silicon;"
        + "after:BuildCraft|Factory;"
        + "after:BuildCraft|Energy;"
        + "after:BuildCraft|Core;"
        + "after:BuildCraft|Builders;"
        + "after:GalacticraftCore;"
        + "after:GalacticraftMars;"
        + "after:GalacticraftPlanets;"
        + "after:ThermalExpansion|Transport;"
        + "after:ThermalExpansion|Energy;"
        + "after:ThermalExpansion|Factory;"
        + "after:RedPowerCore;"
        + "after:RedPowerBase;"
        + "after:RedPowerMachine;"
        + "after:RedPowerCompat;"
        + "after:RedPowerWiring;"
        + "after:RedPowerLogic;"
        + "after:RedPowerLighting;"
        + "after:RedPowerWorld;"
        + "after:RedPowerControl;"
        + "after:UndergroundBiomes;"
        + "after:TConstruct;"
        + "after:Translocator;"
        + "after:gendustry;")
public class GTMod {

    static {
        try {
            // Client
            ConfigurationManager.registerConfig(Client.class);

            // GregTech.cfg
            ConfigurationManager.registerConfig(Gregtech.class);

            // MachineStats.cfg
            ConfigurationManager.registerConfig(MachineStats.class);

            // OverPoweredStuff
            ConfigurationManager.registerConfig(OPStuff.class);

            // Other
            ConfigurationManager.registerConfig(Other.class);

            // WorldGeneration
            ConfigurationManager.registerConfig(Worldgen.class);

        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int NBT_VERSION = calculateTotalGTVersion(VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);

    @Mod.Instance("gregtech")
    public static GTMod GT;

    @SidedProxy(modId = "gregtech", clientSide = "gregtech.common.GTClient", serverSide = "gregtech.common.GTProxy")
    public static GTProxy proxy;
    /** Field renamed, reference {@link gregtech.GTMod#proxy} instead */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // should be removed after all mods have updated..
    public static GTProxy gregtechproxy;
    public static final boolean DEBUG = Boolean.getBoolean("gt.debug");

    public static GTAchievements achievements;
    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");

    public GTMod() {
        GTValues.DW = new GTDummyWorld();
        GTValues.NW = new GTNetwork();
        GTValues.RA = new RecipeAdder();

        // TEs that can be wrenched.
        for (int i = 0; i < 4; i++) {
            GregTechAPI.registerTileEntityConstructor(i, i2 -> GregTechAPI.constructBaseMetaTileEntity());
        }

        // TEs that can be mined.
        for (int i = 12; i < 16; i++) {
            GregTechAPI.registerTileEntityConstructor(i, i2 -> GregTechAPI.constructBaseMetaTileEntity());
        }

        for (int i = 4; i < 12; i++) {
            GregTechAPI.registerTileEntityConstructor(i, i2 -> new BaseMetaPipeEntity());
        }

        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.BlockIcons.VOID.name();
        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.ItemIcons.VOID.name();

        UnificationOreAdapter.load();
    }

    public static GTClient clientProxy() {
        if (proxy.isClientSide()) {
            return (GTClient) proxy;
        } else {
            throw new RuntimeException("Client Proxy accessed from the dedicated server!");
        }
    }

    public boolean isClientSide() {
        return proxy.isClientSide();
    }

    public EntityPlayer getThePlayer() {
        return proxy.getThePlayer();
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion) {
        return calculateTotalGTVersion(majorVersion, minorVersion, 0);
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion, int patchVersion) {
        return majorVersion * 1000000 + minorVersion * 1000 + patchVersion;
    }

    @Mod.EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event) {
        Locale.setDefault(Locale.ENGLISH);
        if (GregTechAPI.sPreloadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTechAPI.sBeforeGTPreload) {
            tRunnable.run();
        }

        GTPreLoad.getConfiguration(event.getModConfigurationDirectory());
        GTPreLoad.createLogFiles(
            event.getModConfigurationDirectory()
                .getParentFile());

        PowerGogglesConfigHandler.init(new File(event.getModConfigurationDirectory() + "/GregTech/Goggles.cfg"));

        proxy.onPreInitialization(event);

        GTLog.out.println("GTMod: Setting Configs");

        GTPreLoad.loadConfig();

        new Thread(new GTCapesLoader(), "GT Cape Loader").start();

        // ModularUI
        GTGuis.registerFactories();
        GTGuiTextures.init();
        GTGuiTheme.registerThemes();

        // Load enchantments
        new EnchantmentHazmat();
        new EnchantmentEnderDamage();
        new EnchantmentRadioactivity();

        Materials.init();

        GTPreLoad.initLocalization(
            event.getModConfigurationDirectory()
                .getParentFile());
        GTPreLoad.adjustScrap();

        AE2Compat.onPreInit();

        GTPreLoad.runMineTweakerCompat();

        new LoaderOreProcessing().run();
        new LoaderGTOreDictionary().run();
        new LoaderGTItemData().run();
        new LoaderGTBlockFluid().run();
        new LoaderMetaTileEntities().run();
        new LoaderMetaPipeEntities().run();

        new LoaderCircuitBehaviors().run();

        // populate itemstack instance for NBT check in GTRecipe
        setItemStacks();

        GTPreLoad.sortToTheEnd();
        GregTechAPI.sPreloadFinished = true;
        GTLog.out.println("GTMod: Preload-Phase finished!");
        GTLog.ore.println("GTMod: Preload-Phase finished!");

        GTUIInfos.init();

        IBlockWithClientMeta.register();

        for (Runnable tRunnable : GregTechAPI.sAfterGTPreload) {
            tRunnable.run();
        }

        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) AssemblyLineServer.fillMap(event);
    }

    @Mod.EventHandler
    public void onInitialization(FMLInitializationEvent event) {
        if (GregTechAPI.sLoadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTechAPI.sBeforeGTLoad) {
            tRunnable.run();
        }

        if (Forestry.isModLoaded()) {
            // TODO: Refactor GTBees with proper state handling
            // noinspection InstantiationOfUtilityClass
            new GTBees();
        }

        // Disable Low Grav regardless of config if Cleanroom is disabled.
        if (!proxy.mEnableCleanroom) {
            proxy.mLowGravProcessing = false;
        }

        proxy.onInitialization(event);
        new MTERecipeLoader().run();

        new GTItemIterator().run();
        proxy.registerUnificationEntries();
        new FuelLoader().run();
        new FissionFuelLoader().run();

        if (Mods.Waila.isModLoaded()) {
            Waila.init();
        }
        if (Mods.HoloInventory.isModLoaded()) {
            HoloInventory.init();
        }

        GTStructureChannels.register();

        LHECoolantRegistry.registerBaseCoolants();

        GT_FML_LOGGER.debug("Registering SpaceDimensions");
        SpaceDimRegisterer.register();
        // This needs to run BEFORE creating any circuit assembler recipes, since the downstream
        // recipe map for the assembly matrix relies on doing recipe lookups here.
        // I really hope I can put this here without breaking something
        RecipeHandlers.populateCircuitComponentRecipeMaps();

        GregTechAPI.sLoadFinished = true;
        GTLog.out.println("GTMod: Load-Phase finished!");
        GTLog.ore.println("GTMod: Load-Phase finished!");

        for (Runnable tRunnable : GregTechAPI.sAfterGTLoad) {
            tRunnable.run();
        }
    }

    @Mod.EventHandler
    public void onPostInitialization(FMLPostInitializationEvent event) {
        if (GregTechAPI.sPostloadStarted) {
            return;
        }

        // Seems only used by GGFab so far
        for (Runnable tRunnable : GregTechAPI.sBeforeGTPostload) {
            tRunnable.run();
        }

        proxy.onPostInitialization(event);

        if (DEBUG) {
            // Prints all the used MTE id and their associated TE name, turned on with -Dgt.debug=true in jvm args
            final int bound = GregTechAPI.METATILEENTITIES.length;
            for (int i1 = 1; i1 < bound; i1++) {
                if (GregTechAPI.METATILEENTITIES[i1] != null) {
                    GTLog.out.printf("META %d %s\n", i1, GregTechAPI.METATILEENTITIES[i1].getMetaName());
                }
            }
        }

        proxy.registerUnificationEntries();

        new BookAndLootLoader().run();
        new ItemMaxStacksizeLoader().run();
        new BlockResistanceLoader().run();
        new RecyclerBlacklistLoader().run();
        new MachineRecipeLoader().run();
        new ScrapboxDropLoader().run();
        new CropLoader().run();
        new GTWorldgenloader().run();
        new CoverLoader().run();
        StoneType.init();

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

        AE2Compat.onPostInit();

        Arrays
            .stream(
                new String[] { "blastfurnace", "blockcutter", "inductionFurnace", "generator", "windMill", "waterMill",
                    "solarPanel", "centrifuge", "electrolyzer", "compressor", "electroFurnace", "extractor",
                    "macerator", "recycler", "metalformer", "orewashingplant", "massFabricator", "replicator", })
            .map(tName -> GTModHandler.getIC2Item(tName, 1L))
            .forEach(GTModHandler::removeRecipeByOutputDelayed);

        GTPostLoad.changeWoodenVanillaTools();

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
            tRunnable.run();
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
        GTPostLoad.addCauldronRecipe();
        GTPostLoad.identifyAnySteam();

        VoidMinerLoader.init();

        achievements = new GTAchievements();

        GTRecipe.GTppRecipeHelper = true;
        GTLog.out.println("GTMod: Loading finished, de-allocating temporary Init Variables.");
        GregTechAPI.sBeforeGTPreload = null;
        GregTechAPI.sAfterGTPreload = null;
        GregTechAPI.sBeforeGTLoad = null;
        GregTechAPI.sAfterGTLoad = null;
        GregTechAPI.sBeforeGTPostload = null;
        GregTechAPI.sAfterGTPostload = null;
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.onLoadComplete(event);
        for (Runnable tRunnable : GregTechAPI.sGTCompleteLoad) {
            tRunnable.run();
        }
        GregTechAPI.sGTCompleteLoad = null;
        GregTechAPI.sFullLoadFinished = true;
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        proxy.onServerAboutToStart(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

        for (Runnable tRunnable : GregTechAPI.sBeforeGTServerstart) {
            tRunnable.run();
        }

        proxy.onServerStarting(event);
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

        if (proxy.mCraftingUnification) {
            GTLog.out.println("GTMod: Crafting Recipes");
            for (IRecipe tRecipe : CraftingManager.getInstance()
                .getRecipeList()) {
                if ((tRecipe instanceof IRecipe)) {
                    tStacks.add(tRecipe.getRecipeOutput());
                }
            }
        }
        for (ItemStack tOutput : tStacks) {
            if (!proxy.mRegisteredOres.contains(tOutput)) {
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
            tRunnable.run();
        }

        event.registerServerCommand(new GTMiscCommand());
        event.registerServerCommand(new SPCommand());
        event.registerServerCommand(new SPMCommand());
        event.registerServerCommand(new SpaceProjectCommand());
        event.registerServerCommand(new GTPowerfailCommand());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        proxy.onServerStarted(event);
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        for (Runnable tRunnable : GregTechAPI.sBeforeGTServerstop) {
            tRunnable.run();
        }
        proxy.onServerStopping(event);
        for (Runnable tRunnable : GregTechAPI.sAfterGTServerstop) {
            tRunnable.run();
        }
        // Interrupt IDLE Threads to close down cleanly
        RunnableMachineUpdate.shutdownExecutorService();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        proxy.onServerStopped(event);
    }

    @Mod.EventHandler
    public void onIDChangingEvent(FMLModIdMappingEvent event) {
        if (event.remappedIds.isEmpty()) return;

        GTUtility.reInit();
        GTRecipe.reInit();
        RemovedMetaRegistry.init();
    }

    @Mod.EventHandler
    public void onMissingMappings(FMLMissingMappingsEvent event) {
        MissingMappingsHandler.handleMappings(event.getAll());
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
