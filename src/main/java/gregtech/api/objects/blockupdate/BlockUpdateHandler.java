package gregtech.api.objects.blockupdate;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import appeng.api.util.WorldCoord;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TickTime;

// this is a middleware for block updates
// Greg's BaseMetaTileEntity uses World::markBlockForUpdate() to update the texture of a machine
// World::markBlockForUpdate() triggers block updates of all blocks within the same chunk
// this class makes sure chunk updates are perfmormed only once even if several blocks requested update
// (valid obly for requests made using BlockUpdateHandler::enqueueBlockUpdate)
// and introduces a per chunk cooldown to slow the things a bit
// cause too frequent updates are not necessary for visual appearance of a block

@SideOnly(Side.CLIENT)
public class BlockUpdateHandler {

    public static final int MIN_UPDATE_COOLDOWN = TickTime.SECOND / 2;
    public static final int MAX_UPDATE_COOLDOWN = TickTime.SECOND;

    public final static BlockUpdateHandler Instance = new BlockUpdateHandler();

    private BlockUpdateHandler() {

        blocksToUpdate = new UpdateQueue();
        cooldowns = new ChunkCooldowns();

        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    public void enqueueBlockUpdate(World world, WorldCoord pos) {

        var player = getPlayer();

        if (world != player.worldObj) return;

        ResetDataIfPlayerWorldChanged(player);

        blocksToUpdate.put(getBlockChunkCoords(world, pos), pos);
    }

    @SubscribeEvent
    public void OnClientTickEvent(ClientTickEvent event) {

        if (event.phase != Phase.START) return;

        ResetDataIfPlayerWorldChanged(getPlayer());

        var it = blocksToUpdate.entrySet()
            .iterator();

        while (it.hasNext()) {

            var entry = it.next();
            ChunkCoordIntPair chunkCoords = entry.getKey();
            WorldCoord blockCoords = entry.getValue();

            RandomCooldown cooldown = cooldowns.get(chunkCoords);

            if (cooldown == null) {
                cooldown = new RandomCooldown(MIN_UPDATE_COOLDOWN, MAX_UPDATE_COOLDOWN);
                cooldowns.put(chunkCoords, cooldown);
            }

            if (!cooldown.hasPassed()) continue;

            currWorld.markBlockForUpdate(blockCoords.x, blockCoords.y, blockCoords.z);
            cooldown.set();
            it.remove();
        }
    }

    private EntityClientPlayerMP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    private void ResetDataIfPlayerWorldChanged(EntityClientPlayerMP player) {

        if (player == null) return;

        World playerWorld = player.worldObj;

        if (currWorld != playerWorld) {
            blocksToUpdate.clear();
            cooldowns.clear();
            currWorld = playerWorld;
        }
    }

    private ChunkCoordIntPair getBlockChunkCoords(World world, WorldCoord pos) {

        Chunk chunk = world.getChunkFromBlockCoords(pos.x, pos.z);
        return chunk.getChunkCoordIntPair();
    }

    private UpdateQueue blocksToUpdate;
    private ChunkCooldowns cooldowns;
    private World currWorld = null;

    protected class UpdateQueue extends HashMap<ChunkCoordIntPair, WorldCoord> {
    }

    protected class ChunkCooldowns extends HashMap<ChunkCoordIntPair, RandomCooldown> {
    }
}
