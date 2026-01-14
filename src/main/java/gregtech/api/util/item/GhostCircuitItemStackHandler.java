package gregtech.api.util.item;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemIntegratedCircuit;

/**
 * Inventory handler dedicated for {@link gregtech.common.modularui2.sync.GhostCircuitSyncHandler
 * GhostCircuitSyncHandler} and {@link gregtech.common.modularui2.widget.GhostCircuitSlotWidget GhostCircuitSlotWidget}.
 */
public class GhostCircuitItemStackHandler implements IItemHandlerModifiable {

    public static final int NO_CONFIG = -1;

    private final IItemHandlerModifiable inventory;
    private final int circuitSlot;

    public GhostCircuitItemStackHandler(IMetaTileEntity mte) {
        if (!(mte instanceof IConfigurationCircuitSupport ccs)) {
            throw new IllegalArgumentException(mte + " does not implement IConfigurationCircuitSupport");
        }
        this.inventory = mte.getInventoryHandler();
        this.circuitSlot = ccs.getCircuitSlot();
    }

    /**
     * Returns whether circuit item is present.
     */
    public boolean hasCircuit() {
        return inventory.getStackInSlot(circuitSlot) != null;
    }

    /**
     * Gets current configuration the circuit item is set to. If circuit is not present, returns {@link #NO_CONFIG}.
     */
    public int getCircuitConfig() {
        if (hasCircuit()) {
            return inventory.getStackInSlot(circuitSlot)
                .getItemDamage();
        }
        return NO_CONFIG;
    }

    /**
     * Sets the configuration of the circuit. Accepts {@link #NO_CONFIG}.
     */
    public void setCircuitConfig(int config) {
        if (config == NO_CONFIG) {
            inventory.setStackInSlot(circuitSlot, null);
        } else if (config >= 1 && config <= ItemIntegratedCircuit.MAX_CIRCUIT_NUMBER) {
            inventory.setStackInSlot(circuitSlot, GTUtility.getIntegratedCircuit(config));
        } else {
            throw new IllegalArgumentException("Invalid circuit config: " + config);
        }
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack) {
        validateSlot(slot);
        if (isItemValid(slot, stack)) {
            if (stack != null) {
                stack.stackSize = 0;
            }
            inventory.setStackInSlot(circuitSlot, stack);
        }
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlot(slot);
        return inventory.getStackInSlot(circuitSlot);
    }

    @Nullable
    @Override
    public ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
        validateSlot(slot);
        return stack;
    }

    @Nullable
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        validateSlot(slot);
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        validateSlot(slot);
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        validateSlot(slot);
        return stack == null || stack.getItem() instanceof ItemIntegratedCircuit;
    }

    private void validateSlot(int slot) {
        if (slot != 0) throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
    }
}
