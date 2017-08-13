package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Executable;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;
import org.kucro3.klink.syntax.Translator;

public class VarControl implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Var[] vars, Sequence seq, Flow codeBlock) 
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
			Translator translator = new Translator(Sequence.appendTail(seq, ";"), lib);
			Executable executable = translator.pull();
			return new GetFromExpression(leftVar, executable, requireObj);
			
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
			var.define(env.getVars());
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
			dst.get(env.getVars()).ref = src.get(env.getVars()).ref;
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
			dst.get(env.getVars()).ref = requireObj ? env.popReturnSlot() : env.popBooleanSlot();
		}
		
		private final Executable executable;
		
		private final boolean requireObj;
		
		private final Var dst;
	}
}