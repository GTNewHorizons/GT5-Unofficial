package binnie.extrabees.gui.punnett;

import binnie.core.AbstractMod;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.resource.StyleSheet;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.craftgui.resource.minecraft.PaddedTexture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.core.ExtraBeeTexture;
import cpw.mods.fml.relauncher.Side;
import forestry.api.genetics.ISpeciesRoot;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowPunnettSquare
  extends Window
{
  ControlSlot bee1;
  ControlSlot bee2;
  ControlPunnett punnett;
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowPunnettSquare(player, inventory, side);
  }
  
  public WindowPunnettSquare(EntityPlayer player, IInventory inventory, Side side)
  {
    super(245.0F, 205.0F, player, inventory, side);
  }
  
  public AbstractMod getMod()
  {
    return ExtraBees.instance;
  }
  
  public String getName()
  {
    return "Punnett";
  }
  
  public void initialiseClient()
  {
    setTitle("Punnett Square");
    
    CraftGUI.Render.stylesheet(new StyleSheetPunnett());
  }
  
  ISpeciesRoot root = null;
  
  static class StyleSheetPunnett
    extends StyleSheet
  {
    public StyleSheetPunnett()
    {
      this.textures.put(CraftGUITexture.Window, new PaddedTexture(0, 0, 160, 160, 0, ExtraBeeTexture.GUIPunnett, 32, 32, 32, 32));
      this.textures.put(CraftGUITexture.Slot, new StandardTexture(160, 0, 18, 18, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(ExtraBeeGUITexture.Chromosome, new StandardTexture(160, 36, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
      this.textures.put(ExtraBeeGUITexture.Chromosome2, new StandardTexture(160, 52, 16, 16, 0, ExtraBeeTexture.GUIPunnett));
    }
  }
}
