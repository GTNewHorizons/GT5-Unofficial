package gtPlusPlus.xmod.forestry.trees;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TreefarmManager {

	public static boolean isHumusLoaded = false;
	public static boolean isForestryLogsLoaded = false;
	public static boolean isForestryFenceLoaded = false;
	public static boolean isForestrySaplingsLoaded = false;
	public static boolean isForestryLeavesLoaded = false;
	public static Block blockHumus;

	public static boolean isForestryValid(){
		if (!LoadedMods.Forestry){
			return false;
		}
		if (ReflectionUtils.doesClassExist("forestry.core.blocks.BlockSoil")){
			isHumusLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockLog")){
			isForestryLogsLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockArbFence")){
			isForestryFenceLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockSapling")){
			isForestrySaplingsLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockForestryLeaves")){
			isForestryLeavesLoaded = true;
		}
		return true;
	}

	@Optional.Method(modid = "Forestry")
	public static Block getHumus(){
		if(blockHumus != null){
			return blockHumus;
		}
		else if (ReflectionUtils.doesClassExist("forestry.core.blocks.BlockSoil")){
			try {
				final Class<?> humusClass = Class.forName("forestry.core.blocks.BlockSoil");
				final ItemStack humusStack = ItemUtils.getCorrectStacktype("Forestry:soil", 1);
				if (humusClass != null){
					blockHumus = Block.getBlockFromItem(humusStack.getItem());
					return Block.getBlockFromItem(humusStack.getItem());
				}
			} catch (final ClassNotFoundException e) {}
		}
		return null;
	}

	public static boolean isWoodLog(final Block log){
		final String tTool = log.getHarvestTool(0);

		if ((log == Blocks.log) || (log == Blocks.log2)){
			return true;
		}

		//Forestry/General Compat
		if (log.getClass().getName().toLowerCase().contains("blocklog")){
			return true;
		}

		//IC2 Rubber Tree Compat
		if (log.getClass().getName().toLowerCase().contains("rubwood") || log.getClass().getName().toLowerCase().contains("rubleaves")){
			return true;
		}

		return  (OrePrefixes.log.contains(new ItemStack(log, 1))&& ((tTool != null) && (tTool.equals("axe")))) || (log.getMaterial() != Material.wood) ? false : (OrePrefixes.fence.contains(new ItemStack(log, 1)) ? false : true);
	}

	public static boolean isLeaves(final Block log){
		if (log.getUnlocalizedName().toLowerCase().contains("leaf")){
			return true;
		}
		if (log.getUnlocalizedName().toLowerCase().contains("leaves")){
			return true;
		}
		if (log.getLocalizedName().toLowerCase().contains("leaf")){
			return true;
		}
		if (log.getLocalizedName().toLowerCase().contains("leaves")){
			return true;
		}
		return  OrePrefixes.leaves.contains(new ItemStack(log, 1)) || (log.getMaterial() == Material.leaves);
	}

	public static boolean isSapling(final Block log){
		if (log != null){
			if (OrePrefixes.sapling.contains(new ItemStack(log, 1))){
				Utils.LOG_INFO(""+log.getLocalizedName());
			}
			if (log.getLocalizedName().toLowerCase().contains("sapling")){
				Utils.LOG_INFO(""+log.getLocalizedName());
				return true;
			}
		}
		return  OrePrefixes.sapling.contains(new ItemStack(log, 1));
	}

	public static boolean isDirtBlock(final Block dirt){
		return  (dirt == Blocks.dirt ? true : (dirt == Blocks.grass ? true : (getHumus() == null ? false : (dirt == blockHumus ? true : false))));
	}

	public static boolean isFenceBlock(final Block fence){
		return  (fence == Blocks.fence ? true : (fence == Blocks.fence_gate ? true : (fence == Blocks.nether_brick_fence ? true : (OrePrefixes.fence.contains(new ItemStack(fence, 1)) ? true : false))));
	}

	public static boolean isAirBlock(final Block air){

		if (air.getLocalizedName().toLowerCase().contains("air")){
			return true;
		}

		if (air.getClass().getName().toLowerCase().contains("residual") || air.getClass().getName().toLowerCase().contains("heat")){
			return true;
		}

		//Utils.LOG_INFO("Found "+air.getLocalizedName());

		return (air == Blocks.air ? true : (air instanceof BlockAir ? true : false));
	}

	/*public static boolean isSaplingBlock(Block sapling){
		return (sapling == Blocks.sapling ? true : (sapling == Blocks.))
	}*/

}
