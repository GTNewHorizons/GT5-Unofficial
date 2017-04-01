package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.util.Utils;

public class InternalEntityRegistry {

	public static void registerEntities(){	
		Utils.LOG_INFO("Registering GT++ Entities.");
		//EntityRegistry.registerModEntity(EntityMiningChargePrimed.class, "MiningCharge", 3, Main.modInstance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", 3, GTplusplus.instance, 64, 20, true);
		
	}
	
}
