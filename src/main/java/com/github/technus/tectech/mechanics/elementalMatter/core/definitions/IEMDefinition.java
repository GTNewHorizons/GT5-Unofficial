package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_CHARGE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_CLASS_TYPE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_COLORABLE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_MASS;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_NOMENCLATURE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_TIMESPAN_INFO;
import static com.github.technus.tectech.util.TT_Utility.areBitsSet;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 11.11.2016.
 */
public interface IEMDefinition extends Comparable<IEMDefinition>, Cloneable { // IMMUTABLE

    double STABLE_RAW_LIFE_TIME = 1.5e36D;
    double NO_DECAY_RAW_LIFE_TIME = -1D;
    long DEFAULT_ENERGY_LEVEL = 0;
    double DEFAULT_ENERGY_REQUIREMENT = 25000D; // legit cuz normal atoms should only emit a gamma if they don't have
                                                // defined energy levels
    // add text based creators for recipe formula input?

    // Nomenclature
    String getLocalizedTypeName();

    String getShortLocalizedName();

    default String getLocalizedName() {
        return getLocalizedTypeName() + ": " + getShortLocalizedName();
    }

    String getShortSymbol();

    String getSymbol();

    default void addScanShortSymbols(ArrayList<String> lines, int capabilities, long energyLevel,
            EMDefinitionsRegistry registry) {
        if (areBitsSet(
                SCAN_GET_NOMENCLATURE | SCAN_GET_CLASS_TYPE | SCAN_GET_CHARGE | SCAN_GET_MASS | SCAN_GET_TIMESPAN_INFO,
                capabilities)) {
            lines.add(getShortSymbol());
        }
    }

    default void addScanResults(ArrayList<String> lines, int capabilities, long energyLevel,
            EMDefinitionsRegistry registry) {
        if (TT_Utility.areBitsSet(
                SCAN_GET_CLASS_TYPE | SCAN_GET_CHARGE | SCAN_GET_MASS | SCAN_GET_TIMESPAN_INFO,
                capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.class") + " = "
                            + registry.getTypes().get(getClass()).getLocalizedName());
            if (areBitsSet(SCAN_GET_NOMENCLATURE, capabilities)) {
                lines.add(translateToLocal("tt.keyword.scan.name") + " = " + getLocalizedName());
                lines.add(translateToLocal("tt.keyword.scan.symbol") + " = " + getSymbol());
            }
        }
        if (areBitsSet(SCAN_GET_CHARGE, capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.charge") + " = "
                            + getCharge() / 3D
                            + " "
                            + translateToLocal("tt.keyword.unit.charge"));
        }
        if (areBitsSet(SCAN_GET_COLORABLE, capabilities)) {
            lines.add(
                    hasColor() ? translateToLocal("tt.keyword.scan.colored")
                            : translateToLocal("tt.keyword.scan.colorless"));
        }
        if (areBitsSet(SCAN_GET_MASS, capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.mass") + " = "
                            + getMass()
                            + " "
                            + translateToLocal("tt.keyword.unit.mass"));
        }
        if (areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)) {
            lines.add(
                    (isTimeSpanHalfLife() ? translateToLocal("tt.keyword.scan.half_life")
                            : translateToLocal("tt.keyword.scan.life_time")) + " = "
                            + getRawTimeSpan(energyLevel)
                            + " "
                            + translateToLocal("tt.keyword.unit.time"));
            lines.add("    " + translateToLocal("tt.keyphrase.scan.at_current_energy_level"));
        }
    }

    int getMatterMassType(); // bigger number means bigger things usually, but it is just used to differentiate
    // between classes of iED

    int getGeneration();

    // Not dynamically changing stuff
    IEMDefinition getAnti(); // gives new anti particle def

    EMDecay[] getDecayArray(); // possible decays

    EMDecay[] getNaturalDecayInstant(); // natural decay if lifespan <1tick

    EMDecay[] getEnergyInducedDecay(long energyLevel); // energetic decay

    boolean usesSpecialEnergeticDecayHandling();

    boolean usesMultipleDecayCalls(long energyLevel);

    boolean decayMakesEnergy(long energyLevel);

    boolean fusionMakesEnergy(long energyLevel);

    double getEnergyDiffBetweenStates(long currentEnergy, long newEnergyLevel); // positive or negative

    double getMass(); // mass... MeV/c^2

    int getCharge(); // charge 1/3 electron charge

    // dynamically changing stuff
    int getMaxColors();

    default boolean hasColor() {
        return getMaxColors() > 0;
    }

    double getRawTimeSpan(long currentEnergy); // defined in static fields or generated

    boolean isTimeSpanHalfLife();

    EMConstantStackMap getSubParticles(); // contents... null if none

    NBTTagCompound toNBT(EMDefinitionsRegistry registry);

    default EMDefinitionStack getStackForm(double amount) {
        return new EMDefinitionStack(this, amount);
    }

    default IEMDefinition clone() {
        return this;
    }

    default int compareClassID(IEMDefinition obj) {
        return getMatterMassType() - obj.getMatterMassType();
    }
}
