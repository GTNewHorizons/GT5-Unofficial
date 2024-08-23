package gregtech.api.world;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.GregTech_API;

public abstract class GT_Worldgen {

    public final String mWorldGenName;
    public final boolean mEnabled;
    private final Map<String, Boolean> mDimensionMap = new ConcurrentHashMap<>();

    @SuppressWarnings({ "unchecked", "rawtypes" }) // The adding of "this" needs a List<this> which does not exist
    public GT_Worldgen(String aName, List aList, boolean aDefault) {
        mWorldGenName = aName;
        mEnabled = GregTech_API.sWorldgenFile.get("worldgen", mWorldGenName, aDefault);
        if (mEnabled) aList.add(this);
    }

    /**
     * @param aWorld         The World Object
     * @param aRandom        The Random Generator to use
     * @param aBiome         The Name of the Biome (always != null)
     * @param aDimensionType The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
     * @param aChunkX        xCoord of the Chunk
     * @param aChunkZ        zCoord of the Chunk
     * @return if the Worldgeneration has been successfully completed
     */
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        return false;
    }

    public int executeWorldgenChunkified(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, int seedX, int seedZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        return 4; // This is for the empty Orevein
    }

    /**
     * @param aWorld         The World Object
     * @param aRandom        The Random Generator to use
     * @param aBiome         The Name of the Biome (always != null)
     * @param aDimensionType The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
     * @param aChunkX        xCoord of the Chunk
     * @param aChunkZ        zCoord of the Chunk
     * @return if the Worldgeneration has been successfully completed
     */
    public boolean executeCavegen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        return false;
    }

    /**
     *
     * @param aWorld                The World Object
     * @param aDimensionType        The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
     * @param aAllowedDimensionType The Type of allowed Worldgeneration
     * @return if generation for this world is allowed for MoronTech (tm) OreGen (ATM (2.0.3.1Dev) only End, Nether,
     *         Overworld, Twilight Forest and Deep Dark)
     */
    public boolean isGenerationAllowed(World aWorld, int aDimensionType, int aAllowedDimensionType) {
        return isGenerationAllowed(aWorld.provider.getDimensionName(), aDimensionType, aAllowedDimensionType);
    }

    /**
     *
     * @param aDimName              The Dimension Name
     * @param aDimensionType        The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
     * @param aAllowedDimensionType The Type of allowed Worldgeneration
     * @return if generation for this world is allowed for MoronTech (tm) OreGen (ATM (2.0.3.1Dev) only End, Nether,
     *         Overworld, Twilight Forest and Deep Dark)
     */
    public boolean isGenerationAllowed(String aDimName, int aDimensionType, int aAllowedDimensionType) {
        if (!(aDimName.equalsIgnoreCase("Overworld") || aDimName.equalsIgnoreCase("Nether")
            || aDimName.equalsIgnoreCase("The End")
            || aDimName.equalsIgnoreCase("Twilight Forest")
            || aDimName.equalsIgnoreCase("Underdark"))) return false;

        Boolean tAllowed = mDimensionMap.get(aDimName);
        if (tAllowed == null) {
            boolean tValue = GregTech_API.sWorldgenFile
                .get("worldgen." + mWorldGenName, aDimName, aDimensionType == aAllowedDimensionType);
            mDimensionMap.put(aDimName, tValue);
            return tValue;
        }
        return tAllowed;
    }

    public boolean isGenerationAllowed(World aWorld, int aAllowedDimensionType) {
        World allowedWorld = DimensionManager.getWorld(aAllowedDimensionType);
        if (allowedWorld != null && allowedWorld.provider != null) {
            return isGenerationAllowed(aWorld, allowedWorld.provider.getClass());
        } else {
            return aWorld.provider.dimensionId == aAllowedDimensionType;
        }
    }

    /**
     *
     * @param aWorld                 The World Object
     * @param aAllowedDimensionTypes The Types of allowed Worldgeneration
     * @return if generation for this world is allowed for MoronTech (tm) OreGen (ATM (2.0.3.1Dev) only End, Nether,
     *         Overworld, Twilight Forest and Deep Dark)
     */
    public boolean isGenerationAllowed(World aWorld, Class... aAllowedDimensionTypes) {
        String aDimName = aWorld.provider.getDimensionName();
        if (!(aDimName.equalsIgnoreCase("Overworld") || aDimName.equalsIgnoreCase("Nether")
            || aDimName.equalsIgnoreCase("The End")
            || aDimName.equalsIgnoreCase("Twilight Forest")
            || aDimName.equalsIgnoreCase("Underdark"))) return false;

        Boolean tAllowed = mDimensionMap.get(aDimName);
        if (tAllowed == null) {
            boolean tValue = GregTech_API.sWorldgenFile.get(
                "worldgen." + mWorldGenName,
                aDimName,
                Arrays.stream(aAllowedDimensionTypes)
                    .anyMatch(worldProvider -> worldProvider.isInstance(aWorld.provider)));
            mDimensionMap.put(aDimName, tValue);
            return tValue;
        }
        return tAllowed;
    }
}
