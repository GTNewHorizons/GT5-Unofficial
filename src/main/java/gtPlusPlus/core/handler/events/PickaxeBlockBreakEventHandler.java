package gtPlusPlus.core.handler.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PickaxeBlockBreakEventHandler {

    @SubscribeEvent
    public void onPlayerInteraction(final PlayerInteractEvent aEvent) {
        if ((aEvent.entityPlayer != null) && (aEvent.entityPlayer.worldObj != null)
                && (aEvent.action != null)
                && (aEvent.world.provider != null)
                && !aEvent.entityPlayer.worldObj.isRemote
                && (aEvent.action != null)
                && (aEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
            // Utils.LOG_ERROR("Test");
        }
    }
}
