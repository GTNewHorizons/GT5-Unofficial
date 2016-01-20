package binnie.craftgui.genetics.machine;

import binnie.Binnie;
import binnie.core.genetics.BreedingSystem;
import binnie.core.genetics.Gene;
import binnie.core.genetics.ManagerGenetics;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.minecraft.Window;
import binnie.genetics.genetics.GeneTracker;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosomeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ControlGeneScroll
  extends Control
  implements IControlValue<BreedingSystem>
{
  protected ControlGeneScroll(IWidget parent, float x, float y, float w, float h)
  {
    super(parent, x, y, w, h);
  }
  
  private String filter = "";
  private BreedingSystem system = null;
  
  public void setFilter(String filter)
  {
    this.filter = filter.toLowerCase();
    refresh();
  }
  
  public void setGenes(BreedingSystem system)
  {
    this.system = system;
    refresh();
  }
  
  public void refresh()
  {
    deleteAllChildren();
    
    GeneTracker tracker = GeneTracker.getTracker(Window.get(this).getWorld(), Window.get(this).getUsername());
    
    Map<IChromosomeType, List<IAllele>> genes = Binnie.Genetics.getChromosomeMap(this.system.getSpeciesRoot());
    
    int x = 0;
    int y = 0;
    
    boolean isNEI = ((WindowGeneBank)Window.get(this)).isNei;
    for (Map.Entry<IChromosomeType, List<IAllele>> entry : genes.entrySet())
    {
      List<IAllele> discovered = new ArrayList();
      for (IAllele allele : (List)entry.getValue())
      {
        Gene gene = new Gene(allele, (IChromosomeType)entry.getKey(), this.system.getSpeciesRoot());
        if (((isNEI) || (tracker.isSequenced(new Gene(allele, (IChromosomeType)entry.getKey(), this.system.getSpeciesRoot())))) && (gene.getName().toLowerCase().contains(this.filter))) {
          discovered.add(allele);
        }
      }
      if (discovered.size() != 0)
      {
        x = 0;
        new ControlText(this, new IPoint(x, y), this.system.getChromosomeName((IChromosomeType)entry.getKey()));
        y += 12;
        for (IAllele allele : discovered)
        {
          if (x + 18 > getSize().x())
          {
            y += 20;
            x = 0;
          }
          new ControlGene(this, x, y).setValue(new Gene(allele, (IChromosomeType)entry.getKey(), this.system.getSpeciesRoot()));
          x += 18;
        }
        y += 24;
      }
    }
    setSize(new IPoint(getSize().x(), y));
  }
  
  public void setValue(BreedingSystem system)
  {
    setGenes(system);
  }
  
  public BreedingSystem getValue()
  {
    return this.system;
  }
}
