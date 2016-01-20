package binnie.craftgui.extratrees.dictionary;

import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.mod.database.ControlDatabaseIndividualDisplay;
import binnie.craftgui.mod.database.DatabaseTab;
import binnie.craftgui.mod.database.EnumDiscoveryState;
import binnie.craftgui.mod.database.PageSpecies;
import binnie.craftgui.window.Panel;
import forestry.api.genetics.IAlleleSpecies;

public class PageSpeciesImage
  extends PageSpecies
{
  ControlDatabaseIndividualDisplay display;
  
  public PageSpeciesImage(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
    new Panel(this, 7.0F, 25.0F, 130.0F, 120.0F, MinecraftGUI.PanelType.Gray);
    this.display = new ControlDatabaseIndividualDisplay(this, 12.0F, 25.0F, 120.0F);
    this.display.hastooltip = false;
    new ControlTextCentered(this, 8.0F, ((DatabaseTab)getValue()).toString());
  }
  
  public void onValueChanged(IAlleleSpecies species)
  {
    this.display.setSpecies(species, EnumDiscoveryState.Show);
  }
}
