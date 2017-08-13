package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Util;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public class PrintlnVar implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, final Var[] var, Sequence seq, Flow codeBlock) 
	{
		return (sys, env) -> {
			Util.checkVariable(var, 1);
			env.getSource().getOut().println(var[0].require(env.getVars()).ref);
		};
	}
	
	public static Expression instance()
	{
		return new Expression("printlnvar", new PrintlnVar());
	}
}