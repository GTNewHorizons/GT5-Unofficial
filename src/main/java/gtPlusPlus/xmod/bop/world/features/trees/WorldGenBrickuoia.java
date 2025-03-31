package gtPlusPlus.xmod.bop.world.features.trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenBrickuoia extends WorldGenAbstractTree {

    private final int minTreeHeight;
    private final int randomTreeHeight;
    private final Block wood;
    private final Block leaves;
    private final int woodMeta;
    private final int leavesMeta;

    public WorldGenBrickuoia(Block wood, Block leaves, int woodMeta, int leavesMeta, boolean doBlockNotify,
        int minTreeHeight, int randomTreeHeight) {
        super(doBlockNotify);
        this.wood = wood;
        this.leaves = leaves;
        this.woodMeta = woodMeta;
        this.leavesMeta = leavesMeta;
        this.minTreeHeight = minTreeHeight;
        this.randomTreeHeight = randomTreeHeight;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int treeHeight = random.nextInt(this.randomTreeHeight) + this.minTreeHeight;
        boolean flag = true;
        if ((y >= 1) && (y + treeHeight + 1 <= 256)) {
            for (int i1 = y; i1 <= y + 1 + treeHeight; i1++) {
                byte b0 = 1;
                if (i1 == y) {
                    b0 = 0;
                }
                if (i1 >= y + 1 + treeHeight - 2) {
                    b0 = 2;
                }
                for (int j1 = x - b0; (j1 <= x + b0) && (flag); j1++) {
                    for (int k1 = z - b0; (k1 <= z + b0) && (flag); k1++) {
                        if ((i1 >= 0) && (i1 < 256)) {
                            Block block = world.getBlock(j1, i1, k1);
                            if (!isReplaceable(world, j1, i1, k1)) {
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
            }
            Block block2 = world.getBlock(x, y - 1, z);

            boolean isSoil = block2
                .canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
            if ((isSoil) && (y < 256 - treeHeight - 1)) {
                block2.onPlantGrow(world, x, y - 1, z, x, y, z);
                final byte v3 = 3;
                final byte v0 = 0;
                for (int bY = y - v3 + treeHeight; bY <= y + treeHeight; bY++) {
                    int i3 = bY - (y + treeHeight);
                    int l1 = v0 + 1 - i3;
                    for (int bX = x - l1; bX <= x + l1; bX++) {
                        int j2 = bX - x;
                        for (int bZ = z - l1; bZ <= z + l1; bZ++) {
                            int l2 = bZ - z;
                            if ((Math.abs(j2) != l1) || (Math.abs(l2) != l1)
                                || ((random.nextInt(2) != 0) && (i3 != 0))) {
                                Block block1 = world.getBlock(bX, bY, bZ);
                                if ((block1.isAir(world, bX, bY, bZ)) || (block1.isLeaves(world, bX, bY, bZ))) {
                                    setBlockAndNotifyAdequately(world, bX, bY, bZ, this.leaves, this.leavesMeta);
                                }
                            }
                        }
                    }
                }
                for (int layer = 0; layer < treeHeight; layer++) {
                    Block block = world.getBlock(x, y + layer, z);
                    if ((block.isAir(world, x, y + layer, z)) || (block.isLeaves(world, x, y + layer, z))) {
                        setBlockAndNotifyAdequately(world, x, y + layer, z, this.wood, this.woodMeta);

                        // Trying this to make trunk
                        for (int bX = -2; bX <= 2; bX++) {
                            for (int bZ = -2; bZ <= 2; bZ++) {
                                // exclude corners
                                if (Math.abs(bX) == 2 && Math.abs(bZ) == 2) {
                                    continue;
                                }
                                setBlockAndNotifyAdequately(world, x + bX, y + layer, z + bZ, this.wood, this.woodMeta);
                            }
                        }

                        if (layer >= 5) {
                            if (layer % 5 == 0) {
                                // Bottom layer
                                Block block5 = world.getBlock(x, y + layer, z);
                                for (int xDir = -5; xDir <= 5; xDir++) {
                                    for (int zDir = -5; zDir <= 5; zDir++) {
                                        if (skipLeafPlacement(-5, 5, xDir, -5, 5, zDir)) {
                                            // noop
                                        } else {
                                            block5 = world.getBlock(x + xDir, y + layer, z + zDir);
                                            if (block5.isAir(world, x + xDir, y + layer, x + zDir)) {
                                                setBlockAndNotifyAdequately(
                                                    world,
                                                    x + xDir,
                                                    y + layer,
                                                    z + zDir,
                                                    this.leaves,
                                                    this.leavesMeta);
                                            }
                                        }
                                    }
                                }
                                // Next Layer
                                for (int xDir = -4; xDir <= 4; xDir++) {
                                    for (int zDir = -4; zDir <= 4; zDir++) {
                                        if (skipLeafPlacement(-4, 4, xDir, -4, 4, zDir)) {
                                            // noop
                                        } else {

                                            block5 = world.getBlock(x + xDir, y + layer + 1, z + zDir);
                                            if (block5.isAir(world, x + xDir, y + layer + 1, x + zDir)) {
                                                setBlockAndNotifyAdequately(
                                                    world,
                                                    x + xDir,
                                                    y + layer + 1,
                                                    z + zDir,
                                                    this.leaves,
                                                    this.leavesMeta);
                                            }
                                        }
                                    }
                                }

                                // Third Layer
                                for (int xDir = -3; xDir <= 3; xDir++) {
                                    for (int zDir = -3; zDir <= 3; zDir++) {
                                        if (skipLeafPlacement(-3, 3, xDir, -3, 3, zDir)) {
                                            // noop
                                        } else {
                                            block5 = world.getBlock(x + xDir, y + layer + 2, z + zDir);
                                            if (block5.isAir(world, x + xDir, y + layer + 2, x + zDir)) {
                                                setBlockAndNotifyAdequately(
                                                    world,
                                                    x + xDir,
                                                    y + layer + 2,
                                                    z + zDir,
                                                    this.leaves,
                                                    this.leavesMeta);
                                            }
                                        }
                                    }
                                }

                                // Fourth Layer
                                for (int xDir = -2; xDir <= 2; xDir++) {
                                    for (int zDir = -2; zDir <= 2; zDir++) {
                                        if (skipLeafPlacement(-2, 2, xDir, -2, 2, zDir)) {
                                            // noop
                                        } else {
                                            block5 = world.getBlock(x + xDir, y + layer + 3, z + zDir);
                                            if (block5.isAir(world, x + xDir, y + layer + 3, x + zDir)) {
                                                setBlockAndNotifyAdequately(
                                                    world,
                                                    x + xDir,
                                                    y + layer + 3,
                                                    z + zDir,
                                                    this.leaves,
                                                    this.leavesMeta);
                                            }
                                        }
                                    }
                                }
                            } else {

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

    private boolean skipLeafPlacement(int xDirMin, int xDirMax, int xDir, int zDirMin, int zDirMax, int zDir) {
        final int absX = Math.abs(xDir), absZ = Math.abs(zDir);
        return absX <= 2 && absZ <= 2 && !(absX == 2 && absZ == 2);
    }
}
