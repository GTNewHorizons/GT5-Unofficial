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
import binnie.craftgui.minecraft.control.ControlLiquidTank;
import binnie.craftgui.minecraft.control.ControlMachineProgress;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.craftgui.minecraft.control.ControlSlotArray;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.genetics.Genetics;
import binnie.genetics.core.GeneticsTexture;
import binnie.genetics.machine.Inoculator;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WindowInoculator
  extends WindowMachine
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowInoculator(player, inventory, side);
  }
  
  public WindowInoculator(EntityPlayer player, IInventory inventory, Side side)
  {
    super(266, 240, player, inventory, side);
  }
  
  static Texture ProgressBase = new StandardTexture(0, 72, 142, 72, GeneticsTexture.GUIProcess2.getTexture());
  static Texture Progress = new StandardTexture(0, 0, 142, 72, GeneticsTexture.GUIProcess2.getTexture());
  
  public void initialiseClient()
  {
    setTitle("Inoculator");
    
    int x = 16;
    int y = 32;
    

    new ControlLiquidTank(this, x, y + 18 + 16).setTankID(0);
    

    CraftGUIUtil.horizontalGrid(x, y, new IWidget[] { new ControlSlotArray(this, 0, 0, 2, 1).create(Inoculator.slotSerumReserve), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowRight.getIcon()), new ControlSlot(this, 0.0F, 0.0F).assign(0), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowRight.getIcon()), new ControlSlotArray(this, 0, 0, 2, 1).create(Inoculator.slotSerumExpended) });
    







    x += 18;
    
    new ControlMachineProgress(this, x, y + 24, ProgressBase, Progress, Position.Left);
    

    new ControlEnergyBar(this, 91, 118, 60, 16, Position.Left);
    new ControlErrorState(this, 161.0F, 118.0F);
    

    x += 142;
    

    CraftGUIUtil.verticalGrid(x, y, TextJustification.MiddleLeft, 8.0F, new IWidget[] { new ControlSlotArray(this, x, y, 4, 1).create(Inoculator.slotReserve), new ControlSlot(this, x, y + 18 + 8).assign(9), new ControlSlotArray(this, x, y + 18 + 8 + 18 + 8, 4, 1).create(Inoculator.slotFinished) });
    




    new ControlIconDisplay(this, x + 18, y + 18 + 2, GUIIcon.ArrowUpLeft.getIcon());
    new ControlIconDisplay(this, x + 18, y + 18 + 18, GUIIcon.ArrowLeftDown.getIcon());
    






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
