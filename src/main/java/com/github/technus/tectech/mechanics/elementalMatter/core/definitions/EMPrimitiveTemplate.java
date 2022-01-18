package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMFluidDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMItemDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMOredictDequantizationInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;
import static com.github.technus.tectech.util.Util.areBitsSet;

/**
 * Created by danie_000 on 22.10.2016.
 * EXTEND THIS TO ADD NEW PRIMITIVES, WATCH OUT FOR ID'S!!!
 */
public abstract class EMPrimitiveTemplate extends EMComplexTemplate {
    private final String name;
    private final String symbol;
    //float-mass in eV/c^2
    private final double mass;
    //int -electric charge in 1/3rds of electron charge for optimization
    private final int    charge;
    //byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    private final byte   color;
    //-1/-2/-3 anti matter generations, +1/+2/+3 matter generations, 0 self anti
    private final byte type;

    private EMPrimitiveTemplate anti;//IMMUTABLE
    private EMDecay[]           elementalDecays;
    private byte                naturalDecayInstant;
    private byte energeticDecayInstant;
    private double rawLifeTime;

    private final int ID;

    //no _ at end - normal particle
    //   _ at end - anti particle
    //  __ at end - self is antiparticle

    protected EMPrimitiveTemplate(String name, String symbol, int type, double mass, int charge, int color, int ID) {
        this.name = name;
        this.symbol = symbol;
        this.type = (byte) type;
        this.mass = mass;
        this.charge = charge;
        this.color = (byte) color;
        this.ID = ID;
        EMDefinitionsRegistry.registerDirectDefinition(this,ID);
    }

    //
    protected void init(EMPrimitiveTemplate antiParticle, double rawLifeTime, int naturalInstant, int energeticInstant, EMDecay... elementalDecaysArray) {
        anti = antiParticle;
        this.rawLifeTime = rawLifeTime;
        naturalDecayInstant = (byte) naturalInstant;
        energeticDecayInstant = (byte) energeticInstant;
        elementalDecays =elementalDecaysArray;
        EMDefinitionsRegistry.registerForDisplay(this);
    }

    @Override
    public String getLocalizedName() {
        return "Undefined: " + getName();
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getShortSymbol() {
        return getSymbol();
    }

    @Override
    public IEMDefinition getAnti() {
        return anti;//no need for copy
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public byte getColor() {
        return color;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public EMDecay[] getNaturalDecayInstant() {
        if (naturalDecayInstant < 0) {
            return elementalDecays;
        }else if (naturalDecayInstant>=elementalDecays.length){
            return EMDecay.NO_PRODUCT;
        }
        return new EMDecay[]{elementalDecays[naturalDecayInstant]};
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        if (energeticDecayInstant < 0) {
            return elementalDecays;
        }else if (energeticDecayInstant>=elementalDecays.length){
            return EMDecay.NO_PRODUCT;
        }
        return new EMDecay[]{elementalDecays[energeticDecayInstant]};
    }

    @Override
    public double getEnergyDiffBetweenStates(long currentEnergyLevel, long newEnergyLevel) {
        return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT *(newEnergyLevel-currentEnergyLevel);
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
    public EMDecay[] getDecayArray() {
        return elementalDecays;
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
        return rawLifeTime;
    }

    @Override
    public final EMConstantStackMap getSubParticles() {
        return null;
    }

    @Override
    public EMFluidDequantizationInfo someAmountIntoFluidStack() {
        return null;
    }

    @Override
    public EMItemDequantizationInfo someAmountIntoItemsStack() {
        return null;
    }

    @Override
    public EMOredictDequantizationInfo someAmountIntoOredictStack() {
        return null;
    }

    @Override
    public byte getMatterType() {
        return type;
    }

    @Override
    public final NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(EMDefinitionsRegistry.getDirectTagName(), ID);
        return nbt;
    }

    @Override
    public final byte getClassType() {
        return getClassTypeStatic();
    }

    public static byte getClassTypeStatic(){
        return -128;
    }

    @Override
    public void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS|SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add(getShortSymbol());
        }
    }

    @Override
    public void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            lines.add("CLASS = " + EMDefinitionsRegistry.getDirectTagName() + ' ' + getClassType());
        }
        if(areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS|SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add("NAME = "+ getLocalizedName());
            lines.add("SYMBOL = "+getSymbol());
        }
        if(areBitsSet(SCAN_GET_CHARGE,capabilities)) {
            lines.add("CHARGE = " + getCharge() / 3D + " e");
        }
        if(areBitsSet(SCAN_GET_COLOR,capabilities)) {
            lines.add(getColor() < 0 ? "COLORLESS" : "CARRIES COLOR");
        }
        if(areBitsSet(SCAN_GET_MASS,capabilities)) {
            lines.add("MASS = " + getMass() + " eV/c\u00b2");
        }
        if(areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)){
            lines.add((isTimeSpanHalfLife()?"HALF LIFE = ":"LIFE TIME = ")+getRawTimeSpan(energyLevel)+ " s");
            lines.add("    "+"At current energy level");
        }
    }

    @Override
    public final int compareTo(IEMDefinition o) {
        if (getClassType() == o.getClassType()) {
            int oID = ((EMPrimitiveTemplate) o).ID;
            return Integer.compare(ID, oID);
        }
        return compareClassID(o);
    }

    @Override
    public final int hashCode() {
        return ID;
    }

    public String getName() {
        return name;
    }
}