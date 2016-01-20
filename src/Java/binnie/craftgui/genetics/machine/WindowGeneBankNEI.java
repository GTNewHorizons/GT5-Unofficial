package binnie.craftgui.genetics.machine;

import binnie.craftgui.minecraft.Window;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowGeneBankNEI
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowGeneBank(player, inventory, side, true);
  }
}
