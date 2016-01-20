package binnie.extrabees.apiary;

import binnie.core.machines.Machine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.component.IInteraction.RightClick;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.proxy.ExtraBeesProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ComponentExtraBeeGUI
  extends MachineComponent
  implements IInteraction.RightClick
{
  ExtraBeeGUID id;
  
  public ComponentExtraBeeGUI(Machine machine, ExtraBeeGUID id)
  {
    super(machine);
    this.id = id;
  }
  
  public void onRightClick(World world, EntityPlayer player, int x, int y, int z)
  {
    ExtraBees.proxy.openGui(this.id, player, x, y, z);
  }
}
