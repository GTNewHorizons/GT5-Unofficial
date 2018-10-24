package gtPlusPlus.xmod.galacticraft.system.objects;

import net.minecraft.world.biome.BiomeGenBase.Height;

public class BiomeSettings {

	private final String mBiomeName;
	private final int mBiomeID;
	private final Height mHeight;
	
	public BiomeSettings(String aName, int aID, float aHeightMin, float aHeightMax) {
		mBiomeName = aName;
		mBiomeID = aID;
		mHeight = new Height(aHeightMin, aHeightMax);
		
	}

	public synchronized final String getName() {
		return mBiomeName;
	}

	public synchronized final int getID() {
		return mBiomeID;
	}

	public synchronized final Height getHeight() {
		return mHeight;
	}
}
