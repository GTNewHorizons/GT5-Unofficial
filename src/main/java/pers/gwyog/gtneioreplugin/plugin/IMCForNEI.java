package pers.gwyog.gtneioreplugin.plugin;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;

public class IMCForNEI {

    public static void IMCSender() {
        // Though these 2 are already registered in NEI jar, we need to re-register
        // because new DimensionDisplayItems made tabs a bit taller.
        sendHandler("pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5VeinStat", "gregtech:gt.blockores:386");

        sendHandler("pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5SmallOreStat", "gregtech:gt.blockores:85");

        sendHandler(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.metaitem.01:32619");
        sendCatalyst(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.blockmachines:1157");
        sendCatalyst(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.blockmachines:141");
        sendCatalyst(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.blockmachines:142");
        sendCatalyst(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.blockmachines:149");
        sendCatalyst(
                "pers.gwyog.gtneioreplugin.plugin.gregtech5.PluginGT5UndergroundFluid",
                "gregtech:gt.blockmachines:148");
    }

    private static void sendHandler(String name, String itemStack) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", name);
        aNBT.setString("modName", GTNEIOrePlugin.NAME);
        aNBT.setString("modId", GTNEIOrePlugin.MODID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", itemStack);
        aNBT.setInteger("handlerHeight", 160);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 2);
        aNBT.setInteger("yShift", 0);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }

    private static void sendCatalyst(String name, String itemStack, int priority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", name);
        aNBT.setString("itemName", itemStack);
        aNBT.setInteger("priority", priority);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String name, String itemStack) {
        sendCatalyst(name, itemStack, 0);
    }
}
