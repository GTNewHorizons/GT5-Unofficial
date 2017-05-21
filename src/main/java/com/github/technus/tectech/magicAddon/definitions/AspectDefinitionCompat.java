package com.github.technus.tectech.magicAddon.definitions;


import com.github.technus.tectech.elementalMatter.classes.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;

import java.util.HashMap;

/**
 * Created by Tec on 21.05.2017.
 */
public class AspectDefinitionCompat {
    public static AspectDefinitionCompat aspectDefinitionCompat;
    static final HashMap<cElementalDefinition,String> defToAspect = new HashMap<>();
    public static final HashMap<String,cElementalDefinition> aspectToDef = new HashMap<>();

    public void run(){}

    Object getAspect(cElementalDefinition definition){
        return null;
    }

    String getAspectTag(cElementalDefinition definition){
        return null;
    }

    iElementalDefinition getDefinition(String aspect){
        return null;
    }
}
