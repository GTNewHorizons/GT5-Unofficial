package gtPlusPlus.core.handler.events;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import com.kuba6000.mobsinfo.api.IMobExtraInfoProvider;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

@Optional.Interface(iface = "com.kuba6000.mobsinfo.api.IMobExtraInfoProvider", modid = Mods.ModIDs.MOBS_INFO)
public class EntityDeathHandler implements IMobExtraInfoProvider {

    private static final HashMap<Class<?>, ArrayList<Triple<ItemStack, Integer, Integer>>> mMobDropMap = new HashMap<>();
    private static final HashMap<Class<?>, ArrayList<Triple<ItemStack, Integer, Integer>>> mBurningMobDropMap = new HashMap<>();
    private static final ArrayList<Class<?>> mInternalClassKeyCache = new ArrayList<>();

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
     *
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class<?> aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        registerDropsForMob(aMobClass, aStack, aMaxAmount, aChance, false);
    }

    /**
     * Provides the ability to provide custom drops upon the death of burning EntityLivingBase objects.
     *
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForBurningMob(Class<?> aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        registerDropsForMob(aMobClass, aStack, aMaxAmount, aChance, true);
    }

    private static void registerDropsForMob(Class<?> aMobClass, ItemStack aStack, int aMaxAmount, int aChance,
        boolean dropWhenMobIsBurning) {
        var targetDropMap = (dropWhenMobIsBurning) ? mBurningMobDropMap : mMobDropMap;
        var aData = Triple.of(aStack, aMaxAmount, aChance);
        var aDataMap = targetDropMap.get(aMobClass);

        if (aDataMap == null) {
            aDataMap = new ArrayList<>();
        }
        aDataMap.add(aData);
        targetDropMap.put(aMobClass, aDataMap);
        mInternalClassKeyCache.add(aMobClass);
    }

    private static ItemStack processItemDropTriplet(Triple<ItemStack, Integer, Integer> aData) {
        ItemStack aLoot = aData.getLeft();
        int aMaxDrop = aData.getMiddle();
        int aChanceOutOf10000 = aData.getRight();
        if (MathUtils.randInt(0, 10000) <= aChanceOutOf10000) {
            aLoot = GTUtility.copyAmount(MathUtils.randInt(1, aMaxDrop), aLoot);
            return aLoot;
        }
        return null;
    }

    private static boolean processDropsForMob(EntityLivingBase entityLiving) {
        ArrayList<Triple<ItemStack, Integer, Integer>> aMobData = null;
        if (entityLiving.isBurning()) {
            aMobData = mBurningMobDropMap.get(entityLiving.getClass());
            // falls back to the normal drop map below if nothing is defined in the burning drop map
        }
        if (aMobData == null || aMobData.isEmpty()) {
            aMobData = mMobDropMap.get(entityLiving.getClass());
        }

        boolean aDidDrop = false;
        if (aMobData != null && !aMobData.isEmpty()) {
            ItemStack aPossibleDrop;
            for (Triple<ItemStack, Integer, Integer> g : aMobData) {
                aPossibleDrop = processItemDropTriplet(g);
                if (aPossibleDrop != null) {
                    if (entityLiving.entityDropItem(aPossibleDrop, MathUtils.randFloat(0, 1)) != null) {
                        aDidDrop = true;
                    }
                }
            }
        }
        return aDidDrop;
    }

    private static void dropMeatFromPlayer(EntityPlayer aPlayer) {
        var meatVariant = (aPlayer.isBurning()) ? GregtechItemList.CookedHumanMeat : GregtechItemList.RawHumanMeat;

        // always drop some meat.
        int aBigMeatStackSize1 = MathUtils.randInt(4, 8);
        aPlayer.entityDropItem(meatVariant.get(aBigMeatStackSize1), MathUtils.randInt(0, 1));

        // additional chances for more meat.
        if (MathUtils.randInt(0, 10) < 7) {
            int aBigMeatStackSize2 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(meatVariant.get(aBigMeatStackSize2), MathUtils.randInt(0, 1));
        }
        if (MathUtils.randInt(0, 10) < 4) {
            int aBigMeatStackSize3 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(meatVariant.get(aBigMeatStackSize3), MathUtils.randInt(0, 1));
        }
        if (MathUtils.randInt(0, 10) < 2) {
            int aBigMeatStackSize4 = MathUtils.randInt(4, 8);
            aPlayer.entityDropItem(meatVariant.get(aBigMeatStackSize4), MathUtils.randInt(0, 1));
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event == null || event.entityLiving == null) {
            return;
        }
        if (GTUtility.isRealPlayer(event.entityLiving)) {
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

    @Optional.Method(modid = Mods.ModIDs.MOBS_INFO)
    @Override
    public void provideExtraDropsInformation(@NotNull String entityString, @NotNull ArrayList<MobDrop> drops,
        @NotNull MobRecipe recipe) {
        var dropEntry = mMobDropMap.get(recipe.entity.getClass());

        if (dropEntry != null && !dropEntry.isEmpty()) {
            for (Triple<ItemStack, Integer, Integer> data : dropEntry) {
                ItemStack loot = data.getLeft();
                int maxDrop = data.getMiddle();
                int chance = data.getRight();
                if (loot == null) continue;

                loot = loot.copy();
                loot.stackSize = 1;

                MobDrop drop = new MobDrop(
                    loot,
                    MobDrop.DropType.Normal,
                    (int) (MobDrop.getChanceBasedOnFromTo(1, maxDrop) * 10000d * ((double) chance / 10000d)),
                    null,
                    null,
                    false,
                    false);

                drop.clampChance();

                drops.add(drop);
            }
        }
    }
}
