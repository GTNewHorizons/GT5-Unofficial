package com.gtnewhorizons.gtnhintergalactic.client.lore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to identify fields which should be updated on a resource refresh. Annotated fields must be static and
 * of type {@link String}. To use this, register the declaring class via {@link LoreHandler#registerLoreHolder(Class)}.
 * 
 * @author glowredman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoreHolder {

    /**
     * The localization key
     * 
     * @author glowredman
     */
    String value();

}
