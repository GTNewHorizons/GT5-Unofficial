package com.github.technus.tectech.mechanics.elementalMatter.core.maps;

import static com.github.technus.tectech.util.TT_Utility.unpackNBT;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 22.01.2017.
 */
public final class EMDefinitionStackMap extends EMStackMap<EMDefinitionStack>
        implements IEMMapWriteExact<EMDefinitionStack> { // Transient class for construction of definitions/recipes
    // Constructors + Clone, all make a whole new OBJ.
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

    public static EMDefinitionStackMap fromNBT(EMDefinitionsRegistry registry, NBTTagCompound nbt) throws EMException {
        return new EMDefinitionStackMap(
                unpackNBT(EMDefinitionStack.class, inner -> EMDefinitionStack.fromNBT(registry, inner), nbt));
    }
}
