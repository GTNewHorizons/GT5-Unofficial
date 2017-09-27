package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.elementalMatter.definitions.complex.dAtomDefinition;
import com.github.technus.tectech.elementalMatter.definitions.complex.dHadronDefinition;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eLeptonDefinition;

import static com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition.stableRawLifeTime;
import static com.github.technus.tectech.elementalMatter.definitions.complex.dAtomDefinition.addOverride;

/**
 * Created by Tec on 29.05.2017.
 */
public class AtomOverrider implements Runnable{
    @Override
    public void run() {
        try {
            addOverride(new dAtomDefinition(
                    new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 2),
                    dHadronDefinition.hadron_p2,
                    new cElementalDefinitionStack(dHadronDefinition.hadron_n, 3)
            ), stableRawLifeTime);//He3

            addOverride(new dAtomDefinition(
                    eLeptonDefinition.lepton_e1,
                    dHadronDefinition.hadron_p1,
                    dHadronDefinition.hadron_n1
            ), stableRawLifeTime);//D

            addOverride(new dAtomDefinition(
                    eLeptonDefinition.lepton_e1,
                    dHadronDefinition.hadron_p1,
                    dHadronDefinition.hadron_n2
            ), 1e9f);//T


        }catch (tElementalException e){
            e.printStackTrace();
        }
    }
}
