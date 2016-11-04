package gtPlusPlus.xmod.forestry.bees.alveary;

import forestry.api.apiculture.IBeeHousing;
import net.minecraft.inventory.ISidedInventory;

public abstract interface ISidedFrameWearingInventory extends ISidedInventory {
	public abstract void wearOutFrames(IBeeHousing paramIBeeHousing, int paramInt);
}