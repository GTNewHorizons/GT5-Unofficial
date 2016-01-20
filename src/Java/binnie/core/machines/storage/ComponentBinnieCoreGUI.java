package binnie.core.machines.storage;

import binnie.core.BinnieCore;
import binnie.core.gui.BinnieCoreGUI;
import binnie.core.machines.Machine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.component.IInteraction.RightClick;
import binnie.core.proxy.BinnieProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

class ComponentBinnieCoreGUI
  extends MachineComponent
  implements IInteraction.RightClick
{
  private BinnieCoreGUI id;
  
  public ComponentBinnieCoreGUI(Machine machine, BinnieCoreGUI id)
  {
    super(machine);
    this.id = id;
  }
  
  public void onRightClick(World world, EntityPlayer player, int x, int y, int z)
  {
    BinnieCore.proxy.openGui(this.id, player, x, y, z);
  }
}
