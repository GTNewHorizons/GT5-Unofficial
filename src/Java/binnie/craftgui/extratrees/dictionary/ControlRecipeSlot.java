package binnie.craftgui.extratrees.dictionary;

import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.machines.component.IComponentRecipe;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlSlotBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ControlRecipeSlot
  extends ControlSlotBase
{
  public ControlRecipeSlot(IWidget parent, int x, int y)
  {
    super(parent, x, y, 50);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        TileEntity tile = (TileEntity)Window.get(ControlRecipeSlot.this.getWidget()).getInventory();
        if ((tile == null) || (!(tile instanceof TileEntityMachine))) {
          return;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        Window.get(ControlRecipeSlot.this.getWidget()).sendClientAction("recipe", nbt);
      }
    });
    setRotating();
  }
  
  public ItemStack getItemStack()
  {
    IComponentRecipe recipe = (IComponentRecipe)Machine.getInterface(IComponentRecipe.class, Window.get(this).getInventory());
    return recipe.isRecipe() ? recipe.getProduct() : null;
  }
}
