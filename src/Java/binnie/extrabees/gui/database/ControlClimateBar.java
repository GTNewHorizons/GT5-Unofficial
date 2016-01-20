package binnie.extrabees.gui.database;

import binnie.Binnie;
import binnie.core.genetics.ManagerGenetics;
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
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.EnumTolerance;
import java.util.ArrayList;
import java.util.List;

public class ControlClimateBar
  extends Control
  implements ITooltip
{
  public ControlClimateBar(IWidget parent, int x, int y, int width, int height)
  {
    super(parent, x, y, width, height);
    addAttribute(Attribute.MouseOver);
  }
  
  public ControlClimateBar(IWidget parent, int x, int y, int width, int height, boolean humidity)
  {
    super(parent, x, y, width, height);
    addAttribute(Attribute.MouseOver);
    this.isHumidity = true;
  }
  
  boolean isHumidity = false;
  List<Integer> tolerated = new ArrayList();
  
  public void getTooltip(Tooltip list)
  {
    if (this.tolerated.isEmpty()) {
      return;
    }
    int types = this.isHumidity ? 3 : 6;
    
    int type = (int)((int)(getRelativeMousePosition().x() - 1.0F) / ((getSize().x() - 2.0F) / types));
    if (!this.tolerated.contains(Integer.valueOf(type))) {
      return;
    }
    if (type < types) {
      if (this.isHumidity) {
        list.add(EnumHumidity.values()[type].name);
      } else {
        list.add(EnumTemperature.values()[(type + 1)].name);
      }
    }
  }
  
  int[] tempColours = { 65531, 7912447, 5242672, 16776960, 16753152, 16711680 };
  int[] humidColours = { 16770979, 1769216, 3177727 };
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.EnergyBarBack, getArea());
    
    int types = this.isHumidity ? 3 : 6;
    int w = (int)((getSize().x() - 2.0F) / types);
    for (int i = 0; i < types; i++)
    {
      int x = i * w;
      if (this.tolerated.contains(Integer.valueOf(i)))
      {
        int colour = 0;
        if (this.isHumidity) {
          colour = this.humidColours[i];
        } else {
          colour = this.tempColours[i];
        }
        CraftGUI.Render.solid(new IArea(x + 1, 1.0F, w, getSize().y() - 2.0F), colour);
      }
    }
    CraftGUI.Render.texture(CraftGUITexture.EnergyBarGlass, getArea());
  }
  
  public void setSpecies(IAlleleBeeSpecies species)
  {
    this.tolerated.clear();
    if (species == null) {
      return;
    }
    EnumTolerance tolerance;
    int main;
    EnumTolerance tolerance;
    if (!this.isHumidity)
    {
      int main = species.getTemperature().ordinal() - 1;
      IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(Binnie.Genetics.getBeeRoot().getTemplate(species.getUID()));
      

      tolerance = genome.getToleranceTemp();
    }
    else
    {
      main = species.getHumidity().ordinal();
      IBeeGenome genome = Binnie.Genetics.getBeeRoot().templateAsGenome(Binnie.Genetics.getBeeRoot().getTemplate(species.getUID()));
      

      tolerance = genome.getToleranceHumid();
    }
    this.tolerated.add(Integer.valueOf(main));
    switch (1.$SwitchMap$forestry$api$genetics$EnumTolerance[tolerance.ordinal()])
    {
    case 1: 
    case 2: 
      this.tolerated.add(Integer.valueOf(main + 5));
    case 3: 
    case 4: 
      this.tolerated.add(Integer.valueOf(main + 4));
    case 5: 
    case 6: 
      this.tolerated.add(Integer.valueOf(main + 3));
    case 7: 
    case 8: 
      this.tolerated.add(Integer.valueOf(main + 2));
    case 9: 
    case 10: 
      this.tolerated.add(Integer.valueOf(main + 1));
    }
    switch (1.$SwitchMap$forestry$api$genetics$EnumTolerance[tolerance.ordinal()])
    {
    case 1: 
    case 11: 
      this.tolerated.add(Integer.valueOf(main - 5));
    case 3: 
    case 12: 
      this.tolerated.add(Integer.valueOf(main - 4));
    case 5: 
    case 13: 
      this.tolerated.add(Integer.valueOf(main - 3));
    case 7: 
    case 14: 
      this.tolerated.add(Integer.valueOf(main - 2));
    case 9: 
    case 15: 
      this.tolerated.add(Integer.valueOf(main - 1));
    }
  }
}
