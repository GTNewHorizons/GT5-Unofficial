package binnie.craftgui.genetics.machine;

import binnie.Binnie;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.resource.BinnieIcon;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
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
import binnie.genetics.api.IGene;
import binnie.genetics.core.GeneticsTexture;
import binnie.genetics.genetics.Engineering;
import forestry.api.genetics.IChromosomeType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ControlGene
  extends Control
  implements IControlValue<IGene>, ITooltip
{
  IGene gene;
  
  public void getTooltip(Tooltip tooltip)
  {
    String cName = Binnie.Genetics.getSystem(this.gene.getSpeciesRoot()).getChromosomeName(this.gene.getChromosome());
    tooltip.add(cName + ": " + this.gene.getName());
    if ((isMouseOver()) && (canFill(Window.get(this).getHeldItemStack())))
    {
      tooltip.add("Left click to assign gene");
      IGene existingGene = Engineering.getGene(Window.get(this).getHeldItemStack(), this.gene.getChromosome().ordinal());
      if (existingGene == null) {
        return;
      }
      String dName = Binnie.Genetics.getSystem(this.gene.getSpeciesRoot()).getChromosomeName(this.gene.getChromosome());
      tooltip.add("Will replace " + dName + ": " + existingGene.getName());
    }
  }
  
  private boolean canFill(ItemStack stack)
  {
    return (stack != null) && (stack.stackSize == 1) && (Engineering.isGeneAcceptor(stack)) && (Engineering.canAcceptGene(stack, getValue()));
  }
  
  protected ControlGene(IWidget parent, float x, float y)
  {
    super(parent, x, y, 16.0F, 16.0F);
    addAttribute(Attribute.MouseOver);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (ControlGene.this.canFill(Window.get(ControlGene.this.getWidget()).getHeldItemStack()))
        {
          NBTTagCompound action = new NBTTagCompound();
          
          NBTTagCompound geneNBT = new NBTTagCompound();
          ControlGene.this.getValue().writeToNBT(geneNBT);
          
          action.setTag("gene", geneNBT);
          
          Window.get(ControlGene.this.getWidget()).sendClientAction("gene-select", action);
        }
      }
    });
  }
  
  public IGene getValue()
  {
    return this.gene;
  }
  
  public void setValue(IGene value)
  {
    this.gene = value;
  }
  
  public void onRenderForeground() {}
  
  public void onRenderBackground()
  {
    if ((isMouseOver()) && (canFill(Window.get(this).getHeldItemStack())))
    {
      CraftGUI.Render.solid(getArea(), -1);
      CraftGUI.Render.solid(getArea().inset(1), -12303292);
    }
    CraftGUI.Render.colour(-1);
    
    CraftGUI.Render.iconItem(IPoint.ZERO, GeneticsTexture.dnaIcon.getIcon());
  }
}
