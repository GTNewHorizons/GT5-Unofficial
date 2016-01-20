package binnie.core.machines.inventory;

import binnie.core.machines.IMachine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.power.ITankMachine;
import binnie.core.machines.power.TankInfo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class ComponentTankContainer
  extends MachineComponent
  implements ITankMachine
{
  private Map<Integer, TankSlot> tanks = new LinkedHashMap();
  
  public ComponentTankContainer(IMachine machine)
  {
    super(machine);
  }
  
  public final TankSlot addTank(int index, String name, int capacity)
  {
    TankSlot tank = new TankSlot(index, name, capacity);
    this.tanks.put(Integer.valueOf(index), tank);
    return tank;
  }
  
  public final int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    int index = getTankIndexToFill(from, resource);
    if (this.tanks.containsKey(Integer.valueOf(index))) {
      return fill(index, resource, doFill);
    }
    return 0;
  }
  
  public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    int index = getTankIndexToDrain(from, null);
    if (this.tanks.containsKey(Integer.valueOf(index))) {
      return drain(index, maxDrain, doDrain);
    }
    return null;
  }
  
  private final int fill(int tankIndex, FluidStack resource, boolean doFill)
  {
    if (!this.tanks.containsKey(Integer.valueOf(tankIndex))) {
      return 0;
    }
    if (!isLiquidValidForTank(resource, tankIndex)) {
      return 0;
    }
    TankSlot tank = (TankSlot)this.tanks.get(Integer.valueOf(tankIndex));
    int filled = tank.getTank().fill(resource, doFill);
    if (filled > 0) {
      markDirty();
    }
    return filled;
  }
  
  private final FluidStack drain(int tankIndex, int maxDrain, boolean doDrain)
  {
    if (!this.tanks.containsKey(Integer.valueOf(tankIndex))) {
      return null;
    }
    TankSlot tank = (TankSlot)this.tanks.get(Integer.valueOf(tankIndex));
    FluidStack drained = tank.getTank().drain(maxDrain, doDrain);
    if (drained != null) {
      markDirty();
    }
    return drained;
  }
  
  private int getTankIndexToFill(ForgeDirection from, FluidStack resource)
  {
    for (TankSlot tank : this.tanks.values()) {
      if ((tank.isValid(resource)) && (tank.canInsert(from)) && ((tank.getContent() == null) || (tank.getContent().isFluidEqual(resource)))) {
        return tank.getIndex();
      }
    }
    return -1;
  }
  
  private int getTankIndexToDrain(ForgeDirection from, FluidStack resource)
  {
    for (TankSlot tank : this.tanks.values()) {
      if ((tank.getContent() != null) && 
        (tank.canExtract(from)) && ((resource == null) || (resource.isFluidEqual(tank.getContent())))) {
        return tank.getIndex();
      }
    }
    return -1;
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    super.readFromNBT(nbttagcompound);
    if (nbttagcompound.hasKey("liquidTanks"))
    {
      NBTTagList tanksNBT = nbttagcompound.getTagList("liquidTanks", 10);
      for (int i = 0; i < tanksNBT.tagCount(); i++)
      {
        NBTTagCompound tankNBT = tanksNBT.getCompoundTagAt(i);
        int index = tankNBT.getInteger("index");
        if (this.tanks.containsKey(Integer.valueOf(index))) {
          ((TankSlot)this.tanks.get(Integer.valueOf(index))).readFromNBT(tankNBT);
        }
      }
    }
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    super.writeToNBT(nbttagcompound);
    
    NBTTagList tanksNBT = new NBTTagList();
    for (Map.Entry<Integer, TankSlot> entry : this.tanks.entrySet())
    {
      NBTTagCompound tankNBT = new NBTTagCompound();
      tankNBT.setInteger("index", ((Integer)entry.getKey()).intValue());
      ((TankSlot)entry.getValue()).writeToNBT(tankNBT);
      tanksNBT.appendTag(tankNBT);
    }
    nbttagcompound.setTag("liquidTanks", tanksNBT);
  }
  
  public boolean isTankReadOnly(int tank)
  {
    return ((TankSlot)this.tanks.get(Integer.valueOf(tank))).isReadOnly();
  }
  
  public boolean isLiquidValidForTank(FluidStack liquid, int tank)
  {
    TankSlot slot = getTankSlot(tank);
    return slot != null;
  }
  
  public TankInfo[] getTankInfos()
  {
    return TankInfo.get(this);
  }
  
  public IFluidTank getTank(int index)
  {
    return getTanks()[index];
  }
  
  public IFluidTank[] getTanks()
  {
    List<IFluidTank> ltanks = new ArrayList();
    for (TankSlot tank : this.tanks.values()) {
      ltanks.add(tank.getTank());
    }
    return (IFluidTank[])ltanks.toArray(new IFluidTank[0]);
  }
  
  public TankSlot getTankSlot(int index)
  {
    return (TankSlot)this.tanks.get(Integer.valueOf(index));
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    int index = getTankIndexToDrain(from, null);
    if (this.tanks.containsKey(Integer.valueOf(index))) {
      return drain(index, resource.amount, doDrain);
    }
    return null;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return fill(from, new FluidStack(fluid, 1), false) > 0;
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return drain(from, new FluidStack(fluid, 1), false) != null;
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    FluidTankInfo[] info = new FluidTankInfo[getTanks().length];
    for (int i = 0; i < info.length; i++) {
      info[i] = new FluidTankInfo(getTanks()[i]);
    }
    return info;
  }
  
  public void markDirty()
  {
    if (getMachine() != null) {
      getMachine().markDirty();
    }
  }
}
