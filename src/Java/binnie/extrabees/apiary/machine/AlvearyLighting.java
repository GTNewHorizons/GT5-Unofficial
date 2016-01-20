package binnie.extrabees.apiary.machine;

import binnie.core.machines.Machine;
import binnie.craftgui.minecraft.IMachineInformation;
import binnie.extrabees.apiary.ComponentBeeModifier;
import binnie.extrabees.core.ExtraBeeTexture;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;

public class AlvearyLighting
{
  public static class PackageAlvearyLighting
    extends AlvearyMachine.AlvearyPackage
    implements IMachineInformation
  {
    public PackageAlvearyLighting()
    {
      super(ExtraBeeTexture.AlvearyLighting.getTexture(), false);
    }
    
    public void createMachine(Machine machine)
    {
      new AlvearyLighting.ComponentLighting(machine);
    }
  }
  
  public static class ComponentLighting
    extends ComponentBeeModifier
    implements IBeeModifier, IBeeListener
  {
    public ComponentLighting(Machine machine)
    {
      super();
    }
    
    public boolean isSelfLighted()
    {
      return true;
    }
  }
}
