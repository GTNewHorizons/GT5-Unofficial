package gtPlusPlus.core.chunkloading;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;

/**
 *
 * This class handles re-initializing chunks after a server restart Credits to Repo-Alt for the original implementation.
 * 
 * @author Repo-Alt, Alkalus
 *
 */
public class GTPP_ChunkManager implements ForgeChunkManager.LoadingCallback, ForgeChunkManager.OrderedLoadingCallback,
        ForgeChunkManager.PlayerOrderedLoadingCallback {

    private static GTPP_ChunkManager instance;

    public static boolean enableChunkloaders = true;
    public static boolean alwaysReloadChunkloaders = false;
    public static boolean debugChunkloaders = false;

    public static GTPP_ChunkManager getInstance() {
        if (instance == null) {
            instance = new GTPP_ChunkManager();
        }
        return instance;
    }

    public static void init() {
        if (enableChunkloaders) {
            ForgeChunkManager.setForcedChunkLoadingCallback(GTplusplus.instance, getInstance());
            MinecraftForge.EVENT_BUS.register(getInstance());
        }
    }

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        for (Ticket ticket : tickets) {
            if (ticket.isPlayerTicket()) continue;
            Entity entity = ticket.getEntity();
            if (entity == null) {
                int x = ticket.getModData().getInteger("xCoord");
                int y = ticket.getModData().getInteger("yCoord");
                int z = ticket.getModData().getInteger("zCoord");

                if (y >= 0) {
                    TileEntity tile = world.getTileEntity(x, y, z);
                    if (((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GregtechMetaTileEntityChunkLoader) {
                        ((GregtechMetaTileEntityChunkLoader) ((IGregTechTileEntity) tile).getMetaTileEntity())
                                .forceChunkLoading(ticket);
                    }
                }
            }
        }
    }

    @Override
    public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
        Set<Ticket> validTickets = new HashSet<>();
        for (Ticket ticket : tickets) {
            Entity entity = ticket.getEntity();
            if (entity == null) {
                int x = ticket.getModData().getInteger("xCoord");
                int y = ticket.getModData().getInteger("yCoord");
                int z = ticket.getModData().getInteger("zCoord");

                if (y >= 0) {
                    TileEntity tile = world.getTileEntity(x, y, z);
                    if (((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GregtechMetaTileEntityChunkLoader) {
                        validTickets.add(ticket);
                    }
                }
            }
        }

        return new LinkedList<>(validTickets);
    }

    @Override
    public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
        return LinkedListMultimap.create();
    }

    public static Set<ChunkCoordIntPair> getChunksAround(int xChunk, int zChunk, int radius) {
        Set<ChunkCoordIntPair> chunkList = new HashSet<>();
        for (int xx = xChunk - radius; xx <= xChunk + radius; xx++) {
            for (int zz = zChunk - radius; zz <= zChunk + radius; zz++) {
                chunkList.add(new ChunkCoordIntPair(xx, zz));
            }
        }
        return chunkList;
    }
}
