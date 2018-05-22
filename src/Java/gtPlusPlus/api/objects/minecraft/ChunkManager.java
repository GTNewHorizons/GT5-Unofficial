/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package gtPlusPlus.api.objects.minecraft;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.ReverseAutoMap;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.*;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ChunkManager implements LoadingCallback, OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback {

	private static final ChunkManager instance;
	private static volatile long mInternalTickCounter = 0;	
	private static ReverseAutoMap<String> mIdToUUIDMap = new ReverseAutoMap<String>();
	
	public static ConcurrentHashMap<String, Triplet<Integer, GregtechMetaTileEntityChunkLoader, DimChunkPos>> mChunkLoaderManagerMap = new ConcurrentHashMap<String, Triplet<Integer, GregtechMetaTileEntityChunkLoader, DimChunkPos>>();
	
	

	static {
		instance = new ChunkManager();
	}

	public ChunkManager() {
		Utils.registerEvent(this);
	}
	
	public static boolean setIdAndUniqueString(int id, String blockposString) {
		if (mIdToUUIDMap.injectCleanDataToAutoMap(id, blockposString)) {
			Logger.INFO("Found Cached ID from NBT, cleanly injected into ChunkManager.");
			return true;
		}
		else {
			Logger.INFO("Creating new Cached ID based on blockpos UID");
			if (mIdToUUIDMap.injectCleanDataToAutoMap(mIdToUUIDMap.getNextFreeMapID(), blockposString)) {
				Logger.INFO("Success! Cleanly injected into ChunkManager.");
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public static int getIdFromUniqueString(String blockposString) {
		if (mIdToUUIDMap.containsValue(blockposString)) {
			Logger.INFO("Found Cached ID based on blockpos UID");
			return mIdToUUIDMap.get(blockposString);
		}
		else {
			Logger.INFO("Creating new Cached ID based on blockpos UID");
			return mIdToUUIDMap.putToInternalMap(blockposString);
		}
	}
	
	public static String getUniqueStringFromID(int id) {
		if (mIdToUUIDMap.containsKey(id)) {
			return mIdToUUIDMap.get(id);
		}
		else {
			return "0@0@0@0";
		}
	}


	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent e){
		mInternalTickCounter++;
		try {
			if (mInternalTickCounter % (20*15) == 0) {
				for (String g : mChunkLoaderManagerMap.keySet()) {	
					BlockPos i = BlockPos.generateBlockPos(g);				
					if (i == null) {
						mChunkLoaderManagerMap.remove(g);
						Logger.MACHINE_INFO("Bad Mapping: "+g);
						continue;
					}
					else {
						Logger.MACHINE_INFO("Good Mapping: "+i.getLocationString());					
					}
					Block mBlock = i.world.getBlock(i.xPos, i.yPos, i.zPos);
					TileEntity mTile = i.world.getTileEntity(i.xPos, i.yPos, i.zPos);	
					IGregTechTileEntity mGTile = null;
					boolean remove = false;
					if (((mTile = i.world.getTileEntity(i.xPos, i.yPos, i.zPos)) != null) && (mTile instanceof IGregTechTileEntity)){
						mGTile = (IGregTechTileEntity) mTile; //943-945
						if (mGTile instanceof GregtechMetaTileEntityChunkLoader || mGTile.getMetaTileID() == 943 || mGTile.getMetaTileID() == 944 || mGTile.getMetaTileID() == 945) {
							Logger.MACHINE_INFO("Found Valid Chunk Loader Entity.");
							continue;
						}
						else {
							Logger.MACHINE_INFO("Found Valid GT Tile which was not a Chunk Loader Entity.");
							remove = true;
						}
					}
					else if ((mTile = i.world.getTileEntity(i.xPos, i.yPos, i.zPos)) != null){
						Logger.MACHINE_INFO("Found Valid Tile Entity.");
						remove = true;
					}
					else {
						mTile = null;
						remove = true;
					}
					if (mBlock == null || mGTile == null || mBlock != GregTech_API.sBlockMachines) {
						remove = true;
					}

					if (remove) {						
						//1
						if (mBlock != null) {
							Logger.MACHINE_INFO("Found Block.");
							//2
							if (mBlock == GregTech_API.sBlockMachines) {
								Logger.MACHINE_INFO("Found GT Machine.");								
								//3
								if (mTile != null) {									
									//4
									if (GregtechMetaTileEntityChunkLoader.class.isInstance(mTile.getClass())) {
										Logger.MACHINE_INFO("Found Valid Chunk Loader.");
									}
									else {
										Logger.MACHINE_INFO("Tile Entity was not a Chunk Loader.");
									}//4
								}
								else {
									Logger.MACHINE_INFO("Tile Entity was Null though.");									
								}//3
							}
							else {
								Logger.MACHINE_INFO("Found Block that was not a GT Machine.");							
							}//2
						}
						else {
							Logger.MACHINE_INFO("Found Null Block.");							
						}//1
					}
					mChunkLoaderManagerMap.remove(i.getUniqueIdentifier());
					Logger.INFO("Removing invalid Chunk Loader. Mapping: "+i.getUniqueIdentifier());
					continue;
				}
			}		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public static ChunkManager getInstance() {
		return instance;
	}

	@SubscribeEvent
	public void entityEnteredChunk(EntityEvent.EnteringChunk event) {

	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks between the
	 * start and end chunks.
	 * <p/>
	 * One of the pairs of start/end coords need to be equal.
	 * <p/>
	 * Coordinates are in chunk coordinates, not world coordinates.
	 *
	 * @param xChunkA Start Chunk x-Coord
	 * @param zChunkA Start Chunk z-Coord
	 * @param xChunkB End Chunk x-Coord
	 * @param zChunkB End Chunk z-Coord
	 * @param max     Max number of chunks to return
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getChunksBetween(int xChunkA, int zChunkA, int xChunkB, int zChunkB, int max) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();

		if (xChunkA != xChunkB && zChunkA != zChunkB) {
			return chunkList;
		}

		int xStart = Math.min(xChunkA, xChunkB);
		int xEnd = Math.max(xChunkA, xChunkB);

		int zStart = Math.min(zChunkA, zChunkB);
		int zEnd = Math.max(zChunkA, zChunkB);

		for (int xx = xStart; xx <= xEnd; xx++) {
			for (int zz = zStart; zz <= zEnd; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
				if (chunkList.size() >= max) {
					return chunkList;
				}
			}
		}
		return chunkList;
	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks around point [x,
	 * z]. Coordinates are in chunk coordinates, not world coordinates.
	 *
	 * @param xChunk Chunk x-Coord
	 * @param zChunk Chunk z-Coord
	 * @param radius Distance from [x, z] to include, in number of chunks.
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getChunksAround(int xChunk, int zChunk, int radius) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();
		for (int xx = xChunk - radius; xx <= xChunk + radius; xx++) {
			for (int zz = zChunk - radius; zz <= zChunk + radius; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}
		return chunkList;
	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks around point [x,
	 * z]. Coordinates are in world coordinates, not chunk coordinates.
	 *
	 * @param xWorld World x-Coord
	 * @param zWorld World z-Coord
	 * @param radius Distance from [x, z] to include, in blocks.
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getBufferAround(int xWorld, int zWorld, int radius) {
		int minX = (xWorld - radius) >> 4;
			int maxX = (xWorld + radius) >> 4;
		int minZ = (zWorld - radius) >> 4;
		int maxZ = (zWorld + radius) >> 4;

		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();
		for (int xx = minX; xx <= maxX; xx++) {
			for (int zz = minZ; zz <= maxZ; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}
		return chunkList;
	}

	private void printAnchor(String type, int x, int y, int z) {
		Logger.INFO("[Chunk Loader] "+type+" @ [x: "+x+"][y: "+y+"][z: "+z+"]");
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {

		if (world.isRemote) return;

		System.out.println("Callback 2");
		for (Ticket ticket : tickets) {
			if (ticket.isPlayerTicket())
				continue;
			Entity entity = ticket.getEntity();
			if (entity == null) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");

				if (y >= 0) {
					BlockPos tile = new BlockPos(x, y, z);					
					Ticket H = tryForceLoadChunk(new DimChunkPos(world, tile).getChunk());

					int jhg = 0;
					while (jhg < 50) {
						jhg++;
					}

					if (!mChunkLoaderManagerMap.isEmpty()) {						
						GregtechMetaTileEntityChunkLoader f = mChunkLoaderManagerMap.get(tile.getUniqueIdentifier()).getValue_2();
						int timeout = 0;
						while (f == null) {
							if (timeout > 5000) {
								Logger.INFO("[Chunk Loader] Timed out");
								break;
							}
							else {
								GregtechMetaTileEntityChunkLoader g;
								if (!mChunkLoaderManagerMap.isEmpty()) {
									g = mChunkLoaderManagerMap.get(tile.getUniqueIdentifier()).getValue_2();
									if (g == null) {
										timeout++;
									}
									else {
										Logger.INFO("[Chunk Loader]Tile became Valid");
										f = g;
										break;
									}
								}
							}
						}
						try {
							if (f != null) {
								if (H != null) {
									ForgeChunkManager.releaseTicket(H);
								}								
								f.forceChunkLoading(ticket);
								printAnchor("Force Chunk Loading. Chunk Loader has ID of "+f.getLoaderID()+". ",x,y,z);
							}
							else {
								Logger.INFO("Tile Entity is null.");
							}
						}
						catch (Throwable t) {
							t.printStackTrace();
							Logger.INFO("Mild problem with chunk loading, nothing to worry about.");
						}


						if (H != null) {
							ForgeChunkManager.releaseTicket(H);
						}

					}					

					/*if (tile instanceof IGregTechTileEntity) {
						final IGregTechTileEntity tGregTechTileEntity = (IGregTechTileEntity) tile;
						IGregTechTileEntity anchor = (IGregTechTileEntity) tile;
						GregtechMetaTileEntityChunkLoader jun = (GregtechMetaTileEntityChunkLoader) anchor;
						jun.forceChunkLoading(ticket);
						//printAnchor(anchor.getName(), x, y, z);
					}*/
				}
			} 
		}
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		//        System.out.println("Callback 1");
		Set<Ticket> adminTickets = new HashSet<Ticket>();
		Set<Ticket> worldTickets = new HashSet<Ticket>();
		Set<Ticket> cartTickets = new HashSet<Ticket>();
		for (Ticket ticket : tickets) {
			Entity entity = ticket.getEntity();
			if (entity == null) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");
				if (y >= 0) {
					worldTickets.add(ticket);
				}
			}
		}

		List<Ticket> claimedTickets = new LinkedList<Ticket>();
		claimedTickets.addAll(cartTickets);
		claimedTickets.addAll(adminTickets);
		claimedTickets.addAll(worldTickets);
		return claimedTickets;
	}

	@Override
	public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
		return LinkedListMultimap.create();
	}


	public static Timer createChunkQueue() {
		return ChunkTimerLoader();
	}

	public static Ticket tryForceLoadChunk(Chunk c) {
		Ticket T = getTicketFromForge(c.worldObj);
		if (T == null) {
			Logger.INFO("[Chunk Loader] Trying to force load a chunk that holds a chunkloader, however the Chunk Loading Ticket was null.");				
			return null;
		}
		ForgeChunkManager.forceChunk(T, c.getChunkCoordIntPair());
		Logger.INFO("[Chunk Loader] Trying to force load a chunk that holds a chunkloader.");	
		return T;
	}

	public static Ticket getTicketFromForge(World world) {
		return ForgeChunkManager.requestTicket(GTplusplus.instance, world, Type.NORMAL);
	}

	static Timer ChunkTimerLoader() {		
		Timer timer;
		timer = new Timer();
		timer.schedule(new ChunkCache(), 10 * 1000);
		return timer;
	}

	//Timer Task for notifying the player.
	static class ChunkCache extends TimerTask {
		public ChunkCache() {

		}

		@Override
		public void run() {
			if (mChunkLoaderManagerMap.size() > 0) {
				for (Triplet<Integer, GregtechMetaTileEntityChunkLoader, DimChunkPos> j : mChunkLoaderManagerMap.values()) {
					Ticket T;
					Chunk C;				
					T = j.getValue_2().getTicketFromForge();
					C = j.getValue_3().getChunk();				
					ForgeChunkManager.forceChunk(T, C.getChunkCoordIntPair());
					Logger.INFO("[Chunk Loader] Trying to force load a chunk that holds a chunkloader.");
				}
			}
			else {
				Logger.INFO("[Chunk Loader] No chunks to try to force load chunks that hold chunkloaders.");				
			}
		}
	}
}