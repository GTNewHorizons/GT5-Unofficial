package gtPlusPlus.core.creative;

import gtPlusPlus.core.creative.tabs.*;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;

public class AddToCreativeTab {

	public static CreativeTabs	tabBlock;
	public static CreativeTabs	tabMisc;
	public static CreativeTabs	tabCombat;
	public static CreativeTabs	tabTools;
	public static CreativeTabs	tabMachines;
	public static CreativeTabs	tabOther;

	public static void initialiseTabs() {
		AddToCreativeTab.tabBlock = new MiscUtilCreativeTabBlock("MiscUtilBlockTab");
		AddToCreativeTab.tabMisc = new MiscUtilCreativeTabMisc("MiscUtilMiscTab");
		AddToCreativeTab.tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		AddToCreativeTab.tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");
		AddToCreativeTab.tabOther = new MiscUtilCreativeTabOther("MiscUtilOtherTab");

		if (CORE.DEBUG) {
			// tabCombat = new MiscUtilCreativeTabCombat("MiscUtilCombatTab");
		}
	}
}
