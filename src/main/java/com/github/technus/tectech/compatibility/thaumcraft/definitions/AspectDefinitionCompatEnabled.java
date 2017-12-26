package com.github.technus.tectech.compatibility.thaumcraft.definitions;

import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;

import static com.github.technus.tectech.compatibility.thaumcraft.definitions.ePrimalAspectDefinition.*;

/**
 * Created by Tec on 21.05.2017.
 */
public final class AspectDefinitionCompatEnabled extends AspectDefinitionCompat {
    @Override
    public void run(){
        AspectDefinitionCompat.defToAspect.put(magic_air,"aer");
        AspectDefinitionCompat.defToAspect.put(magic_earth,"terra");
        AspectDefinitionCompat.defToAspect.put(magic_fire,"ignis");
        AspectDefinitionCompat.defToAspect.put(magic_water,"aqua");
        AspectDefinitionCompat.defToAspect.put(magic_order,"ordo");
        AspectDefinitionCompat.defToAspect.put(magic_entropy,"perditio");

        AspectDefinitionCompat.aspectToDef.put("aer",magic_air);
        AspectDefinitionCompat.aspectToDef.put("terra",magic_earth);
        AspectDefinitionCompat.aspectToDef.put("ignis",magic_fire);
        AspectDefinitionCompat.aspectToDef.put("aqua",magic_water);
        AspectDefinitionCompat.aspectToDef.put("ordo",magic_order);
        AspectDefinitionCompat.aspectToDef.put("perditio",magic_entropy);

        ArrayList<Aspect> list=Aspect.getCompoundAspects();
        Aspect[] array=list.toArray(new Aspect[0]);
        while (list.size()>0) {
            for (Aspect aspect : array) {
                if (list.contains(aspect)) {
                    Aspect[] content = aspect.getComponents();
                    if (content.length != 2) {
                        list.remove(aspect);
                    }else if(AspectDefinitionCompat.aspectToDef.containsKey(content[0].getTag()) && AspectDefinitionCompat.aspectToDef.containsKey(content[1].getTag())){
                        try {
                            dComplexAspectDefinition newAspect;
                            if(content[0].getTag().equals(content[1].getTag())){
                                newAspect = new dComplexAspectDefinition(AspectDefinitionCompat.aspectToDef.get(content[0].getTag()).getStackForm(2)
                                );
                            }else{
                                newAspect = new dComplexAspectDefinition(AspectDefinitionCompat.aspectToDef.get(content[0].getTag()).getStackForm(1), AspectDefinitionCompat.aspectToDef.get(content[1].getTag()).getStackForm(1)
                                );
                            }
                            AspectDefinitionCompat.aspectToDef.put(aspect.getTag(),newAspect);
                            AspectDefinitionCompat.defToAspect.put(newAspect,aspect.getTag());
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
        return Aspect.getAspect(AspectDefinitionCompat.defToAspect.get(definition));
    }

    @Override
    String getAspectTag(cElementalDefinition definition) {
        return AspectDefinitionCompat.defToAspect.get(definition);
    }

    @Override
    iElementalDefinition getDefinition(String aspect) {
        return AspectDefinitionCompat.aspectToDef.get(aspect);
    }

}
