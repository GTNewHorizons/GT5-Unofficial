package gtPlusPlus.core.handler.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.mixin.interfaces.PotionExt;
import gtPlusPlus.core.util.math.MathUtils;

public class PlayerSleepEventHandler {

    private static final ArrayList<Potion> potionBuffs = new ArrayList<>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerSleepEventHandler());
        potionBuffs.add(Potion.moveSpeed);
        potionBuffs.add(Potion.waterBreathing);
        potionBuffs.add(Potion.resistance);
        potionBuffs.add(Potion.regeneration);
        potionBuffs.add(Potion.damageBoost);
        potionBuffs.add(Potion.digSpeed);
        potionBuffs.add(Potion.fireResistance);
        potionBuffs.add(Potion.field_76434_w); // Health Boost
        potionBuffs.add(Potion.field_76444_x); // Absorption
        potionBuffs.trimToSize();
    }

    @SubscribeEvent
    public void onPlayerWakeUp(PlayerWakeUpEvent event) {
        EntityPlayer player = event.entityPlayer;
        if (player == null || player.worldObj.isRemote) return;
        if (player.getEntityWorld()
            .getWorldTime() % 24000 != 0) {
            return;
        }
        final List<Integer> potionToRemove = new ArrayList<>();
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
            if (potion instanceof PotionExt && ((PotionExt) potion).gt5u$isBadEffect()) {
                potionToRemove.add(potion.id);
            }
        }
        for (Integer i : potionToRemove) {
            player.removePotionEffect(i);
        }
        if (!potionToRemove.isEmpty()) {
            messagePlayer(player, "sleep.event.downsides");
            return;
        }
        // Try Heal
        float currentHP = player.getHealth();
        float maxHP = player.getMaxHealth();
        if (currentHP < maxHP) {
            float missingHP = maxHP - currentHP;
            float heal = MathUtils.randFloat(1, missingHP);
            player.heal(heal);
            messagePlayer(player, (heal >= missingHP / 2 ? "sleep.event.good" : "sleep.event.okay"));
            return;
        }
        // Try give a buff
        Potion aPotionToApply = potionBuffs.get(MathUtils.randInt(0, potionBuffs.size() - 1));
        player.addPotionEffect(
            new PotionEffect(aPotionToApply.id, MathUtils.randInt(60, 180) * 20, MathUtils.randInt(0, 2)));
        messagePlayer(player, "sleep.event.wellrested");
    }

    private static void messagePlayer(EntityPlayer player, String aChatKey) {
        player.addChatComponentMessage(new ChatComponentTranslation(aChatKey));
    }
}
