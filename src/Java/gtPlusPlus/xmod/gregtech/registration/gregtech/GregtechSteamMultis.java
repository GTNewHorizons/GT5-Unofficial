package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamMacerator;

public class GregtechSteamMultis {

	public static void run(){

		Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");
		
		GregtechItemList.Controller_SteamMaceratorMulti.set(new GregtechMetaTileEntity_SteamMacerator(31030, "gtpp.multimachine.steam.macerator", "Steam Grinder").getStackForm(1L));
		
		
	}
	
}
