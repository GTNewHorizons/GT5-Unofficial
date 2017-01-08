package gtPlusPlus.xmod.forestry.trees;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Optional;

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
			Class<?> humusClass = Class.forName("forestry.core.blocks.BlockSoil");
			ItemStack humusStack = ItemUtils.getCorrectStacktype("Forestry:soil", 1);
			if (humusClass != null){
				blockHumus = Block.getBlockFromItem(humusStack.getItem());
				return Block.getBlockFromItem(humusStack.getItem());
			}
		} catch (ClassNotFoundException e) {}
	}
	return null;
}
	
	public static boolean isWoodLog(Block log){
		String tTool = log.getHarvestTool(0);
		return  OrePrefixes.log.contains(new ItemStack(log, 1))&& ((tTool != null) && (tTool.equals("axe"))) || (log.getMaterial() == Material.wood);
	}
	
	public static boolean isLeaves(Block log){
		String tTool = log.getHarvestTool(0);
		return  OrePrefixes.leaves.contains(new ItemStack(log, 1)) || (log.getMaterial() == Material.leaves);
	}

	public static boolean isDirtBlock(Block dirt){    	
		return  (dirt == Blocks.dirt ? true : (dirt == Blocks.grass ? true : (getHumus() == null ? false : (dirt == blockHumus ? true : false))));
	}

	public static boolean isFenceBlock(Block fence){
		return  (fence == Blocks.fence ? true : (fence == Blocks.fence_gate ? true : (fence == Blocks.nether_brick_fence ? true : false)));		
	}
	
	public static boolean isAirBlock(Block air){
		
		if (air.getLocalizedName().toLowerCase().contains("air")){
			return true;
		}
		
		return (air == Blocks.air ? true : (air instanceof BlockAir ? true : false));
	}
	
	/*public static boolean isSaplingBlock(Block sapling){
		return (sapling == Blocks.sapling ? true : (sapling == Blocks.))
	}*/

}
