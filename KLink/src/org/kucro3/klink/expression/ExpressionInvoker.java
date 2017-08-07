package org.kucro3.klink.expression;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.syntax.Sequence;

public interface ExpressionInvoker {
	public Object call(Environment env, Variable var, Sequence seq);
}