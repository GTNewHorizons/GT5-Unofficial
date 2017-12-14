package com.github.technus.tectech.elementalMatter.core;

import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import java.util.TreeMap;

import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;

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
        map=new cElementalMutableDefinitionStackMap(in).map;
    }

    public cElementalDefinitionStackMap(cElementalDefinitionStack... in) {
        map=new cElementalMutableDefinitionStackMap(in).map;
    }

    public cElementalDefinitionStackMap(TreeMap<iElementalDefinition, cElementalDefinitionStack> in) {
        map = new TreeMap<>(in);
    }

    cElementalDefinitionStackMap(cElementalMutableDefinitionStackMap unsafeMap){
        map=unsafeMap.map;
    }

    //IMMUTABLE DON'T NEED IT
    @Override
    public final cElementalDefinitionStackMap clone() {
        return this;
    }

    public cElementalMutableDefinitionStackMap toMutable() {
        return new cElementalMutableDefinitionStackMap(map);
    }

    @Override
    @Deprecated//BETTER TO JUST MAKE A MUTABLE VERSION AND DO SHIT ON IT
    public TreeMap<iElementalDefinition, cElementalDefinitionStack> getRawMap() {
        return toMutable().getRawMap();
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
