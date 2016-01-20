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
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.Distillery;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowDistillery
  extends Window
{
  public WindowDistillery(EntityPlayer player, IInventory inventory, Side side)
  {
    super(224.0F, 192.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "Distillery";
  }
  
  public void initialiseClient()
  {
    setTitle(Machine.getMachine(getInventory()).getPackage().getDisplayName());
    
    int x = 16;
    
    new ControlLiquidTank(this, x, 35).setTankID(Distillery.tankInput);
    x += 34;
    new ControlDistilleryProgress(this, x, 32.0F);
    x += 64;
    new ControlLiquidTank(this, x, 35).setTankID(Distillery.tankOutput);
    x += 34;
    new ControlEnergyBar(this, x, 36, 60, 16, Position.Left);
    
    new ControlPlayerInventory(this);
    new ControlErrorState(this, x + 21, 62.0F);
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowDistillery(player, inventory, side);
  }
}
