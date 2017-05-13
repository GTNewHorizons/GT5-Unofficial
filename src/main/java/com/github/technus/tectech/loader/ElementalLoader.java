package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.elementalMatter.magicAddon.definitions.dComplexAspectDefinition;
import com.github.technus.tectech.elementalMatter.magicAddon.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;

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
