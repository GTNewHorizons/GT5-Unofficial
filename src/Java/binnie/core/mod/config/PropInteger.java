package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Retention(RetentionPolicy.RUNTIME)
@ConfigProperty.Type(propertyClass=PropertyInteger.class)
public @interface PropInteger
{
  public static class PropertyInteger
    extends PropertyBase<Integer, PropInteger>
  {
    public PropertyInteger(Field field, BinnieConfiguration file, ConfigProperty configProperty, PropInteger annotedProperty)
      throws IllegalArgumentException, IllegalAccessException
    {
      super(file, configProperty, annotedProperty);
    }
    
    protected Property getProperty()
    {
      return this.file.get(getCategory(), getKey(), ((Integer)this.defaultValue).intValue());
    }
    
    protected Integer getConfigValue()
    {
      return Integer.valueOf(this.property.getInt(((Integer)this.defaultValue).intValue()));
    }
    
    protected void addComments()
    {
      addComment("Default value is " + this.defaultValue + ".");
    }
  }
}
