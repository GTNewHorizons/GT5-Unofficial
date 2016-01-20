package binnie.craftgui.mod.database;

import binnie.craftgui.core.IWidget;
import forestry.api.genetics.IAlleleSpecies;

public abstract class PageSpecies
  extends PageAbstract<IAlleleSpecies>
{
  public PageSpecies(IWidget parent, DatabaseTab tab)
  {
    super(parent, tab);
  }
}
