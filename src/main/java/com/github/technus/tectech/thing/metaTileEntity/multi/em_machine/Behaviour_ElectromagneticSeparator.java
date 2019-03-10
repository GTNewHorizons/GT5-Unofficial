package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;

import java.util.ArrayList;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_ElectromagneticSeparator implements GT_MetaTileEntity_EM_machine.Behaviour {
    private final byte tier;
    private final int ticks;
    private final byte precisionFull;
    private final byte precisionMinimal;
    private final float maxCapacity;
    private final long maxCharge;
    private final int offsetMax;
    private final static String[] DESCRIPTION_I =new String[]{"Full Precision Input [e/3]","Minimal Precision Input [e/3]","Offset Input [e/3]",null};
    private final static String[] DESCRIPTION_O =new String[]{"Full Precision Limit [e/3]","Minimal Precision Limit [e/3]","Offset Limit [e/3]",null,"Max Charge [e/3]","Max Capacity [eV/c^2]","Max Power Usage[EU/t]","Max Recipe Rime [tick]"};

    public Behaviour_ElectromagneticSeparator(int desiredTier){
        tier=(byte) desiredTier;
        ticks =Math.max(20,(1<<(12-desiredTier))*20);
        maxCapacity= dAtomDefinition.getSomethingHeavy().getMass()*(2<<tier);
        maxCharge=144*(1<<(tier-5));
        switch (tier){
            case 12:
                precisionFull=1;
                precisionMinimal =1;
                break;
            case 11:
                precisionFull=2;
                precisionMinimal =1;
                break;
            case 10:
                precisionFull=3;
                precisionMinimal =1;
                break;
            case 9:
                precisionFull=3;
                precisionMinimal =2;
                break;
            case 8:
                precisionFull=3;
                precisionMinimal =3;
                break;
            case 7:
                precisionFull=6;
                precisionMinimal =3;
                break;
            case 6:
                precisionFull=12;
                precisionMinimal =3;
                break;
            case 5:
                precisionFull=24;
                precisionMinimal =6;
                break;
            default: precisionFull= precisionMinimal =Byte.MAX_VALUE;
        }
        offsetMax=1<<((tier-8)<<1);
    }

    @Override
    protected void getFullLedDescriptionIn(ArrayList<String> baseDescr, int hatchNo, int paramID) {
        if(hatchNo<=1) {
            String desc=DESCRIPTION_I[(hatchNo << 1) + paramID];
            if(desc!=null){
                baseDescr.add(desc);
            }
        }
    }

    @Override
    protected void getFullLedDescriptionOut(ArrayList<String> baseDescr, int hatchNo, int paramID) {
        if(hatchNo<=3){
            String desc=DESCRIPTION_O[(hatchNo<<1)+paramID];
            if(desc!=null){
                baseDescr.add(desc);
            }
        }
    }

    @Override
    public boolean setAndCheckParametersOutAndStatuses(GT_MetaTileEntity_EM_machine te, double[] parametersToCheckAndFix) {
        boolean check=true;

        te.setParameterOut(0,0,precisionFull);
        te.setParameterOut(0,1,precisionMinimal);
        te.setParameterOut(1,0,offsetMax);
        te.setStatusOfParameterOut(1,1,STATUS_UNUSED);
        te.setParameterOut(2,0,maxCharge);
        te.setParameterOut(2,1,maxCapacity);
        te.setParameterOut(3,0,V[tier]);
        te.setParameterOut(3,1,ticks);

        double full=parametersToCheckAndFix[0];
        if(Double.isInfinite(full) && full>0) {
            te.setStatusOfParameterIn(0,0,STATUS_TOO_HIGH);
            check=false;
        }else if(full>precisionFull){
            te.setStatusOfParameterIn(0,0, STATUS_HIGH);
        }else if(full==precisionFull){
            te.setStatusOfParameterIn(0,0,STATUS_OK);
        }else if(full<precisionFull){
            te.setStatusOfParameterIn(0,0,STATUS_TOO_LOW);
            check=false;
        }else {
            te.setStatusOfParameterIn(0,0,STATUS_WRONG);
            check=false;
        }

        double minimal=parametersToCheckAndFix[1];
        if(Double.isInfinite(minimal) && minimal>0) {
            te.setStatusOfParameterIn(0,1,STATUS_TOO_HIGH);
            check=false;
        }else if(minimal>precisionMinimal){
            if(minimal>full){
                te.setStatusOfParameterIn(0,1,STATUS_TOO_HIGH);
                check=false;
            }else {
                te.setStatusOfParameterIn(0,1, STATUS_HIGH);
            }
        }else if(minimal==precisionMinimal){
            if(minimal>full){
                te.setStatusOfParameterIn(0,1,STATUS_TOO_HIGH);
                check=false;
            }else {
                te.setStatusOfParameterIn(0,1, STATUS_OK);
            }
        }else if(minimal<precisionMinimal){
            te.setStatusOfParameterIn(0,1,STATUS_TOO_LOW);
            check=false;
        }else {
            te.setStatusOfParameterIn(0,1,STATUS_WRONG);
            check=false;
        }

        double offset=parametersToCheckAndFix[2];
        if(offset>offsetMax){
            te.setStatusOfParameterIn(1,0,STATUS_TOO_HIGH);
            check=false;
        }else if(offset>0){
            te.setStatusOfParameterIn(1,0,STATUS_HIGH);
        }else if(offset==0){
            te.setStatusOfParameterIn(1,0,STATUS_OK);
        }else if(offset>=-offsetMax){
            te.setStatusOfParameterIn(1,0,STATUS_LOW);
        }else if(offset<-offsetMax){
            te.setStatusOfParameterIn(1,0,STATUS_TOO_LOW);
            check=false;
        }else {
            te.setStatusOfParameterIn(1,0,STATUS_WRONG);
            check=false;
        }

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

        long totalCharge=Math.abs(input.getCharge());
        if (totalCharge>maxCharge) return new MultiblockControl<>(excessMass);//AND THEN IT EXPLODES

        int mEut=(int)(((double)totalCharge/(double) maxCharge)*V[tier]);
        mEut = Math.max(mEut, 512);
        int mTicks=(int)(ticks*(inputMass/maxCapacity));
        mTicks=Math.max(mTicks,20);

        cElementalInstanceStackMap[] outputs = new cElementalInstanceStackMap[3];
        for (int i = 0; i < 3; i++) {
            outputs[i] = new cElementalInstanceStackMap();
        }

        double offsetIn=checkedAndFixedParameters[2];
        double precisionFullIn=checkedAndFixedParameters[0];
        double precisionMinimalIn=checkedAndFixedParameters[1];
        double levelsCountPlus1=precisionFullIn-precisionMinimalIn+1;

        //take all from hatch handler and put into new map - this takes from hatch to inner data storage
        stacks = input.takeAllToNewMap().values();//cleanup stacks
        for(cElementalInstanceStack stack:stacks){
            double charge=stack.definition.getCharge()-offsetIn;
            if(charge<precisionMinimalIn && charge>-precisionMinimalIn){
                outputs[1].putReplace(stack);
            }else if(charge>=precisionFullIn){
                outputs[2].putReplace(stack);
            }else if(charge<=-precisionFullIn){
                outputs[0].putReplace(stack);
            }else{
                long amount=(long)(stack.amount*((Math.abs(charge)-precisionMinimalIn+1)/levelsCountPlus1));//todo check
                if(amount>=stack.amount){
                    if(charge>0){
                        outputs[2].putReplace(stack);
                    }else {
                        outputs[0].putReplace(stack);
                    }
                }else {
                    cElementalInstanceStack clone=stack.clone();
                    clone.amount-=amount;
                    outputs[1].putReplace(clone);

                    stack.amount=amount;
                    if(charge>0){
                        outputs[2].putReplace(stack);
                    }else {
                        outputs[0].putReplace(stack);
                    }
                }
            }
        }

        return new MultiblockControl<>(outputs, mEut, 1+((int)Math.abs(offsetIn))/3, 0, 10000, mTicks, 0, excessMass);
    }
}
