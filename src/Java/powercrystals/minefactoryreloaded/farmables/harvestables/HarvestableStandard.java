package powercrystals.minefactoryreloaded.farmables.harvestables;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

public class HarvestableStandard implements IFactoryHarvestable
{
	private Block _block;
	private HarvestType _harvestType;

	public HarvestableStandard(Block block, HarvestType harvestType)
	{
		if (block == Blocks.air)
			throw new IllegalArgumentException("Passed air FactoryHarvestableStandard");

		_block = block;
		_harvestType = harvestType;
	}

	public HarvestableStandard(Block block)
	{
		this(block, HarvestType.Normal);
	}

	@Override
	public Block getPlant()
	{
		return _block;
	}

	@Override
	public HarvestType getHarvestType()
	{
		return _harvestType;
	}

	@Override
	public boolean breakBlock()
	{
		return true;
	}

	@Override
	public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		return true;
	}

	@Override
	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		return world.getBlock(x, y, z).getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	}

	@Override
	public void preHarvest(World world, int x, int y, int z)
	{
	}

	@Override
	public void postHarvest(World world, int x, int y, int z)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, getPlant());
	}
}
