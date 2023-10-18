package gtPlusPlus.core.util.minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.handler.events.EntityDeathHandler;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;

public class EntityUtils {

    public static void setEntityOnFire(final Entity aEntity, final int length) {
        if (!isEntityImmuneToFire(aEntity)) {
            aEntity.setFire(length);
        }
    }

    public static boolean isEntityImmuneToFire(Entity aEntity) {
        return aEntity.isImmuneToFire();
    }

    public static BlockPos findBlockPosUnderEntity(final Entity parEntity) {
        final int blockX = MathHelper.floor_double(parEntity.posX);
        final int blockY = MathHelper.floor_double(parEntity.boundingBox.minY) - 1;
        final int blockZ = MathHelper.floor_double(parEntity.posZ);
        return new BlockPos(blockX, blockY, blockZ, parEntity.worldObj);
    }

    public static BlockPos findBlockPosOfEntity(final Entity parEntity) {
        final int blockX = MathHelper.floor_double(parEntity.posX);
        final int blockY = MathHelper.floor_double(parEntity.boundingBox.minY);
        final int blockZ = MathHelper.floor_double(parEntity.posZ);
        return new BlockPos(blockX, blockY, blockZ, parEntity.worldObj);
    }

    public static void applyRadiationDamageToEntity(final int stackSize, final int radiationLevel, final World world,
                                                    final Entity entityHolding) {
        if (!world.isRemote) {
            if ((radiationLevel > 0) && (entityHolding instanceof final EntityLivingBase entityLiving)) {
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)
                            && !GT_Utility.isWearingFullRadioHazmat(entityLiving)) {
                        if (entityLiving.getActivePotionEffect(IC2Potion.radiation) != null) {
                            entityLiving.getActivePotionEffect(IC2Potion.radiation);
                        }
                        applyRadioactivity(entityLiving, radiationLevel, stackSize);
                    }
                }
            }
        }
    }

    public static void applyRadioactivity(EntityLivingBase aEntity, int aLevel, int aAmountOfItems) {
        if (aLevel > 0 && aEntity != null
                && aEntity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
                && aEntity.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD
                && !ItemArmorHazmat.hasCompleteHazmat(aEntity)) {
            PotionEffect tEffect;
            aEntity.addPotionEffect(
                    new PotionEffect(
                            Potion.moveSlowdown.id,
                            aLevel * 140 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.moveSlowdown)) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                    new PotionEffect(
                            Potion.digSlowdown.id,
                            aLevel * 150 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.digSlowdown)) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                    new PotionEffect(
                            Potion.confusion.id,
                            aLevel * 130 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.confusion)) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                    new PotionEffect(
                            Potion.weakness.id,
                            aLevel * 150 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.weakness)) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                    new PotionEffect(
                            Potion.hunger.id,
                            aLevel * 130 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.hunger)) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                    new PotionEffect(
                            IC2Potion.radiation.id,
                            aLevel * 180 * aAmountOfItems + Math.max(
                                    0,
                                    ((tEffect = aEntity.getActivePotionEffect(Potion.potionTypes[24])) == null ? 0
                                            : tEffect.getDuration())),
                            Math.max(0, (5 * aLevel) / 7)));
        }
    }

    public static void applyHeatDamageToEntity(final int heatLevel, final World world, final Entity entityHolding) {
        if (!world.isRemote) {
            if ((heatLevel > 0) && (entityHolding instanceof final EntityLivingBase entityLiving)) {
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    if (!GT_Utility.isWearingFullHeatHazmat(entityLiving)) {
                        GT_Utility.applyHeatDamage(entityLiving, heatLevel);
                    }
                }
            }
        }
    }

    public static void doDamage(Entity entity, DamageSource dmg, int i) {
        entity.attackEntityFrom(dmg, i);
    }

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
     * 
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class<?> aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        EntityDeathHandler.registerDropsForMob(aMobClass, aStack, aMaxAmount, aChance);
    }
}
