package gtPlusPlus.xmod.ic2.block.kieticgenerator.container;

import ic2.core.ContainerFullInv;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.slot.SlotInvSlot;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerKineticWindgenerator
  extends ContainerFullInv<TileEntityWindKineticGenerator>
{
  public ContainerKineticWindgenerator(EntityPlayer entityPlayer, TileEntityWindKineticGenerator tileEntity1)
  {
    super(entityPlayer, tileEntity1, 166);
    
    addSlotToContainer(new SlotInvSlot(tileEntity1.rotorSlot, 0, 80, 26));
  }
  
  public List<String> getNetworkedFields()
  {
    List<String> ret = super.getNetworkedFields();
    ret.add("windStrength");
    return ret;
  }
}
