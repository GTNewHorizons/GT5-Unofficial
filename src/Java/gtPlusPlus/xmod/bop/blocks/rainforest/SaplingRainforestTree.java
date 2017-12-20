package gtPlusPlus.xmod.bop.blocks.rainforest;

import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.bop.blocks.base.SaplingBase;
import gtPlusPlus.xmod.bop.world.features.trees.WorldGenRainForestTree_Ex;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class SaplingRainforestTree extends SaplingBase {

	public SaplingRainforestTree(){
		super("Rainforest Oak Sapling", "rainforestoak", new String[]{"rainforest"});
	}

	@Override
	public void func_149878_d(World world, int x, int y, int z, Random rand){
		Logger.WARNING("func_149878_d - 1");
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
		int l = world.getBlockMetadata(x, y, z) & 7;
		rand.nextInt(10);
		new WorldGenBigTree(true);
		new WorldGenTrees(true);
		int i1 = 0;
		int j1 = 0;
		boolean flag = false;

		Block block = Blocks.air;

		if (flag)
		{
			world.setBlock(x + i1, y, z + j1, block, 0, 4);
			world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
			world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
			world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
		}
		else
		{
			world.setBlock(x, y, z, block, 0, 4);
		}
		Object o = new WorldGenRainForestTree_Ex(BOP_Block_Registrator.log_Rainforest, BOP_Block_Registrator.leaves_Rainforest, 0, 0, true, 50, 75);

		if (!((WorldGenerator)o).generate(world, rand, x + i1, y, z + j1))
		{
			if (flag)
			{
				world.setBlock(x + i1, y, z + j1, this, l, 4);
				world.setBlock(x + i1 + 1, y, z + j1, this, l, 4);
				world.setBlock(x + i1, y, z + j1 + 1, this, l, 4);
				world.setBlock(x + i1 + 1, y, z + j1 + 1, this, l, 4);
			}
			else
			{
				world.setBlock(x, y, z, this, l, 4);
			}
		}
	}

}