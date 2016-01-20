package binnie.core.machines;

import binnie.core.BinnieCore;
import binnie.core.machines.inventory.IChargedSlots;
import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.IProcess;
import binnie.core.machines.power.ITankMachine;
import binnie.core.machines.power.PowerInterface;
import binnie.core.machines.power.PowerSystem;
import binnie.core.proxy.BinnieProxy;
import binnie.core.util.ItemStackSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class MachineUtil
{
  private IMachine machine;
  
  public MachineUtil(IMachine machine)
  {
    this.machine = machine;
  }
  
  public IInventory getInventory()
  {
    return (IInventory)this.machine.getInterface(IInventory.class);
  }
  
  public ITankMachine getTankContainer()
  {
    return (ITankMachine)this.machine.getInterface(ITankMachine.class);
  }
  
  public IPoweredMachine getPoweredMachine()
  {
    return (IPoweredMachine)this.machine.getInterface(IPoweredMachine.class);
  }
  
  public boolean isSlotEmpty(int slot)
  {
    return getInventory().getStackInSlot(slot) == null;
  }
  
  public IFluidTank getTank(int id)
  {
    return getTankContainer().getTanks()[id];
  }
  
  public boolean spaceInTank(int id, int amount)
  {
    IFluidTank tank = getTank(id);
    int space = tank.getCapacity() - tank.getFluidAmount();
    return amount <= space;
  }
  
  public ItemStack getStack(int slot)
  {
    return getInventory().getStackInSlot(slot);
  }
  
  public void deleteStack(int slot)
  {
    setStack(slot, null);
  }
  
  public ItemStack decreaseStack(int slotWood, int amount)
  {
    return getInventory().decrStackSize(slotWood, amount);
  }
  
  public void setStack(int slot, ItemStack stack)
  {
    getInventory().setInventorySlotContents(slot, stack);
  }
  
  public void fillTank(int id, FluidStack liquidStack)
  {
    IFluidTank tank = getTank(id);
    tank.fill(liquidStack, true);
  }
  
  public void addStack(int slot, ItemStack addition)
  {
    if (isSlotEmpty(slot))
    {
      setStack(slot, addition);
    }
    else
    {
      ItemStack merge = getStack(slot);
      if ((merge.isItemEqual(addition)) && (merge.stackSize + addition.stackSize <= merge.getMaxStackSize()))
      {
        merge.stackSize += addition.stackSize;
        setStack(slot, merge);
      }
    }
  }
  
  public FluidStack drainTank(int tank, int amount)
  {
    return getTank(tank).drain(amount, true);
  }
  
  public boolean liquidInTank(int tank, int amount)
  {
    return (getTank(tank).drain(amount, false) != null) && (getTank(tank).drain(amount, false).amount == amount);
  }
  
  public void damageItem(int slot, int damage)
  {
    ItemStack item = getStack(slot);
    if (damage < 0) {
      item.setItemDamage(Math.max(0, item.getItemDamage() + damage));
    } else if (item.attemptDamageItem(damage, new Random())) {
      setStack(slot, null);
    }
    setStack(slot, item);
  }
  
  public boolean isTankEmpty(int tankInput)
  {
    return getTank(tankInput).getFluidAmount() == 0;
  }
  
  public FluidStack getFluid(int tankInput)
  {
    return getTank(tankInput).getFluid() == null ? null : getTank(tankInput).getFluid();
  }
  
  public ItemStack[] getStacks(int[] slotGrains)
  {
    ItemStack[] stacks = new ItemStack[slotGrains.length];
    for (int i = 0; i < slotGrains.length; i++) {
      stacks[i] = getStack(slotGrains[i]);
    }
    return stacks;
  }
  
  public ItemStack hasIngredients(int recipe, int[] inventory)
  {
    return null;
  }
  
  public boolean hasIngredients(int[] recipe, int[] inventory)
  {
    ItemStackSet requiredStacks = new ItemStackSet();
    for (ItemStack stack : getStacks(recipe)) {
      requiredStacks.add(stack);
    }
    ItemStackSet inventoryStacks = new ItemStackSet();
    for (ItemStack stack : getStacks(inventory)) {
      inventoryStacks.add(stack);
    }
    requiredStacks.removeAll(inventoryStacks);
    
    return requiredStacks.isEmpty();
  }
  
  public void useEnergyMJ(float powerUsage)
  {
    getPoweredMachine().getInterface().useEnergy(PowerSystem.MJ, powerUsage, true);
  }
  
  public boolean hasEnergyMJ(float powerUsage)
  {
    return getPoweredMachine().getInterface().useEnergy(PowerSystem.MJ, powerUsage, false) >= powerUsage;
  }
  
  public float getSlotCharge(int slot)
  {
    return ((IChargedSlots)this.machine.getInterface(IChargedSlots.class)).getCharge(slot);
  }
  
  public void useCharge(int slot, float loss)
  {
    ((IChargedSlots)this.machine.getInterface(IChargedSlots.class)).alterCharge(slot, -loss);
  }
  
  public Random getRandom()
  {
    return new Random();
  }
  
  public void refreshBlock()
  {
    this.machine.getWorld().markBlockForUpdate(this.machine.getTileEntity().xCoord, this.machine.getTileEntity().yCoord, this.machine.getTileEntity().zCoord);
  }
  
  public IProcess getProcess()
  {
    return (IProcess)this.machine.getInterface(IProcess.class);
  }
  
  public List<ItemStack> getNonNullStacks(int[] slotacclimatiser)
  {
    List<ItemStack> stacks = new ArrayList();
    for (ItemStack stack : getStacks(slotacclimatiser)) {
      if (stack != null) {
        stacks.add(stack);
      }
    }
    return stacks;
  }
  
  public boolean isServer()
  {
    return BinnieCore.proxy.isSimulating(this.machine.getWorld());
  }
}
