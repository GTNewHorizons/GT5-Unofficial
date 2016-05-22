package miscutil.gregtech.api.init;

import static miscutil.core.lib.LoadedMods.Gregtech;
import miscutil.gregtech.api.init.machines.GregtechConduits;
import miscutil.gregtech.api.init.machines.GregtechEnergyBuffer;
import miscutil.gregtech.api.init.machines.GregtechIndustrialCentrifuge;
import miscutil.gregtech.api.init.machines.GregtechIndustrialCokeOven;
import miscutil.gregtech.api.init.machines.GregtechSafeBlock;
import miscutil.gregtech.api.init.machines.GregtechSteamCondenser;

public class InitGregtech {

	public static void run() {
		if (Gregtech) {
		GregtechEnergyBuffer.run();
		GregtechConduits.run();
		GregtechSteamCondenser.run();
		GregtechSafeBlock.run();
		GregtechIndustrialCentrifuge.run();
		GregtechIndustrialCokeOven.run();
		}

	}
}
