package gtPlusPlus.core.handler.events;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class EntityDeathHandler {

    private static final HashMap<Class, AutoMap<Triplet<ItemStack, Integer, Integer>>> mMobDropMap = new HashMap<>();
    private static final HashSet<Class> mInternalClassKeyCache = new HashSet<>();

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
     * 
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        Triplet<ItemStack, Integer, Integer> aData = new Triplet<>(aStack, aMaxAmount, aChance);
        AutoMap<Triplet<ItemStack, Integer, Integer>> aDataMap = mMobDropMap.get(aMobClass);
        if (aDataMap == null) {
            aDataMap = new AutoMap<>();
        }
        aDataMap.put(aData);
        mMobDropMap.put(aMobClass, aDataMap);

        Logger.INFO(
                "[Loot] Registered " + aStack.getDisplayName()
                        + " (1-"
                        + aMaxAmount
                        + ") as a valid drop for "
                        + aMobClass.getCanonicalName());

        mInternalClassKeyCache.add(aMobClass);
    }

    private static ItemStack processItemDropTriplet(Triplet<ItemStack, Integer, Integer> aData) {
        ItemStack aLoot = aData.getValue_1();
        int aMaxDrop = aData.getValue_2();
        int aChanceOutOf10000 = aData.getValue_3();
        if (MathUtils.randInt(0, 10000) <= aChanceOutOf10000) {
            aLoot = ItemUtils.getSimpleStack(aLoot, MathUtils.randInt(1, aMaxDrop));
            if (ItemUtils.checkForInvalidItems(aLoot)) {
                return aLoot;
            }
        }
        return null;
    }

    private static boolean processDropsForMob(EntityLivingBase entityLiving) {
        AutoMap<Triplet<ItemStack, Integer, Integer>> aMobData = mMobDropMap.get(entityLiving.getClass());
        boolean aDidDrop = false;
        if (aMobData != null) {
            if (!aMobData.isEmpty()) {
                ItemStack aPossibleDrop;
                for (Triplet<ItemStack, Integer, Integer> g : aMobData) {
                    aPossibleDrop = processItemDropTriplet(g);
                    if (aPossibleDrop != null) {
                        if (entityLiving.entityDropItem(aPossibleDrop, MathUtils.randFloat(0, 1)) != null) {
                            aDidDrop = true;
                        }
                    }
                }
            }
        }
        return aDidDrop;
    }

    private static void dropMeatFromPlayer(EntityPlayer aPlayer) {

        // always drop some meat.
        int aBigMeatStackSize1 = MathUtils.randInt(4, 8);
        aPlayer.entityDropItem(
                ItemUtils.simpleMetaStack(ModItems.itemMetaFood, 0, aBigMeatStackSize1),
                MathUtils.randInt(0, 1));

        // additional chances for more meat.
        if (MathUtils.randInt(0, 10) < 7) {
            int aBigMeatStackSize2 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(
                    ItemUtils.simpleMetaStack(ModItems.itemMetaFood, 0, aBigMeatStackSize2),
                    MathUtils.randInt(0, 1));
        }
        if (MathUtils.randInt(0, 10) < 4) {
            int aBigMeatStackSize3 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(
                    ItemUtils.simpleMetaStack(ModItems.itemMetaFood, 0, aBigMeatStackSize3),
                    MathUtils.randInt(0, 1));
        }
        if (MathUtils.randInt(0, 10) < 2) {
            int aBigMeatStackSize4 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(
                    ItemUtils.simpleMetaStack(ModItems.itemMetaFood, 0, aBigMeatStackSize4),
                    MathUtils.randInt(0, 1));
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event == null || event.entityLiving == null) {
            return;
        }
        if (PlayerUtils.isRealPlayer(event.entityLiving)) {
            EntityPlayer aPlayer = (EntityPlayer) event.entityLiving;
            dropMeatFromPlayer(aPlayer);
        } else {
            for (Class<?> c : mInternalClassKeyCache) {
                if (c.isInstance(event.entityLiving)) {
                    processDropsForMob(event.entityLiving);
                }
            }
        }
    }
}
