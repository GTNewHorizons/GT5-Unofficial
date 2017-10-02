package gtPlusPlus.xmod.forestry;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.blocks.BlockRegistry;
import forestry.core.items.ItemBlockForestry;
import forestry.core.proxy.Proxies;
import forestry.core.utils.StringUtil;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.forestry.bees.blocks.BlockDenseBeeHouse;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class HANDLER_FR extends BlockRegistry {

	public static BlockDenseBeeHouse apiculture;

	public static void preInit(){
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
			if (CORE.configSwitches.enableCustomAlvearyBlocks){
				//AlvearyHandler.run();
			}
		}
	}

	public static void Init(){
		if (LoadedMods.Forestry){
			apiculture = new BlockDenseBeeHouse();
			apiculture.setBlockName("gtpp." + "beehouse");
			GameRegistry.registerBlock(apiculture, ItemBlockForestry.class, StringUtil.cleanBlockName(apiculture));
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
		Proxies.common.addBlockDestroyEffects(world, x, y, z, block, 0);
	}


}
