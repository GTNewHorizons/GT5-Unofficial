package bloodasp.galacticgreg;

import static gregtech.api.enums.GT_Values.oreveinPlacerOres;
import static gregtech.api.enums.GT_Values.oreveinPlacerOresMultiplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.auxiliary.GTOreGroup;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.dynconfig.DynamicOreMixWorldConfig;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;

public class GT_Worldgen_GT_Ore_Layer_Space extends GT_Worldgen {

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

    private DynamicOreMixWorldConfig _mDynWorldConfig;

    public GT_Worldgen_GT_Ore_Layer_Space(String pName, boolean pDefault, int pMinY, int pMaxY, int pWeight,
        int pDensity, int pSize, Materials pPrimary, Materials pSecondary, Materials pBetween, Materials pSporadic) {
        super(pName, GalacticGreg.oreVeinWorldgenList, pDefault);
        mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", pMinY));
        mMaxY = ((short) Math
            .max(this.mMinY + 5, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", pMaxY)));
        mWeight = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RandomWeight", pWeight));
        mDensity = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Density", pDensity));
        mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Size", pSize)));
        mPrimaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OrePrimaryLayer", pPrimary.mMetaItemSubID));
        mSecondaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSecondaryLayer", pSecondary.mMetaItemSubID));
        mBetweenMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSporadiclyInbetween", pBetween.mMetaItemSubID));
        mSporadicMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSporaticlyAround", pSporadic.mMetaItemSubID));

        _mDynWorldConfig = new DynamicOreMixWorldConfig(mWorldGenName);
        _mDynWorldConfig.InitDynamicConfig();

        GalacticGreg.Logger.trace("Initialized new OreLayer: %s", pName);

        if (mEnabled) GT_Worldgen_GT_Ore_Layer_Space.sWeight += this.mWeight;

    }

    public GT_Worldgen_GT_Ore_Layer_Space(String pName, boolean pDefault, int pMinY, int pMaxY, int pWeight,
        int pDensity, int pSize, short pPrimary, short pSecondary, short pBetween, short pSporadic) {
        super(pName, GalacticGreg.oreVeinWorldgenList, pDefault);
        mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", pMinY));
        mMaxY = ((short) Math
            .max(this.mMinY + 5, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", pMaxY)));
        mWeight = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RandomWeight", pWeight));
        mDensity = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Density", pDensity));
        mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Size", pSize)));
        mPrimaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OrePrimaryLayer", pPrimary));
        mSecondaryMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSecondaryLayer", pSecondary));
        mBetweenMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSporadiclyInbetween", pBetween));
        mSporadicMeta = ((short) GregTech_API.sWorldgenFile
            .get("worldgen." + this.mWorldGenName, "OreSporaticlyAround", pSporadic));

        _mDynWorldConfig = new DynamicOreMixWorldConfig(mWorldGenName);
        _mDynWorldConfig.InitDynamicConfig();

        GalacticGreg.Logger.trace("Initialized new OreLayer: %s", pName);

        if (mEnabled) sWeight += this.mWeight;

    }

    /**
     * Check if *this* orelayer is enabled for pDimensionDef
     *
     * @param pDimensionDef the ChunkProvider in question
     * @return
     */
    public boolean isEnabledForDim(ModDimensionDef pDimensionDef) {
        return _mDynWorldConfig.isEnabledInDim(pDimensionDef);
    }

    private static Map<String, Integer> _mBufferedVeinCountList = new HashMap<>();

    /**
     * Get the number of enabled OreMixes for given Dimension. This query is buffered and will only consume calculation
     * time on the first run for each dimension
     *
     * @param pDimensionDef
     * @return
     */
    private static int getNumOremixedForDim(ModDimensionDef pDimensionDef) {
        int tVal = 0;
        if (_mBufferedVeinCountList.containsKey(pDimensionDef.getDimIdentifier()))
            tVal = _mBufferedVeinCountList.get(pDimensionDef.getDimIdentifier());
        else {
            for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList)
                if (tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space
                    && ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).isEnabledForDim(pDimensionDef)) tVal++;

            _mBufferedVeinCountList.put(pDimensionDef.getDimIdentifier(), tVal);
        }

        return tVal;
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
            for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList)
                if (tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space
                    && ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).isEnabledForDim(pDimensionDef))
                    tReturn.add(tWorldGen.mWorldGenName);
                else if (tWorldGen instanceof BW_Worldgen_Ore_Layer_Space
                    && ((BW_Worldgen_Ore_Layer_Space) tWorldGen).isEnabledForDim(pDimensionDef))
                    tReturn.add(tWorldGen.mWorldGenName);

            _mBufferedVeinList.put(pDimensionDef.getDimIdentifier(), tReturn);
        }

        return tReturn;
    }

    private static short getMaxWeightForDim(ModDimensionDef pDimensionDef) {
        short tVal = 0;
        for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList)
            if (tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space
                && ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).isEnabledForDim(pDimensionDef)
                && tVal < ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight)
                tVal = ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;

        return tVal;
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

        // int tRangeSplit = getMaxWeightForDim(pDimensionDef) / 2;

        if (pIgnoreWeight) {
            List<String> tEnabledVeins = getOreMixIDsForDim(pDimensionDef);
            int tRnd = pRandom.nextInt(tEnabledVeins.size());
            String tVeinName = tEnabledVeins.get(tRnd);

            GT_Worldgen tGen = null;
            for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList)
                if (tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space
                    && ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWorldGenName.equals(tVeinName)) tGen = tWorldGen;

            if (tGen != null) {
                // GT_Worldgen_GT_Ore_Layer_Space tGen = GalacticGreg.oreVeinWorldgenList.get(tRndMix);
                GalacticGreg.Logger.trace("Using Oremix %s for asteroid", tGen.mWorldGenName);
                primaryMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tGen).mPrimaryMeta;
                secondaryMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tGen).mSecondaryMeta;
                betweenMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tGen).mBetweenMeta;
                sporadicMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tGen).mSporadicMeta;
            }
        } else {
            if ((GT_Worldgen_GT_Ore_Layer_Space.sWeight > 0) && (GalacticGreg.oreVeinWorldgenList.size() > 0)) {
                GalacticGreg.Logger.trace("About to select oremix");
                boolean temp = true;
                int tRandomWeight;
                for (int i = 0; (i < 256) && (temp); i++) {
                    tRandomWeight = pRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
                    for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) {
                        if (!(tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space)) continue;

                        tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;
                        if (tRandomWeight <= 0) {
                            try {
                                if (((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).isEnabledForDim(pDimensionDef)) {
                                    GalacticGreg.Logger.trace("Using Oremix %s for asteroid", tWorldGen.mWorldGenName);
                                    primaryMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mPrimaryMeta;
                                    secondaryMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mSecondaryMeta;
                                    betweenMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mBetweenMeta;
                                    sporadicMeta = ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mSporadicMeta;

                                    temp = false;
                                    break;
                                }
                            } catch (Throwable e) {
                                e.printStackTrace(GT_Log.err);
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

        if (!_mDynWorldConfig.isEnabledInDim(tMDD)) {
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
                            GT_TileEntity_Ores_Space
                                .setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mSecondaryMeta);
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
                            GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mBetweenMeta);
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
                            GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mPrimaryMeta);
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
                            GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, i, tZ, this.mSporadicMeta);
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
                    GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mPrimaryMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mSecondaryMeta > 0) GT_TileEntity_Ores_Space
                    .setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mSecondaryMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mBetweenMeta > 0)
                    GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mBetweenMeta + 16000);
                tX = pRandom.nextInt(16) + pChunkX + 2;
                tZ = pRandom.nextInt(16) + pChunkZ + 2;
                tY = pRandom.nextInt(190) + 10; // Y height can vary from 10 to 200 for small ores.
                if (this.mSporadicMeta > 0) GT_TileEntity_Ores_Space
                    .setOuterSpaceOreBlock(tMDD, pWorld, tX, tY, tZ, this.mSporadicMeta + 16000);
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
