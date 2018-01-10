package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstBoolean extends Const {
	public ConstBoolean()
	{
		super((seq) -> Util.parseBoolean(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=boolean", new ConstBoolean());
	}
}