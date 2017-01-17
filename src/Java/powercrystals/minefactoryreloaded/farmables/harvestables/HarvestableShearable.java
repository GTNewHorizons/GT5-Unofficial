package powercrystals.minefactoryreloaded.farmables.harvestables;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import powercrystals.minefactoryreloaded.api.HarvestType;

public class HarvestableShearable extends HarvestableStandard
{
	public HarvestableShearable(Block block, HarvestType harvestType)
	{
		super(block, harvestType);
	}

	public HarvestableShearable(Block block)
	{
		super(block);
	}

	@Override
	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> settings, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if (settings.get("silkTouch") == Boolean.TRUE)
		{
			if (block instanceof IShearable)
			{
				ItemStack stack = new ItemStack(Items.shears, 1, 0);
				if (((IShearable)block).isShearable(stack, world, x, y, z))
				{
					return ((IShearable)block).onSheared(stack, world, x, y, z, 0);
				}
			}
			if (Item.getItemFromBlock(block) != null)
			{
				ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
				int meta = block.getDamageValue(world, x, y, z);
				drops.add(new ItemStack(block, 1, meta));
				return drops;
			}
		}

		return block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	}
}
