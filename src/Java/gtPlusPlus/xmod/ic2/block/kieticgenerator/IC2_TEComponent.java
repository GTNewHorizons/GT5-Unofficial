package gtPlusPlus.xmod.ic2.block.kieticgenerator;

import ic2.core.block.TileEntityBlock;

import java.io.DataInput;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public abstract class IC2_TEComponent
{
  protected final TileEntityBlock parent;
  
  public IC2_TEComponent(TileEntityBlock parent)
  {
    this.parent = parent;
  }
  
  public abstract String getDefaultName();
  
  public void readFromNbt(NBTTagCompound nbt) {}
  
  public NBTTagCompound writeToNbt()
  {
    return null;
  }
  
  public void onLoaded() {}
  
  public void onUnloaded() {}
  
  public void onNeighborUpdate(Block srcBlock) {}
  
  public void onContainerUpdate(String name, EntityPlayerMP player) {}
  
  public void onNetworkUpdate(DataInput is)
    throws IOException
  {}
  
  public boolean enableWorldTick()
  {
    return false;
  }
  
  public void onWorldTick() {}
}
