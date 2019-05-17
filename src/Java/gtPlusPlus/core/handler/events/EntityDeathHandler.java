package gtPlusPlus.core.handler.events;

import java.util.HashMap;
import java.util.HashSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityDeathHandler {
	
	
	private static final HashMap<Class, AutoMap<Triplet<ItemStack, Integer, Integer>>> mMobDropMap = new HashMap<Class, AutoMap<Triplet<ItemStack, Integer, Integer>>>();
	private static final HashSet<Class> mInternalClassKeyCache = new HashSet<Class>();
	
	/**
	 * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
	 * @param aMobClass - The Base Class you want to drop this item.
	 * @param aStack - The ItemStack, stack size is not respected.
	 * @param aMaxAmount - The maximum size of the ItemStack which drops.
	 * @param aChance - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
	 */
	public static void registerDropsForMob(Class aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {		
		Triplet<ItemStack, Integer, Integer> aData = new Triplet<ItemStack, Integer, Integer>(aStack, aMaxAmount, aChance);		
		AutoMap<Triplet<ItemStack, Integer, Integer>> aDataMap = mMobDropMap.get(aMobClass);
		if (aDataMap == null) {
			aDataMap = new AutoMap<Triplet<ItemStack, Integer, Integer>>();
		}
		aDataMap.put(aData);
		mMobDropMap.put(aMobClass, aDataMap);	
		
		Logger.INFO("[Loot] Registered "+aStack.getDisplayName()+" (1-"+aMaxAmount+") as a valid drop for "+aMobClass.getCanonicalName());
		
		if (!mInternalClassKeyCache.contains(aMobClass)) {
			mInternalClassKeyCache.add(aMobClass);
		}
		
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
	
	
	
	

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		boolean aDidDrop = false;		
		if (event == null || event.entityLiving == null) {
			return;
		}
		for (Class c : mInternalClassKeyCache) {
			if (c.isInstance(event.entityLiving)) {
				aDidDrop = processDropsForMob(event.entityLiving);
			}
		}
	}

}
