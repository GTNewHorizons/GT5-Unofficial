package gregtech.client.capes;

import java.util.Map;
import java.util.UUID;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.mixin.interfaces.accessors.AbstractClientPlayerAccessor;

public class GTCapesEventHandler {

    private final Map<UUID, ResourceLocation> capeMaps;

    public GTCapesEventHandler(Map<UUID, ResourceLocation> map) {
        capeMaps = map;
    }

    @SubscribeEvent
    public void onPlayerSpawn(EntityJoinWorldEvent event) {
        if (event.world.isRemote && event.entity instanceof AbstractClientPlayer player && !player.func_152122_n()) {
            if (event.entity instanceof AbstractClientPlayerAccessor accessor) {
                accessor.gt5u$setCape(capeMaps.get(player.getUniqueID()));
            } else {
                // the mixin didn't inject
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

}
