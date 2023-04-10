package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations;

import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_air;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_earth;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_entropy;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_fire;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_order;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition.magic_water;

import java.util.ArrayList;

import thaumcraft.api.aspects.Aspect;

import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMComplexAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;

/**
 * Created by Tec on 21.05.2017.
 */
public final class AspectDefinitionCompatEnabled extends AspectDefinitionCompat {

    @Override
    public void run(EMDefinitionsRegistry registry) {
        getDefToAspect().put(magic_air, "aer");
        getDefToAspect().put(magic_earth, "terra");
        getDefToAspect().put(magic_fire, "ignis");
        getDefToAspect().put(magic_water, "aqua");
        getDefToAspect().put(magic_order, "ordo");
        getDefToAspect().put(magic_entropy, "perditio");

        getAspectToDef().put("aer", magic_air);
        getAspectToDef().put("terra", magic_earth);
        getAspectToDef().put("ignis", magic_fire);
        getAspectToDef().put("aqua", magic_water);
        getAspectToDef().put("ordo", magic_order);
        getAspectToDef().put("perditio", magic_entropy);

        ArrayList<Aspect> list = Aspect.getCompoundAspects();
        Aspect[] array = list.toArray(new Aspect[0]);
        while (!list.isEmpty()) {
            for (Aspect aspect : array) {
                if (list.contains(aspect)) {
                    Aspect[] content = aspect.getComponents();
                    if (content.length != 2) {
                        list.remove(aspect);
                    } else if (getAspectToDef().containsKey(content[0].getTag())
                            && getAspectToDef().containsKey(content[1].getTag())) {
                                try {
                                    EMComplexAspectDefinition newAspect;
                                    if (content[0].getTag().equals(content[1].getTag())) {
                                        newAspect = new EMComplexAspectDefinition(
                                                getAspectToDef().get(content[0].getTag()).getStackForm(2));
                                    } else {
                                        newAspect = new EMComplexAspectDefinition(
                                                getAspectToDef().get(content[0].getTag()).getStackForm(1),
                                                getAspectToDef().get(content[1].getTag()).getStackForm(1));
                                    }
                                    getAspectToDef().put(aspect.getTag(), newAspect);
                                    getDefToAspect().put(newAspect, aspect.getTag());
                                    registry.registerForDisplay(newAspect);
                                } catch (EMException e) {
                                    /**/
                                } finally {
                                    list.remove(aspect);
                                }
                            }
                }
            }
        }
    }

    @Override
    public String getAspectTag(IEMDefinition definition) {
        return getDefToAspect().get(definition);
    }

    public String getAspectLocalizedName(IEMDefinition definition) {
        Aspect aspect = Aspect.aspects.get(getAspectTag(definition));
        return aspect != null ? aspect.getName() : null;
    }

    @Override
    public IEMDefinition getDefinition(String aspect) {
        return getAspectToDef().get(aspect);
    }
}
