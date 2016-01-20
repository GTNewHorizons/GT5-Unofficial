package binnie.extrabees.gui.database;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.biome.BiomeGenBase;

public class ControlBiomes
  extends Control
  implements ITooltip
{
  public ControlBiomes(IWidget parent, int x, int y, int width, int height)
  {
    super(parent, x, y, width * 16, height * 16);
    addAttribute(Attribute.MouseOver);
  }
  
  List<Integer> tolerated = new ArrayList();
  
  public void getTooltip(Tooltip list)
  {
    if (this.tolerated.isEmpty()) {
      return;
    }
    int x = (int)(getRelativeMousePosition().x() / 16.0F);
    int y = (int)(getRelativeMousePosition().y() / 16.0F);
    
    int i = x + y * 8;
    if (i < this.tolerated.size()) {
      list.add(BiomeGenBase.getBiome(((Integer)this.tolerated.get(i)).intValue()).biomeName);
    }
  }
  
  public void onRenderForeground()
  {
    for (int i = 0; i < this.tolerated.size(); i++)
    {
      int x = i % 8 * 16;
      int y = i / 8 * 16;
      if (BiomeGenBase.getBiome(i) != null) {
        CraftGUI.Render.colour(BiomeGenBase.getBiome(i).color);
      }
      CraftGUI.Render.texture(CraftGUITexture.Button, new IArea(x, y, 16.0F, 16.0F));
    }
  }
  
  public void setSpecies(IAlleleBeeSpecies species)
  {
    this.tolerated.clear();
    if (species == null) {
      return;
    }
    IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(Binnie.Genetics.getBeeRoot().getTemplate(species.getUID()));
    

    IBee bee = Binnie.Genetics.getBeeRoot().getBee(BinnieCore.proxy.getWorld(), genome);
  }
}
