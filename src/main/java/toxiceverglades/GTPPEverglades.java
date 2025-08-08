package toxiceverglades;

import static gregtech.api.enums.Mods.GTPlusPlusEverglades;

import net.minecraft.block.Block;
import net.minecraftforge.common.DimensionManager;

import bwcrossmod.galacticgreg.VoidMinerUtility;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Version;
import gregtech.api.enums.Mods;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialsOres;
import toxiceverglades.biome.BiomeEverglades;
import toxiceverglades.block.DarkWorldContentLoader;
import toxiceverglades.dimension.DimensionEverglades;
import toxiceverglades.gen.WorldGenEvergladesBase;
import toxiceverglades.gen.WorldGenEvergladesOreLayer;
import toxiceverglades.gen.WorldGenEvergladesOres;

@Mod(
    modid = Mods.ModIDs.G_T_PLUS_PLUS_EVERGLADES,
    name = GTPPEverglades.NAME,
    version = GTPPEverglades.VERSION,
    dependencies = "required-after:Forge; after:dreamcraft; after:IC2; required-after:gregtech; required-after:miscutils;")
public class GTPPEverglades {

    public static final String NAME = "GT++ Toxic Everglades";
    public static final String VERSION = GT_Version.VERSION;

    // Mod Instance
    @Mod.Instance(Mods.ModIDs.G_T_PLUS_PLUS_EVERGLADES)
    public static GTPPEverglades instance;

    // Dark World Handler
    protected static volatile BiomeEverglades Everglades_Biome;
    protected static volatile DimensionEverglades Everglades_Dimension;

    // Pre-Init
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Logger.INFO("Loading " + GTPlusPlusEverglades.ID + " V" + VERSION);

        // Setup
        setVars(event);

        setEvergladesBiome(new BiomeEverglades());
        Everglades_Dimension = new DimensionEverglades();

        // Load Dark World
        getEvergladesBiome().instance = instance;
        Everglades_Dimension.instance = instance;
    }

    @EventHandler
    public void load(final FMLInitializationEvent e) {
        Logger.INFO("Begin resource allocation for " + GTPlusPlusEverglades.ID + " V" + VERSION);

        // Load World and Biome
        GameRegistry.registerWorldGenerator(new WorldGenEvergladesBase(), Short.MAX_VALUE);
        getEvergladesBiome().load();
        Everglades_Dimension.load();
        addToVoidMinerDrops();
    }

    public static synchronized void GenerateOreMaterials() {
        MaterialGenerator.generateOreMaterial(MaterialsOres.CROCROITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.GEIKIELITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.NICHROMITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.TITANITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ZIMBABWEITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ZIRCONILITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.GADOLINITE_CE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.GADOLINITE_Y);
        MaterialGenerator.generateOreMaterial(MaterialsOres.LEPERSONNITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.SAMARSKITE_Y);
        MaterialGenerator.generateOreMaterial(MaterialsOres.SAMARSKITE_YB);
        MaterialGenerator.generateOreMaterial(MaterialsOres.XENOTIME);
        MaterialGenerator.generateOreMaterial(MaterialsOres.YTTRIAITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.YTTRIALITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.YTTROCERITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ZIRCON);
        MaterialGenerator.generateOreMaterial(MaterialsOres.POLYCRASE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ZIRCOPHYLLITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ZIRKELITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.LANTHANITE_LA);
        MaterialGenerator.generateOreMaterial(MaterialsOres.LANTHANITE_CE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.LANTHANITE_ND);
        MaterialGenerator.generateOreMaterial(MaterialsOres.AGARDITE_Y);
        MaterialGenerator.generateOreMaterial(MaterialsOres.AGARDITE_CD);
        MaterialGenerator.generateOreMaterial(MaterialsOres.AGARDITE_LA);
        MaterialGenerator.generateOreMaterial(MaterialsOres.AGARDITE_ND);
        MaterialGenerator.generateOreMaterial(MaterialsOres.HIBONITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.CERITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.FLUORCAPHITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.FLORENCITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.CRYOLITE);

        MaterialGenerator.generateOreMaterial(MaterialsOres.LAUTARITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.LAFOSSAITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.DEMICHELEITE_BR);
        MaterialGenerator.generateOreMaterial(MaterialsOres.COMANCHEITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.PERROUDITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.HONEAITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.ALBURNITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.MIESSIITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.KASHINITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.IRARSITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.GREENOCKITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.RADIOBARITE);
        MaterialGenerator.generateOreMaterial(MaterialsOres.DEEP_EARTH_REACTOR_FUEL_DEPOSIT);

    }

    protected synchronized void setVars(FMLPreInitializationEvent event) {
        if (DimensionManager.isDimensionRegistered(DimensionEverglades.DIMID)) {
            DimensionEverglades.DIMID = DimensionManager.getNextFreeDimId();
        }

        /*
         * Set World Generation Values
         */
        WorldGenEvergladesOres.generateValidOreVeins();
        WorldGenEvergladesBase.oreveinPercentage = 64;
        WorldGenEvergladesBase.oreveinAttempts = 16;
        WorldGenEvergladesBase.oreveinMaxPlacementAttempts = 4;
        if (ASMConfiguration.debug.debugMode || GTPPCore.DEVENV) {
            WorldGenEvergladesBase.debugWorldGen = true;
        }
        DarkWorldContentLoader.run();
    }

    public void addToVoidMinerDrops() {
        for (WorldGenEvergladesOreLayer t : WorldGenEvergladesOres.validOreveins.values()) {
            addVMDrop(t.mPrimaryMeta, 0, t.mWeight);
            addVMDrop(t.mSecondaryMeta, 0, t.mWeight);
            addVMDrop(t.mBetweenMeta, 0, t.mWeight / 8f);
            addVMDrop(t.mSporadicMeta, 0, t.mWeight / 8f);
        }
    }

    public void addVMDrop(Block block, int meta, float weight) {
        VoidMinerUtility
            .addBlockToDimensionList(gtPlusPlus.core.config.Configuration.worldgen.EVERGLADES_ID, block, meta, weight);
    }

    @EventHandler
    public static void postInit(final FMLPostInitializationEvent e) {
        Logger.INFO("Finished loading Everglades plugin for GT++.");
    }

    public static synchronized BiomeEverglades getEvergladesBiome() {
        return Everglades_Biome;
    }

    public static synchronized void setEvergladesBiome(BiomeEverglades darkWorld_Biome) {
        Everglades_Biome = darkWorld_Biome;
    }
}
