package tectech.loader;

import java.io.File;

import eu.usrv.yamcore.config.ConfigManager;

public class TecTechConfig extends ConfigManager {

    public TecTechConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
        super(pConfigBaseDirectory, pModCollectionDirectory, pModID);
    }


    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PreInit() {


    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void Init() {
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PostInit() {}
}
