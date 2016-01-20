package binnie.craftgui.genetics.machine;

import binnie.core.AbstractMod;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.CraftGUIUtil;
import binnie.craftgui.core.geometry.IArea;
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
import binnie.craftgui.minecraft.control.ControlSlotCharge;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.extrabees.core.ExtraBeeTexture;
import binnie.genetics.Genetics;
import binnie.genetics.machine.Sequencer;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class WindowSequencer
  extends WindowMachine
{
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowSequencer(player, inventory, side);
  }
  
  public WindowSequencer(EntityPlayer player, IInventory inventory, Side side)
  {
    super(226, 224, player, inventory, side);
  }
  
  static Texture ProgressBase = new StandardTexture(64, 114, 98, 9, ExtraBeeTexture.GUIProgress.getTexture());
  static Texture Progress = new StandardTexture(64, 123, 98, 9, ExtraBeeTexture.GUIProgress.getTexture());
  ControlText slotText;
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound action)
  {
    if ((side == Side.CLIENT) && (name.equals("username"))) {
      this.slotText.setValue("§8Genes will be sequenced by " + action.getString("username"));
    }
    super.recieveGuiNBT(side, player, name, action);
  }
  
  public void initialiseClient()
  {
    setTitle("Sequencer");
    

    int x = 16;
    int y = 32;
    



    CraftGUIUtil.horizontalGrid(x, y, TextJustification.MiddleCenter, 2.0F, new IWidget[] { new ControlSlotArray(this, 0, 0, 2, 2).create(Sequencer.slotReserve), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowRight.getIcon()), new ControlSequencerProgress(this, 0, 0), new ControlIconDisplay(this, 0.0F, 0.0F, GUIIcon.ArrowRight.getIcon()), new ControlSlot(this, 0.0F, 0.0F).assign(6) });
    







    ControlSlot slotTarget = new ControlSlot(this, x + 96, y + 16);
    slotTarget.assign(5);
    
    x = 34;
    y = 92;
    
    this.slotText = new ControlText(this, new IArea(0.0F, y, w(), 12.0F), "§8Userless. Will not save sequences", TextJustification.MiddleCenter);
    


    y += 20;
    
    ControlSlot slotDye = new ControlSlot(this, x, y);
    slotDye.assign(0);
    x += 20;
    new ControlSlotCharge(this, x, y, 0).setColour(16750848);
    
    x += 32;
    new ControlEnergyBar(this, x, y, 60, 16, Position.Left);
    
    x += 92;
    ControlErrorState errorState = new ControlErrorState(this, x, y + 1);
    


    new ControlPlayerInventory(this);
  }
  
  public String getTitle()
  {
    return "Incubator";
  }
  
  protected AbstractMod getMod()
  {
    return Genetics.instance;
  }
  
  protected String getName()
  {
    return "Sequencer";
  }
}
