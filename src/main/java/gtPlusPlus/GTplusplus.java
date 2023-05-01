package gtPlusPlus;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Names;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustomCapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.chunkloading.GTPP_ChunkManager;
import gtPlusPlus.core.commands.CommandDebugChunks;
import gtPlusPlus.core.commands.CommandEnableDebugWhileRunning;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.MobMentality;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.handler.events.MissingMappingsEvent;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.ItemGiantEgg;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.nei.NEI_IMC_Sender;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.GregtechMTE_ChemicalPlant;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelterGT_GTNH;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_MultisUsingFluidInsteadOfCells;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;

@MCVersion(value = "1.7.10")
@Mod(
        modid = Names.G_T_PLUS_PLUS,
        name = CORE.name,
        version = CORE.VERSION,
        dependencies = "required-after:Forge;" + " after:TConstruct;"
                + " after:PlayerAPI;"
                + " after:dreamcraft;"
                + " after:IC2;"
                + " required-after:gregtech;"
                + " after:Forestry;"
                + " after:MagicBees;"
                + " after:CoFHCore;"
                + " after:Railcraft;"
                + " after:CompactWindmills;"
                + " after:ForbiddenMagic;"
                + " after:ExtraUtilities;"
                + " after:Thaumcraft;"
                + " after:EnderIO;"
                + " after:tectech;"
                + " after:OpenBlocks;"
                + " after:IC2NuclearControl;"
                + " after:TGregworks;"
                + " after:StevesCarts;"
                + " required-after:gtnhlib@[0.0.10,);")
public class GTplusplus implements ActionListener {

    public static enum INIT_PHASE {

        SUPER(null),
        PRE_INIT(SUPER),
        INIT(PRE_INIT),
        POST_INIT(INIT),
        SERVER_START(POST_INIT),
        STARTED(SERVER_START);

        protected boolean mIsPhaseActive = false;
        private final INIT_PHASE mPrev;

        private INIT_PHASE(INIT_PHASE aPreviousPhase) {
            mPrev = aPreviousPhase;
        }

        public final synchronized boolean isPhaseActive() {
            return mIsPhaseActive;
        }

        public final synchronized void setPhaseActive(boolean aIsPhaseActive) {
            if (mPrev != null && mPrev.isPhaseActive()) {
                mPrev.setPhaseActive(false);
            }
            mIsPhaseActive = aIsPhaseActive;
            if (CURRENT_LOAD_PHASE != this) {
                CURRENT_LOAD_PHASE = this;
            }
        }
    }

    public static INIT_PHASE CURRENT_LOAD_PHASE = INIT_PHASE.SUPER;

    // Mod Instance
    @Mod.Instance(Names.G_T_PLUS_PLUS)
    public static GTplusplus instance;

    // GT++ Proxy Instances
    @SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.proxy.ServerProxy")
    public static CommonProxy proxy;

    // Loads Textures
    @SideOnly(value = Side.CLIENT)
    public static void loadTextures() {
        Logger.INFO("Loading some textures on the client.");
        // Tools
        Logger.WARNING("Processing texture: " + TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());
        Logger.WARNING("Processing texture: " + TexturesGtTools.ANGLE_GRINDER.getTextureFile().getResourcePath());
        Logger.WARNING("Processing texture: " + TexturesGtTools.ELECTRIC_SNIPS.getTextureFile().getResourcePath());
        Logger.WARNING("Processing texture: " + TexturesGtTools.ELECTRIC_LIGHTER.getTextureFile().getResourcePath());
        Logger.WARNING(
                "Processing texture: " + TexturesGtTools.ELECTRIC_BUTCHER_KNIFE.getTextureFile().getResourcePath());

        // Blocks
        Logger.WARNING(
                "Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
    }

    public GTplusplus() {
        super();
        INIT_PHASE.SUPER.setPhaseActive(true);

        for (int i = 12; i < 16; i++) {
            GregTech_API.registerTileEntityConstructor(i, Meta_GT_Proxy::constructCustomGregtechMetaTileEntityByMeta);
        }
    }

    // Pre-Init
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        INIT_PHASE.PRE_INIT.setPhaseActive(true);
        // Load all class objects within the plugin package.
        Core_Manager.veryEarlyInit();
        PacketHandler.init();

        if (!Utils.isServer()) {
            enableCustomCapes = true;
        }

        // Give this a go mate.
        // initAnalytics();
        setupMaterialBlacklist();

        // Handle GT++ Config
        ConfigHandler.handleConfigFile(event);

        // Check for Dev
        CORE.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        // Utils.LOG_INFO("User's Country: " + CORE.USER_COUNTRY);

        Utils.registerEvent(new LoginEventHandler());
        Utils.registerEvent(new MissingMappingsEvent());
        Utils.registerEvent(new MobMentality());
        Logger.INFO("Login Handler Initialized");

        proxy.preInit(event);
        Logger.INFO("Setting up our own GT_Proxy.");
        Meta_GT_Proxy.preInit();
        Core_Manager.preInit();
        GTPP_ChunkManager.init();
    }

    // Init
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        INIT_PHASE.INIT.setPhaseActive(true);
        proxy.init(event);
        proxy.registerNetworkStuff();
        Meta_GT_Proxy.init();
        Core_Manager.init();
        NEI_IMC_Sender.IMCSender();
        // Used by foreign players to generate .lang files for translation.
        if (CORE.ConfigSwitches.dumpItemAndBlockData) {
            LocaleUtils.generateFakeLocaleFile();
        }
    }

    // Post-Init
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        INIT_PHASE.POST_INIT.setPhaseActive(true);
        proxy.postInit(event);
        BookHandler.runLater();
        Meta_GT_Proxy.postInit();
        Core_Manager.postInit();
        // SprinklerHandler.registerModFerts();

        ItemGiantEgg.postInit(ModItems.itemBigEgg);
        BlockEventHandler.init();

        Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Logger.INFO(
                "| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess
                        + " | Failed: "
                        + RegistrationHandler.recipesFailed
                        + " |");
        Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Logger.INFO("Finally, we are finished. Have some cripsy bacon as a reward.");

        // Log free GT++ Meta IDs
        if (CORE.DEVENV) {
            // 750 - 999 are reserved for Alkalus.
            for (int i = 750; i < 1000; i++) {
                if (gregtech.api.GregTech_API.METATILEENTITIES[i] == null) {
                    Logger.INFO("MetaID " + i + " is free.");
                }
            }
            // 30000 - 31999 are reserved for Alkalus.
            for (int i = 30000; i < 32000; i++) {
                if (gregtech.api.GregTech_API.METATILEENTITIES[i] == null) {
                    Logger.INFO("MetaID " + i + " is free.");
                }
            }
        }
    }

    @EventHandler
    public synchronized void serverStarting(final FMLServerStartingEvent event) {
        INIT_PHASE.SERVER_START.setPhaseActive(true);
        event.registerServerCommand(new CommandMath());
        event.registerServerCommand(new CommandEnableDebugWhileRunning());
        event.registerServerCommand(new CommandDebugChunks());
        if (Thaumcraft.isModLoaded()) {
            event.registerServerCommand(new CommandDumpAspects());
        }
        Core_Manager.serverStart();
        INIT_PHASE.STARTED.setPhaseActive(true);
    }

    @Mod.EventHandler
    public synchronized void serverStopping(final FMLServerStoppingEvent event) {
        Core_Manager.serverStop();
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {}

    /**
     * This {@link EventHandler} is called after the {@link FMLPostInitializationEvent} stages of all loaded mods
     * executes successfully. {@link #onLoadComplete(FMLLoadCompleteEvent)} exists to inject recipe generation after
     * Gregtech and all other mods are entirely loaded and initialized.
     *
     * @param event - The {@link EventHandler} object passed through from FML to {@link #GTplusplus()}'s
     *              {@link #instance}.
     */
    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.onLoadComplete(event);
        generateGregtechRecipeMaps();
    }

    protected void generateGregtechRecipeMaps() {

        int[] mValidCount = new int[] { 0, 0, 0 };
        int[] mInvalidCount = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        int[] mOriginalCount = new int[] { 0, 0, 0 };

        RecipeGen_BlastSmelterGT_GTNH.generateGTNHBlastSmelterRecipesFromEBFList();
        FishPondFakeRecipe.generateFishPondRecipes();
        // GregtechMiniRaFusion.generateSlowFusionrecipes();
        SemiFluidFuelHandler.generateFuels();
        GregtechMTE_ChemicalPlant.generateRecipes();

        mInvalidCount[0] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);
        mInvalidCount[1] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT);
        mInvalidCount[2] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GT_Recipe.GT_Recipe_Map.sVacuumRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes_GT);
        mInvalidCount[3] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);
        mInvalidCount[4] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sMultiblockChemicalDehydratorRecipes);
        mInvalidCount[5] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPP_Recipe.GTPP_Recipe_Map.sColdTrapRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sNuclearSaltProcessingPlantRecipes);
        mInvalidCount[6] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPP_Recipe.GTPP_Recipe_Map.sReactorProcessingUnitRecipes,
                GTPP_Recipe.GTPP_Recipe_Map.sNuclearSaltProcessingPlantRecipes);
        /*
         * //Large Centrifuge generation mOriginalCount[0] =
         * GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.size(); for (GT_Recipe x :
         * GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) { if (x != null) { if
         * (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs)) { if
         * (CORE.RA.addMultiblockCentrifugeRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mChances,
         * x.mDuration, x.mEUt, x.mSpecialValue)) { mValidCount[0]++; } else { mInvalidCount[0]++; } } else {
         * Logger.INFO("[Recipe] Error generating Large Centrifuge recipe.");
         * Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(x.mInputs));
         * Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(x.mFluidInputs));
         * Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(x.mOutputs));
         * Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(x.mFluidOutputs)); } } else { mInvalidCount[0]++;
         * } } if (GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList.size() < 1) { for (GT_Recipe
         * a : GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList) {
         * GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.add(a); } } //Large Electrolyzer generation
         * mOriginalCount[1] = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList.size(); for (GT_Recipe x :
         * GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList) { if (x != null) { if
         * (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs)) { if
         * (CORE.RA.addMultiblockElectrolyzerRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mChances,
         * x.mDuration, x.mEUt, x.mSpecialValue)) { mValidCount[1]++; } else { mInvalidCount[1]++; } } else {
         * Logger.INFO("[Recipe] Error generating Large Electrolyzer recipe.");
         * Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(x.mInputs));
         * Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(x.mFluidInputs));
         * Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(x.mOutputs));
         * Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(x.mFluidOutputs)); } } else { mInvalidCount[1]++;
         * } } if (GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mRecipeList.size() < 1) { for
         * (GT_Recipe a : GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mRecipeList) {
         * GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.add(a); } } //Advanced Vacuum Freezer
         * generation mOriginalCount[2] = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList.size(); for (GT_Recipe x :
         * GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList) { if (x != null &&
         * RecipeUtils.doesGregtechRecipeHaveEqualCells(x)) { if (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs))
         * { if (CORE.RA.addAdvancedFreezerRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mChances,
         * x.mDuration, x.mEUt, x.mSpecialValue)) { mValidCount[2]++; } } else { mInvalidCount[2]++; } } else {
         * mInvalidCount[2]++; } } //Redo plasma recipes in Adv. Vac.
         * //Meta_GT_Proxy.generatePlasmaRecipesForAdvVacFreezer(); String[] machineName = new String[] {"Centrifuge",
         * "Electrolyzer", "Vacuum Freezer"}; for (int i=0;i<3;i++) {
         * Logger.INFO("[Recipe] Generated "+mValidCount[i]+" recipes for the Industrial "+machineName[i]
         * +". The original machine can process "+mOriginalCount[i]+" recipes, meaning "+mInvalidCount[i]
         * +" are invalid for this Multiblock's processing in some way."); }
         */
    }

    protected void dumpGtRecipeMap(final GT_Recipe_Map r) {
        final Collection<GT_Recipe> x = r.mRecipeList;
        Logger.INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug.");
        for (final GT_Recipe newBo : x) {
            Logger.INFO("========================");
            Logger.INFO("Dumping Input: " + ItemUtils.getArrayStackNames(newBo.mInputs));
            Logger.INFO("Dumping Inputs " + ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
            Logger.INFO("Dumping Duration: " + newBo.mDuration);
            Logger.INFO("Dumping EU/t: " + newBo.mEUt);
            Logger.INFO("Dumping Output: " + ItemUtils.getArrayStackNames(newBo.mOutputs));
            Logger.INFO("Dumping Output: " + ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
            Logger.INFO("========================");
        }
    }

    private static final boolean setupMaterialBlacklist() {
        Material.invalidMaterials.put(Materials._NULL);
        Material.invalidMaterials.put(Materials.Clay);
        Material.invalidMaterials.put(Materials.Phosphorus);
        Material.invalidMaterials.put(Materials.Steel);
        Material.invalidMaterials.put(Materials.Bronze);
        Material.invalidMaterials.put(Materials.Hydrogen);
        // Infused TC stuff
        Material.invalidMaterials.put(Materials.InfusedAir);
        Material.invalidMaterials.put(Materials.InfusedEarth);
        Material.invalidMaterials.put(Materials.InfusedFire);
        Material.invalidMaterials.put(Materials.InfusedWater);
        // EIO Materials
        Material.invalidMaterials.put(Materials.SoulSand);
        Material.invalidMaterials.put(Materials.EnderPearl);
        Material.invalidMaterials.put(Materials.EnderEye);
        Material.invalidMaterials.put(Materials.Redstone);
        Material.invalidMaterials.put(Materials.Glowstone);
        Material.invalidMaterials.put(Materials.Soularium);
        Material.invalidMaterials.put(Materials.PhasedIron);

        if (Material.invalidMaterials.size() > 0) {
            return true;
        }
        return false;
    }

    private static final HashMap<String, Item> sMissingItemMappings = new HashMap<String, Item>();
    private static final HashMap<String, Block> sMissingBlockMappings = new HashMap<String, Block>();

    private static void processMissingMappings() {
        sMissingItemMappings.put("miscutils:Ammonium", GameRegistry.findItem(GTPlusPlus.ID, "itemCellAmmonium"));
        sMissingItemMappings.put("miscutils:Hydroxide", GameRegistry.findItem(GTPlusPlus.ID, "itemCellHydroxide"));
        sMissingItemMappings.put(
                "miscutils:BerylliumHydroxide",
                GameRegistry.findItem(GTPlusPlus.ID, "itemCellmiscutils:BerylliumHydroxide"));
        sMissingItemMappings.put("miscutils:Bromine", GameRegistry.findItem(GTPlusPlus.ID, "itemCellBromine"));
        sMissingItemMappings.put("miscutils:Krypton", GameRegistry.findItem(GTPlusPlus.ID, "itemCellKrypton"));
        sMissingItemMappings.put(
                "miscutils:itemCellZirconiumTetrafluoride",
                GameRegistry.findItem(GTPlusPlus.ID, "ZirconiumTetrafluoride"));
        sMissingItemMappings
                .put("miscutils:Li2BeF4", GameRegistry.findItem(GTPlusPlus.ID, "itemCellLithiumTetrafluoroberyllate"));

        // Cryolite
        sMissingBlockMappings.put("miscutils:oreCryolite", GameRegistry.findBlock(GTPlusPlus.ID, "oreCryoliteF"));
        sMissingItemMappings
                .put("miscutils:itemDustTinyCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustTinyCryoliteF"));
        sMissingItemMappings
                .put("miscutils:itemDustSmallCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustSmallCryoliteF"));
        sMissingItemMappings
                .put("miscutils:itemDustCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustCryoliteF"));
        sMissingItemMappings
                .put("miscutils:dustPureCryolite", GameRegistry.findItem(GTPlusPlus.ID, "dustPureCryoliteF"));
        sMissingItemMappings
                .put("miscutils:dustImpureCryolite", GameRegistry.findItem(GTPlusPlus.ID, "dustImpureCryoliteF"));
        sMissingItemMappings.put("miscutils:crushedCryolite", GameRegistry.findItem(GTPlusPlus.ID, "crushedCryoliteF"));
        sMissingItemMappings.put(
                "miscutils:crushedPurifiedCryolite",
                GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedCryoliteF"));
        sMissingItemMappings.put(
                "miscutils:crushedCentrifugedCryolite",
                GameRegistry.findItem(GTPlusPlus.ID, "crushedCentrifugedCryoliteF"));
        sMissingItemMappings.put("miscutils:oreCryolite", GameRegistry.findItem(GTPlusPlus.ID, "oreCryoliteF"));

        // Fluorite
        sMissingBlockMappings.put("miscutils:oreFluorite", GameRegistry.findBlock(GTPlusPlus.ID, "oreFluoriteF"));
        sMissingItemMappings
                .put("miscutils:itemDustTinyFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustTinyFluoriteF"));
        sMissingItemMappings
                .put("miscutils:itemDustSmallFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustSmallFluoriteF"));
        sMissingItemMappings
                .put("miscutils:itemDustFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustFluoriteF"));
        sMissingItemMappings
                .put("miscutils:dustPureFluorite", GameRegistry.findItem(GTPlusPlus.ID, "dustPureFluoriteF"));
        sMissingItemMappings
                .put("miscutils:dustImpureFluorite", GameRegistry.findItem(GTPlusPlus.ID, "dustImpureFluoriteF"));
        sMissingItemMappings.put("miscutils:crushedFluorite", GameRegistry.findItem(GTPlusPlus.ID, "crushedFluoriteF"));
        sMissingItemMappings.put(
                "miscutils:crushedPurifiedFluorite",
                GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedFluoriteF"));
        sMissingItemMappings.put(
                "miscutils:crushedCentrifugedFluorite",
                GameRegistry.findItem(GTPlusPlus.ID, "crushedCentrifugedFluoriteF"));
        sMissingItemMappings.put("miscutils:oreFluorite", GameRegistry.findItem(GTPlusPlus.ID, "oreFluoriteF"));
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event) {
        processMissingMappings();
        for (MissingMapping mapping : event.getAll()) {
            if (mapping.name.startsWith("Australia:")) {
                mapping.ignore();
                continue;
            }
            if (mapping.type == GameRegistry.Type.ITEM) {
                Item aReplacement = sMissingItemMappings.get(mapping.name);
                if (aReplacement != null) {
                    remap(aReplacement, mapping);
                } else {
                    // Logger.INFO("Unable to remap: "+mapping.name+", item has no replacement mapping.");
                }
            } else if (mapping.type == GameRegistry.Type.BLOCK) {
                Block aReplacement = sMissingBlockMappings.get(mapping.name);
                if (aReplacement != null) {
                    remap(aReplacement, mapping);
                } else {
                    // Logger.INFO("Unable to remap: "+mapping.name+", block has no replacement mapping.");
                }
            }
        }
    }

    private static void remap(Item item, FMLMissingMappingsEvent.MissingMapping mapping) {
        mapping.remap(item);
        Logger.INFO("Remapping item " + mapping.name + " to " + GTPlusPlus.ID + ":" + item.getUnlocalizedName());
    }

    private static void remap(Block block, FMLMissingMappingsEvent.MissingMapping mapping) {
        mapping.remap(block);
        Logger.INFO("Remapping block " + mapping.name + " to " + GTPlusPlus.ID + ":" + block.getUnlocalizedName());
    }
}
