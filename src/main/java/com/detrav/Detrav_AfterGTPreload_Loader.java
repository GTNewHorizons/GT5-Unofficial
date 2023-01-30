package com.detrav;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.items.processing.ProcessingDetravToolProspector;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class Detrav_AfterGTPreload_Loader implements Runnable {

    @Override
    public void run() {

        // items
        new DetravMetaGeneratedTool01();

        // recipes and etc
        new ProcessingDetravToolProspector();

    }
}
