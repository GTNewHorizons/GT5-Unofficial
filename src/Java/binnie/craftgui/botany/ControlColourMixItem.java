package binnie.craftgui.botany;

import binnie.botany.api.IColourMix;
import binnie.core.genetics.BreedingSystem;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlOption;
import binnie.craftgui.mod.database.WindowAbstractDatabase;

public class ControlColourMixItem
  extends ControlOption<IColourMix>
{
  ControlColourDisplay itemWidget1;
  ControlColourDisplay itemWidget2;
  ControlColourDisplay itemWidget3;
  ControlColourMixSymbol addSymbol;
  ControlColourMixSymbol arrowSymbol;
  
  public ControlColourMixItem(ControlList<IColourMix> controlList, IColourMix option, int y)
  {
    super(controlList, option, y);
    this.itemWidget1 = new ControlColourDisplay(this, 4.0F, 4.0F);
    this.itemWidget2 = new ControlColourDisplay(this, 44.0F, 4.0F);
    this.itemWidget3 = new ControlColourDisplay(this, 104.0F, 4.0F);
    this.addSymbol = new ControlColourMixSymbol(this, 24, 4, 0);
    this.arrowSymbol = new ControlColourMixSymbol(this, 64, 4, 1);
    
    BreedingSystem system = ((WindowAbstractDatabase)getSuperParent()).getBreedingSystem();
    if (getValue() != null)
    {
      this.itemWidget1.setValue(((IColourMix)getValue()).getColour1());
      this.itemWidget2.setValue(((IColourMix)getValue()).getColour2());
      this.itemWidget3.setValue(((IColourMix)getValue()).getResult());
      this.addSymbol.setValue((IColourMix)getValue());
      this.arrowSymbol.setValue((IColourMix)getValue());
    }
  }
}
