package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Retention(RetentionPolicy.RUNTIME)
@ConfigProperty.Type(propertyClass=PropertyPercentage.class)
public @interface PropPercentage
{
  int upper() default 100;
  
  int lower() default 0;
  
  public static class PropertyPercentage
    extends PropertyBase<Integer, PropPercentage>
  {
    public PropertyPercentage(Field field, BinnieConfiguration file, ConfigProperty configProperty, PropPercentage annotedProperty)
      throws IllegalArgumentException, IllegalAccessException
    {
      super(file, configProperty, annotedProperty);
    }
    
    protected Integer getConfigValue()
    {
      return Integer.valueOf(this.property.getInt(((Integer)this.defaultValue).intValue()));
    }
    
    protected void addComments()
    {
      addComment("Default value is " + this.defaultValue + "%.");
      addComment("Range is " + ((PropPercentage)this.annotatedProperty).lower() + "-" + ((PropPercentage)this.annotatedProperty).upper() + "%.");
    }
    
    protected Property getProperty()
    {
      return this.file.get(getCategory(), getKey(), ((Integer)this.defaultValue).intValue());
    }
  }
}
