package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Centrifuge implements GT_MetaTileEntity_EM_machine.Behaviour {
    private final byte tier;
    private float radius, maxRPM, maxRCF, maxForce, maxCapacity;
    private Parameters.Group.ParameterIn settingRPM, settingFraction;
    private final static NameFunction<GT_MetaTileEntity_EM_machine> rpmName= (gt_metaTileEntity_em_machine, iParameter) -> "RPM Setting";
    private static final StatusFunction<GT_MetaTileEntity_EM_machine> rpmStatus= (gt_metaTileEntity_em_machine, iParameter) -> {
        double v=iParameter.get();
        if(Double.isNaN(v)){
            return STATUS_WRONG;
        }

    };
    private final static NameFunction<GT_MetaTileEntity_EM_machine> fractionName= (gt_metaTileEntity_em_machine, iParameter) -> "Fraction Count";
    private static final StatusFunction<GT_MetaTileEntity_EM_machine> fractionStatus= (gt_metaTileEntity_em_machine, iParameter) -> {

    };
    private final static String[] DESCRIPTION_O =new String[]{"RPM Setting","RCF Setting","Radius [mm]","Max RPM","Max Force [eV/c^2 * m/s]","Max Capacity [eV/c^2]","Max Power Usage[EU/t]","Max Recipe Rime [tick]"};

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

    @Override
    protected void getFullLedDescriptionOut(ArrayList<String> baseDescr, int hatchNo, int paramID) {
        if(hatchNo<=2) {
            baseDescr.add(DESCRIPTION_O[(hatchNo<<1)+paramID]);
        }
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
        settingRPM=hatch1.makeInParameter(0,0,,);
        settingFraction=hatch1.makeInParameter(1,2,,);
    }

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        boolean check=true;

        double RPM = settingRPM.get();
        if (RPM > maxRPM) {
            te.setStatusOfParameterIn(0, 0, STATUS_TOO_HIGH);
            te.setParameterOut(0, 0, maxRPM);//rpm
            te.setParameterOut(0, 1, maxRCF);//rcf
            check=false;
        } else if (RPM > maxRPM / 3f * 2f) {
            te.setStatusOfParameterIn(0, 0, STATUS_HIGH);
        } else if (RPM > maxRPM / 3f) {
            te.setStatusOfParameterIn(0, 0, STATUS_OK);
        } else if (RPM > 0) {
            te.setStatusOfParameterIn(0, 0, STATUS_LOW);
        } else if (RPM <= 0) {
            te.setStatusOfParameterIn(0, 0, STATUS_TOO_LOW);
            te.setParameterOut(0, 0, 0);//rpm
            te.setParameterOut(0, 1, 0);//rcf
            check=false;
        } else {
            te.setStatusOfParameterIn(0, 0, STATUS_WRONG);
            te.setParameterOut(0, 0, 0);//rpm
            te.setParameterOut(0, 1, 0);//rcf
            check=false;
        }

        if(check) {
            te.setParameterOut(0, 0, RPM);
            te.setParameterOut(0, 1, getRCF(RPM));
        }

        double fractionCount = settingFraction.get();
        if (fractionCount > 6) {
            parametersToCheckAndFix[1] = 6;
            te.setStatusOfParameterIn(0, 1, STATUS_TOO_HIGH);
            check=false;
        } else if (fractionCount >= 2) {
            te.setStatusOfParameterIn(0, 1, STATUS_OK);
        } else if (fractionCount < 2) {
            parametersToCheckAndFix[1] = 2;
            te.setStatusOfParameterIn(0, 1, STATUS_TOO_LOW);
            check=false;
        } else {
            te.setStatusOfParameterIn(0, 1, STATUS_WRONG);
            check=false;
        }

        te.setParameterOut(3,0,(int) (Math.pow(parametersToCheckAndFix[0] / maxRPM, 3f) * V[tier]));//max eut
        te.setParameterOut(3,1,(int) (20 * (fractionCount - 1)));//max time

        return check;
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

        double RCF = getRCF(checkedAndFixedParameters[0]);
        if (inputMass * RCF > maxForce) return new MultiblockControl<>(excessMass);//AND THEN IT EXPLODES

        // how many output hatches to use
        int fractionCount = (int) checkedAndFixedParameters[1];
        cElementalInstanceStackMap[] outputs = new cElementalInstanceStackMap[fractionCount];
        for (int i = 0; i < fractionCount; i++) {
            outputs[i] = new cElementalInstanceStackMap();
        }

        //mixing factor...
        double mixingFactor=Math.min(1d-(RCF/maxRCF)*(1d-MIXING_FACTORS[tier-5][fractionCount-2]),1);
        if(DEBUG_MODE){
            TecTech.LOGGER.info("mixingFactor "+mixingFactor);
        }

        int mEut = (int) (Math.pow(checkedAndFixedParameters[0] / maxRPM, 3f) * V[tier]);
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
