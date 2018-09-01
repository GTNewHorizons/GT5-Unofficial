package gtPlusPlus.core.handler.chunkloading;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.PlayerOrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

public class ChunkManager implements LoadingCallback, OrderedLoadingCallback, PlayerOrderedLoadingCallback {
	private static ChunkManager instance;

	public static ChunkManager getInstance() {
		if (instance == null) {
			instance = new ChunkManager();
		}

		return instance;
	}

	@SubscribeEvent
	public void entityEnteredChunk(EnteringChunk event) {

	}

	public Set<ChunkCoordIntPair> getChunksBetween(int xChunkA, int zChunkA, int xChunkB, int zChunkB, int max) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();
		if (xChunkA != xChunkB && zChunkA != zChunkB) {
			return chunkList;
		} else {
			int xStart = Math.min(xChunkA, xChunkB);
			int xEnd = Math.max(xChunkA, xChunkB);
			int zStart = Math.min(zChunkA, zChunkB);
			int zEnd = Math.max(zChunkA, zChunkB);

			for (int xx = xStart; xx <= xEnd; ++xx) {
				for (int zz = zStart; zz <= zEnd; ++zz) {
					chunkList.add(new ChunkCoordIntPair(xx, zz));
					if (chunkList.size() >= max) {
						return chunkList;
					}
				}
			}

			return chunkList;
		}
	}

	public Set<ChunkCoordIntPair> getChunksAround(int xChunk, int zChunk, int radius) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();

		for (int xx = xChunk - radius; xx <= xChunk + radius; ++xx) {
			for (int zz = zChunk - radius; zz <= zChunk + radius; ++zz) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}

		return chunkList;
	}

	public Set<ChunkCoordIntPair> getBufferAround(int xWorld, int zWorld, int radius) {
		int minX = xWorld - radius >> 4;
		int maxX = xWorld + radius >> 4;
		int minZ = zWorld - radius >> 4;
		int maxZ = zWorld + radius >> 4;
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();

		for (int xx = minX; xx <= maxX; ++xx) {
			for (int zz = minZ; zz <= maxZ; ++zz) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}

		return chunkList;
	}

	public void ticketsLoaded(List<Ticket> tickets, World world) {
		Iterator<Ticket> var3 = tickets.iterator();
		while (var3.hasNext()) {
			Ticket ticket = (Ticket) var3.next();
			if (!ticket.isPlayerTicket()) {
				Entity entity = ticket.getEntity();
				if (entity == null) {
					int x = ticket.getModData().getInteger("xCoord");
					int y = ticket.getModData().getInteger("yCoord");
					int z = ticket.getModData().getInteger("zCoord");
					if (y >= 0) {
						TileEntity tile = world.getTileEntity(x, y, z);
						if (tile instanceof IGregTechTileEntity) {
							IGregTechTileEntity g = (IGregTechTileEntity) tile;
							if (g instanceof GregtechMetaTileEntityChunkLoader) {
								GregtechMetaTileEntityChunkLoader t = (GregtechMetaTileEntityChunkLoader) g;
								t.forceChunkLoading(t.getBaseMetaTileEntity(), ticket);
								// this.printChunkLoader(t.getName(), x, y, z);
							}
						}
					}
				}
			}
		}
	}

	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		Set<Ticket> adminTickets = new HashSet<Ticket>();
		Set<Ticket> worldTickets = new HashSet<Ticket>();
		Set<Ticket> cartTickets = new HashSet<Ticket>();
		Iterator<Ticket> var7 = tickets.iterator();

		while (var7.hasNext()) {
			Ticket ticket = (Ticket) var7.next();
			Entity entity = ticket.getEntity();
			if (entity == null) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");
				String type = ticket.getModData().getString("type");
				if (y >= 0) {
					if (type.equals("AdminChunkLoader")) {
						adminTickets.add(ticket);
					} else if (type.equals("StandardChunkLoader")) {
						worldTickets.add(ticket);
					} else if (type.isEmpty()) {
						worldTickets.add(ticket);
					}
				}
			} 
		}

		List<Ticket> claimedTickets = new LinkedList<Ticket>();
		claimedTickets.addAll(cartTickets);
		claimedTickets.addAll(adminTickets);
		claimedTickets.addAll(worldTickets);
		return claimedTickets;
	}

	public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
		return LinkedListMultimap.create();
	}
}