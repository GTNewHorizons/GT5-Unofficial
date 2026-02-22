package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.MetaBaseItem;
import gregtech.crossmod.backhand.Backhand;

public final class GTMouseEventHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onMouseEvent(MouseEvent event) {
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
            || buttonPressed == Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode()) {
            return;
        }

        ItemStack heldItemStack = player.getHeldItem();
        ItemStack offhandItemStack = null;
        final boolean isOffhand;

        if (isInvalid(heldItemStack)) {
            heldItemStack = Backhand.getOffhandItem(player);

            if (isInvalid(heldItemStack)) {
                return;
            }
            isOffhand = true;
        } else {
            isOffhand = false;
        }

        final MetaBaseItem mbItem = (MetaBaseItem) heldItemStack.getItem();
        if (mbItem == null) {
            return;
        }

        if (buttonPressed == Minecraft.getMinecraft().gameSettings.keyBindPickBlock.getKeyCode()) {
            event.setCanceled(mbItem.onMiddleClick(heldItemStack, player));
        } else if (!isOffhand && buttonPressed == Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode()) {
            event.setCanceled(mbItem.onLeftClick(heldItemStack, player));
        }
    }

    private static boolean isInvalid(final ItemStack heldItem) {
        return heldItem == null || !(heldItem.getItem() instanceof MetaBaseItem);
    }
}
