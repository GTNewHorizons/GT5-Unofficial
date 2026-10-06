package gregtech.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.hazards.ItemHazards;
import gregtech.api.util.GTUtility;

/// Applies the [ItemHazards] of everything a survival player carries, in the main inventory and the armor slots,
/// every [#INTERVAL] ticks: radiation per stack, heat or frost per stack, and one poison effect when any stack is
/// toxic. Poison outlasts the interval, so it stays on while a toxic stack is carried.
public class MaterialHazardHandler {

    private static final int INTERVAL = 120;
    private static final int POISON_TICKS = INTERVAL + 20;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase != TickEvent.Phase.START || !event.side.isServer()
            || player.isDead
            || player.capabilities.isCreativeMode
            || player.ticksExisted % INTERVAL != 0) return;
        boolean toxic = false;
        for (ItemStack stack : player.inventory.mainInventory) toxic |= applyStackHazards(player, stack);
        for (ItemStack stack : player.inventory.armorInventory) toxic |= applyStackHazards(player, stack);
        if (toxic && !HazardProtection.isWearingFullBioHazmat(player)) {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_TICKS, 4));
        }
    }

    /// Applies `stack`'s radiation and heat to `player`, and returns whether it is toxic.
    private static boolean applyStackHazards(EntityPlayer player, ItemStack stack) {
        if (stack == null) return false;
        GTUtility.applyRadioactivity(player, ItemHazards.radiationLevel(stack), stack.stackSize);
        float heat = ItemHazards.heatDamage(stack);
        if (heat > 0) {
            GTUtility.applyHeatDamageFromItem(player, heat, stack);
        } else if (heat < 0) {
            GTUtility.applyFrostDamage(player, -heat);
        }
        return ItemHazards.isToxic(stack);
    }
}
