package com.github.technus.tectech.loader;

import com.github.technus.tectech.thing.casing.GT_Block_CasingsTT;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class CasingLoader implements Runnable {
    public void run() {
        GT_Container_CasingsTT.sBlockCasingsTT = new GT_Block_CasingsTT();
    }
}
