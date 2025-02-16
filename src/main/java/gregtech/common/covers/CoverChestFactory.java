package gregtech.common.covers;

import gregtech.api.covers.CoverFactoryBase;

public class CoverChestFactory extends CoverFactoryBase<CoverChest.ChestInventory> {

    private final int slots;
    private final int stackSizeLimit = 1;

    public CoverChestFactory(int slots) {
        this.slots = slots;
    }

    @Override
    public CoverChest.ChestInventory createDataObject() {
        return new CoverChest.ChestInventory(slots, stackSizeLimit);
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

}
