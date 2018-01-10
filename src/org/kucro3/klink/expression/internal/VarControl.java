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
			final boolean zv = Util.parseBoolean(seq.next());
			return (sys, env) -> {leftVar.force(env, zv);};
			
		case "<=long":
			final long lv = Util.parseLong(seq.next());
			return (sys, env) -> {leftVar.force(env, lv);};
			
		case "<=int":
			final int iv = Util.parseInt(seq.next());
			return (sys, env) -> {leftVar.force(env, iv);};
			
		case "<=short":
			final short sv = Util.parseShort(seq.next());
			return (sys, env) -> {leftVar.force(env, sv);};
			
		case "<=byte":
			final byte bv = Util.parseByte(seq.next());
			return (sys, env) -> {leftVar.force(env, bv);};
			
		case "<=double":
			final double dv = Util.parseDouble(seq.next());
			return (sys, env) -> {leftVar.force(env, dv);};
			
		case "<=float":
			final float fv = Util.parseFloat(seq.next());
			return (sys, env) -> {leftVar.force(env, fv);};
			
		case "<=string":
			final String strv = seq.leftToString();
			return (sys, env) -> {leftVar.force(env, strv);};
			
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