package miscutil.gregtech.init;

import static miscutil.core.lib.LoadedMods.Gregtech;
import miscutil.core.util.Utils;
import miscutil.gregtech.init.machines.GregtechConduits;
import miscutil.gregtech.init.machines.GregtechEnergyBuffer;

public class InitGregtech {

	public static void run() {
		if (Gregtech) {
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Meta-TileEntities.");
		}

		
		
		/***
		 * Load up Blocks classes
		 ***/

		// Machines
		// GregtechCobbleGenerator.run(); TODO - Weird Textures
		GregtechEnergyBuffer.run();
		GregtechConduits.run();

	}
}
