package gtPlusPlus.core.entity;

import gtPlusPlus.GTplusplus;
import cpw.mods.fml.common.registry.EntityRegistry;

public class InternalEntityRegistry {

	public static void registerEntities(){
	
		//EntityRegistry.registerModEntity(EntityMiningChargePrimed.class, "MiningCharge", 3, Main.modInstance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", 3, GTplusplus.instance, 64, 20, true);
		
	}
	
}
