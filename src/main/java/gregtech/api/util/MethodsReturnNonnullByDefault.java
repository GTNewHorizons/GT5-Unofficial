package gregtech.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * This annotation can be applied to a package or class to indicate that
 * the methods in that element are nonnull by default unless there is:
 * <ul>
 * <li>An explicit nullness annotation
 * <li>The method overrides a method in a superclass (in which case the
 * annotation of the corresponding method in the superclass applies)
 * </ul>
 */
@Nonnull
@TypeQualifierDefault({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodsReturnNonnullByDefault {}
