package miscutil.core.creative;

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
		//tabCombat = new MiscUtilCreativeTabCombat("MiscUtilCombatTab");
		tabTools = new MiscUtilCreativeTabTools("MiscUtilToolsTab");
		if (CORE.DEBUG){
			tabMachines = new MiscUtilCreativeTabMachines("MiscUtilMachineTab");			
		}
	}
}
