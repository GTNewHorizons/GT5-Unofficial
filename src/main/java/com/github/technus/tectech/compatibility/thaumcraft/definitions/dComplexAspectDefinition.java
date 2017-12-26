package com.github.technus.tectech.compatibility.thaumcraft.definitions;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eBosonDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat.aspectDefinitionCompat;
import static com.github.technus.tectech.elementalMatter.core.cElementalDecay.noDecay;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;

/**
 * Created by Tec on 06.05.2017.
 */
public final class dComplexAspectDefinition extends cElementalDefinition implements iElementalAspect {
    private final int hash;
    public final float mass;

    private static final byte nbtType = (byte) 'c';

    private final cElementalDefinitionStackMap aspectStacks;

    @Deprecated
    public dComplexAspectDefinition(cElementalDefinition... aspects) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(aspects));
    }

    @Deprecated
    private dComplexAspectDefinition(boolean check, cElementalDefinition... aspects) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(aspects));
    }

    public dComplexAspectDefinition(cElementalDefinitionStack... aspects) throws tElementalException {
        this(true, new cElementalDefinitionStackMap(aspects));
    }

    private dComplexAspectDefinition(boolean check, cElementalDefinitionStack... aspects) throws tElementalException {
        this(check, new cElementalDefinitionStackMap(aspects));
    }

    public dComplexAspectDefinition(cElementalDefinitionStackMap aspects) throws tElementalException {
        this(true, aspects);
    }

    private dComplexAspectDefinition(boolean check, cElementalDefinitionStackMap aspects) throws tElementalException {
        if (check && !canTheyBeTogether(aspects)) {
            throw new tElementalException("Hadron Definition error");
        }
        aspectStacks = aspects;
        float mass=0;
        for(cElementalDefinitionStack stack:aspects.values()){
            mass+=stack.getMass();
        }
        this.mass=mass;
        hash=super.hashCode();
    }

    //public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(cElementalDefinitionStackMap stacks) {
        long amount = 0;
        for (cElementalDefinitionStack aspects : stacks.values()) {
            if (!(aspects.definition instanceof dComplexAspectDefinition) && !(aspects.definition instanceof ePrimalAspectDefinition)) {
                return false;
            }
            amount += aspects.amount;
        }
        return amount==2;
    }

    @Override
    public String getName() {
        String name= aspectDefinitionCompat.getAspectTag(this);
        if(name!=null){
            name=name.substring(0,1).toUpperCase()+name.substring(1);
        }else{
            name=getSymbol();
        }
        return "Aspect: "+name;
    }

    @Override
    public String getSymbol() {
        StringBuilder symbol = new StringBuilder(8);
        for (cElementalDefinitionStack aspect : aspectStacks.values()) {
            if (aspect.definition instanceof ePrimalAspectDefinition) {
                for (int i = 0; i < aspect.amount; i++) {
                    symbol.append(aspect.definition.getSymbol());
                }
            } else {
                symbol.append('(');
                for (int i = 0; i < aspect.amount; i++) {
                    symbol.append(aspect.definition.getSymbol());
                }
                symbol.append(')');
            }
        }
        return symbol.toString();
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        cElementalDefinitionStack[] quarkStacksValues = aspectStacks.values();
        nbt.setInteger("i", quarkStacksValues.length);
        for (int i = 0; i < quarkStacksValues.length; i++) {
            nbt.setTag(Integer.toString(i), quarkStacksValues[i].toNBT());
        }
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        cElementalDefinitionStack[] stacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        }
        try {
            return new dComplexAspectDefinition(stacks);
        } catch (tElementalException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public float getRawTimeSpan(long currentEnergy) {
        return -1;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }

    @Override
    public int getCharge() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte getColor() {
        return -1;
    }

    @Override
    public cElementalDefinitionStackMap getSubParticles() {
        return aspectStacks;
    }

    @Override
    public cElementalDecay[] getEnergyInducedDecay(long energyLevel) {
        return new cElementalDecay[]{new cElementalDecay(0.75F, aspectStacks), eBosonDefinition.deadEnd};
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
    public cElementalDecay[] getNaturalDecayInstant() {
        return noDecay;
    }

    @Override
    public cElementalDecay[] getDecayArray() {
        return noDecay;
    }

    @Override
    public float getMass() {
        return mass;
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
    public Object materializeIntoAspect() {
        return aspectDefinitionCompat.getAspect(this);
    }

    @Override
    public iElementalDefinition getAnti() {
        return null;
    }

    public static void run() {
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, dComplexAspectDefinition.class.getMethod("fromNBT", NBTTagCompound.class),(byte)-96);
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        if(DEBUG_MODE) {
            TecTech.Logger.info("Registered Elemental Matter Class: ComplexAspect " + nbtType + ' ' + -96);
        }
    }

    @Override
    public byte getClassType() {
        return -96;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(Util.areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            lines.add("CLASS = " + nbtType + ' ' + getClassType());
        }
        if(Util.areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS, capabilities)) {
            lines.add("NAME = "+getName());
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
            lines.add("LIFE TIME = "+getRawTimeSpan(energyLevel)+ " s");
            lines.add("    "+"At current energy level");
        }
    }
}
