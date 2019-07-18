package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.dAtomDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;

import java.util.Arrays;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Centrifuge implements GT_MetaTileEntity_EM_machine.IBehaviour {
    private final byte tier;
    private float radius, maxRPM, maxRCF, maxForce, maxCapacity;
    private Parameters.Group.ParameterIn settingRPM, settingFraction;
    private final static INameFunction<GT_MetaTileEntity_EM_machine> rpmName= (gt_metaTileEntity_em_machine, iParameter) -> "RPM Setting";
    private final IStatusFunction<GT_MetaTileEntity_EM_machine> rpmStatus= (gt_metaTileEntity_em_machine, iParameter) -> {
        double v=iParameter.get();
        if(Double.isNaN(v)){
            return STATUS_WRONG;
        }
        if (v <=0) {
            return STATUS_TOO_LOW;
        }else if (v>maxRPM){
            return STATUS_TOO_HIGH;
        }
        return STATUS_OK;
    };
    private final static INameFunction<GT_MetaTileEntity_EM_machine> fractionName= (gt_metaTileEntity_em_machine, iParameter) -> "Fraction Count";
    private static final IStatusFunction<GT_MetaTileEntity_EM_machine> fractionStatus= (gt_metaTileEntity_em_machine, iParameter) -> {
        double v=iParameter.get();
        if(Double.isNaN(v)){
            return STATUS_WRONG;
        }
        v=(int)v;
        if (v <= 1) {
            return STATUS_TOO_LOW;
        }else if (v>6){
            return STATUS_TOO_HIGH;
        }
        return STATUS_OK;
    };
    //private final static String[] DESCRIPTION_O =new String[]{"RPM Setting","RCF Setting","Radius [mm]","Max RPM","Max Force [eV/c^2 * m/s]","Max Capacity [eV/c^2]","Max Power Usage[EU/t]","Max Recipe Rime [tick]"};

    private static final double[/*tier+5*/][/*outputHatches+2*/] MIXING_FACTORS =new double[][]{
            {.45,.85,.95,1,1,},
            {.4 ,.75,.9,.95,1,},
            {.35,.45,.75,.9,.95,},
            {.25,.3,.45,.75,.9,},
            {.2,.25,.3,.45,.75,},
            {.1,.15,.2,.3,.45,},
            {.05,.1,.15,.2,.25,},
            {.01,.05,.1,.15,.2,},
    };

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
            int amountToAdd = TecTech.RANDOM.nextInt(remainingAmount) + 1;
            stacks[TecTech.RANDOM.nextInt(fractionCount)].amount += amountToAdd;
            remainingAmount -= amountToAdd;
        }
    }

    @Override
    public void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        Parameters.Group hatch1=parameters.getGroup(7);
        settingRPM=hatch1.makeInParameter(0,0,rpmName,rpmStatus);
        settingFraction=hatch1.makeInParameter(1,2,fractionName,fractionStatus);
    }

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return settingRPM.getStatus(true).isOk && settingFraction.getStatus(true).isOk;
    }

    @Override
    public MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        cElementalInstanceStackMap input = inputs[0];
        if (input == null || input.isEmpty()) return null;//nothing in only valid input

        cElementalInstanceStack[] stacks = input.values();

        double inputMass = 0;
        for (cElementalInstanceStack stack : stacks) {
            inputMass += Math.abs(stack.getMass());
        }
        float excessMass = 0;
        while (inputMass > maxCapacity) {
            cElementalInstanceStack randomStack = stacks[TecTech.RANDOM.nextInt(stacks.length)];
            int amountToRemove = TecTech.RANDOM.nextInt((int) randomStack.getAmount()) + 1;
            randomStack.amount -= amountToRemove;//mutates the parent InstanceStackMap
            if (randomStack.amount <= 0) {
                input.remove(randomStack.definition);
            }
            float mass = Math.abs(randomStack.getDefinition().getMass()) * amountToRemove;
            excessMass += mass;
            inputMass -= mass;
        }

        inputMass = Math.abs(input.getMass());

        double RCF = getRCF(settingRPM.get());
        if (inputMass * RCF > maxForce) return new MultiblockControl<>(excessMass);//AND THEN IT EXPLODES

        // how many output hatches to use
        int fractionCount = (int) settingFraction.get();
        cElementalInstanceStackMap[] outputs = new cElementalInstanceStackMap[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            outputs[i] = new cElementalInstanceStackMap();
        }

        //mixing factor...
        double mixingFactor=Math.min(1d-(RCF/maxRCF)*(1d-MIXING_FACTORS[tier-5][fractionCount-2]),1);
        if(DEBUG_MODE){
            TecTech.LOGGER.info("mixingFactor "+mixingFactor);
        }

        int mEut = (int) (Math.pow(settingRPM.get() / maxRPM, 3f) * V[tier]);
        mEut = Math.max(mEut, 512);
        mEut = -mEut;
        int mTicks = (int) (20 * (inputMass / maxCapacity) * (fractionCount - 1));
        mTicks=Math.max(mTicks,20);


        //take all from hatch handler and put into new map - this takes from hatch to inner data storage
        stacks = input.takeAllToNewMap().values();//cleanup stacks
        if (stacks.length > 1) {
            Arrays.sort(stacks, (o1, o2) -> {
                float m1 = o1.definition.getMass();
                float m2 = o2.definition.getMass();
                if (m1 < m2) return -1;
                if (m1 > m2) return 1;
                return o1.compareTo(o2);
            });

            double absMassPerOutput = 0;//"volume"
            for (cElementalInstanceStack stack : stacks) {
                double tempMass=Math.abs(stack.getMass());
                if(tempMass!=0) {
                    long amount = stack.amount;
                    stack.amount *= mixingFactor;
                    addRandomly(stack, outputs, fractionCount);
                    stack.amount = amount - stack.amount;
                    absMassPerOutput += tempMass;
                }
            }
            //if(DEBUG_MODE){
            //    TecTech.LOGGER.info("absMass "+absMassPerOutput);
            //}
            absMassPerOutput /= fractionCount;
            if(DEBUG_MODE){
                TecTech.LOGGER.info("absMassPerOutput "+absMassPerOutput);
            }

            nextFraction:
            for (int fraction = 0; fraction < fractionCount - 1; fraction++) {
                double remaining = absMassPerOutput;
                for (int stackNo = 0; stackNo < stacks.length; stackNo++) {
                    if (stacks[stackNo] != null) {
                        double stackMass = Math.abs(stacks[stackNo].getMass());
                        long amount = (long) (remaining/Math.abs(stacks[stackNo].definition.getMass()));
                        //if(DEBUG_MODE){
                        //    TecTech.LOGGER.info("stackMass "+stackMass);
                        //    TecTech.LOGGER.info("defMass "+stacks[stackNo].definition.getMass());
                        //    TecTech.LOGGER.info("remaining "+remaining);
                        //    TecTech.LOGGER.info("amountToMoveAvailable "+amount+"/"+stacks[stackNo].amount);
                        //}
                        if (stackMass == 0) {
                            addRandomly(stacks[stackNo], outputs, fractionCount);
                            stacks[stackNo] = null;
                        } else if (amount >= stacks[stackNo].amount) {
                            remaining -= stackMass;
                            outputs[fraction].putUnify(stacks[stackNo]);
                            stacks[stackNo] = null;
                        } else if (amount > 0) {
                            remaining -= amount * stacks[stackNo].definition.getMass();
                            cElementalInstanceStack clone = stacks[stackNo].clone();
                            clone.amount = amount;
                            outputs[fraction].putUnify(clone);
                            stacks[stackNo].amount-=amount;
                            //if(DEBUG_MODE){
                            //    TecTech.LOGGER.info("remainingAfter "+remaining);
                            //    TecTech.LOGGER.info("amountCloneAfter "+clone.amount+"/"+stacks[stackNo].amount);
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
                    outputs[fractionCount - 1].putUnify(stack);
                }
            }
        } else {
            addRandomly(stacks[0], outputs, fractionCount);
        }
        return new MultiblockControl<>(outputs, mEut, 1, 0, 10000, mTicks, 0, excessMass);
    }
}
