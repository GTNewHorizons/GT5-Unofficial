package binnie.craftgui.genetics.machine;

import binnie.core.AbstractMod;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.CraftGUIUtil;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.minecraft.GUIIcon;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlEnergyBar;
import binnie.craftgui.minecraft.control.ControlErrorState;
import binnie.craftgui.minecraft.control.ControlIconDisplay;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.minecraft.control.ControlSlotArray;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.genetics.Genetics;
import binnie.genetics.core.GeneticsTexture;
import binnie.genetics.machine.Inoculator;
import binnie.genetics.machine.Splicer;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowSplicer
  extends WindowMachine
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowSplicer(player, inventory, side);
  }
  
  public WindowSplicer(EntityPlayer player, IInventory inventory, Side side)
  {
    super(280, 240, player, inventory, side);
  }
  
  static Texture ProgressBase = new StandardTexture(0, 72, 142, 72, GeneticsTexture.GUIProcess2.getTexture());
  static Texture Progress = new StandardTexture(0, 0, 142, 72, GeneticsTexture.GUIProcess2.getTexture());
  
  public void initialiseClient()
  {
    setTitle("Splicer");
    
    int x = 16;
    



    new ControlSplicerProgress(this, 84.0F, 32.0F, w() - 172.0F, 102.0F);
    
    CraftGUIUtil.horizontalGrid(x, 62.0F, new IWidget[] { new ControlSlotArray(this, 0, 0, 2, 1).create(Splicer.slotSerumReserve), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowRight.getIcon()), new ControlSlot(this, 0.0F, 0.0F).assign(0) });
    



    new ControlSlotArray(this, x + 12, 84, 2, 1).create(Splicer.slotSerumExpended);
    
    new ControlIconDisplay(this, x + 12 + 36 + 4, 86.0F, GUIIcon.ArrowUpLeft.getIcon());
    


    new ControlEnergyBar(this, 196, 64, 60, 16, Position.Left);
    new ControlErrorState(this, 218.0F, 86.0F);
    
    x += 142;
    
    CraftGUIUtil.verticalGrid((w() - 72.0F) / 2.0F, 32.0F, TextJustification.MiddleCenter, 4.0F, new IWidget[] { new ControlSlotArray(this, 0, 0, 4, 1).create(Inoculator.slotReserve), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowDown.getIcon()), new ControlSlot(this, 0.0F, 0.0F).assign(9), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowDown.getIcon()), new ControlSlotArray(this, 0, 0, 4, 1).create(Inoculator.slotFinished) });
    





    new ControlPlayerInventory(this);
  }
  
  public String getTitle()
  {
    return "Inoculator";
  }
  
  protected AbstractMod getMod()
  {
    return Genetics.instance;
  }
  
  protected String getName()
  {
    return "Inoculator";
  }
}
