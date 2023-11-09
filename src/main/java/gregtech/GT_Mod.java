package gregtech;

import static gregtech.GT_Version.*;
import static gregtech.api.GregTech_API.registerCircuitProgrammer;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsBlocks;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsFluids;
import static gregtech.api.ModernMaterials.PartRecipeGenerators.Utility.registerAllMaterialsRecipes;
import static gregtech.api.enums.Mods.Forestry;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;

import appeng.api.AEApi;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.ModernMaterialsRegistration;
import gregtech.api.ModernMaterials.ModernMaterialsTextureRegister;
import gregtech.api.enchants.Enchantment_EnderDamage;
import gregtech.api.enchants.Enchantment_Hazmat;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.*;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.internal.IGT_Mod;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.threads.GT_Runnable_MachineBlockUpdate;
import gregtech.api.util.*;
import gregtech.common.GT_DummyWorld;
import gregtech.common.GT_Network;
import gregtech.common.GT_Proxy;
import gregtech.common.GT_RecipeAdder;
import gregtech.common.covers.GT_Cover_FacadeAE;
import gregtech.common.entities.GT_Entity_Arrow;
import gregtech.common.entities.GT_Entity_Arrow_Potion;
import gregtech.common.misc.GT_Command;
import gregtech.common.misc.spaceprojects.commands.SPM_Command;
import gregtech.common.misc.spaceprojects.commands.SP_Command;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import gregtech.crossmod.holoinventory.HoloInventory;
import gregtech.crossmod.waila.Waila;
import gregtech.loaders.ExtraIcons;
import gregtech.loaders.load.*;
import gregtech.loaders.misc.GT_Achievements;
import gregtech.loaders.misc.GT_Bees;
import gregtech.loaders.misc.GT_CoverLoader;
import gregtech.loaders.postload.*;
import gregtech.loaders.preload.*;
import gregtech.nei.IMCForNEI;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

@Mod(
    modid = Mods.Names.GREG_TECH,
    name = "GregTech",
    version = "MC1710",
    guiFactory = "gregtech.client.GT_GuiFactory",
    dependencies = " required-after:IC2;" + " required-after:structurelib;"
        + " required-after:gtnhlib@[0.0.8,);"
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
public class GT_Mod implements IGT_Mod {

    @Deprecated // Keep for use in BaseMetaTileEntity
    public static final int VERSION = VERSION_MAJOR, SUBVERSION = VERSION_MINOR;

    @Deprecated
    public static final int TOTAL_VERSION = calculateTotalGTVersion(VERSION, SUBVERSION);
    public static final int NBT_VERSION = calculateTotalGTVersion(VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);

    @Deprecated
    public static final int REQUIRED_IC2 = 624;

    @Mod.Instance(Mods.Names.GREG_TECH)
    public static GT_Mod instance;

    @SidedProxy(
        modId = Mods.Names.GREG_TECH,
        clientSide = "gregtech.common.GT_Client",
        serverSide = "gregtech.common.GT_Server")
    public static GT_Proxy gregtechproxy;

    public static int MAX_IC2 = 2147483647;
    public static GT_Achievements achievements;
    public static final String aTextGeneral = "general";
    public static final String aTextIC2 = "ic2_";
    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");

    @SuppressWarnings("deprecation")
    public GT_Mod() {
        GT_Values.GT = this;
        GT_Values.DW = new GT_DummyWorld();
        GT_Values.NW = new GT_Network();
        GT_Values.RA = new GT_RecipeAdder();

        for (int i = 0; i < 4; i++) {
            GregTech_API.registerTileEntityConstructor(i, i2 -> GregTech_API.constructBaseMetaTileEntity());
        }
        for (int i = 4; i < 12; i++) {
            GregTech_API.registerTileEntityConstructor(i, i2 -> new BaseMetaPipeEntity());
        }

        // noinspection deprecation// Need run-time initialization
        GregTech_API.sRecipeAdder = GT_Values.RA;

        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.BlockIcons.VOID.name();
        // noinspection ResultOfMethodCallIgnored// Suspicious likely pointless
        Textures.ItemIcons.VOID.name();
    }

    @SuppressWarnings("unused") // TODO: Delete this method
    public static int calculateTotalGTVersion(int minorVersion) {
        return calculateTotalGTVersion(VERSION, minorVersion);
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion) {
        return calculateTotalGTVersion(majorVersion, minorVersion, 0);
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion, int patchVersion) {
        return majorVersion * 1000000 + minorVersion * 1000 + patchVersion;
    }

    @Mod.EventHandler
    public void onPreLoad(FMLPreInitializationEvent aEvent) {

        new ModernMaterialsRegistration().run(aEvent);

        Locale.setDefault(Locale.ENGLISH);
        if (GregTech_API.sPreloadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTech_API.sBeforeGTPreload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        Configuration tMainConfig = GT_PreLoad.getConfiguration(aEvent.getModConfigurationDirectory());
        GT_PreLoad.initCompat();
        GT_PreLoad.createLogFiles(
            aEvent.getModConfigurationDirectory()
                .getParentFile(),
            tMainConfig);

        gregtechproxy.onPreLoad();

        GT_Log.out.println("GT_Mod: Setting Configs");

        GT_PreLoad.loadConfig(tMainConfig);

        new Enchantment_Hazmat();
        new Enchantment_EnderDamage();
        new Enchantment_Radioactivity();

        Materials.init();

        GT_Log.out.println("GT_Mod: Saving Main Config");
        tMainConfig.save();

        GT_PreLoad.initLocalization(
            aEvent.getModConfigurationDirectory()
                .getParentFile());
        GT_PreLoad.adjustScrap();

        EntityRegistry.registerModEntity(GT_Entity_Arrow.class, "GT_Entity_Arrow", 1, GT_Values.GT, 160, 1, true);
        EntityRegistry
            .registerModEntity(GT_Entity_Arrow_Potion.class, "GT_Entity_Arrow_Potion", 2, GT_Values.GT, 160, 1, true);
        AEApi.instance()
            .registries()
            .interfaceTerminal()
            .register(GT_MetaTileEntity_Hatch_CraftingInput_ME.class);

        GT_PreLoad.runMineTweakerCompat();

        new GT_Loader_OreProcessing().run();
        new GT_Loader_OreDictionary().run();
        new GT_Loader_ItemData().run();
        new GT_Loader_Item_Block_And_Fluid().run();
        new GT_Loader_MetaTileEntities().run();
        new GT_Loader_MultiTileEntities().run();

        new GT_Loader_CircuitBehaviors().run();
        new GT_CoverBehaviorLoader().run();
        new GT_SonictronLoader().run();
        new GT_SpawnEventHandler();

        GT_PreLoad.sortToTheEnd();
        GregTech_API.sPreloadFinished = true;
        GT_Log.out.println("GT_Mod: Preload-Phase finished!");
        GT_Log.ore.println("GT_Mod: Preload-Phase finished!");

        GT_UIInfos.init();

        for (Runnable tRunnable : GregTech_API.sAfterGTPreload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) GT_Assemblyline_Server.fillMap(aEvent);

    }

    @Mod.EventHandler
    public void onLoad(FMLInitializationEvent aEvent) {

        MinecraftForge.EVENT_BUS.register(new ExtraIcons());
        MinecraftForge.EVENT_BUS.register(new ModernMaterialsTextureRegister());

        if (GregTech_API.sLoadStarted) {
            return;
        }

        // --- Modern Materials fluid registration ---
        registerAllMaterialsFluids();
        registerAllMaterialsBlocks();
        // -------------------------------------------

        for (Runnable tRunnable : GregTech_API.sBeforeGTLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        if (Forestry.isModLoaded())
            // noinspection InstantiationOfUtilityClass//TODO: Refactor GT_Bees with proper state handling
            new GT_Bees();

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

        new GT_Loader_MetaTileEntities_Recipes().run();

        if (gregtechproxy.mSortToTheEnd) {
            new GT_ItemIterator().run();
            gregtechproxy.registerUnificationEntries();
            new GT_FuelLoader().run();
        }
        if (Mods.Waila.isModLoaded()) {
            Waila.init();
        }
        if (Mods.HoloInventory.isModLoaded()) {
            HoloInventory.init();
        }
        IMCForNEI.IMCSender();
        GregTech_API.sLoadFinished = true;
        GT_Log.out.println("GT_Mod: Load-Phase finished!");
        GT_Log.ore.println("GT_Mod: Load-Phase finished!");

        for (Runnable tRunnable : GregTech_API.sAfterGTLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }
    }

    @Mod.EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent) {
        if (GregTech_API.sPostloadStarted) {
            return;
        }

        registerAllMaterialsRecipes();

        for (Runnable tRunnable : GregTech_API.sBeforeGTPostload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        gregtechproxy.onPostLoad();

        final int bound = GregTech_API.METATILEENTITIES.length;
        for (int i1 = 1; i1 < bound; i1++) {
            if (GregTech_API.METATILEENTITIES[i1] != null) {
                GT_Log.out.printf("META %d %s\n", i1, GregTech_API.METATILEENTITIES[i1].getMetaName());
            }
        }

        if (gregtechproxy.mSortToTheEnd) {
            gregtechproxy.registerUnificationEntries();
        } else {
            new GT_ItemIterator().run();
            gregtechproxy.registerUnificationEntries();
            new GT_FuelLoader().run();
        }
        new GT_BookAndLootLoader().run();
        new GT_ItemMaxStacksizeLoader().run();
        new GT_BlockResistanceLoader().run();
        new GT_RecyclerBlacklistLoader().run();
        new GT_MinableRegistrator().run();
        new GT_FakeRecipeLoader().run();
        new GT_MachineRecipeLoader().run();
        new GT_ScrapboxDropLoader().run();
        new GT_CropLoader().run();
        new GT_Worldgenloader().run();
        new GT_CoverLoader().run();

        GT_RecipeRegistrator.registerUsagesForMaterials(
            null,
            false,
            new ItemStack(Blocks.planks, 1),
            new ItemStack(Blocks.cobblestone, 1),
            new ItemStack(Blocks.stone, 1),
            new ItemStack(Items.leather, 1));

        GT_OreDictUnificator.addItemData(
            GT_ModHandler.getRecipeOutput(
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                null,
                null,
                null),
            new ItemData(Materials.Tin, 10886400L));
        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.storageblockcrafting, "tile.glowstone", false)) {
            GT_ModHandler.removeRecipe(
                new ItemStack(Items.glowstone_dust, 1),
                new ItemStack(Items.glowstone_dust, 1),
                null,
                new ItemStack(Items.glowstone_dust, 1),
                new ItemStack(Items.glowstone_dust, 1));
        }
        GT_ModHandler.removeRecipeDelayed(
            new ItemStack(Blocks.wooden_slab, 1, 0),
            new ItemStack(Blocks.wooden_slab, 1, 1),
            new ItemStack(Blocks.wooden_slab, 1, 2));
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.wooden_slab, 6, 0),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "WWW", 'W', new ItemStack(Blocks.planks, 1, 0) });

        // Save a copy of these list before activateOreDictHandler(), then loop over them.
        Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList = GT_ModHandler.getMaceratorRecipeList();
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList = GT_ModHandler.getCompressorRecipeList();
        Map<IRecipeInput, RecipeOutput> aExtractorRecipeList = GT_ModHandler.getExtractorRecipeList();
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList = GT_ModHandler.getOreWashingRecipeList();
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList = GT_ModHandler.getThermalCentrifugeRecipeList();

        GT_Log.out.println(
            "GT_Mod: Activating OreDictionary Handler, this can take some time, as it scans the whole OreDictionary");
        GT_FML_LOGGER.info(
            "If your Log stops here, you were too impatient. Wait a bit more next time, before killing Minecraft with the Task Manager.");

        GT_PostLoad.activateOreDictHandler();
        GT_PostLoad.replaceVanillaMaterials();
        GT_PostLoad.removeIc2Recipes(
            aMaceratorRecipeList,
            aCompressorRecipeList,
            aExtractorRecipeList,
            aOreWashingRecipeList,
            aThermalCentrifugeRecipeList);

        if (GT_Values.D1) {
            GT_ModHandler.sSingleNonBlockDamagableRecipeList.forEach(
                iRecipe -> GT_Log.out.println(
                    "=> " + iRecipe.getRecipeOutput()
                        .getDisplayName()));
        }
        new GT_CraftingRecipeLoader().run();
        if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "ic2forgehammer", true)) {
            GT_ModHandler.removeRecipeByOutput(ItemList.IC2_ForgeHammer.getWildcard(1L));
        }
        GT_ModHandler.removeRecipeByOutput(GT_ModHandler.getIC2Item("machine", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("machine", 1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "RwR", "RRR", 'R', OrePrefixes.plate.get(Materials.Iron) });

        GT_PostLoad.registerFluidCannerRecipes();

        if (Forestry.isModLoaded()) {
            GT_Forestry_Compat.transferCentrifugeRecipes();
            GT_Forestry_Compat.transferSqueezerRecipes();
        }
        GT_MetaTileEntity_DigitalChestBase.registerAEIntegration();
        ItemStack facade = AEApi.instance()
            .definitions()
            .items()
            .facade()
            .maybeItem()
            .transform(i -> new ItemStack(i, 1, GT_Values.W))
            .orNull();
        if (facade != null) {
            GregTech_API.registerCover(facade, null, new GT_Cover_FacadeAE());
        }

        Arrays
            .stream(
                new String[] { "blastfurnace", "blockcutter", "inductionFurnace", "generator", "windMill", "waterMill",
                    "solarPanel", "centrifuge", "electrolyzer", "compressor", "electroFurnace", "extractor",
                    "macerator", "recycler", "metalformer", "orewashingplant", "massFabricator", "replicator", })
            .filter(
                tName -> GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, aTextIC2 + tName, true))
            .map(tName -> GT_ModHandler.getIC2Item(tName, 1L))
            .forEach(GT_ModHandler::removeRecipeByOutputDelayed);

        GT_PostLoad.nerfVanillaTools();
        new GT_ExtremeDieselFuelLoader().run();

        /*
         * Until this point most crafting recipe additions, and removals, have been buffered. Go through, execute the
         * removals in bulk, and then any deferred additions. The bulk removals in particular significantly speed up the
         * recipe list modifications.
         */

        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GT_Log.out.println("GT_Mod: Adding buffered Recipes.");
        GT_ModHandler.stopBufferingCraftingRecipes();
        // noinspection UnstableApiUsage// Stable enough for this project
        GT_FML_LOGGER.info("Executed delayed Crafting Recipes (" + stopwatch.stop() + "). Have a Cake.");

        GT_Log.out.println("GT_Mod: Saving Lang File.");
        new GT_MachineTooltipsLoader().run();
        GT_LanguageManager.sEnglishFile.save();
        GregTech_API.sPostloadFinished = true;
        GT_Log.out.println("GT_Mod: PostLoad-Phase finished!");
        GT_Log.ore.println("GT_Mod: PostLoad-Phase finished!");
        for (Runnable tRunnable : GregTech_API.sAfterGTPostload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }
        GT_PostLoad.addFakeRecipes();

        if (GregTech_API.mOutputRF || GregTech_API.mInputRF) {
            GT_Utility.checkAvailabilities();
            if (!GT_Utility.RF_CHECK) {
                GregTech_API.mOutputRF = false;
                GregTech_API.mInputRF = false;
            }
        }

        GT_PostLoad.addSolidFakeLargeBoilerFuels();
        GT_PostLoad.identifyAnySteam();

        achievements = new GT_Achievements();

        GT_Recipe.GTppRecipeHelper = true;
        GT_Log.out.println("GT_Mod: Loading finished, de-allocating temporary Init Variables.");
        GregTech_API.sBeforeGTPreload = null;
        GregTech_API.sAfterGTPreload = null;
        GregTech_API.sBeforeGTLoad = null;
        GregTech_API.sAfterGTLoad = null;
        GregTech_API.sBeforeGTPostload = null;
        GregTech_API.sAfterGTPostload = null;

        GT_PostLoad.createGTtoolsCreativeTab();
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent aEvent) {
        for (Runnable tRunnable : GregTech_API.sGTCompleteLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }
        GregTech_API.sGTCompleteLoad = null;
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

        for (Runnable tRunnable : GregTech_API.sBeforeGTServerstart) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        gregtechproxy.onServerStarting();
        // Check for more IC2 recipes on ServerStart to also catch MineTweaker additions
        GT_ModHandler.addIC2RecipesToGT(
            GT_ModHandler.getMaceratorRecipeList(),
            GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
            true,
            true,
            true);
        GT_ModHandler.addIC2RecipesToGT(
            GT_ModHandler.getCompressorRecipeList(),
            GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
            true,
            true,
            true);
        GT_ModHandler.addIC2RecipesToGT(
            GT_ModHandler.getExtractorRecipeList(),
            GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
            true,
            true,
            true);
        GT_ModHandler.addIC2RecipesToGT(
            GT_ModHandler.getOreWashingRecipeList(),
            GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
            false,
            true,
            true);
        GT_ModHandler.addIC2RecipesToGT(
            GT_ModHandler.getThermalCentrifugeRecipeList(),
            GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
            true,
            true,
            true);
        GT_Log.out.println("GT_Mod: Unificating outputs of all known Recipe Types.");
        ArrayList<ItemStack> tStacks = new ArrayList<>(10000);
        GT_Log.out.println("GT_Mod: IC2 Machines");

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

        GT_Log.out.println("GT_Mod: Dungeon Loot");
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
        GT_Log.out.println("GT_Mod: Smelting");

        // Deal with legacy Minecraft raw types
        tStacks.addAll(
            FurnaceRecipes.smelting()
                .getSmeltingList()
                .values());

        if (gregtechproxy.mCraftingUnification) {
            GT_Log.out.println("GT_Mod: Crafting Recipes");
            for (IRecipe tRecipe : CraftingManager.getInstance()
                .getRecipeList()) {
                if ((tRecipe instanceof IRecipe)) {
                    tStacks.add(tRecipe.getRecipeOutput());
                }
            }
        }
        for (ItemStack tOutput : tStacks) {
            if (!gregtechproxy.mRegisteredOres.contains(tOutput)) {
                GT_OreDictUnificator.setStack(tOutput);
            } else {
                logMultilineError(GT_FML_LOGGER, generateGTErr01Message(tOutput));
                tOutput.setStackDisplayName("ERROR! PLEASE CHECK YOUR LOG FOR 'GT-ERR-01'!");
            }
        }
        GregTech_API.mServerStarted = true;
        GT_Log.out.println("GT_Mod: ServerStarting-Phase finished!");
        GT_Log.ore.println("GT_Mod: ServerStarting-Phase finished!");

        for (Runnable tRunnable : GregTech_API.sAfterGTServerstart) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        aEvent.registerServerCommand(new GT_Command());
        aEvent.registerServerCommand(new SP_Command());
        aEvent.registerServerCommand(new SPM_Command());
        // Sets a new Machine Block Update Thread everytime a world is loaded
        GT_Runnable_MachineBlockUpdate.initExecutorService();
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
        GT_Utility.reInit();
        GT_Recipe.reInit();
        try {
            for (Map<? extends GT_ItemStack, ?> gt_itemStackMap : GregTech_API.sItemStackMappings) {
                GT_Utility.reMap(gt_itemStackMap);
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent aEvent) {
        for (Runnable tRunnable : GregTech_API.sBeforeGTServerstop) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        gregtechproxy.onServerStopping();

        for (Runnable tRunnable : GregTech_API.sAfterGTServerstop) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }
        // Interrupt IDLE Threads to close down cleanly
        GT_Runnable_MachineBlockUpdate.shutdownExecutorService();
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
