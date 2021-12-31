package gregtech;

import com.google.common.base.Stopwatch;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.GregTech_API;
import gregtech.api.enchants.Enchantment_EnderDamage;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.*;
import gregtech.api.interfaces.internal.IGT_Mod;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.ReverseShapedRecipe;
import gregtech.api.objects.ReverseShapelessRecipe;
import gregtech.api.objects.XSTR;
import gregtech.api.threads.GT_Runnable_MachineBlockUpdate;
import gregtech.api.util.*;
import gregtech.common.GT_DummyWorld;
import gregtech.common.GT_Network;
import gregtech.common.GT_Proxy;
import gregtech.common.GT_RecipeAdder;
import gregtech.common.entities.GT_Entity_Arrow;
import gregtech.common.entities.GT_Entity_Arrow_Potion;
import gregtech.common.misc.GT_Command;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import gregtech.loaders.ExtraIcons;
import gregtech.loaders.load.GT_CoverBehaviorLoader;
import gregtech.loaders.load.GT_FuelLoader;
import gregtech.loaders.load.GT_ItemIterator;
import gregtech.loaders.load.GT_SonictronLoader;
import gregtech.loaders.misc.GT_Achievements;
import gregtech.loaders.misc.GT_Bees;
import gregtech.loaders.misc.GT_CoverLoader;
import gregtech.loaders.postload.*;
import gregtech.loaders.preload.*;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

import static gregtech.api.enums.GT_Values.MOD_ID_FR;

@SuppressWarnings("ALL")
@Mod(modid = "gregtech", name = "GregTech", version = "MC1710", useMetadata = false,
        dependencies = " required-after:IC2;" +
                " required-after:structurelib;" +
                " after:dreamcraft;" +
                " after:Forestry;" +
                " after:PFAAGeologica;" +
                " after:Thaumcraft;" +
                " after:Railcraft;" +
                " after:appliedenergistics2;" +
                " after:ThermalExpansion;" +
                " after:TwilightForest;" +
                " after:harvestcraft;" +
                " after:magicalcrops;" +
                " after:BuildCraft|Transport;" +
                " after:BuildCraft|Silicon;" +
                " after:BuildCraft|Factory;" +
                " after:BuildCraft|Energy;" +
                " after:BuildCraft|Core;" +
                " after:BuildCraft|Builders;" +
                " after:GalacticraftCore;" +
                " after:GalacticraftMars;" +
                " after:GalacticraftPlanets;" +
                " after:ThermalExpansion|Transport;" +
                " after:ThermalExpansion|Energy;" +
                " after:ThermalExpansion|Factory;" +
                " after:RedPowerCore;" +
                " after:RedPowerBase;" +
                " after:RedPowerMachine;" +
                " after:RedPowerCompat;" +
                " after:RedPowerWiring;" +
                " after:RedPowerLogic;" +
                " after:RedPowerLighting;" +
                " after:RedPowerWorld;" +
                " after:RedPowerControl;" +
                " after:UndergroundBiomes;" +
                " after:TConstruct;" +
                " after:Translocator;")
public class GT_Mod implements IGT_Mod {

    @Deprecated // Keep for use in BaseMetaTileEntity
    public static final int VERSION = 509, SUBVERSION = 40;
    @Deprecated
    public static final int TOTAL_VERSION = calculateTotalGTVersion(VERSION, SUBVERSION);
    public static final int REQUIRED_IC2 = 624;
    @Mod.Instance("gregtech")
    public static GT_Mod instance;
    @SidedProxy(modId = "gregtech", clientSide = "gregtech.common.GT_Client", serverSide = "gregtech.common.GT_Server")
    public static GT_Proxy gregtechproxy;
    public static int MAX_IC2 = 2147483647;
    public static GT_Achievements achievements;
    public static final String aTextGeneral = "general";
    public static final String aTextIC2 = "ic2_";
    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");


    static {
        if ((509 != GregTech_API.VERSION) || (509 != GT_ModHandler.VERSION) || (509 != GT_OreDictUnificator.VERSION) || (509 != GT_Recipe.VERSION) || (509 != GT_Utility.VERSION) || (509 != GT_RecipeRegistrator.VERSION) || (509 != Element.VERSION) || (509 != Materials.VERSION) || (509 != OrePrefixes.VERSION)) {
            throw new GT_ItsNotMyFaultException("One of your Mods included GregTech-API Files inside it's download, mention this to the Mod Author, who does this bad thing, and tell him/her to use reflection. I have added a Version check, to prevent Authors from breaking my Mod that way.");
        }
    }

    public GT_Mod() {
        try {
            Class.forName("ic2.core.IC2").getField("enableOreDictCircuit").set(null, Boolean.FALSE);
        } catch (Throwable ignored) {}
        try {
            Class.forName("ic2.core.IC2").getField("enableCraftingBucket").set(null, Boolean.FALSE);
        } catch (Throwable ignored) {}
        try {
            Class.forName("ic2.core.IC2").getField("enableEnergyInStorageBlockItems").set(null, Boolean.FALSE);
        } catch (Throwable ignored) {}
        GT_Values.GT = this;
        GT_Values.DW = new GT_DummyWorld();
        GT_Values.NW = new GT_Network();
        GregTech_API.sRecipeAdder = GT_Values.RA = new GT_RecipeAdder();

        Textures.BlockIcons.VOID.name();
        Textures.ItemIcons.VOID.name();
    }

    public static int calculateTotalGTVersion(int minorVersion) {
        return calculateTotalGTVersion(VERSION, minorVersion);
    }

    public static int calculateTotalGTVersion(int majorVersion, int minorVersion) {
        return majorVersion * 1000 + minorVersion;
    }

    @Mod.EventHandler
    public void onPreLoad(FMLPreInitializationEvent aEvent) {
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

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new ExtraIcons());
        }

        Configuration tMainConfig = GT_PreLoad.getConfiguration(aEvent.getModConfigurationDirectory());
        GT_PreLoad.initCompat();
        GT_PreLoad.createLogFiles(aEvent.getModConfigurationDirectory().getParentFile(), tMainConfig);

        gregtechproxy.onPreLoad();

        GT_Log.out.println("GT_Mod: Setting Configs");

        GT_PreLoad.loadConfig(tMainConfig);

        new Enchantment_EnderDamage();
        new Enchantment_Radioactivity();

        Materials.init();

        GT_Log.out.println("GT_Mod: Saving Main Config");
        tMainConfig.save();

        GT_PreLoad.initLocalization(aEvent.getModConfigurationDirectory().getParentFile());
        GT_PreLoad.adjustScrap();

        EntityRegistry.registerModEntity(GT_Entity_Arrow.class, "GT_Entity_Arrow", 1, GT_Values.GT, 160, 1, true);
        EntityRegistry.registerModEntity(GT_Entity_Arrow_Potion.class, "GT_Entity_Arrow_Potion", 2, GT_Values.GT, 160, 1, true);

        GT_PreLoad.runMineTweakerCompat();

        new GT_Loader_OreProcessing().run();
        new GT_Loader_OreDictionary().run();
        new GT_Loader_ItemData().run();
        new GT_Loader_Item_Block_And_Fluid().run();
        new GT_Loader_MetaTileEntities().run();

        new GT_Loader_CircuitBehaviors().run();
        new GT_CoverBehaviorLoader().run();
        new GT_SonictronLoader().run();
        new GT_SpawnEventHandler();

        GT_PreLoad.sortToTheEnd();
        GregTech_API.sPreloadFinished = true;
        GT_Log.out.println("GT_Mod: Preload-Phase finished!");
        GT_Log.ore.println("GT_Mod: Preload-Phase finished!");

        for (Runnable tRunnable : GregTech_API.sAfterGTPreload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            GT_Assemblyline_Server.fillMap(aEvent);
    }

    @Mod.EventHandler
    public void onLoad(FMLInitializationEvent aEvent) {
        if (GregTech_API.sLoadStarted) {
            return;
        }

        for (Runnable tRunnable : GregTech_API.sBeforeGTLoad) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        if (Loader.isModLoaded(MOD_ID_FR))
            new GT_Bees();

        // Disable Low Grav regardless of config if Cleanroom is disabled.
        if (!gregtechproxy.mEnableCleanroom) {
            gregtechproxy.mLowGravProcessing = false;
        }

        gregtechproxy.onLoad();
        if (gregtechproxy.mSortToTheEnd) {
            new GT_ItemIterator().run();
            gregtechproxy.registerUnificationEntries();
            new GT_FuelLoader().run();
        }
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

        for (Runnable tRunnable : GregTech_API.sBeforeGTPostload) {
            try {
                tRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
        }

        gregtechproxy.onPostLoad();


        for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
            if (i >= GregTech_API.METATILEENTITIES.length)
                break;
            if (GregTech_API.METATILEENTITIES[i] != null) {
                GT_Log.out.println("META " + i + " " + GregTech_API.METATILEENTITIES[i].getMetaName());
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
        new GT_MachineRecipeLoader().run();
        new GT_ScrapboxDropLoader().run();
        new GT_CropLoader().run();
        new GT_Worldgenloader().run();
        new GT_CoverLoader().run();

        GT_RecipeRegistrator.registerUsagesForMaterials(null, false, new ItemStack(Blocks.planks, 1), new ItemStack(Blocks.cobblestone, 1), new ItemStack(Blocks.stone, 1), new ItemStack(Items.leather, 1));

        GT_OreDictUnificator.addItemData(GT_ModHandler.getRecipeOutput(null, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L), null, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L), null, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L), null, null, null), new ItemData(Materials.Tin, 10886400L));
        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.storageblockcrafting, "tile.glowstone", false)) {
            GT_ModHandler.removeRecipe(new ItemStack(Items.glowstone_dust, 1), new ItemStack(Items.glowstone_dust, 1), null, new ItemStack(Items.glowstone_dust, 1), new ItemStack(Items.glowstone_dust, 1));
        }
        GT_ModHandler.removeRecipeDelayed(new ItemStack(Blocks.wooden_slab, 1, 0), new ItemStack(Blocks.wooden_slab, 1, 1), new ItemStack(Blocks.wooden_slab, 1, 2));
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.wooden_slab, 6, 0), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"WWW", 'W', new ItemStack(Blocks.planks, 1, 0)});

        // Save a copy of these list before activateOreDictHandler(), then loop over them.
        Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList = GT_ModHandler.getMaceratorRecipeList();
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList = GT_ModHandler.getCompressorRecipeList();
        Map<IRecipeInput, RecipeOutput> aExtractorRecipeList = GT_ModHandler.getExtractorRecipeList();
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList = GT_ModHandler.getOreWashingRecipeList();
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList = GT_ModHandler.getThermalCentrifugeRecipeList();

        GT_Log.out.println("GT_Mod: Activating OreDictionary Handler, this can take some time, as it scans the whole OreDictionary");
        GT_FML_LOGGER.info("If your Log stops here, you were too impatient. Wait a bit more next time, before killing Minecraft with the Task Manager.");

        GT_PostLoad.activateOreDictHandler();
        GT_PostLoad.replaceVanillaMaterials();
        GT_PostLoad.removeIc2Recipes(aMaceratorRecipeList, aCompressorRecipeList, aExtractorRecipeList, aOreWashingRecipeList, aThermalCentrifugeRecipeList);

        if (GT_Values.D1) {
            GT_ModHandler.sSingleNonBlockDamagableRecipeList.forEach(iRecipe -> GT_Log.out.println("=> " + iRecipe.getRecipeOutput().getDisplayName()));
        }
        new GT_CraftingRecipeLoader().run();
        if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "ic2forgehammer", true)) {
            GT_ModHandler.removeRecipeByOutput(ItemList.IC2_ForgeHammer.getWildcard(1L));
        }
        GT_ModHandler.removeRecipeByOutput(GT_ModHandler.getIC2Item("machine", 1L));
        GT_ModHandler.addCraftingRecipe(GT_ModHandler.getIC2Item("machine", 1L), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "RwR", "RRR", 'R', OrePrefixes.plate.get(Materials.Iron)});
        
        GT_PostLoad.registerFluidCannerRecipes();

        if (Loader.isModLoaded(MOD_ID_FR)) {
            GT_Forestry_Compat.transferCentrifugeRecipes();
            GT_Forestry_Compat.transferSqueezerRecipes();
        }
        if (GregTech_API.mAE2)
            GT_MetaTileEntity_DigitalChestBase.registerAEIntegration();


        Arrays.stream(new String[]{
                "blastfurnace", "blockcutter", "inductionFurnace", "generator", "windMill", "waterMill", "solarPanel", "centrifuge", "electrolyzer", "compressor",
                "electroFurnace", "extractor", "macerator", "recycler", "metalformer", "orewashingplant", "massFabricator", "replicator",
            })
           .filter(tName -> GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, aTextIC2 + tName, true))
           .map(tName -> GT_ModHandler.getIC2Item(tName, 1L)).forEach(GT_ModHandler::removeRecipeByOutputDelayed);


        GT_PostLoad.nerfVanillaTools();
        new GT_ExtremeDieselFuelLoader().run();
        
        
        /* 
         * Until this point most crafting recipe additions, and removals, have been buffered.
         * Go through, execute the removals in bulk, and then any deferred additions.  The bulk removals in particular significantly speed up the recipe list
         * modifications.
         */

        Stopwatch stopwatch = Stopwatch.createStarted();
        GT_Log.out.println("GT_Mod: Adding buffered Recipes.");
        GT_ModHandler.stopBufferingCraftingRecipes();
        GT_FML_LOGGER.info("Executed delayed Crafting Recipes (" + stopwatch.stop() + "). Have a Cake.");
        
        GT_Log.out.println("GT_Mod: Saving Lang File.");
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

        ReverseShapedRecipe.runReverseRecipes();
        ReverseShapelessRecipe.runReverseRecipes();

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
        //Check for more IC2 recipes on ServerStart to also catch MineTweaker additions
        GT_ModHandler.addIC2RecipesToGT(GT_ModHandler.getMaceratorRecipeList(), GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(GT_ModHandler.getCompressorRecipeList(), GT_Recipe.GT_Recipe_Map.sCompressorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(GT_ModHandler.getExtractorRecipeList(), GT_Recipe.GT_Recipe_Map.sExtractorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(GT_ModHandler.getOreWashingRecipeList(), GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, false, true, true);
        GT_ModHandler.addIC2RecipesToGT(GT_ModHandler.getThermalCentrifugeRecipeList(), GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, true, true, true);
        GT_Log.out.println("GT_Mod: Unificating outputs of all known Recipe Types.");
        ArrayList<ItemStack> tStacks = new ArrayList<>(10000);
        GT_Log.out.println("GT_Mod: IC2 Machines");

        ic2.api.recipe.Recipes.cannerBottle.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.centrifuge.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.compressor.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.extractor.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.macerator.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerCutting.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerExtruding.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.metalformerRolling.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.matterAmplifier.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);
        ic2.api.recipe.Recipes.oreWashing.getRecipes().values().stream().map(t -> t.items).forEach(tStacks::addAll);

        GT_Log.out.println("GT_Mod: Dungeon Loot");
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("dungeonChest").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("bonusChest").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("villageBlacksmith").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdCrossing").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdLibrary").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("strongholdCorridor").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidJungleDispenser").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidJungleChest").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("pyramidDesertyChest").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        for (WeightedRandomChestContent tContent : ChestGenHooks.getInfo("mineshaftCorridor").getItems(new XSTR())) {
            tStacks.add(tContent.theItemId);
        }
        GT_Log.out.println("GT_Mod: Smelting");

        FurnaceRecipes.smelting().getSmeltingList().values().forEach(k -> tStacks.add((ItemStack) k));

        if (gregtechproxy.mCraftingUnification) {
            GT_Log.out.println("GT_Mod: Crafting Recipes");
            for (Object tRecipe : CraftingManager.getInstance().getRecipeList()) {
                if ((tRecipe instanceof IRecipe)) {
                    tStacks.add(((IRecipe) tRecipe).getRecipeOutput());
                }
            }
        }
        for (ItemStack tOutput : tStacks) {
            if (gregtechproxy.mRegisteredOres.contains(tOutput)) {
                GT_FML_LOGGER.error("GT-ERR-01: @ " + tOutput.getUnlocalizedName() + "   " + tOutput.getDisplayName());
                GT_FML_LOGGER.error("A Recipe used an OreDict Item as Output directly, without copying it before!!! This is a typical CallByReference/CallByValue Error");
                GT_FML_LOGGER.error("Said Item will be renamed to make the invalid Recipe visible, so that you can report it properly.");
                GT_FML_LOGGER.error("Please check all Recipes outputting this Item, and report the Recipes to their Owner.");
                GT_FML_LOGGER.error("The Owner of the ==>RECIPE<==, NOT the Owner of the Item, which has been mentioned above!!!");
                GT_FML_LOGGER.error("And ONLY Recipes which are ==>OUTPUTTING<== the Item, sorry but I don't want failed Bug Reports.");
                GT_FML_LOGGER.error("GregTech just reports this Error to you, so you can report it to the Mod causing the Problem.");
                GT_FML_LOGGER.error("Even though I make that Bug visible, I can not and will not fix that for you, that's for the causing Mod to fix.");
                GT_FML_LOGGER.error("And speaking of failed Reports:");
                GT_FML_LOGGER.error("Both IC2 and GregTech CANNOT be the CAUSE of this Problem, so don't report it to either of them.");
                GT_FML_LOGGER.error("I REPEAT, BOTH, IC2 and GregTech CANNOT be the source of THIS BUG. NO MATTER WHAT.");
                GT_FML_LOGGER.error("Asking in the IC2 Forums, which Mod is causing that, won't help anyone, since it is not possible to determine, which Mod it is.");
                GT_FML_LOGGER.error("If it would be possible, then I would have had added the Mod which is causing it to the Message already. But it is not possible.");
                GT_FML_LOGGER.error("Sorry, but this Error is serious enough to justify this Wall-O-Text and the partially allcapsed Language.");
                GT_FML_LOGGER.error("Also it is a Ban Reason on the IC2-Forums to post this seriously.");
                tOutput.setStackDisplayName("ERROR! PLEASE CHECK YOUR LOG FOR 'GT-ERR-01'!");
            } else {
                GT_OreDictUnificator.setStack(tOutput);
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
            for (Map<gregtech.api.objects.GT_ItemStack, ?> gt_itemStackMap : GregTech_API.sItemStackMappings) {
                GT_Utility.reMap((Map) gt_itemStackMap);
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
        //Interrupt IDLE Threads to close down cleanly
        GT_Runnable_MachineBlockUpdate.shutdownExecutorService();
    }

    public static void logStackTrace(Throwable t) {
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        GT_FML_LOGGER.error(sw);
    }
}
