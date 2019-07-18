package com.github.technus.tectech.mechanics.elementalMatter.definitions.complex;

import com.github.technus.tectech.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition.STABLE_RAW_LIFE_TIME;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(iaeaNuclide.class.getResourceAsStream("nuclides.csv")));
            ArrayList<String[]> blockOfData=new ArrayList<>(4);
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=19) {
                    throw new Error("Invalid count (" + split.length + ") of separators in IAEA nuclides database " + line);
                }
                if(!split[1].isEmpty() && !blockOfData.isEmpty()) {
                    new iaeaNuclide(blockOfData.toArray(new String[blockOfData.size()][]));
                    blockOfData.clear();
                }
                blockOfData.add(split);
            }
            if(!blockOfData.isEmpty()) {
                new iaeaNuclide(blockOfData.toArray(new String[blockOfData.size()][]));
                blockOfData.clear();
            }
            reader.close();
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(iaeaNuclide.class.getResourceAsStream("nuclidesTable.csv")));
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=47) {
                    throw new Error("Invalid count (" + split.length + ") of separators in IAEA nuvlidesTable database " + line);
                }
                get(Integer.parseInt(split[0]),Integer.parseInt(split[1])).getMoreData(split);
            }
            reader.close();
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(iaeaNuclide.class.getResourceAsStream("energyLevels.csv")));
            while((line=reader.readLine())!=null) {
                String[] split= Util.splitButDifferent(line,",");
                if(split.length!=27) {
                    throw new Error("Invalid count (" + split.length + ") of separators in IAEA energyLevels database " + line);
                }
                new energeticState(split);
            }
            reader.close();
        }catch (Exception e){
            System.out.println(line);
            e.printStackTrace();
        }

        for(iaeaNuclide nuclide:NUCLIDES.values()) {
            nuclide.makeArrayOfEnergyStates();
        }
    }

    public static iaeaNuclide get(int protons, int neutrons){
        return NUCLIDES.get((protons<<16)+neutrons);
    }

    public final short N,Z;
    public final float halfTime;//sec
    public final float mass;//eV/c^2
    public final short discovery;//year
    private TreeMap<Float,energeticState> energeticStates;
    public energeticState[] energeticStatesArray;


    private iaeaNuclide(String[][] rows){
        N=Short.parseShort(rows[1][2]);
        Z=Short.parseShort(rows[1][0]);
        NUCLIDES.put(((int)Z <<16)+N,this);

        String[] parts = Util.splitButDifferent(rows[0][16], "|");
        double Mass=doubleOrNaN(parts[0],"mass");
        if(!Double.isNaN(Mass)) {
            //System.out.println("Mass =\t" + Mass+"\t"+(N+Z)+"\t"+N+"\t"+Z+"\t"+(Mass/(N+Z)));
            mass = (float)(Mass* MICRO_AMU_TO_EV_DIV_C_C);
        }
        else {
            mass = Float.NaN;
        }

        discovery=(short)doubleOrNaN(rows[0][18],"discovery");

        if(rows[0][3].contains("STABLE")){
            halfTime = STABLE_RAW_LIFE_TIME;
        }else{
            parts = Util.splitButDifferent(rows[0][4], "|");
            halfTime = (float)doubleOrNaN(parts[0],"half life");
        }
    }

    private void getMoreData(String[] cells){
        //if(DEBUG_MODE) {
        //    if (add(cells[14])) System.out.println(N + " " + Z);
        //    if (add(cells[17])) System.out.println(N + " " + Z);
        //    if (add(cells[20])) System.out.println(N + " " + Z);
        //}
        new energeticState(this, halfTime, getDecaysFixed(cells[14],doubleOrNaN(cells[15],"chance1"),cells[17],doubleOrNaN(cells[18],"chance1"),cells[20],doubleOrNaN(cells[21],"chance1")));
    }

    private static final energeticState[] empty=new energeticState[0];
    private void makeArrayOfEnergyStates(){
        if(energeticStates==null || energeticStates.isEmpty()) {
            energeticStatesArray = empty;
        } else {
            energeticStatesArray = energeticStates.values().toArray(new energeticState[0]);
        }
    }

    private double doubleOrNaN(String s, String name){
        s=s.replaceAll("#","");
        if(!s.isEmpty()) {
            try {
                double value=Double.parseDouble(s);
                if(Double.isNaN(value)) {
                    return Double.NaN;
                }
                return value != 0 ?value:Double.NaN;
            } catch (Exception e) {
                System.out.println("Invalid Value " + name + ' ' + N + ' ' + Z + ' ' + s);
                e.printStackTrace();
            }
        }
        return Double.NaN;
    }

    public static final class energeticState{
        public final float energy;
        public final float Thalf;
        public final iaeaDecay[] decaymodes;

        private energeticState(iaeaNuclide nuclide,float Thalf,iaeaDecay[] decaymodes){
            energy=0;
            this.Thalf=Thalf;
            this.decaymodes=decaymodes;
            if(nuclide.energeticStates==null) {
                nuclide.energeticStates = new TreeMap<>();
            }
            nuclide.energeticStates.put(energy,this);
        }

        private energeticState(String[] cells){
            iaeaNuclide nuclide= get((int)doubleOrNaN(cells[0],"protons"),(int)doubleOrNaN(cells[1],"neutrons"));
            if(nuclide==null) {
                throw new Error("Missing nuclide " + (int) doubleOrNaN(cells[0], "protons") + ' ' + (int) doubleOrNaN(cells[1], "neutrons"));
            }
            energy =(float) (doubleOrNaN(cells[3],"energy level",nuclide)*1000f);//to eV
            if(energy<0) {
                throw new Error("Invalid energy " + nuclide.N + ' ' + nuclide.Z + ' ' + cells[3]);
            }
            Thalf =(float) doubleOrNaN(cells[10],"half life",nuclide);
            if(nuclide.energeticStates==null) {
                new Exception("Should be initialized before doing this... "+ nuclide.N + ' ' +nuclide.Z).printStackTrace();
                nuclide.energeticStates = new TreeMap<>();
            }
            nuclide.energeticStates.put(energy,this);
            //if(DEBUG_MODE) {
            //    if (add(cells[12])) System.out.println(nuclide.N + " " + nuclide.Z);
            //    if (add(cells[15])) System.out.println(nuclide.N + " " + nuclide.Z);
            //    if (add(cells[18])) System.out.println(nuclide.N + " " + nuclide.Z);
            //}
            decaymodes = getDecaysFixed(cells[12],doubleOrNaN(cells[13],"chance 1",nuclide),cells[15],doubleOrNaN(cells[16],"chance 2",nuclide),cells[18],doubleOrNaN(cells[19],"chance 3",nuclide));
        }

        private double doubleOrNaN(String s, String name){
            return doubleOrNaN(s,name,null);
        }

        private double doubleOrNaN(String s, String name, iaeaNuclide nuclide){
            s = s.replaceAll("#", "");
            if (!s.isEmpty()) {
                try {
                    return Double.parseDouble(s);
                } catch (Exception e) {
                    if(nuclide==null){
                        System.out.println("Invalid Value " + name + ' ' + s);
                    }else {
                        System.out.println("Invalid Value " + name + ' ' + nuclide.N + ' ' + nuclide.Z + ' ' + s);
                    }
                    e.printStackTrace();
                }
            }
            return Double.NaN;
        }
    }

    private static HashSet<String> decays=new HashSet<>();
    private static boolean add(String s){
        if(decays.add(s)){
            System.out.println(s);
            return true;
        }
        return false;
    }

    private static iaeaDecay[] getDecaysFixed(String decay1, double chance1,String decay2, double chance2,String decay3, double chance3){
        boolean do1,do2,do3;
        do1= !decay1.isEmpty() && !Double.isNaN(chance1);
        do2= !decay2.isEmpty() && !Double.isNaN(chance2);
        do3= !decay3.isEmpty() && !Double.isNaN(chance3);
        TreeMap<Double,iaeaDecay> decays=new TreeMap<>();
        if(do1 && do2 && chance1==100 && chance2==100 && chance3!=100){
            decays.put(1D, new iaeaDecay(1f, decay1));
            if(do3) {
                chance3/=100d;
                decays.put(chance3, new iaeaDecay((float) chance3, decay2));
                chance2=1d-chance3;
            }
            chance2/=2d;
            decays.put(chance2, new iaeaDecay((float) chance2, decay2));
        }else if(do1 && chance1==100){
            decays.put(1D, new iaeaDecay(1f, decay1));
            if(do2) {
                chance2/=100d;
                decays.put(chance2, new iaeaDecay((float) chance2, decay2));
            }
            if(do3) {
                chance3 /= 100d;
                if(do2) {
                    chance3 *= chance2;
                }
                decays.put(chance3, new iaeaDecay((float) chance3, decay3));
            }
        }else{
            double normalization= (do1?chance1:0) + (do2?chance2:0) + (do3?chance3:0);
            if(do1) {
                chance1/=normalization;
                decays.put(chance1, new iaeaDecay((float) chance1, decay1));
            }
            if(do2) {
                chance2/=normalization;
                decays.put(chance2, new iaeaDecay((float) chance2, decay2));
            }
            if(do3) {
                chance3/=normalization;
                decays.put(chance3, new iaeaDecay((float) chance3, decay3));
            }
            if(do1||do2||do3) {
                decays.put(1D, iaeaDecay.DEAD_END);
            }
        }
        //if(DEBUG_MODE){
        //    System.out.println("INVALID SUM?\t"+normalization+"\t"+decay1+"\t"+chance1+"\t"+decay2+"\t"+chance2+"\t"+decay3+"\t"+chance3);
        //}
        return decays.values().toArray(new iaeaDecay[0]);
    }

    public static final class iaeaDecay{
        public final float chance;
        public final String decayName;
        public static final iaeaDecay DEAD_END=new iaeaDecay(1f,"DEAD_END");
        private iaeaDecay(float chance,String decayName){
            this.chance=chance;
            this.decayName=decayName;
        }
    }
}
