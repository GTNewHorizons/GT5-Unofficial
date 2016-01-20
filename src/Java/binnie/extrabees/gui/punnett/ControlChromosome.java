package binnie.extrabees.gui.punnett;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.renderer.Renderer;
import forestry.api.genetics.IChromosomeType;

public class ControlChromosome
  extends Control
  implements IControlValue<IChromosomeType>, ITooltip
{
  IChromosomeType value;
  
  protected ControlChromosome(IWidget parent, float x, float y, IChromosomeType type)
  {
    super(parent, x, y, 16.0F, 16.0F);
    setValue(type);
    addAttribute(Attribute.MouseOver);
  }
  
  public IChromosomeType getValue()
  {
    return this.value;
  }
  
  public void setValue(IChromosomeType value)
  {
    this.value = value;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(ExtraBeeGUITexture.Chromosome, getArea());
    CraftGUI.Render.colour(16711680);
    CraftGUI.Render.texture(ExtraBeeGUITexture.Chromosome2, getArea());
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    if (this.value != null) {
      tooltip.add(this.value.getName());
    }
  }
}
