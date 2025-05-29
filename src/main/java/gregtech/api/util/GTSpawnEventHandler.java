package gregtech.api.util;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.datastructs.spatialhashgrid.SpatialHashGrid;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.eventbus.Phase;
import com.gtnewhorizon.gtnhlib.util.DistanceUtil;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.common.tileentities.machines.basic.MTEMonsterRepellent;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@SuppressWarnings("unused")
@EventBusSubscriber(phase = Phase.PRE)
public class GTSpawnEventHandler {

    public static Int2ObjectOpenHashMap<SpatialHashGrid<MTEMonsterRepellent>> mobReps = new Int2ObjectOpenHashMap<>();
    private final static int MAX_RANGE_CHECK = 400;

    // Range of a powered repellent
    public static int getPoweredRepellentRange(int aTier) {
        return 16 + (48 * aTier);
    }

    // Range of an unpowered repellent
    public static int getUnpoweredRepellentRange(int aTier) {
        return 4 + (12 * aTier);
    }

    @SubscribeEvent
    public static void denyMobSpawn(CheckSpawn event) {
        if (event.getResult() == Event.Result.DENY) return;

        if (event.entityLiving instanceof EntitySlime slime && !slime.hasCustomNameTag()
            && event.getResult() == Event.Result.ALLOW) {
            event.setResult(Event.Result.DEFAULT);
        }

        if (event.getResult() == Event.Result.ALLOW) {
            return;
        }

        if (event.entityLiving.isCreatureType(EnumCreatureType.monster, false)) {
            var grid = mobReps.get(event.entity.worldObj.provider.dimensionId);

            var nearby = grid.findNearbyChebyshev(
                (int) event.entity.posX,
                (int) event.entity.posY,
                (int) event.entity.posZ,
                MAX_RANGE_CHECK);

            for (MTEMonsterRepellent mte : nearby) {
                if (DistanceUtil.chebyshevDistance(
                    mte.getBaseMetaTileEntity()
                        .getXCoord(),
                    mte.getBaseMetaTileEntity()
                        .getYCoord(),
                    mte.getBaseMetaTileEntity()
                        .getZCoord(),
                    event.entity.posX,
                    event.entity.posY,
                    event.entity.posZ) <= mte.mRange) {
                    if (event.entityLiving instanceof EntitySlime slime) {
                        slime.setCustomNameTag("DoNotSpawnSlimes");
                    }
                    event.setResult(Event.Result.DENY);
                    // We're already DENYing it. No reason to keep checking
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        mobReps.computeIfAbsent(
            event.world.provider.dimensionId,
            v -> new SpatialHashGrid<>(
                16,
                (vec, te) -> vec.set(
                    te.getBaseMetaTileEntity()
                        .getXCoord(),
                    te.getBaseMetaTileEntity()
                        .getYCoord(),
                    te.getBaseMetaTileEntity()
                        .getZCoord())));
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        if (mobReps.containsKey(event.world.provider.dimensionId)) {
            mobReps.remove(event.world.provider.dimensionId);
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignored) {
        mobReps.clear();
    }
}
