package binnie.core.machines.transfer;

import binnie.core.machines.Machine;
import binnie.core.machines.inventory.IInventorySlots;
import binnie.core.machines.inventory.IValidatedTankContainer;
import binnie.core.machines.inventory.InventorySlot;
import binnie.core.machines.power.ITankMachine;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

public class TransferRequest
{
  private ItemStack itemToTransfer = null;
  private ItemStack returnItem = null;
  private IInventory origin;
  private IInventory destination;
  private int[] targetSlots = new int[0];
  private int[] targetTanks = new int[0];
  private boolean transferLiquids = true;
  private boolean ignoreReadOnly = false;
  
  public TransferRequest(ItemStack toTransfer, IInventory destination)
  {
    int[] target = new int[destination.getSizeInventory()];
    for (int i = 0; i < target.length; i++) {
      target[i] = i;
    }
    int[] targetTanks = new int[0];
    if ((destination instanceof ITankMachine))
    {
      targetTanks = new int[((ITankMachine)destination).getTanks().length];
      for (int i = 0; i < targetTanks.length; i++) {
        targetTanks[i] = i;
      }
    }
    if (toTransfer != null)
    {
      setItemToTransfer(toTransfer.copy());
      setReturnItem(toTransfer.copy());
    }
    setOrigin(null);
    setDestination(destination);
    setTargetSlots(target);
    setTargetTanks(targetTanks);
    this.transferLiquids = true;
  }
  
  private void setItemToTransfer(ItemStack itemToTransfer)
  {
    this.itemToTransfer = itemToTransfer;
  }
  
  private void setReturnItem(ItemStack returnItem)
  {
    this.returnItem = returnItem;
  }
  
  public TransferRequest setOrigin(IInventory origin)
  {
    this.origin = origin;
    return this;
  }
  
  private void setDestination(IInventory destination)
  {
    this.destination = destination;
  }
  
  public TransferRequest setTargetSlots(int[] targetSlots)
  {
    this.targetSlots = targetSlots;
    return this;
  }
  
  public TransferRequest setTargetTanks(int[] targetTanks)
  {
    this.targetTanks = targetTanks;
    return this;
  }
  
  public TransferRequest ignoreValidation()
  {
    this.ignoreReadOnly = true;
    return this;
  }
  
  public ItemStack getReturnItem()
  {
    return this.returnItem;
  }
  
  public ItemStack transfer(boolean doAdd)
  {
    ItemStack item = this.returnItem;
    if ((item == null) || (this.destination == null)) {
      return null;
    }
    if ((this.transferLiquids) && ((this.destination instanceof ITankMachine))) {
      for (int tankID : this.targetTanks)
      {
        item = transferToTank(item, this.origin, (ITankMachine)this.destination, tankID, doAdd);
        if (item != null) {
          item = transferFromTank(item, this.origin, (ITankMachine)this.destination, tankID, doAdd);
        }
      }
    }
    if (item != null) {
      for (int slot : this.targetSlots) {
        if ((this.destination.isItemValidForSlot(slot, item)) || (this.ignoreReadOnly)) {
          if ((!(this.destination instanceof IInventorySlots)) || (((IInventorySlots)this.destination).getSlot(slot) == null) || (!((IInventorySlots)this.destination).getSlot(slot).isRecipe())) {
            if (this.destination.getStackInSlot(slot) != null) {
              if (item.isStackable())
              {
                ItemStack merged = this.destination.getStackInSlot(slot).copy();
                ItemStack[] newStacks = mergeStacks(item.copy(), merged.copy());
                item = newStacks[0];
                if (!areItemsEqual(merged, newStacks[1])) {
                  this.insertedSlots.add(new TransferSlot(slot, this.destination));
                }
                if (doAdd) {
                  this.destination.setInventorySlotContents(slot, newStacks[1]);
                }
                if (item == null) {
                  return null;
                }
              }
            }
          }
        }
      }
    }
    if (item != null) {
      for (int slot : this.targetSlots) {
        if ((this.destination.isItemValidForSlot(slot, item)) || (this.ignoreReadOnly)) {
          if ((!(this.destination instanceof IInventorySlots)) || (((IInventorySlots)this.destination).getSlot(slot) == null) || (!((IInventorySlots)this.destination).getSlot(slot).isRecipe())) {
            if ((this.destination.getStackInSlot(slot) == null) && (item != null))
            {
              this.insertedSlots.add(new TransferSlot(slot, this.destination));
              if (doAdd) {
                this.destination.setInventorySlotContents(slot, item.copy());
              }
              return null;
            }
          }
        }
      }
    }
    setReturnItem(item);
    return getReturnItem();
  }
  
  private static boolean areItemsEqual(ItemStack merged, ItemStack itemstack)
  {
    return (ItemStack.areItemStackTagsEqual(itemstack, merged)) && (itemstack.isItemEqual(merged));
  }
  
  public static ItemStack[] mergeStacks(ItemStack itemstack, ItemStack merged)
  {
    if (areItemsEqual(itemstack, merged))
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
  
  private ItemStack transferToTank(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    item = transferToTankUsingContainerData(item, origin, destination, tankID, doAdd);
    item = transferToTankUsingFluidContainer(item, origin, destination, tankID, doAdd);
    return item;
  }
  
  private ItemStack transferToTankUsingFluidContainer(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    if ((item == null) || (!(item.getItem() instanceof IFluidContainerItem))) {
      return item;
    }
    IFluidContainerItem fluidContainer = (IFluidContainerItem)item.getItem();
    FluidStack fluid = fluidContainer.getFluid(item);
    if (fluid == null) {
      return item;
    }
    IFluidTank tank = destination.getTanks()[tankID];
    
    IValidatedTankContainer validated = (IValidatedTankContainer)Machine.getInterface(IValidatedTankContainer.class, destination);
    if ((validated != null) && ((!validated.isLiquidValidForTank(fluid, tankID)) || (validated.isTankReadOnly(tankID)))) {
      return item;
    }
    int maxFill = tank.fill(fluid, false);
    
    FluidStack toTake = fluidContainer.drain(item, maxFill, true);
    if (doAdd) {
      tank.fill(toTake, true);
    }
    return item;
  }
  
  private ItemStack transferToTankUsingContainerData(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
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
    if ((validated != null) && (
      (!validated.isLiquidValidForTank(containerLiquid, tankID)) || (validated.isTankReadOnly(tankID)))) {
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
    TransferRequest containersDump = new TransferRequest(emptyContainers, origin);
    
    ItemStack containersThatCantBeDumped = containersDump.transfer(false);
    if (containersThatCantBeDumped != null) {
      return item;
    }
    if (doAdd)
    {
      FluidStack liquidToFillTank = containerLiquid.copy();
      liquidToFillTank.amount *= numberOfContainersToAdd;
      tank.fill(liquidToFillTank, true);
      containersDump.transfer(true);
    }
    return leftOverContainers;
  }
  
  private ItemStack transferFromTank(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    item = transferFromTankUsingContainerData(item, origin, destination, tankID, doAdd);
    item = transferFromTankUsingFluidContainer(item, origin, destination, tankID, doAdd);
    return item;
  }
  
  private ItemStack transferFromTankUsingFluidContainer(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
  {
    if ((item == null) || (!(item.getItem() instanceof IFluidContainerItem))) {
      return item;
    }
    IFluidContainerItem fluidContainer = (IFluidContainerItem)item.getItem();
    
    IFluidTank tank = destination.getTanks()[tankID];
    
    FluidStack fluid = tank.getFluid();
    if (fluid == null) {
      return item;
    }
    int amount = fluidContainer.fill(item, fluid, false);
    
    amount = Math.min(amount, tank.drain(amount, false) == null ? 0 : tank.drain(amount, false).amount);
    if (amount <= 0) {
      return item;
    }
    fluidContainer.fill(item, tank.drain(amount, doAdd), doAdd);
    
    return item;
  }
  
  private ItemStack transferFromTankUsingContainerData(ItemStack item, IInventory origin, ITankMachine destination, int tankID, boolean doAdd)
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
    FluidStack fluid = null;
    ItemStack filled = null;
    if (containerLiquidData != null)
    {
      fluid = containerLiquidData.fluid;
      filled = containerLiquidData.filledContainer;
    }
    if ((fluid == null) || (filled == null)) {
      return item;
    }
    int maximumExtractedLiquid = item.stackSize * fluid.amount;
    
    FluidStack drainedLiquid = tank.drain(maximumExtractedLiquid, false);
    int amountInTank = drainedLiquid == null ? 0 : drainedLiquid.amount;
    
    int numberOfContainersToFill = amountInTank / fluid.amount;
    if (numberOfContainersToFill > item.stackSize) {
      numberOfContainersToFill = item.stackSize;
    }
    ItemStack leftOverContainers = item.copy();
    leftOverContainers.stackSize -= numberOfContainersToFill;
    if (leftOverContainers.stackSize <= 0) {
      leftOverContainers = null;
    }
    ItemStack filledContainers = filled.copy();
    filledContainers.stackSize = 0;
    filledContainers.stackSize += numberOfContainersToFill;
    if (filledContainers.stackSize <= 0) {
      filledContainers = null;
    }
    TransferRequest containersDump = new TransferRequest(filledContainers, origin);
    
    ItemStack containersThatCantBeDumped = containersDump.transfer(false);
    if (containersThatCantBeDumped != null) {
      return item;
    }
    if (doAdd)
    {
      tank.drain(maximumExtractedLiquid, true);
      containersDump.transfer(true);
    }
    return leftOverContainers;
  }
  
  private List<TransferSlot> insertedSlots = new ArrayList();
  private List<Integer> insertedTanks = new ArrayList();
  
  public List<TransferSlot> getInsertedSlots()
  {
    return this.insertedSlots;
  }
  
  public List<Integer> getInsertedTanks()
  {
    return this.insertedTanks;
  }
  
  public static class TransferSlot
  {
    public int id;
    public IInventory inventory;
    
    public TransferSlot(int id, IInventory inventory)
    {
      this.id = id;
      this.inventory = inventory;
    }
  }
  
  public IInventory getOrigin()
  {
    return this.origin;
  }
  
  public IInventory getDestination()
  {
    return this.destination;
  }
  
  public ItemStack getItemToTransfer()
  {
    return this.itemToTransfer;
  }
  
  public int[] getTargetSlots()
  {
    return this.targetSlots;
  }
  
  public int[] getTargetTanks()
  {
    return this.targetTanks;
  }
}
