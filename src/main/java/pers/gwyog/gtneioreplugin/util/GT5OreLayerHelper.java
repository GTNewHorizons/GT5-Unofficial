package pers.gwyog.gtneioreplugin.util;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GT5OreLayerHelper {

    public static Integer weightPerWorld[] = new Integer[33];
    public static Integer DimIDs[] = new Integer[33];
    public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();
    public static HashMap<OreLayerWrapper, String> bufferedDims = new HashMap<>();

    public GT5OreLayerHelper() {
        for (int i = 0; i < DimIDs.length; i++)
            weightPerWorld[i] = 0;
        for (int i = 0; i < DimIDs.length; i++)
            DimIDs[i] = 0;
        for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList)
            mapOreLayerWrapper.put(tWorldGen.mWorldGenName, new OreLayerWrapper(tWorldGen));
        for (OreLayerWrapper layer : mapOreLayerWrapper.values()) {
            bufferedDims.put(layer, getDims(layer));
        }
    }

    public static String getDims(OreLayerWrapper oreLayer) {
        return GT5CFGHelper.GT5CFG(oreLayer.veinName.replace("ore.mix.custom.", "").replace("ore.mix.", ""));
    }


    public class OreLayerWrapper {
        public String veinName, worldGenHeightRange;
        public short[] Meta = new short[4];
        public short randomWeight, size, density;
        public List<Integer> Weight = new ArrayList<Integer>();

        public OreLayerWrapper(GT_Worldgen_GT_Ore_Layer worldGen) {
            this.veinName = worldGen.mWorldGenName;
            this.Meta[0] = worldGen.mPrimaryMeta;
            this.Meta[1] = worldGen.mSecondaryMeta;
            this.Meta[2] = worldGen.mBetweenMeta;
            this.Meta[3] = worldGen.mSporadicMeta;
            this.size = worldGen.mSize;
            this.density = worldGen.mDensity;
            this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
            this.randomWeight = worldGen.mWeight;
        }
    }
}
    