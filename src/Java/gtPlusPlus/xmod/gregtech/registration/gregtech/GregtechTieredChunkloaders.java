package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredTank;

public class GregtechTieredChunkloaders {
	public static void run() {
		if (LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Portable Fluid Tanks.");
				run1();
		}

	}

	private static void run1() {
		int ID = 945;
		GregtechItemList.GT_Chunkloader_HV
				.set(new GregtechMetaTileEntityChunkLoader(ID++, "chunkloader.tier.00", "Chunkloader MK I", 4)
						.getStackForm(1L));
	}
}
