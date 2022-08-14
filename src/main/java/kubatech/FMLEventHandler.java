package kubatech;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import kubatech.network.LoadConfigPacket;
import net.minecraft.entity.player.EntityPlayerMP;

public class FMLEventHandler {
    // Gets fired only server-sided
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;
        kubatech.info("Sending config to " + event.player.getDisplayName());
        kubatech.NETWORK.sendTo(LoadConfigPacket.instance, (EntityPlayerMP) event.player);
    }
}
