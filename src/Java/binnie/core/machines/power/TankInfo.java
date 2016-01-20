package binnie.core.machines.power;

import forestry.api.core.INBTTagable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class TankInfo
  implements INBTTagable
{
  public FluidStack liquid;
  private float capacity = 0.0F;
  
  public TankInfo(IFluidTank tank)
  {
    this.capacity = tank.getCapacity();
    this.liquid = tank.getFluid();
  }
  
  public TankInfo() {}
  
  public float getAmount()
  {
    return this.liquid == null ? 0.0F : this.liquid.amount;
  }
  
  public float getCapacity()
  {
    return this.capacity;
  }
  
  public boolean isEmpty()
  {
    return this.liquid == null;
  }
  
  public IIcon getIcon()
  {
    return this.liquid.getFluid().getStillIcon();
  }
  
  public String getName()
  {
    return this.liquid == null ? "" : this.liquid.getFluid().getLocalizedName();
  }
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    this.capacity = nbt.getInteger("capacity");
    if (nbt.hasKey("liquid")) {
      this.liquid = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("liquid"));
    }
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    nbt.setInteger("capacity", (int)getCapacity());
    if (this.liquid == null) {
      return;
    }
    NBTTagCompound tag = new NBTTagCompound();
    this.liquid.writeToNBT(tag);
    nbt.setTag("liquid", tag);
  }
  
  public static TankInfo[] get(ITankMachine machine)
  {
    TankInfo[] info = new TankInfo[machine.getTanks().length];
    for (int i = 0; i < info.length; i++) {
      info[i] = new TankInfo(machine.getTanks()[i]);
    }
    return info;
  }
}
