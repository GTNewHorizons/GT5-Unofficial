package bloodasp.galacticgreg;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;

public class WorldGenGaGT implements Runnable {

    @Override
    public void run() {
        new GT_Worldgenerator_Space();

        /*
         * This part here enables every GT Small Ore for Space Dims.
         */
        for (int k = 0; k < GT_Worldgen_GT_Ore_SmallPieces.sList.size(); ++k) {
            new GT_Worldgen_GT_Ore_SmallPieces_Space(
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mWorldGenName,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mEnabled,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMinY,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMaxY,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mAmount,
                GT_Worldgen_GT_Ore_SmallPieces.sList.get(k).mMeta);
        }

        /*
         * This part here enables every GT Ore for Space Dims.
         */
        for (int k = 0; k < GT_Worldgen_GT_Ore_Layer.sList.size(); ++k) {
            new GT_Worldgen_GT_Ore_Layer_Space(
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mWorldGenName,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mEnabled,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mMinY,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mMaxY,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mWeight,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mDensity,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSize,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mPrimaryMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSecondaryMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mBetweenMeta,
                GT_Worldgen_GT_Ore_Layer.sList.get(k).mSporadicMeta);
        }
    }

}
