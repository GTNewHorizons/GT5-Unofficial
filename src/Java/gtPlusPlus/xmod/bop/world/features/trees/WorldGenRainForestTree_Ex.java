package gtPlusPlus.xmod.bop.world.features.trees;

import java.util.Random;

import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenRainForestTree_Ex extends WorldGenAbstractTree{

	private final int minTreeHeight;
	private final int randomTreeHeight;
	private final Block wood;
	private final Block leaves;
	private final int woodMeta;
	private final int leavesMeta;

	//  return random.nextInt(5) == 0 ? new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true) : new WorldGenRainforestTree1(BOPCBlocks.logs4, BOPCBlocks.colorizedLeaves2, 3, 2, false, 8, 8);

	public WorldGenRainForestTree_Ex(Block wood, Block leaves, int woodMeta, int leavesMeta, boolean doBlockNotify, int minTreeHeight, int randomTreeHeight) {
		super(doBlockNotify);
		this.wood = wood;
		this.leaves = leaves;
		this.woodMeta = woodMeta;
		this.leavesMeta = leavesMeta;
		this.minTreeHeight = minTreeHeight;
		this.randomTreeHeight = randomTreeHeight;
	}	

	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		int treeHeight = random.nextInt(this.randomTreeHeight) + this.minTreeHeight;
		boolean flag = true;
		if ((y >= 1) && (y + treeHeight + 1 <= 256))
		{
			for (int i1 = y; i1 <= y + 1 + treeHeight; i1++)
			{
				byte b0 = 1;
				if (i1 == y) {
					b0 = 0;
				}
				if (i1 >= y + 1 + treeHeight - 2) {
					b0 = 2;
				}
				for (int j1 = x - b0; (j1 <= x + b0) && (flag); j1++) {
					for (int k1 = z - b0; (k1 <= z + b0) && (flag); k1++) {
						if ((i1 >= 0) && (i1 < 256))
						{
							Block block = world.getBlock(j1, i1, k1);
							if (!isReplaceable(world, j1, i1, k1)) {
								flag = false;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}
			if (!flag) {
				return false;
			}
			Block block2 = world.getBlock(x, y - 1, z);

			boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
			if ((isSoil) && (y < 256 - treeHeight - 1))
			{
				block2.onPlantGrow(world, x, y - 1, z, x, y, z);
				byte b0 = 3;
				byte b1 = 0;
				Utils.LOG_WARNING("Sapling located at X:"+x+" | Y:"+y+" | Z:"+z);
				for (int k1 = y - b0 + treeHeight; k1 <= y + treeHeight; k1++)
				{
					Utils.LOG_WARNING("[1]| k1 = "+k1+" | y = "+y+" | b0 = "+b0+" | l = "+treeHeight+" | ");
					int i3 = k1 - (y + treeHeight);
					int l1 = b1 + 1 - i3;
					for (int i2 = x - l1; i2 <= x + l1; i2++){
						int j2 = i2 - x;
						Utils.LOG_WARNING("[2]| i2 = "+i2+" | x = "+x+" | l1 = "+l1+" | j2 = "+j2+" | ");
						for (int k2 = z - l1; k2 <= z + l1; k2++){
							int l2 = k2 - z;
							Utils.LOG_WARNING("[3]| k2 = "+k2+" | z = "+z+" | l1 = "+l1+" | l2 = "+l2+" | ");
							if ((Math.abs(j2) != l1) || (Math.abs(l2) != l1) || ((random.nextInt(2) != 0) && (i3 != 0)))
							{
								Block block1 = world.getBlock(i2, k1, k2);
								if ((block1.isAir(world, i2, k1, k2)) || (block1.isLeaves(world, i2, k1, k2))) {
									Utils.LOG_WARNING("Setting something as leaves?");
									setBlockAndNotifyAdequately(world, i2, k1, k2, this.leaves, this.leavesMeta);
								}
							}
						}
					}
				}
				for (int k1 = 0; k1 < treeHeight; k1++)
				{
					Utils.LOG_WARNING("Building tree - Layer "+k1+" | Max height is "+treeHeight);
					Block block = world.getBlock(x, y + k1, z);
					if ((block.isAir(world, x, y + k1, z)) || (block.isLeaves(world, x, y + k1, z))){
						setBlockAndNotifyAdequately(world, x, y + k1, z, this.wood, this.woodMeta);

						//Trying this to make trunk
						setBlockAndNotifyAdequately(world, x, y + k1, z+1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + k1, z-1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x+1, y + k1, z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x-1, y + k1, z, this.wood, this.woodMeta);


						if (k1 >= 5){
							if (k1 % 5 == 0){								
								//Bottom layer
								Block block5 = world.getBlock(x, y + k1, z);
								for (int xDir = -5; xDir <= 5; xDir++){
									for (int zDir = -5; zDir <= 5; zDir++){
										if (canRemoveLeafBlockLayer2(-5, 5, xDir, -5, 5, zDir)){
											Utils.LOG_WARNING("Doing Nothing at "+"x="+xDir+" | y="+y+" z="+zDir);
										}
										else {
											Utils.LOG_WARNING("Trying to place leaves at "+"x="+xDir+" | y="+y+" z="+zDir);
											block5 = world.getBlock(x+xDir, y+k1, z+zDir);
											if (block5.isAir(world, x+xDir, y+k1, x+zDir)){
												setBlockAndNotifyAdequately(world, x+xDir, y + k1, z+zDir, this.leaves, this.leavesMeta);
											}	
										}										
									}
								}
								//Next Layer
								for (int xDir = -4; xDir <= 4; xDir++){
									for (int zDir = -4; zDir <= 4; zDir++){
										if (canRemoveLeafBlockLayer2(-4, 4, xDir, -4, 4, zDir)){
											Utils.LOG_WARNING("Doing Nothing at "+"x="+xDir+" | y="+y+k1+1+" z="+zDir);
										}
										else {
											Utils.LOG_WARNING("Trying to place leaves at "+"x="+xDir+" | y="+y + k1 + 1+" z="+zDir);
											block5 = world.getBlock(x+xDir, y+k1 + 1, z+zDir);
											if (block5.isAir(world, x+xDir, y+k1 + 1, x+zDir)){
												setBlockAndNotifyAdequately(world, x+xDir, y + k1 + 1, z+zDir, this.leaves, this.leavesMeta);
											}	
										}	
									}
								}

								//Third Layer
								for (int xDir = -3; xDir <= 3; xDir++){
									for (int zDir = -3; zDir <= 3; zDir++){
										if (canRemoveLeafBlockLayer2(-3, 3, xDir, -3, 3, zDir)){
											Utils.LOG_WARNING("Doing Nothing at "+"x="+xDir+" | y="+y+k1+2+" z="+zDir);
										}
										else {
											Utils.LOG_WARNING("Trying to place leaves at "+"x="+xDir+" | y="+y+k1+2+" z="+zDir);
											block5 = world.getBlock(x+xDir, y+k1+2, z+zDir);
											if (block5.isAir(world, x+xDir, y+k1+2, x+zDir)){
												setBlockAndNotifyAdequately(world, x+xDir, y + k1 + 2, z+zDir, this.leaves, this.leavesMeta);
											}	
										}	
									}
								}

								//Fourth Layer
								for (int xDir = -2; xDir <= 2; xDir++){
									for (int zDir = -2; zDir <= 2; zDir++){
										if (canRemoveLeafBlockLayer2(-2, 2, xDir, -2, 2, zDir)){
											Utils.LOG_WARNING("Doing Nothing at "+"x="+xDir+" | y="+y+k1+3+" z="+zDir);
										}
										else {
											Utils.LOG_WARNING("Trying to place leaves at "+"x="+xDir+" | y="+y+k1+3+" z="+zDir);
											block5 = world.getBlock(x+xDir, y+k1+3, z+zDir);
											if (block5.isAir(world, x+xDir, y+k1+3, x+zDir)){
												setBlockAndNotifyAdequately(world, x+xDir, y + k1 + 3, z+zDir, this.leaves, this.leavesMeta);
											}	
										}	
									}
								}
							}
							else {

							}
						}				

						setBlockAndNotifyAdequately(world, x - 3, y + (treeHeight - 3), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x + 3, y + (treeHeight - 3), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 3), z - 3, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 3), z + 3, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x - 2, y + (treeHeight - 4), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x + 2, y + (treeHeight - 4), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 4), z - 2, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 4), z + 2, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x - 2, y + (treeHeight - 5), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x + 2, y + (treeHeight - 5), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 5), z - 2, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 5), z + 2, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x - 1, y + (treeHeight - 6), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x + 1, y + (treeHeight - 6), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 6), z - 1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 6), z + 1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 3), z, this.leaves, this.leavesMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 2), z, this.leaves, this.leavesMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 1), z, this.leaves, this.leavesMeta);
						setBlockAndNotifyAdequately(world, x, y + treeHeight, z, this.leaves, this.leavesMeta);
						func_150515_a(world, x, y + (treeHeight - 4), z, Blocks.air);
						func_150515_a(world, x, y + (treeHeight - 5), z, Blocks.air);
						setBlockAndNotifyAdequately(world, x - 1, y + (treeHeight - 3), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x + 1, y + (treeHeight - 3), z, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 3), z - 1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 3), z + 1, this.wood, this.woodMeta);
						setBlockAndNotifyAdequately(world, x, y + (treeHeight - 2), z, this.wood, this.woodMeta);
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean canRemoveLeafBlockLayer2(int xDirMin, int xDirMax, int xDir, int zDirMin, int zDirMax, int zDir){
		if (xDir == xDirMin || xDir == xDirMin +1 || xDir == xDirMax || xDir == xDirMax - 1){
			if (zDir == zDirMin || zDir == zDirMin + 1 || zDir == zDirMax || zDir == zDirMax - 1){
				if (xDir == xDirMin + 1 && zDir == zDirMin + 1){
					return false;
				}
				else if (xDir == xDirMin + 1 && zDir == zDirMax - 1){
					return false;
				}
				else if (xDir == xDirMax - 1 && zDir == zDirMin + 1){
					return false;
				}
				else if (xDir == xDirMax - 1 && zDir == zDirMax - 1){
					return false;
				}
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}

}
