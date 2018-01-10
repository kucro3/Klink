package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstInt extends Const {
	public ConstInt()
	{
		super((seq) -> Util.parseInt(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=int", new ConstInt());
	}
}