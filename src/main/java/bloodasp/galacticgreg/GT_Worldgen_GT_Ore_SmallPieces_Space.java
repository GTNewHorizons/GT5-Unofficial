package bloodasp.galacticgreg;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.SmallOreBuilder;

public class GT_Worldgen_GT_Ore_SmallPieces_Space extends GT_Worldgen {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final short mMeta;

    private long mProfilingStart;
    private long mProfilingEnd;
    private Map<String, Boolean> allowedDims;

    public GT_Worldgen_GT_Ore_SmallPieces_Space(SmallOreBuilder ore) {
        super(ore.smallOreName, GalacticGreg.smallOreWorldgenList, ore.enabledByDefault);

        mMinY = (short) ore.minY;
        mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        mAmount = (short) Math.max(1, ore.amount);
        mMeta = (short) ore.ore.mMetaItemSubID;

        allowedDims = new HashMap<>();
        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            if (!mc.getEnabled()) continue;

            for (ModDimensionDef mdd : mc.getDimensionList()) {
                String tDimIdentifier = mdd.getDimIdentifier();
                if (allowedDims.containsKey(tDimIdentifier)) GalacticGreg.Logger.error(
                    "Found 2 Dimensions with the same Identifier: %s Dimension will not generate Ores",
                    tDimIdentifier);
                else {
                    boolean tFlag = ore.dimsEnabled.getOrDefault(mdd.getDimensionName(), false);
                    allowedDims.put(tDimIdentifier, tFlag);
                }
            }
        }

        GalacticGreg.Logger.trace("Initialized new OreLayer: %s", ore.smallOreName);
    }

    /**
     * Check if *this* orelayer is enabled for pDimensionDef
     *
     * @param pDimensionDef the ChunkProvider in question
     * @return
     */
    public boolean isEnabledForDim(ModDimensionDef pDimensionDef) {
        return allowedDims.getOrDefault(pDimensionDef.getDimIdentifier(), false);
    }

    @Override
    public boolean executeWorldgen(World pWorld, Random pRandom, String pBiome, int pDimensionType, int pChunkX,
        int pChunkZ, IChunkProvider pChunkGenerator, IChunkProvider pChunkProvider) {
        GalacticGreg.Logger.trace("Entering executeWorldgen for [%s]", mWorldGenName);
        ModDimensionDef tMDD = GalacticGregRegistry.getDimensionTypeByChunkGenerator(pChunkGenerator);
        if (tMDD == null) {
            GalacticGreg.Logger
                .trace("Can't find dimension definition for ChunkProvider %s, skipping", pChunkGenerator.toString());
            return false;
        }

        if (!isEnabledForDim(tMDD)) {
            GalacticGreg.Logger
                .trace("OreGen for %s is disallowed in dimension %s, skipping", mWorldGenName, tMDD.getDimensionName());
            return false;
        }

        if (GalacticGreg.GalacticConfig.ProfileOreGen) mProfilingStart = System.currentTimeMillis();
        // ---------------------------

        if (this.mMeta > 0) {
            int i = 0;
            for (int j = Math.max(1, this.mAmount / 2 + pRandom.nextInt(this.mAmount) / 2); i < j; i++) {
                GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(
                    tMDD,
                    pWorld,
                    pChunkX + pRandom.nextInt(16),
                    this.mMinY + pRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)),
                    pChunkZ + pRandom.nextInt(16),
                    this.mMeta + 16000);
            }
        }
        // ---------------------------
        if (GalacticGreg.GalacticConfig.ProfileOreGen) {
            try {
                mProfilingEnd = System.currentTimeMillis();
                long tTotalTime = mProfilingEnd - mProfilingStart;
                GalacticGreg.Profiler.AddTimeToList(tMDD, tTotalTime);
                GalacticGreg.Logger.debug(
                    "Done with SmallOre-Worldgen in DimensionType %s. Generation took %d ms",
                    tMDD.getDimensionName(),
                    tTotalTime);
            } catch (Exception ignored) {} // Silently ignore errors
        }

        GalacticGreg.Logger.trace("Leaving executeWorldgen");
        return true;
    }

    public boolean isAllowedForHeight(int pTargetHeight) {
        return (pTargetHeight >= mMinY && pTargetHeight <= mMaxY);
    }
}
