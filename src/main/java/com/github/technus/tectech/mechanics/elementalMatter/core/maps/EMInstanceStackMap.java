package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.util.DoubleCount.add;
import static com.github.technus.tectech.util.TT_Utility.unpackNBT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecayResult;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class EMInstanceStackMap extends EMStackMap<EMInstanceStack> implements IEMMapWrite<EMInstanceStack> {

    // Constructors
    public EMInstanceStackMap() {}

    public EMInstanceStackMap(EMInstanceStack... inSafe) {
        this(true, inSafe);
    }

    public EMInstanceStackMap(boolean clone, EMInstanceStack... in) {
        if (clone) {
            EMInstanceStack[] stacks = new EMInstanceStack[in.length];
            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = in[i].clone();
            }
            putUnifyAll(stacks);
        } else {
            putUnifyAll(in);
        }
    }

    private EMInstanceStackMap(NavigableMap<IEMDefinition, EMInstanceStack> inSafe) {
        this(true, inSafe);
    }

    private EMInstanceStackMap(boolean clone, NavigableMap<IEMDefinition, EMInstanceStack> in) {
        super(clone ? new TreeMap<>() : in);
        if (clone) {
            for (EMInstanceStack stack : in.values()) {
                putUnify(stack.clone());
            }
        }
    }

    @Override
    public Class<EMInstanceStack> getType() {
        return EMInstanceStack.class;
    }

    @Override
    public EMInstanceStackMap clone() {
        return new EMInstanceStackMap(getBackingMap());
    }

    // Remove overflow
    public double removeOverflow(int stacksCount, double stackCapacity) {
        double massRemoved = 0;

        if (size() > stacksCount) {
            IEMDefinition[] keys = keySetToArray();
            for (int i = stacksCount; i < keys.length; i++) {
                massRemoved += get(keys[i]).getDefinitionStack().getMass();
                removeKey(keys[i]);
            }
        }

        for (EMInstanceStack instance : valuesToArray()) {
            if (instance.getAmount() > stackCapacity) {
                massRemoved += instance.getDefinition().getMass() * (instance.getAmount() - stackCapacity);
                instance.setAmount(stackCapacity);
            }
        }
        return massRemoved;
    }

    // Getters
    public String[] getElementalInfo() {
        String[] info = new String[size()];
        int i = 0;
        for (Map.Entry<IEMDefinition, EMInstanceStack> entry : entrySet()) {
            EMInstanceStack instance = entry.getValue();
            info[i++] = EnumChatFormatting.BLUE + instance.getDefinition().getLocalizedName()
                    + " "
                    + EnumChatFormatting.AQUA
                    + instance.getDefinition().getSymbol()
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.amount")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + TT_Utility.formatNumberExp(instance.getAmount() / AVOGADRO_CONSTANT)
                    + " "
                    + translateToLocal("tt.keyword.unit.mol")
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.energy")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + GT_Utility
                            .formatNumbers(instance.getDefinition().getEnergyDiffBetweenStates(0, instance.getEnergy()))
                    + " "
                    + translateToLocal("tt.keyword.unit.energy")
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.charge")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + TT_Utility.formatNumberExp(instance.getCharge())
                    + " "
                    + translateToLocal("tt.keyword.unit.charge")
                    + EnumChatFormatting.RESET
                    + " "
                    + translateToLocal("tt.keyword.short.time")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + (instance.getLifeTime() < 0 ? translateToLocal("tt.keyword.stable")
                            : TT_Utility.formatNumberShortExp(instance.getLifeTime()) + " "
                                    + translateToLocal("tt.keyword.unit.time"))
                    + EnumChatFormatting.RESET;
        }
        return info;
    }

    public ArrayList<String> getScanInfo(int[] capabilities) {
        ArrayList<String> list = new ArrayList<>(16);
        for (Map.Entry<IEMDefinition, EMInstanceStack> e : entrySet()) {
            e.getValue().addScanResults(list, capabilities);
        }
        return list;
    }

    public double tickContent(double lifeTimeMult, int postEnergize, double seconds) {
        // cleanUp();
        double diff = 0;
        for (EMInstanceStack instance : takeAllToArray()) {
            instance.setAge(instance.getAge() + seconds);
            EMDecayResult newInstances = instance.decay(lifeTimeMult, instance.getAge(), postEnergize);
            if (newInstances == null) {
                putUnify(instance);
            } else {
                diff = add(diff, newInstances.getMassDiff());
                putUnifyAll(newInstances.getOutput());
            }
        }
        return diff;
    }

    // NBT
    public static EMInstanceStackMap fromNBT(EMDefinitionsRegistry registry, NBTTagCompound nbt) throws EMException {
        return new EMInstanceStackMap(
                false,
                unpackNBT(EMInstanceStack.class, inner -> EMInstanceStack.fromNBT(registry, inner), nbt));
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder("Instance Stack Map\n");
        for (Map.Entry<IEMDefinition, EMInstanceStack> stack : entrySet()) {
            build.append(stack.getValue().toString()).append('\n');
        }
        return build.toString();
    }

    public EMInstanceStack[] takeAllToArray() {
        EMInstanceStack[] newStack = valuesToArray(); // just in case to uncouple The map
        clear();
        return newStack;
    }

    public EMInstanceStackMap takeAll() {
        EMInstanceStackMap newStack = new EMInstanceStackMap(false, new TreeMap<>(getBackingMap())); // just in case to
                                                                                                     // uncouple The map
        clear();
        return newStack;
    }

    @Deprecated
    public EMDefinitionStackMap toDefinitionMapForComparison() {
        EMDefinitionStack[] list = new EMDefinitionStack[size()];
        int i = 0;
        for (Map.Entry<IEMDefinition, EMInstanceStack> entry : entrySet()) {
            EMInstanceStack value = entry.getValue();
            list[i++] = new EMDefinitionStack(value.getDefinition(), value.getAmount());
        }
        return new EMDefinitionStackMap(list);
    }

    @Override
    public EMInstanceStack putUnify(EMInstanceStack stack) {
        EMInstanceStack target = get(stack.getDefinition());
        if (target == null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = add(target.getAmount(), stack.getAmount());
        if (IEMStack.isValidAmount(newAmount)) {
            stack = target.unifyIntoThis(stack);
            putReplace(stack);
            return stack;
        } else {
            removeKey(stack.getDefinition());
            return null;
        }
    }

    @Override
    public EMInstanceStack putUnifyExact(EMInstanceStack stack) {
        EMInstanceStack target = get(stack.getDefinition());
        if (target == null) {
            putReplace(stack);
            return stack;
        }
        double newAmount = target.getAmount() + stack.getAmount();
        if (IEMStack.isValidAmount(newAmount)) {
            stack = target.unifyIntoThis(stack);
            putReplace(stack);
            return stack;
        } else {
            removeKey(stack.getDefinition());
            return null;
        }
    }
}
