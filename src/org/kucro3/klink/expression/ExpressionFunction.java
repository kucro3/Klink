package org.kucro3.klink.expression;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExpressionFunctionRepeatable.class)
public @interface ExpressionFunction {
	public String name();
}