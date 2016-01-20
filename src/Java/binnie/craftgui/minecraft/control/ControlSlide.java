package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IBorder;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import org.lwjgl.opengl.GL11;

public class ControlSlide
  extends Control
{
  private IArea expanded;
  private IArea shrunk;
  private boolean slideActive = true;
  private Position anchor;
  private String label = null;
  
  public ControlSlide(IWidget parent, float x, float y, float w, float h, Position anchor2)
  {
    super(parent, x, y, w, h);
    addAttribute(Attribute.MouseOver);
    addAttribute(Attribute.BlockTooltip);
    this.expanded = new IArea(getPosition(), getSize());
    this.anchor = anchor2.opposite();
    float border = this.anchor.x() != 0 ? this.expanded.w() - 6.0F : this.expanded.h() - 6.0F;
    this.shrunk = this.expanded.inset(new IBorder(this.anchor, border));
    

    this.slideActive = false;
  }
  
  public void onRenderBackground()
  {
    super.onRenderBackground();
    if (this.label != null)
    {
      float lw = CraftGUI.Render.textWidth(this.label) + 16;
      float lh = CraftGUI.Render.textHeight() + 16;
      boolean hor = this.anchor.x() != 0;
      IArea ar = isSlideActive() ? this.expanded : this.shrunk;
      IArea tabArea = new IArea(hor ? -lh / 2.0F : -lw / 2.0F, hor ? -lw / 2.0F : -lh / 2.0F, hor ? lh : lw, hor ? lw : lh);
      IPoint shift = new IPoint(ar.w() * (1 - this.anchor.x()) / 2.0F, ar.h() * (1 - this.anchor.y()) / 2.0F);
      



      tabArea = tabArea.shift(shift.x() - (-3.0F + lh / 2.0F) * this.anchor.x(), shift.y() - (-3.0F + lh / 2.0F) * this.anchor.y());
      Texture texture = CraftGUI.Render.getTexture(isSlideActive() ? CraftGUITexture.Tab : CraftGUITexture.TabDisabled).crop(this.anchor.opposite(), 8.0F);
      CraftGUI.Render.texture(texture, tabArea);
      texture = CraftGUI.Render.getTexture(CraftGUITexture.TabOutline).crop(this.anchor.opposite(), 8.0F);
      CraftGUI.Render.texture(texture, tabArea.inset(2));
      IArea labelArea = new IArea(-lw / 2.0F, 0.0F, lw, lh);
      GL11.glPushMatrix();
      GL11.glTranslatef(shift.x() + this.anchor.x() * 2.0F, shift.y() + this.anchor.y() * 2.0F, 0.0F);
      if (this.anchor.x() != 0) {
        GL11.glRotatef(90.0F, 0.0F, 0.0F, this.anchor.x());
      }
      if (this.anchor.y() > 0) {
        GL11.glTranslatef(0.0F, -lh, 0.0F);
      }
      CraftGUI.Render.text(labelArea, TextJustification.MiddleCenter, this.label, 16777215);
      GL11.glPopMatrix();
    }
    CraftGUI.Render.texture(CraftGUITexture.Window, getArea());
    Object slideTexture = this.anchor == Position.Left ? CraftGUITexture.SlideLeft : this.anchor == Position.Top ? CraftGUITexture.SlideUp : this.anchor == Position.Bottom ? CraftGUITexture.SlideDown : CraftGUITexture.SlideRight;
    
    CraftGUI.Render.texture(slideTexture, new IPoint((this.anchor.x() + 1.0F) * w() / 2.0F - 8.0F, (this.anchor.y() + 1.0F) * h() / 2.0F - 8.0F));
  }
  
  public boolean isSlideActive()
  {
    return this.slideActive;
  }
  
  public void onUpdateClient()
  {
    boolean mouseOver = isMouseOverWidget(getRelativeMousePosition());
    if (mouseOver != this.slideActive) {
      setSlide(mouseOver);
    }
  }
  
  public boolean isMouseOverWidget(IPoint relativeMouse)
  {
    return getArea().outset(isSlideActive() ? 16 : 8).outset(new IBorder(this.anchor.opposite(), 16.0F)).contains(relativeMouse);
  }
  
  public boolean isChildVisible(IWidget child)
  {
    return this.slideActive;
  }
  
  public void setSlide(boolean b)
  {
    this.slideActive = b;
    IArea area = isSlideActive() ? this.expanded : this.shrunk;
    setSize(area.size());
    setPosition(area.pos());
  }
  
  public void setLabel(String l)
  {
    this.label = l;
  }
}
