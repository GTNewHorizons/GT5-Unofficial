package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMComplexAspectDefinition;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMScalarBosonDefinition;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ElementalLoader {

    public void run(EMDefinitionsRegistry registry) {
        // ===================================================================================================
        // Definition init
        // ===================================================================================================

        EMPrimitiveDefinition.run(registry);

        EMQuarkDefinition.run(registry);
        EMLeptonDefinition.run(registry);
        EMNeutrinoDefinition.run(registry);
        EMGaugeBosonDefinition.run(registry);
        EMScalarBosonDefinition.run(registry);

        EMHadronDefinition.run(registry);

        EMAtomDefinition.run(registry);

        EMPrimalAspectDefinition.run(registry);
        EMComplexAspectDefinition.run(registry);
    }
}
