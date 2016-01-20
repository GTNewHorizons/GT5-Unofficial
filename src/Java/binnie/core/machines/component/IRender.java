package binnie.core.machines.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.world.World;

public abstract interface IRender
{
  public static abstract interface DisplayTick
  {
    @SideOnly(Side.CLIENT)
    public abstract void onDisplayTick(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Random paramRandom);
  }
  
  public static abstract interface RandomDisplayTick
  {
    @SideOnly(Side.CLIENT)
    public abstract void onRandomDisplayTick(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Random paramRandom);
  }
  
  public static abstract interface Render
  {
    @SideOnly(Side.CLIENT)
    public abstract void renderInWorld(RenderItem paramRenderItem, double paramDouble1, double paramDouble2, double paramDouble3);
  }
}
