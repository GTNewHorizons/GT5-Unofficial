package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.DoubleCount.div;
import static com.github.technus.tectech.util.DoubleCount.mul;
import static com.github.technus.tectech.util.DoubleCount.sub;

import java.util.Arrays;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Centrifuge implements GT_MetaTileEntity_EM_machine.IBehaviour {

    private final byte tier;
    private double maxRPM;
    private final double radius;
    private final double maxRCF;
    private final double maxForce;
    private final double maxCapacity;
    private Parameters.Group.ParameterIn settingRPM, settingFraction;
    private static final INameFunction<GT_MetaTileEntity_EM_machine> rpmName = (gt_metaTileEntity_em_machine,
            iParameter) -> "RPM Setting";
    private final IStatusFunction<GT_MetaTileEntity_EM_machine> rpmStatus = (gt_metaTileEntity_em_machine,
            iParameter) -> {
        double v = iParameter.get();
        if (Double.isNaN(v)) {
            return STATUS_WRONG;
        }
        if (v <= 0) {
            return STATUS_TOO_LOW;
        } else if (v > maxRPM) {
            return STATUS_TOO_HIGH;
        }
        return STATUS_OK;
    };
    private static final INameFunction<GT_MetaTileEntity_EM_machine> fractionName = (gt_metaTileEntity_em_machine,
            iParameter) -> "Fraction Count";
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> fractionStatus = (gt_metaTileEntity_em_machine,
            iParameter) -> {
        double v = iParameter.get();
        if (Double.isNaN(v)) {
            return STATUS_WRONG;
        }
        v = (int) v;
        if (v <= 1) {
            return STATUS_TOO_LOW;
        } else if (v > 6) {
            return STATUS_TOO_HIGH;
        }
        return STATUS_OK;
    };
    // private final static String[] DESCRIPTION_O =new String[]{"RPM Setting","RCF Setting","Radius [mm]","Max
    // RPM","Max Force [eV/c^2 * m/s]","Max Capacity [eV/c^2]","Max Power Usage[EU/t]","Max Recipe Rime [tick]"};

    private static final double[ /* tier+5 */][ /* outputHatches+2 */] MIXING_FACTORS = new double[][] {
            { .45, .85, .95, 1, 1, }, { .4, .75, .9, .95, 1, }, { .35, .45, .75, .9, .95, }, { .25, .3, .45, .75, .9, },
            { .2, .25, .3, .45, .75, }, { .1, .15, .2, .3, .45, }, { .05, .1, .15, .2, .25, },
            { .01, .05, .1, .15, .2, }, };

    // 6 to 12 recommended
    public Behaviour_Centrifuge(int desiredTier) {
        tier = (byte) desiredTier;
        radius = 0.5D - (12D - tier) / 64D;
        maxRCF = Math.pow(Math.E, tier) * 12D;
        maxRPM = Math.sqrt(maxRCF / (0.001118D * radius));
        double maxSafeMass = EMAtomDefinition.getSomethingHeavy().getMass() * (1 << tier);
        maxForce = maxSafeMass * maxRCF; // (eV/c^2 * m/s) / g
        maxCapacity = maxSafeMass * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED * radius; // eV/c^2
    }

    private double getRCF(double RPM) {
        return RPM * RPM * radius * 0.001118;
    }

    private void addRandomly(EMInstanceStack me, EMInstanceStackMap[] toThis, int fractionCount) {
        double amountPerFraction = div(me.getAmount(), fractionCount);
        EMInstanceStack[] stacks = new EMInstanceStack[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            stacks[i] = me.clone();
            stacks[i].setAmount(amountPerFraction);
            toThis[i].putReplace(stacks[i]);
        }
    }

    @Override
    public void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        Parameters.Group hatch1 = parameters.getGroup(7);
        settingRPM = hatch1.makeInParameter(0, 0, rpmName, rpmStatus);
        settingFraction = hatch1.makeInParameter(1, 2, fractionName, fractionStatus);
    }

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return settingRPM.getStatus(true).isOk && settingFraction.getStatus(true).isOk;
    }

    @Override
    public MultiblockControl<EMInstanceStackMap[]> process(EMInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te,
            Parameters parameters) {
        EMInstanceStackMap input = inputs[0];
        if (input == null || input.isEmpty()) return null; // nothing in only valid input

        EMInstanceStack[] stacks = input.valuesToArray();

        double inputMass = 0;
        for (EMInstanceStack stack : stacks) {
            inputMass += Math.abs(stack.getMass());
        }
        double excessMass = 0;
        while (inputMass > maxCapacity) {
            EMInstanceStack randomStack = stacks[TecTech.RANDOM.nextInt(stacks.length)];
            double amountToRemove = TecTech.RANDOM.nextDouble() / 10D * randomStack.getAmount();
            randomStack.setAmount(sub(randomStack.getAmount(), amountToRemove)); // mutates the parent InstanceStackMap
            if (randomStack.isInvalidAmount()) {
                input.removeKey(randomStack.getDefinition());
                stacks = input.valuesToArray();
            }
            double mass = Math.abs(randomStack.getDefinition().getMass()) * amountToRemove;
            excessMass += mass;
            inputMass -= mass;
        }

        inputMass = Math.abs(input.getMass());

        double RCF = getRCF(settingRPM.get());
        if (inputMass * RCF > maxForce) return new MultiblockControl<>(excessMass); // AND THEN IT EXPLODES

        // how many output hatches to use
        int fractionCount = (int) settingFraction.get();
        EMInstanceStackMap[] outputs = new EMInstanceStackMap[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            outputs[i] = new EMInstanceStackMap();
        }

        // mixing factor...
        double mixingFactor = Math.min(1d - (RCF / maxRCF) * (1d - MIXING_FACTORS[tier - 5][fractionCount - 2]), 1);
        if (DEBUG_MODE) {
            TecTech.LOGGER.info("mixingFactor " + mixingFactor);
        }

        int mEut = (int) (Math.pow(settingRPM.get() / maxRPM, 3D) * V[tier]);
        mEut = Math.max(mEut, 512);
        mEut = -mEut;
        int mTicks = (int) (20 * (inputMass / maxCapacity) * (fractionCount - 1));
        mTicks = Math.max(mTicks, 20);

        // take all from hatch handler and put into new map - this takes from hatch to inner data storage
        stacks = input.takeAll().valuesToArray(); // cleanup stacks
        if (stacks.length > 1) {
            Arrays.sort(stacks, (o1, o2) -> {
                double m1 = o1.getDefinition().getMass();
                double m2 = o2.getDefinition().getMass();
                if (m1 < m2) return -1;
                if (m1 > m2) return 1;
                return o1.compareTo(o2);
            });

            double absMassPerOutput = 0; // "volume"
            for (EMInstanceStack stack : stacks) {
                double tempMass = Math.abs(stack.getMass());
                if (tempMass != 0) {
                    double amount = stack.getAmount();
                    stack.setAmount(mul(stack.getAmount(), mixingFactor));
                    addRandomly(stack, outputs, fractionCount);
                    stack.setAmount(sub(amount, stack.getAmount()));
                    absMassPerOutput += tempMass;
                }
            }
            absMassPerOutput = div(absMassPerOutput, fractionCount);
            if (DEBUG_MODE) {
                TecTech.LOGGER.info("absMassPerOutput " + absMassPerOutput);
            }

            nextFraction: for (int fraction = 0; fraction < fractionCount - 1; fraction++) {
                double remaining = absMassPerOutput;
                for (int stackNo = 0; stackNo < stacks.length; stackNo++) {
                    if (stacks[stackNo] != null) {
                        double stackMass = Math.abs(stacks[stackNo].getMass());
                        double amount = div(remaining, Math.abs(stacks[stackNo].getDefinition().getMass()));

                        if (stackMass == 0) {
                            addRandomly(stacks[stackNo], outputs, fractionCount);
                            stacks[stackNo] = null;
                        } else if (amount >= stacks[stackNo].getAmount()) {
                            remaining = sub(remaining, stackMass);
                            outputs[fraction].putUnify(stacks[stackNo]);
                            stacks[stackNo] = null;
                        } else if (amount > 0) {
                            remaining = sub(remaining, mul(amount, stacks[stackNo].getDefinition().getMass()));
                            EMInstanceStack clone = stacks[stackNo].clone();
                            clone.setAmount(amount);
                            outputs[fraction].putUnify(clone);
                            stacks[stackNo].setAmount(sub(stacks[stackNo].getAmount(), amount));

                        } else {
                            continue nextFraction;
                        }
                    }
                }
            }
            // add remaining
            for (EMInstanceStack stack : stacks) {
                if (stack != null) {
                    outputs[fractionCount - 1].putUnify(stack);
                }
            }
        } else {
            addRandomly(stacks[0], outputs, fractionCount);
        }
        return new MultiblockControl<>(outputs, mEut, 1, 0, 10000, mTicks, 0, excessMass);
    }
}
