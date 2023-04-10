package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.DoubleCount.mul;
import static com.github.technus.tectech.util.DoubleCount.sub;

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
public class Behaviour_ElectromagneticSeparator implements GT_MetaTileEntity_EM_machine.IBehaviour {

    private final byte tier;
    private int ticks;
    private byte precisionFull, precisionMinimal;
    private double maxCapacity;
    private double maxCharge;
    private int offsetMax;
    private Parameters.Group.ParameterIn fullSetting, minimalSetting, offsetSetting;
    private static final INameFunction<GT_MetaTileEntity_EM_machine> fullName = (gt_metaTileEntity_em_machine,
            iParameter) -> "Full Precision Input [e/3]";
    private final IStatusFunction<GT_MetaTileEntity_EM_machine> fullStatus = (gt_metaTileEntity_em_machine,
            iParameter) -> {
        double v = iParameter.get();
        if (Double.isNaN(v)) {
            return STATUS_WRONG;
        }
        v = (int) v;
        if (Double.isInfinite(v) && v > 0) {
            return STATUS_TOO_HIGH;
        } else if (v > precisionFull) {
            return STATUS_HIGH;
        } else if (v < precisionFull) {
            return STATUS_TOO_LOW;
        }
        return STATUS_OK;
    };
    private static final INameFunction<GT_MetaTileEntity_EM_machine> minimalName = (gt_metaTileEntity_em_machine,
            iParameter) -> "Minimal Precision Input [e/3]";
    private final IStatusFunction<GT_MetaTileEntity_EM_machine> minimalStatus = (gt_metaTileEntity_em_machine,
            iParameter) -> {
        double minimal = iParameter.get();
        double full = fullSetting.get();
        if (Double.isInfinite(minimal) && minimal > 0) {
            return STATUS_TOO_HIGH;
        } else if (minimal > precisionMinimal) {
            if (minimal > full) {
                return STATUS_TOO_HIGH;
            } else {
                return STATUS_HIGH;
            }
        } else if (minimal == precisionMinimal) {
            if (minimal > full) {
                return STATUS_TOO_HIGH;
            } else {
                return STATUS_OK;
            }
        } else if (minimal < precisionMinimal) {
            return STATUS_TOO_LOW;
        } else {
            return STATUS_WRONG;
        }
    };
    private static final INameFunction<GT_MetaTileEntity_EM_machine> offsetName = (gt_metaTileEntity_em_machine,
            iParameter) -> "Offset Input [e/3]";
    private final IStatusFunction<GT_MetaTileEntity_EM_machine> offsetStatus = (gt_metaTileEntity_em_machine,
            iParameter) -> {
        double offset = iParameter.get();
        if (offset > offsetMax) {
            return STATUS_TOO_HIGH;
        } else if (offset > 0) {
            return STATUS_HIGH;
        } else if (offset == 0) {
            return STATUS_OK;
        } else if (offset >= -offsetMax) {
            return STATUS_LOW;
        } else if (offset < -offsetMax) {
            return STATUS_TOO_LOW;
        } else {
            return STATUS_WRONG;
        }
    };
    // private final static String[] DESCRIPTION_O =new String[]{"Full Precision Limit [e/3]","Minimal Precision Limit
    // [e/3]","Offset Limit [e/3]",null,"Max Charge [e/3]","Max Capacity [eV/c^2]","Max Power Usage[EU/t]","Max Recipe
    // Rime [tick]"};

    public Behaviour_ElectromagneticSeparator(int desiredTier) {
        tier = (byte) desiredTier;
        ticks = Math.max(20, (1 << (12 - desiredTier)) * 20);
        maxCapacity = EMAtomDefinition.getSomethingHeavy().getMass() * (2 << tier)
                * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
        maxCharge = 144D * (1 << (tier - 5));
        switch (tier) {
            case 12:
                precisionFull = 1;
                precisionMinimal = 1;
                break;
            case 11:
                precisionFull = 2;
                precisionMinimal = 1;
                break;
            case 10:
                precisionFull = 3;
                precisionMinimal = 1;
                break;
            case 9:
                precisionFull = 3;
                precisionMinimal = 2;
                break;
            case 8:
                precisionFull = 3;
                precisionMinimal = 3;
                break;
            case 7:
                precisionFull = 6;
                precisionMinimal = 3;
                break;
            case 6:
                precisionFull = 12;
                precisionMinimal = 3;
                break;
            case 5:
                precisionFull = 24;
                precisionMinimal = 6;
                break;
            default:
                precisionFull = precisionMinimal = Byte.MAX_VALUE;
        }
        offsetMax = 1 << ((tier - 8) << 1);
    }

    @Override
    public void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        Parameters.Group hatch1 = parameters.getGroup(7);
        fullSetting = hatch1.makeInParameter(0, 0, fullName, fullStatus);
        minimalSetting = hatch1.makeInParameter(1, 2, minimalName, minimalStatus);
        Parameters.Group hatch2 = parameters.getGroup(8);
        offsetSetting = hatch2.makeInParameter(0, 0, offsetName, offsetStatus);
    }

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return fullSetting.getStatus(true).isOk && minimalSetting.getStatus(true).isOk
                && offsetSetting.getStatus(true).isOk;
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
        float excessMass = 0;
        while (inputMass > maxCapacity) {
            EMInstanceStack randomStack = stacks[TecTech.RANDOM.nextInt(stacks.length)];
            double amountToRemove = TecTech.RANDOM.nextDouble() / 10D * randomStack.getAmount();
            randomStack.setAmount(sub(randomStack.getAmount(), amountToRemove)); // mutates the parent InstanceStackMap
            if (randomStack.isInvalidAmount()) {
                input.removeKey(randomStack.getDefinition());
            }
            double mass = Math.abs(randomStack.getDefinition().getMass()) * amountToRemove;
            excessMass += mass;
            inputMass -= mass;
        }

        double totalCharge = Math.abs(input.getCharge());
        if (totalCharge > maxCharge) return new MultiblockControl<>(excessMass); // AND THEN IT EXPLODES

        int mEut = (int) ((totalCharge / maxCharge) * V[tier]);
        mEut = Math.max(mEut, 512);
        int mTicks = (int) (ticks * (inputMass / maxCapacity));
        mTicks = Math.max(mTicks, 20);

        EMInstanceStackMap[] outputs = new EMInstanceStackMap[3];
        for (int i = 0; i < 3; i++) {
            outputs[i] = new EMInstanceStackMap();
        }

        double offsetIn = offsetSetting.get();
        double precisionFullIn = fullSetting.get();
        double precisionMinimalIn = minimalSetting.get();
        double levelsCountPlus1 = precisionFullIn - precisionMinimalIn + 1;

        // take all from hatch handler and put into new map - this takes from hatch to inner data storage
        stacks = input.takeAll().valuesToArray(); // cleanup stacks
        for (EMInstanceStack stack : stacks) {
            double charge = stack.getDefinition().getCharge() - offsetIn;
            if (charge < precisionMinimalIn && charge > -precisionMinimalIn) {
                outputs[1].putReplace(stack);
            } else if (charge >= precisionFullIn) {
                outputs[2].putReplace(stack);
            } else if (charge <= -precisionFullIn) {
                outputs[0].putReplace(stack);
            } else {
                double amount = mul(stack.getAmount(), (Math.abs(charge) - precisionMinimalIn + 1D) / levelsCountPlus1); // todo
                                                                                                                         // check
                if (amount < stack.getAmount()) {
                    EMInstanceStack clone = stack.clone();
                    clone.setAmount(sub(clone.getAmount(), amount));
                    outputs[1].putReplace(clone);

                    stack.setAmount(amount);
                }
                if (charge > 0) {
                    outputs[2].putReplace(stack);
                } else {
                    outputs[0].putReplace(stack);
                }
            }
        }

        return new MultiblockControl<>(
                outputs,
                mEut,
                1 + ((int) Math.abs(offsetIn)) / 3,
                0,
                10000,
                mTicks,
                0,
                excessMass);
    }
}
