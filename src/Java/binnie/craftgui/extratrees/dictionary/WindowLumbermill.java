package binnie.craftgui.extratrees.dictionary;

import binnie.core.AbstractMod;
import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.MachinePackage;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlEnergyBar;
import binnie.craftgui.minecraft.control.ControlErrorState;
import binnie.craftgui.minecraft.control.ControlLiquidTank;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.Lumbermill;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowLumbermill
  extends Window
{
  public WindowLumbermill(EntityPlayer player, IInventory inventory, Side side)
  {
    super(220.0F, 192.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "Lumbermill";
  }
  
  public void initialiseClient()
  {
    setTitle(Machine.getMachine(getInventory()).getPackage().getDisplayName());
    new ControlSlot(this, 42.0F, 43.0F).assign(Lumbermill.slotWood);
    new ControlSlot(this, 148.0F, 43.0F).assign(Lumbermill.slotPlanks);
    new ControlSlot(this, 172.0F, 28.0F).assign(Lumbermill.slotBark);
    new ControlSlot(this, 172.0F, 58.0F).assign(Lumbermill.slotSawdust);
    new ControlLumbermillProgress(this, 70.0F, 43.0F);
    new ControlLiquidTank(this, 16, 32);
    new ControlEnergyBar(this, 8, 112, 16, 60, Position.Bottom);
    new ControlPlayerInventory(this);
    new ControlErrorState(this, 95.0F, 73.0F);
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowLumbermill(player, inventory, side);
  }
}
