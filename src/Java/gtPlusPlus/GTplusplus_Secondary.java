package gtPlusPlus;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.core.world.darkworld.biome.Biome_DarkWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

@ChildMod(parent = CORE.MODID, mod = @Mod(modid = "GT++DarkWorld",
name = "GT++ Dark World",
version = "The Version to play. ;)",
dependencies = "after:Miscutils;after:Gregtech",
customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class GTplusplus_Secondary implements IFuelHandler, IWorldGenerator{

	public static final String MODID2 = "GT++ Dark World";
	public static final String VERSION2 = "0.1";

	// Dark World Handler
	Biome_DarkWorld DarkWorld_Biome = new Biome_DarkWorld();
	Dimension_DarkWorld DarkWorld_Dimension = new Dimension_DarkWorld();
	public static int globalDarkWorldPortalSpawnTimer = 0;

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Utils.LOG_INFO("Begin resource allocation for " + MODID2 + " V" + VERSION2);
		try {
			initMod();
		} catch (final Throwable $) {
			final ModContainer This = FMLCommonHandler.instance().findContainerFor(this);
			LogManager.getLogger(This.getModId()).log(Level.ERROR, "There was a problem loading " + This.getName(), $);
		}

		//Load Dark World and Biome
		GameRegistry.registerFuelHandler(this);
		GameRegistry.registerWorldGenerator(this, 1);
		DarkWorld_Biome.load();
		DarkWorld_Dimension.load();

	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Utils.LOG_INFO("Loading " + MODID2 + " V" + VERSION2);

		// Load Dark World
		DarkWorld_Biome.instance = GTplusplus.instance;
		DarkWorld_Dimension.instance = GTplusplus.instance;
		DarkWorld_Biome.preInit(event);
		DarkWorld_Dimension.preInit(event);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		DarkWorld_Biome.serverLoad(event);
		DarkWorld_Dimension.serverLoad(event);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (DarkWorld_Biome.addFuel(fuel) != 0)
			return DarkWorld_Biome.addFuel(fuel);
		if (DarkWorld_Dimension.addFuel(fuel) != 0)
			return DarkWorld_Dimension.addFuel(fuel);
		return 0;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX = chunkX * 16;
		chunkZ = chunkZ * 16;
		if (world.provider.dimensionId == -1) {
			DarkWorld_Biome.generateNether(world, random, chunkX, chunkZ);
		}
		if (world.provider.dimensionId == 0) {
			DarkWorld_Biome.generateSurface(world, random, chunkX, chunkZ);
		}
		if (world.provider.dimensionId == -1) {
			DarkWorld_Dimension.generateNether(world, random, chunkX, chunkZ);
		}
		if (world.provider.dimensionId == 0) {

		}

	}

	private static void initMod() {

	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Finished loading Dark World plugin for GT++.");
	}

}
