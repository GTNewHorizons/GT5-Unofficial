package binnie.craftgui.minecraft.control;

import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.power.IProcess;
import binnie.core.machines.power.ProcessInfo;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.Window;

public class ControlProgressBase
  extends Control
{
  protected float progress;
  
  public ControlProgressBase(IWidget parent, float x, float y, float w, float h)
  {
    super(parent, x, y, w, h);
    this.progress = 0.0F;
    addAttribute(Attribute.MouseOver);
  }
  
  public void setProgress(float progress)
  {
    this.progress = progress;
    if (this.progress < 0.0F) {
      this.progress = 0.0F;
    } else if (this.progress > 1.0F) {
      this.progress = 1.0F;
    }
  }
  
  protected ProcessInfo getProcess()
  {
    return Window.get(this).getContainer().getProcessInfo();
  }
  
  public void onUpdateClient()
  {
    ProcessInfo process = getProcess();
    if (process != null) {
      setProgress(process.getCurrentProgress() / 100.0F);
    }
  }
  
  public void getHelpTooltip(Tooltip tooltip)
  {
    ProcessInfo process = getProcess();
    
    IProcess machineProcess = (IProcess)Machine.getMachine(Window.get(this).getInventory()).getInterface(IProcess.class);
    if (process != null)
    {
      tooltip.add("Progress");
      if (this.progress == 0.0F) {
        tooltip.add("Not in Progress");
      } else if (process.getProcessTime() > 0) {
        tooltip.add(machineProcess.getTooltip() + " (" + (int)process.getCurrentProgress() + "%)");
      } else {
        tooltip.add("In Progress");
      }
      if (process.getProcessTime() > 0)
      {
        tooltip.add("Time Left: " + convertTime((int)((1.0F - this.progress) * process.getProcessTime())));
        
        tooltip.add("Total Time: " + convertTime(process.getProcessTime()));
        tooltip.add("Energy Cost: " + process.getProcessEnergy() * 10 + " RF");
      }
      else
      {
        tooltip.add("Energy Cost: " + process.getEnergyPerTick() * 10.0F + " RF / tick");
      }
    }
  }
  
  public static String convertTime(int time)
  {
    int seconds = (int)(time / 20.0F);
    int minutes = 0;
    while (seconds >= 60)
    {
      minutes++;
      seconds -= 60;
    }
    String ts = "";
    if (minutes > 0) {
      ts = ts + minutes + " minute" + (minutes == 1 ? "" : "s");
    }
    if (seconds > 0)
    {
      if (ts.length() > 0) {
        ts = ts + " ";
      }
      ts = ts + seconds + " second" + (seconds == 1 ? "" : "s");
    }
    return ts;
  }
}
