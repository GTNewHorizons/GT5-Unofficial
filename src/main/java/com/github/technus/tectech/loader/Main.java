package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.casing.GT_Loader_CasingsTT;

public class Main {
    private Machines ElementalLoader;
    private GT_Loader_CasingsTT ElementalCasing;
    private Recipes ElementalRecipes;

    public Main() {
    }

    public void run() {
        ElementalCasing = new GT_Loader_CasingsTT();
        ElementalCasing.run();
        TecTech.Logger.info("Casing Init Done");
        ElementalLoader = new Machines();
        ElementalLoader.run();
        TecTech.Logger.info("Machine Init Done");
    }

    public void run2() {
        ElementalRecipes = new Recipes();
        ElementalRecipes.run();
        TecTech.Logger.info("Recipe Init Done");
    }
}
