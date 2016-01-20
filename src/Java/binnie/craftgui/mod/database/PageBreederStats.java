package binnie.craftgui.mod.database;

import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.IWidget;

public class PageBreederStats
  extends Control
{
  String player;
  
  public PageBreederStats(IWidget parent, int w, int h, String player)
  {
    super(parent, 0.0F, 0.0F, w, h);
    this.player = player;
    
    ControlText pageBranchOverview_branchName = new ControlTextCentered(this, 8.0F, "§nStats§r");
    
    BreedingSystem system = ((WindowAbstractDatabase)getSuperParent()).getBreedingSystem();
  }
}
