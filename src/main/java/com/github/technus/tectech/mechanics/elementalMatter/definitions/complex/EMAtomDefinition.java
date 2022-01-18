package com.github.technus.tectech.mechanics.elementalMatter.definitions.complex;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.gtpp.GtppAtomLoader;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMComplexTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMFluidDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMItemDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMOredictDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMBosonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.util.XSTR;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Materials;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.AVOGADRO_CONSTANT_144;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMBosonDefinition.boson_Y__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMBosonDefinition.deadEnd;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;
import static com.github.technus.tectech.util.XSTR.XSTR_INSTANCE;
import static gregtech.api.enums.OrePrefixes.dust;

/**
 * Created by danie_000 on 18.11.2016.
 */
public final class EMAtomDefinition extends EMComplexTemplate {
    public static final long ATOM_COMPLEXITY_LIMIT=65536L;
    private static final byte BYTE_OFFSET=32;

    private final int hash;
    public static double refMass, refUnstableMass;

    private static final byte                                   nbtType                    = (byte) 'a';
    private static final Random                                 xstr                       = new XSTR();//NEEDS SEPARATE!
    private static       Map<Integer, TreeSet<Integer>>         stableIsotopes             = new HashMap<>();
    private static final Map<Integer, EMAtomDefinition>         stableAtoms                = new HashMap<>();
    private static       Map<Integer, TreeMap<Double, Integer>> mostStableUnstableIsotopes = new HashMap<>();
    private static final Map<Integer, EMAtomDefinition>         unstableAtoms              = new HashMap<>();
    private static       EMDefinitionStack                      alpha,deuterium,tritium,helium_3,beryllium_8,carbon_14,neon_24,silicon_34;
    private static final HashMap<EMAtomDefinition,Double> lifetimeOverrides = new HashMap<>();

    private final EMNuclideIAEA iaea;

    private static EMAtomDefinition somethingHeavy;
    public static EMAtomDefinition getSomethingHeavy() {
        return somethingHeavy;
    }

    private static final ArrayList<Runnable> overrides = new ArrayList<>();
    public static void addOverride(EMAtomDefinition atom, double rawLifeTime){
        lifetimeOverrides.put(atom,rawLifeTime);
    }

    //float-mass in eV/c^2
    private final double mass;
    //public final int charge;
    private final int    charge;
    //int -electric charge in 1/3rds of electron charge for optimization
    private final int    chargeLeptons;
    private       double rawLifeTime;
    //generation max present inside - minus if contains any anti quark
    private final byte   type;

    private final byte decayMode;//t neutron to proton+,0,f proton to neutron
    //public final boolean stable;

    private final int neutralCount;
    private final int element;

    private final boolean iaeaDefinitionExistsAndHasEnergyLevels;

    private final EMConstantStackMap elementalStacks;

    //stable is rawLifeTime>=10^9

    public EMAtomDefinition(EMDefinitionStack... things) throws EMException {
        this(true, new EMConstantStackMap(things));
    }

    private EMAtomDefinition(boolean check, EMDefinitionStack... things) throws EMException {
        this(check, new EMConstantStackMap(things));
    }

    public EMAtomDefinition(EMConstantStackMap things) throws EMException {
        this(true, things);
    }

    private EMAtomDefinition(boolean check, EMConstantStackMap things) throws EMException {
        if (check && !canTheyBeTogether(things)) {
            throw new EMException("Atom Definition error");
        }
        elementalStacks = things;

        double mass = 0;
        int cLeptons = 0;
        int cNucleus = 0;
        int neutralCount = 0, element = 0;
        int type = 0;
        boolean containsAnti = false;
        for (EMDefinitionStack stack : elementalStacks.valuesToArray()) {
            IEMDefinition def    = stack.getDefinition();
            int           amount = (int) stack.getAmount();
            if((int) stack.getAmount() != stack.getAmount()){
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            mass += stack.getMass();
            if (def.getMatterType() < 0) {
                containsAnti = true;
            }
            type = Math.max(type, Math.abs(def.getMatterType()));

            if (def instanceof EMLeptonDefinition) {
                cLeptons += stack.getCharge();
            } else {
                cNucleus += stack.getCharge();
                if (def.getCharge() == 3) {
                    element += amount;
                } else if (def.getCharge() == -3) {
                    element -= amount;
                } else if (def.getCharge() == 0) {
                    neutralCount += amount;
                }
            }
        }
        this.type = containsAnti ? (byte) -type : (byte) type;
        //this.mass = mass;
        chargeLeptons = cLeptons;
        charge = cNucleus + cLeptons;
        this.neutralCount = neutralCount;
        this.element = element;

        element = Math.abs(element);

        //stability curve
        int StableIsotope = stableIzoCurve(element);
        int izoDiff = neutralCount - StableIsotope;
        int izoDiffAbs = Math.abs(izoDiff);

        xstr.setSeed((element + 1L) * (neutralCount + 100L));
        iaea = EMNuclideIAEA.get(element,neutralCount);
        if(getIaea() !=null){
            if(Double.isNaN(getIaea().getMass())) {
                this.mass = mass;
            } else {
                this.mass = getIaea().getMass();
            }

            if(Double.isNaN(getIaea().getHalfTime())) {
                Double overriddenLifeTime= lifetimeOverrides.get(this);
                double rawLifeTimeTemp;
                if(overriddenLifeTime!=null) {
                    rawLifeTimeTemp = overriddenLifeTime;
                } else {
                    rawLifeTimeTemp = calculateLifeTime(izoDiff, izoDiffAbs, element, neutralCount, containsAnti);
                }
                rawLifeTime = Math.min(rawLifeTimeTemp, STABLE_RAW_LIFE_TIME);
            }else {
                rawLifeTime = containsAnti ? getIaea().getHalfTime() * 1.5514433E-21d * (1d + xstr.nextDouble() * 9d) : getIaea().getHalfTime();
            }
            iaeaDefinitionExistsAndHasEnergyLevels = getIaea().getEnergeticStatesArray().length>1;
        }else{
            this.mass=mass;

            Double overriddenLifeTime= lifetimeOverrides.get(this);
            double rawLifeTimeTemp;
            if(overriddenLifeTime!=null) {
                rawLifeTimeTemp = overriddenLifeTime;
            } else {
                rawLifeTimeTemp = calculateLifeTime(izoDiff, izoDiffAbs, element, neutralCount, containsAnti);
            }
            rawLifeTime = Math.min(rawLifeTimeTemp, STABLE_RAW_LIFE_TIME);

            iaeaDefinitionExistsAndHasEnergyLevels =false;
        }

        if(getIaea() ==null || getIaea().getEnergeticStatesArray()[0].energy!=0) {
            if (izoDiff == 0) {
                decayMode = 0;
            } else {
                decayMode = izoDiff > 0 ? (byte) Math.min(2, 1 + izoDiffAbs / 4) : (byte) -Math.min(2, 1 + izoDiffAbs / 4);
            }
        }else{
            decayMode = izoDiff > 0 ? (byte) (Math.min(2, 1 + izoDiffAbs / 4)+ BYTE_OFFSET) : (byte) (-Math.min(2, 1 + izoDiffAbs / 4) + BYTE_OFFSET);
        }
        //this.stable = this.rawLifeTime >= STABLE_RAW_LIFE_TIME;
        hash=super.hashCode();
    }

    private static int stableIzoCurve(int element) {
        return (int) Math.round(-1.19561E-06D * Math.pow(element, 4D) +
                1.60885E-04D * Math.pow(element, 3D) +
                3.76604E-04D * Math.pow(element, 2D) +
                1.08418E+00D * (double) element);
    }

    private static double calculateLifeTime(int izoDiff, int izoDiffAbs, int element, int isotope, boolean containsAnti) {
        double rawLifeTime;

        if (element <= 83 && isotope < 127 && (izoDiffAbs == 0 || element == 1 && isotope == 0 || element == 2 && isotope == 1 || izoDiffAbs == 1 && element > 2 && element % 2 == 1 || izoDiffAbs == 3 && element > 30 && element % 2 == 0 || izoDiffAbs == 5 && element > 30 && element % 2 == 0 || izoDiffAbs == 2 && element > 20 && element % 2 == 1)) {
            rawLifeTime = (1D + xstr.nextDouble() * 9D) * (containsAnti ? 2.381e4D : 1.5347e25D);
        } else {
            //Y = (X-A)/(B-A) * (D-C) + C
            double unstabilityEXP;
            if (element == 0) {
                return 1e-35D;
            } else if (element == 1) {
                unstabilityEXP = 1.743D - Math.abs(izoDiff - 1) * 9.743D;
            } else if (element == 2) {
                switch (isotope) {
                    case 4:
                        unstabilityEXP = 1.61D;
                        break;
                    case 5:
                        unstabilityEXP = -7.523D;
                        break;
                    case 6:
                        unstabilityEXP = -1.51D;
                        break;
                    default:
                        unstabilityEXP = -(izoDiffAbs * 6.165D);
                        break;
                }
            } else if (element <= 83 || isotope <= 127 && element <= 120) {
                double elementPow4 = Math.pow(element, 4);

                unstabilityEXP = Math.min(element / 2.4D, 6 + ((element + 1) % 2) * 3e6D / elementPow4) + -izoDiff * elementPow4 / 1e8D - Math.abs(izoDiff - 1 + element / 60D) * (3D - element / 12.5D + element * element / 1500D);
            } else if (element < 180) {
                unstabilityEXP = Math.min((element - 85) * 2, 16 + ((isotope + 1) % 2) * 2.5D - (element - 85) / 3D) - Math.abs(izoDiff) * (3D - element / 13D + element * element / 1600D);
            } else {
                return -1;
            }
            if ((isotope == 127 || isotope == 128) && element < 120 && element > 83) {
                unstabilityEXP -= 1.8D;
            }
            if (element > 83 && element < 93 && isotope % 2 == 0 && izoDiff == 3) {
                unstabilityEXP += 6;
            }
            if (element > 93 && element < 103 && isotope % 2 == 0 && izoDiff == 4) {
                unstabilityEXP += 6;
            }
            rawLifeTime = (containsAnti ? 1e-8D : 1) * Math.pow(10D, unstabilityEXP) * (1D + xstr.nextDouble() * 9D);
        }

        if (rawLifeTime < 8e-15D) {
            return 1e-35D;
        }
        if (rawLifeTime > 8e28D) {
            return 8e30D;
        }
        return rawLifeTime;
    }

    private static boolean canTheyBeTogether(EMConstantStackMap stacks) {
        boolean nuclei = false;
        long qty=0;
        for (EMDefinitionStack stack : stacks.valuesToArray()) {
            if (stack.getDefinition() instanceof EMHadronDefinition) {
                if (((EMHadronDefinition) stack.getDefinition()).getAmount() != 3) {
                    return false;
                }
                nuclei = true;
            } else if (!(stack.getDefinition() instanceof EMLeptonDefinition)) {
                return false;
            }
            if((int) stack.getAmount() != stack.getAmount()){
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            qty+= stack.getAmount();
        }
        return nuclei && qty<ATOM_COMPLEXITY_LIMIT;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    public int getChargeLeptons() {
        return chargeLeptons;
    }

    public int getChargeHadrons() {
        return getCharge() - getChargeLeptons();
    }

    public int getIonizationElementWise() {
        return getElement() * 3 + getChargeLeptons();
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public byte getMatterType() {
        return type;
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
        if(currentEnergy<=0) {
            return rawLifeTime;
        }
        if(iaeaDefinitionExistsAndHasEnergyLevels){
            if(currentEnergy>= getIaea().getEnergeticStatesArray().length){
                return getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length-1].Thalf/(currentEnergy- getIaea().getEnergeticStatesArray().length+1);
            }
            return getIaea().getEnergeticStatesArray()[(int)currentEnergy].Thalf;
        }
        return rawLifeTime/(currentEnergy+1);
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }

    @Override
    public byte getColor() {
        return -10;
    }

    @Override
    public String getLocalizedName() {
        int element = Math.abs(this.getElement());
        boolean negative = this.getElement() < 0;
        try {
            if (Math.abs(getMatterType()) != 1) {
                return (negative ? "~? " : "? ") + Nomenclature.NAME[element];
            }
            return negative ? '~' + Nomenclature.NAME[element] : Nomenclature.NAME[element];
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return (negative ? "Element: ~" : "Element: ") + element;
        }
    }

    @Override
    public String getSymbol() {
        int element = Math.abs(this.getElement());
        boolean negative = this.getElement() < 0;
        try {
            return (negative ? "~" : "") + Nomenclature.SYMBOL[element] + " N:" + getNeutralCount() + " I:" + (getNeutralCount() +element) + " C:" + getCharge();
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            try {
                int s100 = element / 100, s1 = element / 10 % 10, s10 = element % 10;
                return (negative ? "~" : "") + Nomenclature.SYMBOL_IUPAC[10 + s100] + Nomenclature.SYMBOL_IUPAC[s10] + Nomenclature.SYMBOL_IUPAC[s1] + " N:" + getNeutralCount() + " I:" + (getNeutralCount() +element) + " C:" + getCharge();
            } catch (Exception E) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return (negative ? "~" : "") + "? N:" + getNeutralCount() + " I:" + (getNeutralCount() +element) + " C:" + getCharge();
            }
        }
    }

    @Override
    public String getShortSymbol() {
        int element = Math.abs(this.getElement());
        boolean negative = this.getElement() < 0;
        try {
            return (negative ? "~" : "") + Nomenclature.SYMBOL[element];
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            try {
                int s100 = element / 100, s1 = element / 10 % 10, s10 = element % 10;
                return (negative ? "~" : "") + Nomenclature.SYMBOL_IUPAC[10 + s100] + Nomenclature.SYMBOL_IUPAC[s10] + Nomenclature.SYMBOL_IUPAC[s1];
            } catch (Exception E) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return (negative ? "~" : "") + "?";
            }
        }
    }

    @Override
    public EMConstantStackMap getSubParticles() {
        return elementalStacks.clone();
    }

    @Override
    public EMDecay[] getDecayArray() {
        ArrayList<EMDecay> decaysList =new ArrayList<>(4);
        return getDecayArray(decaysList, getDecayMode(),true);
    }

    private EMDecay[] getDecayArray(ArrayList<EMDecay> decaysList, int decayMode, boolean tryAnti) {//todo?
        if (getMatterType() == 1) {
            switch (decayMode) {
                case -2:
                    if(TecTech.RANDOM.nextBoolean() && ElectronCapture(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    } else if(PbetaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case -1:
                    if(Emmision(decaysList, EMHadronDefinition.hadron_p1)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 0:
                    if(alphaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 1:
                    if(Emmision(decaysList, EMHadronDefinition.hadron_n1)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                case 2:
                    if(MbetaDecay(decaysList)) {
                        return decaysList.toArray(EMDecay.NO_PRODUCT);
                    }
                    break;
                default:
                    if(decayMode>8){
                        if(iaeaDecay(decaysList,0)) {
                            return decaysList.toArray(EMDecay.NO_PRODUCT);
                        }
                        return getDecayArray(decaysList,decayMode- BYTE_OFFSET,false);
                    }
            }
            return EMDecay.NO_DECAY;
        }else if(getMatterType() ==-1){
            EMAtomDefinition anti =getAnti();
            if(anti!=null) {
                return anti.getDecayArray(decaysList, decayMode, false);
            }
        }
        return getNaturalDecayInstant();
    }

    private boolean iaeaDecay(ArrayList<EMDecay> decaysList, long energy){
        EMNuclideIAEA.energeticState state;
        if(energy> getIaea().getEnergeticStatesArray().length) {
            state = getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1];
        } else if(energy<=0) {
            state = getIaea().getEnergeticStatesArray()[0];
        } else {
            state = getIaea().getEnergeticStatesArray()[(int) energy];
        }
        for (int i=0;i<state.decaymodes.length;i++){
            if(!getDecayFromIaea(decaysList,state.decaymodes[i],energy)) {
                decaysList.clear();
                return false;
            }
        }
        return !decaysList.isEmpty();
    }

    private boolean getDecayFromIaea(ArrayList<EMDecay> decaysList, EMNuclideIAEA.iaeaDecay decay, long energy){
        EMDefinitionStackMap withThis =elementalStacks.toMutable(), newStuff =new EMDefinitionStackMap();
        switch (decay.decayName){
            case "D": {
                if (withThis.removeAllAmountsExact(deuterium.getDefinition().getSubParticles())){
                    withThis.putReplace(deuterium);
                    decaysList.add(new EMDecay(decay.chance,withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            } break;
            case "3H": {
                if (withThis.removeAllAmountsExact(tritium.getDefinition().getSubParticles())){
                    withThis.putReplace(tritium);
                    decaysList.add(new EMDecay(decay.chance,withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            } break;
            case "3HE": {
                if (withThis.removeAllAmountsExact(helium_3.getDefinition().getSubParticles())){
                    withThis.putReplace(helium_3);
                    decaysList.add(new EMDecay(decay.chance,withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            } break;
            case "8BE": {
                if (withThis.removeAllAmountsExact(beryllium_8.getDefinition().getSubParticles())){
                    withThis.putReplace(beryllium_8);
                    decaysList.add(new EMDecay(decay.chance,withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                    return true;
                }
            } break;
            case "14C": {
                if (withThis.removeAllAmountsExact(carbon_14.getDefinition().getSubParticles())){
                    newStuff.putReplace(carbon_14);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "24NE": {
                if (withThis.removeAllAmountsExact(neon_24.getDefinition().getSubParticles())){
                    newStuff.putReplace(neon_24);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "34SI": {
                if (withThis.removeAllAmountsExact(silicon_34.getDefinition().getSubParticles())){
                    newStuff.putReplace(silicon_34);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "A": case "A?": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2, EMHadronDefinition.hadron_p2)){
                    newStuff.putReplace(alpha);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "2B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B-": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "2B-": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "EC": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "2EC": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B++EC": case "EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B+A": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p3, EMHadronDefinition.hadron_n1)){
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(alpha);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B+P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B+2P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p3)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B-A": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n3, EMHadronDefinition.hadron_p1)){
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(alpha);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B-N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_n1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B-2N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n3)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_n2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "B-P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)){
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "ECA": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1, EMLeptonDefinition.lepton_e1, EMHadronDefinition.hadron_p3)){
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(alpha);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "ECP": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1, EMHadronDefinition.hadron_p2)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "EC2P": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1, EMHadronDefinition.hadron_p3)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "ECP+EC2P": {//todo look at branching ratios
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e2, EMHadronDefinition.hadron_p5)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    newStuff.putReplace(EMHadronDefinition.hadron_p3);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)){
                    newStuff.putReplace(EMHadronDefinition.hadron_n1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "2N": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n2)){
                    newStuff.putReplace(EMHadronDefinition.hadron_n2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1)){
                    newStuff.putReplace(EMHadronDefinition.hadron_p1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "2P": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2)){
                    newStuff.putReplace(EMHadronDefinition.hadron_p2);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "SF": {
                if (Fission(decaysList, withThis, newStuff, decay.chance, false)) {
                    return true;
                }
            } break;
            case "B-F": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_n1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_p1);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try{
                        if(Fission(decaysList,withThis,newStuff,decay.chance,false)) {
                            return true;
                        }
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "ECF": case "ECSF": case "EC(+SF)": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    try{
                        if(Fission(decaysList,withThis,newStuff,decay.chance,false)) {
                            return true;
                        }
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "SF(+EC+B+)": case "SF+EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    try{
                        if(Fission(decaysList,withThis,newStuff,decay.chance,false)) {
                            return true;
                        }
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "SF+EC+B-": {
                if (withThis.removeAllAmountsExact(EMLeptonDefinition.lepton_e1)){
                    newStuff.putReplace(EMLeptonDefinition.lepton_e1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve_1);
                    try{
                        if(Fission(decaysList,withThis,newStuff,decay.chance,false)) {
                            return true;
                        }
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "IT": case "IT?": case "G": {
                if(energy>0){
                    decaysList.add(new EMDecay(decay.chance, this, boson_Y__));
                }else{
                    if(DEBUG_MODE) {
                        TecTech.LOGGER.info("Tried to emit Gamma from ground state");
                    }
                    decaysList.add(new EMDecay(decay.chance, this));
                }
                return true;
            } //break;
            case "IT+EC+B+": {
                if (withThis.removeAllAmountsExact(EMHadronDefinition.hadron_p2, EMLeptonDefinition.lepton_e1)){
                    withThis.putUnifyExact(EMHadronDefinition.hadron_n2);
                    newStuff.putReplace(EMLeptonDefinition.lepton_e_1);
                    newStuff.putReplace(EMNeutrinoDefinition.lepton_Ve2);
                    newStuff.putReplace(EMBosonDefinition.boson_Y__1);
                    try{
                        newStuff.putReplace(new EMAtomDefinition(withThis.toImmutable_optimized_unsafe_LeavesExposedElementalTree()).getStackForm(1));
                        decaysList.add(new EMDecay(decay.chance,newStuff.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
                        return true;
                    }catch (Exception e){
                        if(DEBUG_MODE) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            case "DEAD_END":
                decaysList.add(deadEnd);
                return true;
            default: throw new Error("Unsupported decay mode: " + decay.decayName + ' ' + getNeutralCount() + ' ' + getElement());
        }
        if(DEBUG_MODE) {
            TecTech.LOGGER.info("Failed to decay " + getElement() + ' ' + getNeutralCount() + ' ' + decay.decayName);
        }
        return false;
    }

    private boolean Emmision(ArrayList<EMDecay> decaysList, EMDefinitionStack emit) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(emit)) {
            try {
                decaysList.add(new EMDecay(1, new EMDefinitionStack(new EMAtomDefinition(tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()), 1), emit));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean alphaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAllAmountsExact(alpha.getDefinition().getSubParticles())) {
            try {
                decaysList.add(new EMDecay(1, new EMDefinitionStack(new EMAtomDefinition(tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()), 1), alpha));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean MbetaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(EMHadronDefinition.hadron_n1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_p1);
                decaysList.add(new EMDecay(1, new EMDefinitionStack(new EMAtomDefinition(tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()), 1), EMLeptonDefinition.lepton_e1, EMNeutrinoDefinition.lepton_Ve_1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean PbetaDecay(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAmountExact(EMHadronDefinition.hadron_p1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_n1);
                decaysList.add(new EMDecay(1, new EMDefinitionStack(new EMAtomDefinition(tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()), 1), EMLeptonDefinition.lepton_e_1, EMNeutrinoDefinition.lepton_Ve1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean ElectronCapture(ArrayList<EMDecay> decaysList) {
        EMDefinitionStackMap tree = elementalStacks.toMutable();
        if (tree.removeAllAmountsExact(EMHadronDefinition.hadron_p1, EMLeptonDefinition.lepton_e1)) {
            try {
                tree.putUnifyExact(EMHadronDefinition.hadron_n1);
                decaysList.add(new EMDecay(1, new EMDefinitionStack(new EMAtomDefinition(tree.toImmutable_optimized_unsafe_LeavesExposedElementalTree()), 1), EMNeutrinoDefinition.lepton_Ve1));
                return true;
            } catch (Exception e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean Fission(ArrayList<EMDecay> decaysList, EMDefinitionStackMap fissile, EMDefinitionStackMap particles, double probability, boolean spontaneousCheck) {
        EMDefinitionStackMap heavy      = new EMDefinitionStackMap();
        double[]             liquidDrop = liquidDropFunction(Math.abs(getElement())<=97);

        for(EMDefinitionStack stack: fissile.valuesToArray()){
            if(spontaneousCheck && stack.getDefinition() instanceof EMHadronDefinition &&
                    (stack.getAmount() <=80 || stack.getAmount() <90 && XSTR_INSTANCE.nextInt(10)< stack.getAmount() -80)) {
                return false;
            }
            if(stack.getDefinition().getCharge()==0){
                //if(stack.definition instanceof dHadronDefinition){
                    double neutrals= stack.getAmount() *liquidDrop[2];
                    int neutrals_cnt=(int)Math.floor(neutrals);
                    neutrals_cnt+=neutrals-neutrals_cnt>XSTR_INSTANCE.nextDouble()?1:0;
                    particles.putUnifyExact(new EMDefinitionStack(stack.getDefinition(), neutrals_cnt));

                    int heavy_cnt=(int)Math.ceil(stack.getAmount() *liquidDrop[1]);
                    while(heavy_cnt+neutrals_cnt> stack.getAmount()) {
                        heavy_cnt--;
                    }
                    fissile.removeAmountExact(new EMDefinitionStack(stack.getDefinition(),heavy_cnt+neutrals_cnt));
                    heavy.putReplace(new EMDefinitionStack(stack.getDefinition(), heavy_cnt));
                //}else{
                //    particles.add(stack);
                //    light.remove(stack.definition);
                //}
            }else{
                int heavy_cnt=(int)Math.ceil(stack.getAmount() *liquidDrop[0]);
                if(heavy_cnt%2==1 && XSTR_INSTANCE.nextDouble()>0.05D) {
                    heavy_cnt--;
                }
                EMDefinitionStack new_stack =new EMDefinitionStack(stack.getDefinition(), heavy_cnt);
                fissile.removeAmountExact(new_stack);
                heavy.putReplace(new_stack);
            }
        }

        try {
            particles.putReplace(new EMDefinitionStack(new EMAtomDefinition(fissile.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),1));
            particles.putReplace(new EMDefinitionStack(new EMAtomDefinition(heavy.toImmutable_optimized_unsafe_LeavesExposedElementalTree()),1));
            decaysList.add(new EMDecay(probability, particles.toImmutable_optimized_unsafe_LeavesExposedElementalTree()));
            return true;
        } catch (Exception e) {
            if(DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static double[] liquidDropFunction(boolean asymmetric) {
        double[] out = new double[3];

        out[0] = XSTR_INSTANCE.nextGaussian();

        if (out[0] < 1 && out[0] >= -1) {
            if (XSTR_INSTANCE.nextBoolean()) {
                out[0] = XSTR_INSTANCE.nextDouble() * 2d - 1d;
            }
        }

        if (asymmetric && out[0] > XSTR_INSTANCE.nextDouble() && XSTR_INSTANCE.nextInt(4) == 0) {
            out[0] = -out[0];
        }

        //scale to splitting ratio
        out[0] = out[0] * 0.05d + .6d;

        if (out[0] < 0 || out[0] > 1) {
            return liquidDropFunction(asymmetric);
        }
        if (out[0] < .5d) {
            out[0] = 1d - out[0];
        }

        //extra neutrals
        out[2] = 0.012d + XSTR_INSTANCE.nextDouble() * 0.01d;

        if (asymmetric) {
            out[1] = out[0];
        } else {
            out[1] = out[0] - out[2] * .5d;
        }

        return out;
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        if (iaeaDefinitionExistsAndHasEnergyLevels) {
            ArrayList<EMDecay> decays =new ArrayList<>(4);
            if(iaeaDecay(decays,energyLevel)){
                return decays.toArray(EMDecay.NO_PRODUCT);
            }
        }
        if(energyLevel< Math.abs(getCharge())/3+ getNeutralCount()) {
            return new EMDecay[]{new EMDecay(1, this, boson_Y__)};
        }
        return getNaturalDecayInstant();
    }

    @Override
    public double getEnergyDiffBetweenStates(long currentEnergyLevel,long newEnergyLevel) {
        if(iaeaDefinitionExistsAndHasEnergyLevels){
            double result=0;
            boolean backwards=newEnergyLevel<currentEnergyLevel;
            if(backwards){
                long temp=currentEnergyLevel;
                currentEnergyLevel=newEnergyLevel;
                newEnergyLevel=temp;
            }

            if(currentEnergyLevel<=0){
                if(newEnergyLevel<=0) {
                    return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
                } else {
                    result += IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * -currentEnergyLevel;
                }
            }else {
                result -= getIaea().getEnergeticStatesArray()[(int) Math.min(getIaea().getEnergeticStatesArray().length - 1, currentEnergyLevel)].energy;
            }
            if(newEnergyLevel>= getIaea().getEnergeticStatesArray().length){
                if(currentEnergyLevel>= getIaea().getEnergeticStatesArray().length) {
                    return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
                } else {
                    result += IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - getIaea().getEnergeticStatesArray().length + 1);
                }
                result+= getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length-1].energy;
            }else {
                result += getIaea().getEnergeticStatesArray()[(int) newEnergyLevel].energy;
            }

            return backwards?-result:result;
        }
        return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT *(newEnergyLevel-currentEnergyLevel);
    }

    @Override
    public boolean usesSpecialEnergeticDecayHandling() {
        return iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public boolean usesMultipleDecayCalls(long energyLevel) {
        if(!iaeaDefinitionExistsAndHasEnergyLevels) return false;
        EMNuclideIAEA.energeticState state;
        if(energyLevel> getIaea().getEnergeticStatesArray().length) {
            state = getIaea().getEnergeticStatesArray()[getIaea().getEnergeticStatesArray().length - 1];
        } else if(energyLevel<=0) {
            state = getIaea().getEnergeticStatesArray()[0];
        } else {
            state = getIaea().getEnergeticStatesArray()[(int) energyLevel];
        }
        for (EMNuclideIAEA.iaeaDecay decay:state.decaymodes){
            if(decay.decayName.contains("F")) return true;//if is fissile
        }
        return false;
    }

    @Override
    public boolean decayMakesEnergy(long energyLevel) {
        return iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public boolean fusionMakesEnergy(long energyLevel) {
        return getIaea() !=null || iaeaDefinitionExistsAndHasEnergyLevels;
    }

    @Override
    public EMDecay[] getNaturalDecayInstant() {
        //disembody
        ArrayList<EMDefinitionStack> decaysInto = new ArrayList<>();
        for (EMDefinitionStack elementalStack : elementalStacks.valuesToArray()) {
            if (elementalStack.getDefinition().getMatterType() == 1 || elementalStack.getDefinition().getMatterType() == -1) {
                //covers both quarks and antiquarks
                decaysInto.add(elementalStack);
            } else {
                //covers both quarks and antiquarks
                decaysInto.add(new EMDefinitionStack(boson_Y__, 2));
            }
        }
        return new EMDecay[]{new EMDecay(0.75D, decaysInto.toArray(new EMDefinitionStack[0])), deadEnd};
    }

    //@Override
    //public iElementalDefinition getAnti() {
    //    cElementalDefinitionStack[] stacks = this.elementalStacks.values();
    //    cElementalDefinitionStack[] antiElements = new cElementalDefinitionStack[stacks.length];
    //    for (int i = 0; i < antiElements.length; i++) {
    //        antiElements[i] = new cElementalDefinitionStack(stacks[i].definition.getAnti(), stacks[i].amount);
    //    }
    //    try {
    //        return new dAtomDefinition(false, antiElements);
    //    } catch (tElementalException e) {
    //        if (DEBUG_MODE) e.printStackTrace();
    //        return null;
    //    }
    //}

    @Override
    public EMAtomDefinition getAnti() {
        EMDefinitionStackMap anti = new EMDefinitionStackMap();
        for (EMDefinitionStack stack : elementalStacks.valuesToArray()) {
            anti.putReplace(new EMDefinitionStack(stack.getDefinition().getAnti(), stack.getAmount()));
        }
        try {
            return new EMAtomDefinition(anti.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public EMFluidDequantizationInfo someAmountIntoFluidStack() {
        return EMTransformationInfo.TRANSFORMATION_INFO.getFluidDequantization().get(this);
    }

    @Override
    public EMItemDequantizationInfo someAmountIntoItemsStack() {
        return EMTransformationInfo.TRANSFORMATION_INFO.getItemDequantization().get(this);
    }

    @Override
    public EMOredictDequantizationInfo someAmountIntoOredictStack() {
        return EMTransformationInfo.TRANSFORMATION_INFO.getOredictDequantization().get(this);
    }

    public EMNuclideIAEA getIaea() {
        return iaea;
    }

    public byte getDecayMode() {
        return decayMode;
    }

    public int getNeutralCount() {
        return neutralCount;
    }

    public int getElement() {
        return element;
    }

    private static final class Nomenclature {
        private static final String[] SYMBOL = new String[]{"Nt", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"};
        private static final String[] NAME = new String[]{"Neutronium", "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminium", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Caesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium", "Copernicium", "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson"};
        private static final String[] SYMBOL_IUPAC = new String[]{"n", "u", "b", "t", "q", "p", "h", "s", "o", "e", "N", "U", "B", "T", "Q", "P", "H", "S", "O", "E"};
    }

    @Override
    protected int getIndirectTagValue() {
        return nbtType;
    }

    public static EMAtomDefinition fromNBT(NBTTagCompound nbt) {
        EMDefinitionStack[] stacks = new EMDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = EMDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        }
        try {
            return new EMAtomDefinition(stacks);
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void run() {
        for (Runnable r : overrides) {
            r.run();
        }

        for(Map.Entry<EMAtomDefinition,Double> entry:lifetimeOverrides.entrySet()){
            try {
                lifetimeOverrides.put(new EMAtomDefinition(entry.getKey().elementalStacks), entry.getValue());
            }catch (EMException e){
                e.printStackTrace(); //Impossible
            }
        }

        //populate stable isotopes
        for (int element = 1; element < 83; element++)//Up to Bismuth exclusive
        {
            for (int isotope = 0; isotope < 130; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                //stability curve
                int StableIsotope = stableIzoCurve(element);
                int izoDiff = isotope - StableIsotope;
                int izoDiffAbs = Math.abs(izoDiff);
                double        rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                EMNuclideIAEA nuclide     = EMNuclideIAEA.get(element, isotope);
                if (rawLifeTime >= STABLE_RAW_LIFE_TIME || nuclide != null && nuclide.getHalfTime() >= STABLE_RAW_LIFE_TIME) {
                    TreeSet<Integer> isotopes = stableIsotopes.computeIfAbsent(element, k -> new TreeSet<>());
                    isotopes.add(isotope);
                }
            }
        }

        //populate unstable isotopes
        for (int element = 83; element < 150; element++) {
            for (int isotope = 100; isotope < 180; isotope++) {
                xstr.setSeed((long) (element + 1) * (isotope + 100));
                //stability curve
                int Isotope = stableIzoCurve(element);
                int izoDiff = isotope - Isotope;
                int izoDiffAbs = Math.abs(izoDiff);
                double rawLifeTime = calculateLifeTime(izoDiff, izoDiffAbs, element, isotope, false);
                TreeMap<Double, Integer> isotopes = mostStableUnstableIsotopes.computeIfAbsent(element, k -> new TreeMap<>());
                isotopes.put(rawLifeTime, isotope);
            }
        }

        try {
            for (Map.Entry<Integer, TreeSet<Integer>> integerTreeSetEntry : stableIsotopes.entrySet()) {
                stableAtoms.put(integerTreeSetEntry.getKey(), new EMAtomDefinition(
                        new EMDefinitionStack(EMHadronDefinition.hadron_p, integerTreeSetEntry.getKey()),
                        new EMDefinitionStack(EMHadronDefinition.hadron_n, integerTreeSetEntry.getValue().first()),
                        new EMDefinitionStack(EMLeptonDefinition.lepton_e, integerTreeSetEntry.getKey())));
                if (DEBUG_MODE) {
                    TecTech.LOGGER.info("Added Stable Atom:" + integerTreeSetEntry.getKey() + ' ' + integerTreeSetEntry.getValue().first() + ' ' + stableAtoms.get(integerTreeSetEntry.getKey()).getMass());
                }
            }
            for (Map.Entry<Integer, TreeMap<Double, Integer>> integerTreeMapEntry : mostStableUnstableIsotopes.entrySet()) {
                unstableAtoms.put(integerTreeMapEntry.getKey(), new EMAtomDefinition(
                        new EMDefinitionStack(EMHadronDefinition.hadron_p, integerTreeMapEntry.getKey()),
                        new EMDefinitionStack(EMHadronDefinition.hadron_n, integerTreeMapEntry.getValue().lastEntry().getValue()),
                        new EMDefinitionStack(EMLeptonDefinition.lepton_e, integerTreeMapEntry.getKey())));
                if (DEBUG_MODE) {
                    TecTech.LOGGER.info("Added Unstable Atom:" + integerTreeMapEntry.getKey() + ' ' + integerTreeMapEntry.getValue().lastEntry().getValue() + ' ' + unstableAtoms.get(integerTreeMapEntry.getKey()).getMass());
                }
            }
            deuterium=new EMAtomDefinition(
                    EMHadronDefinition.hadron_p1,
                    EMHadronDefinition.hadron_n1,
                    EMLeptonDefinition.lepton_e1).getStackForm(1);
            tritium=new EMAtomDefinition(
                    EMHadronDefinition.hadron_p1,
                    EMHadronDefinition.hadron_n2,
                    EMLeptonDefinition.lepton_e1).getStackForm(1);
            helium_3=new EMAtomDefinition(
                    EMHadronDefinition.hadron_p2,
                    EMHadronDefinition.hadron_n1,
                    EMLeptonDefinition.lepton_e2).getStackForm(1);
            alpha = new EMAtomDefinition(
                    EMHadronDefinition.hadron_p2,
                    EMHadronDefinition.hadron_n2).getStackForm(1);
            beryllium_8=new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 4),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 4),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 4)).getStackForm(1);
            carbon_14=new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 6),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 8),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 6)).getStackForm(1);
            neon_24=new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 10),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 14),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 10)).getStackForm(1);
            silicon_34=new EMAtomDefinition(
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 14),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 20),
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 14)).getStackForm(1);
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }

        try {
            EMDefinitionsRegistry.registerDefinitionClass(nbtType, EMAtomDefinition::fromNBT,EMAtomDefinition.class,getClassTypeStatic());
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        if(DEBUG_MODE) {
            TecTech.LOGGER.info("Registered Elemental Matter Class: Atom " + nbtType + ' ' + getClassTypeStatic());
        }
    }

    public static void setTransformation(){
        /*----STABLE ATOMS----**/
        refMass = getFirstStableIsotope(1).getMass() * AVOGADRO_CONSTANT_144;

        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(1), AVOGADRO_CONSTANT_144),Materials.Hydrogen.mGas,144);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(2), AVOGADRO_CONSTANT_144),Materials.Helium.mGas, 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(3), AVOGADRO_CONSTANT_144), dust, Materials.Lithium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(4), AVOGADRO_CONSTANT_144), dust, Materials.Beryllium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(5), AVOGADRO_CONSTANT_144), dust, Materials.Boron,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(6), AVOGADRO_CONSTANT_144), dust, Materials.Carbon,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(7), AVOGADRO_CONSTANT_144),Materials.Nitrogen.mGas, 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(8), AVOGADRO_CONSTANT_144),Materials.Oxygen.mGas, 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(9), AVOGADRO_CONSTANT_144),Materials.Fluorine.mGas, 144);
        //transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(10), AVOGADRO_CONSTANT_144),Materials.Neon.mGas.getID(), 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(11), AVOGADRO_CONSTANT_144), dust, Materials.Sodium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(12), AVOGADRO_CONSTANT_144), dust, Materials.Magnesium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(13), AVOGADRO_CONSTANT_144), dust, Materials.Aluminium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(14), AVOGADRO_CONSTANT_144), dust, Materials.Silicon,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(15), AVOGADRO_CONSTANT_144), dust, Materials.Phosphorus,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(16), AVOGADRO_CONSTANT_144), dust, Materials.Sulfur,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(17), AVOGADRO_CONSTANT_144),Materials.Argon.mGas, 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(19), AVOGADRO_CONSTANT_144), dust, Materials.Potassium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(20), AVOGADRO_CONSTANT_144), dust, Materials.Calcium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(21), AVOGADRO_CONSTANT_144), dust, Materials.Scandium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(22), AVOGADRO_CONSTANT_144), dust, Materials.Titanium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(23), AVOGADRO_CONSTANT_144), dust, Materials.Vanadium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(24), AVOGADRO_CONSTANT_144), dust, Materials.Chrome,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(25), AVOGADRO_CONSTANT_144), dust, Materials.Manganese,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(26), AVOGADRO_CONSTANT_144), dust, Materials.Iron,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(27), AVOGADRO_CONSTANT_144), dust, Materials.Cobalt,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(28), AVOGADRO_CONSTANT_144), dust, Materials.Nickel,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(29), AVOGADRO_CONSTANT_144), dust, Materials.Copper,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(30), AVOGADRO_CONSTANT_144), dust, Materials.Zinc,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(31), AVOGADRO_CONSTANT_144), dust, Materials.Gallium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(32), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Germanium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(33), AVOGADRO_CONSTANT_144), dust, Materials.Arsenic,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(34), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Selenium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(35), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Bromine,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(36), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Krypton,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(37), AVOGADRO_CONSTANT_144), dust, Materials.Rubidium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(38), AVOGADRO_CONSTANT_144), dust, Materials.Strontium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(39), AVOGADRO_CONSTANT_144), dust, Materials.Yttrium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(40), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Zirconium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(41), AVOGADRO_CONSTANT_144), dust, Materials.Niobium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(42), AVOGADRO_CONSTANT_144), dust, Materials.Molybdenum,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(43), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Technetium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(44), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Ruthenium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(45), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Rhodium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(46), AVOGADRO_CONSTANT_144), dust, Materials.Palladium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(47), AVOGADRO_CONSTANT_144), dust, Materials.Silver,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(48), AVOGADRO_CONSTANT_144), dust, Materials.Cadmium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(49), AVOGADRO_CONSTANT_144), dust, Materials.Indium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(50), AVOGADRO_CONSTANT_144), dust, Materials.Tin,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(51), AVOGADRO_CONSTANT_144), dust, Materials.Antimony,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(52), AVOGADRO_CONSTANT_144), dust, Materials.Tellurium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(53), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Iodine,1);
        //transformation.addFluid(new cElementalDefinitionStack(getFirstStableIsotope(54), AVOGADRO_CONSTANT_144),Materials.Xenon.mGas.getID(), 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(55), AVOGADRO_CONSTANT_144), dust, Materials.Caesium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(56), AVOGADRO_CONSTANT_144), dust, Materials.Barium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(57), AVOGADRO_CONSTANT_144), dust, Materials.Lanthanum,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(58), AVOGADRO_CONSTANT_144), dust, Materials.Cerium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(59), AVOGADRO_CONSTANT_144), dust, Materials.Praseodymium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(60), AVOGADRO_CONSTANT_144), dust, Materials.Neodymium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(61), AVOGADRO_CONSTANT_144), dust, Materials.Promethium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(62), AVOGADRO_CONSTANT_144), dust, Materials.Samarium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(63), AVOGADRO_CONSTANT_144), dust, Materials.Europium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(64), AVOGADRO_CONSTANT_144), dust, Materials.Gadolinium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(65), AVOGADRO_CONSTANT_144), dust, Materials.Terbium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(66), AVOGADRO_CONSTANT_144), dust, Materials.Dysprosium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(67), AVOGADRO_CONSTANT_144), dust, Materials.Holmium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(68), AVOGADRO_CONSTANT_144), dust, Materials.Erbium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(69), AVOGADRO_CONSTANT_144), dust, Materials.Thulium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(70), AVOGADRO_CONSTANT_144), dust, Materials.Ytterbium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(71), AVOGADRO_CONSTANT_144), dust, Materials.Lutetium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(72), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Hafnum,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(73), AVOGADRO_CONSTANT_144), dust, Materials.Tantalum,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(74), AVOGADRO_CONSTANT_144), dust, Materials.Tungsten,1);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(75), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Rhenium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(76), AVOGADRO_CONSTANT_144), dust, Materials.Osmium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(77), AVOGADRO_CONSTANT_144), dust, Materials.Iridium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(78), AVOGADRO_CONSTANT_144), dust, Materials.Platinum,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(79), AVOGADRO_CONSTANT_144), dust, Materials.Gold,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getFirstStableIsotope(80), AVOGADRO_CONSTANT_144),Materials.Mercury.mFluid, 144);
        //transformation.addOredict(new cElementalDefinitionStack(getFirstStableIsotope(81), AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Thallium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getFirstStableIsotope(82), AVOGADRO_CONSTANT_144), dust, Materials.Lead,1);

        /*----UNSTABLE ATOMS----**/
        refUnstableMass = getFirstStableIsotope(82).getMass() * AVOGADRO_CONSTANT_144;

        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(83), AVOGADRO_CONSTANT_144), dust, Materials.Bismuth,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(84),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Polonium,1);
        //transformation.addFluid(new cElementalDefinitionStack(getBestUnstableIsotope(85),AVOGADRO_CONSTANT_144),Materials.Astatine.mPlasma.getID(), 144);
        EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(getBestUnstableIsotope(86), AVOGADRO_CONSTANT_144),Materials.Radon.mGas, 144);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(87),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Francium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(88),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Radium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(89),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Actinium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(90), AVOGADRO_CONSTANT_144), dust, Materials.Thorium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(91),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Protactinium,1);
        ////transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(92),AVOGADRO_CONSTANT_144), dust, Materials.Uranium,1);
        //transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(93),AVOGADRO_CONSTANT_144),OrePrefixes.dust, Materials.Neptunium,1);
        ////transformation.addOredict(new cElementalDefinitionStack(getBestUnstableIsotope(94),AVOGADRO_CONSTANT_144), dust, Materials.Plutonium,1);
        EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(getBestUnstableIsotope(95), AVOGADRO_CONSTANT_144), dust, Materials.Americium,1);

        try {
            EMAtomDefinition temp;
            EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(deuterium.getDefinition(), AVOGADRO_CONSTANT_144),Materials.Deuterium.mGas, 144);

            EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(tritium.getDefinition(), AVOGADRO_CONSTANT_144),Materials.Tritium.mGas, 144);

            EMTransformationInfo.TRANSFORMATION_INFO.addFluid(new EMDefinitionStack(helium_3.getDefinition(), AVOGADRO_CONSTANT_144),Materials.Helium_3.mGas, 144);

            temp=new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 146)
            );
            EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(temp, AVOGADRO_CONSTANT_144), dust, Materials.Uranium/*238*/,1);

            double tempMass=temp.getMass();

            temp=new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 92),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 143)
            );
            EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(temp, AVOGADRO_CONSTANT_144), dust, Materials.Uranium235,1);

            TecTech.LOGGER.info("Diff Mass U : "+(tempMass-temp.getMass()));

            temp=new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 145)
            );
            EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(temp, AVOGADRO_CONSTANT_144), dust, Materials.Plutonium/*239*/,1);

            somethingHeavy=new EMAtomDefinition(
                    new EMDefinitionStack(EMLeptonDefinition.lepton_e, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_p, 94),
                    new EMDefinitionStack(EMHadronDefinition.hadron_n, 147)
            );
            EMTransformationInfo.TRANSFORMATION_INFO.addOredict(new EMDefinitionStack(somethingHeavy, AVOGADRO_CONSTANT_144), dust, Materials.Plutonium241,1);

            TecTech.LOGGER.info("Diff Mass Pu: "+(somethingHeavy.getMass()-temp.getMass()));

            TecTech.LOGGER.info("Neutron Mass: "+ EMHadronDefinition.hadron_n.getMass());

        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }

        if(Loader.isModLoaded(Reference.GTPLUSPLUS)) {
            new GtppAtomLoader().run();
        }
    }

    public static EMAtomDefinition getFirstStableIsotope(int element) {
        return stableAtoms.get(element);
    }

    public static EMAtomDefinition getBestUnstableIsotope(int element) {
        return unstableAtoms.get(element);
    }

    @Override
    public byte getClassType() {
        return getClassTypeStatic();
    }

    public static byte getClassTypeStatic(){
        return 64;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(Util.areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS|SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add(getShortSymbol());
        }
    }

    @Override
    public void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(Util.areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            lines.add("CLASS = " + nbtType + ' ' + getClassType());
        }
        if(Util.areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS|SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add("NAME = "+ getLocalizedName());
            lines.add("SYMBOL = "+getSymbol());
        }
        if(Util.areBitsSet(SCAN_GET_CHARGE,capabilities)) {
            lines.add("CHARGE = " + getCharge() / 3D + " e");
        }
        if(Util.areBitsSet(SCAN_GET_COLOR,capabilities)) {
            lines.add(getColor() < 0 ? "COLORLESS" : "CARRIES COLOR");
        }
        if(Util.areBitsSet(SCAN_GET_MASS,capabilities)) {
            lines.add("MASS = " + getMass() + " eV/c\u00b2");
        }
        if(iaeaDefinitionExistsAndHasEnergyLevels && Util.areBitsSet(SCAN_GET_ENERGY_STATES,capabilities)){
            for(int i = 1; i< getIaea().getEnergeticStatesArray().length; i++){
                lines.add("E LEVEL "+i+" = "+ getIaea().getEnergeticStatesArray()[i].energy+" eV");
            }
        }
        if(Util.areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)){
            lines.add("HALF LIFE = "+getRawTimeSpan(energyLevel)+ " s");
            lines.add("    At current energy level");
        }
    }
}
