package pers.gwyog.gtneioreplugin.plugin;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Loader;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5AsteroidStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5IEVeinStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5SmallOreStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech6.PluginGT6BedrockOreStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech6.PluginGT6SmallOreStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech6.PluginGT6VeinStat;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;

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
        if (GTNEIOrePlugin.GTVersion.equals("GT5")) {
            PluginGT5VeinStat pluginVeinStat = new PluginGT5VeinStat();
            PluginGT5AsteroidStat pluginAsteriodStat = new PluginGT5AsteroidStat();
            PluginGT5SmallOreStat pluginSmallOreStat = new PluginGT5SmallOreStat();
            API.registerRecipeHandler(pluginVeinStat);
            API.registerUsageHandler(pluginVeinStat);
            API.registerRecipeHandler(pluginAsteriodStat);
            API.registerUsageHandler(pluginAsteriodStat);
            API.registerRecipeHandler(pluginSmallOreStat);
            API.registerUsageHandler(pluginSmallOreStat);
            if (GT5OreLayerHelper.immersiveEngineeringSupport) {
                PluginGT5IEVeinStat pluginIEVeinStat = new PluginGT5IEVeinStat();
                API.registerRecipeHandler(pluginIEVeinStat);
                API.registerUsageHandler(pluginIEVeinStat);
            }
        }
        else {
            PluginGT6VeinStat pluginGT6VeinStat = new PluginGT6VeinStat();
            PluginGT6SmallOreStat pluginGT6SmallOreStat = new PluginGT6SmallOreStat();
            PluginGT6BedrockOreStat pluginGT6BedrockOreStat = new PluginGT6BedrockOreStat();
            API.registerRecipeHandler(pluginGT6VeinStat);
            API.registerUsageHandler(pluginGT6VeinStat);
            API.registerRecipeHandler(pluginGT6SmallOreStat);
            API.registerUsageHandler(pluginGT6SmallOreStat);
            API.registerRecipeHandler(pluginGT6BedrockOreStat);
            API.registerUsageHandler(pluginGT6BedrockOreStat);
        } 
    }

}
