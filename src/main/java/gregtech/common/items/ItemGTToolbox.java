package gregtech.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.items.GTGenericItem;
import gregtech.common.gui.modularui.item.ItemToolboxGui;

public class ItemGTToolbox extends GTGenericItem implements IGuiHolder<PlayerInventoryGuiData> {

    public LimitingItemStackHandler handler = new LimitingItemStackHandler(9, 1);;

    public ItemGTToolbox(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new ItemToolboxGui(syncManager, data).build();
    }

    // TODO: doubt this works
    public ItemStack[] getInventory(ItemStack stack) {
        if (stack.getTagCompound() == null || stack.getTagCompound()
            .getCompoundTag("Items") == null) return null;
        ItemStack[] inventory = new ItemStack[this.getSizeInventory()];
        NBTTagCompound items = stack.getTagCompound()
            .getCompoundTag("Items");
        for (int i = 0; i < this.getSizeInventory(); i++) {
            inventory[i] = ItemStack.loadItemStackFromNBT(items);
        }
        return inventory;
    }

    public int getSizeInventory() {
        return 9;
    }

}
