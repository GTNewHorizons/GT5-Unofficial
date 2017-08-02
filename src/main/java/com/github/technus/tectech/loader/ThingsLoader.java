package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Block_CasingsTT;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import cpw.mods.fml.common.Loader;
import openmodularturrets.blocks.turretbases.TurretBaseEM;
import openmodularturrets.blocks.turretheads.TurretHeadEM;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {
    public void run() {
        GT_Container_CasingsTT.sBlockCasingsTT = new GT_Block_CasingsTT();
        TecTech.Logger.info("Elemental Casing registered");

        QuantumGlassBlock.run();
        TecTech.Logger.info("Quantum Glass registered");

        if(Loader.isModLoaded("openmodularturrets")) {
            TurretHeadEM.run();
            TecTech.Logger.info("TurretHeadEM registered");
            TurretBaseEM.run();
            TecTech.Logger.info("TurretBaseEM registered");
        }

        DebugContainer_EM.run();
        DebugBuilder.run();
        TecTech.Logger.info("Debug Items registered");
    }
}
