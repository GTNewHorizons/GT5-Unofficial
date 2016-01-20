package binnie.craftgui.minecraft.control;

import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.Texture;

public class ControlProgress
  extends ControlProgressBase
{
  private Texture progressBlank;
  private Texture progressBar;
  private Position direction;
  
  public ControlProgress(IWidget parent, int x, int y, Texture progressBlank, Texture progressBar, Position dir)
  {
    super(parent, x, y, progressBlank == null ? 0.0F : progressBlank.w(), progressBlank == null ? 0.0F : progressBlank.h());
    this.progressBlank = progressBlank;
    this.progressBar = progressBar;
    this.progress = 0.0F;
    this.direction = dir;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(this.progressBlank, getArea());
    CraftGUI.Render.texturePercentage(this.progressBar, getArea(), this.direction, this.progress);
  }
}
