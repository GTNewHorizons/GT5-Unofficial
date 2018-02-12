package pers.gwyog.gtneioreplugin.plugin;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Loader;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
//import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5AsteroidStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5SmallOreStat;
import pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;
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
            PluginGT5VeinStat pluginVeinStat = new PluginGT5VeinStat();
            //PluginGT5AsteroidStat pluginAsteriodStat = new PluginGT5AsteroidStat();
            PluginGT5SmallOreStat pluginSmallOreStat = new PluginGT5SmallOreStat();
            API.registerRecipeHandler(pluginVeinStat);
            API.registerUsageHandler(pluginVeinStat);
            //API.registerRecipeHandler(pluginAsteriodStat);
            //API.registerUsageHandler(pluginAsteriodStat);
            API.registerRecipeHandler(pluginSmallOreStat);
            API.registerUsageHandler(pluginSmallOreStat);
        }
    }
