package gtPlusPlus.core.chunkloading;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.interfaces.IChunkLoader;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;

public class StaticChunkFunctions {

	public static void saveNBTDataForTileEntity(IGregTechTileEntity aBaseMetaTileEntity, NBTTagCompound aNBT) { 
		IChunkLoader aTileEntity = getChunkLoader(aBaseMetaTileEntity);   	
		aNBT.setBoolean("chunkLoadingEnabled", aTileEntity.getChunkLoadingActive());
		aNBT.setBoolean("isChunkloading", aTileEntity.getResidingChunk() != null);
		if (aTileEntity.getResidingChunk() != null) {
			aNBT.setInteger("loadedChunkXPos", aTileEntity.getResidingChunk().chunkXPos);
			aNBT.setInteger("loadedChunkZPos", aTileEntity.getResidingChunk().chunkZPos);
		}
	}

	public static void loadNBTDataForTileEntity(IGregTechTileEntity aBaseMetaTileEntity, NBTTagCompound aNBT) {   
		IChunkLoader aTileEntity = getChunkLoader(aBaseMetaTileEntity); 	
		if (aNBT.hasKey("chunkLoadingEnabled")) {
			aTileEntity.setChunkLoadingActive(aNBT.getBoolean("chunkLoadingEnabled"));        	
		}
		if (aNBT.getBoolean("isChunkloading")) {
			aTileEntity.setResidingChunk(new ChunkCoordIntPair(aNBT.getInteger("loadedChunkXPos"), aNBT.getInteger("loadedChunkZPos")));
		}
	}

	public static void onRemoval(IGregTechTileEntity aBaseMetaTileEntity) {
		IChunkLoader aTileEntity = getChunkLoader(aBaseMetaTileEntity);
		if (aTileEntity.getChunkLoadingActive()) {
			GTPP_ChunkManager.releaseTicket((TileEntity)aBaseMetaTileEntity);
		}
	}

	public static boolean onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		IChunkLoader aTileEntity = getChunkLoader(aBaseMetaTileEntity);
		if (aBaseMetaTileEntity.isServerSide() && aTileEntity.getResidingChunk() != null && !aTileEntity.getDoesWorkChunkNeedReload() && !aBaseMetaTileEntity.isAllowedToWork()) {
			// if machine has stopped, stop chunkloading
			GTPP_ChunkManager.releaseTicket((TileEntity)aBaseMetaTileEntity);
			aTileEntity.setDoesWorkChunkNeedReload(true);
			return false;
		}
		return true;
	}

	public static void createInitialWorkingChunk(IGregTechTileEntity aBaseMetaTileEntity, int aChunkX, int aDrillZ) {
		final int centerX = aChunkX >> 4;
		final int centerZ = aDrillZ >> 4;
		IChunkLoader aTileEntity = getChunkLoader(aBaseMetaTileEntity);
		aTileEntity.addChunkToLoadedList(new ChunkCoordIntPair(centerX, centerZ));
		GTPP_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity.getMetaTileEntity(), aTileEntity.getResidingChunk());
		aTileEntity.setDoesWorkChunkNeedReload(false);
	}

	private static final IChunkLoader getChunkLoader(IGregTechTileEntity aTile) {
		return (IChunkLoader) ((IGregTechTileEntity)aTile).getMetaTileEntity();
	}

}
