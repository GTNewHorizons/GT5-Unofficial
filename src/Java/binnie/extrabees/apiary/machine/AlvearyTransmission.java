package binnie.extrabees.apiary.machine;

import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.power.ComponentPowerReceptor;
import binnie.core.machines.power.IPoweredMachine;
import binnie.craftgui.minecraft.IMachineInformation;
import binnie.extrabees.apiary.TileExtraBeeAlveary;
import binnie.extrabees.core.ExtraBeeTexture;
import cofh.api.energy.IEnergyHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class AlvearyTransmission
{
  public static class PackageAlvearyTransmission
    extends AlvearyMachine.AlvearyPackage
    implements IMachineInformation
  {
    public PackageAlvearyTransmission()
    {
      super(ExtraBeeTexture.AlvearyTransmission.getTexture(), false);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentPowerReceptor(machine, 1000);
      new AlvearyTransmission.ComponentTransmission(machine);
    }
  }
  
  public static class ComponentTransmission
    extends MachineComponent
  {
    public ComponentTransmission(Machine machine)
    {
      super();
    }
    
    public void onUpdate()
    {
      super.onUpdate();
      int energy = getUtil().getPoweredMachine().getEnergyStored(ForgeDirection.NORTH);
      if (energy == 0) {
        return;
      }
      TileExtraBeeAlveary tile = (TileExtraBeeAlveary)getMachine().getTileEntity();
      
      List<IEnergyHandler> handlers = new ArrayList();
      for (TileEntity alvearyTile : tile.getAlvearyBlocks()) {
        if (((alvearyTile instanceof IEnergyHandler)) && (alvearyTile != tile)) {
          handlers.add((IEnergyHandler)alvearyTile);
        }
      }
      if (handlers.isEmpty()) {
        return;
      }
      int maxOutput = 500;
      int output = energy / handlers.size();
      if (output > maxOutput) {
        output = maxOutput;
      }
      if (output < 1) {
        output = 1;
      }
      for (IEnergyHandler handler : handlers)
      {
        int recieved = handler.receiveEnergy(ForgeDirection.NORTH, output, false);
        getUtil().getPoweredMachine().extractEnergy(ForgeDirection.NORTH, recieved, false);
        energy = getUtil().getPoweredMachine().getEnergyStored(ForgeDirection.NORTH);
        if (energy == 0) {
          return;
        }
      }
    }
  }
}
