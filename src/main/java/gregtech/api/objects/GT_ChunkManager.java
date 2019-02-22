package gregtech.api.objects;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.ArrayList;
import java.util.List;

// This class handles re-initializing chunks after a server restart
public class GT_ChunkManager implements ForgeChunkManager.OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback {
	// Actually process the tickets 
	public void ticketsLoaded( List<Ticket> tickets, World world) {
		
	}
	
	// Determine if tickets should be kept.  Based on if the ticket is a machine or working chunk ticket. Working chunk tickets are tossed
	// and re-created when the machine re-activates.  Machine tickets are kept only if the config alwaysReloadChunkloaders is true. Otherwise
	// machine chunks are tossed and re-created only when the machine re-activates, similar to a Passive Anchor.
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {

	}
	
	// Determine if player tickets should be kept.  This is where a ticket list per player would be created and maintained. When
	// a player join event occurs, their name/UUID/whatevs is compared against tickets on this list and those tickets reactivated.
	// Since that info would be maintained/dealt with on a per-player startup, the list returned back to Forge is empty.
	public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
		// Not currently used, so just return an empty list.
		ListMultimap<String, Ticket> validTickets = ArrayListMultimap.create();
		
		return validTickets;
    }
}

// This interface provides some variables and determines if a tile entity is a chunk loader or not.  Add it to any TE that 
// needs to support chunkloading, and call the functions during the TE block placement/breaking. 
public interface IChunkLoader {
	Ticket machineTicket; // The ticket associated with the machine.
	ArrayList<Ticket> workingTickets = new ArrayList<Ticket>(); // The tickets associated with working chunks for this machine. Typically one, but may be more than one.
	// Debug variables
	static ArrayList<Ticket> allTickets = new ArrayList<Ticket>();
	
	public static void addMachineTicket(IChunkLoader chunkLoader, Ticket savedTicket) {
		if(!GT_Values.enableChunkloaders) return;
		// Take an old ticket and assign it to the TE.
		
		// Activate ticket
	}
	
	public static void newMachineTicket(IChunkLoader chunkLoader, Block block) {
		if(!GT_Values.enableChunkloaders) return;
		// Request a new ticket at Block location.
		
		// Activate ticket
	}
	
	public static void removeMachineTicket(IChunkLoader chunkLoader) {
		if(!GT_Values.enableChunkloaders) return;
		// Deactivate ticket
		
		// Release release
	}
	
	public static void addWorkingTicket(IChunkLoader chunkLoader, Ticket savedTicket) {
		if(!GT_Values.enableChunkloaders) return;
		// Take an old ticket and assign it to the TE.
		
		// Activate ticket
	}
	
	public static void newWorkingTicket(IChunkLoader chunkLoader, Block block) {
		if(!GT_Values.enableChunkloaders) return;
		// Request a new ticket at Block location?
		
		// Activate ticket
	}
	
	public static void removeWorkingTicket(IChunkLoader chunkLoader, Block block) {
		if(!GT_Values.enableChunkloaders) return;
		// Deactivate ticket at Block location?
		
		// Release release
	}	
	
	public static void printTickets() {
		if(!GT_Values.enableChunkloaders) return;
		
		for( Ticket ticket : allTickets ) {
			 GT_Log.out.println( ticket.getModData().getString("Name") + 
					 	   " " + ticket.getModData().getString("Block") +
					 	   " " + ticket.getModData().getString("XCoord") +
					 	   " " + ticket.getModData().getString("YCoord") +
					 	   " " + ticket.getModData().getString("ZCoord") );
		}
	}
}