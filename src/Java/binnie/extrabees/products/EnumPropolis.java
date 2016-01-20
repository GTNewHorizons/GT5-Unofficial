package binnie.extrabees.products;

import binnie.Binnie;
import binnie.core.item.IItemEnum;
import binnie.core.liquid.ManagerLiquid;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import forestry.api.recipes.ISqueezerManager;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public enum EnumPropolis
  implements IItemEnum
{
  WATER(2405321, 12762791, "Water"),  OIL(1519411, 12762791, "oil"),  FUEL(10718482, 12762791, "fuel"),  MILK,  FRUIT,  SEED,  ALCOHOL,  CREOSOTE(8877313, 12428819, "creosote"),  GLACIAL,  PEAT;
  
  int[] colour = new int[0];
  String liquidName;
  boolean active = true;
  
  private EnumPropolis()
  {
    this(16777215, 16777215, "");
    this.active = false;
  }
  
  private EnumPropolis(int colour, int colour2, String liquid)
  {
    this.colour = new int[] { colour, colour2 };
    this.liquidName = liquid;
  }
  
  public void addRecipe()
  {
    FluidStack liquid = Binnie.Liquid.getLiquidStack(this.liquidName, 500);
    if (liquid != null) {
      RecipeManagers.squeezerManager.addRecipe(20, new ItemStack[] { get(1) }, liquid, null, 0);
    }
  }
  
  public boolean isActive()
  {
    return (this.active) && (Binnie.Liquid.getLiquidStack(this.liquidName, 100) != null);
  }
  
  public static EnumPropolis get(ItemStack itemStack)
  {
    int i = itemStack.getItemDamage();
    if ((i >= 0) && (i < values().length)) {
      return values()[i];
    }
    return values()[0];
  }
  
  public ItemStack get(int size)
  {
    return new ItemStack(ExtraBees.propolis, size, ordinal());
  }
  
  public String getName(ItemStack stack)
  {
    return ExtraBees.proxy.localise("item.propolis." + name().toLowerCase());
  }
}
