package binnie.core.genetics;

import forestry.api.genetics.IIndividual;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IButterflyNursery;
import net.minecraft.entity.player.EntityPlayer;

public class VirtualMothHousing
  extends VirtualHousing
  implements IButterflyNursery
{
  public VirtualMothHousing(EntityPlayer player)
  {
    super(player);
  }
  
  public IButterfly getCaterpillar()
  {
    return null;
  }
  
  public IIndividual getNanny()
  {
    return null;
  }
  
  public void setCaterpillar(IButterfly butterfly) {}
  
  public boolean canNurse(IButterfly butterfly)
  {
    return false;
  }
}
