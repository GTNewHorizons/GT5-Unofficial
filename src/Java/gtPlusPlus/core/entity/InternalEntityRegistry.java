package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.util.Utils;

public class InternalEntityRegistry {

	static int mEntityID = 0;
	
	public static void registerEntities(){	
		Utils.LOG_INFO("Registering GT++ Entities.");
		//EntityRegistry.registerModEntity(EntityMiningChargePrimed.class, "MiningCharge", 3, Main.modInstance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityToxinballSmall.class, "toxinBall", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityStaballoyConstruct.class, "constructStaballoy", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 20, true);
		
	}
	
}
