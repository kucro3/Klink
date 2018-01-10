package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Ref;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class LoadExpressionPack implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		final String file = seq.next();
		return (sys, env) -> sys.getPackLoader().load(sys, lib, file);
	}
	
	public static Expression instance()
	{
		return new Expression("loadexps", new LoadExpressionPack());
	}
}