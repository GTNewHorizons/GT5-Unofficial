package binnie.core.machines.power;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;

import java.util.LinkedList;
import java.util.List;

import miscutil.core.lib.LoadedMods;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import binnie.core.machines.IMachine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.component.IBuildcraft;
import binnie.core.machines.component.IInteraction;
import binnie.core.triggers.TriggerData;
import binnie.core.triggers.TriggerPower;

public class ComponentPowerReceptor
  extends MachineComponent
  implements IPoweredMachine, IBuildcraft.TriggerProvider, IInteraction.ChunkUnload, IInteraction.Invalidation
{
  private boolean registeredToIC2EnergyNet = false;
  float previousPower = 0.0F;
  LinkedList<Float> inputs = new LinkedList();
  static final int inputAverageTicks = 20;
  private PowerInterface container;
  
  public ComponentPowerReceptor(IMachine machine)
  {
    this(machine, 1000);
  }
  
  public ComponentPowerReceptor(IMachine machine, int storage)
  {
    super(machine);
    this.container = new PowerInterface(storage);
    if (!this.registeredToIC2EnergyNet) {
      addToEnergyNet();
    }
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    super.readFromNBT(nbttagcompound);
    this.container.readFromNBT(nbttagcompound);
    if (!this.registeredToIC2EnergyNet) {
      addToEnergyNet();
    }
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    super.writeToNBT(nbttagcompound);
    this.container.writeToNBT(nbttagcompound);
  }
  
  public void onUpdate()
  {
    if ((!this.registeredToIC2EnergyNet) && (!getMachine().getTileEntity().isInvalid())) {
      addToEnergyNet();
    }
  }
  
  public PowerInfo getPowerInfo()
  {
    return new PowerInfo(this, 0.0F);
  }
  
  public final void getTriggers(List<TriggerData> triggers)
  {
    triggers.add(TriggerPower.powerNone(this));
    triggers.add(TriggerPower.powerLow(this));
    triggers.add(TriggerPower.powerMedium(this));
    triggers.add(TriggerPower.powerHigh(this));
    triggers.add(TriggerPower.powerFull(this));
  }
  
  public double getDemandedEnergy()
  {
    return this.container.getEnergySpace(PowerSystem.EU);
  }
  

  public int getSinkTier()
  {
    return 1;
  }
  

  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    this.container.addEnergy(PowerSystem.EU, amount, true);
    return 0.0D;
  }
  

  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return acceptsPowerSystem(PowerSystem.EU);
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return (int)this.container.addEnergy(PowerSystem.RF, maxReceive, !simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return 0;
  }
  
  public int getEnergyStored(ForgeDirection from)
  {
    return (int)this.container.getEnergy(PowerSystem.RF);
  }
  
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return (int)this.container.getCapacity(PowerSystem.RF);
  }
  
  public boolean canConnectEnergy(ForgeDirection from)
  {
    boolean can = acceptsPowerSystem(PowerSystem.RF);
    return can;
  }
  
  public PowerInterface getInterface()
  {
    return this.container;
  }
  
  private boolean acceptsPowerSystem(PowerSystem system)
  {
    return true;
  }
  
  public void onInvalidation()
  {
    removeFromEnergyNet();
  }
  
  public void onChunkUnload()
  {
    removeFromEnergyNet();
  }
  
  private void addToEnergyNet()
  {
    if (getMachine().getWorld() == null) {
      return;
    }
    if (LoadedMods.IndustrialCraft2) {
      do_addToEnergyNet();
    }
  }
  
  private void removeFromEnergyNet()
  {
    if (getMachine().getWorld() == null) {
      return;
    }
    if (LoadedMods.IndustrialCraft2) {
      do_removeFromEnergyNet();
    }
  }
  
  private void do_addToEnergyNet()
  {
    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile)getMachine().getTileEntity()));
    this.registeredToIC2EnergyNet = true;
  }
  
  private void do_removeFromEnergyNet()
  {
    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile)getMachine().getTileEntity()));
    this.registeredToIC2EnergyNet = false;
  }
}
