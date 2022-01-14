package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;

/**
 * Created by Tec on 12.05.2017.
 */
public final class cElementalConstantStackMap/*IMMUTABLE*/ extends cElementalStackMap<cElementalDefinitionStack> {//Target class for construction of definitions/recipes
    //Constructors + Clone, all make a whole new OBJ.
    public static final cElementalConstantStackMap EMPTY = new cElementalConstantStackMap();

    private cElementalConstantStackMap() {
        super(Collections.emptyNavigableMap());
    }

    public cElementalConstantStackMap(cElementalDefinitionStack... in) {
        this(new cElementalDefinitionStackMap(in).map);
    }

    public cElementalConstantStackMap(NavigableMap<iElementalDefinition, cElementalDefinitionStack> in) {
        super(Collections.unmodifiableNavigableMap(in));
    }

    @Override
    public Class<cElementalDefinitionStack> getType() {
        return cElementalDefinitionStack.class;
    }

    //IMMUTABLE DON'T NEED IT
    @Override
    public cElementalConstantStackMap clone() {
        return this;
    }

    public cElementalDefinitionStackMap toMutable() {
        return new cElementalDefinitionStackMap(new TreeMap<>(map));
    }

    public static cElementalConstantStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(nbtE__)) {
                throw new tElementalException("Something went Wrong");
            }
        }
        return new cElementalConstantStackMap(defStacks);
    }
}
