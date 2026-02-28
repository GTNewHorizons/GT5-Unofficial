package gregtech.codegen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides javadoc-style quick notes about a material to aid in viewing material properties in the IDE.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Documented
@SuppressWarnings("unused") // Used by codegen
public @interface MaterialInfo {
    /**
     * @return Short information about the material to display in the IDE when autocompleting a material accessor method.
     */
    String info();
}
