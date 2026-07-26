package toxiceverglades;

import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.GT_Version;
import gregtech.api.enums.Mods;
import toxiceverglades.biome.BiomeEverglades;
import toxiceverglades.block.DarkWorldContentLoader;
import toxiceverglades.dimension.DimensionEverglades;

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

        // Load World and Biome
        getEvergladesBiome().load();
        Everglades_Dimension.load();
    }

    protected synchronized void setVars(FMLPreInitializationEvent event) {
        if (DimensionManager.isDimensionRegistered(DimensionEverglades.DIMID)) {
            DimensionEverglades.DIMID = DimensionManager.getNextFreeDimId();
        }
        DarkWorldContentLoader.run();
    }

    public static synchronized BiomeEverglades getEvergladesBiome() {
        return Everglades_Biome;
    }

    public static synchronized void setEvergladesBiome(BiomeEverglades darkWorld_Biome) {
        Everglades_Biome = darkWorld_Biome;
    }
}
