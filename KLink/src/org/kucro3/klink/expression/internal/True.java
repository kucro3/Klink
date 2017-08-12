package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.Expression.ReturnType;
import org.kucro3.klink.expression.ExpressionInvoker;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public class True implements ExpressionInvoker {
	@Override
	public Object call(Klink sys, Environment env, Variable[] var, Sequence seq, Flow codeBlock) 
	{
		return true;
	}
	
	public static Expression instance()
	{
		return new Expression("true", new True(), ReturnType.BOOLEAN);
	}
}