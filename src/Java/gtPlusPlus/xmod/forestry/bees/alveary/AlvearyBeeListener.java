package gtPlusPlus.xmod.forestry.bees.alveary;

import forestry.api.apiculture.DefaultBeeListener;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.InventoryFrameHousing;

public class AlvearyBeeListener extends DefaultBeeListener {
	private final IAlvearyFrameHousing apiary;

	public AlvearyBeeListener(IAlvearyFrameHousing apiary) {
		this.apiary = apiary;
	}

	@Override
	public void wearOutEquipment(int amount) {
		InventoryFrameHousing apiaryInventory = apiary.getAlvearyInventory();
		apiaryInventory.wearOutFrames(apiary, amount);
	}
}
