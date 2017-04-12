package com.github.technus.tectech.loader;

import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {
    public void run() {
        QuantumGlassBlock.run();
        DebugContainer_EM.run();
        DebugBuilder.run();
    }
}
