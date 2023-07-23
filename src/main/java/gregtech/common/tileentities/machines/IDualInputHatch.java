package gregtech.common.tileentities.machines;

import java.util.Iterator;

public interface IDualInputHatch {
    boolean justUpdated();
    Iterator<? extends IDualInputInventory> inventories();

}
