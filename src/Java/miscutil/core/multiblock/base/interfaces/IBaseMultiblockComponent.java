package miscutil.core.multiblock.base.interfaces;

import net.minecraft.util.ChunkCoordinates;

import com.mojang.authlib.GameProfile;

public interface IBaseMultiblockComponent {
	  public abstract ChunkCoordinates getCoordinates();
	  
	  public abstract GameProfile getOwner();
	  
	  public abstract IBaseMultiblockLogic getMultiblockLogic();
	  
	  public abstract void onMachineAssembled(IBaseMultiblockController paramIMultiblockController, ChunkCoordinates paramChunkCoordinates1, ChunkCoordinates paramChunkCoordinates2);
	  
	  public abstract void onMachineBroken();
	}
