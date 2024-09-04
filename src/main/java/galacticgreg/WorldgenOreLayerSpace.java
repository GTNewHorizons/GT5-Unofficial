package galacticgreg;

import static gregtech.api.enums.GTValues.oreveinPlacerOres;
import static gregtech.api.enums.GTValues.oreveinPlacerOresMultiplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.auxiliary.GTOreGroup;
import galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.OreMixBuilder;

public class WorldgenOreLayerSpace extends GTWorldgen {

    public static int sWeight = 0;
    public final short mMinY;
    public final short mMaxY;
    public final short mWeight;
    public final short mDensity;
    public final short mSize;
    public final short mPrimaryMeta;
    public final short mSecondaryMeta;
    public final short mBetweenMeta;
    public final short mSporadicMeta;

    private long mProfilingStart;
    private long mProfilingEnd;
    private Map<String, Boolean> allowedDims;

    public WorldgenOreLayerSpace(OreMixBuilder mix) {
        super(mix.oreMixName, GalacticGreg.oreVeinWorldgenList, mix.enabledByDefault);

        mMinY = (short) mix.minY;
        mMaxY = (short) Math.max(this.mMinY + 5, mix.maxY);
        mWeight = (short) mix.weight;
        mDensity = (short) mix.density;
        mSize = (short) Math.max(1, mix.size);
        mPrimaryMeta = (short) mix.primary.mMetaItemSubID;
        mSecondaryMeta = (short) mix.secondary.mMetaItemSubID;
        mBetweenMeta = (short) mix.between.mMetaItemSubID;
        mSporadicMeta = (short) mix.sporadic.mMetaItemSubID;

        allowedDims = new HashMap<>();

        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            if (!mc.isModLoaded()) continue;

            for (ModDimensionDef mdd : mc.getDimensionList()) {
                String tDimIdentifier = mdd.getDimIdentifier();
                if (allowedDims.containsKey(tDimIdentifier)) GalacticGreg.Logger.error(
                    "Found 2 Dimensions with the same Identifier: %s Dimension will not generate Ores",
                    tDimIdentifier);
                else {
                    boolean tFlag = mix.dimsEnabled.getOrDefault(mdd.getDimensionName(), false);
                    allowedDims.put(tDimIdentifier, tFlag);
                }
            }
        }

        GalacticGreg.Logger.trace("Initialized new OreLayer: %s", mix.oreMixName);
        if (mEnabled) sWeight += this.mWeight;
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

    private static Map<String, List<String>> _mBufferedVeinList = new HashMap<>();

    /**
     * Get a List of all Veins which are enabled for given Dim. Query is buffered
     *
     * @param pDimensionDef
     * @return null if nothing is found or error
     */
    private static List<String> getOreMixIDsForDim(ModDimensionDef pDimensionDef) {
        List<String> tReturn;

        if (_mBufferedVeinList.containsKey(pDimensionDef.getDimIdentifier()))
            tReturn = _mBufferedVeinList.get(pDimensionDef.getDimIdentifier());
        else {
            tReturn = new ArrayList<>();
            for (GTWorldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) if (tWorldGen instanceof WorldgenOreLayerSpace
                && ((WorldgenOreLayerSpace) tWorldGen).isEnabledForDim(pDimensionDef))
                tReturn.add(tWorldGen.mWorldGenName);

            _mBufferedVeinList.put(pDimensionDef.getDimIdentifier(), tReturn);
        }

        return tReturn;
    }

    /**
     * Select a random ore-vein from the list
     *
     * @param pDimensionDef
     * @param pRandom
     * @return
     */
    public static GTOreGroup getRandomOreGroup(ModDimensionDef pDimensionDef, Random pRandom, boolean pIgnoreWeight) {
        short primaryMeta = 0;
        short secondaryMeta = 0;
        short betweenMeta = 0;
        short sporadicMeta = 0;

        if (pIgnoreWeight) {
            List<String> tEnabledVeins = getOreMixIDsForDim(pDimensionDef);
            int tRnd = pRandom.nextInt(tEnabledVeins.size());
            String tVeinName = tEnabledVeins.get(tRnd);

            GTWorldgen tGen = null;
            for (GTWorldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) if (tWorldGen instanceof WorldgenOreLayerSpace
                && ((WorldgenOreLayerSpace) tWorldGen).mWorldGenName.equals(tVeinName)) tGen = tWorldGen;

            if (tGen != null) {
                // GT_Worldgen_GT_Ore_Layer_Space tGen = GalacticGreg.oreVeinWorldgenList.get(tRndMix);
                GalacticGreg.Logger.trace("Using Oremix %s for asteroid", tGen.mWorldGenName);
                primaryMeta = ((WorldgenOreLayerSpace) tGen).mPrimaryMeta;
                secondaryMeta = ((WorldgenOreLayerSpace) tGen).mSecondaryMeta;
                betweenMeta = ((WorldgenOreLayerSpace) tGen).mBetweenMeta;
                sporadicMeta = ((WorldgenOreLayerSpace) tGen).mSporadicMeta;
            }
        } else {
            if ((WorldgenOreLayerSpace.sWeight > 0) && (GalacticGreg.oreVeinWorldgenList.size() > 0)) {
                GalacticGreg.Logger.trace("About to select oremix");
                boolean temp = true;
                int tRandomWeight;
                for (int i = 0; (i < 256) && (temp); i++) {
                    tRandomWeight = pRandom.nextInt(WorldgenOreLayerSpace.sWeight);
                    for (GTWorldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) {
                        if (!(tWorldGen instanceof WorldgenOreLayerSpace)) continue;

                        tRandomWeight -= ((WorldgenOreLayerSpace) tWorldGen).mWeight;
                        if (tRandomWeight <= 0) {
                            try {
                                if (((WorldgenOreLayerSpace) tWorldGen).isEnabledForDim(pDimensionDef)) {
                                    GalacticGreg.Logger.trace("Using Oremix %s for asteroid", tWorldGen.mWorldGenName);
                                    primaryMeta = ((WorldgenOreLayerSpace) tWorldGen).mPrimaryMeta;
                                    secondaryMeta = ((WorldgenOreLayerSpace) tWorldGen).mSecondaryMeta;
                                    betweenMeta = ((WorldgenOreLayerSpace) tWorldGen).mBetweenMeta;
                                    sporadicMeta = ((WorldgenOreLayerSpace) tWorldGen).mSporadicMeta;

                                    temp = false;
                                    break;
                                }
                            } catch (Throwable e) {
                                e.printStackTrace(GTLog.err);
                            }
                        }
                    }
                }
            }
        }
        if (primaryMeta != 0 || secondaryMeta != 0 || betweenMeta != 0 || sporadicMeta != 0)
            return new GTOreGroup(primaryMeta, secondaryMeta, betweenMeta, sporadicMeta);
        else return null;
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
        int tMinY = this.mMinY + pRandom.nextInt(this.mMaxY - this.mMinY - 7);

        int cX = pChunkX - pRandom.nextInt(this.mSize);
        int eX = pChunkX + 16 + pRandom.nextInt(this.mSize);
        int cZ = pChunkZ - pRandom.nextInt(this.mSize);
        int eZ = pChunkZ + 16 + pRandom.nextInt(this.mSize);
        for (int tX = cX; tX <= eX; tX++) {
            for (int tZ = cZ; tZ <= eZ; tZ++) {
                if (this.mSecondaryMeta > 0) {
                    for (int i = tMinY - 1; i < tMinY + 3; i++) {
                        int placeX = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        int placeZ = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        if ((pRandom.nextInt(placeZ) == 0) || (pRandom.nextInt(placeX) == 0)) {
                            TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mSecondaryMeta);
                        }
                    }
                }
                if (this.mBetweenMeta > 0) {
                    for (int i = tMinY + 2; i < tMinY + 6; i++) {
                        int placeX = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        int placeZ = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        if (((pRandom.nextInt(placeZ) == 0) || (pRandom.nextInt(placeX) == 0))
                            && (pRandom.nextInt(2) == 0)) {
                            TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mBetweenMeta);
                        }
                    }

                }
                if (this.mPrimaryMeta > 0) {
                    for (int i = tMinY + 4; i < tMinY + 8; i++) {
                        int placeX = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        int placeZ = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        if ((pRandom.nextInt(placeZ) == 0) || (pRandom.nextInt(placeX) == 0)) {
                            TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mPrimaryMeta);
                        }
                    }
                }
                if (this.mSporadicMeta > 0) {
                    for (int i = tMinY - 1; i < tMinY + 8; i++) {
                        int placeX = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        int placeZ = Math.max(
                            1,
                            Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ))
                                / getDensityFromPos(tX, tZ, pChunkX, pChunkZ));
                        if (((pRandom.nextInt(placeX) == 0) || (pRandom.nextInt(placeZ) == 0))
                            && (pRandom.nextInt(7) == 0)) {
                            TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mSporadicMeta);
                        }
                    }
                }
            }
        }

        if (oreveinPlacerOres) {
            int nSmallOres = (cX - eX) * (cZ - eZ) * this.mDensity / 10 * oreveinPlacerOresMultiplier;
            for (int nSmallOresCount = 0; nSmallOresCount < nSmallOres; nSmallOresCount++) {
                int tX = pRandom.nextInt(16) + pChunkX + 2;
                int tZ = pRandom.nextInt(16) + pChunkZ + 2;
                int tY = pRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mPrimaryMeta > 0)
                    TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mPrimaryMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mSecondaryMeta > 0)
                    TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mSecondaryMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mBetweenMeta > 0)
                    TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mBetweenMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(190) + 10; // Y height can vary from 10 to 200 for small ores.
                if (this.mSporadicMeta > 0)
                    TileEntitySpaceOres.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mSporadicMeta + 16000);
            }
        }

        // ---------------------------
        if (GalacticGreg.GalacticConfig.ProfileOreGen) {
            try {
                mProfilingEnd = System.currentTimeMillis();
                long tTotalTime = mProfilingEnd - mProfilingStart;
                GalacticGreg.Profiler.AddTimeToList(tMDD, tTotalTime);
                GalacticGreg.Logger.debug(
                    "Done with OreLayer-Worldgen in DimensionType %s. Generation took %d ms",
                    tMDD.getDimensionName(),
                    tTotalTime);
            } catch (Exception ignored) {} // Silently ignore errors
        }

        GalacticGreg.Logger.trace("Leaving executeWorldgen");
        return true;
    }

    public int getDensityFromPos(int aX, int aZ, int aSeedX, int aSeedZ) {
        if (aX < 0) aX -= 16;
        if (aZ < 0) aZ -= 16;
        return Math.max(
            1,
            this.mDensity
                / ((int) Math.sqrt(2 + Math.pow(aX / 16 - aSeedX / 16, 2) + Math.pow(aZ / 16 - aSeedZ / 16, 2))));
    }
}
