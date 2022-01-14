package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class cElementalDefinitionStackMap extends cElementalStackMap<cElementalDefinitionStack> implements iElementalMapRW<cElementalDefinitionStack> {//Transient class for construction of definitions/recipes
    //Constructors + Clone, all make a whole new OBJ.
    public cElementalDefinitionStackMap() {}

    public cElementalDefinitionStackMap(cElementalDefinitionStack... in) {
        putUnifyAll(in);
    }

    public cElementalDefinitionStackMap(NavigableMap<iElementalDefinition, cElementalDefinitionStack> in) {
        super(in);
    }

    @Override
    public Class<cElementalDefinitionStack> getType() {
        return cElementalDefinitionStack.class;
    }

    @Override
    public cElementalDefinitionStackMap clone() {
        return new cElementalDefinitionStackMap(new TreeMap<>(map));
    }

    public cElementalConstantStackMap toImmutable() {
        return new cElementalConstantStackMap(new TreeMap<>(map));
    }

    public cElementalConstantStackMap toImmutable_optimized_unsafe_LeavesExposedElementalTree() {
        return new cElementalConstantStackMap(map);
    }

    public static cElementalDefinitionStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(nbtE__)) {
                throw new tElementalException("Something went Wrong");
            }
        }
        return new cElementalDefinitionStackMap(defStacks);
    }
}
