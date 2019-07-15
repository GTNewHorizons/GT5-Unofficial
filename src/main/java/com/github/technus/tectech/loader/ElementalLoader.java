package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.dComplexAspectDefinition;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalPrimitive;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.iaeaNuclide;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.hadron.dHadronDefinition;
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

        cElementalPrimitive.run();

        cPrimitiveDefinition.run();

        eQuarkDefinition.run();
        eLeptonDefinition.run();
        eNeutrinoDefinition.run();
        eBosonDefinition.run();

        dHadronDefinition.run();

        iaeaNuclide.run();

        dAtomDefinition.run();

        ePrimalAspectDefinition.run();

        dComplexAspectDefinition.run();
    }
}
