package binnie.craftgui.mod.database;

import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.renderer.Renderer;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IClassification;
import java.util.ArrayList;
import java.util.List;

public class PageBranchOverview
  extends PageBranch
{
  private ControlText pageBranchOverview_branchName;
  private ControlText pageBranchOverview_branchScientific;
  private ControlText pageBranchOverview_branchAuthority;
  
  public PageBranchOverview(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
    
    this.pageBranchOverview_branchName = new ControlTextCentered(this, 8.0F, "");
    
    this.pageBranchOverview_branchScientific = new ControlTextCentered(this, 32.0F, "");
    this.pageBranchOverview_branchAuthority = new ControlTextCentered(this, 44.0F, "");
  }
  
  private List<ControlText> pageBranchOverview_branchDescription = new ArrayList();
  
  public void onValueChanged(IClassification branch)
  {
    this.pageBranchOverview_branchName.setValue("§n" + branch.getName() + " Branch§r");
    
    this.pageBranchOverview_branchScientific.setValue("§oApidae " + branch.getScientific() + "§r");
    
    this.pageBranchOverview_branchAuthority.setValue("Discovered by §l" + branch.getMemberSpecies()[0].getAuthority() + "§r");
    for (IWidget widget : this.pageBranchOverview_branchDescription) {
      deleteChild(widget);
    }
    this.pageBranchOverview_branchDescription.clear();
    
    String desc = branch.getDescription();
    if ((desc == null) || (desc == "")) {
      desc = "No Description Provided.";
    }
    String line = "";
    
    List<String> descLines = new ArrayList();
    for (String str : desc.split(" "))
    {
      if (CraftGUI.Render.textWidth(line + " " + str) > 134)
      {
        descLines.add("§o" + line + "§r");
        line = "";
      }
      line = line + " " + str;
    }
    descLines.add(line);
    
    int i = 0;
    for (String dLine : descLines) {
      this.pageBranchOverview_branchDescription.add(new ControlTextCentered(this, 84 + 12 * i++, dLine));
    }
  }
}
