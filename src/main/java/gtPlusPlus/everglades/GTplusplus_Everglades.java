package gtPlusPlus.everglades;

import static gregtech.api.enums.Mods.GTPlusPlusEverglades;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import net.minecraft.block.Block;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

import com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.Everglades;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.everglades.biome.Biome_Everglades;
import gtPlusPlus.everglades.block.DarkWorldContentLoader;
import gtPlusPlus.everglades.dimension.Dimension_Everglades;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT_Base;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT_Ore_Layer;
import gtPlusPlus.everglades.gen.gt.WorldGen_Ores;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;

@Mod(
    modid = Mods.Names.G_T_PLUS_PLUS_EVERGLADES,
    name = Everglades.NAME,
    version = Everglades.VERSION,
    dependencies = "required-after:Forge; after:dreamcraft; after:IC2; required-after:gregtech; required-after:miscutils;")
public class GTplusplus_Everglades implements ActionListener {

    // Mod Instance
    @Mod.Instance(Mods.Names.G_T_PLUS_PLUS_EVERGLADES)
    public static GTplusplus_Everglades instance;

    // Dark World Handler
    protected static volatile Biome_Everglades Everglades_Biome;
    protected static volatile Dimension_Everglades Everglades_Dimension;

    // Pre-Init
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Logger.INFO("Loading " + GTPlusPlusEverglades.ID + " V" + Everglades.VERSION);

        // Setup
        setVars(event);

        setEvergladesBiome(new Biome_Everglades());
        Everglades_Dimension = new Dimension_Everglades();

        // Load Dark World
        getEvergladesBiome().instance = instance;
        Everglades_Dimension.instance = instance;
        getEvergladesBiome().preInit(event);

        // Load/Set Custom Ore Gen
        HANDLER_GT.sCustomWorldgenFile = new GTPP_Config(
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
        MaterialGenerator.generateOreMaterial(ORES.CROCROITE);
        MaterialGenerator.generateOreMaterial(ORES.GEIKIELITE);
        MaterialGenerator.generateOreMaterial(ORES.NICHROMITE);
        MaterialGenerator.generateOreMaterial(ORES.TITANITE);
        MaterialGenerator.generateOreMaterial(ORES.ZIMBABWEITE);
        MaterialGenerator.generateOreMaterial(ORES.ZIRCONILITE);
        MaterialGenerator.generateOreMaterial(ORES.GADOLINITE_CE);
        MaterialGenerator.generateOreMaterial(ORES.GADOLINITE_Y);
        MaterialGenerator.generateOreMaterial(ORES.LEPERSONNITE);
        MaterialGenerator.generateOreMaterial(ORES.SAMARSKITE_Y);
        MaterialGenerator.generateOreMaterial(ORES.SAMARSKITE_YB);
        MaterialGenerator.generateOreMaterial(ORES.XENOTIME);
        MaterialGenerator.generateOreMaterial(ORES.YTTRIAITE);
        MaterialGenerator.generateOreMaterial(ORES.YTTRIALITE);
        MaterialGenerator.generateOreMaterial(ORES.YTTROCERITE);
        MaterialGenerator.generateOreMaterial(ORES.ZIRCON);
        MaterialGenerator.generateOreMaterial(ORES.POLYCRASE);
        MaterialGenerator.generateOreMaterial(ORES.ZIRCOPHYLLITE);
        MaterialGenerator.generateOreMaterial(ORES.ZIRKELITE);
        MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_LA);
        MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_CE);
        MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_ND);
        MaterialGenerator.generateOreMaterial(ORES.AGARDITE_Y);
        MaterialGenerator.generateOreMaterial(ORES.AGARDITE_CD);
        MaterialGenerator.generateOreMaterial(ORES.AGARDITE_LA);
        MaterialGenerator.generateOreMaterial(ORES.AGARDITE_ND);
        MaterialGenerator.generateOreMaterial(ORES.HIBONITE);
        MaterialGenerator.generateOreMaterial(ORES.CERITE);
        MaterialGenerator.generateOreMaterial(ORES.FLUORCAPHITE);
        MaterialGenerator.generateOreMaterial(ORES.FLORENCITE);
        MaterialGenerator.generateOreMaterial(ORES.CRYOLITE);

        MaterialGenerator.generateOreMaterial(ORES.LAUTARITE);
        MaterialGenerator.generateOreMaterial(ORES.LAFOSSAITE);
        MaterialGenerator.generateOreMaterial(ORES.DEMICHELEITE_BR);
        MaterialGenerator.generateOreMaterial(ORES.COMANCHEITE);
        MaterialGenerator.generateOreMaterial(ORES.PERROUDITE);
        MaterialGenerator.generateOreMaterial(ORES.HONEAITE);
        MaterialGenerator.generateOreMaterial(ORES.ALBURNITE);
        MaterialGenerator.generateOreMaterial(ORES.MIESSIITE);
        MaterialGenerator.generateOreMaterial(ORES.KASHINITE);
        MaterialGenerator.generateOreMaterial(ORES.IRARSITE);
        MaterialGenerator.generateOreMaterial(ORES.GREENOCKITE);
        MaterialGenerator.generateOreMaterial(ORES.RADIOBARITE);
        MaterialGenerator.generateOreMaterial(ORES.DEEP_EARTH_REACTOR_FUEL_DEPOSIT);

    }

    protected synchronized void setVars(FMLPreInitializationEvent event) {
        // Init WorldGen config.
        HANDLER_GT.sCustomWorldgenFile = new GTPP_Config(
            new Configuration(
                new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));

        if (DimensionManager.isDimensionRegistered(Dimension_Everglades.DIMID)) {
            Dimension_Everglades.DIMID = DimensionManager.getNextFreeDimId();
        }

        /*
         * Set World Generation Values
         */
        WorldGen_Ores.generateValidOreVeins();
        WorldGen_GT_Base.oreveinPercentage = 64;
        WorldGen_GT_Base.oreveinAttempts = 16;
        WorldGen_GT_Base.oreveinMaxPlacementAttempts = 4;
        if (CORE_Preloader.DEBUG_MODE || CORE.DEVENV) {
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
        GT_TileEntity_VoidMiner_Base.addBlockToDimensionList(CORE.EVERGLADES_ID, block, meta, weight);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        getEvergladesBiome().serverLoad(event);
    }

    @EventHandler
    public static void postInit(final FMLPostInitializationEvent e) {
        Logger.INFO("Finished loading Everglades plugin for GT++.");
    }

    public static synchronized Biome_Everglades getEvergladesBiome() {
        return Everglades_Biome;
    }

    public static synchronized void setEvergladesBiome(Biome_Everglades darkWorld_Biome) {
        Everglades_Biome = darkWorld_Biome;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
}
