package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.util.ColorUtil;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.item.general.spawn.ItemCustomSpawnEgg;

public class InternalEntityRegistry {

    static int mEntityID = 0;

    public static void registerEntities() {
        Logger.INFO("Registering GT++ Entities.");

        EntityRegistry.registerModEntity(
            EntityPrimedMiningExplosive.class,
            "MiningCharge",
            mEntityID++,
            GTplusplus.instance,
            64,
            20,
            true);

        EntityRegistry
            .registerModEntity(EntityToxinballSmall.class, "toxinBall", mEntityID++, GTplusplus.instance, 64, 20, true);

        EntityRegistry.registerModEntity(
            EntityStaballoyConstruct.class,
            "constructStaballoy",
            mEntityID++,
            GTplusplus.instance,
            64,
            3,
            true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(
            0,
            "constructStaballoy",
            ColorUtil.toRGB(20, 200, 20),
            ColorUtil.toRGB(20, 20, 20));

        EntityRegistry
            .registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 3, true);
        ItemCustomSpawnEgg
            .registerEntityForSpawnEgg(1, "sickBlaze", ColorUtil.toRGB(40, 180, 40), ColorUtil.toRGB(75, 75, 75));

        EntityRegistry.registerModEntity(
            EntityLightningAttack.class,
            "EntityLightningAttack",
            mEntityID++,
            GTplusplus.instance,
            64,
            20,
            true);

    }
}
