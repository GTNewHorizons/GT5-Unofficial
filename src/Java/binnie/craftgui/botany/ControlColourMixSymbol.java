package binnie.craftgui.botany;

import binnie.botany.api.IColourMix;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.CraftGUITextureSheet;
import binnie.craftgui.resource.minecraft.StandardTexture;

public class ControlColourMixSymbol
  extends Control
  implements ITooltip
{
  static Texture MutationPlus = new StandardTexture(2, 94, 16, 16, CraftGUITextureSheet.Controls2);
  static Texture MutationArrow = new StandardTexture(20, 94, 32, 16, CraftGUITextureSheet.Controls2);
  IColourMix value;
  int type;
  
  public void onRenderBackground()
  {
    super.onRenderBackground();
    if (this.type == 0) {
      CraftGUI.Render.texture(MutationPlus, IPoint.ZERO);
    } else {
      CraftGUI.Render.texture(MutationArrow, IPoint.ZERO);
    }
  }
  
  protected ControlColourMixSymbol(IWidget parent, int x, int y, int type)
  {
    super(parent, x, y, 16 + type * 16, 16.0F);
    this.value = null;
    this.type = type;
    addAttribute(Attribute.MouseOver);
  }
  
  public void setValue(IColourMix value)
  {
    this.value = value;
    
    setColour(16777215);
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    if (this.type == 1)
    {
      float chance = this.value.getChance();
      tooltip.add("Current Chance - " + chance + "%");
    }
  }
}
