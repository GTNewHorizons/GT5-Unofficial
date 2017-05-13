package com.github.technus.tectech.elementalMatter.classes;

import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.cPrimitiveDefinition.nbtE__;

/**
 * Created by Tec on 12.05.2017.
 */
public final class cElementalDefinitionStackMap/*IMMUTABLE*/ extends cElementalStackMap {//Target class for construction of definitions/recipes
    //Constructors + Clone, all make a whole new OBJ.
    public static final cElementalDefinitionStackMap empty = new cElementalDefinitionStackMap();

    private cElementalDefinitionStackMap() {
        map = new TreeMap<>();
    }

    @Deprecated
    public cElementalDefinitionStackMap(iElementalDefinition... in) {
        map = new TreeMap<>();
        for (iElementalDefinition definition : in)
            map.put(definition, new cElementalDefinitionStack(definition, 1));
    }

    public cElementalDefinitionStackMap(cElementalDefinitionStack... in) {
        map = new TreeMap<>();
        for (cElementalDefinitionStack stack : in)
            map.put(stack.definition, stack);
    }

    public cElementalDefinitionStackMap(TreeMap<iElementalDefinition, cElementalDefinitionStack> in) {
        map = new TreeMap<>(in);
    }

    //IMMUTABLE DON'T NEED IT
    @Override
    public cElementalDefinitionStackMap clone() {
        return this;
    }

    public cElementalMutableDefinitionStackMap constructMutable() {
        return new cElementalMutableDefinitionStackMap(map);
    }

    @Override
    @Deprecated//BETTER TO JUST MAKE A MUTABLE VERSION AND DO SHIT ON IT
    public TreeMap<iElementalDefinition, cElementalDefinitionStack> getRawMap() {
        return constructMutable().getRawMap();
    }

    public static cElementalDefinitionStackMap fromNBT(NBTTagCompound nbt) throws tElementalException {
        final cElementalDefinitionStack[] defStacks = new cElementalDefinitionStack[nbt.getInteger("i")];
        for (int i = 0; i < defStacks.length; i++) {
            defStacks[i] = cElementalDefinitionStack.fromNBT(nbt.getCompoundTag(Integer.toString(i)));
            if (defStacks[i].definition.equals(nbtE__))
                throw new tElementalException("Something went Wrong");
        }
        return new cElementalDefinitionStackMap(defStacks);
    }
}
