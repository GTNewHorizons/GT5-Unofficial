package com.github.technus.tectech.casing;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Loader_CasingsTT implements Runnable {
    public void run(){
        GT_Container_CasingsTT.sBlockCasingsTT = new GT_Block_CasingsTT();
    }
}
