package org.kucro3.klink.expression;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExpressionFunctionRepeatable {
	ExpressionFunction[] value();
}