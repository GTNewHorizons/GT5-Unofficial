package pers.gwyog.gtneioreplugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
			FMLLog.bigWarning("GT_CFG_NOT_FOUND");
			return "Error while Loading CFG";
		}
		else
		try {
		int buffer = (int) (0.1*Runtime.getRuntime().freeMemory()); 
		if (buffer > F.length())
			buffer = (int) F.length();
		//allocate 10% of free memory for read-in-buffer, if there is less than filesize memory aviable
			//FMLLog.info("GT_CFG_FOUND");
		FileReader in = new FileReader(F);
		//FMLLog.info("FileReader created");
		BufferedReader reader = new BufferedReader(in, buffer);
		//FMLLog.info("BufferedReader" +Integer.toString(buffer)+"created");
		String st="";
		List<String> raw= new ArrayList<String>();
		List<String> rawbools = new ArrayList<String>();
		boolean found = false;
	   
		do{
			//read until reached eof or mix {
	    	st = reader.readLine();
	    	//FMLLog.info("st: "+st);
	    	if ((reader.readLine() != null))
	    	if (st.contains("mix {")) {
	    		do{
	    			//read until reached eof or Veinname {
	    			st = reader.readLine();
	    			//FMLLog.info("MIXst: "+st);
	    			if ((reader.readLine() != null))
	    			if (st.contains(Veinname)) {
	    				//FMLLog.info("VEINNAMEst: "+st);
	    			//for (int i=0; i < 44;i++)
	    			do{
	    				//add everything below Veinname { undtil } to raw
	    			raw.add(reader.readLine());
	    			}while (!reader.readLine().contains("}")&&(reader.readLine() != null));
	    			found = true;
	    			}
	    			}while((!found) || (reader.readLine() != null));
	        }
	    }while((!found) || (reader.readLine() != null));
	    
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
	    
	    //HashSet<String> rawboolsset = new HashSet<String>();
	    if (!rawbools.isEmpty()) {
	    	//remove dublicats
	    	/*
	    	for (int i=0; i < rawbools.size();i++){
	    		st = rawbools.get(i).replace("B:", "").replace("_true", "").replace("_false", "").replaceAll(" ", "");
	    		rawboolsset.add(st);
	    	}
	    	rawbools = new ArrayList<String>(rawboolsset);
	    	*/
	    	//filter for dims set to true
	    	for (int i=0; i < rawbools.size();i++) {
	    		st = rawbools.get(i);
	    		//FMLLog.info("RawBools:"+st);
	    		for (int j=0; j < DimensionHelper.DimName.length;j++) {
    				if(st.contains(DimensionHelper.DimName[j]))
    				if(st.contains("=true"))
    				ret=(ret+DimensionHelper.DimNameDisplayed[j]+" ");
    			}
	    	}
	    }
	    ret = ret.trim();
	    //FMLLog.info("ret:"+ret);
	    if(ret.equals("")||ret.equals(" "))
	    ret ="Not aviable in any Galactic Dim, maybe Deep Dark or Bedrock Dim";
        return ret;
		} catch (IOException e) {
		    e.printStackTrace();
		    return "Error while Loading CFG";
		}
	}
}
