package gtPlusPlus.core.handler.events;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.potion.GtPotionEffect;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;

public class PlayerSleepEventHandler {

    private static Field sEffectDuration = ReflectionUtils
            .getField(PotionEffect.class, !CORE_Preloader.DEV_ENVIRONMENT ? "field_76460_b" : "duration");
    private static ArrayList<Potion> sPositiveEffects = new ArrayList<>();
    private static ArrayList<Potion> sNegativeEffects = new ArrayList<>();

    public static void init() {
        Utils.registerEvent(new PlayerSleepEventHandler());
        sPositiveEffects.add(Potion.moveSpeed);
        sPositiveEffects.add(Potion.waterBreathing);
        sPositiveEffects.add(Potion.resistance);
        sPositiveEffects.add(Potion.regeneration);
        sPositiveEffects.add(Potion.damageBoost);
        sPositiveEffects.add(Potion.digSpeed);
        sPositiveEffects.add(Potion.fireResistance);
        sPositiveEffects.add(Potion.field_76434_w); // Health Boost
        sPositiveEffects.add(Potion.field_76444_x); // Absorption
        sNegativeEffects.add(Potion.blindness);
        sNegativeEffects.add(Potion.confusion);
        sNegativeEffects.add(Potion.digSlowdown);
        sNegativeEffects.add(Potion.harm);
        sNegativeEffects.add(Potion.hunger);
        sNegativeEffects.add(Potion.moveSlowdown);
        sNegativeEffects.add(Potion.poison);
        sNegativeEffects.add(Potion.weakness);
        sNegativeEffects.add(Potion.wither);
    }

    @SubscribeEvent
    public void sleep(PlayerSleepInBedEvent event) {}

    @SubscribeEvent
    public void wake(PlayerWakeUpEvent event) {
        EntityPlayer aPlayer = event.entityPlayer;
        if (aPlayer != null && !aPlayer.worldObj.isRemote) {
            if (event.entityPlayer.getEntityWorld().getWorldTime() % 24000 != 0) {
                return;
            }
            boolean aRemovedBad = false;
            try {
                Collection<PotionEffect> aActive = aPlayer.getActivePotionEffects();
                for (PotionEffect aEffect : aActive) {
                    for (Potion aBadPotion : sNegativeEffects) {
                        if (aEffect.getPotionID() == aBadPotion.getId()) {
                            ReflectionUtils.setField(aEffect, sEffectDuration, 1);
                            aRemovedBad = true;
                            Logger.INFO("Set duration of " + aEffect.getEffectName() + " to 1 tick");
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (aRemovedBad) {
                messagePlayer(aPlayer, "sleep.event.downsides");
            } else {
                // Try Heal
                float aCurrentHP = aPlayer.getHealth();
                float aMaxHP = aPlayer.getMaxHealth();
                if (aCurrentHP < aMaxHP) {
                    float aDamage = aMaxHP - aCurrentHP;
                    float aToHeal = MathUtils.randFloat(1, aDamage);
                    if (aToHeal > 0) {
                        aPlayer.heal(aToHeal);
                        messagePlayer(aPlayer, (aToHeal >= aDamage / 2 ? "sleep.event.good" : "sleep.event.okay"));
                    }
                }
                // Already healed, try give a buff
                else {
                    int aRandomBuff = MathUtils.randInt(0, sPositiveEffects.size() - 1);
                    Potion aPotionToApply = sPositiveEffects.get(aRandomBuff);
                    if (aPotionToApply != null) {
                        aPlayer.addPotionEffect(
                                new GtPotionEffect(
                                        aPotionToApply.id,
                                        MathUtils.randInt(60, 180),
                                        MathUtils.randInt(0, 2)));
                        messagePlayer(aPlayer, "sleep.event.wellrested");
                    }
                }
            }
        }
    }

    private static void messagePlayer(EntityPlayer aPlayer, String aChatKey) {
        PlayerUtils.messagePlayer(aPlayer, new ChatComponentTranslation(aChatKey, new Object[0]));
    }
}
