package gtPlusPlus.plugin.agrichem;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.plugin.manager.Core_Manager;

// Called by Core_Manager#veryEarlyInit
@SuppressWarnings("unused")
public class Core_Agrichem implements IPlugin {

    static final Core_Agrichem mInstance;

    static {
        mInstance = new Core_Agrichem();
        Core_Manager.registerPlugin(mInstance);
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
