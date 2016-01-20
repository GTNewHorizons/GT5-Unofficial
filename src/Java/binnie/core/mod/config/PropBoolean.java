package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Retention(RetentionPolicy.RUNTIME)
@ConfigProperty.Type(propertyClass=PropertyBoolean.class)
public @interface PropBoolean
{
  public static class PropertyBoolean
    extends PropertyBase<Boolean, PropBoolean>
  {
    public PropertyBoolean(Field field, BinnieConfiguration file, ConfigProperty configProperty, PropBoolean annotedProperty)
      throws IllegalArgumentException, IllegalAccessException
    {
      super(file, configProperty, annotedProperty);
    }
    
    protected Property getProperty()
    {
      return this.file.get(getCategory(), getKey(), ((Boolean)this.defaultValue).booleanValue());
    }
    
    protected Boolean getConfigValue()
    {
      return Boolean.valueOf(this.property.getBoolean(((Boolean)this.defaultValue).booleanValue()));
    }
    
    protected void addComments()
    {
      addComment("Default value is " + this.defaultValue + ".");
    }
  }
}
