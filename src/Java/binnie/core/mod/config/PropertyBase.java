package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

abstract class PropertyBase<ValueType, AnnotationType extends Annotation>
{
  Configuration file;
  Property property;
  ValueType defaultValue;
  private ConfigProperty configProperty;
  AnnotationType annotatedProperty;
  private List<String> comments = new ArrayList();
  private Field field;
  
  protected PropertyBase(Field field, BinnieConfiguration file, ConfigProperty configProperty, AnnotationType annotedProperty)
    throws IllegalArgumentException, IllegalAccessException
  {
    this.field = field;
    this.file = file;
    this.configProperty = configProperty;
    this.annotatedProperty = annotedProperty;
    this.defaultValue = getDefaultValue(field);
    this.property = getProperty();
    for (String comment : configProperty.comment()) {
      addComment(comment);
    }
    addComments();
    this.property.comment = getComment();
    field.set(null, getConfigValue());
  }
  
  protected abstract Property getProperty();
  
  protected abstract ValueType getConfigValue();
  
  protected abstract void addComments();
  
  protected String getCategory()
  {
    return this.configProperty.category().equals("") ? ((ConfigProperty.Type)this.annotatedProperty.annotationType().getAnnotation(ConfigProperty.Type.class)).category() : this.configProperty.category();
  }
  
  protected String getKey()
  {
    return this.configProperty.key();
  }
  
  protected ValueType getDefaultValue(Field field)
    throws IllegalArgumentException, IllegalAccessException
  {
    return field.get(null);
  }
  
  protected void addComment(String comment)
  {
    this.comments.add(comment);
  }
  
  protected String getComment()
  {
    String comment = "";
    for (String com : this.comments) {
      comment = comment + com + " ";
    }
    return comment;
  }
}
