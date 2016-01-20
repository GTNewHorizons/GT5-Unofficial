package binnie.craftgui.controls.listbox;

import binnie.core.util.IValidator;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.events.EventValueChanged;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ControlList<T>
  extends Control
  implements IControlValue<T>
{
  ControlListBox<T> parent;
  
  protected ControlList(ControlListBox<T> parent, float x, float y, float w, float h)
  {
    super(parent, x, y, w, h);
    this.parent = parent;
  }
  
  T value = null;
  Map<T, IWidget> allOptions = new LinkedHashMap();
  Map<T, IWidget> optionWidgets = new LinkedHashMap();
  
  public T getValue()
  {
    return this.value;
  }
  
  public void setValue(T value)
  {
    if (value == this.value) {
      return;
    }
    this.value = value;
    if ((value != null) && (this.optionWidgets.containsKey(value)))
    {
      IWidget child = (IWidget)this.optionWidgets.get(value);
      this.parent.ensureVisible(child.y(), child.y() + child.h(), h());
    }
    getParent().callEvent(new EventValueChanged(getParent(), value));
  }
  
  boolean creating = false;
  IValidator<IWidget> validator;
  
  public void setOptions(Collection<T> options)
  {
    deleteAllChildren();
    this.allOptions.clear();
    int i = 0;
    for (T option : options)
    {
      IWidget optionWidget = ((ControlListBox)getParent()).createOption(option, 0);
      if (optionWidget != null) {
        this.allOptions.put(option, optionWidget);
      }
      i++;
    }
    filterOptions();
  }
  
  public void filterOptions()
  {
    int height = 0;
    this.optionWidgets.clear();
    for (Map.Entry<T, IWidget> entry : this.allOptions.entrySet()) {
      if (isValidOption((IWidget)entry.getValue()))
      {
        ((IWidget)entry.getValue()).show();
        this.optionWidgets.put(entry.getKey(), entry.getValue());
        ((IWidget)entry.getValue()).setPosition(new IPoint(0.0F, height));
        height = (int)(height + ((IWidget)entry.getValue()).getSize().y());
      }
      else
      {
        ((IWidget)entry.getValue()).hide();
      }
    }
    this.creating = true;
    setValue(getValue());
    setSize(new IPoint(getSize().x(), height));
  }
  
  public Collection<T> getOptions()
  {
    return this.optionWidgets.keySet();
  }
  
  public Collection<T> getAllOptions()
  {
    return this.allOptions.keySet();
  }
  
  public int getIndexOf(T value)
  {
    int index = 0;
    for (T option : getOptions())
    {
      if (option.equals(value)) {
        return index;
      }
      index++;
    }
    return -1;
  }
  
  public int getCurrentIndex()
  {
    return getIndexOf(getValue());
  }
  
  public void setIndex(int currentIndex)
  {
    int index = 0;
    for (T option : getOptions())
    {
      if (index == currentIndex)
      {
        setValue(option);
        return;
      }
      index++;
    }
    setValue(null);
  }
  
  private boolean isValidOption(IWidget widget)
  {
    return this.validator == null ? true : this.validator.isValid(widget);
  }
  
  public void setValidator(IValidator<IWidget> validator)
  {
    if (this.validator != validator)
    {
      this.validator = validator;
      filterOptions();
    }
  }
}
