package gregtech.api.world;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

public abstract class GTWorldgen {

    public final String mWorldGenName;
    public final boolean mEnabled;
    private final Map<String, Boolean> mDimensionMap = new ConcurrentHashMap<>();

    public static final int WRONG_BIOME = 0;
    public static final int WRONG_DIMENSION = 1;
    public static final int NO_OVERLAP = 2;
    public static final int ORE_PLACED = 3;
    public static final int NO_OVERLAP_AIR_BLOCK = 4;

    @SuppressWarnings({ "unchecked", "rawtypes" }) // The adding of "this" needs a List<this> which does not exist
    public GTWorldgen(String aName, List aList, boolean aDefault) {
        mWorldGenName = aName;
        mEnabled = aDefault;
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
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aChunkX, int aChunkZ,
        IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        return false;
    }

    public int executeWorldgenChunkified(World aWorld, Random aRandom, String aBiome, int aChunkX, int aChunkZ,
        int seedX, int seedZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        return ORE_PLACED; // This is for the empty Orevein
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
        if (aDimName.equalsIgnoreCase("Underdark")) {
            return false;
        }
        if (!(aDimName.equalsIgnoreCase("Overworld") || aDimName.equalsIgnoreCase("Nether")
            || aDimName.equalsIgnoreCase("The End")
            || aDimName.equalsIgnoreCase("Twilight Forest"))) return false;

        Boolean tAllowed = mDimensionMap.get(aDimName);
        if (tAllowed == null) {
            mDimensionMap.put(aDimName, aDimensionType == aAllowedDimensionType);
            return aDimensionType == aAllowedDimensionType;
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
        return isGenerationAllowed(aWorld, null, aAllowedDimensionTypes);
    }

    /**
     *
     * @param aWorld                 The World Object
     * @param blackListedProviders   List of blacklisted Worldgeneration classes
     * @param aAllowedDimensionTypes The Types of allowed Worldgeneration
     * @return if generation for this world is allowed for MoronTech (tm) OreGen (ATM (2.0.3.1Dev) only End, Nether,
     *         Overworld, Twilight Forest and Deep Dark)
     */
    public boolean isGenerationAllowed(World aWorld, String[] blackListedProviders, Class... aAllowedDimensionTypes) {
        String aDimName = aWorld.provider.getDimensionName();
        if (aDimName.equalsIgnoreCase("Underdark")) {
            return false;
        }
        if (!(aDimName.equalsIgnoreCase("Overworld") || aDimName.equalsIgnoreCase("Nether")
            || aDimName.equalsIgnoreCase("The End")
            || aDimName.equalsIgnoreCase("Twilight Forest"))) return false;

        Boolean tAllowed = mDimensionMap.get(aDimName);
        if (tAllowed == null) {
            if (blackListedProviders != null) {
                for (String dimClass : blackListedProviders) {
                    if (dimClass.equals(
                        aWorld.provider.getClass()
                            .getName())) {
                        return false;
                    }
                }
            }
            boolean value = false;
            for (Class aAllowedDimensionType : aAllowedDimensionTypes) {
                if (aAllowedDimensionType.isInstance(aWorld.provider)) {
                    value = true;
                    break;
                }
            }

            mDimensionMap.put(aDimName, value);
            return value;
        }
        return tAllowed;
    }
}
