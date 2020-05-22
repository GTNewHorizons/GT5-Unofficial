package common.container;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import reactor.ButtonSlot;

public class Container_ModularNuclearReactor extends Container {

	private int nextSlotID = 0;
	private final Slot[] REACTOR_SLOTS = new Slot[54];
	private final Slot SLOT_CONFIGURATION;
	private final Slot BUTTON_EU_MODE;
	private final Slot BUTTON_FLUID_MODE;
	private final Slot BUTTON_CONDITION;
	private final Slot BUTTON_CONFIGURE;
	private final Slot BUTTON_RESET;

	
	public Container_ModularNuclearReactor(IGregTechTileEntity te, EntityPlayer player) {
				
		// Add the reactor chamber
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 6; y++){
				REACTOR_SLOTS[nextSlotID] = super.addSlotToContainer(new Slot(te, getNextSlotID(), (16 + 67 * x), (16 + 67 * y)));
			}
		}
		// Add the configuration slot
		SLOT_CONFIGURATION = super.addSlotToContainer(new Slot(te, getNextSlotID(), 0, 0));
		
		// Add buttons (they're also slots)
		BUTTON_EU_MODE = super.addSlotToContainer(new ButtonSlot(te, getNextSlotID(), 0, 0));	
		BUTTON_FLUID_MODE = super.addSlotToContainer(new ButtonSlot(te, getNextSlotID(), 0, 0));
		BUTTON_CONDITION = super.addSlotToContainer(new ButtonSlot(te, getNextSlotID(), 0, 0));
		BUTTON_CONFIGURE = super.addSlotToContainer(new ButtonSlot(te, getNextSlotID(), 0, 0));
		BUTTON_RESET = super.addSlotToContainer(new ButtonSlot(te, getNextSlotID(), 0, 0));
		
	}
	
	private int getNextSlotID() {
		nextSlotID++;
		return nextSlotID - 1;
	}

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotRaw) {
        ItemStack stack = null;
        final Slot slot = (Slot) inventorySlots.get(slotRaw);

        if (slot != null && slot.getHasStack()) {
            final ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (slotRaw < 3 * 9) {
                if (!mergeItemStack(stackInSlot, 3 * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(stackInSlot, 0, 3 * 9, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {		
		return true;
	}

}
