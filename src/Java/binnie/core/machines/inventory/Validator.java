package binnie.core.machines.inventory;

import binnie.core.util.IValidator;

public abstract class Validator<T>
  implements IValidator<T>
{
  public abstract String getTooltip();
}
