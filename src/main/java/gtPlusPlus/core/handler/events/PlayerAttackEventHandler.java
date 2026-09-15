package gtPlusPlus.core.handler.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.item.bauble.BaseBauble;

public class PlayerAttackEventHandler {

    @Optional.Method(modid = Mods.ModIDs.BAUBLES)
    @SubscribeEvent
    public void onPlayerAttacked(LivingAttackEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer player)) {
            return;
        }
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
        if (baubles == null) {
            return;
        }
        final ItemStack bauble1 = baubles.getStackInSlot(1);
        if (bauble1 != null && bauble1.getItem() instanceof BaseBauble gtBauble
            && gtBauble.getDamageNegations()
                .contains(event.source.damageType)) {
            event.setCanceled(true);
            return;
        }
        final ItemStack bauble2 = baubles.getStackInSlot(2);
        if (bauble2 != null && bauble2.getItem() instanceof BaseBauble gtBauble
            && gtBauble.getDamageNegations()
                .contains(event.source.damageType)) {
            event.setCanceled(true);
        }
    }
}
