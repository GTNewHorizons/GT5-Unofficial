package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.magicAddon.definitions.dComplexAspectDefinition;
import com.github.technus.tectech.magicAddon.definitions.ePrimalAspectDefinition;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ElementalLoader implements Runnable {
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

        dAtomDefinition.run();

        ePrimalAspectDefinition.run();

        dComplexAspectDefinition.run();
    }
}
