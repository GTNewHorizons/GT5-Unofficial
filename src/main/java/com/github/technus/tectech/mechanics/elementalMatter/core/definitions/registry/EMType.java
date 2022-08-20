package com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EMType {
    private final Map<String, IEMDefinition> definitions = new HashMap<>();
    private final Map<String, IEMDefinition> definitionsR = Collections.unmodifiableMap(definitions);
    private final Class<? extends IEMDefinition> clazz;
    private final String unlocalizedName;

    public EMType(Class<? extends IEMDefinition> clazz, String unlocalizedName) {
        this.clazz = clazz;
        this.unlocalizedName = unlocalizedName;
    }

    public Class<? extends IEMDefinition> getClazz() {
        return clazz;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return translateToLocal(getUnlocalizedName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return clazz == ((EMType) o).clazz;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    public void addDefinition(String bind, IEMDefinition definition) {
        if (definitions.put(bind, definition) != null) {
            EMException e = new EMException("NBT Bind collision on Direct bind!");
            if (DEBUG_MODE) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
    }

    public Map<String, IEMDefinition> getDefinitions() {
        return definitionsR;
    }
}
