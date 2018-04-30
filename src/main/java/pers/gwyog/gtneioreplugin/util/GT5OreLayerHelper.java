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
        public String veinName,worldGenHeightRange, weightedIEChance;
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
    
    public static <T> List<Oremix> Combsort(List<Oremix> liste) {
    	List<Oremix> liste2 = new ArrayList<Oremix>(liste.size());
    	for (Oremix element : liste) {
    		liste2.add(element);
    	}
    	
    	int schritt = liste2.size();
    	boolean vertauscht = false;
    	do {
    		vertauscht = false;
    		if (schritt > 1) {
    			schritt = (int) (schritt / 1.3);
    		}
    		for (int i = 0; i < liste2.size() - schritt; i++) {
    			if (liste2.get(i).getOreName().substring(0, 3).compareTo((liste2.get(i + schritt).getOreName().substring(0, 3))) > 0) {
    				T tmp = (T) liste2.get(i);
    				liste2.set(i, liste2.get(i + schritt));
    				liste2.set(i + schritt, (Oremix) tmp);
    				vertauscht = true;
    			}
    		}
    	} while (vertauscht || schritt > 1);
    	return liste2;
    }
    
    public static void make_csv() {
        	Iterator it = mapOreLayerWrapper.entrySet().iterator();
        	while (it.hasNext()) {
        		Oremix oremix = new Oremix();
        		
                Map.Entry pair = (Map.Entry)it.next();
                String Dims = PluginGT5VeinStat.getDims((OreLayerWrapper)pair.getValue()); 
                OreLayerWrapper oreLayer = (OreLayerWrapper) pair.getValue();
                oremix.setOreName(oreLayer.veinName.split("\\.")[2]);
                oremix.setPrimary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[0]));
                oremix.setSecondary(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[1]));
                oremix.setInbetween(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[2]));
                oremix.setAround(PluginGT5VeinStat.getGTOreLocalizedName(oreLayer.Meta[3]));
                oremix.setSize(oreLayer.size);
                oremix.setHeight(oreLayer.worldGenHeightRange);
                oremix.setDensity(oreLayer.density);
                oremix.setWeight(oreLayer.randomWeight);
                oremix.setMix(Integer.toString(oreLayer.Meta[0])+"|"+Integer.toString(oreLayer.Meta[1])+"|"+Integer.toString(oreLayer.Meta[2])+"|"+Integer.toString(oreLayer.Meta[3]));
                oremix.as=Dims.contains("As");
                oremix.bc=Dims.contains("BC");
                oremix.be=Dims.contains("BE");
                oremix.bf=Dims.contains("BF");
                oremix.ca=Dims.contains("Ca");
                oremix.cb=Dims.contains("CA");
                oremix.ce=Dims.contains("Ce");
                oremix.dd=Dims.contains("DD");
                oremix.de=Dims.contains("De");
                oremix.ea=Dims.contains("EA");
                oremix.en=Dims.contains("En");
                oremix.eu=Dims.contains("Eu");
                oremix.ga=Dims.contains("Ga");
                oremix.ha=Dims.contains("Ha");
                oremix.io=Dims.contains("Io");
                oremix.kb=Dims.contains("KB");
                oremix.make=Dims.contains("MM");
                oremix.ma=Dims.contains("Ma");
                oremix.me=Dims.contains("Me");
                oremix.mi=Dims.contains("Mi");
                oremix.mo=Dims.contains("Mo");
                oremix.ob=Dims.contains("Ob");
                oremix.ph=Dims.contains("Ph");
                oremix.pl=Dims.contains("Pl");
                oremix.pr=Dims.contains("Pr");
                oremix.tcetie=Dims.contains("TE");
                oremix.tf=Dims.contains("TF");
                oremix.ti=Dims.contains("Ti");
                oremix.tr=Dims.contains("Tr");
                oremix.vb=Dims.contains("VB");
                oremix.ve=Dims.contains("Ve");
                oremix.setOverworld(Dims.contains("Ow"));
                oremix.setNether(Dims.contains("Ne"));
                oremix.setEnd(Dims.contains("EN"));
                GTNEIOrePlugin.OreVeins.add(oremix);
               
                
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
        	}
        	BufferedWriter one = null;
            try {
        					one = Files.newBufferedWriter(Paths.get(GTNEIOrePlugin.CSVname));
        				} catch (IOException e1) {
        					e1.printStackTrace();
        				}
           
            ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
            strat.setType(Oremix.class);
            String[] columns = "ORENAME,PRIMARY,SECONDARY,INBETWEEN,AROUND,mix,TIER,HEIGHT,DENSITY,SIZE,WEIGHT,overworld,nether,end,ea,tf,mo,ma,ph,de,as,ce,eu,ga,ca,io,ve,me,en,ti,mi,ob,pr,tr,pl,kb,ha,make,dd,cb,vb,bc,be,bf,tcetie".split("\\,");
            strat.setColumnMapping(columns);
            		StatefulBeanToCsv<Oremix> beanToCsv = new StatefulBeanToCsvBuilder(one)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(strat)
                    .build();
            		List towrite = Combsort(GTNEIOrePlugin.OreVeins);
                	    try {
                	    	one.write("Ore Name,Primary,Secondary,Inbetween,Around,ID,Tier,Height,Density,Size,Weight,Overworld,Nether,End,End Asteroids,Twilight Forest,Moon,Mars,Phobos,Deimos,Asteroids,Ceres,Europa,Ganymede,Callisto,Io,Venus,Mercury,Enceladus,Titan,Miranda,Oberon,Proteus,Triton,Pluto,Kuiper Belt,Haumea,Makemake,Deep Dark,Centauri Bb,Vega B,Barnard C,Barnard E,Barnard F,T Ceti E");
                	    	one.newLine();
                	    	beanToCsv.write(towrite);
                	    	} catch (Exception e) {
        					e.printStackTrace();
        				}
                	    try {
                	    	one.flush();
        					one.close();
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}       
    	}
    }
    