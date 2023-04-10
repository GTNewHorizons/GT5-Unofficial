package com.github.technus.tectech.mechanics.elementalMatter.core.stacks;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.null__;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_AGE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_AMOUNT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_COLOR_VALUE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_DEPTH_LEVEL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_ENERGY;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_ENERGY_LEVEL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_scanner.SCAN_GET_TIMESPAN_MULT;
import static com.github.technus.tectech.util.DoubleCount.add;
import static com.github.technus.tectech.util.DoubleCount.distribute;
import static com.github.technus.tectech.util.DoubleCount.div;
import static com.github.technus.tectech.util.DoubleCount.ulpSigned;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecayResult;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class EMInstanceStack implements IEMStack {

    public static int MIN_MULTIPLE_DECAY_CALLS = 4, MAX_MULTIPLE_DECAY_CALLS = 16;
    public static double DECAY_CALL_PER = EM_COUNT_PER_MATERIAL_AMOUNT; // todo

    private final IEMDefinition definition;
    private double amount;

    private double age;
    // energy - if positive then particle should try to decay
    private long energy;
    // byte color; 0=Red 1=Green 2=Blue 0=Cyan 1=Magenta 2=Yellow, else ignored (-1 - uncolorable)
    private int color;
    private double lifeTime;
    private double lifeTimeMult;

    public EMInstanceStack(IEMStack stackSafe) {
        this(stackSafe.getDefinition(), stackSafe.getAmount(), 1D, 0D, 0);
    }

    public EMInstanceStack(IEMStack stackSafe, double lifeTimeMult, double age, long energy) {
        this(stackSafe.getDefinition(), stackSafe.getAmount(), lifeTimeMult, age, energy);
    }

    public EMInstanceStack(IEMDefinition defSafe, double amount) {
        this(defSafe, amount, 1D, 0D, 0);
    }

    public EMInstanceStack(IEMDefinition defSafe, double amount, double lifeTimeMult, double age, long energy) {
        definition = defSafe == null ? null__ : defSafe;
        if (getDefinition().hasColor()) {
            this.color = (byte) TecTech.RANDOM.nextInt(getDefinition().getMaxColors());
        } else { // transforms colorable??? into proper color
            this.color = getDefinition().getMaxColors();
        }
        this.lifeTimeMult = lifeTimeMult;
        lifeTime = getDefinition().getRawTimeSpan(energy) * this.lifeTimeMult;
        setEnergy(energy);
        this.setAge(age);
        this.setAmount(amount);
    }

    // Clone proxy
    private EMInstanceStack(EMInstanceStack stack) {
        definition = stack.getDefinition();
        color = stack.color;
        setAge(stack.getAge());
        setAmount(stack.getAmount());
        lifeTime = stack.lifeTime;
        lifeTimeMult = stack.lifeTimeMult;
        energy = stack.energy;
    }

    @Override
    public EMInstanceStack clone() {
        return new EMInstanceStack(this);
    }

    @Override
    public EMInstanceStack mutateAmount(double newAmount) {
        this.setAmount(newAmount);
        return this;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    public long getEnergy() {
        return energy;
    }

    public void setEnergy(long newEnergyLevel) {
        energy = newEnergyLevel;
        setLifeTimeMultiplier(getLifeTimeMultiplier());
    }

    public double getEnergySettingCost(long currentEnergyLevel, long newEnergyLevel) {
        return getDefinition().getEnergyDiffBetweenStates(currentEnergyLevel, newEnergyLevel) * getAmount();
    }

    public double getEnergySettingCost(long newEnergyLevel) {
        return getEnergySettingCost(energy, newEnergyLevel) * getAmount();
    }

    public EMDefinitionStack getDefinitionStack() {
        return new EMDefinitionStack(getDefinition(), getAmount());
    }

    @Override
    public IEMDefinition getDefinition() {
        return definition;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) { // does not allow changing magic element
        if (this.color < 0 || this.color > 2 || color < 0 || color >= 3) {
            return;
        }
        this.color = color;
    }

    public void nextColor() { // does not allow changing magic element
        if (definition.hasColor()) {
            color = (byte) TecTech.RANDOM.nextInt(definition.getMaxColors());
        }
    }

    public double getLifeTime() {
        return lifeTime;
    }

    public void setLifeTimeMultiplier(double mult) {
        if (mult <= 0) // since infinity*0=nan
        {
            throw new IllegalArgumentException("multiplier must be >0");
        }
        lifeTimeMult = mult;
        if (getDefinition().getRawTimeSpan(energy) <= 0) {
            return;
        }
        lifeTime = getDefinition().getRawTimeSpan(energy) * lifeTimeMult;
    }

    public double getLifeTimeMultiplier() {
        return lifeTimeMult;
    }

    public EMDecayResult tickStackByOneSecond(double lifeTimeMult, int postEnergize) {
        return tickStack(lifeTimeMult, postEnergize, 1D);
    }

    public EMDecayResult tickStack(double lifeTimeMult, int postEnergize, double seconds) {
        setAge(getAge() + seconds);
        EMDecayResult newInstances = decay(lifeTimeMult, getAge(), postEnergize);
        if (newInstances == null) {
            nextColor();
        } else {
            for (EMInstanceStack newInstance : newInstances.getOutput().valuesToArray()) {
                newInstance.nextColor();
            }
        }
        return newInstances;
    }

    public EMDecayResult decay() {
        return decay(1D, getAge(), 0); // try to decay without changes
    }

    public EMDecayResult decay(double apparentAge, long postEnergize) {
        return decay(1D, apparentAge, postEnergize);
    }

    public EMDecayResult decay(double lifeTimeMult, double apparentAge, long postEnergize) {
        long newEnergyLevel = postEnergize + energy;
        if (newEnergyLevel > 0) {
            newEnergyLevel -= 1;
        } else if (newEnergyLevel < 0) {
            newEnergyLevel += 1;
        }
        EMDecayResult output;
        if (getDefinition().usesMultipleDecayCalls(energy)) {
            double amountTemp = getAmount();
            int decayCnt = (int) min(
                    MAX_MULTIPLE_DECAY_CALLS,
                    max(getAmount() / DECAY_CALL_PER, MIN_MULTIPLE_DECAY_CALLS));
            setAmount(div(getAmount(), decayCnt));
            decayCnt--;

            output = decayMechanics(lifeTimeMult, apparentAge, newEnergyLevel);
            if (output == null) {
                setAmount(amountTemp);
                return null;
            }

            for (int i = 0; i < decayCnt; i++) {
                EMDecayResult map = decayMechanics(lifeTimeMult, apparentAge, newEnergyLevel);
                if (map != null) {
                    output.getOutput().putUnifyAll(map.getOutput());
                    output.setMassDiff(add(output.getMassDiff(), map.getMassDiff()));
                    output.setMassAffected(output.getMassDiff() + map.getMassDiff());
                }
            }
            setAmount(amountTemp);
        } else {
            output = decayMechanics(lifeTimeMult, apparentAge, newEnergyLevel);
        }
        if (output != null) {
            output.getOutput().cleanUp();
        }
        return output;
    }

    private EMDecayResult decayMechanics(double lifeTimeMult, double apparentAge, long newEnergyLevel) {
        if (energy > 0 && !getDefinition().usesSpecialEnergeticDecayHandling()) {
            setLifeTimeMultiplier(getLifeTimeMultiplier());
            return decayCompute(getDefinition().getEnergyInducedDecay(energy), lifeTimeMult, -1D, newEnergyLevel);
        } else if (getDefinition().getRawTimeSpan(energy) < 0) {
            return null; // return null, decay cannot be achieved
        } else if (getDefinition().isTimeSpanHalfLife()) {
            return exponentialDecayCompute(
                    energy > 0 ? getDefinition().getEnergyInducedDecay(energy) : getDefinition().getDecayArray(),
                    lifeTimeMult,
                    -1D,
                    newEnergyLevel);
        } else {
            if (1 > lifeTime) {
                return decayCompute(
                        energy > 0 ? getDefinition().getEnergyInducedDecay(energy)
                                : getDefinition().getNaturalDecayInstant(),
                        lifeTimeMult,
                        0D,
                        newEnergyLevel);
            } else if (apparentAge > lifeTime) {
                return decayCompute(
                        energy > 0 ? getDefinition().getEnergyInducedDecay(energy) : getDefinition().getDecayArray(),
                        lifeTimeMult,
                        0D,
                        newEnergyLevel);
            }
        }
        return null; // return null since decay cannot be achieved
    }

    // Use to get direct decay output providing correct decay array
    private EMDecayResult exponentialDecayCompute(EMDecay[] decays, double lifeTimeMult, double newProductsAge,
            long newEnergyLevel) {
        double newAmount = div(getAmount(), Math.pow(2D, 1D /* 1 second */ / lifeTime));

        if (newAmount == getAmount()) {
            newAmount -= ulpSigned(newAmount);
        } else if (newAmount < 1) {
            return decayCompute(decays, lifeTimeMult, newProductsAge, newEnergyLevel);
        }

        // split to non decaying and decaying part
        double amount = this.getAmount();
        this.setAmount(this.getAmount() - newAmount);
        EMDecayResult products = decayCompute(decays, lifeTimeMult, newProductsAge, newEnergyLevel);
        this.setAmount(newAmount);
        if (products != null) {
            products.getOutput().putUnify(clone());
        }
        this.setAmount(amount);
        return products;
    }

    // Use to get direct decay output providing correct decay array
    private EMDecayResult decayCompute(EMDecay[] decays, double lifeTimeMult, double newProductsAge,
            long newEnergyLevel) {
        if (decays == null) {
            return null; // Can not decay so it won't
        }
        boolean makesEnergy = getDefinition().decayMakesEnergy(energy);
        double mass = getMass();
        if (decays.length == 0) {
            return makesEnergy ? null : new EMDecayResult(new EMInstanceStackMap(), mass, 0);
            // provide non null 0 length array for annihilation
        } else if (decays.length == 1) { // only one type of decay :D, doesn't need dead end
            if (decays[0] == deadEnd) {
                return makesEnergy ? null
                        : new EMDecayResult(
                                decays[0].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, getAmount()),
                                mass,
                                0);
            }
            EMInstanceStackMap output = decays[0].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, getAmount());
            if (newProductsAge < 0) {
                if (output.size() == 1) {
                    if (output.size() == 1 && output.getFirst().getDefinition().equals(getDefinition())) {
                        output.getFirst().setEnergy(energy);
                        output.getFirst().setAge(getAge());
                    }
                } else {
                    for (EMInstanceStack stack : output.valuesToArray()) {
                        if (stack.getDefinition().equals(getDefinition())) {
                            stack.setAge(getAge());
                        }
                    }
                }
            } else {
                if (output.size() == 1 && output.getFirst().getDefinition().equals(getDefinition())) {
                    output.getFirst().setEnergy(energy);
                }
            }
            if (energy <= 0 && output.getMass() > mass) {
                return null; // no energy usage to decay
            }
            return new EMDecayResult(new EMInstanceStackMap(), mass, makesEnergy ? output.getMass() - mass : 0);
        } else {
            EMDecayResult totalOutput = new EMDecayResult(new EMInstanceStackMap(), getMass(), 0);
            EMInstanceStackMap output = totalOutput.getOutput(), results;
            int differentDecays = decays.length;
            double[] probabilities = new double[differentDecays];
            for (int i = 0; i < probabilities.length; i++) {
                probabilities[i] = decays[i].getProbability();
            }
            double[] qttyOfDecay;
            try {
                qttyOfDecay = distribute(getAmount(), probabilities);
            } catch (ArithmeticException e) {
                Minecraft.getMinecraft().crashed(new CrashReport("Decay failed for: " + this, e));
                return null;
            }

            if (getDefinition().decayMakesEnergy(energy)) {
                for (int i = differentDecays - 1; i >= 0; i--) {
                    if (decays[i] == deadEnd) {
                        EMInstanceStack clone = clone();
                        clone.setAmount(qttyOfDecay[i]);
                        output.putUnify(clone);
                    } else {
                        results = decays[i].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, qttyOfDecay[i]);
                        output.putUnifyAll(results);
                        totalOutput.setMassDiff(add(totalOutput.getMassDiff(), results.getMass() - mass));
                    }
                }
            } else {
                for (int i = differentDecays - 1; i >= 0; i--) {
                    results = decays[i].getResults(lifeTimeMult, newProductsAge, newEnergyLevel, qttyOfDecay[i]);
                    output.putUnifyAll(results);
                }
            }

            if (newProductsAge < 0) {
                if (output.size() == 1 && output.getFirst().getDefinition().equals(getDefinition())) {
                    output.getFirst().setEnergy(energy);
                    output.getFirst().setAge(getAge());
                } else {
                    for (EMInstanceStack stack : output.valuesToArray()) {
                        if (stack.getDefinition().equals(getDefinition())) {
                            stack.setAge(getAge());
                        }
                    }
                }
            } else {
                if (output.size() == 1 && output.getFirst().getDefinition().equals(getDefinition())) {
                    output.getFirst().setEnergy(energy);
                    output.getFirst().setAge(getAge());
                }
            }
            if (energy <= 0 && output.getMass() > getMass()) {
                return null; // no energy usage to decay
            }
            return totalOutput;
        }
    }

    public EMInstanceStack unifyIntoThis(EMInstanceStack... instances) {
        if (instances == null) {
            return this;
        }
        // returns with the definition from the first object passed
        double energyTotal = getEnergySettingCost(0, energy);
        long maxEnergy = energy;
        long minEnergy = energy;

        for (EMInstanceStack instance : instances) {
            // if (instance != null && compareTo(instance) == 0) {
            setAmount(add(getAmount(), instance.getAmount()));
            energyTotal += instance.getEnergySettingCost(0, instance.energy);
            maxEnergy = max(instance.energy, maxEnergy);
            minEnergy = min(instance.energy, maxEnergy);
            lifeTimeMult = min(lifeTimeMult, instance.lifeTimeMult);
            setAge(max(getAge(), instance.getAge()));
            // }
        }

        if (energyTotal >= 0) {
            for (; maxEnergy > 0; maxEnergy--) {
                if (getEnergySettingCost(0, maxEnergy) < energyTotal) {
                    break;
                }
            }
            setEnergy(maxEnergy);
        } else {
            for (; minEnergy < 0; minEnergy++) {
                if (getEnergySettingCost(minEnergy, 0) < energyTotal) {
                    break;
                }
            }
            setEnergy(minEnergy);
        }
        return this;
    }

    public EMInstanceStack unifyIntoThisExact(EMInstanceStack... instances) {
        if (instances == null) {
            return this;
        }
        // returns with the definition from the first object passed
        double energyTotal = getEnergySettingCost(0, energy);
        long maxEnergy = energy;
        long minEnergy = energy;

        for (EMInstanceStack instance : instances) {
            // if (instance != null && compareTo(instance) == 0) {
            setAmount(getAmount() + instance.getAmount());
            energyTotal += instance.getEnergySettingCost(0, instance.energy);
            maxEnergy = max(instance.energy, maxEnergy);
            minEnergy = min(instance.energy, maxEnergy);
            lifeTimeMult = min(lifeTimeMult, instance.lifeTimeMult);
            setAge(max(getAge(), instance.getAge()));
            // }
        }

        if (energyTotal >= 0) {
            for (; maxEnergy > 0; maxEnergy--) {
                if (getEnergySettingCost(0, maxEnergy) < energyTotal) {
                    break;
                }
            }
            setEnergy(maxEnergy);
        } else {
            for (; minEnergy < 0; minEnergy++) {
                if (getEnergySettingCost(minEnergy, 0) < energyTotal) {
                    break;
                }
            }
            setEnergy(minEnergy);
        }
        return this;
    }

    public void addScanResults(ArrayList<String> lines, int[] detailsOnDepthLevels) {
        int capabilities = detailsOnDepthLevels[0];
        if (TT_Utility.areBitsSet(SCAN_GET_DEPTH_LEVEL, capabilities)) {
            lines.add(translateToLocal("tt.keyword.scan.depth") + " = " + 0);
        }
        getDefinition().addScanResults(lines, capabilities, energy, TecTech.definitionsRegistry);

        if (TT_Utility.areBitsSet(SCAN_GET_TIMESPAN_MULT, capabilities)) {
            lines.add(translateToLocal("tt.keyword.scan.life_mult") + " = " + lifeTimeMult);
        }
        if (TT_Utility.areBitsSet(SCAN_GET_AGE, capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.age") + " = "
                            + getAge()
                            + " "
                            + translateToLocal("tt.keyword.unit.time"));
        }
        if (TT_Utility.areBitsSet(SCAN_GET_COLOR_VALUE, capabilities)) {
            lines.add(translateToLocal("tt.keyword.scan.color") + " = " + color);
        }
        if (TT_Utility.areBitsSet(SCAN_GET_ENERGY, capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.energy") + " = "
                            + getDefinition().getEnergyDiffBetweenStates(0, energy)
                            + " "
                            + translateToLocal("tt.keyword.unit.energy"));
        }
        if (TT_Utility.areBitsSet(SCAN_GET_ENERGY_LEVEL, capabilities)) {
            lines.add(translateToLocal("tt.keyword.scan.energyLevel") + " = " + energy);
        }
        if (TT_Utility.areBitsSet(SCAN_GET_AMOUNT, capabilities)) {
            lines.add(
                    translateToLocal("tt.keyword.scan.amount") + " = "
                            + getAmount() / AVOGADRO_CONSTANT
                            + " "
                            + translateToLocal("tt.keyword.unit.mol"));
        }

        scanContents(lines, getDefinition().getSubParticles(), 1, detailsOnDepthLevels);
    }

    private void scanContents(ArrayList<String> lines, EMConstantStackMap definitions, int depth,
            int[] detailsOnDepthLevels) {
        if (definitions != null && depth < detailsOnDepthLevels.length) {
            int deeper = depth + 1;
            for (EMDefinitionStack definitionStack : definitions.valuesToArray()) {
                lines.add(""); // def separator
                if (TT_Utility.areBitsSet(SCAN_GET_DEPTH_LEVEL, detailsOnDepthLevels[depth])) {
                    lines.add(translateToLocal("tt.keyword.scan.depth") + " = " + depth);
                }
                getDefinition().addScanResults(lines, detailsOnDepthLevels[depth], energy, TecTech.definitionsRegistry);
                if (TT_Utility.areBitsSet(SCAN_GET_AMOUNT, detailsOnDepthLevels[depth])) {
                    lines.add(translateToLocal("tt.keyword.scan.count") + " = " + definitionStack.getAmount());
                }
                scanContents(lines, definitionStack.getDefinition().getSubParticles(), deeper, detailsOnDepthLevels);
            }
        }
    }

    public NBTTagCompound toNBT(EMDefinitionsRegistry registry) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", getDefinition().toNBT(registry));
        nbt.setDouble("Q", getAmount());
        nbt.setDouble("M", getLifeTimeMultiplier());
        nbt.setDouble("A", getAge());
        nbt.setLong("e", getEnergy());
        nbt.setInteger("c", getColor());
        return nbt;
    }

    public static EMInstanceStack fromNBT(EMDefinitionsRegistry registry, NBTTagCompound nbt) {
        EMInstanceStack instance = new EMInstanceStack(
                registry.fromNBT(nbt.getCompoundTag("d")),
                nbt.getDouble("Q"),
                nbt.getDouble("M"),
                nbt.getDouble("A"),
                nbt.getLong("e"));
        instance.setColor(nbt.getInteger("c"));
        return instance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IEMDefinition) {
            return getDefinition().compareTo((IEMDefinition) obj) == 0;
        }
        if (obj instanceof IEMStack) {
            return getDefinition().compareTo(((IEMStack) obj).getDefinition()) == 0;
        }
        return false;
    }

    // Amount shouldn't be hashed if this is just indicating amount and not structure, DOES NOT CARE ABOUT
    // creativeTabTecTech INFO
    @Override
    public int hashCode() {
        return getDefinition().hashCode();
    }

    @Override
    public String toString() {
        return getDefinition().toString() + ' '
                + getAmount() / AVOGADRO_CONSTANT
                + " "
                + translateToLocal("tt.keyword.unit.mol")
                + " "
                + getMass()
                + " "
                + translateToLocal("tt.keyword.unit.mass");
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }
}
