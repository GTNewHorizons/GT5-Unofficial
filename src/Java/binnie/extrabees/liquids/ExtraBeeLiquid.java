package binnie.extrabees.liquids;

import binnie.Binnie;
import binnie.core.liquid.FluidContainer;
import binnie.core.liquid.ILiquidType;
import binnie.core.liquid.ManagerLiquid;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

public enum ExtraBeeLiquid
  implements ILiquidType
{
  ACID("acid", 11528985),  POISON("poison", 15406315),  GLACIAL("liquidnitrogen", 9881800);
  
  String ident;
  IIcon icon;
  int colour;
  
  private ExtraBeeLiquid(String ident, int colour)
  {
    this.ident = ident;
    this.colour = colour;
  }
  
  public IIcon getIcon()
  {
    return this.icon;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcon(IIconRegister register)
  {
    this.icon = ExtraBees.proxy.getIcon(register, "liquids/" + getIdentifier());
  }
  
  public String getName()
  {
    return ExtraBees.proxy.localise(toString().toLowerCase());
  }
  
  public String getIdentifier()
  {
    return this.ident;
  }
  
  public int getColour()
  {
    return 16777215;
  }
  
  public FluidStack get(int amount)
  {
    return Binnie.Liquid.getLiquidStack(this.ident, amount);
  }
  
  public int getTransparency()
  {
    return 255;
  }
  
  public boolean canPlaceIn(FluidContainer container)
  {
    return true;
  }
  
  public boolean showInCreative(FluidContainer container)
  {
    return (container == FluidContainer.Bucket) || (container == FluidContainer.Can) || (container == FluidContainer.Capsule) || (container == FluidContainer.Refractory);
  }
  
  public int getContainerColour()
  {
    return this.colour;
  }
}
