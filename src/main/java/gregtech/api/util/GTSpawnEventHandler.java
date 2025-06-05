package gregtech.api.util;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.objects.VolumeMembershipCheck;

public class GTSpawnEventHandler {

    private final VolumeMembershipCheck volumeCheck = new VolumeMembershipCheck(
        VolumeMembershipCheck.VolumeShape.SPHERE);

    public void addRepellent(int dim, int centerX, int centerY, int centerZ, int radius) {
        volumeCheck.addVolume(dim, centerX, centerY, centerZ, radius);
    }

    public void removeRepellent(int dim, int centerX, int centerY, int centerZ) {
        volumeCheck.removeVolume(dim, centerX, centerY, centerZ);
    }

    @SubscribeEvent
    public void denyMobSpawn(CheckSpawn event) {
        if (event.getResult() == Event.Result.DENY) return;

        if (event.entityLiving instanceof EntitySlime slime && !slime.hasCustomNameTag()
            && event.getResult() == Event.Result.ALLOW) {
            event.setResult(Event.Result.DEFAULT);
        }

        if (event.getResult() == Event.Result.ALLOW) {
            return;
        }

        if (event.entityLiving.isCreatureType(EnumCreatureType.monster, false)) {
            if (volumeCheck.isInVolume(
                event.entity.worldObj.provider.dimensionId,
                event.entity.posX,
                event.entity.posY,
                event.entity.posZ)) {
                if (event.entityLiving instanceof EntitySlime slime) {
                    slime.setCustomNameTag("DoNotSpawnSlimes");
                }
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
