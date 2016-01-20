package binnie.core.machines.inventory;

import binnie.core.machines.Machine;
import binnie.core.machines.power.ITankMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class TransferHandler
{
  public static ItemStack transfer(ItemStack item, IInventory origin, IInventory destination, boolean doAdd)
  {
    ItemStack ret = transferItemToInventory(item, destination, doAdd);
    if ((destination instanceof ITankMachine))
    {
      ret = transferContainerIntoTank(ret, origin, (ITankMachine)destination, doAdd);
      ret = transferTankIntoContainer(ret, origin, (ITankMachine)destination, doAdd);
    }
    return ret;
  }
  
  public static ItemStack transferItemToInventory(ItemStack item, IInventory destination, boolean doAdd)
  {
    if ((item == null) || (destination == null)) {
      return item;
    }
    ItemStack addition = item.copy();
    for (int i = 0; i < destination.getSizeInventory(); i++)
    {
      addition = transferToInventory(addition, destination, new int[] { i }, doAdd, false);
      if (addition == null) {
        return null;
      }
    }
    return addition;
  }
  
  public static ItemStack transferToInventory(ItemStack item, IInventory destination, int[] targetSlots, boolean doAdd, boolean ignoreValidation)
  {
    for (int i : targetSlots) {
      if ((destination.isItemValidForSlot(i, item)) || (ignoreValidation))
      {
        if (destination.getStackInSlot(i) == null)
        {
          if (doAdd) {
            destination.setInventorySlotContents(i, item.copy());
          }
          return null;
        }
        if (item.isStackable())
        {
          ItemStack merged = destination.getStackInSlot(i).copy();
          ItemStack[] newStacks = mergeStacks(item.copy(), merged.copy());
          item = newStacks[0];
          if (doAdd) {
            destination.setInventorySlotContents(i, newStacks[1]);
          }
          if (item == null) {
            return null;
          }
        }
      }
    }
    return item;
  }
  
  public static ItemStack[] mergeStacks(ItemStack itemstack, ItemStack merged)
  {
    if ((ItemStack.areItemStackTagsEqual(itemstack, merged)) && (itemstack.isItemEqual(merged)))
    {
      int space = merged.getMaxStackSize() - merged.stackSize;
      if (space > 0) {
        if (itemstack.stackSize > space)
        {
          itemstack.stackSize -= space;
          merged.stackSize += space;
        }
        else if (itemstack.stackSize <= space)
        {
          merged.stackSize += itemstack.stackSize;
          itemstack = null;
        }
      }
    }
    return new ItemStack[] { itemstack, merged };
  }
  
  public static ItemStack transferContainerIntoTank(ItemStack item, IInventory origin, ITankMachine destination, boolean doAdd)
  {
    if (item == null) {
      return null;
    }
    IFluidTank[] tanks = destination.getTanks();
    ItemStack stack = item.copy();
    for (int i = 0; i < tanks.length; i++) {
      stack = transferToTank(stack, origin, destination, i, doAdd);
    }
    return stack;
  }
  
  public static ItemStack transferTankIntoContainer(ItemStack item, IInventory origin, ITankMachine destination, boolean doAdd)
  {
    if (item == null) {
      return null;
    }
    IFluidTank[] tanks = destination.getTanks();
    ItemStack stack = item.copy();
    for (int i = 0; i < tanks.length; i++) {
      stack = transferFromTank(stack, origin, destination, i, doAdd);
    }
    return stack;
  }
  
  public static ItemStack transferToTank(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    if (item == null) {
      return item;
    }
    FluidStack containerLiquid = null;
    FluidContainerRegistry.FluidContainerData containerLiquidData = null;
    for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
      if (data.filledContainer.isItemEqual(item))
      {
        containerLiquidData = data;
        containerLiquid = data.fluid.copy();
        break;
      }
    }
    if (containerLiquid == null) {
      return item;
    }
    IFluidTank tank = destination.getTanks()[tankID];
    
    IValidatedTankContainer validated = (IValidatedTankContainer)Machine.getInterface(IValidatedTankContainer.class, destination);
    if ((validated != null) && 
      (!validated.isLiquidValidForTank(containerLiquid, tankID))) {
      return item;
    }
    FluidStack largeAmountOfLiquid = containerLiquid.copy();
    largeAmountOfLiquid.amount = tank.getCapacity();
    int amountAdded = tank.fill(largeAmountOfLiquid, false);
    
    int numberOfContainersToAdd = amountAdded / containerLiquid.amount;
    if (numberOfContainersToAdd > item.stackSize) {
      numberOfContainersToAdd = item.stackSize;
    }
    ItemStack leftOverContainers = item.copy();
    leftOverContainers.stackSize -= numberOfContainersToAdd;
    if (leftOverContainers.stackSize <= 0) {
      leftOverContainers = null;
    }
    ItemStack emptyContainers = containerLiquidData.emptyContainer.copy();
    emptyContainers.stackSize = 0;
    emptyContainers.stackSize += numberOfContainersToAdd;
    if (emptyContainers.stackSize <= 0) {
      emptyContainers = null;
    }
    ItemStack containersThatCantBeDumped = transferItemToInventory(emptyContainers, origin, false);
    if (containersThatCantBeDumped != null) {
      return item;
    }
    if (doAdd)
    {
      FluidStack liquidToFillTank = containerLiquid.copy();
      liquidToFillTank.amount *= numberOfContainersToAdd;
      tank.fill(liquidToFillTank, true);
      transferItemToInventory(emptyContainers, origin, true);
    }
    return leftOverContainers;
  }
  
  public static ItemStack transferFromTank(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    if (item == null) {
      return item;
    }
    IFluidTank tank = destination.getTanks()[tankID];
    FluidStack liquidInTank = tank.getFluid();
    if (liquidInTank == null) {
      return item;
    }
    FluidContainerRegistry.FluidContainerData containerLiquidData = null;
    for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
      if ((data.emptyContainer.isItemEqual(item)) && (liquidInTank.isFluidEqual(data.fluid)))
      {
        containerLiquidData = data;
        break;
      }
    }
    if (containerLiquidData == null) {
      return item;
    }
    int maximumExtractedLiquid = item.stackSize * containerLiquidData.fluid.amount;
    
    FluidStack drainedLiquid = tank.drain(maximumExtractedLiquid, false);
    int amountInTank = drainedLiquid == null ? 0 : drainedLiquid.amount;
    
    int numberOfContainersToFill = amountInTank / containerLiquidData.fluid.amount;
    if (numberOfContainersToFill > item.stackSize) {
      numberOfContainersToFill = item.stackSize;
    }
    ItemStack leftOverContainers = item.copy();
    leftOverContainers.stackSize -= numberOfContainersToFill;
    if (leftOverContainers.stackSize <= 0) {
      leftOverContainers = null;
    }
    ItemStack filledContainers = containerLiquidData.filledContainer.copy();
    filledContainers.stackSize = 0;
    filledContainers.stackSize += numberOfContainersToFill;
    if (filledContainers.stackSize <= 0) {
      filledContainers = null;
    }
    ItemStack containersThatCantBeDumped = transferItemToInventory(filledContainers, origin, false);
    if (containersThatCantBeDumped != null) {
      return item;
    }
    if (doAdd)
    {
      tank.drain(maximumExtractedLiquid, true);
      transferItemToInventory(filledContainers, origin, true);
    }
    return leftOverContainers;
  }
}
