package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public abstract class Const implements ExpressionCompiler {
	protected Const(Parser parser)
	{
		this.parser = parser;
	}
	
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		Util.checkVariable(refs, (a) -> a.length > 0);
		final Object v = this.parser.parse(seq);
		return (sys, env) -> {
			for(Ref ref : refs)
				ref.set(env, v);
		};
	}
	
	private final Parser parser;
	
	public static interface Parser
	{
		Object parse(Sequence seq);
	}
}