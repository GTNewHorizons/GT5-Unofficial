package binnie.extrabees.apiary;

import binnie.core.machines.Machine;
import binnie.core.machines.MachineComponent;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IIndividual;
import net.minecraft.item.ItemStack;

public class ComponentBeeModifier
  extends MachineComponent
  implements IBeeModifier, IBeeListener
{
  public ComponentBeeModifier(Machine machine)
  {
    super(machine);
  }
  
  public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
  
  public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return 1.0F;
  }
  
  public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return 1.0F;
  }
  
  public float getProductionModifier(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
  
  public float getFloweringModifier(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
  
  public boolean isSealed()
  {
    return false;
  }
  
  public boolean isSelfLighted()
  {
    return false;
  }
  
  public boolean isSunlightSimulated()
  {
    return false;
  }
  
  public boolean isHellish()
  {
    return false;
  }
  
  public void onQueenChange(ItemStack queen) {}
  
  public void wearOutEquipment(int amount) {}
  
  public void onQueenDeath(IBee queen) {}
  
  public void onPostQueenDeath(IBee queen) {}
  
  public boolean onPollenRetrieved(IBee queen, IIndividual pollen, boolean isHandled)
  {
    return false;
  }
  
  public boolean onEggLaid(IBee queen)
  {
    return false;
  }
  
  public float getGeneticDecay(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
}
