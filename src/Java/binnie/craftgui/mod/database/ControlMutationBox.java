package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.Window;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IMutation;
import java.util.List;

class ControlMutationBox
  extends ControlListBox<IMutation>
{
  private int index;
  private Type type;
  
  public IWidget createOption(IMutation value, int y)
  {
    return new ControlMutationItem((ControlList)getContent(), value, this.species, y);
  }
  
  static enum Type
  {
    Resultant,  Further;
    
    private Type() {}
  }
  
  public ControlMutationBox(IWidget parent, int x, int y, int width, int height, Type type)
  {
    super(parent, x, y, width, height, 12.0F);
    this.type = type;
  }
  
  private IAlleleSpecies species = null;
  
  public void setSpecies(IAlleleSpecies species)
  {
    if (species != this.species)
    {
      this.species = species;
      this.index = 0;
      movePercentage(-100.0F);
      
      BreedingSystem system = ((WindowAbstractDatabase)getSuperParent()).getBreedingSystem();
      
      List<IMutation> discovered = system.getDiscoveredMutations(Window.get(this).getWorld(), Window.get(this).getUsername());
      if (species != null) {
        if (this.type == Type.Resultant)
        {
          setOptions(system.getResultantMutations(species));
        }
        else
        {
          List<IMutation> mutations = system.getFurtherMutations(species);
          for (int i = 0; i < mutations.size();)
          {
            IMutation mutation = (IMutation)mutations.get(i);
            if ((!discovered.contains(mutations)) && (!((IAlleleSpecies)mutation.getTemplate()[0]).isCounted())) {
              mutations.remove(i);
            } else {
              i++;
            }
          }
          setOptions(mutations);
        }
      }
    }
  }
}
