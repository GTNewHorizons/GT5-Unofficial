package binnie.core.machines.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import binnie.core.machines.IMachine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.transfer.TransferRequest;

public class ComponentInventoryTransfer
  extends MachineComponent
{
  public ComponentInventoryTransfer(IMachine machine)
  {
    super(machine);
  }
  
  public abstract class Transfer
  {
    protected ComponentInventoryTransfer.Condition condition;
    protected IMachine machine;
    
    private Transfer(IMachine machine)
    {
      this.machine = machine;
    }
    
    public final void transfer(IInventory inv)
    {
      if ((this.condition == null) || (fufilled(inv))) {
        doTransfer(inv);
      }
    }
    
    protected boolean fufilled(IInventory inv)
    {
      return true;
    }
    
    protected void doTransfer(IInventory inv) {}
    
    public final Transfer setCondition(ComponentInventoryTransfer.Condition condition)
    {
      this.condition = condition;
      condition.transfer = this;
      return this;
    }
    
    public final IMachine getMachine()
    {
      return this.machine;
    }
  }
  
  private class Restock
    extends ComponentInventoryTransfer.Transfer
  {
    int[] buffer;
    int destination;
    int limit;
    
    private Restock(IMachine machine, int[] buffer, int destination, int limit)
    {
      super(machine);
      this.buffer = buffer;
      this.destination = destination;
      this.limit = limit;
    }
    
    private Restock(IMachine machine, int[] buffer, int destination)
    {
      this(machine, buffer, destination, 64);
    }
    
    protected void doTransfer(IInventory inv)
    {
      if (inv.getStackInSlot(this.destination) == null) {
        for (int i : this.buffer) {
          if (inv.getStackInSlot(i) != null)
          {
            ItemStack newStack = inv.decrStackSize(i, this.limit);
            if (newStack != null)
            {
              inv.setInventorySlotContents(this.destination, newStack);
              return;
            }
          }
        }
      }
    }
  }
  
  private class Storage
    extends ComponentInventoryTransfer.Transfer
  {
    int source;
    int[] destination;
    
    private Storage(IMachine machine, int source, int[] destination)
    {
      super(machine);
      this.source = source;
      this.destination = destination;
    }
    
    protected void doTransfer(IInventory inv)
    {
      if (inv.getStackInSlot(this.source) != null) {
        inv.setInventorySlotContents(this.source, new TransferRequest(inv.getStackInSlot(this.source), inv).setTargetSlots(this.destination).ignoreValidation().transfer(true));
      }
    }
    
    protected boolean fufilled(IInventory inv)
    {
      ItemStack stack = inv.getStackInSlot(this.source);
      return (stack != null) && (this.condition.fufilled(stack));
    }
  }
  
  private List<Transfer> transfers = new ArrayList();
  
  public void addRestock(int[] buffer, int destination, int limit)
  {
    this.transfers.add(new Restock(getMachine(), buffer, destination, limit));
  }
  
  public void addRestock(int[] buffer, int destination)
  {
    this.transfers.add(new Restock(getMachine(), buffer, destination));
  }
  
  public void addStorage(int source, int[] destination)
  {
    this.transfers.add(new Storage(getMachine(), source, destination));
  }
  
  public void performTransfer(int source, int[] destination)
  {
    new Storage(getMachine(), source, destination).transfer((IInventory)getMachine().getInterface(IInventoryMachine.class));
  }
  
  public void onUpdate()
  {
    for (Transfer transfer : this.transfers) {
      transfer.transfer((IInventory)getMachine().getInterface(IInventoryMachine.class));
    }
  }
  
  public void addStorage(int source, int[] destination, Condition condition)
  {
    this.transfers.add(new Storage(getMachine(), source, destination).setCondition(condition));
  }
  
  public static abstract class Condition
  {
    public ComponentInventoryTransfer.Transfer transfer;
    
    public abstract boolean fufilled(ItemStack paramItemStack);
  }
}
