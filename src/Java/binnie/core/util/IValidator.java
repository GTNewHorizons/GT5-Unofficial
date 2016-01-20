package binnie.core.util;

public abstract interface IValidator<T>
{
  public abstract boolean isValid(T paramT);
}
