package gtPlusPlus.core.chunkloading;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.IChunkLoader;
import gtPlusPlus.core.lib.CORE;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * This class handles re-initializing chunks after a server restart
 * Credits to Repo-Alt for the original implementation.
 * @author Repo-Alt, Alkalus
 *
 */
public class GTPP_ChunkManager implements ForgeChunkManager.OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback {


	private Map<TileEntity, Ticket> registeredTickets = new HashMap<>();

	private static GTPP_ChunkManager instance = new GTPP_ChunkManager();

	public static boolean enableChunkloaders = true;
	public static boolean alwaysReloadChunkloaders = false;
	public static boolean debugChunkloaders = false;

	public static void init() {		
		if (enableChunkloaders) {
			ForgeChunkManager.setForcedChunkLoadingCallback(GTplusplus.instance, instance);
		}	
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {}

	// Determine if tickets should be kept.  Based on if the ticket is a machine or working chunk ticket. 
	// Working chunk tickets are tossed and re-created when the machine re-activates.  
	// Machine tickets are kept only if the config alwaysReloadChunkloaders is true. Otherwise
	// machine chunks are tossed and re-created only when the machine re-activates, similar to a Passive Anchor.
	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		List<Ticket> validTickets = new ArrayList<>();
		if (alwaysReloadChunkloaders) {
			for (Ticket ticket : tickets) {
				int x = ticket.getModData().getInteger("OwnerX");
				int y = ticket.getModData().getInteger("OwnerY");
				int z = ticket.getModData().getInteger("OwnerZ");
				if (y > 0) {
					TileEntity tile = world.getTileEntity(x, y, z);
					if (tile != null && tile instanceof IGregTechTileEntity && ((IGregTechTileEntity)tile).isAllowedToWork()) {
						ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x >> 4, z >> 4));
						if (!registeredTickets.containsKey(tile)) {
							registeredTickets.put(tile, ticket);
							if (((IGregTechTileEntity)tile).getMetaTileEntity() instanceof IChunkLoader) {
								ForgeChunkManager.forceChunk(ticket, ((IChunkLoader)((IGregTechTileEntity)tile).getMetaTileEntity()).getResidingChunk());
							}
							validTickets.add(ticket);
						}
					}
				}
			}
		}
		return validTickets;
	}

	// Determine if player tickets should be kept.  This is where a ticket list per player would be created and maintained. When
	// a player join event occurs, their name/UUID/whatevs is compared against tickets on this list and those tickets reactivated.
	// Since that info would be maintained/dealt with on a per-player startup, the list returned back to Forge is empty.
	@Override
	public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
		// Not currently used, so just return an empty list.
		return ArrayListMultimap.create();
	}

	// Request a chunk to be loaded for this machine
	// may pass null chunk to load just the machine itself, if "alwaysReloadChunkloaders" is enabled in config
	static public boolean requestPlayerChunkLoad(TileEntity owner, ChunkCoordIntPair chunkXZ, String player) {
		if (!enableChunkloaders) {
			return false;
		}
		if (!alwaysReloadChunkloaders && chunkXZ == null) {
			return false;
		}
		if (debugChunkloaders && chunkXZ != null) {
			GT_Log.out.println("GT_ChunkManager: Chunk request: (" + chunkXZ.chunkXPos + ", " + chunkXZ.chunkZPos + ")");
		}
		if (instance.registeredTickets.containsKey(owner)) {
			ForgeChunkManager.forceChunk(instance.registeredTickets.get(owner), chunkXZ);
		} 
		else {
			Ticket ticket = null;
			if (player != "") {
				ticket = ForgeChunkManager.requestPlayerTicket(GT_Mod.instance, player, owner.getWorldObj(), ForgeChunkManager.Type.NORMAL);
			}
			else {
				ticket = ForgeChunkManager.requestTicket(GT_Mod.instance, owner.getWorldObj(), ForgeChunkManager.Type.NORMAL);
			}
			if (ticket == null) {
				if (debugChunkloaders) {
					GT_Log.out.println("GT_ChunkManager: ForgeChunkManager.requestTicket failed");
				}
				return false;
			}
			if (debugChunkloaders) {
				GT_Log.out.println("GT_ChunkManager: ticket issued for machine at: (" + owner.xCoord + ", " + owner.yCoord + ", " + owner.zCoord + ")" );
			}
			NBTTagCompound tag = ticket.getModData();
			tag.setInteger("OwnerX", owner.xCoord);
			tag.setInteger("OwnerY", owner.yCoord);
			tag.setInteger("OwnerZ", owner.zCoord);
			ForgeChunkManager.forceChunk(ticket, chunkXZ);
			if (alwaysReloadChunkloaders) {
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(owner.xCoord << 4, owner.zCoord << 4));
			}
			instance.registeredTickets.put(owner, ticket);
		}
		return true;
	}

	static public boolean requestChunkLoad(TileEntity owner, ChunkCoordIntPair chunkXZ) {
		return requestPlayerChunkLoad(owner, chunkXZ, "");
	}

	static public void releaseChunk(TileEntity owner, ChunkCoordIntPair chunkXZ) {
		if (!enableChunkloaders) {
			return;
		}
		Ticket ticket = instance.registeredTickets.get(owner);
		if (ticket != null) {
			if (debugChunkloaders) {
				GT_Log.out.println("GT_ChunkManager: Chunk release: (" + chunkXZ.chunkXPos + ", " + chunkXZ.chunkZPos + ")");
			}
			ForgeChunkManager.unforceChunk(ticket, chunkXZ);
		}
	}

	static public void releaseTicket(TileEntity owner) {
		if (!enableChunkloaders) {
			return;
		}
		Ticket ticket = instance.registeredTickets.get(owner);
		if (ticket != null) {
			if (debugChunkloaders) {
				GT_Log.out.println("GT_ChunkManager: ticket released by machine at: (" + owner.xCoord + ", " + owner.yCoord + ", " + owner.zCoord + ")" );
				for (ChunkCoordIntPair chunk : ticket.getChunkList()) {
					GT_Log.out.println("GT_ChunkManager: Chunk release: (" + chunk.chunkXPos + ", " + chunk.chunkZPos + ")");
				}
			}
			ForgeChunkManager.releaseTicket(ticket);
			instance.registeredTickets.remove(owner);
		}
	}

	public static void printTickets() {
		if (!debugChunkloaders) {
			return;
		}
		GT_Log.out.println("GT_ChunkManager: Start forced chunks dump:");
		instance.registeredTickets.forEach((machine, ticket) -> {
			GT_Log.out.print("GT_ChunkManager: Chunks forced by the machine at (" + machine.xCoord + ", " + machine.yCoord + ", " + machine.zCoord + ")");
			if (ticket.isPlayerTicket()) {
				GT_Log.out.print(" Owner: " + ticket.getPlayerName());
			}
			GT_Log.out.print(" :");
			for (ChunkCoordIntPair c : ticket.getChunkList()) {
				GT_Log.out.print("(");
				GT_Log.out.print(c.chunkXPos);
				GT_Log.out.print(", ");
				GT_Log.out.print(c.chunkZPos);
				GT_Log.out.print("), ");
			}
		});
		GT_Log.out.println("GT_ChunkManager: End forced chunks dump:");
	}
	public static class DebugCommand extends CommandBase {
		@Override
		public String getCommandName() {
			return "gtpp:dump_chunks";
		}
		@Override
		public int getRequiredPermissionLevel() {
			return 0;
		}
		@Override
		public String getCommandUsage(ICommandSender sender) {
			return "/" + getCommandName();
		}
		@Override
		public void processCommand(ICommandSender sender, String[] args) {
			printTickets();
		}
	}
}
