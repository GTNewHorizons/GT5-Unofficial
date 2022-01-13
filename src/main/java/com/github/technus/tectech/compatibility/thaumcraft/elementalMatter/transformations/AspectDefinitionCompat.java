package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations;


import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;

import java.util.HashMap;

/**
 * Created by Tec on 21.05.2017.
 */
public class AspectDefinitionCompat {
    public static AspectDefinitionCompat aspectDefinitionCompat;
    public final HashMap<iElementalDefinition,String> defToAspect = new HashMap<>();
    public final HashMap<String,iElementalDefinition> aspectToDef = new HashMap<>();

    public void run(){}

    public Object getAspect(iElementalDefinition definition){
        return null;
    }

    public String getAspectTag(iElementalDefinition definition){
        return null;
    }

    public iElementalDefinition getDefinition(String aspect){
        return null;
    }
}
