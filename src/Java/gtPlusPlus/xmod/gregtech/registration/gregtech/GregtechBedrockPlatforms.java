package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.bedrock.GregtechMetaTileEntity_BedrockMiningPlatform1;

public class GregtechBedrockPlatforms {

	//941-945
	
	public static void run() {
		Logger.INFO("Gregtech5u Content | Registering Bedrock Mining Platform.");
		GregtechItemList.BedrockMiner_MKI.set(new GregtechMetaTileEntity_BedrockMiningPlatform1(941, "multimachine.tier.01.bedrockminer", "Experimental Deep Earth Drilling Platform - MK I").getStackForm(1));
	}

}
