package binnie.craftgui.mod.database;

import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.events.EventValueChanged.Handler;
import cpw.mods.fml.common.Mod.EventHandler;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IClassification;

public class PageBranchSpecies
  extends PageBranch
{
  private ControlText pageBranchSpecies_title;
  private ControlSpeciesBox pageBranchSpecies_speciesList;
  
  @Mod.EventHandler
  public void onHandleEvent(EventValueChanged<IAlleleSpecies> event) {}
  
  public PageBranchSpecies(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
    
    this.pageBranchSpecies_title = new ControlTextCentered(this, 8.0F, "Species");
    
    addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        if (event.isOrigin(PageBranchSpecies.this.pageBranchSpecies_speciesList)) {
          ((WindowAbstractDatabase)PageBranchSpecies.this.getSuperParent()).gotoSpecies((IAlleleSpecies)event.getValue());
        }
      }
    });
    this.pageBranchSpecies_speciesList = new ControlSpeciesBox(this, 4.0F, 20.0F, 136.0F, 152.0F);
  }
  
  public void onValueChanged(IClassification branch)
  {
    this.pageBranchSpecies_speciesList.setBranch(branch);
  }
}
