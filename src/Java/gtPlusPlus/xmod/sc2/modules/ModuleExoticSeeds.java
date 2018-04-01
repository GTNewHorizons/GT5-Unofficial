package gtPlusPlus.xmod.sc2.modules;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import static net.minecraft.init.Blocks.farmland;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public class ModuleExoticSeeds extends ModuleAddon implements ICropModule {

	public ModuleExoticSeeds(MinecartModular cart) {
		super(cart);
	}

	private synchronized Block getBlockFromItemSeeds(ItemStack seed) {
		try {

			Item seedItem = seed.getItem();
			if (!(seedItem instanceof ItemSeeds)) return null;

			Block cropBlock = (Block) ReflectionUtils.getField(ItemSeeds.class, "field_150925_a").get(seedItem);

			return cropBlock;
		} catch (Throwable t) {

		}
		return null;
	}

	@Override
	public boolean isSeedValid(ItemStack seed) {
		return getBlockFromItemSeeds(seed) != null;
	}

	@Override
	public Block getCropFromSeed(ItemStack seed) {
		return getBlockFromItemSeeds(seed);
	}

	@Override
	public boolean isReadyToHarvest(int x, int y, int z) {
		World world = getCart().worldObj;
		Block b = world.getBlock(x, y, z);
		int m = world.getBlockMetadata(x, y, z);        

		//If Forestry is loaded, let's make this upgrade convert farmland to Humus.
		/*if (LoadedMods.Forestry) {
			Block mFarmLand = world.getBlock(x, y-1, z);
			if (mFarmLand == farmland) {
				Block h = tryGetHumus();
				if (h != farmland) {
					world.setBlock(x, y-1, z, h);
				}
			}
		}*/
		
		
		return b instanceof BlockCrops && m == 7;
	}

	
	
	/**
	 * Static Class & Block References for Forestry content. 
	 * Stops Forestry being a hard requirement for this feature without having to make @Optional annotations.
	 */
	
	private static Class<?> mForestryHumusBlockClass;
	private static Class<?> mForestryBlockRegistryCoreClass;
	private static Block mForestryHumusBlock;
	
	private synchronized Block tryGetHumus() {
		if (!LoadedMods.Forestry) {
			return farmland;
		}
		else {
			if (mForestryHumusBlockClass == null || mForestryHumusBlock == null) {
				try {
					mForestryHumusBlockClass = Class.forName("forestry.plugins.PluginCore");
					Field blocks = ReflectionUtils.getField(mForestryHumusBlockClass, "blocks");
					if (blocks != null) {
						Object blockRegistryCoreObject = blocks.get(null);
						mForestryBlockRegistryCoreClass = Class.forName("forestry.core.blocks.BlockRegistryCore");
						if (mForestryBlockRegistryCoreClass != null && blockRegistryCoreObject != null) {
							Field soil = ReflectionUtils.getField(mForestryBlockRegistryCoreClass, "soil");
							if (soil != null) {
								Block testHumus = (Block) soil.get(blockRegistryCoreObject);
								if (testHumus != null) {
									mForestryHumusBlock = testHumus;
								}
							}
						}        			    				
					}
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (mForestryHumusBlock != null) {
				return mForestryHumusBlock;
			}
		}
		return farmland;
	}

}