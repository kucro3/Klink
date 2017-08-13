package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public class True implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Var[] var, Sequence seq, Flow codeBlock) 
	{
		return (sys, env) -> env.setBooleanSlot(true);
	}
	
	public static Expression instance()
	{
		return new Expression("true", new True());
	}
}