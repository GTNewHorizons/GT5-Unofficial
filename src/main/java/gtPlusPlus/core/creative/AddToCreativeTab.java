package gtPlusPlus.core.creative;

import net.minecraft.creativetab.CreativeTabs;
import gregtech.api.util.GT_CreativeTab;
import gtPlusPlus.core.creative.tabs.*;
import gtPlusPlus.xmod.bop.creative.MiscUtilsBOPTab;

public class AddToCreativeTab {

	public static CreativeTabs tabBlock;
	public static CreativeTabs tabMisc;
	public static CreativeTabs tabCombat;
	public static CreativeTabs tabTools;
	public static CreativeTabs tabMachines;
	public static CreativeTabs tabOther;
	public static CreativeTabs tabBOP;

	public static void initialiseTabs() {
		//GT_CreativeTab
		/*tabBlock = new MiscUtilCreativeTabBlock("MiscUtilBlockTab");
		tabMisc = new MiscUtilCreativeTabMisc("MiscUtilMiscTab");
		tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");
		tabOther = new MiscUtilCreativeTabOther("MiscUtilOtherTab");
		tabBOP = new MiscUtilsBOPTab("MiscUtilBOP");*/
		
		tabBlock = new GT_CreativeTab("GTPP_BLOCKS", "GT++ Blocks");
		tabMisc = new GT_CreativeTab("GTPP_MISC", "GT++ Misc");
		tabTools = new GT_CreativeTab("GTPP_TOOLS", "GT++ Tools");
		tabMachines = new GT_CreativeTab("GTPP_MACHINES", "GT++ Machines");
		tabOther = new GT_CreativeTab("GTPP_OTHER", "GT++ Other");
		tabBOP = new GT_CreativeTab("GTPP_OTHER_2", "GT++ Other II");
		
	}
}
