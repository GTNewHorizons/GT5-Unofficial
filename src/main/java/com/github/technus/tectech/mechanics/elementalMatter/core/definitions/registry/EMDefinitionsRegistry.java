package com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.null__;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import net.minecraft.nbt.NBTTagCompound;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.IEMMapRead;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;

public class EMDefinitionsRegistry {

    private static final String TAG_NAME = "EM";

    private final NavigableSet<IEMDefinition> stacksRegistered = new TreeSet<>();

    private final Map<Integer, IEMDefinition> hashMap = new HashMap<>();
    private final Map<Integer, IEMDefinition> hashMapR = Collections.unmodifiableMap(hashMap);

    private final Map<Class<? extends IEMDefinition>, EMType> types = new HashMap<>();
    private final Map<Class<? extends IEMDefinition>, EMType> typesR = Collections.unmodifiableMap(types);
    private final Map<Class<? extends IEMDefinition>, EMType> directTypes = new HashMap<>();
    private final Map<Class<? extends IEMDefinition>, EMType> directTypesR = Collections.unmodifiableMap(directTypes);
    private final Map<Class<? extends IEMDefinition>, EMIndirectType> indirectTypes = new HashMap<>();
    private final Map<Class<? extends IEMDefinition>, EMIndirectType> indirectTypesR = Collections
            .unmodifiableMap(indirectTypes);

    private final Map<String, EMType> binds = new HashMap<>();
    private final Map<String, EMType> bindsR = Collections.unmodifiableMap(binds);
    private final Map<String, IEMDefinition> directBinds = new HashMap<>();
    private final Map<String, IEMDefinition> directBindsR = Collections.unmodifiableMap(directBinds);
    private final Map<String, EMIndirectType> indirectBinds = new HashMap<>();
    private final Map<String, EMIndirectType> indirectBindsR = Collections.unmodifiableMap(indirectBinds);

    public NBTTagCompound directToNBT(String bind) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(TAG_NAME, bind);
        return nbt;
    }

    public NBTTagCompound indirectToNBT(String bind, IEMMapRead<EMDefinitionStack> content) {
        NBTTagCompound nbt = content.toNBT(this);
        nbt.setString(TAG_NAME, bind);
        return nbt;
    }

    public IEMDefinition fromNBT(NBTTagCompound nbt) {
        IEMDefinition definition;
        try {
            String bind = nbt.getString(TAG_NAME);
            definition = directBinds.get(bind);
            if (definition == null) {
                definition = indirectBinds.get(bind).create(this, nbt);
            }
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
            if (definition == nbtE__) {
                throw new EMException("Deserialized to NBT ERROR!");
            } else if (definition == null) {
                throw new EMException("Deserialized to NULL POINTER!");
            } else if (definition == null__) {
                throw new EMException("Deserialized to NULL!");
            }
        }
        if (definition == null) {
            return null__;
        }
        return definition;
    }

    public boolean isOccupied(String bind) {
        return binds.containsKey(bind);
    }

    protected void addType(EMType emType) {
        if (types.put(emType.getClazz(), emType) != null) {
            EMException e = new EMException("Class collision! " + emType.getClazz().getName());
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
    }

    protected void bindType(String bind, EMType emType) {
        if (binds.put(bind, emType) != null) {
            EMException e = new EMException("NBT Bind collision! " + bind);
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
    }

    public void registerDefinitionClass(String bind, EMIndirectType emIndirectType) {
        addType(emIndirectType);
        indirectTypes.put(emIndirectType.getClazz(), emIndirectType);
        bindType(bind, emIndirectType);
        indirectBinds.put(bind, emIndirectType);
    }

    public void registerDefinitionClass(EMType emDirectType) {
        addType(emDirectType);
    }

    public void registerDirectDefinition(String bind, IEMDefinition definition) {
        if (hashMap.put(definition.hashCode(), definition) != null) {
            EMException e = new EMException("Hash collision! " + definition.hashCode());
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
        EMType emType = directTypes.get(definition.getClass());
        if (emType == null) {
            emType = types.get(definition.getClass());
            if (emType != null) {
                directTypes.put(definition.getClass(), emType);
            } else {
                EMException e = new EMException("Direct Type bind missing! " + definition.getClass().getName());
                if (DEBUG_MODE) {
                    e.printStackTrace();
                } else {
                    throw e;
                }
            }
        }
        if (emType != null) {
            directTypes.put(definition.getClass(), emType);
            bindType(bind, emType);
            emType.addDefinition(bind, definition);
            directBinds.put(bind, definition);
        }
    }

    public void registerForDisplay(IEMDefinition definition) {
        stacksRegistered.add(definition);
        IEMDefinition anti = definition.getAnti();
        if (anti != null) {
            stacksRegistered.add(anti);
        }
    }

    @Deprecated
    public Map<Integer, IEMDefinition> getHashMapping() {
        return hashMapR;
    }

    public NavigableSet<IEMDefinition> getStacksRegisteredForDisplay() {
        return stacksRegistered;
    }

    public Map<String, EMType> getBinds() {
        return bindsR;
    }

    public Map<String, IEMDefinition> getDirectBinds() {
        return directBindsR;
    }

    public Map<String, EMIndirectType> getIndirectBinds() {
        return indirectBindsR;
    }

    public Map<Class<? extends IEMDefinition>, EMType> getTypes() {
        return typesR;
    }

    public Map<Class<? extends IEMDefinition>, EMType> getDirectTypes() {
        return directTypesR;
    }

    public Map<Class<? extends IEMDefinition>, EMIndirectType> getIndirectTypes() {
        return indirectTypesR;
    }
}
