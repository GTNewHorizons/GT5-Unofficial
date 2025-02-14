package toxiceverglades.gen;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class WorldGenEverglades {

    public final String mWorldGenName;
    public final boolean mEnabled;
    private final Map<String, Boolean> mDimensionMap = new ConcurrentHashMap<>();

    public WorldGenEverglades(String aName, List aList, boolean aDefault) {
        this.mWorldGenName = aName;
        this.mEnabled = aDefault;
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
        Boolean tAllowed = this.mDimensionMap.get(aDimName);
        if (tAllowed == null) {
            boolean tValue = (aDimensionType == aAllowedDimensionType);
            this.mDimensionMap.put(aDimName, tValue);
            return tValue;
        } else {
            return tAllowed;
        }
    }
}
