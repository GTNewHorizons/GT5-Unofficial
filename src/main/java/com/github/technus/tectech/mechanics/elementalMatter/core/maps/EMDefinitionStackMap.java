package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.IEMDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class EMDefinitionStackMap extends EMStackMap<EMDefinitionStack> implements IEMMapWriteExact<EMDefinitionStack> {//Transient class for construction of definitions/recipes
    //Constructors + Clone, all make a whole new OBJ.
    public EMDefinitionStackMap() {}

    public EMDefinitionStackMap(EMDefinitionStack... in) {
        putUnifyAllExact(in);
    }

    public EMDefinitionStackMap(NavigableMap<IEMDefinition, EMDefinitionStack> in) {
        super(in);
    }

    @Override
    public Class<EMDefinitionStack> getType() {
        return EMDefinitionStack.class;
    }

    @Override
    public EMDefinitionStackMap clone() {
        return new EMDefinitionStackMap(new TreeMap<>(getBackingMap()));
    }

    public EMConstantStackMap toImmutable() {
        return new EMConstantStackMap(new TreeMap<>(getBackingMap()));
    }

    public EMConstantStackMap toImmutable_optimized_unsafe_LeavesExposedElementalTree() {
        return new EMConstantStackMap(getBackingMap());
    }

    public static EMDefinitionStackMap fromNBT(NBTTagCompound nbt) throws EMException {
        EMDefinitionStack[] defStacks = new EMDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = EMDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].getDefinition().equals(nbtE__)) {
                throw new EMException("Something went Wrong");
            }
        }
        return new EMDefinitionStackMap(defStacks);
    }
}
