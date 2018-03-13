package gtPlusPlus.everglades.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenWaterlily;

public class WorldGenDeadLilly extends WorldGenWaterlily {

    @Override
	public boolean generate(World world, Random rand, int x, int y, int z)
    {
        for (int l = 0; l < 10; ++l)
        {
            int i1 = x + rand.nextInt(8) - rand.nextInt(8);
            int j1 = y + rand.nextInt(4) - rand.nextInt(4);
            int k1 = z + rand.nextInt(8) - rand.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && Blocks.waterlily.canPlaceBlockAt(world, i1, j1, k1))
            {
                world.setBlock(i1, j1, k1, Blocks.waterlily, 0, 2);
            }
        }

        return true;
    }
}