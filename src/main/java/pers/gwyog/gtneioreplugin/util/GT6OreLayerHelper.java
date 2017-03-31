package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import gregapi.block.IBlockPlacable;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import gregapi.worldgen.Worldgen_GT_Ore_Layer;
import gregtech.GT_Mod;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class GT6OreLayerHelper {
    public static int[] weightPerWorld = {0, 0, 0};
    public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();
    public static Set<PrefixBlock> setOreNormalBasicTypes = new HashSet<PrefixBlock>();
    
    public GT6OreLayerHelper() {
        for (Worldgen_GT_Ore_Layer tWorldGen: Worldgen_GT_Ore_Layer.sList)
            mapOreLayerWrapper.put(tWorldGen.mWorldGenName, new OreLayerWrapper(tWorldGen));
        Set<IBlockPlacable> setNormalOreBasicTypesTemp = new HashSet<IBlockPlacable>(CS.BlocksGT.stoneToNormalOres.values());
        for (IBlockPlacable block: setNormalOreBasicTypesTemp)
            if (block instanceof PrefixBlock)
                setOreNormalBasicTypes.add((PrefixBlock)block);
    }  
    
    public class OreLayerWrapper {
        public String veinName;
        public short primaryMeta;
        public short secondaryMeta;
        public short betweenMeta;
        public short sporadicMeta;
        public String worldGenHeightRange;
        public int randomWeight;
        public boolean genOverworld = false;
        public boolean genNether = false;
        public boolean genEnd = false;

        public OreLayerWrapper(Worldgen_GT_Ore_Layer worldGen) {
            this.veinName = worldGen.mWorldGenName;
            this.primaryMeta = worldGen.mPrimaryMeta;
            this.secondaryMeta = worldGen.mSecondaryMeta;
            this.betweenMeta = worldGen.mBetweenMeta;
            this.sporadicMeta = worldGen.mSporadicMeta;
            this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
            this.randomWeight = worldGen.mWeight;
            this.genOverworld = worldGen.mOverworld;
            this.genNether = worldGen.mNether;
            this.genEnd = worldGen.mEnd;
            weightPerWorld[0] += this.genOverworld ? this.randomWeight : 0;
            weightPerWorld[1] += this.genNether ? this.randomWeight : 0;
            weightPerWorld[2] += this.genEnd ? this.randomWeight : 0;
        }    
    } 
    
}
