package binnie.core.machines;

import binnie.Binnie;
import binnie.core.network.packet.MachinePayload;
import forestry.api.core.INBTTagable;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class MachineComponent
  implements INBTTagable
{
  private IMachine machine;
  
  public MachineComponent(IMachine machine)
  {
    setMachine(machine);
    machine.addComponent(this);
  }
  
  public void setMachine(IMachine machine)
  {
    this.machine = machine;
  }
  
  public IMachine getMachine()
  {
    return this.machine;
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound) {}
  
  public void writeToNBT(NBTTagCompound nbttagcompound) {}
  
  public void onUpdate() {}
  
  public Class[] getComponentInterfaces()
  {
    return Binnie.Machine.getComponentInterfaces(getClass());
  }
  
  public void onInventoryUpdate() {}
  
  public final MachinePayload getPayload()
  {
    return new MachinePayload(Binnie.Machine.getNetworkID(getClass()));
  }
  
  public void recieveData(MachinePayload payload) {}
  
  public MachineUtil getUtil()
  {
    return getMachine().getMachineUtil();
  }
  
  public void onDestruction() {}
  
  public IInventory getInventory()
  {
    return (IInventory)getMachine().getInterface(IInventory.class);
  }
}
