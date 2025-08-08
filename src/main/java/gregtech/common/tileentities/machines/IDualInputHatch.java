package gregtech.common.tileentities.machines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IDualInputHatch {

    /**
     * @return {@code true} if there is new items and/or fluids inserted to the inventories, and it will trigger the
     *         recipe check.
     */
    boolean justUpdated();

    Iterator<? extends IDualInputInventory> inventories();

    void updateTexture(int id);

    void updateCraftingIcon(ItemStack icon);

    Optional<IDualInputInventory> getFirstNonEmptyInventory();

    boolean supportsFluids();

    /**
     * Get the shared items in the inventory. Commonly, in the extra or manual slots, providing like Molds.
     *
     * @return the shared items in the inventory
     */
    ItemStack[] getSharedItems();

    default ItemStack[] getAllItems() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (ItemStack item : getSharedItems()) {
            if (item != null) {
                rList.add(item);
            }
        }
        Iterator<? extends IDualInputInventory> inventoryIterator = this.inventories();
        while (inventoryIterator.hasNext()) {
            ItemStack[] items = inventoryIterator.next()
                .getItemInputs();
            if (items == null) {
                continue;
            }
            for (ItemStack item : items) {
                if (item != null) {
                    rList.add(item);
                }
            }
        }
        return rList.toArray(new ItemStack[rList.size()]);
    }

    default FluidStack[] getAllFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        Iterator<? extends IDualInputInventory> inventoryIterator = this.inventories();
        while (inventoryIterator.hasNext()) {
            FluidStack[] fluids = inventoryIterator.next()
                .getFluidInputs();
            if (fluids == null) {
                continue;
            }
            for (FluidStack fluid : fluids) {
                if (fluid != null) {
                    rList.add(fluid);
                }
            }
        }
        return rList.toArray(new FluidStack[rList.size()]);

    };
}
