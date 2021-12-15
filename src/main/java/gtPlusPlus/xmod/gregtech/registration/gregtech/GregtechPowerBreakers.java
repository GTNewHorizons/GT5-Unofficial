package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBreaker;

public class GregtechPowerBreakers {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Power Breakers.");			
			run1();
		}

	}

	private static void run1() {		
		//30200
		int aStartID = 30400;		
		GregtechItemList.BreakerBox_ULV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.00",
				"Ultra Low Voltage Breaker Box", 0, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_LV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.01",
				"Low Voltage Breaker Box", 1, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_MV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.02",
				"Medium Voltage Breaker Box", 2, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_HV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.03",
				"High Voltage Breaker Box", 3, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_EV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.04",
				"Extreme Voltage Breaker Box", 16, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_IV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.05",
				"Insane Voltage Breaker Box", 5, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_LuV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.06",
				"Ludicrous Voltage Breaker Box", 6, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_ZPM.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.07",
				"ZPM Voltage Breaker Box", 7, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_UV.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.08",
				"Ultimate Voltage Breaker Box", 8, "", 16)).getStackForm(1L));
		GregtechItemList.BreakerBox_MAX.set((new GT_MetaTileEntity_BasicBreaker(aStartID++, "breaker.tier.09",
				"MAX Voltage Breaker Box", 9, "", 16)).getStackForm(1L));

	}
}