package binnie.core.machines.storage;

import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.craftgui.genetics.machine.WindowMachine;
import binnie.craftgui.minecraft.Window;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowTest
  extends WindowMachine
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowCompartment(player, inventory, side);
  }
  
  public WindowTest(EntityPlayer player, IInventory inventory, Side side)
  {
    super(320, 240, player, inventory, side);
  }
  
  public void initialiseClient() {}
  
  public String getTitle()
  {
    return "Test";
  }
  
  protected AbstractMod getMod()
  {
    return BinnieCore.instance;
  }
  
  protected String getName()
  {
    return "Test";
  }
}
