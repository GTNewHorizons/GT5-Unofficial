package pers.gwyog.gtneioreplugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.common.FMLLog;


public class GT5CFGHelper {
	
	public static String GT5CFG(File F, String Veinname) {
		//FMLLog.info(Veinname);
		if (F == null) {
			FMLLog.bigWarning("GT_CFG_NOT_found[0]");
			return "Error while Loading CFG";
		}
		else
		try {
		int buffer = (int) (0.1*Runtime.getRuntime().freeMemory()); 
		if (buffer > F.length())
			buffer = (int) F.length();
		//allocate 10% of free memory for read-in-buffer, if there is less than filesize memory aviable
			//FMLLog.info("GT_CFG_found[0]");
		FileReader in = new FileReader(F);
		//FMLLog.info("FileReader created");
		BufferedReader reader = new BufferedReader(in, buffer);
		//FMLLog.info("BufferedReader" +Integer.toString(buffer)+"created");
		String st=null;
		List<String> raw= new ArrayList<String>();
		List<String> rawbools = new ArrayList<String>();
		Boolean[] found = new Boolean[2];
		found[0] = false;
		found[1] = false;
	   
		do{
			//FMLLog.info("erste");
			//read until reached eof or mix {
	    	st = reader.readLine();
	    	//FMLLog.info("st: "+st);
	    	if (st != null && st.trim().equals("mix {")) {
	    		while(!((st == null)||((st != null)&&found[0]))){
	    			//FMLLog.info("zweite");
	    			st = reader.readLine();
	    			//read until reached eof or Veinname {
	    			//FMLLog.info("MIXst: "+st);
	    			if (st != null && st.trim().equals(Veinname+" {")) {
	    			//FMLLog.info("VEINNAMEst: "+st);
	    				while (!((st == null)||((st != null) && found[0]))){
	    					st = reader.readLine();
	    					if ((!(st == null)) && st.trim().equals("}"))
	    						found[0] = true;
	    					//FMLLog.info("dritte");
	    					//add everything below Veinname { undtil } to raw
	    					raw.add(st);
	    				   }
	    				}
	    			}
	        }
	    	
	    	if (st != null && st.trim().equals("dimensions {")) {
	    		while(!((st == null)||((st != null)&&found[1]))){
	    			//FMLLog.info("zweite");
	    			st = reader.readLine();
	    			if (st != null && (st.trim().equals("mix {"))) {
	    				while(!((st == null)||((st != null)&&found[1]))){
	    					//FMLLog.info("dritte");
	    					st = reader.readLine();
	    					//read until reached eof or Veinname {
	    					//FMLLog.info("MIXst: "+st);
	    					if (st != null && st.trim().equals(Veinname+" {")) {
	    						//FMLLog.info("VEINNAMEst: "+st);
	    						while (!((st == null)||((st != null)&&found[1]))){
	    							st = reader.readLine();
	    							if ((!(st == null)) && st.trim().equals("}"))
	    								found[1] = true;
	    							//FMLLog.info("vierte");
	    							//add everything below Veinname { undtil } to raw
	    							raw.add(st);
	    				   		}
	    					}
	    				}
	    			}
	    		}
	    	}
	    }while(st != null);
		reader.close();//not needed anymore
	    
	    if (!raw.isEmpty())
	    for (int i=0; i < raw.size();i++) {
	    	//filter needed booleans from raw
	    	///FMLLog.info("raw contains"+raw.get(i));
	    	for (int j=0; j < DimensionHelper.DimName.length;j++)
	    	if(raw.get(i).contains(DimensionHelper.DimName[j]))
	    			rawbools.add(raw.get(i));
	    	//FMLLog.info("rawbools: "+rawbools.get(i));
	    }
	    else FMLLog.info("raw is empty");
	    
	    String ret=" ";
	    
	    HashSet<String> rawboolsset = new HashSet<String>();
	    if (!rawbools.isEmpty()) {
	    	//remove dublicats
	    	for (int i=0; i < rawbools.size();i++){
	    		st = rawbools.get(i).replace("B:", "").replace("_true", "").replace("_false", "").replaceAll(" ", "").replaceAll("\"", "");
	    		rawboolsset.add(st);
	    	}
	    	rawbools = new ArrayList<String>(rawboolsset);
	    	//filter for dims set to true
	    	for (int i=0; i < rawbools.size();i++) {
	    		st = rawbools.get(i);
	    		//FMLLog.info("RawBools:"+st);
	    		for (int j=0; j < DimensionHelper.DimName.length;j++) {
    				if(st.contains(DimensionHelper.DimName[j]))
    				if(st.contains("=true"))
    				ret=(ret+DimensionHelper.DimNameDisplayed[j]+",");
    			}
	    	}
	    }
	    ret = ret.trim();
	    //FMLLog.info("ret:"+ret);
	    if(ret.equals("")||ret.equals(" "))
	    ret ="Not aviable in any Galactic Dim!";
        return ret;
		} catch (IOException e) {
		    e.printStackTrace();
		    return "Error while Loading CFG";
		}
	}
}
