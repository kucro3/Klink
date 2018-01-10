package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstFloat extends Const {
	public ConstFloat()
	{
		super((seq) -> Util.parseFloat(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=float", new ConstFloat());
	}
}
