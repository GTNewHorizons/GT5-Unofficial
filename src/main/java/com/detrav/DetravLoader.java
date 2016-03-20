package com.detrav;

import com.detrav.items.DetravMetaGeneratedItem01;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.tools.ProcessingDetravShaping;
import com.detrav.tools.ProcessingDetravToolProPick;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class DetravLoader implements Runnable {
    @Override
    public void run() {

        new DetravMetaGeneratedItem01();
        new DetravMetaGeneratedTool01();
        new ProcessingDetravToolProPick();
        new ProcessingDetravShaping();
        //new Detrav_MetaGenerated_Tool_01();
        //new ProcessingDetravToolProPick();
    }
}
