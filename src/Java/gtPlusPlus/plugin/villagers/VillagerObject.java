package gtPlusPlus.plugin.villagers;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.plugin.villagers.entity.EntityBaseVillager;
import net.minecraft.util.ResourceLocation;

public class VillagerObject {

	public final int mID;
	public final String mName;


	public VillagerObject(int aID, String aName, Object aProfession, Object aCareer, Object aSkin, IVillageTradeHandler aCustomTrade) {

		mID = aID;
		mName = aName;

		//Register Custom Trade to Registry.
		if (aCustomTrade != null) {
			Core_VillagerAdditions.mVillagerTrades.put(new Pair<Integer, IVillageTradeHandler>(aID, aCustomTrade));
		}
		//Register Skin to Registry.
		if (aSkin != null) {

			if (aSkin instanceof String) {
				String s = (String) aSkin;
				aSkin = new ResourceLocation(CORE.MODID+":"+"textures/entity/villager/"+s+".png");
			}
			if (aSkin instanceof ResourceLocation) {
				Core_VillagerAdditions.mVillagerSkins.put(aID, (ResourceLocation) aSkin);
			}		

		}				
		VillagerUtils.registerNewVillager(aID, this);
	}

}
