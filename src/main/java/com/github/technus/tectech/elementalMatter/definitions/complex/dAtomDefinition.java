package com.github.technus.tectech.elementalMatter.definitions.complex;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.gtpp.GtppAtomLoader;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.cElementalMutableDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.transformations.*;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eLeptonDefinition;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eNeutrinoDefinition;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.XSTR;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition.boson_Y__;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition.deadEnd;
import static gregtech.api.enums.OrePrefixes.dust;

/**
 * Created by danie_000 on 18.11.2016.
 */
public final class dAtomDefinition extends cElementalDefinition {
    private final int hash;
    public static final bTransformationInfo transformation=new bTransformationInfo(16,0,64);
    public static float refMass, refUnstableMass;

    private static final byte nbtType = (byte) 'a';
    private static final Random xstr = new XSTR();//NEEDS SEPARATE!
    private static Map<Integer, TreeSet<Integer>> stableIsotopes = new HashMap<>();
    private static final Map<Integer, dAtomDefinition> stableAtoms = new HashMap<>();
    private static Map<Integer, TreeMap<Float, Integer>> mostStableUnstableIsotopes = new HashMap<>();
    private static final Map<Integer, dAtomDefinition> unstableAtoms = new HashMap<>();
    private static cElementalDefinitionStack alpha;

    private static final HashMap<dAtomDefinition,Float> lifetimeOverrides = new HashMap<>();
    public static final ArrayList<Runnable> overrides = new ArrayList<>();

    public static void addOverride(dAtomDefinition atom, float rawLifeTime){
        lifetimeOverrides.put(atom,rawLifeTime);
    }

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

    public final int neutralCount;
    public final int element;

    private final cElementalDefinitionStackMap elementalStacks;

    //stable is rawLifeTime>=10^9

    @Deprecated
    public dAtomDefinition(iElementalDefinition... things) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(things));
    }

    @Deprecated
    private dAtomDefinition(boolean check, iElementalDefinition... things) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(things));
    }

    public dAtomDefinition(cElementalDefinitionStack... things) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(things));
    }

    private dAtomDefinition(boolean check, cElementalDefinitionStack... things) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(things));
    }

    public dAtomDefinition(cElementalDefinitionStackMap things) throws tElementalException {
        this(true, things);
    }

    private dAtomDefinition(boolean check, cElementalDefinitionStackMap things) throws tElementalException {
        if (check && !canTheyBeTogether(things)) throw new tElementalException("Atom Definition error");
        this.elementalStacks = things;

        float mass = 0;
        int cLeptons = 0;
        int cNucleus = 0;
        int neutralCount = 0, element = 0;
        int type = 0;
        boolean containsAnti = false;
        for (cElementalDefinitionStack stack : elementalStacks.values()) {
            iElementalDefinition def = stack.definition;
            int amount = stack.amount;
            mass += stack.getMass();
            if (def.getType() < 0) containsAnti = true;
            type = Math.max(type, Math.abs(def.getType()));

            if (def instanceof eLeptonDefinition) {
                cLeptons += stack.getCharge();
            } else {
                cNucleus += stack.getCharge();
                if (def.getCharge() == 3) element += amount;
                else if (def.getCharge() == -3) element -= amount;
                else if (def.getCharge() == 0) neutralCount += amount;
            }
        }
        this.type = containsAnti ? (byte) -type : (byte) type;
        this.mass = mass;
        this.chargeLeptons = cLeptons;
        this.charge = cNucleus + cLeptons;
        this.neutralCount = neutralCount;
        this.element = element;

        element = Math.abs(element);

        xstr.setSeed((long) (element + 1) * (neutralCount + 100));

        //stability curve
        int StableIsotope = stableIzoCurve(element);
        int izoDiff = neutralCount - StableIsotope;
        int izoDiffAbs = Math.abs(izoDiff);

        hash=super.hashCode();

        Float overriddenLifeTime=lifetimeOverrides.get(this);
        float rawLifeTimeTemp;
        if(overriddenLifeTime!=null)
            rawLifeTimeTemp = overriddenLifeTime;
        else
            rawLifeTimeTemp= calculateLifeTime(izoDiff, izoDiffAbs, element, neutralCount, containsAnti);

        this.rawLifeTime=rawLifeTimeTemp>stableRawLifeTime?stableRawLifeTime:rawLifeTimeTemp;

        if (izoDiff == 0)
            this.decayMode = 0;
        else
            this.decayMode = izoDiff > 0 ? (byte) Math.min(2, 1 + izoDiffAbs / 4) : (byte) -Math.min(2, 1 + izoDiffAbs / 4);
        this.stable = this.rawLifeTime>=stableRawLifeTime;
    }

    private static int stableIzoCurve(int element) {
        return (int) Math.round(-1.19561E-06 * Math.pow(element, 4D) +
                1.60885E-04 * Math.pow(element, 3D) +
                3.76604E-04 * Math.pow(element, 2D) +
                1.08418E+00 * (double) element);
    }

    private static float calculateLifeTime(int izoDiff, int izoDiffAbs, int element, int isotope, boolean containsAnti) {
        float rawLifeTime;

        if (element <= 83 && isotope < 127 && (izoDiffAbs == 0 ||
                (element == 1 && isotope == 0) ||
                (element == 2 && isotope == 1) ||
                (izoDiffAbs == 1 && element > 2 && element % 2 == 1) ||
                (izoDiffAbs == 3 && element > 30 && element % 2 == 0) ||
                (izoDiffAbs == 5 && element > 30 && element % 2 == 0) ||
                (izoDiffAbs == 2 && element > 20 && element % 2 == 1))) {
            rawLifeTime = containsAnti ? 2.381e4f * (1f + xstr.nextFloat() * 9f) : (1f + xstr.nextFloat() * 9f) * 1.5347e25F;
        } else {
            //Y = (X-A)/(B-A) * (D-C) + C
            float unstabilityEXP = 0;
            if (element == 0) {
                return 1e-35f;
            } else if (element == 1) {
                unstabilityEXP = 1.743f - (Math.abs(izoDiff - 1) * 9.743f);
            } else if (element == 2) {
                switch (isotope) {
                    case 4:
                        unstabilityEXP = 1.61f;
                        break;
                    case 5:
                        unstabilityEXP = -7.523F;
                        break;
                    case 6:
                        unstabilityEXP = -1.51f;
                        break;
                    default:
                        unstabilityEXP = -(izoDiffAbs * 6.165F);
                        break;
                }
            } else if (element <= 83 || (isotope <= 127 && element <= 120)) {
                float elementPow4 = (float) Math.pow(element, 4f);

                unstabilityEXP = Math.min(element / 2.4f, 6 + ((element + 1) % 2) * 3e6F / elementPow4) + (((float) -izoDiff * elementPow4) / 1e8F) - (Math.abs(izoDiff - 1 + element / 60F) * (3f - (element / 12.5f) + ((element * element) / 1500f)));
            } else if (element < 180) {
                unstabilityEXP = Math.min((element - 85) * 2, 16 + ((isotope + 1) % 2) * 2.5F - (element - 85) / 3F) - (Math.abs(izoDiff) * (3f - (element / 13f) + ((element * element) / 1600f)));
            } else return -1;
            if ((isotope == 127 || isotope == 128) && (element < 120 && element > 83)) unstabilityEXP -= 1.8f;
            if (element > 83 && element < 93 && isotope % 2 == 0 && izoDiff == 3) unstabilityEXP += 6;
            if (element > 93 && element < 103 && isotope % 2 == 0 && izoDiff == 4) unstabilityEXP += 6;
            rawLifeTime = (containsAnti ? 1e-8f : 1f) * (float) (Math.pow(10F, unstabilityEXP)) * (1f + xstr.nextFloat() * 9f);
        }

        if (rawLifeTime < 8e-15) return 1e-35f;
        if (rawLifeTime > 8e28) return 8e30f;
        return rawLifeTime;
    }

    private static boolean canTheyBeTogether(cElementalDefinitionStackMap stacks) {
        boolean nuclei = false;
        for (cElementalDefinitionStack stack : stacks.values())
            if (stack.definition instanceof dHadronDefinition) {
                if (((dHadronDefinition) stack.definition).amount != 3) return false;
                nuclei = true;
            } else if (!(stack.definition instanceof eLeptonDefinition)) return false;
        return nuclei;
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

    public int getIonizationElementWise() {
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
        final int element = Math.abs(this.element);
        final boolean negative = element < 0;
        try {
            if (type != 1) return (negative ? "~? " : "? ") + nomenclature.Name[element];
            return negative ? "~" + nomenclature.Name[element] : nomenclature.Name[element];
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
            return (negative ? "Element: ~" : "Element: ") + element;
        }
    }

    @Override
    public String getSymbol() {
        final int element = Math.abs(this.element);
        final boolean negative = element < 0;
        try {
            return (negative ? "~" : "") + nomenclature.Symbol[element] + " N:" + neutralCount + " I:" + (neutralCount+element) + " C: " + getCharge();
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
            try {
                int s100 = element / 100, s1 = (element / 10) % 10, s10 = (element) % 10;
                return (negative ? "~" : "") + nomenclature.SymbolIUPAC[10 + s100] + nomenclature.SymbolIUPAC[s10] + nomenclature.SymbolIUPAC[s1] + " N:" + neutralCount + " I:" + (neutralCount+element) + " C: " + getCharge();
            } catch (Exception E) {
                if (DEBUG_MODE) e.printStackTrace();
                return (negative ? "~" : "") + "? N:" + neutralCount + " I:" + (neutralCount+element) + " C: " + getCharge();
            }
        }
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return elementalStacks.clone();
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        if (this.type == 1) {
            switch (decayMode) {
                case -2:
                    return PbetaDecay();
                case -1:
                    return Emmision(dHadronDefinition.hadron_p1);
                case 0:
                    return alphaDecay();
                case 1:
                    return Emmision(dHadronDefinition.hadron_n1);
                case 2:
                    return MbetaDecay();
                default:
                    return getNaturalDecayInstant();
            }
        } else {
            return getNaturalDecayInstant();
        }
    }

    private cElementalDecay[] Emmision(cElementalDefinitionStack emit) {
        final cElementalMutableDefinitionStackMap tree = new cElementalMutableDefinitionStackMap(elementalStacks.values());
        if (tree.removeAmount(false, emit)) {
            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree.toImmutable()), 1), emit),
                        deadEnd
                };
            } catch (Exception e) {
                if (DEBUG_MODE) e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] alphaDecay() {
        final cElementalMutableDefinitionStackMap tree = new cElementalMutableDefinitionStackMap(elementalStacks.values());
        if (tree.removeAllAmounts(false, dHadronDefinition.hadron_n2, dHadronDefinition.hadron_p2)) {
            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree.toImmutable()), 1), alpha),
                        deadEnd
                };
            } catch (Exception e) {
                if (DEBUG_MODE) e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] MbetaDecay() {
        final cElementalMutableDefinitionStackMap tree = new cElementalMutableDefinitionStackMap(elementalStacks.values());
        if (tree.removeAmount(false, dHadronDefinition.hadron_n1)) {
            try {
                tree.putUnify(dHadronDefinition.hadron_p1);
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree.toImmutable()), 1), eLeptonDefinition.lepton_e1, eNeutrinoDefinition.lepton_Ve_1),
                        deadEnd
                };
            } catch (Exception e) {
                if (DEBUG_MODE) e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    private cElementalDecay[] PbetaDecay() {
        final cElementalMutableDefinitionStackMap tree = new cElementalMutableDefinitionStackMap(elementalStacks.values());
        if (tree.removeAmount(false, dHadronDefinition.hadron_p1)) {
            try {
                tree.putUnify(dHadronDefinition.hadron_n1);
                return new cElementalDecay[]{
                        new cElementalDecay(0.5f, this),
                        new cElementalDecay(0.5f, new cElementalDefinitionStack(new dAtomDefinition(tree.toImmutable()), 1), eLeptonDefinition.lepton_e_1, eNeutrinoDefinition.lepton_Ve1),
                        deadEnd
                };
            } catch (Exception e) {
                if (DEBUG_MODE) e.printStackTrace();
            }
        }
        return getNaturalDecayInstant();
    }

    @Override
    public cElementalDecay[] getEnergeticDecayInstant() {
        //strip leptons
        boolean doIt = true;
        ArrayList<cElementalDefinitionStack> decaysInto = new ArrayList<cElementalDefinitionStack>();
        ArrayList<cElementalDefinitionStack> newAtom = new ArrayList<cElementalDefinitionStack>();
        for (cElementalDefinitionStack elementalStack : elementalStacks.values()) {
            if (elementalStack.definition instanceof eLeptonDefinition && doIt) {
                doIt = false;
                if (elementalStack.amount > 1)
                    newAtom.add(new cElementalDefinitionStack(elementalStack.definition, elementalStack.amount - 1));
                decaysInto.add(new cElementalDefinitionStack(elementalStack.definition, 1));
            } else {
                newAtom.add(elementalStack);
            }
        }
        try {
            decaysInto.add(new cElementalDefinitionStack(new dAtomDefinition(newAtom.toArray(new cElementalDefinitionStack[newAtom.size()])), 1));
            return new cElementalDecay[]{new cElementalDecay(0.95F, decaysInto.toArray(new cElementalDefinitionStack[decaysInto.size()])), eBosonDefinition.deadEnd};
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            for (cElementalDefinitionStack things : newAtom) {
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
        cElementalDefinitionStack[] stacks = this.elementalStacks.values();
        cElementalDefinitionStack[] antiElements = new cElementalDefinitionStack[stacks.length];
        for (int i = 0; i < antiElements.length; i++) {
            antiElements[i] = new cElementalDefinitionStack(stacks[i].definition.getAnti(), stacks[i].amount);
        }
        try {
            return new dAtomDefinition(false, antiElements);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    //@Override
    //public iElementalDefinition getAnti() {
    //    cElementalMutableDefinitionStackMap anti = new cElementalMutableDefinitionStackMap();
    //    for (cElementalDefinitionStack stack : elementalStacks.values())
    //        anti.putReplace(new cElementalDefinitionStack(stack.definition.getAnti(), stack.amount));
    //    try {
    //        return new dAtomDefinition(anti.toImmutable());
    //    } catch (tElementalException e) {
    //        if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
    //        return null;
    //    }
    //}

    @Override
    public aFluidDequantizationInfo someAmountIntoFluidStack() {
        return transformation.fluidDequantization.get(this);
    }

    @Override
    public aItemDequantizationInfo someAmountIntoItemsStack() {
        return null;
    }

    @Override
    public aOredictDequantizationInfo someAmountIntoOredictStack() {
        return transformation.oredictDequantization.get(this);
    }

    private final static class nomenclature {
        static final private String[] Symbol = new String[]{"Nt", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"};
        static final private String[] Name = new String[]{"Neutronium", "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminium", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Caesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium", "Copernicium", "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson"};
        static final private String[] SymbolIUPAC = new String[]{"n", "u", "b", "t", "q", "p", "h", "s", "o", "e", "N", "U", "B", "T", "Q", "P", "H", "S", "O", "E"};
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        cElementalDefinitionStack[] elementalStacksValues = elementalStacks.values();
        nbt.setInteger("i", elementalStacksValues.length);
        for (int i = 0; i < elementalStacksValues.length; i++)
            nbt.setTag(Integer.toString(i), elementalStacksValues[i].toNBT());
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++)
            stacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        try {
            return new dAtomDefinition(stacks);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
            return null;
        }
    }

    public static void run() {
        for (Runnable r : overrides) r.run();

        for(Map.Entry<dAtomDefinition,Float> entry:lifetimeOverrides.entrySet()){
            try {
                lifetimeOverrides.put(new dAtomDefinition(entry.getKey().elementalStacks), entry.getValue());
            }catch (tElementalException e){
                e.printStackTrace(); //Impossible
            }
        }

        //populate stable isotopes
        for (int element = 1; element < 84; element++)//Up to Astatine exclusive
            for (int isotope = 0; isotope < 130; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                //stability curve
                final int StableIsotope = stableIzoCurve(element);
                final int izoDiff = isotope - StableIsotope;
                final int izoDiffAbs = Math.abs(izoDiff);
                final float rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                if (rawLifeTime>=stableRawLifeTime) {
                    TreeSet<Integer> isotopes = stableIsotopes.get(element);
                    if (isotopes == null) stableIsotopes.put(element, isotopes = new TreeSet<>());
                    isotopes.add(isotope);
                }
            }

        //populate unstable isotopes
        for (int element = 84; element < 150; element++)
            for (int isotope = 100; isotope < 180; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                //stability curve
                final int Isotope = stableIzoCurve(element);
                final int izoDiff = isotope - Isotope;
                final int izoDiffAbs = Math.abs(izoDiff);
                final float rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                TreeMap<Float, Integer> isotopes = mostStableUnstableIsotopes.get(element);
                if (isotopes == null) mostStableUnstableIsotopes.put(element, isotopes = new TreeMap<>());
                isotopes.put(rawLifeTime, isotope);
            }

        try {
            for (int key : stableIsotopes.keySet()) {
                stableAtoms.put(key, new dAtomDefinition(
                        new cElementalDefinitionStack(dHadronDefinition.hadron_p, key),
                        new cElementalDefinitionStack(dHadronDefinition.hadron_n, stableIsotopes.get(key).first()),
                        new cElementalDefinitionStack(eLeptonDefinition.lepton_e, key)));
                if (DEBUG_MODE)
                    TecTech.Logger.info("Added Stable Atom:" + key + " " + stableIsotopes.get(key).first() + " " + stableAtoms.get(key).getMass());
            }
            for (int key : mostStableUnstableIsotopes.keySet()) {
                unstableAtoms.put(key, new dAtomDefinition(
                        new cElementalDefinitionStack(dHadronDefinition.hadron_p, key),
                        new cElementalDefinitionStack(dHadronDefinition.hadron_n, mostStableUnstableIsotopes.get(key).lastEntry().getValue()),
                        new cElementalDefinitionStack(eLeptonDefinition.lepton_e, key)));
                if (DEBUG_MODE)
                    TecTech.Logger.info("Added Unstable Atom:" + key + " " + mostStableUnstableIsotopes.get(key).lastEntry().getValue() + " " + unstableAtoms.get(key).getMass());
            }
            alpha = new cElementalDefinitionStack(
                    new dAtomDefinition(
                            new cElementalDefinitionStack(dHadronDefinition.hadron_p, 2),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_n, 2))
                    , 1);
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
        }

        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dAtomDefinition.class.getMethod("fromNBT", NBTTagCompound.class),(byte)64);
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
        if(DEBUG_MODE)
            TecTech.Logger.info("Registered Elemental Matter Class: Atom "+nbtType+" "+64);
    }

    public static void setTransformation(){
        /*----STABLE ATOMS----**/
        refMass = getFirstStableIsotope(1).getMass() * 144F;

        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(1), 144),Materials.Hydrogen.mGas.getID(),144);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(2), 144),Materials.Helium.mGas.getID(), 144);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(3), 144), dust, Materials.Lithium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(4), 144), dust, Materials.Beryllium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(5), 144), dust, Materials.Boron,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(6), 144), dust, Materials.Carbon,1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(7), 144),Materials.Nitrogen.mGas.getID(), 144);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(8), 144),Materials.Oxygen.mGas.getID(), 144);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(9), 144),Materials.Fluorine.mGas.getID(), 144);
        //transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(10), 144),Materials.Neon.mGas.getID(), 144);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(11), 144), dust, Materials.Sodium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(12), 144), dust, Materials.Magnesium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(13), 144), dust, Materials.Aluminium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(14), 144), dust, Materials.Silicon,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(15), 144), dust, Materials.Phosphorus,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(16), 144), dust, Materials.Sulfur,1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(17), 144),Materials.Chlorine.mGas.getID(), 144);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(18), 144),Materials.Argon.mGas.getID(), 144);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(19), 144), dust, Materials.Potassium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(20), 144), dust, Materials.Calcium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(21), 144), dust, Materials.Scandium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(22), 144), dust, Materials.Titanium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(23), 144), dust, Materials.Vanadium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(24), 144), dust, Materials.Chrome,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(25), 144), dust, Materials.Manganese,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(26), 144), dust, Materials.Iron,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(27), 144), dust, Materials.Cobalt,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(28), 144), dust, Materials.Nickel,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(29), 144), dust, Materials.Copper,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(30), 144), dust, Materials.Zinc,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(31), 144), dust, Materials.Gallium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(32), 144),OrePrefixes.dust, Materials.Germanium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(33), 144), dust, Materials.Arsenic,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(34), 144),OrePrefixes.dust, Materials.Selenium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(35), 144),OrePrefixes.dust, Materials.Bromine,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(36), 144),OrePrefixes.dust, Materials.Krypton,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(37), 144), dust, Materials.Rubidium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(38), 144), dust, Materials.Strontium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(39), 144), dust, Materials.Yttrium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(40), 144),OrePrefixes.dust, Materials.Zirconium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(41), 144), dust, Materials.Niobium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(42), 144), dust, Materials.Molybdenum,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(43), 144),OrePrefixes.dust, Materials.Technetium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(44), 144),OrePrefixes.dust, Materials.Ruthenium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(45), 144),OrePrefixes.dust, Materials.Rhodium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(46), 144), dust, Materials.Palladium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(47), 144), dust, Materials.Silver,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(48), 144), dust, Materials.Cadmium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(49), 144), dust, Materials.Indium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(50), 144), dust, Materials.Tin,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(51), 144), dust, Materials.Antimony,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(52), 144), dust, Materials.Tellurium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(53), 144),OrePrefixes.dust, Materials.Iodine,1);
        //transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(54), 144),Materials.Xenon.mGas.getID(), 144);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(55), 144), dust, Materials.Caesium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(56), 144), dust, Materials.Barium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(57), 144), dust, Materials.Lanthanum,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(58), 144), dust, Materials.Cerium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(59), 144), dust, Materials.Praseodymium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(60), 144), dust, Materials.Neodymium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(61), 144), dust, Materials.Promethium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(62), 144), dust, Materials.Samarium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(63), 144), dust, Materials.Europium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(64), 144), dust, Materials.Gadolinium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(65), 144), dust, Materials.Terbium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(66), 144), dust, Materials.Dysprosium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(67), 144), dust, Materials.Holmium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(68), 144), dust, Materials.Erbium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(69), 144), dust, Materials.Thulium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(70), 144), dust, Materials.Ytterbium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(71), 144), dust, Materials.Lutetium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(72), 144),OrePrefixes.dust, Materials.Hafnum,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(73), 144), dust, Materials.Tantalum,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(74), 144), dust, Materials.Tungsten,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(75), 144),OrePrefixes.dust, Materials.Rhenium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(76), 144), dust, Materials.Osmium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(77), 144), dust, Materials.Iridium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(78), 144), dust, Materials.Platinum,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(79), 144), dust, Materials.Gold,1);
        transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(80), 144),Materials.Mercury.mFluid.getID(), 144);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(81), 144),OrePrefixes.dust, Materials.Thallium,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(82), 144), dust, Materials.Lead,1);
        transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(83), 144), dust, Materials.Bismuth,1);

        /*----UNSTABLE ATOMS----**/
        refUnstableMass = getFirstStableIsotope(83).getMass() * 144F;

        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(84),144),OrePrefixes.dust, Materials.Polonium,1);
        //transformation.addFluid(new cElementalDefinitionStack(getBestUnstableIsotope(85),144),Materials.Astatine.mPlasma.getID(), 144);
        transformation.addFluid(new cElementalDefinitionStack(getBestUnstableIsotope(86),144),Materials.Radon.mGas.getID(), 144);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(87),144),OrePrefixes.dust, Materials.Francium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(88),144),OrePrefixes.dust, Materials.Radium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(89),144),OrePrefixes.dust, Materials.Actinium,1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(90),144), dust, Materials.Thorium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(91),144),OrePrefixes.dust, Materials.Protactinium,1);
        ////transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(92),144), dust, Materials.Uranium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(93),144),OrePrefixes.dust, Materials.Neptunium,1);
        ////transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(94),144), dust, Materials.Plutonium,1);
        transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(95),144), dust, Materials.Americium,1);

        /* ... */
        cElementalDefinitionStack neutrons=new cElementalDefinitionStack(dHadronDefinition.hadron_n, 100000);
        transformation.oredictDequantization.put(neutrons.definition,new aOredictDequantizationInfo(neutrons, dust,Materials.Neutronium,1));
        bTransformationInfo.oredictQuantization.put(
                OreDictionary.getOreID(OrePrefixes.ingotHot.name()+Materials.Neutronium.mName),
                new aOredictQuantizationInfo(OrePrefixes.ingotHot,Materials.Neutronium,1 ,neutrons)
        );

        try {
            dAtomDefinition temp;
            temp=new dAtomDefinition(
                    eLeptonDefinition.lepton_e1,
                    dHadronDefinition.hadron_p1,
                    dHadronDefinition.hadron_n1
            );
            transformation.addFluid(new cElementalDefinitionStack(temp, 144),Materials.Deuterium.mGas.getID(), 144);

            temp=new dAtomDefinition(
                    eLeptonDefinition.lepton_e1,
                    dHadronDefinition.hadron_p1,
                    dHadronDefinition.hadron_n2
            );
            transformation.addFluid(new cElementalDefinitionStack(temp, 144),Materials.Tritium.mGas.getID(), 144);

            temp=new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 2),
                    dHadronDefinition.hadron_p2,
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 3)
            );
            transformation.addFluid(new cElementalDefinitionStack(temp, 144),Materials.Helium_3.mGas.getID(), 144);

            temp=new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 92),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_p, 92),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 146)
            );
            transformation.addOredict(new cElementalDefinitionStack(temp, 144),OrePrefixes.dust, Materials.Uranium/*238*/,1);

            temp=new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 92),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_p, 92),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 143)
            );
            transformation.addOredict(new cElementalDefinitionStack(temp, 144),OrePrefixes.dust, Materials.Uranium235,1);

            temp=new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 94),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_p, 94),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 145)
            );
            transformation.addOredict(new cElementalDefinitionStack(temp, 144),OrePrefixes.dust, Materials.Plutonium/*239*/,1);

            temp=new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 94),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_p, 94),
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 147)
            );
            transformation.addOredict(new cElementalDefinitionStack(temp, 144),OrePrefixes.dust, Materials.Plutonium241,1);
        } catch (tElementalException e) {
            if (DEBUG_MODE) e.printStackTrace();
        }

        if(Loader.isModLoaded("miscutils")) new GtppAtomLoader().run();
    }

    public static dAtomDefinition getFirstStableIsotope(int element) {
        return stableAtoms.get(element);
    }

    public static dAtomDefinition getBestUnstableIsotope(int element) {
        return unstableAtoms.get(element);
    }

    @Override
    public byte getClassType() {
        return 64;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
