package com.github.technus.tectech.elementalMatter.core.templates;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemDequantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;

/**
 * Created by danie_000 on 22.10.2016.
 * EXTEND THIS TO ADD NEW PRIMITIVES, WATCH OUT FOR ID'S!!!  (-1 to 32 can be assumed as used)
 */
public abstract class cElementalPrimitive extends cElementalDefinition {
    private static final byte nbtType = (byte) 'p';

    public static final Map<Integer, iElementalDefinition> bindsBO = new HashMap<>();

    public final String name;
    public final String symbol;
    //float-mass in eV/c^2
    public final float mass;
    //int -electric charge in 1/3rds of electron charge for optimization
    public final byte charge;
    //byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    public final byte color;
    //-1/-2/-3 anti matter generations, +1/+2/+3 matter generations, 0 self anti
    public final byte type;

    private cElementalPrimitive anti;//IMMUTABLE
    private cElementalDecay[] elementalDecays;
    private byte naturalDecayInstant;
    private byte energeticDecayInstant;
    private float rawLifeTime;

    public final int ID;

    //no _ at end - normal particle
    //   _ at end - anti particle
    //  __ at end - self is antiparticle

    protected cElementalPrimitive(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        this.name = name;
        this.symbol = symbol;
        this.type = (byte) type;
        this.mass = mass;
        this.charge = (byte) charge;
        this.color = (byte) color;
        this.ID = ID;
        if (bindsBO.put(ID, this) != null) {
            Minecraft.getMinecraft().crashed(new CrashReport("Primitive definition", new tElementalException("Duplicate ID")));
        }
    }

    //
    protected void init(cElementalPrimitive antiParticle, float rawLifeTime, int naturalInstant, int energeticInstant, cElementalDecay... elementalDecaysArray) {
        anti = antiParticle;
        this.rawLifeTime = rawLifeTime;
        naturalDecayInstant = (byte) naturalInstant;
        energeticDecayInstant = (byte) energeticInstant;
        elementalDecays = elementalDecaysArray;
    }

    @Override
    public String getName() {
        return "Undefined: " + name;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public iElementalDefinition getAnti() {
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
    public float getMass() {
        return mass;
    }

    @Override
    public cElementalDecay[] getNaturalDecayInstant() {
        if (naturalDecayInstant < 0) {
            return elementalDecays;
        }
        return new cElementalDecay[]{elementalDecays[naturalDecayInstant]};
    }

    @Override
    public cElementalDecay[] getEnergyInducedDecay(long energyLevel) {
        if (energeticDecayInstant < 0) {
            return elementalDecays;
        }
        return new cElementalDecay[]{elementalDecays[energeticDecayInstant]};
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
    public cElementalDecay[] getDecayArray() {
        return elementalDecays;
    }

    @Override
    public float getRawTimeSpan(long currentEnergy) {
        return rawLifeTime;
    }

    @Override
    public final cElementalDefinitionStackMap getSubParticles() {
        return null;
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
    public byte getType() {
        return type;
    }

    @Override
    public final NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("t", nbtType);
        nbt.setInteger("c", ID);
        return nbt;
    }

    public static iElementalDefinition fromNBT(NBTTagCompound content) {
        iElementalDefinition primitive = bindsBO.get(content.getInteger("c"));
        return primitive == null ? null__ : primitive;
    }

    @Override
    public final byte getClassType() {
        return -128;
    }

    @Override
    public void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel) {
        if(Util.areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            lines.add("CLASS = " + nbtType + ' ' + getClassType());
        }
        if(Util.areBitsSet(SCAN_GET_NOMENCLATURE|SCAN_GET_CHARGE|SCAN_GET_MASS|SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add("NAME = "+getName());
            lines.add("SYMBOL = "+getSymbol());
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
            lines.add((isTimeSpanHalfLife()?"HALF LIFE = ":"LIFE TIME = ")+getRawTimeSpan(energyLevel)+ " s");
            lines.add("    "+"At current energy level");
        }
    }

    public static void run() {
        try {
            cElementalDefinition.addCreatorFromNBT(nbtType, cElementalPrimitive.class.getMethod("fromNBT", NBTTagCompound.class),(byte)-128);
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        if(DEBUG_MODE) {
            TecTech.Logger.info("Registered Elemental Matter Class: Primitive " + nbtType + ' ' + -128);
        }
    }

    @Override
    public final int compareTo(iElementalDefinition o) {
        if (getClassType() == o.getClassType()) {
            int oID = ((cElementalPrimitive) o).ID;
            if (ID > oID) {
                return 1;
            }
            if (ID < oID) {
                return -1;
            }
            return 0;
        }
        return compareClassID(o);
    }

    @Override
    public final int hashCode() {
        return ID;
    }
}