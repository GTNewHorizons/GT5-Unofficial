package com.github.technus.tectech.elementalMatter.core.templates;

import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 23.01.2017.
 */
public abstract class cElementalDefinition extends iElementalDefinition {
    //Nothing array
    public static final iElementalDefinition[] nothing = new cElementalPrimitive[0];

    //add text based creators for recipe formula input?
    private static final Map<Byte, Method> nbtCreationBind = new HashMap<>();//creator methods in subclasses
    private static final HashSet<Byte> classSet = new HashSet<>();

    protected static void addCreatorFromNBT(byte shortcutNBT, Method constructorFromNBT,byte classID) {
        if(nbtCreationBind.put(shortcutNBT, constructorFromNBT)!=null) throw new Error("Duplicate NBT shortcut! "+shortcutNBT);
        if(!classSet.add(classID)) throw new Error("Duplicate Class ID! "+classID);
    }

    @Override
    public final cElementalDefinition clone() {
        return this;//IMMUTABLE
    }

    public static iElementalDefinition fromNBT(NBTTagCompound nbt) {
        try {
            return (iElementalDefinition) nbtCreationBind.get(nbt.getByte("t")).invoke(null, nbt);
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
            return nbtE__;
        }
    }

    @Override
    public int compareTo(iElementalDefinition o) {
        final int classCompare = compareClassID(o);
        if (classCompare != 0) return classCompare;

        //only of the internal def stacks!!!
        //that allows neat check if the same thing and
        //top hierarchy amount can be used to store amount info
        return compareInnerContentsWithAmounts(getSubParticles().values(), o.getSubParticles().values());
    }

    //use only for nested operations!
    private static int compareInnerContentsWithAmounts(cElementalDefinitionStack[] tc, cElementalDefinitionStack[] sc) {
        if (tc == null) {
            if (sc == null) return 0;
            else return -1;
        }
        if (sc == null) return 1;

        final int lenDiff = tc.length - sc.length;
        if (lenDiff != 0) return lenDiff;

        for (int i = 0; i < tc.length; i++) {
            int cn = tc[i].definition.compareTo(sc[i].definition);
            if (cn != 0) return cn;

            if (tc[i].amount > sc[i].amount) return 1;
            if (tc[i].amount < sc[i].amount) return -1;
        }
        return 0;
    }

    @Override
    public final cElementalDefinitionStack getStackForm(int i) {
        return new cElementalDefinitionStack(this,i);
    }

    @Override
    public final boolean equals(Object obj) {
        if(this==obj)return true;
        if (obj instanceof iElementalDefinition)
            return compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    @Override
    public int hashCode() {//Internal amounts should be also hashed
        int hash = -(getSubParticles().size() << 4);
        for (cElementalDefinitionStack s : getSubParticles().values()) {
            hash += ((s.amount & 0x1) == 0 ? -s.amount : s.amount) + s.definition.hashCode();
        }
        return hash;
    }
}
