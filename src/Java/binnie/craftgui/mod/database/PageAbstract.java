package binnie.craftgui.mod.database;

import binnie.craftgui.controls.page.ControlPage;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;

public abstract class PageAbstract<T>
  extends ControlPage<DatabaseTab>
{
  public PageAbstract(IWidget parent, DatabaseTab tab)
  {
    super(parent, 0.0F, 0.0F, parent.getSize().x(), parent.getSize().y(), tab);
  }
  
  public abstract void onValueChanged(T paramT);
}
