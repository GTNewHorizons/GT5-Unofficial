package binnie.extrabees.apiary;

import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.genetics.BeeModifierLogic;
import binnie.core.genetics.EnumBeeBooleanModifier;
import binnie.core.genetics.EnumBeeModifier;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IHiveFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum EnumHiveFrame
  implements IHiveFrame
{
  Cocoa,  Cage,  Soul,  Clay,  Debug;
  
  Item item;
  
  public static void init()
  {
    Cocoa.logic.setModifier(EnumBeeModifier.Lifespan, 0.75F, 0.25F);
    Cocoa.logic.setModifier(EnumBeeModifier.Production, 1.5F, 5.0F);
    
    Cage.logic.setModifier(EnumBeeModifier.Territory, 0.5F, 0.1F);
    Cage.logic.setModifier(EnumBeeModifier.Lifespan, 0.75F, 0.5F);
    Cage.logic.setModifier(EnumBeeModifier.Production, 0.75F, 0.5F);
    
    Soul.logic.setModifier(EnumBeeModifier.Mutation, 1.5F, 5.0F);
    Soul.logic.setModifier(EnumBeeModifier.Lifespan, 0.75F, 0.5F);
    Soul.logic.setModifier(EnumBeeModifier.Production, 0.25F, 0.1F);
    Soul.setMaxDamage(80);
    
    Clay.logic.setModifier(EnumBeeModifier.Lifespan, 1.5F, 5.0F);
    Clay.logic.setModifier(EnumBeeModifier.Mutation, 0.5F, 0.2F);
    Clay.logic.setModifier(EnumBeeModifier.Production, 0.75F, 0.2F);
    
    Debug.logic.setModifier(EnumBeeModifier.Lifespan, 1.0E-004F, 1.0E-004F);
    


    GameRegistry.addRecipe(new ItemStack(Cocoa.item), new Object[] { " c ", "cFc", " c ", Character.valueOf('F'), Mods.Forestry.stack("frameImpregnated"), Character.valueOf('c'), new ItemStack(Items.dye, 1, 3) });
    


    GameRegistry.addShapelessRecipe(new ItemStack(Cage.item), new Object[] { Mods.Forestry.stack("frameImpregnated"), Blocks.iron_bars });
    

    GameRegistry.addShapelessRecipe(new ItemStack(Soul.item), new Object[] { Mods.Forestry.stack("frameImpregnated"), Blocks.soul_sand });
    

    GameRegistry.addRecipe(new ItemStack(Clay.item), new Object[] { " c ", "cFc", " c ", Character.valueOf('F'), Mods.Forestry.stack("frameImpregnated"), Character.valueOf('c'), Items.clay_ball });
  }
  
  public int getIconIndex()
  {
    return 55 + ordinal();
  }
  
  public void setMaxDamage(int damage)
  {
    this.maxDamage = damage;
  }
  
  int maxDamage = 240;
  BeeModifierLogic logic = new BeeModifierLogic();
  
  private EnumHiveFrame() {}
  
  public ItemStack frameUsed(IBeeHousing house, ItemStack frame, IBee queen, int wear)
  {
    frame.setItemDamage(frame.getItemDamage() + wear);
    if (frame.getItemDamage() >= frame.getMaxDamage()) {
      return null;
    }
    return frame;
  }
  
  public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.Territory, currentModifier);
  }
  
  public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.Mutation, currentModifier);
  }
  
  public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.Lifespan, currentModifier);
  }
  
  public float getProductionModifier(IBeeGenome genome, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.Production, currentModifier);
  }
  
  public float getFloweringModifier(IBeeGenome genome, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.Flowering, currentModifier);
  }
  
  public float getGeneticDecay(IBeeGenome genome, float currentModifier)
  {
    return this.logic.getModifier(EnumBeeModifier.GeneticDecay, currentModifier);
  }
  
  public boolean isSealed()
  {
    return this.logic.getModifier(EnumBeeBooleanModifier.Sealed);
  }
  
  public boolean isSelfLighted()
  {
    return this.logic.getModifier(EnumBeeBooleanModifier.SelfLighted);
  }
  
  public boolean isSunlightSimulated()
  {
    return this.logic.getModifier(EnumBeeBooleanModifier.SunlightStimulated);
  }
  
  public boolean isHellish()
  {
    return this.logic.getModifier(EnumBeeBooleanModifier.Hellish);
  }
  
  public String getName()
  {
    return ExtraBees.proxy.localise("item.frame." + toString().toLowerCase());
  }
}
