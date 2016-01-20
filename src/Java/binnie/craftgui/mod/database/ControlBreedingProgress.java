package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.craftgui.resource.minecraft.CraftGUITextureSheet;
import binnie.craftgui.resource.minecraft.StandardTexture;

class ControlBreedingProgress
  extends Control
{
  private static Texture Progress = new StandardTexture(80, 22, 4, 4, CraftGUITextureSheet.Controls2);
  private float percentage;
  private int colour;
  
  public ControlBreedingProgress(IWidget parent, int x, int y, int width, int height, BreedingSystem system, float percentage)
  {
    super(parent, x, y, width, height);
    this.percentage = percentage;
    this.colour = system.getColour();
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.PanelBlack, getArea());
    
    IArea area = getArea().inset(1);
    
    area.setSize(new IPoint(area.size().x() * this.percentage, area.size().y()));
    
    CraftGUI.Render.colour(this.colour);
    
    CraftGUI.Render.texture(Progress, area);
  }
}
