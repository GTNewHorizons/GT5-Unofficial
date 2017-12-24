package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.*;

public class GregtechPollutionDevices {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Anti-Pollution Devices.");
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				run1();
			}
		}
	}

	private static void run1() {
		if (CORE.ConfigSwitches.enableMachine_Pollution){
			// 759
			GregtechItemList.Pollution_Detector.set(
					new GregtechMetaPollutionDetector(756, "pollutiondetector.01.tier.single", "Pollution Detection Device",
							0, "Tells you if you're living in Gwalior yet.", 0).getStackForm(1L));
			GregtechItemList.Pollution_Creator.set(new GregtechMetaPollutionCreator(951, "pollutioncreator.01.tier.single",
					"Smog Device", 4, "Polluting the skies.", 0).getStackForm(1L));

			GregtechItemList.Pollution_Cleaner_ULV.set(new GregtechMetaAtmosphericReconditioner(757,
					"pollutioncleaner.01.tier.single", "Pollution Scrubber", 0).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_LV.set(new GregtechMetaAtmosphericReconditioner(758,
					"pollutioncleaner.02.tier.single", "Upgraded Pollution Scrubber", 1).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_MV.set(new GregtechMetaAtmosphericReconditioner(759,
					"pollutioncleaner.03.tier.single", "Advanced Pollution Scrubber", 2).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_HV.set(new GregtechMetaAtmosphericReconditioner(760,
					"pollutioncleaner.04.tier.single", "Precision Pollution Scrubber", 3).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_EV.set(new GregtechMetaAtmosphericReconditioner(761,
					"pollutioncleaner.05.tier.single", "Air Recycler", 4).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_IV.set(new GregtechMetaAtmosphericReconditioner(762,
					"pollutioncleaner.06.tier.single", "Upgraded Air Recycler", 5).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_LuV.set(new GregtechMetaAtmosphericReconditioner(763,
					"pollutioncleaner.07.tier.single", "Advanced Air Recycler", 6).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_ZPM.set(new GregtechMetaAtmosphericReconditioner(764,
					"pollutioncleaner.08.tier.single", "Precision Air Recycler", 7).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_UV.set(new GregtechMetaAtmosphericReconditioner(765,
					"pollutioncleaner.09.tier.single", "Atmospheric Cleaner", 8).getStackForm(1L));
			GregtechItemList.Pollution_Cleaner_MAX.set(new GregtechMetaAtmosphericReconditioner(766,
					"pollutioncleaner.10.tier.single", "Biosphere Cleanser", 9).getStackForm(1L));
		}
	}

}
