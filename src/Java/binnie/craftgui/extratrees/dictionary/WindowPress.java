package binnie.craftgui.extratrees.dictionary;

import binnie.core.AbstractMod;
import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.MachinePackage;
import binnie.core.machines.power.ErrorState.InsufficientPower;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.InventoryType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlEnergyBar;
import binnie.craftgui.minecraft.control.ControlErrorState;
import binnie.craftgui.minecraft.control.ControlLiquidTank;
import binnie.craftgui.minecraft.control.ControlPlayerInventory;
import binnie.craftgui.minecraft.control.ControlSlot;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.Press;
import binnie.extratrees.machines.Press.ComponentFruitPressLogic;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class WindowPress
  extends Window
{
  public WindowPress(EntityPlayer player, IInventory inventory, Side side)
  {
    super(194.0F, 192.0F, player, inventory, side);
  }
  
  protected AbstractMod getMod()
  {
    return ExtraTrees.instance;
  }
  
  protected String getName()
  {
    return "Press";
  }
  
  public void initialiseClient()
  {
    setTitle(Machine.getMachine(getInventory()).getPackage().getDisplayName());
    new ControlSlot(this, 24.0F, 52.0F).assign(Press.slotFruit);
    new ControlLiquidTank(this, 99, 32).setTankID(Press.tankWater);
    new ControlEnergyBar(this, 154, 32, 16, 60, Position.Bottom);
    new ControlPlayerInventory(this);
    new ControlErrorState(this, 128.0F, 54.0F);
    new ControlFruitPressProgress(this, 62.0F, 24.0F);
    
    ((Window)getSuperParent()).getContainer().getOrCreateSlot(InventoryType.Machine, Press.slotCurrent);
  }
  
  public static Window create(EntityPlayer player, IInventory inventory, Side side)
  {
    return new WindowPress(player, inventory, side);
  }
  
  public void recieveGuiNBT(Side side, EntityPlayer player, String name, NBTTagCompound action)
  {
    Press.ComponentFruitPressLogic logic = (Press.ComponentFruitPressLogic)Machine.getInterface(Press.ComponentFruitPressLogic.class, getInventory());
    super.recieveGuiNBT(side, player, name, action);
    if ((side == Side.SERVER) && (name.equals("fruitpress-click"))) {
      if ((logic.canWork() == null) && ((logic.canProgress() == null) || ((logic.canProgress() instanceof ErrorState.InsufficientPower))))
      {
        logic.alterProgress(2.0F);
      }
      else if ((side == Side.SERVER) && (name.equals("clear-fruit")))
      {
        logic.setProgress(0.0F);
        getInventory().setInventorySlotContents(Press.slotCurrent, null);
      }
    }
  }
}
