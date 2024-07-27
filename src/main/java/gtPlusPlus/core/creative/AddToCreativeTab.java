package gtPlusPlus.core.creative;

import net.minecraft.creativetab.CreativeTabs;

import gregtech.api.util.GT_CreativeTab;

public class AddToCreativeTab {

    public static CreativeTabs tabBlock;
    public static CreativeTabs tabMisc;
    public static CreativeTabs tabTools;
    public static CreativeTabs tabMachines;
    public static CreativeTabs tabOther;
    public static CreativeTabs tabBOP;

    public static void initialiseTabs() {
        // GT_CreativeTab
        tabBlock = new GT_CreativeTab("GTPP_BLOCKS", "GT++ Blocks");
        tabMisc = new GT_CreativeTab("GTPP_MISC", "GT++ Misc");
        tabTools = new GT_CreativeTab("GTPP_TOOLS", "GT++ Tools");
        tabMachines = new GT_CreativeTab("GTPP_MACHINES", "GT++ Machines");
        tabOther = new GT_CreativeTab("GTPP_OTHER", "GT++ Other");
        tabBOP = new GT_CreativeTab("GTPP_OTHER_2", "GT++ Other II");
    }
}
