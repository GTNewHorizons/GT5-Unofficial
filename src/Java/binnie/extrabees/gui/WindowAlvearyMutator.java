package binnie.extrabees.gui;

import binnie.core.AbstractMod;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.apiary.machine.AlvearyMutator;
import cpw.mods.fml.relauncher.Side;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class WindowAlvearyMutator
  extends Window
{
  Machine machine;
  ControlPlayerInventory playerInventory;
  
  public WindowAlvearyMutator(EntityPlayer player, IInventory inventory, Side side)
  {
    super(176.0F, 176.0F, player, inventory, side);
    this.machine = ((TileEntityMachine)inventory).getMachine();
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    if ((player == null) || (inventory == null)) {
      return null;
    }
    return new WindowAlvearyMutator(player, inventory, side);
  }
  
  public void initialiseClient()
  {
    setTitle("Mutator");
    
    this.playerInventory = new ControlPlayerInventory(this);
    
    ControlSlot slot = new ControlSlot(this, 79.0F, 30.0F);
    slot.assign(0);
    
    new ControlText(this, new IArea(0.0F, 52.0F, w(), 16.0F), "Possible Mutagens:", TextJustification.MiddleCenter).setColour(5592405);
    
    int size = AlvearyMutator.getMutagens().size();
    
    int w = size * 18;
    float x;
    if (size > 0)
    {
      x = (w() - w) / 2.0F;
      for (ItemStack stack : AlvearyMutator.getMutagens())
      {
        ControlItemDisplay display = new ControlItemDisplay(this, x, 66.0F);
        display.setItemStack(stack);
        display.hastooltip = true;
        x += 18.0F;
      }
    }
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "AlvearyMutator";
  }
}
