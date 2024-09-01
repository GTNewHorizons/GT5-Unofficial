package gtPlusPlus.plugin.agrichem;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.plugin.manager.CoreManager;

// Called by Core_Manager#veryEarlyInit
@SuppressWarnings("unused")
public class AgrichemCore implements IPlugin {

    static final AgrichemCore mInstance;

    static {
        mInstance = new AgrichemCore();
        CoreManager.registerPlugin(mInstance);
        mInstance.log("Preparing " + mInstance.getPluginName() + " for use.");
    }

    @Override
    public boolean preInit() {
        AgrichemFluids.init();
        return true;
    }

    @Override
    public boolean init() {
        mInstance.log("Setting Items");
        return true;
    }

    @Override
    public boolean postInit() {
        mInstance.log("Generating Recipes");
        return true;
    }

    @Override
    public boolean serverStart() {
        return true;
    }

    @Override
    public boolean serverStop() {
        return true;
    }

    @Override
    public String getPluginName() {
        return "GT++ Agrichemistry Module";
    }

    @Override
    public String getPluginAbbreviation() {
        return "FARM";
    }
}
