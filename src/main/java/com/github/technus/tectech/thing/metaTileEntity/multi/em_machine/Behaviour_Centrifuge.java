package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;

import java.util.Arrays;
import java.util.Comparator;

import static com.github.technus.tectech.Util.V;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Centrifuge implements GT_MetaTileEntity_EM_machine.Behaviour {
    private static final double MAX_RCF = (float) (Math.pow(Math.E, 15) * 12);
    private final float radius, maxRPM, maxRCF, maxForce, maxCapacity;
    private final byte tier;

    //6 to 12 recommended
    public Behaviour_Centrifuge(int desiredTier) {
        tier = (byte) desiredTier;
        radius = 0.5f - (12 - tier) / 64f;
        maxRCF = (float) (Math.pow(Math.E, tier) * 12);
        maxRPM = (float) Math.sqrt(maxRCF / (0.001118 * radius));
        float maxSafeMass = dAtomDefinition.getSomethingHeavy().getMass() * (1 << tier);
        maxForce = maxSafeMass * maxRCF;// (eV/c^2 * m/s) / g
        maxCapacity = maxSafeMass * 4f * radius;// eV/c^2
    }

    @Override
    public boolean setAndCheckParametersOutAndStatuses(GT_MetaTileEntity_EM_machine te, double[] parametersToCheckAndFix) {
        boolean check=true;

        te.setParameterOut(1, 0, radius * 1000);//in mm
        te.setParameterOut(1, 1, maxRPM);
        te.setParameterOut(2, 0, maxForce * 9.80665);// (eV/c^2 * m/s)
        te.setParameterOut(2, 1, maxCapacity);// eV/c^2

        double RPM = parametersToCheckAndFix[0];
        if (RPM > maxRPM) {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
            te.setParameterOut(0, 0, maxRPM);//rpm
            te.setParameterOut(0, 1, getRCF(maxRPM));//rcf
            check=false;
        } else if (RPM > maxRPM / 3f * 2f) {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_HIGH);
        } else if (RPM > maxRPM / 3f) {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
        } else if (RPM > 0) {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_LOW);
        } else if (RPM <= 0) {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
            te.setParameterOut(0, 0, 0);//rpm
            te.setParameterOut(0, 1, 0);//rcf
            check=false;
        } else {
            te.setStatusOfParameterIn(0, 0, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
            te.setParameterOut(0, 0, 0);//rpm
            te.setParameterOut(0, 1, 0);//rcf
            check=false;
        }

        if(check) {
            te.setParameterOut(0, 0, RPM);
            te.setParameterOut(0, 1, getRCF(RPM));
        }

        double fractionCount = parametersToCheckAndFix[1];
        if (fractionCount > 6) {
            parametersToCheckAndFix[1] = 6;
            te.setStatusOfParameterIn(0, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_HIGH);
            check=false;
        } else if (fractionCount >= 2) {
            te.setStatusOfParameterIn(0, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_OK);
        } else if (fractionCount < 2) {
            parametersToCheckAndFix[1] = 2;
            te.setStatusOfParameterIn(0, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_TOO_LOW);
            check=false;
        } else {
            te.setStatusOfParameterIn(0, 1, GT_MetaTileEntity_MultiblockBase_EM.STATUS_WRONG);
            check=false;
        }

        te.setParameterOut(3,0,(int) (Math.pow(parametersToCheckAndFix[0] / maxRPM, 3f) * V[tier]));//max eut
        te.setParameterOut(3,1,(int) (20 * (MAX_RCF / getRCF(RPM)) * (fractionCount - 1)));//max time

        return check;
    }

    @Override
    public MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, double[] checkedAndFixedParameters) {
        cElementalInstanceStackMap input = inputs[0];
        if (input == null || input.isEmpty()) return null;//nothing in only valid input

        cElementalInstanceStack[] stacks = input.values();

        double inputMass = 0;
        for (cElementalInstanceStack stack : stacks) {
            inputMass += Math.abs(stack.getMass());
        }
        float excessMass = 0;
        while (inputMass > maxCapacity) {
            cElementalInstanceStack randomStack = stacks[TecTech.Rnd.nextInt(stacks.length)];
            int amountToRemove = TecTech.Rnd.nextInt((int) randomStack.getAmount()) + 1;
            randomStack.amount -= amountToRemove;//mutates the parent InstanceStackMap
            if (randomStack.amount <= 0) {
                input.remove(randomStack.definition);
            }
            float mass = Math.abs(randomStack.getDefinition().getMass()) * amountToRemove;
            excessMass += mass;
            inputMass -= mass;
        }

        inputMass = Math.abs(input.getMass());

        double RCF = getRCF(checkedAndFixedParameters[0]);
        if (inputMass * RCF > maxForce) return new MultiblockControl<>(excessMass);//AND THEN IT EXPLODES

        // how many output hatches to use
        int fractionCount = (int) checkedAndFixedParameters[1];
        cElementalInstanceStackMap[] outputs = new cElementalInstanceStackMap[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            outputs[i] = new cElementalInstanceStackMap();
        }

        int mEut = (int) (Math.pow(checkedAndFixedParameters[0] / maxRPM, 3f) * V[tier]);
        mEut = Math.max(mEut, 512);
        mEut = -mEut;
        int mTicks = (int) (20 * (MAX_RCF / RCF) * (inputMass / maxCapacity) * (fractionCount - 1));
        mTicks=Math.max(mTicks,20);


        //take all from hatch handler and put into new map - this takes from hatch to inner data storage
        stacks = input.takeAllToNewMap().values();//cleanup stacks
        if (stacks.length > 1) {
            Arrays.sort(stacks, new Comparator<cElementalInstanceStack>() {
                @Override
                public int compare(cElementalInstanceStack o1, cElementalInstanceStack o2) {
                    float m1 = o1.definition.getMass();
                    float m2 = o2.definition.getMass();
                    if (m1 < m2) return -1;
                    if (m1 > m2) return 1;
                    return o1.compareTo(o2);
                }
            });

            //output sorting bleeding
            float bleedingMod=0.5f-((float)(RCF/maxRCF)*(tier/25f));


            double absMassPerOutput = 0;//"volume"
            for (cElementalInstanceStack stack : stacks) {
                absMassPerOutput += Math.abs(stack.getMass());
            }
            if(DEBUG_MODE){
                TecTech.Logger.info("absMass "+absMassPerOutput);
            }

            absMassPerOutput /= fractionCount;
            if(DEBUG_MODE){
                TecTech.Logger.info("absMassPerOutput "+absMassPerOutput);
            }

            nextFraction:
            for (int fraction = 0; fraction < fractionCount - 1; fraction++) {
                double remaining = absMassPerOutput;
                for (int stackNo = 0; stackNo < stacks.length; stackNo++) {
                    if (stacks[stackNo] != null) {
                        double stackMass = Math.abs(stacks[stackNo].getMass());
                        long amount = (long) (remaining/Math.abs(stacks[stackNo].definition.getMass()));
                        //if(DEBUG_MODE){
                        //    TecTech.Logger.info("stackMass "+stackMass);
                        //    TecTech.Logger.info("defMass "+stacks[stackNo].definition.getMass());
                        //    TecTech.Logger.info("remaining "+remaining);
                        //    TecTech.Logger.info("amountToMoveAvailable "+amount+"/"+stacks[stackNo].amount);
                        //}
                        if (stackMass == 0) {
                            addRandomly(stacks[stackNo], outputs, fractionCount);
                            stacks[stackNo] = null;
                        } else if (amount >= stacks[stackNo].amount) {
                            remaining -= stackMass;
                            outputs[fraction].putReplace(stacks[stackNo]);
                            stacks[stackNo] = null;
                        } else if (amount > 0) {
                            remaining -= amount * stacks[stackNo].definition.getMass();
                            cElementalInstanceStack clone = stacks[stackNo].clone();
                            clone.amount = amount;
                            outputs[fraction].putReplace(clone);
                            stacks[stackNo].amount-=amount;
                            //if(DEBUG_MODE){
                            //    TecTech.Logger.info("remainingAfter "+remaining);
                            //    TecTech.Logger.info("amountCloneAfter "+clone.amount+"/"+stacks[stackNo].amount);
                            //}
                        } else {
                            continue nextFraction;
                        }
                    }
                }
            }
            //add remaining
            for (cElementalInstanceStack stack : stacks) {
                if (stack != null) {
                    outputs[fractionCount - 1].putReplace(stack);
                }
            }
        } else {
            addRandomly(stacks[0], outputs, fractionCount);
        }
        return new MultiblockControl<>(outputs, mEut, 1, 0, 10000, mTicks, 0, excessMass);
    }

    private double getRCF(double RPM) {
        return RPM * RPM * radius * 0.001118;
    }

    private void addRandomly(cElementalInstanceStack me, cElementalInstanceStackMap[] toThis, int fractionCount) {
        long amountPerFraction = me.amount / fractionCount;
        cElementalInstanceStack[] stacks = new cElementalInstanceStack[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            stacks[i] = me.clone();
            stacks[i].amount = amountPerFraction;
            toThis[i].putReplace(stacks[i]);
        }
        int remainingAmount = (int) (me.amount % fractionCount);
        while (remainingAmount > 0) {
            int amountToAdd = TecTech.Rnd.nextInt(remainingAmount) + 1;
            stacks[TecTech.Rnd.nextInt(fractionCount)].amount += amountToAdd;
            remainingAmount -= amountToAdd;
        }
    }
}
