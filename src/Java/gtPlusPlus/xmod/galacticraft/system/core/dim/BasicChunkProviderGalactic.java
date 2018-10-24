package gtPlusPlus.xmod.galacticraft.system.core.dim;

import gtPlusPlus.xmod.galacticraft.system.core.world.gen.ChunkProviderGalactic;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import net.minecraft.world.World;

public class BasicChunkProviderGalactic extends ChunkProviderGalactic {

	public BasicChunkProviderGalactic(World par1World, long seed, boolean mapFeaturesEnabled) {
		super(par1World, seed, mapFeaturesEnabled);
	}

	@Override
	public BiomeDecoratorSpace getBiomeGenerator() {
		return null;
	}
	
}
