package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations.AspectDefinitionCompat;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMDefinitionsRegistry;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMComplexTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMFluidDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMItemDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMOredictDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMBosonDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.*;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 06.05.2017.
 */
public final class EMComplexAspectDefinition extends EMComplexTemplate {
    private final int    hash;
    private final double mass;

    private static final byte nbtType = (byte) 'c';

    private final EMConstantStackMap aspectStacks;

    public EMComplexAspectDefinition(EMDefinitionStack... aspects) throws EMException {
        this(true, new EMConstantStackMap(aspects));
    }

    private EMComplexAspectDefinition(boolean check, EMDefinitionStack... aspects) throws EMException {
        this(check, new EMConstantStackMap(aspects));
    }

    public EMComplexAspectDefinition(EMConstantStackMap aspects) throws EMException {
        this(true, aspects);
    }

    private EMComplexAspectDefinition(boolean check, EMConstantStackMap aspects) throws EMException {
        if (check && !canTheyBeTogether(aspects)) {
            throw new EMException("Complex Aspect Definition error");
        }
        aspectStacks = aspects;
        float mass = 0;
        for (EMDefinitionStack stack : aspects.valuesToArray()) {
            mass += stack.getMass();
        }
        this.mass = mass;
        hash = super.hashCode();
    }

    //public but u can just try{}catch(){} the constructor it still calls this method
    private static boolean canTheyBeTogether(EMConstantStackMap stacks) {
        long amount = 0;
        for (EMDefinitionStack aspects : stacks.valuesToArray()) {
            if (!(aspects.getDefinition() instanceof EMComplexAspectDefinition) && !(aspects.getDefinition() instanceof EMPrimalAspectDefinition)) {
                return false;
            }
            if((int) aspects.getAmount() != aspects.getAmount()){
                throw new ArithmeticException("Amount cannot be safely converted to int!");
            }
            amount += aspects.getAmount();
        }
        return amount == 2;
    }

    @Override
    public String getLocalizedName() {
        String name = AspectDefinitionCompat.aspectDefinitionCompat.getAspectTag(this);
        if (name != null) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            name = getSymbol();
        }
        return translateToLocal("tt.keyword.Aspect") + ": " + name;
    }

    @Override
    public String getSymbol() {
        StringBuilder symbol = new StringBuilder(8);
        for (EMDefinitionStack aspect : aspectStacks.valuesToArray()) {
            if (aspect.getDefinition() instanceof EMPrimalAspectDefinition) {
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getSymbol());
                }
            } else {
                symbol.append('(');
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getSymbol());
                }
                symbol.append(')');
            }
        }
        return symbol.toString();
    }

    @Override
    public String getShortSymbol() {
        StringBuilder symbol = new StringBuilder(8);
        for (EMDefinitionStack aspect : aspectStacks.valuesToArray()) {
            if (aspect.getDefinition() instanceof EMPrimalAspectDefinition) {
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getShortSymbol());
                }
            } else {
                symbol.append('(');
                for (int i = 0; i < aspect.getAmount(); i++) {
                    symbol.append(aspect.getDefinition().getShortSymbol());
                }
                symbol.append(')');
            }
        }
        return symbol.toString();
    }

    @Override
    protected int getIndirectTagValue() {
        return nbtType;
    }

    public static EMComplexAspectDefinition fromNBT(NBTTagCompound nbt) {
        EMDefinitionStack[] stacks = new EMDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = EMDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
        }
        try {
            return new EMComplexAspectDefinition(stacks);
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public double getRawTimeSpan(long currentEnergy) {
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
    public byte getMatterType() {
        return 0;
    }

    @Override
    public byte getColor() {
        return -1;
    }

    @Override
    public EMConstantStackMap getSubParticles() {
        return aspectStacks;
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        return new EMDecay[]{new EMDecay(0.75F, aspectStacks), EMBosonDefinition.deadEnd};
    }

    @Override
    public double getEnergyDiffBetweenStates(long currentEnergyLevel, long newEnergyLevel) {
        return IEMDefinition.DEFAULT_ENERGY_REQUIREMENT * (newEnergyLevel - currentEnergyLevel);
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
    public EMDecay[] getNaturalDecayInstant() {
        return NO_DECAY;
    }

    @Override
    public EMDecay[] getDecayArray() {
        return NO_DECAY;
    }

    @Override
    public double getMass() {
        return mass;
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
    public IEMDefinition getAnti() {
        return null;
    }

    public static void run() {
        try {
            EMDefinitionsRegistry.registerDefinitionClass(nbtType, EMComplexAspectDefinition::fromNBT,EMComplexAspectDefinition.class, getClassTypeStatic());
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        if (DEBUG_MODE) {
            TecTech.LOGGER.info("Registered Elemental Matter Class: ComplexAspect " + nbtType + ' ' + getClassTypeStatic());
        }
    }

    @Override
    public byte getClassType() {
        return getClassTypeStatic();
    }

    public static byte getClassTypeStatic() {
        return -96;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel) {
        if (Util.areBitsSet(SCAN_GET_NOMENCLATURE | SCAN_GET_CHARGE | SCAN_GET_MASS, capabilities)) {
            lines.add(getShortSymbol());
        }
    }

    @Override
    public void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel) {
        if (Util.areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            lines.add(translateToLocal("tt.keyword.CLASS") + " = " + nbtType + ' ' + getClassType());
        }
        if (Util.areBitsSet(SCAN_GET_NOMENCLATURE | SCAN_GET_CHARGE | SCAN_GET_MASS, capabilities)) {
            lines.add(translateToLocal("tt.keyword.NAME") + " = " + getLocalizedName());
            //lines.add("SYMBOL = "+getSymbol());
        }
        if (Util.areBitsSet(SCAN_GET_CHARGE, capabilities)) {
            lines.add(translateToLocal("tt.keyword.CHARGE") + " = " + getCharge() / 3f + " e");
        }
        if (Util.areBitsSet(SCAN_GET_COLOR, capabilities)) {
            lines.add(getColor() < 0 ? translateToLocal("tt.keyword.COLORLESS") : translateToLocal("tt.keyphrase.CARRIES_COLOR"));
        }
        if (Util.areBitsSet(SCAN_GET_MASS, capabilities)) {
            lines.add(translateToLocal("tt.keyword.MASS") + " = " + getMass() + " eV/c\u00b2");
        }
        if (Util.areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add(translateToLocal("tt.keyphrase.LIFE_TIME") + " = " + getRawTimeSpan(energyLevel) + " s");
            lines.add("    " + translateToLocal("tt.keyphrase.At_current_energy_level"));
        }
    }
}
