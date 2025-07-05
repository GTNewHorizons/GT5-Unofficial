package gregtech.common.data.maglev;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.gtnewhorizon.gtnhlib.datastructs.space.ArrayProximityMap4D;
import com.gtnewhorizon.gtnhlib.datastructs.space.VolumeShape;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class TetherManager {

    /** Map storage of active pylons (dim, x, y, z, range, tether) **/
    private final ArrayProximityMap4D<Tether> ACTIVE_PYLONS = new ArrayProximityMap4D<>(VolumeShape.CUBE);

    /** Used by pylons to determine if a player is connected */
    private final Map<EntityPlayerMP, Tether> PLAYER_TETHERS = new HashMap<>();

    public void registerPylon(int dimID, Tether tether, int range) {
        ACTIVE_PYLONS.put(tether, dimID, tether.sourceX(), tether.sourceY(), tether.sourceZ(), range);
    }

    public void unregisterPylon(int dimID, Tether tether) {
        ACTIVE_PYLONS.remove(dimID, tether.sourceX(), tether.sourceY(), tether.sourceZ());
    }

    public Tether getClosestActivePylon(EntityPlayer player) {
        return ACTIVE_PYLONS.getClosest(player.dimension, player.posX, player.posY, player.posZ);
    }

    public void connectPlayer(EntityPlayer player, Tether tether) {
        if (player instanceof EntityPlayerMP playerMP) {
            tether.connectPlayer();
            PLAYER_TETHERS.put(playerMP, tether);
        }
    }

    public void disconnectPlayer(EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            final Tether removed = PLAYER_TETHERS.remove(playerMP);
            if (removed != null) {
                removed.disconnectPlayer();
            }
        }
    }

    public Tether getConnectedPylon(EntityPlayer player) {
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
