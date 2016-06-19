package miscutil.core.creative;

import miscutil.core.creative.tabs.MiscUtilCreativeTabBlock;
import miscutil.core.creative.tabs.MiscUtilCreativeTabMachines;
import miscutil.core.creative.tabs.MiscUtilCreativeTabMisc;
import miscutil.core.creative.tabs.MiscUtilCreativeTabTools;
import miscutil.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;

public class AddToCreativeTab {

	public static CreativeTabs tabBlock;
	public static CreativeTabs tabMisc;
	public static CreativeTabs tabCombat;
	public static CreativeTabs tabTools;
	public static CreativeTabs tabMachines;

	public static void initialiseTabs(){
		tabBlock = new MiscUtilCreativeTabBlock("MiscUtilBlockTab");
		tabMisc = new MiscUtilCreativeTabMisc("MiscUtilMiscTab");
		tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");

		if (CORE.DEBUG){
			//tabCombat = new MiscUtilCreativeTabCombat("MiscUtilCombatTab");
		}
	}
}
