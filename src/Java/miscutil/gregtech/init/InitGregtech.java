package miscutil.gregtech.init;

import miscutil.core.lib.Strings;
import miscutil.gregtech.init.machines.GregtechEnergyBuffer;
import cpw.mods.fml.common.FMLLog;

public class InitGregtech {

	public static void run() {
		if (Strings.GREGTECH) {
			FMLLog.info("MiscUtils|Gregtech5u Content: Registering MetaTileEntities.");
		}

		/***
		 * Load up Blocks classes
		 ***/

		// Machines
		// GregtechCobbleGenerator.run(); TODO - Weird Textures
		GregtechEnergyBuffer.run();

	}
}
