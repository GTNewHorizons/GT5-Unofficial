package miscutil.core.multiblock.base.interfaces;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import raisintoast.core.multiblock.IMultiblockPart;

public interface IBaseMultiblockLogic {

	  public abstract boolean isConnected();
	  
	  public abstract IBaseMultiblockController getController();
	  
	  public abstract void validate(World paramWorld, IMultiblockPart paramIMultiblockComponent);
	  
	  public abstract void invalidate(World paramWorld, IMultiblockPart paramIMultiblockComponent);
	  
	  public abstract void onChunkUnload(World paramWorld, IMultiblockPart paramIMultiblockComponent);
	  
	  public abstract void encodeDescriptionPacket(NBTTagCompound paramNBTTagCompound);
	  
	  public abstract void decodeDescriptionPacket(NBTTagCompound paramNBTTagCompound);
	  
	  public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound);
	  
	  public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound);
	
}
