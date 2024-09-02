package gtPlusPlus.everglades;

import static gregtech.api.enums.Mods.GTPlusPlusEverglades;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import net.minecraft.block.Block;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

import bwcrossmod.galacticgreg.MTEVoidMinerBase;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.lib.GTPPCore.Everglades;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.everglades.biome.BiomeEverglades;
import gtPlusPlus.everglades.block.DarkWorldContentLoader;
import gtPlusPlus.everglades.dimension.DimensionEverglades;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT_Base;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT_Ore_Layer;
import gtPlusPlus.everglades.gen.gt.WorldGen_Ores;
import gtPlusPlus.preloader.PreloaderCore;
import gtPlusPlus.xmod.gregtech.HandlerGT;
import gtPlusPlus.xmod.gregtech.api.util.GTPPConfig;

@Mod(
    modid = Mods.Names.G_T_PLUS_PLUS_EVERGLADES,
    name = Everglades.NAME,
    version = Everglades.VERSION,
    dependencies = "required-after:Forge; after:dreamcraft; after:IC2; required-after:gregtech; required-after:miscutils;")
public class GTPPEverglades implements ActionListener {

    // Mod Instance
    @Mod.Instance(Mods.Names.G_T_PLUS_PLUS_EVERGLADES)
    public static GTPPEverglades instance;

    // Dark World Handler
    protected static volatile BiomeEverglades Everglades_Biome;
    protected static volatile DimensionEverglades Everglades_Dimension;

    // Pre-Init
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Logger.INFO("Loading " + GTPlusPlusEverglades.ID + " V" + Everglades.VERSION);

        // Setup
        setVars(event);

        setEvergladesBiome(new BiomeEverglades());
        Everglades_Dimension = new DimensionEverglades();

        // Load Dark World
        getEvergladesBiome().instance = instance;
        Everglades_Dimension.instance = instance;
        getEvergladesBiome().preInit(event);

        // Load/Set Custom Ore Gen
        HandlerGT.sCustomWorldgenFile = new GTPPConfig(
            new Configuration(
                new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));
    }

    @EventHandler
    public void load(final FMLInitializationEvent e) {
        Logger.INFO("Begin resource allocation for " + GTPlusPlusEverglades.ID + " V" + Everglades.VERSION);

        // Load World and Biome
        GameRegistry.registerWorldGenerator(new WorldGen_GT_Base(), Short.MAX_VALUE);
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
        // Init WorldGen config.
        HandlerGT.sCustomWorldgenFile = new GTPPConfig(
            new Configuration(
                new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));

        if (DimensionManager.isDimensionRegistered(DimensionEverglades.DIMID)) {
            DimensionEverglades.DIMID = DimensionManager.getNextFreeDimId();
        }

        /*
         * Set World Generation Values
         */
        WorldGen_Ores.generateValidOreVeins();
        WorldGen_GT_Base.oreveinPercentage = 64;
        WorldGen_GT_Base.oreveinAttempts = 16;
        WorldGen_GT_Base.oreveinMaxPlacementAttempts = 4;
        if (PreloaderCore.DEBUG_MODE || GTPPCore.DEVENV) {
            WorldGen_GT_Base.debugWorldGen = true;
        }
        DarkWorldContentLoader.run();
    }

    public void addToVoidMinerDrops() {
        for (WorldGen_GT_Ore_Layer t : WorldGen_Ores.validOreveins.values()) {
            addVMDrop(t.mPrimaryMeta, 0, t.mWeight);
            addVMDrop(t.mSecondaryMeta, 0, t.mWeight);
            addVMDrop(t.mBetweenMeta, 0, t.mWeight / 8f);
            addVMDrop(t.mSporadicMeta, 0, t.mWeight / 8f);
        }
    }

    public void addVMDrop(Block block, int meta, float weight) {
        MTEVoidMinerBase.addBlockToDimensionList(GTPPCore.EVERGLADES_ID, block, meta, weight);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        getEvergladesBiome().serverLoad(event);
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

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
}
