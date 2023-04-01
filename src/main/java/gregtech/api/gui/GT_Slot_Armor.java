package gregtech.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_Armor extends Slot {

    final int mArmorType;
    final EntityPlayer mPlayer;

    public GT_Slot_Armor(IInventory inventory, int slotIndex, int xPos, int yPos, int armorType, EntityPlayer aPlayer) {
        super(inventory, slotIndex, xPos, yPos);
        mArmorType = armorType;
        mPlayer = aPlayer;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack aStack) {
        return aStack != null && aStack.getItem() != null
                && aStack.getItem()
                         .isValidArmor(aStack, mArmorType, mPlayer);
    }
}
