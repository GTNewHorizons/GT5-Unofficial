package com.github.technus.tectech.mechanics.elementalMatter.core.decay;

import static com.github.technus.tectech.util.DoubleCount.mul;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class EMDecay {

    public static final EMDecay[] NO_DECAY = null;
    // DECAY IMPOSSIBLE!!!
    // Do not use regular NULL java will not make it work with varargs!!!
    // Or cast null into ARRAY type but this static is more convenient!!!
    public static final EMDecay[] NO_PRODUCT = new EMDecay[0];
    // this in turn can be used to tell that the thing should just vanish
    private final EMConstantStackMap outputStacks;
    private final double probability;

    public EMDecay(IEMDefinition... outSafe) {
        this(1D, outSafe);
    }

    public EMDecay(double probability, IEMDefinition... outSafe) {
        EMDefinitionStack[] outArr = new EMDefinitionStack[outSafe.length];
        for (int i = 0; i < outArr.length; i++) {
            outArr[i] = new EMDefinitionStack(outSafe[i], 1D);
        }
        outputStacks = new EMConstantStackMap(outArr);
        this.probability = probability;
    }

    public EMDecay(EMDefinitionStack... outSafe) {
        this(1D, outSafe);
    }

    public EMDecay(double probability, EMDefinitionStack... out) {
        outputStacks = new EMConstantStackMap(out);
        this.probability = probability;
    }

    public EMDecay(EMConstantStackMap tree) {
        this(1D, tree);
    }

    public EMDecay(double probability, EMConstantStackMap tree) {
        outputStacks = tree;
        this.probability = probability;
    }

    public EMInstanceStackMap getResults(double lifeMult, double age, long newEnergyLevel, double amountDecaying) {
        EMInstanceStackMap decayResult = new EMInstanceStackMap();
        if (getOutputStacks() == null) {
            return decayResult; // This is to prevent null pointer exceptions.
        }
        // Deny decay code is in instance!
        boolean empty = true;
        for (EMDefinitionStack stack : getOutputStacks().valuesToArray()) {
            if (stack.getAmount() > 0) {
                empty = false;
                break;
            }
        }
        if (empty) {
            return decayResult;
        }

        for (EMDefinitionStack stack : getOutputStacks().valuesToArray()) {
            decayResult.putUnify(
                    new EMInstanceStack(
                            stack.getDefinition(),
                            mul(amountDecaying, stack.getAmount()),
                            lifeMult,
                            age /* new products */,
                            (long) (newEnergyLevel / Math.max(1D, Math.abs(stack.getAmount()))))); // get instances from
                                                                                                   // stack
        }
        return decayResult;
    }

    public EMConstantStackMap getOutputStacks() {
        return outputStacks;
    }

    public double getProbability() {
        return probability;
    }
}
