package gtPlusPlus.xmod.forestry;

import cpw.mods.fml.common.Optional;
import forestry.core.blocks.BlockRegistry;
import forestry.core.proxy.Proxies;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class HANDLER_FR extends BlockRegistry {

	//public static BlockDenseBeeHouse apiculture;

	public static void preInit(){
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();			
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

	public static boolean createBlockBreakParticles(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			createBlockBreakParticles_INTERNAL(world, x, y, z, block);
		}
		return false;
	}

	@Optional.Method(modid = "Forestry")
	private static void createBlockBreakParticles_INTERNAL(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			forestry.core.proxy.ProxyCommon mCommonProxy = ReflectionUtils.getField("forestry.core.proxy.ProxyCommon", "common");
			if (mCommonProxy != null){
				mCommonProxy.addBlockDestroyEffects(world, x, y, z, block, 0);
			}
		}
		
	}


}
