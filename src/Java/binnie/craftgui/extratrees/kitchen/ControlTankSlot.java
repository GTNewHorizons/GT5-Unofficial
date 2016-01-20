package binnie.craftgui.extratrees.kitchen;

import binnie.core.machines.power.TankInfo;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.Window;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class ControlTankSlot
  extends ControlSlotFluid
{
  int tankID = 0;
  
  public ControlTankSlot(IWidget parent, int x, int y, int i)
  {
    super(parent, x, y, null);
    this.tankID = i;
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (event.getButton() == 0)
        {
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setByte("id", (byte)ControlTankSlot.this.tankID);
          Window.get(ControlTankSlot.this.getWidget()).sendClientAction("tank-click", nbt);
        }
      }
    });
  }
  
  public void onUpdateClient()
  {
    this.fluidStack = Window.get(this).getContainer().getTankInfo(this.tankID).liquid;
    int height = (int)(16.0F * ((this.fluidStack == null ? 0 : this.fluidStack.amount) / 1000.0F));
    this.itemDisplay.setCroppedZone(this.itemDisplay, new IArea(0.0F, 16 - height, 16.0F, 16.0F));
    super.onUpdateClient();
  }
  
  public void onRenderBackground()
  {
    super.onRenderBackground();
  }
}
