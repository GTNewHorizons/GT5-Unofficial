package gtPlusPlus.core.handler;

import cpw.mods.fml.common.IFuelHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BurnableFuelHandler implements IFuelHandler{

	@Override
	public int getBurnTime(ItemStack aStack) {
		String Modid = ItemUtils.getModId(aStack);
			//Iterate over my burnables.
			for (Pair<Integer, ItemStack> temp : CORE.burnables) {
				
				int aStackID = Item.getIdFromItem(aStack.getItem());
				int burnID = Item.getIdFromItem(temp.getValue().getItem());
				//Utils.LOG_INFO("[Fuel Handler] ["+(aStackID == burnID)+"] Trying to look for a burnvalue for "+aStack.getDisplayName()+" | "+Modid+" | aStackID="+aStackID+" | burnID="+burnID);
				if (aStackID == burnID){
					Utils.LOG_INFO("[Fuel Handler] match found!");
					int burn = temp.getKey();
					ItemStack fuel = temp.getValue();
					ItemStack testItem = ItemUtils.getSimpleStack(fuel, aStack.stackSize);

					if (aStack.isItemEqual(testItem)){
						Utils.LOG_INFO("[Fuel Handler] Found "+fuel.getDisplayName()+" with a burntime of "+burn);
						return burn;
					}
				}
			}
		
		//If it's not my fuel, return 0.
		return 0;
	}

}
