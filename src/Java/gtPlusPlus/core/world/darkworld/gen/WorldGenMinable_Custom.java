package gtPlusPlus.core.world.darkworld.gen;

import static gtPlusPlus.core.lib.CORE.PI;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import gtPlusPlus.api.objects.Logger;

public class WorldGenMinable_Custom extends WorldGenMinable
{
	/** The block to generate. */
	private Block oreToGenerate;
	/** The number of blocks to generate. */
	private int numberOfBlocks;
	/** The block to replace. */
	private Block blockToReplace;
	/** The meta of the block. */
	private int mineableBlockMeta;

	public WorldGenMinable_Custom(final Block block, final int count){
		super(block, count, Blocks.stone);
	}

	public WorldGenMinable_Custom(final Block block, final int count, final Block target){
		super(block, count, target);
		this.oreToGenerate = block;
		this.numberOfBlocks = count;
		this.blockToReplace = target;
	}

	public WorldGenMinable_Custom(final Block block, final int meta, final int number, final Block target){
		this(block, number, target);
		this.mineableBlockMeta = meta;
	}

	@Override
	public boolean generate(final World world, final Random rand, final int x, final int y, final int z)
	{
		final float f = rand.nextFloat() * PI;
		final double d0 = x + 16 + ((MathHelper.sin(f) * this.numberOfBlocks) / 4.0F);
		final double d1 = (x + 16) - ((MathHelper.sin(f) * this.numberOfBlocks) / 4.0F);
		final double d2 = z + 16 + ((MathHelper.cos(f) * this.numberOfBlocks) / 4.0F);
		final double d3 = (z + 16) - ((MathHelper.cos(f) * this.numberOfBlocks) / 4.0F);
		final double d4 = (y + rand.nextInt(8)) - 1;
		final double d5 = (y + rand.nextInt(8)) - 1;

		for (int l = 0; l <= this.numberOfBlocks; ++l)
		{
			final double d6 = d0 + (((d1 - d0) * l) / this.numberOfBlocks);
			final double d7 = d4 + (((d5 - d4) * l) / this.numberOfBlocks);
			final double d8 = d2 + (((d3 - d2) * l) / this.numberOfBlocks);
			final double d9 = (rand.nextDouble() * this.numberOfBlocks) / 8.0D;
			final double d10 = ((MathHelper.sin((l * PI) / this.numberOfBlocks) + 1.0F) * d9) + 1.0D;
			final double d11 = ((MathHelper.sin((l * PI) / this.numberOfBlocks) + 1.0F) * d9) + 1.0D;
			final int i1 = MathHelper.floor_double(d6 - (d10 / 2.0D));
			final int j1 = MathHelper.floor_double(d7 - (d11 / 2.0D));
			final int k1 = MathHelper.floor_double(d8 - (d10 / 2.0D));
			final int l1 = MathHelper.floor_double(d6 + (d10 / 2.0D));
			final int i2 = MathHelper.floor_double(d7 + (d11 / 2.0D));
			final int j2 = MathHelper.floor_double(d8 + (d10 / 2.0D));

			for (int k2 = i1; k2 <= l1; ++k2){
				final double d12 = ((k2 + 0.5D) - d6) / (d10 / 2.0D);
				if ((d12 * d12) < 1.0D){
					for (int l2 = j1; l2 <= i2; ++l2){
						final double d13 = ((l2 + 0.5D) - d7) / (d11 / 2.0D);
						if (((d12 * d12) + (d13 * d13)) < 1.0D){
							for (int i3 = k1; i3 <= j2; ++i3){
								final double d14 = ((i3 + 0.5D) - d8) / (d10 / 2.0D);
								if ((((d12 * d12) + (d13 * d13) + (d14 * d14)) < 1.0D) && world.getBlock(k2, l2, i3).isReplaceableOreGen(world, k2, l2, i3, this.blockToReplace))
								{
									world.setBlock(k2, l2, i3, this.oreToGenerate, this.mineableBlockMeta, 3);
									Logger.INFO("Generated a "+this.oreToGenerate.getLocalizedName()+" at x: "+k2+" | y: "+l2+" | z: "+i3);
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