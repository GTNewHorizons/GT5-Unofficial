package gtPlusPlus.xmod.forestry.bees.alveary;

import forestry.api.apiculture.IBeeHousing;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.InventoryFrameHousing;

public interface IAlvearyFrameHousing extends IBeeHousing {
	InventoryFrameHousing getAlvearyInventory();
	void wearOutFrames(IBeeHousing beeHousing, int amount);
}
