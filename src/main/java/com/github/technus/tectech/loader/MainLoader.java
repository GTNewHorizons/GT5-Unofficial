package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;

import static com.github.technus.tectech.TecTech.mainTab;

public class MainLoader {//TODO add checks for - is mod loaded dreamcraft to enable higher tier machinery. (above UV), or implement a check for GT tier values.
    public MainLoader() {}

    public void things() {
        CasingLoader casingLoader = new CasingLoader();
        casingLoader.run();
        TecTech.Logger.info("Casing Init Done");

        ThingsLoader thingsLoader = new ThingsLoader();
        thingsLoader.run();
        TecTech.Logger.info("Other things Init Done");

        MachineLoader machineLoader = new MachineLoader();
        machineLoader.run();
        TecTech.Logger.info("Machine Init Done");
    }

    public void recipes() {
        RecipeLoader recipeLoader = new RecipeLoader();
        recipeLoader.run();
        TecTech.Logger.info("Recipe Init Done");
    }

    public void registerThingsInTabs(){
        QuantumGlassBlock.INSTANCE.setCreativeTab(mainTab);
        GT_Container_CasingsTT.sBlockCasingsTT.setCreativeTab(mainTab);
        DebugContainer_EM.INSTANCE.setCreativeTab(mainTab);
        DebugBuilder.INSTANCE.setCreativeTab(mainTab);
        TecTech.Logger.info("CreativeTab initiation complete");
    }
}
