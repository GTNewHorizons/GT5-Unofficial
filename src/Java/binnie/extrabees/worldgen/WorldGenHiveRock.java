package binnie.extrabees.worldgen;

import binnie.extrabees.ExtraBees;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenHiveRock
  extends WorldGenerator
{
  public boolean generate(World world, Random random, int i, int j, int k)
  {
    BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(i, k);
    Block block = world.getBlock(i, j, k);
    if (block == null) {
      return true;
    }
    if (block.isReplaceableOreGen(world, i, j, k, Blocks.stone)) {
      world.setBlock(i, j, k, ExtraBees.hive, 1, 0);
    }
    return true;
  }
}
