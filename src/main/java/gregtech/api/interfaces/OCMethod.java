package gregtech.api.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// A marker annotation that indicates that a method is used by OpenComputers. This doesn't do anything, it's just an
/// indicator for the intended usage of seemingly unused methods.
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface OCMethod {

}
