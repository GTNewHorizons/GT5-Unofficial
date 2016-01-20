package binnie.core.machines.component;

import net.minecraft.item.ItemStack;

public abstract interface IComponentRecipe
{
  public abstract boolean isRecipe();
  
  public abstract ItemStack doRecipe(boolean paramBoolean);
  
  public abstract ItemStack getProduct();
}
