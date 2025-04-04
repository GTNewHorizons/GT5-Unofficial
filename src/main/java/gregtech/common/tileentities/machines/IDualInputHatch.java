package gregtech.common.tileentities.machines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.logic.ProcessingLogic;

public interface IDualInputHatch {

    boolean justUpdated();

    Iterator<? extends IDualInputInventory> inventories();

    void updateTexture(int id);

    void updateCraftingIcon(ItemStack icon);

    Optional<IDualInputInventory> getFirstNonEmptyInventory();

    boolean supportsFluids();

    ItemStack[] getSharedItems();

    void setProcessingLogic(ProcessingLogic pl);

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
