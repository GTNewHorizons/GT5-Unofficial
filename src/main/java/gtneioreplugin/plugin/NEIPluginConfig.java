package gtneioreplugin.plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StoneType;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.GTNEIOrePlugin;
import gtneioreplugin.plugin.gregtech5.PluginGT5SmallOreStat;
import gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid;
import gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat;

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

        List<ItemList> catalysts = Arrays.asList(
            ItemList.OilDrill1,
            ItemList.OilDrill2,
            ItemList.OilDrill3,
            ItemList.OilDrill4,
            ItemList.OilDrillInfinite);
        for (ItemList catalyst : catalysts) {
            API.addRecipeCatalyst(catalyst.get(1), pluginGT5UndergroundFluid);
        }
    }

    @SubscribeEvent
    public void registerHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        // Though first two handlers are already registered in NEI jar, we need to re-register
        // because new DimensionDisplayItems made tabs a bit taller.
        Map<String, ItemStack> handlers = new HashMap<>();

        OreInfo<Materials> info = OreInfo.getNewInfo();

        info.stoneType = StoneType.Stone;
        info.material = Materials.Manyullyn;

        handlers.put("PluginGT5VeinStat", OreManager.getStack(info, 1));

        info.material = Materials.Platinum;
        info.isSmall = true;

        handlers.put("PluginGT5SmallOreStat", OreManager.getStack(info, 1));

        info.release();

        handlers.put("PluginGT5UndergroundFluid", ItemList.Electric_Pump_UEV.get(1));
        for (Map.Entry<String, ItemStack> handler : handlers.entrySet()) {
            event.registerHandlerInfo(
                new HandlerInfo.Builder(
                    "gtneioreplugin.plugin.gregtech5." + handler.getKey(),
                    GTNEIOrePlugin.NAME,
                    GTNEIOrePlugin.MODID).setHeight(160)
                        .setMaxRecipesPerPage(2)
                        .setDisplayStack(handler.getValue())
                        .build());
        }
    }
}
