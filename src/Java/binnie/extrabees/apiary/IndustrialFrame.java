package binnie.extrabees.apiary;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum IndustrialFrame
  implements IBeeModifier
{
  Empty("Empty", 5, 0),  Light("Glowstone Lighting", 2, 4),  Rain("Rain Shielding", 2, 4),  Sunlight("Sunlight Simulator", 4, 8),  Soul("Low Grade Mutagen", 5, 15),  Uranium("High Grade Mutagen", 10, 50),  Cage("Meshed Restrainer", 3, 12),  Freedom("Territory Extension", 3, 16),  Honey("Honey Amplifier", 4, 12),  Jelly("Gelatin Amplifier", 8, 36),  Leaf("Pollinator MK I", 3, 15),  Pollen("Pollinator MK II", 7, 25),  Clay("Lifespan Extensor", 2, 10),  Emerald("Eon Simulator", 7, 20),  NetherStar("Immortality Gate", 12, 50),  Poison("Mortality Inhibitor", 8, 18);
  
  String name;
  
  private IndustrialFrame(String name, int wear, int power)
  {
    this.name = name;
    this.wearMod = wear;
    this.power = power;
  }
  
  public static ItemStack getItemStack(Item item, IndustrialFrame frame)
  {
    ItemStack stack = new ItemStack(item);
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("frame", frame.ordinal());
    stack.setTagCompound(nbt);
    return stack;
  }
  
  static
  {
    Light.lighted = true;
    
    Rain.rain = true;
    
    Sunlight.lighted = true;
    Sunlight.sunlight = true;
    
    Soul.mutationMod = 1.3F;
    
    Uranium.mutationMod = 2.0F;
    
    Cage.territoryMod = 0.4F;
    
    Freedom.territoryMod = 1.4F;
    
    Honey.productionMod = 1.4F;
    
    Jelly.productionMod = 1.8F;
    
    Leaf.floweringMod = 1.4F;
    
    Pollen.floweringMod = 2.0F;
    
    Clay.lifespanMod = 1.4F;
    
    Emerald.lifespanMod = 2.0F;
    
    NetherStar.lifespanMod = 20.0F;
    
    Poison.lifespanMod = 0.5F;
  }
  
  float territoryMod = 1.0F;
  float mutationMod = 1.0F;
  float lifespanMod = 1.0F;
  float productionMod = 1.0F;
  float floweringMod = 1.0F;
  boolean lighted = false;
  boolean sunlight = false;
  boolean rain = false;
  int wearMod;
  int power;
  
  public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
  {
    return this.territoryMod;
  }
  
  public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return this.mutationMod;
  }
  
  public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return this.lifespanMod;
  }
  
  public float getProductionModifier(IBeeGenome genome, float currentModifier)
  {
    return this.productionMod;
  }
  
  public float getFloweringModifier(IBeeGenome genome, float currentModifier)
  {
    return this.floweringMod;
  }
  
  public boolean isSealed()
  {
    return this.rain;
  }
  
  public boolean isSelfLighted()
  {
    return this.lighted;
  }
  
  public boolean isSunlightSimulated()
  {
    return this.sunlight;
  }
  
  public boolean isHellish()
  {
    return false;
  }
  
  public Object getName()
  {
    return this.name;
  }
  
  public int getWearModifier()
  {
    return this.wearMod;
  }
  
  public int getPowerUsage()
  {
    return this.power;
  }
  
  public float getGeneticDecay(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
}
