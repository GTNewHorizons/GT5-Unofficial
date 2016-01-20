package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Retention(RetentionPolicy.RUNTIME)
@ConfigProperty.Type(propertyClass=PropertyDouble.class)
public @interface PropDouble
{
  public static class PropertyDouble
    extends PropertyBase<Double, PropDouble>
  {
    public PropertyDouble(Field field, BinnieConfiguration file, ConfigProperty configProperty, PropDouble annotedProperty)
      throws IllegalArgumentException, IllegalAccessException
    {
      super(file, configProperty, annotedProperty);
    }
    
    protected Property getProperty()
    {
      return this.file.get(getCategory(), getKey(), ((Double)this.defaultValue).doubleValue());
    }
    
    protected Double getConfigValue()
    {
      return Double.valueOf(this.property.getDouble(((Double)this.defaultValue).doubleValue()));
    }
    
    protected void addComments()
    {
      addComment("Default value is " + this.defaultValue + ".");
    }
  }
}
