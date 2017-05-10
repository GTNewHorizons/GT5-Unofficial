package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {
    public void run() {

        QuantumGlassBlock.run();
        TecTech.Logger.info("Quantum Glass registered");

        DebugContainer_EM.run();
        DebugBuilder.run();
        TecTech.Logger.info("Debug Items registered");
    }
}
