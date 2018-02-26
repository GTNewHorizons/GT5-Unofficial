package gtPlusPlus.core.creative;

import net.minecraft.creativetab.CreativeTabs;

import gtPlusPlus.core.creative.tabs.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
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
		tabBlock = new MiscUtilCreativeTabBlock("MiscUtilBlockTab");
		tabMisc = new MiscUtilCreativeTabMisc("MiscUtilMiscTab");
		tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");
		tabOther = new MiscUtilCreativeTabOther("MiscUtilOtherTab");
		if (LoadedMods.BiomesOPlenty) {
			tabBOP = new MiscUtilsBOPTab("MiscUtilBOP");
		}

		if (CORE.DEBUG) {
			// tabCombat = new MiscUtilCreativeTabCombat("MiscUtilCombatTab");
		}
	}
}
