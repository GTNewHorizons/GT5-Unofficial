package gtPlusPlus.xmod.bop.blocks.pine;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.bop.blocks.base.SaplingBase;
import gtPlusPlus.xmod.bop.world.features.trees.WorldGenPineTree;

public class SaplingPineTree extends SaplingBase {

    public SaplingPineTree() {
        super("Pine Sapling", "pine", new String[] { "pine" });
    }

    @Override
    public void func_149878_d(World world, int x, int y, int z, Random rand) {
        Logger.WARNING("func_149878_d - 1");
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
        rand.nextInt(10);
        new WorldGenBigTree(true);
        new WorldGenTrees(true);
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        Block block = Blocks.air;

        if (flag) {
            world.setBlock(x + i1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
        } else {
            world.setBlock(x, y, z, block, 0, 4);
        }
        Object obj = new WorldGenPineTree();
        if (obj != null) {
            world.setBlockToAir(x, y, z);
            if (!((WorldGenerator) obj).generate(world, GTPPCore.RANDOM, x, y, z)) {
                world.setBlock(x, y, z, this, 0, 2);
            }
        }
    }
}
