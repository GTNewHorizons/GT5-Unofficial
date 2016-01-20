package binnie.core.machines.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract interface IInteraction
{
  public static abstract interface ChunkUnload
  {
    public abstract void onChunkUnload();
  }
  
  public static abstract interface Invalidation
  {
    public abstract void onInvalidation();
  }
  
  public static abstract interface RightClick
  {
    public abstract void onRightClick(World paramWorld, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3);
  }
}
