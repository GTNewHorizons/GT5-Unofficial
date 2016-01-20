package binnie.core.genetics;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IIndividual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

public class VirtualBeeHousing
  extends VirtualHousing
  implements IBeeHousing
{
  public VirtualBeeHousing(EntityPlayer player)
  {
    super(player);
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
  
  public ItemStack getQueen()
  {
    return null;
  }
  
  public ItemStack getDrone()
  {
    return null;
  }
  
  public void setQueen(ItemStack itemstack) {}
  
  public void setDrone(ItemStack itemstack) {}
  
  public boolean canBreed()
  {
    return true;
  }
  
  public boolean addProduct(ItemStack product, boolean all)
  {
    return false;
  }
  
  public void wearOutEquipment(int amount) {}
  
  public void onQueenChange(ItemStack queen) {}
  
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
    return getBiomeId() == BiomeGenBase.hell.biomeID;
  }
  
  public float getFloweringModifier(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
  
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
