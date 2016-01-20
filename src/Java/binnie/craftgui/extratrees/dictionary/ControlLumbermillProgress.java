package binnie.craftgui.extratrees.dictionary;

import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlProgressBase;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.craftgui.window.Panel;
import binnie.extratrees.core.ExtraTreeTexture;
import binnie.extratrees.machines.Lumbermill;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class ControlLumbermillProgress
  extends ControlProgressBase
{
  public void onUpdateClient()
  {
    super.onUpdateClient();
    if (this.oldProgress != this.progress)
    {
      this.oldProgress = this.progress;
      this.animation += 5.0F;
    }
  }
  
  float oldProgress = 0.0F;
  float animation = 0.0F;
  static Texture Saw = new StandardTexture(0, 0, 6, 32, ExtraTreeTexture.Gui);
  static Texture Saw2 = new StandardTexture(2, 0, 4, 32, ExtraTreeTexture.Gui);
  
  public void onRenderForeground()
  {
    GL11.glDisable(2896);
    int sawX = (int)(63.0F * this.progress);
    
    CraftGUI.Render.texture(Saw, new IPoint(sawX, -8.0F + 6.0F * (float)Math.sin(this.animation)));
    
    ItemStack item = Window.get(this).getInventory().getStackInSlot(Lumbermill.slotWood);
    if (item == null) {
      return;
    }
    GL11.glDisable(2896);
    
    Block block = null;
    if ((item.getItem() instanceof ItemBlock)) {
      block = ((ItemBlock)item.getItem()).field_150939_a;
    }
    if (block == null) {
      return;
    }
    IIcon icon = block.getIcon(2, item.getItemDamage());
    for (int i = 0; i < 4; i++) {
      CraftGUI.Render.iconBlock(new IPoint(1 + i * 16, 1.0F), icon);
    }
    ItemStack result = Lumbermill.getPlankProduct(item);
    if (result == null) {
      return;
    }
    Block block2 = null;
    if ((item.getItem() instanceof ItemBlock)) {
      block2 = ((ItemBlock)result.getItem()).field_150939_a;
    }
    if (block2 == null) {
      return;
    }
    IIcon icon2 = block2.getIcon(2, result.getItemDamage());
    
    IPoint size = getSize();
    IPoint pos = getAbsolutePosition();
    CraftGUI.Render.limitArea(new IArea(pos.add(new IPoint(0.0F, 0.0F)), new IPoint(this.progress * 64.0F + 2.0F, 18.0F)));
    
    GL11.glEnable(3089);
    for (int i = 0; i < 4; i++) {
      CraftGUI.Render.iconBlock(new IPoint(1 + i * 16, 1.0F), icon2);
    }
    GL11.glDisable(3089);
    
    CraftGUI.Render.texture(Saw2, new IPoint(sawX + 2, -8.0F + 6.0F * (float)Math.sin(this.animation)));
  }
  
  protected ControlLumbermillProgress(IWidget parent, float x, float y)
  {
    super(parent, x, y, 66.0F, 18.0F);
    new Panel(this, 0.0F, 0.0F, 66.0F, 18.0F, MinecraftGUI.PanelType.Black);
  }
}
