package org.kucro3.klink.expression;

import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public interface ExpressionCompiler {
	public ExpressionInstance compile(ExpressionLibrary lib, Var[] var, Sequence seq, Flow codeBlock);
}