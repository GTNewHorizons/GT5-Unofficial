package binnie.craftgui.mod.database;

import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlListBox;
import binnie.craftgui.core.IWidget;
import forestry.api.genetics.IClassification;

class ControlBranchBox
  extends ControlListBox<IClassification>
{
  public IWidget createOption(IClassification value, int y)
  {
    return new ControlBranchBoxOption((ControlList)getContent(), value, y);
  }
  
  public ControlBranchBox(IWidget parent, float x, float y, float width, float height)
  {
    super(parent, x, y, width, height, 12.0F);
  }
}
