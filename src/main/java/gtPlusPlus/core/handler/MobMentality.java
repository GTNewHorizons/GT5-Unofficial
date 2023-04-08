package gtPlusPlus.core.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class MobMentality {

    public static HashSet<Class<EntityLivingBase>> sIgnoredTypes = new HashSet<Class<EntityLivingBase>>();

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent event) {
        final EntityLivingBase target = event.entityLiving;
        for (Class<EntityLivingBase> aEntityClass : sIgnoredTypes) {
            if (aEntityClass.isInstance(target)) {
                return;
            }
        }
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase entity = target;
            final Entity attacker = event.source.getSourceOfDamage();
            if (
            /* this.configuration.shouldIgnoreNeutralMobs() && */ !(entity instanceof IMob)) {
                return;
            }
            if (attacker == null) {
                return;
            }
            if (attacker instanceof EntityLivingBase && !PlayerUtils.isRealPlayer((EntityLivingBase) attacker)) {
                return;
            }
            if (attacker instanceof EntityPlayer && PlayerUtils.isCreative((EntityPlayer) attacker)) {
                return;
            }
            if (attacker instanceof EntityLivingBase) {
                List<Entity> aEntityList = target.worldObj.loadedEntityList;
                List<EntityLivingBase> aRangedEntity = new ArrayList<EntityLivingBase>();
                for (Entity aEntity : aEntityList) {
                    if (target.getClass().isInstance(aEntity)) {
                        if (EntityUtils.getDistance(target, aEntity) <= 32) {
                            aRangedEntity.add((EntityLivingBase) aEntity);
                        }
                    }
                }
                for (EntityLivingBase aEntity : aRangedEntity) {
                    aEntity.setRevengeTarget((EntityLivingBase) attacker);
                }
            }
        }
    }
}
