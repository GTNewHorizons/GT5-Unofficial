package gregtech.common.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.items.MetaBaseItem;
import gregtech.crossmod.backhand.Backhand;

public class OffhandToolFunctionalityHandler {

    public OffhandToolFunctionalityHandler() {}

    @SubscribeEvent
    public void onPlaceEvent(PlaceEvent event) {
        final ItemStack offhand = Backhand.getOffhandItem(event.player);
        if (offhand != null && offhand.getItem() instanceof final MetaBaseItem item) {
            item.onBlockPlacedWhileWieldingOffhanded(event.blockSnapshot, offhand, event.player);
        }
    }
}
