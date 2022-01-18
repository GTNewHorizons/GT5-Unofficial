package com.github.technus.tectech.mechanics.elementalMatter.core.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;
import java.util.function.Function;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.null__;

public class EMDefinitionsRegistry {
    private static final NavigableSet<IEMDefinition>                                     STACKS_REGISTERED = new TreeSet<>();
    private static final Map<Integer, IEMDefinition>                                     DIRECT_BINDS      = new HashMap<>();
    private static final Map<Integer, Function<NBTTagCompound, ? extends IEMDefinition>> CLASS_BINDS       = new HashMap<>();//creator methods in subclasses
    private static final Map<Integer, Class<? extends IEMDefinition>>                    CLASSES           = new HashMap<>();
    private static final Map<Class<? extends IEMDefinition>, Integer>                    CLASS_TYPES       = new HashMap<>();
    private static final String                                                          INDIRECT_TAG      = "t";
    private static final String                                                          DIRECT_TAG        = "c";

    private EMDefinitionsRegistry() {
    }

    static {
        CLASS_BINDS.put(0, EMDefinitionsRegistry::getDefinitionDirect);
        CLASS_BINDS.put((int) 'p', EMDefinitionsRegistry::getDefinitionDirect);
    }

    private static IEMDefinition getDefinitionDirect(NBTTagCompound nbt) {
        return DIRECT_BINDS.get(nbt.getInteger(getDirectTagName()));
    }

    public static IEMDefinition fromNBT(NBTTagCompound nbt) {
        IEMDefinition apply;
        try {
            apply = CLASS_BINDS.get(nbt.getInteger(getIndirectTagName())).apply(nbt);
        } catch (Exception e) {
            EMException emException = new EMException("Failed to create from: " + nbt.toString(), e);
            if (DEBUG_MODE) {
                emException.printStackTrace();
                return nbtE__;
            } else {
                throw emException;
            }
        }
        if (!DEBUG_MODE) {
            if (apply == nbtE__) {
                throw new EMException("Deserialized to NBT ERROR!");
            } else if (apply == null__ || apply == null) {
                throw new EMException("Deserialized to NULL POINTER!");
            }
        }
        if (apply == null) {
            return null__;
        }
        return apply;
    }

    public static <T extends IEMDefinition> void registerDefinitionClass(int shortcutNBT, Function<NBTTagCompound, T> creator, Class<T> clazz, int classID) {
        if (CLASS_BINDS.put(shortcutNBT, creator) != null) {
            EMException e = new EMException("Duplicate NBT shortcut! " + shortcutNBT + " used for NBT based creation");
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
        if (CLASSES.put(classID, clazz) != null) {
            EMException e = new EMException("Duplicate Class ID! " + classID + " used for class comparison");
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
        CLASS_TYPES.put(clazz, classID);
    }

    public static void registerDirectDefinition(IEMDefinition definition, int id) {
        IEMDefinition old = DIRECT_BINDS.put(id, definition);
        if (old != null) {
            EMException e = new EMException("Duplicate primitive EM ID: " + id +
                    " for " + definition.getLocalizedName() +
                    " and " + old.getLocalizedName());
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
    }

    public static NavigableSet<IEMDefinition> getStacksRegisteredForDisplay() {
        return STACKS_REGISTERED;
    }

    public static void registerForDisplay(IEMDefinition definition){
        STACKS_REGISTERED.add(definition);
        STACKS_REGISTERED.add(definition.getAnti());
    }

    public static String getIndirectTagName() {
        return INDIRECT_TAG;
    }

    public static String getDirectTagName() {
        return DIRECT_TAG;
    }
}
