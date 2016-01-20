package binnie.extrabees.gui;

import binnie.core.AbstractMod;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlEnergyBar;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.apiary.machine.AlvearyStimulator;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowAlvearyStimulator
  extends Window
{
  Machine machine;
  ControlPlayerInventory playerInventory;
  
  public WindowAlvearyStimulator(EntityPlayer player, IInventory inventory, Side side)
  {
    super(176.0F, 144.0F, player, inventory, side);
    this.machine = ((TileEntityMachine)inventory).getMachine();
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    if ((player == null) || (inventory == null)) {
      return null;
    }
    return new WindowAlvearyStimulator(player, inventory, side);
  }
  
  public void initialiseClient()
  {
    setTitle("Stimulator");
    new ControlEnergyBar(this, 75, 29, 60, 16, Position.Left);
    ControlSlot slot = new ControlSlot(this, 41.0F, 28.0F);
    slot.assign(AlvearyStimulator.slotCircuit);
    
    this.playerInventory = new ControlPlayerInventory(this);
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "AlvearyStimulator";
  }
}
