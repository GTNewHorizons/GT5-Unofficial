package binnie.core.liquid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

public abstract interface IFluidType
{
  public abstract IIcon getIcon();
  
  public abstract void registerIcon(IIconRegister paramIIconRegister);
  
  public abstract String getName();
  
  public abstract String getIdentifier();
  
  public abstract FluidStack get(int paramInt);
  
  public abstract int getColour();
  
  public abstract int getContainerColour();
  
  public abstract int getTransparency();
  
  public abstract boolean canPlaceIn(FluidContainer paramFluidContainer);
  
  public abstract boolean showInCreative(FluidContainer paramFluidContainer);
}
