package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 22.10.2016.
 * EXTEND THIS TO ADD NEW PRIMITIVES, WATCH OUT FOR ID'S!!!
 */
public abstract class EMPrimitiveTemplate extends EMComplexTemplate {
    private final String name;
    private final String symbol;
    // float-mass in eV/c^2
    private final double mass;
    // int -electric charge in 1/3rds of electron charge for optimization
    private final int charge;
    // byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    private final int color;
    // -1/-2/-3 anti matter generations, +1/+2/+3 matter generations, 0 self anti
    private final int generation;

    private IEMDefinition anti; // IMMUTABLE
    private EMDecay[] elementalDecays;
    private byte naturalDecayInstant;
    private byte energeticDecayInstant;
    private double rawLifeTime;

    private final int ID;
    private final String bind;

    // no _ at end - normal particle
    //   _ at end - anti particle
    //  __ at end - self is antiparticle

    protected EMPrimitiveTemplate(
            String name, String symbol, int generation, double mass, int charge, int color, int ID, String bind) {
        this.name = name;
        this.symbol = symbol;
        this.generation = generation;
        this.mass = mass;
        this.charge = charge;
        this.color = color;
        this.ID = ID;
        this.bind = bind;
    }

    //
    protected void init(
            EMDefinitionsRegistry registry,
            IEMDefinition antiParticle,
            double rawLifeTime,
            int naturalInstant,
            int energeticInstant,
            EMDecay... elementalDecaysArray) {
        anti = antiParticle;
        this.rawLifeTime = rawLifeTime;
        naturalDecayInstant = (byte) naturalInstant;
        energeticDecayInstant = (byte) energeticInstant;
        elementalDecays = elementalDecaysArray;
        registry.registerForDisplay(this);
        registry.registerDirectDefinition(bind, this);
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
    public String getShortLocalizedName() {
        return translateToLocal(getUnlocalizedName());
    }

    @Override
    public IEMDefinition getAnti() {
        return anti; // no need for copy
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public int getMaxColors() {
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
        } else if (naturalDecayInstant >= elementalDecays.length) {
            return EMDecay.NO_PRODUCT;
        }
        return new EMDecay[] {elementalDecays[naturalDecayInstant]};
    }

    @Override
    public EMDecay[] getEnergyInducedDecay(long energyLevel) {
        if (energeticDecayInstant < 0) {
            return elementalDecays;
        } else if (energeticDecayInstant >= elementalDecays.length) {
            return EMDecay.NO_PRODUCT;
        }
        return new EMDecay[] {elementalDecays[energeticDecayInstant]};
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
    public int getGeneration() {
        return generation;
    }

    @Override
    public final NBTTagCompound toNBT(EMDefinitionsRegistry registry) {
        return registry.directToNBT(bind);
    }

    @Override
    protected final String getTagValue() {
        return bind;
    }

    @Override
    public final int getMatterMassType() {
        return getClassTypeStatic();
    }

    public static int getClassTypeStatic() {
        return Short.MIN_VALUE;
    }

    @Override
    public final int compareTo(IEMDefinition o) {
        if (getMatterMassType() == o.getMatterMassType()) {
            int oID = ((EMPrimitiveTemplate) o).ID;
            return Integer.compare(ID, oID);
        }
        return compareClassID(o);
    }

    @Override
    public final int hashCode() {
        return ID;
    }

    public String getUnlocalizedName() {
        return name;
    }
}
