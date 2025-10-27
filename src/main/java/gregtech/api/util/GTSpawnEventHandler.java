package gregtech.api.util;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

import com.gtnewhorizon.gtnhlib.datastructs.space.ArrayProximityCheck4D;
import com.gtnewhorizon.gtnhlib.datastructs.space.VolumeShape;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GTSpawnEventHandler {

    private final ArrayProximityCheck4D repellents = new ArrayProximityCheck4D(VolumeShape.SPHERE);

    public void putRepellent(IGregTechTileEntity mte, int radius) {
        repellents.put(mte.getWorld().provider.dimensionId, mte.getXCoord(), mte.getYCoord(), mte.getZCoord(), radius);
    }

    public void removeRepellent(IGregTechTileEntity mte) {
        repellents.remove(mte.getWorld().provider.dimensionId, mte.getXCoord(), mte.getYCoord(), mte.getZCoord());
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
            if (repellents.isInRange(
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
