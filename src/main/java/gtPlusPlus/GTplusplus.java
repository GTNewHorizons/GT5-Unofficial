package gtPlusPlus;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Names;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.Configuration;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.PacketHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.plugin.fixes.vanilla.VanillaBedHeightFix;
import gtPlusPlus.xmod.gregtech.common.MetaGTProxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenBlastSmelterGTNH;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMultisUsingFluidInsteadOfCells;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;

@Mod(
    modid = Names.G_T_PLUS_PLUS,
    name = GTPPCore.name,
    version = GTPPCore.VERSION,
    guiFactory = "gtPlusPlus.core.gui.config.GTPPGuiFactory",
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
public class GTplusplus {

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

    static {
        try {
            ConfigurationManager.registerConfig(Configuration.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
    public static INIT_PHASE CURRENT_LOAD_PHASE = INIT_PHASE.SUPER;

    @Mod.Instance(Names.G_T_PLUS_PLUS)
    public static GTplusplus instance;

    @SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.common.CommonProxy")
    public static CommonProxy proxy;

    @SideOnly(value = Side.CLIENT)
    public static void loadTextures() {
        Logger.INFO("Loading some textures on the client.");
        // Tools
        Logger.WARNING(
            "Processing texture: " + TexturesGtTools.ANGLE_GRINDER.getTextureFile()
                .getResourcePath());
        Logger.WARNING(
            "Processing texture: " + TexturesGtTools.ELECTRIC_SNIPS.getTextureFile()
                .getResourcePath());

        // Blocks
        Logger.WARNING(
            "Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile()
                .getResourcePath());
    }

    public GTplusplus() {
        super();
        INIT_PHASE.SUPER.setPhaseActive(true);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        INIT_PHASE.PRE_INIT.setPhaseActive(true);
        PacketHandler.init();

        // Give this a go mate.
        setupMaterialBlacklist();

        // Check for Dev
        GTPPCore.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        proxy.preInit(event);
        Logger.INFO("Setting up our own GTProxy.");
        MetaGTProxy.preInit();
        AgrichemFluids.preInit();
        fixVanillaOreDict();
        new VanillaBedHeightFix();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        INIT_PHASE.INIT.setPhaseActive(true);
        proxy.init(event);
        proxy.registerNetworkStuff();
        MetaGTProxy.init();
        // Used by foreign players to generate .lang files for translation.
        if (Configuration.debug.dumpItemAndBlockData) {
            LocaleUtils.generateFakeLocaleFile();
        }
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        INIT_PHASE.POST_INIT.setPhaseActive(true);
        proxy.postInit(event);
        BookHandler.runLater();
        MetaGTProxy.postInit();

        Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Logger.INFO(
            "| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess
                + " | Failed: "
                + RegistrationHandler.recipesFailed
                + " |");
        Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Logger.INFO("Finally, we are finished. Have some cripsy bacon as a reward.");

        // Log free GT++ Meta IDs
        if (GTPPCore.DEVENV) {
            // 750 - 999 are reserved for Alkalus.
            for (int i = 750; i < 1000; i++) {
                if (GregTechAPI.METATILEENTITIES[i] == null) {
                    Logger.INFO("MetaID " + i + " is free.");
                }
            }
            // 30000 - 31999 are reserved for Alkalus.
            for (int i = 30000; i < 32000; i++) {
                if (GregTechAPI.METATILEENTITIES[i] == null) {
                    Logger.INFO("MetaID " + i + " is free.");
                }
            }
        }
    }

    @EventHandler
    public synchronized void serverStarting(final FMLServerStartingEvent event) {
        INIT_PHASE.SERVER_START.setPhaseActive(true);
        if (Thaumcraft.isModLoaded()) {
            event.registerServerCommand(new CommandDumpAspects());
        }
        INIT_PHASE.STARTED.setPhaseActive(true);
    }

    /**
     * This {@link EventHandler} is called after the {@link FMLPostInitializationEvent} stages of all loaded mods
     * executes successfully. {@link #onLoadComplete(FMLLoadCompleteEvent)} exists to inject recipe generation after
     * Gregtech and all other mods are entirely loaded and initialized.
     *
     * @param event - The {@link EventHandler} object passed through from FML to {@link #GTplusplus()}'s
     *              {@link #instance}.
     */
    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.onLoadComplete(event);
        generateGregtechRecipeMaps();
    }

    protected void generateGregtechRecipeMaps() {

        RecipeGenBlastSmelterGTNH.generateGTNHBlastSmelterRecipesFromEBFList();
        FishPondFakeRecipe.generateFishPondRecipes();
        SemiFluidFuelHandler.generateFuels();

        RecipeGenMultisUsingFluidInsteadOfCells
            .generateRecipesNotUsingCells(RecipeMaps.centrifugeRecipes, GTPPRecipeMaps.centrifugeNonCellRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells
            .generateRecipesNotUsingCells(RecipeMaps.electrolyzerRecipes, GTPPRecipeMaps.electrolyzerNonCellRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells
            .generateRecipesNotUsingCells(RecipeMaps.vacuumFreezerRecipes, GTPPRecipeMaps.advancedFreezerRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells
            .generateRecipesNotUsingCells(RecipeMaps.mixerRecipes, GTPPRecipeMaps.mixerNonCellRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
            GTPPRecipeMaps.chemicalDehydratorRecipes,
            GTPPRecipeMaps.chemicalDehydratorNonCellRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
            GTPPRecipeMaps.coldTrapRecipes,
            GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes);
        RecipeGenMultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
            GTPPRecipeMaps.reactorProcessingUnitRecipes,
            GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes);
    }

    private static void setupMaterialBlacklist() {
        Material.invalidMaterials.add(Materials._NULL);
        Material.invalidMaterials.add(Materials.Clay);
        Material.invalidMaterials.add(Materials.Phosphorus);
        Material.invalidMaterials.add(Materials.Steel);
        Material.invalidMaterials.add(Materials.Bronze);
        Material.invalidMaterials.add(Materials.Hydrogen);
        // Infused TC stuff
        Material.invalidMaterials.add(Materials.InfusedAir);
        Material.invalidMaterials.add(Materials.InfusedEarth);
        Material.invalidMaterials.add(Materials.InfusedFire);
        Material.invalidMaterials.add(Materials.InfusedWater);
        // EIO Materials
        Material.invalidMaterials.add(Materials.SoulSand);
        Material.invalidMaterials.add(Materials.EnderPearl);
        Material.invalidMaterials.add(Materials.EnderEye);
        Material.invalidMaterials.add(Materials.Redstone);
        Material.invalidMaterials.add(Materials.Glowstone);
        Material.invalidMaterials.add(Materials.Soularium);
        Material.invalidMaterials.add(Materials.PhasedIron);

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
        sMissingItemMappings
            .put("miscutils:crushedPurifiedCryolite", GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedCryoliteF"));
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
        sMissingItemMappings
            .put("miscutils:crushedPurifiedFluorite", GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedFluoriteF"));
        sMissingItemMappings.put(
            "miscutils:crushedCentrifugedFluorite",
            GameRegistry.findItem(GTPlusPlus.ID, "crushedCentrifugedFluoriteF"));
        sMissingItemMappings.put("miscutils:oreFluorite", GameRegistry.findItem(GTPlusPlus.ID, "oreFluoriteF"));
    }

    @EventHandler
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

    private static void fixVanillaOreDict() {
        registerToOreDict(ItemUtils.getSimpleStack(Items.blaze_rod), "rodBlaze");
        registerToOreDict(ItemUtils.getSimpleStack(Items.nether_wart), "cropNetherWart");
        registerToOreDict(ItemUtils.getSimpleStack(Items.reeds), "sugarcane");
        registerToOreDict(ItemUtils.getSimpleStack(Items.paper), "paper");
        registerToOreDict(ItemUtils.getSimpleStack(Items.ender_pearl), "enderpearl");
        registerToOreDict(ItemUtils.getSimpleStack(Items.bone), "bone");
        registerToOreDict(ItemUtils.getSimpleStack(Items.gunpowder), "gunpowder");
        registerToOreDict(ItemUtils.getSimpleStack(Items.string), "string");
        registerToOreDict(ItemUtils.getSimpleStack(Items.nether_star), "netherStar");
        registerToOreDict(ItemUtils.getSimpleStack(Items.leather), "leather");
        registerToOreDict(ItemUtils.getSimpleStack(Items.feather), "feather");
        registerToOreDict(ItemUtils.getSimpleStack(Items.egg), "egg");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.end_stone), "endstone");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.vine), "vine");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.cactus), "blockCactus");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.grass), "grass");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.obsidian), "obsidian");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.crafting_table), "workbench");
    }

    private static void registerToOreDict(ItemStack aStack, String aString) {
        if (aStack.getItem() == Items.blaze_rod) {
            Logger
                .INFO("Registering " + aStack.getDisplayName() + " to OreDictionary under the tag '" + aString + "'.");
        } else {
            Logger.INFO(
                "Registering " + aStack.getDisplayName()
                    + " to OreDictionary under the tag '"
                    + aString
                    + "'. (Added to Forge in 1.8.9)");
        }
        ItemUtils.addItemToOreDictionary(aStack, aString);
    }
}
