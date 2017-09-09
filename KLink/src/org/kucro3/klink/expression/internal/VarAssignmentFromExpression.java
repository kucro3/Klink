package org.kucro3.klink.expression.internal;

import java.util.Optional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.flow.Operation;
import org.kucro3.klink.syntax.Sequence;
import org.kucro3.util.Reference;

public class VarAssignmentFromExpression implements ExpressionCompiler {
	public VarAssignmentFromExpression(boolean obj)
	{
		this.flag = obj;
	}
	
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context)
	{
		final Reference<Optional<Operation>> exec = new Reference<>();
		context.getTranslator().temporary(Sequence.appendTail(seq, ";"), (trans) -> {
			exec.set(trans.pullOperation());
		});
		return (sys, env) -> {
			exec.get().ifPresent((e) -> e.execute(sys));
			java.lang.Object obj = flag ? env.popReturnSlot() : env.popBooleanSlot();
			for(Ref ref : refs)
				ref.set(env, obj);
		};
	}
	
	private final boolean flag;
	
	public static final class Object
	{
		public static Expression instance()
		{
			return new Expression("<=", new VarAssignmentFromExpression(true));
		}
	}
	
	public static final class Boolean
	{
		public static Expression instance()
		{
			return new Expression("<?", new VarAssignmentFromExpression(false));
		}
	}
}