package binnie.craftgui.extratrees.dictionary;

import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.MachinePackage;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextEdit;
import binnie.craftgui.controls.scroll.ControlScrollableContent;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventTextEdit;
import binnie.craftgui.events.EventTextEdit.Handler;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlErrorState;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.window.Panel;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.Designer;
import binnie.extratrees.machines.Designer.ComponentWoodworkerRecipe;
import binnie.extratrees.machines.DesignerType;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowWoodworker
  extends Window
{
  ControlTextEdit textEdit;
  ControlTileSelect tileSelect;
  
  public void initialiseClient()
  {
    setTitle(Machine.getMachine(getInventory()).getPackage().getDisplayName());
    
    new ControlText(this, new IArea(190.0F, 36.0F, 114.0F, 10.0F), BinnieCore.proxy.localise("gui.design"), TextJustification.TopCenter).setColour(4473924);
    
    new Panel(this, 188.0F, 48.0F, 118.0F, 126.0F, MinecraftGUI.PanelType.Gray);
    
    this.textEdit = new ControlTextEdit(this, 188.0F, 178.0F, 118.0F, 12.0F);
    
    ControlScrollableContent scroll = new ControlScrollableContent(this, 190.0F, 50.0F, 114.0F, 122.0F, 12.0F);
    


    this.tileSelect = new ControlTileSelect(scroll, 0.0F, 0.0F);
    
    scroll.setScrollableContent(this.tileSelect);
    




    new ControlPlayerInventory(this).setPosition(new IPoint(14.0F, 96.0F));
    
    new ControlErrorState(this, 76.0F, 65.0F);
    ControlRecipeSlot slotFinished;
    if (getInventory() != null)
    {
      ControlSlot slotWood1 = new ControlSlot(this, 22.0F, 34.0F);
      slotWood1.assign(Designer.design1Slot);
      ControlSlot slotWood2 = new ControlSlot(this, 62.0F, 34.0F);
      slotWood2.assign(Designer.design2Slot);
      
      ControlSlot slotBeeswax = new ControlSlot(this, 42.0F, 64.0F);
      slotBeeswax.assign(Designer.beeswaxSlot);
      
      slotFinished = new ControlRecipeSlot(this, 112, 34);
    }
  }
  
  public WindowWoodworker(EntityPlayer player, IInventory inventory, Side side)
  {
    super(320.0F, 216.0F, player, inventory, side);
    
    addEventHandler(new EventTextEdit.Handler()
    {
      public void onEvent(EventTextEdit event)
      {
        WindowWoodworker.this.tileSelect.refresh((String)event.getValue());
      }
    }.setOrigin(EventHandler.Origin.DirectChild, this));
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowWoodworker(player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "Woodworker";
  }
  
  public DesignerType getDesignerType()
  {
    return ((Designer.ComponentWoodworkerRecipe)Machine.getInterface(Designer.ComponentWoodworkerRecipe.class, getInventory())).getDesignerType();
  }
}
