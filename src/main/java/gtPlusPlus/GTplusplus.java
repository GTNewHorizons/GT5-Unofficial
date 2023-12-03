package gtPlusPlus;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Names;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustomCapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.commands.CommandEnableDebugWhileRunning;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelterGT_GTNH;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_MultisUsingFluidInsteadOfCells;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;

@Mod(
        modid = Names.G_T_PLUS_PLUS,
        name = CORE.name,
        version = CORE.VERSION,
        dependencies = "required-after:Forge;" + " after:TConstruct;"
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

    public enum INIT_PHASE {

        SUPER(null),
        PRE_INIT(SUPER),
        INIT(PRE_INIT),
        POST_INIT(INIT),
        SERVER_START(POST_INIT),
        STARTED(SERVER_START);

        private boolean mIsPhaseActive = false;
        private final INIT_PHASE mPrev;

        INIT_PHASE(INIT_PHASE aPreviousPhase) {
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
        setupMaterialBlacklist();

        // Handle GT++ Config
        ConfigHandler.handleConfigFile(event);

        // Check for Dev
        CORE.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        proxy.preInit(event);
        Logger.INFO("Setting up our own GT_Proxy.");
        Meta_GT_Proxy.preInit();
        Core_Manager.preInit();
    }

    // Init
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        INIT_PHASE.INIT.setPhaseActive(true);
        proxy.init(event);
        proxy.registerNetworkStuff();
        Meta_GT_Proxy.init();
        Core_Manager.init();
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

        int[] mInvalidCount = new int[] { 0, 0, 0, 0, 0, 0, 0 };

        RecipeGen_BlastSmelterGT_GTNH.generateGTNHBlastSmelterRecipesFromEBFList();
        FishPondFakeRecipe.generateFishPondRecipes();
        SemiFluidFuelHandler.generateFuels();

        mInvalidCount[0] = RecipeGen_MultisUsingFluidInsteadOfCells
                .generateRecipesNotUsingCells(RecipeMaps.centrifugeRecipes, GTPPRecipeMaps.centrifugeNonCellRecipes);
        mInvalidCount[1] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                RecipeMaps.electrolyzerRecipes,
                GTPPRecipeMaps.electrolyzerNonCellRecipes);
        mInvalidCount[2] = RecipeGen_MultisUsingFluidInsteadOfCells
                .generateRecipesNotUsingCells(RecipeMaps.vacuumFreezerRecipes, GTPPRecipeMaps.advancedFreezerRecipes);
        mInvalidCount[3] = RecipeGen_MultisUsingFluidInsteadOfCells
                .generateRecipesNotUsingCells(RecipeMaps.mixerRecipes, GTPPRecipeMaps.mixerNonCellRecipes);
        mInvalidCount[4] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPPRecipeMaps.chemicalDehydratorRecipes,
                GTPPRecipeMaps.chemicalDehydratorNonCellRecipes);
        mInvalidCount[5] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPPRecipeMaps.coldTrapRecipes,
                GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes);
        mInvalidCount[6] = RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
                GTPPRecipeMaps.reactorProcessingUnitRecipes,
                GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes);
    }

    private static void setupMaterialBlacklist() {
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

    }

    private static final HashMap<String, Item> sMissingItemMappings = new HashMap<>();
    private static final HashMap<String, Block> sMissingBlockMappings = new HashMap<>();

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
                }

            } else if (mapping.type == GameRegistry.Type.BLOCK) {
                Block aReplacement = sMissingBlockMappings.get(mapping.name);
                if (aReplacement != null) {
                    remap(aReplacement, mapping);
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
