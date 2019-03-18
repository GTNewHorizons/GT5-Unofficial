package gtPlusPlus.api.objects.minecraft;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.tileentities.machines.TileEntityPooCollector;
import gtPlusPlus.core.util.Utils;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ThreadPooCollector extends Thread {

	public boolean canRun = true;   
    public boolean isRunning = false;
    
    private static final long INIT_TIME;
    private static long internalTickCounter = 0;

	private static final ThreadPooCollector mThread;
    private static final HashMap<String, Pair<BlockPos, TileEntityPooCollector>> mPooCollectors = new LinkedHashMap<String, Pair<BlockPos, TileEntityPooCollector>>();
    
	
	static {
		mThread = new ThreadPooCollector();
		INIT_TIME = (System.currentTimeMillis());
	}
	
	public ThreadPooCollector() {
        setName("gtpp.handler.poop");
        run();
	}
	
	public static ThreadPooCollector getInstance() {
		return mThread;
	}
	
	public static void addTask(TileEntityPooCollector aTile) {
		BlockPos aTempPos = new BlockPos(aTile);
		mPooCollectors.put(aTempPos.getUniqueIdentifier(), new Pair<BlockPos, TileEntityPooCollector>(aTempPos, aTile));
	}
	
	public static void stopThread() {
		mThread.canRun = false;
	}
	
	
	@Override
	public void run() {
		
		if (!isRunning) {
			isRunning = true;
		}
		else {
			return;
		}

		while (canRun) {			
			if (mPooCollectors.isEmpty() || GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED) {
				continue;
			} else {
				internalTickCounter = Utils.getTicksFromSeconds(
						Utils.getSecondsFromMillis(Utils.getMillisSince(INIT_TIME, System.currentTimeMillis())));
				if (internalTickCounter % 100 == 0) {
					for (Pair<BlockPos, TileEntityPooCollector> pair : mPooCollectors.values()) {
						if (pair != null) {
							BlockPos p = pair.getKey();
							if (p != null) {
								if (p.world != null) {
									World w = p.world;									
									if (w == null) {
										continue;
									}									
									Chunk c = w.getChunkFromBlockCoords(p.xPos, p.zPos);
									if (c != null) {
										if (c.isChunkLoaded) {
											int startX = p.xPos - 2;
											int startY = p.yPos;
											int startZ = p.zPos - 2;
											int endX = p.xPos + 3;
											int endY = p.yPos + 5;
											int endZ = p.zPos + 3;
											AxisAlignedBB box = AxisAlignedBB.getBoundingBox(startX, startY, startZ,
													endX, endY, endZ);
											if (box != null) {
												@SuppressWarnings("unchecked")
												List<EntityAnimal> animals = w.getEntitiesWithinAABB(EntityAnimal.class, box);
												if (animals != null && !animals.isEmpty()) {
													pair.getValue().onPostTick(animals);
												}
											} else {
												continue;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}		
	
	
}
