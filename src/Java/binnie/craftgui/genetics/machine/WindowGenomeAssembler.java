package binnie.craftgui.genetics.machine;

import binnie.core.AbstractMod;
import binnie.craftgui.minecraft.Window;
import binnie.genetics.Genetics;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowGenomeAssembler
  extends WindowMachine
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowGenomeAssembler(player, inventory, side);
  }
  
  public WindowGenomeAssembler(EntityPlayer player, IInventory inventory, Side side)
  {
    super(320, 240, player, inventory, side);
  }
  
  public String getTitle()
  {
    return "Genome Assembler";
  }
  
  protected AbstractMod getMod()
  {
    return Genetics.instance;
  }
  
  protected String getName()
  {
    return "GenomeAssembler";
  }
}
