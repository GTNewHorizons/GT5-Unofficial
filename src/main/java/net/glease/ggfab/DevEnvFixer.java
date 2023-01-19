package net.glease.ggfab;

import gregtech.loaders.materialprocessing.ProcessingModSupport;

public class DevEnvFixer implements Runnable {
    @Override
    public void run() {
        ProcessingModSupport.aTGregSupport = true;
        ProcessingModSupport.aEnableThermalFoundationMats = true;
    }
}
