package pers.gwyog.gtneioreplugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.FMLLog;
import gregtech.api.GregTech_API;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class GT5CFGHelper {

    // Do NOT ever put a comma in this or it will break split calls later in the code. Bad, but it is what it is.
    public static String oreVeinNotInAnyDim = "Not available in any Galactic Dim!";

    private static File F = GregTech_API.sWorldgenFile.mConfig.getConfigFile();

    public static String GT5CFGSmallOres(String Veinname) {
        List<String> raw = new ArrayList<String>();
        List<String> rawbools = new ArrayList<String>();
        String st = null;
        Configuration c = new Configuration(F);
        ConfigCategory configCategory = c.getCategory("worldgen." + Veinname);
        for (Property p : configCategory.getOrderedValues()) {
            if (p.isBooleanValue() && p.getBoolean()) {
                raw.add(p.getName() + "=" + p.getBoolean());
            }
        }
        if (!raw.isEmpty()) for (int i = 0; i < raw.size(); i++) {
            for (int j = 0; j < DimensionHelper.DimName.length; j++)
                if (raw.get(i).contains(DimensionHelper.DimName[j])) rawbools.add(raw.get(i));
        }
        else GTNEIOrePlugin.LOG.info("Config entry not found for Vein: " + Veinname);

        String ret = " ";

        HashSet<String> rawboolsset = new HashSet<String>();
        if (!rawbools.isEmpty()) {
            for (int i = 0; i < rawbools.size(); i++) {
                st = rawbools.get(i).replace("B:", "").replace("_true", "").replace("_false", "").replaceAll(" ", "")
                        .replaceAll("\"", "");
                rawboolsset.add(st);
            }
            rawbools = new ArrayList<String>(rawboolsset);
            for (int j = 0; j < DimensionHelper.DimName.length; j++) {
                for (int i = 0; i < rawbools.size(); i++) {
                    st = rawbools.get(i);
                    if (st.contains(DimensionHelper.DimName[j]))
                        if (st.contains("=true")) ret = (ret + DimensionHelper.DimNameDisplayed[j] + ",");
                }
            }
        }
        ret = ret.trim();
        if (ret.equals("") || ret.equals(" ")) ret = oreVeinNotInAnyDim;
        return ret;
    }

    public static String GT5CFG(String Veinname) {
        // FMLLog.info(Veinname);
        if (F == null) {
            FMLLog.bigWarning("GT_CFG_NOT_found[0]");
            return "Error while Loading CFG";
        } else try {
            int buffer = (int) (0.1 * Runtime.getRuntime().freeMemory());
            if (buffer > F.length()) buffer = (int) F.length();
            // allocate 10% of free memory for read-in-buffer, if there is less than filesize memory available
            // FMLLog.info("GT_CFG_found[0]");
            FileReader in = new FileReader(F);
            // FMLLog.info("FileReader created");
            BufferedReader reader = new BufferedReader(in, buffer);
            // FMLLog.info("BufferedReader" +Integer.toString(buffer)+"created");
            String st = null;
            List<String> raw = new ArrayList<String>();
            List<String> rawbools = new ArrayList<String>();
            Boolean[] found = new Boolean[2];
            found[0] = false;
            found[1] = false;

            do {
                // FMLLog.info("erste");
                // read until reached eof or mix {
                st = reader.readLine();
                // FMLLog.info("st: "+st);
                if (st != null && st.trim().equals("mix {")) {
                    while (!((st == null) || ((st != null) && found[0]))) {
                        // FMLLog.info("zweite");
                        st = reader.readLine();
                        // read until reached eof or Veinname {
                        // FMLLog.info("MIXst: "+st);
                        if (st != null && st.trim().equals(Veinname + " {")) {
                            // FMLLog.info("VEINNAMEst: "+st);
                            while (!((st == null) || ((st != null) && found[0]))) {
                                st = reader.readLine();
                                if ((!(st == null)) && st.trim().equals("}")) found[0] = true;
                                // FMLLog.info("dritte");
                                // add everything below Veinname { undtil } to raw
                                raw.add(st);
                            }
                        }
                    }
                }

                if (st != null && st.trim().equals("dimensions {")) {
                    while (!((st == null) || ((st != null) && found[1]))) {
                        // FMLLog.info("zweite");
                        st = reader.readLine();
                        if (st != null && (st.trim().equals("mix {"))) {
                            while (!((st == null) || ((st != null) && found[1]))) {
                                // FMLLog.info("dritte");
                                st = reader.readLine();
                                // read until reached eof or Veinname {
                                // FMLLog.info("MIXst: "+st);
                                if (st != null && st.trim().equals(Veinname + " {")) {
                                    // FMLLog.info("VEINNAMEst: "+st);
                                    while (!((st == null) || ((st != null) && found[1]))) {
                                        st = reader.readLine();
                                        if ((!(st == null)) && st.trim().equals("}")) found[1] = true;
                                        // FMLLog.info("vierte");
                                        // add everything below Veinname { undtil } to raw
                                        raw.add(st);
                                    }
                                }
                            }
                        }
                    }
                }
            } while (st != null);
            reader.close(); // not needed anymore

            if (!raw.isEmpty()) for (int i = 0; i < raw.size(); i++) {
                // filter needed booleans from raw
                /// FMLLog.info("raw contains"+raw.get(i));
                for (int j = 0; j < DimensionHelper.DimName.length; j++)
                    if (raw.get(i).contains(DimensionHelper.DimName[j])) rawbools.add(raw.get(i));
                // FMLLog.info("rawbools: "+rawbools.get(i));
            }
            else GTNEIOrePlugin.LOG.info("Config entry not found for Vein: " + Veinname);

            String ret = " ";

            HashSet<String> rawboolsset = new HashSet<String>();
            if (!rawbools.isEmpty()) {
                // remove dublicates
                for (int i = 0; i < rawbools.size(); i++) {
                    st = rawbools.get(i).replace("B:", "").replace("_true", "").replace("_false", "")
                            .replaceAll(" ", "").replaceAll("\"", "");
                    rawboolsset.add(st);
                }
                rawbools = new ArrayList<String>(rawboolsset);
                // filter for dims set to true
                for (int j = 0; j < DimensionHelper.DimName.length; j++) {
                    // FMLLog.info("RawBools:"+st);
                    for (int i = 0; i < rawbools.size(); i++) {
                        st = rawbools.get(i);
                        if (st.contains(DimensionHelper.DimName[j]))
                            if (st.contains("=true")) ret = (ret + DimensionHelper.DimNameDisplayed[j] + ",");
                    }
                }
            }
            ret = ret.trim();
            // FMLLog.info("ret:"+ret);
            if (ret.equals("") || ret.equals(" ")) ret = oreVeinNotInAnyDim;
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while Loading CFG";
        }
    }
}
