package gregtech.api.util;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_MonsterRepellent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

public class GT_SpawnEventHandler {

    public static volatile List<int[]> mobReps = new CopyOnWriteArrayList<>();
    // Future Optimiztation ideas, if this isn't sufficient
    // 1: Keep a weakref list of mob repellents so we already have the tile
    // 2: Have the tick method update a HashMap of (int[], range) so we don't have to load the tile at all

    public GT_SpawnEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void denyMobSpawn(CheckSpawn event) {
        if (event.getResult() == Event.Result.DENY) return;

        if (event.entityLiving instanceof EntitySlime
                && !(((EntitySlime) event.entityLiving).getCustomNameTag().length() > 0)) {
            if (event.getResult() == Event.Result.ALLOW) event.setResult(Event.Result.DEFAULT);
        }

        if (event.getResult() == Event.Result.ALLOW) {
            return;
        }

        if (event.entityLiving.isCreatureType(EnumCreatureType.monster, false)) {
            for (int[] rep : mobReps) {
                if (rep[3] == event.entity.worldObj.provider.dimensionId) {
                    // If the chunk isn't loaded, we ignore this Repellent
                    if (!event.entity.worldObj.blockExists(rep[0], rep[1], rep[2])) continue;

                    final TileEntity tTile = event.entity.worldObj.getTileEntity(rep[0], rep[1], rep[2]);
                    if (tTile instanceof BaseMetaTileEntity
                            && ((BaseMetaTileEntity) tTile).getMetaTileEntity()
                                    instanceof GT_MetaTileEntity_MonsterRepellent) {
                        final int r = ((GT_MetaTileEntity_MonsterRepellent)
                                        ((BaseMetaTileEntity) tTile).getMetaTileEntity())
                                .mRange;
                        final double dx = rep[0] + 0.5F - event.entity.posX;
                        final double dy = rep[1] + 0.5F - event.entity.posY;
                        final double dz = rep[2] + 0.5F - event.entity.posZ;
                        if ((dx * dx + dz * dz + dy * dy) <= Math.pow(r, 2)) {
                            if (event.entityLiving instanceof EntitySlime)
                                ((EntitySlime) event.entityLiving).setCustomNameTag("DoNotSpawnSlimes");
                            event.setResult(Event.Result.DENY);
                            // We're already DENYing it.  No reason to keep checking
                            return;
                        }
                    }
                }
            }
        }
    }
}
