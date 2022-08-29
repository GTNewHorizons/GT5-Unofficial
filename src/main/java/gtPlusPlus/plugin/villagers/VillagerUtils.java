package gtPlusPlus.plugin.villagers;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import java.util.HashMap;

public class VillagerUtils {

    public static final HashMap<Integer, VillagerObject> mVillagerMap = new HashMap<Integer, VillagerObject>();

    public static void registerNewVillager(
            int aID,
            String aName,
            Object aProfession,
            Object aCareer,
            Object aSkin,
            IVillageTradeHandler aCustomTrade) {
        registerNewVillager(aID, new VillagerObject(aID, aName, aProfession, aCareer, aSkin, aCustomTrade));
    }

    public static void registerNewVillager(int aID, VillagerObject aVillager) {
        mVillagerMap.put(aID, aVillager);
    }
}
