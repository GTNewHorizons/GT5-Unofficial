package gtPlusPlus.xmod.forestry.bees.alveary;

import forestry.api.apiculture.DefaultBeeListener;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.InventoryFrameHousing;

public class AlvearyBeeListener extends DefaultBeeListener {
	private final IAlvearyFrameHousing apiary;

	public AlvearyBeeListener(final IAlvearyFrameHousing apiary) {
		this.apiary = apiary;
	}

	@Override
	public void wearOutEquipment(final int amount) {
		final InventoryFrameHousing apiaryInventory = this.apiary.getAlvearyInventory();
		apiaryInventory.wearOutFrames(this.apiary, amount);
	}
}
