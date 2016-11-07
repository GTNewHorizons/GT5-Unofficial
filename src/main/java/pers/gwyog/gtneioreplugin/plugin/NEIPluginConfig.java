package pers.gwyog.gtneioreplugin.plugin;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Loader;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper;

public class NEIPluginConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return "GregTech Ore Plugin";
    }

    @Override
    public String getVersion() {
        return GTNEIOrePlugin.VERSION;
    }

    @Override
    public void loadConfig() {
        PluginVeinStat pluginVeinStat = new PluginVeinStat();
        PluginAsteroidStat pluginAsteriodStat = new PluginAsteroidStat();
        PluginSmallOreStat pluginSmallOreStat = new PluginSmallOreStat();
        API.registerRecipeHandler(pluginVeinStat);
        API.registerUsageHandler(pluginVeinStat);
        API.registerRecipeHandler(pluginAsteriodStat);
        API.registerUsageHandler(pluginAsteriodStat);
        API.registerRecipeHandler(pluginSmallOreStat);
        API.registerUsageHandler(pluginSmallOreStat);
        if (GTOreLayerHelper.immersiveEngineeringSupport) {
            PluginIEVeinStat pluginIEVeinStat = new PluginIEVeinStat();
            API.registerRecipeHandler(pluginIEVeinStat);
            API.registerUsageHandler(pluginIEVeinStat);
        }
    }

}
