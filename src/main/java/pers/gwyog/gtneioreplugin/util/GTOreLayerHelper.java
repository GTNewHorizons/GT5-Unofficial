package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import sun.awt.windows.WWindowPeer;

public class GTOreLayerHelper {
	public static boolean restrictBiomeSupport = false;
	public static boolean endAsteroidSupport = false;
	public static boolean gcBasicSupport = false;
	public static boolean gcAsteroidSupport = false;
	public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();
	public static List<List<Short>> listVein = new ArrayList<List<Short>>();
	public static List<List<Short>> listAsteroid = new ArrayList<List<Short>>();

	public GTOreLayerHelper() {
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
	}
	
	public class OreLayerWrapper {
		public String veinName;
	    public short primaryMeta;
	    public short secondaryMeta;
	    public short betweenMeta;
	    public short sporadicMeta;
		public String worldGenHeightRange;
	    public String weightedChance;
	    public String restrictBiome;
	    public boolean genOverworld = false;
	    public boolean genNether = false;
	    public boolean genEnd = false;
	    public boolean genMoon = false;
	    public boolean genMars = false;
	    public boolean genEndAsteroid = false;
	    public boolean genGCAsteroid = false;
	    
	    public OreLayerWrapper(GT_Worldgen_GT_Ore_Layer worldGen) {
	    	this.veinName = worldGen.mWorldGenName;
	    	this.primaryMeta = worldGen.mPrimaryMeta;
	    	this.secondaryMeta = worldGen.mSecondaryMeta;
	    	this.betweenMeta = worldGen.mBetweenMeta;
	    	this.sporadicMeta = worldGen.mSporadicMeta;
	    	this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
	    	this.weightedChance = String.format("%.2f%%", (100.0f*worldGen.mWeight)/GT_Worldgen_GT_Ore_Layer.sWeight);
	    	this.genOverworld = worldGen.mOverworld;
	    	this.genNether = worldGen.mNether;
	    	this.genEnd = worldGen.mEnd;
	    	if (restrictBiomeSupport) 
	    		this.restrictBiome = worldGen.mRestrictBiome;
	    	if (GTOreLayerHelper.gcBasicSupport) {
	    		this.genMoon = worldGen.mMoon;
	    		this.genMars = worldGen.mMars;
	    	}
			if (GTOreLayerHelper.endAsteroidSupport)
	    		this.genEndAsteroid = worldGen.mEndAsteroid;
	    	if (GTOreLayerHelper.gcAsteroidSupport) 
	    		this.genGCAsteroid = worldGen.mAsteroid;
	    	List<Short> list = new ArrayList<Short>();
	    	list.add(primaryMeta);
	    	list.add(secondaryMeta);
	    	list.add(betweenMeta);
	    	list.add(sporadicMeta);
	    	listVein.add(list);
	    	if (genEndAsteroid || genGCAsteroid) 
	    		listAsteroid.add(list);
	    }
	}
	
}
