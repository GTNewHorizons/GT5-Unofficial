package binnie.core.liquid;

import net.minecraftforge.fluids.Fluid;

class BinnieFluid
  extends Fluid
{
  private final String name;
  final IFluidType fluidType;
  
  public String getLocalizedName()
  {
    return this.name;
  }
  
  public BinnieFluid(IFluidType fluid)
  {
    super(fluid.getIdentifier());
    this.fluidType = fluid;
    this.name = fluid.getName();
  }
  
  public int getColor()
  {
    return this.fluidType.getColour();
  }
  
  public IFluidType getType()
  {
    return this.fluidType;
  }
}
