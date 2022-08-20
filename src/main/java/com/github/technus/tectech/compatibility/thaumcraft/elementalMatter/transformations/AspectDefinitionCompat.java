package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import java.util.HashMap;

/**
 * Created by Tec on 21.05.2017.
 */
public class AspectDefinitionCompat {
    public static AspectDefinitionCompat aspectDefinitionCompat;
    private final HashMap<IEMDefinition, String> defToAspect = new HashMap<>();
    private final HashMap<String, IEMDefinition> aspectToDef = new HashMap<>();

    public void run(EMDefinitionsRegistry registry) {}

    public String getAspectTag(IEMDefinition definition) {
        return null;
    }

    public String getAspectLocalizedName(IEMDefinition definition) {
        return null;
    }

    public IEMDefinition getDefinition(String aspect) {
        return null;
    }

    public HashMap<IEMDefinition, String> getDefToAspect() {
        return defToAspect;
    }

    public HashMap<String, IEMDefinition> getAspectToDef() {
        return aspectToDef;
    }
}
