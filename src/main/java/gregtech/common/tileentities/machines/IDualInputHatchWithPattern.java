package gregtech.common.tileentities.machines;

import java.util.Iterator;

public interface IDualInputHatchWithPattern extends IDualInputHatch {

    @Override
    Iterator<? extends IDualInputInventoryWithPattern> inventories();

}
