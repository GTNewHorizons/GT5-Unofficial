package gtPlusPlus.xmod.forestry.trees;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.*;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableTreeLeaves;
import forestry.api.arboriculture.*;
import forestry.api.genetics.*;


public class ForestryLeaf extends HarvestableTreeLeaves implements IFactoryFruit
{
	private ITreeRoot root;
	private ReplacementBlock repl;
	protected Item _item;

	public ForestryLeaf(Block block)
	{
		super(block);
		root = (ITreeRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
		repl = EmptyReplacement.INSTANCE;
		_item = Item.getItemFromBlock(block);
	}

	@Override
	public boolean canBePicked(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFruitBearer)
		{
			IFruitBearer fruit = (IFruitBearer)te;
			return fruit.getRipeness() >= 0.99f;
		}
		return false;
	}

	public boolean canFertilize(World world, int x, int y, int z)
	{
		return !canBePicked(world, x, y, z);
	}

	public boolean fertilize(World world, Random rand, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFruitBearer)
		{
			IFruitBearer fruit = (IFruitBearer)te;
			fruit.addRipeness(1f);
			return true;
		}
		return false;
	}

	@Override
	public ReplacementBlock getReplacementBlock(World world, int x, int y, int z)
	{
		return repl;
	}

	@Override
	public void prePick(World world, int x, int y, int z)
	{
	}

	@Override // HARVESTER
	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> settings, int x, int y, int z)
	{
		ITree tree = getTree(world, x, y, z);
		if (tree == null)
			return null;

		ArrayList<ItemStack> prod = new ArrayList<ItemStack>();

		float modifier = 1f;
		if (settings.get("silkTouch") == Boolean.TRUE)
		{
			ItemStack item = new ItemStack(_item);
			NBTTagCompound tag = new NBTTagCompound();
			tree.writeToNBT(tag);
			item.setTagCompound(tag);
			prod.add(item);
		}
		else
		{
			boolean hasMate = tree.getMate() != null;
			for (ITree s : getSaplings(tree, world, x, y, z, modifier))
				if (s != null) {
					if ((hasMate && !s.isGeneticEqual(tree)) || rand.nextInt(32) == 0)
						if (rand.nextBoolean())
							prod.add(root.getMemberStack(s, EnumGermlingType.POLLEN.ordinal()));

					prod.add(root.getMemberStack(s, EnumGermlingType.SAPLING.ordinal()));
				}

			getFruits(world, x, y, z, tree, prod);
		}

		return prod;
	}

	
	private static ITree[] getSaplings(ITree tree, World world, int x, int y, int z, float modifier) {
		return tree.getSaplings(world, null, x, y, z, modifier);
	}

	@Override // FRUIT PICKER
	public List<ItemStack> getDrops(World world, Random rand, int x, int y, int z)
	{
		ITree tree = getTree(world, x, y, z);
		if (tree == null)
			return null;

		ArrayList<ItemStack> prod = new ArrayList<ItemStack>();
		getFruits(world, x, y, z, tree, prod);
		return prod;
	}

	private ITree getTree(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPollinatable) {
			IIndividual t = ((IPollinatable)te).getPollen();
			if (t instanceof ITree)
				return (ITree)t;
		}
		return null;
	}

	private void getFruits(World world, int x, int y, int z, ITree tree, ArrayList<ItemStack> prod)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFruitBearer)
		{
			IFruitBearer fruit = (IFruitBearer)te;
			if (fruit.hasFruit())
			{
				//int period = tree.getGenome().getFruitProvider().getRipeningPeriod();
				//ItemStack[] o = tree.produceStacks(world, x, y, z, (int)(fruit.getRipeness() * period + 0.1f));
				prod.addAll(fruit.pickFruit(null));
			}
		}
	}

	@Override
	public void postPick(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFruitBearer)
		{
			IFruitBearer fruit = (IFruitBearer)te;
			fruit.addRipeness(-fruit.getRipeness());
		}
	}
}
