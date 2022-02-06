package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;

/**
 * Created by Tec on 12.05.2017.
 */
public final class EMConstantStackMap/*IMMUTABLE*/ extends EMStackMap<EMDefinitionStack> {//Target class for construction of definitions/recipes
    //Constructors + Clone, all make a whole new OBJ.
    public static final EMConstantStackMap EMPTY = new EMConstantStackMap();

    private EMConstantStackMap() {
        super(Collections.emptyNavigableMap());
    }

    public EMConstantStackMap(EMDefinitionStack... in) {
        this(new EMDefinitionStackMap(in).getBackingMap());
    }

    public EMConstantStackMap(NavigableMap<IEMDefinition, EMDefinitionStack> in) {
        super(Collections.unmodifiableNavigableMap(in));
    }

    @Override
    public Class<EMDefinitionStack> getType() {
        return EMDefinitionStack.class;
    }

    //IMMUTABLE DON'T NEED IT
    @Override
    public EMConstantStackMap clone() {
        return this;
    }

    public EMDefinitionStackMap toMutable() {
        return new EMDefinitionStackMap(new TreeMap<>(getBackingMap()));
    }

    public static EMConstantStackMap fromNBT(EMDefinitionsRegistry registry, NBTTagCompound nbt) throws EMException {
        EMDefinitionStack[] defStacks = new EMDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = EMDefinitionStack.fromNBT(registry,nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].getDefinition().equals(nbtE__)) {
                throw new EMException("Something went Wrong");
            }
        }
        return new EMConstantStackMap(defStacks);
    }
}
