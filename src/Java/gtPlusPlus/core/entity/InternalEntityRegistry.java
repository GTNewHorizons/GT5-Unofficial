package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.entity.type.EntityAustralianSpiderBase;
import gtPlusPlus.australia.entity.type.EntityBoar;
import gtPlusPlus.australia.entity.type.EntityDingo;
import gtPlusPlus.australia.entity.type.EntityOctopus;
import gtPlusPlus.core.entity.monster.EntityBatKing;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.util.Utils;

public class InternalEntityRegistry {

	static int mEntityID = 0;
	
	public static void registerEntities(){	
		Logger.INFO("Registering GT++ Entities.");
		
        //EntityRegistry.registerGlobalEntityID(EntityPrimedMiningExplosive.class, "MiningCharge", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(200, 0, 200), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", mEntityID++, GTplusplus.instance, 64, 20, true);
       

        //EntityRegistry.registerGlobalEntityID(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(255, 255, 255));
        EntityRegistry.registerModEntity(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntityToxinballSmall.class, "toxinBall", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 25, 0), Utils.rgbtoHexValue(0, 125, 0));
        EntityRegistry.registerModEntity(EntityToxinballSmall.class, "toxinBall", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntityStaballoyConstruct.class, "constructStaballoy", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 75, 0), Utils.rgbtoHexValue(50, 220, 50));
        EntityRegistry.registerModEntity(EntityStaballoyConstruct.class, "constructStaballoy", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntitySickBlaze.class, "sickBlaze", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 75, 0), Utils.rgbtoHexValue(75, 175, 75));
        EntityRegistry.registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 20, true);
       

        //EntityRegistry.registerGlobalEntityID(EntityTeslaTowerLightning.class, "plasmaBolt", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntityTeslaTowerLightning.class, "plasmaBolt", mEntityID++, GTplusplus.instance, 64, 5, true);
        
        /**
         * Globals, which generate spawn eggs. (Currently required for Giant chicken spawning)
         */
        
        EntityRegistry.registerGlobalEntityID(EntityGiantChickenBase.class, "bigChickenFriendly", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        EntityRegistry.registerGlobalEntityID(EntityBatKing.class, "batKing", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(175, 175, 0), Utils.rgbtoHexValue(0, 175, 175));
        //EntityRegistry.registerModEntity(EntityGiantChickenBase.class, "bigChickenFriendly", mEntityID++, GTplusplus.instance, 64, 20, true);
        
        
      
        
        
        //Australia
        EntityRegistry.registerModEntity(EntityAustralianSpiderBase.class, "AusSpider", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityBoar.class, "AusBoar", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityDingo.class, "AusDingo", mEntityID++, GTplusplus.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityOctopus.class, "AusOctopus", mEntityID++, GTplusplus.instance, 32, 20, true);
        //EntityRegistry.registerGlobalEntityID(EntityAustralianSpiderBase.class, "AusSpider", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityBoar.class, "AusBoar", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityDingo.class, "AusDingo", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityOctopus.class, "AusOctopus", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        
        
        
        
        
        
       
	}
	
}
