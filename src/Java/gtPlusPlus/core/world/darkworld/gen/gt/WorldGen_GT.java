package gtPlusPlus.core.world.darkworld.gen.gt;

import gregtech.api.GregTech_API;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WorldGen_GT {
	public final String mWorldGenName;
	public final boolean mEnabled;
	private final Map<String, Boolean> mDimensionMap = new ConcurrentHashMap<String, Boolean>();

	public WorldGen_GT(String aName, List aList, boolean aDefault) {
		this.mWorldGenName = aName;
		this.mEnabled = HANDLER_GT.sCustomWorldgenFile.get("worldgen", this.mWorldGenName, aDefault);
		if (this.mEnabled) {
			aList.add(this);
		}

	}

	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
			int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		return false;
	}

	public boolean executeCavegen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
			int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		return false;
	}

	public boolean isGenerationAllowed(World aWorld, int aDimensionType, int aAllowedDimensionType) {
		String aDimName = aWorld.provider.getDimensionName();
		Boolean tAllowed = (Boolean) this.mDimensionMap.get(aDimName);
		if (tAllowed == null) {
			boolean tValue = HANDLER_GT.sCustomWorldgenFile.get("worldgen.dimensions." + this.mWorldGenName, aDimName,
					aDimensionType == aAllowedDimensionType);
			this.mDimensionMap.put(aDimName, Boolean.valueOf(tValue));
			return tValue;
		} else {
			return tAllowed.booleanValue();
		}
	}
}