package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlOption;
import binnie.craftgui.minecraft.Window;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IMutation;

class ControlMutationItem
  extends ControlOption<IMutation>
{
  private ControlDatabaseIndividualDisplay itemWidget1;
  private ControlDatabaseIndividualDisplay itemWidget2;
  private ControlDatabaseIndividualDisplay itemWidget3;
  private ControlMutationSymbol addSymbol;
  private ControlMutationSymbol arrowSymbol;
  
  public ControlMutationItem(ControlList<IMutation> controlList, IMutation option, IAlleleSpecies species, int y)
  {
    super(controlList, option, y);
    this.itemWidget1 = new ControlDatabaseIndividualDisplay(this, 4.0F, 4.0F);
    this.itemWidget2 = new ControlDatabaseIndividualDisplay(this, 44.0F, 4.0F);
    this.itemWidget3 = new ControlDatabaseIndividualDisplay(this, 104.0F, 4.0F);
    this.addSymbol = new ControlMutationSymbol(this, 24, 4, 0);
    this.arrowSymbol = new ControlMutationSymbol(this, 64, 4, 1);
    
    boolean isNEI = ((WindowAbstractDatabase)getSuperParent()).isNEI();
    BreedingSystem system = ((WindowAbstractDatabase)getSuperParent()).getBreedingSystem();
    if (getValue() != null)
    {
      boolean isMutationDiscovered = system.isMutationDiscovered((IMutation)getValue(), Window.get(this).getWorld(), Window.get(this).getUsername());
      

      IAlleleSpecies allele = null;
      EnumDiscoveryState state = null;
      
      allele = (IAlleleSpecies)((IMutation)getValue()).getAllele0();
      state = species == allele ? EnumDiscoveryState.Show : (isNEI) || (isMutationDiscovered) ? EnumDiscoveryState.Show : EnumDiscoveryState.Undetermined;
      this.itemWidget1.setSpecies(allele, state);
      allele = (IAlleleSpecies)((IMutation)getValue()).getAllele1();
      state = species == allele ? EnumDiscoveryState.Show : (isNEI) || (isMutationDiscovered) ? EnumDiscoveryState.Show : EnumDiscoveryState.Undetermined;
      this.itemWidget2.setSpecies(allele, state);
      allele = (IAlleleSpecies)((IMutation)getValue()).getTemplate()[0];
      state = species == allele ? EnumDiscoveryState.Show : (isNEI) || (isMutationDiscovered) ? EnumDiscoveryState.Show : EnumDiscoveryState.Undetermined;
      this.itemWidget3.setSpecies(allele, state);
      this.addSymbol.setValue((IMutation)getValue());
      this.arrowSymbol.setValue((IMutation)getValue());
    }
  }
}
