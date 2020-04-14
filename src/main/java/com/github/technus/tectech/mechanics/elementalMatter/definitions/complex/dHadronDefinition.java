package com.github.technus.tectech.mechanics.elementalMatter.definitions.complex;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalMutableDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.*;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eBosonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eQuarkDefinition;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.dComplexAspectDefinition.getNbtTagCompound;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.dAtomDefinition.transformation;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eBosonDefinition.boson_Y__;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;
import static gregtech.api.enums.OrePrefixes.dust;

/**
 * Created by danie_000 on 17.11.2016.
 */
public final class dHadronDefinition extends cElementalDefinition {//TODO Optimize map i/o
    private final int hash;

    private static final byte nbtType = (byte) 'h';
    //Helpers
    public static final Map<dHadronDefinition,String> SYMBOL_MAP =new HashMap<>();
    public static final Map<dHadronDefinition,String> NAME_MAP =new HashMap<>();
    public static dHadronDefinition hadron_p, hadron_n, hadron_p_, hadron_n_;
    public static cElementalDefinitionStack hadron_p1, hadron_n1, hadron_p2, hadron_n2, hadron_p3, hadron_n3, hadron_p5;
    private static float protonMass = 0F;
    private static float neutronMass = 0F;
    private static final float actualProtonMass=938272081.3f;
    private static final float actualNeutronMass=939565413.3f;

    //float-mass in eV/c^2
    public final float mass;
    //int -electric charge in 1/3rds of electron charge for optimization
    public final int charge;
    public final float rawLifeTime;
    public final byte amount;
    //generation max present inside - minus if contains any antiquark
    public final byte type;
    //private final FluidStack fluidThing;
    //private final ItemStack itemThing;

    private final cElementalDefinitionStackMap quarkStacks;

    @Deprecated
    public dHadronDefinition(eQuarkDefinition... quarks) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(quarks));
    }

    @Deprecated
    private dHadronDefinition(boolean check, eQuarkDefinition... quarks) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(quarks));
    }

    public dHadronDefinition(cElementalDefinitionStack... quarks) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(quarks));
    }

    private dHadronDefinition(boolean check, cElementalDefinitionStack... quarks) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(quarks));
    }

    public dHadronDefinition(cElementalDefinitionStackMap quarks) throws tElementalException {
        this(true, quarks);
    }

    private dHadronDefinition(boolean check, cElementalDefinitionStackMap quarks) throws tElementalException {
        if (check && !canTheyBeTogether(quarks)) {
            throw new tElementalException("Hadron Definition error");
        }
        quarkStacks = quarks;

        byte amount = 0;
        int charge = 0;
        int type = 0;
        boolean containsAnti = false;
        float mass = 0;
        for (cElementalDefinitionStack quarkStack : quarkStacks.values()) {
            amount += quarkStack.amount;
            mass += quarkStack.getMass();
            charge += quarkStack.getCharge();
            type = Math.max(Math.abs(quarkStack.definition.getType()), type);
            if (quarkStack.definition.getType() < 0) {
                containsAnti = true;
            }
        }
        this.amount = amount;
        this.charge = charge;
        this.type = containsAnti ? (byte) -type : (byte) type;
        int mult = this.amount * this.amount * (this.amount - 1);
        mass = mass * 5.543F * (float) mult;//yes it becomes heavier

        if (mass == protonMass && this.amount == 3) {
            rawLifeTime = iElementalDefinition.STABLE_RAW_LIFE_TIME;
            mass=actualProtonMass;
        } else if (mass == neutronMass && this.amount == 3) {
            rawLifeTime = 882F;
            mass=actualNeutronMass;
        } else {
            if (this.amount == 3) {
                rawLifeTime = 1.34F / mass * (float) Math.pow(9.81, charge);
            } else if (this.amount == 2) {
                rawLifeTime = 1.21F / mass / (float) Math.pow(19.80, charge);
            } else {
                rawLifeTime = 1.21F / mass / (float) Math.pow(9.80, charge);
            }
        }
        this.mass=mass;
        hash=super.hashCode();
    }

    //public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(cElementalDefinitionStackMap stacks) {
        long amount = 0;
        for (cElementalDefinitionStack quarks : stacks.values()) {
            if (!(quarks.definition instanceof eQuarkDefinition)) {
                return false;
            }
            amount += quarks.amount;
        }
        return amount >= 2 && amount <= 12;
    }

    @Override
    public String getName() {
        StringBuilder name= new StringBuilder(getSimpleName());
        name.append(':');
        String sym= NAME_MAP.get(this);
        if(sym!=null){
            name.append(' ').append(sym);
        }else {
            for (cElementalDefinitionStack quark : quarkStacks.values()) {
                name.append(' ').append(quark.definition.getSymbol()).append(quark.amount);
            }
        }
        return name.toString();
    }

    private String getSimpleName() {
        switch (amount) {
            case 2:
                return "Meson";
            case 3:
                return "Baryon";
            case 4:
                return "Tetraquark";
            case 5:
                return "Pentaquark";
            case 6:
                return "Hexaquark";
            default:
                return "Hadron";
        }
    }

    @Override
    public String getSymbol() {
        String sym=SYMBOL_MAP.get(this);
        if(sym!=null){
            return sym;
        }else {
            StringBuilder symbol = new StringBuilder(8);
            for (cElementalDefinitionStack quark : quarkStacks.values()) {
                for (int i = 0; i < quark.amount; i++) {
                    symbol.append(quark.definition.getSymbol());
                }
            }
            return symbol.toString();
        }
    }

    @Override
    public String getShortSymbol() {
        String sym=SYMBOL_MAP.get(this);
        if(sym!=null){
            return sym;
        }else {
            StringBuilder symbol = new StringBuilder(8);
            for (cElementalDefinitionStack quark : quarkStacks.values()) {
                for (int i = 0; i < quark.amount; i++) {
                    symbol.append(quark.definition.getShortSymbol());
                }
            }
            return symbol.toString();
        }
    }

    @Override
    public byte getColor() {
        return -7;
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return quarkStacks;
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType()) {
            return cElementalDecay.noProduct;
        }
        ArrayList<cElementalDefinitionStack> decaysInto = new ArrayList<>();
        for (cElementalDefinitionStack quarks : quarkStacks) {
            if (quarks.definition.getType() == 1 || quarks.definition.getType() == -1) {
                //covers both quarks and antiquarks
                decaysInto.add(quarks);
            } else {
                //covers both quarks and antiquarks
                decaysInto.add(new cElementalDefinitionStack(boson_Y__, 2));
            }
        }
        return new cElementalDecay[]{
                new cElementalDecay(0.75F, decaysInto.toArray(new cElementalDefinitionStack[0])),
                eBosonDefinition.deadEnd
        };
    }

    @Override
    public cElementalDecay[] getEnergyInducedDecay(long energyLevel) {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType()) {
            return cElementalDecay.noProduct;
        }
        return new cElementalDecay[]{new cElementalDecay(0.75F, quarkStacks), eBosonDefinition.deadEnd}; //decay into quarks
    }

    @Override
    public float getEnergyDiffBetweenStates(long currentEnergyLevel, long newEnergyLevel) {
        return iElementalDefinition.DEFAULT_ENERGY_REQUIREMENT *(newEnergyLevel-currentEnergyLevel);
    }

    @Override
    public boolean usesSpecialEnergeticDecayHandling() {
        return false;
    }

    @Override
    public boolean usesMultipleDecayCalls(long energyLevel) {
        return false;
    }

    @Override
    public boolean decayMakesEnergy(long energyLevel) {
        return false;
    }

    @Override
    public boolean fusionMakesEnergy(long energyLevel) {
        return false;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        cElementalDefinitionStack[] quarkStacks = this.quarkStacks.values();
        if (amount == 2 && quarkStacks.length == 2 && quarkStacks[0].definition.getMass() == quarkStacks[1].definition.getMass() && quarkStacks[0].definition.getType() == -quarkStacks[1].definition.getType()) {
            return cElementalDecay.noProduct;
        } else if (amount != 3) {
            return new cElementalDecay[]{new cElementalDecay(0.95F, quarkStacks), eBosonDefinition.deadEnd}; //decay into quarks
        } else {
            ArrayList<eQuarkDefinition> newBaryon = new ArrayList<>();
            iElementalDefinition[] Particles = new iElementalDefinition[2];
            for (cElementalDefinitionStack quarks : quarkStacks) {
                for (int i = 0; i < quarks.amount; i++) {
                    newBaryon.add((eQuarkDefinition) quarks.definition);
                }
            }
            //remove last
            eQuarkDefinition lastQuark = newBaryon.remove(2);

            if (Math.abs(lastQuark.getType()) > 1) {
                cElementalDefinitionStack[] decay = lastQuark.getDecayArray()[1].outputStacks.values();
                newBaryon.add((eQuarkDefinition) decay[0].definition);
                Particles[0] = decay[1].definition;
                Particles[1] = decay[2].definition;
            } else {
                cElementalDefinitionStack[] decay = lastQuark.getDecayArray()[0].outputStacks.values();
                newBaryon.add((eQuarkDefinition) decay[0].definition);
                Particles[0] = decay[1].definition;
                Particles[1] = decay[2].definition;
            }

            eQuarkDefinition[] contentOfBaryon = newBaryon.toArray(new eQuarkDefinition[3]);

            try {
                return new cElementalDecay[]{
                        new cElementalDecay(0.99F, new dHadronDefinition(false, contentOfBaryon), Particles[0], Particles[1]),
                        new cElementalDecay(0.001F, new dHadronDefinition(false, contentOfBaryon), Particles[0], Particles[1], boson_Y__),
                        eBosonDefinition.deadEnd};
            } catch (tElementalException e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return new cElementalDecay[]{eBosonDefinition.deadEnd};
            }
        }
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public float getRawTimeSpan(long currentEnergy) {
        return rawLifeTime;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }

    @Override
    public byte getType() {
        return type;
    }

    //@Override
    //public iElementalDefinition getAnti() {
    //    cElementalDefinitionStack[] stacks = this.quarkStacks.values();
    //    cElementalDefinitionStack[] antiElements = new cElementalDefinitionStack[stacks.length];
    //    for (int i = 0; i < antiElements.length; i++) {
    //        antiElements[i] = new cElementalDefinitionStack(stacks[i].definition.getAnti(), stacks[i].amount);
    //    }
    //    try {
    //        return new dHadronDefinition(false, antiElements);
    //    } catch (tElementalException e) {
    //        if (DEBUG_MODE) e.printStackTrace();
    //        return null;
    //    }
    //}

    @Override
    public iElementalDefinition getAnti() {
        cElementalMutableDefinitionStackMap anti = new cElementalMutableDefinitionStackMap();
        for (cElementalDefinitionStack stack : quarkStacks.values()) {
            anti.putReplace(new cElementalDefinitionStack(stack.definition.getAnti(), stack.amount));
        }
        try {
            return new dHadronDefinition(anti.toImmutable_optimized_unsafeLeavesExposedElementalTree());
        } catch (tElementalException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public aFluidDequantizationInfo someAmountIntoFluidStack() {
        return null;
    }

    @Override
    public aItemDequantizationInfo someAmountIntoItemsStack() {
        return null;
    }

    @Override
    public aOredictDequantizationInfo someAmountIntoOredictStack() {
        return null;
    }

    @Override
    public NBTTagCompound toNBT() {
        return getNbtTagCompound(nbtType, quarkStacks);
    }

    public static dHadronDefinition fromNBT(NBTTagCompound nbt) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        }
        try {
            return new dHadronDefinition(stacks);
        } catch (tElementalException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void run() {
        try {
            hadron_p = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(2), eQuarkDefinition.quark_d.getStackForm(1)));
            protonMass = hadron_p.mass;
            //redefine the proton with proper lifetime (the lifetime is based on mass comparison)
            hadron_p = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(2), eQuarkDefinition.quark_d.getStackForm(1)));
            SYMBOL_MAP.put(hadron_p,"p");
            NAME_MAP.put(hadron_p,"Proton");
            DebugElementalInstanceContainer_EM.STACKS_REGISTERED.add(hadron_p);
            hadron_p_ = (dHadronDefinition) hadron_p.getAnti();
            SYMBOL_MAP.put(hadron_p_,"~p");
            NAME_MAP.put(hadron_p_,"Anti Proton");
            DebugElementalInstanceContainer_EM.STACKS_REGISTERED.add(hadron_p_);
            hadron_n = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(1), eQuarkDefinition.quark_d.getStackForm(2)));
            neutronMass = hadron_n.mass;
            //redefine the neutron with proper lifetime (the lifetime is based on mass comparison)
            hadron_n = new dHadronDefinition(new cElementalDefinitionStackMap(eQuarkDefinition.quark_u.getStackForm(1), eQuarkDefinition.quark_d.getStackForm(2)));
            SYMBOL_MAP.put(hadron_n, "n");
            NAME_MAP.put(hadron_n, "Neutron");
            DebugElementalInstanceContainer_EM.STACKS_REGISTERED.add(hadron_n);
            hadron_n_ = (dHadronDefinition) hadron_n.getAnti();
            SYMBOL_MAP.put(hadron_n_,"~n");
            NAME_MAP.put(hadron_n_,"Anti Neutron");
            DebugElementalInstanceContainer_EM.STACKS_REGISTERED.add(hadron_n_);
        } catch (tElementalException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            protonMass = -1;
            neutronMass = -1;
        }
        hadron_p1 = new cElementalDefinitionStack(hadron_p, 1);
        hadron_n1 = new cElementalDefinitionStack(hadron_n, 1);
        hadron_p2 = new cElementalDefinitionStack(hadron_p, 2);
        hadron_n2 = new cElementalDefinitionStack(hadron_n, 2);
        hadron_p3 = new cElementalDefinitionStack(hadron_p, 3);
        hadron_n3 = new cElementalDefinitionStack(hadron_n, 3);
        hadron_p5 = new cElementalDefinitionStack(hadron_p, 5);

        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dHadronDefinition.class.getMethod("fromNBT", NBTTagCompound.class),(byte)-64);
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        if(DEBUG_MODE) {
            TecTech.LOGGER.info("Registered Elemental Matter Class: Hadron " + nbtType + ' ' + -64);
        }
    }

    public static void setTransformations(){
        //Added to atom map, but should be in its own
        cElementalDefinitionStack neutrons=new cElementalDefinitionStack(hadron_n, 100000);
        transformation.oredictDequantization.put(neutrons.definition,new aOredictDequantizationInfo(neutrons, dust, Materials.Neutronium,1));
        bTransformationInfo.oredictQuantization.put(
                OreDictionary.getOreID(OrePrefixes.ingotHot.name()+Materials.Neutronium.mName),
                new aOredictQuantizationInfo(OrePrefixes.ingotHot,Materials.Neutronium,1 ,neutrons)
        );
    }

    @Override
    public byte getClassType() {
        return -64;
    }

    public static byte getClassTypeStatic(){
        return -64;
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
            lines.add("NAME = "+getSimpleName());
            //lines.add("SYMBOL = "+getSymbol());
        }
        if(Util.areBitsSet(SCAN_GET_CHARGE,capabilities)) {
            lines.add("CHARGE = " + getCharge() / 3f + " e");
        }
        if(Util.areBitsSet(SCAN_GET_COLOR,capabilities)) {
            lines.add(getColor() < 0 ? "COLORLESS" : "CARRIES COLOR");
        }
        if(Util.areBitsSet(SCAN_GET_MASS,capabilities)) {
            lines.add("MASS = " + getMass() + " eV/c\u00b2");
        }
        if(Util.areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)){
            lines.add("HALF LIFE = "+getRawTimeSpan(energyLevel)+ " s");
            lines.add("    "+"At current energy level");
        }
    }
}
