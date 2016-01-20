package binnie.craftgui.core.renderer;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IBorder;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.minecraft.GuiCraftGUI;
import binnie.craftgui.resource.IStyleSheet;
import binnie.craftgui.resource.Texture;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class Renderer
{
  GuiCraftGUI gui;
  
  public Renderer(GuiCraftGUI gui)
  {
    this.gui = gui;
  }
  
  public final void preRender(IWidget widget)
  {
    GL11.glPushMatrix();
    GL11.glTranslatef(widget.getPosition().x(), widget.getPosition().y(), 0.0F);
    colour(widget.getColour());
    if (widget.isCroppedWidet())
    {
      IWidget cropRelative = widget.getCropWidget() != null ? widget.getCropWidget() : widget;
      IPoint pos = cropRelative.getAbsolutePosition();
      IArea cropZone = widget.getCroppedZone();
      limitArea(new IArea(pos.add(cropZone.pos()), cropZone.size()));
      GL11.glEnable(3089);
    }
    GL11.glDisable(2929);
  }
  
  public final void postRender(IWidget widget)
  {
    if (widget.isCroppedWidet()) {
      GL11.glDisable(3089);
    }
    GL11.glEnable(2929);
    GL11.glPopMatrix();
  }
  
  int currentColour = 16777215;
  Texture currentTexture;
  IStyleSheet stylesheet;
  
  public void colour(int hex)
  {
    this.currentColour = hex;
    int a = (hex & 0xFF000000) >> 24;
    int r = (hex & 0xFF0000) >> 16;
    int g = (hex & 0xFF00) >> 8;
    int b = hex & 0xFF;
    if (a < 0) {
      a += 256;
    }
    if ((a > 0) && (a != 255))
    {
      GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
      GL11.glEnable(3042);
    }
    else
    {
      GL11.glColor3f(r / 255.0F, g / 255.0F, b / 255.0F);
    }
  }
  
  public Texture getTexture(Object key)
  {
    if ((key instanceof Texture)) {
      return (Texture)key;
    }
    return this.stylesheet.getTexture(key);
  }
  
  public void setTexture(Texture texture)
  {
    if ((texture != this.currentTexture) && (texture != null)) {
      BinnieCore.proxy.bindTexture(texture.getFilename());
    }
    colour(this.currentColour);
  }
  
  public void texture(Object texture, IPoint position)
  {
    texture(getTexture(texture), position);
  }
  
  public void texture(Texture texture, IPoint position)
  {
    if (texture == null) {
      return;
    }
    setTexture(texture);
    IPoint point = position.sub(new IPoint(texture.getBorder().l(), texture.getBorder().t()));
    IArea textureArea = texture.getArea().outset(texture.getBorder());
    this.gui.renderTexture(point, textureArea);
  }
  
  public void texture(Object window, IArea area)
  {
    texture(getTexture(window), area);
  }
  
  public void texture(Texture texture, IArea area)
  {
    if (texture == null) {
      return;
    }
    setTexture(texture);
    IArea textureArea = texture.getArea().outset(texture.getBorder());
    IArea targetArea = area.outset(texture.getBorder());
    if ((textureArea.w() == targetArea.w()) && (textureArea.h() == targetArea.h())) {
      this.gui.renderTexture(targetArea.pos(), textureArea);
    } else {
      this.gui.renderTexturePadded(targetArea, textureArea, texture.getTotalPadding());
    }
  }
  
  public void stylesheet(IStyleSheet sheet)
  {
    this.stylesheet = sheet;
  }
  
  public int textWidth(String text)
  {
    return this.gui.getFontRenderer().getStringWidth(text);
  }
  
  public int textHeight()
  {
    return this.gui.getFontRenderer() == null ? 0 : this.gui.getFontRenderer().FONT_HEIGHT;
  }
  
  public void text(IPoint pos, String text, int colour)
  {
    text(new IArea(pos, new IPoint(500.0F, 500.0F)), TextJustification.TopLeft, text, colour);
  }
  
  public void text(IArea area, TextJustification justification, String text, int colour)
  {
    IPoint pos = area.pos();
    if (area.size().x() <= 0.0F) {
      return;
    }
    List<String> wrappedStrings = this.gui.getFontRenderer().listFormattedStringToWidth(text, (int)area.size().x());
    float totalHeight = wrappedStrings.size() * textHeight();
    float posY = area.pos().y();
    if (area.size().y() > totalHeight) {
      posY += (area.size().y() - totalHeight) * justification.getYOffset();
    }
    for (String string : wrappedStrings)
    {
      float stringWidth = textWidth(string);
      float posX = area.size().x() - stringWidth;
      posX *= justification.getXOffset();
      GL11.glDisable(2929);
      this.gui.getFontRenderer().drawString(string, (int)(pos.x() + posX), (int)posY, colour);
      
      posY += textHeight();
    }
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
  }
  
  public void solid(IArea area, int colour)
  {
    this.gui.drawRect(area.pos().x(), area.pos().y(), area.pos().x() + area.size().x(), area.pos().y() + area.size().y(), 0xFF000000 | colour);
  }
  
  public void solidAlpha(IArea area, int c1)
  {
    this.gui.drawGradientArea(area.pos().x(), area.pos().y(), area.pos().x() + area.size().x(), area.pos().y() + area.size().y(), c1, c1);
  }
  
  public void gradientRect(IArea area, int c1, int c2)
  {
    this.gui.drawGradientArea(area.pos().x(), area.pos().y(), area.pos().x() + area.size().x(), area.pos().y() + area.size().y(), c1, c2);
  }
  
  public void item(IPoint pos, ItemStack item)
  {
    this.gui.renderItem(pos, item, false);
  }
  
  public void item(IPoint pos, ItemStack item, boolean rotating)
  {
    this.gui.renderItem(pos, item, rotating);
  }
  
  public void iconBlock(IPoint pos, IIcon icon)
  {
    this.gui.renderIcon(pos, icon, TextureMap.locationBlocksTexture);
  }
  
  public void iconItem(IPoint pos, IIcon icon)
  {
    this.gui.renderIcon(pos, icon, TextureMap.locationItemsTexture);
  }
  
  public void limitArea(IArea area)
  {
    this.gui.limitArea(area);
  }
  
  public float textHeight(String text, float width)
  {
    return this.gui.getFontRenderer().listFormattedStringToWidth(text, (int)width).size() * textHeight();
  }
  
  public void texturePercentage(Texture texture, IArea area, Position direction, float percentage)
  {
    float dist = (direction == Position.Top) || (direction == Position.Bottom) ? percentage * texture.h() : percentage * texture.w();
    
    float dim = (direction == Position.Top) || (direction == Position.Bottom) ? texture.h() : texture.w();
    
    float x = area.pos().x();
    float y = area.pos().y();
    float w = area.size().x();
    float h = area.size().y();
    switch (1.$SwitchMap$binnie$craftgui$core$geometry$Position[direction.ordinal()])
    {
    case 1: 
      h *= percentage;
      break;
    case 2: 
      x += (1.0F - percentage) * w;
      w *= percentage;
      break;
    case 3: 
      w *= percentage;
      break;
    case 4: 
      y += h - (int)(percentage * h);
      h *= percentage;
    }
    texture(texture.crop(direction, dim - dist), new IArea(x, y, w, h));
  }
  
  public void test(IWidget widget) {}
}
