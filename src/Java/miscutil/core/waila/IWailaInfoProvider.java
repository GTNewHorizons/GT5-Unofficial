package miscutil.core.waila;

import java.text.NumberFormat;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract interface IWailaInfoProvider
{
  public static final int BIT_BASIC = 1;
  public static final int BIT_COMMON = 2;
  public static final int BIT_DETAILED = 4;
  public static final int ALL_BITS = 7;
  public static NumberFormat fmt = null;
  
  public abstract void getWailaInfo(List<String> paramList, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract int getDefaultDisplayMask(World paramWorld, int paramInt1, int paramInt2, int paramInt3);
}
