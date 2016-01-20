package binnie.core.mod.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface ConfigProperty
{
  String key();
  
  String category() default "";
  
  String[] comment() default {};
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
  public static @interface Type
  {
    Class<? extends PropertyBase> propertyClass();
    
    String category() default "general";
  }
}
