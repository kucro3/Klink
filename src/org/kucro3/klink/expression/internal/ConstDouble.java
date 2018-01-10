package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstDouble extends Const {
	public ConstDouble()
	{
		super((seq) -> Util.parseDouble(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=double", new ConstDouble());
	}
}