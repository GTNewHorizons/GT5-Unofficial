package binnie.extrabees.gui;

import binnie.core.AbstractMod;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.extrabees.ExtraBees;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowAlvearyIndustrialFrame
  extends Window
{
  Machine machine;
  ControlPlayerInventory playerInventory;
  
  public WindowAlvearyIndustrialFrame(EntityPlayer player, IInventory inventory, Side side)
  {
    super(176.0F, 144.0F, player, inventory, side);
    this.machine = ((TileEntityMachine)inventory).getMachine();
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    if ((player == null) || (inventory == null)) {
      return null;
    }
    return new WindowAlvearyIndustrialFrame(player, inventory, side);
  }
  
  public void initialiseClient()
  {
    setTitle("Industrial Frame Housing");
    
    this.playerInventory = new ControlPlayerInventory(this);
    
    ControlSlot slot = new ControlSlot(this, 79.0F, 30.0F);
    slot.assign(0);
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "AlvearyIndustrialFrame";
  }
}
