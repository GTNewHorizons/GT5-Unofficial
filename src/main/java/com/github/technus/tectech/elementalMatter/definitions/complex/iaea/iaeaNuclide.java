package com.github.technus.tectech.elementalMatter.definitions.complex.iaea;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.auxiliary.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition.STABLE_RAW_LIFE_TIME;

public final class iaeaNuclide {
    public static final double AMU_TO_EV_DIV_C_C=9.31494061E08D,MICRO_AMU_TO_EV_DIV_C_C=9.31494061E02D;

    //Nuclide			 T1/2 	 T1/2 [s]	 Decay Modes		  ? 	 Q 	 Q?-	 Q?	 QEC	 Q?- n	 Sn	 Sp	 Binding/A	 Atomic Mass	 Mass Excess	 Discovery
    //Abund. [mole fract.]		 BR [%]		[?N]	[barn]	[keV]	[keV]	[keV]	[keV]	[keV]	[keV]	[keV]	[? AMU]	[keV]

    //Z,N,symb,radius, unc, energy, unc, jp, half-life operator, half_life, unc,unit, half_life [s], unc, decay, decay %, unc, decay, decay %, unc, decay, decay %, unc,isospin,magn. dipole, unc, elect. quad , unc,Qb-,unc,Qb- n,unc,Qa, unc, Qec, unc,Sn,unc, Sp,unc,Binding/A,unc,atomic mass, unc, mass excess,unc,
    //Z,N,symbol,energy , unc, jp,half-life operator, half_life, unc,unit, half_life [s], unc, decay, decay %, unc, decay, decay %, unc, decay, decay %, unc,isospin,magn. dipole, unc, elect. quadrupole , unc,
    private static final HashMap<Integer,iaeaNuclide> NUCLIDES=new HashMap<>();

    public static void run(){
        String line="";

        try {
            ResourceLocation loc = new ResourceLocation(Reference.MODID+":nuclides.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
            ArrayList<String[]> blockOfData=new ArrayList<>(4);
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=19) throw new Error("Invalid count ("+split.length+") of separators in IAEA nuclides database " + line);
                if(split[1].length()>0 && blockOfData.size()>0) {
                    new iaeaNuclide(blockOfData.toArray(new String[0][]));
                    blockOfData.clear();
                }
                blockOfData.add(split);
            }
            if(blockOfData.size()>0) {
                new iaeaNuclide(blockOfData.toArray(new String[0][]));
                blockOfData.clear();
            }
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        try {
            ResourceLocation loc = new ResourceLocation(Reference.MODID+":nuclidesTable.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=47) throw new Error("Invalid count ("+split.length+") of separators in IAEA nuvlidesTable database " + line);
                get(Integer.parseInt(split[0]),Integer.parseInt(split[1])).getMoreData(split);
            }
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        try {
            ResourceLocation loc = new ResourceLocation(Reference.MODID+":energyLevels.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=27) throw new Error("Invalid count ("+split.length+") of separators in IAEA energyLevels database " + line);
                new energeticState(split);
            }
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        for(String s:decays){
            System.out.println(s);
        }

        for(iaeaNuclide nuclide:NUCLIDES.values()){
            //todo fix decays
        }
    }

    public static iaeaNuclide get(int protons, int neutrons){
        return NUCLIDES.get((protons<<16)+neutrons);
    }

    public final short N,Z;
    public final float Thalf;//sec
    public final float mass;//eV/c^2
    public final short discovery;//year
    public TreeMap<Float,energeticState> energeticStates;


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
    }

    private void getMoreData(String[] cells){
        if(add(cells[14])); //System.out.println(Z+" "+N);
        if(add(cells[17])); //System.out.println(Z+" "+N);
        if(add(cells[20])); //System.out.println(Z+" "+N);
        TreeMap<Float,String> decaymodes=new TreeMap<>();

        new energeticState(this,Thalf,decaymodes);
    }

    private double doubleOrNaN(String s, String name){
        s=s.replaceAll("#","");
        if(s.length()>0) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.println("Invalid Value " + name + " " + N + " " + Z + " " + s);
                e.printStackTrace();
            }
        }
        return Double.NaN;
    }

    public static class energeticState{
        public final float energy;
        public final float Thalf;
        public TreeMap<Float,String> decaymodes;

        private energeticState(iaeaNuclide nuclide,float Thalf,TreeMap<Float,String> decaymodes){
            energy=0;
            this.Thalf=Thalf;
            this.decaymodes=decaymodes;
            if(nuclide.energeticStates==null)
                nuclide.energeticStates=new TreeMap<>();
            nuclide.energeticStates.put(energy,this);
        }

        private energeticState(String[] cells){
            iaeaNuclide nuclide=get((int)doubleOrNaN(cells[0],"protons"),(int)doubleOrNaN(cells[1],"neutrons"));
            if(nuclide==null)
                throw new Error("Missing nuclide "+(int)doubleOrNaN(cells[0],"protons")+" "+(int)doubleOrNaN(cells[1],"neutrons"));
            this.energy=(float) (doubleOrNaN(cells[3],"energy level",nuclide)*1000);//to eV
            this.Thalf=(float) doubleOrNaN(cells[10],"half life",nuclide);
            if(nuclide.energeticStates==null) {
                new Exception("Should be initialized before doing this... "+ nuclide.N +" "+nuclide.Z).printStackTrace();
                nuclide.energeticStates = new TreeMap<>();
            }
            nuclide.energeticStates.put(energy,this);

            if(add(cells[12])); //System.out.println(nuclide.Z+" "+nuclide.N);
            if(add(cells[15])); //System.out.println(nuclide.Z+" "+nuclide.N);
            if(add(cells[18])); //System.out.println(nuclide.Z+" "+nuclide.N);
        }

        private double doubleOrNaN(String s, String name){
            return doubleOrNaN(s,name,null);
        }

        private double doubleOrNaN(String s, String name, iaeaNuclide nuclide){
            s = s.replaceAll("#", "");
            if (s.length() > 0) {
                try {
                    return Double.parseDouble(s);
                } catch (Exception e) {
                    if(nuclide==null){
                        System.out.println("Invalid Value " + name + " " + s);
                    }else {
                        System.out.println("Invalid Value " + name + " " + nuclide.N + " " + nuclide.Z + " " + s);
                    }
                    e.printStackTrace();
                }
            }
            return Double.NaN;
        }
    }

    private static HashSet<String> decays=new HashSet<>();
    private static boolean add(String s){
        int len=decays.size();
        decays.add(s);
        if(decays.size()>len){
            System.out.println(s);
            return true;
        }
        return false;
    }
    public enum decayType{
        ;
        public final String name;
        decayType(String name){
            this.name=name;
        }
    }
}
