package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class PrintVar implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, final Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		return (sys, env) -> {
			Util.checkVariable(refs, 1);
			env.getSource().getOut().print(refs[0].get(env));
		};
	}
	
	public static Expression instance()
	{
		return new Expression("printvar", new PrintVar());
	}
}