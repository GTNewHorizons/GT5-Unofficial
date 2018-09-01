package gtPlusPlus.core.handler.chunkloading;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ChunkManager;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.network.PacketHandler;
import net.minecraftforge.common.ForgeChunkManager;

public class ChunkLoading {
	
	private final ChunkLoading instance;
	
	public ChunkLoading() {
		instance = this;
	}
	
	public ChunkLoading getInstance() {
		return this.instance;
	}


	public void preInit(final FMLPreInitializationEvent event) {
		PacketHandler.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(GTplusplus.instance, ChunkManager.getInstance());
		Utils.registerEvent(ChunkManager.getInstance());
	}
	

	public void init(final FMLInitializationEvent event) {
		
	}
	

	public void postInit(final FMLPostInitializationEvent event) {
		
	}
	

	public synchronized void serverStarting(final FMLServerStartingEvent event) {

	}

	public void serverStopping(final FMLServerStoppingEvent event){
		//Chunkload Handler
		if (ChunkManager.mChunkLoaderManagerMap.size() > 0) {
			Logger.INFO("Clearing Chunk Loaders.");
			ChunkManager.clearInternalMaps();
		}
	}

}
