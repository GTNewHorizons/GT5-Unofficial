package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IsaMill;

public class GregtechIsaMill {

	public static void run(){
		
		GregtechItemList.Controller_IsaMill_Controller.set(new GregtechMetaTileEntity_IsaMill(31027, "gtpp.multimachine.isamill", "IsaMill Grinding Machine").getStackForm(1L));
		
	}
	
}
