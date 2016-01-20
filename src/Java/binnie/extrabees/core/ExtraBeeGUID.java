package binnie.extrabees.core;

import binnie.core.gui.IBinnieGUID;
import binnie.craftgui.minecraft.Window;
import binnie.extrabees.gui.WindowAlvearyFrame;
import binnie.extrabees.gui.WindowAlvearyHatchery;
import binnie.extrabees.gui.WindowAlvearyMutator;
import binnie.extrabees.gui.WindowAlvearyStimulator;
import binnie.extrabees.gui.database.WindowApiaristDatabase;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public enum ExtraBeeGUID
  implements IBinnieGUID
{
  Database,  DatabaseNEI,  AlvearyMutator,  AlvearyFrame,  AlvearyStimulator,  PunnettSquare,  AlvearyHatchery;
  
  private ExtraBeeGUID() {}
  
  public Window getWindow(EntityPlayer player, World world, int x, int y, int z, Side side)
  {
    Window window = null;
    
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    
    IInventory object = null;
    if ((tileEntity instanceof IInventory)) {
      object = (IInventory)tileEntity;
    }
    switch (1.$SwitchMap$binnie$extrabees$core$ExtraBeeGUID[ordinal()])
    {
    case 1: 
    case 2: 
      window = WindowApiaristDatabase.create(player, side, this != Database);
      break;
    case 3: 
      window = WindowAlvearyMutator.create(player, object, side);
      break;
    case 4: 
      window = WindowAlvearyFrame.create(player, object, side);
      break;
    case 5: 
      window = WindowAlvearyStimulator.create(player, object, side);
      break;
    case 6: 
      window = WindowAlvearyHatchery.create(player, object, side);
      break;
    }
    return window;
  }
}
