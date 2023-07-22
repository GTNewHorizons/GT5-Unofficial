package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.monster.EntityBatKing;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityThrowableBomb;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.item.general.spawn.ItemCustomSpawnEgg;
import gtPlusPlus.core.util.Utils;

public class InternalEntityRegistry {

    static int mEntityID = 0;

    public static void registerEntities() {
        Logger.INFO("Registering GT++ Entities.");

        // EntityRegistry.registerGlobalEntityID(EntityPrimedMiningExplosive.class, "MiningCharge",
        // EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(
                EntityPrimedMiningExplosive.class,
                "MiningCharge",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);

        // EntityRegistry.registerGlobalEntityID(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric",
        // EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(200, 0, 200), Utils.rgbtoHexValue(125, 125,
        // 125));
        EntityRegistry.registerModEntity(
                EntitySulfuricAcidPotion.class,
                "throwablePotionSulfuric",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);

        // EntityRegistry.registerGlobalEntityID(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric",
        // EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(255, 255, 255));
        EntityRegistry.registerModEntity(
                EntityHydrofluoricAcidPotion.class,
                "throwablePotionHydrofluoric",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);

        // EntityRegistry.registerGlobalEntityID(EntityToxinballSmall.class, "toxinBall",
        // EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 25, 0), Utils.rgbtoHexValue(0, 125, 0));
        EntityRegistry.registerModEntity(
                EntityToxinballSmall.class,
                "toxinBall",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);

        // EntityRegistry.registerGlobalEntityID(EntityStaballoyConstruct.class, "constructStaballoy",
        // EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 75, 0), Utils.rgbtoHexValue(50, 220, 50));
        EntityRegistry.registerModEntity(
                EntityStaballoyConstruct.class,
                "constructStaballoy",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(
                0,
                "constructStaballoy",
                Utils.rgbtoHexValue(20, 200, 20),
                Utils.rgbtoHexValue(20, 20, 20));

        EntityRegistry
                .registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(
                1,
                "sickBlaze",
                Utils.rgbtoHexValue(40, 180, 40),
                Utils.rgbtoHexValue(75, 75, 75));

        EntityRegistry.registerModEntity(
                EntityThrowableBomb.class,
                "EntityThrowableBomb",
                mEntityID++,
                GTplusplus.instance,
                64,
                10,
                true);

        EntityRegistry.registerModEntity(
                EntityLightningAttack.class,
                "EntityLightningAttack",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);

        /**
         * Globals, which generate spawn eggs. (Currently required for Giant chicken spawning)
         */
        EntityRegistry.registerModEntity(
                EntityGiantChickenBase.class,
                "bigChickenFriendly",
                mEntityID++,
                GTplusplus.instance,
                64,
                20,
                true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(
                2,
                "bigChickenFriendly",
                Utils.rgbtoHexValue(255, 0, 0),
                Utils.rgbtoHexValue(175, 175, 175));
        EntityRegistry
                .registerModEntity(EntityBatKing.class, "batKing", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(
                3,
                "batKing",
                Utils.rgbtoHexValue(175, 175, 0),
                Utils.rgbtoHexValue(0, 175, 175));
    }
}
