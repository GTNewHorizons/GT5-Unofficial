package gtPlusPlus;

import static gregtech.api.enums.Mods.ModIDs;
import static gregtech.api.enums.Mods.Thaumcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.FishPondRecipes;
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
import gtPlusPlus.xmod.gregtech.common.MetaGTProxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenBlastSmelterGTNH;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenMultisUsingFluidInsteadOfCells;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;

@Mod(
    modid = ModIDs.G_T_PLUS_PLUS,
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

    @Mod.Instance(Mods.ModIDs.G_T_PLUS_PLUS)
    public static GTplusplus instance;

    @SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.common.CommonProxy")
    public static CommonProxy proxy;

    @SideOnly(value = Side.CLIENT)
    public static void loadTextures() {
        Logger.INFO("Loading some textures on the client.");
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
        fixVanillaOreDict();
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
        FishPondRecipes.generateFishPondRecipes();
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

    private static void fixVanillaOreDict() {
        OreDictionary.registerOre("rodBlaze", new ItemStack(Items.blaze_rod));
        OreDictionary.registerOre("cropNetherWart", new ItemStack(Items.nether_wart));
        OreDictionary.registerOre("sugarcane", new ItemStack(Items.reeds));
        OreDictionary.registerOre("paper", new ItemStack(Items.paper));
        OreDictionary.registerOre("enderpearl", new ItemStack(Items.ender_pearl));
        OreDictionary.registerOre("bone", new ItemStack(Items.bone));
        OreDictionary.registerOre("gunpowder", new ItemStack(Items.gunpowder));
        OreDictionary.registerOre("string", new ItemStack(Items.string));
        OreDictionary.registerOre("netherStar", new ItemStack(Items.nether_star));
        OreDictionary.registerOre("leather", new ItemStack(Items.leather));
        OreDictionary.registerOre("feather", new ItemStack(Items.feather));
        OreDictionary.registerOre("egg", new ItemStack(Items.egg));
        OreDictionary.registerOre("endstone", new ItemStack(Blocks.end_stone));
        OreDictionary.registerOre("vine", new ItemStack(Blocks.vine));
        OreDictionary.registerOre("blockCactus", new ItemStack(Blocks.cactus));
        OreDictionary.registerOre("grass", new ItemStack(Blocks.grass));
        OreDictionary.registerOre("obsidian", new ItemStack(Blocks.obsidian));
        OreDictionary.registerOre("workbench", new ItemStack(Blocks.crafting_table));
    }
}
