package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMComplexAspectDefinition;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.EMPrimitive;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMNuclideIAEA;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.*;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ElementalLoader implements Runnable {
    @Override
    public void run() {
        // ===================================================================================================
        // Definition init
        // ===================================================================================================

        EMPrimitive.run();

        EMPrimitiveDefinition.run();

        EMQuarkDefinition.run();
        EMLeptonDefinition.run();
        EMNeutrinoDefinition.run();
        EMBosonDefinition.run();

        EMHadronDefinition.run();

        EMNuclideIAEA.run();

        EMAtomDefinition.run();

        EMPrimalAspectDefinition.run();

        EMComplexAspectDefinition.run();
    }
}
