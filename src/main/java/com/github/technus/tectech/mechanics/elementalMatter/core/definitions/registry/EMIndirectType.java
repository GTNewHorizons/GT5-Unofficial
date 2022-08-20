package com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import java.util.function.BiFunction;
import net.minecraft.nbt.NBTTagCompound;

public class EMIndirectType extends EMType {
    private final BiFunction<EMDefinitionsRegistry, NBTTagCompound, IEMDefinition> creator;

    public EMIndirectType(
            BiFunction<EMDefinitionsRegistry, NBTTagCompound, IEMDefinition> creator,
            Class<? extends IEMDefinition> clazz,
            String unlocalizedName) {
        super(clazz, unlocalizedName);
        this.creator = creator;
    }

    protected BiFunction<EMDefinitionsRegistry, NBTTagCompound, IEMDefinition> getCreator() {
        return creator;
    }

    public IEMDefinition create(EMDefinitionsRegistry registry, NBTTagCompound nbt) {
        try {
            return creator.apply(registry, nbt);
        } catch (Exception e) {
            EMException emException = new EMException("Failed to create from: " + nbt.toString(), e);
            if (DEBUG_MODE) {
                emException.printStackTrace();
                return nbtE__;
            } else {
                throw emException;
            }
        }
    }
}
