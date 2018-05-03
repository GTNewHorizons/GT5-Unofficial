package pers.gwyog.gtneioreplugin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;

public class GT5OreLayerHelper {
	
    public static Integer weightPerWorld[] = new Integer[33];
    public static Integer DimIDs[] = new Integer[33];
    public static HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<String, OreLayerWrapper>();

    public GT5OreLayerHelper() {
    	for (int i=0; i < DimIDs.length;i++)
    		weightPerWorld[i]=0;
    	for (int i=0; i < DimIDs.length;i++)
    		DimIDs[i]=0;
        for (GT_Worldgen_GT_Ore_Layer tWorldGen: GT_Worldgen_GT_Ore_Layer.sList)
            mapOreLayerWrapper.put(tWorldGen.mWorldGenName, new OreLayerWrapper(tWorldGen));
  }
    
    public class OreLayerWrapper {
        public String veinName, worldGenHeightRange, weightedIEChance;
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
    