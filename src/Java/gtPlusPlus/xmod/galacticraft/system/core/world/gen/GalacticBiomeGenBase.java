package gtPlusPlus.xmod.galacticraft.system.core.world.gen;

import gtPlusPlus.xmod.galacticraft.system.core.world.gen.biome.BiomeGenGalaxy;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class GalacticBiomeGenBase extends BiomeGenBase {
	
	public static BiomeGenBase mGalaxy;
	public Block stoneBlock;
	public byte topMeta;
	public byte fillerMeta;
	public byte stoneMeta;
	
	public int mBiomeID;

	public GalacticBiomeGenBase(int id) {
		super(id);
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.rainfall = 0.0F;
		this.setColor(-16744448);
		mBiomeID = id;
	}

	public GalacticBiomeGenBase setColor(int var1) {
		return (GalacticBiomeGenBase) super.setColor(var1);
	}

	public float getSpawningChance() {
		return 0.1F;
	}

	static {
		mGalaxy = (new BiomeGenGalaxy(177)).setBiomeName("Galaxy");
	}
}