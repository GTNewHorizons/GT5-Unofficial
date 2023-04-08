package gtPlusPlus.core.util.minecraft;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.AABB;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.handler.events.EntityDeathHandler;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;

public class EntityUtils {

    public static void setEntityOnFire(final Entity aEntity, final int length) {
        if (!isEntityImmuneToFire(aEntity)) {
            aEntity.setFire(length);
        }
    }

    public static boolean isEntityOnFire(final Entity aEntity) {
        Field aIsOnFire = ReflectionUtils.getField(Entity.class, "fire");
        if (aIsOnFire != null) {
            int aFireTime = (int) ReflectionUtils.getFieldValue(aIsOnFire, aEntity);
            return aFireTime > 0;
        }
        return false;
    }

    public static boolean isEntityImmuneToFire(Entity aEntity) {
        return aEntity.isImmuneToFire();
    }

    public static int getFacingDirection(final Entity entity) {
        final int d = MathHelper.floor_double((entity.rotationYaw * 4.0F) / 360 + 0.50) & 3;
        return d;
    }

    @Deprecated
    public static Block findBlockUnderEntityNonBoundingBox(final Entity parEntity) {
        final int blockX = MathHelper.floor_double(parEntity.posX);
        final int blockY = MathHelper.floor_double(parEntity.posY - 0.2D - parEntity.yOffset);
        final int blockZ = MathHelper.floor_double(parEntity.posZ);
        return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
    }

    public static Block findBlockUnderEntity(final Entity parEntity) {
        final int blockX = MathHelper.floor_double(parEntity.posX);
        final int blockY = MathHelper.floor_double(parEntity.boundingBox.minY) - 1;
        final int blockZ = MathHelper.floor_double(parEntity.posZ);
        return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
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

    // TODO
    public static void registerEntityToBiomeSpawns(final Class<EntityLiving> classy, final EnumCreatureType EntityType,
            final BiomeGenBase baseBiomeGen) {
        EntityRegistry.addSpawn(classy, 6, 1, 5, EntityType, baseBiomeGen); // change the values to vary the spawn
                                                                            // rarity, biome, etc.
    }

    public static boolean applyRadiationDamageToEntity(final int stackSize, final int radiationLevel, final World world,
            final Entity entityHolding) {
        if (!world.isRemote) {
            if ((radiationLevel > 0) && (entityHolding instanceof EntityLivingBase)) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)
                            && !GT_Utility.isWearingFullRadioHazmat(entityLiving)) {
                        int duration;
                        if (entityLiving.getActivePotionEffect(IC2Potion.radiation) != null) {
                            // Utils.LOG_INFO("t");
                            duration = (radiationLevel * 5)
                                    + entityLiving.getActivePotionEffect(IC2Potion.radiation).getDuration();
                        } else {
                            // Utils.LOG_INFO("f");
                            duration = radiationLevel * 30;
                        }
                        // IC2Potion.radiation.applyTo(entityLiving, duration, damage * 15);
                        applyRadioactivity(entityLiving, radiationLevel, stackSize);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean applyRadioactivity(EntityLivingBase aEntity, int aLevel, int aAmountOfItems) {
        if (aLevel > 0 && aEntity != null
                && aEntity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
                && aEntity.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD
                && !ItemArmorHazmat.hasCompleteHazmat(aEntity)) {
            PotionEffect tEffect = null;
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
            return true;
        }
        return false;
    }

    public static boolean applyHeatDamageToEntity(final int heatLevel, final World world, final Entity entityHolding) {
        if (!world.isRemote) {
            if ((heatLevel > 0) && (entityHolding instanceof EntityLivingBase)) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    if (!GT_Utility.isWearingFullHeatHazmat(entityLiving)) {
                        return GT_Utility.applyHeatDamage(entityLiving, heatLevel);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Static Version of the method used in {@code doFireDamage(entity, int)} to save memory.
     */
    private static volatile Method dealFireDamage = null;

    /**
     * Reflective Call to do Fire Damage to an entity (Does not set entity on fire though)
     */
    public static synchronized boolean doFireDamage(Entity entity, int amount) {
        if (dealFireDamage == null) {
            dealFireDamage = ReflectionUtils.getMethod(Entity.class, "dealFireDamage", int.class);
        } else {
            try {
                dealFireDamage.invoke(entity, amount);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
        }
        return false;
    }

    public static void doDamage(Entity entity, DamageSource dmg, int i) {
        entity.attackEntityFrom(dmg, i);
    }

    public static boolean isTileEntityRegistered(Class aTileClass, String aTileName) {
        Field aRegistry = ReflectionUtils
                .getField(ReflectionUtils.getClass("net.minecraft.tileentity.TileEntity"), "nameToClassMap");
        Field aRegistry2 = ReflectionUtils
                .getField(ReflectionUtils.getClass("net.minecraft.tileentity.TileEntity"), "classToNameMap");
        try {
            Object o = aRegistry.get(null);
            if (o != null) {
                Map nameToClassMap = (Map) o;
                if (!nameToClassMap.containsKey(aTileName)) {
                    o = aRegistry2.get(null);
                    if (o != null) {
                        Map classToNameMap = (Map) o;
                        if (!classToNameMap.containsKey(aTileClass)) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getDistance(Entity p1, Entity p2) {
        return Math
                .sqrt(Math.pow(p1.posX - p2.posX, 2) + Math.pow(p1.posY - p2.posY, 2) + Math.pow(p1.posZ - p2.posZ, 2));
    }

    public static AutoMap<Entity> getEntitiesWithinBoundingBoxExcluding(Entity aExclusion, AABB aBoundingBox) {
        if (aExclusion == null) {
            return new AutoMap<Entity>();
        } else {
            List<Entity> aEntities = aBoundingBox.world()
                    .getEntitiesWithinAABBExcludingEntity(aExclusion, aBoundingBox.get());
            return new AutoMap<Entity>(aEntities);
        }
    }

    public static AutoMap<Entity> getEntitiesWithinBoundingBox(Class aEntityType, AABB aBoundingBox) {
        if (aEntityType == null) {
            return new AutoMap<Entity>();
        } else {
            List<Entity> aEntities = aBoundingBox.world().getEntitiesWithinAABB(aEntityType, aBoundingBox.get());
            return new AutoMap<Entity>(aEntities);
        }
    }

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects. Simplified function with
     * static Max drop size of 1.
     * 
     * @param aMobClass - The Base Class you want to drop this item.
     * @param aStack    - The ItemStack, stack size is not respected.
     * @param aChance   - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class aMobClass, ItemStack aStack, int aChance) {
        registerDropsForMob(aMobClass, aStack, 1, aChance);
    }

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
     * 
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        EntityDeathHandler.registerDropsForMob(aMobClass, aStack, aMaxAmount, aChance);
    }
}
