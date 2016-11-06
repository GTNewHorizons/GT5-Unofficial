package gtPlusPlus.core.creative;

import gtPlusPlus.core.creative.tabs.MiscUtilCreativeTabBlock;
import gtPlusPlus.core.creative.tabs.MiscUtilCreativeTabMachines;
import gtPlusPlus.core.creative.tabs.MiscUtilCreativeTabMisc;
import gtPlusPlus.core.creative.tabs.MiscUtilCreativeTabOther;
import gtPlusPlus.core.creative.tabs.MiscUtilCreativeTabTools;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;

public class AddToCreativeTab {

	public static CreativeTabs tabBlock;
	public static CreativeTabs tabMisc;
	public static CreativeTabs tabCombat;
	public static CreativeTabs tabTools;
	public static CreativeTabs tabMachines;
	public static CreativeTabs tabOther;

	public static void initialiseTabs(){
		tabBlock = new MiscUtilCreativeTabBlock("MiscUtilBlockTab");
		tabMisc = new MiscUtilCreativeTabMisc("MiscUtilMiscTab");
		tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");
		tabOther = new MiscUtilCreativeTabOther("MiscUtilOtherTab");
		
		if (CORE.DEBUG){
			//tabCombat = new MiscUtilCreativeTabCombat("MiscUtilCombatTab");
		}
	}
}
