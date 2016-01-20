package binnie.core.gui;

import binnie.core.machines.storage.WindowCompartment;
import binnie.craftgui.binniecore.WindowFieldKit;
import binnie.craftgui.binniecore.WindowGenesis;
import binnie.craftgui.minecraft.Window;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public enum BinnieCoreGUI
  implements IBinnieGUID
{
  Compartment,  FieldKit,  Genesis;
  
  private BinnieCoreGUI() {}
  
  public Window getWindow(EntityPlayer player, IInventory object, Side side)
    throws Exception
  {
    switch (1.$SwitchMap$binnie$core$gui$BinnieCoreGUI[ordinal()])
    {
    case 1: 
      return new WindowCompartment(player, object, side);
    case 2: 
      return new WindowFieldKit(player, null, side);
    case 3: 
      return new WindowGenesis(player, null, side);
    }
    return null;
  }
  
  public Window getWindow(EntityPlayer player, World world, int x, int y, int z, Side side)
  {
    Window window = null;
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    
    IInventory object = null;
    if ((tileEntity instanceof IInventory)) {
      object = (IInventory)tileEntity;
    }
    try
    {
      window = getWindow(player, object, side);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return window;
  }
}
