package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.Loader;
import gregapi.block.IBlockPlacable;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import gregapi.data.OP;
import gregapi.lang.LanguageHandler;
import gregapi.worldgen.Worldgen_GT_Ore_Layer;
import gregtech.GT_Mod;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class GT6OreLayerHelper {
    public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();
    public static HashMap<Short, String> mapMetaToLocalizedName = new HashMap<Short, String>();
    public static Set<PrefixBlock> setOreNormalBasicTypes = new HashSet<PrefixBlock>();
    
    public GT6OreLayerHelper() {
        for (Worldgen_GT_Ore_Layer tWorldGen: Worldgen_GT_Ore_Layer.sList)
            mapOreLayerWrapper.put(tWorldGen.mWorldGenName, new OreLayerWrapper(tWorldGen));
        Set<IBlockPlacable> setNormalOreBasicTypesTemp = new HashSet<IBlockPlacable>(CS.BlocksGT.stoneToNormalOres.values());
        for (IBlockPlacable block: setNormalOreBasicTypesTemp)
            if (block instanceof PrefixBlock)
                setOreNormalBasicTypes.add((PrefixBlock)block);
    }
    
    public static String getLocalizedOreName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.getLocalName(OP.ore, CS.BlocksGT.ore.getMetaMaterial(meta));
    }   
    
    public class OreLayerWrapper {
        public String veinName;
        public short primaryMeta;
        public short secondaryMeta;
        public short betweenMeta;
        public short sporadicMeta;
        public String worldGenHeightRange;
        public String weightedChance;
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
            this.weightedChance = String.format("%.2f%%", (100.0f*worldGen.mWeight)/Worldgen_GT_Ore_Layer.sWeight);
            this.genOverworld = worldGen.mOverworld;
            this.genNether = worldGen.mNether;
            this.genEnd = worldGen.mEnd;
            if (!mapMetaToLocalizedName.keySet().contains(primaryMeta))
                mapMetaToLocalizedName.put(primaryMeta, getLocalizedOreName(primaryMeta));
            if (!mapMetaToLocalizedName.keySet().contains(secondaryMeta))
                mapMetaToLocalizedName.put(secondaryMeta, getLocalizedOreName(secondaryMeta));
            if (!mapMetaToLocalizedName.keySet().contains(betweenMeta))
                mapMetaToLocalizedName.put(betweenMeta, getLocalizedOreName(betweenMeta));
            if (!mapMetaToLocalizedName.keySet().contains(sporadicMeta))
                mapMetaToLocalizedName.put(sporadicMeta, getLocalizedOreName(sporadicMeta));
        }    
    } 
    
}
