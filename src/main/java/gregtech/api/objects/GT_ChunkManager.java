package gregtech.api.objects;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// This class handles re-initializing chunks after a server restart
public class GT_ChunkManager implements ForgeChunkManager.OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback {
    private Map<TileEntity, Ticket> registeredTickets = new HashMap<>();
    public static GT_ChunkManager instance = new GT_ChunkManager();

    public static void init() {
        ForgeChunkManager.setForcedChunkLoadingCallback(GT_Mod.instance, instance);
       // MinecraftForge.EVENT_BUS.register(instance);
    }

    // Actually process the tickets
    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        if (!GT_Values.alwaysReloadChunkloaders)
            return;
        for (Ticket ticket : tickets) {
            if (ticket.isPlayerTicket())
                continue;
            int x = ticket.getModData().getInteger("OriginX");
            int y = ticket.getModData().getInteger("OriginY");
            int z = ticket.getModData().getInteger("OriginZ");
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null)
                registeredTickets.put(tile, ticket);
        }
    }

    // Determine if tickets should be kept.  Based on if the ticket is a machine or working chunk ticket. Working chunk tickets are tossed
    // and re-created when the machine re-activates.  Machine tickets are kept only if the config alwaysReloadChunkloaders is true. Otherwise
    // machine chunks are tossed and re-created only when the machine re-activates, similar to a Passive Anchor.
    @Override
    public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
        return tickets;
    }

    // Determine if player tickets should be kept.  This is where a ticket list per player would be created and maintained. When
    // a player join event occurs, their name/UUID/whatevs is compared against tickets on this list and those tickets reactivated.
    // Since that info would be maintained/dealt with on a per-player startup, the list returned back to Forge is empty.
    // ? will think about that, not filter here yet
    @Override
    public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
        // Not currently used, so just return an empty list.
        return tickets;
    }

    public boolean requestPlayerChunkLoad(TileEntity owner, ChunkCoordIntPair chunkXZ, String player) {
        if (!GT_Values.enableChunkloaders)
            return false;
        if (registeredTickets.containsKey(owner)) {
            ForgeChunkManager.forceChunk(registeredTickets.get(owner), chunkXZ);
        } else {
            Ticket ticket = null;
            if (player != "")
                ticket = ForgeChunkManager.requestPlayerTicket(GT_Mod.instance, player, owner.getWorldObj(), ForgeChunkManager.Type.NORMAL);
            else
                ticket = ForgeChunkManager.requestTicket(GT_Mod.instance, owner.getWorldObj(), ForgeChunkManager.Type.NORMAL);
            if (ticket == null) {
                if (GT_Values.debugChunkloders)
                    GT_Log.out.println("ForgeChunkManager.requestTicket failed");
                return false;
            }
            NBTTagCompound tag = ticket.getModData();
            tag.setInteger("OwnerX", owner.xCoord);
            tag.setInteger("OwnerY", owner.yCoord);
            tag.setInteger("OwnerZ", owner.zCoord);
            ForgeChunkManager.forceChunk(ticket, chunkXZ);
            registeredTickets.put(owner, ticket);
        }
        return true;
    }

    public boolean requestChunkLoad(TileEntity owner, ChunkCoordIntPair chunkXZ) {
        return requestPlayerChunkLoad(owner, chunkXZ, "");
    }

    public void releaseChunk(TileEntity owner, ChunkCoordIntPair chunkXZ) {
        Ticket ticket = registeredTickets.get(owner);
        if (ticket != null) {
            ForgeChunkManager.unforceChunk(ticket, chunkXZ);
        }
    }

    public void releaseAllChunks(TileEntity owner) {
        Ticket ticket = registeredTickets.get(owner);
        if (ticket != null) {
            for (ChunkCoordIntPair c : ticket.getChunkList()){
                ForgeChunkManager.unforceChunk(ticket, c);
            }
        }
    }

    public static void printTickets() {
        if (!GT_Values.debugChunkloders)
            return;
        for (Ticket ticket : instance.registeredTickets.values()) {
            if (ticket.isPlayerTicket())
                GT_Log.out.print("Player forced chunks " + ticket.getPlayerName() + " :");
            else
                GT_Log.out.print("Forced chunks:");
            for (ChunkCoordIntPair c : ticket.getChunkList()) {
                GT_Log.out.print("(");
                GT_Log.out.print(c.chunkXPos);
                GT_Log.out.print(", ");
                GT_Log.out.print(c.chunkZPos);
                GT_Log.out.print("), ");
            }
            GT_Log.out.println();
        }
    }
}
