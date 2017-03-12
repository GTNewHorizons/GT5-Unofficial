package com.github.technus.tectech.elementalMatter.definitions;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.classes.*;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import gregtech.api.objects.XSTR;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.Level;

import java.util.*;

import static com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStackTree.stackUpTree;
import static com.github.technus.tectech.elementalMatter.definitions.eBosonDefinition.boson_Y__;
import static com.github.technus.tectech.elementalMatter.definitions.eBosonDefinition.deadEnd;

/**
 * Created by danie_000 on 18.11.2016.
 */
public final class dAtomDefinition extends cElementalDefinition {
    private static final byte nbtType=(byte)'a';
    private static final Random xstr = new XSTR();//NEEDS SEPARATE!
    private static HashMap<Integer,TreeSet<Integer>> stableIsotopes=new HashMap<>();
    private static final TreeMap<Integer,dAtomDefinition> stableAtoms=new TreeMap<>();
    private static HashMap<Integer,TreeMap<Float,Integer>> mostStableUnstableIsotopes=new HashMap<>();
    private static final TreeMap<Integer,dAtomDefinition> unstableAtoms=new TreeMap<>();
    private static cElementalDefinitionStack alfa;

    //float-mass in eV/c^2
    public final float mass;
    //public final int charge;
    public final int charge;
    //int -electric charge in 1/3rds of electron charge for optimization
    public final int chargeLeptons;
    public final float rawLifeTime;
    //generation max present inside - minus if contains any anti quark
    public final byte type;

    public final byte decayMode;//t neutron to proton+,0,f proton to neutron
    public final boolean stable;

    //private final FluidStack fluidThing;
    //private final ItemStack itemThing;
    public final int isotope;
    public final int element;
    private cElementalDefinitionStackTree elementalStacks;

    //stable is rawLifeTime>=10^9

    //private final FluidStack fluidThing;
    //private final ItemStack itemThing;

    @Deprecated
    public dAtomDefinition(iElementalDefinition... things) throws tElementalException {
        this(true, stackUpTree(things));
    }

    @Deprecated
    private dAtomDefinition(boolean check, iElementalDefinition... things) throws tElementalException {
        this(check, stackUpTree(things));
    }

    public dAtomDefinition(cElementalDefinitionStack... things) throws tElementalException {
        this(true, stackUpTree(things));
    }

    private dAtomDefinition(boolean check, cElementalDefinitionStack... things) throws tElementalException {
        this(check, stackUpTree(things));
    }

    public dAtomDefinition(cElementalDefinitionStackTree things) throws tElementalException {
        this(true, things);
    }

    private dAtomDefinition(boolean check, cElementalDefinitionStackTree things) throws tElementalException {
        if (check && !canTheyBeTogether(things)) throw new tElementalException("Atom Definition error");
        this.elementalStacks = things;

        float mass = 0;
        int cLeptons = 0;
        int cNucleus = 0;
        int isotope = 0, element = 0;
        int type = 0;
        boolean containsAnti = false;
        for (cElementalDefinitionStack stack : elementalStacks.values()) {
            iElementalDefinition def = stack.definition;
            int amount = stack.amount;
            mass += amount * def.getMass();
            if (def.getType() < 0) containsAnti = true;
            type = Math.max(type, Math.abs(def.getType()));

            if (def instanceof eLeptonDefinition) {
                cLeptons += amount * def.getCharge();
            } else {
                cNucleus += amount * def.getCharge();
                if (def.getCharge() == 3) element += amount;
                else if (def.getCharge() == -3) element -= amount;
                else if (def.getCharge() == 0) {
                    isotope += amount;
                }
            }
        }
        this.type = containsAnti ? (byte) -type : (byte) type;
        this.mass = mass;
        this.chargeLeptons = cLeptons;
        this.charge = cNucleus + cLeptons;
        this.isotope = isotope;
        this.element = element;

        element = Math.abs(element);

        xstr.setSeed((long) (element+1)*(isotope+100));

        //stability curve
        int StableIsotope = stableIzoCurve(element);
        int izoDiff =isotope-StableIsotope;
        int izoDiffAbs = Math.abs(izoDiff);

        this.rawLifeTime= calculateLifeTime(izoDiff,izoDiffAbs,element,isotope,containsAnti);
        if(izoDiff==0)
            this.decayMode=0;
        else
            this.decayMode=izoDiff>0?(byte)Math.min(2,1+izoDiffAbs/4):(byte)-Math.min(2,1+izoDiffAbs/4);
        this.stable=isStable(this.rawLifeTime);
    }

    private static boolean isStable(float lifeTime){
        return lifeTime>1.5e25f;
    }

    private static int stableIzoCurve(int element){
        return (int) Math.round(-1.19561E-06 * Math.pow(element, 4D) +
                1.60885E-04 * Math.pow(element, 3D) +
                3.76604E-04 * Math.pow(element, 2D) +
                1.08418E+00 * (double) element);
    }

    private static float calculateLifeTime(int izoDiff, int izoDiffAbs, int element, int isotope, boolean containsAnti){
        float rawLifeTime;

        if (element<=83 && isotope<127 && (izoDiffAbs == 0 ||
                (element == 1 && isotope == 0) ||
                (element == 2 && isotope == 1) ||
                (izoDiffAbs == 1 && element > 2 && element % 2 == 1) ||
                (izoDiffAbs == 3 && element > 30 && element % 2 == 0) ||
                (izoDiffAbs == 5 && element > 30 && element % 2 == 0) ||
                (izoDiffAbs == 2 && element > 20 && element % 2 == 1))) {
            rawLifeTime = containsAnti ? 2.381e4f*(1f+xstr.nextFloat()*9f) : (1f+xstr.nextFloat()*9f) * 1.5347e25F;
        } else {
            //Y = (X-A)/(B-A) * (D-C) + C
            float unstabilityEXP=0;
            if(element==0){
                return 1e-35f;
            }else if(element==1){
                unstabilityEXP=1.743f-(Math.abs(izoDiff-1)*9.743f);
            }else if(element==2){
                switch(isotope){
                    case 4:
                        unstabilityEXP=1.61f;break;
                    case 5:
                        unstabilityEXP=-7.523F;break;
                    case 6:
                        unstabilityEXP=-1.51f;break;
                    default:
                        unstabilityEXP=-(izoDiffAbs*6.165F);break;
                }
            }else if(element<=83 || (isotope<=127 && element<=120)){
                float elementPow4=(float)Math.pow(element,4f);

                unstabilityEXP=Math.min(element/2.4f,6+((element+1)%2)*3e6F/elementPow4)+(((float)-izoDiff*elementPow4)/1e8F)-(Math.abs(izoDiff-1+element/60F)*(3f-(element/12.5f)+((element*element)/1500f)));
            }else if(element<180){
                unstabilityEXP=Math.min((element-85)*2,16+((isotope+1)%2)*2.5F-(element-85)/3F)-(Math.abs(izoDiff)*(3f-(element/13f)+((element*element)/1600f)));
            }else return -1;
            if((isotope==127 || isotope==128) && (element<120 && element>83)) unstabilityEXP-=1.8f;
            if(element>83 && element<93 && isotope%2==0 && izoDiff==3) unstabilityEXP+=6;
            if(element>93 && element<103 && isotope%2==0 && izoDiff==4) unstabilityEXP+=6;
            rawLifeTime = (containsAnti ? 1e-8f : 1f)*(float)(Math.pow(10F,unstabilityEXP))*(1f+xstr.nextFloat()*9f);
        }

        if(rawLifeTime<8e-15)return 1e-35f;
        if(rawLifeTime>8e28)return 8e30f;
        return rawLifeTime;
    }

    private static boolean canTheyBeTogether(cElementalDefinitionStackTree stacks) {
        boolean nuclei = false;
        for (cElementalDefinitionStack stack : stacks.values())
            if (stack.definition instanceof dHadronDefinition) {
                if (((dHadronDefinition) stack.definition).amount != 3) return false;
                nuclei = true;
            } else if (!(stack.definition instanceof eLeptonDefinition)) return false;
        return nuclei;
    }

    public boolean checkThis() {
        return canTheyBeTogether(elementalStacks);
    }

    @Override
    public int getCharge() {
        return charge;
    }

    public int getChargeLeptons() {
        return chargeLeptons;
    }

    public int getChargeHadrons() {
        return charge - chargeLeptons;
    }

    public int getIonization() {
        return (element * 3) + chargeLeptons;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public float getRawLifeTime() {
        return rawLifeTime;
    }

    @Override
    public byte getColor() {
        return -10;
    }

    @Override
    public String getName() {
        final int element=Math.abs(this.element);
        final boolean negative=element<0;
        try{
            if(type!=1) return (negative?"~? ":"? ") +nomenclature.Name[element];
            return negative?"~"+ nomenclature.Name[element]:nomenclature.Name[element];
        }catch(Exception e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            return (negative?"Element: ~":"Element: ") + element;
        }
    }

    @Override
    public String getSymbol() {
        final int element=Math.abs(this.element);
        final boolean negative=element<0;
        try{
            return (negative?"~":"") + nomenclature.Symbol[element]+" N:"+isotope+" I:"+getIonization();
        }catch(Exception e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            try {
                int s100 = element / 100, s1 = (element/10) % 10, s10 = (element) % 10;
                return (negative?"~":"") + nomenclature.SymbolIUPAC[10+s100]+nomenclature.SymbolIUPAC[s10]+nomenclature.SymbolIUPAC[s1]+ " N:" + isotope + " I:" + getIonization();
            }catch (Exception E){
                if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
                return (negative?"~":"") + "? N:" + isotope + " I:" + getIonization();
            }
        }
    }

    @Override
    public cElementalDefinitionStackTree getSubParticles() {
        return elementalStacks;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        if(this.type==1){
            switch(decayMode){
                case -2: return PbetaDecay();
                case -1: return Emmision(dHadronDefinition.hadron_p1);
                case  0: return alphaDecay();
                case  1: return Emmision(dHadronDefinition.hadron_n1);
                case  2: return MbetaDecay();
                default: return getNaturalDecayInstant();
            }
        }else{
            return getNaturalDecayInstant();
        }
    }

    private cElementalDecay[] Emmision(cElementalDefinitionStack emit){
        final cElementalDefinitionStackTree tree=new cElementalDefinitionStackTree(elementalStacks.values());
        if(tree.removeAmount(false,emit)){
            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree),1),emit),
                        deadEnd
                };
            }catch (Exception e){
                if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] alphaDecay(){
        final cElementalDefinitionStackTree tree=new cElementalDefinitionStackTree(elementalStacks.values());
        if(tree.removeAllAmounts(false,dHadronDefinition.hadron_n2,dHadronDefinition.hadron_p2)){
            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree),1),alfa),
                        deadEnd
                };
            }catch (Exception e){
                if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] MbetaDecay(){
        final cElementalDefinitionStackTree tree=new cElementalDefinitionStackTree(elementalStacks.values());
        if(tree.removeAmount(false,dHadronDefinition.hadron_n1)){
            try {
                tree.putUnify(dHadronDefinition.hadron_p1);
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree),1),eLeptonDefinition.lepton_e1,eNeutrinoDefinition.lepton_Ve_1),
                        deadEnd
                };
            }catch (Exception e){
                if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] PbetaDecay(){
        final cElementalDefinitionStackTree tree=new cElementalDefinitionStackTree(elementalStacks.values());
        if(tree.removeAmount(false,dHadronDefinition.hadron_p1)){
            try {
                tree.putUnify(dHadronDefinition.hadron_n1);
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree),1),eLeptonDefinition.lepton_e_1,eNeutrinoDefinition.lepton_Ve1),
                        deadEnd
                };
            }catch (Exception e){
                if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    @Override
    public cElementalDecay[] getEnergeticDecayInstant() {
        //strip leptons
        boolean doIt=true;
        ArrayList<cElementalDefinitionStack> decaysInto = new ArrayList<cElementalDefinitionStack>();
        ArrayList<cElementalDefinitionStack> newAtom = new ArrayList<cElementalDefinitionStack>();
        for (cElementalDefinitionStack elementalStack : elementalStacks.values()) {
            if(elementalStack.definition instanceof eLeptonDefinition && doIt){
                doIt=false;
                if(elementalStack.amount>1)
                    newAtom.add(new cElementalDefinitionStack(elementalStack.definition,elementalStack.amount-1));
                decaysInto.add(new cElementalDefinitionStack(elementalStack.definition,1));
            }else{
                newAtom.add(elementalStack);
            }
        }
        try {
            decaysInto.add(new cElementalDefinitionStack(new dAtomDefinition(newAtom.toArray(new cElementalDefinitionStack[newAtom.size()])),1));
            return new cElementalDecay[]{new cElementalDecay(0.95F, decaysInto.toArray(new cElementalDefinitionStack[decaysInto.size()])), eBosonDefinition.deadEnd};
        }catch(tElementalException e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            for(cElementalDefinitionStack things:newAtom){
                decaysInto.add(things);
            }
            return new cElementalDecay[]{new cElementalDecay(0.75F, decaysInto.toArray(new cElementalDefinitionStack[decaysInto.size()])), eBosonDefinition.deadEnd};
        }
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        //disembody
        ArrayList<cElementalDefinitionStack> decaysInto = new ArrayList<cElementalDefinitionStack>();
        for (cElementalDefinitionStack elementalStack : elementalStacks.values()) {
            if (elementalStack.definition.getType() == 1 || elementalStack.definition.getType() == -1) {
                //covers both quarks and antiquarks
                decaysInto.add(elementalStack);
            } else {
                //covers both quarks and antiquarks
                decaysInto.add(new cElementalDefinitionStack(boson_Y__, 2));
            }
        }
        return new cElementalDecay[]{new cElementalDecay(0.75F, decaysInto.toArray(new cElementalDefinitionStack[decaysInto.size()])), eBosonDefinition.deadEnd};
    }

    @Override
    public iElementalDefinition getAnti() {
        cElementalDefinitionStack[] elementalStacks=this.elementalStacks.values();
        cElementalDefinitionStack[] antiElements = new cElementalDefinitionStack[elementalStacks.length];
        for (int i = 0; i < antiElements.length; i++) {
            antiElements[i] = new cElementalDefinitionStack(elementalStacks[i].definition.getAnti(), elementalStacks[i].amount);
        }
        try {
            return new dAtomDefinition(false, antiElements);
        } catch (tElementalException e) {
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            return null;
        }
    }

    @Override
    public FluidStack materializesIntoFluid() {

        return null;
    }

    @Override
    public ItemStack materializesIntoItem() {

        return null;
    }

    private final static class nomenclature{
        static final private String[] Symbol = new String[]{"Nt", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"};
        static final private String[] Name = new String[]{"Neutronium", "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminium", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Caesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium", "Copernicium", "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson"};
        static final private String[] SymbolIUPAC = new String[]{"n","u","b","t","q","p","h","s","o","e","N","U","B","T","Q","P","H","S","O","E"};
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setByte("t",nbtType);
        cElementalDefinitionStack[] elementalStacksValues=elementalStacks.values();
        nbt.setInteger("i",elementalStacksValues.length);
        for (int i=0;i<elementalStacksValues.length;i++)
            nbt.setTag(Integer.toString(i),elementalStacksValues[i].toNBT());
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt){
        cElementalDefinitionStack[] stacks=new cElementalDefinitionStack[nbt.getInteger("i")];
        for(int i=0;i<stacks.length;i++)
            stacks[i]=cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        try {
            return new dAtomDefinition(stacks);
        }catch (tElementalException e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
            return null;
        }
    }

    public static void run(){
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dAtomDefinition.class.getMethod("fromNBT", NBTTagCompound.class));
        }catch (Exception e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
        }
        //populate stable isotopes
        el:
        for(int element=1;element<84;element++)//Up to Astatine exclusive
            for(int isotope=0;isotope<130;isotope++){
                xstr.setSeed((long) (element+1)*(isotope+100));
                //stability curve
                final int StableIsotope = stableIzoCurve(element);
                final int izoDiff =isotope-StableIsotope;
                final int izoDiffAbs = Math.abs(izoDiff);
                final float rawLifeTime= calculateLifeTime(izoDiff,izoDiffAbs,element,isotope,false);
                if(isStable(rawLifeTime)){
                    TreeSet<Integer> isotopes=stableIsotopes.get(element);
                    if(isotopes==null) stableIsotopes.put(element,isotopes=new TreeSet<>());
                    isotopes.add(isotope);
                }
            }

        for(int element=84;element<150;element++)
            for(int isotope=100;isotope<180;isotope++){
                xstr.setSeed((long) (element+1)*(isotope+100));
                //stability curve
                final int Isotope = stableIzoCurve(element);
                final int izoDiff =isotope-Isotope;
                final int izoDiffAbs = Math.abs(izoDiff);
                final float rawLifeTime= calculateLifeTime(izoDiff,izoDiffAbs,element,isotope,false);
                TreeMap<Float,Integer> isotopes=mostStableUnstableIsotopes.get(element);
                if(isotopes==null) mostStableUnstableIsotopes.put(element,isotopes=new TreeMap<>());
                isotopes.put(rawLifeTime,isotope);
            }

        try {
            for(int key:stableIsotopes.keySet()){
                stableAtoms.put(key,new dAtomDefinition(
                        new cElementalDefinitionStack(dHadronDefinition.hadron_p,key),
                        new cElementalDefinitionStack(dHadronDefinition.hadron_n,stableIsotopes.get(key).first()),
                        new cElementalDefinitionStack(eLeptonDefinition.lepton_e,key)));
                if(TecTech.ModConfig.DEBUG_MODE)
                    TecTech.Logger.info("Added Stable Atom:"+key+" "+stableIsotopes.get(key).first()+" "+stableAtoms.get(key).getMass());
            }
            for(int key:mostStableUnstableIsotopes.keySet()){
                unstableAtoms.put(key,new dAtomDefinition(
                        new cElementalDefinitionStack(dHadronDefinition.hadron_p,key),
                        new cElementalDefinitionStack(dHadronDefinition.hadron_n,mostStableUnstableIsotopes.get(key).lastEntry().getValue()),
                        new cElementalDefinitionStack(eLeptonDefinition.lepton_e,key)));
                if(TecTech.ModConfig.DEBUG_MODE)
                    TecTech.Logger.info("Added Unstable Atom:"+key+" "+mostStableUnstableIsotopes.get(key).lastEntry().getValue()+" "+unstableAtoms.get(key).getMass());
            }
            alfa = new cElementalDefinitionStack(
                    new dAtomDefinition(
                            new cElementalDefinitionStack(dHadronDefinition.hadron_p, 2),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_n, 2))
                    ,1);
        }catch (Exception e){
            if(TecTech.ModConfig.DEBUG_MODE)e.printStackTrace();
        }
    }

    public static dAtomDefinition getFirstStableIsotope(int element){
        return stableAtoms.get(element);
    }

    public static dAtomDefinition getBestUnstableIsotope(int element){
        return unstableAtoms.get(element);
    }

    @Override
    public byte getClassType() {
        return 64;
    }
}
