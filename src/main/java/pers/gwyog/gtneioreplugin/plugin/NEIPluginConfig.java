package pers.gwyog.gtneioreplugin.plugin;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5SmallOreStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;

@SuppressWarnings("unused")
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
        PluginGT5VeinStat pluginVeinStat = new PluginGT5VeinStat();
        PluginGT5SmallOreStat pluginSmallOreStat = new PluginGT5SmallOreStat();
        PluginGT5UndergroundFluid pluginGT5UndergroundFluid = new PluginGT5UndergroundFluid();
        API.registerRecipeHandler(pluginVeinStat);
        API.registerUsageHandler(pluginVeinStat);
        API.registerRecipeHandler(pluginSmallOreStat);
        API.registerUsageHandler(pluginSmallOreStat);
        API.registerRecipeHandler(pluginGT5UndergroundFluid);
        API.registerUsageHandler(pluginGT5UndergroundFluid);
    }
}
