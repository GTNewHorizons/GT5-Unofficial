package gregtech.api.util;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

import com.gtnewhorizon.gtnhlib.datastructs.space.VolumeMembershipCheck;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GTSpawnEventHandler {

    private final VolumeMembershipCheck volumeCheck = new VolumeMembershipCheck(
        VolumeMembershipCheck.VolumeShape.SPHERE);

    public void putRepellent(int dim, int x, int y, int z, int radius) {
        volumeCheck.putVolume(dim, x, y, z, radius);
    }

    public void removeRepellent(int dim, int x, int y, int z) {
        volumeCheck.removeVolume(dim, x, y, z);
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
