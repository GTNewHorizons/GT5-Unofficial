package gtPlusPlus.core.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;

public class SlotJukebox extends SlotGeneric {

	private final boolean isDisplay;
	

	public SlotJukebox(IInventory inventory, int x, int y, int z) {
		this(inventory, x, y, z, false);
	}
	
	public SlotJukebox(IInventory inventory, int x, int y, int z, boolean display) {
		super(inventory, x, y, z);
		isDisplay = display;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return (itemstack != null && itemstack.getItem() instanceof ItemRecord);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean canTakeStack(EntityPlayer p_82869_1_) {
		return !isDisplay;
	}

}
