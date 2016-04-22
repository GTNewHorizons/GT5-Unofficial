package miscutil.gregtech.api.init;

import static miscutil.core.lib.LoadedMods.Gregtech;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.init.machines.GregtechConduits;
import miscutil.gregtech.api.init.machines.GregtechEnergyBuffer;
import miscutil.gregtech.api.init.machines.GregtechSafeBlock;
import miscutil.gregtech.api.init.machines.GregtechSteamCondenser;

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
		GregtechSteamCondenser.run();
		GregtechSafeBlock.run();

	}
}
