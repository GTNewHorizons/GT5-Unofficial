package gtPlusPlus.core.creative;

import net.minecraft.creativetab.CreativeTabs;

import gregtech.api.util.GTCreativeTab;

public class AddToCreativeTab {

    public static CreativeTabs tabBlock;
    public static CreativeTabs tabMisc;
    public static CreativeTabs tabTools;
    public static CreativeTabs tabMachines;
    public static CreativeTabs tabOther;
    public static CreativeTabs tabBOP;

    public static void initialiseTabs() {
        // GT_CreativeTab
        tabBlock = new GTCreativeTab("GTPP_BLOCKS", "GT++ Blocks");
        tabMisc = new GTCreativeTab("GTPP_MISC", "GT++ Misc");
        tabTools = new GTCreativeTab("GTPP_TOOLS", "GT++ Tools");
        tabMachines = new GTCreativeTab("GTPP_MACHINES", "GT++ Machines");
        tabOther = new GTCreativeTab("GTPP_OTHER", "GT++ Other");
        tabBOP = new GTCreativeTab("GTPP_OTHER_2", "GT++ Other II");
    }
}
