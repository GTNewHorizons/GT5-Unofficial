package gtPlusPlus.plugin.villagers;

import java.util.HashMap;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import net.minecraft.util.ResourceLocation;

public class VillagerUtils {

	public static final HashMap<Integer, VillagerObject> mVillagerMap = new HashMap<Integer, VillagerObject>();
	
	public static void registerNewVillager(int aID, String aName, Object aProfession, Object aCareer, ResourceLocation aSkin, IVillageTradeHandler aCustomTrade) {
		registerNewVillager(aID, new VillagerObject(aID, aName, aProfession, aCareer, aSkin, aCustomTrade));
	}
	
	public static void registerNewVillager(int aID, VillagerObject aVillager) {
		mVillagerMap.put(aID, aVillager);
	}
	
	
	
}
