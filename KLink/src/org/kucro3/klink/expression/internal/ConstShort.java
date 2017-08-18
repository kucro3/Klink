package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstShort extends Const {
	public ConstShort()
	{
		super((seq) -> Util.parseShort(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=short", new ConstShort());
	}
}