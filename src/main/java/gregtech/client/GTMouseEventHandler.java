package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import com.gtnewhorizon.gtnhlib.event.PickBlockEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.item.IMiddleClickItem;
import gregtech.api.items.MetaBaseItem;
import gregtech.crossmod.backhand.Backhand;

public final class GTMouseEventHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLeftClickEvent(MouseEvent event) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        /*
         * Minecraft defines the mouse buttons as follows:
         * -100: Left mouse button
         * -99: Right mouse button
         * -98: Middle mouse button
         * And the event defines the mouse buttons:
         * 0 = Left click
         * 1 = Right click
         * 2 = Middle click
         * So you can align the two with simple addition.
         */
        final int buttonPressed = -100 + event.button;

        if (player == null || player.isDead
            || !event.buttonstate
            || buttonPressed != Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode()) {
            return;
        }

        ItemStack heldItemStack = player.getHeldItem();
        if (isInvalid(heldItemStack)) {
            return;
        }

        if (heldItemStack.getItem() instanceof final MetaBaseItem mbItem) {
            event.setCanceled(mbItem.onLeftClick(heldItemStack, player));
        }
    }

    @SubscribeEvent
    public void onPickBlockEvent(PickBlockEvent event) {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player == null || player.isDead) {
            return;
        }

        for (final ItemStack stack : new ItemStack[]{player.getHeldItem(), Backhand.getOffhandItem(player)}) {
            if (!isInvalid(stack)) {
                final Item item = stack.getItem();
                boolean found = false;

                if (item instanceof final IMiddleClickItem mcItem) {
                    found = mcItem.onMiddleClick(stack, player);
                } else if (item instanceof final MetaBaseItem mbItem) {
                    found = mbItem.onMiddleClick(stack, player);
                }

                if (found) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    private static boolean isInvalid(final ItemStack heldItem) {
        return heldItem == null
            || !(heldItem.getItem() instanceof MetaBaseItem || heldItem.getItem() instanceof IMiddleClickItem);
    }
}
