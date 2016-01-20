package binnie.craftgui.extratrees.dictionary;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.controls.scroll.IControlScrollable;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import binnie.extratrees.api.CarpentryManager;
import binnie.extratrees.api.ICarpentryInterface;
import binnie.extratrees.api.IDesign;
import binnie.extratrees.api.IDesignCategory;
import binnie.extratrees.carpentry.EnumDesign;
import binnie.extratrees.machines.Designer.ComponentWoodworkerRecipe;
import binnie.extratrees.machines.DesignerType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ControlTileSelect
  extends Control
  implements IControlValue<IDesign>, IControlScrollable
{
  public static class ControlTile
    extends Control
    implements IControlValue<IDesign>, ITooltip
  {
    IDesign value;
    
    protected ControlTile(IWidget parent, float x, float y, IDesign value)
    {
      super(x, y, 18.0F, 18.0F);
      setValue(value);
      addAttribute(Attribute.MouseOver);
      
      addSelfEventHandler(new EventMouse.Down.Handler()
      {
        public void onEvent(EventMouse.Down event)
        {
          TileEntityMachine tile = (TileEntityMachine)Window.get(ControlTileSelect.ControlTile.this.getWidget()).getInventory();
          if (tile == null) {
            return;
          }
          Designer.ComponentWoodworkerRecipe recipe = (Designer.ComponentWoodworkerRecipe)tile.getMachine().getComponent(Designer.ComponentWoodworkerRecipe.class);
          
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setShort("d", (short)CarpentryManager.carpentryInterface.getDesignIndex(ControlTileSelect.ControlTile.this.getValue()));
          
          Window.get(ControlTileSelect.ControlTile.this.getWidget()).sendClientAction("design", nbt);
        }
      });
    }
    
    public void getTooltip(Tooltip tooltip)
    {
      tooltip.add(Binnie.Language.localise(BinnieCore.instance, "gui.designer.pattern", new Object[] { getValue().getName() }));
    }
    
    public IDesign getValue()
    {
      return this.value;
    }
    
    public void onRenderBackground()
    {
      CraftGUI.Render.texture(CraftGUITexture.Slot, IPoint.ZERO);
    }
    
    public void onRenderForeground()
    {
      ItemStack image = ((WindowWoodworker)getSuperParent()).getDesignerType().getDisplayStack(getValue());
      CraftGUI.Render.item(new IPoint(1.0F, 1.0F), image);
      if (((IControlValue)getParent()).getValue() != getValue()) {
        if (Window.get(this).getMousedOverWidget() == this) {
          CraftGUI.Render.gradientRect(getArea().inset(1), 1157627903, 1157627903);
        } else {
          CraftGUI.Render.gradientRect(getArea().inset(1), -1433892728, -1433892728);
        }
      }
    }
    
    public void setValue(IDesign value)
    {
      this.value = value;
    }
  }
  
  IDesign value = EnumDesign.Blank;
  float shownHeight = 92.0F;
  
  protected ControlTileSelect(IWidget parent, float x, float y)
  {
    super(parent, x, y, 102.0F, 20 * (CarpentryManager.carpentryInterface.getSortedDesigns().size() / 4) + 22);
    
    refresh("");
  }
  
  public float getPercentageIndex()
  {
    return 0.0F;
  }
  
  public float getPercentageShown()
  {
    return 0.0F;
  }
  
  public IDesign getValue()
  {
    return this.value;
  }
  
  public void movePercentage(float percentage) {}
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    TileEntityMachine tile = (TileEntityMachine)Window.get(this).getInventory();
    if (tile == null) {
      return;
    }
    Designer.ComponentWoodworkerRecipe recipe = (Designer.ComponentWoodworkerRecipe)tile.getMachine().getComponent(Designer.ComponentWoodworkerRecipe.class);
    
    setValue(recipe.getDesign());
  }
  
  public void refresh(String filterText)
  {
    deleteAllChildren();
    int cx = 2;
    int cy = 2;
    
    Map<IDesignCategory, List<IDesign>> designs = new HashMap();
    for (IDesignCategory category : CarpentryManager.carpentryInterface.getAllDesignCategories())
    {
      designs.put(category, new ArrayList());
      for (IDesign tile : category.getDesigns()) {
        if ((filterText == "") || (tile.getName().toLowerCase().contains(filterText))) {
          ((List)designs.get(category)).add(tile);
        }
      }
      if (((List)designs.get(category)).isEmpty()) {
        designs.remove(category);
      }
    }
    for (IDesignCategory category : designs.keySet())
    {
      cx = 2;
      new ControlText(this, new IPoint(cx, cy + 3), category.getName());
      cy += 16;
      for (IDesign tile : (List)designs.get(category))
      {
        if (cx > 90)
        {
          cx = 2;
          cy += 20;
        }
        new ControlTile(this, cx, cy, tile);
        cx += 20;
      }
      cy += 20;
    }
    int height = cy;
    
    setSize(new IPoint(getSize().x(), height));
  }
  
  public void setPercentageIndex(float index) {}
  
  public void setValue(IDesign value)
  {
    this.value = value;
  }
  
  public float getMovementRange()
  {
    return 0.0F;
  }
}
