package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_computer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_research;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_MultiblockBase_EM;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class RecipeLoader implements Runnable {
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

        ePrimalAspectDefinition.run();

        dComplexAspectDefinition.run();

        // ===================================================================================================
        // Recipe init
        // ===================================================================================================

        GT_MetaTileEntity_MultiblockBase_EM.run();
        GT_MetaTileEntity_Hatch_Rack.run();
        GT_MetaTileEntity_EM_computer.run();
        GT_MetaTileEntity_EM_research.run();
        GT_MetaTileEntity_EM_quantizer.run();
    }
}
