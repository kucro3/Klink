package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Ref;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class False implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		return (sys, env) -> env.setBooleanSlot(false);
	}
	
	public static Expression instance()
	{
		return new Expression("false", new False());
	}
}