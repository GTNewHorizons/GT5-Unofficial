package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantifier;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class GT_Loader_Recipes implements Runnable {
    public void run() {
        // ===================================================================================================
        // def init
        // ===================================================================================================

        cElementalPrimitive.run();

        cPrimitiveDefinition.run();

        eQuarkDefinition.run();
        eLeptonDefinition.run();
        eNeutrinoDefinition.run();
        eBosonDefinition.run();

        dHadronDefinition.run();

        dAtomDefinition.run();

        // ===================================================================================================
        // Recipe init
        // ===================================================================================================

        GT_MetaTileEntity_EM_quantifier.run();
    }
}
