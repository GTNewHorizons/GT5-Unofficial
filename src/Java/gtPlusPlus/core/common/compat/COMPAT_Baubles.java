package gtPlusPlus.core.common.compat;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.bauble.MonsterKillerBaseBauble;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;
import gtPlusPlus.core.item.general.ItemSlowBuildingRing;
import gtPlusPlus.core.lib.LoadedMods;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;

public class COMPAT_Baubles {

	public static void run(){
		if (LoadedMods.Baubles){
			baublesLoaded();
		}
		else {
			baublesNotLoaded();
		}
	}

	public static void baublesLoaded(){
		Logger.INFO("Baubles Found - Loading Wearables.");
		ModItems.itemPersonalCloakingDevice = new ItemCloakingDevice(0);
		//itemPersonalCloakingDeviceCharged = new ItemCloakingDevice(0).set;
		ModItems.itemPersonalHealingDevice = new ItemHealingDevice();

		try {
			ModItems.itemChargePack1 = new BatteryPackBaseBauble(6);
			ModItems.itemChargePack2 = new BatteryPackBaseBauble(7);
			ModItems.itemChargePack3 = new BatteryPackBaseBauble(8);
			ModItems.itemChargePack4 = new BatteryPackBaseBauble(9);		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		ModItems.itemAmuletMonsterKiller_Zombie = new MonsterKillerBaseBauble(new Class[] {EntityZombie.class}, "Zombie", 6);
		ModItems.itemAmuletMonsterKiller_Skeleton = new MonsterKillerBaseBauble(new Class[] {EntitySkeleton.class}, "Skeleton", 6);
		ModItems.itemAmuletMonsterKiller_Spider = new MonsterKillerBaseBauble(new Class[] {EntitySpider.class}, "Spider", 6);
		ModItems.itemAmuletMonsterKiller_Creeper = new MonsterKillerBaseBauble(new Class[] {EntityCreeper.class}, "Creeper", 6);
		ModItems.itemAmuletMonsterKiller_Enderman = new MonsterKillerBaseBauble(new Class[] {EntityEnderman.class}, "Enderman", 6);
		
		if (LoadedMods.PlayerAPI){
			ModItems.itemSlowBuildingRing = new ItemSlowBuildingRing();
		}
	}

	public static void baublesNotLoaded(){
		Logger.INFO("Baubles Not Found - Skipping Resources.");
	}

}
