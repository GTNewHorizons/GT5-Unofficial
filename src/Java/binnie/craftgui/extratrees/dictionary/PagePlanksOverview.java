package binnie.craftgui.extratrees.dictionary;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.PageAbstract;
import binnie.craftgui.mod.database.WindowAbstractDatabase;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.block.DoorType;
import binnie.extratrees.block.IPlankType;
import binnie.extratrees.block.WoodManager;
import binnie.extratrees.block.decor.FenceType;
import binnie.extratrees.proxy.Proxy;
import net.minecraft.item.ItemStack;

public class PagePlanksOverview
  extends PageAbstract<ItemStack>
{
  public PagePlanksOverview(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
  }
  
  public void onValueChanged(ItemStack species)
  {
    deleteAllChildren();
    WindowAbstractDatabase database = (WindowAbstractDatabase)WindowAbstractDatabase.get(this);
    new ControlText(this, new IArea(0.0F, 0.0F, size().x(), 24.0F), species.getDisplayName(), TextJustification.MiddleCenter);
    


    new ControlText(this, new IArea(12.0F, 24.0F, size().x() - 24.0F, 24.0F), ExtraTrees.proxy.localise("gui.database.planks.use"), TextJustification.MiddleLeft);
    

    IPlankType type = WoodManager.get(species);
    
    int x = 12;
    if (type != null)
    {
      ItemStack fence = WoodManager.getFence(type, new FenceType(0), 1);
      ItemStack gate = WoodManager.getGate(type);
      ItemStack door = WoodManager.getDoor(type, DoorType.Standard);
      if (fence != null)
      {
        new ControlItemDisplay(this, x, 48.0F).setItemStack(fence);
        x += 22;
      }
      if (gate != null)
      {
        new ControlItemDisplay(this, x, 48.0F).setItemStack(gate);
        x += 22;
      }
      if (door != null)
      {
        new ControlItemDisplay(this, x, 48.0F).setItemStack(door);
        x += 22;
      }
    }
    ControlText controlDescription = new ControlText(this, new IArea(8.0F, 84.0F, getSize().x() - 16.0F, 0.0F), "", TextJustification.MiddleCenter);
    

    ControlText controlSignature = new ControlText(this, new IArea(8.0F, 84.0F, getSize().x() - 16.0F, 0.0F), "", TextJustification.BottomRight);
    


    String desc = "";
    if (type != null) {
      desc = type.getDescription();
    }
    String descBody = "§o";
    String descSig = "";
    if ((desc == null) || (desc.length() == 0))
    {
      descBody = descBody + BinnieCore.proxy.localise("gui.database.nodescription");
    }
    else
    {
      String[] descStrings = desc.split("\\|");
      descBody = descBody + descStrings[0];
      for (int i = 1; i < descStrings.length - 1; i++) {
        descBody = descBody + " " + descStrings[i];
      }
      if (descStrings.length > 1) {
        descSig = descSig + descStrings[(descStrings.length - 1)];
      }
    }
    controlDescription.setValue(descBody + "§r");
    controlSignature.setValue(descSig + "§r");
    
    float descHeight = CraftGUI.Render.textHeight(controlDescription.getValue(), controlDescription.getSize().x());
    

    controlSignature.setPosition(new IPoint(controlSignature.pos().x(), controlDescription.getPosition().y() + descHeight + 10.0F));
  }
}
