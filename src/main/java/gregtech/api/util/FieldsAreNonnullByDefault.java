package gregtech.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * This annotation can be applied to a package or class to indicate that
 * the fields in that element are nonnull by default unless there is an explicit nullness annotation.
 * </ul>
 */
@Nonnull
@TypeQualifierDefault({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsAreNonnullByDefault {}
