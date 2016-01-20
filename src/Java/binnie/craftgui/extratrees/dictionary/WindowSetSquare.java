package binnie.craftgui.extratrees.dictionary;

import binnie.core.AbstractMod;
import binnie.craftgui.minecraft.Window;
import binnie.extratrees.ExtraTrees;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class WindowSetSquare
  extends Window
{
  public WindowSetSquare(EntityPlayer player, IInventory inventory, Side side)
  {
    super(150.0F, 150.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return null;
  }
  
  public void initialiseClient() {}
  
  public static Window create(EntityPlayer player, World world, int x, int y, int z, Side side)
  {
    return new WindowSetSquare(player, null, side);
  }
}
