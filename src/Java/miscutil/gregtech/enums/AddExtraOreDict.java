package miscutil.gregtech.enums;

import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.preload.GT_Loader_OreDictionary;
import miscutil.gregtech.init.machines.GregtechEnergyBuffer;
import net.minecraft.item.ItemStack;

public class AddExtraOreDict extends GT_Loader_OreDictionary {

	@Override
	public void run()
	  {
		GT_OreDictUnificator.registerOre(ExtraOreDictNames.buffer_core, new ItemStack(GregtechEnergyBuffer.itemBufferCore));
	  }
	
}
