package vswe.stevescarts.ModuleData;

import java.util.HashMap;

import gtPlusPlus.xmod.sc2.modules.workers.tools.ModuleExoticFarmerUpgraded;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleData.SIDE;
import vswe.stevescarts.ModuleData.ModuleDataGroup;
import vswe.stevescarts.ModuleData.ModuleDataTool;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleDataLoader {	

	private static HashMap<Byte, ModuleData> moduleListEx;
	
	public static HashMap<Byte, ModuleData> getList() {
		return moduleListEx;
	}
	
	public static void load() {
		HashMap<Byte, ModuleData> u = ModuleData.getList();
		if (u.size() < Byte.MAX_VALUE) {
			int mNextFreeID = u.size()+1;
			final ModuleDataGroup farmerGroup = new ModuleDataGroup(vswe.stevescarts.Helpers.Localization.MODULE_INFO.FARMER_GROUP);
			final ModuleData farmerExotic = new ModuleDataTool(mNextFreeID, "Exotic Farmer",
					(Class<? extends ModuleBase>) ModuleExoticFarmerUpgraded.class, 75, true)
					.addSide(SIDE.FRONT);			
			farmerGroup.add(farmerExotic);			
		}
	}
}
