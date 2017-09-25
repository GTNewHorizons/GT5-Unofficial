package gtPlusPlus;

import java.io.File;
import java.util.Random;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.core.world.darkworld.biome.Biome_DarkWorld;
import gtPlusPlus.core.world.darkworld.block.blockDarkWorldGround;
import gtPlusPlus.core.world.darkworld.block.blockDarkWorldPollutedDirt;
import gtPlusPlus.core.world.darkworld.block.blockDarkWorldPortal;
import gtPlusPlus.core.world.darkworld.block.blockDarkWorldPortalFrame;
import gtPlusPlus.core.world.darkworld.item.itemDarkWorldPortalTrigger;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

@MCVersion(value = "1.7.10")
@ChildMod(parent = CORE.MODID, mod = @Mod(modid = "GT++DarkWorld",
name = "GT++ Dark World",
version = CORE.VERSION,
dependencies = "after:Miscutils;after:Gregtech",
customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class GTplusplus_Secondary implements IFuelHandler, IWorldGenerator{

	public static final String MODID2 = "GT++ Dark World";
	public static final String VERSION2 = "0.1";

	// Dark World Handler
	Biome_DarkWorld DarkWorld_Biome;
	Dimension_DarkWorld DarkWorld_Dimension;
	public static int globalDarkWorldPortalSpawnTimer = 0;

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Utils.LOG_INFO("Begin resource allocation for " + MODID2 + " V" + VERSION2);

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

		//Setup
		setVars();
		
		DarkWorld_Biome = new Biome_DarkWorld();
		DarkWorld_Dimension = new Dimension_DarkWorld();
		
		// Load Dark World
		DarkWorld_Biome.instance = GTplusplus.instance;
		DarkWorld_Dimension.instance = GTplusplus.instance;
		DarkWorld_Biome.preInit(event);
		DarkWorld_Dimension.preInit(event);
		
		//Load/Set Custom Ore Gen
		HANDLER_GT.sCustomWorldgenFile = new GTPP_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));
		
	}
	
	void setVars(){
		Dimension_DarkWorld.DIMID = DimensionManager.getNextFreeDimId();
		Dimension_DarkWorld.portalBlock = new blockDarkWorldPortal();
		Dimension_DarkWorld.portalItem = (itemDarkWorldPortalTrigger) (new itemDarkWorldPortalTrigger().setUnlocalizedName("dimensionDarkWorld_trigger"));
		Item.itemRegistry.addObject(423, "dimensionDarkWorld_trigger", Dimension_DarkWorld.portalItem);
		Dimension_DarkWorld.blockTopLayer = new blockDarkWorldGround();
		Dimension_DarkWorld.blockSecondLayer = new blockDarkWorldPollutedDirt();
		GameRegistry.registerBlock(Dimension_DarkWorld.blockTopLayer, "blockDarkWorldGround");
		GameRegistry.registerBlock(Dimension_DarkWorld.blockSecondLayer, "blockDarkWorldGround2");
		Blocks.fire.setFireInfo(Dimension_DarkWorld.blockTopLayer, 30, 20);
		Dimension_DarkWorld.blockPortalFrame = new blockDarkWorldPortalFrame();
		GameRegistry.registerBlock(Dimension_DarkWorld.blockPortalFrame, "blockDarkWorldPortalFrame");
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

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Finished loading Dark World plugin for GT++.");
	}

}
