package gtPlusPlus.xmod.bop.world.features.trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;

public class WorldGenPineTree extends WorldGenAbstractTree {

    public WorldGenPineTree() {
        super(true);
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        while (world.isAirBlock(x, y, z) && y > 2) {
            --y;
        }

        Block aSoilBlock = world.getBlock(x, y, z);

        if (!canBlockSustainSapling(world, aSoilBlock, x, y, z)) {
            return false;
        } else {
            int height;
            int branches;
            for (height = -2; height <= 2; ++height) {
                for (branches = -2; branches <= 2; ++branches) {
                    if (world.isAirBlock(x + height, y - 1, z + branches)
                        && world.isAirBlock(x + height, y - 2, z + branches)
                        && !world.isAirBlock(x + height, y, z + branches)) {
                        return false;
                    }
                }
            }

            height = 2 + MathUtils.randInt(8, 16);
            branches = 2 + MathUtils.randInt(6, 18);
            int h = 1;
            aSoilBlock.onPlantGrow(world, x, y - 1, z, x, y, z);

            int c;
            int r = MathUtils.randInt(1, 3);
            for (c = 0; c < height; ++c) {
                this.buildBlock(world, x, y + h, z, BOPBlockRegistrator.log_Pine, 0);
                h++;
                if (c >= r && c % 2 == 0) {
                    this.generateBranch(world, random, x, y + h, z, c);
                }
            }

            this.generateTop(world, x, y + h, z);
            return true;
        }
    }

    public void generateTop(World world, int x, int y, int z) {
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                this.buildBlock(world, x + i, y, z + j, BOPBlockRegistrator.leaves_Pine, 0);
            }
        }

        this.buildBlock(world, x, y, z, BOPBlockRegistrator.log_Pine, 0);
        this.buildBlock(world, x + 1, y + 1, z, BOPBlockRegistrator.leaves_Pine, 0);
        this.buildBlock(world, x, y + 1, z - 1, BOPBlockRegistrator.leaves_Pine, 0);
        this.buildBlock(world, x, y + 1, z + 1, BOPBlockRegistrator.leaves_Pine, 0);
        this.buildBlock(world, x - 1, y + 1, z, BOPBlockRegistrator.leaves_Pine, 0);
        this.buildBlock(world, x, y + 2, z, BOPBlockRegistrator.leaves_Pine, 0);
    }

    public void generateBranch(World world, Random rand, int x, int y, int z, int n) {
        int var99999;
        int var99998;
        for (var99999 = -1; var99999 < 2; ++var99999) {
            for (var99998 = -1; var99998 < 2; ++var99998) {
                this.buildBlock(world, x + var99999, y, z + var99998, BOPBlockRegistrator.leaves_Pine, 0);
            }
        }

        var99999 = rand.nextInt(2);
        var99998 = rand.nextInt(2);
        int var99997 = rand.nextInt(2);
        int var99996 = rand.nextInt(2);
        if (n % 2 == 0) {
            if (var99998 == 0) {
                this.buildBlock(world, x + 1, y - 1, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x + 2, y - 1, z - 1, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99999 == 0) {
                    this.buildBlock(world, x + 2, y - 2, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x + 2, y - 1, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            } else {
                this.buildBlock(world, x + 1, y, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x + 2, y, z - 1, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x + 2, y, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
            }

            if (var99997 == 0) {
                this.buildBlock(world, x - 2, y - 1, z + 1, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x - 1, y - 1, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99996 == 0) {
                    this.buildBlock(world, x - 2, y - 2, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x - 2, y - 1, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            } else {
                this.buildBlock(world, x - 2, y, z + 1, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x - 1, y, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x - 2, y, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
            }
        } else {
            if (var99998 == 0) {
                this.buildBlock(world, x + 2, y - 1, z + 1, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x + 1, y - 1, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99999 == 0) {
                    this.buildBlock(world, x + 2, y - 2, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x + 2, y - 1, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            } else {
                this.buildBlock(world, x + 2, y, z + 1, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x + 1, y, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99999 == 0) {
                    this.buildBlock(world, x + 2, y - 1, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x + 2, y, z + 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            }

            if (var99997 == 0) {
                this.buildBlock(world, x - 1, y - 1, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x - 2, y - 1, z - 1, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99996 == 0) {
                    this.buildBlock(world, x - 2, y - 2, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x - 2, y - 1, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            } else {
                this.buildBlock(world, x - 1, y, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                this.buildBlock(world, x - 2, y, z - 1, BOPBlockRegistrator.leaves_Pine, 0);
                if (var99996 == 0) {
                    this.buildBlock(world, x - 2, y - 1, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                } else {
                    this.buildBlock(world, x - 2, y, z - 2, BOPBlockRegistrator.leaves_Pine, 0);
                }
            }
        }

        this.buildBlock(world, x, y, z, BOPBlockRegistrator.log_Pine, 0);
        this.buildBlock(world, x, y + 1, z, BOPBlockRegistrator.log_Pine, 0);
    }

    public void buildBlock(World world, int x, int y, int z, Block block, int meta) {
        if (world.isAirBlock(x, y, z) || world.getBlock(x, y, z)
            .isLeaves(world, x, y, z)) {
            world.setBlock(x, y, z, block, meta, 2);
        }
    }

    public boolean canBlockSustainSapling(World world, Block block, int x, int y, int z) {
        return block.canSustainPlant(world, x, y, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
    }
}
