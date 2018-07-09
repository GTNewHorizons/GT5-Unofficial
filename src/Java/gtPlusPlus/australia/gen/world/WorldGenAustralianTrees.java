package gtPlusPlus.australia.gen.world;

import java.util.Random;

import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenAustralianTrees extends WorldGenTrees {
	
	/** The minimum height of a generated tree. */
	private final int minHeight;
	/** True if this tree should grow Vines. */
	private final boolean growVines;
	/** The metadata value of the wood to use in tree generation. */
	private final int woodMeta;
	/** The metadata value of the leaves to use in tree generation. */
	private final int leavesMeta;

	public WorldGenAustralianTrees(boolean p_i2027_1_) {
		this(p_i2027_1_, 4, false);
	}

	public WorldGenAustralianTrees(boolean p_i2028_1_, int aMinHeight, boolean aVinesGrow) {
		super(p_i2028_1_, aMinHeight, 0, 0, aVinesGrow);
		this.minHeight = aMinHeight;
		this.woodMeta = 0;
		this.leavesMeta = 0;
		this.growVines = aVinesGrow;
	}

	public boolean generate(World aWorld, Random aRand, int aX, int aY, int aZ) {
		int aActualMinHeight = MathUtils.randInt(4, 8) + this.minHeight;
		boolean flag = true;

		if (aY >= 1 && aY + aActualMinHeight + 1 <= 256) {
			byte b0;
			int k1;
			Block block;

			for (int i1 = aY; i1 <= aY + 1 + aActualMinHeight; ++i1) {
				b0 = 1;

				if (i1 == aY) {
					b0 = 0;
				}

				if (i1 >= aY + 1 + aActualMinHeight - 2) {
					b0 = 2;
				}

				for (int j1 = aX - b0; j1 <= aX + b0 && flag; ++j1) {
					for (k1 = aZ - b0; k1 <= aZ + b0 && flag; ++k1) {
						if (i1 >= 0 && i1 < 256) {
							block = aWorld.getBlock(j1, i1, k1);

							if (!this.isReplaceable(aWorld, j1, i1, k1)) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				Block block2 = aWorld.getBlock(aX, aY - 1, aZ);

				boolean isSoil = block2.canSustainPlant(aWorld, aX, aY - 1, aZ, ForgeDirection.UP,
						(BlockSapling) Blocks.sapling);
				if (isSoil && aY < 256 - aActualMinHeight - 1) {
					block2.onPlantGrow(aWorld, aX, aY - 1, aZ, aX, aY, aZ);
					b0 = 3;
					byte b1 = 0;
					int l1;
					int i2;
					int j2;
					int i3;

					for (k1 = aY - b0 + aActualMinHeight; k1 <= aY + aActualMinHeight; ++k1) {
						i3 = k1 - (aY + aActualMinHeight);
						l1 = b1 + 5 - i3 / 2;

						for (i2 = aX - l1; i2 <= aX + l1; ++i2) {
							j2 = i2 - aX;

							for (int k2 = aZ - l1; k2 <= aZ + l1; ++k2) {
								int l2 = k2 - aZ;

								if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || aRand.nextInt(2) != 0 && i3 != 0) {
									Block block1 = aWorld.getBlock(i2, k1, k2);

									if (block1.isAir(aWorld, i2, k1, k2) || block1.isLeaves(aWorld, i2, k1, k2)) {
										this.setBlockAndNotifyAdequately(aWorld, i2, k1, k2, Blocks.leaves,
												this.leavesMeta);
									}
								}
							}
						}
					}

					for (k1 = 0; k1 < aActualMinHeight; ++k1) {
						block = aWorld.getBlock(aX, aY + k1, aZ);

						if (block.isAir(aWorld, aX, aY + k1, aZ) || block.isLeaves(aWorld, aX, aY + k1, aZ)) {
							this.setBlockAndNotifyAdequately(aWorld, aX, aY + k1, aZ, Blocks.log, this.woodMeta);

							if (this.growVines && k1 > 0) {
								if (aRand.nextInt(3) > 0 && aWorld.isAirBlock(aX - 1, aY + k1, aZ)) {
									this.setBlockAndNotifyAdequately(aWorld, aX - 1, aY + k1, aZ, Blocks.vine, 8);
								}

								if (aRand.nextInt(3) > 0 && aWorld.isAirBlock(aX + 1, aY + k1, aZ)) {
									this.setBlockAndNotifyAdequately(aWorld, aX + 1, aY + k1, aZ, Blocks.vine, 2);
								}

								if (aRand.nextInt(3) > 0 && aWorld.isAirBlock(aX, aY + k1, aZ - 1)) {
									this.setBlockAndNotifyAdequately(aWorld, aX, aY + k1, aZ - 1, Blocks.vine, 1);
								}

								if (aRand.nextInt(3) > 0 && aWorld.isAirBlock(aX, aY + k1, aZ + 1)) {
									this.setBlockAndNotifyAdequately(aWorld, aX, aY + k1, aZ + 1, Blocks.vine, 4);
								}
							}
						}
					}

					if (this.growVines) {
						for (k1 = aY - 3 + aActualMinHeight; k1 <= aY + aActualMinHeight; ++k1) {
							i3 = k1 - (aY + aActualMinHeight);
							l1 = 2 - i3 / 2;

							for (i2 = aX - l1; i2 <= aX + l1; ++i2) {
								for (j2 = aZ - l1; j2 <= aZ + l1; ++j2) {
									if (aWorld.getBlock(i2, k1, j2).isLeaves(aWorld, i2, k1, j2)) {
										if (aRand.nextInt(4) == 0
												&& aWorld.getBlock(i2 - 1, k1, j2).isAir(aWorld, i2 - 1, k1, j2)) {
											this.growVines(aWorld, i2 - 1, k1, j2, 8);
										}

										if (aRand.nextInt(4) == 0
												&& aWorld.getBlock(i2 + 1, k1, j2).isAir(aWorld, i2 + 1, k1, j2)) {
											this.growVines(aWorld, i2 + 1, k1, j2, 2);
										}

										if (aRand.nextInt(4) == 0
												&& aWorld.getBlock(i2, k1, j2 - 1).isAir(aWorld, i2, k1, j2 - 1)) {
											this.growVines(aWorld, i2, k1, j2 - 1, 1);
										}

										if (aRand.nextInt(4) == 0
												&& aWorld.getBlock(i2, k1, j2 + 1).isAir(aWorld, i2, k1, j2 + 1)) {
											this.growVines(aWorld, i2, k1, j2 + 1, 4);
										}
									}
								}
							}
						}

						if (aRand.nextInt(5) == 0 && aActualMinHeight > 5) {
							for (k1 = 0; k1 < 2; ++k1) {
								for (i3 = 0; i3 < 4; ++i3) {
									if (aRand.nextInt(4 - k1) == 0) {
										l1 = aRand.nextInt(3);
										this.setBlockAndNotifyAdequately(aWorld,
												aX + Direction.offsetX[Direction.rotateOpposite[i3]], aY + aActualMinHeight - 5 + k1,
												aZ + Direction.offsetZ[Direction.rotateOpposite[i3]], Blocks.cocoa,
												l1 << 2 | i3);
									}
								}
							}
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * Grows vines downward from the given block for a given length. Args: World, x,
	 * starty, z, vine-length
	 */
	private void growVines(World p_76529_1_, int p_76529_2_, int p_76529_3_, int p_76529_4_, int p_76529_5_) {
		this.setBlockAndNotifyAdequately(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, Blocks.vine, p_76529_5_);
		int i1 = 4;

		while (true) {
			--p_76529_3_;

			if (!p_76529_1_.getBlock(p_76529_2_, p_76529_3_, p_76529_4_).isAir(p_76529_1_, p_76529_2_, p_76529_3_,
					p_76529_4_) || i1 <= 0) {
				return;
			}

			this.setBlockAndNotifyAdequately(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, Blocks.vine, p_76529_5_);
			--i1;
		}
	}
}