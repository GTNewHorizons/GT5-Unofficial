package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;

public class InternalEntityRegistry {

	static int mEntityID = 0;
	
	public static void registerEntities(){	
		Logger.INFO("Registering GT++ Entities.");
		
        EntityRegistry.registerGlobalEntityID(EntityPrimedMiningExplosive.class, "MiningCharge", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        EntityRegistry.registerGlobalEntityID(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", mEntityID++, GTplusplus.instance, 64, 20, true);
       

        EntityRegistry.registerGlobalEntityID(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        EntityRegistry.registerGlobalEntityID(EntityToxinballSmall.class, "toxinBall", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityToxinballSmall.class, "toxinBall", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        EntityRegistry.registerGlobalEntityID(EntityStaballoyConstruct.class, "constructStaballoy", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityStaballoyConstruct.class, "constructStaballoy", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        EntityRegistry.registerGlobalEntityID(EntitySickBlaze.class, "sickBlaze", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 20, true);
       

        EntityRegistry.registerGlobalEntityID(EntityTeslaTowerLightning.class, "plasmaBolt", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityTeslaTowerLightning.class, "plasmaBolt", mEntityID++, GTplusplus.instance, 64, 5, true);
       
	}
	
}
