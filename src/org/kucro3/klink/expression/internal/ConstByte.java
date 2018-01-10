package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;

public class ConstByte extends Const {
	public ConstByte()
	{
		super((seq) -> Util.parseByte(seq.next()));
	}
	
	public static Expression instance()
	{
		return new Expression("<=byte", new ConstByte());
	}
}