package gtPlusPlus.core.world.darkworld;

import java.util.Random;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.world.darkworld.block.*;
import gtPlusPlus.core.world.darkworld.item.itemDarkWorldPortalTrigger;
import gtPlusPlus.core.world.darkworld.world.CustomWorldType;
import gtPlusPlus.core.world.darkworld.world.WorldProviderMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

@SuppressWarnings("unchecked")
public class Dimension_DarkWorld {

	public Object instance;
	public static int DIMID = 227;

	public static blockDarkWorldPortal portalBlock;
	public static itemDarkWorldPortalTrigger portalItem;
	public static Block blockTopLayer;
	public static Block blockSecondLayer = Blocks.dirt;
	public static Block blockMainFiller = Blocks.stone;
	public static Block blockSecondaryFiller;
	public static Block blockFluidLakes = ModBlocks.blockFluidSludge;

	public static Block blockPortalFrame;
	
    /** Default (1.1) world type. */
    //public static final WorldType TestType = (new CustomWorldType(12, "TEST", 0));

	static {

		DIMID = DimensionManager.getNextFreeDimId();
		portalBlock = new blockDarkWorldPortal();
		portalItem = (itemDarkWorldPortalTrigger) (new itemDarkWorldPortalTrigger().setUnlocalizedName("dimensionDarkWorld_trigger"));
		blockTopLayer = new blockDarkWorldGround();
		blockSecondLayer = new blockDarkWorldPollutedDirt();
		Blocks.fire.setFireInfo(blockTopLayer, 30, 20);
		blockPortalFrame = new blockDarkWorldPortalFrame();
		
		//Registration
		GameRegistry.registerBlock(portalBlock, "dimensionDarkWorld_portal");
		GameRegistry.registerBlock(blockPortalFrame, "blockDarkWorldPortalFrame");
		GameRegistry.registerBlock(blockTopLayer, "blockDarkWorldGround");
		GameRegistry.registerBlock(blockSecondLayer, "blockDarkWorldGround2");
		Item.itemRegistry.addObject(423, "dimensionDarkWorld_trigger", portalItem);
		
		//Use Oil for Things.
		if (LoadedMods.BuildCraft){
			//blockFluidLakes = 
		}
		
	}

	public Dimension_DarkWorld() {
	}

	public void load() {		
		DimensionManager.registerProviderType(DIMID, WorldProviderMod.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
		// GameRegistry.addSmelting(Items.record_11, new ItemStack(block),
		// 1.0f);

	}

	public void registerRenderers() {
		
	}

	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
		
	}

	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		
	}

	public int addFuel(ItemStack fuel) {
		return 0;
	}

	public void serverLoad(FMLServerStartingEvent event) {
		
	}

	public void preInit(FMLPreInitializationEvent event) {
		
	}

}
