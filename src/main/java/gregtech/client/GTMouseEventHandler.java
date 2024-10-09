package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.MetaBaseItem;

@EventBusSubscriber(side = Side.CLIENT)
public class GTMouseEventHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onMouseEvent(MouseEvent event) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player == null || player.isDead) {
            return;
        }

        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) {
            return;
        }

        if (event.button == 0 && event.buttonstate && heldItem.getItem() instanceof MetaBaseItem mbItem) {
            event.setCanceled(mbItem.onLeftClick(heldItem, player));
        }

        if (event.button == 2 && event.buttonstate && heldItem.getItem() instanceof MetaBaseItem mbItem) {
            event.setCanceled(mbItem.onMiddleClick(heldItem, player));
        }
    }
}
