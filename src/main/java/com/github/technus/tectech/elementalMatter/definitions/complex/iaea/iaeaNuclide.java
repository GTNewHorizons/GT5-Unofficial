package com.github.technus.tectech.elementalMatter.definitions.complex.iaea;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.auxiliary.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition.STABLE_RAW_LIFE_TIME;

public final class iaeaNuclide {
    public static final double AMU_TO_EV_DIV_C_C=9.31494061E08D,MICRO_AMU_TO_EV_DIV_C_C=9.31494061E02D;

    //Nuclide			 T1/2 	 T1/2 [s]	 Decay Modes		  ? 	 Q 	 Q?-	 Q?	 QEC	 Q?- n	 Sn	 Sp	 Binding/A	 Atomic Mass	 Mass Excess	 Discovery
    //Abund. [mole fract.]		 BR [%]		[?N]	[barn]	[keV]	[keV]	[keV]	[keV]	[keV]	[keV]	[keV]	[? AMU]	[keV]
    private static final HashMap<Integer,iaeaNuclide> NUCLIDES=new HashMap<>();

    public static void run(){
        try {
            ResourceLocation loc = new ResourceLocation(Reference.MODID+":nuclides.tsv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
            ArrayList<String[]> blockOfData=new ArrayList<>(4);
            String line;
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,"\t");
                if(split.length!=19) throw new Error("Invalid count ("+split.length+") of separators in IAEA database " + line);
                if(split[1].length()>0 && !blockOfData.isEmpty()) {
                    new iaeaNuclide(blockOfData.toArray(new String[0][]));
                    blockOfData.clear();
                }
                blockOfData.add(split);
            }
            if(!blockOfData.isEmpty()) {
                new iaeaNuclide(blockOfData.toArray(new String[0][]));
                blockOfData.clear();
            }
        }catch (Throwable e){
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
    }

    public static iaeaNuclide get(int protons, int neutrons){
        return NUCLIDES.get((protons<<16)+neutrons);
    }

    public final short N,Z;
    public final float Thalf;//sec
    //public final HashMap<String,Float> decaymodes;

    public final float mass;//eV/c^2
    public final short discovery;//year


    private iaeaNuclide(String[][] rows){
        N=Short.parseShort(rows[1][2]);
        Z=Short.parseShort(rows[1][0]);
        NUCLIDES.put((((int)Z)<<16)+N,this);


        String[] parts = Util.splitButDifferent(rows[0][16], "|");
        double Mass=doubleOrNaN(parts[0],"mass");
        if(Mass!=Double.NaN) mass = (float)(Mass*MICRO_AMU_TO_EV_DIV_C_C);
        else mass=Float.NaN;

        discovery=(short)doubleOrNaN(rows[0][18],"discovery");

        if(rows[0][3].contains("STABLE")){
            Thalf= STABLE_RAW_LIFE_TIME;
        }else{
            parts = Util.splitButDifferent(rows[0][4], "|");
            Thalf = (float)doubleOrNaN(parts[0],"half life");
        }


        //for(String[] S:rows) {
        //    for(String s:S)System.out.print(s+" ");
        //    System.out.println();
        //}
        //System.out.println("KEKEKEK");
    }

    private double doubleOrNaN(String s, String name){
        s=s.replaceAll("#","");
        if(s.length()>0) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.println("Invalid Value " + name + " " + N + " " + Z + " " + s);
                return Double.NaN;
            }
        }
        return Double.NaN;
    }
}
