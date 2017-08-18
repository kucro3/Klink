package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Operation;
import org.kucro3.klink.syntax.Sequence;
import org.kucro3.klink.syntax.Translator;

public class VarCall implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		String vars = seq.next();
		Ref[] v;
		
		if(vars.startsWith("(") && vars.endsWith(")"))
		{
			String[] svar = vars.substring(1, vars.length() - 1).split(",");
			v = new Ref[svar.length];
			for(int i = 0; i < svar.length; i++)
				v[i] = Util.toRef(svar[i]);
		}
		else
			(v = new Ref[1])[0] = Util.toRef(vars);
		
		seq.decRow();
		Translator translator = new Translator(Sequence.appendTail(seq, ";"), lib);
		Operation operation = translator.pullOperation(v);
		
		return new Compiled(operation);
	}
	
	public static Expression instance()
	{
		return new Expression("#", new VarCall());
	}
	
	public static class Compiled implements ExpressionInstance
	{
		public Compiled(Operation operation)
		{
			this.operation = operation;
		}
		
		@Override
		public void call(Klink sys, Environment env) 
		{
			if(operation != null)
				this.operation.execute(sys);
		}
		
		private final Operation operation;
	}
}