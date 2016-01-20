package binnie.extrabees.worldgen;

import binnie.extrabees.ExtraBees;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class WorldGenHiveNether
  extends WorldGenerator
{
  public boolean generate(World world, Random random, int i, int j, int k)
  {
    BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(i, k);
    if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER)) {
      return true;
    }
    if (embedInWall(world, Blocks.netherrack, i, j, k)) {
      world.setBlock(i, j, k, ExtraBees.hive, 2, 0);
    }
    return true;
  }
  
  public boolean embedInWall(World world, Block blockID, int i, int j, int k)
  {
    return (world.getBlock(i, j, k) == blockID) && (world.getBlock(i, j + 1, k) == blockID) && (world.getBlock(i, j - 1, k) == blockID) && ((world.isAirBlock(i + 1, j, k)) || (world.isAirBlock(i - 1, j, k)) || (world.isAirBlock(i, j, k + 1)) || (world.isAirBlock(i, j, k - 1)));
  }
}
