package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.Util;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;
import org.kucro3.util.Reference;

public class VarControl implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context) 
	{
		String leftName = seq.next();
		Var leftVar = new Var(leftName);
		
		boolean leftSrc = false, requireObj = false;
		
		if(!seq.hasNext())
			return new DefVar(leftVar);
		
		String control = seq.next();
		
		switch(control)
		{
		case "->":
			leftSrc = true;
		case "<-":
			Var rightVar = new Var(seq.next());
			return leftSrc ? new PutVar(leftVar, rightVar) : new PutVar(rightVar, leftVar);
			
		case "<=":
			requireObj = true;
		case "<?":
			Reference<Executable> executable = new Reference<>();
			context.getTranslator().temporary(Sequence.appendTail(seq, ";"), (trans) -> {
				executable.set(trans.pull());
			});
			return new GetFromExpression(leftVar, executable.get(), requireObj);
			
		case "<=boolean":
			return (sys, env) -> {leftVar.set(env, Util.parseBoolean(seq.next()));};
			
		case "<=long":
			return (sys, env) -> {leftVar.set(env, Util.parseLong(seq.next()));};
			
		case "<=int":
			return (sys, env) -> {leftVar.set(env, Util.parseInt(seq.next()));};
			
		case "<=short":
			return (sys, env) -> {leftVar.set(env, Util.parseShort(seq.next()));};
			
		case "<=byte":
			return (sys, env) -> {leftVar.set(env, Util.parseByte(seq.next()));};
			
		case "<=double":
			return (sys, env) -> {leftVar.set(env, Util.parseDouble(seq.next()));};
			
		case "<=float":
			return (sys, env) -> {leftVar.set(env, Util.parseFloat(seq.next()));};
			
		case "<=string":
			return (sys, env) -> {leftVar.set(env, seq.leftToString());};
			
		default:
			throw UnknownControlSymbol(control);
		}
	}
	
	public static Expression instance()
	{
		return new Expression("$", new VarControl());
	}
	
	public static ScriptException UnknownControlSymbol(String symbol)
	{
		return new ScriptException("Invaild control symbol: " + symbol);
	}
	
	public static class DefVar implements ExpressionInstance
	{
		public DefVar(Var var)
		{
			this.var = var;
		}
		
		@Override
		public void call(Klink sys, Environment env) 
		{
			var.define(env);
		}
		
		private final Var var;
	}
	
	public static class PutVar implements ExpressionInstance
	{
		public PutVar(Var src, Var dst)
		{
			this.src = src;
			this.dst = dst;
		}
		
		@Override
		public void call(Klink sys, Environment env) 
		{
			dst.force(env, src.get(env));
		}
		
		private final Var src, dst;
	}
	
	public static class GetFromExpression implements ExpressionInstance
	{
		public GetFromExpression(Var dst, Executable executable, boolean requireObj)
		{
			this.dst = dst;
			this.executable = executable;
			this.requireObj = requireObj;
		}
		
		@Override
		public void call(Klink sys, Environment env) 
		{
			executable.execute(sys);
			dst.force(env, requireObj ? env.popReturnSlot() : env.popBooleanSlot());
		}
		
		private final Executable executable;
		
		private final boolean requireObj;
		
		private final Var dst;
	}
}