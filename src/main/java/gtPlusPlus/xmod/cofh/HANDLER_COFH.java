package gtPlusPlus.xmod.cofh;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.RF2EU_Battery;
import gtPlusPlus.core.lib.LoadedMods;

public class HANDLER_COFH {

	public static void initItems() {
		if (LoadedMods.CoFHCore && (LoadedMods.IndustrialCraft2 || LoadedMods.IndustrialCraft2Classic)) {
			ModItems.RfEuBattery = new RF2EU_Battery();				
		}
	}
	
}
