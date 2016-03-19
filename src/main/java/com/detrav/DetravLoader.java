package com.detrav;

import com.detrav.tools.DetravMetaGeneratedItem01;
import com.detrav.tools.DetravToolProPick;
import com.detrav.tools.Detrav_MetaGenerated_Tool_01;
import com.detrav.tools.ProcessingToolHeadProPick;
import gregtech.api.enums.OrePrefixes;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class DetravLoader implements Runnable {
    @Override
    public void run() {
        new DetravMetaGeneratedItem01();
        //new Detrav_MetaGenerated_Tool_01();
        //new ProcessingToolHeadProPick();
    }
}
