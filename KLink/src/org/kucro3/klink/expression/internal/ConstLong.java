package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstLong extends Const {
	public ConstLong()
	{
		super((seq) -> Util.parseLong(seq.next()));
	}

	public static Expression instance()
	{
		return new Expression("<=long", new ConstLong());
	}
}
