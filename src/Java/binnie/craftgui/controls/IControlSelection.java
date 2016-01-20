package binnie.craftgui.controls;

public abstract interface IControlSelection<T>
{
  public abstract T getSelectedValue();
  
  public abstract void setSelectedValue(T paramT);
  
  public abstract boolean isSelected(IControlSelectionOption<T> paramIControlSelectionOption);
}
