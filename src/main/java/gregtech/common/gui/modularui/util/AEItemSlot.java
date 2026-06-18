package gregtech.common.gui.modularui.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.widget.IGuiAction.MousePressed;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;

import appeng.api.storage.data.IAEItemStack;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import gregtech.api.util.GTUtility;
import gregtech.common.inventory.AEInventory;

public class AEItemSlot extends ItemSlot {

    private final AEInventory inv;
    private final int slot;
    private boolean dumpable;

    public AEItemSlot(PanelSyncManager syncManager, String slotGroup, AEInventory inv, int slot) {
        this.inv = inv;
        this.slot = slot;

        slot(new AEModularSlot(inv, slot).slotGroup(slotGroup));

        itemTooltip().tooltipBuilder(tooltip -> {
            IAEItemStack stack = inv.getAEStackInSlot(slot);

            if (stack != null) {
                tooltip.add(
                    GTUtility.translate("GT5U.gui.text.amount_out_of", stack.getStackSize(), inv.getAESlotLimit(slot)));
                tooltip.newLine();
                if (dumpable) {
                    tooltip.add(EnumChatFormatting.BLUE + "Hold ALT and click slot to eject it into a matter cluster");
                }
            }
        });

        syncManager.registerSyncedAction(slotGroup + "." + slot + ".dump", packet -> {
            if (!dumpable) return;

            EntityPlayer player = syncManager.getContainer()
                .getPlayer();

            if (player.worldObj.isRemote) return;

            ItemStack split = inv.extractItem(slot, Integer.MAX_VALUE, false, true);

            if (ItemUtil.isStackEmpty(split)) return;

            ItemStack cluster = ItemMatterCluster.makeCluster(split);

            player.inventory.addItemStackToInventory(cluster);

            if (cluster.stackSize > 0) {
                player.worldObj.spawnEntityInWorld(
                    new EntityItem(player.worldObj, player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, cluster));
            }
        });

        listenGuiAction((MousePressed) mouseButton -> {
            if (!dumpable) return false;

            if (AEItemSlot.this.isHovering() && Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
                syncManager.callSyncedAction(slotGroup + "." + slot + ".dump", buffer -> {});

                return true;
            }

            return false;
        });
    }

    public AEItemSlot setDumpable(boolean dumpable) {
        this.dumpable = dumpable;
        return this;
    }

    @Override
    protected void drawSlotAmountText(int amount, String format) {
        IAEItemStack stack = inv.getAEStackInSlot(slot);
        GuiDraw.drawStandardSlotAmountText(stack == null ? 0L : stack.getStackSize(), format, getArea());
    }

    private static class AEModularSlot extends ModularSlot {

        public AEModularSlot(IItemHandler itemHandler, int index) {
            super(itemHandler, index);
            accessibility(false, false);
        }

        @Override
        public void putStack(ItemStack stack) {
            // no-op to disable MC's slot syncing, which truncates >int max stack sizes
        }
    }
}
