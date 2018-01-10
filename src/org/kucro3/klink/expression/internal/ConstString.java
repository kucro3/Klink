package org.kucro3.klink.expression.internal;

import org.kucro3.klink.expression.Expression;

public class ConstString extends Const {
	public ConstString()
	{
		super((seq) -> seq.leftToString());
	}
	
	public static Expression instance()
	{
		return new Expression("<=string", new ConstString());
	}
}