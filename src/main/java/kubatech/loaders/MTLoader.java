package kubatech.loaders;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import minetweaker.MineTweakerImplementationAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MTLoader {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[MT Loader]");
    public static MTLoader instance = null;

    public static void init() {
        if (instance == null) {
            instance = new MTLoader();
            MineTweakerImplementationAPI.onPostReload(instance::MTOnPostReload);
        }
    }

    public void MTOnPostReload(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        LOG.info("MT Recipes Loaded!");
        if (LoaderReference.Thaumcraft) TCLoader.register();
    }
}
