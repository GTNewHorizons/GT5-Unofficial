package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class GT5OreLayerHelper {
    public static boolean restrictBiomeSupport = false;
    public static boolean endAsteroidSupport = false;
    public static boolean gcBasicSupport = false;
    public static boolean gcAsteroidSupport = false;
    public static boolean immersiveEngineeringSupport = false;
    public static int[] weightPerWorld = {0, 0, 0, 0, 0};
    public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();

    public GT5OreLayerHelper() {
        checkExtraSupport();
        for (GT_Worldgen_GT_Ore_Layer tWorldGen: GT_Worldgen_GT_Ore_Layer.sList)
            mapOreLayerWrapper.put(tWorldGen.mWorldGenName, new OreLayerWrapper(tWorldGen));
  }
    
    private static void checkExtraSupport() {
        Class clazzGTOreLayer = null;
        try {
            clazzGTOreLayer = Class.forName("gregtech.common.GT_Worldgen_GT_Ore_Layer");
        } catch (ClassNotFoundException e) {}
        if (clazzGTOreLayer != null) {
            try {
                Field fieldRestrictBiome = clazzGTOreLayer.getField("mRestrictBiome");
                restrictBiomeSupport = true;
            } catch (Exception e) {}
            try {
                Field fieldEndAsteroid = clazzGTOreLayer.getField("mEndAsteroid");
                endAsteroidSupport = true;
            } catch (Exception e) {}
            try {
                Field fieldGCMoon = clazzGTOreLayer.getField("mMoon");
                Field fieldGCMars = clazzGTOreLayer.getField("mMars");
                gcBasicSupport = true;
            } catch (Exception e) {}
            try {
                Field fieldGCAsteroid = clazzGTOreLayer.getField("mAsteroid");
                gcAsteroidSupport = true;
            } catch (Exception e) {}
        }
        
        // immersive engineering support for GT5.09.25+
        if (Loader.instance().isModLoaded("ImmersiveEngineering")) {
            Class clazzGTMod = null;
            Class clazzGTProxy = null;
            Class clazzGTAPI = null;
            try {
                clazzGTMod = Class.forName("gregtech.GT_Mod");
                clazzGTProxy = Class.forName("gregtech.common.GT_Proxy");
                clazzGTAPI = Class.forName("gregtech.api.GregTech_API");
            } catch (ClassNotFoundException e) {}
            if (clazzGTMod!=null && clazzGTProxy!=null && clazzGTAPI!=null) {
                try {
                    Field fieldImmersiveEngineeringRecipes = clazzGTAPI.getField("mImmersiveEngineering");
                    Field fieldGTProxy = clazzGTMod.getField("gregtechproxy");
                    Field fieldImmersiveEngineering = clazzGTProxy.getField("mImmersiveEngineeringRecipes");
                    immersiveEngineeringSupport = GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes;
                } catch (Exception e) {}
            }
        }
    }
    
    public class OreLayerWrapper {
        public String veinName;
        public short primaryMeta;
        public short secondaryMeta;
        public short betweenMeta;
        public short sporadicMeta;
        public String worldGenHeightRange;
        public String weightedIEChance;
        public String restrictBiome;
        public int randomWeight;
        public boolean genOverworld = false;
        public boolean genNether = false;
        public boolean genEnd = false;
        public boolean genMoon = false;
        public boolean genMars = false;
        public boolean genEndAsteroid = false;
        public boolean genGCAsteroid = false;
        public boolean genIEVein = false;
        
        public OreLayerWrapper(GT_Worldgen_GT_Ore_Layer worldGen) {
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
            if (restrictBiomeSupport) 
                this.restrictBiome = worldGen.mRestrictBiome;
            if (GT5OreLayerHelper.gcBasicSupport) {
                this.genMoon = worldGen.mMoon;
                this.genMars = worldGen.mMars;
                weightPerWorld[3] += this.genMoon ? this.randomWeight : 0;
                weightPerWorld[4] += this.genMars ? this.randomWeight : 0;
            }
            if (GT5OreLayerHelper.endAsteroidSupport)
                this.genEndAsteroid = worldGen.mEndAsteroid;
            if (GT5OreLayerHelper.gcAsteroidSupport) 
                this.genGCAsteroid = worldGen.mAsteroid;

            // immersive engineering support for GT5.09.25+
            if (immersiveEngineeringSupport) {
                this.genIEVein = true;
                this.weightedIEChance = String.format("%.3f%%", (100.0f*worldGen.mWeight)/GT_Worldgen_GT_Ore_Layer.sWeight/8);
            }
        }
        
    }
    
}
