package binnie.core.network.packet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

abstract interface IPacketLocation
{
  public abstract TileEntity getTarget(World paramWorld);
  
  public abstract int getX();
  
  public abstract int getY();
  
  public abstract int getZ();
}
