package binnie.craftgui.minecraft;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IBorder;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventKey.Down;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Up;
import binnie.craftgui.events.EventMouse.Wheel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCraftGUI
  extends GuiContainer
{
  IPoint mousePos = new IPoint(0.0F, 0.0F);
  private Window window;
  private ItemStack draggedItem;
  
  public void updateScreen()
  {
    this.window.updateClient();
  }
  
  public Minecraft getMinecraft()
  {
    return this.mc;
  }
  
  public GuiCraftGUI(Window window)
  {
    super(window.getContainer());
    this.window = window;
    resize(window.getSize());
  }
  
  protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {}
  
  public void initGui()
  {
    super.initGui();
    this.mc.thePlayer.openContainer = this.inventorySlots;
    this.guiLeft = ((this.width - this.xSize) / 2);
    this.guiTop = ((this.height - this.ySize) / 2);
    this.window.setSize(new IPoint(this.xSize, this.ySize));
    this.window.setPosition(new IPoint(this.guiLeft, this.guiTop));
    this.window.initGui();
  }
  
  public ItemStack getDraggedItem()
  {
    return this.draggedItem;
  }
  
  public void drawScreen(int mouseX, int mouseY, float par3)
  {
    this.window.setMousePosition(mouseX - (int)this.window.getPosition().x(), mouseY - (int)this.window.getPosition().y());
    

    drawDefaultBackground();
    

    GL11.glDisable(32826);
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(2896);
    GL11.glDisable(2929);
    
    this.zLevel = 10.0F;
    itemRender.zLevel = this.zLevel;
    
    this.window.render();
    

    RenderHelper.enableGUIStandardItemLighting();
    GL11.glPushMatrix();
    GL11.glEnable(32826);
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    




    InventoryPlayer playerInventory = this.mc.thePlayer.inventory;
    this.draggedItem = playerInventory.getItemStack();
    if (this.draggedItem != null)
    {
      renderItem(new IPoint(mouseX - 8, mouseY - 8), this.draggedItem, 200, false);
      renderItem(new IPoint(mouseX - 8, mouseY - 8), this.draggedItem, 200, false);
    }
    GL11.glDisable(32826);
    GL11.glPopMatrix();
    




    GL11.glDisable(2896);
    GL11.glDisable(2929);
    

    MinecraftTooltip tooltip = new MinecraftTooltip();
    if (isHelpMode())
    {
      tooltip.setType(Tooltip.Type.Help);
      this.window.getHelpTooltip(tooltip);
    }
    else
    {
      tooltip.setType(Tooltip.Type.Standard);
      this.window.getTooltip(tooltip);
    }
    if (tooltip.exists()) {
      renderTooltip(new IPoint(mouseX, mouseY), tooltip);
    }
    this.zLevel = 0.0F;
    
    GL11.glEnable(2896);
    GL11.glEnable(2929);
  }
  
  public void renderTooltip(IPoint mousePosition, MinecraftTooltip tooltip)
  {
    int mouseX = (int)mousePosition.x();
    int mouseY = (int)mousePosition.y();
    FontRenderer font = getFontRenderer();
    GL11.glDisable(32826);
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(2896);
    GL11.glDisable(2929);
    int k = 0;
    
    List<String> strings = new ArrayList();
    for (String string : tooltip.getList()) {
      if (string != null) {
        if (!string.contains("~~~")) {
          strings.addAll(font.listFormattedStringToWidth(string, tooltip.maxWidth));
        } else {
          strings.add(string);
        }
      }
    }
    Iterator iterator = strings.iterator();
    while (iterator.hasNext())
    {
      String s = (String)iterator.next();
      int l = font.getStringWidth(s);
      if (s.contains("~~~")) {
        l = 12 + font.getStringWidth(s.replaceAll("~~~(.*?)~~~", ""));
      }
      if (l > k) {
        k = l;
      }
    }
    int i1 = mouseX + 12;
    int j1 = mouseY - 12;
    int k1 = 8;
    if (strings.size() > 1) {
      k1 += 2 + (strings.size() - 1) * 10;
    }
    if (i1 + k > this.width) {
      i1 -= 28 + k;
    }
    if (j1 + k1 + 6 > this.height) {
      j1 = this.height - k1 - 6;
    }
    this.zLevel = 300.0F;
    itemRender.zLevel = 300.0F;
    int l1 = -267386864;
    int i2 = 1342177280 + MinecraftTooltip.getOutline(tooltip.getType());
    int j2 = i2;
    drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
    drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
    drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
    drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
    drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
    
    drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
    drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
    drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
    drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
    for (int k2 = 0; k2 < strings.size(); k2++)
    {
      String s1 = (String)strings.get(k2);
      if (k2 == 0) {
        s1 = MinecraftTooltip.getTitle(tooltip.getType()) + s1;
      } else {
        s1 = MinecraftTooltip.getBody(tooltip.getType()) + s1;
      }
      if (s1.contains("~~~"))
      {
        String split = s1.split("~~~")[1];
        try
        {
          NBTTagCompound nbt = (NBTTagCompound)JsonToNBT.func_150315_a(split);
          ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
          GL11.glPushMatrix();
          GL11.glTranslatef(i1, j1 - 1.5F, 0.0F);
          GL11.glScalef(0.6F, 0.6F, 1.0F);
          renderItem(new IPoint(0.0F, 0.0F), stack, false);
          GL11.glPopMatrix();
        }
        catch (NBTException e)
        {
          e.printStackTrace();
        }
        s1 = "   " + s1.replaceAll("~~~(.*?)~~~", "");
      }
      font.drawStringWithShadow(s1, i1, j1, -1);
      if (k2 == 0) {
        j1 += 2;
      }
      j1 += 10;
    }
    this.zLevel = 0.0F;
    itemRender.zLevel = 0.0F;
    GL11.glEnable(2896);
    GL11.glEnable(2929);
    RenderHelper.enableStandardItemLighting();
    GL11.glEnable(32826);
  }
  
  protected void mouseClicked(int x, int y, int button)
  {
    IWidget origin = this.window;
    if (this.window.getMousedOverWidget() != null) {
      origin = this.window.getMousedOverWidget();
    }
    this.window.callEvent(new EventMouse.Down(origin, x, y, button));
  }
  
  public boolean isShiftDown()
  {
    return Keyboard.isKeyDown(this.mc.gameSettings.keyBindSneak.getKeyCode());
  }
  
  protected void keyTyped(char c, int key)
  {
    if ((key == 1) || ((key == this.mc.gameSettings.keyBindInventory.getKeyCode()) && (this.window.getFocusedWidget() == null))) {
      this.mc.thePlayer.closeScreen();
    }
    IWidget origin = this.window.getFocusedWidget() == null ? this.window : this.window.getFocusedWidget();
    
    this.window.callEvent(new EventKey.Down(origin, c, key));
  }
  
  protected void mouseMovedOrUp(int x, int y, int button)
  {
    IWidget origin = this.window.getMousedOverWidget() == null ? this.window : this.window.getMousedOverWidget();
    float dy;
    if (button == -1)
    {
      float dx = Mouse.getEventDX() * this.width / this.mc.displayWidth;
      dy = -(Mouse.getEventDY() * this.height / this.mc.displayHeight);
    }
    else
    {
      this.window.callEvent(new EventMouse.Up(origin, x, y, button));
    }
  }
  
  public void handleMouseInput()
  {
    super.handleMouseInput();
    int dWheel = Mouse.getDWheel();
    IWidget origin = this.window.getFocusedWidget() == null ? this.window : this.window.getFocusedWidget();
    if (dWheel != 0) {
      this.window.callEvent(new EventMouse.Wheel(this.window, dWheel));
    }
  }
  
  public void onGuiClosed()
  {
    this.window.onClose();
  }
  
  public void renderTexturedRect(float x, float y, float u, float v, float w, float h)
  {
    drawTexturedModalRect((int)x, (int)y, (int)u, (int)v, (int)w, (int)h);
  }
  
  public void renderTexture(IPoint position, IArea textureArea)
  {
    drawTexturedModalRect((int)position.x(), (int)position.y(), (int)textureArea.pos().x(), (int)textureArea.pos().y(), (int)textureArea.size().x(), (int)textureArea.size().y());
  }
  
  private void renderTexturedRect(IArea area, IPoint uv)
  {
    renderTexturedRect(area.pos().x(), area.pos().y(), uv.x(), uv.y(), area.size().x(), area.size().y());
  }
  
  public void renderTexturePadded(IArea area, IArea texture, IBorder padding)
  {
    int borderLeft = (int)padding.l();
    int borderRight = (int)padding.r();
    int borderTop = (int)padding.t();
    int borderBottom = (int)padding.b();
    
    int posX = (int)area.pos().x();
    int posY = (int)area.pos().y();
    int width = (int)area.size().x();
    int height = (int)area.size().y();
    
    int textWidth = (int)texture.w();
    int textHeight = (int)texture.h();
    
    int u = (int)texture.x();
    int v = (int)texture.y();
    if (borderTop + borderBottom > height)
    {
      borderTop = height / 2;
      borderBottom = height / 2;
    }
    if (borderLeft + borderRight > width)
    {
      borderLeft = width / 2;
      borderRight = width / 2;
    }
    IPoint origin = area.pos();
    
    drawTexturedModalRect(posX, posY, u, v, borderLeft, borderTop);
    

    drawTexturedModalRect(posX + width - borderRight, posY, u + textWidth - borderRight, v, borderRight, borderTop);
    

    drawTexturedModalRect(posX, posY + height - borderBottom, u, v + textHeight - borderBottom, borderLeft, borderBottom);
    

    drawTexturedModalRect(posX + width - borderRight, posY + height - borderBottom, u + textWidth - borderRight, v + textHeight - borderBottom, borderRight, borderBottom);
    


    int currentXPos = borderLeft;
    while (currentXPos < width - borderRight)
    {
      int distanceXRemaining = width - borderRight - currentXPos;
      

      int texturingWidth = textWidth - borderLeft - borderRight;
      if (texturingWidth > distanceXRemaining) {
        texturingWidth = distanceXRemaining;
      }
      if (texturingWidth <= 0) {
        break;
      }
      drawTexturedModalRect(posX + currentXPos, posY, u + borderLeft, v, texturingWidth, borderTop);
      
      drawTexturedModalRect(posX + currentXPos, posY + height - borderBottom, u + borderLeft, v + textHeight - borderBottom, texturingWidth, borderBottom);
      

      int currentYPos = borderTop;
      while (currentYPos < height - borderBottom)
      {
        int distanceYRemaining = height - borderBottom - currentYPos;
        

        int texturingHeight = textHeight - borderTop - borderBottom;
        if (texturingHeight > distanceYRemaining) {
          texturingHeight = distanceYRemaining;
        }
        if (texturingHeight <= 0) {
          break;
        }
        drawTexturedModalRect(posX + currentXPos, posY + currentYPos, u + borderLeft, v + borderTop, texturingWidth, texturingHeight);
        
        currentYPos += texturingHeight;
      }
      currentXPos += texturingWidth;
    }
    int currentYPos = borderTop;
    while (currentYPos < height - borderBottom)
    {
      int distanceYRemaining = height - borderBottom - currentYPos;
      

      int texturingHeight = textHeight - borderTop - borderBottom;
      if (texturingHeight > distanceYRemaining) {
        texturingHeight = distanceYRemaining;
      }
      if (texturingHeight <= 0) {
        break;
      }
      drawTexturedModalRect(posX, posY + currentYPos, u, v + borderTop, borderLeft, texturingHeight);
      
      drawTexturedModalRect(posX + width - borderRight, posY + currentYPos, u + textWidth - borderRight, v + borderTop, borderRight, texturingHeight);
      currentYPos += texturingHeight;
    }
  }
  
  public void drawGradientArea(float p_73733_1_, float p_73733_2_, float p_73733_3_, float p_73733_4_, int p_73733_5_, int p_73733_6_)
  {
    float f = (p_73733_5_ >> 24 & 0xFF) / 255.0F;
    float f1 = (p_73733_5_ >> 16 & 0xFF) / 255.0F;
    float f2 = (p_73733_5_ >> 8 & 0xFF) / 255.0F;
    float f3 = (p_73733_5_ & 0xFF) / 255.0F;
    float f4 = (p_73733_6_ >> 24 & 0xFF) / 255.0F;
    float f5 = (p_73733_6_ >> 16 & 0xFF) / 255.0F;
    float f6 = (p_73733_6_ >> 8 & 0xFF) / 255.0F;
    float f7 = (p_73733_6_ & 0xFF) / 255.0F;
    GL11.glDisable(3553);
    GL11.glEnable(3042);
    GL11.glDisable(3008);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glShadeModel(7425);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.setColorRGBA_F(f1, f2, f3, f);
    tessellator.addVertex(p_73733_3_, p_73733_2_, this.zLevel);
    tessellator.addVertex(p_73733_1_, p_73733_2_, this.zLevel);
    tessellator.setColorRGBA_F(f5, f6, f7, f4);
    tessellator.addVertex(p_73733_1_, p_73733_4_, this.zLevel);
    tessellator.addVertex(p_73733_3_, p_73733_4_, this.zLevel);
    tessellator.draw();
    GL11.glShadeModel(7424);
    GL11.glDisable(3042);
    GL11.glEnable(3008);
    GL11.glEnable(3553);
  }
  
  public void renderItem(IPoint pos, ItemStack item, boolean rotating)
  {
    renderItem(pos, item, (int)this.zLevel + 3, rotating);
  }
  
  private void renderItem(IPoint pos, ItemStack item, int zLevel, boolean rotating)
  {
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    
    GL11.glPushMatrix();
    
    RenderHelper.enableGUIStandardItemLighting();
    GL11.glEnable(32826);
    GL11.glEnable(2929);
    






    FontRenderer font = item.getItem().getFontRenderer(item);
    if (font == null) {
      font = getFontRenderer();
    }
    if (item != null)
    {
      BinnieCore.proxy.getMinecraftInstance();float phase = (float)Minecraft.getSystemTime() / 20.0F;
      
      GL11.glPushMatrix();
      if (rotating)
      {
        GL11.glTranslatef(8.0F, 8.0F, 0.0F);
        GL11.glRotatef(phase, 0.0F, -0.866F, 0.5F);
        GL11.glTranslatef(-8.0F, -8.0F, -67.099998F);
      }
      itemRender.renderItemAndEffectIntoGUI(font, this.mc.renderEngine, item, (int)pos.x(), (int)pos.y());
      GL11.glPopMatrix();
      itemRender.renderItemOverlayIntoGUI(font, this.mc.renderEngine, item, (int)pos.x(), (int)pos.y(), null);
    }
    GL11.glClear(256);
    GL11.glEnable(3042);
    
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    

    RenderHelper.disableStandardItemLighting();
    
    CraftGUI.Render.colour(-1);
    
    GL11.glEnable(32826);
    GL11.glPopMatrix();
  }
  
  public void renderIcon(IPoint pos, IIcon icon, ResourceLocation map)
  {
    if (icon == null) {
      return;
    }
    GL11.glPushMatrix();
    GL11.glEnable(32826);
    

    BinnieCore.proxy.bindTexture(map);
    

    itemRender.zLevel = this.zLevel;
    
    itemRender.renderIcon((int)pos.x(), (int)pos.y(), icon, 16, 16);
    


    GL11.glEnable(32826);
    GL11.glPopMatrix();
  }
  
  public boolean isHelpMode()
  {
    return Keyboard.isKeyDown(15);
  }
  
  public FontRenderer getFontRenderer()
  {
    return this.fontRendererObj;
  }
  
  public void resize(IPoint size)
  {
    this.xSize = ((int)size.x());
    this.ySize = ((int)size.y());
    this.guiLeft = ((this.width - this.xSize) / 2);
    this.guiTop = ((this.height - this.ySize) / 2);
    this.window.setPosition(new IPoint(this.guiLeft, this.guiTop));
  }
  
  public void limitArea(IArea area)
  {
    float x = area.pos().x();
    float y = area.pos().y();
    float w = area.size().x();
    float h = area.size().y();
    
    y = this.height - (y + h);
    float k = this.xSize;
    float scaleX = this.width / this.mc.displayWidth;
    float scaleY = this.height / this.mc.displayHeight;
    
    x += 0.0F;
    y += 0.0F;
    w += 0.0F;
    h += 0.0F;
    


    GL11.glScissor((int)(x / scaleX), (int)(y / scaleY), (int)(w / scaleX), (int)(h / scaleY));
  }
  
  public int getZLevel()
  {
    return (int)this.zLevel;
  }
  
  public void drawRect(float p_73734_0_, float p_73734_1_, float p_73734_2_, float p_73734_3_, int p_73734_4_)
  {
    if (p_73734_0_ < p_73734_2_)
    {
      float j1 = p_73734_0_;
      p_73734_0_ = p_73734_2_;
      p_73734_2_ = j1;
    }
    if (p_73734_1_ < p_73734_3_)
    {
      float j1 = p_73734_1_;
      p_73734_1_ = p_73734_3_;
      p_73734_3_ = j1;
    }
    float f3 = (p_73734_4_ >> 24 & 0xFF) / 255.0F;
    float f = (p_73734_4_ >> 16 & 0xFF) / 255.0F;
    float f1 = (p_73734_4_ >> 8 & 0xFF) / 255.0F;
    float f2 = (p_73734_4_ & 0xFF) / 255.0F;
    Tessellator tessellator = Tessellator.instance;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(f, f1, f2, f3);
    tessellator.startDrawingQuads();
    tessellator.addVertex(p_73734_0_, p_73734_3_, 0.0D);
    tessellator.addVertex(p_73734_2_, p_73734_3_, 0.0D);
    tessellator.addVertex(p_73734_2_, p_73734_1_, 0.0D);
    tessellator.addVertex(p_73734_0_, p_73734_1_, 0.0D);
    tessellator.draw();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
  }
}
