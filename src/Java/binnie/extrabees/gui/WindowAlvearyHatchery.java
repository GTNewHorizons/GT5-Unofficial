package binnie.extrabees.gui;

import binnie.core.AbstractMod;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlotArray;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.apiary.machine.AlvearyHatchery;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowAlvearyHatchery
  extends Window
{
  Machine machine;
  ControlPlayerInventory playerInventory;
  
  public WindowAlvearyHatchery(EntityPlayer player, IInventory inventory, Side side)
  {
    super(176.0F, 144.0F, player, inventory, side);
    this.machine = ((TileEntityMachine)inventory).getMachine();
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    if ((player == null) || (inventory == null)) {
      return null;
    }
    return new WindowAlvearyHatchery(player, inventory, side);
  }
  
  public void initialiseClient()
  {
    setTitle("Hatchery");
    
    this.playerInventory = new ControlPlayerInventory(this);
    
    ControlSlotArray slot = new ControlSlotArray(this, 43, 30, 5, 1);
    slot.create(AlvearyHatchery.slotLarvae);
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "AlvearyHatchery";
  }
}
