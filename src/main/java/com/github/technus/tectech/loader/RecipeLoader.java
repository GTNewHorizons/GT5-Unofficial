package com.github.technus.tectech.loader;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.elementalMatter.magicAddon.definitions.dComplexAspectDefinition;
import com.github.technus.tectech.elementalMatter.magicAddon.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_computer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_research;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.common.Loader;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class RecipeLoader implements Runnable {
    public void run() {
        // ===================================================================================================
        // Recipes init - common goes here rest goes into methods below
        // ===================================================================================================



        if (Loader.isModLoaded("dreamcraft")) runDreamRun();//TODO init recipes for GTNH version
        else runBloodRun();//TODO init recipes for NON-GTNH version
    }

    private void runDreamRun(){

    }

    private void runBloodRun(){
        //Don't init things after UV!!! They don't conform to the standards.
    }
}
