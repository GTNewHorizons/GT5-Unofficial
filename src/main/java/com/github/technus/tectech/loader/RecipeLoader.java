package com.github.technus.tectech.loader;

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
