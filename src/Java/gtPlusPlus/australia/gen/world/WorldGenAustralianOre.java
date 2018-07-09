package gtPlusPlus.australia.gen.world;

import java.util.Random;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenAustralianOre extends WorldGenerator {
	
	private Block mOreToSpawn;
	private int numberOfBlocks;
	private Block mBlockToReplace;
	private int mineableBlockMeta;

	public WorldGenAustralianOre(Block aBlock, int aNumber) {
		this(aBlock, aNumber, Blocks.stone);
		this.mineableBlockMeta = 0;
	}

	public WorldGenAustralianOre(Block aBlock, int aNumber, Block aReplace) {
		this.mOreToSpawn = aBlock;
		this.numberOfBlocks = aNumber;
		this.mBlockToReplace = aReplace;
	}

	public WorldGenAustralianOre(Block block, int meta, int number, Block target) {
		this(block, number, target);
		this.mineableBlockMeta = meta;
	}
	
	

	public boolean generate(World aWorld, Random aRand, int aX, int aY, int aZ) {
		float f = MathUtils.randFloat(0f, 1.5f) * CORE.PI;
		double d0 = (double) ((float) (aX + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
		double d1 = (double) ((float) (aX + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
		double d2 = (double) ((float) (aZ + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
		double d3 = (double) ((float) (aZ + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
		double d4 = (double) (aY + aRand.nextInt(MathUtils.randInt(3, 6)) - 2);
		double d5 = (double) (aY + aRand.nextInt(MathUtils.randInt(3, 6)) - 2);

		for (int aOreGenerated = 0; aOreGenerated <= this.numberOfBlocks; ++aOreGenerated) {
			double d6 = d0 + (d1 - d0) * (double) aOreGenerated / (double) this.numberOfBlocks;
			double d7 = d4 + (d5 - d4) * (double) aOreGenerated / (double) this.numberOfBlocks;
			double d8 = d2 + (d3 - d2) * (double) aOreGenerated / (double) this.numberOfBlocks;
			double d9 = aRand.nextDouble() * (double) this.numberOfBlocks / 16.0D;
			double d10 = (double) (MathHelper.sin((float) aOreGenerated * CORE.PI / (float) this.numberOfBlocks) + 1.0F)
					* d9 + 1.0D;
			double d11 = (double) (MathHelper.sin((float) aOreGenerated * CORE.PI / (float) this.numberOfBlocks) + 1.0F)
					* d9 + 1.0D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

			for (int k2 = i1; k2 <= l1; ++k2) {
				double d12 = ((double) k2 + 0.5D - d6) / (d10 / 2.0D);

				if (d12 * d12 < 1.0D) {
					for (int l2 = j1; l2 <= i2; ++l2) {
						double d13 = ((double) l2 + 0.5D - d7) / (d11 / 2.0D);

						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int i3 = k1; i3 <= j2; ++i3) {
								double d14 = ((double) i3 + 0.5D - d8) / (d10 / 2.0D);

								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && aWorld.getBlock(k2, l2, i3)
										.isReplaceableOreGen(aWorld, k2, l2, i3, mBlockToReplace)) {
									aWorld.setBlock(k2, l2, i3, this.mOreToSpawn, mineableBlockMeta, 2);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}