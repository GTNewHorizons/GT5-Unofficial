package binnie.craftgui.genetics.machine;

import binnie.core.AbstractMod;
import binnie.craftgui.minecraft.Window;
import binnie.genetics.Genetics;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowGeneProject
  extends Window
{
  public WindowGeneProject(EntityPlayer player, IInventory inventory, Side side)
  {
    super(100.0F, 100.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return Genetics.instance;
  }
  
  protected String getName()
  {
    return "GeneProjects";
  }
  
  public void initialiseClient()
  {
    setTitle("Gene Projects");
  }
}
