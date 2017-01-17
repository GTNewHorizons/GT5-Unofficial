package powercrystals.minefactoryreloaded.farmables.plantables;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

/*
 * Used for directly placing blocks (ie saplings) and items (ie sugarcane). Pass in source ID to constructor,
 * so one instance per source ID.
 */

public class PlantableStandard implements IFactoryPlantable
{
	public static final int WILDCARD = OreDictionary.WILDCARD_VALUE;

	protected Item _seed;
	protected Block _block;
	protected ReplacementBlock _plantedBlock;
	protected int _validMeta;

	public PlantableStandard(Block block)
	{
		this(Item.getItemFromBlock(block), block);
	}

	public PlantableStandard(Block block, Block plantedBlock)
	{
		this(Item.getItemFromBlock(block), plantedBlock);
	}

	public PlantableStandard(Item block, Block plantedBlock)
	{
		this(block, plantedBlock, WILDCARD);
	}

	public PlantableStandard(Block block, int meta)
	{
		this(Item.getItemFromBlock(block), block, meta);
	}

	public PlantableStandard(Block block, Block plantedBlock, int meta)
	{
		this(Item.getItemFromBlock(block), plantedBlock, meta);
	}

	public PlantableStandard(Item block, Block plantedBlock, int validMeta)
	{
		this(block, plantedBlock, validMeta, new ReplacementBlock(plantedBlock));
	}

	public PlantableStandard(Item block, Block plantedBlock, int validMeta, int plantedMeta)
	{
		this(block, plantedBlock, validMeta, new ReplacementBlock(plantedBlock).setMeta(plantedMeta));
	}

	public PlantableStandard(Item block, Block plantedBlock, int validMeta, boolean useItemMeta)
	{
		this(block, plantedBlock, validMeta, new ReplacementBlock(plantedBlock).setMeta(useItemMeta));
	}

	public PlantableStandard(Item block, Block plantedBlock, int validMeta, ReplacementBlock repl)
	{
		_seed = block;
		_block = plantedBlock;
		_validMeta = validMeta;
		_plantedBlock = repl;
	}

	@Override
	public boolean canBePlanted(ItemStack stack, boolean forFermenting)
	{
		return _validMeta == WILDCARD || stack.getItemDamage() == _validMeta;
	}

	@Override
	public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack)
	{
		if (!world.isAirBlock(x, y, z))
			return false;

		Block groundId = world.getBlock(x, y - 1, z);
		return (_block.canPlaceBlockAt(world, x, y, z) && _block.canReplace(world, x, y, z, 0, stack)) ||
				(_block instanceof IPlantable && groundId != null &&
				groundId.canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable)_block));
	}

	@Override
	public void prePlant(World world, int x, int y, int z, ItemStack stack)
	{
		return;
	}

	@Override
	public void postPlant(World world, int x, int y, int z, ItemStack stack)
	{
		return;
	}

	@Override
	public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack)
	{
		return _plantedBlock;
	}

	@Override
	public Item getSeed()
	{
		return _seed;
	}
}
