package gtPlusPlus.core.world.dimensionA.world.biomes;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeDarkForest extends ModBiomes{

	public BiomeDarkForest(int biomeId) {
		super(biomeId);
		this.theBiomeDecorator.treesPerChunk = 30;
		this.theBiomeDecorator.grassPerChunk = 5;
		this.theBiomeDecorator.flowersPerChunk = 1;
		
		this.topBlock = Blocks.grass;
		this.fillerBlock = Blocks.dirt;
	}
	
	/**
	 * Remove this to remove vines from dimension
	 */
    @Override
	public void decorate(World world, Random random, int par3, int par4) {
        super.decorate(world, random, par3, par4);
//        WorldGenVines worldgenvines = new WorldGenVines();
//
//        for (int k = 0; k < 50; ++k) {
//            int l = par3 + random.nextInt(16) + 8;
//            byte b0 = 64;
//            int i1 = par4 + random.nextInt(16) + 8;
//            worldgenvines.generate(world, random, l, b0, i1);
//        }
    }
}
