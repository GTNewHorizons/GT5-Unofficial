package gregtech.api.objects.blockupdate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import appeng.api.util.WorldCoord;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TickTime;

// this is a middleware for block updates
// Greg's BaseMetaTileEntity uses World::markBlockForUpdate() to update the texture of a machine
// World::markBlockForUpdate() triggers block updates of all blocks within the same chunk
// this class makes sure chunk updates are performed only once even if several blocks requested update
// (valid only for requests made using BlockUpdateHandler::enqueueBlockUpdate)
// and introduces a per chunk cooldown to slow the things a bit
// cause too frequent updates are not necessary for visual appearance of a block

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class BlockUpdateHandler {

    public static final int MIN_UPDATE_COOLDOWN = TickTime.SECOND / 2;
    public static final int MAX_UPDATE_COOLDOWN = TickTime.SECOND;
    private static final HashMap<ChunkCoordIntPair, WorldCoord> blocksToUpdate = new HashMap<>();
    private static final HashMap<ChunkCoordIntPair, RandomCooldown> cooldowns = new HashMap<>();
    private static int currentDim;
    private static long internalTickCounter = 0;

    public static void enqueueBlockUpdate(World world, WorldCoord pos) {

        EntityClientPlayerMP player = getPlayer();

        if (world != player.worldObj) return;

        resetDataIfPlayerWorldChanged(player);

        blocksToUpdate.put(getBlockChunkCoords(world, pos), pos);
    }

    @SubscribeEvent
    public static void onClientTickEvent(ClientTickEvent event) {

        if (event.phase != Phase.START) return;

        resetDataIfPlayerWorldChanged(getPlayer());

        Iterator<Map.Entry<ChunkCoordIntPair, WorldCoord>> it = blocksToUpdate.entrySet()
            .iterator();

        while (it.hasNext()) {

            Map.Entry<ChunkCoordIntPair, WorldCoord> entry = it.next();
            ChunkCoordIntPair chunkCoords = entry.getKey();
            WorldCoord blockCoords = entry.getValue();

            RandomCooldown cooldown = cooldowns.get(chunkCoords);

            if (cooldown == null) {
                cooldown = new RandomCooldown(MIN_UPDATE_COOLDOWN, MAX_UPDATE_COOLDOWN);
                cooldowns.put(chunkCoords, cooldown);
            }

            if (!cooldown.hasPassed(internalTickCounter)) continue;

            Minecraft.getMinecraft().theWorld.markBlockForUpdate(blockCoords.x, blockCoords.y, blockCoords.z);
            cooldown.set(internalTickCounter);
            it.remove();
        }

        ++internalTickCounter;
    }

    private static EntityClientPlayerMP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    private static void resetDataIfPlayerWorldChanged(EntityClientPlayerMP player) {

        if (player == null) return;

        int playerDim = player.dimension;

        if (currentDim != playerDim) {
            blocksToUpdate.clear();
            cooldowns.clear();
            currentDim = playerDim;
        }
    }

    private static ChunkCoordIntPair getBlockChunkCoords(World world, WorldCoord pos) {

        Chunk chunk = world.getChunkFromBlockCoords(pos.x, pos.z);
        return chunk.getChunkCoordIntPair();
    }
}
