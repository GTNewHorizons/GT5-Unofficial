package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;

import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.ePrimalAspectDefinition.*;

/**
 * Created by Tec on 21.05.2017.
 */
public final class AspectDefinitionCompatEnabled extends AspectDefinitionCompat {
    @Override
    public void run(){
        defToAspect.put(magic_air,"aer");
        defToAspect.put(magic_earth,"terra");
        defToAspect.put(magic_fire,"ignis");
        defToAspect.put(magic_water,"aqua");
        defToAspect.put(magic_order,"ordo");
        defToAspect.put(magic_entropy,"perditio");

        aspectToDef.put("aer",magic_air);
        aspectToDef.put("terra",magic_earth);
        aspectToDef.put("ignis",magic_fire);
        aspectToDef.put("aqua",magic_water);
        aspectToDef.put("ordo",magic_order);
        aspectToDef.put("perditio",magic_entropy);

        ArrayList<Aspect> list=Aspect.getCompoundAspects();
        Aspect[] array= list.toArray(new Aspect[list.size()]);
        while (!list.isEmpty()) {
            for (Aspect aspect : array) {
                if (list.contains(aspect)) {
                    Aspect[] content = aspect.getComponents();
                    if (content.length != 2) {
                        list.remove(aspect);
                    }else if(aspectToDef.containsKey(content[0].getTag()) && aspectToDef.containsKey(content[1].getTag())){
                        try {
                            dComplexAspectDefinition newAspect;
                            if(content[0].getTag().equals(content[1].getTag())){
                                newAspect = new dComplexAspectDefinition(aspectToDef.get(content[0].getTag()).getStackForm(2)
                                );
                            }else{
                                newAspect = new dComplexAspectDefinition(aspectToDef.get(content[0].getTag()).getStackForm(1), aspectToDef.get(content[1].getTag()).getStackForm(1)
                                );
                            }
                            aspectToDef.put(aspect.getTag(),newAspect);
                            defToAspect.put(newAspect,aspect.getTag());
                        }catch (tElementalException e) {
                            /**/
                        }finally {
                            list.remove(aspect);
                        }
                    }
                }
            }
        }
    }

    @Override
    Aspect getAspect(cElementalDefinition definition) {
        return Aspect.getAspect(defToAspect.get(definition));
    }

    @Override
    String getAspectTag(cElementalDefinition definition) {
        return defToAspect.get(definition);
    }

    @Override
    iElementalDefinition getDefinition(String aspect) {
        return aspectToDef.get(aspect);
    }

}
