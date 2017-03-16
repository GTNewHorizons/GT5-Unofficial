package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.casing.GT_Loader_CasingsTT;

public class GT_CustomLoader {
    private GT_Loader_Machines ElementalLoader;
    private GT_Loader_CasingsTT ElementalCasing;
    private GT_Loader_Recipes ElementalRecipes;

    public GT_CustomLoader() {
    }

    public void run() {
        ElementalCasing = new GT_Loader_CasingsTT();
        ElementalCasing.run();
        TecTech.Logger.info("Casing Init Done");
        ElementalLoader = new GT_Loader_Machines();
        ElementalLoader.run();
        TecTech.Logger.info("Machine Init Done");
    }

    public void run2() {
        ElementalRecipes = new GT_Loader_Recipes();
        ElementalRecipes.run();
        TecTech.Logger.info("Recipe Init Done");
    }
}
