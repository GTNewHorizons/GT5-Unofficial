package gtPlusPlus.xmod.forestry;

import forestry.core.proxy.Proxies;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.forestry.bees.alveary.AlvearyHandler;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import cpw.mods.fml.common.Optional;

public class HANDLER_FR {


	public static void preInit(){		
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			if (CORE.configSwitches.enableCustomAlvearyBlocks){
				AlvearyHandler.run();
			}
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
		
		}		
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();	
		}		
	}	
	
	public static boolean createBlockBreakParticles(World world, int x, int y, int z, Block block){
		if (LoadedMods.Forestry){
			createBlockBreakParticles_INTERNAL(world, x, y, z, block);
		}
		return false;
	}
	
	@Optional.Method(modid = "Forestry")
	private static void createBlockBreakParticles_INTERNAL(World world, int x, int y, int z, Block block){
		Proxies.common.addBlockDestroyEffects(world, x, y, z, block, 0);
	}
	
	
}
