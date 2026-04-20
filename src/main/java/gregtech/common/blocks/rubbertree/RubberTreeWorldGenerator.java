package gregtech.common.blocks.rubbertree;

import gregtech.api.GregTechAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RubberTreeWorldGenerator extends WorldGenAbstractTree {

    public RubberTreeWorldGenerator(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World world, @NotNull Random random, int x, int y, int z) {
        int height = 5 + random.nextInt(3);

        if (y < 1 || y + height + 1 > 256) {
            return false;
        }

        Block soil = world.getBlock(x, y - 1, z);
        if (soil == null || !soil.canSustainPlant(
            world,
            x,
            y - 1,
            z,
            ForgeDirection.UP,
            (BlockSapling) GregTechAPI.sBlockRubberSapling)) {
            return false;
        }

        // Check necessary space
        for (int yy = y; yy <= y + 1 + height; yy++) {
            int radius = 1;

            if (yy == y) {
                radius = 0;
            } else if (yy >= y + height - 1) {
                radius = 2;
            }

            for (int xx = x - radius; xx <= x + radius; xx++) {
                for (int zz = z - radius; zz <= z + radius; zz++) {
                    if (yy < 0 || yy >= 256) {
                        return false;
                    }

                    Block block = world.getBlock(xx, yy, zz);
                    if (block != null && !block.isAir(world, xx, yy, zz) && !block.isLeaves(world, xx, yy, zz)
                        && !block.canBeReplacedByLeaves(world, xx, yy, zz)) {
                        return false;
                    }
                }
            }
        }

        soil.onPlantGrow(world, x, y - 1, z, x, y, z);

        // Leaves
        for (int yy = y - 3 + height; yy <= y + height; yy++) {
            int layer = yy - (y + height);
            int radius = 1 - layer / 2;

            for (int xx = x - radius; xx <= x + radius; xx++) {
                int dx = xx - x;

                for (int zz = z - radius; zz <= z + radius; zz++) {
                    int dz = zz - z;

                    if (Math.abs(dx) != radius || Math.abs(dz) != radius || random.nextInt(2) != 0 && layer != 0) {
                        Block block = world.getBlock(xx, yy, zz);

                        if (block == null || block.isAir(world, xx, yy, zz) || block.isLeaves(world, xx, yy, zz)
                            || block.canBeReplacedByLeaves(world, xx, yy, zz)) {
                            setBlockAndNotifyAdequately(world, xx, yy, zz, GregTechAPI.sBlockRubberLeaves, 0);
                        }
                    }
                }
            }
        }

        // Tree log
        for (int trunkY = 0; trunkY < height; trunkY++) {
            Block block = world.getBlock(x, y + trunkY, z);

            if (block == null || block.isAir(world, x, y + trunkY, z) || block.isLeaves(world, x, y + trunkY, z)
                || block.canBeReplacedByLeaves(world, x, y + trunkY, z)) {

                int meta = BlockRubberLog.makeMeta(true, BlockRubberLog.AXIS_Y);
                setBlockAndNotifyAdequately(world, x, y + trunkY, z, GregTechAPI.sBlockRubberLog, meta);
            }
        }

        return true;
    }
}
