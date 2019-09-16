package gregtech.common.gui;

import gregtech.common.items.GT_VolumetricFlask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public final class GT_ContainerVolumetricFlask extends Container {
    ItemStack flask;

    public GT_ContainerVolumetricFlask(InventoryPlayer inventoryPlayer) {
        flask = inventoryPlayer.getCurrentItem();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return (flask != null) && (flask.stackSize > 0) && ((flask.getItem() instanceof GT_VolumetricFlask));
    }
}
