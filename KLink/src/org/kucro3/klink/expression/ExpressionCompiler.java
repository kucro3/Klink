package org.kucro3.klink.expression;

import org.kucro3.klink.Ref;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public interface ExpressionCompiler {
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock);
}