package binnie.core.machines.inventory;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public class TankSlot
  extends BaseSlot<FluidStack>
{
  public static final String NameJuice = "Juice Tank";
  public static final String NameWater = "Water Tank";
  public static String NameCupboard = "Cupboard Slot";
  private FluidTank tank;
  
  public TankSlot(int index, String name, int capacity)
  {
    super(index, name);
    this.tank = new FluidTank(capacity);
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    FluidStack liquid = FluidStack.loadFluidStackFromNBT(nbttagcompound);
    
    setContent(liquid);
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    if (getContent() != null) {
      getContent().writeToNBT(nbttagcompound);
    }
  }
  
  public FluidStack getContent()
  {
    return this.tank.getFluid();
  }
  
  public void setContent(FluidStack itemStack)
  {
    this.tank.setFluid(itemStack);
  }
  
  public IFluidTank getTank()
  {
    return this.tank;
  }
  
  public String getName()
  {
    return Binnie.Language.localise(BinnieCore.instance, "gui.slot." + this.unlocName);
  }
}
