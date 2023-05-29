package gregtech.api.logic;

import java.util.UUID;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemInventoryLogic {
    
    protected String displayName;
    protected final IItemHandlerModifiable inventory;
    protected UUID connectedFluidInventory;

    public ItemInventoryLogic(int numberOfSlots) {
        this(new ItemStackHandler(numberOfSlots));
    }
    public ItemInventoryLogic(IItemHandlerModifiable inventory) {
        this.inventory = inventory;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getConnectedFluidInventory() {
        return connectedFluidInventory;
    }

    public void setConnectedFluidInventory(UUID connectedFluidTank) {
        this.connectedFluidInventory = connectedFluidTank;
    }

    public void saveToNBT(NBTTagCompound nbt) {
        
    }

    public void loadFromNBT(NBTTagCompound nbt) {

    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public ItemStack[] getStoredItems() {
        return inventory.getStacks().toArray(new ItemStack[0]);
    }
}
