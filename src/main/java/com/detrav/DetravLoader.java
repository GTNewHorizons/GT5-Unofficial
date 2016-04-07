package com.detrav;

import com.detrav.items.DetravMetaGeneratedItem01;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.items.processing.ProcessingDetravPortableCharger;
import com.detrav.items.processing.ProcessingDetravShaping;
import com.detrav.items.processing.ProcessingDetravToolProPick;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class DetravLoader implements Runnable {
    @Override
    public void run() {

        //items
        new DetravMetaGeneratedItem01();
        new DetravMetaGeneratedTool01();

        //recipes and etc
        new ProcessingDetravToolProPick();
        new ProcessingDetravShaping();
        new ProcessingDetravPortableCharger();
        //new Detrav_MetaGenerated_Tool_01();
        //new ProcessingDetravToolProPick();
    }
}
