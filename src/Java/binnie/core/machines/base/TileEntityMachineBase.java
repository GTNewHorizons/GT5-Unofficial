package binnie.core.machines.base;

import binnie.core.machines.Machine;
import binnie.core.machines.inventory.IInventoryMachine;
import binnie.core.machines.inventory.TankSlot;
import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.ITankMachine;
import binnie.core.machines.power.PowerInfo;
import binnie.core.machines.power.PowerInterface;
import binnie.core.machines.power.TankInfo;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class TileEntityMachineBase
  extends TileEntity
  implements IInventoryMachine, ITankMachine, IPoweredMachine
{
  public IInventoryMachine getInventory()
  {
    IInventoryMachine inv = (IInventoryMachine)Machine.getInterface(IInventoryMachine.class, this);
    return (inv == null) || (inv == this) ? new DefaultInventory() : inv;
  }
  
  public ITankMachine getTankContainer()
  {
    ITankMachine inv = (ITankMachine)Machine.getInterface(ITankMachine.class, this);
    return (inv == null) || (inv == this) ? new DefaultTankContainer() : inv;
  }
  
  public IPoweredMachine getPower()
  {
    IPoweredMachine inv = (IPoweredMachine)Machine.getInterface(IPoweredMachine.class, this);
    return (inv == null) || (inv == this) ? new DefaultPower() : inv;
  }
  
  public int getSizeInventory()
  {
    return getInventory().getSizeInventory();
  }
  
  public ItemStack getStackInSlot(int index)
  {
    return getInventory().getStackInSlot(index);
  }
  
  public ItemStack decrStackSize(int index, int amount)
  {
    return getInventory().decrStackSize(index, amount);
  }
  
  public ItemStack getStackInSlotOnClosing(int var1)
  {
    return getInventory().getStackInSlotOnClosing(var1);
  }
  
  public void setInventorySlotContents(int index, ItemStack itemStack)
  {
    getInventory().setInventorySlotContents(index, itemStack);
  }
  
  public String getInventoryName()
  {
    return getInventory().getInventoryName();
  }
  
  public int getInventoryStackLimit()
  {
    return getInventory().getInventoryStackLimit();
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
    if (isInvalid()) {
      return false;
    }
    if (getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
      return false;
    }
    if (entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) > 64.0D) {
      return false;
    }
    return getInventory().isUseableByPlayer(entityplayer);
  }
  
  public void openInventory()
  {
    getInventory().openInventory();
  }
  
  public void closeInventory()
  {
    getInventory().closeInventory();
  }
  
  public boolean hasCustomInventoryName()
  {
    return getInventory().hasCustomInventoryName();
  }
  
  public void markDirty()
  {
    super.markDirty();
    getInventory().markDirty();
  }
  
  public boolean isItemValidForSlot(int slot, ItemStack itemStack)
  {
    return getInventory().isItemValidForSlot(slot, itemStack);
  }
  
  public int[] getAccessibleSlotsFromSide(int var1)
  {
    return getInventory().getAccessibleSlotsFromSide(var1);
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
    return getInventory().canInsertItem(i, itemstack, j);
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
    return getInventory().canExtractItem(i, itemstack, j);
  }
  
  public boolean isReadOnly(int slot)
  {
    return getInventory().isReadOnly(slot);
  }
  
  public PowerInfo getPowerInfo()
  {
    return getPower().getPowerInfo();
  }
  
  public TankInfo[] getTankInfos()
  {
    return getTankContainer().getTankInfos();
  }
  
  public boolean isTankReadOnly(int tank)
  {
    return getTankContainer().isTankReadOnly(tank);
  }
  
  public boolean isLiquidValidForTank(FluidStack liquid, int tank)
  {
    return getTankContainer().isLiquidValidForTank(liquid, tank);
  }
  
  public TankSlot addTank(int index, String name, int capacity)
  {
    return getTankContainer().addTank(index, name, capacity);
  }
  
  public IFluidTank getTank(int index)
  {
    return getTankContainer().getTank(index);
  }
  
  public TankSlot getTankSlot(int index)
  {
    return getTankContainer().getTankSlot(index);
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return getTankContainer().fill(from, resource, doFill);
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    return getTankContainer().drain(from, resource, doDrain);
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return getTankContainer().drain(from, maxDrain, doDrain);
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return getTankContainer().canFill(from, fluid);
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return getTankContainer().canDrain(from, fluid);
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    return getTankContainer().getTankInfo(from);
  }
  
  public IFluidTank[] getTanks()
  {
    return getTankContainer().getTanks();
  }
  
  @Optional.Method(modid="IC2")
  public double getDemandedEnergy()
  {
    return getPower().getDemandedEnergy();
  }
  
  @Optional.Method(modid="IC2")
  public int getSinkTier()
  {
    return getPower().getSinkTier();
  }
  
  @Optional.Method(modid="IC2")
  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    return getPower().injectEnergy(directionFrom, amount, voltage);
  }
  
  @Optional.Method(modid="IC2")
  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return getPower().acceptsEnergyFrom(emitter, direction);
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return getPower().receiveEnergy(from, maxReceive, simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return getPower().extractEnergy(from, maxExtract, simulate);
  }
  
  public int getEnergyStored(ForgeDirection from)
  {
    return getPower().getEnergyStored(from);
  }
  
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return getPower().getMaxEnergyStored(from);
  }
  
  public boolean canConnectEnergy(ForgeDirection from)
  {
    return getPower().canConnectEnergy(from);
  }
  
  public PowerInterface getInterface()
  {
    return getPower().getInterface();
  }
}
