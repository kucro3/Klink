package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class VarAssignment implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context)
	{
		Util.checkVariable(refs, (unused) -> refs.length > 0);
		Ref src = Util.toRef(seq.next());
		return (sys, env) -> {
			Object srcobj = src.get(env);
			for(Ref ref : refs)
				ref.set(env, srcobj);
		};
	}
	
	public static Expression instance()
	{
		return new Expression("<-", new VarAssignment());
	}
}