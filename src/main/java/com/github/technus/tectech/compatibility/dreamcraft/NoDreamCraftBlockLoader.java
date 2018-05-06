package com.github.technus.tectech.compatibility.dreamcraft;

import com.github.technus.tectech.thing.casing.GT_Block_CasingsNH;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;

public class NoDreamCraftBlockLoader implements Runnable {
    @Override
    public void run() {
        TT_Container_Casings.sBlockCasingsNH = new GT_Block_CasingsNH();
    }
}
