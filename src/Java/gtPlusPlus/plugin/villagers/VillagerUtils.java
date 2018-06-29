package gtPlusPlus.plugin.villagers;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.plugin.villagers.entity.EntityBaseVillager;
import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

public class VillagerUtils {

	
	public static void registerNewVillager(int aID, String aName, Object aProfession, Object aCareer, ResourceLocation aSkin, IVillageTradeHandler aCustomTrade) {
		
		
		
		//Register Custom Trade to Registry.
		if (aCustomTrade != null) {
		Core_VillagerAdditions.mVillagerTrades.put(new Pair<Integer, IVillageTradeHandler>(aID, aCustomTrade));
		}
		//Register Skin to Registry.
		if (aSkin != null) {
			Core_VillagerAdditions.mVillagerSkins.put(aID, aSkin);
		}
		
		EntityBaseVillager entityvillager = new EntityBaseVillager(null, aID);
		createNewMobSpawner(aID, entityvillager);
		
	}
	
	
	
	
	
	
	
	
	
	public static void createNewMobSpawner(int aID, Entity aEntity) {
		TileEntityGenericSpawner.registerNewMobSpawner(aID, aEntity);
	}
	
	
	
}
