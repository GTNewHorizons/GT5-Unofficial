package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.definitions.*;
import com.github.technus.tectech.thing.block.QuantumGlass;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_computer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_MultiblockBase_EM;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class ThingsLoader implements Runnable {
    public void run() {
        QuantumGlass.run();
        DebugContainer_EM.run();
        DebugBuilder.run();
    }
}
