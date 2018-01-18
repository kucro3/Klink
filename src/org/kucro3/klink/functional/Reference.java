package org.kucro3.klink.functional;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
    public Class<?> value();
}
