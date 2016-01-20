package binnie.extrabees.worldgen;

import binnie.extrabees.ExtraBees;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenHiveWater
  extends WorldGenerator
{
  public boolean generate(World world, Random random, int i, int j, int k)
  {
    BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(i, k);
    
    int i1 = i + random.nextInt(8) - random.nextInt(8);
    int j1 = j + random.nextInt(4) - random.nextInt(4);
    int k1 = k + random.nextInt(8) - random.nextInt(8);
    if ((world.getBlock(i1, j1, k1) != Blocks.water) && (world.getBlock(i1, j1, k1) != Blocks.water)) {
      return false;
    }
    if ((world.getBlock(i1, j1 - 1, k1).getMaterial() == Material.sand) || (world.getBlock(i1, j1 - 1, k1).getMaterial() == Material.clay) || (world.getBlock(i1, j1 - 1, k1).getMaterial() == Material.ground) || (world.getBlock(i1, j1 - 1, k1).getMaterial() == Material.rock)) {
      world.setBlock(i1, j1, k1, ExtraBees.hive, 0, 0);
    }
    return true;
  }
}
