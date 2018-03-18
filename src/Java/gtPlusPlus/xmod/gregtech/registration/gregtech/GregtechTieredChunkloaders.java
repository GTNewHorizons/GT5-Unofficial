package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;

public class GregtechTieredChunkloaders {
	public static void run() {
		if (LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Chunk Loaders.");
				run1();
		}

	}

	private static void run1() {
		int ID = 943;
		GregtechItemList.GT_Chunkloader_HV
		.set(new GregtechMetaTileEntityChunkLoader(ID++, "chunkloader.tier.01", "Chunkloader MK I", 3)
				.getStackForm(1L));
		GregtechItemList.GT_Chunkloader_EV
		.set(new GregtechMetaTileEntityChunkLoader(ID++, "chunkloader.tier.02", "Chunkloader MK II", 4)
				.getStackForm(1L));
		GregtechItemList.GT_Chunkloader_IV
		.set(new GregtechMetaTileEntityChunkLoader(ID++, "chunkloader.tier.03", "Chunkloader MK III", 5)
				.getStackForm(1L));
	}
}
