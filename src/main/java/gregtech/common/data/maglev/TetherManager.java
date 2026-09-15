package gregtech.common.data.maglev;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;

public class TetherManager {

    private final Map<MTEMagLevPylon, Integer> ACTIVE_PYLONS = new HashMap<>();

    /** Used by pylons to determine if a player is connected */
    private final Map<EntityPlayerMP, MTEMagLevPylon> PLAYER_TETHERS = new HashMap<>();

    public void registerPylon(MTEMagLevPylon tether, int range) {
        ACTIVE_PYLONS.put(tether, range);
    }

    public void unregisterPylon(MTEMagLevPylon tether) {
        ACTIVE_PYLONS.remove(tether);
    }

    public MTEMagLevPylon getClosestActivePylon(EntityPlayer player) {
        MTEMagLevPylon closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Map.Entry<MTEMagLevPylon, Integer> entry : ACTIVE_PYLONS.entrySet()) {
            IGregTechTileEntity mte = entry.getKey()
                .getBaseMetaTileEntity();
            if (mte.getWorld().provider.dimensionId != player.dimension) continue;
            double dx = player.posX - mte.getXCoord() - 0.5;
            double dz = player.posZ - mte.getZCoord() - 0.5;
            double range = entry.getValue() + 0.5;
            if (Math.abs(dx) >= range || Math.abs(dz) >= range) continue;
            double dy = player.posY - mte.getYCoord() - 0.5;
            double distance = dx * dx + dy * dy + dz * dz;
            if (distance < closestDistance) {
                closest = entry.getKey();
                closestDistance = distance;
            }
        }
        return closest;
    }

    public void connectPlayer(EntityPlayer player, MTEMagLevPylon tether) {
        if (player instanceof EntityPlayerMP playerMP) {
            tether.connectPlayer();
            PLAYER_TETHERS.put(playerMP, tether);
        }
    }

    public void disconnectPlayer(EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            final MTEMagLevPylon removed = PLAYER_TETHERS.remove(playerMP);
            if (removed != null) {
                removed.disconnectPlayer();
            }
        }
    }

    public MTEMagLevPylon getConnectedPylon(EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            return PLAYER_TETHERS.get(playerMP);
        }
        return null;
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event) {
        disconnectPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangeDim(PlayerChangedDimensionEvent event) {
        disconnectPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP playerMP) {
            disconnectPlayer(playerMP);
        }
    }
}
