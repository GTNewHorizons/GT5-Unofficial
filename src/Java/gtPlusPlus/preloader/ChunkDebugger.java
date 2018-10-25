package gtPlusPlus.preloader;

import java.util.LinkedHashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkDebugger {


	public static final Map<Integer, Pair<String, String>> mChunkTicketsMap = new LinkedHashMap<Integer, Pair<String, String>>();
	public static final Map<String, Pair<String, String>> mChunksLoadedByModsMap = new LinkedHashMap<String, Pair<String, String>>();

	public static void storeTicketToCache(Ticket aTicket, World aWorld) {    
		mChunkTicketsMap.put(aTicket.hashCode(), new Pair<String, String>(aTicket.getModId(), ""+aTicket.world.provider.dimensionId));
		Logger.REFLECTION("Ticket created by "+aTicket.getModId()+" for dimension "+aTicket.world.provider.dimensionId);
	}

	public static void storeLoadChunkToCache(Ticket aTicket, ChunkCoordIntPair aChunk) {    
		mChunksLoadedByModsMap.put(aChunk.toString(), new Pair<String, String>(aTicket.getModId(), aChunk.toString()));	
		Logger.REFLECTION("Chunk Loaded by "+aTicket.getModId()+" at position "+aChunk.toString()+" for dimension "+aTicket.world.provider.dimensionId);
	}

	public static void removeTicketFromCache(Ticket aTicket) {
		Pair<String, String> aPair = mChunkTicketsMap.get(aTicket.hashCode());
		Logger.REFLECTION("Ticket released by "+aTicket.getModId()+" for dimension "+aPair.getValue());
		mChunkTicketsMap.remove(aTicket.hashCode());
	}

	public static void removeLoadedChunkFromCache(ChunkCoordIntPair aChunk) {
		if (aChunk == null || aChunk.toString() == null) {
			return;
		}
		Pair<String, String> aPair = mChunksLoadedByModsMap.get(aChunk.toString());
		if (aPair == null) {
			return;
		}
		String aKey = aPair.getKey();
		String aValue = aPair.getValue();
		if (aKey != null && aValue != null) {
			Logger.REFLECTION("Chunk Loaded by "+aKey+" at position "+aChunk.toString()+" for dimension "+aValue);
		}		
		mChunksLoadedByModsMap.remove(aChunk.toString());
	}
	
}
